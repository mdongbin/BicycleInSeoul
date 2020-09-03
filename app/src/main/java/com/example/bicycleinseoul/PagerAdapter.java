package com.example.bicycleinseoul;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabIndex;

    public PagerAdapter(@NonNull FragmentManager fm, int tabIndex) {
        super(fm, tabIndex);
        this.tabIndex = tabIndex;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: {

                    return MapFragment.initMapFragment();

            }
            case 1: {
                return new AlarmFragment();
            }
            case 2: {
                return new FavoriteFragment();
            }
            case 3: {
                return new OtherFragment();
            }

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabIndex;
    }
}
