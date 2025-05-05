package com.dacd.youtube;
import config.Config;
import services.YouTubeAPIService;
import services.YouTubeRepository;
import java.util.List;
import java.util.concurrent.*;
import models.YouTubeVideo;

public class Main {
    public static void main(String[] args) {
        Config config = new Config(args);
        YouTubeAPIService apiService = new YouTubeAPIService(config.getApiKey());
        YouTubeRepository repository = new YouTubeRepository(config.getDbPath());

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Ejemplo: Buscar videos relacionados con "eventos culturales"
                List<YouTubeVideo> videos = apiService.searchVideos("eventos culturales");
                videos.forEach(video -> {
                    repository.saveVideo(video);
                    System.out.println("Guardado video: " + video.getTitle());
                });
            } catch (Exception e) {
                System.err.println("Error en YouTube Feeder: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.HOURS); // Consulta cada hora
    }
}