package com.gilbertopapa.chatstreamcode.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gilbertopapa.chatstreamcode.fragment.ContactFragment;
import com.gilbertopapa.chatstreamcode.fragment.TalksFragment;

/**
 * Created by GilbertoPapa on 16/07/2018.
 */

public class TabAdapter extends FragmentStatePagerAdapter{
    private String[] titleAbs = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new TalksFragment();
                break;
            case 1:
                fragment = new ContactFragment();
                break;
        }

        return fragment;

    }

    @Override
    public int getCount() {
        return titleAbs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleAbs[position];
    }
}
