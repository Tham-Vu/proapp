package com.example.proapp_gateway.utils;

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

    public LoggerInfo(String stepName, String url, String request, Date requestTime, Date responseTime, String content) {
        this.stepName = stepName;
        this.url = url;
        this.request = request;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.content = content;
    }

    public LoggerInfo(String username, String role, String stepName, String url, String request, Date requestTime, Date responseTime, String content) {
        this.username = username;
        this.role = role;
        this.stepName = stepName;
        this.url = url;
        this.request = request;
        this.requestTime = requestTime;
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
