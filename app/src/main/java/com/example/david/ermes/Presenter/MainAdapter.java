package com.example.david.ermes.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.EventActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private List<Match> matchList = new ArrayList<>();
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

    public void initList(){

        this.matchList = Match.toyListofMatches(context);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }



    public class MainViewHolder extends RecyclerView.ViewHolder{

        TextView date_of_event;
        TextView hour_of_event;
        TextView place_of_event;
        ImageView sport_icon;

        public MainViewHolder(final View itemView) {
            super(itemView);


            // qui setto il costruttore del viewholder

            // qui ci sarebbe da correggere
            date_of_event = (TextView) itemView.findViewById(R.id.date_of_event);
            hour_of_event = (TextView) itemView.findViewById(R.id.hour_of_event);
            sport_icon = (ImageView) itemView.findViewById(R.id.sport_icon);
            place_of_event = (TextView) itemView.findViewById(R.id.place_of_event);

            //settare l'onclicklistener qui :)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, EventActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event",matchList.get(getAdapterPosition()));
                    i.putExtras(bundle);
                    context.startActivity(i);

                }
            });
        }

        public void bind(int position) {
            MainAdapterViewHolder.bindElements(matchList,position,itemView,date_of_event,hour_of_event,sport_icon,place_of_event);

        }
    }
}
