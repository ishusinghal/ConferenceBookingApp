package com.mashreq.mashreqconferencebooking.exception;


import lombok.Getter;

@Getter
public enum ErroCode {
    E_SYSTEM_ERROR("E_SYSTEM_ERROR", "SYSTEM_ERROR"),
    E_INTERFACE_ERROR_UNKNOWN_PARAMETERS("E_INTERFACE_ERROR_UNKNOWN_PARAMETERS", "INTERFACE_ERROR_UNKNOWN_PARAMETERS"),
    E_INTERNAL_SERVER_ERROR("E_INTERNAL_SERVER_ERROR", "Something went wrong"),
    E_CONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY("E_CONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY","Conference Room booking possible only for Today's Date"),
    E_CONF_ROOM_BOOK_END_DATE_GREATER_THAN_START("E_CONF_ROOM_BOOK_END_DATE_GREATER_THAN_START","End date Should not be before start date"),
    E_CONF_ROOM_BOOKING_INTERVAL("E_CONF_ROOM_BOOKING_INTERVAL","Conference Room booking can be done only in intervals of 15 mins, 2:00 - 2:15 or 2:00 - 2:30 or 2:00 - 3:00"),
    E_CONF_ROOM_BOOKING_UNDER_MAINT("E_CONF_ROOM_BOOKING_UNDER_MAINT","Conference Room Booking Under Maintenance. 9:00 - 9:15 OR 13:00 - 13:15 OR 17:00 - 17:15 Booking Not Possible, Please select different slots"),
    E_CONF_ROOM_BOOKING_INVAILD_USER("E_CONF_ROOM_BOOKING_INVAILD_USER","Invalid User.Please ask admin to get registered."),
    E_CONF_ROOM_BOOKING_ROOM_UNAVAILABLE("E_CONF_ROOM_BOOKING_ROOM_UNAVAILABLE","No Conference Room Available");

    private String errorCode;
    private String message;

    ErroCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}