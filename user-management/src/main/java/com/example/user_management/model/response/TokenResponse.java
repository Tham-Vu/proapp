package com.example.user_management.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;

    @Override
    public String toString() {
        return "TokenResponse{" +
                "access_token='" + access_token +
                ", refresh_token='" + refresh_token +
                ", token_type='" + token_type +
                ", expires_in='" + expires_in +
                '}';
    }
}
