package com.servilink.webmasterapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.servilink.webmasterapp.Model.Device;
import com.servilink.webmasterapp.Model.ManualEntry;
import com.servilink.webmasterapp.ParameterLIst;
import com.servilink.webmasterapp.R;

import java.io.Serializable;
import java.util.List;

public class ShowDataAdapter extends RecyclerView.Adapter<ShowDataAdapter.MyViewHolder> {
    List<ManualEntry> deviceItemList;

    Activity context;

    public ShowDataAdapter(Activity context, List<ManualEntry> deviceItemList) {
        this.context = context;
        this.deviceItemList = deviceItemList;

    }

    @NonNull
    @Override
    public ShowDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_showdata, parent, false);
        return new ShowDataAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ShowDataAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tagValue.setText(deviceItemList.get(position).getValue());
        holder.tagName.setText(deviceItemList.get(position).getTagId()+"");
        holder.entryDateTime.setText(deviceItemList.get(position).getDateTime());

    }


    @Override
    public int getItemCount() {
        return deviceItemList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tagName, tagValue,entryDateTime;

        public MyViewHolder(View view) {
            super(view);
            tagName = view.findViewById(R.id.tagName);
            tagValue = view.findViewById(R.id.tagValue);
            entryDateTime=view.findViewById(R.id.entryDateTime);
        }
    }
}

