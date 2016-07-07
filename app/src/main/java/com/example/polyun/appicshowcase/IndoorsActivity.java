package com.example.polyun.appicshowcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.customlbs.library.Indoors;
import com.customlbs.library.IndoorsException;
import com.customlbs.library.IndoorsFactory;
import com.customlbs.library.IndoorsLocationListener;
import com.customlbs.library.callbacks.LoadingBuildingStatus;
import com.customlbs.library.model.Building;
import com.customlbs.library.model.Zone;
import com.customlbs.shared.Coordinate;
import com.customlbs.surface.library.IndoorsSurfaceFactory;
import com.customlbs.surface.library.IndoorsSurfaceFragment;
import com.customlbs.surface.library.IndoorsSurfaceOverlayUtil;
import com.customlbs.surface.library.SurfaceState;
import com.customlbs.surface.library.ViewMode;
import com.customlbs.surface.library.VisibleMapRect;

import java.util.ArrayList;
import java.util.List;

import static com.customlbs.surface.library.IndoorsSurfaceOverlayUtil.buildingCoordinateToCanvasAbsolute;

public class IndoorsActivity extends AppCompatActivity implements IndoorsLocationListener{

    //private Indoors indoors;
    private IndoorsSurfaceFragment IndoorsSurfaceFragment;
    private SurfaceState custom_Surface_State       = new SurfaceState();
    IndoorsFactory.Builder indoorsBuilder           = new IndoorsFactory.Builder();
    IndoorsSurfaceFactory.Builder surfaceBuilder    = new IndoorsSurfaceFactory.Builder();
    Toast currentToast;
    List<Long> lastZoneIDList                       = new ArrayList<Long>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_indoors);

        show_indoors();
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu menu to inflate
     * @return true: menu inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles selection of menu item
     * @param item Selected menu item
     * @return true in case of main-menu item. else: return value from super-method.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_show_zones:
                Intent intent = new Intent(this, ZonesActivity.class);
                //intent.putExtra("ZonesList", getZoneNames());
                intent.putParcelableArrayListExtra("ZonesList", (ArrayList<Zone>)IndoorsSurfaceFragment.getZones());
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void show_indoors() {
        showToast("Start indoors");
        indoorsBuilder.setUserInteractionListener(this);

        //surfaceBuilder.setIndoorsBuilder(indoorsBuilder);

        //IndoorsSurfaceFragment IndoorsSurfaceFragment = surfaceBuilder.build();


        indoorsBuilder.setContext(this);


        // TODO: replace this with your API-key
        indoorsBuilder.setApiKey("8367396f-ba11-4512-aeb3-6cef6a39acf7");

        // TODO: replace 12345 with the id of the building you uploaded to
        // our cloud using the MMT
        indoorsBuilder.setBuildingId((long) 783306659);
        showToast("BuildingID loaded and Interaction Listener loaded");
        //Toast.makeText(getApplicationContext(), "BuildingID loaded", Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), "Interaction Listener loaded", Toast.LENGTH_SHORT).show();
        surfaceBuilder.setIndoorsBuilder(indoorsBuilder);

        custom_Surface_State.autoSelect = false;
        custom_Surface_State.orientedNaviArrow = false;
        surfaceBuilder.setSurfaceState(custom_Surface_State);

        IndoorsSurfaceFragment = surfaceBuilder.build();
        IndoorsSurfaceFragment.setViewMode(ViewMode.DEFAULT);

        showToast("IndoorsSurfaceFragment loaded");
        //Toast.makeText(getApplicationContext(), "IndoorsSurfaceFragment loaded", Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, IndoorsSurfaceFragment, "indoors");
        transaction.commit();

    }

    @Override
    public void loadingBuilding(LoadingBuildingStatus loadingBuildingStatus) {
        int progress = loadingBuildingStatus.getProgress();
        showToast("Building Loading progress " + progress);
        //Toast toast = Toast.makeText(getApplicationContext(), "Building Loading progress " + progress, Toast.LENGTH_SHORT);
        //toast.show();
    }

    @Override
    public void buildingLoaded(Building building) {
        showToast("Building loaded");
        //Toast.makeText(getApplicationContext(), "Building loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void leftBuilding(Building building) {

    }

    @Override
    public void positionUpdated(Coordinate userPosition, int accuracy) {
        //Location geoLocation = IndoorsCoordinateUtil.toGeoLocation(userPosition, this.building);
        int x = userPosition.x;
        int y = userPosition.y;
        int z = userPosition.z;
        VisibleMapRect rect1 = IndoorsSurfaceFragment.getVisibleMapRect();

        SurfaceState surface_State = surfaceBuilder.getSurfaceState();


        //surface_State.lockOnUserPosition = true;
        //surface_State.selectFittingBackground();
        surface_State.adjustMapPosition();

        //Update old Zone-List cause there is no zone-left-event
        zoneListChanged(IndoorsSurfaceFragment.getCurrentZones());


        //Toast toast = Toast.makeText(getApplicationContext(), "Position Update x:"+x+" y:"+y+" z:"+z, Toast.LENGTH_SHORT);
        //toast.show();

        //move to position
        float zoom = surface_State.mapZoomFactor;
        float mapx = surface_State.getMapX();
        float mapy = surface_State.getMapY();
        double pd = surface_State.currentTiles.getMmPerPixel(); //pixel density
        float canvas_x = (float)((double)x / (pd / zoom) + mapx);
        float canvas_y = (float)((double)y / (pd / zoom) + mapy);
        // canvas_x und y liefern dasselbe wie IndoorsSurfaceOverlayUtil.CanvasCoordinate cc = buildingCoordinateToCanvasAbsolute(surface_State, x, y);

        int mapwidth = surface_State.surfaceWidth;
        int mapheight =surface_State.surfaceHeight;
        float vp_x = surface_State.getMapX() + ((float)(mapwidth / 2) - canvas_x);
        float vp_y = surface_State.getMapY() + ((float)(mapheight / 2) - canvas_y);

        IndoorsSurfaceFragment.setVisibleMapRectAndUpdateSurface(new VisibleMapRect(vp_x,vp_y,rect1.zoom));

        String toast_message ="Position Update ("+x+","+y+") z:"+z
                + "\n VisibleMapRect Center ("+rect1.x+","+rect1.y+") zoom:"+rect1.zoom
                + "\n Map Size: ("+mapwidth+","+mapheight+")"
                + "\n MmPixels:"+ surface_State.currentTiles.getMmPerPixel()
                + "\n Canvas Coordinates: ("+(int)canvas_x + ","+(int)canvas_y+")"
                + "\n Surface Coordinate: (" +(int)vp_x + ","+(int)vp_y+")";

        showToast(toast_message);

    }

    @Override
    public void orientationUpdated(float v) {
        //Toast.makeText(getApplicationContext(), "Orienation Update, phi:"+v, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changedFloor(int i, String s) {

    }

    @Override
    public void enteredZones(List<Zone> list) {
        if (zoneListChanged(list)) {
            for (Zone zone : list) {
                String zoneMessage = "Zone name: " + zone.getName() +
                        "\nZone ID: " + zone.getId() +
                        "\nZone description: " + zone.getDescription();
                for (Coordinate coord: zone.getZonePoints()) {
                    zoneMessage += "\nCoordinate: x" + coord.x + " y:" + coord.y;
                }
                showToast(zoneMessage);
            }
        }
    }

    /**
     * Check if given zone list is different from last one
     * @param zoneListNew List of new zones to check for differences
     * @return true: given zone list is different from previous one. Else false.
     */
    private boolean zoneListChanged(List<Zone> zoneListNew) {
        List<Long> zoneIDListNew    = new ArrayList<Long>();
        boolean isChanged           = false;
        for (Zone zone: zoneListNew) {
            zoneIDListNew.add(zone.getId());
            if (!lastZoneIDList.contains(zone.getId())) {
                isChanged           = true;
            }
        }
        lastZoneIDList              = zoneIDListNew;

        return isChanged;
    }

    @Override
    public void buildingLoadingCanceled() {
        showToast("Canceled Buliding Loading");
        //Toast.makeText(getApplicationContext(), "Canceled Building Loading", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onError(IndoorsException e) {

    }

    String[] getZoneNames() {
        List<Zone> zoneList = IndoorsSurfaceFragment.getZones();
        ArrayList<String> zoneNameList = new ArrayList<String>();
        for (Zone zone: zoneList) {
            zoneNameList.add(zone.getName());
        }

        return zoneNameList.toArray(new String[zoneNameList.size()]);
    }


    void showToast(String text)
    {
        if(currentToast == null)
        {
            currentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        }

        currentToast.setText(text);
        currentToast.setDuration(Toast.LENGTH_LONG);
        currentToast.show();
    }
}
