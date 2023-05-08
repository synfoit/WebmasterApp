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
import com.servilink.webmasterapp.ParameterLIst;
import com.servilink.webmasterapp.R;

import java.io.Serializable;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {
    List<Device> deviceItemList;

    Activity context;

    public DeviceListAdapter(Activity context, List<Device> deviceItemList) {
        this.context = context;
        this.deviceItemList = deviceItemList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_devicelist, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.device_name.setText(deviceItemList.get(position).getPlcName());
        int index = position + 1;
        holder.imageView.setText(String.valueOf(index));
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, ParameterLIst.class);
            intent.putExtra("deviceName",deviceItemList.get(position).getPlcName());
            intent.putExtra("tagIdList",(Serializable)deviceItemList.get(position).getTagList());
            context.startActivity(intent);
        });


    }


    @Override
    public int getItemCount() {
        return deviceItemList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView device_name, imageView;
        ImageView im_arrow;
        public MyViewHolder(View view) {
            super(view);
            device_name = view.findViewById(R.id.dr_text);
            imageView = view.findViewById(R.id.imageView);
            im_arrow=view.findViewById(R.id.im_arrow);
        }
    }
}
