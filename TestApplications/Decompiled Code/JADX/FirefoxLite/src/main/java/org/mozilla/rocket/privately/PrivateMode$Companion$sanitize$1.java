package org.mozilla.rocket.privately;

import android.content.Context;
import java.io.File;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PrivateMode.kt */
final class PrivateMode$Companion$sanitize$1 implements Runnable {
    final /* synthetic */ Context $context;

    PrivateMode$Companion$sanitize$1(Context context) {
        this.$context = context;
    }

    public final void run() {
        Context applicationContext = this.$context.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "context.applicationContext");
        File cacheDir = applicationContext.getCacheDir();
        if (cacheDir != null) {
            PrivateMode.Companion.clean(cacheDir, this.$context);
        }
        cacheDir = this.$context.getApplicationContext().getDir("webview", 0);
        if (cacheDir != null) {
            PrivateMode.Companion.clean(cacheDir, this.$context);
        }
    }
}
