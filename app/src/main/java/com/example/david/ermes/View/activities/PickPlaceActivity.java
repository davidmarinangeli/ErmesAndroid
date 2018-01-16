package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.SportDatabaseRepository;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.SportChip;
import com.example.david.ermes.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tylersuehr.chips.ChipsInputLayout;

import java.util.ArrayList;
import java.util.List;

public class PickPlaceActivity extends AppCompatActivity {

    Location location_create;
    MapView mMapView;
    private GoogleMap googleMap;
    private MaterialEditText place_name;
    private Button dismiss;
    private Button fine;
    private ChipsInputLayout sport_chips;
    private Spinner sport_spinner;
    private String selected_sport_string;
    SpinnerAdapter sportadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_place);

        dismiss = findViewById(R.id.dismiss);
        place_name = findViewById(R.id.nome_location);
        fine = findViewById(R.id.accept);
        sport_chips = findViewById(R.id.sports_chips);
        sport_spinner = findViewById(R.id.sport_spinner_place);

        mMapView = findViewById(R.id.pick_place_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        createSportSpinner();
        sport_spinner.setOnItemSelectedListener(new sportSpinnerSelectedListener());

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

                    }
                }));
            }
        });

        dismiss.setOnClickListener(view -> {finish(); hideKeyboard();});

        fine.setOnClickListener(view -> {
            if ((location_create != null)) {
                if ((place_name.getText() != null) && (location_create.getLatitude() != 0)) {
                    final List<SportChip> sport_chips_list = (List<SportChip>) sport_chips.getSelectedChips();
                    final List<String> sport_id_list = new ArrayList<>();

                    SportRepository.getInstance().fetchAll(object -> {
                        if (object != null) {
                            List<Sport> sport = (List<Sport>) object;

                            for (Sport s : sport) {
                                for (SportChip sportChip_name : sport_chips_list)
                                    if (sportChip_name.getTitle().equals(s.getName())) {
                                        sport_id_list.add(s.getID());

                                    }
                            }
                            location_create.setName(place_name.getText().toString());
                            location_create.setSportIds(sport_id_list);
                            location_create.save();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    });
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

        final ArrayList<String> arraySpinner = new ArrayList<>();

        SportRepository.getInstance().fetchAll(object -> {
            for (Sport s : (ArrayList<Sport>) object) {
                arraySpinner.add(s.getName());
            }
            sportadapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, arraySpinner);
            sport_spinner.setAdapter(sportadapter);


        });
    }

    private class sportSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected_sport_string = parent.getItemAtPosition(pos).toString();
            SportChip sportChip = new SportChip();
            sportChip.setName(selected_sport_string);

            sport_chips.addSelectedChip(sportChip);
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
}
