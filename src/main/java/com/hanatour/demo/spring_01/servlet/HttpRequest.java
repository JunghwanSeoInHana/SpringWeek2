package com.hanatour.demo.spring_01.servlet;

import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> params;
    private final Map<String, String> headers;
    private final String contentType;

    public HttpRequest(String method, String path, Map<String, String> params, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.params = params;
        this.headers = headers;
        this.contentType = findContentType();
    }

    private String findContentType() {
        return headers.getOrDefault("Content-Type", "application/json");
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getContentType() {
        return contentType;
    }
}
