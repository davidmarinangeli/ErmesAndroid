package com.example.david.ermes.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.NotificationType;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.AccountActivity;
import com.example.david.ermes.View.activities.EventActivity;

import static com.example.david.ermes.Model.models.NotificationType.*;

import java.util.List;

/**
 * Created by nicol on 10/01/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.default_notification_item,
                parent,
                false
        );

        return new NotificationAdapter.NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    public void refreshList(List<Notification> notifications){
        this.notifications = notifications;

        notifyDataSetChanged();
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;
        TextView date;
        LinearLayout layout;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notification_title);
            text = itemView.findViewById(R.id.notification_text);
            date = itemView.findViewById(R.id.notification_date);
            layout = itemView.findViewById(R.id.notification_container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executeNotificationAction(notifications.get(getAdapterPosition()));
                }
            });

            date.setTypeface(null, Typeface.ITALIC);
            date.setText("in data: doman");
        }

        public void bind(int position) {
            if (notifications != null) {
                Notification n = notifications.get(position);

                title.setText(n.getTitle());
                text.setText(n.getText());

                if (n.isRead()) {
                    layout.setBackgroundColor(Color.WHITE);
                }
            }
        }

        private void executeNotificationAction(final Notification notification) {
            final ProgressDialog mDialog = new ProgressDialog(context);
            mDialog.setMessage("Attendi...");
            mDialog.setCancelable(false);

            switch (notification.getType()) {
                case MATCH_INVITE_USER:
                    Log.i("NOTIFICATION TYPE", NotificationType.toString(MATCH_INVITE_USER));

                    mDialog.show();

                    MatchRepository.getInstance().fetchMatchById(notification.getIdMatch(),
                            new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    mDialog.dismiss();

                                    if (object != null) {
                                        notification.setRead(true);

                                        Intent i = new Intent(context, EventActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("event", (Match) object);
                                        i.putExtras(bundle);
                                        context.startActivity(i);

                                        notification.save(new FirebaseCallback() {
                                            @Override
                                            public void callback(Object object) {
                                                notifyDataSetChanged();
                                            }
                                        });

                                    } else {
                                        Toast.makeText(context, "C'è stato un problema",
                                                Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                    break;
                case FRIENDSHIP_REQUEST:
                    Log.i("NOTIFICATION TYPE", NotificationType.toString(FRIENDSHIP_REQUEST));

                    Intent i = new Intent(context, AccountActivity.class);
                    context.startActivity(i);
                    break;
                case FRIENDSHIP_ACCEPTED:
                    Log.i("NOTIFICATION TYPE", NotificationType.toString(FRIENDSHIP_ACCEPTED));
                    break;
            }
        }
    }
}
