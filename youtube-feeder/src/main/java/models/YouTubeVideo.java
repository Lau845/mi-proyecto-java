package models;

import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class YouTubeVideo {
    private JsonObject videoData;

    public JsonObject getVideoData() {
        return videoData;
    }
    private String videoId;
    private String title;
    private String channel;
    private String publishedAt;
    private Integer newsArticleId;  // Clave foránea opcional

    // Constructor
    public YouTubeVideo(String videoId, String title, String channel, String publishedAt) {
        this.videoId = videoId;
        this.title = title;
        this.channel = channel;
        this.publishedAt = publishedAt;
    }

    // Getters
    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public Integer getNewsArticleId() {
        return newsArticleId;
    }

    // Setter para la clave foránea
    public void setNewsArticleId(Integer newsArticleId) {
        this.newsArticleId = newsArticleId;
    }

    // Opcional: Formatear fecha para SQLite
    public LocalDateTime getParsedPublishedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return LocalDateTime.parse(publishedAt, formatter);
    }

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}