package org.secuso.privacyfriendlynetmonitor.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.p000v4.view.ViewPager;
import android.support.p003v7.app.AppCompatActivity;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.PagerAdapter;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class AppConnections_Chart extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle(getIntent().getStringExtra("AppName"));
        String stringExtra = getIntent().getStringExtra("AppSubName");
        setContentView((int) C0501R.layout.app_report_detail_layout);
        TabLayout tabLayout = (TabLayout) findViewById(C0501R.C0499id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText((CharSequence) "Day"));
        tabLayout.addTab(tabLayout.newTab().setText((CharSequence) "Week"));
        tabLayout.addTab(tabLayout.newTab().setText((CharSequence) "Month"));
        tabLayout.setTabGravity(0);
        final ViewPager viewPager = (ViewPager) findViewById(C0501R.C0499id.pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), stringExtra));
        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabReselected(Tab tab) {
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }
}
