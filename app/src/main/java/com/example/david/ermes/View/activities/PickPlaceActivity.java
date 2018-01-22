package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Context;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tylersuehr.chips.ChipsInputLayout;
import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipSelectionObserver;

import java.util.ArrayList;
import java.util.List;

public class PickPlaceActivity extends AppCompatActivity {

    Location location_create;
    MapView mMapView;
    private GoogleMap googleMap;
    private MaterialEditText place_name;
    private ImageButton dismiss;
    private ImageButton fine;
    private ChipsInputLayout sport_chips;

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
}
