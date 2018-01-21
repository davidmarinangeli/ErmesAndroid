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

    public static NotificationRepository getInstance() {
        return instance;
    }

    public NotificationRepository() {
    }

    public void fetchNotificationsByIdCreator(String idCreator, FirebaseCallback firebaseCallback) {
        NotificationDatabaseRepository.getInstance().fetchByParam("idCreator", idCreator,
                object -> {
                    List<_Notification> list = (List<_Notification>) object;
                    List<Notification> result = null;

                    if (list != null) {
                        result = _Notification.convertToNotificationList(list);
                    }

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(result);
                    }
                });
    }

    public void fetchUnreadFriendshipRequestsByIdCreator(String idCreator,
                                                         FirebaseCallback firebaseCallback) {
        fetchNotificationsByIdCreator(idCreator, object -> {
            List<Notification> result = (List<Notification>) object;

            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    Notification n = result.get(i);

                    if (n.isRead()) {
                        result.remove(i);
                        i--;
                    }
                }
            }

            if (firebaseCallback != null) {
                firebaseCallback.callback(result);
            }
        });
    }

    public void fetchUnreadFriendshipRequestsByIdOwner(String idOwner,
                                                       FirebaseCallback firebaseCallback) {
        fetchNotificationsByIdOwner(idOwner, object -> {
            List<Notification> result = (List<Notification>) object;

            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    Notification n = result.get(i);

                    if (n.isRead()) {
                        result.remove(i);
                        i--;
                    }
                }
            }

            if (firebaseCallback != null) {
                firebaseCallback.callback(result);
            }
        });
    }

    public void fetchNotificationsByIdOwner(String idOwner, final FirebaseCallback firebaseCallback) {
        NotificationDatabaseRepository.getInstance().fetchByParam("idOwner", idOwner,
                object -> {
                    if (object != null) {
                        List<_Notification> list = (List<_Notification>) object;

                        firebaseCallback.callback(_Notification.convertToNotificationList(list));
                    } else {
                        firebaseCallback.callback(null);
                    }
                });
    }

    public void fetchUnreadFriendshipRequestByIdCreatorAndIdOwner(String idCreator, String idOwner,
                                                                  FirebaseCallback firebaseCallback) {
        NotificationDatabaseRepository.getInstance().fetchUnreadByIdCreatorAndIdOwner(idCreator,
                idOwner, object -> {
                    _Notification n = (_Notification) object;
                    Notification result = null;

                    if (n != null) {
                        result = n.convertToNotification();
                    }

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(result);
                    }
                });
    }

    public void sendNotification(Notification notification, FirebaseCallback firebaseCallback) {
        NotificationDatabaseRepository.getInstance().push(notification.convertTo_Notification(),
                object -> {
                    if (object != null) {
                        notification.setId((String) object);
                    }

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(notification);
                    }
                });
    }
}
