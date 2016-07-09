package com.example.polyun.appicshowcase;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.customlbs.library.model.Zone;

import java.util.*;

/**
 * Created by Polyun on 06.07.2016.
 */
public class Splash extends Activity {

    private BluetoothAdapter BTAdapter;

    private static Long building_id = (long) 0;

    private List<DeviceItem> mAdapter;

    public static int REQUEST_BLUETOOTH = 1;

    private final BroadcastReceiver bReciever = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("DEVICELIST", "Bluetooth device found\n");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                float rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String uuid = BluetoothDevice.EXTRA_UUID;
                // Create a new device item
                DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), rssi, uuid, "false");
                Log.d("DEVICELIST", "Found device " + device.getName() + " Address: "+ device.getAddress() + " RSSI:" + rssi+ " UUID:" + uuid + "Building_ID: " + Location_Store.get_location(newDevice.getAddress()) + "\n");
                building_id = Location_Store.get_location(newDevice.getAddress());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Thread timerThread = new Thread(){
            public void run(){

                try{
                    Log.d("DEVICELIST", "Start timer thread \n" );
                    BTAdapter = BluetoothAdapter.getDefaultAdapter();
                    // Phone does not support Bluetooth so let the user know and exit.
                    if (BTAdapter == null) {
                        Log.d("DEVICELIST","No Bluetooth active \n");
                    }

                    if (!BTAdapter.isEnabled()) {
                        Log.d("DEVICELIST","Bluetooth not enabled \n");
                    }
                    else{
                        Log.d("DEVICELIST","Bluetooth enabled \n");
                        registerReceiver(bReciever,  new IntentFilter(BluetoothDevice.ACTION_FOUND));

                        BTAdapter.startDiscovery();
                    }
                    int attempts = 10;
                    while(building_id == (long) 0 && attempts > 0){
                        sleep(1000);
                        Log.d("DEVICELIST","Attempts left" + attempts +"\n");
                        attempts -= 1;
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Log.d("DEVICELIST","Identified Building ID: "+building_id+" \n");
                    Intent intent = new Intent(Splash.this, IndoorsActivity.class);
                    if (building_id !=  (long) 0) {
                        intent.putExtra("Building_ID", building_id);
                        startActivity(intent);
                    }
                    else{
                        Log.d("DEVICELIST","Could not identify building: "+building_id+" \n");
                        finish();
                    }
                }
            }
        };
        timerThread.start();



    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(bReciever);
        super.onDestroy();
    }
}
