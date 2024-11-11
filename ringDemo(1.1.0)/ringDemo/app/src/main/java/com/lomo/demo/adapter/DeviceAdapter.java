package com.lomo.demo.adapter;


import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lomo.demo.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * 我的行程填充器
 * Created by sunshine on 2017/3/7.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.StrokeHolder> implements Comparator<DeviceBean> {


    private OnItemClickListener onItemClickListener;
    private List<DeviceBean> dataEntityList = new ArrayList<>();

    @Override
    public StrokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_bluetooth_info, null);
        return new StrokeHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(StrokeHolder holder, int position) {
        DeviceBean resultEntity = dataEntityList.get(position);
        holder.setData(resultEntity);
    }

    @Override
    public int getItemCount() {
        return Math.min(dataEntityList.size(), 50);
    }

    public DeviceBean getItemBean(int position) {
        return dataEntityList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateData(DeviceBean deviceBean) {
        dataEntityList.add(deviceBean);
        dataEntityList.sort(this);
        notifyDataSetChanged();
    }

    public void clearData() {
        dataEntityList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int compare(DeviceBean lhs, DeviceBean rhs) {
        return rhs.getRssi() - lhs.getRssi();
    }

    public void updateStatus() {
        notifyDataSetChanged();
    }

    class StrokeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDeviceName;
        TextView tvDeviceMac;
        TextView tvConnect;
        OnItemClickListener onItemClick;

        public StrokeHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tvDeviceName = (TextView) itemView.findViewById(R.id.tv_device_name);
            tvDeviceMac = (TextView) itemView.findViewById(R.id.tv_device_mac);
            tvConnect = (TextView) itemView.findViewById(R.id.img_connect);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.onItemClick = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @SuppressLint("MissingPermission")
        public void setData(DeviceBean resultEntity) {
            tvDeviceName.setText(resultEntity.getDevice().getName());
            tvDeviceMac.setText(resultEntity.getDevice().getAddress());
            tvConnect.setText(String.valueOf(resultEntity.getRssi()));
        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onItemClick(dataEntityList.get(getPosition()), getPosition());
            }
        }
    }
}
