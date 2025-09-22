package dev.thangngo.lmssoftdreams.exceptions;

import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
