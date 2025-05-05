import config.Config;
import models.NewsArticle;
import services.NewsAPIService;
import services.NewsRepository;

import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Config config = new Config(args);
        NewsAPIService apiService = new NewsAPIService(config.getApiKey(), config.getQueryKeyword());
        NewsRepository repository = new NewsRepository(config.getDbPath());

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<NewsArticle> articles = apiService.fetchLatestNews();
                articles.forEach(article -> {
                    if (repository.isNew(article)) {
                        repository.save(article);
                        System.out.println("Guardada noticia: " + article.getTitle());
                    }
                });
            } catch (Exception e) {
                System.err.println("Error en la consulta: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.HOURS);
    }
}