package com.neurallift.keuanganku.ui.target;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.data.model.TargetTabungan;

public class TargetFragment extends Fragment implements TargetAdapter.Listener {

    private TargetViewModel viewModel;
    private TargetAdapter adapter;
    private View layoutKosong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        layoutKosong = view.findViewById(R.id.layoutKosong);
        RecyclerView rv = view.findViewById(R.id.rvTarget);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TargetAdapter(this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabTambahTarget);
        fab.setOnClickListener(v ->
                new TambahTargetBottomSheet().show(getChildFragmentManager(), "tambahTarget"));

        viewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);
        viewModel.getAllTarget().observe(getViewLifecycleOwner(), daftar -> {
            adapter.setDaftar(daftar);
            layoutKosong.setVisibility(daftar == null || daftar.isEmpty() ? View.VISIBLE : View.GONE);
        });

        return view;
    }

    @Override
    public void onSetor(TargetTabungan target) {
        tampilkanDialogJumlah(target, true);
    }

    @Override
    public void onTarik(TargetTabungan target) {
        tampilkanDialogJumlah(target, false);
    }

    @Override
    public void onEdit(TargetTabungan target) {
        TambahTargetBottomSheet.newInstance(target).show(getChildFragmentManager(), "editTarget");
    }

    @Override
    public void onHapus(TargetTabungan target) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage(R.string.target_hapus_konfirmasi)
                .setPositiveButton(R.string.hapus, (dialog, which) -> {
                    viewModel.delete(target);
                    Toast.makeText(requireContext(), R.string.target_dihapus, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.batal, null)
                .show();
    }

    private void tampilkanDialogJumlah(TargetTabungan target, boolean tambah) {
        EditText et = new EditText(requireContext());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint(R.string.masukkan_nominal);

        FrameLayout wadah = new FrameLayout(requireContext());
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        wadah.setPadding(pad, pad / 2, pad, 0);
        wadah.addView(et);

        new AlertDialog.Builder(requireContext())
                .setTitle(tambah ? R.string.target_tambah_tabungan : R.string.target_kurangi_tabungan)
                .setView(wadah)
                .setPositiveButton(R.string.simpan, (dialog, which) -> {
                    String digit = et.getText().toString().replaceAll("[^0-9]", "");
                    if (digit.isEmpty() || Double.parseDouble(digit) <= 0) {
                        Toast.makeText(requireContext(), R.string.nominal_kosong, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double jumlah = Double.parseDouble(digit);
                    boolean sudahTercapai = target.getTerkumpul() >= target.getTargetNominal();
                    double baru = tambah
                            ? target.getTerkumpul() + jumlah
                            : Math.max(0, target.getTerkumpul() - jumlah);
                    target.setTerkumpul(baru);
                    viewModel.update(target);
                    if (!sudahTercapai && baru >= target.getTargetNominal()) {
                        Toast.makeText(requireContext(), R.string.target_selamat, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.batal, null)
                .show();
    }
}
