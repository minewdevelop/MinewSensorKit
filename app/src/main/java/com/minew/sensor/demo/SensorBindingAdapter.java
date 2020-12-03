package com.minew.sensor.demo;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.minewtech.sensorKit.config.SensorType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SensorBindingAdapter {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static Date date = new Date();

    @BindingAdapter("type")
    public static void bindSensorType(TextView tv, int type) {
        if (type == SensorType.DOOR_TYPE) {
            tv.setText("Door");
        } else if (type == SensorType.HT_TYPE) {
            tv.setText("TH");
        } else {
            tv.setText("Unknown");
        }
    }

    @BindingAdapter("time")
    public static void bindHistoryTime(TextView tv, long time) {
        date.setTime(time);
        tv.setText(dateFormat.format(time));
    }
}
