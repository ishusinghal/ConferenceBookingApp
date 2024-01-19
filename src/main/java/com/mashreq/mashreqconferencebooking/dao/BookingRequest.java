package com.mashreq.mashreqconferencebooking.dao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookingRequest {

    private String startTime;
    private String endTime;

    @Min(value = 1, message = "Minimum Participants to be invited in Conference Room booking is 1")
    @Max(value = 20, message = "Maximum Participants to be invited in Conference Room booking is 20")
    private int participants;

    private int userId;

}
