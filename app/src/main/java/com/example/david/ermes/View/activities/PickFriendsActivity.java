package com.example.david.ermes.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.FriendshipRepository;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.PickFriendsAdapter;
import com.example.david.ermes.View.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class PickFriendsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView friendsrecyclerview;
    private PickFriendsAdapter pickFriendsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView no_friends;
    private ImageButton spunta_done;
    private Match result_match;
    private Team result_team;

    private ProgressDialog progressDialog;
    private boolean invite_match;
    private boolean invite_team;
    private int activity_request_code;
    private boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_friends);

        progressDialog = new ProgressDialog(this);
        saved = false;

        no_friends = findViewById(R.id.no_friends);
        toolbar = findViewById(R.id.create_event_toolbar);
        spunta_done = findViewById(R.id.spunta_done);

        toolbar.setTitle("Invita amici");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        activity_request_code = intent.getIntExtra(TeamActivity.ACTIVITY_REQUEST_CODE_KEY, 0);
        result_match = intent.getParcelableExtra("match");
        result_team = intent.getParcelableExtra(TeamActivity.ACTIVITY_TEAM_KEY);
        invite_match = result_match != null;
        invite_team = result_team != null;

        linearLayoutManager = new LinearLayoutManager(this);
        pickFriendsAdapter = new PickFriendsAdapter(this);
        friendsrecyclerview = findViewById(R.id.pick_friends_recycler);
        friendsrecyclerview.setAdapter(pickFriendsAdapter);
        friendsrecyclerview.setNestedScrollingEnabled(false);
        friendsrecyclerview.setLayoutManager(linearLayoutManager);
        initList();

        spunta_done.setOnClickListener(view -> {
            progressDialog.show();

            if (invite_match) {
                pickFriendsAdapter.saveFriendsList(object -> {
                    List<User> invited_friends = (List<User>) object;

                    if (invited_friends != null && !invited_friends.isEmpty()) {
                        for (User user : invited_friends) {
                            if (!result_match.getPartecipants().contains(user.getUID()) &&
                                    !result_match.getPending().contains(user.getUID())) {
                                result_match.addPending(user.getUID());
                                Notification invitation = Notification.createMatchInvitation(
                                        user.getUID(), result_match.getId());
                                if (invitation != null) {
                                    invitation.save();
                                } else {
                                    Snackbar.make(view,
                                            "Impossibile inviare inviti per la partita",
                                            Snackbar.LENGTH_LONG);
                                }
                            }
                        }

                        result_match.save(object1 -> {
                            Intent save_intent = new Intent();
                            save_intent.putExtra("new_match", result_match);
                            setResult(RESULT_OK, save_intent);

                            progressDialog.dismiss();
                            finish();
                        });
                    } else {
                        setResult(RESULT_CANCELED);
                        progressDialog.dismiss();
                        finish();
                    }
                });
            } else if (invite_team) {
                result_team = pickFriendsAdapter.getResultTeam();

                Bundle extras = new Bundle();
                extras.putParcelable("result_team", result_team);

                Intent returnIntent = new Intent();
                returnIntent.putExtras(extras);
                setResult(Activity.RESULT_OK, returnIntent);

                progressDialog.dismiss();
                finish();
            }
        });

        setNoFriendsState();

    }

    public void initList() {
        if (DatabaseManager.get().isLogged()) {
            progressDialog.show();

            FriendshipRepository.getInstance().fetchFriendshipsByUserId(User.getCurrentUserId(), object -> {
                if (object != null) {
                    List<Friendship> user_friends = (List<Friendship>) object;
                    List<User> my_friends = new ArrayList<>();

                    resetFetchFriendsCount();

                    if (!user_friends.isEmpty()) {
                        setMoreThanZeroFriendsState();

                        for (Friendship f : user_friends) {
                            String id = f.getId1().equals(User.getCurrentUserId()) ? f.getId2() : f.getId1();
                            UserRepository.getInstance().fetchUserById(id, object1 -> {
                                incrementFetchFriendsCount();

                                if (object1 != null) {
                                    my_friends.add((User) object1);
                                }

                                if (getFetchFriendsCount() == user_friends.size()) {
                                    if (invite_match) {
                                        pickFriendsAdapter.refreshList(my_friends, result_match);
                                    } else if (invite_team) {
                                        pickFriendsAdapter.refreshList(my_friends, result_team,
                                                activity_request_code);
                                    }

                                    if (pickFriendsAdapter.getItemCount() > 0) {
                                        no_friends.setVisibility(View.GONE);
                                    }

                                    progressDialog.dismiss();
                                }
                            });
                        }
                    } else setNoFriendsState();
                } else setNoFriendsState();
            });
        } else setNoLoggedUserState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void setMoreThanZeroFriendsState() {
        spunta_done.setVisibility(View.VISIBLE);

        no_friends.setVisibility(View.GONE);

        progressDialog.dismiss();
    }

    private void setNoFriendsState() {
        spunta_done.setVisibility(View.GONE);

        no_friends.setText("Nessun amico");
        no_friends.setVisibility(View.VISIBLE);

        progressDialog.dismiss();
    }

    private void setNoLoggedUserState() {
        setNoFriendsState();

        no_friends.setText("Nessun utente loggato");
        no_friends.setVisibility(View.VISIBLE);
    }

    private int fetch_friends_count = 0;
    private void resetFetchFriendsCount() { fetch_friends_count = 0; }
    private void incrementFetchFriendsCount() { fetch_friends_count++; }
    private int getFetchFriendsCount() { return fetch_friends_count; }
}
