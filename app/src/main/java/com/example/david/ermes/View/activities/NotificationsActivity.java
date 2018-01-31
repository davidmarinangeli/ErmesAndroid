package com.example.david.ermes.View.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.NotificationRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.NotificationAdapter;
import com.example.david.ermes.View.ProgressDialog;

import java.util.List;


public class NotificationsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView no_notifications;

    private User currentUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        progressDialog = new ProgressDialog(this);

        adapter = new NotificationAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        no_notifications = findViewById(R.id.no_notifications_label);

        recyclerView = findViewById(R.id.notifications_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        toolbar = findViewById(R.id.notification_toolbar);
        toolbar.setTitle("Notifiche");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserRepository.getInstance().getUser(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    currentUser = (User) object;

                    adapter.setCurrentUser(currentUser);
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
            progressDialog.show();

            NotificationRepository.getInstance().fetchNotificationsByIdOwner(user.getUID(),
                    object -> {
                        if (object != null) {
                            adapter.refreshList((List<Notification>) object);

                            if (adapter.getItemCount() <= 0) {
                                no_notifications.setText("Nessuna notifica");
                                no_notifications.setVisibility(View.VISIBLE);
                            } else {
                                no_notifications.setVisibility(View.GONE);

                                List<Notification> list = (List<Notification>) object;
                                int unreadCount = Notification.getUnreadNotificationsFromList(list)
                                                .size();

                                String toolbar_title = unreadCount > 0 ?
                                        "Notifiche (" + String.valueOf(unreadCount) + ")"
                                        : "Notifiche";

                                toolbar.setTitle(toolbar_title);
                            }
                        }

                        progressDialog.dismiss();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Nessun utente loggato",
                    Toast.LENGTH_LONG).show();
        }
    }
}
