package com.example.user_management.utils;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoggerInfo {
    private String username;
    private String role;
    private String stepName;
    private String url;
    private String request_id;
    private String request;
    private Date requestTime;
    private Date responseTime;
    private String content;

    public LoggerInfo(String username, String stepName, String url, String request_id, String request, Date requestTime) {
        this.username = username;
        this.stepName = stepName;
        this.url = url;
        this.request_id = request_id;
        this.request = request;
        this.requestTime = requestTime;
    }

    public LoggerInfo(String stepName, Date responseTime, String content) {
        this.stepName = stepName;
        this.responseTime = responseTime;
        this.content = content;
    }

    @Override
    public String toString() {
        return "LoggerInfo{" +
                "username='" + username + ", role='" + role +
                ", stepName='" + stepName +
                ", url='" + url +
                ", request_id='" + request_id +
                ", request='" + request +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                ", content='" + content + '}';
    }
}
