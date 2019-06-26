package android.support.v4.media.session;

import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;

@RequiresApi(24)
class MediaSessionCompatApi24 {
   private static final String TAG = "MediaSessionCompatApi24";

   public static Object createCallback(MediaSessionCompatApi24.Callback var0) {
      return new MediaSessionCompatApi24.CallbackProxy(var0);
   }

   public static String getCallingPackage(Object var0) {
      MediaSession var2 = (MediaSession)var0;

      try {
         String var3 = (String)var2.getClass().getMethod("getCallingPackage").invoke(var2);
         return var3;
      } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException var1) {
         Log.e("MediaSessionCompatApi24", "Cannot execute MediaSession.getCallingPackage()", var1);
         return null;
      }
   }

   public interface Callback extends MediaSessionCompatApi23.Callback {
      void onPrepare();

      void onPrepareFromMediaId(String var1, Bundle var2);

      void onPrepareFromSearch(String var1, Bundle var2);

      void onPrepareFromUri(Uri var1, Bundle var2);
   }

   static class CallbackProxy extends MediaSessionCompatApi23.CallbackProxy {
      public CallbackProxy(MediaSessionCompatApi24.Callback var1) {
         super(var1);
      }

      public void onPrepare() {
         ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepare();
      }

      public void onPrepareFromMediaId(String var1, Bundle var2) {
         ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepareFromMediaId(var1, var2);
      }

      public void onPrepareFromSearch(String var1, Bundle var2) {
         ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepareFromSearch(var1, var2);
      }

      public void onPrepareFromUri(Uri var1, Bundle var2) {
         ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepareFromUri(var1, var2);
      }
   }
}
