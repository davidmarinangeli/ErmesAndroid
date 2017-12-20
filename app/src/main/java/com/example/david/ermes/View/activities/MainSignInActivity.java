package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainSignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton googleLogInButton;
    private Button signUpButton;
    private Button loginInButton;
    private EditText login_editext;
    private EditText password_editext;
    private TextView userlogintext;

    private Button logoutbutton;

    private GoogleSignInAccount account;
    private FirebaseAuth mAuth;

    private String clientID = "663865499839-k726rla8ijgek2u3je8vp8il59dm2bd2.apps.googleusercontent.com";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setto i parametri dell'editext login e password
        login_editext = findViewById(R.id.mail);
        password_editext = findViewById(R.id.pwd);

        logoutbutton = findViewById(R.id.logoutbutton);
        logoutbutton.setOnClickListener(this);

        // prendo l'istanza del FBAuth
        mAuth = FirebaseAuth.getInstance();


        // setto i parametri dei bottoni di Login
        googleLogInButton = findViewById(R.id.google_login_button);
        loginInButton = findViewById(R.id.loginbutton);
        signUpButton = findViewById(R.id.signupbutton);
        loginInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        userlogintext = findViewById(R.id.user_email_login);
        googleLogInButton.setOnClickListener(this);

        TextView textView = (TextView) googleLogInButton.getChildAt(0);
        textView.setPadding(0, 0, 14, 0);
        textView.setText("Accedi con Google");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                signInGoogle();
                break;
            case R.id.loginbutton:
                String email = login_editext.getText().toString();
                String password = password_editext.getText().toString();
                if ((!email.isEmpty()) && (!password.isEmpty())) {
                    signInNormal(email,password);
                } else {
                    //TODO: settare il comportamento in caso di bad filling
                }
                    break;
            case R.id.signupbutton:
                Intent signupactivity = new Intent(this,SignUpActivity.class);
                startActivity(signupactivity);

            case R.id.logoutbutton:
                signOut();
            default:
                break;
        }
    }

    private void signInNormal(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.d("errore", result.getStatus().getStatusMessage() + "");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            final FirebaseUser u = mAuth.getCurrentUser();
                            updateUI(u);

                            UserRepository.getInstance().fetchUserById(u.getUid(), new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    if (object != null) {
                                        final User fetch_user = (User) object;

                                        fetch_user.fetchFavoriteSport(new FirebaseCallback() {
                                            @Override
                                            public void callback(Object object) {
                                                fetch_user.setName(u.getDisplayName());

                                                if (object != null) {
                                                    fetch_user.setFavSport((Sport) object);
                                                    Log.d("USER FAVSPORT", fetch_user.getFavSport().toString());
                                                }

                                                fetch_user.save();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    public void updateUI(FirebaseUser user) {
        if (user != null)
            userlogintext.setText(user.getEmail());
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.d(TAG, "Signed out");
                userlogintext.setText("");
            }
        });
    }

}
