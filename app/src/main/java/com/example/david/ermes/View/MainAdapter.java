package com.example.david.ermes.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.EventActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<Match> matchList = new ArrayList<>();
    private int other_sports_match_index = 0;
    private String favSportName;
    private Context context;


    public MainAdapter(Context c) {
        this.context = c;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //dico qual è l'item da inserire nella RecyclerView
        final View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_sport_event_item, parent, false);
        return new MainAdapter.MainViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return matchList.size();

    }

    public void refreshList(List<Match> matches){
        matchList = matches;
        other_sports_match_index = 0;

        notifyDataSetChanged();
    }

    public void refreshList(List<Match> matches, String idFavSport) {
        if (matches != null && !matches.isEmpty()) {
            other_sports_match_index = 0;
            matchList.clear();

            for (Match match : matches) {
                if (match.getIdSport().equals(idFavSport)) {
                    matchList.add(other_sports_match_index++, match);
                } else {
                    matchList.add(match);
                }
            }

            notifyDataSetChanged();
        }
    }

    public void setFavSportName(String sportName) {
        favSportName = sportName;

        if (matchList != null && !matchList.isEmpty()) {
            notifyDataSetChanged();
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        LinearLayout user_fav_sport_label;
        TextView user_fav_sport_label_text;

        TextView date_of_event;
        TextView hour_of_event;
        TextView place_of_event;
        ImageView sport_icon;

        public MainViewHolder(final View itemView) {
            super(itemView);

            // qui setto il costruttore del viewholder
            user_fav_sport_label = itemView.findViewById(R.id.user_fav_sport_label);
            user_fav_sport_label_text = itemView.findViewById(R.id.user_fav_sport_label_text);
            user_fav_sport_label_text.setVisibility(View.VISIBLE);

            date_of_event = itemView.findViewById(R.id.date_of_event);
            hour_of_event = itemView.findViewById(R.id.hour_of_event);
            sport_icon = itemView.findViewById(R.id.sport_icon_event);
            place_of_event = itemView.findViewById(R.id.place_of_event);

            //settare l'onclicklistener qui :)
            itemView.setOnClickListener(view -> {
                itemView.setActivated(false);
                
                Intent i = new Intent(context, EventActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("event", matchList.get(getAdapterPosition()));
                i.putExtras(bundle);
                context.startActivity(i);

                itemView.setActivated(true);
            });
        }

        public void bind(int position) {

            // se l'utente è loggato e ci sono match del suo sport preferito
            if (other_sports_match_index > 0) {
                if (position == 0) {
                    user_fav_sport_label_text.setText(favSportName);
                    user_fav_sport_label.setVisibility(View.VISIBLE);
                } else if (position == other_sports_match_index) {
                    user_fav_sport_label_text.setText("Altri sport");
                    user_fav_sport_label.setVisibility(View.VISIBLE);
                } else {
                    user_fav_sport_label.setVisibility(View.GONE);
                }
            } else {
                user_fav_sport_label.setVisibility(View.GONE);
            }

            MainAdapterViewHolder.bindElements(matchList, position, itemView, date_of_event, hour_of_event, sport_icon, place_of_event);
        }
    }
}
