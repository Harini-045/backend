package com.application.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private String stackTrace;

    private String errorCode;
    private String message;
    private String status;

    private String metaInfo;
    private String serviceName;
    private String requestedUrl;

    public ApiError(Date timestamp, String errorCode, String message, String status, String metaInfo,
                    String serviceName, String requestedUrl, String stackTrace) {
        this.timestamp = timestamp;
        this.stackTrace = stackTrace;
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
        this.metaInfo = metaInfo;
        this.serviceName = serviceName;
        this.requestedUrl = requestedUrl;
    }

}
