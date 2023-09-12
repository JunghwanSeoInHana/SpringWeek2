package com.hanatour.demo.spring_01;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.hanatour.demo.spring_01.servlet.HttpResponse;
import com.hanatour.demo.spring_01.servlet.ServletService;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

public class TinyCat {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public void run(ApplicationContext applicationContext, int port) {
        ServletService servletService = applicationContext.getBean(ServletService.class);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    try {
                        HttpResponse httpResponse = servletService.doServlet(clientSocket);
                        if (Objects.isNull(httpResponse)) {
                            continue;
                        }

                        sendJsonResponse(clientSocket, httpResponse);
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
        if ("text/plain".equals(contentType)) {
            responseContent = String.valueOf(httpResponse.getResponseBody());
        } else if ("application/xml".equals(contentType)) {
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true);
            xmlMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
            responseContent = xmlMapper.writeValueAsString(Map.of("result", httpResponse.getResponseBody()));
        } else {
            responseContent = objectMapper.writeValueAsString(Map.of("result", httpResponse.getResponseBody()));
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
