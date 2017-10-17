package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.david.ermes.Presenter.Match;
import com.example.david.ermes.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        TextView orario = findViewById(R.id.textTime);
        TextView data = findViewById(R.id.textDate);



        orario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog time = TimePickerDialog.newInstance(CreateEventActivity.this,now.get(Calendar.HOUR),now.get(Calendar.MINUTE), now.get(Calendar.SECOND),true);
                time.show(getFragmentManager(), "Timepickerdialog");
            }

        });
        data.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog date = DatePickerDialog.newInstance(CreateEventActivity.this,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                date.show(getFragmentManager(),"Datepickerdialog");

            }
        });


        Button fine = findViewById((R.id.buttonfine));
        final Match m = new Match("basket",213123131,32924,"albere", null);

        fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //qui mettere il comportamento alla creazione del match
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
