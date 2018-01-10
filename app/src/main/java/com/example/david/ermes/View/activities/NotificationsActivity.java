package com.example.david.ermes.View.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.NotificationRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.NotificationAdapter;

import java.util.List;


public class NotificationsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        adapter = new NotificationAdapter(this);
        recyclerView = findViewById(R.id.notifications_container);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        toolbar = findViewById(R.id.notification_toolbar);
        toolbar.setTitle("Notifiche");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserRepository.getInstance().getUser(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    currentUser = (User) object;

                    toolbar.setSubtitle(currentUser.getName());
                    fetchNotifications(currentUser);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fetchNotifications(User user) {
        if (user != null) {
            NotificationRepository.getInstance().fetchNotificationsByIdOwner(user.getUID(),
                    new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            if (object != null) {
                                adapter.refreshList((List<Notification>) object);
                            }
                        }
                    });
        } else {
            // TODO textView "Devi loggarti, stronzo"
        }
    }
}
