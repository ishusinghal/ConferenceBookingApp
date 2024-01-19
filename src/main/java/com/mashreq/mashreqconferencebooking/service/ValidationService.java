package com.mashreq.mashreqconferencebooking.service;

import com.mashreq.mashreqconferencebooking.exception.BadRequestExeption;
import com.mashreq.mashreqconferencebooking.exception.ConferenceBookingGenericException;
import com.mashreq.mashreqconferencebooking.exception.ErroCode;
import com.mashreq.mashreqconferencebooking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ValidationService {
    private UserRepository userRepository;

    public Optional<com.mashreq.mashreqconferencebooking.entity.User> validateAndGetUser(int userId) {
        return userRepository.findById(userId);
    }

    public static void checkBookingAvailability(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (!(startDateTime.toLocalDate().equals(currentDateTime.toLocalDate()) &&
                endDateTime.toLocalDate().equals(currentDateTime.toLocalDate()))) {
            throw new ConferenceBookingGenericException(new RuntimeException(), ErroCode.E_CONF_ROOM_BOOK_ONLY_FOR_CURRENT_DAY);
        }

        if (!(startDateTime.compareTo(endDateTime) < 0)) {
            throw new ConferenceBookingGenericException(new RuntimeException(), ErroCode.E_CONF_ROOM_BOOK_END_DATE_GREATER_THAN_START);
        }

        if (startDateTime.getMinute() % 15 != 0 || endDateTime.getMinute() % 15 != 0) {
            throw new ConferenceBookingGenericException(new RuntimeException(), ErroCode.E_CONF_ROOM_BOOKING_INTERVAL);
        }

        if (isBookingUnderMaintenance(startDateTime, endDateTime, LocalTime.of(16, 59, 59), LocalTime.of(17, 14, 59))
                || isBookingUnderMaintenance(startDateTime, endDateTime, LocalTime.of(8, 59, 59), LocalTime.of(9, 14, 59))
                || isBookingUnderMaintenance(startDateTime, endDateTime, LocalTime.of(12, 59, 59), LocalTime.of(13, 14, 59))) {
            throw new ConferenceBookingGenericException(new RuntimeException(), ErroCode.E_CONF_ROOM_BOOKING_UNDER_MAINT);
        }
    }

    private static boolean isBookingUnderMaintenance(
            LocalDateTime startDateTime, LocalDateTime endDateTime, LocalTime maintenanceStartTime, LocalTime maintenanceEndTime) {
        return ((startDateTime.toLocalTime().isAfter(maintenanceStartTime) && startDateTime.toLocalTime().isBefore(maintenanceEndTime))
        || (endDateTime.toLocalTime().isAfter(maintenanceStartTime) && endDateTime.toLocalTime().isBefore(maintenanceEndTime)));
    }




}
