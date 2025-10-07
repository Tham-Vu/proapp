package com.example.user_management.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseModel {
    private String request_id;
    private String request_time;
    private int code;
    private String message;
    private String data;

    @Override
    public String toString() {
        return "CommonResponseModel{" +
                "request_id='" + request_id +
                ", request_time='" + request_time +
                ", code=" + code +
                ", message='" + message +
                ", data='" + data +
                '}';
    }
}
