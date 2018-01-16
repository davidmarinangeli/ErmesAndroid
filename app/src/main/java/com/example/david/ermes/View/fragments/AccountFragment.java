package com.example.david.ermes.View.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.FriendsActivity;
import com.example.david.ermes.View.activities.MainSignInActivity;
import com.example.david.ermes.View.activities.MyMatchesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    Button loginbutton;
    Button logoutbutton;

    private CardView myMatchesCard;
    private CardView friendsCard;

    private User currentUser;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserRepository.getInstance().getUser(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                currentUser = (User) object;
            }
        });

        loginbutton = view.findViewById(R.id.loginbutton);
        logoutbutton = view.findViewById(R.id.logoutbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainSignInActivity.class);
                startActivity(i);

            }
        });

        myMatchesCard = view.findViewById(R.id.myMatchesCard);
        myMatchesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    Bundle extras = new Bundle();
                    extras.putParcelable("user", currentUser);

                    Intent myMatchesActivity = new Intent(view.getContext(), MyMatchesActivity.class);
                    myMatchesActivity.putExtras(extras);
                    startActivity(myMatchesActivity);
                } else if (User.getCurrentUserId() != null) {
                    Toast.makeText(view.getContext(), "Attendi...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Nessun utente loggato", Toast.LENGTH_SHORT).show();
                }
            }
        });

        friendsCard = view.findViewById(R.id.cardViewFriends);
        friendsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    Bundle extras = new Bundle();
                    extras.putParcelable("user", currentUser);

                    Intent friendsActivity = new Intent(view.getContext(), FriendsActivity.class);
                    friendsActivity.putExtras(extras);
                    startActivity(friendsActivity);
                } else if (User.getCurrentUserId() != null) {
                    Toast.makeText(view.getContext(), "Attendi...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Nessun utente loggato", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
