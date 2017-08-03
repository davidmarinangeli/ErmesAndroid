package com.example.david.ermes.View.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.ermes.Presenter.MainAdapter;
import com.example.david.ermes.Presenter.Match;
import com.example.david.ermes.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        TextView orario = (TextView) findViewById(R.id.textTime);
        TextView data = (TextView) findViewById(R.id.textDate);



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


        Button fine = (Button) findViewById((R.id.buttonfine));
        fine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Match x =new Match("Clacio",1500547551959L,12,"Parco tua mamma");
                    startActivity(new Intent(CreateEventActivity.this, MainActivity.class));


                }
        });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        TextView orario = (TextView) findViewById(R.id.textTime);
        orario.setText(""+hourOfDay+":"+minute);

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        TextView data = (TextView) findViewById(R.id.textDate);
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
