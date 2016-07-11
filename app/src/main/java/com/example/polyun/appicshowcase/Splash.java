package com.example.polyun.appicshowcase;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.customlbs.library.model.Zone;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Polyun on 06.07.2016.
 */
public class Splash extends Activity {

    private BluetoothAdapter BTAdapter;

    private static Long building_id = null;
    private static boolean found = false;
    private List<DeviceItem> mAdapter;

    private void isFound(boolean f){
        found = f;
    }

    public static void setBuilding_id(Long building_id) {
        Splash.building_id = building_id;
    }

    public static Long getBuilding_id() {
        return building_id;
    }

    public static boolean isFound() {
        return found;
    }

    public static int REQUEST_BLUETOOTH = 1;

    private final BroadcastReceiver bReciever = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                float rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String address = device.getAddress();
                String uuid = intent.getStringExtra(BluetoothDevice.EXTRA_UUID);
                ParcelUuid[] uuids= device.getUuids();
                device.fetchUuidsWithSdp();
                String name = device.getName();

                Long building_id_temp = Location_Store.get_location(address);
                setBuilding_id(building_id_temp);
                isFound(true);
                Log.d("DEVICELIST", "Found device " + device.getName() + " Address: "+ device.getAddress() + " RSSI:" + rssi+ " UUID:" + uuid + "Building_ID: " + building_id_temp + "\n");
                /*for (ParcelUuid single_uuid: uuids) {
                    Log.d("DEVICELIST", "Going through UUIDs ");
                }*/

                //Log.d("DEVICELIST", "Found uuids " + uuids.toString() + "\n");

                // Create a new device item
                //DeviceItem newDevice = new DeviceItem(name, address, rssi, uuid, "false");

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
                Log.d("DEVICELIST","\n STARTING \n");
                try{
                    sleep(1000);
                    BTAdapter = ((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)).getAdapter();
                    //BluetoothAdapter.getDefaultAdapter();
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
                    sleep(1000);
                    int attempts = 5;
                    while(isFound() == false && attempts > 0){
                        Log.d("DEVICELIST","Could not identify building: "+getBuilding_id()+ "   Attempts left: " + attempts + " Found: " + isFound() +"\n");
                        attempts -= 1;
                        // sometimes discovery just finishes by itself. make sure it is started every at every attempt again for the next attempt.
                        if (!BTAdapter.isDiscovering()){
                            BTAdapter.startDiscovery();
                        }
                        sleep(3000);
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Log.d("DEVICELIST","Identified Building ID: "+building_id+" \n");
                    Intent intent = new Intent(Splash.this, IndoorsActivity.class);
                    if (building_id != null) {
                        intent.putExtra("Building_ID", building_id);
                        startActivity(intent);
                    }
                    else{
                        Log.d("DEVICELIST","Could not identify building: "+building_id+" \n");
                        Log.d("DEVICELIST", "Starting with default Building ID");
                        intent.putExtra("Building_ID", (long) 783306659);
                        startActivity(intent);
                        //finish();
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
