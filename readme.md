# DompetKu

DompetKu adalah aplikasi Android untuk mencatat keuangan pribadi: pemasukan, pengeluaran, kasir sederhana, dan laporan keuangan. Semua data tersimpan di perangkat, tanpa login dan tanpa internet.

## Info Aplikasi

| | |
|---|---|
| Versi | 1.0.0 |
| Developer | Hanz Dev Official |
| Email | hanzzcodee@gmail.com |
| Dukungan | https://saweria.co/hanzreally |

Info ini juga tersedia di dalam aplikasi: ketuk ikon info (i) pada kartu saldo di Beranda.

## Fitur

### Transaksi
- Tambah transaksi pemasukan dan pengeluaran
- Ubah dan hapus transaksi
- Kategori dan catatan untuk setiap transaksi
- Pengelompokan transaksi berdasarkan tanggal
- Bagikan detail transaksi
- Filter berdasarkan jenis, kategori, akun, dan rentang tanggal

### Akun
- Banyak akun keuangan (misalnya Kas, Bank, E-Wallet)
- Saldo setiap akun
- Riwayat transaksi per akun

> Akun di aplikasi ini adalah akun keuangan milik Anda sendiri, bukan akun login.

### Target Tabungan
- Buat target tabungan dengan nama, jumlah target, dan tenggat (opsional)
- Tambah atau kurangi tabungan, progres tampil dalam bar dan persen
- Ringkasan target di Beranda, ketuk untuk membuka daftar target

> Tabungan pada target bersifat terpisah dan tidak mengubah saldo akun.

### Pengingat (Notifikasi)
- Pengingat harian untuk mencatat transaksi, jam bisa diatur
- Pengingat target tabungan setiap hari Senin, hanya muncul jika ada target yang belum tercapai
- Bisa dinyalakan dan dimatikan lewat ikon lonceng di kartu saldo Beranda
- Tetap muncul walaupun aplikasi tidak dibuka, dan dijadwalkan ulang otomatis setelah HP dinyalakan ulang
- Android 13 ke atas meminta izin notifikasi saat pengingat pertama kali dinyalakan

### Pengaturan dan Tema
- Ikon pengaturan (roda gigi) di kartu saldo Beranda
- Tema Terang, Gelap, atau Ikuti sistem (pilihan tersimpan dan langsung dipakai)
- Jalan pintas ke Pengingat, Target Tabungan, Info Aplikasi, Hubungi Developer, dan Dukung Developer

### Kasir
- Daftar barang dengan harga, satuan, kategori, dan stok
- Stok berkurang otomatis saat barang terjual, dan penjualan dibatasi sesuai stok
- Edit dan hapus barang lewat menu titik tiga di setiap barang
- Pembayaran tunai dan non tunai, lengkap dengan kembalian
- Invoice yang bisa disimpan sebagai gambar atau dibagikan

### Laporan
- Laporan harian, mingguan, bulanan, dan periode kustom
- Ringkasan pemasukan, pengeluaran, dan laba/rugi
- Grafik garis laba rugi dan grafik lingkaran per kategori

### Beranda
- Saldo tersedia
- Grafik ringkasan pemasukan dan pengeluaran
- Daftar transaksi terakhir
- Geser kiri/kanan untuk berpindah periode

### Lainnya
- Format mata uang Rupiah (Rp) dan tanggal berbahasa Indonesia
- Seluruh tampilan berbahasa Indonesia
- Mode gelap
- Font Inter
- Data disimpan lokal dengan database Room

## Data dan Privasi

- Tidak ada login atau pendaftaran.
- Aplikasi tidak meminta izin internet, sehingga data tidak dikirim ke server mana pun.
- Pengingat dijadwalkan di dalam perangkat, bukan lewat server.
- Jika aplikasi dihapus atau datanya dibersihkan, data ikut terhapus.

## Teknologi

- Bahasa: Java
- Arsitektur: MVVM dengan pola Repository
- Database lokal: Room
- Komponen: LiveData, ViewModel, Navigation Component, Material Components
- Grafik: MPAndroidChart

### Struktur Proyek

```
app/src/main/
├── java/com/neurallift/keuanganku/
│   ├── data/        # model, DAO, repository, database
│   ├── notifikasi/  # penjadwal dan penerima pengingat
│   ├── ui/          # akun, home, info, kasir, kategori, laporan, notifikasi, pengaturan, target, transaksi
│   └── utils/       # format tanggal, mata uang, dan grafik
└── res/             # layout, navigasi, string, font, gambar
```

### Tabel Database

- `transaksi`: data transaksi
- `kategori`: kategori transaksi
- `akun`: akun keuangan
- `barang`: barang untuk fitur kasir
- `target_tabungan`: target tabungan

## Menjalankan Proyek

### Kebutuhan
- Android Studio versi terbaru
- Android SDK 24 atau lebih baru
- Java 11 atau lebih baru

### Menjalankan di Perangkat
1. Buka proyek di Android Studio.
2. Tunggu sinkronisasi Gradle selesai.
3. Jalankan di emulator atau perangkat.

## Build Release

Build release dijalankan manual.

1. Buat keystore (cukup sekali):
   ```
   keytool -genkeypair -v -keystore dompetku-release.jks -alias dompetku -keyalg RSA -keysize 2048 -validity 10000
   ```
2. Salin `keystore.properties.example` menjadi `keystore.properties`, lalu isi datanya.
3. Jalankan perintah berikut (di Windows: `gradlew.bat assembleRelease`):
   ```
   ./gradlew assembleRelease
   ```
4. APK ada di `app/build/outputs/apk/release/`.

Jika `keystore.properties` belum ada, build release memakai kunci debug. Gunakan keystore sendiri untuk APK yang akan dibagikan, dan simpan dengan aman karena update aplikasi harus ditandatangani kunci yang sama.

Alternatif tanpa Android Studio: unggah proyek ke GitHub, buka tab **Actions**, pilih **Build Release APK (manual)**, lalu klik **Run workflow**.

## Kredit

- Developer: Hanz Dev Official
- Proyek DompetKu asli oleh NeuralLift
- Font: Inter (SIL Open Font License 1.1)
