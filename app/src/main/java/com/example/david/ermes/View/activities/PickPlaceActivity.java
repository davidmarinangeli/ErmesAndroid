package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickPlaceActivity extends AppCompatActivity {

    final Location location_create = new Location();
    MapView mMapView;
    private GoogleMap googleMap;
    private EditText place_name;
    private Button dismiss;
    private Button fine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_place);

        dismiss = findViewById(R.id.dismiss);
        place_name = findViewById(R.id.nome_location);
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

            final MarkerOptions[] marker = {new MarkerOptions()};


            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(final LatLng point) {
                        UserRepository.getInstance().getUser(new FirebaseCallback() {
                            @Override
                            public void callback(Object object) {
                                if (object != null) {
                                    // TODO Auto-generated method stub
                                    marker[0] = new MarkerOptions()
                                            //.title(String.valueOf(place_name.getText()))
                                            .position(new LatLng(point.latitude, point.longitude));
                                    googleMap.clear();
                                    googleMap.addMarker(marker[0]);

                                    location_create.setLatitude(marker[0].getPosition().latitude);
                                    location_create.setLongitude(marker[0].getPosition().longitude);
                                    location_create.setName(String.valueOf(place_name.getText()));
                                    location_create.setIdUserCreator(((User) object).getUID());
                                }
                            }
                        });
                    }
                });
            }
        });


        fine.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            location_create.save();
            view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            setResult(Activity.RESULT_OK);

            finish();
        }
    };
}
