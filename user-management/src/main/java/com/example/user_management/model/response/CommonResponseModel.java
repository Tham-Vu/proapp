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

    public CommonResponseModel(String request_time, int code, String message) {
        this.request_time = request_time;
        this.code = code;
        this.message = message;
    }

    public CommonResponseModel(String request_time, int code, String message, String data) {
        this.request_time = request_time;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponseModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

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
