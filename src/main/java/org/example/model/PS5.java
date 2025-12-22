package org.example.model;

public class PS5 {
    private int id;
    private String nama;
    private String tipe;
    private int hargaPerHari;
    private String status; // Available, Rented
    private String kondisi;

    public PS5(int id, String nama, String tipe, int hargaPerHari, String status, String kondisi) {
        this.id = id;
        this.nama = nama;
        this.tipe = tipe;
        this.hargaPerHari = hargaPerHari;
        this.status = status;
        this.kondisi = kondisi;
    }

    // Getters
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getTipe() { return tipe; }
    public int getHargaPerHari() { return hargaPerHari; }
    public String getStatus() { return status; }
    public String getKondisi() { return kondisi; }

    // Setters
    public void setNama(String nama) { this.nama = nama; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    public void setHargaPerHari(int hargaPerHari) { this.hargaPerHari = hargaPerHari; }
    public void setStatus(String status) { this.status = status; }
    public void setKondisi(String kondisi) { this.kondisi = kondisi; }

    // Format untuk disimpan ke file
    public String toCSV() {
        return String.format("%d,%s,%s,%d,%s,%s",
                id, nama, tipe, hargaPerHari, status, kondisi);
    }

    // Parse dari CSV
    public static PS5 fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            int id = Integer.parseInt(parts[0]);
            String nama = parts[1];
            String tipe = parts[2];
            int harga = Integer.parseInt(parts[3]);
            String status = parts[4];
            String kondisi = parts[5];

            return new PS5(id, nama, tipe, harga, status, kondisi);
        } catch (Exception e) {
            System.out.println("Error parsing PS5: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("PS5-%02d: %s (%s) - Rp%,d/hari - %s",
                id, nama, tipe, hargaPerHari, status);
    }
}