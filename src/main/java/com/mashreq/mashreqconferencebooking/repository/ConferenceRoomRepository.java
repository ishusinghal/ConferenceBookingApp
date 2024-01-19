package com.mashreq.mashreqconferencebooking.repository;

import com.mashreq.mashreqconferencebooking.entity.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Integer> {
}
