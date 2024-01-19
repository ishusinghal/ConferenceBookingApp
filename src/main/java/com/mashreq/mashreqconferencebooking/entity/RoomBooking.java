package com.mashreq.mashreqconferencebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Table(name="room_booking")
@EqualsAndHashCode(of = "bookingId")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoomBooking {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int roomBookingId;

    @NotNull
    @ManyToOne
    private User userId;

    @NotNull(message = "Field must not be empty.")
    @Future(message = "Date should be future")
    @Setter
    private Date startDateTime;

    @NotNull(message = "Field must not be empty.")
    @Future(message = "Date should be future")
    @Setter
    private Date endDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="roomId")
    @Setter
    private ConferenceRoom conferenceRoom;

}
