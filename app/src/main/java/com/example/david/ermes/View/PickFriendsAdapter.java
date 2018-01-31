package com.example.david.ermes.View;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
<<<<<<< HEAD
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.PickFriendsActivity;
=======
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.PickFriendsActivity;
import com.example.david.ermes.View.activities.TeamActivity;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/17/2018.
 */

public class PickFriendsAdapter extends RecyclerView.Adapter<PickFriendsAdapter.PickFriendsViewHolder> {

    private final PickFriendsActivity pickactivity;
    private List<User> friendship_list = new ArrayList<>();
    private List<User> invited_friends = new ArrayList<>();
    private Match result_match;
<<<<<<< HEAD
    private Context context;
    private int who_i_can_invite = 0;

=======
    private Team result_team;
    private Context context;
    private int who_i_can_invite = 0;

    private int team_activity_code = 0;

>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
    public PickFriendsAdapter(PickFriendsActivity from, Context context) {
        this.context = context;
        this.pickactivity = from;
    }

    @Override
    public PickFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_item,
                parent,
                false
        );
        return new PickFriendsAdapter.PickFriendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PickFriendsViewHolder holder, int position) {
        holder.bind(position);
    }


    public void refreshList(List<User> friends_list, Match match) {
        friendship_list = friends_list;
        result_match = match;
        who_i_can_invite = PickFriendsActivity.peopleICanInvite(result_match);
<<<<<<< HEAD
        notifyDataSetChanged();
    }

=======
        team_activity_code = 0;
        notifyDataSetChanged();
    }

    public void refreshList(List<User> friends_list, Team team, int code) {
        friendship_list = friends_list;
        result_team = team;
        team_activity_code = code;
        notifyDataSetChanged();
    }

    public Team getResultTeam() {
        return result_team;
    }

>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

    @Override
    public int getItemCount() {
        return friendship_list.size();
    }

    public void saveFriendsList(FirebaseCallback firebaseCallback) {
        firebaseCallback.callback(invited_friends);
    }

    public class PickFriendsViewHolder extends RecyclerView.ViewHolder {

        CheckBox invite_checkbox;
        TextView friend_name;
        ImageView friend_image;
        //final int[] final_invited_list = {who_i_can_invite};

        public PickFriendsViewHolder(View itemView) {
            super(itemView);

            friend_image = itemView.findViewById(R.id.friend_image_invitation);
            friend_name = itemView.findViewById(R.id.invite_friend_name);
            invite_checkbox = itemView.findViewById(R.id.checkBox_friend);

            invite_checkbox.setOnClickListener(view -> {

<<<<<<< HEAD
                if (!invite_checkbox.isChecked()) {
                    invited_friends.remove(friendship_list.get(getAdapterPosition()));
                    pickactivity.editFreeSlot(object -> {
                        TextView maxplayers = (TextView) object;
                        who_i_can_invite++;
                        maxplayers.setText(who_i_can_invite+"");
                    });

                } else {

                    if (invited_friends.size() <= who_i_can_invite) {
                        invited_friends.add(friendship_list.get(getAdapterPosition()));
                        pickactivity.editFreeSlot(object -> {
                            TextView maxplayers = (TextView) object;
                            who_i_can_invite--;
                            maxplayers.setText(who_i_can_invite+"");
                        });
                    } else {
                        Snackbar.make(itemView, "Puoi invitare al massimo " + result_match.getMaxPlayers() + " giocatori", Snackbar.LENGTH_LONG);
                        invite_checkbox.setChecked(false);
=======
                if (result_match != null) {
                    if (!invite_checkbox.isChecked()) {
                        invited_friends.remove(friendship_list.get(getAdapterPosition()));
                        pickactivity.editFreeSlot(object -> {
                            TextView maxplayers = (TextView) object;
                            who_i_can_invite++;
                            maxplayers.setText(who_i_can_invite + "");
                        });

                    } else {

                        if (invited_friends.size() <= who_i_can_invite) {
                            invited_friends.add(friendship_list.get(getAdapterPosition()));
                            pickactivity.editFreeSlot(object -> {
                                TextView maxplayers = (TextView) object;
                                who_i_can_invite--;
                                maxplayers.setText(who_i_can_invite + "");
                            });
                        } else {
                            Snackbar.make(itemView, "Puoi invitare al massimo " + result_match.getMaxPlayers() + " giocatori", Snackbar.LENGTH_LONG);
                            invite_checkbox.setChecked(false);
                        }
                    }
                } else if (result_team != null) {
                    String user_id = friendship_list.get(getAdapterPosition()).getUID();

                    if (invite_checkbox.isChecked()) {
                        result_team.addUser(user_id);
                    } else {
                        result_team.removeUser(user_id);
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
                    }
                }
            });

        }

        public void bind(int pos) {
            friend_name.setText(friendship_list.get(pos).getName());
            Picasso.with(context).load(friendship_list.get(pos).getPhotoURL()).memoryPolicy(MemoryPolicy.NO_CACHE).into(friend_image);
<<<<<<< HEAD
            if (result_match.getPartecipants().contains(friendship_list.get(pos).getUID())) {
=======

            if (result_match != null && result_match.getPartecipants().contains(friendship_list.get(pos).getUID())) {
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
                // se è già tra gli invitati disattivo la checkbox
                invite_checkbox.setChecked(true);
                invite_checkbox.setEnabled(false);
                friend_name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
<<<<<<< HEAD
            }

=======
            } else if (result_team != null && result_team.getUserIdList().contains(friendship_list.get(pos).getUID())) {
                switch (team_activity_code) {
                    case TeamActivity.SELECT_FRIENDS_CREATE_TEAM_REQUEST_CODE:
                        invite_checkbox.setChecked(true);
                        invite_checkbox.setEnabled(true);
                        break;
                    case TeamActivity.SELECT_FRIENDS_ADD_REQUEST_CODE:
                        invite_checkbox.setChecked(true);
                        invite_checkbox.setEnabled(false);
                        break;
                    default:
                        break;
                }
            }
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
        }

    }

}

