package com.mashreq.mashreqconferencebooking.exception;


import ch.qos.logback.core.spi.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * This is to handle GenericException Response for MOB/MOL
 */
@Data
@ToString
public class ConferenceBookingGenericException extends RuntimeException {
    private static final long serialVersionUID = 7514408457818505294L;
    private ErroCode errorCode;
    private Object errorDetails;
    private boolean authError;

    public String getErrorCode() {
        return this.errorCode.getErrorCode();
    }

    public ConferenceBookingGenericException(String s) {
        super(s);
    }

    public ConferenceBookingGenericException(Exception ex) {
        super(ex);
    }

    public ConferenceBookingGenericException(Exception ex, ErroCode errorCode) {
        super(ex);
        this.errorCode = errorCode;
    }

    public ConferenceBookingGenericException(Exception ex, ErroCode errorCode, boolean authError) {
        super(ex);
        this.errorCode = errorCode;
        this.authError = authError;
    }

    public Object getErrorDetails() {
        return this.errorDetails;
    }

    public void setErrorDetails(Object errorDetails) {
        this.errorDetails = errorDetails;
    }

    public boolean isAuthError() {
        return this.authError;
    }

    public void setAuthError(boolean authError) {
        this.authError = authError;
    }

}