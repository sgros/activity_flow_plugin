package org.secuso.privacyfriendlynetmonitor.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.PagerAdapter;

public class AppConnections_Chart extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setTitle(this.getIntent().getStringExtra("AppName"));
      String var2 = this.getIntent().getStringExtra("AppSubName");
      this.setContentView(2131427372);
      TabLayout var4 = (TabLayout)this.findViewById(2131296504);
      var4.addTab(var4.newTab().setText("Day"));
      var4.addTab(var4.newTab().setText("Week"));
      var4.addTab(var4.newTab().setText("Month"));
      var4.setTabGravity(0);
      final ViewPager var3 = (ViewPager)this.findViewById(2131296422);
      var3.setAdapter(new PagerAdapter(this.getSupportFragmentManager(), var4.getTabCount(), var2));
      var3.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(var4));
      var4.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
         public void onTabReselected(TabLayout.Tab var1) {
         }

         public void onTabSelected(TabLayout.Tab var1) {
            var3.setCurrentItem(var1.getPosition());
         }

         public void onTabUnselected(TabLayout.Tab var1) {
         }
      });
   }
}
