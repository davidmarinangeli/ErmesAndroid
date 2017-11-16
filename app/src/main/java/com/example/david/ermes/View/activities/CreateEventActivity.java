package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.github.clans.fab.FloatingActionMenu;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener{

    TextView orario;
    TextView data;
    EditText location;
    Spinner sport_selector;
    Button fine_creazione;
    SpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        orario = findViewById(R.id.textTime);
        location = findViewById(R.id.luogo);
        data = findViewById(R.id.textDate);
        sport_selector = findViewById(R.id.sport_spinner);
        fine_creazione = findViewById(R.id.buttonfine);


        orario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog time = TimePickerDialog.newInstance(
                        CreateEventActivity.this,
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        true);
                time.show(getFragmentManager(), "Timepickerdialog");
            }

        });

        data.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog date = DatePickerDialog.newInstance(CreateEventActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                date.show(getFragmentManager(),"Datepickerdialog");

                //Log.d("TIMEPICKER", String.valueOf(date..get(Calendar.DAY_OF_MONTH)));

            }
        });


        final ArrayList<String> arraySpinner = new ArrayList<>();
        Sport.fetchAllSports(new FirebaseCallback() {
            @Override
            public void callback(List list) {
                for (Sport s : (ArrayList<Sport>) list){
                    arraySpinner.add(s.getName());
                }
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item,arraySpinner);
                sport_selector.setAdapter(adapter);
            }
        });

        fine_creazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User current_user = User.getCurrentUser();
                Location location = new Location();
                location.setName(location.getName());
                //qui mettere il comportamento alla creazione del match
                //Match result_match = new Match(current_user.getName(),location,);

                finish();
            }
        });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        TextView orario = findViewById(R.id.textTime);
        orario.setText(""+hourOfDay+":"+minute);

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        TextView data = findViewById(R.id.textDate);
        String meseTestuale = new String();
        switch(monthOfYear){
            case 0 : meseTestuale="Gennaio";
                break;
            case 1 : meseTestuale="Febbraio";
                break;
            case 2 : meseTestuale="Marzo";
                break;
            case 3 : meseTestuale="Aprile";
                break;
            case 4 : meseTestuale="Maggio";
                break;
            case 5 : meseTestuale="Giugno";
                break;
            case 6 : meseTestuale="Luglio";
                break;
            case 7 : meseTestuale="Agosto";
                break;
            case 8 : meseTestuale="Settembra";
                break;
            case 9 : meseTestuale="Ottobre";
                break;
            case 10 : meseTestuale="Novembre";
                break;
            case 11 : meseTestuale="Dicembre";
                break;

        }

        data.setText(dayOfMonth+" "+meseTestuale+" "+year);

    }
}
