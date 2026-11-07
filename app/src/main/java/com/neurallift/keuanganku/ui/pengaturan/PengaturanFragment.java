package com.neurallift.keuanganku.ui.pengaturan;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.utils.IntentUtils;
import com.neurallift.keuanganku.utils.TemaUtils;

/** Pusat pengaturan: tema, bahasa, pengingat, target tabungan, dan tentang aplikasi. */
public class PengaturanFragment extends Fragment {

    private TextView tvTemaNilai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> navController.navigateUp());

        tvTemaNilai = view.findViewById(R.id.tvTemaNilai);
        TextView tvVersiNilai = view.findViewById(R.id.tvVersiNilai);
        tampilkanTema();
        tvVersiNilai.setText(getString(R.string.pengaturan_versi_nilai, getVersionName()));

        view.findViewById(R.id.rowTema).setOnClickListener(v -> pilihTema());
        view.findViewById(R.id.rowPengingat).setOnClickListener(v -> navController.navigate(R.id.navigation_notifikasi));
        view.findViewById(R.id.rowTarget).setOnClickListener(v -> navController.navigate(R.id.navigation_target));
        view.findViewById(R.id.rowInfo).setOnClickListener(v -> navController.navigate(R.id.navigation_info));
        view.findViewById(R.id.rowEmail).setOnClickListener(v -> IntentUtils.kirimEmail(requireContext()));
        view.findViewById(R.id.rowSaweria).setOnClickListener(v -> IntentUtils.bukaSaweria(requireContext()));

        return view;
    }

    private String[] namaTema() {
        // Urutan harus sama dengan TemaUtils.TERANG, GELAP, SISTEM
        return new String[]{
                getString(R.string.tema_terang),
                getString(R.string.tema_gelap),
                getString(R.string.tema_sistem)
        };
    }

    private void tampilkanTema() {
        tvTemaNilai.setText(namaTema()[TemaUtils.getTema(requireContext())]);
    }

    private void pilihTema() {
        Context context = requireContext();
        new AlertDialog.Builder(context)
                .setTitle(R.string.pengaturan_pilih_tema)
                .setSingleChoiceItems(namaTema(), TemaUtils.getTema(context), (dialog, which) -> {
                    dialog.dismiss();
                    TemaUtils.setTema(context.getApplicationContext(), which);
                    if (isAdded()) tampilkanTema();
                })
                .setNegativeButton(R.string.batal, null)
                .show();
    }

    private String getVersionName() {
        try {
            String nama = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0).versionName;
            return nama != null ? nama : "-";
        } catch (PackageManager.NameNotFoundException e) {
            return "-";
        }
    }
}
