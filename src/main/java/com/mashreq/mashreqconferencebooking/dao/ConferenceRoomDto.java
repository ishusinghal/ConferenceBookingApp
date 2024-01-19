package com.mashreq.mashreqconferencebooking.dao;

import lombok.Data;

@Data
public class ConferenceRoomDto {
    private int roomId;
    private Integer floor;
    private String name;
    private Integer size;

}
