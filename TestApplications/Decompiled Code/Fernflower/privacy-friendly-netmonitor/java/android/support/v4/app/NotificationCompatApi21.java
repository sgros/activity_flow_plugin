package android.support.v4.app;

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
import java.util.Set;

@RequiresApi(21)
class NotificationCompatApi21 {
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
      Bundle var1 = null;
      if (var0 == null) {
         return null;
      } else {
         Bundle var2 = new Bundle();
         String[] var3 = var0.getParticipants();
         int var4 = 0;
         String var5 = var1;
         if (var3 != null) {
            var5 = var1;
            if (var0.getParticipants().length > 1) {
               var5 = var0.getParticipants()[0];
            }
         }

         Parcelable[] var6;
         for(var6 = new Parcelable[var0.getMessages().length]; var4 < var6.length; ++var4) {
            var1 = new Bundle();
            var1.putString("text", var0.getMessages()[var4]);
            var1.putString("author", var5);
            var6[var4] = var1;
         }

         var2.putParcelableArray("messages", var6);
         RemoteInputCompatBase.RemoteInput var7 = var0.getRemoteInput();
         if (var7 != null) {
            var2.putParcelable("remote_input", fromCompatRemoteInput(var7));
         }

         var2.putParcelable("on_reply", var0.getReplyPendingIntent());
         var2.putParcelable("on_read", var0.getReadPendingIntent());
         var2.putStringArray("participants", var0.getParticipants());
         var2.putLong("timestamp", var0.getLatestTimestamp());
         return var2;
      }
   }

   static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle var0, NotificationCompatBase.UnreadConversation.Factory var1, RemoteInputCompatBase.RemoteInput.Factory var2) {
      RemoteInputCompatBase.RemoteInput var3 = null;
      if (var0 == null) {
         return null;
      } else {
         Parcelable[] var4 = var0.getParcelableArray("messages");
         String[] var6;
         if (var4 != null) {
            boolean var5 = false;
            var6 = new String[var4.length];
            int var7 = 0;

            boolean var12;
            while(true) {
               if (var7 >= var6.length) {
                  var12 = true;
                  break;
               }

               if (!(var4[var7] instanceof Bundle)) {
                  var12 = var5;
                  break;
               }

               var6[var7] = ((Bundle)var4[var7]).getString("text");
               if (var6[var7] == null) {
                  var12 = var5;
                  break;
               }

               ++var7;
            }

            if (!var12) {
               return null;
            }
         } else {
            var6 = null;
         }

         PendingIntent var11 = (PendingIntent)var0.getParcelable("on_read");
         PendingIntent var8 = (PendingIntent)var0.getParcelable("on_reply");
         android.app.RemoteInput var9 = (android.app.RemoteInput)var0.getParcelable("remote_input");
         String[] var10 = var0.getStringArray("participants");
         if (var10 != null && var10.length == 1) {
            if (var9 != null) {
               var3 = toCompatRemoteInput(var9, var2);
            }

            return var1.build(var6, var3, var8, var11, var10, var0.getLong("timestamp"));
         } else {
            return null;
         }
      }
   }

   private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(android.app.RemoteInput var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      return var1.build(var0.getResultKey(), var0.getLabel(), var0.getChoices(), var0.getAllowFreeFormInput(), var0.getExtras(), (Set)null);
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      private android.app.Notification.Builder b;
      private RemoteViews mBigContentView;
      private RemoteViews mContentView;
      private Bundle mExtras;
      private int mGroupAlertBehavior;
      private RemoteViews mHeadsUpContentView;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, String var19, ArrayList var20, Bundle var21, int var22, int var23, Notification var24, String var25, boolean var26, String var27, RemoteViews var28, RemoteViews var29, RemoteViews var30, int var31) {
         android.app.Notification.Builder var34 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         int var32 = var2.flags;
         boolean var33 = false;
         if ((var32 & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var34 = var34.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var34 = var34.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var34 = var34.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if ((var2.flags & 128) != 0) {
            var14 = true;
         } else {
            var14 = var33;
         }

         this.b = var34.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13).setLocalOnly(var18).setGroup(var25).setGroupSummary(var26).setSortKey(var27).setCategory(var19).setColor(var22).setVisibility(var23).setPublicVersion(var24);
         this.mExtras = new Bundle();
         if (var21 != null) {
            this.mExtras.putAll(var21);
         }

         Iterator var36 = var20.iterator();

         while(var36.hasNext()) {
            String var35 = (String)var36.next();
            this.b.addPerson(var35);
         }

         this.mContentView = var28;
         this.mBigContentView = var29;
         this.mHeadsUpContentView = var30;
         this.mGroupAlertBehavior = var31;
      }

      private void removeSoundAndVibration(Notification var1) {
         var1.sound = null;
         var1.vibrate = null;
         var1.defaults &= -2;
         var1.defaults &= -3;
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

         if (this.mGroupAlertBehavior != 0) {
            if (var1.getGroup() != null && (var1.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
               this.removeSoundAndVibration(var1);
            }

            if (var1.getGroup() != null && (var1.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
               this.removeSoundAndVibration(var1);
            }
         }

         return var1;
      }

      public android.app.Notification.Builder getBuilder() {
         return this.b;
      }
   }
}
