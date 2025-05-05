package services;

import models.Event;
import models.NewsArticle;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class NewsAPIService {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final String apiKey;
    private final String queryKeyword;
    private final ActiveMQPublisher mqPublisher;

    public NewsAPIService(String apiKey, String queryKeyword) {
        this.apiKey = apiKey;
        this.queryKeyword = queryKeyword;
        this.mqPublisher = new ActiveMQPublisher();
    }

    public List<NewsArticle> fetchLatestNews() throws Exception {
        String url = "https://newsapi.org/v2/everything?q=" + queryKeyword + "&apiKey=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        List<NewsArticle> articles = new ArrayList<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Error en NewsAPI: " + response.code());
            }

            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonArray articlesJson = json.getAsJsonArray("articles");

            for (JsonElement article : articlesJson) {
                JsonObject obj = article.getAsJsonObject();
                NewsArticle newsArticle = new NewsArticle(
                        obj.get("title").getAsString(),
                        obj.get("source").getAsJsonObject().get("name").getAsString(),
                        obj.get("publishedAt").getAsString(),
                        obj.get("url").getAsString(),
                        queryKeyword
                );
                articles.add(newsArticle);

                // Publicar evento en ActiveMQ
                publishNewsEvent(newsArticle);
            }
        }
        return articles;
    }

    private void publishNewsEvent(NewsArticle article) {
        try {
            JsonObject eventData = new JsonObject();
            eventData.addProperty("title", article.getTitle());
            eventData.addProperty("source", article.getSource());
            eventData.addProperty("publishedAt", article.getPublishedAt());
            eventData.addProperty("url", article.getUrl());
            eventData.addProperty("queryKeyword", article.getQueryKeyword());

            Event event = new Event("news-feeder", "News", eventData);
            mqPublisher.publish(event);

        } catch (Exception e) {
            System.err.println("Error al publicar evento: " + e.getMessage());
        }
    }
}

