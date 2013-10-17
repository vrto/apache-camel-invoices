package com.vrtoonjava.invoices;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LowEnoughAmountPredicateTest {

    @Test
    public void testAccept() {
        LowEnoughAmountPredicate predicate = new LowEnoughAmountPredicate();

        assertTrue(predicate.matches(exchange(invoice("0"))));
        assertTrue(predicate.matches(exchange(invoice("100"))));
        assertTrue(predicate.matches(exchange(invoice("9999"))));

        assertFalse(predicate.matches(exchange(invoice("10000"))));
        assertFalse(predicate.matches(exchange(invoice("100000"))));
        assertFalse(predicate.matches(exchange(invoice("13337"))));
    }

    private Invoice invoice(String amount) {
        return new Invoice("some-iban", "some-addr", "some-acc", new BigDecimal(amount));
    }

    private Exchange exchange(Invoice invoice) {
        Exchange exchange = new DefaultExchange((CamelContext) null); // doesn't matter now
        DefaultMessage in = new DefaultMessage();
        in.setBody(invoice);
        exchange.setIn(in);
        return exchange;
    }

}
