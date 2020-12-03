package com.minew.sensor.demo;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minew.sensor.demo.databinding.ItemHistoryBinding;
import com.minewtech.sensorKit.bean.SensorHistoryData;
import com.minewtech.sensorKit.bean.history.ThHistoryData;

import java.util.ArrayList;
import java.util.List;

public class HistoryDataAdapter extends RecyclerView.Adapter<HistoryDataAdapter.HistoryHolder> {

    private List<SensorHistoryData> dataList = new ArrayList<SensorHistoryData>();

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.bindData(dataList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addData(ArrayList<SensorHistoryData> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public SensorHistoryData getItem(int position) {
        return dataList.get(position);
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        private ItemHistoryBinding itemHistoryBinding;
        public HistoryHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            itemHistoryBinding = binding;
        }

        public void bindData(SensorHistoryData history) {
            itemHistoryBinding.setHistoryData(history);
            itemHistoryBinding.executePendingBindings();
        }
    }
}
