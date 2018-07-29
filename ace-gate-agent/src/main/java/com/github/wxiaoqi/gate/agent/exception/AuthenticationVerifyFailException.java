package com.github.wxiaoqi.gate.agent.exception;

/**
 * 权限验证失败
 * Created by ace on 2017/7/5.
 */
public class AuthenticationVerifyFailException extends RuntimeException {
    public AuthenticationVerifyFailException(String message) {
        super(message);
    }
}
