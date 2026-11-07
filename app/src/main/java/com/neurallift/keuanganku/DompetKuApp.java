package com.neurallift.keuanganku;

import android.app.Application;

import com.neurallift.keuanganku.utils.TemaUtils;

public class DompetKuApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TemaUtils.terapkan(this);
    }
}
