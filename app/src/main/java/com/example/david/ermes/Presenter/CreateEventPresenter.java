package com.example.david.ermes.Presenter;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.View.activities.CreateEventActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/3/2018.
 */

public class CreateEventPresenter {

    private CreateEventActivity createEventActivity;

    public CreateEventPresenter(CreateEventActivity createEventActivity){
        this.createEventActivity = createEventActivity;
    }

   public void saveMatch(final long timeInMillis, final String sport){

       UserRepository.getInstance().getUser(new FirebaseCallback() {
           @Override
           public void callback(Object object) {
               if (object != null) {
                   final User current_user = (User) object;

                   final Location location = new Location("Alessandro Volta", 46.0490089, 11.123597, current_user.getUID());

                  SportRepository.getInstance().fetchSportByName(sport, new FirebaseCallback(){
                      @Override
                      public void callback(Object object) {
                          if (object != null){
                              Sport found_sport = (Sport) object;

                              List<String> missingstuff = new ArrayList<>();
                              missingstuff.add("rete");
                              missingstuff.add("pallone");

                              //qui mettere il comportamento alla creazione del match
                              Match result_match = new Match(
                                      current_user.getUID(),
                                      location.getId(),
                                      TimeUtils.fromMillisToDate(timeInMillis),
                                      true,
                                      found_sport.getID(),
                                      found_sport.getNumPlayers(),
                                      2,
                                      missingstuff
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
