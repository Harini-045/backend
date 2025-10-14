package com.application.exception;

import com.application.config.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerException extends BusinessGlobalException {

    private String error;
    private String errorCode;
    private String errMessage;
    private String statusCode;

    private String serviceName;
    private String requestedUrl;
    private String metaInfo;

    public ServerException() {
        super("Internal error in service");
        this.statusCode = Constants.INTERNAL_SERVER;
        this.errorCode = Constants.SERVICE_ERROR;
    }

    public ServerException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
        this.statusCode = Constants.INTERNAL_SERVER;
        this.errorCode = Constants.SERVICE_ERROR;
    }

    public ServerException(String errMessage, String errorCode) {
        super(errMessage, Constants.INTERNAL_SERVER, errorCode);
        this.errMessage = errMessage;
        this.statusCode = Constants.INTERNAL_SERVER;
        this.errorCode = errorCode;
    }

    public ServerException(String errMessage, String errorCode, String requestedUrl,
                           String serviceName) {
        super(errMessage, Constants.INTERNAL_SERVER, errorCode, requestedUrl);
        this.errMessage = errMessage;
        this.errorCode = errorCode;
        this.statusCode = Constants.INTERNAL_SERVER;
        this.requestedUrl = requestedUrl;
        this.serviceName = serviceName;
    }

    public ServerException(String errMessage, String errorCode, String requestedUrl,
                           String serviceName, String statusCode) {
        super(errMessage, Constants.INTERNAL_SERVER, errorCode, requestedUrl);
        this.errMessage = errMessage;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.requestedUrl = requestedUrl;
        this.serviceName = serviceName;
    }
}
