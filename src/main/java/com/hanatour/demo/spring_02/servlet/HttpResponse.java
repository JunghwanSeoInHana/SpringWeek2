package com.hanatour.demo.spring_02.servlet;

import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private String statusCode;
    private Map<String, String> headers;
    private Object responseBody;

    public String getStatusCode() {
        if(Objects.isNull(statusCode) || " ".equals(statusCode)) {
            return "200";
        }

        return statusCode;
    }

    public Map<String, String> getHeaders() {
        if(this.headers.isEmpty()) {
            headers.put("Content-Type", "text/html");
        }

        return headers;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }
}
