package com.example.david.ermes.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.FriendshipRepository;
import com.example.david.ermes.Model.repository.NotificationRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.OnUserListFetchEnd;
import com.example.david.ermes.Presenter.UserListPresenter;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;
import com.example.david.ermes.View.activities.AccountActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nicol on 15/01/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.FriendViewHolder> {

    public enum ListType {
        GENERIC, FRIENDS
    }

    private int refreshCount = 0;

    private List<User> userList;
    private List<Notification> myRequestList;
    private List<Notification> toMeRequestList;
    private List<Friendship> friendshipList;
    private List<String> sportList;

    private UserListPresenter presenter;
    private Context context;

    public UserListAdapter(Context context) {
        this.context = context;
        sportList = new ArrayList<>();

        presenter = new UserListPresenter();
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.default_friend_item,
                parent,
                false
        );

        return new UserListAdapter.FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    private void refresh(List<User> users, List<Friendship> friendships,
                         List<Notification> myFriendshipRequests, List<String> sports,
                         List<Notification> toMeFriendshipRequests) {
        userList = users;
        friendshipList = friendships;
        myRequestList = myFriendshipRequests;
        toMeRequestList = toMeFriendshipRequests;
        sportList = sports;

        notifyDataSetChanged();
    }

    private void setPresenterCallback(FirebaseCallback firebaseCallback) {
        presenter.setOnUserListFetchEnd((users, friendships, myFriendshipRequests,
                                         toMeFriendshipRequests, sports) -> {
            refresh(users, friendships, myFriendshipRequests, sports, toMeFriendshipRequests);

            if (firebaseCallback != null) {
                if (users != null) {
                    firebaseCallback.callback(users.size());
                } else {
                    firebaseCallback.callback(0);
                }
            }
        });
    }

    public void refreshFriendList(FirebaseCallback firebaseCallback) {
        setPresenterCallback(firebaseCallback);
        presenter.prepareFriendList();
    }

    public void refreshUserList(List<String> userIdList, FirebaseCallback firebaseCallback) {
        setPresenterCallback(firebaseCallback);
        presenter.prepareList(userIdList);
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friendName;
        TextView friendInfo;
        TextView friendshipDate;
        CircularImageView friendImage;
        Button friendshipRequestButton;

        View itemView;
        UserListPresenter.RelationType relationType;
        long friendship_date;

        public FriendViewHolder(View itemView) {
            super(itemView);

            friendName = itemView.findViewById(R.id.friend_name_invitation);
            friendInfo = itemView.findViewById(R.id.friend_info);
            friendshipDate = itemView.findViewById(R.id.friendship_date);
            friendImage = itemView.findViewById(R.id.friend_image);
            friendshipRequestButton = itemView.findViewById(R.id.friendship_request_button);

            this.itemView = itemView;
            this.itemView.setOnClickListener(view -> {
                Intent accountActivity = new Intent(context, AccountActivity.class);

                Bundle extras = new Bundle();
                extras.putParcelable("user", userList.get(getAdapterPosition()));

                accountActivity.putExtras(extras);
                context.startActivity(accountActivity);
            });

            friendshipRequestButton.setOnClickListener(view -> {
                relationType = getRelationType(getAdapterPosition());

                switch (relationType) {
                    case NO_RELATION:
                        User user = userList.get(getAdapterPosition());
                        Friendship.requestFriendshipTo(user.getUID(), object -> {
                            presenter.updateMyFriendhipRequest(getAdapterPosition(),
                                    (Notification) object);
                            notifyDataSetChanged();
                        });
                        break;
                    case REPLY_REQUEST:
                        Notification request = toMeRequestList.get(getAdapterPosition());
                        Friendship.acceptRequest(request, object -> {
                            presenter.updateFriendship(getAdapterPosition(),
                                    (Friendship) object);
                            notifyDataSetChanged();
                        });
                        break;
                    default:
                        break;
                }
            });
        }

        public void bind(int position) {
            if (userList != null && userList.size() > 0) {
                relationType = getRelationType(getAdapterPosition());
                User user = userList.get(position);
                String sport = sportList.get(position);

                if (user != null) {
                    // name
                    friendName.setText(user.getName());

                    // image
                    if (user.getPhotoURL() != null && !user.getPhotoURL().isEmpty()) {
                        Picasso.with(context).load(user.getPhotoURL()).into(friendImage);
                    } else {
                        Picasso.with(context).load(R.drawable.user_placeholder).into(friendImage);
                    }

                    // info
                    String info = String.format("%s anni",
                            String.valueOf(TimeUtils.getAgeFromBirth(user.getBirthDate())));
                    if (sport != null && !sport.isEmpty()) {
                        info += " | " + sport;
                    }
                    friendInfo.setText(info);

                    // friendship date
                    switch (relationType) {
                        case FRIENDS:
                            friendshipDate.setText(new StringBuilder()
                                    .append("Amici dal ")
                                    .append(TimeUtils.fromMillistoYearMonthDay(friendship_date)));
                            friendshipDate.setVisibility(View.VISIBLE);

                            friendshipRequestButton.setVisibility(View.GONE);
                            break;
                        case WAITING_FOR_RESPONSE:
                            friendshipRequestButton.setText("Richiesta\neffettuata");
                            friendshipRequestButton.setVisibility(View.VISIBLE);
                            friendshipRequestButton.setActivated(false);

                            friendshipDate.setVisibility(View.GONE);
                            break;
                        case REPLY_REQUEST:
                            friendshipRequestButton.setText("Rispondi alla\nrichiesta");
                            friendshipRequestButton.setVisibility(View.VISIBLE);
                            friendshipRequestButton.setActivated(true);

                            friendshipDate.setVisibility(View.GONE);
                            break;
                        default:
                            friendshipRequestButton.setText("Chiedi\namicizia");
                            friendshipRequestButton.setVisibility(View.VISIBLE);
                            friendshipRequestButton.setActivated(true);

                            friendshipDate.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        }

        private UserListPresenter.RelationType getRelationType(int position) {
            if (position >= 0) {
                Friendship friendship = friendshipList != null && position < friendshipList.size() ?
                        friendshipList.get(position) : null;

                Notification friendshipRequestNotification =
                        myRequestList != null && position < myRequestList.size() ?
                                myRequestList.get(position) : null;

                Notification toMeFriendshipRequestNotification =
                        toMeRequestList != null && position < toMeRequestList.size() ?
                                toMeRequestList.get(position) : null;

                if (friendship != null) {
                    friendship_date = friendship.getDate();
                    return UserListPresenter.RelationType.FRIENDS;
                } else if (friendshipRequestNotification != null) {
                    return UserListPresenter.RelationType.WAITING_FOR_RESPONSE;
                } else if (toMeFriendshipRequestNotification != null) {
                    return UserListPresenter.RelationType.REPLY_REQUEST;
                } else {
                    return UserListPresenter.RelationType.NO_RELATION;
                }
            }

            return UserListPresenter.RelationType.NO_RELATION;
        }
    }
}
