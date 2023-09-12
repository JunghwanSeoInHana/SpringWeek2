package com.hanatour.demo.spring_02.servlet;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HttpResponseConvertor {
    public HttpResponse convert(String statusCode, Map<String, String> headers, String responseBody) {
        return new HttpResponse(statusCode, headers, responseBody);
    }

    public HttpResponse convert(Map<String, String> headers, Object responseBody) {
        return new HttpResponse("200", headers, responseBody);
    }

    public HttpResponse convert(Object responseBody) {
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");

        return new HttpResponse("200", defaultHeaders, responseBody);
    }
}
