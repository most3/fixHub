package com.fixhub.common.exception;

/**
 * 用于表示业务约束冲突（如唯一键重复）的异常。
 */
public class DuplicateResourceException extends BizException {

    public DuplicateResourceException(String message) {
        super(409, message);
    }
}
