package com.fiek.regionalsnowforecast;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<ResortMapLocation> clusterManager;
    final LatLng brezovica = new LatLng(42.2088, 20.9555);
    final LatLng popovasapka = new LatLng(42.0164, 20.8856);
    final LatLng mavrovo = new LatLng(41.6483, 20.7363);
    final LatLng bansko = new LatLng(41.8404, 23.484856);
    final LatLng kolasin = new LatLng(42.8205, 19.5241);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_TERRAIN);
        options.zoomControlsEnabled(true);
        options.compassEnabled(true);
        options.minZoomPreference(1);
        options.zoomGesturesEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setupMapFragment();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(brezovica));
            }
        });
        clusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        addItems();
    }

    private void addItems() {

        clusterManager.addItem(new ResortMapLocation(brezovica, "Brezovica Ski Resort", "Currently closed!"));
        clusterManager.addItem(new ResortMapLocation(mavrovo, "Mavrovo Ski Resort", "Currently closed!"));
        clusterManager.addItem(new ResortMapLocation(popovasapka, "Popova Sapka Ski Resort", "Currently closed!"));
        clusterManager.addItem(new ResortMapLocation(bansko, "Bansko Ski Resort", "Currently closed!"));
        clusterManager.addItem(new ResortMapLocation(kolasin, "Kolasin Ski Resort", "Currently closed!"));

    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
    }
}