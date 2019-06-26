package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.p000v4.app.TaskStackBuilder;
import android.support.p000v4.view.GravityCompat;
import android.support.p000v4.widget.DrawerLayout;
import android.support.p003v7.app.ActionBarDrawerToggle;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import org.secuso.privacyfriendlynetmonitor.Activities.SettingsActivity.GeneralPreferenceFragment;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class BaseActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    static final int NAVDRAWER_LAUNCH_DELAY = 250;
    DrawerLayout mDrawerLayout;
    private Handler mHandler;
    NavigationView mNavigationView;
    protected SharedPreferences mSharedPreferences;

    /* Access modifiers changed, original: protected */
    public int getNavigationDrawerID() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.mHandler = new Handler();
        overridePendingTransition(0, 0);
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(C0501R.C0499id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return goToNavigationItem(menuItem.getItemId());
    }

    /* Access modifiers changed, original: protected */
    public boolean goToNavigationItem(final int i) {
        if (i == getNavigationDrawerID()) {
            this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
            return true;
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                BaseActivity.this.callDrawerItem(i);
            }
        }, 250);
        this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        selectNavigationItem(i);
        View findViewById = findViewById(C0501R.C0499id.main_content);
        if (findViewById != null) {
            findViewById.animate().alpha(0.0f).setDuration(150);
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void selectNavigationItem(int i) {
        for (int i2 = 0; i2 < this.mNavigationView.getMenu().size(); i2++) {
            this.mNavigationView.getMenu().getItem(i2).setChecked(i == this.mNavigationView.getMenu().getItem(i2).getItemId());
        }
    }

    private void createBackStack(Intent intent) {
        if (VERSION.SDK_INT >= 16) {
            TaskStackBuilder create = TaskStackBuilder.create(this);
            create.addNextIntentWithParentStack(intent);
            create.startActivities();
            return;
        }
        startActivity(intent);
        finish();
    }

    private void callDrawerItem(int i) {
        if (i != C0501R.C0499id.nav_about) {
            Intent intent;
            switch (i) {
                case C0501R.C0499id.nav_help /*2131296407*/:
                    createBackStack(new Intent(this, HelpActivity.class));
                    return;
                case C0501R.C0499id.nav_history /*2131296408*/:
                    createBackStack(new Intent(this, HistoryActivity.class));
                    return;
                case C0501R.C0499id.nav_main /*2131296409*/:
                    intent = new Intent(this, MainActivity.class);
                    intent.setFlags(67108864);
                    startActivity(intent);
                    return;
                case C0501R.C0499id.nav_settings /*2131296410*/:
                    intent = new Intent(this, SettingsActivity.class);
                    intent.putExtra(":android:show_fragment", GeneralPreferenceFragment.class.getName());
                    intent.putExtra(":android:no_headers", true);
                    createBackStack(intent);
                    return;
                case C0501R.C0499id.nav_tutorial /*2131296411*/:
                    TutorialActivity.setTutorial_click(true);
                    createBackStack(new Intent(this, TutorialActivity.class));
                    return;
                default:
                    return;
            }
        }
        createBackStack(new Intent(this, AboutActivity.class));
    }

    /* Access modifiers changed, original: protected */
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        setToolbar();
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(C0501R.C0499id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
        this.mDrawerLayout = (DrawerLayout) findViewById(C0501R.C0499id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, toolbar, C0501R.string.navigation_drawer_open, C0501R.string.navigation_drawer_close);
        this.mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        this.mNavigationView = (NavigationView) findViewById(C0501R.C0499id.nav_view);
        this.mNavigationView.setNavigationItemSelectedListener(this);
        selectNavigationItem(getNavigationDrawerID());
        View findViewById = findViewById(C0501R.C0499id.main_content);
        if (findViewById != null) {
            findViewById.setAlpha(0.0f);
            findViewById.animate().alpha(1.0f).setDuration(250);
        }
    }
}
