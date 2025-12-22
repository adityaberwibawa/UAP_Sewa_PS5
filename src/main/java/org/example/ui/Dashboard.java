package org.example.ui;

import org.example.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private ListPS5 listPs5Panel;
    private FormSewa formSewaPanel;
    private FormPengembalian formPengembalianPanel;
    private Laporan laporanPanel;

    public Dashboard() {
        setTitle("PS5 Rental System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(ThemeManager.getBackgroundColor());

        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ThemeManager.getBackgroundColor());

        // Home Panel
        JPanel homePanel = createHomePanel();
        contentPanel.add(homePanel, "HOME");

        // Initialize and add other panels
        listPs5Panel = new ListPS5(this);
        formSewaPanel = new FormSewa(this);
        formPengembalianPanel = new FormPengembalian(this);
        laporanPanel = new Laporan(this);

        contentPanel.add(listPs5Panel, "LIST_PS5");
        contentPanel.add(formSewaPanel, "FORM_SEWA");
        contentPanel.add(formPengembalianPanel, "FORM_PENGEMBALIAN");
        contentPanel.add(laporanPanel, "LAPORAN");

        setContentPane(contentPanel);
    }

    private JPanel createHomePanel() {
        JPanel mainPanel = ThemeManager.createStyledPanel(false);
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Header
        JLabel titleLabel = ThemeManager.createStyledLabel("ANJAY MABAR PS5 HOEE", 28, true);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        // Tombol navigasi
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String[] buttons = {"List PS5", "Sewa Baru", "Pengembalian", "Laporan"};
        String[] descriptions = {
                "Lihat dan kelola semua PS5 yang tersedia",
                "Buat transaksi penyewaan baru",
                "Proses pengembalian PS5 yang disewa",
                "Lihat laporan dan statistik transaksi"
        };

        for (int i = 0; i < buttons.length; i++) {
            JPanel btnContainer = new JPanel(new BorderLayout(5, 5));
            btnContainer.setBackground(ThemeManager.getBackgroundColor());

            JButton btn = ThemeManager.createStyledButton(buttons[i]);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setPreferredSize(new Dimension(200, 60));

            JLabel descLabel = ThemeManager.createStyledLabel(descriptions[i], 11, false);
            descLabel.setHorizontalAlignment(SwingConstants.CENTER);

            btnContainer.add(btn, BorderLayout.CENTER);
            btnContainer.add(descLabel, BorderLayout.SOUTH);

            btn.addActionListener(new NavListener(buttons[i]));
            buttonPanel.add(btnContainer);
        }

        // Footer
        JPanel footerPanel = ThemeManager.createStyledPanel(true);
        JLabel footerLabel = ThemeManager.createStyledLabel("© 2024 Sistem Sewa PS5 - Version 1.0", 12, false);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel);

        // Add to frame
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    public void showHome() {
        cardLayout.show(contentPanel, "HOME");
    }

    class NavListener implements ActionListener {
        private final String page;

        public NavListener(String page) {
            this.page = page;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (page) {
                case "List PS5":
                    listPs5Panel.refresh();
                    cardLayout.show(contentPanel, "LIST_PS5");
                    break;
                case "Sewa Baru":
                    formSewaPanel.refresh();
                    cardLayout.show(contentPanel, "FORM_SEWA");
                    break;
                case "Pengembalian":
                    formPengembalianPanel.refresh();
                    cardLayout.show(contentPanel, "FORM_PENGEMBALIAN");
                    break;
                case "Laporan":
                    cardLayout.show(contentPanel, "LAPORAN");
                    break;
            }
        }
    }
}