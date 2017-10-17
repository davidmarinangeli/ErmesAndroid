package com.example.david.ermes.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by David on 10/10/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    ArrayList<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public void addFragments(android.support.v4.app.Fragment fragments) {
        this.fragments.add(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
