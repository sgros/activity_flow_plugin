// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_day;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_week;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_month;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter
{
    Bundle data;
    int mNumOfTabs;
    
    public PagerAdapter(final FragmentManager fragmentManager, final int mNumOfTabs, final String s) {
        super(fragmentManager);
        this.mNumOfTabs = mNumOfTabs;
        (this.data = new Bundle()).putString("AppName", s);
    }
    
    @Override
    public int getCount() {
        return this.mNumOfTabs;
    }
    
    @Override
    public Fragment getItem(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 2: {
                final Fragment_month fragment_month = new Fragment_month();
                fragment_month.setArguments(this.data);
                return fragment_month;
            }
            case 1: {
                final Fragment_week fragment_week = new Fragment_week();
                fragment_week.setArguments(this.data);
                return fragment_week;
            }
            case 0: {
                final Fragment_day fragment_day = new Fragment_day();
                fragment_day.setArguments(this.data);
                return fragment_day;
            }
        }
    }
}
