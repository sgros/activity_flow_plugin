// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.app;

import android.util.SparseArray;
import android.app.Notification$Action$Builder;
import java.util.Iterator;
import android.app.Notification;
import android.net.Uri;
import android.text.TextUtils;
import android.os.Build$VERSION;
import java.util.ArrayList;
import android.app.Notification$Builder;
import android.widget.RemoteViews;
import android.os.Bundle;
import java.util.List;

class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor
{
    private final List<Bundle> mActionExtrasList;
    private RemoteViews mBigContentView;
    private final Notification$Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Bundle mExtras;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;
    
    NotificationCompatBuilder(final NotificationCompat.Builder mBuilderCompat) {
        this.mActionExtrasList = new ArrayList<Bundle>();
        this.mExtras = new Bundle();
        this.mBuilderCompat = mBuilderCompat;
        if (Build$VERSION.SDK_INT >= 26) {
            this.mBuilder = new Notification$Builder(mBuilderCompat.mContext, mBuilderCompat.mChannelId);
        }
        else {
            this.mBuilder = new Notification$Builder(mBuilderCompat.mContext);
        }
        final Notification mNotification = mBuilderCompat.mNotification;
        this.mBuilder.setWhen(mNotification.when).setSmallIcon(mNotification.icon, mNotification.iconLevel).setContent(mNotification.contentView).setTicker(mNotification.tickerText, mBuilderCompat.mTickerView).setVibrate(mNotification.vibrate).setLights(mNotification.ledARGB, mNotification.ledOnMS, mNotification.ledOffMS).setOngoing((mNotification.flags & 0x2) != 0x0).setOnlyAlertOnce((mNotification.flags & 0x8) != 0x0).setAutoCancel((mNotification.flags & 0x10) != 0x0).setDefaults(mNotification.defaults).setContentTitle(mBuilderCompat.mContentTitle).setContentText(mBuilderCompat.mContentText).setContentInfo(mBuilderCompat.mContentInfo).setContentIntent(mBuilderCompat.mContentIntent).setDeleteIntent(mNotification.deleteIntent).setFullScreenIntent(mBuilderCompat.mFullScreenIntent, (mNotification.flags & 0x80) != 0x0).setLargeIcon(mBuilderCompat.mLargeIcon).setNumber(mBuilderCompat.mNumber).setProgress(mBuilderCompat.mProgressMax, mBuilderCompat.mProgress, mBuilderCompat.mProgressIndeterminate);
        if (Build$VERSION.SDK_INT < 21) {
            this.mBuilder.setSound(mNotification.sound, mNotification.audioStreamType);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            this.mBuilder.setSubText(mBuilderCompat.mSubText).setUsesChronometer(mBuilderCompat.mUseChronometer).setPriority(mBuilderCompat.mPriority);
            final Iterator<NotificationCompat.Action> iterator = mBuilderCompat.mActions.iterator();
            while (iterator.hasNext()) {
                this.addAction(iterator.next());
            }
            final Bundle mExtras = mBuilderCompat.mExtras;
            if (mExtras != null) {
                this.mExtras.putAll(mExtras);
            }
            if (Build$VERSION.SDK_INT < 20) {
                if (mBuilderCompat.mLocalOnly) {
                    this.mExtras.putBoolean("android.support.localOnly", true);
                }
                final String mGroupKey = mBuilderCompat.mGroupKey;
                if (mGroupKey != null) {
                    this.mExtras.putString("android.support.groupKey", mGroupKey);
                    if (mBuilderCompat.mGroupSummary) {
                        this.mExtras.putBoolean("android.support.isGroupSummary", true);
                    }
                    else {
                        this.mExtras.putBoolean("android.support.useSideChannel", true);
                    }
                }
                final String mSortKey = mBuilderCompat.mSortKey;
                if (mSortKey != null) {
                    this.mExtras.putString("android.support.sortKey", mSortKey);
                }
            }
            this.mContentView = mBuilderCompat.mContentView;
            this.mBigContentView = mBuilderCompat.mBigContentView;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            this.mBuilder.setShowWhen(mBuilderCompat.mShowWhen);
            if (Build$VERSION.SDK_INT < 21) {
                final ArrayList<String> mPeople = mBuilderCompat.mPeople;
                if (mPeople != null && !mPeople.isEmpty()) {
                    final Bundle mExtras2 = this.mExtras;
                    final ArrayList<String> mPeople2 = mBuilderCompat.mPeople;
                    mExtras2.putStringArray("android.people", (String[])mPeople2.toArray(new String[mPeople2.size()]));
                }
            }
        }
        if (Build$VERSION.SDK_INT >= 20) {
            this.mBuilder.setLocalOnly(mBuilderCompat.mLocalOnly).setGroup(mBuilderCompat.mGroupKey).setGroupSummary(mBuilderCompat.mGroupSummary).setSortKey(mBuilderCompat.mSortKey);
            this.mGroupAlertBehavior = mBuilderCompat.mGroupAlertBehavior;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mBuilder.setCategory(mBuilderCompat.mCategory).setColor(mBuilderCompat.mColor).setVisibility(mBuilderCompat.mVisibility).setPublicVersion(mBuilderCompat.mPublicVersion).setSound(mNotification.sound, mNotification.audioAttributes);
            final Iterator<String> iterator2 = mBuilderCompat.mPeople.iterator();
            while (iterator2.hasNext()) {
                this.mBuilder.addPerson((String)iterator2.next());
            }
            this.mHeadsUpContentView = mBuilderCompat.mHeadsUpContentView;
            if (mBuilderCompat.mInvisibleActions.size() > 0) {
                Bundle bundle;
                if ((bundle = mBuilderCompat.getExtras().getBundle("android.car.EXTENSIONS")) == null) {
                    bundle = new Bundle();
                }
                final Bundle bundle2 = new Bundle();
                for (int i = 0; i < mBuilderCompat.mInvisibleActions.size(); ++i) {
                    bundle2.putBundle(Integer.toString(i), NotificationCompatJellybean.getBundleForAction(mBuilderCompat.mInvisibleActions.get(i)));
                }
                bundle.putBundle("invisible_actions", bundle2);
                mBuilderCompat.getExtras().putBundle("android.car.EXTENSIONS", bundle);
                this.mExtras.putBundle("android.car.EXTENSIONS", bundle);
            }
        }
        if (Build$VERSION.SDK_INT >= 24) {
            this.mBuilder.setExtras(mBuilderCompat.mExtras).setRemoteInputHistory(mBuilderCompat.mRemoteInputHistory);
            final RemoteViews mContentView = mBuilderCompat.mContentView;
            if (mContentView != null) {
                this.mBuilder.setCustomContentView(mContentView);
            }
            final RemoteViews mBigContentView = mBuilderCompat.mBigContentView;
            if (mBigContentView != null) {
                this.mBuilder.setCustomBigContentView(mBigContentView);
            }
            final RemoteViews mHeadsUpContentView = mBuilderCompat.mHeadsUpContentView;
            if (mHeadsUpContentView != null) {
                this.mBuilder.setCustomHeadsUpContentView(mHeadsUpContentView);
            }
        }
        if (Build$VERSION.SDK_INT >= 26) {
            this.mBuilder.setBadgeIconType(mBuilderCompat.mBadgeIcon).setShortcutId(mBuilderCompat.mShortcutId).setTimeoutAfter(mBuilderCompat.mTimeout).setGroupAlertBehavior(mBuilderCompat.mGroupAlertBehavior);
            if (mBuilderCompat.mColorizedSet) {
                this.mBuilder.setColorized(mBuilderCompat.mColorized);
            }
            if (!TextUtils.isEmpty((CharSequence)mBuilderCompat.mChannelId)) {
                this.mBuilder.setSound((Uri)null).setDefaults(0).setLights(0, 0, 0).setVibrate((long[])null);
            }
        }
    }
    
    private void addAction(final NotificationCompat.Action action) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 20) {
            final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            if (action.getRemoteInputs() != null) {
                final android.app.RemoteInput[] fromCompat = RemoteInput.fromCompat(action.getRemoteInputs());
                for (int length = fromCompat.length, i = 0; i < length; ++i) {
                    notification$Action$Builder.addRemoteInput(fromCompat[i]);
                }
            }
            Bundle bundle;
            if (action.getExtras() != null) {
                bundle = new Bundle(action.getExtras());
            }
            else {
                bundle = new Bundle();
            }
            bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
            if (Build$VERSION.SDK_INT >= 24) {
                notification$Action$Builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            }
            bundle.putInt("android.support.action.semanticAction", action.getSemanticAction());
            if (Build$VERSION.SDK_INT >= 28) {
                notification$Action$Builder.setSemanticAction(action.getSemanticAction());
            }
            bundle.putBoolean("android.support.action.showsUserInterface", action.getShowsUserInterface());
            notification$Action$Builder.addExtras(bundle);
            this.mBuilder.addAction(notification$Action$Builder.build());
        }
        else if (sdk_INT >= 16) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
        }
    }
    
    private void removeSoundAndVibration(final Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= 0xFFFFFFFE;
        notification.defaults &= 0xFFFFFFFD;
    }
    
    public Notification build() {
        final NotificationCompat.Style mStyle = this.mBuilderCompat.mStyle;
        if (mStyle != null) {
            mStyle.apply(this);
        }
        RemoteViews contentView;
        if (mStyle != null) {
            contentView = mStyle.makeContentView(this);
        }
        else {
            contentView = null;
        }
        final Notification buildInternal = this.buildInternal();
        if (contentView != null) {
            buildInternal.contentView = contentView;
        }
        else {
            final RemoteViews mContentView = this.mBuilderCompat.mContentView;
            if (mContentView != null) {
                buildInternal.contentView = mContentView;
            }
        }
        if (Build$VERSION.SDK_INT >= 16 && mStyle != null) {
            final RemoteViews bigContentView = mStyle.makeBigContentView(this);
            if (bigContentView != null) {
                buildInternal.bigContentView = bigContentView;
            }
        }
        if (Build$VERSION.SDK_INT >= 21 && mStyle != null) {
            final RemoteViews headsUpContentView = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
            if (headsUpContentView != null) {
                buildInternal.headsUpContentView = headsUpContentView;
            }
        }
        if (Build$VERSION.SDK_INT >= 16 && mStyle != null) {
            final Bundle extras = NotificationCompat.getExtras(buildInternal);
            if (extras != null) {
                mStyle.addCompatExtras(extras);
            }
        }
        return buildInternal;
    }
    
    protected Notification buildInternal() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 26) {
            return this.mBuilder.build();
        }
        if (sdk_INT >= 24) {
            final Notification build = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
                if (build.getGroup() != null && (build.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(build);
                }
                if (build.getGroup() != null && (build.flags & 0x200) == 0x0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(build);
                }
            }
            return build;
        }
        if (sdk_INT >= 21) {
            this.mBuilder.setExtras(this.mExtras);
            final Notification build2 = this.mBuilder.build();
            final RemoteViews mContentView = this.mContentView;
            if (mContentView != null) {
                build2.contentView = mContentView;
            }
            final RemoteViews mBigContentView = this.mBigContentView;
            if (mBigContentView != null) {
                build2.bigContentView = mBigContentView;
            }
            final RemoteViews mHeadsUpContentView = this.mHeadsUpContentView;
            if (mHeadsUpContentView != null) {
                build2.headsUpContentView = mHeadsUpContentView;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (build2.getGroup() != null && (build2.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(build2);
                }
                if (build2.getGroup() != null && (build2.flags & 0x200) == 0x0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(build2);
                }
            }
            return build2;
        }
        if (sdk_INT >= 20) {
            this.mBuilder.setExtras(this.mExtras);
            final Notification build3 = this.mBuilder.build();
            final RemoteViews mContentView2 = this.mContentView;
            if (mContentView2 != null) {
                build3.contentView = mContentView2;
            }
            final RemoteViews mBigContentView2 = this.mBigContentView;
            if (mBigContentView2 != null) {
                build3.bigContentView = mBigContentView2;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (build3.getGroup() != null && (build3.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(build3);
                }
                if (build3.getGroup() != null && (build3.flags & 0x200) == 0x0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(build3);
                }
            }
            return build3;
        }
        if (sdk_INT >= 19) {
            final SparseArray<Bundle> buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", (SparseArray)buildActionExtrasMap);
            }
            this.mBuilder.setExtras(this.mExtras);
            final Notification build4 = this.mBuilder.build();
            final RemoteViews mContentView3 = this.mContentView;
            if (mContentView3 != null) {
                build4.contentView = mContentView3;
            }
            final RemoteViews mBigContentView3 = this.mBigContentView;
            if (mBigContentView3 != null) {
                build4.bigContentView = mBigContentView3;
            }
            return build4;
        }
        if (sdk_INT >= 16) {
            final Notification build5 = this.mBuilder.build();
            final Bundle extras = NotificationCompat.getExtras(build5);
            final Bundle bundle = new Bundle(this.mExtras);
            for (final String s : this.mExtras.keySet()) {
                if (extras.containsKey(s)) {
                    bundle.remove(s);
                }
            }
            extras.putAll(bundle);
            final SparseArray<Bundle> buildActionExtrasMap2 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap2 != null) {
                NotificationCompat.getExtras(build5).putSparseParcelableArray("android.support.actionExtras", (SparseArray)buildActionExtrasMap2);
            }
            final RemoteViews mContentView4 = this.mContentView;
            if (mContentView4 != null) {
                build5.contentView = mContentView4;
            }
            final RemoteViews mBigContentView4 = this.mBigContentView;
            if (mBigContentView4 != null) {
                build5.bigContentView = mBigContentView4;
            }
            return build5;
        }
        return this.mBuilder.getNotification();
    }
    
    @Override
    public Notification$Builder getBuilder() {
        return this.mBuilder;
    }
}
