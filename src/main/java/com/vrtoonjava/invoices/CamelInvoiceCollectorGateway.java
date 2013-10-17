package com.vrtoonjava.invoices;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CamelInvoiceCollectorGateway implements InvoiceCollectorGateway {

    @Produce(uri = "seda:newInvoicesChannel")
    ProducerTemplate producerTemplate;

    @Override
    public void collectInvoices(Collection<Invoice> invoices) {
        producerTemplate.sendBody(invoices);
    }

}
