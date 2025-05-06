package com.miempresa.businessunit.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miempresa.businessunit.event.NewsEvent;
import com.miempresa.businessunit.event.VideoEvent;

import java.io.*;
import java.nio.file.*;

public class EventLoader {
    private final DataMart dataMart;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventLoader(DataMart dataMart) {
        this.dataMart = dataMart;
    }

    public void loadEvents(String pathStr) {
        try {
            Files.walk(Paths.get(pathStr))
                    .filter(p -> p.toString().endsWith(".events"))
                    .forEach(this::readFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (path.toString().contains("News")) {
                    NewsEvent event = mapper.readValue(line, NewsEvent.class);
                    dataMart.addEvent(event);
                } else if (path.toString().contains("Videos")) {
                    VideoEvent event = mapper.readValue(line, VideoEvent.class);
                    dataMart.addEvent(event);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}