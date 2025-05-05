package services;

import models.Event;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class ActiveMQPublisher {
    private static final String BROKER_URL = "tcp://localhost:61616";

    public void publish(Event event) {
        try {
            // 1. Configuración con timeout y exception listener
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            factory.setConnectResponseTimeout(10000); // Timeout de 10 segundos
            factory.setExceptionListener(e -> System.err.println("[ActiveMQ] Error: " + e));

            // 2. Establecer conexión (con verificación explícita)
            Connection connection = factory.createConnection();
            connection.start(); // ¡Esta línea es crucial!

            // 3. Crear sesión y publicar
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(event.getTopic());
            MessageProducer producer = session.createProducer(topic);

            String json = new com.google.gson.Gson().toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

            System.out.println("[ActiveMQ] Evento publicado en tópico: " + event.getTopic()); // Log adicional

            // 4. Cerrar recursos
            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            System.err.println("[ActiveMQ] Error al publicar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}