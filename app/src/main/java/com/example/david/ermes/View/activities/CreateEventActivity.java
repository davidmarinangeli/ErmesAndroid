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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

    private TextView event_orario_textview;
    private TextView event_data_textview;

    private Spinner sport_selector;
    private Spinner location_selector;

    private ImageButton fine_creazione;
    private Button num_players_button;

    private ChipsInputLayout missing_chips;

    private SpinnerAdapter sportadapter;
    private SpinnerAdapter locationadapter;

    private SwitchCompat ispublic_switch;

    private Calendar match_calendar_time;

    private FusedLocationProviderClient mFusedLocationClient;
    private Toolbar toolbar;

    private String selected_sport_string;
    private String selected_location_string;

    private com.example.david.ermes.Model.models.Location selected_location;


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
        toolbar = findViewById(R.id.create_event_toolbar);

        sport = new Sport();
        downloaded_locations = new ArrayList<>();

        toolbar.setTitle("Crea l'evento");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        event_data_textview.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(onDateSetListener);
            datePickerDialog.setMinDate(Calendar.getInstance());
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

        // creo l'alert con i numeri e lo mostro
        AlertDialog alert = getAlertDialog();
        num_players_button.setOnClickListener(v -> alert.show());

        //setto i parametri delle chips
        missing_chips.setShowChipAvatarEnabled(false);
        fine_creazione.setOnClickListener(v -> {
            ArrayList<MissingStuffElement> chips_title_list = new ArrayList<>();
            for (Chip chip : missing_chips.getSelectedChips()) {
                chips_title_list.add(new MissingStuffElement(chip.getTitle(), false, ""));
            }

            if (selected_sport_string != null && selected_location != null) {
                createEventPresenter.saveMatch(match_calendar_time.getTimeInMillis(),
                        selected_sport_string,
                        selected_location,
                        chips_title_list,
                        ispublic_switch.isChecked(),
                        String.valueOf(num_players_button.getText()));
            }
        });

        createEventPresenter = new CreateEventPresenter(this);

    }

    private AlertDialog getAlertDialog() {
        MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(30)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        return new AlertDialog.Builder(this)
                .setTitle("Numero giocatori")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok),
                        (dialogInterface, i) -> num_players_button.setText(String.valueOf(numberPicker.getValue()))).create();
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


    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

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