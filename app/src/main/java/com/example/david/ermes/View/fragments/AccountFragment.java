package com.example.david.ermes.View.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.CustomizeProfileActivity;
import com.example.david.ermes.View.activities.FriendsActivity;
import com.example.david.ermes.View.activities.MainSignInActivity;
import com.example.david.ermes.View.activities.MyMatchesActivity;
import com.github.clans.fab.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Toolbar toolbar;

    private TextView name;
    private TextView age;
    private TextView sport;
    private TextView welcome_text;

    private CircularImageView image_account;

    private CardView myMatchesCard;
    private CardView friendsCard;
    private CardView customizeProfileCard;
    private ImageView appIcon;
    private TextView welcomeDescription;

    private Button welcome_button;

    private ScrollView main_scrollview;

    private User currentUser;

    private ImageView cover;
    private FloatingActionButton default_event_fab;
    private FloatingActionButton add_place_fab;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        currentUser = args != null ?
                args.getParcelable("user")
                : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_account, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!DatabaseManager.get().isLogged()) {
            main_scrollview.setVisibility(View.GONE);
            welcome_button.setVisibility(View.VISIBLE);
            welcome_text.setVisibility(View.VISIBLE);
            welcomeDescription.setVisibility(View.VISIBLE);
            appIcon.setVisibility(View.VISIBLE);
            welcome_button.setOnClickListener(view12 -> {
                Intent i = new Intent(view12.getContext(), MainSignInActivity.class);
                startActivity(i);


            });
        } else {

            main_scrollview.setVisibility(View.VISIBLE);
            welcome_button.setVisibility(View.GONE);
            welcome_text.setVisibility(View.GONE);
            appIcon.setVisibility(View.GONE);
            welcomeDescription.setVisibility(View.GONE);

            onViewCreated(getView(),null);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.name_account);
        age = view.findViewById(R.id.age_account);
        sport = view.findViewById(R.id.sport_account);
        image_account = view.findViewById(R.id.image_account);
        cover = view.findViewById(R.id.fav_sport_cover);

        myMatchesCard = view.findViewById(R.id.myMatchesCard);
        friendsCard = view.findViewById(R.id.cardViewFriends);

        main_scrollview = view.findViewById(R.id.account_nested_scrollview);

        welcome_button = view.findViewById(R.id.open_login);
        welcome_text = view.findViewById(R.id.welcome_button);
        appIcon = view.findViewById(R.id.welcome_icon);
        welcomeDescription = view.findViewById(R.id.welcome_descrption);

        if (currentUser == null) {
            // se sto vedendo il mio account
            UserRepository.getInstance().getUser(object -> {
                currentUser = (User) object;

                initComponents();
            });
        } else {
            // se sto vedendo l'account di altri
            initComponents();
        }

        myMatchesCard.setOnClickListener(view1 -> {
            if (currentUser != null) {
                Bundle extras = new Bundle();
                extras.putParcelable("user", currentUser);

                Intent myMatchesActivity = new Intent(view1.getContext(), MyMatchesActivity.class);
                myMatchesActivity.putExtras(extras);
                startActivity(myMatchesActivity);
            } else if (DatabaseManager.get().isLogged()) {
                Toast.makeText(view1.getContext(), "Attendi...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view1.getContext(), "Nessun utente loggato", Toast.LENGTH_SHORT).show();
            }
        });

        if (currentUser != null && !currentUser.getUID().equals(User.getCurrentUserId())) {
            friendsCard.setVisibility(View.GONE);
        }
        friendsCard.setOnClickListener(view13 -> {
            if (currentUser != null) {
                Bundle extras = new Bundle();
                extras.putParcelable("user", currentUser);

                Intent friendsActivity = new Intent(view13.getContext(), FriendsActivity.class);
                friendsActivity.putExtras(extras);
                startActivity(friendsActivity);
            } else if (DatabaseManager.get().isLogged()) {
                Toast.makeText(view13.getContext(), "Attendi...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view13.getContext(), "Nessun utente loggato", Toast.LENGTH_SHORT).show();
            }
        });
        customizeProfileCard = view.findViewById(R.id.myProfileCard);
        if (currentUser != null && currentUser.getUID() != User.getCurrentUserId()) {
            friendsCard.setVisibility(View.GONE);
        }
        customizeProfileCard.setOnClickListener(view12 -> {
            if (currentUser != null) {
                Bundle extras = new Bundle();
                extras.putParcelable("user", currentUser);

                Intent profileAcitivty = new Intent(view12.getContext(), CustomizeProfileActivity.class);
                profileAcitivty.putExtras(extras);
                startActivity(profileAcitivty);


            } else if (User.getCurrentUserId() != null) {
                Toast.makeText(view12.getContext(), "Attendi...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view12.getContext(), "Nessun utente loggato", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initComponents() {
        if (currentUser != null) {
            name.setText(currentUser.getName());
            age.setText(String.valueOf(TimeUtils.getAgeFromBirth(currentUser.getBirthDate())));

            if (currentUser.getPhotoURL() != null && !currentUser.getPhotoURL().isEmpty()) {
                Picasso.with(getContext()).load(currentUser.getPhotoURL()).into(image_account);
            } else {
                Picasso.with(getContext()).load(R.drawable.user_placeholder).into(image_account);
            }

            cover.setImageResource(User.getSportCoverForFavouriteSport(Integer.valueOf(currentUser.getIdFavSport()),getContext()));
            SportRepository.getInstance().fetchSportById(currentUser.getIdFavSport(),
                    object -> {
                        Sport fetch_sport = (Sport) object;

                        if (fetch_sport != null) {
                            sport.setText(fetch_sport.getName());
                        }
                    });
        }
    }

}
