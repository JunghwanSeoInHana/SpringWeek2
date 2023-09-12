package com.hanatour.demo.spring_01.servlet;

import java.util.Map;

public class HttpResponse {
    private final String statusCode;
    private final Map<String, String> headers;
    private final Object responseBody;

    public HttpResponse(String statusCode, Map<String, String> headers, Object responseBody) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getResponseBody() {
        return responseBody;
    }
}
