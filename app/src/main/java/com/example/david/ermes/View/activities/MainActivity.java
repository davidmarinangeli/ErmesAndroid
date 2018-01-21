package com.example.david.ermes.View.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.NotificationRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.ViewPagerAdapter;
import com.example.david.ermes.View.customviews.CoolViewPager;
import com.example.david.ermes.View.fragments.AccountFragment;
import com.example.david.ermes.View.fragments.FirstOpenLoginFragment;
import com.example.david.ermes.View.fragments.HomeFragment;
import com.example.david.ermes.View.fragments.MapsFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import static android.animation.ValueAnimator.INFINITE;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CoolViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private FloatingActionMenu menu;
    private AHBottomNavigation bottomNavigation;
    private FloatingActionButton default_event_fab;
    private FloatingActionButton add_place_fab;
    private ImageButton notificationsButton;

    AHBottomNavigationItem left_item;
    AHBottomNavigationItem right_item;
    AHBottomNavigationItem central_item;

    private ValueAnimator notification_anim;
    private int num_notifications;

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

        default_event_fab = findViewById(R.id.addefaultevent);
        add_place_fab = findViewById(R.id.addplace);

        notificationsButton = findViewById(R.id.toolbar_notifications_button);
        notification_anim = new ValueAnimator();

        num_notifications = 0;

        initBottomNavigationView();
        manageFABs();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (User.getCurrentUserId() != null && DatabaseManager.get().isLogged())
            viewPagerAdapter.replaceFragment(2, new AccountFragment());
        else
            viewPagerAdapter.replaceFragment(2,new FirstOpenLoginFragment());

    }

    private void manageFABs() {
        if (User.getCurrentUserId() != null) {

            default_event_fab.setColorNormal(default_event_fab.getColorNormal());
            default_event_fab.setColorPressed(default_event_fab.getColorPressed());
            add_place_fab.setColorNormal(add_place_fab.getColorNormal());
            add_place_fab.setColorPressed(add_place_fab.getColorPressed());

        } else {

            default_event_fab.setLabelVisibility(View.GONE);
            add_place_fab.setLabelVisibility(View.GONE);

            default_event_fab.setColorNormal(default_event_fab.getColorDisabled());
            default_event_fab.setColorPressed(R.color.inactive_pressed);
            add_place_fab.setColorNormal(add_place_fab.getColorDisabled());
            add_place_fab.setColorPressed(R.color.inactive_pressed);

        }


        default_event_fab.setColorFilter(R.color.white);

        default_event_fab.setOnClickListener(view1 -> {
            if (User.getCurrentUserId() != null) {
                Intent i = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivityForResult(i, 1);
            } else {
                Snackbar.make(default_event_fab, "Registrati per creare una partita ", Snackbar.LENGTH_LONG).show();
            }
        });

        add_place_fab.setOnClickListener(view -> {
            if (User.getCurrentUserId() != null) {
                Intent i = new Intent(MainActivity.this, PickPlaceActivity.class);
                startActivityForResult(i, 1);
            } else {

                Snackbar.make(add_place_fab, "Registrati per aggiungere un luogo", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (User.getCurrentUserId() != null && DatabaseManager.get().isLogged()) {

            notificationsButton.setVisibility(View.VISIBLE);

            NotificationRepository.getInstance().fetchNotificationsByIdOwner(User.getCurrentUserId(),
                    object -> {
                        // TODO lasciamo il fetch delle notifiche nella main activity per mettere il numeretto in rosso?

                        List<Notification> list = (List<Notification>) object;

                        if (list == null) {
                            list = new ArrayList<>();
                        } else if (Notification.getUnreadNotificationsFromList(list).size() > 0) {

                            if (notification_anim != null) {
                                if (notification_anim.isPaused()) {
                                    notification_anim.resume();
                                } else {
//                                        notification_anim.setIntValues(Color.WHITE, R.color.colorAccent,
//                                                R.color.colorAccent, Color.WHITE);
                                    notification_anim.setIntValues(
                                            Color.argb(255, 255, 255, 255),
                                            Color.argb(255, 68, 138, 255),
                                            Color.argb(255, 68, 138, 255),
                                            Color.argb(255, 255, 255, 255)
                                    );
                                    notification_anim.setEvaluator(new ArgbEvaluator());

                                    notification_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                            notificationsButton.setColorFilter(
                                                    (Integer) valueAnimator.getAnimatedValue(),
                                                    PorterDuff.Mode.SRC_ATOP);
                                        }
                                    });

                                    notification_anim.setRepeatCount(INFINITE);
                                    notification_anim.setDuration(4000);
                                    notification_anim.start();
                                }
                            }
                        } else {
                            notificationsButton.setColorFilter(Color.argb(255, 255, 255, 255));
                        }

                        List<Notification> finalList = list;
                        notificationsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent notificationActivity = new Intent(MainActivity.this,
                                        NotificationsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("notifications", (ArrayList<Notification>) finalList);
                                notificationActivity.putExtras(bundle);

                                startActivity(notificationActivity);
                            }
                        });
                    });
        } else notificationsButton.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (notification_anim != null) {
            notification_anim.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (notification_anim != null) {
            notification_anim.pause();
        }
    }

    private void initBottomNavigationView() {

        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewPager);

        // Da qui creo la bottom navigation view
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Creo items
        left_item = new AHBottomNavigationItem(R.string.title_maps, R.drawable.ic_place_black_24dp, R.color.colorPrimary);
        right_item = new AHBottomNavigationItem(R.string.title_account, R.drawable.ic_person_black_24dp, R.color.colorPrimary);
        central_item = new AHBottomNavigationItem(R.string.title_home, R.drawable.ic_home_black_24dp, R.color.colorPrimary);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        bottomNavigation.removeAllItems();

        // Aggiungo items
        bottomNavigation.addItem(left_item);
        bottomNavigation.addItem(central_item);
        bottomNavigation.addItem(right_item);

        viewPagerAdapter.addFragment(new MapsFragment());
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new FirstOpenLoginFragment());

        viewPager.setAdapter(viewPagerAdapter);

        //setto colore
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(this, R.color.inactive));
        bottomNavigation.setForceTint(true);

        bottomNavigation.setCurrentItem(1);
        viewPager.setCurrentItem(1);

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            viewPager.setCurrentItem(position);
            toolbar.setVisibility(View.VISIBLE);
            return true;
        });
    }


}
