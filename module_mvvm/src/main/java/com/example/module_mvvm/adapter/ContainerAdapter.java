package com.example.module_mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.module_mvvm.databinding.ItemCellModelBinding;
import com.example.module_mvvm.model.bean.CellModel;

import java.util.List;

public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.CellModelViewHolder> {

    private List<CellModel> cellModels;

    public ContainerAdapter(List<CellModel> cellModels) {
        this.cellModels = cellModels;
    }

    @NonNull
    @Override
    public CellModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCellModelBinding itemBinding = ItemCellModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        CellModelViewHolder viewHolder = new CellModelViewHolder(itemBinding.getRoot());
        viewHolder.setItemBinding(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CellModelViewHolder holder, int position) {
        holder.getItemBinding().setCellModel(cellModels.get(position));
    }

    @Override
    public int getItemCount() {
        return cellModels.size();
    }

    static class CellModelViewHolder extends RecyclerView.ViewHolder{
        ItemCellModelBinding itemBinding;

        public CellModelViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ItemCellModelBinding getItemBinding() {
            return itemBinding;
        }

        public void setItemBinding(ItemCellModelBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
    }
}
