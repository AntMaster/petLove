package com.shumahe.pethome.Exception;

import com.shumahe.pethome.Enums.ResultEnum;
import lombok.Data;


@Data
public class PetHomeException extends RuntimeException {

    private int code;

    public PetHomeException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public PetHomeException(int code, String message) {
        super(message);
        this.code = code;
    }

}
