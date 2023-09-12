package com.hanatour.demo.spring_01.servlet;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

@Component
public class HttpRequestConvertor {
    public HttpRequest convert(Socket clientSocket) throws IOException {
        List<String> lines = getRequest(clientSocket);
        if (lines.isEmpty()) {
            return null;
        }

        String request = lines.get(0);
        if (Objects.isNull(request)) {
            throw new RuntimeException();
        }

        Map<String, String> headers = getHeaders(lines);

        String[] requestParts = request.split(" ");
        String method = requestParts[0];
        String url = requestParts[1];

        String path = url.split("\\?")[0];
        Map<String, String> requestParams = parseQueryParameters(url);

        return new HttpRequest(method, path, requestParams, headers);
    }

    private static List<String> getRequest(Socket clientSocket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        List<String> lines = new ArrayList<>();
        String line;
        while (StringUtils.isEmpty(line = bufferedReader.readLine()) == false) {
            System.out.println(line);
            lines.add(line);
        }

        return lines;
    }

    private Map<String, String> parseQueryParameters(String url) {
        Map<String, String> queryParams = new HashMap<>();
        String queryString = url.substring(url.indexOf('?') + 1);
        String[] paramPairs = queryString.split("&");
        for (String paramPair : paramPairs) {
            String[] keyValue = paramPair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }


    private static Map<String, String> getHeaders(List<String> lines) {
        Map<String, String> headers = new HashMap<>();

        lines.forEach(line -> {
            if ("".equals(line)) {
                return;
            }

            if (line.contains(":")) {
                String[] split = line.split(":");
                headers.put(split[0], split[1].trim());
            }
        });

        return headers;
    }
}
