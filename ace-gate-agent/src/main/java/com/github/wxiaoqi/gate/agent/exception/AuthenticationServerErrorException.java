package com.github.wxiaoqi.gate.agent.exception;

/**
 * 权限服务异常,自定义异常
 * Created by ace on 2017/7/5.
 */
public class AuthenticationServerErrorException extends RuntimeException {
    public AuthenticationServerErrorException(String message) {
        super(message);
    }
}
