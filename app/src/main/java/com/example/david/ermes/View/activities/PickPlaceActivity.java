package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.SportChip;
import com.example.david.ermes.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tylersuehr.chips.ChipsInputLayout;
import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipSelectionObserver;

import java.util.ArrayList;
import java.util.List;

public class PickPlaceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Location location_create;
    MapView mMapView;
    private GoogleMap googleMap;
    private MaterialEditText place_name;
    private ImageButton dismiss;
    private ImageButton fine;
    private ChipsInputLayout sport_chips;


    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    private List<String> all_sports_ids;
    private List<String> selected_sports_ids;
    private List<SportChip> choises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_place);

        selected_sports_ids = new ArrayList<>();

        dismiss = findViewById(R.id.dismiss);
        place_name = findViewById(R.id.nome_location);
        fine = findViewById(R.id.accept);
        sport_chips = findViewById(R.id.sports_chips);
        // sport_spinner = findViewById(R.id.sport_spinner_place);

        sport_chips.addChipSelectionObserver(new ChipSelectionObserver() {
            @Override
            public void onChipSelected(Chip chip) {
                int sport_index = choises.indexOf(chip);
                selected_sports_ids.add(all_sports_ids.get(sport_index));
            }

            @Override
            public void onChipDeselected(Chip chip) {
                int sport_index = choises.indexOf(chip);
                selected_sports_ids.remove(all_sports_ids.get(sport_index));
            }
        });

        mMapView = findViewById(R.id.pick_place_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        createSportSpinner();
        // sport_spinner.setOnItemSelectedListener(new sportSpinnerSelectedListener());

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
                location_create = new Location();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted
                        buildGoogleApiClient();
                        googleMap.setMyLocationEnabled(true);
                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                } else {
                    buildGoogleApiClient();
                    googleMap.setMyLocationEnabled(true);
                }


                googleMap.setOnMapClickListener(point -> UserRepository.getInstance().getUser(object -> {
                    if (object != null) {
                        marker[0] = new MarkerOptions()
                                //.title(String.valueOf(place_name.getText()))
                                .position(new LatLng(point.latitude, point.longitude));
                        googleMap.clear();
                        googleMap.addMarker(marker[0]);

                        location_create.setLatitude(marker[0].getPosition().latitude);
                        location_create.setLongitude(marker[0].getPosition().longitude);
                        location_create.setIdUserCreator(((User) object).getUID());

                        if (googleMap.isMyLocationEnabled()){
                            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                CameraPosition position = new CameraPosition.Builder().zoom(12).target(latLng).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                            });
                        }
                    }
                }));
            }
        });

        dismiss.setOnClickListener(view -> {
            finish();
            hideKeyboard();
        });

        fine.setOnClickListener(view -> {
            if ((location_create != null)) {
                if ((place_name.getText() != null) && (location_create.getLatitude() != 0)) {
                    if (selected_sports_ids.size() == 0) {
                        selected_sports_ids = all_sports_ids;
                    }

                    location_create.setName(place_name.getText().toString());
                    location_create.setSportIds(selected_sports_ids);
                    location_create.save();
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    if (place_name.getText() == null) {
                        place_name.setError("Inserisci un nome");
                    } else if (location_create.getLatitude() == 0) {
                        Toast.makeText(getBaseContext(), "Clicca sulla mappa per inserire il luogo", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                Toast.makeText(getBaseContext(), "Impossibile creare luogo al momento", Toast.LENGTH_LONG).show();
            }

            hideKeyboard();
        });
    }

    private void hideKeyboard() {
        View view;
        view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void createSportSpinner() {

        choises = new ArrayList<>();
        all_sports_ids = new ArrayList<>();

        SportRepository.getInstance().fetchAll(object -> {
            for (Sport s : (ArrayList<Sport>) object) {
                all_sports_ids.add(s.getID());

                SportChip sc = new SportChip(Integer.parseInt(s.getID()), s.getName());
                choises.add(sc);
            }

            sport_chips.setFilterableChipList(choises);
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Posizione GPS Richiesta")
                        .setMessage("Ermes necessita della posizione per mostrarti i luoghi")
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(PickPlaceActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permesso non concesso", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }
}
