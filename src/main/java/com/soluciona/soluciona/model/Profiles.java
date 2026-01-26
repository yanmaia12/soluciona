package com.soluciona.soluciona.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor

public class Profiles {

    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "default_location")
    private String defaultLocation;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "fcm_token")
    private String fcmToken;

}
