package com.example.polyun.appicshowcase;

/**
 * Created by Polyun on 15.07.2016.
 */
public class IndoorsLocation {
    private String Address;
    private Long Building_ID;
    private String API_Key;

    public IndoorsLocation() {
    }

    public IndoorsLocation(String Address, Long Building_ID, String API_Key) {
        this.Address = Address;
        this.Building_ID = Building_ID;
        this.API_Key = API_Key;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Long getBuilding_ID() {
        return Building_ID;
    }

    public void setBuilding_ID(Long building_ID) {
        Building_ID = building_ID;
    }

    public String getAPI_Key() {
        return API_Key;
    }

    public void setAPI_Key(String API_Key) {
        this.API_Key = API_Key;
    }
}