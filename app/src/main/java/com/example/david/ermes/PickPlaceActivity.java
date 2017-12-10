package com.example.david.ermes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickPlaceActivity extends AppCompatActivity {

    MapView mMapView;
    private GoogleMap googleMap;
    private Button dismiss;
    private Button fine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_place);

        dismiss = findViewById(R.id.dismiss);
        fine = findViewById(R.id.accept);

        mMapView = findViewById(R.id.pick_place_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        // TODO Auto-generated method stub
                        String location = (String) getIntent().getExtras().get("location");
                        MarkerOptions marker = new MarkerOptions().position(
                                new LatLng(point.latitude, point.longitude)).title(location);

                        googleMap.addMarker(marker);

                    }
                });
            }
        });

    }
}
