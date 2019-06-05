// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.content.res.Resources;
import android.support.compat.R;
import android.graphics.Bitmap;
import android.content.Context;
import android.widget.RemoteViews;
import java.util.ArrayList;
import android.app.Notification$BigTextStyle;
import android.app.PendingIntent;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.app.Notification;

public class NotificationCompat
{
    public static Bundle getExtras(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 19) {
            return notification.extras;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        }
        return null;
    }
    
    public static class Action
    {
        public PendingIntent actionIntent;
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;
        
        public Action(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this(n, charSequence, pendingIntent, new Bundle(), null, null, true, 0, true);
        }
        
        Action(final int icon, final CharSequence charSequence, final PendingIntent actionIntent, Bundle mExtras, final RemoteInput[] mRemoteInputs, final RemoteInput[] mDataOnlyRemoteInputs, final boolean mAllowGeneratedReplies, final int mSemanticAction, final boolean mShowsUserInterface) {
            this.mShowsUserInterface = true;
            this.icon = icon;
            this.title = Builder.limitCharSequenceLength(charSequence);
            this.actionIntent = actionIntent;
            if (mExtras == null) {
                mExtras = new Bundle();
            }
            this.mExtras = mExtras;
            this.mRemoteInputs = mRemoteInputs;
            this.mDataOnlyRemoteInputs = mDataOnlyRemoteInputs;
            this.mAllowGeneratedReplies = mAllowGeneratedReplies;
            this.mSemanticAction = mSemanticAction;
            this.mShowsUserInterface = mShowsUserInterface;
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
    
    public static class BigTextStyle extends Style
    {
        private CharSequence mBigText;
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                final Notification$BigTextStyle bigText = new Notification$BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
                if (this.mSummaryTextSet) {
                    bigText.setSummaryText(this.mSummaryText);
                }
            }
        }
        
        public BigTextStyle bigText(final CharSequence charSequence) {
            this.mBigText = Builder.limitCharSequenceLength(charSequence);
            return this;
        }
    }
    
    public static class Builder
    {
        public ArrayList<Action> mActions;
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
        ArrayList<Action> mInvisibleActions;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        Notification mNotification;
        int mNumber;
        @Deprecated
        public ArrayList<String> mPeople;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        String mShortcutId;
        boolean mShowWhen;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility;
        
        public Builder(final Context mContext, final String mChannelId) {
            this.mActions = new ArrayList<Action>();
            this.mInvisibleActions = new ArrayList<Action>();
            this.mShowWhen = true;
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            this.mNotification = new Notification();
            this.mContext = mContext;
            this.mChannelId = mChannelId;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList<String>();
        }
        
        protected static CharSequence limitCharSequenceLength(final CharSequence charSequence) {
            if (charSequence == null) {
                return charSequence;
            }
            CharSequence subSequence = charSequence;
            if (charSequence.length() > 5120) {
                subSequence = charSequence.subSequence(0, 5120);
            }
            return subSequence;
        }
        
        private Bitmap reduceLargeIconSize(final Bitmap bitmap) {
            if (bitmap == null || Build$VERSION.SDK_INT >= 27) {
                return bitmap;
            }
            final Resources resources = this.mContext.getResources();
            final int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_width);
            final int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_height);
            if (bitmap.getWidth() <= dimensionPixelSize && bitmap.getHeight() <= dimensionPixelSize2) {
                return bitmap;
            }
            final double min = Math.min(dimensionPixelSize / (double)Math.max(1, bitmap.getWidth()), dimensionPixelSize2 / (double)Math.max(1, bitmap.getHeight()));
            return Bitmap.createScaledBitmap(bitmap, (int)Math.ceil(bitmap.getWidth() * min), (int)Math.ceil(bitmap.getHeight() * min), true);
        }
        
        private void setFlag(final int n, final boolean b) {
            if (b) {
                final Notification mNotification = this.mNotification;
                mNotification.flags |= n;
            }
            else {
                final Notification mNotification2 = this.mNotification;
                mNotification2.flags &= n;
            }
        }
        
        public Builder addAction(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this.mActions.add(new Action(n, charSequence, pendingIntent));
            return this;
        }
        
        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }
        
        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }
        
        public Builder setAutoCancel(final boolean b) {
            this.setFlag(16, b);
            return this;
        }
        
        public Builder setColor(final int mColor) {
            this.mColor = mColor;
            return this;
        }
        
        public Builder setContentIntent(final PendingIntent mContentIntent) {
            this.mContentIntent = mContentIntent;
            return this;
        }
        
        public Builder setContentText(final CharSequence charSequence) {
            this.mContentText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setContentTitle(final CharSequence charSequence) {
            this.mContentTitle = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setDefaults(final int defaults) {
            this.mNotification.defaults = defaults;
            if ((defaults & 0x4) != 0x0) {
                final Notification mNotification = this.mNotification;
                mNotification.flags |= 0x1;
            }
            return this;
        }
        
        public Builder setLargeIcon(final Bitmap bitmap) {
            this.mLargeIcon = this.reduceLargeIconSize(bitmap);
            return this;
        }
        
        public Builder setOngoing(final boolean b) {
            this.setFlag(2, b);
            return this;
        }
        
        public Builder setPriority(final int mPriority) {
            this.mPriority = mPriority;
            return this;
        }
        
        public Builder setShowWhen(final boolean mShowWhen) {
            this.mShowWhen = mShowWhen;
            return this;
        }
        
        public Builder setSmallIcon(final int icon) {
            this.mNotification.icon = icon;
            return this;
        }
        
        public Builder setStyle(final Style mStyle) {
            if (this.mStyle != mStyle) {
                this.mStyle = mStyle;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                }
            }
            return this;
        }
    }
    
    public abstract static class Style
    {
        CharSequence mBigContentTitle;
        protected Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;
        
        public Style() {
            this.mSummaryTextSet = false;
        }
        
        public void addCompatExtras(final Bundle bundle) {
        }
        
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
        }
        
        public RemoteViews makeBigContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public RemoteViews makeContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public RemoteViews makeHeadsUpContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public void setBuilder(final Builder mBuilder) {
            if (this.mBuilder != mBuilder) {
                this.mBuilder = mBuilder;
                if (this.mBuilder != null) {
                    this.mBuilder.setStyle(this);
                }
            }
        }
    }
}
