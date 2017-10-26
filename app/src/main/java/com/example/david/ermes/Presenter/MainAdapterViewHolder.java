package com.example.david.ermes.Presenter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;


/**
 * Created by David on 09/06/2017.
 */

public class MainAdapterViewHolder {


    public static void viewHolderConstructor(View itemView,TextView date_of_event, ImageView sport_icon, TextView place){    }

    public static void bindElements(List<Match> matchList, int position, View itemView, TextView date_of_event, TextView hour_of_event, ImageView sport_icon, TextView place){
/*
        long date = matchList.get(position).getDate();
        int sport_id = matchList.get(position).getImageID();
        String where = matchList.get(position).getLocation();

        Context cx = itemView.getContext();

        Calendar c = Calendar.getInstance();
        c.setTime(TimeUtils.fromMillisToDate(date));
        date_of_event.setText(c.get(Calendar.DAY_OF_MONTH) +" "+ TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)) );

        hour_of_event.setText(TimeUtils.getFormattedHourMinute(c));

        place.setText(where);
        Picasso.with(cx).load(sport_id).memoryPolicy(MemoryPolicy.NO_CACHE).into(sport_icon);
        */
    }
}
