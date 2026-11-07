package com.neurallift.keuanganku.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "target_tabungan")
public class TargetTabungan {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nama;
    private double targetNominal;
    private double terkumpul;
    // Format yyyy-MM-dd, boleh kosong (tanpa tenggat)
    private String tenggat;

    public TargetTabungan(String nama, double targetNominal, String tenggat) {
        this.nama = nama;
        this.targetNominal = targetNominal;
        this.tenggat = tenggat;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getTargetNominal() { return targetNominal; }
    public void setTargetNominal(double targetNominal) { this.targetNominal = targetNominal; }

    public double getTerkumpul() { return terkumpul; }
    public void setTerkumpul(double terkumpul) { this.terkumpul = terkumpul; }

    public String getTenggat() { return tenggat; }
    public void setTenggat(String tenggat) { this.tenggat = tenggat; }
}
