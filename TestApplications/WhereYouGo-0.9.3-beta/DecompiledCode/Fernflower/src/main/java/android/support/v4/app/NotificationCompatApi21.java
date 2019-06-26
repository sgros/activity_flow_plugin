package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(21)
@RequiresApi(21)
class NotificationCompatApi21 {
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
   private static final String KEY_AUTHOR = "author";
   private static final String KEY_MESSAGES = "messages";
   private static final String KEY_ON_READ = "on_read";
   private static final String KEY_ON_REPLY = "on_reply";
   private static final String KEY_PARTICIPANTS = "participants";
   private static final String KEY_REMOTE_INPUT = "remote_input";
   private static final String KEY_TEXT = "text";
   private static final String KEY_TIMESTAMP = "timestamp";

   private static android.app.RemoteInput fromCompatRemoteInput(RemoteInputCompatBase.RemoteInput var0) {
      return (new android.app.RemoteInput.Builder(var0.getResultKey())).setLabel(var0.getLabel()).setChoices(var0.getChoices()).setAllowFreeFormInput(var0.getAllowFreeFormInput()).addExtras(var0.getExtras()).build();
   }

   static Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation var0) {
      Bundle var6;
      if (var0 == null) {
         var6 = null;
      } else {
         Bundle var1 = new Bundle();
         Parcelable[] var2 = null;
         String var3 = var2;
         if (var0.getParticipants() != null) {
            var3 = var2;
            if (var0.getParticipants().length > 1) {
               var3 = var0.getParticipants()[0];
            }
         }

         var2 = new Parcelable[var0.getMessages().length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            Bundle var5 = new Bundle();
            var5.putString("text", var0.getMessages()[var4]);
            var5.putString("author", var3);
            var2[var4] = var5;
         }

         var1.putParcelableArray("messages", var2);
         RemoteInputCompatBase.RemoteInput var7 = var0.getRemoteInput();
         if (var7 != null) {
            var1.putParcelable("remote_input", fromCompatRemoteInput(var7));
         }

         var1.putParcelable("on_reply", var0.getReplyPendingIntent());
         var1.putParcelable("on_read", var0.getReadPendingIntent());
         var1.putStringArray("participants", var0.getParticipants());
         var1.putLong("timestamp", var0.getLatestTimestamp());
         var6 = var1;
      }

      return var6;
   }

   public static String getCategory(Notification var0) {
      return var0.category;
   }

   static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle var0, NotificationCompatBase.UnreadConversation.Factory var1, RemoteInputCompatBase.RemoteInput.Factory var2) {
      Object var3 = null;
      Object var4 = null;
      NotificationCompatBase.UnreadConversation var5;
      if (var0 == null) {
         var5 = (NotificationCompatBase.UnreadConversation)var4;
      } else {
         Parcelable[] var14 = var0.getParcelableArray("messages");
         String[] var6 = null;
         if (var14 != null) {
            var6 = new String[var14.length];
            boolean var7 = true;
            int var8 = 0;

            boolean var9;
            while(true) {
               var9 = var7;
               if (var8 >= var6.length) {
                  break;
               }

               if (!(var14[var8] instanceof Bundle)) {
                  var9 = false;
                  break;
               }

               var6[var8] = ((Bundle)var14[var8]).getString("text");
               if (var6[var8] == null) {
                  var9 = false;
                  break;
               }

               ++var8;
            }

            var5 = (NotificationCompatBase.UnreadConversation)var4;
            if (!var9) {
               return var5;
            }
         }

         PendingIntent var10 = (PendingIntent)var0.getParcelable("on_read");
         PendingIntent var11 = (PendingIntent)var0.getParcelable("on_reply");
         android.app.RemoteInput var12 = (android.app.RemoteInput)var0.getParcelable("remote_input");
         String[] var13 = var0.getStringArray("participants");
         var5 = (NotificationCompatBase.UnreadConversation)var4;
         if (var13 != null) {
            var5 = (NotificationCompatBase.UnreadConversation)var4;
            if (var13.length == 1) {
               RemoteInputCompatBase.RemoteInput var15 = (RemoteInputCompatBase.RemoteInput)var3;
               if (var12 != null) {
                  var15 = toCompatRemoteInput(var12, var2);
               }

               var5 = var1.build(var6, var15, var11, var10, var13, var0.getLong("timestamp"));
            }
         }
      }

      return var5;
   }

   private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(android.app.RemoteInput var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      return var1.build(var0.getResultKey(), var0.getLabel(), var0.getChoices(), var0.getAllowFreeFormInput(), var0.getExtras());
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      private android.app.Notification.Builder b;
      private RemoteViews mBigContentView;
      private RemoteViews mContentView;
      private Bundle mExtras;
      private RemoteViews mHeadsUpContentView;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, String var19, ArrayList var20, Bundle var21, int var22, int var23, Notification var24, String var25, boolean var26, String var27, RemoteViews var28, RemoteViews var29, RemoteViews var30) {
         android.app.Notification.Builder var31 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         if ((var2.flags & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var31 = var31.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var31 = var31.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var31 = var31.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if ((var2.flags & 128) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.b = var31.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13).setLocalOnly(var18).setGroup(var25).setGroupSummary(var26).setSortKey(var27).setCategory(var19).setColor(var22).setVisibility(var23).setPublicVersion(var24);
         this.mExtras = new Bundle();
         if (var21 != null) {
            this.mExtras.putAll(var21);
         }

         Iterator var32 = var20.iterator();

         while(var32.hasNext()) {
            String var33 = (String)var32.next();
            this.b.addPerson(var33);
         }

         this.mContentView = var28;
         this.mBigContentView = var29;
         this.mHeadsUpContentView = var30;
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

         if (this.mHeadsUpContentView != null) {
            var1.headsUpContentView = this.mHeadsUpContentView;
         }

         return var1;
      }

      public android.app.Notification.Builder getBuilder() {
         return this.b;
      }
   }
}
