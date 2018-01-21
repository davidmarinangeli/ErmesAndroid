package com.example.david.ermes.View;

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


    public void addFragment(android.support.v4.app.Fragment fragment) {
        this.fragments.add(fragment);
    }

    public void replaceFragment(int position, Fragment fragment) {
        if (fragments != null && position < fragments.size()) {
            this.fragments.set(position, fragment);
            notifyDataSetChanged();
        }
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
