package com.lomo.demo.activity;

import static com.lomo.demo.application.FileUtils.writeTxtToFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
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
import com.lm.sdk.DataApi;
import com.lm.sdk.LmAPI;
import com.lm.sdk.LogicalApi;
import com.lm.sdk.greenDao.DayLastBeanDao;
import com.lm.sdk.inter.IHeartListener;
import com.lm.sdk.inter.IHistoryListener;
import com.lm.sdk.inter.IQ2Listener;
import com.lm.sdk.inter.IResponseListener;
import com.lm.sdk.mode.DayLastBean;
import com.lm.sdk.mode.HistoryDataBean;
import com.lm.sdk.utils.BLEUtils;
import com.lm.sdk.utils.ByteKit;
import com.lm.sdk.utils.ImageSaverUtil;
import com.lm.sdk.utils.TimeUtils;
import com.lomo.demo.R;
import com.lomo.demo.application.App;
import com.lomo.demo.base.BaseActivity;

import org.greenrobot.greendao.query.WhereCondition;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestActivity extends BaseActivity implements IResponseListener, View.OnClickListener {

    TextView tv_result;
    private String mac = "B2:20:11:00:00:C6";

    Button bt_connect;
    Button bt_battery;
    Button bt_version;
    Button bt_sync_time;
    Button bt_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LmAPI.addWLSCmdListener(this,this);
        bt_connect = findViewById(R.id.bt_connect);
        bt_test = findViewById(R.id.bt_test);
        bt_battery = findViewById(R.id.bt_battery);
        bt_version = findViewById(R.id.bt_version);
        bt_sync_time = findViewById(R.id.bt_sync_time);
        tv_result = findViewById(R.id.tv_result);

        bt_connect.setOnClickListener(this);
        bt_test.setOnClickListener(this);
        bt_battery.setOnClickListener(this);
        bt_version.setOnClickListener(this);
        bt_sync_time.setOnClickListener(this);
        tv_result.setText("数据区域");
        /**
        * 申请权限
        */
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

    private static AlertDialog info;
    private void setMessage(String content,int i){
        if (info == null){
            info = new AlertDialog.Builder(TestActivity.this).create();
            info.setMessage(content);
        }else{
            info.setMessage(content);
        }
        info.show();
        if(i == 100){
            info.dismiss();
            Toast.makeText(this, "测量完成", Toast.LENGTH_SHORT).show();
        }
    }
    private static Toast toast;
    public static void showToast(Context context, String content) {

        if (toast == null) {
            toast = Toast.makeText(context,content,Toast.LENGTH_SHORT);
        } else {

            toast.setText(content);
        }
        toast.show();
    }

    private String hrhrv(byte[] data){
        String str = "";
        for (int i = 0; i < data.length/10; i++) {
            ByteBuffer buffer = ByteBuffer.wrap(data,i * 10,10);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            int green = buffer.getInt();
            short accX = buffer.getShort();
            short accY = buffer.getShort();
            short accZ = buffer.getShort();
            //green是心率，X,Y,Z为加速度。视需求返回
            str += "green:" + green + ";accX:" + accX +";accY:" + accY + ";accZ:" + accZ + "\r\n";
        }
        return str;
    }
    private String spo2hr(byte[] data){
        String str = "";
        for (int i = 0; i < data.length/14; i++) {
            ByteBuffer buffer = ByteBuffer.wrap(data,i * 14,14);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int red = buffer.getInt();
            int ied = buffer.getInt();
            short accX = buffer.getShort();
            short accY = buffer.getShort();
            short accZ = buffer.getShort();
            //red:红色,ied:红外,X,Y,Z为加速度。视需求返回
            str += "red:" + red + ";ied:" + ied + ";accX:" + accX + ";accY:" + accY + ";accZ:" + accZ + "\r\n";
        }
        return str;
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
            Log.d("TAG", "===" + device.getName());
            Log.d("TAG", "===" + device.getAddress());
            Log.d("TAG","onLeScan bytes = " + Arrays.toString(bytes));
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
        LmAPI.setDebug(true);
    }

    @Override
    public void lmBleConnectionFailed(int i) {

    }

    @Override
    public void VERSION(byte b, String s) {
        tv_result.append("\n获取版本信息成功"+s);
    }

    @Override
    public void syncTime(byte b,byte[] var2) {
        tv_result.append("\n"+(b==0?"同步时间成功":"同步时间失败"));
    }

    @Override
    public void stepCount(byte[] bytes, byte b) {

    }

//    @Override
//    public void stepCount(byte[] bytes) {
//
//    }

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
    public void collection(byte[] bytes, byte b) {

    }

    @Override
    public void BPwaveformData(byte b, byte b1, String s) {

    }

//    @Override
//    public void collection(byte[] bytes) {
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_connect:
                connect(mac);
                LmAPI.READ_TIME();
                break;
            case R.id.bt_test:
                 tv_result.append("\n开始测量心率");
                 LmAPI.GET_HEART_ROTA((byte) 1, new IHeartListener() {

                    @Override
                    public void progress(int progress) {
                        Log.d("TAG", "progress:" + progress);
                        //tv_result.append("\n正在测量心率..." + String.format("%02d%%",progress));
                        //showToast(TestActivity.this,"正在测量心率..." + String.format("%02d%%",progress));
                        setMessage("正在测量心率..." + String.format("%02d%%",progress),progress);
                    }

                    @Override
                    public void resultData(int heart, int heartRota, int yaLi, int temp) {
                        Log.d("TAG", "resultData success:");
                    }

                     @Override
                     public void waveformData(byte seq, byte number, String s) {

                     }

                    @Override
                    public void error(int value) {
                        Log.d("TAG", "success:");
                    }

                    @Override
                    public void success() {
                        Log.d("TAG", "success:");
                    }
                });

                //skipPage();
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