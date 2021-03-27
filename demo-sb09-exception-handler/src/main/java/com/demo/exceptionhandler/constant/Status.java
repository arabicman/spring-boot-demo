package com.demo.exceptionhandler.constant;

import lombok.Getter;

@Getter
public enum Status {
    /**
     * 操作成功
     */
    OK(200, "操作成功"),

    NOT_ACCEPTABLE(404, "无法访问"),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(500, "服务器出错啦");
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 内容
     */
    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
