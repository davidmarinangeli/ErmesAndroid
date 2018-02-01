package com.example.david.ermes.View.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.R;
import com.example.david.ermes.View.fragments.FutureMatchesFragment;
import com.example.david.ermes.View.fragments.MyOwnMatchesFragment;
import com.example.david.ermes.View.fragments.PassedMatchesFragment;

public class MyMatchesActivity extends AppCompatActivity {

    private User currentUser;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_matches);

        currentUser = getIntent().getExtras().getParcelable("user");

        Toolbar toolbar = findViewById(R.id.my_matches_toolbar);
        toolbar.setTitle("Partite");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.my_matches_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.my_matches_tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FutureMatchesFragment fmfragment = new FutureMatchesFragment();
                    fmfragment.setUser(currentUser);
                    return fmfragment;
                case 1:
                    PassedMatchesFragment pmfragment = new PassedMatchesFragment();
                    pmfragment.setUser(currentUser);
                    return pmfragment;
                case 2:
                    MyOwnMatchesFragment momfragment = new MyOwnMatchesFragment();
                    momfragment.setUser(currentUser);
                    return momfragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
