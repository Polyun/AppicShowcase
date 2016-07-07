package com.example.polyun.appicshowcase;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.customlbs.library.model.Zone;

import java.util.ArrayList;
import java.util.List;

public class ZonesActivity extends ListActivity {

    private TextView selection;
    //private String[] zones;
    private ArrayList<Zone> zones;
    private static final String[] items={"lorem", "ipsum", "dolor", "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat", "placerat", "ante", "porttitor", "sodales", "pellentesque", "augue", "purus"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            zones = intent.getParcelableArrayListExtra("ZonesList");
        }
        if(zones == null)
            zones = new ArrayList<Zone>();

        setContentView(R.layout.activity_zones);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getZoneNames(zones)));
        selection = (TextView)findViewById(R.id.selection);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selection.setText(items[position]);
    }

    String[] getZoneNames(ArrayList<Zone> zoneList) {
        //List<Zone> zoneList = IndoorsSurfaceFragment.getZones();
        ArrayList<String> zoneNameList = new ArrayList<String>();
        for (Zone zone: zoneList) {
            zoneNameList.add(zone.getName());
        }

        return zoneNameList.toArray(new String[zoneNameList.size()]);
    }

}
