package com.lomo.demo;

public class AdPcmTool {

    static {
        System.loadLibrary("ambientlight");
    }

    public native byte[] adpcmToPcmFromJNI(byte[] adpcm);
}
