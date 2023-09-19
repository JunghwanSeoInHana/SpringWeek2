package com.hanatour.demo.spring_02;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.hanatour.demo.spring_02.exception.PathNotFoundException;
import com.hanatour.demo.spring_02.servlet.HttpRequest;
import com.hanatour.demo.spring_02.servlet.HttpRequestConvertor;
import com.hanatour.demo.spring_02.servlet.HttpResponse;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class TinyCat {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public void run(ApplicationContext applicationContext, int port) {
        DispatcherServlet dispatcherServlet = applicationContext.getBean(DispatcherServlet.class);
        HttpRequestConvertor httpRequestConvertor = applicationContext.getBean(HttpRequestConvertor.class);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    try {
                        HttpRequest httpRequest = httpRequestConvertor.convert(clientSocket);
                        HttpResponse httpResponse = new HttpResponse();

                        // HttpServlet.service() 생략
                        // FrameworkServlet.processRequest() 생략

                        dispatcherServlet.doService(httpRequest, httpResponse);

                        sendJsonResponse(clientSocket, httpResponse);
                    } catch (PathNotFoundException e) {
                        sendDefaultNotFoundResponse(clientSocket);
                    } catch (Exception e) {
                        sendDefaultInternalServerResponse(clientSocket);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendJsonResponse(Socket clientSocket, HttpResponse httpResponse) throws IOException {
        String contentType = httpResponse.getHeaders().get("Content-Type");

        String responseContent;
        if ("application/json".equals(contentType)) {
            responseContent = objectMapper.writeValueAsString(Map.of("result", httpResponse.getResponseBody()));
        } else if ("application/xml".equals(contentType)) {
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true);
            xmlMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
            responseContent = xmlMapper.writeValueAsString(Map.of("result", httpResponse.getResponseBody()));
        } else {
            responseContent = String.valueOf(httpResponse.getResponseBody());
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("HTTP/1.1 " + httpResponse.getStatusCode());
        out.println("Content-Type: " + contentType);
        out.println("Connection: close");
        out.println("Content-Length: " + responseContent.length());
        out.println("");
        out.println(responseContent);
        out.close();
    }

    private void sendDefaultNotFoundResponse(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("HTTP/1.1 404");
        out.println("Content-Type: text/plain");
        out.println("Connection: close");
        out.println("Content-Length: 0");
        out.println("");
        out.close();
    }

    private void sendDefaultInternalServerResponse(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("HTTP/1.1 500");
        out.println("Content-Type: text/plain");
        out.println("Connection: close");
        out.println("Content-Length: 0");
        out.println("");
        out.close();
    }
}
