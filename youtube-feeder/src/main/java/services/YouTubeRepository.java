package services;

import models.YouTubeVideo;
import java.sql.*;

public class YouTubeRepository {
    private final String dbPath;

    public YouTubeRepository(String dbPath) {
        this.dbPath = dbPath;
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS youtube_videos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "video_id TEXT UNIQUE," +  // ID único de YouTube
                "title TEXT," +
                "channel TEXT," +
                "published_at DATETIME," +
                "news_article_id INTEGER," + // Clave foránea
                "FOREIGN KEY (news_article_id) REFERENCES news_articles(id))";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear tabla de videos: " + e.getMessage());
        }
    }

    public void saveVideo(YouTubeVideo video) {
        String sql = "INSERT OR IGNORE INTO youtube_videos " +
                "(video_id, title, channel, published_at, news_article_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, video.getVideoId());
            pstmt.setString(2, video.getTitle());
            pstmt.setString(3, video.getChannel());
            pstmt.setString(4, video.getPublishedAt());
            pstmt.setObject(5, video.getNewsArticleId(), Types.INTEGER);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar video: " + e.getMessage());
        }
    }
}