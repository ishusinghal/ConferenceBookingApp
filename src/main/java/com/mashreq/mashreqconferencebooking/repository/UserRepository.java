package com.mashreq.mashreqconferencebooking.repository;

import com.mashreq.mashreqconferencebooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {


}
