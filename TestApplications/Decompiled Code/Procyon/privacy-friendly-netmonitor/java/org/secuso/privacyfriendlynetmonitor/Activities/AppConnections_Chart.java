// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AppConnections_Chart extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setTitle((CharSequence)this.getIntent().getStringExtra("AppName"));
        final String stringExtra = this.getIntent().getStringExtra("AppSubName");
        this.setContentView(2131427372);
        final TabLayout tabLayout = this.findViewById(2131296504);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.setTabGravity(0);
        final ViewPager viewPager = this.findViewById(2131296422);
        viewPager.setAdapter(new PagerAdapter(this.getSupportFragmentManager(), tabLayout.getTabCount(), stringExtra));
        viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener((TabLayout.OnTabSelectedListener)new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(final Tab tab) {
            }
            
            @Override
            public void onTabSelected(final Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            
            @Override
            public void onTabUnselected(final Tab tab) {
            }
        });
    }
}
