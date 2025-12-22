package org.example.ui;

import org.example.util.*;
import org.example.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FormPengembalian extends JPanel {
    private JComboBox<String> sewaCombo;
    private JTextArea detailArea;
    private Dashboard dashboard;

    public FormPengembalian(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getBackgroundColor());

        initUI();
        loadActiveRentals();
    }

    private void initUI() {
        JPanel mainPanel = ThemeManager.createStyledPanel(true);
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getBackgroundColor());

        JLabel titleLabel = ThemeManager.createStyledLabel("🔄 FORM PENGEMBALIAN PS5", 20, true);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Refresh button
        JButton refreshBtn = ThemeManager.createStyledButton("Refresh Data");
        refreshBtn.addActionListener(e -> loadActiveRentals());
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ThemeManager.getBackgroundColor());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pilih Sewa
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(ThemeManager.createStyledLabel("Pilih Transaksi Sewa:", 14, true), gbc);

        gbc.gridx = 1;
        sewaCombo = new JComboBox<>();
        ThemeManager.styleComboBox(sewaCombo);
        sewaCombo.addActionListener(e -> showRentalDetails());
        contentPanel.add(sewaCombo, gbc);

        // Detail Panel
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel detailTitle = ThemeManager.createStyledLabel("Detail Penyewaan:", 14, true);
        detailTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        contentPanel.add(detailTitle, gbc);

        gbc.gridy = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        detailArea = new JTextArea(8, 40);
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailArea.setForeground(ThemeManager.getTableTextColor());
        detailArea.setBackground(ThemeManager.getPanelColor());
        detailArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.getViewport().setBackground(ThemeManager.getPanelColor());
        contentPanel.add(detailScroll, gbc);

        // Button Panel
        gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0; gbc.weighty = 0;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());

        JButton prosesBtn = ThemeManager.createStyledButton("🔄 Proses Pengembalian");
        prosesBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        prosesBtn.addActionListener(e -> processPengembalian());

        JButton batalBtn = ThemeManager.createStyledButton("Kembali ke Dashboard");
        batalBtn.addActionListener(e -> dashboard.showHome());

        buttonPanel.add(prosesBtn);
        buttonPanel.add(batalBtn);
        contentPanel.add(buttonPanel, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        loadActiveRentals();
        detailArea.setText("");
    }

    private void loadActiveRentals() {
        sewaCombo.removeAllItems();
        detailArea.setText("");

        List<String> rentalLines = FileHelper.readAllLines("data/sewa.txt");
        List<String> ps5Lines = FileHelper.readAllLines("data/ps5.txt");

        boolean foundActive = false;

        for (String rentalLine : rentalLines) {
            if (!rentalLine.trim().isEmpty()) {
                Rental rental = Rental.fromCSV(rentalLine);
                if (rental != null && "Active".equalsIgnoreCase(rental.getStatus())) {
                    foundActive = true;
                    PS5 ps5 = findPS5ById(ps5Lines, rental.getPs5Id());
                    String ps5Name = (ps5 != null) ? ps5.getNama() : "N/A";
                    sewaCombo.addItem(String.format("ID: %d - %s (%s)",
                            rental.getId(), rental.getNamaPenyewa(), ps5Name));
                }
            }
        }

        if (!foundActive) {
            sewaCombo.addItem("Tidak ada penyewaan aktif");
            sewaCombo.setEnabled(false);
        } else {
            sewaCombo.setEnabled(true);
            sewaCombo.setSelectedIndex(0);
            showRentalDetails();
        }
    }

    private void showRentalDetails() {
        String selected = (String) sewaCombo.getSelectedItem();
        if (selected == null || selected.contains("Tidak ada")) {
            detailArea.setText("");
            return;
        }

        try {
            int rentalId = Integer.parseInt(selected.split(" ")[1]);

            List<String> rentalLines = FileHelper.readAllLines("data/sewa.txt");
            List<String> ps5Lines = FileHelper.readAllLines("data/ps5.txt");

            for (String rentalLine : rentalLines) {
                if (!rentalLine.trim().isEmpty()) {
                    Rental rental = Rental.fromCSV(rentalLine);
                    if (rental != null && rental.getId() == rentalId) {
                        PS5 ps5 = findPS5ById(ps5Lines, rental.getPs5Id());

                        String detailText = String.format(
                                "═══════════════════════════════════\n" +
                                        "       DETAIL PENYEWAAN\n" +
                                        "═══════════════════════════════════\n\n" +
                                        "ID Transaksi    : %d\n" +
                                        "Nama Penyewa    : %s\n" +
                                        "Telepon         : %s\n" +
                                        "Tanggal Sewa    : %s\n" +
                                        "Durasi          : %d hari\n" +
                                        "Status          : %s\n\n" +
                                        "═══════════════════════════════════\n" +
                                        "        DETAIL PS5\n" +
                                        "═══════════════════════════════════\n\n" +
                                        "ID PS5          : %d\n" +
                                        "Nama PS5        : %s\n" +
                                        "Tipe            : %s\n" +
                                        "Harga/Hari      : Rp%,d\n" +
                                        "Kondisi         : %s\n\n" +
                                        "═══════════════════════════════════\n" +
                                        "Total Biaya     : Rp%,.0f\n" +
                                        "═══════════════════════════════════",
                                rental.getId(),
                                rental.getNamaPenyewa(),
                                rental.getTelepon(),
                                rental.getTanggalSewa(),
                                rental.getDurasiHari(),
                                rental.getStatus(),
                                rental.getPs5Id(),
                                ps5 != null ? ps5.getNama() : "Tidak Diketahui",
                                ps5 != null ? ps5.getTipe() : "Tidak Diketahui",
                                ps5 != null ? ps5.getHargaPerHari() : 0,
                                ps5 != null ? ps5.getKondisi() : "Tidak Diketahui",
                                rental.getTotalHarga()
                        );

                        detailArea.setText(detailText);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            detailArea.setText("Gagal memuat detail penyewaan");
        }
    }

    private PS5 findPS5ById(List<String> ps5Lines, int id) {
        for (String line : ps5Lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && ps5.getId() == id) {
                    return ps5;
                }
            }
        }
        return null;
    }

    private void processPengembalian() {
        String selected = (String) sewaCombo.getSelectedItem();
        if (selected == null || selected.contains("Tidak ada")) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi yang valid!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Extract rental ID
            int rentalId = Integer.parseInt(selected.split(" ")[1]);

            // Konfirmasi
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin memproses pengembalian ini?\n" +
                            "Transaksi akan ditandai sebagai selesai dan PS5 akan tersedia kembali.",
                    "Konfirmasi Pengembalian",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            //  Update Rental Status 
            List<String> rentalLines = FileHelper.readAllLines("data/sewa.txt");
            List<String> newRentalLines = new ArrayList<>();
            int ps5IdToUpdate = -1;
            Rental selectedRental = null;

            for (String line : rentalLines) {
                if (!line.trim().isEmpty()) {
                    Rental rental = Rental.fromCSV(line);
                    if (rental != null) {
                        if (rental.getId() == rentalId) {
                            rental.setStatus("Completed");
                            ps5IdToUpdate = rental.getPs5Id();
                            selectedRental = rental;
                        }
                        newRentalLines.add(rental.toCSV());
                    }
                }
            }
            FileHelper.updateFile("data/sewa.txt", newRentalLines);

            // Update PS5 Status
            if (ps5IdToUpdate != -1) {
                List<String> ps5Lines = FileHelper.readAllLines("data/ps5.txt");
                List<String> newPs5Lines = new ArrayList<>();
                PS5 updatedPS5 = null;

                for (String line : ps5Lines) {
                    if (!line.trim().isEmpty()) {
                        PS5 ps5 = PS5.fromCSV(line);
                        if (ps5 != null) {
                            if (ps5.getId() == ps5IdToUpdate) {
                                ps5.setStatus("Available");
                                updatedPS5 = ps5;
                            }
                            newPs5Lines.add(ps5.toCSV());
                        }
                    }
                }
                FileHelper.updateFile("data/ps5.txt", newPs5Lines);

                // Tampilkan konfirmasi sukses
                if (selectedRental != null && updatedPS5 != null) {
                    String successMessage = String.format(
                            "✅ PENGEMBALIAN BERHASIL\n\n" +
                                    "Transaksi ID: %d\n" +
                                    "Penyewa: %s\n" +
                                    "PS5: %s (%s)\n" +
                                    "Durasi Sewa: %d hari\n" +
                                    "Total: Rp%,.0f\n\n" +
                                    "PS5 sekarang tersedia untuk disewa kembali.",
                            selectedRental.getId(),
                            selectedRental.getNamaPenyewa(),
                            updatedPS5.getNama(),
                            updatedPS5.getTipe(),
                            selectedRental.getDurasiHari(),
                            selectedRental.getTotalHarga()
                    );

                    JOptionPane.showMessageDialog(this,
                            successMessage,
                            "Pengembalian Berhasil",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            // Refresh data
            loadActiveRentals();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}