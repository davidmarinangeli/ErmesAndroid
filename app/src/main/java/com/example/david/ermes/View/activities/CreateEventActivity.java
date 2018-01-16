package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.MissingStuffElement;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.CreateEventPresenter;
import com.example.david.ermes.Presenter.utils.LocationUtils;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tylersuehr.chips.ChipsInputLayout;
import com.tylersuehr.chips.data.Chip;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class CreateEventActivity extends AppCompatActivity {

    TextView event_orario_textview;
    TextView event_data_textview;

    Spinner sport_selector;
    Spinner location_selector;

    Button fine_creazione;
    Button num_players_button;

    ChipsInputLayout missing_chips;

    SpinnerAdapter sportadapter;
    SpinnerAdapter locationadapter;

    SwitchCompat ispublic_switch;

    Calendar match_calendar_time;

    private FusedLocationProviderClient mFusedLocationClient;


    String selected_sport_string;
    String selected_location_string;

    com.example.david.ermes.Model.models.Location selected_location;


    final String SPORT_HINT = "Seleziona uno sport...";

    // mi salvo gli OGGETTI Location così da evitare una callback in più dopo per fetchare gli oggetti a partire dai nomi
    ArrayList<com.example.david.ermes.Model.models.Location> downloaded_locations;
    Sport sport;
    Location user_location;

    private CreateEventPresenter createEventPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        event_orario_textview = findViewById(R.id.textTime);
        event_data_textview = findViewById(R.id.textDate);

        location_selector = findViewById(R.id.location_spinner);
        sport_selector = findViewById(R.id.sport_spinner);

        missing_chips = findViewById(R.id.chips_input);

        fine_creazione = findViewById(R.id.buttonfine);
        num_players_button = findViewById(R.id.choose_players_number);

        ispublic_switch = findViewById(R.id.ispublic_switch);

        match_calendar_time = Calendar.getInstance();

        sport = new Sport();
        downloaded_locations = new ArrayList<>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        event_data_textview.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(onDateSetListener);
            datePickerDialog.show(getFragmentManager(), "datepickerdialog");
        });

        event_orario_textview.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener, true);
            timePickerDialog.show(getFragmentManager(), "timepickerdialog");
        });

        // imposto i listener per entrambi
        sport_selector.setOnItemSelectedListener(new sportSpinnerSelectedListener());
        location_selector.setOnItemSelectedListener(new locationSpinnerSelectedListener());

        //inizializzo lo spinner per lo sport
        createSportSpinner();

        // prima di inizializzare lo spinner per la location vedo se ho la posizione...
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {

                        // se mi ha garantito l'accesso ed ho l'ultima location creo lo spinner
                        if (location != null) {
                            user_location = location;
                            createLocationSpinner();
                        } else {
                            // altrimenti lo creo con altri parametri ì
                        }
                    });
        }

        num_players_button.setOnClickListener(v -> showNumDialog());
        fine_creazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ArrayList<MissingStuffElement> chips_title_list = new ArrayList<>();
                for (Chip chip : missing_chips.getSelectedChips()) {
                    chips_title_list.add(new MissingStuffElement(chip.getTitle(), false, ""));
                }

        fine_creazione.setOnClickListener(v -> {
            ArrayList<MissingStuffElement> chips_title_list = new ArrayList<>();
            for (Chip chip : missing_chips.getSelectedChips()) {
                chips_title_list.add(new MissingStuffElement(chip.getTitle(), false, ""));
            }

            if (selected_sport_string != null && selected_location != null) {
                createEventPresenter.saveMatch(match_calendar_time.getTimeInMillis(), selected_sport_string, selected_location, chips_title_list, ispublic_switch.isChecked());
            }
        });

        createEventPresenter = new CreateEventPresenter(this);
    }

    private ArrayList<com.example.david.ermes.Model.models.Location> createLocationSpinner() {

        //insieme ai nomi delle location, da inserire nello spinner
        final ArrayList<String> locationSpinner = new ArrayList<>();

        LocationRepository.getInstance().fetchLocationsByProximity(
                LocationUtils.fromAndroidLocationtoErmesLocation(user_location),
                object -> {
                    for (com.example.david.ermes.Model.models.Location l : (ArrayList<com.example.david.ermes.Model.models.Location>) object) {
                        locationSpinner.add(l.getName());
                        downloaded_locations.add(l);
                    }
                    locationadapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, locationSpinner);
                    location_selector.setAdapter(locationadapter);


                });
        return downloaded_locations;
    }

    private MaterialNumberPicker createDialog(){
        MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(10)
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
        return numberPicker;

    }
    private void showNumDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Numero giocatori")
                .setView(createDialog())
                .setPositiveButton(getString(android.R.string.ok), (dialog, which) ->

                        Toast.makeText(this,"Yay",Toast.LENGTH_SHORT).show())
                .show();
    }
    private void createSportSpinner() {

        final ArrayList<String> arraySpinner = new ArrayList<>();

        SportRepository.getInstance().fetchAll(object -> {
            for (Sport s : (ArrayList<Sport>) object) {
                arraySpinner.add(s.getName());
            }
            sportadapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, arraySpinner);
            sport_selector.setAdapter(sportadapter);


        });
    }

    public void goToMainActivity(Match resultMatch) {
        Intent result_intent = new Intent(CreateEventActivity.this, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("new_event", resultMatch);
        result_intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, result_intent);

        finish();
    }

    // inizio abominio per i listener negli spinner
    private class sportSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected_sport_string = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    private class locationSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected_location_string = parent.getItemAtPosition(pos).toString();

            for (com.example.david.ermes.Model.models.Location l : downloaded_locations) {
                if (l.getName().equals(selected_location_string)) {
                    selected_location = l;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    // fine abominio per gli spinner


    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePicker, int year, int month, int day) {
            match_calendar_time.set(Calendar.YEAR, year);
            match_calendar_time.set(Calendar.MONTH, month);
            match_calendar_time.set(Calendar.DAY_OF_MONTH, day);

            event_data_textview.setText(TimeUtils.fromMillistoYearMonthDay(match_calendar_time.getTimeInMillis()));

        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            match_calendar_time.set(Calendar.HOUR_OF_DAY, hourOfDay);
            match_calendar_time.set(Calendar.MINUTE, minute);
            match_calendar_time.set(Calendar.SECOND, second);

            String hour_minute = hourOfDay + ":" + minute;
            event_orario_textview.setText(hour_minute);
        }
    };
}