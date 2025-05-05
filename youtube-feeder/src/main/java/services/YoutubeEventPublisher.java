package services;

import models.Event;
import models.YouTubeVideo;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import com.google.gson.JsonObject;

public class YouTubeEventPublisher {
    private static final String BROKER_URL = "tcp://localhost:61616";

    public void publishVideoEvent(YouTubeVideo video) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("Videos");

            MessageProducer producer = session.createProducer(topic);
            JsonObject eventData = video.getVideoData(); // Asegúrate que este método exista
            Event event = new Event("youtube-feeder", "Videos", eventData);

            String json = new com.google.gson.Gson().toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

        } catch (JMSException e) {
            System.err.println("Error al publicar: " + e.getMessage());
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (JMSException e) { /* Ignorar */ }
            }
        }
    }
}