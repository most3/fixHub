package com.fixhub.common.exception;

import com.fixhub.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 统一异常处理，将服务端异常转换为统一的 JSON 响应。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBizException(BizException ex) {
        log.warn("Business exception: {}", ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理资源重复冲突，如用户名已存在。
     */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<Void> handleDuplicateResource(DuplicateResourceException ex) {
        return Result.error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    /**
     * 处理认证或授权失败。
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleUnauthorized(UnauthorizedException ex) {
        return Result.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    /**
     * 处理 Bean Validation 校验失败，按字段返回错误信息。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 兜底异常处理，避免堆栈信息泄露给调用端。
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请联系管理员");
    }
}
