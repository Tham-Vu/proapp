package com.example.proapp_gateway.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonResponse {
    private int code;
    private String message;

    @Override
    public String toString() {
        return "JsonResponse{" +
                "code=" + code +
                ", message='" + message +
                '}';
    }
}
