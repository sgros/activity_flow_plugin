package android.support.v4.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser.MediaItem;
import android.media.session.MediaSession.Token;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.service.media.MediaBrowserService;
import android.service.media.MediaBrowserService.Result;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(21)
@RequiresApi(21)
class MediaBrowserServiceCompatApi21 {
   public static Object createService(Context var0, MediaBrowserServiceCompatApi21.ServiceCompatProxy var1) {
      return new MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor(var0, var1);
   }

   public static void notifyChildrenChanged(Object var0, String var1) {
      ((MediaBrowserService)var0).notifyChildrenChanged(var1);
   }

   public static IBinder onBind(Object var0, Intent var1) {
      return ((MediaBrowserService)var0).onBind(var1);
   }

   public static void onCreate(Object var0) {
      ((MediaBrowserService)var0).onCreate();
   }

   public static void setSessionToken(Object var0, Object var1) {
      ((MediaBrowserService)var0).setSessionToken((Token)var1);
   }

   static class BrowserRoot {
      final Bundle mExtras;
      final String mRootId;

      BrowserRoot(String var1, Bundle var2) {
         this.mRootId = var1;
         this.mExtras = var2;
      }
   }

   static class MediaBrowserServiceAdaptor extends MediaBrowserService {
      final MediaBrowserServiceCompatApi21.ServiceCompatProxy mServiceProxy;

      MediaBrowserServiceAdaptor(Context var1, MediaBrowserServiceCompatApi21.ServiceCompatProxy var2) {
         this.attachBaseContext(var1);
         this.mServiceProxy = var2;
      }

      public android.service.media.MediaBrowserService.BrowserRoot onGetRoot(String var1, int var2, Bundle var3) {
         MediaBrowserServiceCompatApi21.BrowserRoot var4 = this.mServiceProxy.onGetRoot(var1, var2, var3);
         android.service.media.MediaBrowserService.BrowserRoot var5;
         if (var4 == null) {
            var5 = null;
         } else {
            var5 = new android.service.media.MediaBrowserService.BrowserRoot(var4.mRootId, var4.mExtras);
         }

         return var5;
      }

      public void onLoadChildren(String var1, Result var2) {
         this.mServiceProxy.onLoadChildren(var1, new MediaBrowserServiceCompatApi21.ResultWrapper(var2));
      }
   }

   static class ResultWrapper {
      Result mResultObj;

      ResultWrapper(Result var1) {
         this.mResultObj = var1;
      }

      public void detach() {
         this.mResultObj.detach();
      }

      List parcelListToItemList(List var1) {
         ArrayList var4;
         if (var1 == null) {
            var4 = null;
         } else {
            ArrayList var2 = new ArrayList();
            Iterator var3 = var1.iterator();

            while(true) {
               var4 = var2;
               if (!var3.hasNext()) {
                  break;
               }

               Parcel var5 = (Parcel)var3.next();
               var5.setDataPosition(0);
               var2.add(MediaItem.CREATOR.createFromParcel(var5));
               var5.recycle();
            }
         }

         return var4;
      }

      public void sendResult(Object var1) {
         if (var1 instanceof List) {
            this.mResultObj.sendResult(this.parcelListToItemList((List)var1));
         } else if (var1 instanceof Parcel) {
            Parcel var2 = (Parcel)var1;
            var2.setDataPosition(0);
            this.mResultObj.sendResult(MediaItem.CREATOR.createFromParcel(var2));
            var2.recycle();
         } else {
            this.mResultObj.sendResult((Object)null);
         }

      }
   }

   public interface ServiceCompatProxy {
      MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(String var1, int var2, Bundle var3);

      void onLoadChildren(String var1, MediaBrowserServiceCompatApi21.ResultWrapper var2);
   }
}
