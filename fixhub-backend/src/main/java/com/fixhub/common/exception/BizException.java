package com.fixhub.common.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    
    private final Integer code;
    
    public BizException(String message) {
        super(message);
        this.code = 400; // Default business error code
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
