package com.example.polyun.appicshowcase;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.customlbs.library.Indoors;
import com.customlbs.library.IndoorsException;
import com.customlbs.library.IndoorsFactory;
import com.customlbs.library.IndoorsLocationListener;
import com.customlbs.library.callbacks.LoadingBuildingStatus;
import com.customlbs.library.callbacks.RoutingCallback;
import com.customlbs.library.model.Building;
import com.customlbs.library.model.Floor;
import com.customlbs.library.model.Zone;
import com.customlbs.shared.Coordinate;
import com.customlbs.surface.library.IndoorsSurfaceFactory;
import com.customlbs.surface.library.IndoorsSurfaceFragment;
import com.customlbs.surface.library.IndoorsSurfaceOverlayUtil;
import com.customlbs.surface.library.SurfaceState;
import com.customlbs.surface.library.ViewMode;
import com.customlbs.surface.library.VisibleMapRect;
import com.example.polyun.appicshowcase.FloorSelectionFragment.OnFloorSelectedListener;
import java.util.ArrayList;
import java.util.List;

import static com.customlbs.surface.library.IndoorsSurfaceOverlayUtil.buildingCoordinateToCanvasAbsolute;

public class IndoorsActivity extends AppCompatActivity implements IndoorsLocationListener, OnFloorSelectedListener {

    //private Indoors indoors;
    private static Long BuildingID;
    private static String API_key;
    private IndoorsSurfaceFragment IndoorsSurfaceFragment;
    private SurfaceState custom_Surface_State       = new SurfaceState();
    IndoorsFactory.Builder indoorsBuilder           = new IndoorsFactory.Builder();
    IndoorsSurfaceFactory.Builder surfaceBuilder    = new IndoorsSurfaceFactory.Builder();
    Toast currentToast;
    List<Long> lastZoneIDList                       = new ArrayList<Long>();
    Coordinate routeToCoordinate                    = null;
    private FloorSelectionFragment floorSelectionFragment;
    FragmentManager fragmentManager = null;
    private ArrayList<Floor> floors = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_indoors);
        BuildingID = getIntent().getExtras().getLong("Building_ID");
        API_key = getIntent().getExtras().getString("API_key");
        fragmentManager = getSupportFragmentManager();
        floorSelectionFragment = new FloorSelectionFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.floors_fragment, floorSelectionFragment, "indoorsFloors");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d("Activity Result", "Result OK");
            Point point = (Point) data.getParcelableExtra("RouteCoordinate");
            routeToCoordinate = new Coordinate(point.x, point.y, 1);
            //showToast("Activity Result OK");
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                //startActivity(intent);
                startActivityForResult(intent, 0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void show_indoors() {
        showToast("Start indoors");
        indoorsBuilder.setUserInteractionListener(this);
        floorSelectionFragment = new FloorSelectionFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.floors_fragment, floorSelectionFragment, "indoorsFloors");

        //surfaceBuilder.setIndoorsBuilder(indoorsBuilder);

        //IndoorsSurfaceFragment IndoorsSurfaceFragment = surfaceBuilder.build();


        indoorsBuilder.setContext(this);

        // API key and building ID are put in Extra by the Splash Screen activity
        indoorsBuilder.setApiKey(API_key);
        indoorsBuilder.setBuildingId(BuildingID);
        showToast("BuildingID loaded and Interaction Listener loaded");
        //Toast.makeText(getApplicationContext(), "BuildingID loaded", Toast.LENGTH_SHORT).show();
        String map_dir = indoorsBuilder.getMapDirectory();
        Log.d("DEVICELIST", "MapDirectory: " + map_dir + "\n");
        showToast("MapDirectory: " + map_dir);
        //Toast.makeText(getApplicationContext(), "Interaction Listener loaded", Toast.LENGTH_SHORT).show();
        surfaceBuilder.setIndoorsBuilder(indoorsBuilder);

        custom_Surface_State.autoSelect = false;
        custom_Surface_State.orientedNaviArrow = false;
        surfaceBuilder.setSurfaceState(custom_Surface_State);

        IndoorsSurfaceFragment = surfaceBuilder.build();
        IndoorsSurfaceFragment.setViewMode(ViewMode.DEFAULT);

        showToast("IndoorsSurfaceFragment loaded");
        //Toast.makeText(getApplicationContext(), "IndoorsSurfaceFragment loaded", Toast.LENGTH_SHORT).show();

        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, IndoorsSurfaceFragment, "indoors");
        transaction.commit();
    }

    @Override
    public void loadingBuilding(LoadingBuildingStatus loadingBuildingStatus) {
        int progress = loadingBuildingStatus.getProgress();
        // showToast("Building Loading progress " + progress);
        //Toast toast = Toast.makeText(getApplicationContext(), "Building Loading progress " + progress, Toast.LENGTH_SHORT);
        //toast.show();
    }

    @Override
    public void buildingLoaded(Building building) {
        showToast("Building loaded: " + building.getDescription() + building.getName());
        /*
        if (routeToCoordinate != null) {
            //IndoorsSurfaceFragment.routeTo(routeToCoordinate, true);
            //IndoorsSurfaceFragment.updateSurface();
            IndoorsSurfaceFragment.getIndoors().getRouteAToB(IndoorsSurfaceFragment.getCurrentUserPosition(),
                    routeToCoordinate,
                    new RoutingCallback() {
                        @Override
                        public void onError(IndoorsException arg0) {
                            // TODO Auto-generated method stub
                            arg0.getErrorCode();
                        }

                        @Override
                        public void setRoute(ArrayList<Coordinate> route) {
                            surfaceBuilder.getSurfaceState().setRoutingPath(route);
                            // this how to enable route snapping starting 3.8
                            //IndoorsFactory.getInstance().enableSnapToRoute(route);
                            IndoorsSurfaceFragment.updateSurface();
                        }
                    });
        }
        */
        //showToast("Building loaded: " + building.getDescription() + "\n" +building.getName());
        floors = building.getFloors();
        updateFloorList(building);
        showToast("Floors: " + floors.toString());

        //Toast.makeText(getApplicationContext(), "Building loaded", Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.notification_icon)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");

    }

    @Override
    public void leftBuilding(Building building) {

    }

    @Override
    public void positionUpdated(Coordinate userPosition, int accuracy) {
//        //Location geoLocation = IndoorsCoordinateUtil.toGeoLocation(userPosition, this.building);
//        int x = userPosition.x;
//        int y = userPosition.y;
//        int z = userPosition.z;
//        VisibleMapRect rect1 = IndoorsSurfaceFragment.getVisibleMapRect();
//
//        SurfaceState surface_State = surfaceBuilder.getSurfaceState();
//
//        //surface_State.lockOnUserPosition = true;
//        //surface_State.selectFittingBackground();
//        surface_State.adjustMapPosition();
//
//        //Update old Zone-List cause there is no zone-left-event
//        zoneListChanged(IndoorsSurfaceFragment.getCurrentZones());
//
//        //move to position
//        float zoom = surface_State.mapZoomFactor;
//        float mapx = surface_State.getMapX();
//        float mapy = surface_State.getMapY();
//        double pd = surface_State.currentTiles.getMmPerPixel(); //pixel density
//        float canvas_x = (float)((double)x / (pd / zoom) + mapx);
//        float canvas_y = (float)((double)y / (pd / zoom) + mapy);
//        // canvas_x und y liefern dasselbe wie IndoorsSurfaceOverlayUtil.CanvasCoordinate cc = buildingCoordinateToCanvasAbsolute(surface_State, x, y);
//
//        int mapwidth = surface_State.surfaceWidth;
//        int mapheight =surface_State.surfaceHeight;
//        float vp_x = surface_State.getMapX() + ((float)(mapwidth / 2) - canvas_x);
//        float vp_y = surface_State.getMapY() + ((float)(mapheight / 2) - canvas_y);
//
//        IndoorsSurfaceFragment.setVisibleMapRectAndUpdateSurface(new VisibleMapRect(vp_x,vp_y,rect1.zoom));
//
//        String toast_message ="Position Update ("+x+","+y+") z:"+z
//                + "\n VisibleMapRect Center ("+rect1.x+","+rect1.y+") zoom:"+rect1.zoom
//                + "\n Map Size: ("+mapwidth+","+mapheight+")"
//                + "\n MmPixels:"+ surface_State.currentTiles.getMmPerPixel()
//                + "\n Canvas Coordinates: ("+(int)canvas_x + ","+(int)canvas_y+")"
//                + "\n Surface Coordinate: (" +(int)vp_x + ","+(int)vp_y+")";
//
//        showToast(toast_message);

    }

    @Override
    public void orientationUpdated(float v) {
        //Toast.makeText(getApplicationContext(), "Orienation Update, phi:"+v, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changedFloor(int i, String s) {
        // the floors are saved the other way around, so we look at size-1-i, to get the index backwards
        showToast("changed Floor:" + i + " " + s + " to " + floors.get(floors.size()-1-i).toString());
        SurfaceState surface_State = surfaceBuilder.getSurfaceState();
        surface_State.setFloor(floors.get(floors.size()-1-i));
    }

    @Override
    public void enteredZones(List<Zone> list) {
        if (zoneListChanged(list) && false) {
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

    public void updateFloorList(Building building) {
        // update floor list if fragment is visible
        if (floorSelectionFragment != null) {
            floorSelectionFragment.updateFloors(building);
        }
    }

    @Override
    public void onFloorSelected(Floor floor) {
        String message = null;

        if (floor == null) {
            message = getText(R.string.automatic_floor_selected).toString();
            custom_Surface_State.autoSelect = true;

            if (custom_Surface_State.lastFloorLevelSelectedByLibrary != SurfaceState.NO_FLOOR_SELECTED_BY_LIBRARY) {
                custom_Surface_State.setFloor(custom_Surface_State.building
                        .getFloorByLevel(custom_Surface_State.lastFloorLevelSelectedByLibrary));
            }
        } else {
            message = new StringBuilder(getString(R.string.floor)).append(" '")
                    .append(floor.getName()).append("' ").append(getString(R.string.selected))
                    .toString();
            custom_Surface_State.autoSelect = false;
            custom_Surface_State.setFloor(floor);
            //checkVisibilityOfMenuItems();
        }

        // Print selection to screen.
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
