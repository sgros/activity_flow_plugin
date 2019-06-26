package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   static final int MAIN_CONTENT_FADEIN_DURATION = 250;
   static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
   static final int NAVDRAWER_LAUNCH_DELAY = 250;
   DrawerLayout mDrawerLayout;
   private Handler mHandler;
   NavigationView mNavigationView;
   protected SharedPreferences mSharedPreferences;

   private void callDrawerItem(int var1) {
      if (var1 != 2131296404) {
         Intent var2;
         switch(var1) {
         case 2131296407:
            this.createBackStack(new Intent(this, HelpActivity.class));
            break;
         case 2131296408:
            this.createBackStack(new Intent(this, HistoryActivity.class));
            break;
         case 2131296409:
            var2 = new Intent(this, MainActivity.class);
            var2.setFlags(67108864);
            this.startActivity(var2);
            break;
         case 2131296410:
            var2 = new Intent(this, SettingsActivity.class);
            var2.putExtra(":android:show_fragment", SettingsActivity.GeneralPreferenceFragment.class.getName());
            var2.putExtra(":android:no_headers", true);
            this.createBackStack(var2);
            break;
         case 2131296411:
            TutorialActivity.setTutorial_click(true);
            this.createBackStack(new Intent(this, TutorialActivity.class));
         }
      } else {
         this.createBackStack(new Intent(this, AboutActivity.class));
      }

   }

   private void createBackStack(Intent var1) {
      if (VERSION.SDK_INT >= 16) {
         TaskStackBuilder var2 = TaskStackBuilder.create(this);
         var2.addNextIntentWithParentStack(var1);
         var2.startActivities();
      } else {
         this.startActivity(var1);
         this.finish();
      }

   }

   protected int getNavigationDrawerID() {
      return 0;
   }

   protected boolean goToNavigationItem(final int var1) {
      if (var1 == this.getNavigationDrawerID()) {
         this.mDrawerLayout.closeDrawer(8388611);
         return true;
      } else {
         this.mHandler.postDelayed(new Runnable() {
            public void run() {
               BaseActivity.this.callDrawerItem(var1);
            }
         }, 250L);
         this.mDrawerLayout.closeDrawer(8388611);
         this.selectNavigationItem(var1);
         View var2 = this.findViewById(2131296393);
         if (var2 != null) {
            var2.animate().alpha(0.0F).setDuration(150L);
         }

         return true;
      }
   }

   public void onBackPressed() {
      DrawerLayout var1 = (DrawerLayout)this.findViewById(2131296336);
      if (var1.isDrawerOpen(8388611)) {
         var1.closeDrawer(8388611);
      } else {
         super.onBackPressed();
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      this.mHandler = new Handler();
      this.overridePendingTransition(0, 0);
   }

   public boolean onNavigationItemSelected(MenuItem var1) {
      return this.goToNavigationItem(var1.getItemId());
   }

   protected void onPostCreate(Bundle var1) {
      super.onPostCreate(var1);
      this.setToolbar();
   }

   void selectNavigationItem(int var1) {
      for(int var2 = 0; var2 < this.mNavigationView.getMenu().size(); ++var2) {
         boolean var3;
         if (var1 == this.mNavigationView.getMenu().getItem(var2).getItemId()) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mNavigationView.getMenu().getItem(var2).setChecked(var3);
      }

   }

   public void setToolbar() {
      Toolbar var1 = (Toolbar)this.findViewById(2131296523);
      if (this.getSupportActionBar() == null) {
         this.setSupportActionBar(var1);
      }

      this.mDrawerLayout = (DrawerLayout)this.findViewById(2131296336);
      ActionBarDrawerToggle var2 = new ActionBarDrawerToggle(this, this.mDrawerLayout, var1, 2131624030, 2131624029);
      this.mDrawerLayout.addDrawerListener(var2);
      var2.syncState();
      this.mNavigationView = (NavigationView)this.findViewById(2131296412);
      this.mNavigationView.setNavigationItemSelectedListener(this);
      this.selectNavigationItem(this.getNavigationDrawerID());
      View var3 = this.findViewById(2131296393);
      if (var3 != null) {
         var3.setAlpha(0.0F);
         var3.animate().alpha(1.0F).setDuration(250L);
      }

   }
}
