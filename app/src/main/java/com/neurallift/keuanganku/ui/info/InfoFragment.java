package com.neurallift.keuanganku.ui.info;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.utils.IntentUtils;

/**
 * Halaman Info Aplikasi: nama, versi, developer, email, dan tautan dukungan (Saweria).
 */
public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        TextView tvVersi = view.findViewById(R.id.tvVersi);
        tvVersi.setText(getVersionName());

        view.findViewById(R.id.rowEmail).setOnClickListener(v -> IntentUtils.kirimEmail(requireContext()));
        view.findViewById(R.id.rowSaweria).setOnClickListener(v -> IntentUtils.bukaSaweria(requireContext()));

        return view;
    }

    private String getVersionName() {
        try {
            String versionName = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0).versionName;
            return versionName != null ? versionName : "-";
        } catch (PackageManager.NameNotFoundException e) {
            return "-";
        }
    }

}
