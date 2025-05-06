package com.datascience.eventstore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventStoreBuilder {
    public static void saveToFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Crea directorios si no existen

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(content);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("⛔ Error al guardar evento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}