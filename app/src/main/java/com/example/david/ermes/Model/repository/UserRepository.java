package com.example.david.ermes.Model.repository;


import com.example.david.ermes.Model.db.DatabaseManager.OnDataChangedListener;
import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.UserDatabaseRepository;
import com.example.david.ermes.Model.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();

    public static UserRepository getInstance() {
        return instance;
    }

    private UserRepository() {

//        UserDatabaseRepository.getInstance().setUserDataChangedListener(new OnDataChangedListener<User>() {
//
//            @Override public void onDataChanged(User data) {
//                UserRepository.this.user = data;
//            }
//        });
    }

    public void getUser(final FirebaseCallback firebaseCallback) {
        UserDatabaseRepository.getInstance().getCurrentUser(firebaseCallback);
    }

    public void saveUser(User user) {
        UserDatabaseRepository.getInstance().save(user);
    }

    public void fetchUserById(String id, final FirebaseCallback firebaseCallback) {
        UserDatabaseRepository.getInstance().fetchUserById(id, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                User user = null;
                if (object != null) {
                    user = ((DbModels._User) object).convertToUser();
                }

                firebaseCallback.callback(user);
            }
        });
    }
}
