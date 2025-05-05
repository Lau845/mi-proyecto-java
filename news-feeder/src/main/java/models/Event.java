package models;

import com.google.gson.JsonObject;
import java.time.Instant;

public class Event {
    private String ts;  // Timestamp en UTC
    private String ss;  // Fuente (ej: "news-feeder")
    private String topic;
    private JsonObject data;

    public Event(String ss, String topic, JsonObject data) {
        this.ts = Instant.now().toString();
        this.ss = ss;
        this.topic = topic;
        this.data = data;
    }

    // Getters
    public String getTs() { return ts; }
    public String getSs() { return ss; }
    public String getTopic() { return topic; }
    public JsonObject getData() { return data; }
}