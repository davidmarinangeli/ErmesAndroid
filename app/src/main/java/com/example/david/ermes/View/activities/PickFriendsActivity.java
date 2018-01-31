package com.example.david.ermes.View.activities;

<<<<<<< HEAD
=======
import android.app.Activity;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
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

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
<<<<<<< HEAD
=======
import com.example.david.ermes.Model.models.Team;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.FriendshipRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.PickFriendsAdapter;
<<<<<<< HEAD
=======
import com.example.david.ermes.View.ProgressDialog;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

import java.util.ArrayList;
import java.util.List;

public class PickFriendsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView friendsrecyclerview;
    private PickFriendsAdapter pickFriendsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView no_friends;
    private TextView max_players;
    private ImageButton spunta_done;
    private Match result_match;
<<<<<<< HEAD
=======
    private Team result_team;

    private ProgressDialog progressDialog;
    private boolean invite_match;
    private boolean invite_team;
    private int activity_request_code;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_friends);

<<<<<<< HEAD
=======
        progressDialog = new ProgressDialog(this);

>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
        max_players = findViewById(R.id.remaining_invitation);
        no_friends = findViewById(R.id.no_friends);
        toolbar = findViewById(R.id.create_event_toolbar);
        spunta_done = findViewById(R.id.spunta_done);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Invita amici");

        Intent intent = getIntent();
<<<<<<< HEAD
        result_match = intent.getParcelableExtra("match");
=======
        activity_request_code = intent.getIntExtra(TeamActivity.ACTIVITY_REQUEST_CODE_KEY, 0);
        result_match = intent.getParcelableExtra("match");
        result_team = intent.getParcelableExtra(TeamActivity.ACTIVITY_TEAM_KEY);
        invite_match = result_match != null;
        invite_team = result_team != null;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

        linearLayoutManager = new LinearLayoutManager(this);
        pickFriendsAdapter = new PickFriendsAdapter(PickFriendsActivity.this,this);
        friendsrecyclerview = findViewById(R.id.pick_friends_recycler);
        friendsrecyclerview.setAdapter(pickFriendsAdapter);
        friendsrecyclerview.setNestedScrollingEnabled(false);
        friendsrecyclerview.setLayoutManager(linearLayoutManager);
        initList();

<<<<<<< HEAD
        max_players.setText(String.valueOf(peopleICanInvite(result_match)));

        spunta_done.setOnClickListener(view -> pickFriendsAdapter.saveFriendsList(object -> {
            if (object != null){
                List<User> invited_friends = (List<User>)object;
                for (User user : invited_friends) {
                    if (!result_match.getPartecipants().contains(user.getUID()) &&
                            !result_match.getPending().contains(user.getUID())) {
                        result_match.addPending(user.getUID());
                        Notification invitation = Notification.createMatchInvitation(user.getUID(), result_match.getId());
                        if (invitation != null) {
                            invitation.save();
                        } else {
                            Snackbar.make(view, "Impossibile inviare inviti per la partita", Snackbar.LENGTH_LONG);
                        }
                    }
                }
                result_match.save(object1 -> {
                    Intent save_intent = new Intent();
                    save_intent.putExtra("new_match", result_match);
                    setResult(RESULT_OK, save_intent);
                    finish();
                });
            }
        })) ;

    }

    public void editFreeSlot(FirebaseCallback firebaseCallback){
        firebaseCallback.callback(max_players);
    };

    public void initList() {
=======
        if (invite_match) {
            max_players.setText(String.valueOf(peopleICanInvite(result_match)));
        } else {
            max_players.setVisibility(View.GONE);
        }

        spunta_done.setOnClickListener(view -> {
            progressDialog.show();

            if (invite_match) {
                pickFriendsAdapter.saveFriendsList(object -> {
                    if (object != null) {
                        List<User> invited_friends = (List<User>) object;

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

    }

    public void editFreeSlot(FirebaseCallback firebaseCallback) {
        firebaseCallback.callback(max_players);
    }

    public void initList() {
        progressDialog.show();
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
        FriendshipRepository.getInstance().fetchFriendshipsByUserId(User.getCurrentUserId(), object -> {
            if (object != null) {
                List<Friendship> user_friends = (List<Friendship>) object;
                List<User> my_friends = new ArrayList<>();

                resetFetchFriendsCount();
                for (Friendship f : user_friends) {
                    String id = f.getId1().equals(User.getCurrentUserId()) ? f.getId2() : f.getId1();
                    UserRepository.getInstance().fetchUserById(id,object1 -> {
                        incrementFetchFriendsCount();

                        if (object1 != null){
                            my_friends.add((User) object1);
                        }

                        if (getFetchFriendsCount() == user_friends.size()) {
<<<<<<< HEAD
                            pickFriendsAdapter.refreshList(my_friends,result_match);
=======
                            if (invite_match) {
                                pickFriendsAdapter.refreshList(my_friends, result_match);
                            } else if (invite_team) {
                                pickFriendsAdapter.refreshList(my_friends, result_team,
                                        activity_request_code);
                            }
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

                            if (pickFriendsAdapter.getItemCount()>0){
                                no_friends.setVisibility(View.GONE);
                            }
<<<<<<< HEAD
=======

                            progressDialog.dismiss();
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
                        }
                    });
                }

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    public static int peopleICanInvite(Match result_match) {
        return (result_match.getMaxPlayers())-(result_match.getPartecipants().size())-(result_match.getPending().size());
    }

    private int fetch_friends_count = 0;
    private void resetFetchFriendsCount() { fetch_friends_count = 0; }
    private void incrementFetchFriendsCount() { fetch_friends_count++; }
    private int getFetchFriendsCount() { return fetch_friends_count; }
}
