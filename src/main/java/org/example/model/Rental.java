package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Rental {
    private int id;
    private String namaPenyewa;
    private String telepon;
    private int ps5Id;
    private LocalDate tanggalSewa;
    private int durasiHari;
    private double totalHarga;
    private String status; // Active, Completed

    public Rental(int id, String namaPenyewa, String telepon, int ps5Id, LocalDate tanggalSewa, int durasiHari, double totalHarga, String status) {
        this.id = id;
        this.namaPenyewa = namaPenyewa;
        this.telepon = telepon;
        this.ps5Id = ps5Id;
        this.tanggalSewa = tanggalSewa;
        this.durasiHari = durasiHari;
        this.totalHarga = totalHarga;
        this.status = status;
    }

    public int getId() { return id; }
    public String getNamaPenyewa() { return namaPenyewa; }
    public String getTelepon() { return telepon; }
    public int getPs5Id() { return ps5Id; }
    public LocalDate getTanggalSewa() { return tanggalSewa; }
    public int getDurasiHari() { return durasiHari; }
    public double getTotalHarga() { return totalHarga; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }

    // Format untuk disimpan ke file
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%d,%s,%s,%d,%s,%d,%.2f,%s",
                id, namaPenyewa, telepon, ps5Id, tanggalSewa.format(formatter),
                durasiHari, totalHarga, status);
    }

    // Parse dari CSV
    public static Rental fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            int id = Integer.parseInt(parts[0]);
            String namaPenyewa = parts[1];
            String telepon = parts[2];
            int ps5Id = Integer.parseInt(parts[3]);
            LocalDate tanggalSewa = LocalDate.parse(parts[4]);
            int durasiHari = Integer.parseInt(parts[5]);
            double totalHarga = Double.parseDouble(parts[6]);
            String status = parts[7];

            return new Rental(id, namaPenyewa, telepon, ps5Id, tanggalSewa,
                    durasiHari, totalHarga, status);
        } catch (Exception e) {
            System.out.println("Error parsing Rental: " + e.getMessage());
            return null;
        }
    }
}