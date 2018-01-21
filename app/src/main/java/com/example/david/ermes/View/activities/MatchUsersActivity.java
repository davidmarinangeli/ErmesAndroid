package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 15/01/2018.
 */

public class MatchUsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UserListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView no_users_label;

    private List<String> userIdList;
    private String title;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        adapter = new UserListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        no_users_label = findViewById(R.id.no_friends_label);

        recyclerView = findViewById(R.id.friends_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        currentUser = getIntent().getExtras().getParcelable("user");
        userIdList = getIntent().getExtras().getStringArrayList("users");
        title = getIntent().getExtras().getString("title", "Title");

        adapter.setCurrentUser(currentUser);
        adapter.refreshUserList(userIdList, null);

        toolbar = findViewById(R.id.friends_toolbar);
        toolbar.setTitle(title + " (" + userIdList.size() + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (userIdList.size() > 0) {
            no_users_label.setVisibility(View.GONE);
        } else {
            no_users_label.setText("Nessun utente");
            no_users_label.setVisibility(View.VISIBLE);
        }
    }

    private int fetch_users_count = 0;
    private void incrementFetchUsersCount() {
        fetch_users_count++;
    }

    private int getFetchUsersCount() {
        return fetch_users_count;
    }

    private void resetFetchUsersCount() {
        fetch_users_count = 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
