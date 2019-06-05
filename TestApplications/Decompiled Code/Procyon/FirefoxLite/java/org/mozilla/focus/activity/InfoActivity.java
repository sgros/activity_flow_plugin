// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.activity;

import android.view.View;
import android.view.View$OnClickListener;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import org.mozilla.focus.locale.Locales;
import android.content.Intent;
import android.content.Context;
import org.mozilla.focus.fragment.InfoFragment;

public class InfoActivity extends BaseActivity
{
    private InfoFragment infoFragment;
    
    public static final Intent getAboutIntent(final Context context) {
        return getIntentFor(context, "focusabout:", Locales.getLocalizedResources(context).getString(2131755247));
    }
    
    public static final Intent getIntentFor(final Context context, final String s, final String s2) {
        final Intent intent = new Intent(context, (Class)InfoActivity.class);
        intent.putExtra("extra_url", s);
        intent.putExtra("extra_title", s2);
        return intent;
    }
    
    @Override
    public void applyLocale() {
    }
    
    @Override
    public void onBackPressed() {
        if (this.infoFragment != null && this.infoFragment.canGoBack()) {
            this.infoFragment.goBack();
            return;
        }
        super.onBackPressed();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131492893);
        final String stringExtra = this.getIntent().getStringExtra("extra_url");
        final String stringExtra2 = this.getIntent().getStringExtra("extra_title");
        this.infoFragment = InfoFragment.create(stringExtra);
        this.getSupportFragmentManager().beginTransaction().replace(2131296483, this.infoFragment).commit();
        final Toolbar supportActionBar = this.findViewById(2131296700);
        supportActionBar.setTitle(stringExtra2);
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        supportActionBar.setNavigationOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                InfoActivity.this.finish();
            }
        });
    }
}
