package com.application.exception;

import com.application.config.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessGlobalException extends RuntimeException {

    private String errorCode;
    private String errMessage;
    private String statusCode;

    private String serviceName;
    private String requestedUrl;
    private String metaInfo;

    public BusinessGlobalException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
        this.errorCode = Constants.SERVICE_ERROR;
        this.statusCode = Constants.INTERNAL_SERVER;
    }

    public BusinessGlobalException(String errMessage, String statusCode, String errorCode) {
        super(errMessage);
        this.errMessage = errMessage;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public BusinessGlobalException(String errMessage, String statusCode, String errorCode,
                                   String requestedUrl) {
        super(errMessage);
        this.errMessage = errMessage;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.requestedUrl = requestedUrl;
    }

}
