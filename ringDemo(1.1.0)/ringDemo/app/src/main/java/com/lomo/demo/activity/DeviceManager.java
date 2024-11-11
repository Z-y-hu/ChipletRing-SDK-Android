package com.lomo.demo.activity;

import com.lomo.demo.adapter.DeviceBean;

import java.util.HashMap;
import java.util.Map;

public class DeviceManager {
    //创建一个字典，存储mac和DeviceBean进行对应
    public static Map<String, DeviceBean> deviceMap = new HashMap<>();
}
