import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Main {
    private static final String NEWS_API_KEY = "29ee668ea1e3452487efcf707588897f"; // Ej: "6a1b2c3d4e5f6g7h8i9j0"
    private static final String NEWS_API_URL = "https://newsapi.org/v2/everything?q=cultural+events&apiKey=" + NEWS_API_KEY;
    private static final String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        try {
            // 1. Consultar API de noticias
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(NEWS_API_URL))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // 2. Enviar a ActiveMQ
                ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
                Connection connection = factory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic("News");

                MessageProducer producer = session.createProducer(topic);
                TextMessage message = session.createTextMessage(response.body());

                producer.send(message);
                System.out.println("Noticia enviada: " + response.body().substring(0, 50) + "...");

                connection.close();
            } else {
                System.err.println("Error en API: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}