package com.example.david.ermes.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.NotificationType;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.StyleUtils;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.AccountActivity;
import com.example.david.ermes.View.activities.EventActivity;
import com.example.david.ermes.Presenter.utils.TimeUtils;

import static com.example.david.ermes.Model.models.NotificationType.*;

import java.util.Calendar;
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

        View item;

        ImageView icon;
        TextView title;
        TextView text;
        TextView date;
        LinearLayout layout;
        Button left_button;
        Button right_button;

        ProgressDialog mDialog;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.notification_icon);
            title = itemView.findViewById(R.id.notification_title);
            text = itemView.findViewById(R.id.notification_text);
            date = itemView.findViewById(R.id.notification_date);
            layout = itemView.findViewById(R.id.notification_container);
            left_button = itemView.findViewById(R.id.notification_left_button);
            right_button = itemView.findViewById(R.id.notification_right_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executeNotificationAction(notifications.get(getAdapterPosition()));
                }
            });

            date.setTypeface(null, Typeface.ITALIC);

            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Attendi...");
            mDialog.setCancelable(false);

            item = itemView;
        }

        public void bind(int position) {
            if (notifications != null && notifications.size() > 0) {
                Notification n = notifications.get(position);

                styleDefault(n);
                styleByNotificationType(n);
            }
        }

        private void styleDefault(Notification notification) {
            // set default style

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(notification.getDate());

            title.setText(notification.getTitle());
            text.setText(notification.getText());
            date.setText(TimeUtils.fromMillistoYearMonthDay(notification.getDate()) + " alle " +
                    TimeUtils.getFormattedHourMinute(c));

            if (notification.isRead()) {
                layout.setBackgroundColor(Color.WHITE);
//                setButtonsVisible(View.GONE);
            }
        }

        private void styleByNotificationType(Notification notification) {
            switch (notification.getType()) {
                case MATCH_INVITE_USER:
                    // buttons style
                    setButtonsVisible(View.VISIBLE);

                    left_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_event_available_black_24dp, 0, 0, 0);
                    right_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_event_busy_black_24dp, 0, 0, 0);

                    left_button.setText("Partecipa");
                    right_button.setText("Rifiuta");

                    icon.setImageResource(R.drawable.ic_insert_invitation_black_40dp);

                    if (notification.isRead()) {
                        date.append("\n\nHai già risposto a questo invito.");
                    }

                    setButtonsListenersMatchInviteUser(notification);
                    break;
                case FRIENDSHIP_REQUEST:
                    // buttons style
                    setButtonsVisible(View.VISIBLE);

                    left_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_done_black_24dp, 0, 0, 0);
                    right_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_close_black_24dp, 0, 0, 0);

                    left_button.setText("Accetta");
                    right_button.setText("Elimina");

                    icon.setImageResource(R.drawable.ic_person_add_black_40dp);

                    if (notification.isRead()) {
                        date.append("\n\nHai già risposto a questa richiesta.");
                    }

                    setButtonsListenersFriendshipRequest(notification);
                    break;
                case FRIENDSHIP_ACCEPTED:
                    icon.setImageResource(R.drawable.ic_group_black_40dp);

                    setButtonsVisible(View.GONE);
                    break;
            }

            if (notification.isRead()) {
                setButtonsVisible(View.GONE);
            }
        }

        private void setButtonsListenersMatchInviteUser(final Notification notification) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.show();

                    MatchRepository.getInstance().fetchMatchById(notification.getIdMatch(),
                            new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    mDialog.dismiss();

                                    if (object != null) {
                                        Intent i = new Intent(context, EventActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("event", (Match) object);
                                        i.putExtras(bundle);
                                        context.startActivity(i);

                                    } else {
                                        Toast.makeText(context, "C'è stato un problema",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            left_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Click " + left_button.getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });

            right_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Click " + right_button.getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setButtonsListenersFriendshipRequest(final Notification notification) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO link alla pagina profilo (notification.idCreator)
                }
            });

            left_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // save friendship
                    UserRepository.getInstance().getUser(new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            if (object != null) {
                                final User currentUser = (User) object;

                                new Friendship(notification.getIdCreator(), currentUser.getUID(),
                                        System.currentTimeMillis()).save(new FirebaseCallback() {
                                    @Override
                                    public void callback(Object object) {
                                        notification.setRead(true);
                                        notification.save(new FirebaseCallback() {
                                            @Override
                                            public void callback(Object object) {
                                                Notification.createFriendshipAccepted(
                                                        notification.getIdCreator()).save();

                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            });

            right_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notification.setRead(true);
                    notification.save(new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }

        private void executeNotificationAction(final Notification notification) {

            switch (notification.getType()) {
                case MATCH_INVITE_USER:
                    Log.i("NOTIFICATION TYPE", NotificationType.toString(MATCH_INVITE_USER));


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

        private void setButtonsVisible(int visible) {
            left_button.setVisibility(visible);
            right_button.setVisibility(visible);

            final int PADDING = StyleUtils.getDpByPixels(context, 16);

            switch (visible) {
                case View.GONE:
                    layout.setPadding(PADDING, PADDING, PADDING, PADDING);
                    break;
                case View.VISIBLE:
                    final int PADDING_BOTTOM = StyleUtils.getDpByPixels(context, 4);
                    layout.setPadding(PADDING, PADDING, PADDING, PADDING_BOTTOM);
                    break;
            }
        }
    }
}
