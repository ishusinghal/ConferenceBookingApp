package com.mashreq.mashreqconferencebooking;

import com.mashreq.mashreqconferencebooking.dao.*;
import com.mashreq.mashreqconferencebooking.entity.ConferenceRoom;
import com.mashreq.mashreqconferencebooking.entity.RoomBooking;
import com.mashreq.mashreqconferencebooking.exception.ConferenceBookingGenericException;
import com.mashreq.mashreqconferencebooking.repository.ConferenceRoomRepository;
import com.mashreq.mashreqconferencebooking.repository.RoomBookingRepository;
import com.mashreq.mashreqconferencebooking.service.RoomBookingService;
import com.mashreq.mashreqconferencebooking.service.ValidationService;
import com.mashreq.mashreqconferencebooking.util.DateTimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomBookingServiceTest {

    @Mock
    private RoomBookingRepository roomBookingRepository;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;
    @Mock
    private ValidationService validationService;

    @InjectMocks
    RoomBookingService roomBookingService;

    public void setUp(){
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

    }

    @Test
    public void testShouldbeAbleToCreateRoomBooking() throws Exception {
        com.mashreq.mashreqconferencebooking.entity.User user = new com.mashreq.mashreqconferencebooking.entity.User();
        user.setFirstName("ishu");
        user.setLastName("singhal");
        Optional<com.mashreq.mashreqconferencebooking.entity.User> userEntity = Optional.of(user);
        when(validationService.validateAndGetUser(1)).thenReturn(userEntity);

        Long startTime= 1705689000L;
        Long endTime= 1705689900L;
        ConferenceRoom conferenceRoom= ConferenceRoom.builder()
                .roomId(123)
                .floor(2)
                .size(7)
                .name("Beauty")
                .build();

        LocalDateTime startDateTime = DateTimeUtil.getLocalDateTime(startTime);
        LocalDateTime endDateTime = DateTimeUtil.getLocalDateTime(endTime);

        List<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        conferenceRoom= ConferenceRoom.builder()
                .roomId(120)
                .floor(2)
                .size(5)
                .name("Inspire")
                .build();

        conferenceRoomList.add(conferenceRoom);

        List<ConferenceRoomDto> conferenceRoomDtoList = new ArrayList<>();
        ConferenceDto conferenceDto = new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(conferenceRoomDtoList);

        RoomBooking roomBooking= new RoomBooking();
        conferenceRoom= ConferenceRoom.builder()
                .roomId(125)
                .floor(2)
                .size(7)
                .name("Strive")
                .build();

        roomBooking.setConferenceRoom(conferenceRoom);

        List<RoomBooking> roomBookingList = new ArrayList<>();
        roomBookingList.add(roomBooking);

        when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);
        when(roomBookingRepository.findRoomByStartAndEndTime(DateTimeUtil.getFormattedTimeStamp(startDateTime), DateTimeUtil.getFormattedTimeStamp(endDateTime))).thenReturn(roomBookingList);
        when(conferenceRoomRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(conferenceRoom));

        when(roomBookingRepository.save(roomBooking)).thenReturn(roomBooking);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setUserId(1);
        bookingRequest.setStartTime("1705689000");
        bookingRequest.setEndTime("1705689900");
        bookingRequest.setParticipants(2);

       Assertions.assertNotNull(roomBookingService.createRoomBooking(bookingRequest).getRoomBookingId());
    }

    @Test
    public void shouldThrowCONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY() throws Exception {
        Long startDateTime= 1718874900L;
        Long endDateTime= 1718875800L;
        LocalDateTime localDateTime= LocalDateTime.now();

        ConferenceRoom conferenceRoom= new ConferenceRoom();
        conferenceRoom.setFloor(1);
        conferenceRoom.setName("Beauty");
        conferenceRoom.setFloor(1);

        List<ConferenceRoom> conferenceRoomList = new ArrayList<>();

        List<ConferenceRoomDto> conferenceRoomDtoList = new ArrayList<>();
        ConferenceDto conferenceDto = new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(conferenceRoomDtoList);

        RoomBooking roomBooking= new RoomBooking();
        List<RoomBooking> roomBookingList = new ArrayList<>();
        roomBookingList.add(roomBooking);

        assertThrows(ConferenceBookingGenericException.class, () ->  roomBookingService.findRoom(startDateTime, endDateTime).getAvailableConferenceRooms().get(0).getRoomId());
    }

    @Test
    public void shouldbeAbletoFindRoom() throws Exception {

        Long startTime= 1705642200L;
        Long endTime= 1705643100L;
        ConferenceRoom conferenceRoom= ConferenceRoom.builder()
                .roomId(123)
                .floor(2)
                .size(7)
                .name("Beauty")
                .build();

        LocalDateTime startDateTime = DateTimeUtil.getLocalDateTime(startTime);
        LocalDateTime endDateTime = DateTimeUtil.getLocalDateTime(endTime);

        List<ConferenceRoom> conferenceRoomList = new ArrayList<>();
        conferenceRoomList.add(conferenceRoom);
        conferenceRoom= ConferenceRoom.builder()
                .roomId(120)
                .floor(2)
                .size(5)
                .name("Inspire")
                .build();

        conferenceRoomList.add(conferenceRoom);

        List<ConferenceRoomDto> conferenceRoomDtoList = new ArrayList<>();
        ConferenceDto conferenceDto = new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(conferenceRoomDtoList);

        RoomBooking roomBooking= new RoomBooking();
        conferenceRoom= ConferenceRoom.builder()
                .roomId(125)
                .floor(2)
                .size(7)
                .name("Strive")
                .build();

        roomBooking.setConferenceRoom(conferenceRoom);

        List<RoomBooking> roomBookingList = new ArrayList<>();
        roomBookingList.add(roomBooking);

        when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);
        when(roomBookingRepository.findRoomByStartAndEndTime(DateTimeUtil.getFormattedTimeStamp(startDateTime), DateTimeUtil.getFormattedTimeStamp(endDateTime))).thenReturn(roomBookingList);

       Assertions.assertEquals(roomBookingService.findRoom(startTime, endTime).getAvailableConferenceRooms().size(),2);
    }

}
