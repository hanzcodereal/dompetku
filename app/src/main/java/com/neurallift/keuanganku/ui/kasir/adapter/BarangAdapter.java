package com.neurallift.keuanganku.ui.kasir.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neurallift.keuanganku.R;
import com.neurallift.keuanganku.data.model.Barang;
import com.neurallift.keuanganku.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.BarangViewHolder> {

    private List<Barang> barangList = new ArrayList<>();
    private BarangAdapter.OnItemClickListener onItemClickListener;
    private BarangAdapter.OnEditClickListener onEditClickListener;
    private BarangAdapter.OnDeleteClickListener onDeleteClickListener;

    public interface  OnItemClickListener {
        void onItemClick(Barang barang);
    }

    public interface OnEditClickListener {
        void onEditClick(Barang barang);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Barang barang);
    }

    public void setOnItemClickListener(BarangAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnEditClickListener(BarangAdapter.OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(BarangAdapter.OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setBarangList(List<Barang> barangList) {
        this.barangList = barangList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        return new BarangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangViewHolder holder, int position) {
        Barang barang = barangList.get(position);
        holder.bind(barang);
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    class BarangViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaDepan;
        private TextView tvNamaBarang;
        private TextView tvHarga;
        private TextView tvSatuan;
        private TextView tvStok;
        private ImageButton btnMenu;

        public BarangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaDepan = itemView.findViewById(R.id.tvNamaDepan);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvSatuan = itemView.findViewById(R.id.tvSatuan);
            tvStok = itemView.findViewById(R.id.tvStok);
            btnMenu = itemView.findViewById(R.id.btnMenuBarang);
        }

        public void bind(Barang barang) {
            tvNamaDepan.setText(barang.getNama().substring(0, 1));
            tvNamaBarang.setText(barang.getNama());
            tvHarga.setText(FormatUtils.formatCurrency(barang.getHarga()));
            tvSatuan.setText(" / " + barang.getSatuan());

            if (barang.getStok() > 0) {
                tvStok.setText(itemView.getContext().getString(R.string.barang_stok, barang.getStok()));
                tvStok.setTextColor(itemView.getContext().getResources().getColor(R.color.colorSecondaryText));
            } else {
                tvStok.setText(R.string.barang_stok_habis);
                tvStok.setTextColor(itemView.getContext().getResources().getColor(R.color.colorExpense));
            }

            btnMenu.setOnClickListener(v -> {
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.inflate(R.menu.menu_akun_item);
                menu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit) {
                        if (onEditClickListener != null) onEditClickListener.onEditClick(barang);
                        return true;
                    } else if (item.getItemId() == R.id.action_hapus) {
                        if (onDeleteClickListener != null) onDeleteClickListener.onDeleteClick(barang);
                        return true;
                    }
                    return false;
                });
                menu.show();
            });

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(barang);
                }
            });

            itemView.setOnLongClickListener(v ->{
                if(onDeleteClickListener != null){
                    onDeleteClickListener.onDeleteClick(barang);
                }
                return true;
            });

        }
    }
}
