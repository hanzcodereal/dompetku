package com.neurallift.keuanganku.data;

import com.neurallift.keuanganku.data.dao.AkunDao;
import com.neurallift.keuanganku.data.dao.KategoriDao;
import com.neurallift.keuanganku.data.model.Akun;
import com.neurallift.keuanganku.data.model.Kategori;

/**
 * Mengisi data awal saat aplikasi pertama kali dipasang.
 * Hanya akun dan kategori bawaan, tanpa transaksi contoh.
 */
public class DatabaseInitializer {

    public static void populateDatabase(AppDatabase db) {
        populateKategori(db.kategoriDao());
        populateAkun(db.akunDao());
    }

    private static void populateKategori(KategoriDao kategoriDao) {
        String[] kategori = {
                "Penjualan", "Bahan Baku", "Sewa", "Listrik", "Air",
                "Gaji Karyawan", "Peralatan", "Pemasaran", "Lainnya"
        };
        for (String nama : kategori) {
            kategoriDao.insert(new Kategori(nama));
        }
    }

    private static void populateAkun(AkunDao akunDao) {
        String[] akun = {"Kas", "QRIS"};
        for (String nama : akun) {
            akunDao.insert(new Akun(nama));
        }
    }
}
