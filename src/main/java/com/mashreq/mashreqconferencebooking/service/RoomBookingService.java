package com.mashreq.mashreqconferencebooking.service;

import com.mashreq.mashreqconferencebooking.dao.*;
import com.mashreq.mashreqconferencebooking.entity.ConferenceRoom;
import com.mashreq.mashreqconferencebooking.entity.RoomBooking;
import com.mashreq.mashreqconferencebooking.exception.ConferenceBookingGenericException;
import com.mashreq.mashreqconferencebooking.exception.ErroCode;
import com.mashreq.mashreqconferencebooking.exception.ResourceNotFoundException;
import com.mashreq.mashreqconferencebooking.repository.ConferenceRoomRepository;
import com.mashreq.mashreqconferencebooking.repository.RoomBookingRepository;
import com.mashreq.mashreqconferencebooking.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RoomBookingService {

    @Autowired
    private RoomBookingRepository roomBookingRepository;
    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;
    @Autowired
    private ValidationService validationService;

    public RoomBookingInfo createRoomBooking(BookingRequest bookingRequest) throws Exception {
            String bookRefNo = getBookingRefNo();
            Optional<com.mashreq.mashreqconferencebooking.entity.User> userEntity = validationService.validateAndGetUser(bookingRequest.getUserId());
            if (!userEntity.isPresent()) {
                throw new ConferenceBookingGenericException(new RuntimeException(), ErroCode.E_CONF_ROOM_BOOKING_INVAILD_USER);
            }
            ConferenceDto conferenceDto = findRoom(Long.valueOf(bookingRequest.getStartTime()), Long.valueOf(bookingRequest.getEndTime()));
            if (Objects.isNull(conferenceDto) || Objects.isNull(conferenceDto.getAvailableConferenceRooms()) || conferenceDto.getAvailableConferenceRooms().size() <= 0) {
                throw new ConferenceBookingGenericException(ErroCode.E_CONF_ROOM_BOOKING_ROOM_UNAVAILABLE.getErrorCode());
            }
            List<ConferenceRoomDto> availableConferenceRooms = conferenceDto.getAvailableConferenceRooms();
            availableConferenceRooms = availableConferenceRooms.stream().sorted(Comparator.comparing(ConferenceRoomDto::getSize)).collect(Collectors.toList());

            ConferenceRoomDto foundAvailableRoom = findRoomByCapacity(availableConferenceRooms, bookingRequest.getParticipants());

            if (Objects.isNull(foundAvailableRoom)) {
                throw new ResourceNotFoundException(ErroCode.E_CONF_ROOM_BOOKING_ROOM_UNAVAILABLE.getErrorCode());
            }

            Optional<ConferenceRoom> conferenceRoomOptional = conferenceRoomRepository.findById(foundAvailableRoom.getRoomId());
            RoomBooking roomBookingSavedEntity = saveBooking(bookingRequest, conferenceRoomOptional.get(), userEntity);
            log.info("Booking saved successfully for UserId {} with bookingRefNo {} and RoomBookingId {}", bookingRequest.getUserId(), bookRefNo, roomBookingSavedEntity.getRoomBookingId());
            return new RoomBookingInfo(roomBookingSavedEntity.getRoomBookingId(), roomBookingSavedEntity.getStartDateTime(),
                    roomBookingSavedEntity.getEndDateTime(), userEntity.get().getUserName(), foundAvailableRoom);
    }

    private static String getBookingRefNo() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        StringBuilder bookingReferenceBuilder = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int digit = secureRandom.nextInt(10);
            bookingReferenceBuilder.append(digit);
        }
        return bookingReferenceBuilder.toString();
    }


    private RoomBooking saveBooking(BookingRequest bookingRequest, ConferenceRoom foundAvailableRoom, Optional<com.mashreq.mashreqconferencebooking.entity.User> userEntity) {
        RoomBooking roomBooking= new RoomBooking();
        roomBooking.setConferenceRoom(foundAvailableRoom);
        roomBooking.setUserId(userEntity.get());
        roomBooking.setStartDateTime(DateTimeUtil.getFormattedTimeStamp(DateTimeUtil.getLocalDateTime(Long.valueOf(bookingRequest.getStartTime()))));
        roomBooking.setEndDateTime(DateTimeUtil.getFormattedTimeStamp(DateTimeUtil.getLocalDateTime(Long.valueOf(bookingRequest.getEndTime()))));
        return roomBookingRepository.save(roomBooking);
    }

    public static ConferenceRoomDto findRoomByCapacity(List<ConferenceRoomDto> availableConferenceRooms, int target) {
        for (ConferenceRoomDto conferenceRoom : availableConferenceRooms) {
            if (conferenceRoom.getSize() > target) {
                return conferenceRoom;
            }
        }
        // If no higher number is found, return null
        return null;
    }

    public ConferenceDto findRoom(long startTime, long endTime) throws Exception {
        List<ConferenceRoom> conferenceRoomList = null;
        try {
            LocalDateTime startDateTime = DateTimeUtil.getLocalDateTime(startTime);
            LocalDateTime endDateTime = DateTimeUtil.getLocalDateTime(endTime);

            validationService.checkBookingAvailability(startDateTime, endDateTime);

            List<RoomBooking> bookingList = roomBookingRepository.findRoomByStartAndEndTime(DateTimeUtil.getFormattedTimeStamp(startDateTime), DateTimeUtil.getFormattedTimeStamp(endDateTime));
            conferenceRoomList = conferenceRoomRepository.findAll();
            for (RoomBooking roomBooking : bookingList) {
                conferenceRoomList.removeIf(x -> x.getRoomId() == roomBooking.getConferenceRoom().getRoomId());
            }

        } catch (DateTimeParseException e) {
            log.error("Invalid date-time format. Please use the correct Format");
            throw new ConferenceBookingGenericException(e, ErroCode.E_CONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY);
        } catch (Exception ex) {
            log.error("exception internal server error {}", ex.getMessage());
            throw new ConferenceBookingGenericException(ex, ErroCode.E_INTERNAL_SERVER_ERROR);
        }

        List<ConferenceRoomDto> conferenceRoomDtoList = new ArrayList<>();
        copyBeanProperties(conferenceRoomList, conferenceRoomDtoList);
        ConferenceDto conferenceDto = new ConferenceDto();
        conferenceDto.setAvailableConferenceRooms(conferenceRoomDtoList);
        return conferenceDto;
    }

    private static void copyBeanProperties(List<ConferenceRoom>  conferenceRoomList,  List<ConferenceRoomDto> conferenceRoomDtoList) {
        for(ConferenceRoom conferenceRoom: conferenceRoomList){
            ConferenceRoomDto conferenceRoomDto= new ConferenceRoomDto();
            conferenceRoomDto.setRoomId(conferenceRoom.getRoomId());
            conferenceRoomDto.setName(conferenceRoom.getName());
            conferenceRoomDto.setSize(conferenceRoom.getSize());
            conferenceRoomDto.setFloor(conferenceRoom.getFloor());
            conferenceRoomDtoList.add(conferenceRoomDto);
        }
    }
}
