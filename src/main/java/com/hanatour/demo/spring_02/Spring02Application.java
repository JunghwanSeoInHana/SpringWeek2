package com.hanatour.demo.spring_02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Spring02Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(Spring02Application.class.getPackageName());
        context.refresh();

        int tinyCatPort = 8080;
        TinyCat tinyCat = new TinyCat();
        tinyCat.run(context, tinyCatPort);
    }
}
