package com.example.david.ermes.Presenter;

import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.User;

import java.util.List;

/**
 * Created by nicol on 20/01/2018.
 */

public interface OnUserListFetchEnd {
    public void end(List<User> userList, List<Friendship> friendshipList,
                    List<Notification> myFriendshipRequests,
                    List<Notification> toMeFriendshipRequests, List<String> sportList);
}
