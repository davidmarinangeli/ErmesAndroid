package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.UserListAdapter;

public class TeamActivity extends AppCompatActivity {

    public static final String CREATE_TEAM = "CREATE_TEAM";
    public static final String TEAM_VIEW = "TEAM_VIEW";
    public static final String ACTIVITY_TYPE_KEY = "contentType";
    public static final String ACTIVITY_TEAM_KEY = "team";
    public static final String ACTIVITY_REQUEST_CODE_KEY = "requestCode";
    public static final int SELECT_FRIENDS_CREATE_TEAM_REQUEST_CODE = 7;
    public static final int SELECT_FRIENDS_ADD_REQUEST_CODE = 13;

    private User currentUser;
    private Team currentTeam;
    private String activityType;
    private UserListAdapter adapter;

    private Toolbar toolbar;

    private ConstraintLayout create_team_container;
    private RecyclerView team_recycler_view;

    private EditText team_name_edit_text;
    private Button add_friends_btn;
    private ImageButton action_btn;
    private TextView invite_text;
    private Button leave_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        initGlobalValues();

        toolbar = findViewById(R.id.team_toolbar);
        create_team_container = findViewById(R.id.create_team_container);
        team_recycler_view = findViewById(R.id.team_users_recycler_view);

        team_name_edit_text = findViewById(R.id.create_team_name);
        add_friends_btn = findViewById(R.id.create_team_add_friends);
        action_btn = findViewById(R.id.team_toolbar_action_button);
        invite_text = findViewById(R.id.selected_friends_number_label);
        leave_btn = findViewById(R.id.team_leave_button);

        adapter = new UserListAdapter(this);
        team_recycler_view.setAdapter(adapter);
        team_recycler_view.setNestedScrollingEnabled(false);
        team_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        team_recycler_view.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initGlobalValues() {
        Bundle args = getIntent().getExtras();
        currentTeam = args != null ?
                args.getParcelable(ACTIVITY_TEAM_KEY)
                : null;
        activityType = args != null ?
                args.getString(ACTIVITY_TYPE_KEY)
                : null;
    }

    private void setContent() {
        switch (activityType) {
            case CREATE_TEAM:
                createTeamContent();
                break;
            case TEAM_VIEW:
                teamViewContent();
                break;
            default:
                Toast.makeText(this, "Cannot find 'contentType' value into intent",
                        Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    private void createTeamContent() {

        create_team_container.setVisibility(View.VISIBLE);
        team_recycler_view.setVisibility(View.GONE);
        action_btn.setImageResource(R.drawable.ic_done_white_24dp);
        action_btn.setVisibility(View.VISIBLE);
        leave_btn.setVisibility(View.GONE);

        toolbar.setTitle("Crea un nuovo team");

        add_friends_btn.setOnClickListener(
                v -> startSelectFriendsActivity(SELECT_FRIENDS_CREATE_TEAM_REQUEST_CODE));

        action_btn.setOnClickListener(v -> {
            action_btn.setEnabled(false);
            String team_name = team_name_edit_text.getText().toString();

            if (currentTeam == null || currentTeam.getUserIdList().size() == 0) {
                Toast.makeText(this, "Nessun amico selezionato", Toast.LENGTH_LONG).show();
                action_btn.setEnabled(true);
            } else if (team_name.replaceAll("\\s+", "").isEmpty()) {
                Toast.makeText(this, "Inserisci un nome", Toast.LENGTH_LONG).show();
                action_btn.setEnabled(true);
            } else {
                fetchCurrentUser(object -> {
                    currentTeam.setName(team_name);
                    currentTeam.addUser(User.getCurrentUserId());

                    currentTeam.save(object1 -> {
                        currentTeam = (Team) object1;

                        for (String userId : currentTeam.getUserIdList()) {
                            if (!userId.equals(currentUser.getUID())) {
                                Notification.createTeamAdded(currentUser, userId, currentTeam).save();
                            }
                        }

                        action_btn.setEnabled(true);
                        finish();
                    });
                });
            }
        });
    }

    private void teamViewContent() {
        if (currentTeam != null) {
            if (currentTeam.getUserIdList().contains(User.getCurrentUserId())) {
                create_team_container.setVisibility(View.GONE);
                team_recycler_view.setVisibility(View.VISIBLE);
                action_btn.setImageResource(R.drawable.ic_person_add_black_24dp);
                action_btn.setVisibility(View.VISIBLE);
                toolbar.setTitle(currentTeam.getName());
                leave_btn.setVisibility(View.VISIBLE);

                action_btn.setOnClickListener(
                        v -> startSelectFriendsActivity(SELECT_FRIENDS_ADD_REQUEST_CODE));

                leave_btn.setOnClickListener(v -> new MaterialDialog.Builder(this)
                        .title("Sei sicuro di voler abbandonare il team?")
                        .negativeText("No")
                        .negativeColor(getResources().getColor(R.color.red))
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .positiveText("Si")
                        .onPositive((dialog, which) -> leaveTeam())
                        .show());

                adapter.refreshUserList(currentTeam.getUserIdList(),
                        object -> toolbar.setSubtitle(String.valueOf((Integer) object) + " membri"));
            } else {
                Toast.makeText(this, "Non fai parte di questo team",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Cannot find team parcelable into intent",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void startSelectFriendsActivity(int code) {
        if (currentTeam == null) {
            currentTeam = new Team();
        }

        Bundle extras = new Bundle();
        extras.putParcelable("team", currentTeam);
        extras.putInt(ACTIVITY_REQUEST_CODE_KEY, code);

        Intent selectFriendsActivity = new Intent(this, PickFriendsActivity.class);
        selectFriendsActivity.putExtras(extras);
        startActivityForResult(selectFriendsActivity, code);
    }

    private void leaveTeam() {
        if (currentTeam != null) {
            fetchCurrentUser(object -> {
                currentTeam.removeUser(User.getCurrentUserId());
                currentTeam.save(object1 -> {
                    currentTeam = (Team) object1;

                    for (String userId : currentTeam.getUserIdList()) {
                        if (!userId.equals(currentUser.getUID())) {
                            Notification.leaveTeam(currentUser, userId, currentTeam).save();
                        }
                    }

                    finish();
                });
            });
        } else {
            Toast.makeText(this, "Cannot find team parcelable into intent",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void fetchCurrentUser(FirebaseCallback firebaseCallback) {
        if (currentUser == null) {
            UserRepository.getInstance().getUser(object -> {
                currentUser = (User) object;
                firebaseCallback.callback(currentUser);
            });
        } else {
            firebaseCallback.callback(currentUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SELECT_FRIENDS_CREATE_TEAM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    currentTeam = data.getParcelableExtra("result_team");
                    int num = currentTeam.getUserIdList().size();

                    if (num == 0) {
                        currentTeam = null;
                    } else {
                        invite_text.setText("Aggiunti " + String.valueOf(num) + " amici al team");
                    }
                }

                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }

                break;
            case SELECT_FRIENDS_ADD_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Team temp = data.getParcelableExtra("result_team");
                    int num = temp.getUserIdList().size() - currentTeam.getUserIdList().size();

                    if (num > 0) {
                        Team prev_team = currentTeam;
                        currentTeam = temp;

                        adapter.refreshUserList(currentTeam.getUserIdList(), null);
                        toolbar.setSubtitle(String.valueOf(currentTeam.getUserIdList().size()) +
                                " membri");

                        fetchCurrentUser(object ->
                            currentTeam.save(object1 -> {
                                currentTeam = (Team) object1;

                                for (String userId : currentTeam.getUserIdList()) {
                                    if (!prev_team.getUserIdList().contains(userId)) {
                                        Notification
                                                .createTeamAdded(currentUser, userId, currentTeam)
                                                .save();
                                    }
                                }
                            }));
                    }
                }
                break;
        }
    }
}
