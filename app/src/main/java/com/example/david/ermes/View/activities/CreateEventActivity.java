package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.david.ermes.Presenter.FirebaseCallback;
import com.example.david.ermes.Presenter.Location;
import com.example.david.ermes.Presenter.Match;
import com.example.david.ermes.Presenter.Sport;
import com.example.david.ermes.Presenter.User;
import com.example.david.ermes.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    TextView event_orario_textview;
    TextView event_data_textview;
    EditText location_edittext;
    Spinner sport_selector;
    Button fine_creazione;
    SpinnerAdapter adapter;
    Calendar match_calendar_time;

    String selected_sport;
    Sport sport;
    Location location;


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

        event_data_textview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(onDateSetListener);
                datePickerDialog.show(getFragmentManager(),"datepickerdialog");
            }
        });

        event_orario_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener,true);
                timePickerDialog.show(getFragmentManager(),"timepickerdialog");
            }
        });

        sport_selector.setOnItemSelectedListener(new itemSelectedListener());

        final ArrayList<String> arraySpinner = new ArrayList<>();
        Sport.fetchAllSports(new FirebaseCallback() {
            @Override
            public void callback(List list) {
                for (Sport s : (ArrayList<Sport>) list) {
                    arraySpinner.add(s.getName());
                }
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, arraySpinner);
                sport_selector.setAdapter(adapter);



            }
        });

        fine_creazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                User current_user = User.getCurrentUser();

                location = new Location("Alessandro Volta", 46.0490089, 11.123597, current_user);

                //sport = (Sport) sport_selector.getSelectedItem();

                List<String> missingstuff = new ArrayList<>();
                missingstuff.add("rete");
                missingstuff.add("pallone");

                //qui mettere il comportamento alla creazione del match
                Match result_match = new Match(
                        current_user.getUID(),
                        location,
                        com.example.david.ermes.Presenter.utils.TimeUtils.fromMillisToDate(match_calendar_time.getTimeInMillis()),
                        true,
                        //sport.getName()
                        selected_sport,
                        //sport.getNumPlayers()
                        10,
                        2,
                        missingstuff
                );

                result_match.save();

                Intent result_intent = new Intent(v.getContext(),MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("new_event",result_match);
                result_intent.putExtras(bundle);
                setResult(Activity.RESULT_OK,result_intent);

                finish();
            }
        });
    }

    public class itemSelectedListener implements AdapterView.OnItemSelectedListener {

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
            match_calendar_time.set(Calendar.YEAR,year);
            match_calendar_time.set(Calendar.MONTH,month);
            match_calendar_time.set(Calendar.DAY_OF_MONTH,day);

            event_data_textview.setText(com.example.david.ermes.Presenter.utils.TimeUtils.fromMillistoYearMonthDay(match_calendar_time.getTimeInMillis()));

        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            match_calendar_time.set(Calendar.HOUR_OF_DAY,hourOfDay);
            match_calendar_time.set(Calendar.MINUTE,minute);
            match_calendar_time.set(Calendar.SECOND,second);

            String hour_minute = hourOfDay +":"+minute;
            event_orario_textview.setText(hour_minute);
        }
    };
}
