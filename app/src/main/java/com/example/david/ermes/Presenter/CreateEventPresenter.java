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

    public void saveMatch(final long timeInMillis, final String sport, final Location selected_location, final ArrayList<MissingStuffElement> chips_title_list) {

        UserRepository.getInstance().getUser(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    final User current_user = (User) object;

                    SportRepository.getInstance().fetchSportByName(sport, new FirebaseCallback() {
                        @Override
                        public void callback(Object object) {
                            if (object != null) {
                                Sport found_sport = (Sport) object;

                                //qui mettere il comportamento alla creazione del match
                                Match result_match = new Match(
                                        current_user.getUID(),
                                        selected_location.getId(),
                                        com.example.david.ermes.Presenter.utils.TimeUtils.fromMillisToDate(timeInMillis),
                                        true,
                                        found_sport.getID(),
                                        found_sport.getNumPlayers(),
                                        2,
                                        chips_title_list,
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
