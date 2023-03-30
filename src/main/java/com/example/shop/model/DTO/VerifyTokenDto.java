package com.example.shop.model.DTO;

import lombok.Data;

@Data
public class VerifyTokenDto {
    private String usernameOrEmail;
    private String token;
}
