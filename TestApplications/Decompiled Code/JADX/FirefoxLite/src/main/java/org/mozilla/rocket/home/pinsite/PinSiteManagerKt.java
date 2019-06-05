package org.mozilla.rocket.home.pinsite;

import android.content.Context;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PinSiteManager.kt */
public final class PinSiteManagerKt {
    public static final PinSiteManager getPinSiteManager(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return new PinSiteManager(new SharedPreferencePinSiteDelegate(context));
    }
}
