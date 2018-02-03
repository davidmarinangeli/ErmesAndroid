package com.example.david.ermes.View;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.TeamActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/17/2018.
 */

public class PickFriendsAdapter extends RecyclerView.Adapter<PickFriendsAdapter.PickFriendsViewHolder> {

    private List<User> friendship_list = new ArrayList<>();
    private List<User> invited_friends = new ArrayList<>();
    private Match result_match;
    private Team result_team;
    private Context context;

    private int team_activity_code = 0;

    public PickFriendsAdapter(Context context) {
        this.context = context;
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

        public PickFriendsViewHolder(View itemView) {
            super(itemView);

            friend_image = itemView.findViewById(R.id.friend_image_invitation);
            friend_name = itemView.findViewById(R.id.invite_friend_name);
            invite_checkbox = itemView.findViewById(R.id.checkBox_friend);

            invite_checkbox.setOnClickListener(view -> {

                if (result_match != null) {
                    if (!invite_checkbox.isChecked()) {
                        invited_friends.remove(friendship_list.get(getAdapterPosition()));
                    } else {
                        invited_friends.add(friendship_list.get(getAdapterPosition()));
                    }
                } else if (result_team != null) {
                    String user_id = friendship_list.get(getAdapterPosition()).getUID();

                    if (invite_checkbox.isChecked()) {
                        result_team.addUser(user_id);
                    } else {
                        result_team.removeUser(user_id);
                    }
                }
            });

        }

        public void bind(int pos) {
            friend_name.setText(friendship_list.get(pos).getName());
            if (!friendship_list.get(pos).getPhotoURL().isEmpty()) {
                Picasso.with(context).load(friendship_list.get(pos).getPhotoURL()).memoryPolicy(MemoryPolicy.NO_CACHE).into(friend_image);
            }
            if (result_match != null && result_match.getPartecipants().contains(friendship_list.get(pos).getUID())) {
                // se è già tra gli invitati disattivo la checkbox
                invite_checkbox.setChecked(true);
                invite_checkbox.setEnabled(false);
                friend_name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
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
        }

    }

}

