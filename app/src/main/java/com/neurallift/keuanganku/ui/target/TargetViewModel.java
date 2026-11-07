package com.neurallift.keuanganku.ui.target;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.neurallift.keuanganku.data.model.TargetTabungan;
import com.neurallift.keuanganku.data.repository.TargetTabunganRepository;

import java.util.List;

public class TargetViewModel extends AndroidViewModel {

    private final TargetTabunganRepository repository;
    private final LiveData<List<TargetTabungan>> allTarget;

    public TargetViewModel(@NonNull Application application) {
        super(application);
        repository = new TargetTabunganRepository(application);
        allTarget = repository.getAllTarget();
    }

    public LiveData<List<TargetTabungan>> getAllTarget() {
        return allTarget;
    }

    public void insert(TargetTabungan target) {
        repository.insert(target);
    }

    public void update(TargetTabungan target) {
        repository.update(target);
    }

    public void delete(TargetTabungan target) {
        repository.delete(target);
    }
}
