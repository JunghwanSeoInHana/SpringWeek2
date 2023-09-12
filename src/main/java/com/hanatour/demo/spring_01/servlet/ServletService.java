package com.hanatour.demo.spring_01.servlet;

import com.hanatour.demo.spring_01.RequestHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class ServletService {
    private final RequestHandler requestHandler;
    private final HttpRequestConvertor httpRequestConvertor;
    private final HttpResponseConvertor httpResponseConvertor;

    public ServletService(RequestHandler requestHandler, HttpRequestConvertor httpRequestConvertor, HttpResponseConvertor httpResponseConvertor) {
        this.requestHandler = requestHandler;
        this.httpRequestConvertor = httpRequestConvertor;
        this.httpResponseConvertor = httpResponseConvertor;
    }

    public HttpResponse doServlet(Socket clientSocket) throws IOException {
        HttpRequest httpRequest = httpRequestConvertor.convert(clientSocket);

        Object result = requestHandler.handleRequest(httpRequest);
        return httpResponseConvertor.convert(httpRequest.getHeaders(), result);
    }
}
