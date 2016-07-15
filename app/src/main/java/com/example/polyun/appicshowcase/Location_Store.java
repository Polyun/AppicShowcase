package com.example.polyun.appicshowcase;

import android.support.v4.util.Pair;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by Polyun on 09.07.2016.
 */
public class Location_Store {
    private static HashMap<String, Pair<Long,String>> bt_add2build_id = new HashMap<String,  Pair<Long,String>>();

    /* Erste Spaungasse Karte
    private static final Long id_spaungasse = (long) 783306659;
    private static final String api = "8367396f-ba11-4512-aeb3-6cef6a39acf7";
    */


    //809209744	USEFORSLAM_Dopice
    //783306659	Spaungasse_Navigation
    //809877099	SLAM_Spaungasse
    //809876490	Spaungasse_FP

    // Spaungasse fingerprint 2 Floors
    private static final Long id_spaungasse = (long) 809876490;
    private static final String api = "8367396f-ba11-4512-aeb3-6cef6a39acf7";

    public static String getApi() {
        return api;
    }

    static
    {
        bt_add2build_id.put("CE:FF:67:D0:30:CA", new Pair<Long,String>(id_spaungasse,api));
        bt_add2build_id.put("C8:B6:51:54:97:BF", new Pair<Long,String>(id_spaungasse,api));
        bt_add2build_id.put("F3:B0:21:50:EF:79", new Pair<Long,String>(id_spaungasse,api));
        bt_add2build_id.put("D3:FB:24:49:2D:77", new Pair<Long,String>(id_spaungasse,api));
        bt_add2build_id.put("CE:5B:0F:63:69:1E", new Pair<Long,String>(id_spaungasse,api));
        bt_add2build_id.put("E1:EC:6A:23:A2:07", new Pair<Long,String>(id_spaungasse,api));
        bt_add2build_id.put("F3:C7:8A:13:06:44", new Pair<Long,String>(id_spaungasse,api));
    }

    public static Pair<Long,String> get_location(String bt_add){
        return bt_add2build_id.get(bt_add);
    }

}


