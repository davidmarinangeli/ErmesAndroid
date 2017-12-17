package com.example.david.ermes.View.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.MainSignInActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    Button loginbutton;
    Button logoutbutton;


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

        loginbutton = view.findViewById(R.id.loginbutton);
        logoutbutton = view.findViewById(R.id.logoutbutton);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainSignInActivity.class);
                startActivity(i);

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

}
