package com.example.user_management.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "LoginModel{" +
                "username='" + username +
                ", password='" + password +
                '}';
    }
}
