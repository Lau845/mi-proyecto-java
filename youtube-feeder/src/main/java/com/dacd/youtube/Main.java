
package com.dacd.youtube;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Main {
    private static final String YOUTUBE_API_KEY = "AIzaSyABCVIV0YNFK5C2Nrdk2aWIPyCJoGfvypg"; // Reemplaza con tu clave
    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=cultural+events&type=video&key=" + YOUTUBE_API_KEY;
    private static final String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        try {
            // 1. Consultar API de YouTube
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(YOUTUBE_API_URL))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // 2. Enviar a ActiveMQ
                ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
                Connection connection = factory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic("Videos");  // Cambiado a tópico "Videos"

                MessageProducer producer = session.createProducer(topic);
                TextMessage message = session.createTextMessage(response.body());

                producer.send(message);
                System.out.println("Video enviado: " + response.body().substring(0, 50) + "...");

                connection.close();
            } else {
                System.err.println("Error en YouTube API: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}