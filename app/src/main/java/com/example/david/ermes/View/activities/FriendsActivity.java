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
import com.example.david.ermes.R;
import com.example.david.ermes.View.UserListAdapter;

/**
 * Created by nicol on 15/01/2018.
 */

public class FriendsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UserListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView no_friends_label;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        currentUser = getIntent().getExtras().getParcelable("user");

        adapter = new UserListAdapter(this);
        adapter.setCurrentUser(currentUser);
        layoutManager = new LinearLayoutManager(this);
        no_friends_label = findViewById(R.id.no_friends_label);

        recyclerView = findViewById(R.id.friends_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        toolbar = findViewById(R.id.friends_toolbar);
        toolbar.setTitle("Amici");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentUser != null) {
            toolbar.setSubtitle(currentUser.getName());
            no_friends_label.setVisibility(View.GONE);

            adapter.refreshFriendList(object -> {
                int count = (int) object;

                if (count <= 0) {
                    no_friends_label.setText("Nessun amico");
                    no_friends_label.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setTitle(String.valueOf(count).concat(" Amici"));
                    no_friends_label.setVisibility(View.GONE);
                }
            });
        } else {
            no_friends_label.setText("Nessun utente loggato");
            no_friends_label.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
