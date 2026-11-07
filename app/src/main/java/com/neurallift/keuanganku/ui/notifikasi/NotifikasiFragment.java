package com.neurallift.keuanganku.ui.notifikasi;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.notifikasi.NotifikasiHelper;
import com.neurallift.keuanganku.notifikasi.NotifikasiScheduler;

import java.util.Locale;

public class NotifikasiFragment extends Fragment {

    private SwitchMaterial swHarian;
    private SwitchMaterial swTarget;
    private TextView tvWaktu;
    private boolean memuat = false;
    private Runnable setelahIzin;

    private final ActivityResultLauncher<String> izinLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), diberikan -> {
                Runnable aksi = setelahIzin;
                setelahIzin = null;
                if (!isAdded()) return;
                if (diberikan && aksi != null) {
                    aksi.run();
                } else {
                    Toast.makeText(requireContext(), R.string.notifikasi_izin_ditolak, Toast.LENGTH_LONG).show();
                    muatTampilan();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        swHarian = view.findViewById(R.id.swHarian);
        swTarget = view.findViewById(R.id.swTarget);
        tvWaktu = view.findViewById(R.id.tvWaktu);

        swHarian.setOnCheckedChangeListener((tombol, aktif) -> {
            if (memuat) return;
            Context app = requireContext().getApplicationContext();
            if (aktif) {
                denganIzin(() -> {
                    NotifikasiScheduler.setHarianAktif(app, true);
                    NotifikasiScheduler.jadwalkanHarian(app);
                    muatTampilan();
                    Toast.makeText(app, R.string.notifikasi_diaktifkan, Toast.LENGTH_SHORT).show();
                });
            } else {
                NotifikasiScheduler.setHarianAktif(app, false);
                NotifikasiScheduler.batalkan(app, NotifikasiScheduler.REQ_HARIAN);
            }
        });

        swTarget.setOnCheckedChangeListener((tombol, aktif) -> {
            if (memuat) return;
            Context app = requireContext().getApplicationContext();
            if (aktif) {
                denganIzin(() -> {
                    NotifikasiScheduler.setTargetAktif(app, true);
                    NotifikasiScheduler.jadwalkanTarget(app);
                    muatTampilan();
                    Toast.makeText(app, R.string.notifikasi_diaktifkan, Toast.LENGTH_SHORT).show();
                });
            } else {
                NotifikasiScheduler.setTargetAktif(app, false);
                NotifikasiScheduler.batalkan(app, NotifikasiScheduler.REQ_TARGET);
            }
        });

        view.findViewById(R.id.rowWaktu).setOnClickListener(v -> pilihWaktu());

        view.findViewById(R.id.btnCoba).setOnClickListener(v -> denganIzin(() -> {
            Context app = requireContext().getApplicationContext();
            if (!NotifikasiHelper.izinTersedia(app)) {
                Toast.makeText(app, R.string.notifikasi_dimatikan, Toast.LENGTH_LONG).show();
                return;
            }
            NotifikasiHelper.tampilkan(app, 1999,
                    getString(R.string.notif_coba_judul), getString(R.string.notif_coba_isi));
        }));

        muatTampilan();
        return view;
    }

    private void muatTampilan() {
        Context c = requireContext();
        memuat = true;
        swHarian.setChecked(NotifikasiScheduler.isHarianAktif(c));
        swTarget.setChecked(NotifikasiScheduler.isTargetAktif(c));
        tvWaktu.setText(String.format(Locale.US, "%02d:%02d",
                NotifikasiScheduler.getJam(c), NotifikasiScheduler.getMenit(c)));
        memuat = false;
    }

    private void pilihWaktu() {
        Context c = requireContext();
        new TimePickerDialog(c, (picker, jam, menit) -> {
            Context app = c.getApplicationContext();
            NotifikasiScheduler.setWaktu(app, jam, menit);
            NotifikasiScheduler.jadwalkanSemua(app);
            muatTampilan();
        }, NotifikasiScheduler.getJam(c), NotifikasiScheduler.getMenit(c), true).show();
    }

    /** Menjalankan aksi setelah izin notifikasi tersedia (Android 13+ perlu izin runtime). */
    private void denganIzin(Runnable aksi) {
        boolean perluIzin = Build.VERSION.SDK_INT >= 33
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED;
        if (perluIzin) {
            setelahIzin = aksi;
            izinLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            aksi.run();
        }
    }
}
