package com.soluciona.soluciona.dto;

import lombok.Data;
import java.util.List;

@Data
public class ServicePostRequestDTO {

    private String title;
    private String description;
    private String price;
    private String location;
    private List<String> photos;
    private Long categoryId;
    private Double latitude;
    private Double longitude;
    private String fullAddress;
}