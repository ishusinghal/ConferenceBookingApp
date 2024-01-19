package com.mashreq.mashreqconferencebooking.controller;

import com.mashreq.mashreqconferencebooking.dao.BookingRequest;
import com.mashreq.mashreqconferencebooking.dao.ConferenceDto;
import com.mashreq.mashreqconferencebooking.dao.RoomBookingInfo;
import com.mashreq.mashreqconferencebooking.service.RoomBookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RoomBookingController {

    private RoomBookingService roomBookingService;

    @GetMapping("/conference-rooms/findroom")
    public ResponseEntity<ConferenceDto> findRoom(@RequestParam(value = "startTime") long startTime, @RequestParam(value = "endTime") long endTime) throws Exception {
        return new ResponseEntity<ConferenceDto>(roomBookingService.findRoom(startTime, endTime), HttpStatus.OK);
    }

    @PostMapping("/conference-rooms/booking")
    public ResponseEntity<RoomBookingInfo> createRoomBooking(@RequestBody @Valid BookingRequest bookingRequest) throws Exception {
        RoomBookingInfo roomBookingInfo = roomBookingService.createRoomBooking(bookingRequest);
        return new ResponseEntity<RoomBookingInfo>(roomBookingInfo, HttpStatus.CREATED);
    }

}
