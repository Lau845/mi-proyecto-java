package com.miempresa.businessunit.ui;

import com.miempresa.businessunit.event.NewsEvent;
import com.miempresa.businessunit.event.VideoEvent;
import com.miempresa.businessunit.storage.DataMart;

import java.util.List;
import java.util.Scanner;

public class CLI {
    private final DataMart dataMart;

    public CLI(DataMart dataMart) {
        this.dataMart = dataMart;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("\n--- BUSINESS UNIT ---");
            System.out.println("1. Ver eventos de noticias");
            System.out.println("2. Ver eventos de videos");
            System.out.println("0. Salir");
            System.out.print("> ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1 -> showNews();
                case 2 -> showVideos();
            }
        } while (option != 0);
    }

    private void showNews() {
        List<NewsEvent> news = dataMart.getNewsEvents();
        System.out.println("\n[Noticias]");
        news.forEach(n -> System.out.println("- " + n.date + " | " + n.title + " | " + n.location));
    }

    private void showVideos() {
        List<VideoEvent> videos = dataMart.getVideoEvents();
        System.out.println("\n[Videos]");
        videos.forEach(v -> System.out.println("- " + v.date + " | " + v.title + " | Canal: " + v.channel));
    }
}