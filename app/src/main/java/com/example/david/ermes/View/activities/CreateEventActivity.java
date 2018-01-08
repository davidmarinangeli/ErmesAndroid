package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.CreateEventPresenter;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    TextView event_orario_textview;
    TextView event_data_textview;
    EditText location_edittext;
    Spinner sport_selector;
    Button fine_creazione;
    SpinnerAdapter adapter;
    Calendar match_calendar_time;

    private FusedLocationProviderClient mFusedLocationClient;


    String selected_sport;
    final String SPORT_HINT = "Seleziona uno sport...";
    Sport sport;

    private CreateEventPresenter createEventPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        event_orario_textview = findViewById(R.id.textTime);
        event_data_textview = findViewById(R.id.textDate);

        location_edittext = findViewById(R.id.luogo);
        sport_selector = findViewById(R.id.sport_spinner);
        fine_creazione = findViewById(R.id.buttonfine);
        match_calendar_time = Calendar.getInstance();

        sport = new Sport();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        event_data_textview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(onDateSetListener);
                datePickerDialog.show(getFragmentManager(), "datepickerdialog");
            }
        });

        event_orario_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener, true);
                timePickerDialog.show(getFragmentManager(), "timepickerdialog");
            }
        });

        sport_selector.setOnItemSelectedListener(new itemSelectedListener());

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });
        }

        final ArrayList<String> arraySpinner = new ArrayList<>();
        SportRepository.getInstance().fetchAll(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                for (Sport s : (ArrayList<Sport>) object) {
                    arraySpinner.add(s.getName());
                    //arraySpinner.add(0,SPORT_HINT);
                }
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, arraySpinner) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        } else {
                            return true;
                        }
                    }
                };
                sport_selector.setAdapter(adapter);


            }
        });

        fine_creazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (selected_sport != null && !selected_sport.isEmpty()) {
                    createEventPresenter.saveMatch(match_calendar_time.getTimeInMillis(), selected_sport);
                }
            }
        });

        createEventPresenter = new CreateEventPresenter(this);
    }

    public void goToMainActivity(Match resultMatch) {
        Intent result_intent = new Intent(CreateEventActivity.this, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("new_event", resultMatch);
        result_intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, result_intent);

        finish();
    }

    private class itemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected_sport = parent.getItemAtPosition(pos).toString();

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
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