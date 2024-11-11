package com.lomo.demo.application;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.lm.sdk.LmAPI;
import com.lomo.demo.adapter.DeviceBean;

/**
 * @author Lizhao
 */
public class App extends Application {
    private static App app;
    private DeviceBean deviceBean;
    private BluetoothAdapter mBluetoothAdapter;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        LmAPI.init(this);
        LmAPI.setDebug(true);
    }


    public static App getInstance() {
        return app;
    }
    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;

    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }


    public BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getInstance().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        return mBluetoothAdapter;
    }
}
