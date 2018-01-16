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

    public void refreshList(List<Notification> notifications) {
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

            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Attendi...");
            mDialog.setCancelable(false);

            item = itemView;
        }

        public void bind(int position) {
            if (notifications != null && notifications.size() > 0) {
                Notification n = notifications.get(position);

                styleDefault(n);
                styleByNotification(n);
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

        private void styleByNotification(Notification notification) {
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


                    setButtonsListenersMatchInviteUser(notification, null);
                    MatchRepository.getInstance().fetchMatchById(notification.getIdMatch(),
                            new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    mDialog.dismiss();

                                    setButtonsListenersMatchInviteUser(notification, (Match) object);
                                }
                            });
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

                    setButtonsListenersFriendshipRequest(notification, null);
                    UserRepository.getInstance().fetchUserById(notification.getIdCreator(),
                            new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    setButtonsListenersFriendshipRequest(notification, (User) object);
                                }
                            });
                    break;
                case FRIENDSHIP_ACCEPTED:
                    icon.setImageResource(R.drawable.ic_group_black_40dp);

                    setItemListenerFriendshipAccepted(notification, null);
                    UserRepository.getInstance().fetchUserById(notification.getIdCreator(),
                            new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    setItemListenerFriendshipAccepted(notification, (User) object);
                                }
                            });

                    setButtonsVisible(View.GONE);
                    break;
            }

            if (notification.isRead()) {
                setButtonsVisible(View.GONE);
            }
        }

        private void setButtonsListenersMatchInviteUser(Notification notification, Match match) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.show();

                    if (match != null) {
                        Intent i = new Intent(context, EventActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("event", match);
                        i.putExtras(bundle);
                        context.startActivity(i);

                    } else {
                        Toast.makeText(context, "Attendi...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            left_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setActivated(false);

                    mDialog.show();

                    resetMatchInviteUserToken();

                    if (match != null && User.getCurrentUserId() != null) {

                        if (match.getPending().contains(User.getCurrentUserId())) {
                            match.addPartecipant(User.getCurrentUserId());
                            match.save(new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    incrementMatchInviteUserToken();

                                    if (getMatchInviteUserToken() == MAX_TOKEN) {
                                        itemView.setActivated(true);
                                        mDialog.dismiss();
                                        notifyDataSetChanged();
                                    }
                                }
                            });
                        } else {
                            incrementMatchInviteUserToken();

                            Toast.makeText(context, "Non sei più invitato a " +
                                            "questa partita!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        notification.setRead(true);
                        notification.save(new FirebaseCallback() {
                            @Override
                            public void callback(Object object) {
                                incrementMatchInviteUserToken();

                                if (getMatchInviteUserToken() == MAX_TOKEN) {
                                    itemView.setActivated(true);
                                    mDialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(context, "Qualcosa è andato storto",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            right_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setActivated(false);

                    mDialog.show();

                    resetMatchInviteUserToken();

                    if (match != null && User.getCurrentUserId() != null) {
                        match.removePending(User.getCurrentUserId());

                        match.save(new FirebaseCallback() {
                            @Override
                            public void callback(Object object) {
                                incrementMatchInviteUserToken();

                                if (getMatchInviteUserToken() == MAX_TOKEN) {
                                    itemView.setActivated(true);
                                    mDialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            }
                        });

                        notification.setRead(true);
                        notification.save(new FirebaseCallback() {
                            @Override
                            public void callback(Object object) {
                                incrementMatchInviteUserToken();

                                if (getMatchInviteUserToken() == MAX_TOKEN) {
                                    itemView.setActivated(true);
                                    mDialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(context, "Qualcosa è andato storto",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void setButtonsListenersFriendshipRequest(Notification notification, User user) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user != null) {
                        Intent accountActivity = new Intent(context, AccountActivity.class);

                        Bundle extras = new Bundle();
                        extras.putParcelable("user", user);

                        accountActivity.putExtras(extras);
                        context.startActivity(accountActivity);
                    } else {
                        Toast.makeText(context, "Attendi...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            left_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // save friendship
                    itemView.setActivated(false);

                    new Friendship(notification.getIdCreator(), notification.getIdOwner(),
                            System.currentTimeMillis()).save(new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            notification.setRead(true);
                            notification.save(new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    Notification.createFriendshipAccepted(
                                            notification.getIdCreator()).save(
                                            new FirebaseCallback() {
                                                @Override
                                                public void callback(Object object) {
                                                    itemView.setActivated(true);
                                                    notifyDataSetChanged();
                                                }
                                            }
                                    );
                                }
                            });
                        }
                    });
                }
            });

            right_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setActivated(false);

                    notification.setRead(true);
                    notification.save(new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            itemView.setActivated(true);
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }

        private void setItemListenerFriendshipAccepted(Notification notification, User user) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user != null) {
                        notification.setRead(true);
                        notification.save(new FirebaseCallback() {
                            @Override
                            public void callback(Object object) {
                                notifyDataSetChanged();

                                Intent accountActivity = new Intent(context, AccountActivity.class);

                                Bundle extras = new Bundle();
                                extras.putParcelable("user", user);

                                accountActivity.putExtras(extras);
                                context.startActivity(accountActivity);
                            }
                        });
                    } else {
                        Toast.makeText(context, "Attendi...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void setButtonsVisible(int visible) {
            left_button.setVisibility(visible);
            right_button.setVisibility(visible);

            final int PADDING = StyleUtils.getDpByPixels(context, 16);

            switch (visible) {
                case View.GONE:
                    layout.setPadding(PADDING, PADDING / 2, PADDING, PADDING / 2);
                    break;
                case View.VISIBLE:
                    final int PADDING_BOTTOM = StyleUtils.getDpByPixels(context, 4);
                    layout.setPadding(PADDING, PADDING / 2, PADDING, PADDING_BOTTOM);
                    break;
            }
        }

        private int matchInviteUserToken;
        private final int MAX_TOKEN = 2;

        private void resetMatchInviteUserToken() {
            matchInviteUserToken = 0;
        }

        private void incrementMatchInviteUserToken() {
            matchInviteUserToken += 1;
        }

        private int getMatchInviteUserToken() {
            return matchInviteUserToken;
        }
    }
}
