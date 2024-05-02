package com.accio.api.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateBookRequest {
    private String name;
    private List<Integer> librariesIds;
}
