package com.example.david.ermes.View.fragments;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by EMarcantoni on 17-Jan-18.
 */

public class ProfileFragment extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener  {



    private String clientID = "663865499839-k726rla8ijgek2u3je8vp8il59dm2bd2.apps.googleusercontent.com";
    private static final String TAG = "AUTH";

    private MaterialEditText sport_input_text;
    private MaterialEditText email_input_text;
    private MaterialEditText username_input_text;

    private Button signout_button;
    private User currentUser;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // prendo l'istanza del FBAuth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Bundle args = getArguments();
        currentUser = args != null ?
                args.getParcelable("user")
                : null;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signout_button = view.findViewById(R.id.logout_button);

        sport_input_text = view.findViewById(R.id.sport_profile_text);
        email_input_text = view.findViewById(R.id.profile_mail_text);
        username_input_text = view.findViewById(R.id.username_profile_text);

        email_input_text.setText(currentUser.getEmail());
        username_input_text.setText(currentUser.getName());
        email_input_text.setEnabled(false);
        username_input_text.setEnabled(false);

        SportRepository.getInstance().fetchSportById(currentUser.getIdFavSport(),object -> {
            if(object != null){
                Sport s = (Sport) object;
                sport_input_text.setText(s.getName());
                sport_input_text.setEnabled(false);
            }
        });

        sport_input_text.setFocusFraction(1f);
        email_input_text.setFocusFraction(1f);

        signout_button.setOnClickListener(view1 -> signOut());
    }


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
            mAuth.signOut();
            getActivity().finish();
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }
}
