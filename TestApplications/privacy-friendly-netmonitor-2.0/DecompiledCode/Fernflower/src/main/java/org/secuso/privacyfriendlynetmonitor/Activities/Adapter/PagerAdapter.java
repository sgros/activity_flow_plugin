package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_day;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_month;
import org.secuso.privacyfriendlynetmonitor.fragment.Fragment_week;

public class PagerAdapter extends FragmentStatePagerAdapter {
   Bundle data;
   int mNumOfTabs;

   public PagerAdapter(FragmentManager var1, int var2, String var3) {
      super(var1);
      this.mNumOfTabs = var2;
      this.data = new Bundle();
      this.data.putString("AppName", var3);
   }

   public int getCount() {
      return this.mNumOfTabs;
   }

   public Fragment getItem(int var1) {
      switch(var1) {
      case 0:
         Fragment_day var4 = new Fragment_day();
         var4.setArguments(this.data);
         return var4;
      case 1:
         Fragment_week var3 = new Fragment_week();
         var3.setArguments(this.data);
         return var3;
      case 2:
         Fragment_month var2 = new Fragment_month();
         var2.setArguments(this.data);
         return var2;
      default:
         return null;
      }
   }
}
