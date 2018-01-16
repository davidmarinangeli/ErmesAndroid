package com.example.david.ermes.View.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.david.ermes.R;
import com.example.david.ermes.View.PickFriendsAdapter;

public class PickFriendsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView friendsrecyclerview;
    private PickFriendsAdapter pickFriendsAdapter;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_friends);

        toolbar = findViewById(R.id.pick_people_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Invita amici");

        linearLayoutManager = new LinearLayoutManager(this);
        pickFriendsAdapter = new PickFriendsAdapter();
        friendsrecyclerview = findViewById(R.id.pick_friends_recycler);
        friendsrecyclerview.setAdapter(pickFriendsAdapter);
        friendsrecyclerview.setNestedScrollingEnabled(false);
        friendsrecyclerview.setLayoutManager(linearLayoutManager);






    }
}
