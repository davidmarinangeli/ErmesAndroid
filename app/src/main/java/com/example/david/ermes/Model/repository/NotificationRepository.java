package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.NotificationDatabaseRepository;
import com.example.david.ermes.Model.db.DbModels._Notification;
import com.example.david.ermes.Model.models.Notification;

import java.util.List;

/**
 * Created by nicol on 09/01/2018.
 */

public class NotificationRepository {
    private static NotificationRepository instance = new NotificationRepository();
    public static NotificationRepository getInstance() { return instance; }

    public NotificationRepository() {}

    public void fetchNotificationsByIdOwner(String idOwner, final FirebaseCallback firebaseCallback) {
        NotificationDatabaseRepository.getInstance().fetchByIdOwner(idOwner,
                new FirebaseCallback() {
                    @Override
                    public void callback(Object object) {
                        if (object != null) {
                            List<_Notification> list = (List<_Notification>) object;

                            firebaseCallback.callback(_Notification.convertToNotificationList(list));
                        } else {
                            firebaseCallback.callback(null);
                        }
                    }
                });
    }

    public void sendNotification(Notification notification, FirebaseCallback firebaseCallback) {
        NotificationDatabaseRepository.getInstance().push(notification.convertTo_Notification(),
                firebaseCallback);
    }
}
