package com.mashreq.mashreqconferencebooking.exception;

import lombok.Data;

@Data
public class ResponseException {
    private String status;
    private String message;
    private String errorCode;
    private Object errorDetails;
}
