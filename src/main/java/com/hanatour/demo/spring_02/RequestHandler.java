package com.hanatour.demo.spring_02;

import com.hanatour.demo.spring_02.service.calculate.CalculatorService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    public Map<String, Method> doGet() throws NoSuchMethodException {
        Map<String, Method> getRequestHandlers = new HashMap<>();
        getRequestHandlers.put("/calc/add", CalculatorService.class.getDeclaredMethod("add", Integer.class, Integer.class));
        getRequestHandlers.put("/calc/minus", CalculatorService.class.getDeclaredMethod("minus", Integer.class, Integer.class));
        getRequestHandlers.put("/calc/multiply", CalculatorService.class.getDeclaredMethod("multiply", Integer.class, Integer.class));
        getRequestHandlers.put("/calc/divide", CalculatorService.class.getDeclaredMethod("divide", Integer.class, Integer.class));

        return getRequestHandlers;
    }
}