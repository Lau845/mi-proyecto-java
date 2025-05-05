import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    public static Connection connect() {
        Connection conn = null;
        try {
            // Ruta relativa desde news-feeder o youtube-feeder
            String url = "jdbc:sqlite:../shared-db/data/proyecto.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a SQLite.");
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return conn;
    }
}