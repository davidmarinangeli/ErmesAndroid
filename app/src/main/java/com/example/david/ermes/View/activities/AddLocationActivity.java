package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.david.ermes.PickPlaceActivity;
import com.example.david.ermes.R;

public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button open_picker_maps_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        open_picker_maps_button = findViewById(R.id.open_map_picker);
        open_picker_maps_button.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.open_map_picker){
            Intent open_activity = new Intent(this, PickPlaceActivity.class);
            startActivity(open_activity);

        }
    }
}
