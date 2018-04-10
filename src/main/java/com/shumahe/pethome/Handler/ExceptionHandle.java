package com.shumahe.pethome.Handler;

import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO petHomeException(Exception e) {
        if (e instanceof PetHomeException) {
            PetHomeException petHomeException = (PetHomeException) e;
            return ResultVOUtil.error(petHomeException.getCode(), petHomeException.getMessage());
        } else {
            log.error("【系统异常】{}", e);
            return ResultVOUtil.error(999, "累了先休息一下吧");
        }
    }
}
