package com.example.polyun.appicshowcase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *  Created based on tutorial:
 *  http://stacktips.com/tutorials/android/android-listview-tutorial
 *
 *  List Fragments later:
 *  https://github.com/codepath/android_guides/wiki/Creating-and-Using-Fragments
 *  http://www.vogella.com/tutorials/AndroidListView/article.html
 */

public class LocationSelectionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        ArrayList locations = getListData();
        final ListView lv1 = (ListView) findViewById(R.id.custom_list);
        lv1.setAdapter(new LocationListAdapter(this, locations));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                IndoorsLocation locationdata = (IndoorsLocation) o;
                Toast.makeText(LocationSelectionActivity.this, "Selected :" + " " + locationdata, Toast.LENGTH_LONG).show();

                // Start Indoors at the selected location
                Intent intent = new Intent(LocationSelectionActivity.this, IndoorsActivity.class);
                intent.putExtra("Building_ID", locationdata.getBuilding_ID());
                Log.d("Selection", "Starting activity with: " + locationdata.getBuilding_ID() + " \n");
                Log.d("Selection", "Starting with default Building ID");
                intent.putExtra("Building_ID", locationdata.getBuilding_ID());
                intent.putExtra("API_key", locationdata.getAPI_Key());
                startActivity(intent);

            }
        });
    }

    /**
        Define the test locations, these options will be available in the list
     */
    private ArrayList getListData() {
        ArrayList<IndoorsLocation> results = new ArrayList<IndoorsLocation>();

        // Spaungasse
        results.add(new IndoorsLocation("USEFORSLAM_Dopice",  (long) 809209744, "8367396f-ba11-4512-aeb3-6cef6a39acf7"));
        results.add(new IndoorsLocation("Spaungasse_Navigation",(long) 783306659, "8367396f-ba11-4512-aeb3-6cef6a39acf7"));
        results.add(new IndoorsLocation("SLAM_Spaungasse", (long) 809877099, "8367396f-ba11-4512-aeb3-6cef6a39acf7"));
        results.add(new IndoorsLocation("Spaungasse_FP", (long) 809876490, "8367396f-ba11-4512-aeb3-6cef6a39acf7"));

        // Lugner City
        results.add(new IndoorsLocation("Lugner City", (long) 675933321, "cb8ae113-1aa7-41ad-9e02-fa8e37a6de57"));
        results.add(new IndoorsLocation("Lugner City", (long) 675933321, "cb8ae113-1aa7-41ad-9e02-fa8e37a6de57"));
        results.add(new IndoorsLocation("Lugner City Final", (long) 724489931, "cb8ae113-1aa7-41ad-9e02-fa8e37a6de57"));
        results.add(new IndoorsLocation("Lugner City Test", (long) 806192608, "cb8ae113-1aa7-41ad-9e02-fa8e37a6de57"));

        // Office
        results.add(new IndoorsLocation("Office Test proximity", (long) 702790987, "e6a76e4b-08d8-452f-a62e-eaa3f4fc5dec"));
        results.add(new IndoorsLocation("Office Test FP", (long) 702939595, "e6a76e4b-08d8-452f-a62e-eaa3f4fc5dec"));

        //Donauzentrum
        results.add(new IndoorsLocation("Donauzentrum", (long) 737570753, "47262647-9767-4c86-9a72-6734402a9af7"));
        results.add(new IndoorsLocation("Donauzentrum 2", (long) 739128720, "47262647-9767-4c86-9a72-6734402a9af7"));
        results.add(new IndoorsLocation("Donauzentrum_SLAMTEST", (long) 738507986, "47262647-9767-4c86-9a72-6734402a9af7"));
        results.add(new IndoorsLocation("SLAM_Donauzentrum 2", (long) 743266508, "47262647-9767-4c86-9a72-6734402a9af7"));

        // Westbahnhof
        results.add(new IndoorsLocation("Westbahnhof", (long) 783311608, "0552df83-483b-44f6-bb3a-6cf9701f65dc"));

        // Add some more dummy data for testing
        return results;
    }
}
