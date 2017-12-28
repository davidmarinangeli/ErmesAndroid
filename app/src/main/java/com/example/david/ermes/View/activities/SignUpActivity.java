package com.example.david.ermes.View.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.ermes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // TODO : creare un sign-in/up presenter in cui creare la logica dell'activity
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    private Button registrati_button;

    private EditText mail_editText;
    private EditText password_editText;
    private EditText city_editText;
    private EditText username_editText;

    private TextInputLayout mail_til;
    private TextInputLayout password_til;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // prendo l'istanza del FBAuth
        mAuth = FirebaseAuth.getInstance();

        // setto i parametri dell'editext login e password
        mail_editText = findViewById(R.id.signup_mail);
        password_editText = findViewById(R.id.signup_pwd);
        city_editText = findViewById(R.id.city_signup);
        username_editText = findViewById(R.id.username_signup);

        registrati_button = findViewById(R.id.registrati_button);
        registrati_button.setOnClickListener(this);

        mail_til = findViewById(R.id.textInputLayoutMail);
        password_til = findViewById(R.id.textInputLayoutPwd);
    }

    private void signUpNormal(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
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
                    password_til.setError("Inserisci una password");

                if (TextUtils.isEmpty(mail_editText.getText().toString()))
                    mail_til.setError("Inserisci una mail");
            }
        }
    }
}
