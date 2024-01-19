package com.mashreq.mashreqconferencebooking.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="tuser")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "userId")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @NotNull
    @Setter
    private String userName;

    @NotNull
    @Setter
    @Size(min=2, max=30)
    private String firstName;

    @NotNull
    @Setter
    @Size(min=2, max=30)
    private String lastName;


}
