package com.pinpinbox.android.Test;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private double locLat;
    private double locLng;
    private LatLng locLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 20000, 0, this);


    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.98327, 121.56178)).title("Marker"));
        locLat = 24.98327;
        locLng = 121.56178;
        locLatLng = new LatLng(locLat,locLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locLatLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onLocationChanged(Location location) {

//        drawMarker(location);

//        if(location !=null){
//            locLat = 24.98;
//            locLng = 121.56;
//            locLatLng = new LatLng(locLat,locLng);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(locLatLng));
//            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//
//
//        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void drawMarker(Location location) {

        mMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),
                location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,
                90));
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude()));

    }


}
