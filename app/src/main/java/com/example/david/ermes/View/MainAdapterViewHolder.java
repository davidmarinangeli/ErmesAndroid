package com.example.david.ermes.View;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by David on 09/06/2017.
 */

public class MainAdapterViewHolder {


    public static void viewHolderConstructor(View itemView, TextView date_of_event, ImageView sport_icon, TextView place) {
    }

    public static void bindElements(List<Match> matchList,
                                    int position,
                                    final View itemView,
                                    TextView date_of_event,
                                    TextView hour_of_event,
                                    ImageView sport_icon,
                                    final TextView place) {

        Date date = matchList.get(position).getDate();
        //int sport_id = itemView

        Context cx = itemView.getContext();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());

        c.setTime(TimeUtils.fromMillisToDate(c.getTimeInMillis()));
        date_of_event.setText(c.get(Calendar.DAY_OF_MONTH) + " " + TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)));

        hour_of_event.setText(TimeUtils.getFormattedHourMinute(c));

        LocationRepository.getInstance().fetchLocationById(matchList.get(position).getIdLocation(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    Location loc = (Location) object;

                    place.setText(loc.getName());
                }
            }
        });
        //Picasso.with(cx).load(sport_id).memoryPolicy(MemoryPolicy.NO_CACHE).into(sport_icon);

    }
}
