package com.example.david.ermes.View.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // TODO : creare un sign-in/up presenter in cui creare la logica dell'activity
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    private Button registrati_button;

    private Spinner sportspinner;
    private SpinnerAdapter adapter;
    private String selected_sport;

    private MaterialEditText mail_editText;
    private MaterialEditText password_editText;
    private MaterialEditText city_editText;
    private MaterialEditText username_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // prendo l'istanza del FBAuth
        mAuth = FirebaseAuth.getInstance();


        // vado ad istanziare lo spinner
        sportspinner = findViewById(R.id.sport_spinner);

        // setto i parametri dell'editext login e password
        mail_editText = findViewById(R.id.signup_mail);
        password_editText = findViewById(R.id.signup_pwd);
        city_editText = findViewById(R.id.city_signup);
        username_editText = findViewById(R.id.username_signup);

        registrati_button = findViewById(R.id.registrati_button);
        registrati_button.setOnClickListener(this);

        sportspinner.setOnItemSelectedListener(new itemSelectedListener());

        final ArrayList<String> arraySpinner = new ArrayList<>();
        SportRepository.getInstance().fetchAll(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                for (Sport s : (ArrayList<Sport>) object) {
                    arraySpinner.add(s.getName());
                }
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, arraySpinner);
                sportspinner.setAdapter(adapter);


            }
        });


    }

    private void signUpNormal(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private class itemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected_sport = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registrati_button){
            if (!TextUtils.isEmpty(mail_editText.getText().toString()) &&
                    (!TextUtils.isEmpty(password_editText.getText().toString())) &&
                    (!TextUtils.isEmpty(username_editText.getText().toString())) &&
                    (!TextUtils.isEmpty(city_editText.getText().toString()))
                    )
            {
                signUpNormal(mail_editText.getText().toString(), password_editText.getText().toString());

            } else {
                if (TextUtils.isEmpty(password_editText.getText().toString()))
                    password_editText.setError("Inserisci una password");

                if (TextUtils.isEmpty(mail_editText.getText().toString()))
                    mail_editText.setError("Inserisci una mail");

                if (TextUtils.isEmpty(username_editText.getText().toString()))
                    username_editText.setError("Inserisci un username");

                if (TextUtils.isEmpty(city_editText.getText().toString()))
                    city_editText.setError("Inserisci una città");
            }
        }
    }
}
