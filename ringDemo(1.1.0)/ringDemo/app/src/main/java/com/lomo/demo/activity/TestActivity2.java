package com.lomo.demo.activity;

import static com.lomo.demo.activity.TestActivity.savePcmFile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lm.sdk.BLEService;
import com.lm.sdk.LmAPI;
import com.lm.sdk.LogicalApi;
import com.lm.sdk.inter.IBloodPressureListener;
import com.lm.sdk.inter.IQ2Listener;
import com.lm.sdk.inter.IResponseListener;
import com.lm.sdk.inter.ITempListener;
import com.lm.sdk.mode.GreenAndIrBean;
import com.lm.sdk.mode.SleepBean;
import com.lm.sdk.mode.SystemControlBean;
import com.lm.sdk.utils.BLEUtils;
import com.lm.sdk.utils.CMDUtils;
import com.lm.sdk.utils.Logger;
import com.lm.sdk.utils.UtilSharedPreference;
import com.lomo.demo.AdPcmTool;
import com.lomo.demo.R;
import com.lomo.demo.adapter.DeviceBean;
import com.lomo.demo.application.App;
import com.lomo.demo.base.BaseActivity;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class TestActivity2 extends BaseActivity implements IResponseListener, View.OnClickListener {

    TextView tv_result2;
    Button bt_calculate_sleep;
    Button bt_open_audio;
    Button bt_close_audio;
    private Handler handler = new Handler();  // 创建一个 Handler 实例
    private Runnable runnable;                 // 创建一个 Runnable 来定义任务
    String outputPath = com.lomo.demo.FileUtil.getSDPath(App.getInstance(), "保存" + ".pcm");
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        LmAPI.addWLSCmdListener(this,this);
        tv_result2 = findViewById(R.id.tv_result2);
        findViewById(R.id.bt_unpair).setOnClickListener(this);
        findViewById(R.id.bt_set_HID).setOnClickListener(this);
        findViewById(R.id.bt_get_HID).setOnClickListener(this);
        findViewById(R.id.bt_get_HID_code).setOnClickListener(this);
    }

    @Override
    public void SystemControl(SystemControlBean systemControlBean) {

    }

    @Override
    public void lmBleConnecting(int code) {

    }

    @Override
    public void lmBleConnectionSucceeded(int code) {

    }

    @Override
    public void lmBleConnectionFailed(int code) {

    }

    @Override
    public void VERSION(byte type, String version) {

    }

    @Override
    public void syncTime(byte datum, byte[] time) {

    }

    @Override
    public void stepCount(byte[] bytesToInt) {

    }

    @Override
    public void clearStepCount(byte data) {

    }

    @Override
    public void battery(byte b, byte datum) {

    }

    @Override
    public void timeOut() {

    }

    @Override
    public void saveData(String str_data) {

    }

    @Override
    public void reset(byte[] data) {

    }

    @Override
    public void setCollection(byte result) {

    }

    @Override
    public void getCollection(byte[] data) {

    }

    @Override
    public void cleanHistory(byte data) {

    }

    @Override
    public void setBlueToolName(byte data) {

    }

    @Override
    public void readBlueToolName(byte len, String name) {

    }

    @Override
    public void stopRealTimeBP(byte isSend) {

    }

    @Override
    public void BPwaveformData(byte seq, byte number, String waveDate) {

    }

    @Override
    public void onSport(int type, byte[] data) {

    }

    @Override
    public void breathLight(byte time) {

    }

    @Override
    public void SET_HID(byte result) {
        if(result == (byte)0x00){
            postView("\n设置HID失败");
        }else if(result == (byte)0x01){
            postView("\n设置HID成功");
        }
    }

    @Override
    public void GET_HID(byte touch, byte gesture, byte system) {
        postView("\n当前触摸hid模式：" + touch + "\n当前手势hid模式：" + gesture + "\n当前系统：" + system);
    }

    @Override
    public void GET_HID_CODE(byte[] bytes) {
        Logger.show("getHidCode", "支持与否：" + bytes[0] + " 触摸功能：" + bytes[1] + " 空中手势：" + bytes[9] + "\n");

        Logger.show("byteToBitString", byteToBitString(bytes[1]));
        char[] touchModes = byteToBitString(bytes[1]).toCharArray();
        char[] gestureModes = byteToBitString(bytes[9]).toCharArray();

        if (bytes[0] == 0) {
            postView("\n不支持HID功能");
        } else {
            postView("\n支持HID功能");
        }
        if ("00000000".equals(byteToBitString(bytes[1]))) {//不支持触摸功能
            postView("\n不支持触摸功能");
        } else {
            postView("\n支持触摸功能");
        }

        if (touchModes[touchModes.length - 1] == '1') {//拍照
            postView("\n支持触摸拍照功能");
        } else {
            postView("\n不支持触摸拍照功能");
        }

        if (touchModes[touchModes.length - 2] == '1') {//短视频
            postView("\n支持触摸短视频功能");
        } else {
            postView("\n不支持触摸短视频功能");
        }

        if (touchModes[touchModes.length - 3] == '1') {//音乐
            postView("\n支持触摸音乐功能");
        } else {
            postView("\n不支持触摸音乐功能");
        }

        if (touchModes[touchModes.length - 5] == '1') {//音频
            postView("\n支持触摸音频功能");
        } else {
            postView("\n不支持触摸音频功能");
        }

        if ("00000000".equals(byteToBitString(bytes[9]))) {//不支持空中手势
            postView("\n不支持空中手势功能");
        } else {
            postView("\n支持空中手势功能");
        }

        if (gestureModes[gestureModes.length - 1] == '1') {//拍照
            postView("\n支持手势拍照功能");
        } else {
            postView("\n不支持手势拍照功能");
        }

        if (gestureModes[gestureModes.length - 2] == '1') {//短视频
            postView("\n支持手势短视频功能");
        } else {
            postView("\n不支持手势短视频功能");
        }

        if (gestureModes[gestureModes.length - 3] == '1') {//音乐
            postView("\n支持手势音乐功能");
        } else {
            postView("\n不支持手势音乐功能");
        }

        if (gestureModes[gestureModes.length - 5] == '1') {//打响指（拍照）
            postView("\n支持打响指（拍照）功能");
        } else {
            postView("\n不支持打响指（拍照）功能");
        }
    }
    public static String byteToBitString(byte b) {
        StringBuilder bitString = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            bitString.append((b >> i) & 1); // 移位并与1进行与操作，获取最低位的bit
        }
        return bitString.toString();
    }
    @Override
    public void setAudio(short totalLength, int index, byte[] audioData) {

    }

    @Override
    public void stopHeart(byte data) {

    }

    @Override
    public void stopQ2(byte data) {

    }

    @Override
    public void GET_ECG(byte[] bytes) {

    }

    @Override
    public void CONTROL_AUDIO(byte[] bytes) {
        postView("\n音频结果：" + Arrays.toString(bytes));
        byte[] adToPcm = new AdPcmTool().adpcmToPcmFromJNI(bytes);

        savePcmFile(outputPath,adToPcm);
        postView("\n已保存：" + outputPath);
    }

    @Override
    public void motionCalibration(byte sport_count) {

    }

    @Override
    public void stopBloodPressure(byte data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_set_HID:
                byte[] hidBytes = new byte[3];
                hidBytes[0] = 0x04;             //上传实时音频
                hidBytes[1] = (byte) 0xFF;      //关闭
                hidBytes[2] = 0x00;             //系统类型 0：安卓  1：IOS  2：鸿蒙
                LmAPI.SET_HID(hidBytes,TestActivity2.this);
                break;
            case R.id.bt_get_HID:
                LmAPI.GET_HID();//获取HID现在的模式
                break;
            case R.id.bt_get_HID_code:
                LmAPI.GET_HID_CODE((byte)0x00);  //系统类型 0：安卓  1：IOS  2：windows
                break;
            case R.id.bt_unpair://解绑,返回扫描界面
                postView("\n解绑\n");
                BLEUtils.setGetToken(false);
                BLEUtils.disconnectBLE(this);
                BLEUtils.removeBond(BLEService.getmBluetoothDevice());
                UtilSharedPreference.saveString(TestActivity2.this,"address","");
                Intent intent = new Intent(TestActivity2.this, MainActivity.class);

                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    /**
     * @param value 打印的log
     */
    public void postView(String value) {
        tv_result2.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_result2.setScrollbarFadingEnabled(false);//滚动条一直显示
        tv_result2.append(value);
        int scrollAmount = tv_result2.getLayout().getLineTop(tv_result2.getLineCount()) - tv_result2.getHeight();
        if (scrollAmount > 0)
            tv_result2.scrollTo(0, scrollAmount);
        else
            tv_result2.scrollTo(0, 0);

    }
}
