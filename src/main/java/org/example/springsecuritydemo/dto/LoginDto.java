package org.example.springsecuritydemo.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}
