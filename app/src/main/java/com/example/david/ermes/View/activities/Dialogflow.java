package com.example.david.ermes.View.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.R;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class Dialogflow implements AIListener {

    public final static String CLIENT_ACCESS_TOKEN = "67ea85acb1e34baba537dfd93dfde07a";
    public final static String SPORT_KEY = "Sport";
    public final static String DATE_KEY = "date";
    public final static String PLAYERS_NUMBER_KEY = "number";
    public final static String TIME_KEY = "time";
    public final static String VISIBILITY_KEY = "Visibility";
    private AIService aiService;
    private Activity activity;

    private FirebaseCallback firebaseCallback;

    public Dialogflow(Activity activity) {

        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.Italian,
                AIConfiguration.RecognitionEngine.System);


        aiService = AIService.getService(activity, config);
        aiService.setListener(this);
        this.activity = activity;
    }

    public void startListening() {
        aiService.startListening();

    }

    public void setOnFinishListening(FirebaseCallback firebaseCallback) {
        this.firebaseCallback = firebaseCallback;

    }


    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        Calendar cal_date = Calendar.getInstance();
        cal_date.clear();
        Calendar cal_time = Calendar.getInstance();
        cal_time.clear();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        String results = "";
        Match match = new Match();
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                results += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                switch (entry.getKey()) {
                    case SPORT_KEY:
                        break;
                    case PLAYERS_NUMBER_KEY:
                        match.setMaxPlayers(entry.getValue().getAsInt());
                        break;
                    case DATE_KEY:
                        try {
                            Date month_day = date.parse(entry.getValue().getAsString());
                            cal_date.setTime(month_day);
                            if (cal_time.get(Calendar.HOUR) != 0) {
                                cal_date.set(cal_date.get(Calendar.YEAR),
                                        cal_date.get(Calendar.MONTH),
                                        cal_date.get(Calendar.DAY_OF_MONTH),
                                        cal_time.get(Calendar.HOUR_OF_DAY),
                                        cal_time.get(Calendar.MINUTE));
                                match.setDate(cal_date.getTime());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case TIME_KEY:
                        try {
                            Date hour = time.parse(entry.getValue().getAsString());
                            cal_time.setTime(hour);
                            if (cal_date.get(Calendar.DAY_OF_MONTH) != 0) {
                                cal_time.set(cal_date.get(Calendar.YEAR),
                                        cal_date.get(Calendar.MONTH),
                                        cal_date.get(Calendar.DAY_OF_MONTH),
                                        cal_time.get(Calendar.HOUR_OF_DAY),
                                        cal_time.get(Calendar.MINUTE));
                                match.setDate(cal_time.getTime());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case VISIBILITY_KEY:
                        break;
                    default:
                        break;

                }
            }
        }
        Toast.makeText(activity.getBaseContext(),results,Toast.LENGTH_LONG).show();

        if (result.getParameters().get("Sport") != null) {
            String sport = result.getParameters().get("Sport").getAsString();
            SportRepository.getInstance().fetchSportByName(sport, object -> {
                Sport s = (Sport) object;
                match.setIdSport(s.getID());
                firebaseCallback.callback(match);
            });
        }

    }

    @Override
    public void onError(final AIError error) {
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        Toast.makeText(activity.getBaseContext(), "Sto ascoltando", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
