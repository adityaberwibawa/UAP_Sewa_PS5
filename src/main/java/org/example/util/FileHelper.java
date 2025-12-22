package org.example.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    // Baca semua baris dari file
    public static List<String> readAllLines(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("File tidak ditemukan: " + filename);
        }

        return lines;
    }

    // Tulis string ke file
    public static void writeToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Gagal menulis ke file: " + e.getMessage());
        }
    }

    // Tambah baris ke file
    public static void appendToFile(String filename, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Gagal menambah ke file: " + e.getMessage());
        }
    }

    // Update file dengan list baru
    public static void updateFile(String filename, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal update file: " + e.getMessage());
        }
    }
}