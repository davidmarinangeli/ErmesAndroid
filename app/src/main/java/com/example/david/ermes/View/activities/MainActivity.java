package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.repository.FriendshipRepository;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.View.ViewPagerAdapter;
import com.example.david.ermes.R;
import com.example.david.ermes.View.customviews.CoolViewPager;
import com.example.david.ermes.View.fragments.AccountFragment;
import com.example.david.ermes.View.fragments.HomeFragment;
import com.example.david.ermes.View.fragments.MapsFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;


public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private CoolViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private FloatingActionMenu menu;
    private AHBottomNavigation bottomNavigation;
    private FloatingActionButton defaulteventfab;
    private FloatingActionButton addPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        menu = findViewById(R.id.main_fab_menu);
        menu.setAnimated(true);
        menu.setClosedOnTouchOutside(true);

        defaulteventfab = findViewById(R.id.addefaultevent);
        addPlace = findViewById(R.id.addplace);


        defaulteventfab.setColorFilter(R.color.white);

        defaulteventfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivityForResult(i, 1);
            }
        });

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PickPlaceActivity.class);
                startActivityForResult(i,1);
            }
        });

        initBottomNavigationView();
    }

    private void initBottomNavigationView() {
        // Da qui creo la bottom navigation view
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        viewPager = findViewById(R.id.viewPager);

        // Creo items
        AHBottomNavigationItem left_item = new AHBottomNavigationItem(R.string.title_maps, R.drawable.ic_place_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem right_item = new AHBottomNavigationItem(R.string.title_account, R.drawable.ic_person_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem central_item = new AHBottomNavigationItem(R.string.title_home, R.drawable.ic_home_black_24dp, R.color.colorPrimary);

        // Aggiungo items
        bottomNavigation.addItem(left_item);
        bottomNavigation.addItem(central_item);
        bottomNavigation.addItem(right_item);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(new MapsFragment());
        viewPagerAdapter.addFragments(new HomeFragment());
        viewPagerAdapter.addFragments(new AccountFragment());
        viewPager.setAdapter(viewPagerAdapter);


        //setto colore
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(this, R.color.inactive));
        bottomNavigation.setForceTint(true);

        bottomNavigation.setCurrentItem(1);
        viewPager.setCurrentItem(1);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    viewPager.setCurrentItem(position);
                } else if (position == 1) {
                    viewPager.setCurrentItem(position);
                } else if (position == 2) {
                    viewPager.setCurrentItem(position);

                }
                return true;
            }
        });
    }


}
