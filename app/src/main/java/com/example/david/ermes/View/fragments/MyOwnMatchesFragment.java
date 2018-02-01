package com.example.david.ermes.View.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.R;
import com.example.david.ermes.View.MainAdapter;
import com.example.david.ermes.View.ProgressDialog;

import java.util.List;

/**
 * Created by nicol on 31/01/2018.
 */

public class MyOwnMatchesFragment extends Fragment {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private User currentUser;

    private TextView no_matches_label;

    public MyOwnMatchesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_own_matches, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        no_matches_label = view.findViewById(R.id.own_no_matches_label);

        adapter = new MainAdapter(getContext());
        recyclerView = view.findViewById(R.id.own_matches_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        init();
    }

    public void setUser(User user) {
        currentUser = user;
    }

    private void init() {
        if (currentUser != null) {
            progressDialog.show();
            no_matches_label.setText("Nessuna partita");

            MatchRepository.getInstance().fetchMatchesByOwnerId(currentUser.getUID(),
                    object -> {
                        List<Match> matches = (List<Match>) object;

                        if (matches != null && !matches.isEmpty()) {
                            no_matches_label.setVisibility(View.GONE);

                            adapter.refreshList(matches);
                        } else {
                            no_matches_label.setVisibility(View.VISIBLE);
                        }

                        progressDialog.dismiss();
                    });
        } else {
            no_matches_label.setText("Nessun utente");
            no_matches_label.setVisibility(View.VISIBLE);
        }
    }
}
