package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.MessagingStyle;
import android.app.Notification.MessagingStyle.Message;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(24)
@RequiresApi(24)
class NotificationCompatApi24 {
   public static final String CATEGORY_ALARM = "alarm";
   public static final String CATEGORY_CALL = "call";
   public static final String CATEGORY_EMAIL = "email";
   public static final String CATEGORY_ERROR = "err";
   public static final String CATEGORY_EVENT = "event";
   public static final String CATEGORY_MESSAGE = "msg";
   public static final String CATEGORY_PROGRESS = "progress";
   public static final String CATEGORY_PROMO = "promo";
   public static final String CATEGORY_RECOMMENDATION = "recommendation";
   public static final String CATEGORY_SERVICE = "service";
   public static final String CATEGORY_SOCIAL = "social";
   public static final String CATEGORY_STATUS = "status";
   public static final String CATEGORY_SYSTEM = "sys";
   public static final String CATEGORY_TRANSPORT = "transport";

   public static void addMessagingStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1, CharSequence var2, List var3, List var4, List var5, List var6, List var7) {
      MessagingStyle var9 = (new MessagingStyle(var1)).setConversationTitle(var2);

      for(int var8 = 0; var8 < var3.size(); ++var8) {
         Message var10 = new Message((CharSequence)var3.get(var8), (Long)var4.get(var8), (CharSequence)var5.get(var8));
         if (var6.get(var8) != null) {
            var10.setData((String)var6.get(var8), (Uri)var7.get(var8));
         }

         var9.addMessage(var10);
      }

      var9.setBuilder(var0.getBuilder());
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      private android.app.Notification.Builder b;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, String var19, ArrayList var20, Bundle var21, int var22, int var23, Notification var24, String var25, boolean var26, String var27, CharSequence[] var28, RemoteViews var29, RemoteViews var30, RemoteViews var31) {
         android.app.Notification.Builder var32 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         if ((var2.flags & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var32 = var32.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var32 = var32.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var32 = var32.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if ((var2.flags & 128) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.b = var32.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13).setLocalOnly(var18).setExtras(var21).setGroup(var25).setGroupSummary(var26).setSortKey(var27).setCategory(var19).setColor(var22).setVisibility(var23).setPublicVersion(var24).setRemoteInputHistory(var28);
         if (var29 != null) {
            this.b.setCustomContentView(var29);
         }

         if (var30 != null) {
            this.b.setCustomBigContentView(var30);
         }

         if (var31 != null) {
            this.b.setCustomHeadsUpContentView(var31);
         }

         Iterator var34 = var20.iterator();

         while(var34.hasNext()) {
            String var33 = (String)var34.next();
            this.b.addPerson(var33);
         }

      }

      public void addAction(NotificationCompatBase.Action var1) {
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
         var2.setAllowGeneratedReplies(var1.getAllowGeneratedReplies());
         this.b.addAction(var2.build());
      }

      public Notification build() {
         return this.b.build();
      }

      public android.app.Notification.Builder getBuilder() {
         return this.b;
      }
   }
}
