package com.datascience.eventstore;

import builders.EventStoreBuilder;
import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) {
        try {
            new EventStoreBuilder().start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}