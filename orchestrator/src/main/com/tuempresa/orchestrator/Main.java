package com.tuempresa.orchestrator;
import services.NewsApiService;
// —— IMPORT CORRECTO PARA YOUTUBE‑Feeder ——
import services.YoutubeApiService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication  // sólo si quieres usar Spring para inyección
public class Main {
    public static void main(String[] args) {
        // Arrancamos el contexto Spring (para inyectar beans de tus feeders)
        ConfigurableApplicationContext ctx =
                SpringApplication.run(Main.class, args);

        // Pedimos el tema por consola
        System.out.print("Introduce un tema: ");
        Scanner sc = new Scanner(System.in);
        String topic = sc.nextLine().trim();

        // Recuperamos los beans de cada módulo
        NewsService news = ctx.getBean(NewsService.class);
        YoutubeService yt = ctx.getBean(YoutubeService.class);

        // Disparamos la búsqueda y publicación
        System.out.println("→ Buscando noticias de “" + topic + "”…");
        news.fetchAndPublish(topic);

        System.out.println("→ Buscando vídeos de “" + topic + "”…");
        yt.fetchAndPublish(topic);

        // Cerramos
        ctx.close();
    }
}
