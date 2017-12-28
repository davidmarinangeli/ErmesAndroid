package com.example.david.ermes.View.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;

import java.util.Calendar;

/**
 * Created by David on 16/07/2017.
 */

public class EventFragment extends Fragment {

    private TextView sportname;
    private TextView dateofevent;
    private TextView placeofevent;
    private TextView hourofevent;
    private TextView usercreator;
    private TextView tools;
    private Match match;

    public EventFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        match = args.getParcelable("event");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sportname = view.findViewById(R.id.sport_name);
        dateofevent = view.findViewById(R.id.when_text_calendar);
        placeofevent = view.findViewById(R.id.where_text);
        hourofevent = view.findViewById(R.id.when_hour_text_hour);
        usercreator = view.findViewById(R.id.userNameText);

        usercreator.setText("user");
        UserRepository.getInstance().fetchUserById(match.getIdOwner(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    User user = (User) object;
                    usercreator.setText(user.getName());
                }
            }
        });


        sportname.setText(match.getIdSport());
        Calendar c = Calendar.getInstance();
        c.setTime(match.getDate());

        // lo so che pare un macello sta stringa, giuro che correggerò le API
        dateofevent.setText(c.get(Calendar.DAY_OF_MONTH) +" "+ TimeUtils.fromNumericMonthToString(c.get(Calendar.MONTH)) );
        hourofevent.setText(String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
        //placeofevent.setText(match.getPlace());
    }


}
