package com.example.david.ermes.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.StyleUtils;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.AccountActivity;
import com.example.david.ermes.View.activities.EventActivity;
import com.example.david.ermes.Presenter.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nicol on 10/01/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private List<Match> matches;
    private List<User> users;
    private Context context;

    private User currentUser;

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

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void refreshList(List<Notification> notifications) {
        this.notifications = notifications;

        this.matches = new ArrayList<>();
        this.users = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++) {
            this.matches.add(null);
            this.users.add(null);
        }

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
        TextView already_reply;

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
            already_reply = itemView.findViewById(R.id.already_reply);

            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Attendi...");
            mDialog.setCancelable(false);

            item = itemView;

            item.setOnClickListener(itemListener);
            left_button.setOnClickListener(leftButtonListener);
            right_button.setOnClickListener(rightButtonListener);
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
                            R.drawable.ic_event_available_white_24dp, 0, 0, 0);
                    right_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_event_busy_white_24dp, 0, 0, 0);

                    left_button.setText("Partecipa");
                    right_button.setText("Rifiuta");

                    icon.setImageResource(R.drawable.ic_insert_invitation_black_40dp);

                    if (notification.isRead()) {
                        already_reply.setText("Hai già risposto a questo invito");
                        already_reply.setVisibility(View.VISIBLE);
                    }

                    break;
                case FRIENDSHIP_REQUEST:
                    // buttons style
                    setButtonsVisible(View.VISIBLE);

                    left_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_done_white_24dp, 0, 0, 0);
                    right_button.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_close_white_24dp, 0, 0, 0);

                    left_button.setText("Accetta");
                    right_button.setText("Elimina");

                    icon.setImageResource(R.drawable.ic_person_add_black_40dp);

                    if (notification.isRead()) {
                        already_reply.setText("Hai già risposto a questa richiesta");
                        already_reply.setVisibility(View.VISIBLE);
                    }

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

        private View.OnClickListener itemListener = view -> {
            int position = getAdapterPosition();

            switch (notifications.get(position).getType()) {
                case MATCH_INVITE_USER:
                    item.setActivated(false);
                    mDialog.show();

                    if (matches.get(position) != null) {
                        startMatchActivity(position);
                    } else {
                        fetchMatch(notifications.get(position).getIdMatch(), position, object -> {
                            mDialog.dismiss();
                            item.setActivated(true);

                            if (object != null) {
                                startMatchActivity(position);
                            } else {
                                Snackbar.make(item, "Qualcosa è andato storto!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case FRIENDSHIP_REQUEST:
                    item.setActivated(false);
                    mDialog.show();

                    if (users.get(position) != null) {
                        startUserActivity(position);
                    } else {
                        fetchUser(notifications.get(position).getIdCreator(), position, object -> {
                            mDialog.dismiss();
                            item.setActivated(true);

                            if (object != null) {
                                startUserActivity(position);
                            } else {
                                Snackbar.make(item, "Qualcosa è andato storto!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case FRIENDSHIP_ACCEPTED:
                    mDialog.show();
                    item.setActivated(false);

                    if (users.get(position) != null) {
                        readFriendshipAccepted(position);
                    } else {
                        fetchUser(notifications.get(position).getIdCreator(), position, object -> {
                            if (object != null) {
                                readFriendshipAccepted(position);
                            } else {
                                Snackbar.make(item, "Qualcosa è andato storto!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
        };

        private View.OnClickListener leftButtonListener = view -> {
            int position = getAdapterPosition();

            switch (notifications.get(position).getType()) {
                case MATCH_INVITE_USER:
                    item.setActivated(false);
                    Match match = matches.get(position);

                    if (match != null) {
                        acceptMatchInvitation(position);
                    } else {
                        fetchMatch(notifications.get(position).getIdMatch(), position, object -> {
                            item.setActivated(true);

                            if (object != null) {
                                acceptMatchInvitation(position);
                            } else {
                                Snackbar.make(item, "Qualcosa è andato storto!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case FRIENDSHIP_REQUEST:
                    mDialog.show();
                    item.setActivated(false);

                    acceptFriendship(position);
                    break;
                default:
                    break;
            }
        };

        private View.OnClickListener rightButtonListener = view -> {
            int position = getAdapterPosition();

            switch (notifications.get(position).getType()) {
                case MATCH_INVITE_USER:
                    item.setActivated(false);
                    mDialog.show();

                    if (matches.get(position) != null && DatabaseManager.get().isLogged()) {
                        declineMatchInvitation(position);
                    } else {
                        fetchMatch(notifications.get(position).getIdMatch(), position, object -> {
                            item.setActivated(true);

                            if (object != null) {
                                declineMatchInvitation(position);
                            } else {
                                Snackbar.make(item, "Qualcosa è andato storto!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case FRIENDSHIP_REQUEST:
                    mDialog.show();
                    item.setActivated(false);

                    declineFriendShip(position);
                    break;
                default:
                    break;
            }
        };

        private void fetchMatch(String id, int position, FirebaseCallback firebaseCallback) {
            MatchRepository.getInstance().fetchMatchById(id, object -> {
                matches.set(position, (Match) object);

                if (firebaseCallback != null) {
                    firebaseCallback.callback(object);
                }
            });
        }

        private void startMatchActivity(int position) {
            mDialog.dismiss();
            item.setActivated(true);
            Intent i = new Intent(context, EventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", matches.get(position));
            i.putExtras(bundle);
            context.startActivity(i);
        }

        private void acceptMatchInvitation(int position) {
            Match match = matches.get(position);
            Notification notification = notifications.get(position);
            resetMatchInviteUserToken();

            match.addPartecipant(User.getCurrentUserId());
            match.save(object -> {
                incrementMatchInviteUserToken();

                if (getMatchInviteUserToken() == MAX_TOKEN) {
                    itemView.setActivated(true);
                    mDialog.dismiss();
                    notifyDataSetChanged();
                }
            });

            notification.setRead(true);
            notification.save(object -> {
                incrementMatchInviteUserToken();

                if (getMatchInviteUserToken() == MAX_TOKEN) {
                    itemView.setActivated(true);
                    mDialog.dismiss();
                    notifyDataSetChanged();
                }
            });
        }

        private void declineMatchInvitation(int position) {
            Match match = matches.get(position);
            Notification notification = notifications.get(position);

            match.removePending(User.getCurrentUserId());
            resetMatchInviteUserToken();

            match.save(object -> {
                incrementMatchInviteUserToken();

                if (getMatchInviteUserToken() == MAX_TOKEN) {
                    itemView.setActivated(true);
                    mDialog.dismiss();
                    notifyDataSetChanged();
                }
            });

            notification.setRead(true);
            notification.save(object -> {
                incrementMatchInviteUserToken();

                if (getMatchInviteUserToken() == MAX_TOKEN) {
                    itemView.setActivated(true);
                    mDialog.dismiss();
                    notifyDataSetChanged();
                }
            });
        }

        private void fetchUser(String id, int position, FirebaseCallback firebaseCallback) {
            UserRepository.getInstance().fetchUserById(id, object -> {
                users.set(position, (User) object);

                if (firebaseCallback != null) {
                    firebaseCallback.callback(object);
                }
            });
        }

        private void startUserActivity(int position) {
            mDialog.dismiss();
            item.setActivated(true);
            Intent accountActivity = new Intent(context, AccountActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable("user", users.get(position));
            accountActivity.putExtras(extras);
            context.startActivity(accountActivity);
        }

        private void acceptFriendship(int position) {
            Notification notification = notifications.get(position);

            new Friendship(notification.getIdCreator(), notification.getIdOwner(),
                    System.currentTimeMillis()).save(object -> {
                notification.setRead(true);
                notification.save(object1 -> Notification.createFriendshipAccepted(
                        currentUser, notification.getIdCreator()).save(
                        object11 -> {
                            mDialog.dismiss();
                            item.setActivated(true);
                            notifyDataSetChanged();
                        }
                ));
            });
        }

        private void declineFriendShip(int position) {
            Notification notification = notifications.get(position);

            notification.setRead(true);
            notification.save(new FirebaseCallback() {
                @Override
                public void callback(Object object) {
                    mDialog.dismiss();
                    item.setActivated(true);

                    notifyDataSetChanged();
                }
            });
        }

        private void readFriendshipAccepted(int position) {
            notifications.get(position).setRead(true);
            notifications.get(position).save(object -> {
                mDialog.dismiss();
                item.setActivated(true);
                notifyDataSetChanged();

                startUserActivity(position);
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
