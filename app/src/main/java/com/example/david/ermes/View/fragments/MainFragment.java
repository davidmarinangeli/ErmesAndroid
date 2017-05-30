package com.example.david.ermes.View.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.ermes.Presenter.MainAdapter;
import com.example.david.ermes.R;

/**
 * Created by David on 30/05/2017.
 */

public class MainFragment extends Fragment {

    private MainAdapter adapter;
    private RecyclerView recyclerView;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_recyclerview_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler(view);
    }

    private void initRecycler(final View rootView) {

        //inizializzo l'adapter e indirizzo tutti gli elementi
        adapter = new MainAdapter(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_of_event_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.initList();
    }
}
