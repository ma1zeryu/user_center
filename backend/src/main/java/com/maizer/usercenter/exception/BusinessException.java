package com.maizer.usercenter.exception;

import com.maizer.usercenter.common.ErrorCode;

/**
 * 自定义异常类，给原本的异常类扩充了两个字段
 * 这个异常类是给service层抛出的，就是让抛出的异常多了一些有用的信息
 * 然后这个抛出的异常给全局统一处理器去取出其中的信息，封装到统一响应对象之中
 * @author jyp
 */
public class BusinessException extends RuntimeException{

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
