package com.fixhub.device.model;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Device {

    private Long id;

    private String name;

    private String model;

    private String location;

    private DeviceStatus status = DeviceStatus.NORMAL;

    private LocalDate purchaseDate;

    private String description;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    public enum DeviceStatus {
        NORMAL, BROKEN, SCRAPPED
    }

}
