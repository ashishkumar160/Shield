package com.iohertz.ashish.shield.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iohertz.ashish.shield.Model.Device;
import com.iohertz.ashish.shield.R;

import java.util.List;

/**
 * Created by ashish on 6/3/17.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private Context mContext;
    private List<Device> deviceList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.device_name);
            thumbnail = (ImageView) view.findViewById(R.id.device_thumbnail);
        }
    }

    public DeviceAdapter(Context mContext, List<Device> deviceList) {
        this.mContext = mContext;
        this.deviceList = deviceList;
    }

    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_card, parent, false);
        return new DeviceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceAdapter.ViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.name.setText(device.getName());
        Glide.with(mContext).load(device.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

}
