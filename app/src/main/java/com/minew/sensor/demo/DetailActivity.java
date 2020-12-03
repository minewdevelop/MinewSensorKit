package com.minew.sensor.demo;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.minewtech.sensorKit.bean.SensorHistoryData;
import com.minewtech.sensorKit.bean.SensorModule;
import com.minewtech.sensorKit.bean.history.DoorHistoryData;
import com.minewtech.sensorKit.bean.history.ThHistoryData;
import com.minewtech.sensorKit.config.SensorType;
import com.minewtech.sensorKit.interfaces.outside.OnReceiveDataListener;
import com.minewtech.sensorKit.manager.MinewSensorCenterManager;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private MinewSensorCenterManager manager;
    private String macAddress;
    private HistoryDataAdapter adapter;
    private final ArrayList<SensorHistoryData> mHistoryData = new ArrayList<>();
    private KProgressHUD mHud;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail");
        macAddress = getIntent().getStringExtra("macAddress");
        manager = MinewSensorCenterManager.getInstance(this);

        RecyclerView rvHistory = findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryDataAdapter();
        rvHistory.setAdapter(adapter);
        rvHistory.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        mHud = KProgressHUD
                .create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        SensorModule module = manager.getDeviceByAddress(macAddress);

        mHud.setLabel("reading");
        mHud.show();
        if (module.getType() == SensorType.HT_TYPE) {
            manager.readThHistoryData(macAddress, new OnReceiveDataListener<ThHistoryData>() {
                @Override
                public void receiverData(String s, List<ThHistoryData> list) {
                    updateList(list);
                }
            });
        } else if (module.getType() == SensorType.DOOR_TYPE) {
            manager.readDoorHistoryData(macAddress, new OnReceiveDataListener<DoorHistoryData>() {
                @Override
                public void receiverData(String s, List<DoorHistoryData> list) {
                    updateList(list);
                }
            });
        }
    }

    private <T extends SensorHistoryData> void updateList(List<T> list) {
        mHistoryData.clear();
        mHistoryData.addAll(list);
        adapter.addData(mHistoryData);
        mHandler.postDelayed(() -> mHud.dismiss(), 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.disConnect(macAddress);
    }
}
