package com.example.david.ermes.View.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.example.david.ermes.View.activities.MatchUsersActivity;
import com.example.david.ermes.View.activities.PickFriendsActivity;
import android.support.design.widget.FloatingActionButton;

import com.example.david.ermes.View.activities.PickPlaceActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.MemoryPolicy;
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
    private TextView missing_stuff_paragraph;


    private CircularImageView imageCreator;
    private LinearLayout showInvited;
    private LinearLayout showPartecipants;
    private ImageView place_cover;

    private CardView profileCardView;

    private Match match;
    private User matchCreator;

    private Toolbar toolbar;
    private ImageButton invite;
    private FloatingActionButton join;
    private ImageButton delete_match;

    private ImageButton missing_stuff_button;
    private User currentUser;

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
        missing_stuff_paragraph = view.findViewById(R.id.missing_paragraph);

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
        place_cover = view.findViewById(R.id.imageViewFavSport);

        setMissingStuffParagraph();

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
                Uri uri = Uri.parse("https://maps.googleapis.com/maps/api/staticmap?center="
                                + match_location.getLatitude()+ "," + match_location.getLongitude()+
                        "&zoom=18&size=600x400&maptype=hybrid&key=AIzaSyCCBlMJByIIPjLX3U045lwn93G-os92Zfw");

                Picasso.with(this.getContext()).load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(place_cover);

            }
        });


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
            invite_friends.putExtra("match", match);
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


        join.setOnClickListener(view1 -> {
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

        // ** GESTIONE VISIBILITA' OGGETTI IN BASE ALL'ACCOUNT LOGGATO **
        manageItemsByUserCase();
    }

    private void setMissingStuffParagraph() {
        boolean at_least_an_unchecked = false;
        StringBuilder missing_stuff_text = new StringBuilder();
        for (int index = 0; index < match.getMissingStuff().size(); index++) {
            if (!match.getMissingStuff().get(index).isChecked()) {
                missing_stuff_text.append("- ");
                missing_stuff_text.append(match.getMissingStuff().get(index).getName());
                if (index < match.getMissingStuff().size() - 1) {
                    missing_stuff_text.append("\n");
                }
                at_least_an_unchecked = true;
            }
        }

        if (at_least_an_unchecked) {
            missing_stuff_paragraph.setText(missing_stuff_text);
        } else {
            missing_stuff_paragraph.setText("Nessun materiale mancante");
        }
    }

    private void startMatchUsersActivity(String title, List<String> list) {
        Intent intent = new Intent(getContext(), MatchUsersActivity.class);

        Bundle extras = new Bundle();
        extras.putStringArrayList("users", (ArrayList<String>) list);
        extras.putString("title", title);

        if (currentUser != null) {

            extras.putParcelable("user", currentUser);
            intent.putExtras(extras);
            startActivity(intent);
        } else {
            UserRepository.getInstance().getUser(object -> {
                currentUser = (User) object;

                if (currentUser != null) {
                    extras.putParcelable("user", currentUser);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Snackbar.make(getView(), "Errore nello scaricamento dei dati",
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateLabels() {
        participant.setText(String.valueOf(match.getPartecipants().size()));
        freeslots.setText(String.valueOf(match.getMaxPlayers() - match.getPending().size()
                - match.getPartecipants().size()));
        pending.setText(String.valueOf(match.getPending().size()));
    }

    private void manageItemsByUserCase() {

        switch (userCase) {
            case CREATOR:
                invite.setVisibility(View.VISIBLE);
                missing_stuff_button.setVisibility(View.VISIBLE);
                break;
            case PRIVATE_PARTECIPANT:
                join.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_busy_white_24dp));
                invite.setVisibility(View.GONE);
                missing_stuff_button.setVisibility(View.VISIBLE);
                break;
            case PRIVATE_GUEST:
                join.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_available_white_24dp));
                invite.setVisibility(View.GONE);
                missing_stuff_button.setVisibility(View.GONE);
                break;
            case PUBLIC_PARTECIPANT:
                join.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_busy_white_24dp));
                invite.setVisibility(View.VISIBLE);
                missing_stuff_button.setVisibility(View.VISIBLE);
                break;
            case PUBLIC_GUEST:
                join.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_available_white_24dp));
                invite.setVisibility(View.VISIBLE);
                missing_stuff_button.setVisibility(View.GONE);
                break;
            case NOT_PARTECIPANT:
                join.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_available_white_24dp));
                invite.setVisibility(View.VISIBLE);
                missing_stuff_button.setVisibility(View.GONE);
                break;
            case UNAVAILABLE:
                invite.setVisibility(View.GONE);
                missing_stuff_button.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        if (userCase.equals(CREATOR)) {
            join.setVisibility(View.GONE);
            delete_match.setVisibility(View.VISIBLE);
        } else {
            join.setVisibility(View.VISIBLE);
            delete_match.setVisibility(View.GONE);
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
