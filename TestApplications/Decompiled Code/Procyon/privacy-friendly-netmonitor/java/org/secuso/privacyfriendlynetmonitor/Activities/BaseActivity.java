// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.app.Activity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.support.v4.app.TaskStackBuilder;
import android.os.Build$VERSION;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity implements OnNavigationItemSelectedListener
{
    static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    static final int NAVDRAWER_LAUNCH_DELAY = 250;
    DrawerLayout mDrawerLayout;
    private Handler mHandler;
    NavigationView mNavigationView;
    protected SharedPreferences mSharedPreferences;
    
    private void callDrawerItem(final int n) {
        if (n != 2131296404) {
            switch (n) {
                case 2131296411: {
                    TutorialActivity.setTutorial_click(true);
                    this.createBackStack(new Intent((Context)this, (Class)TutorialActivity.class));
                    break;
                }
                case 2131296410: {
                    final Intent intent = new Intent((Context)this, (Class)SettingsActivity.class);
                    intent.putExtra(":android:show_fragment", SettingsActivity.GeneralPreferenceFragment.class.getName());
                    intent.putExtra(":android:no_headers", true);
                    this.createBackStack(intent);
                    break;
                }
                case 2131296409: {
                    final Intent intent2 = new Intent((Context)this, (Class)MainActivity.class);
                    intent2.setFlags(67108864);
                    this.startActivity(intent2);
                    break;
                }
                case 2131296408: {
                    this.createBackStack(new Intent((Context)this, (Class)HistoryActivity.class));
                    break;
                }
                case 2131296407: {
                    this.createBackStack(new Intent((Context)this, (Class)HelpActivity.class));
                    break;
                }
            }
        }
        else {
            this.createBackStack(new Intent((Context)this, (Class)AboutActivity.class));
        }
    }
    
    private void createBackStack(final Intent intent) {
        if (Build$VERSION.SDK_INT >= 16) {
            final TaskStackBuilder create = TaskStackBuilder.create((Context)this);
            create.addNextIntentWithParentStack(intent);
            create.startActivities();
        }
        else {
            this.startActivity(intent);
            this.finish();
        }
    }
    
    protected int getNavigationDrawerID() {
        return 0;
    }
    
    protected boolean goToNavigationItem(final int n) {
        if (n == this.getNavigationDrawerID()) {
            this.mDrawerLayout.closeDrawer(8388611);
            return true;
        }
        this.mHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                BaseActivity.this.callDrawerItem(n);
            }
        }, 250L);
        this.mDrawerLayout.closeDrawer(8388611);
        this.selectNavigationItem(n);
        final View viewById = this.findViewById(2131296393);
        if (viewById != null) {
            viewById.animate().alpha(0.0f).setDuration(150L);
        }
        return true;
    }
    
    @Override
    public void onBackPressed() {
        final DrawerLayout drawerLayout = this.findViewById(2131296336);
        if (drawerLayout.isDrawerOpen(8388611)) {
            drawerLayout.closeDrawer(8388611);
        }
        else {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
        this.mHandler = new Handler();
        this.overridePendingTransition(0, 0);
    }
    
    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        return this.goToNavigationItem(menuItem.getItemId());
    }
    
    @Override
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);
        this.setToolbar();
    }
    
    void selectNavigationItem(final int n) {
        for (int i = 0; i < this.mNavigationView.getMenu().size(); ++i) {
            this.mNavigationView.getMenu().getItem(i).setChecked(n == this.mNavigationView.getMenu().getItem(i).getItemId());
        }
    }
    
    public void setToolbar() {
        final Toolbar supportActionBar = this.findViewById(2131296523);
        if (this.getSupportActionBar() == null) {
            this.setSupportActionBar(supportActionBar);
        }
        this.mDrawerLayout = this.findViewById(2131296336);
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, supportActionBar, 2131624030, 2131624029);
        this.mDrawerLayout.addDrawerListener((DrawerLayout.DrawerListener)actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        (this.mNavigationView = this.findViewById(2131296412)).setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener)this);
        this.selectNavigationItem(this.getNavigationDrawerID());
        final View viewById = this.findViewById(2131296393);
        if (viewById != null) {
            viewById.setAlpha(0.0f);
            viewById.animate().alpha(1.0f).setDuration(250L);
        }
    }
}
