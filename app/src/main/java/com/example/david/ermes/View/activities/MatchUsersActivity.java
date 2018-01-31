package com.example.david.ermes.View.activities;

<<<<<<< HEAD
import android.os.Bundle;
=======
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
<<<<<<< HEAD
=======
import android.widget.Button;
import android.widget.LinearLayout;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
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

<<<<<<< HEAD
=======
    public static final String ACTIVITY_TYPE_KEY = "activity_type";
    public static final String PARTECIPANTS_TYPE = "PARTECIPANTS_TYPE";
    public static final String INVITED_TYPES = "INVITED_TYPES";

    private LinearLayout container;

>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
    private Toolbar toolbar;
    private UserListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView no_users_label;
<<<<<<< HEAD

    private List<String> userIdList;
    private String title;
    private User currentUser;
=======
    private Button create_teams_btn;

    private List<String> userIdList;
    private User currentUser;
    private String activity_type;

    private Integer userCount;
    private Boolean fetchData;
    private boolean already_show_odd_partecipant_dialog;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

<<<<<<< HEAD
        adapter = new UserListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        no_users_label = findViewById(R.id.no_friends_label);
=======
        container = findViewById(R.id.activity_friends_container);

        adapter = new UserListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        no_users_label = findViewById(R.id.no_friends_label);
        create_teams_btn = findViewById(R.id.create_random_teams_button);
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

        recyclerView = findViewById(R.id.friends_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        currentUser = getIntent().getExtras().getParcelable("user");
        userIdList = getIntent().getExtras().getStringArrayList("users");
<<<<<<< HEAD
        title = getIntent().getExtras().getString("title", "Title");

        adapter.setCurrentUser(currentUser);
        adapter.refreshUserList(userIdList, null);

        toolbar = findViewById(R.id.friends_toolbar);
        toolbar.setTitle(title + " (" + userIdList.size() + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
=======
        activity_type = getIntent().getExtras().getString(ACTIVITY_TYPE_KEY, "type");
        already_show_odd_partecipant_dialog = false;

        adapter.setCurrentUser(currentUser);
        adapter.refreshUserList(userIdList, object -> {
            userCount = (Integer) object;
            fetchData = userCount != null && userCount > 0;
        });

        toolbar = findViewById(R.id.friends_toolbar);

        setContent();
    }

    private void setContent() {
        switch (activity_type) {
            case PARTECIPANTS_TYPE:
                toolbar.setTitle("Partecipanti (" + userIdList.size() + ")");

                create_teams_btn.setVisibility(View.VISIBLE);
                create_teams_btn.setOnClickListener(v -> createRandomTeams());
                break;
            case INVITED_TYPES:
                toolbar.setTitle("Invitati (" + userIdList.size() + ")");
                create_teams_btn.setVisibility(View.GONE);
                break;
        }
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

        if (userIdList.size() > 0) {
            no_users_label.setVisibility(View.GONE);
        } else {
            no_users_label.setText("Nessun utente");
            no_users_label.setVisibility(View.VISIBLE);
        }
<<<<<<< HEAD
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
=======

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void createRandomTeams() {
        if (fetchData) {
            if (userCount < 2) {
                Snackbar.make(container, "Devono esserci almeno 2 partecipanti per creare le squadre.",
                        Snackbar.LENGTH_LONG).show();
            } else if (userCount % 2 != 0 && !already_show_odd_partecipant_dialog) {
                already_show_odd_partecipant_dialog = true;

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Il numero dei partecipanti è dispari, la squadra 2 avrà " +
                                "un giocatore in più della squadra 1.")
                        .setPositiveButton("OK", ((dialog, which) -> adapter.createRandomTeams()))
                        .show();
            } else {
                adapter.createRandomTeams();
            }
        } else {
            Snackbar.make(container, "Attendi lo scaricamento dei dati, riprova tra poco.",
                    Snackbar.LENGTH_LONG).show();
        }
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
