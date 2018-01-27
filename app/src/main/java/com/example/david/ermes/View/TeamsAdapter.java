package com.example.david.ermes.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Presenter.TeamUsersPresenter;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.TeamActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 10/01/2018.
 */

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamViewHolder> {

    private List<Team> teams = new ArrayList<>();
    private Context context;

    public TeamsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.default_team_item,
                parent,
                false
        );

        return new TeamsAdapter.TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public void refreshList(List<Team> teams) {
        this.teams = teams;

        notifyDataSetChanged();
    }


    public class TeamViewHolder extends RecyclerView.ViewHolder {

        View item;
        TextView name;
        TextView members;

        public TeamViewHolder(View itemView) {
            super(itemView);

            item = itemView;
            name = itemView.findViewById(R.id.team_list_item_name);
            members = itemView.findViewById(R.id.team_list_item_num_members);
        }

        public void bind(int position) {
            if (teams != null && teams.size() > 0) {
                Team t = teams.get(position);

                item.setOnClickListener(v -> startTeamActivity(t));
                populateMembersLabel(t);
                name.setText(t.getName());
            }
        }

        private void populateMembersLabel(Team team) {
            new TeamUsersPresenter().calculateMembersStringValue(team,
                    object -> members.setText((String) object));
        }

        private void startTeamActivity(Team team) {
            Bundle extras = new Bundle();
            extras.putParcelable(TeamActivity.ACTIVITY_TEAM_KEY, team);
            extras.putString(TeamActivity.ACTIVITY_TYPE_KEY, TeamActivity.TEAM_VIEW);

            Intent i = new Intent(context, TeamActivity.class);
            i.putExtras(extras);
            context.startActivity(i);
        }
    }
}
