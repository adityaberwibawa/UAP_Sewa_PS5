package org.example;
import org.example.ui.*;
import org.example.util.*;
import javax.swing.*;

public class MainApp {

    public static void main(String[] args) {
        ThemeManager.applyLookAndFeel();
        ThemeManager.setTheme(ThemeManager.Theme.DARK_TEAL); // Default theme

        // Buat folder data jika belum ada
        java.io.File dataDir = new java.io.File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Load data sample jika file kosong
        if (new java.io.File("data/ps5.txt").length() == 0) {
            createSampleData();
        }

        // Tampilkan dashboard
        SwingUtilities.invokeLater(() -> {
            try {
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void createSampleData() {
        String[] ps5Data = {
                "1,PS5-001,Standard,15000,Available,Excellent",
                "2,PS5-002,Digital,12000,Available,Good",
                "3,PS5-003,Standard,15000,Rented,Good",
                "4,PS5-004,Digital,12000,Available,Excellent",
                "5,PS5-005,Standard,15000,Available,Fair"
        };

        FileHelper.writeToFile("data/ps5.txt", String.join("\n", ps5Data));
        FileHelper.writeToFile("data/sewa.txt", ""); // File kosong
    }
}