package services;

import models.NewsArticle;
import java.sql.*;

public class NewsRepository {
    private final String dbPath;

    public NewsRepository(String dbPath) {
        this.dbPath = dbPath;
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS news_articles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL UNIQUE," +
                "source TEXT," +
                "published_at DATETIME," +
                "url TEXT UNIQUE," +
                "query_keyword TEXT," +
                "last_updated DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }

    public boolean isNew(NewsArticle article) {
        String sql = "SELECT id FROM news_articles WHERE title = ? OR url = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getUrl());
            return !pstmt.executeQuery().next();  // True si no existe
        } catch (SQLException e) {
            System.err.println("Error al verificar noticia: " + e.getMessage());
            return false;
        }
    }

    public void save(NewsArticle article) {
        String sql = "INSERT INTO news_articles (title, source, published_at, url, query_keyword) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getSource());
            pstmt.setString(3, article.getPublishedAt());
            pstmt.setString(4, article.getUrl());
            pstmt.setString(5, article.getQueryKeyword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar noticia: " + e.getMessage());
        }
    }
}