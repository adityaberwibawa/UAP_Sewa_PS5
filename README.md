# UAP_Sewa_PS5

## Ringkasan
Proyek ini merupakan aplikasi **sistem penyewaan PlayStation 5 (PS5)** sebagai tugas akhir **Ujian Akhir Semester (UAP)** mata kuliah **Pemrograman Lanjut**. Aplikasi dibangun menggunakan **Java** dengan struktur proyek standar berbasis **Maven** dan **GUI** dengan **Javax Swing**, mencakup model data, logika bisnis, dan antarmuka sederhana untuk mengelola data penyewaan konsol PS5.

**Penanggung Jawab:**
- Muhammad Aditya Wibawa (407)
- Aditya Saputra (396)

## Tujuan Proyek
Aplikasi ini dirancang untuk:
1. **Mencatat data konsumen** yang melakukan penyewaan.
2. **Mengelola daftar rental PS5**, termasuk durasi, biaya, dan status sewa.
3. Menyediakan **laporan ringkas** penyewaan untuk analisis.

## Fitur Utama
Fitur inti yang umumnya ada di aplikasi ini meliputi:
- Input dan update data penyewa.
- Input durasi sewa dan perhitungan biaya.
- Tampilan daftar penyewaan aktif dan selesai.
- Penyimpanan data sementara selama aplikasi berjalan.

*(Fitur di atas bisa disesuaikan berdasarkan isi kode yang sebenarnya — konfirmasi ulang jika ada modul khusus.)*

## Struktur Proyek
```text
UAP_Sewa_PS5/
├── src/
│    └── main/java/org/example/         # Source Java
│                           └──model    # Source Java
│                           └──ui       # Source Java
│                           └──util     # Source Java
├── data/                        # Resource / sample data
├── .gitignore
├── pom.xml                      # Konfigurasi Maven
└── README.md
