package org.mozilla.rocket.component;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.rocket.component.LaunchIntentDispatcher.Action;
import org.mozilla.rocket.component.LaunchIntentDispatcher.Companion;
import org.mozilla.rocket.content.HomeFragmentViewState;

/* compiled from: RocketLauncherActivity.kt */
public final class RocketLauncherActivity extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Companion companion = LaunchIntentDispatcher.Companion;
        Context context = this;
        Intent intent = getIntent();
        Intrinsics.checkExpressionValueIsNotNull(intent, "intent");
        Action dispatch = companion.dispatch(context, intent);
        HomeFragmentViewState.reset();
        if (dispatch != null) {
            switch (dispatch) {
                case HANDLED:
                    finish();
                    return;
                case NORMAL:
                    dispatchNormalIntent();
                    return;
                default:
                    return;
            }
        }
    }

    private final void dispatchNormalIntent() {
        Intent intent = new Intent(getIntent());
        intent.setClass(getApplicationContext(), MainActivity.class);
        filterFlags(intent);
        startActivity(intent);
        finish();
    }

    private final void filterFlags(Intent intent) {
        intent.setFlags(intent.getFlags() & -268435457);
        intent.setFlags(intent.getFlags() & -32769);
    }
}
