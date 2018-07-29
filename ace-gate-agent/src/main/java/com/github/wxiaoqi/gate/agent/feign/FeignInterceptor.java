package com.github.wxiaoqi.gate.agent.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.gate.agent.exception.AuthenticationServerErrorException;
import com.github.wxiaoqi.gate.agent.exception.AuthenticationVerifyFailException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Feign拦截器
 * 自动申请服务token、传递当前上下文
 * Created by ace on 2017/7/5.
 */
public class FeignInterceptor implements RequestInterceptor {

    private String clientId;
    private String secret;
    private String authHeader;
    private static String authHost;
    private String tokenHead;

    public FeignInterceptor(String clientId, String secret, String header, String authHost, String tokenHead) {
        this.clientId = clientId;
        this.secret = secret;
        this.authHeader = header;
        this.authHost = authHost;
        this.tokenHead = tokenHead;
        getTokenStrategy = new AutoKeepAliveStrategy();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = getTokenStrategy.getAccessToken(clientId, secret);
        requestTemplate.header(authHeader,tokenHead+" "+token);
    }

    /**
     * 设置token
     * @param clientId
     * @param secret
     * @return
     * @throws AuthenticationServerErrorException
     * @throws AuthenticationVerifyFailException
     */
    private static String getToken(String clientId,String secret) throws AuthenticationServerErrorException, AuthenticationVerifyFailException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientId",clientId);
        jsonObject.put("secret",secret);
        HttpResponse response = HttpRequest.post(authHost + "/auth").contentType("application/json").body(jsonObject.toJSONString())
                .send();
        if (response.statusCode() == 200) {
           String token = JSON.parseObject(response.body()).getString("token");
            //容错
            if (StringUtils.isBlank(token)) {
                // 权限服务异常
                throw new AuthenticationServerErrorException(JSON.toJSONString(response));
            }
            return token;
        // HTTP 401:未授权
        } else if (response.statusCode() == 401) {
            // 权限验证失败
            throw new AuthenticationVerifyFailException(JSON.toJSONString(response));
        } else {
            throw new AuthenticationServerErrorException(JSON.toJSONString(response));
        }
    }

    private interface GetTokenStrategy {
        String getAccessToken(String appId, String appSecret) throws AuthenticationVerifyFailException, AuthenticationServerErrorException;
    }


    private static final class AutoKeepAliveStrategy implements GetTokenStrategy {

        private static final class ClientInfo {
            private final String clientId;
            private final String secret;

            ClientInfo(String clientId, String secret) {
                this.clientId = clientId;
                this.secret = secret;
            }
        }

        private static ClientInfo clientInfo;
        private static final AtomicReference<ScheduledThreadPoolExecutor> executor = new AtomicReference<ScheduledThreadPoolExecutor>();
        private static final AtomicReference<String> accessToken = new AtomicReference<String>();

        @Override
        public String getAccessToken(String clientId, String secret) throws AuthenticationVerifyFailException, AuthenticationServerErrorException {
            clientInfo = new ClientInfo(clientId, secret);
            String token = accessToken.get();
            if(token == null){
                token = getToken(clientId, secret);
                executor.compareAndSet(null, scheduledExecutor());
            }
            return token;
        }

        private ScheduledThreadPoolExecutor scheduledExecutor() {
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        accessToken.set(getToken(clientInfo.clientId, clientInfo.secret));
                    } catch (AuthenticationVerifyFailException e) {
                        e.printStackTrace();
                    } catch (AuthenticationServerErrorException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, iacKeepalive, TimeUnit.MINUTES);
            return executor;
        }
    }

    private static GetTokenStrategy getTokenStrategy;
    private static long iacKeepalive = 30L;

}
