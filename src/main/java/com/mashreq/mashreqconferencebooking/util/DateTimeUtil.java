package com.mashreq.mashreqconferencebooking.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static Timestamp getFormattedTimeStamp(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(localDateTime.format(formatter));
    }

    public static LocalDateTime getLocalDateTime(long startTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime), ZoneId.systemDefault());
    }


}
