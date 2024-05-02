package com.accio.api.controller.request;

import lombok.Data;

@Data
public class CreateLibraryRequest {
    private String name;
    private CreateLocationRequest location;
}
