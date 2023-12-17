package com.skiapi.weatherapiservice.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ErrorDTO {

    private LocalDateTime timeStamp;

    private int status;

    private String path;

    private List<String> errors;
}
