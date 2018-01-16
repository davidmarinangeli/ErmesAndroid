package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.david.ermes.R;
import com.example.david.ermes.View.fragments.AccountFragment;
import com.example.david.ermes.View.fragments.EventFragment;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        AccountFragment accountFragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", getIntent().getExtras().getParcelable("user"));
        accountFragment.setArguments(args);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.account_fragment_container,
                    accountFragment).commit();

        }
    }
}
