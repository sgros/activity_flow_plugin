package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.compat.R;
import android.widget.RemoteViews;
import java.util.ArrayList;

public class NotificationCompat {
   public static Bundle getExtras(Notification var0) {
      if (VERSION.SDK_INT >= 19) {
         return var0.extras;
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getExtras(var0) : null;
      }
   }

   public static class Action {
      public PendingIntent actionIntent;
      public int icon;
      private boolean mAllowGeneratedReplies;
      private final RemoteInput[] mDataOnlyRemoteInputs;
      final Bundle mExtras;
      private final RemoteInput[] mRemoteInputs;
      private final int mSemanticAction;
      boolean mShowsUserInterface;
      public CharSequence title;

      public Action(int var1, CharSequence var2, PendingIntent var3) {
         this(var1, var2, var3, new Bundle(), (RemoteInput[])null, (RemoteInput[])null, true, 0, true);
      }

      Action(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInput[] var5, RemoteInput[] var6, boolean var7, int var8, boolean var9) {
         this.mShowsUserInterface = true;
         this.icon = var1;
         this.title = NotificationCompat.Builder.limitCharSequenceLength(var2);
         this.actionIntent = var3;
         if (var4 == null) {
            var4 = new Bundle();
         }

         this.mExtras = var4;
         this.mRemoteInputs = var5;
         this.mDataOnlyRemoteInputs = var6;
         this.mAllowGeneratedReplies = var7;
         this.mSemanticAction = var8;
         this.mShowsUserInterface = var9;
      }

      public PendingIntent getActionIntent() {
         return this.actionIntent;
      }

      public boolean getAllowGeneratedReplies() {
         return this.mAllowGeneratedReplies;
      }

      public RemoteInput[] getDataOnlyRemoteInputs() {
         return this.mDataOnlyRemoteInputs;
      }

      public Bundle getExtras() {
         return this.mExtras;
      }

      public int getIcon() {
         return this.icon;
      }

      public RemoteInput[] getRemoteInputs() {
         return this.mRemoteInputs;
      }

      public int getSemanticAction() {
         return this.mSemanticAction;
      }

      public boolean getShowsUserInterface() {
         return this.mShowsUserInterface;
      }

      public CharSequence getTitle() {
         return this.title;
      }
   }

   public static class BigTextStyle extends NotificationCompat.Style {
      private CharSequence mBigText;

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            android.app.Notification.BigTextStyle var2 = (new android.app.Notification.BigTextStyle(var1.getBuilder())).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
            if (this.mSummaryTextSet) {
               var2.setSummaryText(this.mSummaryText);
            }
         }

      }

      public NotificationCompat.BigTextStyle bigText(CharSequence var1) {
         this.mBigText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }
   }

   public static class Builder {
      public ArrayList mActions = new ArrayList();
      int mBadgeIcon = 0;
      RemoteViews mBigContentView;
      String mCategory;
      String mChannelId;
      int mColor = 0;
      boolean mColorized;
      boolean mColorizedSet;
      CharSequence mContentInfo;
      PendingIntent mContentIntent;
      CharSequence mContentText;
      CharSequence mContentTitle;
      RemoteViews mContentView;
      public Context mContext;
      Bundle mExtras;
      PendingIntent mFullScreenIntent;
      int mGroupAlertBehavior = 0;
      String mGroupKey;
      boolean mGroupSummary;
      RemoteViews mHeadsUpContentView;
      ArrayList mInvisibleActions = new ArrayList();
      Bitmap mLargeIcon;
      boolean mLocalOnly = false;
      Notification mNotification = new Notification();
      int mNumber;
      @Deprecated
      public ArrayList mPeople;
      int mPriority;
      int mProgress;
      boolean mProgressIndeterminate;
      int mProgressMax;
      Notification mPublicVersion;
      CharSequence[] mRemoteInputHistory;
      String mShortcutId;
      boolean mShowWhen = true;
      String mSortKey;
      NotificationCompat.Style mStyle;
      CharSequence mSubText;
      RemoteViews mTickerView;
      long mTimeout;
      boolean mUseChronometer;
      int mVisibility = 0;

      public Builder(Context var1, String var2) {
         this.mContext = var1;
         this.mChannelId = var2;
         this.mNotification.when = System.currentTimeMillis();
         this.mNotification.audioStreamType = -1;
         this.mPriority = 0;
         this.mPeople = new ArrayList();
      }

      protected static CharSequence limitCharSequenceLength(CharSequence var0) {
         if (var0 == null) {
            return var0;
         } else {
            CharSequence var1 = var0;
            if (var0.length() > 5120) {
               var1 = var0.subSequence(0, 5120);
            }

            return var1;
         }
      }

      private Bitmap reduceLargeIconSize(Bitmap var1) {
         if (var1 != null && VERSION.SDK_INT < 27) {
            Resources var2 = this.mContext.getResources();
            int var3 = var2.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_width);
            int var4 = var2.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_height);
            if (var1.getWidth() <= var3 && var1.getHeight() <= var4) {
               return var1;
            } else {
               double var5 = Math.min((double)var3 / (double)Math.max(1, var1.getWidth()), (double)var4 / (double)Math.max(1, var1.getHeight()));
               return Bitmap.createScaledBitmap(var1, (int)Math.ceil((double)var1.getWidth() * var5), (int)Math.ceil((double)var1.getHeight() * var5), true);
            }
         } else {
            return var1;
         }
      }

      private void setFlag(int var1, boolean var2) {
         Notification var3;
         if (var2) {
            var3 = this.mNotification;
            var3.flags |= var1;
         } else {
            var3 = this.mNotification;
            var3.flags &= var1;
         }

      }

      public NotificationCompat.Builder addAction(int var1, CharSequence var2, PendingIntent var3) {
         this.mActions.add(new NotificationCompat.Action(var1, var2, var3));
         return this;
      }

      public Notification build() {
         return (new NotificationCompatBuilder(this)).build();
      }

      public Bundle getExtras() {
         if (this.mExtras == null) {
            this.mExtras = new Bundle();
         }

         return this.mExtras;
      }

      public NotificationCompat.Builder setAutoCancel(boolean var1) {
         this.setFlag(16, var1);
         return this;
      }

      public NotificationCompat.Builder setColor(int var1) {
         this.mColor = var1;
         return this;
      }

      public NotificationCompat.Builder setContentIntent(PendingIntent var1) {
         this.mContentIntent = var1;
         return this;
      }

      public NotificationCompat.Builder setContentText(CharSequence var1) {
         this.mContentText = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setContentTitle(CharSequence var1) {
         this.mContentTitle = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setDefaults(int var1) {
         this.mNotification.defaults = var1;
         if ((var1 & 4) != 0) {
            Notification var2 = this.mNotification;
            var2.flags |= 1;
         }

         return this;
      }

      public NotificationCompat.Builder setLargeIcon(Bitmap var1) {
         this.mLargeIcon = this.reduceLargeIconSize(var1);
         return this;
      }

      public NotificationCompat.Builder setOngoing(boolean var1) {
         this.setFlag(2, var1);
         return this;
      }

      public NotificationCompat.Builder setPriority(int var1) {
         this.mPriority = var1;
         return this;
      }

      public NotificationCompat.Builder setShowWhen(boolean var1) {
         this.mShowWhen = var1;
         return this;
      }

      public NotificationCompat.Builder setSmallIcon(int var1) {
         this.mNotification.icon = var1;
         return this;
      }

      public NotificationCompat.Builder setStyle(NotificationCompat.Style var1) {
         if (this.mStyle != var1) {
            this.mStyle = var1;
            if (this.mStyle != null) {
               this.mStyle.setBuilder(this);
            }
         }

         return this;
      }
   }

   public abstract static class Style {
      CharSequence mBigContentTitle;
      protected NotificationCompat.Builder mBuilder;
      CharSequence mSummaryText;
      boolean mSummaryTextSet = false;

      public void addCompatExtras(Bundle var1) {
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
      }

      public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      public void setBuilder(NotificationCompat.Builder var1) {
         if (this.mBuilder != var1) {
            this.mBuilder = var1;
            if (this.mBuilder != null) {
               this.mBuilder.setStyle(this);
            }
         }

      }
   }
}
