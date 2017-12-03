package com.example.david.ermes.Model.repository;


import com.example.david.ermes.Model.db.DatabaseManager.OnDataChangedListener;
import com.example.david.ermes.Model.db.UserDatabaseRepository;
import com.example.david.ermes.Model.models.User;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();

    public static UserRepository getInstance() {
        return instance;
    }
    private User user;

    private UserRepository() {

        this.user = UserDatabaseRepository.getInstance().getCurrentUser();

        UserDatabaseRepository.getInstance().setUserDataChangedListener(new OnDataChangedListener<User>() {

            @Override public void onDataChanged(User data) {
                UserRepository.this.user = data;
            }
        });
    }

    public User getUser() {
        return user;
    }
}
