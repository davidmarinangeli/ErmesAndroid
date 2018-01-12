package com.example.david.ermes.View.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;

import java.util.Calendar;

/**
 * Created by David on 16/07/2017.
 */

public class EventFragment extends Fragment {

    private TextView sportname;
    private TextView dateofevent;
    private TextView placeofevent;
    private TextView hourofevent;
    private TextView participant;
    private TextView pending;
    private TextView freeslots;
    private TextView usercreator;
    private TextView tools;
    private Match match;

    public EventFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        match = args.getParcelable("event");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sportname = view.findViewById(R.id.sport_name);
        dateofevent = view.findViewById(R.id.when_text_calendar);
        placeofevent = view.findViewById(R.id.where_text);
        hourofevent = view.findViewById(R.id.when_hour_text_hour);
        usercreator = view.findViewById(R.id.userNameText);
        participant = view.findViewById(R.id.partecipantNumber);
        pending = view.findViewById(R.id.invitedNumber);

        UserRepository.getInstance().fetchUserById(match.getIdOwner(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    User user = (User) object;
                    usercreator.setText(user.getName());
                } else {

                }
            }
        });

        SportRepository.getInstance().fetchSportById(match.getIdSport(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null){
                    Sport match_sport = (Sport) object;
                    sportname.setText(match_sport.getName());
                }
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(match.getDate());

        LocationRepository.getInstance().fetchLocationById(match.getIdLocation(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    Location match_location = (Location) object;
                    placeofevent.setText(match_location.getName());
                }
                }
            });



        // lo so che pare un macello sta stringa, giuro che correggerò le API
        dateofevent.setText(c.get(Calendar.DAY_OF_MONTH) +" "+ TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)) );
        hourofevent.setText(TimeUtils.getFormattedHourMinute(c));
        participant.setText(match.getPartecipants().size());
        pending.setText(match.getPending().size());
        freeslots.setText(match.getMaxPlayers()-match.getPartecipants().size());
    }


}
