package com.example.polyun.appicshowcase;

import java.util.HashMap;

/**
 * Created by Polyun on 09.07.2016.
 */
public class Location_Store {
    private static HashMap<String, Long> bt_add2build_id = new HashMap<String, Long>();
    static
    {
        bt_add2build_id.put("CE:FF:67:D0:30:CA", (long) 783306659);
        bt_add2build_id.put("C8:B6:51:54:97:BF", (long) 783306659);
        bt_add2build_id.put("F3:B0:21:50:EF:79", (long) 783306659);
        bt_add2build_id.put("D3:FB:24:49:2D:77", (long) 783306659);
        bt_add2build_id.put("CE:5B:0F:63:69:1E", (long) 783306659);
    }

    public static Long get_location(String bt_add){
        return bt_add2build_id.get(bt_add);
    }
}
