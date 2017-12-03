package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.david.ermes.PickPlaceActivity;
import com.example.david.ermes.R;

public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button open_picker_maps_button;
    private EditText place_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        place_name = findViewById(R.id.nome_location);
        open_picker_maps_button = findViewById(R.id.open_map_picker);
        open_picker_maps_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.open_map_picker){

            if (place_name.getText() != null) {


                Intent open_activity = new Intent(this, PickPlaceActivity.class);
                open_activity.putExtra("location",String.valueOf(place_name.getText()));

                startActivity(open_activity);
            } else {
                place_name.setHighlightColor(getResources().getColor(R.color.red));
            }

        }
    }
}
