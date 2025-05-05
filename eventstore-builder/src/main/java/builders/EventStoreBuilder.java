package builders;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.nio.file.*;

public class EventStoreBuilder {
    private static final String BROKER_URL = "tcp://localhost:61616";

    public void start() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection();
        connection.setClientID("eventstore-builder");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Suscripción a múltiples topics
        subscribeToTopic(session, "News");
        subscribeToTopic(session, "Videos");
    }

    private void subscribeToTopic(Session session, String topicName) throws JMSException {
        System.out.println("[SUBSCRIPTION] Suscrito a: " + topicName);
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, "subs-" + topicName);
        consumer.setMessageListener(this::saveEventToFile);
    }

    private void saveEventToFile(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String json = textMessage.getText();
            com.google.gson.JsonObject event = new com.google.gson.JsonParser().parse(json).getAsJsonObject();

            String date = event.get("ts").getAsString().substring(0, 10).replace("-", "");
            Path path = Paths.get(
                    "eventstore",
                    event.get("topic").getAsString(),
                    event.get("ss").getAsString(),
                    date + ".events"
            );

            Files.createDirectories(path.getParent());
            Files.write(path, (json + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Error al guardar evento: " + e.getMessage());
        }
    }
}