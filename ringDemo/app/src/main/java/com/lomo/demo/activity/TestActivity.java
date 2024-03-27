package com.lomo.demo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lm.sdk.LmAPI;
import com.lm.sdk.inter.IResponseListener;
import com.lm.sdk.utils.BLEUtils;
import com.lomo.demo.R;
import com.lomo.demo.application.App;
import com.lomo.demo.base.BaseActivity;

import java.util.List;

public class TestActivity extends BaseActivity implements IResponseListener, View.OnClickListener {

    TextView tv_result;
    private String mac = "B2:20:11:00:00:20";

    Button bt_connect;
    Button bt_battery;
    Button bt_version;
    Button bt_sync_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LmAPI.addWLSCmdListener(this,this);
        bt_connect = findViewById(R.id.bt_connect);
        bt_battery = findViewById(R.id.bt_battery);
        bt_version = findViewById(R.id.bt_version);
        bt_sync_time = findViewById(R.id.bt_sync_time);
        tv_result = findViewById(R.id.tv_result);

        bt_connect.setOnClickListener(this);
        bt_battery.setOnClickListener(this);
        bt_version.setOnClickListener(this);
        bt_sync_time.setOnClickListener(this);
        tv_result.setText("数据区域");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            XXPermissions.with(this)
                    // 申请多个权限
                    .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION, Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_CONNECT, Permission.BLUETOOTH_ADVERTISE,Permission.MANAGE_EXTERNAL_STORAGE)
                    // 设置权限请求拦截器（局部设置）
                    //.interceptor(new PermissionInterceptor())
                    // 设置不触发错误检测机制（局部设置）
                    //.unchecked()
                    .request(new OnPermissionCallback() {

                        @Override
                        public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                            if (!allGranted) {
                                Toast.makeText(getApplicationContext(),"获取部分权限成功，但部分权限未正常授予",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                            if (doNotAskAgain) {
                                Toast.makeText(getApplicationContext(),"被永久拒绝授权，请手动授予录音和日历权限",Toast.LENGTH_SHORT).show();
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(TestActivity.this, permissions);
                            } else {
                                Toast.makeText(getApplicationContext(),"获取录音和日历权限失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            XXPermissions.with(TestActivity.this)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                            if (!allGranted) {
                                Toast.makeText(getApplicationContext(),"获取部分权限成功，但部分权限未正常授予",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                            if (doNotAskAgain) {
                                Toast.makeText(getApplicationContext(),"被永久拒绝授权，请手动授予权限",Toast.LENGTH_SHORT).show();
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(TestActivity.this, permissions);
                            } else {
                                Toast.makeText(getApplicationContext(),"获取权限失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    /**
     * 连接设备
     * @param mac
     */
    private void connect(String mac) {
        this.mac = mac;
        BluetoothAdapter bluetoothAdapter = App.getInstance().getBluetoothAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            // 弹出对话框，打开蓝牙
            Toast.makeText(this, "未打开蓝牙，请打开后操作", Toast.LENGTH_SHORT).show();
            return;
        }
        BLEUtils.stopLeScan(this, leScanCallback);
        BLEUtils.startLeScan(this, leScanCallback);
        tv_result.append("\n开始寻找设备："+mac);
    }

    @SuppressLint("MissingPermission")
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] bytes) {
            if (device == null || TextUtils.isEmpty(device.getName())) {
                return;
            }
            Log.e("TAG", "===" + device.getName());
            Log.e("TAG", "===" + device.getAddress());
            if (device.getAddress().equalsIgnoreCase(mac)) {
                BLEUtils.stopLeScan(TestActivity.this, leScanCallback);
                BLEUtils.connectLockByBLE(TestActivity.this, device);
                tv_result.append("\n开始连接设备："+device.getAddress());
            }
        }
    };


    @Override
    public void lmBleConnecting(int i) {

    }

    @Override
    public void lmBleConnectionSucceeded(int i) {
        BLEUtils.setGetToken(true);
        tv_result.append("\n连接成功");
    }

    @Override
    public void lmBleConnectionFailed(int i) {

    }

    @Override
    public void VERSION(byte b, String s) {
        tv_result.append("\n获取版本信息成功"+s);
    }

    @Override
    public void syncTime(byte b) {
        tv_result.append("\n"+(b==0?"同步时间成功":"同步时间失败"));
    }

    @Override
    public void stepCount(byte[] bytes) {

    }

    @Override
    public void battery(byte b, byte b1) {
        tv_result.append("\n获取电量成功："+b1);
    }

    @Override
    public void timeOut() {

    }

    @Override
    public void saveData(String s) {

    }

    @Override
    public void reset(byte[] bytes) {

    }

    @Override
    public void collection(byte[] bytes) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_connect:
                connect(mac);
                break;
            case R.id.bt_sync_time:
                tv_result.append("\n开始同步时间");
                LmAPI.SYNC_TIME();
                break;
            case R.id.bt_version:
                tv_result.append("\n开始获取版本信息");
                LmAPI.GET_VERSION((byte) 0x00);
                break;
            case R.id.bt_battery:
                tv_result.append("\n开始获取电量信息");
                LmAPI.GET_BATTERY((byte) 0x00);
                break;
            default:
                break;
        }
    }
}