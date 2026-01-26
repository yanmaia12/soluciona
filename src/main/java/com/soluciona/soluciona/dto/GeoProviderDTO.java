package com.soluciona.soluciona.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoProviderDTO {
    private Long id;
    private String name;
    private String serviceTitle;
    private String distance;
    private Double rating;
    private String photo;
    private String linkToHire;
}