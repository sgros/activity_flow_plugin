package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import androidx.core.R$dimen;
import androidx.core.text.BidiFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NotificationCompat {
   public static Bundle getExtras(Notification var0) {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 19) {
         return var0.extras;
      } else {
         return var1 >= 16 ? NotificationCompatJellybean.getExtras(var0) : null;
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

      public static final class Builder {
         private boolean mAllowGeneratedReplies;
         private final Bundle mExtras;
         private final int mIcon;
         private final PendingIntent mIntent;
         private ArrayList mRemoteInputs;
         private int mSemanticAction;
         private boolean mShowsUserInterface;
         private final CharSequence mTitle;

         public Builder(int var1, CharSequence var2, PendingIntent var3) {
            this(var1, var2, var3, new Bundle(), (RemoteInput[])null, true, 0, true);
         }

         private Builder(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInput[] var5, boolean var6, int var7, boolean var8) {
            this.mAllowGeneratedReplies = true;
            this.mShowsUserInterface = true;
            this.mIcon = var1;
            this.mTitle = NotificationCompat.Builder.limitCharSequenceLength(var2);
            this.mIntent = var3;
            this.mExtras = var4;
            ArrayList var9;
            if (var5 == null) {
               var9 = null;
            } else {
               var9 = new ArrayList(Arrays.asList(var5));
            }

            this.mRemoteInputs = var9;
            this.mAllowGeneratedReplies = var6;
            this.mSemanticAction = var7;
            this.mShowsUserInterface = var8;
         }

         public NotificationCompat.Action.Builder addRemoteInput(RemoteInput var1) {
            if (this.mRemoteInputs == null) {
               this.mRemoteInputs = new ArrayList();
            }

            this.mRemoteInputs.add(var1);
            return this;
         }

         public NotificationCompat.Action build() {
            ArrayList var1 = new ArrayList();
            ArrayList var2 = new ArrayList();
            ArrayList var3 = this.mRemoteInputs;
            if (var3 != null) {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  RemoteInput var7 = (RemoteInput)var4.next();
                  if (var7.isDataOnly()) {
                     var1.add(var7);
                  } else {
                     var2.add(var7);
                  }
               }
            }

            boolean var5 = var1.isEmpty();
            RemoteInput[] var8 = null;
            RemoteInput[] var6;
            if (var5) {
               var6 = null;
            } else {
               var6 = (RemoteInput[])var1.toArray(new RemoteInput[var1.size()]);
            }

            if (!var2.isEmpty()) {
               var8 = (RemoteInput[])var2.toArray(new RemoteInput[var2.size()]);
            }

            return new NotificationCompat.Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, var8, var6, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface);
         }

         public NotificationCompat.Action.Builder setAllowGeneratedReplies(boolean var1) {
            this.mAllowGeneratedReplies = var1;
            return this;
         }

         public NotificationCompat.Action.Builder setSemanticAction(int var1) {
            this.mSemanticAction = var1;
            return this;
         }

         public NotificationCompat.Action.Builder setShowsUserInterface(boolean var1) {
            this.mShowsUserInterface = var1;
            return this;
         }
      }
   }

   public static class BigTextStyle extends NotificationCompat.Style {
      private CharSequence mBigText;

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            android.app.Notification.BigTextStyle var2 = (new android.app.Notification.BigTextStyle(var1.getBuilder())).setBigContentTitle(super.mBigContentTitle).bigText(this.mBigText);
            if (super.mSummaryTextSet) {
               var2.setSummaryText(super.mSummaryText);
            }
         }

      }

      public NotificationCompat.BigTextStyle bigText(CharSequence var1) {
         this.mBigText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }
   }

   public static class Builder {
      public ArrayList mActions;
      int mBadgeIcon;
      RemoteViews mBigContentView;
      String mCategory;
      String mChannelId;
      int mColor;
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
      int mGroupAlertBehavior;
      String mGroupKey;
      boolean mGroupSummary;
      RemoteViews mHeadsUpContentView;
      ArrayList mInvisibleActions;
      Bitmap mLargeIcon;
      boolean mLocalOnly;
      Notification mNotification;
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
      boolean mShowWhen;
      String mSortKey;
      NotificationCompat.Style mStyle;
      CharSequence mSubText;
      RemoteViews mTickerView;
      long mTimeout;
      boolean mUseChronometer;
      int mVisibility;

      @Deprecated
      public Builder(Context var1) {
         this(var1, (String)null);
      }

      public Builder(Context var1, String var2) {
         this.mActions = new ArrayList();
         this.mInvisibleActions = new ArrayList();
         this.mShowWhen = true;
         this.mLocalOnly = false;
         this.mColor = 0;
         this.mVisibility = 0;
         this.mBadgeIcon = 0;
         this.mGroupAlertBehavior = 0;
         this.mNotification = new Notification();
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
         Bitmap var2 = var1;
         if (var1 != null) {
            if (VERSION.SDK_INT >= 27) {
               var2 = var1;
            } else {
               Resources var11 = this.mContext.getResources();
               int var3 = var11.getDimensionPixelSize(R$dimen.compat_notification_large_icon_max_width);
               int var4 = var11.getDimensionPixelSize(R$dimen.compat_notification_large_icon_max_height);
               if (var1.getWidth() <= var3 && var1.getHeight() <= var4) {
                  return var1;
               }

               double var5 = (double)var3;
               double var7 = (double)Math.max(1, var1.getWidth());
               Double.isNaN(var5);
               Double.isNaN(var7);
               double var9 = var5 / var7;
               var7 = (double)var4;
               var5 = (double)Math.max(1, var1.getHeight());
               Double.isNaN(var7);
               Double.isNaN(var5);
               var7 = Math.min(var9, var7 / var5);
               var5 = (double)var1.getWidth();
               Double.isNaN(var5);
               var4 = (int)Math.ceil(var5 * var7);
               var5 = (double)var1.getHeight();
               Double.isNaN(var5);
               var2 = Bitmap.createScaledBitmap(var1, var4, (int)Math.ceil(var5 * var7), true);
            }
         }

         return var2;
      }

      private void setFlag(int var1, boolean var2) {
         Notification var3;
         if (var2) {
            var3 = this.mNotification;
            var3.flags |= var1;
         } else {
            var3 = this.mNotification;
            var3.flags &= ~var1;
         }

      }

      public NotificationCompat.Builder addAction(int var1, CharSequence var2, PendingIntent var3) {
         this.mActions.add(new NotificationCompat.Action(var1, var2, var3));
         return this;
      }

      public NotificationCompat.Builder addAction(NotificationCompat.Action var1) {
         this.mActions.add(var1);
         return this;
      }

      public NotificationCompat.Builder addPerson(String var1) {
         this.mPeople.add(var1);
         return this;
      }

      public Notification build() {
         return (new NotificationCompatBuilder(this)).build();
      }

      public NotificationCompat.Builder extend(NotificationCompat.Extender var1) {
         var1.extend(this);
         return this;
      }

      public int getColor() {
         return this.mColor;
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

      public NotificationCompat.Builder setCategory(String var1) {
         this.mCategory = var1;
         return this;
      }

      public NotificationCompat.Builder setChannelId(String var1) {
         this.mChannelId = var1;
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
         Notification var2 = this.mNotification;
         var2.defaults = var1;
         if ((var1 & 4) != 0) {
            var2.flags |= 1;
         }

         return this;
      }

      public NotificationCompat.Builder setDeleteIntent(PendingIntent var1) {
         this.mNotification.deleteIntent = var1;
         return this;
      }

      public NotificationCompat.Builder setGroup(String var1) {
         this.mGroupKey = var1;
         return this;
      }

      public NotificationCompat.Builder setGroupAlertBehavior(int var1) {
         this.mGroupAlertBehavior = var1;
         return this;
      }

      public NotificationCompat.Builder setGroupSummary(boolean var1) {
         this.mGroupSummary = var1;
         return this;
      }

      public NotificationCompat.Builder setLargeIcon(Bitmap var1) {
         this.mLargeIcon = this.reduceLargeIconSize(var1);
         return this;
      }

      public NotificationCompat.Builder setLights(int var1, int var2, int var3) {
         Notification var4 = this.mNotification;
         var4.ledARGB = var1;
         var4.ledOnMS = var2;
         var4.ledOffMS = var3;
         byte var5;
         if (var4.ledOnMS != 0 && var4.ledOffMS != 0) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var4 = this.mNotification;
         var4.flags = var5 | var4.flags & -2;
         return this;
      }

      public NotificationCompat.Builder setLocalOnly(boolean var1) {
         this.mLocalOnly = var1;
         return this;
      }

      public NotificationCompat.Builder setNumber(int var1) {
         this.mNumber = var1;
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

      public NotificationCompat.Builder setProgress(int var1, int var2, boolean var3) {
         this.mProgressMax = var1;
         this.mProgress = var2;
         this.mProgressIndeterminate = var3;
         return this;
      }

      public NotificationCompat.Builder setShortcutId(String var1) {
         this.mShortcutId = var1;
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

      public NotificationCompat.Builder setSortKey(String var1) {
         this.mSortKey = var1;
         return this;
      }

      public NotificationCompat.Builder setSound(Uri var1, int var2) {
         Notification var3 = this.mNotification;
         var3.sound = var1;
         var3.audioStreamType = var2;
         if (VERSION.SDK_INT >= 21) {
            var3.audioAttributes = (new android.media.AudioAttributes.Builder()).setContentType(4).setLegacyStreamType(var2).build();
         }

         return this;
      }

      public NotificationCompat.Builder setStyle(NotificationCompat.Style var1) {
         if (this.mStyle != var1) {
            this.mStyle = var1;
            var1 = this.mStyle;
            if (var1 != null) {
               var1.setBuilder(this);
            }
         }

         return this;
      }

      public NotificationCompat.Builder setSubText(CharSequence var1) {
         this.mSubText = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setTicker(CharSequence var1) {
         this.mNotification.tickerText = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setVibrate(long[] var1) {
         this.mNotification.vibrate = var1;
         return this;
      }

      public NotificationCompat.Builder setWhen(long var1) {
         this.mNotification.when = var1;
         return this;
      }
   }

   public interface Extender {
      NotificationCompat.Builder extend(NotificationCompat.Builder var1);
   }

   public static class InboxStyle extends NotificationCompat.Style {
      private ArrayList mTexts = new ArrayList();

      public NotificationCompat.InboxStyle addLine(CharSequence var1) {
         this.mTexts.add(NotificationCompat.Builder.limitCharSequenceLength(var1));
         return this;
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            android.app.Notification.InboxStyle var2 = (new android.app.Notification.InboxStyle(var1.getBuilder())).setBigContentTitle(super.mBigContentTitle);
            if (super.mSummaryTextSet) {
               var2.setSummaryText(super.mSummaryText);
            }

            Iterator var3 = this.mTexts.iterator();

            while(var3.hasNext()) {
               var2.addLine((CharSequence)var3.next());
            }
         }

      }

      public NotificationCompat.InboxStyle setBigContentTitle(CharSequence var1) {
         super.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.InboxStyle setSummaryText(CharSequence var1) {
         super.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         super.mSummaryTextSet = true;
         return this;
      }
   }

   public static class MessagingStyle extends NotificationCompat.Style {
      private CharSequence mConversationTitle;
      private Boolean mIsGroupConversation;
      private final List mMessages = new ArrayList();
      private Person mUser;

      @Deprecated
      public MessagingStyle(CharSequence var1) {
         Person.Builder var2 = new Person.Builder();
         var2.setName(var1);
         this.mUser = var2.build();
      }

      private NotificationCompat.MessagingStyle.Message findLatestIncomingMessage() {
         for(int var1 = this.mMessages.size() - 1; var1 >= 0; --var1) {
            NotificationCompat.MessagingStyle.Message var2 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var1);
            if (var2.getPerson() != null && !TextUtils.isEmpty(var2.getPerson().getName())) {
               return var2;
            }
         }

         if (!this.mMessages.isEmpty()) {
            List var3 = this.mMessages;
            return (NotificationCompat.MessagingStyle.Message)var3.get(var3.size() - 1);
         } else {
            return null;
         }
      }

      private boolean hasMessagesWithoutSender() {
         for(int var1 = this.mMessages.size() - 1; var1 >= 0; --var1) {
            NotificationCompat.MessagingStyle.Message var2 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var1);
            if (var2.getPerson() != null && var2.getPerson().getName() == null) {
               return true;
            }
         }

         return false;
      }

      private TextAppearanceSpan makeFontColorSpan(int var1) {
         return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(var1), (ColorStateList)null);
      }

      private CharSequence makeMessageLine(NotificationCompat.MessagingStyle.Message var1) {
         BidiFormatter var2 = BidiFormatter.getInstance();
         SpannableStringBuilder var3 = new SpannableStringBuilder();
         boolean var4;
         if (VERSION.SDK_INT >= 21) {
            var4 = true;
         } else {
            var4 = false;
         }

         int var5;
         if (var4) {
            var5 = -16777216;
         } else {
            var5 = -1;
         }

         Person var6 = var1.getPerson();
         String var7 = "";
         Object var11;
         if (var6 == null) {
            var11 = "";
         } else {
            var11 = var1.getPerson().getName();
         }

         int var8 = var5;
         Object var9 = var11;
         CharSequence var12;
         if (TextUtils.isEmpty((CharSequence)var11)) {
            var12 = this.mUser.getName();
            var8 = var5;
            var9 = var12;
            if (var4) {
               var8 = var5;
               var9 = var12;
               if (super.mBuilder.getColor() != 0) {
                  var8 = super.mBuilder.getColor();
                  var9 = var12;
               }
            }
         }

         var12 = var2.unicodeWrap((CharSequence)var9);
         var3.append(var12);
         var3.setSpan(this.makeFontColorSpan(var8), var3.length() - var12.length(), var3.length(), 33);
         Object var10;
         if (var1.getText() == null) {
            var10 = var7;
         } else {
            var10 = var1.getText();
         }

         var3.append("  ").append(var2.unicodeWrap((CharSequence)var10));
         return var3;
      }

      public void addCompatExtras(Bundle var1) {
         super.addCompatExtras(var1);
         var1.putCharSequence("android.selfDisplayName", this.mUser.getName());
         var1.putBundle("android.messagingStyleUser", this.mUser.toBundle());
         var1.putCharSequence("android.hiddenConversationTitle", this.mConversationTitle);
         if (this.mConversationTitle != null && this.mIsGroupConversation) {
            var1.putCharSequence("android.conversationTitle", this.mConversationTitle);
         }

         if (!this.mMessages.isEmpty()) {
            var1.putParcelableArray("android.messages", NotificationCompat.MessagingStyle.Message.getBundleArrayForMessages(this.mMessages));
         }

         Boolean var2 = this.mIsGroupConversation;
         if (var2 != null) {
            var1.putBoolean("android.isGroupConversation", var2);
         }

      }

      public NotificationCompat.MessagingStyle addMessage(NotificationCompat.MessagingStyle.Message var1) {
         this.mMessages.add(var1);
         if (this.mMessages.size() > 25) {
            this.mMessages.remove(0);
         }

         return this;
      }

      public NotificationCompat.MessagingStyle addMessage(CharSequence var1, long var2, Person var4) {
         this.addMessage(new NotificationCompat.MessagingStyle.Message(var1, var2, var4));
         return this;
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         this.setGroupConversation(this.isGroupConversation());
         int var2 = VERSION.SDK_INT;
         if (var2 >= 24) {
            android.app.Notification.MessagingStyle var3;
            if (var2 >= 28) {
               var3 = new android.app.Notification.MessagingStyle(this.mUser.toAndroidPerson());
            } else {
               var3 = new android.app.Notification.MessagingStyle(this.mUser.getName());
            }

            if (this.mIsGroupConversation || VERSION.SDK_INT >= 28) {
               var3.setConversationTitle(this.mConversationTitle);
            }

            if (VERSION.SDK_INT >= 28) {
               var3.setGroupConversation(this.mIsGroupConversation);
            }

            android.app.Notification.MessagingStyle.Message var15;
            for(Iterator var4 = this.mMessages.iterator(); var4.hasNext(); var3.addMessage(var15)) {
               NotificationCompat.MessagingStyle.Message var5 = (NotificationCompat.MessagingStyle.Message)var4.next();
               if (VERSION.SDK_INT >= 28) {
                  Person var6 = var5.getPerson();
                  CharSequence var7 = var5.getText();
                  long var8 = var5.getTimestamp();
                  android.app.Person var14;
                  if (var6 == null) {
                     var14 = null;
                  } else {
                     var14 = var6.toAndroidPerson();
                  }

                  var15 = new android.app.Notification.MessagingStyle.Message(var7, var8, var14);
               } else {
                  CharSequence var16;
                  if (var5.getPerson() != null) {
                     var16 = var5.getPerson().getName();
                  } else {
                     var16 = null;
                  }

                  var15 = new android.app.Notification.MessagingStyle.Message(var5.getText(), var5.getTimestamp(), var16);
               }

               if (var5.getDataMimeType() != null) {
                  var15.setData(var5.getDataMimeType(), var5.getDataUri());
               }
            }

            var3.setBuilder(var1.getBuilder());
         } else {
            NotificationCompat.MessagingStyle.Message var12 = this.findLatestIncomingMessage();
            if (this.mConversationTitle != null && this.mIsGroupConversation) {
               var1.getBuilder().setContentTitle(this.mConversationTitle);
            } else if (var12 != null) {
               var1.getBuilder().setContentTitle("");
               if (var12.getPerson() != null) {
                  var1.getBuilder().setContentTitle(var12.getPerson().getName());
               }
            }

            CharSequence var13;
            if (var12 != null) {
               android.app.Notification.Builder var17 = var1.getBuilder();
               if (this.mConversationTitle != null) {
                  var13 = this.makeMessageLine(var12);
               } else {
                  var13 = var12.getText();
               }

               var17.setContentText(var13);
            }

            if (VERSION.SDK_INT >= 16) {
               SpannableStringBuilder var18 = new SpannableStringBuilder();
               boolean var11;
               if (this.mConversationTitle == null && !this.hasMessagesWithoutSender()) {
                  var11 = false;
               } else {
                  var11 = true;
               }

               for(int var10 = this.mMessages.size() - 1; var10 >= 0; --var10) {
                  var12 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var10);
                  if (var11) {
                     var13 = this.makeMessageLine(var12);
                  } else {
                     var13 = var12.getText();
                  }

                  if (var10 != this.mMessages.size() - 1) {
                     var18.insert(0, "\n");
                  }

                  var18.insert(0, var13);
               }

               (new android.app.Notification.BigTextStyle(var1.getBuilder())).setBigContentTitle((CharSequence)null).bigText(var18);
            }
         }

      }

      public List getMessages() {
         return this.mMessages;
      }

      public boolean isGroupConversation() {
         NotificationCompat.Builder var1 = super.mBuilder;
         boolean var2 = false;
         boolean var3 = false;
         if (var1 != null && var1.mContext.getApplicationInfo().targetSdkVersion < 28 && this.mIsGroupConversation == null) {
            if (this.mConversationTitle != null) {
               var3 = true;
            }

            return var3;
         } else {
            Boolean var4 = this.mIsGroupConversation;
            var3 = var2;
            if (var4 != null) {
               var3 = var4;
            }

            return var3;
         }
      }

      public NotificationCompat.MessagingStyle setConversationTitle(CharSequence var1) {
         this.mConversationTitle = var1;
         return this;
      }

      public NotificationCompat.MessagingStyle setGroupConversation(boolean var1) {
         this.mIsGroupConversation = var1;
         return this;
      }

      public static final class Message {
         private String mDataMimeType;
         private Uri mDataUri;
         private Bundle mExtras = new Bundle();
         private final Person mPerson;
         private final CharSequence mText;
         private final long mTimestamp;

         public Message(CharSequence var1, long var2, Person var4) {
            this.mText = var1;
            this.mTimestamp = var2;
            this.mPerson = var4;
         }

         static Bundle[] getBundleArrayForMessages(List var0) {
            Bundle[] var1 = new Bundle[var0.size()];
            int var2 = var0.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               var1[var3] = ((NotificationCompat.MessagingStyle.Message)var0.get(var3)).toBundle();
            }

            return var1;
         }

         private Bundle toBundle() {
            Bundle var1 = new Bundle();
            CharSequence var2 = this.mText;
            if (var2 != null) {
               var1.putCharSequence("text", var2);
            }

            var1.putLong("time", this.mTimestamp);
            Person var3 = this.mPerson;
            if (var3 != null) {
               var1.putCharSequence("sender", var3.getName());
               if (VERSION.SDK_INT >= 28) {
                  var1.putParcelable("sender_person", this.mPerson.toAndroidPerson());
               } else {
                  var1.putBundle("person", this.mPerson.toBundle());
               }
            }

            String var4 = this.mDataMimeType;
            if (var4 != null) {
               var1.putString("type", var4);
            }

            Uri var5 = this.mDataUri;
            if (var5 != null) {
               var1.putParcelable("uri", var5);
            }

            Bundle var6 = this.mExtras;
            if (var6 != null) {
               var1.putBundle("extras", var6);
            }

            return var1;
         }

         public String getDataMimeType() {
            return this.mDataMimeType;
         }

         public Uri getDataUri() {
            return this.mDataUri;
         }

         public Person getPerson() {
            return this.mPerson;
         }

         public CharSequence getText() {
            return this.mText;
         }

         public long getTimestamp() {
            return this.mTimestamp;
         }

         public NotificationCompat.MessagingStyle.Message setData(String var1, Uri var2) {
            this.mDataMimeType = var1;
            this.mDataUri = var2;
            return this;
         }
      }
   }

   public abstract static class Style {
      CharSequence mBigContentTitle;
      protected NotificationCompat.Builder mBuilder;
      CharSequence mSummaryText;
      boolean mSummaryTextSet = false;

      public void addCompatExtras(Bundle var1) {
      }

      public abstract void apply(NotificationBuilderWithBuilderAccessor var1);

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
            var1 = this.mBuilder;
            if (var1 != null) {
               var1.setStyle(this);
            }
         }

      }
   }

   public static final class WearableExtender implements NotificationCompat.Extender {
      private ArrayList mActions = new ArrayList();
      private Bitmap mBackground;
      private String mBridgeTag;
      private int mContentActionIndex = -1;
      private int mContentIcon;
      private int mContentIconGravity = 8388613;
      private int mCustomContentHeight;
      private int mCustomSizePreset = 0;
      private String mDismissalId;
      private PendingIntent mDisplayIntent;
      private int mFlags = 1;
      private int mGravity = 80;
      private int mHintScreenTimeout;
      private ArrayList mPages = new ArrayList();

      private static android.app.Notification.Action getActionFromActionCompat(NotificationCompat.Action var0) {
         android.app.Notification.Action.Builder var1 = new android.app.Notification.Action.Builder(var0.getIcon(), var0.getTitle(), var0.getActionIntent());
         Bundle var2;
         if (var0.getExtras() != null) {
            var2 = new Bundle(var0.getExtras());
         } else {
            var2 = new Bundle();
         }

         var2.putBoolean("android.support.allowGeneratedReplies", var0.getAllowGeneratedReplies());
         if (VERSION.SDK_INT >= 24) {
            var1.setAllowGeneratedReplies(var0.getAllowGeneratedReplies());
         }

         var1.addExtras(var2);
         RemoteInput[] var5 = var0.getRemoteInputs();
         if (var5 != null) {
            android.app.RemoteInput[] var6 = RemoteInput.fromCompat(var5);
            int var3 = var6.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               var1.addRemoteInput(var6[var4]);
            }
         }

         return var1.build();
      }

      public NotificationCompat.WearableExtender addAction(NotificationCompat.Action var1) {
         this.mActions.add(var1);
         return this;
      }

      public NotificationCompat.WearableExtender clone() {
         NotificationCompat.WearableExtender var1 = new NotificationCompat.WearableExtender();
         var1.mActions = new ArrayList(this.mActions);
         var1.mFlags = this.mFlags;
         var1.mDisplayIntent = this.mDisplayIntent;
         var1.mPages = new ArrayList(this.mPages);
         var1.mBackground = this.mBackground;
         var1.mContentIcon = this.mContentIcon;
         var1.mContentIconGravity = this.mContentIconGravity;
         var1.mContentActionIndex = this.mContentActionIndex;
         var1.mCustomSizePreset = this.mCustomSizePreset;
         var1.mCustomContentHeight = this.mCustomContentHeight;
         var1.mGravity = this.mGravity;
         var1.mHintScreenTimeout = this.mHintScreenTimeout;
         var1.mDismissalId = this.mDismissalId;
         var1.mBridgeTag = this.mBridgeTag;
         return var1;
      }

      public NotificationCompat.Builder extend(NotificationCompat.Builder var1) {
         Bundle var2 = new Bundle();
         ArrayList var3;
         int var6;
         if (!this.mActions.isEmpty()) {
            if (VERSION.SDK_INT >= 16) {
               var3 = new ArrayList(this.mActions.size());
               Iterator var4 = this.mActions.iterator();

               while(var4.hasNext()) {
                  NotificationCompat.Action var5 = (NotificationCompat.Action)var4.next();
                  var6 = VERSION.SDK_INT;
                  if (var6 >= 20) {
                     var3.add(getActionFromActionCompat(var5));
                  } else if (var6 >= 16) {
                     var3.add(NotificationCompatJellybean.getBundleForAction(var5));
                  }
               }

               var2.putParcelableArrayList("actions", var3);
            } else {
               var2.putParcelableArrayList("actions", (ArrayList)null);
            }
         }

         var6 = this.mFlags;
         if (var6 != 1) {
            var2.putInt("flags", var6);
         }

         PendingIntent var7 = this.mDisplayIntent;
         if (var7 != null) {
            var2.putParcelable("displayIntent", var7);
         }

         if (!this.mPages.isEmpty()) {
            var3 = this.mPages;
            var2.putParcelableArray("pages", (Parcelable[])var3.toArray(new Notification[var3.size()]));
         }

         Bitmap var8 = this.mBackground;
         if (var8 != null) {
            var2.putParcelable("background", var8);
         }

         var6 = this.mContentIcon;
         if (var6 != 0) {
            var2.putInt("contentIcon", var6);
         }

         var6 = this.mContentIconGravity;
         if (var6 != 8388613) {
            var2.putInt("contentIconGravity", var6);
         }

         var6 = this.mContentActionIndex;
         if (var6 != -1) {
            var2.putInt("contentActionIndex", var6);
         }

         var6 = this.mCustomSizePreset;
         if (var6 != 0) {
            var2.putInt("customSizePreset", var6);
         }

         var6 = this.mCustomContentHeight;
         if (var6 != 0) {
            var2.putInt("customContentHeight", var6);
         }

         var6 = this.mGravity;
         if (var6 != 80) {
            var2.putInt("gravity", var6);
         }

         var6 = this.mHintScreenTimeout;
         if (var6 != 0) {
            var2.putInt("hintScreenTimeout", var6);
         }

         String var9 = this.mDismissalId;
         if (var9 != null) {
            var2.putString("dismissalId", var9);
         }

         var9 = this.mBridgeTag;
         if (var9 != null) {
            var2.putString("bridgeTag", var9);
         }

         var1.getExtras().putBundle("android.wearable.EXTENSIONS", var2);
         return var1;
      }

      public NotificationCompat.WearableExtender setBridgeTag(String var1) {
         this.mBridgeTag = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setDismissalId(String var1) {
         this.mDismissalId = var1;
         return this;
      }
   }
}
