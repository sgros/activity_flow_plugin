package android.support.v4.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Build.VERSION;

public abstract class NotificationCompatSideChannelService extends Service {
   public abstract void cancel(String var1, int var2, String var3);

   public abstract void cancelAll(String var1);

   void checkPermission(int var1, String var2) {
      String[] var3 = this.getPackageManager().getPackagesForUid(var1);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         if (var3[var5].equals(var2)) {
            return;
         }
      }

      throw new SecurityException("NotificationSideChannelService: Uid " + var1 + " is not authorized for package " + var2);
   }

   public abstract void notify(String var1, int var2, String var3, Notification var4);

   public IBinder onBind(Intent var1) {
      Object var2 = null;
      NotificationCompatSideChannelService.NotificationSideChannelStub var3 = (NotificationCompatSideChannelService.NotificationSideChannelStub)var2;
      if (var1.getAction().equals("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")) {
         if (VERSION.SDK_INT > 19) {
            var3 = (NotificationCompatSideChannelService.NotificationSideChannelStub)var2;
         } else {
            var3 = new NotificationCompatSideChannelService.NotificationSideChannelStub();
         }
      }

      return var3;
   }

   private class NotificationSideChannelStub extends INotificationSideChannel.Stub {
      NotificationSideChannelStub() {
      }

      public void cancel(String var1, int var2, String var3) throws RemoteException {
         NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), var1);
         long var4 = clearCallingIdentity();

         try {
            NotificationCompatSideChannelService.this.cancel(var1, var2, var3);
         } finally {
            restoreCallingIdentity(var4);
         }

      }

      public void cancelAll(String var1) {
         NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), var1);
         long var2 = clearCallingIdentity();

         try {
            NotificationCompatSideChannelService.this.cancelAll(var1);
         } finally {
            restoreCallingIdentity(var2);
         }

      }

      public void notify(String var1, int var2, String var3, Notification var4) throws RemoteException {
         NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), var1);
         long var5 = clearCallingIdentity();

         try {
            NotificationCompatSideChannelService.this.notify(var1, var2, var3, var4);
         } finally {
            restoreCallingIdentity(var5);
         }

      }
   }
}
