package com.example.david.ermes.Presenter;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.FriendshipRepository;
import com.example.david.ermes.Model.repository.NotificationRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 19/01/2018.
 */

public class UserListPresenter {

    private boolean presenterReady;
    private void setPresenterReady(boolean presenterReady) {
        this.presenterReady = presenterReady;
    }
    private boolean isPresenterReady() {
        return presenterReady;
    }

    private List<FirebaseCallback> presenterNotReadyCallbackList;

    private List<Integer> user_info_count;
    private final int MAX_USER_INFO_COUNT = 4; // myReq, toMeReq, sport, friendship

    private void incrementUserInfoCount(int index) {
        user_info_count.set(index, user_info_count.get(index) + 1);

        if (user_info_count.get(index) == MAX_USER_INFO_COUNT) {
            notifyFetchEnd(index);
        }
    }

    private void resetUserInfoCount(int size) {
        if (user_info_count == null) {
            user_info_count = new ArrayList<>();
        }

        user_info_count.clear();
        users_temp.clear();
        myReqs_temp.clear();
        toMeReqs_temp.clear();
        sports_temp.clear();

        for (int i = 0; i < size; i++) {
            user_info_count.add(0);
            users_temp.add(null);
            myReqs_temp.add(null);
            toMeReqs_temp.add(null);
            sports_temp.add(null);
        }
    }

    private void resetFriendships_temp(int size) {
        if (friendships_temp == null) {
            friendships_temp = new ArrayList<>();
        }

        friendships_temp.clear();

        for (int i = 0; i < size; i++) {
            friendships_temp.add(null);
        }
    }

    private int getUserInfoCount(int index) {
        return user_info_count.get(index);
    }

    private static int fetch_count = 0;
    private int MAX_FETCH_COUNT = 0;

    private void setMaxFetchCount(int value) {
        MAX_FETCH_COUNT = value;
    }

    private void incrementFetchCount() {
        fetch_count++;
    }

    private void resetFetchCount() {
        fetch_count = 0;
    }

    private int getFetchCount() {
        return fetch_count;
    }

    private List<User> userList;
    private List<Friendship> friendshipList;
    private List<Notification> myRequests;
    private List<Notification> toMeRequests;
    private List<String> sports;

    private String currentID;

    private List<User> users_temp;
    private List<Friendship> friendships_temp;
    private List<Notification> myReqs_temp;
    private List<Notification> toMeReqs_temp;
    private List<String> sports_temp;

    private OnUserListFetchEnd onUserListFetchEnd;

    public UserListPresenter() {
        currentID = User.getCurrentUserId();

        userList = new ArrayList<>();
        friendshipList = new ArrayList<>();
        myRequests = new ArrayList<>();
        toMeRequests = new ArrayList<>();
        sports = new ArrayList<>();

        users_temp = new ArrayList<>();
        myReqs_temp = new ArrayList<>();
        toMeReqs_temp = new ArrayList<>();
        sports_temp = new ArrayList<>();

        setPresenterReady(true);
        presenterNotReadyCallbackList = new ArrayList<>();
    }

    public void setOnUserListFetchEnd(OnUserListFetchEnd onUserListFetchEnd) {
        this.onUserListFetchEnd = onUserListFetchEnd;
    }

    public void prepareList(List<String> idList) {
        if (isPresenterReady()) {
            prepareUserList(idList);
        } else {
            presenterNotReadyCallbackList.add(object -> prepareUserList(idList));
        }
    }

    private void prepareUserList(List<String> idList) {
        setPresenterReady(false);

        clearLists();
        resetFetchCount();
        setMaxFetchCount(idList.size());
        resetUserInfoCount(idList.size());
        resetFriendships_temp(idList.size());

        for (int i = 0; i < idList.size(); i++) {
            final int CURRENT_INDEX = i;
            String userID = idList.get(CURRENT_INDEX);

            UserRepository.getInstance().fetchUserById(userID, object -> {
                User user = (User) object;

                users_temp.set(CURRENT_INDEX, user);

                FriendshipRepository.getInstance()
                        .fetchFriendshipByTwoIds(currentID, userID, object1 -> {
                            friendships_temp.set(CURRENT_INDEX, (Friendship) object1);
                            incrementUserInfoCount(CURRENT_INDEX);
                        });

                NotificationRepository.getInstance()
                        .fetchUnreadFriendshipRequestByIdCreatorAndIdOwner(
                                currentID, userID, object1 -> {
                                    myReqs_temp.set(CURRENT_INDEX, (Notification) object1);
                                    incrementUserInfoCount(CURRENT_INDEX);
                                });

                NotificationRepository.getInstance()
                        .fetchUnreadFriendshipRequestByIdCreatorAndIdOwner(
                                userID, currentID, object1 -> {
                                    toMeReqs_temp.set(CURRENT_INDEX, (Notification) object1);
                                    incrementUserInfoCount(CURRENT_INDEX);
                                });

                SportRepository.getInstance().fetchSportById(user.getIdFavSport(),
                        object1 -> {
                            String s = object1 != null ? ((Sport) object1).getName() : null;
                            sports_temp.set(CURRENT_INDEX, s);
                            incrementUserInfoCount(CURRENT_INDEX);
                        });
            });
        }
    }

    public void prepareFriendList() {
        if (isPresenterReady()) {
            prepareFriends();
        } else {
            presenterNotReadyCallbackList.add(object -> prepareFriends());
        }
    }

    private void prepareFriends() {
        setPresenterReady(false);

        clearLists();
        resetFetchCount();

        FriendshipRepository.getInstance().fetchFriendshipsByUserId(currentID, object -> {
            friendships_temp = (List<Friendship>) object;

            if (friendships_temp != null && !friendships_temp.isEmpty()) {
                setMaxFetchCount(friendships_temp.size());
                resetUserInfoCount(friendships_temp.size());

                for (int i = 0; i < friendships_temp.size(); i++) {
                    final int CURRENT_INDEX = i;
                    Friendship f = friendships_temp.get(CURRENT_INDEX);

                    String idUser = f.getId1().equals(currentID) ?
                            f.getId2() : f.getId1();

                    UserRepository.getInstance().fetchUserById(idUser, object1 -> {
                        User user = (User) object1;

                        users_temp.set(CURRENT_INDEX, user);

                        SportRepository.getInstance().fetchSportById(user.getIdFavSport(),
                                object2 -> {
                                    String s = object2 != null ? ((Sport) object2).getName() : null;
                                    sports_temp.set(CURRENT_INDEX, s);

                                    notifyFetchEnd(CURRENT_INDEX);
                                });
                    });
                }
            } else {
                onUserListFetchEnd.end(null, null, null, null,
                        null);
            }
        });
    }

    private void notifyFetchEnd(int index) {
        notifyFetchEnd(users_temp.get(index), friendships_temp.get(index),
                myReqs_temp.get(index), toMeReqs_temp.get(index), sports_temp.get(index));
    }

    private void notifyFetchEnd(User user, Friendship friendship, Notification myRequest,
                                Notification toMeRequest, String sport) {
        if (getFetchCount() == MAX_FETCH_COUNT) {
            resetFetchCount();
        }

        incrementFetchCount();

        userList.add(user);
        friendshipList.add(friendship);
        myRequests.add(myRequest);
        toMeRequests.add(toMeRequest);
        sports.add(sport);

        if (getFetchCount() == MAX_FETCH_COUNT && onUserListFetchEnd != null) {
            setPresenterReady(true);
            onUserListFetchEnd.end(userList, friendshipList, myRequests, toMeRequests, sports);

            if (presenterNotReadyCallbackList.size() > 0) {
                presenterNotReadyCallbackList.get(0).callback(null);
                presenterNotReadyCallbackList.remove(0);
            }
        }
    }

    private void clearLists() {
        userList.clear();
        friendshipList.clear();
        myRequests.clear();
        toMeRequests.clear();
        sports.clear();
    }
}
