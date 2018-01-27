package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.TeamRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.TeamsAdapter;

import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private TeamsAdapter adapter;
    private RecyclerView recyclerView;

    private Toolbar toolbar;
    private TextView no_teams;
    private ImageButton add_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        adapter = new TeamsAdapter(this);
        recyclerView = findViewById(R.id.teams_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL));

        toolbar = findViewById(R.id.teams_toolbar);
        toolbar.setTitle("I miei team");

        no_teams = findViewById(R.id.no_teams_label);
        no_teams.setVisibility(View.GONE);

        add_team = findViewById(R.id.team_toolbar_action_button);
        add_team.setOnClickListener(v -> startCreateTeamActivity());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initList() {
        if (DatabaseManager.get().isLogged()) {
            TeamRepository.getInstance().fetchTeamsByUserId(User.getCurrentUserId(),
                    object -> {
                        List<Team> list = (List<Team>) object;

                        if (list != null && !list.isEmpty()) {
                            no_teams.setVisibility(View.GONE);
                            adapter.refreshList(list);
                        } else {
                            no_teams.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private void startCreateTeamActivity() {
        Bundle extras = new Bundle();
        extras.putString(TeamActivity.ACTIVITY_TYPE_KEY, TeamActivity.CREATE_TEAM);

        Intent i = new Intent(this, TeamActivity.class);
        i.putExtras(extras);
        startActivity(i);
    }
}
