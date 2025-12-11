package com.soluciona.soluciona.dto;


import com.soluciona.soluciona.enums.RequestStatus;
import lombok.Data;

@Data
public class RequestStatusUpdateDTO {
    private RequestStatus status;
}