package com.example.david.ermes.View.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.ermes.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by EMarcantoni on 17-Jan-18.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {


    private MaterialEditText username_input_text;
    private MaterialEditText surname_input_text;
    private MaterialEditText email_input_text;
    private MaterialEditText password_input_text;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username_input_text = view.findViewById(R.id.username_profile_text);
        username_input_text.setFocusFraction(1f);
        surname_input_text = view.findViewById(R.id.surname_profile_text);
        surname_input_text.setFocusFraction(1f);
        email_input_text = view.findViewById(R.id.profile_mail_text);
        email_input_text.setFocusFraction(1f);
        password_input_text = view.findViewById(R.id.profile_pwd_text);
        password_input_text.setFocusFraction(1f);
    }

}
