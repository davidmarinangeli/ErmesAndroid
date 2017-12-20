package com.example.david.ermes.View.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        mail_editText = findViewById(R.id.mail);
        password_editText = findViewById(R.id.pwd);
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
            if ((mail_editText.getText() != null) &&
                    (password_editText.getText() != null) &&
                    (username_editText.getText() != null) &&
                    (city_editText.getText() != null))
            {
                signUpNormal(mail_editText.getText().toString(), password_editText.getText().toString());

            } else {
                if (mail_editText.getText() == null)
                    mail_til.setError("Inserisci una mail");
                if (password_editText.getText() == null)
                    password_til.setError("Inserisci una password");

            }
        }
    }
}
