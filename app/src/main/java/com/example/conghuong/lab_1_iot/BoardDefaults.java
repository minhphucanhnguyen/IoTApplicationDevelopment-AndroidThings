package com.example.conghuong.lab_1_iot;

import android.os.Build;

public class BoardDefaults {
    private static final String DEVICE_RPI3="rpi3";
    public static String[] getGPIOForRGBLED(){
        switch (Build.DEVICE) {
            case DEVICE_RPI3:
                String[] Pins = new String[6];
                // LED1
                Pins[0] = "BCM26";
                Pins[1] = "BCM19";
                Pins[2] = "BCM13";
                // Button
                Pins[3] = "BCM21";
                // Green pin of LED2
                Pins[4] = "BCM6";
                // Blue pin of LED3
                Pins[5] = "BCM5";
                return Pins;
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }
}
