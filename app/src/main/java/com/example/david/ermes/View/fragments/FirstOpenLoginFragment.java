package com.example.david.ermes.View.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.MainSignInActivity;

public class FirstOpenLoginFragment extends Fragment {

    Button open_login;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_open_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        open_login = view.findViewById(R.id.open_login);

        open_login.setOnClickListener(view1 -> {
            Intent login_intent = new Intent(this.getContext(), MainSignInActivity.class);
            startActivity(login_intent);
        });
    }
}
