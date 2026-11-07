package com.neurallift.keuanganku.ui.target;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.data.model.TargetTabungan;
import com.neurallift.keuanganku.utils.DateTimeUtils;
import com.neurallift.keuanganku.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.TargetViewHolder> {

    public interface Listener {
        void onSetor(TargetTabungan target);
        void onTarik(TargetTabungan target);
        void onEdit(TargetTabungan target);
        void onHapus(TargetTabungan target);
    }

    private List<TargetTabungan> daftar = new ArrayList<>();
    private final Listener listener;

    public TargetAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setDaftar(List<TargetTabungan> daftar) {
        this.daftar = daftar != null ? daftar : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TargetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target, parent, false);
        return new TargetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TargetViewHolder holder, int position) {
        holder.bind(daftar.get(position));
    }

    @Override
    public int getItemCount() {
        return daftar.size();
    }

    class TargetViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNama;
        private final TextView tvJumlah;
        private final TextView tvPersen;
        private final TextView tvSisa;
        private final TextView tvTenggat;
        private final ProgressBar progress;
        private final ImageButton btnMenu;

        TargetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaTarget);
            tvJumlah = itemView.findViewById(R.id.tvJumlahTarget);
            tvPersen = itemView.findViewById(R.id.tvPersenTarget);
            tvSisa = itemView.findViewById(R.id.tvSisaTarget);
            tvTenggat = itemView.findViewById(R.id.tvTenggatTarget);
            progress = itemView.findViewById(R.id.progressTarget);
            btnMenu = itemView.findViewById(R.id.btnMenuTarget);
        }

        void bind(TargetTabungan target) {
            double nominal = target.getTargetNominal();
            double terkumpul = target.getTerkumpul();
            int persen = nominal > 0 ? (int) Math.min(100, Math.round(terkumpul / nominal * 100)) : 0;

            tvNama.setText(target.getNama());
            tvJumlah.setText(FormatUtils.formatCurrency(terkumpul) + " / " + FormatUtils.formatCurrency(nominal));
            progress.setProgress(persen);
            tvPersen.setText(persen + "%");

            if (terkumpul >= nominal) {
                tvSisa.setText(R.string.target_tercapai);
            } else {
                tvSisa.setText(itemView.getContext().getString(
                        R.string.target_sisa, FormatUtils.formatCurrency(nominal - terkumpul)));
            }

            String tenggat = target.getTenggat();
            int warnaTenggat = itemView.getContext().getResources().getColor(R.color.colorSecondaryText);
            if (tenggat == null || tenggat.isEmpty()) {
                tvTenggat.setText(R.string.target_tanpa_tenggat);
            } else {
                boolean terlewat = terkumpul < nominal && tenggat.compareTo(DateTimeUtils.getTodayDate()) < 0;
                tvTenggat.setText(itemView.getContext().getString(
                        terlewat ? R.string.target_tenggat_lewat : R.string.target_tenggat,
                        DateTimeUtils.formatDateFull(tenggat)));
                if (terlewat) {
                    warnaTenggat = itemView.getContext().getResources().getColor(R.color.colorExpense);
                }
            }
            tvTenggat.setTextColor(warnaTenggat);

            itemView.setOnClickListener(v -> listener.onSetor(target));
            btnMenu.setOnClickListener(v -> tampilkanMenu(v, target));
        }

        private void tampilkanMenu(View anchor, TargetTabungan target) {
            PopupMenu menu = new PopupMenu(anchor.getContext(), anchor);
            menu.inflate(R.menu.menu_target_item);
            menu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_setor) {
                    listener.onSetor(target);
                } else if (id == R.id.action_tarik) {
                    listener.onTarik(target);
                } else if (id == R.id.action_edit) {
                    listener.onEdit(target);
                } else if (id == R.id.action_hapus) {
                    listener.onHapus(target);
                } else {
                    return false;
                }
                return true;
            });
            menu.show();
        }
    }
}
