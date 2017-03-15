package com.bx.android.gmaps;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bx.android.gmaps.model.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int ZOOM = 12;

    private List<Place> places;
    private Map<Marker,Place> placesMap;
    protected GoogleMap mMap;

    private double defaultLat= -12.099653;
    private double defaultLng= -77.018999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_map);
        initMap();

        mocks();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(defaultLat, defaultLng), ZOOM));
        addDefaultMarker();

        //events

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                gotoMarkerInfoAction(marker);
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }
        });
        //renderPlacesMap();
        renderAsyncPlacesMap();
    }

    private void gotoMarkerInfoAction(Marker marker) {

    }

    private void renderAsyncPlacesMap(){

        final Handler handler = new Handler(Looper.getMainLooper());
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        double mLat=0;
        double mLng=0;
        long DELAY = 20;
        int x = 0;

        placesMap=new HashMap<Marker, Place>();

        for (final Place place:places) {
            mLat= place.getLat();
            mLng= place.getLng();

            final LatLng position= new LatLng(mLat, mLng);
            builder.include(position);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Marker marker;
                    String title,address;

                    title= place.getName();
                    address= "Lat "+place.getLat()+" Lng "+place.getLng();
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .snippet(address)
                            .icon(getIconMarker()));

                    placesMap.put(marker, place);
                }

            },DELAY*((long)x++));

        }

        LatLngBounds bounds = builder.build();
        centerProvidersMap(bounds);


    }

    private void renderPlacesMap(){

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        double mLat=0;
        double mLng=0;

        placesMap=new HashMap<Marker, Place>();

        for (Place place:places) {
            mLat= place.getLat();
            mLng= place.getLng();

            final LatLng position= new LatLng(mLat, mLng);
            builder.include(position);

            Marker marker;
            String title,address;

            title= place.getName();
            address= "Lat "+place.getLat()+" Lng "+place.getLng();
            marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(address)
                    .icon(getIconMarker()));

            placesMap.put(marker, place);
        }

        LatLngBounds bounds = builder.build();

        centerProvidersMap(bounds);

    }

    private void centerProvidersMap(LatLngBounds bounds) {

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);

    }

    private BitmapDescriptor getIconMarker(){
        return BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker48);
    }



    private void mocks(){
        places= new ArrayList<>();
        places.add(new Place("Local 1", -12.097544,-77.018141));
        places.add(new Place("Local 2", -12.097009, -77.012498));
        places.add(new Place("Local 2", -12.099202, -77.016392));
        places.add(new Place("Local 3", -12.100083, -77.021070));
        places.add(new Place("Local 4", -12.097009, -77.012498));
        places.add(new Place("Local 5", -12.097513, -77.017905));
        places.add(new Place("Local 6", -12.097009, -77.012498));
        places.add(new Place("Local 7", -12.097009, -77.012498));
        places.add(new Place("Local 8", -12.100303, -77.015931));
        places.add(new Place("Local 9", -12.097009, -77.012498));
        places.add(new Place("Local 10", -12.098687, -77.013871));
        places.add(new Place("Local 11", -12.098687, -77.013871));
        places.add(new Place("Local 12", -12.098121, -77.016585));
        places.add(new Place("Local 13", -12.096180, -77.015984));
        places.add(new Place("Local 14", -12.098687, -77.017958));
        places.add(new Place("Local 15", -12.096442, -77.019138));
    }

    private void addDefaultMarker(){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(defaultLat, defaultLng))
                .title("Mi Ubicación")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    private void resetMap(){
        mMap.clear();
    }
}
