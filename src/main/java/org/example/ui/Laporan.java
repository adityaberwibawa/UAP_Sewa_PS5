package org.example.ui;

import org.example.util.*;
import org.example.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Laporan extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel, bulananLabel, aktifLabel;
    private Dashboard dashboard;
    private JComboBox<String> filterCombo;

    public Laporan(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getBackgroundColor());

        initUI();
        loadData();
        calculateSummary();
    }

    private void initUI() {
        JPanel mainPanel = ThemeManager.createStyledPanel(true);
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.getBackgroundColor());

        JLabel title = ThemeManager.createStyledLabel("📊 LAPORAN TRANSAKSI", 20, true);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        headerPanel.add(title, BorderLayout.WEST);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(ThemeManager.getBackgroundColor());

        filterPanel.add(ThemeManager.createStyledLabel("Filter:", 12, false));
        String[] filters = {"Semua", "Aktif", "Selesai", "Bulan Ini", "Bulan Lalu"};
        filterCombo = new JComboBox<>(filters);
        ThemeManager.styleComboBox(filterCombo);
        filterCombo.addActionListener(e -> filterData());
        filterPanel.add(filterCombo);

        headerPanel.add(filterPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Statistik Panel
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Tabel
        String[] columns = {"ID", "Nama Penyewa", "PS5 ID", "Tanggal", "Durasi", "Total", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        ThemeManager.styleTable(table);

        // Custom renderer untuk status
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(ThemeManager.getPanelColor());

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());

        JButton refreshBtn = ThemeManager.createStyledButton("🔄 Refresh Data");
        JButton exportBtn = ThemeManager.createStyledButton("📥 Export ke CSV");
        JButton printBtn = ThemeManager.createStyledButton("🖨️ Cetak Laporan");
        JButton closeBtn = ThemeManager.createStyledButton("Kembali ke Dashboard");

        refreshBtn.addActionListener(e -> {
            loadData();
            calculateSummary();
        });
        exportBtn.addActionListener(e -> exportToCSV());
        printBtn.addActionListener(e -> printReport());
        closeBtn.addActionListener(e -> dashboard.showHome());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = ThemeManager.createStyledPanel(true);
        statsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Total Semua Transaksi
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(ThemeManager.getPanelColor());
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel totalIcon = new JLabel("💰", SwingConstants.CENTER);
        totalIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        totalIcon.setForeground(ThemeManager.getLabelTextColor());

        totalLabel = ThemeManager.createStyledLabel("Total Semua Transaksi", 12, true);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel totalValue = ThemeManager.createStyledLabel("Rp 0", 16, true);
        totalValue.setForeground(ThemeManager.getAccentColor());
        totalValue.setHorizontalAlignment(SwingConstants.CENTER);

        totalPanel.add(totalIcon, BorderLayout.NORTH);
        totalPanel.add(totalLabel, BorderLayout.CENTER);
        totalPanel.add(totalValue, BorderLayout.SOUTH);

        // Pendapatan Bulan Ini
        JPanel monthlyPanel = new JPanel(new BorderLayout());
        monthlyPanel.setBackground(ThemeManager.getPanelColor());
        monthlyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel monthlyIcon = new JLabel("📈", SwingConstants.CENTER);
        monthlyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        monthlyIcon.setForeground(ThemeManager.getLabelTextColor());

        String monthName = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("id"));
        bulananLabel = ThemeManager.createStyledLabel("Pendapatan " + monthName, 12, true);
        bulananLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel monthlyValue = ThemeManager.createStyledLabel("Rp 0", 16, true);
        monthlyValue.setForeground(ThemeManager.getAccentColor());
        monthlyValue.setHorizontalAlignment(SwingConstants.CENTER);

        monthlyPanel.add(monthlyIcon, BorderLayout.NORTH);
        monthlyPanel.add(bulananLabel, BorderLayout.CENTER);
        monthlyPanel.add(monthlyValue, BorderLayout.SOUTH);

        // Transaksi Aktif
        JPanel activePanel = new JPanel(new BorderLayout());
        activePanel.setBackground(ThemeManager.getPanelColor());
        activePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel activeIcon = new JLabel("🔄", SwingConstants.CENTER);
        activeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        activeIcon.setForeground(ThemeManager.getLabelTextColor());

        aktifLabel = ThemeManager.createStyledLabel("Transaksi Aktif", 12, true);
        aktifLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel activeValue = ThemeManager.createStyledLabel("0", 16, true);
        activeValue.setForeground(ThemeManager.getAccentColor());
        activeValue.setHorizontalAlignment(SwingConstants.CENTER);

        activePanel.add(activeIcon, BorderLayout.NORTH);
        activePanel.add(aktifLabel, BorderLayout.CENTER);
        activePanel.add(activeValue, BorderLayout.SOUTH);

        statsPanel.add(totalPanel);
        statsPanel.add(monthlyPanel);
        statsPanel.add(activePanel);

        // Simpan referensi ke label nilai
        this.totalLabel = totalValue;
        this.bulananLabel = monthlyValue;
        this.aktifLabel = activeValue;

        return statsPanel;
    }

    private void loadData() {
        tableModel.setRowCount(0);

        List<String> lines = FileHelper.readAllLines("data/sewa.txt");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                Rental rental = Rental.fromCSV(line);
                if (rental != null) {
                    Object[] row = {
                            rental.getId(),
                            rental.getNamaPenyewa(),
                            rental.getPs5Id(),
                            rental.getTanggalSewa().format(formatter),
                            rental.getDurasiHari() + " hari",
                            String.format("Rp%,.0f", rental.getTotalHarga()),
                            rental.getStatus()
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    private void filterData() {
        String filter = (String) filterCombo.getSelectedItem();
        if (filter == null || filter.equals("Semua")) {
            loadData();
            return;
        }

        List<String> lines = FileHelper.readAllLines("data/sewa.txt");
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);
        LocalDate lastDayOfLastMonth = firstDayOfMonth.minusDays(1);

        tableModel.setRowCount(0);

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                Rental rental = Rental.fromCSV(line);
                if (rental != null) {
                    boolean include = false;

                    switch (filter) {
                        case "Aktif":
                            include = "Active".equals(rental.getStatus());
                            break;
                        case "Selesai":
                            include = "Completed".equals(rental.getStatus());
                            break;
                        case "Bulan Ini":
                            include = !rental.getTanggalSewa().isBefore(firstDayOfMonth) &&
                                    !rental.getTanggalSewa().isAfter(now);
                            break;
                        case "Bulan Lalu":
                            include = !rental.getTanggalSewa().isBefore(firstDayOfLastMonth) &&
                                    !rental.getTanggalSewa().isAfter(lastDayOfLastMonth);
                            break;
                    }

                    if (include) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        Object[] row = {
                                rental.getId(),
                                rental.getNamaPenyewa(),
                                rental.getPs5Id(),
                                rental.getTanggalSewa().format(formatter),
                                rental.getDurasiHari() + " hari",
                                String.format("Rp%,.0f", rental.getTotalHarga()),
                                rental.getStatus()
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        }
    }

    private void calculateSummary() {
        List<String> lines = FileHelper.readAllLines("data/sewa.txt");
        double totalAll = 0;
        double totalThisMonth = 0;
        int activeCount = 0;

        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", new Locale("id"));
        String monthName = now.format(monthFormatter);

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                Rental rental = Rental.fromCSV(line);
                if (rental != null) {
                    totalAll += rental.getTotalHarga();

                    if ("Active".equals(rental.getStatus())) {
                        activeCount++;
                    }

                    // Hitung bulan ini
                    LocalDate rentalDate = rental.getTanggalSewa();
                    if (rentalDate.getMonthValue() == currentMonth &&
                            rentalDate.getYear() == currentYear) {
                        totalThisMonth += rental.getTotalHarga();
                    }
                }
            }
        }

        totalLabel.setText(String.format("Rp%,.0f", totalAll));
        bulananLabel.setText(String.format("Rp%,.0f", totalThisMonth));
        aktifLabel.setText(String.valueOf(activeCount));

        // Update judul
        JPanel statsPanel = (JPanel) ((BorderLayout) ((JPanel) getComponent(0)).getLayout()).getLayoutComponent(BorderLayout.NORTH);
        JPanel monthlyPanel = (JPanel) statsPanel.getComponent(1);
        JLabel titleLabel = (JLabel) ((BorderLayout) monthlyPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        titleLabel.setText("Pendapatan " + monthName);
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan sebagai CSV");
        fileChooser.setSelectedFile(new java.io.File("laporan_transaksi.csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();

            StringBuilder csvContent = new StringBuilder();
            csvContent.append("ID,Nama Penyewa,PS5 ID,Tanggal Sewa,Durasi (hari),Total Harga,Status\n");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                csvContent.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        ((String) tableModel.getValueAt(i, 4)).replace(" hari", ""),
                        ((String) tableModel.getValueAt(i, 5)).replace("Rp", "").replace(",", ""),
                        tableModel.getValueAt(i, 6)
                ));
            }

            try {
                FileHelper.writeToFile(file.getAbsolutePath(), csvContent.toString());
                JOptionPane.showMessageDialog(this,
                        "Laporan berhasil diexport ke: " + file.getAbsolutePath(),
                        "Export Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Gagal export laporan: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printReport() {
        try {
            table.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mencetak laporan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom cell renderer untuk status
    class StatusCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString();
                JLabel label = (JLabel) c;

                switch (status) {
                    case "Active":
                        label.setText("🔄 " + status);
                        label.setForeground(new Color(255, 193, 7)); // Amber
                        break;
                    case "Completed":
                        label.setText("✅ " + status);
                        label.setForeground(new Color(76, 175, 80)); // Green
                        break;
                    default:
                        label.setText("❓ " + status);
                        break;
                }

                label.setHorizontalAlignment(SwingConstants.CENTER);
            }

            return c;
        }
    }
}