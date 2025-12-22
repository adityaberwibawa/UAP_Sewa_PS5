package org.example.ui;
import org.example.model.*;
import org.example.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormSewa extends JPanel {
    private JTextField namaField, teleponField, durasiField;
    private JComboBox<String> ps5Combo;
    private JLabel totalLabel;
    private Dashboard dashboard;

    public FormSewa(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getBackgroundColor());

        initUI();
        loadAvailablePS5();
    }

    private void initUI() {
        JPanel mainPanel = ThemeManager.createStyledPanel(true);
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = ThemeManager.createStyledLabel("FORM PENYEWAAN PS5", 20, true);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title, gbc);

        // Nama Penyewa
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(ThemeManager.createStyledLabel("Nama Penyewa:", 12, false), gbc);
        gbc.gridx = 1;
        namaField = new JTextField(20);
        ThemeManager.styleTextField(namaField);
        mainPanel.add(namaField, gbc);

        // Telepon
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(ThemeManager.createStyledLabel("No. Telepon:", 12, false), gbc);
        gbc.gridx = 1;
        teleponField = new JTextField(15);
        ThemeManager.styleTextField(teleponField);
        mainPanel.add(teleponField, gbc);

        // Pilihan PS5
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(ThemeManager.createStyledLabel("Pilih PS5:", 12, false), gbc);
        gbc.gridx = 1;
        ps5Combo = new JComboBox<>();
        ThemeManager.styleComboBox(ps5Combo);
        ps5Combo.addActionListener(e -> calculateTotal());
        mainPanel.add(ps5Combo, gbc);

        // Durasi
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(ThemeManager.createStyledLabel("Durasi (hari):", 12, false), gbc);
        gbc.gridx = 1;
        durasiField = new JTextField("1");
        ThemeManager.styleTextField(durasiField);
        durasiField.addActionListener(e -> calculateTotal());
        mainPanel.add(durasiField, gbc);

        // Total Harga
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(ThemeManager.createStyledLabel("Total Harga:", 14, true), gbc);
        gbc.gridx = 1;
        totalLabel = ThemeManager.createStyledLabel("Rp 0", 16, true);
        totalLabel.setForeground(ThemeManager.getAccentColor());
        mainPanel.add(totalLabel, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());

        JButton hitungBtn = ThemeManager.createStyledButton("Hitung Total");
        JButton sewaBtn = ThemeManager.createStyledButton("Proses Sewa");
        JButton batalBtn = ThemeManager.createStyledButton("Batal");

        hitungBtn.addActionListener(e -> calculateTotal());
        sewaBtn.addActionListener(e -> processSewa());
        batalBtn.addActionListener(e -> dashboard.showHome());

        buttonPanel.add(hitungBtn);
        buttonPanel.add(sewaBtn);
        buttonPanel.add(batalBtn);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        loadAvailablePS5();
        namaField.setText("");
        teleponField.setText("");
        durasiField.setText("1");
        totalLabel.setText("Rp 0");
    }

    private void loadAvailablePS5() {
        ps5Combo.removeAllItems();
        List<String> lines = FileHelper.readAllLines("data/ps5.txt");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && ps5.getStatus().equals("Available")) {
                    ps5Combo.addItem(ps5.getNama() + " - Rp" + ps5.getHargaPerHari() + "/hari");
                }
            }
        }

        if (ps5Combo.getItemCount() == 0) {
            ps5Combo.addItem("Tidak ada PS5 tersedia");
            ps5Combo.setEnabled(false);
        } else {
            ps5Combo.setEnabled(true);
        }
    }

    private void calculateTotal() {
        try {
            String selected = (String) ps5Combo.getSelectedItem();
            if (selected == null || selected.contains("Tidak ada")) {
                totalLabel.setText("Rp 0");
                return;
            }

            // Extract harga dari string
            int hargaPerHari = 0;
            List<String> lines = FileHelper.readAllLines("data/ps5.txt");
            for (String line : lines) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && selected.contains(ps5.getNama())) {
                    hargaPerHari = ps5.getHargaPerHari();
                    break;
                }
            }

            int durasi = Integer.parseInt(durasiField.getText());
            int total = hargaPerHari * durasi;
            totalLabel.setText(String.format("Rp%,d", total));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Durasi harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processSewa() {
        try {
            // Validasi input
            if (namaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama penyewa harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (teleponField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Telepon harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selected = (String) ps5Combo.getSelectedItem();
            if (selected == null || selected.contains("Tidak ada")) {
                JOptionPane.showMessageDialog(this, "Pilih PS5 yang tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int durasi = Integer.parseInt(durasiField.getText());
            if (durasi <= 0) {
                JOptionPane.showMessageDialog(this, "Durasi minimal 1 hari!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ambil ID PS5 yang dipilih
            int ps5Id = -1;
            int hargaPerHari = 0;
            List<String> lines = FileHelper.readAllLines("data/ps5.txt");
            for (String line : lines) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && selected.contains(ps5.getNama())) {
                    ps5Id = ps5.getId();
                    hargaPerHari = ps5.getHargaPerHari();
                    break;
                }
            }

            if (ps5Id == -1) {
                JOptionPane.showMessageDialog(this, "PS5 tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate ID transaksi
            int newId = 1;
            List<String> rentalLines = FileHelper.readAllLines("data/sewa.txt");
            for (String line : rentalLines) {
                if (!line.trim().isEmpty()) {
                    Rental lastRental = Rental.fromCSV(line);
                    if (lastRental != null && lastRental.getId() >= newId) {
                        newId = lastRental.getId() + 1;
                    }
                }
            }

            // Hitung total
            double total = hargaPerHari * durasi;

            // Buat object Rental
            Rental rental = new Rental(newId, namaField.getText(), teleponField.getText(),
                    ps5Id, LocalDate.now(), durasi, total, "Active");

            // Simpan ke file
            FileHelper.appendToFile("data/sewa.txt", rental.toCSV());

            // Update status PS5 menjadi Rented
            updatePS5Status(ps5Id, "Rented");

            // Tampilkan nota
            showNota(rental, hargaPerHari);

            JOptionPane.showMessageDialog(this, "Sewa berhasil diproses!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refresh();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePS5Status(int ps5Id, String newStatus) {
        List<String> lines = FileHelper.readAllLines("data/ps5.txt");
        List<String> newLines = new ArrayList<>();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && ps5.getId() == ps5Id) {
                    ps5.setStatus(newStatus);
                    newLines.add(ps5.toCSV());
                } else {
                    newLines.add(line);
                }
            }
        }

        FileHelper.updateFile("data/ps5.txt", newLines);
    }

    private void showNota(Rental rental, int hargaPerHari) {
        String nota = String.format(
                "================================\n" +
                        "        NOTA PENYEWAAN PS5      \n" +
                        "================================\n" +
                        "No. Transaksi : %d\n" +
                        "Tanggal       : %s\n" +
                        "Nama Penyewa  : %s\n" +
                        "Telepon       : %s\n" +
                        "PS5 ID        : %d\n" +
                        "Durasi        : %d hari\n" +
                        "Harga/Hari    : Rp%,d\n" +
                        "--------------------------------\n" +
                        "TOTAL         : Rp%,.0f\n" +
                        "================================\n" +
                        "Terima kasih telah menyewa!",
                rental.getId(),
                rental.getTanggalSewa(),
                rental.getNamaPenyewa(),
                rental.getTelepon(),
                rental.getPs5Id(),
                rental.getDurasiHari(),
                hargaPerHari,
                rental.getTotalHarga()
        );

        JTextArea textArea = new JTextArea(nota);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setForeground(ThemeManager.getTableTextColor());
        textArea.setBackground(ThemeManager.getPanelColor());
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Nota Sewa", JOptionPane.INFORMATION_MESSAGE);
    }
}