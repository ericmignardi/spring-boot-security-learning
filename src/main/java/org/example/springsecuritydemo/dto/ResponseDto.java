package org.example.springsecuritydemo.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";
    public ResponseDto(String accessToken){
        this.accessToken = accessToken;
    }
}
