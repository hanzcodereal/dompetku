package com.neurallift.keuanganku.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.neurallift.keuanganku.data.model.TargetTabungan;

import java.util.List;

@Dao
public interface TargetTabunganDao {

    @Insert
    long insert(TargetTabungan target);

    @Update
    void update(TargetTabungan target);

    @Delete
    void delete(TargetTabungan target);

    @Query("SELECT * FROM target_tabungan ORDER BY id DESC")
    LiveData<List<TargetTabungan>> getAllTarget();

    // Dipakai pengingat di latar belakang (harus dipanggil dari thread terpisah)
    @Query("SELECT * FROM target_tabungan WHERE terkumpul < targetNominal ORDER BY id ASC")
    List<TargetTabungan> getBelumTercapaiSync();
}
