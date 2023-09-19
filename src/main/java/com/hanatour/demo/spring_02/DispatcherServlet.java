package com.hanatour.demo.spring_02;

import com.hanatour.demo.spring_02.servlet.HttpRequest;
import com.hanatour.demo.spring_02.servlet.HttpResponse;
import com.hanatour.demo.spring_02.servlet.HttpResponseConvertor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class DispatcherServlet {
    private final RequestHandler requestHandler;
    private final HttpResponseConvertor httpResponseConvertor;

    private Map<String, Map<String, Method>> requestHandlers;

    public DispatcherServlet(RequestHandler requestHandler, HttpResponseConvertor httpResponseConvertor) {
        this.requestHandler = requestHandler;
        this.httpResponseConvertor = httpResponseConvertor;
    }

    public void initStrategies() throws NoSuchMethodException {
        Map<String, Method> getRequestHandlers = requestHandler.doGet();

        Map<String, Map<String, Method>> requdestHandlers = new HashMap<>();
        this.requestHandlers = requdestHandlers.put("GET", getRequestHandlers);
    }

    public void doService(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // Logging
        // Request snapshot

        this.doDispatcher(httpRequest, httpResponse);
    }

    private void doDispatcher(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // HandlerMapping 조회 (Interceptor 처리)
        // HandlerAdapter 조회
        // HandlerAdapter -> Controller call (HandlerMapping 포함)

        Object result = requestHandler.handleRequest(httpRequest);

        httpResponse = httpResponseConvertor.convert(httpRequest.getHeaders(), result);
    }
}
