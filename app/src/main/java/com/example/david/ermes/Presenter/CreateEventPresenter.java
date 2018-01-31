package com.example.david.ermes.Presenter;

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
import java.util.List;

/**
 * Created by david on 1/3/2018.
 */

public class CreateEventPresenter {

    private CreateEventActivity createEventActivity;

    public CreateEventPresenter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
    }

    public void getMatch(final long timeInMillis,
                          final String sport,
                          final Location selected_location,
                          final ArrayList<MissingStuffElement> chips_title_list,
                          final boolean checked,
                          String maxplayers, FirebaseCallback firebaseCallback) {

        UserRepository.getInstance().getUser(object -> {
            if (object != null) {
                final User current_user = (User) object;

                SportRepository.getInstance().fetchSportByName(sport, object1 -> {
                    if (object1 != null) {
                        Sport found_sport = (Sport) object1;

                        int final_max_players;
                        if (Integer.valueOf(maxplayers)>0){
                            final_max_players = Integer.valueOf(maxplayers);
                        } else {
                            final_max_players = found_sport.getNumPlayers();
                        }

                        //qui mettere il comportamento alla creazione del match
                        Match result_match = new Match(
                                current_user.getUID(),
                                selected_location.getId(),
                                com.example.david.ermes.Presenter.utils.TimeUtils.fromMillisToDate(timeInMillis),
                                checked,
                                found_sport.getID(),
                                final_max_players,
                                0,
                                chips_title_list,
                                new ArrayList<String>(),
                                new ArrayList<String>()
                        );


                        result_match.addPartecipant(current_user.getUID());

                        if (firebaseCallback != null) {
                            firebaseCallback.callback(result_match);
                        }
                    }
                });
            }
        });
    }
}
