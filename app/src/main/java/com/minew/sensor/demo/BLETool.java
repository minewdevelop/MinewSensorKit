package com.minew.sensor.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.minewtech.sensorKit.enums.BluetoothState;

public class BLETool {
    public static boolean isBluetoothTurnOn(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }
}
