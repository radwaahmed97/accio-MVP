package com.accio.api.controller.request;

import lombok.Data;

@Data
public class CreateLocationRequest {
    private double longitude;
    private double latitude;
}
