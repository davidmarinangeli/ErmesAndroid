package com.example.david.ermes.View.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.MissingStuffElement;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;

import java.time.Duration;
import java.util.ArrayList;
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

    private Button invite;
    private Button join;
    private Button concludi_wtf;

    private Button missing_stuff_button;

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
        freeslots = view.findViewById(R.id.openSlotNumber);


        missing_stuff_button = view.findViewById(R.id.missing_stuff_button);
        join = view.findViewById(R.id.buttonPartecipa);
        invite = view.findViewById(R.id.buttonInvita);
        concludi_wtf = view.findViewById(R.id.buttonConcludiEvento);

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
                if (object != null) {
                    Sport match_sport = (Sport) object;
                    sportname.setText(match_sport.getName());
                }
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(match.getDate());

/*
        LocationRepository.getInstance().fetchLocationById(match.getIdLocation(), object -> {
            if (object != null) {
                Location match_location = (Location) object;
                placeofevent.setText(match_location.getName());
            }
        });

*/
        UserRepository.getInstance().getUser(object -> {
            if (object != null) {
                User currentUser = (User) object;
                if (currentUser.getUID().equals(match.getIdOwner())) {
                    join.setVisibility(View.GONE);
                }
            }
        });

        if (match.getMissingStuff() != null) {
            missing_stuff_button.setOnClickListener(view1 -> showMultiChoice(new ArrayList<>()));
        } else {
            missing_stuff_button.setEnabled(false);
        }

        // lo so che pare un macello sta stringa, giuro che correggerò le API
        dateofevent.setText(c.get(Calendar.DAY_OF_MONTH) + " " + TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)));
        hourofevent.setText(TimeUtils.getFormattedHourMinute(c));
        //participant.setText(match.getPartecipants().size());
        //pending.setText(match.getPending().size());
        //freeslots.setText(match.getMaxPlayers() - match.getPartecipants().size());
    }

    public void showMultiChoice(final ArrayList<String> got_missing_item_list) {
        new MaterialDialog.Builder(this.getContext()).title("Se hai qualche materiale mancante e puoi portarlo, spunta le caselle!")
                .items(match.getMissingStuff())
                .itemsCallbackMultiChoice(new Integer[]{}, (dialog, which, text) -> {
                    boolean allowSelection = which.length >= 0;
                    for (Integer i : which) {
                        got_missing_item_list.add((String) text[i]);
                    }
                    return allowSelection;
                })
                .positiveColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary))
                .onNeutral((dialog, which) -> dialog.dismiss())
                .alwaysCallMultiChoiceCallback()
                .positiveText("Ok!")
                .autoDismiss(true)
                .neutralText("Annulla")
                .onPositive(((dialog, which) -> {
                    for (MissingStuffElement missing_item : match.getMissingStuff()) {
                        for (String got_item : got_missing_item_list)
                            if (missing_item.getName().equals(got_item)) {
                                match.getMissingStuff().remove(missing_item);
                            }
                    }
                }))
                .show();
    }

}
