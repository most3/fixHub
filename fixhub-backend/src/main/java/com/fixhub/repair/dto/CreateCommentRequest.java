package com.fixhub.repair.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 创建工单评价的请求参数。
 */
public class CreateCommentRequest {

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能低于 1 分")
    @Max(value = 5, message = "评分不能高于 5 分")
    private Integer rating;

    private String content;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
