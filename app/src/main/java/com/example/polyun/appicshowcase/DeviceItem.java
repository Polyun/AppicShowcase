package com.example.polyun.appicshowcase;

/**
 * Created by Matt on 5/12/2015.
 */
public class DeviceItem {

    private String deviceName;
    private String address;
    private float rssi;
    private String uuid;
    private boolean connected;

    public String getDeviceName() {
        return deviceName;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean getConnected() {
        return connected;
    }

    public String getAddress() {
        return address;
    }

    public float getRssi() {
        return rssi;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceItem(String name, String address, float rssi, String uuid, String connected){
        this.deviceName = name;
        this.address = address;
        this.rssi = rssi;
        this.uuid = uuid;
        if (connected == "true") {
            this.connected = true;
        }
        else {
            this.connected = false;
        }
    }
}
