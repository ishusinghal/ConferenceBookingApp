package com.mashreq.mashreqconferencebooking.repository;

import com.mashreq.mashreqconferencebooking.entity.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Integer> {

    @Query("SELECT rb FROM RoomBooking rb WHERE startDateTime= :startTimestamp AND endDateTime = :endTimestamp")
    List<RoomBooking> findRoomByStartAndEndTime(@Param("startTimestamp") Timestamp startTimestamp, @Param("endTimestamp") Timestamp endTimestamp);




}
