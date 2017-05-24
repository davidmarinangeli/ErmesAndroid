package com.example.david.ermes.View.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.example.david.ermes.R;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        TextView sportTitle= (TextView) findViewById(R.id.textViewInsertSport);
        sportTitle.setAlpha(0.87f);

        EditText sportSelected = (EditText) findViewById(R.id.editTextSportSelected);

        TextView dateTitle= (TextView) findViewById(R.id.textViewInsertDate);
        dateTitle.setAlpha(0.87f);







    }
}
