package com.hndt.exception;

import com.hndt.enums.ResultEnum;
import lombok.Getter;

/**
 * @author Hystar
 * @date 2018/1/10
 */
@Getter
public class LiveInteractionException extends RuntimeException {

    private Integer code;

    public LiveInteractionException() {
        super();
    }

    public LiveInteractionException(String message) {
        super(message);
    }

    public LiveInteractionException(ResultEnum resultEnum) {
        super(resultEnum.getValue());
        this.code = resultEnum.getCode();
    }

    public LiveInteractionException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
