package com.example.polyun.appicshowcase;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.customlbs.library.Indoors;
import com.customlbs.library.model.Zone;
import com.customlbs.shared.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZonesActivity extends ListActivity {

    private TextView selection;
    private Zone[] zones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<Zone> zonesList = intent.getParcelableArrayListExtra("ZonesList");
            if (zonesList != null)
                zones = zonesList.toArray(new Zone[zonesList.size()]);
        }
        if(zones == null)
            zones = new Zone[0];

        setContentView(R.layout.activity_zones);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getZoneNames(zones)));
        selection = (TextView)findViewById(R.id.selection);
    }


//    /**
//     * Inflate the menu; this adds items to the action bar if it is present.
//     * @param menu menu to inflate
//     * @return true: menu inflated
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_zones, menu);
//        return true;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zones, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles selection of menu item
     * @param item Selected menu item
     * @return true in case of zones-menu item. else: return value from super-method.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_show_map:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //selection.setText(zones[position].getName());
        Intent intent = new Intent();
        Coordinate b = getZoneCoordinate(zones[position]);
        //intent.putExtra("DATA", "your string");

        intent.putExtra("tgtx",b.x);
        intent.putExtra("tgty",b.y);
        intent.putExtra("tgtz",b.z);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }

    String[] getZoneNames(Zone[] zoneList) {
        ArrayList<String> zoneNameList = new ArrayList<String>();
        for (Zone zone: zoneList) {
            zoneNameList.add(zone.getName());
        }

        return zoneNameList.toArray(new String[zoneNameList.size()]);
    }

    Coordinate getZoneCoordinate(Zone zone) {

        int allX = 0;
        int allY = 0;
        int z = zone.getFloorLevel();
        int zoneListLength = zone.getZonePoints().size();
        for (Coordinate coord: zone.getZonePoints()) {
            allX += coord.x;
            allY += coord.y;
        }
        Coordinate resultCoord = new Coordinate(allX / zoneListLength, allY / zoneListLength,z);
        return resultCoord;
    }

}
