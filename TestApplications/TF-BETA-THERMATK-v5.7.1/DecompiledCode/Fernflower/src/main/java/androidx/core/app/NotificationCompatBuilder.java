package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
   private final List mActionExtrasList = new ArrayList();
   private RemoteViews mBigContentView;
   private final Builder mBuilder;
   private final NotificationCompat.Builder mBuilderCompat;
   private RemoteViews mContentView;
   private final Bundle mExtras = new Bundle();
   private int mGroupAlertBehavior;
   private RemoteViews mHeadsUpContentView;

   NotificationCompatBuilder(NotificationCompat.Builder var1) {
      this.mBuilderCompat = var1;
      if (VERSION.SDK_INT >= 26) {
         this.mBuilder = new Builder(var1.mContext, var1.mChannelId);
      } else {
         this.mBuilder = new Builder(var1.mContext);
      }

      Notification var2 = var1.mNotification;
      Builder var3 = this.mBuilder.setWhen(var2.when).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var1.mTickerView).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
      boolean var4;
      if ((var2.flags & 2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var3 = var3.setOngoing(var4);
      if ((var2.flags & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var3 = var3.setOnlyAlertOnce(var4);
      if ((var2.flags & 16) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      Builder var5 = var3.setAutoCancel(var4).setDefaults(var2.defaults).setContentTitle(var1.mContentTitle).setContentText(var1.mContentText).setContentInfo(var1.mContentInfo).setContentIntent(var1.mContentIntent).setDeleteIntent(var2.deleteIntent);
      PendingIntent var10 = var1.mFullScreenIntent;
      if ((var2.flags & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var5.setFullScreenIntent(var10, var4).setLargeIcon(var1.mLargeIcon).setNumber(var1.mNumber).setProgress(var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate);
      if (VERSION.SDK_INT < 21) {
         this.mBuilder.setSound(var2.sound, var2.audioStreamType);
      }

      Bundle var12;
      String var13;
      if (VERSION.SDK_INT >= 16) {
         this.mBuilder.setSubText(var1.mSubText).setUsesChronometer(var1.mUseChronometer).setPriority(var1.mPriority);
         Iterator var11 = var1.mActions.iterator();

         while(var11.hasNext()) {
            this.addAction((NotificationCompat.Action)var11.next());
         }

         var12 = var1.mExtras;
         if (var12 != null) {
            this.mExtras.putAll(var12);
         }

         if (VERSION.SDK_INT < 20) {
            if (var1.mLocalOnly) {
               this.mExtras.putBoolean("android.support.localOnly", true);
            }

            var13 = var1.mGroupKey;
            if (var13 != null) {
               this.mExtras.putString("android.support.groupKey", var13);
               if (var1.mGroupSummary) {
                  this.mExtras.putBoolean("android.support.isGroupSummary", true);
               } else {
                  this.mExtras.putBoolean("android.support.useSideChannel", true);
               }
            }

            var13 = var1.mSortKey;
            if (var13 != null) {
               this.mExtras.putString("android.support.sortKey", var13);
            }
         }

         this.mContentView = var1.mContentView;
         this.mBigContentView = var1.mBigContentView;
      }

      if (VERSION.SDK_INT >= 19) {
         this.mBuilder.setShowWhen(var1.mShowWhen);
         if (VERSION.SDK_INT < 21) {
            ArrayList var15 = var1.mPeople;
            if (var15 != null && !var15.isEmpty()) {
               var12 = this.mExtras;
               ArrayList var14 = var1.mPeople;
               var12.putStringArray("android.people", (String[])var14.toArray(new String[var14.size()]));
            }
         }
      }

      if (VERSION.SDK_INT >= 20) {
         this.mBuilder.setLocalOnly(var1.mLocalOnly).setGroup(var1.mGroupKey).setGroupSummary(var1.mGroupSummary).setSortKey(var1.mSortKey);
         this.mGroupAlertBehavior = var1.mGroupAlertBehavior;
      }

      if (VERSION.SDK_INT >= 21) {
         this.mBuilder.setCategory(var1.mCategory).setColor(var1.mColor).setVisibility(var1.mVisibility).setPublicVersion(var1.mPublicVersion).setSound(var2.sound, var2.audioAttributes);
         Iterator var7 = var1.mPeople.iterator();

         while(var7.hasNext()) {
            var13 = (String)var7.next();
            this.mBuilder.addPerson(var13);
         }

         this.mHeadsUpContentView = var1.mHeadsUpContentView;
         if (var1.mInvisibleActions.size() > 0) {
            var12 = var1.getExtras().getBundle("android.car.EXTENSIONS");
            Bundle var8 = var12;
            if (var12 == null) {
               var8 = new Bundle();
            }

            var12 = new Bundle();

            for(int var6 = 0; var6 < var1.mInvisibleActions.size(); ++var6) {
               var12.putBundle(Integer.toString(var6), NotificationCompatJellybean.getBundleForAction((NotificationCompat.Action)var1.mInvisibleActions.get(var6)));
            }

            var8.putBundle("invisible_actions", var12);
            var1.getExtras().putBundle("android.car.EXTENSIONS", var8);
            this.mExtras.putBundle("android.car.EXTENSIONS", var8);
         }
      }

      if (VERSION.SDK_INT >= 24) {
         this.mBuilder.setExtras(var1.mExtras).setRemoteInputHistory(var1.mRemoteInputHistory);
         RemoteViews var9 = var1.mContentView;
         if (var9 != null) {
            this.mBuilder.setCustomContentView(var9);
         }

         var9 = var1.mBigContentView;
         if (var9 != null) {
            this.mBuilder.setCustomBigContentView(var9);
         }

         var9 = var1.mHeadsUpContentView;
         if (var9 != null) {
            this.mBuilder.setCustomHeadsUpContentView(var9);
         }
      }

      if (VERSION.SDK_INT >= 26) {
         this.mBuilder.setBadgeIconType(var1.mBadgeIcon).setShortcutId(var1.mShortcutId).setTimeoutAfter(var1.mTimeout).setGroupAlertBehavior(var1.mGroupAlertBehavior);
         if (var1.mColorizedSet) {
            this.mBuilder.setColorized(var1.mColorized);
         }

         if (!TextUtils.isEmpty(var1.mChannelId)) {
            this.mBuilder.setSound((Uri)null).setDefaults(0).setLights(0, 0, 0).setVibrate((long[])null);
         }
      }

   }

   private void addAction(NotificationCompat.Action var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 >= 20) {
         android.app.Notification.Action.Builder var3 = new android.app.Notification.Action.Builder(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
         if (var1.getRemoteInputs() != null) {
            android.app.RemoteInput[] var4 = RemoteInput.fromCompat(var1.getRemoteInputs());
            int var5 = var4.length;

            for(var2 = 0; var2 < var5; ++var2) {
               var3.addRemoteInput(var4[var2]);
            }
         }

         Bundle var6;
         if (var1.getExtras() != null) {
            var6 = new Bundle(var1.getExtras());
         } else {
            var6 = new Bundle();
         }

         var6.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
         if (VERSION.SDK_INT >= 24) {
            var3.setAllowGeneratedReplies(var1.getAllowGeneratedReplies());
         }

         var6.putInt("android.support.action.semanticAction", var1.getSemanticAction());
         if (VERSION.SDK_INT >= 28) {
            var3.setSemanticAction(var1.getSemanticAction());
         }

         var6.putBoolean("android.support.action.showsUserInterface", var1.getShowsUserInterface());
         var3.addExtras(var6);
         this.mBuilder.addAction(var3.build());
      } else if (var2 >= 16) {
         this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, var1));
      }

   }

   private void removeSoundAndVibration(Notification var1) {
      var1.sound = null;
      var1.vibrate = null;
      var1.defaults &= -2;
      var1.defaults &= -3;
   }

   public Notification build() {
      NotificationCompat.Style var1 = this.mBuilderCompat.mStyle;
      if (var1 != null) {
         var1.apply(this);
      }

      RemoteViews var2;
      if (var1 != null) {
         var2 = var1.makeContentView(this);
      } else {
         var2 = null;
      }

      Notification var3 = this.buildInternal();
      if (var2 != null) {
         var3.contentView = var2;
      } else {
         var2 = this.mBuilderCompat.mContentView;
         if (var2 != null) {
            var3.contentView = var2;
         }
      }

      if (VERSION.SDK_INT >= 16 && var1 != null) {
         var2 = var1.makeBigContentView(this);
         if (var2 != null) {
            var3.bigContentView = var2;
         }
      }

      if (VERSION.SDK_INT >= 21 && var1 != null) {
         var2 = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
         if (var2 != null) {
            var3.headsUpContentView = var2;
         }
      }

      if (VERSION.SDK_INT >= 16 && var1 != null) {
         Bundle var4 = NotificationCompat.getExtras(var3);
         if (var4 != null) {
            var1.addCompatExtras(var4);
         }
      }

      return var3;
   }

   protected Notification buildInternal() {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 26) {
         return this.mBuilder.build();
      } else {
         Notification var2;
         if (var1 >= 24) {
            var2 = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
               if (var2.getGroup() != null && (var2.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                  this.removeSoundAndVibration(var2);
               }

               if (var2.getGroup() != null && (var2.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                  this.removeSoundAndVibration(var2);
               }
            }

            return var2;
         } else {
            RemoteViews var9;
            if (var1 >= 21) {
               this.mBuilder.setExtras(this.mExtras);
               var2 = this.mBuilder.build();
               var9 = this.mContentView;
               if (var9 != null) {
                  var2.contentView = var9;
               }

               var9 = this.mBigContentView;
               if (var9 != null) {
                  var2.bigContentView = var9;
               }

               var9 = this.mHeadsUpContentView;
               if (var9 != null) {
                  var2.headsUpContentView = var9;
               }

               if (this.mGroupAlertBehavior != 0) {
                  if (var2.getGroup() != null && (var2.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                     this.removeSoundAndVibration(var2);
                  }

                  if (var2.getGroup() != null && (var2.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                     this.removeSoundAndVibration(var2);
                  }
               }

               return var2;
            } else if (var1 >= 20) {
               this.mBuilder.setExtras(this.mExtras);
               var2 = this.mBuilder.build();
               var9 = this.mContentView;
               if (var9 != null) {
                  var2.contentView = var9;
               }

               var9 = this.mBigContentView;
               if (var9 != null) {
                  var2.bigContentView = var9;
               }

               if (this.mGroupAlertBehavior != 0) {
                  if (var2.getGroup() != null && (var2.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                     this.removeSoundAndVibration(var2);
                  }

                  if (var2.getGroup() != null && (var2.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                     this.removeSoundAndVibration(var2);
                  }
               }

               return var2;
            } else if (var1 >= 19) {
               SparseArray var7 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
               if (var7 != null) {
                  this.mExtras.putSparseParcelableArray("android.support.actionExtras", var7);
               }

               this.mBuilder.setExtras(this.mExtras);
               var2 = this.mBuilder.build();
               var9 = this.mContentView;
               if (var9 != null) {
                  var2.contentView = var9;
               }

               var9 = this.mBigContentView;
               if (var9 != null) {
                  var2.bigContentView = var9;
               }

               return var2;
            } else if (var1 >= 16) {
               var2 = this.mBuilder.build();
               Bundle var3 = NotificationCompat.getExtras(var2);
               Bundle var4 = new Bundle(this.mExtras);
               Iterator var5 = this.mExtras.keySet().iterator();

               while(var5.hasNext()) {
                  String var6 = (String)var5.next();
                  if (var3.containsKey(var6)) {
                     var4.remove(var6);
                  }
               }

               var3.putAll(var4);
               SparseArray var8 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
               if (var8 != null) {
                  NotificationCompat.getExtras(var2).putSparseParcelableArray("android.support.actionExtras", var8);
               }

               var9 = this.mContentView;
               if (var9 != null) {
                  var2.contentView = var9;
               }

               var9 = this.mBigContentView;
               if (var9 != null) {
                  var2.bigContentView = var9;
               }

               return var2;
            } else {
               return this.mBuilder.getNotification();
            }
         }
      }
   }

   public Builder getBuilder() {
      return this.mBuilder;
   }
}
