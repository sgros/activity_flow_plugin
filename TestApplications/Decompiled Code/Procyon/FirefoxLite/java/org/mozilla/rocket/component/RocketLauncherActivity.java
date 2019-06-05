// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.component;

import org.mozilla.rocket.content.HomeFragmentViewState;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.os.Bundle;
import org.mozilla.focus.activity.MainActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public final class RocketLauncherActivity extends AppCompatActivity
{
    private final void dispatchNormalIntent() {
        final Intent intent = new Intent(this.getIntent());
        intent.setClass(this.getApplicationContext(), (Class)MainActivity.class);
        this.filterFlags(intent);
        this.startActivity(intent);
        this.finish();
    }
    
    private final void filterFlags(final Intent intent) {
        intent.setFlags(intent.getFlags() & 0xEFFFFFFF);
        intent.setFlags(intent.getFlags() & 0xFFFF7FFF);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final LaunchIntentDispatcher.Companion companion = LaunchIntentDispatcher.Companion;
        final Context context = (Context)this;
        final Intent intent = this.getIntent();
        Intrinsics.checkExpressionValueIsNotNull(intent, "intent");
        final LaunchIntentDispatcher.Action dispatch = companion.dispatch(context, intent);
        HomeFragmentViewState.reset();
        if (dispatch != null) {
            switch (RocketLauncherActivity$WhenMappings.$EnumSwitchMapping$0[dispatch.ordinal()]) {
                case 2: {
                    this.dispatchNormalIntent();
                    break;
                }
                case 1: {
                    this.finish();
                    break;
                }
            }
        }
    }
}
