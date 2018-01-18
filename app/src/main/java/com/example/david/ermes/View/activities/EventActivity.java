package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.fragments.EventFragment;

public class EventActivity extends AppCompatActivity {

    public static final int INVITE_FRIEND_CODE = 12345;

    private Match currentMatch;
    private EventFragment eventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar tb = findViewById(R.id.event_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // passo gli extra presi
        eventFragment = new EventFragment();
        Bundle args = new Bundle();
        currentMatch = getIntent().getExtras().getParcelable("event");
        args.putParcelable("event", currentMatch);
        eventFragment.setArguments(args);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.event_container, eventFragment).commit();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INVITE_FRIEND_CODE && resultCode == RESULT_OK) {
            Match saved_match = data.getParcelableExtra("new_match");
            eventFragment.updateMatch(saved_match);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
