package com.example.david.ermes.View.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.david.ermes.View.activities.AccountActivity;
import com.example.david.ermes.View.activities.EventActivity;
import com.example.david.ermes.View.activities.MatchUsersActivity;
import com.example.david.ermes.View.activities.PickFriendsActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.david.ermes.View.activities.EventActivity.*;

/**
 * Created by David on 16/07/2017.
 */

public class EventFragment extends Fragment {

    private final String CREATOR = "CREATOR",
        NOT_PARTECIPANT = "NOT_PARTECIPANT",
        PUBLIC_PARTECIPANT = "PUBLIC_PARTECIPANT",
        PRIVATE_PARTECIPANT = "PRIVATE_PARTECIPANT",
        PUBLIC_GUEST = "PUBLIC_GUEST",
        PRIVATE_GUEST = "PRIVATE_GUEST",
        UNAVAILABLE = "UNAVAILABLE";
    private String userCase;

    private TextView sportname;
    private TextView dateofevent;
    private TextView placeofevent;
    private TextView hourofevent;
    private TextView participant;
    private TextView pending;
    private TextView freeslots;
    private TextView usercreator;
    private CircularImageView imageCreator;
    private LinearLayout showInvited;
    private LinearLayout showPartecipants;

    private CardView profileCardView;

    private Match match;
    private User matchCreator;

    private Toolbar toolbar;
    private ImageButton invite;
    private Button join;
    private ImageButton delete_match;

    private Button missing_stuff_button;

    public EventFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        match = args.getParcelable("event");

        manageUserCase();
    }

    private void manageUserCase() {
        if (match.getIdOwner().equals(User.getCurrentUserId())) {
            userCase = CREATOR;
        } else if (!match.isPublic()) {
            if (match.getPartecipants().contains(User.getCurrentUserId())) {
                userCase = PRIVATE_PARTECIPANT;
            } else if (match.getPending().contains(User.getCurrentUserId())) {
                userCase = PRIVATE_GUEST;
            } else {
                userCase = UNAVAILABLE;
            }
        } else {
            if (match.getPartecipants().contains(User.getCurrentUserId())) {
                userCase = PUBLIC_PARTECIPANT;
            } else if (match.getPending().contains(User.getCurrentUserId())) {
                userCase = PUBLIC_GUEST;
            } else if (User.getCurrentUserId() != null) {
                userCase = NOT_PARTECIPANT;
            } else {
                userCase = UNAVAILABLE;
            }
        }
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

        toolbar = view.findViewById(R.id.event_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sportname = view.findViewById(R.id.sport_name);
        dateofevent = view.findViewById(R.id.when_text_calendar);
        placeofevent = view.findViewById(R.id.where_text);
        hourofevent = view.findViewById(R.id.when_hour_text_hour);
        usercreator = view.findViewById(R.id.userNameText);
        imageCreator = view.findViewById(R.id.small_circular_user_image);

        participant = view.findViewById(R.id.partecipantNumber);
        pending = view.findViewById(R.id.invitedNumber);
        freeslots = view.findViewById(R.id.openSlotNumber);

        showInvited = view.findViewById(R.id.invited_users_list);
        showPartecipants = view.findViewById(R.id.partecipant_users_list);

        missing_stuff_button = view.findViewById(R.id.missing_stuff_button);
        join = view.findViewById(R.id.buttonPartecipa);
        invite = view.findViewById(R.id.buttonInvita);
        delete_match = view.findViewById(R.id.elimina_evento);

        profileCardView = view.findViewById(R.id.profileCard);

        // scarico lo user name in base all'id che mi ha dato il match
        UserRepository.getInstance().fetchUserById(match.getIdOwner(), object -> {
            if (object != null) {
                matchCreator = (User) object;
                usercreator.setText(matchCreator.getName());
                Picasso.with(getContext()).load(matchCreator.getPhotoURL()).into(imageCreator);
            } else {

            }
        });

        profileCardView.setOnClickListener(view1 -> {
            if (matchCreator != null) {
                startAccountActivity(matchCreator);
            } else {
                Snackbar.make(view1, "Attendi...", Snackbar.LENGTH_SHORT).show();
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
        manageItemsByUserCase();

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

        invite.setOnClickListener(view1 -> {
            Intent invite_friends = new Intent(getContext(), PickFriendsActivity.class);
            invite_friends.putExtra("match",match);
            getActivity().startActivityForResult(invite_friends, INVITE_FRIEND_CODE);
        });

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


        join.setOnClickListener( view1 -> {
            if (userCase.equals(PRIVATE_PARTECIPANT) ||
                    userCase.equals(PUBLIC_PARTECIPANT)) {
                new MaterialDialog.Builder(this.getContext())
                        .title("Sei sicuro di voler abbandonare la partita?")
                        .negativeText("No")
                        .negativeColor(getResources().getColor(R.color.red))
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .positiveText("Si")
                        .onPositive((dialog, which) -> {
                            match.removePartecipant(User.getCurrentUserId());
                            match.save(object -> {
                                updateUI();

                                Snackbar.make(view, "Hai abbandonato questa partita.",
                                        Snackbar.LENGTH_SHORT).show();
                            });
                        })
                        .show();
            } else {
                match.addPartecipant(User.getCurrentUserId());
                match.save(object -> {
                    updateUI();

                    Snackbar.make(view, "Buona partita!", Snackbar.LENGTH_SHORT).show();
                });
            }
        });

        // visualizzazione lista di partecipanti e invitati
        showInvited.setOnClickListener(view1 -> {
            if (match.getPending().size() > 0) {
                startMatchUsersActivity("Invitati", match.getPending());
            } else {
                Snackbar.make(view, "Non ci sono invitati.", Snackbar.LENGTH_SHORT).show();
            }
        });

        showPartecipants.setOnClickListener(view1 -> {
            if (match.getPartecipants().size() > 0) {
                startMatchUsersActivity("Partecipanti", match.getPartecipants());
            } else {
                Snackbar.make(view, "Non ci sono partecipanti.", Snackbar.LENGTH_SHORT).show();
            }
        });

        // lo so che pare un macello sta stringa, giuro che correggerò le API
        dateofevent.setText(c.get(Calendar.DAY_OF_MONTH) + " " + TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)));
        hourofevent.setText(TimeUtils.getFormattedHourMinute(c));
        participant.setText(String.valueOf(match.getPartecipants().size()));
        pending.setText(String.valueOf(match.getPending().size()));
        freeslots.setText(String.valueOf(match.getMaxPlayers() - match.getPartecipants().size()));
    }

    private void startMatchUsersActivity(String title, List<String> list) {
        Intent intent = new Intent(getContext(), MatchUsersActivity.class);

        Bundle extras = new Bundle();
        extras.putStringArrayList("users", (ArrayList<String>) list);
        extras.putString("title", title);

        intent.putExtras(extras);
        startActivity(intent);
    }

    private void updateLabels() {
        participant.setText(String.valueOf(match.getPartecipants().size()));
        freeslots.setText(String.valueOf(match.getMaxPlayers() - match.getPending().size()
                - match.getPartecipants().size()));
        pending.setText(String.valueOf(match.getPending().size()));
    }

    private void manageItemsByUserCase() {
        switch(userCase) {
            case CREATOR:
                join.setVisibility(View.GONE);
                invite.setVisibility(View.VISIBLE);
                break;
            case PRIVATE_PARTECIPANT:
                join.setVisibility(View.VISIBLE);
                join.setText(R.string.rimuovi);
                invite.setVisibility(View.GONE);
                break;
            case PRIVATE_GUEST:
                join.setVisibility(View.VISIBLE);
                join.setText(R.string.partecipa);
                invite.setVisibility(View.GONE);
                break;
            case PUBLIC_PARTECIPANT:
                join.setVisibility(View.VISIBLE);
                join.setText(R.string.rimuovi);
                invite.setVisibility(View.VISIBLE);
                break;
            case PUBLIC_GUEST:
                join.setVisibility(View.VISIBLE);
                join.setText(R.string.partecipa);
                invite.setVisibility(View.VISIBLE);
                break;
            case NOT_PARTECIPANT:
                join.setVisibility(View.VISIBLE);
                join.setText(R.string.partecipa);
                invite.setVisibility(View.VISIBLE);
                break;
            case UNAVAILABLE:
                join.setVisibility(View.GONE);
                invite.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        if (!userCase.equals(CREATOR)) {
            delete_match.setVisibility(View.GONE);
        } else {
            delete_match.setVisibility(View.VISIBLE);
        }
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

    private void updateUI() {
        manageUserCase();
        updateLabels();
        manageItemsByUserCase();
    }

    public void updateMatch(Match match) {
        this.match = match;
        updateUI();
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

    private void startAccountActivity(User user) {
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = new Intent(getContext(), AccountActivity.class);

        Bundle extras = new Bundle();
        extras.putParcelable("user", user);

        intent.putExtras(extras);
        startActivity(intent);

        progressBar.setVisibility(View.GONE);
    }

}
