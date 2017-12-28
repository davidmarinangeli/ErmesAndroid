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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.david.ermes.View.ViewPagerAdapter;
import com.example.david.ermes.R;
import com.example.david.ermes.View.customviews.CoolViewPager;
import com.example.david.ermes.View.fragments.AccountFragment;
import com.example.david.ermes.View.fragments.HomeFragment;
import com.example.david.ermes.View.fragments.MapsFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private CoolViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private DrawerLayout drawer;
    private FloatingActionMenu menu;
    private AHBottomNavigation bottomNavigation;
    private FragmentManager manager;
    private MaterialSearchView searchView;
    private FloatingActionButton defaulteventfab;
    private FloatingActionButton addPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_contenitore, new HomeFragment()).commit();

        }
        */

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        menu = findViewById(R.id.main_fab_menu);
        menu.setAnimated(true);
        menu.setClosedOnTouchOutside(true);

        defaulteventfab = findViewById(R.id.addefaultevent);
        addPlace = findViewById(R.id.addplace);


        defaulteventfab.setColorFilter(R.color.white);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        searchView = findViewById(R.id.search_view);
        searchView.setHint(getResources().getString(R.string.cerca_evento));
        searchView.setBackIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_blue_24dp));
        searchView.setCursorDrawable(R.drawable.custom_cursor);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //Do some magic
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Do some magic
                    return false;
                }
            });

            searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {
                    //Do some magic
                }

                @Override
                public void onSearchViewClosed() {
                    //Do some magic
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
