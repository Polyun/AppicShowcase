package com.example.polyun.appicshowcase;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import java.util.*;

/**
 * Created by Polyun on 06.07.2016.
 */
public class Splash extends Activity {

    // Setting for Spaungasse, fingerprinted 2 floors map
    private static Long building_id = (long) 809876490;
    private static String API_key = "8367396f-ba11-4512-aeb3-6cef6a39acf7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Test start indoors
        // Intent intent = new Intent(Splash.this, IndoorsActivity.class);

        // Test Splash with Bluetooth Scan in Background
        // Intent intent = new Intent(Splash.this, Splash_Bluetooth_Background.class);

        // Test List selection Activity
        Intent intent = new Intent(Splash.this, LocationSelectionActivity.class);

        intent.putExtra("Building_ID", building_id);
        Log.d("DEVICELIST", "Starting activity with: " + building_id + " \n");
        Log.d("DEVICELIST", "Starting with default Building ID");
        intent.putExtra("Building_ID", building_id);
        intent.putExtra("API_key", API_key);
        startActivity(intent);

        /*

        startActivity(intent);
        */
    }
}