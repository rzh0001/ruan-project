package cloud.ruan.boot.aop;

import cloud.ruan.boot.pojo.Result;
import cloud.ruan.boot.util.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ruanzh.eth
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        return ResultUtil.error(e.getMessage());
    }
}
