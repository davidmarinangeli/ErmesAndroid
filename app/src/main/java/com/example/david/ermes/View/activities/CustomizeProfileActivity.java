package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.david.ermes.R;
import com.example.david.ermes.View.fragments.ProfileFragment;

/**
 * Created by EMarcantoni on 17-Jan-18.
 */

public class CustomizeProfileActivity extends AppCompatActivity {


    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.my_profile_toolbar);
        toolbar.setTitle("Modifica il tuo account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProfileFragment customizeProfileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", getIntent().getExtras().getParcelable("user"));
        customizeProfileFragment.setArguments(args);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.customize_profile_container,customizeProfileFragment).commit();

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
