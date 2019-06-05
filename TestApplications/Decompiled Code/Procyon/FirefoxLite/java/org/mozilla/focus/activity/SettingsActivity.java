// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.activity;

import android.app.Fragment;
import org.mozilla.focus.settings.SettingsFragment;
import android.view.View;
import android.view.View$OnClickListener;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

public class SettingsActivity extends BaseActivity
{
    @Override
    public void applyLocale() {
        this.setTitle(2131755252);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131492897);
        final Toolbar supportActionBar = this.findViewById(2131296700);
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        supportActionBar.setNavigationOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SettingsActivity.this.finish();
            }
        });
        this.getFragmentManager().beginTransaction().replace(2131296374, (Fragment)new SettingsFragment()).commit();
        this.applyLocale();
    }
}
