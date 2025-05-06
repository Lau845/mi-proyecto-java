package com.miempresa.businessunit.storage;

import com.miempresa.businessunit.event.NewsEvent;
import com.miempresa.businessunit.event.VideoEvent;

import java.util.*;

public class DataMart {
    private final List<NewsEvent> newsEvents = new ArrayList<>();
    private final List<VideoEvent> videoEvents = new ArrayList<>();

    public void addEvent(Object event) {
        if (event instanceof NewsEvent) {
            newsEvents.add((NewsEvent) event);
        } else if (event instanceof VideoEvent) {
            videoEvents.add((VideoEvent) event);
        }
    }

    public List<NewsEvent> getNewsEvents() {
        return newsEvents;
    }

    public List<VideoEvent> getVideoEvents() {
        return videoEvents;
    }
}
