package com.maizer.usercenter.exception;

import com.maizer.usercenter.common.BaseResponse;
import com.maizer.usercenter.common.ErrorCode;
import com.maizer.usercenter.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 接收抛出的某种类型的异常，并取出其中的信息，返回统一响应对象
 * 前端就直接抛出错误就行了，
 * 可以直接抛出一个ErrorCode类型的错误或者再加一个description，这些再BusinessException文件中定义
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
