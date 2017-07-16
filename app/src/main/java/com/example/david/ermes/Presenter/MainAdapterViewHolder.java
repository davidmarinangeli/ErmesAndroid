package com.example.david.ermes.Presenter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by David on 09/06/2017.
 */

public class MainAdapterViewHolder {


    public static void viewHolderConstructor(View itemView,TextView date_of_event, ImageView sport_icon){    }

    public static void bindElements(List<Match> matchList, int position, View itemView, TextView date_of_event, ImageView sport_icon){

        String date = matchList.get(position).getDate();
        int sport_id = matchList.get(position).getImageID();

        Context cx = itemView.getContext();

        date_of_event.setText(date);
        Picasso.with(cx).load(sport_id).memoryPolicy(MemoryPolicy.NO_CACHE).into(sport_icon);
    }
}
