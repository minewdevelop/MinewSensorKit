package com.minew.sensor.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minew.sensor.demo.databinding.ItemScanDeviceBinding;
import com.minewtech.sensorKit.bean.SensorModule;

import java.util.ArrayList;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ScanHolder> {
    private ArrayList<SensorModule> dataList = new ArrayList<>();

    public void addNewData(@NonNull ArrayList<SensorModule> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public SensorModule getItem(int position) {
        return dataList.get(position);
    }

    @NonNull
    @Override
    public ScanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScanHolder(ItemScanDeviceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScanHolder holder, int position) {
        holder.bindData(dataList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ScanHolder extends RecyclerView.ViewHolder {

        private ItemScanDeviceBinding binding;
        public ScanHolder(@NonNull final ItemScanDeviceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setOnItemClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        void bindData(SensorModule module) {
            binding.setSensorModule(module);
            binding.executePendingBindings();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
