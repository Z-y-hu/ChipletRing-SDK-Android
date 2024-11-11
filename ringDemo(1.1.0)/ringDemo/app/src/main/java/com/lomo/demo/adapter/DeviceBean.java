package com.lomo.demo.adapter;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 作者: Sunshine
 * 时间: 2017/11/10.
 * 邮箱: 44493547@qq.com
 * 描述:
 */

public class DeviceBean implements Parcelable {
    private BluetoothDevice device;
    private int rssi = 0;
    private String hidDevice;
    public DeviceBean(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }

    protected DeviceBean(Parcel in) {
        device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        rssi = in.readInt();
    }

    public static final Creator<DeviceBean> CREATOR = new Creator<DeviceBean>() {
        @Override
        public DeviceBean createFromParcel(Parcel in) {
            return new DeviceBean(in);
        }

        @Override
        public DeviceBean[] newArray(int size) {
            return new DeviceBean[size];
        }
    };

    public BluetoothDevice getDevice() {
        return device;
    }
    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
    public int getRssi() {
        return rssi;
    }
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
    public String getHidDevice() {
        return hidDevice;
    }

    public void setHidDevice(String hidDevice) {
        this.hidDevice = hidDevice;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(device, i);
        parcel.writeInt(rssi);
    }
}
