package com.example.david.ermes.View;

import com.example.david.ermes.View.ProgressDialog;

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
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.TeamRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.StyleUtils;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.AccountActivity;
import com.example.david.ermes.View.activities.EventActivity;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.View.activities.TeamActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 10/01/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private List<Match> matches;
    private List<User> users;
    private List<Team> teams;
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
        this.teams = new ArrayList<>();

        for (int i = 0; i < notifications.size(); i++) {
            this.matches.add(null);
            this.users.add(null);
            this.teams.add(null);
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
        Button right_button;
        Button left_button;
        TextView already_reply;

        ProgressDialog mDialog;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.notification_icon);
            title = itemView.findViewById(R.id.notification_title);
            text = itemView.findViewById(R.id.notification_text);
            date = itemView.findViewById(R.id.notification_date);
            layout = itemView.findViewById(R.id.notification_container);
            right_button = itemView.findViewById(R.id.notification_right_button);
            left_button = itemView.findViewById(R.id.notification_left_button);
            already_reply = itemView.findViewById(R.id.already_reply);

            mDialog = new ProgressDialog(context);

            item = itemView;

            item.setOnClickListener(itemListener);
            right_button.setOnClickListener(rightButtonListener);
            left_button.setOnClickListener(leftButtonListener);
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

            title.setText(notification.getTitle());
            text.setText(notification.getText());
            date.setText(TimeUtils.getFormattedElapsedTime(notification.getDate()));

            if (notification.isRead()) {
                layout.setBackgroundColor(Color.WHITE);
//                setButtonsVisible(View.GONE);
            } else {
                layout.setBackgroundResource(R.color.notificationBackground);
            }
        }

        private void styleByNotification(Notification notification) {
            switch (notification.getType()) {
                case MATCH_INVITE_USER:
                    // buttons style
                    setButtonsVisible(View.VISIBLE);
                    right_button.setText("Partecipa");
                    left_button.setText("Rifiuta");

                    icon.setImageResource(R.drawable.ic_insert_invitation_black_40dp);

                    if (notification.isRead()) {
                        already_reply.setText("Hai già risposto a questo invito");
                        already_reply.setVisibility(View.VISIBLE);
                    }

                    break;
                case FRIENDSHIP_REQUEST:
                    // buttons style
                    setButtonsVisible(View.VISIBLE);
                    right_button.setText("Accetta");
                    left_button.setText("Elimina");

                    icon.setImageResource(R.drawable.ic_person_add_black_40dp);

                    if (notification.isRead()) {
                        already_reply.setText("Hai già risposto a questa richiesta");
                        already_reply.setVisibility(View.VISIBLE);
                    } else {
                        already_reply.setVisibility(View.GONE);
                    }

                    break;
                case FRIENDSHIP_ACCEPTED:
                    icon.setImageResource(R.drawable.ic_group_black_40dp);

                    already_reply.setVisibility(View.GONE);
                    setButtonsVisible(View.GONE);
                    break;
                case TEAM_ADDED:
                case USER_LEAVE_TEAM:
                    icon.setImageResource(R.drawable.team_icon);

                    already_reply.setVisibility(View.GONE);
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

                    fetchMatch(notifications.get(position).getIdMatch(), position, object -> {
                        mDialog.dismiss();
                        item.setActivated(true);

                        if (object != null) {
                            startMatchActivity(position);
                        } else wrongSnackbar(item);
                    });
                    break;
                case FRIENDSHIP_REQUEST:
                    item.setActivated(false);
                    mDialog.show();

                    fetchUser(notifications.get(position).getIdCreator(), position, object -> {
                        mDialog.dismiss();
                        item.setActivated(true);

                        if (object != null) {
                            startUserActivity((User) object);
                        } else wrongSnackbar(item);
                    });
                    break;
                case FRIENDSHIP_ACCEPTED:
                    mDialog.show();
                    item.setActivated(false);

                    fetchUser(notifications.get(position).getIdCreator(), position, object -> {
                        if (object != null) {
                            readFriendshipAccepted(position, (User) object);
                        } else wrongSnackbar(item);
                    });
                    break;
                case TEAM_ADDED:
                case USER_LEAVE_TEAM:
                    mDialog.show();
                    item.setActivated(false);

                    fetchTeam(notifications.get(position).getIdTeam(), position, object -> {
                        if (object != null) {
                            readTeamNotification(position);
                        } else wrongSnackbar(item);
                    });
            }
        };

        private View.OnClickListener rightButtonListener = view -> {
            int position = getAdapterPosition();

            switch (notifications.get(position).getType()) {
                case MATCH_INVITE_USER:
                    item.setActivated(false);

                    fetchMatch(notifications.get(position).getIdMatch(), position, object -> {
                        item.setActivated(true);

                        if (object != null) {
                            acceptMatchInvitation(position);
                        } else wrongSnackbar(item);
                    });
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

        private View.OnClickListener leftButtonListener = view -> {
            int position = getAdapterPosition();

            switch (notifications.get(position).getType()) {
                case MATCH_INVITE_USER:
                    item.setActivated(false);
                    mDialog.show();

                    fetchMatch(notifications.get(position).getIdMatch(), position, object -> {
                        item.setActivated(true);

                        if (object != null) {
                            declineMatchInvitation(position);
                        } else wrongSnackbar(item);
                    });
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
            if (users.get(position) != null) {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(users.get(position));
                }
            } else {
                UserRepository.getInstance().fetchUserById(id, object -> {
                    users.set(position, (User) object);

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(object);
                    }
                });
            }
        }

        private void fetchMatch(String id, int position, FirebaseCallback firebaseCallback) {
            if (matches.get(position) != null) {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(matches.get(position));
                }
            } else {
                MatchRepository.getInstance().fetchMatchById(id, object -> {
                    matches.set(position, (Match) object);

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(object);
                    }
                });
            }
        }

        private void fetchTeam(String id, int position, FirebaseCallback firebaseCallback) {
            if (teams.get(position) != null) {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(teams.get(position));
                }
            } else {
                TeamRepository.getInstance().fetchTeamById(id, object -> {
                    Team team = (Team) object;
                    teams.set(position, team);

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(team);
                    }
                });
            }
        }

        private void startUserActivity(User user) {
            mDialog.dismiss();
            item.setActivated(true);
            Intent accountActivity = new Intent(context, AccountActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable("user", user);
            accountActivity.putExtras(extras);
            context.startActivity(accountActivity);
        }

        private void startTeamActivity(Team team) {
            mDialog.dismiss();
            item.setActivated(true);
            Intent teamActivity = new Intent(context, TeamActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(TeamActivity.ACTIVITY_TEAM_KEY, team);
            extras.putString(TeamActivity.ACTIVITY_TYPE_KEY, TeamActivity.TEAM_VIEW);
            teamActivity.putExtras(extras);
            context.startActivity(teamActivity);
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
            notification.save(object -> {
                mDialog.dismiss();
                item.setActivated(true);

                notifyDataSetChanged();
            });
        }

        private void readFriendshipAccepted(int position, User user) {
            notifications.get(position).setRead(true);
            notifications.get(position).save(object -> {
                mDialog.dismiss();
                item.setActivated(true);
                notifyDataSetChanged();

                startUserActivity(user);
            });
        }

        private void readTeamNotification(int position) {
            Team focusTeam = teams.get(position);

            notifications.get(position).setRead(true);
            notifications.get(position).save(object -> {
                mDialog.dismiss();
                item.setActivated(true);
                notifyDataSetChanged();

                startTeamActivity(focusTeam);
            });
        }

        private void setButtonsVisible(int visible) {
            right_button.setVisibility(visible);
            left_button.setVisibility(visible);

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

        private void wrongSnackbar(View item) {
            mDialog.dismiss();
            item.setActivated(true);

            Snackbar.make(item, "Qualcosa è andato storto!",
                    Toast.LENGTH_SHORT).show();
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
