// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.content.Context;
import org.secuso.privacyfriendlynetmonitor.Assistant.PrefManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        new PrefManager((Context)this);
        Intent intent;
        if (PrefManager.isFirstTimeLaunch()) {
            intent = new Intent((Context)this, (Class)TutorialActivity.class);
            PrefManager.setFirstTimeLaunch(false);
        }
        else {
            intent = new Intent((Context)this, (Class)MainActivity.class);
            intent.addFlags(67108864);
        }
        this.startActivity(intent);
        this.finish();
    }
}
