package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
<<<<<<< HEAD
=======
import com.example.david.ermes.View.ProgressDialog;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
import com.example.david.ermes.View.fragments.AccountFragment;
import com.example.david.ermes.View.fragments.EventFragment;

import java.time.Duration;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
<<<<<<< HEAD
    private User currentUser;
=======

    private User currentUser;
    private ProgressDialog progressDialog;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

<<<<<<< HEAD
=======
        progressDialog = new ProgressDialog(this);

>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
        toolbar = findViewById(R.id.account_toolbar);

        currentUser = getIntent().getExtras().getParcelable("user");

        if (currentUser != null) {
            init(savedInstanceState);
        } else {
<<<<<<< HEAD
=======
            progressDialog.show();

>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
            UserRepository.getInstance().getUser(object -> {
                currentUser = (User) object;

                if (currentUser != null) {
                    init(savedInstanceState);
                } else {
                    Toast.makeText(this,"Errore nello scaricamento dei dati",
                            Toast.LENGTH_LONG).show();
                }
<<<<<<< HEAD
=======

                progressDialog.dismiss();
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void init(Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(currentUser.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountFragment accountFragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", currentUser);
        accountFragment.setArguments(args);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.account_fragment_container,
                    accountFragment).commit();
        }
    }
}
