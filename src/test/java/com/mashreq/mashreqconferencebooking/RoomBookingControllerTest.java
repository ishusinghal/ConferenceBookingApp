package com.mashreq.mashreqconferencebooking;

import com.mashreq.mashreqconferencebooking.controller.RoomBookingController;
import com.mashreq.mashreqconferencebooking.dao.BookingRequest;
import com.mashreq.mashreqconferencebooking.dao.ConferenceDto;
import com.mashreq.mashreqconferencebooking.dao.ConferenceRoomDto;
import com.mashreq.mashreqconferencebooking.dao.RoomBookingInfo;
import com.mashreq.mashreqconferencebooking.exception.ConferenceBookingGenericException;
import com.mashreq.mashreqconferencebooking.exception.ErroCode;
import com.mashreq.mashreqconferencebooking.service.RoomBookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomBookingControllerTest {

    @InjectMocks
    RoomBookingController roomBookingController;
    @Mock
    RoomBookingService roomBookingService;


    @Test
    public void testShouldBeAbleToFindRoom() throws Exception {
        ConferenceRoomDto conferenceRoomDto= new ConferenceRoomDto();
        conferenceRoomDto.setRoomId(1);
        conferenceRoomDto.setSize(3);
        conferenceRoomDto.setName("Amaze");
        conferenceRoomDto.setFloor(1);

        ConferenceDto conferenceDto= new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(List.of(conferenceRoomDto));

        when(roomBookingService.findRoom(1705670100L, 1705671000L)).thenReturn(conferenceDto);
       Assertions.assertEquals(roomBookingController.findRoom(1705670100L, 1705671000L).getBody().getAvailableConferenceRooms().get(0).getSize(), 3);
    }

    @Test
    public void testShouldBeAbleToThrowMaintenanceMessage() throws Exception {
        ConferenceRoomDto conferenceRoomDto= new ConferenceRoomDto();
        conferenceRoomDto.setRoomId(1);
        conferenceRoomDto.setSize(3);
        conferenceRoomDto.setName("Amaze");
        conferenceRoomDto.setFloor(1);

        ConferenceDto conferenceDto= new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(List.of(conferenceRoomDto));

        DateTimeException ex=  new DateTimeException("Conference Room Booking Under Maintenance. " +
                "9:00 - 9:15 OR 13:00 - 13:15 OR 17:00 - 17:15 Booking Not Possible, Please select different slots");
        Long startTime= 1705654800L;
        Long endTime= 1705655700L;

        when(roomBookingService.findRoom(startTime, endTime)).thenThrow(ex);
        assertThrows(DateTimeException.class, () -> roomBookingController.findRoom(startTime, endTime).getBody());
    }


     @Test
    public void testShouldBeAbleToBookOnlyforCurrentDateWithErrorMessage() throws Exception {
        ConferenceRoomDto conferenceRoomDto= new ConferenceRoomDto();
        conferenceRoomDto.setRoomId(1);
        conferenceRoomDto.setSize(3);
        conferenceRoomDto.setName("Amaze");
        conferenceRoomDto.setFloor(1);

        ConferenceDto conferenceDto= new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(List.of(conferenceRoomDto));

        Long startTime= 1718874900L;
        Long endTime= 1718875800L;

         ConferenceBookingGenericException ex =  new ConferenceBookingGenericException(new RuntimeException(),
                 ErroCode.E_CONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY);

        when(roomBookingService.findRoom(startTime, endTime)).thenThrow(ex);
        assertThrows(ConferenceBookingGenericException.class, () -> roomBookingController.findRoom(startTime, endTime).getBody());
    }

    @Test
    public void testShouldBeAbleToBookOnlyDoneWith15MinIntervalErrorMessage() throws Exception {
        ConferenceRoomDto conferenceRoomDto= new ConferenceRoomDto();
        conferenceRoomDto.setRoomId(1);
        conferenceRoomDto.setSize(3);
        conferenceRoomDto.setName("Amaze");
        conferenceRoomDto.setFloor(1);

        ConferenceDto conferenceDto= new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(List.of(conferenceRoomDto));

        ConferenceBookingGenericException ex =  new ConferenceBookingGenericException(new RuntimeException(),
                ErroCode.E_CONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY);
        Long startTime= 1718874900L;
        Long endTime= 1718875800L;

        when(roomBookingService.findRoom(startTime, endTime)).thenThrow(ex);
        assertThrows(ConferenceBookingGenericException.class, () -> roomBookingController.findRoom(startTime, endTime).getBody());
    }


    @Test
    public void testShouldbeAbletoBookConfRooom() throws Exception {
        Long startTime= 1718874900L;
        Long endTime= 1718875800L;
        BookingRequest bookingRequest= new BookingRequest();
        bookingRequest.setParticipants(3);
        bookingRequest.setStartTime(startTime.toString());
        bookingRequest.setEndTime(endTime.toString());
        bookingRequest.setUserId(1);

        ConferenceRoomDto conferenceRoomDto= new ConferenceRoomDto();
        conferenceRoomDto.setRoomId(1);
        conferenceRoomDto.setSize(3);
        conferenceRoomDto.setName("Amaze");
        conferenceRoomDto.setFloor(1);

        RoomBookingInfo roomBookingInfo = new RoomBookingInfo();
        roomBookingInfo.setRoomBookingId(10201212);
        roomBookingInfo.setStartDateTime(new Date());
        roomBookingInfo.setEndDateTime(new Date());
        roomBookingInfo.setUserName("ishusi");
        roomBookingInfo.setConferenceRoomDto(conferenceRoomDto);

        when(roomBookingService.createRoomBooking(bookingRequest)).thenReturn(roomBookingInfo);

       Assertions.assertEquals(roomBookingController.createRoomBooking(bookingRequest).getBody().getConferenceRoomDto().getRoomId(), 1);
    }

}
