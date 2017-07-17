package com.example.david.ermes.View.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.ermes.Presenter.Match;
import com.example.david.ermes.R;

/**
 * Created by David on 16/07/2017.
 */

public class EventFragment extends Fragment {

    private TextView sportname;
    private TextView dateofevent;
    private TextView placeofevent;
    private Match match;

    public EventFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        match = (Match) args.getSerializable("event");
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

        sportname = (TextView) view.findViewById(R.id.sport_name);
        dateofevent = (TextView) view.findViewById(R.id.when_text_calendar);
        placeofevent = (TextView) view.findViewById(R.id.where_text);
        sportname.setText(match.getSport());
        dateofevent.setText(match.getDate());
        placeofevent.setText(match.getPlace());
    }


}
