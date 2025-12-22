package org.example.ui;

import org.example.util.*;
import org.example.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListPS5 extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private Dashboard dashboard;

    public ListPS5(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getBackgroundColor());

        initUI();
        loadData();
    }

    private void initUI() {

        JPanel mainPanel = ThemeManager.createStyledPanel(true);
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getBackgroundColor());

        JLabel titleLabel = ThemeManager.createStyledLabel("📋 DAFTAR PS5", 20, true);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(ThemeManager.getBackgroundColor());

        searchPanel.add(ThemeManager.createStyledLabel("Cari:", 12, false));
        searchField = new JTextField(15);
        ThemeManager.styleTextField(searchField);
        searchPanel.add(searchField);

        JButton searchBtn = ThemeManager.createStyledButton("Cari");
        searchBtn.addActionListener(e -> searchData());
        searchPanel.add(searchBtn);

        headerPanel.add(searchPanel, BorderLayout.EAST);

        // Tombol aksi di atas
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topButtonPanel.setBackground(ThemeManager.getBackgroundColor());

        String[] topButtons = {"Tambah PS5", "Edit PS5", "Hapus PS5", "Sort by Harga"};
        for (String text : topButtons) {
            JButton btn = ThemeManager.createStyledButton(text);
            btn.addActionListener(e -> {
                switch (text) {
                    case "Tambah PS5": showAddDialog(); break;
                    case "Edit PS5": showEditDialog(); break;
                    case "Hapus PS5": deletePS5(); break;
                    case "Sort by Harga": sortByPrice(); break;
                }
            });
            topButtonPanel.add(btn);
        }

        // Tabel
        String[] columns = {"ID", "Nama", "Tipe", "Harga/Hari", "Status", "Kondisi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        table = new JTable(tableModel);
        ThemeManager.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(ThemeManager.getPanelColor());

        // Tombol bawah
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(ThemeManager.getBackgroundColor());

        JButton refreshBtn = ThemeManager.createStyledButton("Refresh Data");
        JButton backBtn = ThemeManager.createStyledButton("Kembali ke Dashboard");

        refreshBtn.addActionListener(e -> loadData());
        backBtn.addActionListener(e -> dashboard.showHome());

        bottomPanel.add(refreshBtn);
        bottomPanel.add(backBtn);

        // Add to panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(topButtonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear table

        List<String> lines = FileHelper.readAllLines("data/ps5.txt");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null) {
                    Object[] row = {
                            ps5.getId(),
                            ps5.getNama(),
                            ps5.getTipe(),
                            String.format("Rp%,d", ps5.getHargaPerHari()),
                            getStatusWithIcon(ps5.getStatus()),
                            getConditionWithColor(ps5.getKondisi())
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    private String getStatusWithIcon(String status) {
        switch (status) {
            case "Available": return "✅ Tersedia";
            case "Rented": return "🟡 Disewa";
            default: return "❓ Tidak Diketahui";
        }
    }

    private String getConditionWithColor(String kondisi) {
        switch (kondisi) {
            case "Excellent": return "🟢 " + kondisi;
            case "Good": return "🟡 " + kondisi;
            case "Fair": return "🟠 " + kondisi;
            case "Poor": return "🔴 " + kondisi;
            default: return kondisi;
        }
    }

    private void searchData() {
        String keyword = searchField.getText().toLowerCase();

        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<String> lines = FileHelper.readAllLines("data/ps5.txt");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && ps5.getNama().toLowerCase().contains(keyword)) {
                    Object[] row = {
                            ps5.getId(),
                            ps5.getNama(),
                            ps5.getTipe(),
                            String.format("Rp%,d", ps5.getHargaPerHari()),
                            getStatusWithIcon(ps5.getStatus()),
                            getConditionWithColor(ps5.getKondisi())
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    private void sortByPrice() {
        // Ambil data dari file
        List<PS5> ps5List = new ArrayList<>();
        List<String> lines = FileHelper.readAllLines("data/ps5.txt");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null) {
                    ps5List.add(ps5);
                }
            }
        }

        // Sort dengan Comparator
        ps5List.sort((p1, p2) -> Integer.compare(p1.getHargaPerHari(), p2.getHargaPerHari()));

        // Update tabel
        tableModel.setRowCount(0);
        for (PS5 ps5 : ps5List) {
            Object[] row = {
                    ps5.getId(),
                    ps5.getNama(),
                    ps5.getTipe(),
                    String.format("Rp%,d", ps5.getHargaPerHari()),
                    getStatusWithIcon(ps5.getStatus()),
                    getConditionWithColor(ps5.getKondisi())
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JPanel dialogPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        dialogPanel.setBackground(ThemeManager.getBackgroundColor());

        JTextField namaField = new JTextField();
        String[] types = {"Standard", "Digital"};
        JComboBox<String> tipeCombo = new JComboBox<>(types);
        JTextField hargaField = new JTextField();
        String[] conditions = {"Excellent", "Good", "Fair", "Poor"};
        JComboBox<String> kondisiCombo = new JComboBox<>(conditions);

        ThemeManager.styleTextField(namaField);
        ThemeManager.styleComboBox(tipeCombo);
        ThemeManager.styleTextField(hargaField);
        ThemeManager.styleComboBox(kondisiCombo);

        dialogPanel.add(ThemeManager.createStyledLabel("Nama PS5:", 12, false));
        dialogPanel.add(namaField);
        dialogPanel.add(ThemeManager.createStyledLabel("Tipe:", 12, false));
        dialogPanel.add(tipeCombo);
        dialogPanel.add(ThemeManager.createStyledLabel("Harga/Hari:", 12, false));
        dialogPanel.add(hargaField);
        dialogPanel.add(ThemeManager.createStyledLabel("Kondisi:", 12, false));
        dialogPanel.add(kondisiCombo);

        int option = JOptionPane.showConfirmDialog(this, dialogPanel,
                "Tambah PS5 Baru", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nama = namaField.getText().trim();
                String tipe = (String) tipeCombo.getSelectedItem();
                int harga = Integer.parseInt(hargaField.getText().trim());
                String kondisi = (String) kondisiCombo.getSelectedItem();

                if (nama.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (harga <= 0) {
                    JOptionPane.showMessageDialog(this, "Harga harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate ID
                int newId = 1;
                List<String> lines = FileHelper.readAllLines("data/ps5.txt");
                if (!lines.isEmpty()) {
                    for (String line : lines) {
                        if (!line.trim().isEmpty()) {
                            PS5 ps5 = PS5.fromCSV(line);
                            if (ps5 != null && ps5.getId() >= newId) {
                                newId = ps5.getId() + 1;
                            }
                        }
                    }
                }

                PS5 newPS5 = new PS5(newId, nama, tipe, harga, "Available", kondisi);
                FileHelper.appendToFile("data/ps5.txt", newPS5.toCSV());
                loadData();
                JOptionPane.showMessageDialog(this, "PS5 berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih PS5 yang akan diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);

        // Find the PS5 object
        PS5 selectedPS5 = null;
        List<String> lines = FileHelper.readAllLines("data/ps5.txt");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                PS5 ps5 = PS5.fromCSV(line);
                if (ps5 != null && ps5.getId() == id) {
                    selectedPS5 = ps5;
                    break;
                }
            }
        }

        if (selectedPS5 == null) {
            JOptionPane.showMessageDialog(this, "PS5 tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentKondisi = selectedPS5.getKondisi();
        String currentStatus = selectedPS5.getStatus();

        String[] conditions = {"Excellent", "Good", "Fair", "Poor"};
        JComboBox<String> kondisiCombo = new JComboBox<>(conditions);
        kondisiCombo.setSelectedItem(currentKondisi);

        String[] statuses = {"Available", "Rented"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(currentStatus);

        ThemeManager.styleComboBox(kondisiCombo);
        ThemeManager.styleComboBox(statusCombo);

        JPanel dialogPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        dialogPanel.setBackground(ThemeManager.getBackgroundColor());
        dialogPanel.add(ThemeManager.createStyledLabel("Status:", 12, false));
        dialogPanel.add(statusCombo);
        dialogPanel.add(ThemeManager.createStyledLabel("Kondisi:", 12, false));
        dialogPanel.add(kondisiCombo);

        int option = JOptionPane.showConfirmDialog(this, dialogPanel,
                "Update PS5 - " + selectedPS5.getNama(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String newKondisi = (String) kondisiCombo.getSelectedItem();
            String newStatus = (String) statusCombo.getSelectedItem();

            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    PS5 ps5 = PS5.fromCSV(line);
                    if (ps5 != null) {
                        if (ps5.getId() == id) {
                            ps5.setKondisi(newKondisi);
                            ps5.setStatus(newStatus);
                            newLines.add(ps5.toCSV());
                        } else {
                            newLines.add(line);
                        }
                    }
                }
            }
            FileHelper.updateFile("data/ps5.txt", newLines);
            loadData();
            JOptionPane.showMessageDialog(this, "PS5 berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deletePS5() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih PS5 yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String nama = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus PS5: " + nama + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            List<String> lines = FileHelper.readAllLines("data/ps5.txt");
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    PS5 ps5 = PS5.fromCSV(line);
                    if (ps5 != null && ps5.getId() != id) {
                        newLines.add(line);
                    }
                }
            }
            FileHelper.updateFile("data/ps5.txt", newLines);
            loadData();
            JOptionPane.showMessageDialog(this, "PS5 berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}