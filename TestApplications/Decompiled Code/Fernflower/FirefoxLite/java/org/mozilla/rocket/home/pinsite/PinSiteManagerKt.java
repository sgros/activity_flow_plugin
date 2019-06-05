package org.mozilla.rocket.home.pinsite;

import android.content.Context;
import kotlin.jvm.internal.Intrinsics;

public final class PinSiteManagerKt {
   public static final PinSiteManager getPinSiteManager(Context var0) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      return new PinSiteManager((PinSiteDelegate)(new SharedPreferencePinSiteDelegate(var0)));
   }
}
