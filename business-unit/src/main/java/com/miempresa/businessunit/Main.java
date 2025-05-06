package com.miempresa.businessunit;

import com.miempresa.businessunit.messaging.EventConsumer;
import com.miempresa.businessunit.storage.DataMart;
import com.miempresa.businessunit.storage.EventLoader;
import com.miempresa.businessunit.ui.CLI;

public class Main {
    public static void main(String[] args) {
        DataMart dataMart = new DataMart();

        // Cargar datos históricos
        EventLoader loader = new EventLoader(dataMart);
        loader.loadEvents("./eventstore");

        // Iniciar consumo en tiempo real
        EventConsumer consumer = new EventConsumer(dataMart);
        consumer.start();

        // Lanzar CLI
        CLI cli = new CLI(dataMart);
        cli.start();
    }
}