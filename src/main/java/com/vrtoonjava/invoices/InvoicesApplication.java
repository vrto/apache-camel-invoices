package com.vrtoonjava.invoices;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InvoicesApplication {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("/invoices-context.xml");
    }

}
