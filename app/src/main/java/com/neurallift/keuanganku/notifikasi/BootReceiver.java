package com.neurallift.keuanganku.notifikasi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Menjadwalkan ulang pengingat setelah HP dinyalakan ulang atau aplikasi diperbarui. */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) {
            NotifikasiScheduler.jadwalkanSemua(context.getApplicationContext());
        }
    }
}
