package com.example.david.ermes.View.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.MainAdapter;

import java.util.Calendar;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 */

public class HomeFragment extends Fragment {

    private MainAdapter adapter;
    private RecyclerView recyclerView;
    private Match match;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            match = (Match) args.getSerializable("event");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler(view);
    }

    private void initRecycler(final View rootView) {

        //inizializzo l'adapter e indirizzo tutti gli elementi
        adapter = new MainAdapter(getContext());
        recyclerView = rootView.findViewById(R.id.main_contenitore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        initList();
    }

    public void initList() {
        MatchRepository.getInstance().fetchOrderedMatchesByDate(Calendar.getInstance().getTimeInMillis(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                adapter.refreshList((List<Match>) object);
            }
        });
    }
}
