package com.mdd.controller;

import com.mdd.dto.UserDto;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserDto user;

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }
}
