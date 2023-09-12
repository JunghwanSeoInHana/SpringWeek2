package com.hanatour.demo.spring_01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Spring01Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(Spring01Application.class.getPackageName());
        context.refresh();

        int tinyCatPort = 8080;
        TinyCat tinyCat = new TinyCat();
        tinyCat.run(context, tinyCatPort);
    }
}
