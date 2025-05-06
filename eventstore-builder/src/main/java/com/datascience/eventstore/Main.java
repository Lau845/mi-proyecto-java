package com.datascience.eventstore;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Connection connection = null;
        try {

            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.setClientID("EventStoreBuilder");
            connection.start();


            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageConsumer newsConsumer = createDurableSubscriber(session, "News", "NewsSubscriber");
            MessageConsumer videosConsumer = createDurableSubscriber(session, "Videos", "VideosSubscriber");

            System.out.println("✅ Escuchando tópicos: News | Videos");


            while (true) {
                processMessage(newsConsumer, "news");
                processMessage(videosConsumer, "videos");
                Thread.sleep(1000); // Evita sobrecarga de CPU
            }
        } catch (Exception e) {
            System.err.println("⛔ Error crítico: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    System.err.println("⚠️ Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    private static MessageConsumer createDurableSubscriber(Session session, String topicName, String subscriberName)
            throws JMSException {
        Topic topic = session.createTopic(topicName);
        return session.createDurableSubscriber(topic, subscriberName);
    }

    private static void processMessage(MessageConsumer consumer, String type) {
        try {
            Message message = consumer.receiveNoWait();
            if (message instanceof TextMessage) {
                String content = ((TextMessage) message).getText();
                String date = LocalDate.now().format(DATE_FORMATTER);
                String filePath = String.format("data/%s/%s_events.events", date, type);

                EventStoreBuilder.saveToFile(content, filePath);
                System.out.printf("📥 [%s] Evento guardado en %s%n", type.toUpperCase(), filePath);
            }
        } catch (Exception e) {
            System.err.printf("⚠️ Error procesando %s: %s%n", type, e.getMessage());
        }
    }
}