package android.support.v4.media.session;

import android.media.session.MediaSession;
import android.support.annotation.RequiresApi;

@RequiresApi(22)
class MediaSessionCompatApi22 {
   public static void setRatingType(Object var0, int var1) {
      ((MediaSession)var0).setRatingType(var1);
   }
}
