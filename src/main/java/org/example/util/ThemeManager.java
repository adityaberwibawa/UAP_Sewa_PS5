package org.example.util;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public class ThemeManager {

    // THEME DEFINITIONS
    public enum Theme {
        DARK_TEAL,
        LIGHT_BLUE,
        DARK_PURPLE,
        GREEN_NATURE
    }

    //  CURRENT THEME COLORS 
    private static Color primaryColor;
    private static Color backgroundColor;
    private static Color panelColor;
    private static Color labelTextColor;      // Untuk label, judul
    private static Color buttonTextColor;     // Untuk teks di button
    private static Color tableTextColor;      // Untuk teks di tabel
    private static Color accentColor;
    private static Color borderColor;

    //  THEME PRESETS 
    private static final Color[][] THEME_PRESETS = {
            // DARK_TEAL
            {
                    new Color(158, 200, 185),    // primaryColor (hijau muda)
                    new Color(9, 38, 53),        // backgroundColor (biru tua)
                    new Color(27, 66, 66),       // panelColor (hijau tua)
                    new Color(200, 230, 220),    // labelTextColor (hijau sangat muda)
                    Color.WHITE,                 // buttonTextColor (putih)
                    Color.WHITE,                 // tableTextColor (putih)
                    new Color(92, 131, 116),     // accentColor (hijau medium)
                    new Color(60, 100, 90)       // borderColor
            },
            // LIGHT_BLUE
            {
                    new Color(70, 130, 180),     // primaryColor (biru)
                    new Color(240, 248, 255),    // backgroundColor (biru sangat muda)
                    Color.WHITE,                 // panelColor (putih)
                    new Color(25, 25, 112),      // labelTextColor (biru tua)
                    Color.WHITE,                 // buttonTextColor (putih)
                    Color.BLACK,                 // tableTextColor (hitam)
                    new Color(65, 105, 225),     // accentColor (biru royal)
                    new Color(200, 220, 240)     // borderColor
            },
            // DARK_PURPLE
            {
                    new Color(180, 160, 220),    // primaryColor (ungu muda)
                    new Color(30, 30, 45),       // backgroundColor (ungu tua gelap)
                    new Color(50, 45, 65),       // panelColor (ungu gelap)
                    new Color(220, 210, 240),    // labelTextColor (ungu sangat muda)
                    Color.WHITE,                 // buttonTextColor (putih)
                    Color.WHITE,                 // tableTextColor (putih)
                    new Color(120, 90, 180),     // accentColor (ungu medium)
                    new Color(80, 60, 100)       // borderColor
            },
            // GREEN_NATURE
            {
                    new Color(120, 180, 120),    // primaryColor (hijau)
                    new Color(240, 250, 240),    // backgroundColor (hijau sangat muda)
                    Color.WHITE,                 // panelColor (putih)
                    new Color(30, 80, 30),       // labelTextColor (hijau tua)
                    Color.WHITE,                 // buttonTextColor (putih)
                    Color.BLACK,                 // tableTextColor (hitam)
                    new Color(80, 160, 80),      // accentColor (hijau medium)
                    new Color(200, 220, 200)     // borderColor
            }
    };

    //  INITIALIZATION 
    static {
        // Set tema default
        setTheme(Theme.DARK_TEAL);
    }

    //  PUBLIC METHODS 

    public static void setTheme(Theme theme) {
        int index = theme.ordinal();
        Color[] colors = THEME_PRESETS[index];

        primaryColor = colors[0];
        backgroundColor = colors[1];
        panelColor = colors[2];
        labelTextColor = colors[3];
        buttonTextColor = colors[4];
        tableTextColor = colors[5];
        accentColor = colors[6];
        borderColor = colors[7];

        applyThemeToUIManager();
    }

    public static void setCustomTheme(Color[] customColors) {
        if (customColors.length >= 8) {
            primaryColor = customColors[0];
            backgroundColor = customColors[1];
            panelColor = customColors[2];
            labelTextColor = customColors[3];
            buttonTextColor = customColors[4];
            tableTextColor = customColors[5];
            accentColor = customColors[6];
            borderColor = customColors[7];

            applyThemeToUIManager();
        }
    }

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
    }

    //  GETTER METHODS 
    public static Color getPrimaryColor() { return primaryColor; }
    public static Color getBackgroundColor() { return backgroundColor; }
    public static Color getPanelColor() { return panelColor; }
    public static Color getLabelTextColor() { return labelTextColor; }
    public static Color getButtonTextColor() { return buttonTextColor; }
    public static Color getTableTextColor() { return tableTextColor; }
    public static Color getAccentColor() { return accentColor; }
    public static Color getBorderColor() { return borderColor; }

    //  HELPER METHODS FOR COMPONENTS 

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        applyButtonStyle(button);
        return button;
    }

    public static void applyButtonStyle(JButton button) {
        button.setForeground(buttonTextColor);
        button.setBackground(accentColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public static JLabel createStyledLabel(String text, int fontSize, boolean isTitle) {
        JLabel label = new JLabel(text);
        label.setForeground(labelTextColor);
        label.setFont(new Font("Segoe UI", isTitle ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    public static JPanel createStyledPanel(boolean withBorder) {
        JPanel panel = new JPanel();
        panel.setBackground(panelColor);
        if (withBorder) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        } else {
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
        return panel;
    }

    public static void styleTable(JTable table) {
        table.setForeground(tableTextColor);
        table.setBackground(panelColor);
        table.setGridColor(borderColor);
        table.setSelectionBackground(accentColor);
        table.setSelectionForeground(buttonTextColor);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
    }

    public static void styleTextField(JTextField field) {
        field.setForeground(tableTextColor);
        field.setBackground(panelColor.darker());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setForeground(tableTextColor);
        combo.setBackground(panelColor.darker());
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    //  PRIVATE METHOD TO APPLY THEME 
    private static void applyThemeToUIManager() {
        // Basic Nimbus colors
        UIManager.put("nimbusBase", panelColor.darker());
        UIManager.put("control", backgroundColor);
        UIManager.put("nimbusLightBackground", backgroundColor);
        UIManager.put("nimbusSelectionBackground", accentColor);

        // Text colors
        UIManager.put("textForeground", tableTextColor);

        // Button
        UIManager.put("Button.foreground", buttonTextColor);
        UIManager.put("Button.background", accentColor);
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        // Label and text components
        UIManager.put("Label.foreground", labelTextColor);
        UIManager.put("CheckBox.foreground", labelTextColor);
        UIManager.put("RadioButton.foreground", labelTextColor);
        UIManager.put("ToggleButton.foreground", labelTextColor);

        // Text fields
        UIManager.put("TextField.foreground", tableTextColor);
        UIManager.put("TextField.background", panelColor.darker());
        UIManager.put("TextField.selectionBackground", accentColor);
        UIManager.put("TextField.selectionForeground", buttonTextColor);

        // ComboBox
        UIManager.put("ComboBox.foreground", tableTextColor);
        UIManager.put("ComboBox.background", panelColor.darker());

        // TextArea
        UIManager.put("TextArea.foreground", tableTextColor);
        UIManager.put("TextArea.background", panelColor.darker());

        // Table
        UIManager.put("Table.foreground", tableTextColor);
        UIManager.put("Table.background", panelColor);
        UIManager.put("Table.gridColor", borderColor);
        UIManager.put("Table.selectionBackground", accentColor);
        UIManager.put("Table.selectionForeground", buttonTextColor);
        UIManager.put("TableHeader.foreground", buttonTextColor);
        UIManager.put("TableHeader.background", accentColor);

        // ScrollPane
        UIManager.put("ScrollPane.foreground", tableTextColor);
        UIManager.put("ScrollPane.background", backgroundColor);
        UIManager.put("ScrollBar.thumb", accentColor);
        UIManager.put("ScrollBar.track", backgroundColor);

        // Panel
        UIManager.put("Panel.foreground", labelTextColor);
        UIManager.put("Panel.background", backgroundColor);

        // OptionPane
        UIManager.put("OptionPane.foreground", labelTextColor);
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", labelTextColor);

        // InternalFrame
        UIManager.put("InternalFrame.foreground", labelTextColor);
        UIManager.put("InternalFrame.background", backgroundColor);

        // Menu
        UIManager.put("Menu.foreground", labelTextColor);
        UIManager.put("Menu.background", backgroundColor);
        UIManager.put("MenuBar.background", backgroundColor);

        // ProgressBar
        UIManager.put("ProgressBar.foreground", accentColor);
        UIManager.put("ProgressBar.background", panelColor);
    }
}