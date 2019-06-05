package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class MediaControllerCompatApi24 {
   public static class TransportControls extends MediaControllerCompatApi23.TransportControls {
      public static void prepare(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).prepare();
      }

      public static void prepareFromMediaId(Object var0, String var1, Bundle var2) {
         ((android.media.session.MediaController.TransportControls)var0).prepareFromMediaId(var1, var2);
      }

      public static void prepareFromSearch(Object var0, String var1, Bundle var2) {
         ((android.media.session.MediaController.TransportControls)var0).prepareFromSearch(var1, var2);
      }

      public static void prepareFromUri(Object var0, Uri var1, Bundle var2) {
         ((android.media.session.MediaController.TransportControls)var0).prepareFromUri(var1, var2);
      }
   }
}
