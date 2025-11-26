package com.fixhub.common.exception;

/**
 * 代表认证或授权失败的业务异常。
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
