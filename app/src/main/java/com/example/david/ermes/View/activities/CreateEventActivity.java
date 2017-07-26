package com.example.david.ermes.View.activities;

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
import com.example.david.ermes.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


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



    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Log.d("Orario","Ora" +hourOfDay+ "Minuti" +minute+ "Secondi" +second);

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("Data","Giorno" +dayOfMonth+ "Mese" +monthOfYear+ "Anno " +year);
    }
}
