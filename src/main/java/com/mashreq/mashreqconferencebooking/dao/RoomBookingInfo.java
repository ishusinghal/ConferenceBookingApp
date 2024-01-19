package com.mashreq.mashreqconferencebooking.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBookingInfo {

    private int roomBookingId;
    private Date startDateTime;
    private Date endDateTime;
    private String userName;
    private ConferenceRoomDto conferenceRoomDto;

}
