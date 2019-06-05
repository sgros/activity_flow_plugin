package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.os.Bundle;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager;
import android.support.p000v4.app.FragmentStatePagerAdapter;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_day;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_month;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_week;

public class PagerAdapter extends FragmentStatePagerAdapter {
    Bundle data = new Bundle();
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int i, String str) {
        super(fragmentManager);
        this.mNumOfTabs = i;
        this.data.putString("AppName", str);
    }

    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Fragment_day fragment_day = new Fragment_day();
                fragment_day.setArguments(this.data);
                return fragment_day;
            case 1:
                Fragment_week fragment_week = new Fragment_week();
                fragment_week.setArguments(this.data);
                return fragment_week;
            case 2:
                Fragment_month fragment_month = new Fragment_month();
                fragment_month.setArguments(this.data);
                return fragment_month;
            default:
                return null;
        }
    }

    public int getCount() {
        return this.mNumOfTabs;
    }
}
