package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.FriendshipRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.FriendsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicol on 15/01/2018.
 */

public class MatchUsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FriendsListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView no_users_label;

    private List<String> userIdList;
    private String title;
    private String subtitle;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        adapter = new FriendsListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        no_users_label = findViewById(R.id.no_friends_label);

        recyclerView = findViewById(R.id.friends_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        userIdList = getIntent().getExtras().getStringArrayList("users");
        title = getIntent().getExtras().getString("title", "Title");

        toolbar = findViewById(R.id.friends_toolbar);
        toolbar.setTitle(title + " (" + userIdList.size() + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userList = new ArrayList<>();
        resetFetchUsersCount();
        for (String id : userIdList) {
            UserRepository.getInstance().fetchUserById(id, object -> {
                if (object != null) {
                    userList.add((User) object);
                }

                incrementFetchUsersCount();
                if (getFetchUsersCount() == userIdList.size()) {
                    adapter.refreshList(userList);
                }
            });
        }

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
