package com.mashreq.mashreqconferencebooking.dao;

import lombok.Data;

import java.util.List;

@Data
public class ConferenceDto {
    private List<ConferenceRoomDto> availableConferenceRooms;
}
