// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.home.pinsite;

import kotlin.jvm.internal.Intrinsics;
import android.content.Context;

public final class PinSiteManagerKt
{
    public static final PinSiteManager getPinSiteManager(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return new PinSiteManager(new SharedPreferencePinSiteDelegate(context));
    }
}
