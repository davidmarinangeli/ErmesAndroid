package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.FriendshipDatabaseRepository;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.db.DbModels._Friendship;

import java.util.List;

/**
 * Created by nicol on 12/01/2018.
 */

public class FriendshipRepository {
    private static FriendshipRepository instance = new FriendshipRepository();
    public static FriendshipRepository getInstance() { return instance; }

    public FriendshipRepository() {}

    public void saveFriendship(Friendship friendship) {
        FriendshipDatabaseRepository.getInstance().push(friendship.convertTo_Friendship());
    }

    public void fetchFriendshipsByUserId(String id, final FirebaseCallback firebaseCallback) {
        FriendshipDatabaseRepository.getInstance().fetchListById(id,
                new FirebaseCallback() {
                    @Override
                    public void callback(Object object) {
                        List<Friendship> list = null;

                        if (object != null) {
                            list = _Friendship.convertToFriendshipList((List<_Friendship>) object);
                        }

                        firebaseCallback.callback(list);
                    }
                });
    }
}
