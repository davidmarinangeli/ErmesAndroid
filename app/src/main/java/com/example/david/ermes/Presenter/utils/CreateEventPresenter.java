package com.example.david.ermes.Presenter.utils;

import android.support.annotation.NonNull;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.MissingStuffElement;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.View.activities.CreateEventActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by david on 1/3/2018.
 */

public class CreateEventPresenter {

    private CreateEventActivity createEventActivity;

    public CreateEventPresenter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
    }

    public void saveMatch(final long timeInMillis, final String sport) {

        UserRepository.getInstance().getUser(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    final User current_user = (User) object;

//                   final Location location = new Location("Alessandro Volta", 46.0490089, 11.123597, current_user.getUID());


                    SportRepository.getInstance().fetchSportByName(sport, new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            if (object != null) {
                                Sport found_sport = (Sport) object;

                                List<MissingStuffElement> missingstuff = new ArrayList<>();
                                missingstuff.add(new MissingStuffElement("rete", false));
                                missingstuff.add(new MissingStuffElement("pallone", false));

                                //qui mettere il comportamento alla creazione del match
                                Match result_match = new Match(
                                        current_user.getUID(),
                                        "-L06fg0QFeGxR1LfRurU",
                                        com.example.david.ermes.Presenter.utils.TimeUtils.fromMillisToDate(timeInMillis),
                                        true,
                                        found_sport.getID(),
                                        found_sport.getNumPlayers(),
                                        2,
                                        missingstuff,
                                        new ArrayList<String>(),
                                        new ArrayList<String>()
                                );

                                // result_match.setOwner(current_user);
                                result_match.save();

                                createEventActivity.goToMainActivity(result_match);
                            }
                        }
                    });
                }
            }
        });
    }
}
