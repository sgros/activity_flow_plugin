package org.telegram.messenger.support.customtabs;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.RemoteViews;
import java.util.List;

public final class CustomTabsSession {
   private static final String TAG = "CustomTabsSession";
   private final ICustomTabsCallback mCallback;
   private final ComponentName mComponentName;
   private final Object mLock = new Object();
   private final ICustomTabsService mService;

   CustomTabsSession(ICustomTabsService var1, ICustomTabsCallback var2, ComponentName var3) {
      this.mService = var1;
      this.mCallback = var2;
      this.mComponentName = var3;
   }

   public static CustomTabsSession createDummySessionForTesting(ComponentName var0) {
      return new CustomTabsSession((ICustomTabsService)null, new CustomTabsSessionToken.DummyCallback(), var0);
   }

   IBinder getBinder() {
      return this.mCallback.asBinder();
   }

   ComponentName getComponentName() {
      return this.mComponentName;
   }

   public boolean mayLaunchUrl(Uri var1, Bundle var2, List var3) {
      try {
         boolean var4 = this.mService.mayLaunchUrl(this.mCallback, var1, var2, var3);
         return var4;
      } catch (RemoteException var5) {
         return false;
      }
   }

   public int postMessage(String var1, Bundle var2) {
      Object var3 = this.mLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label180: {
         int var4;
         label179: {
            try {
               try {
                  var4 = this.mService.postMessage(this.mCallback, var1, var2);
                  break label179;
               } catch (RemoteException var28) {
               }
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label180;
            }

            try {
               return -2;
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label180;
            }
         }

         label173:
         try {
            return var4;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            break label173;
         }
      }

      while(true) {
         Throwable var30 = var10000;

         try {
            throw var30;
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean requestPostMessageChannel(Uri var1) {
      try {
         boolean var2 = this.mService.requestPostMessageChannel(this.mCallback, var1);
         return var2;
      } catch (RemoteException var3) {
         return false;
      }
   }

   public boolean setActionButton(Bitmap var1, String var2) {
      Bundle var3 = new Bundle();
      var3.putParcelable("android.support.customtabs.customaction.ICON", var1);
      var3.putString("android.support.customtabs.customaction.DESCRIPTION", var2);
      Bundle var6 = new Bundle();
      var6.putBundle("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", var3);

      try {
         boolean var4 = this.mService.updateVisuals(this.mCallback, var6);
         return var4;
      } catch (RemoteException var5) {
         return false;
      }
   }

   public boolean setSecondaryToolbarViews(RemoteViews var1, int[] var2, PendingIntent var3) {
      Bundle var4 = new Bundle();
      var4.putParcelable("android.support.customtabs.extra.EXTRA_REMOTEVIEWS", var1);
      var4.putIntArray("android.support.customtabs.extra.EXTRA_REMOTEVIEWS_VIEW_IDS", var2);
      var4.putParcelable("android.support.customtabs.extra.EXTRA_REMOTEVIEWS_PENDINGINTENT", var3);

      try {
         boolean var5 = this.mService.updateVisuals(this.mCallback, var4);
         return var5;
      } catch (RemoteException var6) {
         return false;
      }
   }

   @Deprecated
   public boolean setToolbarItem(int var1, Bitmap var2, String var3) {
      Bundle var4 = new Bundle();
      var4.putInt("android.support.customtabs.customaction.ID", var1);
      var4.putParcelable("android.support.customtabs.customaction.ICON", var2);
      var4.putString("android.support.customtabs.customaction.DESCRIPTION", var3);
      Bundle var7 = new Bundle();
      var7.putBundle("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", var4);

      try {
         boolean var5 = this.mService.updateVisuals(this.mCallback, var7);
         return var5;
      } catch (RemoteException var6) {
         return false;
      }
   }
}
