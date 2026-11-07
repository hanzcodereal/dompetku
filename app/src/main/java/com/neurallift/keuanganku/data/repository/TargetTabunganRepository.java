package com.neurallift.keuanganku.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.neurallift.keuanganku.data.AppDatabase;
import com.neurallift.keuanganku.data.dao.TargetTabunganDao;
import com.neurallift.keuanganku.data.model.TargetTabungan;

import java.util.List;

public class TargetTabunganRepository {

    private final TargetTabunganDao dao;

    public TargetTabunganRepository(Application application) {
        dao = AppDatabase.getDatabase(application).targetTabunganDao();
    }

    public LiveData<List<TargetTabungan>> getAllTarget() {
        return dao.getAllTarget();
    }

    public void insert(TargetTabungan target) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insert(target));
    }

    public void update(TargetTabungan target) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.update(target));
    }

    public void delete(TargetTabungan target) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.delete(target));
    }
}
