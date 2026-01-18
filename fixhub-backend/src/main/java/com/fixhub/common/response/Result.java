package com.fixhub.common.response;

import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    
    private Integer code;
    private String message;
    private T data;
    private String requestId;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "success";
        result.data = data;
        result.requestId = MDC.get("traceId"); // Assuming traceId is in MDC
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.requestId = MDC.get("traceId");
        return result;
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}
