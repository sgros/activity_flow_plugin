package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;

class PlaybackStateCompatApi22 {
   public static Bundle getExtras(Object var0) {
      return ((PlaybackState)var0).getExtras();
   }
}
