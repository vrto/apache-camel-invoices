package com.vrtoonjava.routes;

import com.vrtoonjava.invoices.LowEnoughAmountPredicate;
import com.vrtoonjava.invoices.PaymentProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InvoicesRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("seda:newInvoicesChannel")
                .log(LoggingLevel.INFO, "Invoices processing STARTED")
                .split(body())
                .to("seda:singleInvoicesChannel");

        from("seda:singleInvoicesChannel")
                .filter(new LowEnoughAmountPredicate())
                .to("seda:filteredInvoicesChannel");

        from("seda:filteredInvoicesChannel")
                .choice()
                    .when().simple("${body.isForeign}")
                        .to("seda:foreignInvoicesChannel")
                    .otherwise()
                        .to("seda:localInvoicesChannel");

        from("seda:foreignInvoicesChannel")
                .transform().method("foreignPaymentCreator", "createPayment")
                .to("seda:bankingChannel");

        from("seda:localInvoicesChannel")
                .transform().method("localPaymentCreator", "createPayment")
                .to("seda:bankingChannel");

        from("seda:bankingChannel")
                .errorHandler(deadLetterChannel("log:failedPayments"))
                .bean(PaymentProcessor.class, "processPayment");
    }

}
