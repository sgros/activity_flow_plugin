package org.telegram.messenger.support.customtabs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomTabsClient {
   private final ICustomTabsService mService;
   private final ComponentName mServiceComponentName;

   CustomTabsClient(ICustomTabsService var1, ComponentName var2) {
      this.mService = var1;
      this.mServiceComponentName = var2;
   }

   public static boolean bindCustomTabsService(Context var0, String var1, CustomTabsServiceConnection var2) {
      Intent var3 = new Intent("android.support.customtabs.action.CustomTabsService");
      if (!TextUtils.isEmpty(var1)) {
         var3.setPackage(var1);
      }

      return var0.bindService(var3, var2, 33);
   }

   public static boolean connectAndInitialize(final Context var0, String var1) {
      if (var1 == null) {
         return false;
      } else {
         var0 = var0.getApplicationContext();
         CustomTabsServiceConnection var2 = new CustomTabsServiceConnection() {
            public final void onCustomTabsServiceConnected(ComponentName var1, CustomTabsClient var2) {
               var2.warmup(0L);
               var0.unbindService(this);
            }

            public final void onServiceDisconnected(ComponentName var1) {
            }
         };

         try {
            boolean var3 = bindCustomTabsService(var0, var1, var2);
            return var3;
         } catch (SecurityException var4) {
            return false;
         }
      }
   }

   public static String getPackageName(Context var0, List var1) {
      return getPackageName(var0, var1, false);
   }

   public static String getPackageName(Context var0, List var1, boolean var2) {
      PackageManager var3 = var0.getPackageManager();
      Object var6;
      if (var1 == null) {
         var6 = new ArrayList();
      } else {
         var6 = var1;
      }

      Intent var4 = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
      Object var5 = var6;
      if (!var2) {
         ResolveInfo var9 = var3.resolveActivity(var4, 0);
         var5 = var6;
         if (var9 != null) {
            String var10 = var9.activityInfo.packageName;
            var5 = new ArrayList(((List)var6).size() + 1);
            ((List)var5).add(var10);
            if (var1 != null) {
               ((List)var5).addAll(var1);
            }
         }
      }

      Intent var7 = new Intent("android.support.customtabs.action.CustomTabsService");
      Iterator var11 = ((List)var5).iterator();

      String var8;
      do {
         if (!var11.hasNext()) {
            return null;
         }

         var8 = (String)var11.next();
         var7.setPackage(var8);
      } while(var3.resolveService(var7, 0) == null);

      return var8;
   }

   public Bundle extraCommand(String var1, Bundle var2) {
      try {
         Bundle var4 = this.mService.extraCommand(var1, var2);
         return var4;
      } catch (RemoteException var3) {
         return null;
      }
   }

   public CustomTabsSession newSession(final CustomTabsCallback var1) {
      ICustomTabsCallback.Stub var2 = new ICustomTabsCallback.Stub() {
         private Handler mHandler = new Handler(Looper.getMainLooper());

         public void extraCallback(final String var1x, final Bundle var2) throws RemoteException {
            if (var1 != null) {
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var1.extraCallback(var1x, var2);
                  }
               });
            }
         }

         public void onMessageChannelReady(final Bundle var1x) throws RemoteException {
            if (var1 != null) {
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var1.onMessageChannelReady(var1x);
                  }
               });
            }
         }

         public void onNavigationEvent(final int var1x, final Bundle var2) {
            if (var1 != null) {
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var1.onNavigationEvent(var1x, var2);
                  }
               });
            }
         }

         public void onPostMessage(final String var1x, final Bundle var2) throws RemoteException {
            if (var1 != null) {
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var1.onPostMessage(var1x, var2);
                  }
               });
            }
         }
      };
      CustomTabsSession var5 = null;

      boolean var3;
      try {
         var3 = this.mService.newSession(var2);
      } catch (RemoteException var4) {
         return var5;
      }

      if (!var3) {
         return null;
      } else {
         var5 = new CustomTabsSession(this.mService, var2, this.mServiceComponentName);
         return var5;
      }
   }

   public boolean warmup(long var1) {
      try {
         boolean var3 = this.mService.warmup(var1);
         return var3;
      } catch (RemoteException var5) {
         return false;
      }
   }
}
