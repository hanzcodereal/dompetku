package com.neurallift.keuanganku.ui.target;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.data.model.TargetTabungan;
import com.neurallift.keuanganku.utils.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TambahTargetBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NAMA = "nama";
    private static final String ARG_NOMINAL = "nominal";
    private static final String ARG_TERKUMPUL = "terkumpul";
    private static final String ARG_TENGGAT = "tenggat";

    private EditText etNama;
    private EditText etNominal;
    private TextView tvTenggat;
    private TextView tvHapusTenggat;

    private boolean editMode = false;
    private int targetId;
    private double terkumpulLama;
    private String tenggat; // yyyy-MM-dd atau null

    public static TambahTargetBottomSheet newInstance(TargetTabungan target) {
        TambahTargetBottomSheet sheet = new TambahTargetBottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, target.getId());
        args.putString(ARG_NAMA, target.getNama());
        args.putDouble(ARG_NOMINAL, target.getTargetNominal());
        args.putDouble(ARG_TERKUMPUL, target.getTerkumpul());
        args.putString(ARG_TENGGAT, target.getTenggat());
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_ID)) {
            editMode = true;
            targetId = args.getInt(ARG_ID);
            terkumpulLama = args.getDouble(ARG_TERKUMPUL);
            tenggat = args.getString(ARG_TENGGAT);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_TENGGAT)) {
            tenggat = savedInstanceState.getString(ARG_TENGGAT);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TENGGAT, tenggat);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_target, container, false);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        etNama = view.findViewById(R.id.etNamaTarget);
        etNominal = view.findViewById(R.id.etNominalTarget);
        tvTenggat = view.findViewById(R.id.tvTenggatTarget);
        tvHapusTenggat = view.findViewById(R.id.tvHapusTenggat);
        Button btnSimpan = view.findViewById(R.id.btnSimpanTarget);

        if (editMode) {
            Bundle args = requireArguments();
            tvTitle.setText(R.string.target_edit);
            btnSimpan.setText(R.string.perbarui);
            if (savedInstanceState == null) {
                etNama.setText(args.getString(ARG_NAMA));
                etNominal.setText(String.valueOf((long) args.getDouble(ARG_NOMINAL)));
            }
        }

        tampilkanTenggat();
        tvTenggat.setOnClickListener(v -> pilihTanggal());
        tvHapusTenggat.setOnClickListener(v -> {
            tenggat = null;
            tampilkanTenggat();
        });
        btnSimpan.setOnClickListener(v -> simpan());

        return view;
    }

    private void tampilkanTenggat() {
        if (tenggat == null || tenggat.isEmpty()) {
            tvTenggat.setText(R.string.target_pilih_tanggal);
            tvTenggat.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            tvHapusTenggat.setVisibility(View.GONE);
        } else {
            tvTenggat.setText(DateTimeUtils.formatDateFull(tenggat));
            tvTenggat.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            tvHapusTenggat.setVisibility(View.VISIBLE);
        }
    }

    private void pilihTanggal() {
        Calendar cal = Calendar.getInstance();
        if (tenggat != null && !tenggat.isEmpty()) {
            Date date = DateTimeUtils.parseDate(tenggat);
            if (date != null) cal.setTime(date);
        }
        new DatePickerDialog(requireContext(), (picker, year, month, day) -> {
            tenggat = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            tampilkanTenggat();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void simpan() {
        String nama = etNama.getText().toString().trim();
        String digit = etNominal.getText().toString().replaceAll("[^0-9]", "");

        if (nama.isEmpty()) {
            etNama.setError(getString(R.string.target_nama_kosong));
            return;
        }
        double nominal = digit.isEmpty() ? 0 : Double.parseDouble(digit);
        if (nominal <= 0) {
            etNominal.setError(getString(R.string.target_nominal_tidak_valid));
            return;
        }

        TargetViewModel viewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);
        TargetTabungan target = new TargetTabungan(nama, nominal, tenggat);
        if (editMode) {
            target.setId(targetId);
            target.setTerkumpul(terkumpulLama);
            viewModel.update(target);
        } else {
            viewModel.insert(target);
        }
        dismiss();
    }
}
