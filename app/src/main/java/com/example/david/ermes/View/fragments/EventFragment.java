package com.example.david.ermes.View.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    private Match match;

    private Button invite;
    private Button join;
    private Button delete_match;

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
        delete_match = view.findViewById(R.id.elimina_evento);

        // scarico lo user name in base all'id che mi ha dato il match
        UserRepository.getInstance().fetchUserById(match.getIdOwner(), object -> {
            if (object != null) {
                User user = (User) object;
                usercreator.setText(user.getName());
            } else {

            }
        });


        // scarico lo sport...
        SportRepository.getInstance().fetchSportById(match.getIdSport(), object -> {
            if (object != null) {
                Sport match_sport = (Sport) object;
                sportname.setText(match_sport.getName());
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(match.getDate());


        // ...scarico la posizione
        LocationRepository.getInstance().fetchLocationById(match.getIdLocation(), object -> {
            if (object != null) {
                Location match_location = (Location) object;
                placeofevent.setText(match_location.getName());
            }
        });


        // ** GESTIONE VISBILITA' OGGETTI IN BASE ALL'ACCOUNT LOGGATO **
        if (User.getCurrentUserId() != null) {
            if (User.getCurrentUserId().equals(match.getIdOwner())) {
                join.setVisibility(View.GONE);
            } else {
                delete_match.setVisibility(View.GONE);
            }
        }


        if (!areAllMissingItemsChecked()) {
            ArrayList<String> missing_items_instring = new ArrayList<>();
            for (MissingStuffElement missing_item : match.getMissingStuff()) {
                if (!missing_item.isChecked()) {
                    missing_items_instring.add(missing_item.getName());
                }
            }
            missing_stuff_button.setOnClickListener(view1 -> showMultiChoice(new ArrayList<>(), missing_items_instring));
        } else {
            missing_stuff_button.setEnabled(false);
        }

        delete_match.setOnClickListener(view1 -> new MaterialDialog.Builder(this.getContext())
                .title("Sei sicuro di voler eliminare l'evento?")
                .negativeText("No")
                .negativeColor(getResources().getColor(R.color.red))
                .onNegative((dialog, which) -> dialog.dismiss())
                .positiveText("Si")
                .onPositive((dialog, which) -> {
                    MatchRepository.getInstance().deleteMatchById(match.getId(), null);
                    getActivity().finish();
                })
                .show());

        // lo so che pare un macello sta stringa, giuro che correggerò le API
        dateofevent.setText(c.get(Calendar.DAY_OF_MONTH) + " " + TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)));
        hourofevent.setText(TimeUtils.getFormattedHourMinute(c));
        participant.setText(String.valueOf(match.getPartecipants().size()));
        pending.setText(String.valueOf(match.getPending().size()));
        freeslots.setText(String.valueOf(match.getMaxPlayers() - match.getPartecipants().size()));
    }

    private boolean areAllMissingItemsChecked() {
        boolean ischecked = true;
        for (MissingStuffElement element : match.getMissingStuff()) {
            if (!element.isChecked()) {
                ischecked = false;
            }

        }
        return ischecked;
    }

    public void showMultiChoice(final ArrayList<String> got_missing_item_list, ArrayList<String> missing_items_instring) {
        new MaterialDialog.Builder(this.getContext()).title("Se hai qualche materiale mancante e puoi portarlo, spunta le caselle!")
                .items(missing_items_instring)
                .itemsCallbackMultiChoice(new Integer[]{}, (dialog, which, text) -> {
                    boolean allowSelection = which.length >= 0;
                    for (int i = 0; i < which.length; i++) {
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
                        if (!missing_item.isChecked()) {
                            for (String got_item : got_missing_item_list)
                                if (missing_item.getName().equals(got_item)) {
                                    missing_item.setChecked(true);
                                    match.save();
                                    onViewCreated(getView(), null);
                                }
                        }
                    }
                }))
                .show();
    }

}
