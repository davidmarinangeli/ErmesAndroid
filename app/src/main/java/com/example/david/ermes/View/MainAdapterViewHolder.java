package com.example.david.ermes.View;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.utils.FetchMatchUtils;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by David on 09/06/2017.
 */

public class MainAdapterViewHolder {

    public static void bindElements(List<Match> matchList,
                                    int position,
                                    final View itemView,
                                    TextView date_of_event,
                                    TextView hour_of_event,
                                    final ImageView sport_icon,
                                    final TextView place) {

        Date date = matchList.get(position).getDate();
        //int sport_id = itemView

        final Context cx = itemView.getContext();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());

        c.setTime(TimeUtils.fromMillisToDate(c.getTimeInMillis()));
        date_of_event.setText(c.get(Calendar.DAY_OF_MONTH) + " " + TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)));

        hour_of_event.setText(TimeUtils.getFormattedHourMinute(c));

        LocationRepository.getInstance().fetchLocationById(matchList.get(position).getIdLocation(), object -> {
            if (object != null) {
                Location loc = (Location) object;

                place.setText(loc.getName());
            }
        });

        final String sport_id = matchList.get(position).getIdSport();
        SportRepository.getInstance().fetchSportById(sport_id, object -> {
            Sport found = (Sport) object;
            if (found != null) {
                String sport_name = found.getName();

                Picasso.with(cx)
                        .load(FetchMatchUtils.setImageToMatch(cx, sport_name))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(sport_icon);
            }
        });


    }
}
