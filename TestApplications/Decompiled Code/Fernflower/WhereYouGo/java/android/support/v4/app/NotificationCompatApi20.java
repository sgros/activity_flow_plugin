package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Action;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;

@TargetApi(20)
@RequiresApi(20)
class NotificationCompatApi20 {
   public static void addAction(android.app.Notification.Builder var0, NotificationCompatBase.Action var1) {
      android.app.Notification.Action.Builder var2 = new android.app.Notification.Action.Builder(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
      if (var1.getRemoteInputs() != null) {
         android.app.RemoteInput[] var3 = RemoteInputCompatApi20.fromCompat(var1.getRemoteInputs());
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            var2.addRemoteInput(var3[var5]);
         }
      }

      Bundle var6;
      if (var1.getExtras() != null) {
         var6 = new Bundle(var1.getExtras());
      } else {
         var6 = new Bundle();
      }

      var6.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
      var2.addExtras(var6);
      var0.addAction(var2.build());
   }

   public static NotificationCompatBase.Action getAction(Notification var0, int var1, NotificationCompatBase.Action.Factory var2, RemoteInputCompatBase.RemoteInput.Factory var3) {
      return getActionCompatFromAction(var0.actions[var1], var2, var3);
   }

   private static NotificationCompatBase.Action getActionCompatFromAction(Action var0, NotificationCompatBase.Action.Factory var1, RemoteInputCompatBase.RemoteInput.Factory var2) {
      RemoteInputCompatBase.RemoteInput[] var4 = RemoteInputCompatApi20.toCompat(var0.getRemoteInputs(), var2);
      boolean var3 = var0.getExtras().getBoolean("android.support.allowGeneratedReplies");
      return var1.build(var0.icon, var0.title, var0.actionIntent, var0.getExtras(), var4, var3);
   }

   private static Action getActionFromActionCompat(NotificationCompatBase.Action var0) {
      android.app.Notification.Action.Builder var1 = new android.app.Notification.Action.Builder(var0.getIcon(), var0.getTitle(), var0.getActionIntent());
      Bundle var2;
      if (var0.getExtras() != null) {
         var2 = new Bundle(var0.getExtras());
      } else {
         var2 = new Bundle();
      }

      var2.putBoolean("android.support.allowGeneratedReplies", var0.getAllowGeneratedReplies());
      var1.addExtras(var2);
      RemoteInputCompatBase.RemoteInput[] var5 = var0.getRemoteInputs();
      if (var5 != null) {
         android.app.RemoteInput[] var6 = RemoteInputCompatApi20.fromCompat(var5);
         int var3 = var6.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var1.addRemoteInput(var6[var4]);
         }
      }

      return var1.build();
   }

   public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(ArrayList var0, NotificationCompatBase.Action.Factory var1, RemoteInputCompatBase.RemoteInput.Factory var2) {
      NotificationCompatBase.Action[] var3;
      if (var0 == null) {
         var3 = null;
      } else {
         NotificationCompatBase.Action[] var4 = var1.newArray(var0.size());
         int var5 = 0;

         while(true) {
            var3 = var4;
            if (var5 >= var4.length) {
               break;
            }

            var4[var5] = getActionCompatFromAction((Action)var0.get(var5), var1, var2);
            ++var5;
         }
      }

      return var3;
   }

   public static String getGroup(Notification var0) {
      return var0.getGroup();
   }

   public static boolean getLocalOnly(Notification var0) {
      boolean var1;
      if ((var0.flags & 256) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static ArrayList getParcelableArrayListForActions(NotificationCompatBase.Action[] var0) {
      ArrayList var1;
      if (var0 == null) {
         var1 = null;
      } else {
         ArrayList var2 = new ArrayList(var0.length);
         int var3 = var0.length;
         int var4 = 0;

         while(true) {
            var1 = var2;
            if (var4 >= var3) {
               break;
            }

            var2.add(getActionFromActionCompat(var0[var4]));
            ++var4;
         }
      }

      return var1;
   }

   public static String getSortKey(Notification var0) {
      return var0.getSortKey();
   }

   public static boolean isGroupSummary(Notification var0) {
      boolean var1;
      if ((var0.flags & 512) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      private android.app.Notification.Builder b;
      private RemoteViews mBigContentView;
      private RemoteViews mContentView;
      private Bundle mExtras;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, ArrayList var19, Bundle var20, String var21, boolean var22, String var23, RemoteViews var24, RemoteViews var25) {
         android.app.Notification.Builder var26 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         if ((var2.flags & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26 = var26.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26 = var26.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26 = var26.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if ((var2.flags & 128) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.b = var26.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13).setLocalOnly(var18).setGroup(var21).setGroupSummary(var22).setSortKey(var23);
         this.mExtras = new Bundle();
         if (var20 != null) {
            this.mExtras.putAll(var20);
         }

         if (var19 != null && !var19.isEmpty()) {
            this.mExtras.putStringArray("android.people", (String[])var19.toArray(new String[var19.size()]));
         }

         this.mContentView = var24;
         this.mBigContentView = var25;
      }

      public void addAction(NotificationCompatBase.Action var1) {
         NotificationCompatApi20.addAction(this.b, var1);
      }

      public Notification build() {
         this.b.setExtras(this.mExtras);
         Notification var1 = this.b.build();
         if (this.mContentView != null) {
            var1.contentView = this.mContentView;
         }

         if (this.mBigContentView != null) {
            var1.bigContentView = this.mBigContentView;
         }

         return var1;
      }

      public android.app.Notification.Builder getBuilder() {
         return this.b;
      }
   }
}
