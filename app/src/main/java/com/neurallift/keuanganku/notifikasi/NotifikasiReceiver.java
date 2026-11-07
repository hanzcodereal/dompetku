package com.neurallift.keuanganku.notifikasi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.data.AppDatabase;
import com.neurallift.keuanganku.data.model.TargetTabungan;
import com.neurallift.keuanganku.utils.FormatUtils;

import java.util.List;

/** Dipanggil oleh AlarmManager, tidak membutuhkan aplikasi sedang terbuka. */
public class NotifikasiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Context app = context.getApplicationContext();
        int jenis = intent.getIntExtra(NotifikasiScheduler.EXTRA_JENIS, -1);

        if (jenis == NotifikasiScheduler.REQ_HARIAN) {
            if (!NotifikasiScheduler.isHarianAktif(app)) return;
            NotifikasiScheduler.jadwalkanHarian(app); // jadwalkan untuk besok
            NotifikasiHelper.tampilkan(app, NotifikasiScheduler.REQ_HARIAN,
                    app.getString(R.string.notif_harian_judul),
                    app.getString(R.string.notif_harian_isi));

        } else if (jenis == NotifikasiScheduler.REQ_TARGET) {
            if (!NotifikasiScheduler.isTargetAktif(app)) return;
            NotifikasiScheduler.jadwalkanTarget(app); // jadwalkan untuk Senin depan

            final PendingResult pendingResult = goAsync();
            AppDatabase.databaseWriteExecutor.execute(() -> {
                try {
                    List<TargetTabungan> belumTercapai =
                            AppDatabase.getDatabase(app).targetTabunganDao().getBelumTercapaiSync();
                    if (belumTercapai == null || belumTercapai.isEmpty()) return;

                    String isi;
                    if (belumTercapai.size() == 1) {
                        TargetTabungan t = belumTercapai.get(0);
                        int persen = t.getTargetNominal() > 0
                                ? (int) Math.round(t.getTerkumpul() / t.getTargetNominal() * 100) : 0;
                        isi = app.getString(R.string.notif_target_satu, t.getNama(), persen,
                                FormatUtils.formatCurrency(t.getTargetNominal() - t.getTerkumpul()));
                    } else {
                        isi = app.getString(R.string.notif_target_banyak, belumTercapai.size());
                    }
                    NotifikasiHelper.tampilkan(app, NotifikasiScheduler.REQ_TARGET,
                            app.getString(R.string.notif_target_judul), isi);
                } finally {
                    pendingResult.finish();
                }
            });
        }
    }
}
