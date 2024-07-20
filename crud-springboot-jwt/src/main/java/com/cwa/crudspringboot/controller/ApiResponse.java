package com.cwa.crudspringboot.controller;

import org.springframework.stereotype.Controller;

@Controller
public class ApiResponse {
    private String token;

    public ApiResponse() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
