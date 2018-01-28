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
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.TeamRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.TeamsAdapter;

import java.util.List;

public class TeamsActivity extends AppCompatActivity {
    public static final String ACTIVITY_CODE_KEY = "activity_code";
    public static final int VIEW_CODE = 123;
    public static final int PICK_CODE = 234;

    private TeamsAdapter adapter;
    private RecyclerView recyclerView;

    private Toolbar toolbar;
    private TextView no_teams;
    private ImageButton add_team;

    private int code;
    private Match result_match;

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

        no_teams = findViewById(R.id.no_teams_label);
        no_teams.setVisibility(View.GONE);

        add_team = findViewById(R.id.team_toolbar_action_button);
        add_team.setOnClickListener(v -> startCreateTeamActivity());

        code = getIntent().getIntExtra(ACTIVITY_CODE_KEY, 0);
        result_match = getIntent().getParcelableExtra("match");

        adapter.setMatch(result_match);

        setContent();
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

    private void setContent() {
        switch (code) {
            case VIEW_CODE:
                toolbar.setTitle("I miei team");
                add_team.setVisibility(View.VISIBLE);
                add_team.setOnClickListener(v -> startCreateTeamActivity());
                break;
            case PICK_CODE:
                toolbar.setTitle("Invita un team");
                add_team.setVisibility(View.GONE);

                adapter.setOnInviteTeamToMatch(match -> successfullyTeamInvited(match));
                break;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initList() {
        if (DatabaseManager.get().isLogged()) {
            TeamRepository.getInstance().fetchTeamsByUserId(User.getCurrentUserId(),
                    object -> {
                        List<Team> list = (List<Team>) object;

                        if (list != null && !list.isEmpty()) {
                            no_teams.setVisibility(View.GONE);
                            adapter.refreshList(list, code);
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

    private void successfullyTeamInvited(Match match) {
        match.save(object -> {
            Intent save_intent = new Intent();
            save_intent.putExtra("new_match", match);
            setResult(RESULT_OK, save_intent);
            finish();
        });
    }
}
