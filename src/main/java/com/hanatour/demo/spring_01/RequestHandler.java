package com.hanatour.demo.spring_01;

import com.hanatour.demo.spring_01.exception.MethodNotAllowedException;
import com.hanatour.demo.spring_01.exception.PathNotFoundException;
import com.hanatour.demo.spring_01.service.calculate.CalculatorService;
import com.hanatour.demo.spring_01.servlet.HttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequestHandler {
    private final CalculatorService calculatorService;

    public RequestHandler(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public Object handleRequest(HttpRequest httpRequest) {
        String method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        Map<String, String> queryParams = httpRequest.getParams();
        int a = Integer.parseInt(queryParams.get("a"));
        int b = Integer.parseInt(queryParams.get("b"));

        if (method.equals("GET")) {
            if (path.equals("/calc/add")) {
                return calculatorService.add(a, b);
            } else if (path.equals("/calc/minus")) {
                return calculatorService.minus(a, b);
            } else if (path.equals("/calc/multiply")) {
                return calculatorService.multiply(a, b);
            } else if (path.equals("/calc/divide")) {
                return calculatorService.divide(a, b);
            } else {
                throw new PathNotFoundException();
            }
        } else {
            throw new MethodNotAllowedException();
        }
    }
}