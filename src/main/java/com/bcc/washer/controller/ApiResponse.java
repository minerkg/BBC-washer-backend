package com.bcc.washer.controller;

import lombok.Builder;

@Builder
public class ApiResponse<T> {

    private String header;
    private T body;

    public ApiResponse(String header, T body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
