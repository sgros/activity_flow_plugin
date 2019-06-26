// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.view.View;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.app.Activity;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427355);
        RunStore.setContext(this);
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        final View viewById = this.findViewById(2131296393);
        if (viewById != null) {
            viewById.setAlpha(0.0f);
            viewById.animate().alpha(1.0f).setDuration(250L);
        }
        this.overridePendingTransition(0, 0);
        this.findViewById(2131296379).setMovementMethod(LinkMovementMethod.getInstance());
        this.findViewById(2131296257).setMovementMethod(LinkMovementMethod.getInstance());
        this.findViewById(2131296477).setMovementMethod(LinkMovementMethod.getInstance());
        this.findViewById(2131296359).setMovementMethod(LinkMovementMethod.getInstance());
        this.findViewById(2131296513).setText((CharSequence)"2.0");
    }
}
