package com.minew.sensor.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.minewtech.sensorKit.bean.SensorModule;
import com.minewtech.sensorKit.enums.SensorConnectionState;
import com.minewtech.sensorKit.interfaces.outside.OnConnStateListener;
import com.minewtech.sensorKit.interfaces.outside.OnScanSensorResultListener;
import com.minewtech.sensorKit.manager.MinewSensorCenterManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnScanSensorResultListener {

    private SwipeRefreshLayout srlScan;
    private RecyclerView rvScan;
    private ScanListAdapter adapter;
    private MinewSensorCenterManager manager;

    private Handler handler = new Handler();
    private Runnable stopScanRunnable = this::stopScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        manager = MinewSensorCenterManager.getInstance(this);
        if (BLETool.isBluetoothTurnOn(this)) {
            requestLocationPermissions();
        } else {
            Toast.makeText(this, "Attention BLE!!", Toast.LENGTH_SHORT).show();
        }

        initListener();
    }

    private void initView() {
        srlScan = findViewById(R.id.srl_scan);
        rvScan = findViewById(R.id.rv_scan);

        adapter = new ScanListAdapter();
        srlScan.setOnRefreshListener(this);
        rvScan.setLayoutManager(new LinearLayoutManager(this));
        rvScan.setAdapter(adapter);
        rvScan.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        adapter.setOnItemClickListener((view, position) -> {
            SensorModule itemData = adapter.getItem(position);
            stopScan();
            manager.connect(MainActivity.this, itemData);
        });
    }

    private void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startScan();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            } else {
                startScan();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            } else {
                startScan();
            }
        }
    }

    private void initListener() {

        manager.setOnConnStateListener((macAddress, sensorConnectionState) -> {
            showToast(macAddress + " " + sensorConnectionState.name());
            switch (sensorConnectionState) {

                case VerifyPassword:
                    manager.sendPassword(macAddress,"minew123");
                    break;
                case Connected:

                    break;
                case Disconnect:

                    break;
                case PasswordError:

                    break;
                case ConnectComplete:
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("macAddress", macAddress);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onRefresh() {
        stopScan();
        adapter.clearData();
        startScan();
        srlScan.setRefreshing(false);
    }

    private void startScan() {
        manager.startScan(this);
        handler.postDelayed(stopScanRunnable, 60 * 1000 * 1000);
    }

    private void stopScan() {
        manager.stopScan();
        handler.removeCallbacks(stopScanRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(stopScanRunnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {

            startScan();
        }
    }

    private Toast toast;

    private void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    public void onScanSensorResult(ArrayList<SensorModule> arrayList) {
        Log.e("tetetetete", "scanResult: " + arrayList.size());
        adapter.addNewData(arrayList);
    }
}