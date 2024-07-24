package com.example.beerApp.dto.authUser;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthUserResponse {
    private String accessToken;
    private long accessExpiresIn;
    private String refreshToken;
    private long refreshExpiresIn;
}
