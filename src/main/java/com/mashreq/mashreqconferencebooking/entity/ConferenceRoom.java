package com.mashreq.mashreqconferencebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name="conference_room")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "roomId")
public class ConferenceRoom {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomId;

    @NotNull(message = "Field must not be empty.")
    @Min(value=0)
    @Max(value=50)
    @Setter
    private Integer floor;

    @NotBlank(message = "Field must not be empty.")
    @Size(min=2,max=30)
    @Setter
    private String name;

    @NotNull(message = "Field must not be empty.")
    @Min(value=1)
    @Max(value=50)
    @Setter
    private Integer size;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "conferenceRoom")
    @Setter
    private List<RoomBooking> roomBookings;


    public String toString() {
        return "ConferenceRoom{" +
                "id=" + roomId +
                ", floor=" + floor +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
