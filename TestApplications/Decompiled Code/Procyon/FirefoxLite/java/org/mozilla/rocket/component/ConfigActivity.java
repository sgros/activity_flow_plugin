// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.component;

import org.mozilla.focus.activity.SettingsActivity;
import android.content.Intent;
import org.mozilla.focus.components.ComponentToggleService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.startService(new Intent(this.getApplicationContext(), (Class)ComponentToggleService.class));
        final Intent intent = new Intent(this.getApplicationContext(), (Class)SettingsActivity.class);
        intent.setFlags(268435456);
        this.startActivity(intent);
        this.finishAndRemoveTask();
    }
}
