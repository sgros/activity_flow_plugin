package org.mozilla.focus.utils;

import android.content.Context;

// $FF: synthetic class
public final class _$$Lambda$FirebaseHelper$BlockingEnabler$KbJvRYq_uiQihzjwaWiFwpeUbX4 implements FirebaseWrapper.RemoteConfigFetchCallback {
   // $FF: synthetic field
   private final Context f$0;

   // $FF: synthetic method
   public _$$Lambda$FirebaseHelper$BlockingEnabler$KbJvRYq_uiQihzjwaWiFwpeUbX4(Context var1) {
      this.f$0 = var1;
   }

   public final void onFetched() {
      FirebaseHelper.BlockingEnabler.lambda$doInBackground$2(this.f$0);
   }
}
