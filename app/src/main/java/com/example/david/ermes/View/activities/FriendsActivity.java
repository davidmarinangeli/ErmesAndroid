package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
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

public class FriendsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FriendsListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView no_friends_label;

    private User currentUser;
    private Map<Friendship, User> collection;

    private static int fetch_friends_count = 0;

    private void incrementFetchFriendsCount() {
        fetch_friends_count++;
    }

    private void resetFetchFriendsCount() {
        fetch_friends_count = 0;
    }

    private int getFetchFriendsCount() {
        return fetch_friends_count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        adapter = new FriendsListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        no_friends_label = findViewById(R.id.no_friends_label);

        recyclerView = findViewById(R.id.friends_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        currentUser = getIntent().getExtras().getParcelable("user");

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

            if (collection == null) {
                refreshList();
            }
        } else {
            no_friends_label.setText("Nessun utente loggato");
            no_friends_label.setVisibility(View.VISIBLE);
        }
    }

    private void refreshList() {
        FriendshipRepository.getInstance().fetchFriendshipsByUserId(currentUser.getUID(), object -> {
            List<Friendship> list = (List<Friendship>) object;

            if (list == null || list.isEmpty()) {
                adapter.refreshList(new HashMap<>());

                no_friends_label.setText("Nessun amico");
                no_friends_label.setVisibility(View.VISIBLE);
            } else {
                toolbar.setTitle("Amici (" + list.size() + ")");
                no_friends_label.setVisibility(View.GONE);

                collection = new HashMap<>();
                resetFetchFriendsCount();

                for (Friendship f : list) {
                    String id = f.getId1() == currentUser.getUID() ?
                            f.getId2() : f.getId1();

                    UserRepository.getInstance().fetchUserById(id, object1 -> {
                        collection.put(f, (User) object1);

                        incrementFetchFriendsCount();
                        if (getFetchFriendsCount() == list.size()) {
                            adapter.refreshList(collection);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
