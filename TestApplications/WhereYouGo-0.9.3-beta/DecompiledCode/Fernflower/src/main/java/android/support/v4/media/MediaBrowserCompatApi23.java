package android.support.v4.media;

import android.annotation.TargetApi;
import android.media.browse.MediaBrowser;
import android.media.browse.MediaBrowser.MediaItem;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class MediaBrowserCompatApi23 {
   public static Object createItemCallback(MediaBrowserCompatApi23.ItemCallback var0) {
      return new MediaBrowserCompatApi23.ItemCallbackProxy(var0);
   }

   public static void getItem(Object var0, String var1, Object var2) {
      ((MediaBrowser)var0).getItem(var1, (android.media.browse.MediaBrowser.ItemCallback)var2);
   }

   interface ItemCallback {
      void onError(@NonNull String var1);

      void onItemLoaded(Parcel var1);
   }

   static class ItemCallbackProxy extends android.media.browse.MediaBrowser.ItemCallback {
      protected final MediaBrowserCompatApi23.ItemCallback mItemCallback;

      public ItemCallbackProxy(MediaBrowserCompatApi23.ItemCallback var1) {
         this.mItemCallback = var1;
      }

      public void onError(@NonNull String var1) {
         this.mItemCallback.onError(var1);
      }

      public void onItemLoaded(MediaItem var1) {
         if (var1 == null) {
            this.mItemCallback.onItemLoaded((Parcel)null);
         } else {
            Parcel var2 = Parcel.obtain();
            var1.writeToParcel(var2, 0);
            this.mItemCallback.onItemLoaded(var2);
         }

      }
   }
}
