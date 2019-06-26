package androidx.core.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.RemoteInput;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Style;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private final List<Bundle> mActionExtrasList = new ArrayList();
    private RemoteViews mBigContentView;
    private final Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Bundle mExtras = new Bundle();
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;

    NotificationCompatBuilder(NotificationCompat.Builder builder) {
        Bundle bundle;
        String str;
        this.mBuilderCompat = builder;
        if (VERSION.SDK_INT >= 26) {
            this.mBuilder = new Builder(builder.mContext, builder.mChannelId);
        } else {
            this.mBuilder = new Builder(builder.mContext);
        }
        Notification notification = builder.mNotification;
        this.mBuilder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, builder.mTickerView).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(builder.mContentTitle).setContentText(builder.mContentText).setContentInfo(builder.mContentInfo).setContentIntent(builder.mContentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(builder.mFullScreenIntent, (notification.flags & 128) != 0).setLargeIcon(builder.mLargeIcon).setNumber(builder.mNumber).setProgress(builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate);
        if (VERSION.SDK_INT < 21) {
            this.mBuilder.setSound(notification.sound, notification.audioStreamType);
        }
        if (VERSION.SDK_INT >= 16) {
            this.mBuilder.setSubText(builder.mSubText).setUsesChronometer(builder.mUseChronometer).setPriority(builder.mPriority);
            Iterator it = builder.mActions.iterator();
            while (it.hasNext()) {
                addAction((Action) it.next());
            }
            bundle = builder.mExtras;
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (VERSION.SDK_INT < 20) {
                if (builder.mLocalOnly) {
                    this.mExtras.putBoolean("android.support.localOnly", true);
                }
                str = builder.mGroupKey;
                if (str != null) {
                    this.mExtras.putString("android.support.groupKey", str);
                    if (builder.mGroupSummary) {
                        this.mExtras.putBoolean("android.support.isGroupSummary", true);
                    } else {
                        this.mExtras.putBoolean("android.support.useSideChannel", true);
                    }
                }
                str = builder.mSortKey;
                if (str != null) {
                    this.mExtras.putString("android.support.sortKey", str);
                }
            }
            this.mContentView = builder.mContentView;
            this.mBigContentView = builder.mBigContentView;
        }
        if (VERSION.SDK_INT >= 19) {
            this.mBuilder.setShowWhen(builder.mShowWhen);
            if (VERSION.SDK_INT < 21) {
                ArrayList arrayList = builder.mPeople;
                if (!(arrayList == null || arrayList.isEmpty())) {
                    bundle = this.mExtras;
                    ArrayList arrayList2 = builder.mPeople;
                    bundle.putStringArray("android.people", (String[]) arrayList2.toArray(new String[arrayList2.size()]));
                }
            }
        }
        if (VERSION.SDK_INT >= 20) {
            this.mBuilder.setLocalOnly(builder.mLocalOnly).setGroup(builder.mGroupKey).setGroupSummary(builder.mGroupSummary).setSortKey(builder.mSortKey);
            this.mGroupAlertBehavior = builder.mGroupAlertBehavior;
        }
        if (VERSION.SDK_INT >= 21) {
            this.mBuilder.setCategory(builder.mCategory).setColor(builder.mColor).setVisibility(builder.mVisibility).setPublicVersion(builder.mPublicVersion).setSound(notification.sound, notification.audioAttributes);
            Iterator it2 = builder.mPeople.iterator();
            while (it2.hasNext()) {
                this.mBuilder.addPerson((String) it2.next());
            }
            this.mHeadsUpContentView = builder.mHeadsUpContentView;
            if (builder.mInvisibleActions.size() > 0) {
                str = "android.car.EXTENSIONS";
                Bundle bundle2 = builder.getExtras().getBundle(str);
                if (bundle2 == null) {
                    bundle2 = new Bundle();
                }
                Bundle bundle3 = new Bundle();
                for (int i = 0; i < builder.mInvisibleActions.size(); i++) {
                    bundle3.putBundle(Integer.toString(i), NotificationCompatJellybean.getBundleForAction((Action) builder.mInvisibleActions.get(i)));
                }
                bundle2.putBundle("invisible_actions", bundle3);
                builder.getExtras().putBundle(str, bundle2);
                this.mExtras.putBundle(str, bundle2);
            }
        }
        if (VERSION.SDK_INT >= 24) {
            this.mBuilder.setExtras(builder.mExtras).setRemoteInputHistory(builder.mRemoteInputHistory);
            RemoteViews remoteViews = builder.mContentView;
            if (remoteViews != null) {
                this.mBuilder.setCustomContentView(remoteViews);
            }
            remoteViews = builder.mBigContentView;
            if (remoteViews != null) {
                this.mBuilder.setCustomBigContentView(remoteViews);
            }
            remoteViews = builder.mHeadsUpContentView;
            if (remoteViews != null) {
                this.mBuilder.setCustomHeadsUpContentView(remoteViews);
            }
        }
        if (VERSION.SDK_INT >= 26) {
            this.mBuilder.setBadgeIconType(builder.mBadgeIcon).setShortcutId(builder.mShortcutId).setTimeoutAfter(builder.mTimeout).setGroupAlertBehavior(builder.mGroupAlertBehavior);
            if (builder.mColorizedSet) {
                this.mBuilder.setColorized(builder.mColorized);
            }
            if (!TextUtils.isEmpty(builder.mChannelId)) {
                this.mBuilder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
            }
        }
    }

    public Builder getBuilder() {
        return this.mBuilder;
    }

    public Notification build() {
        Style style = this.mBuilderCompat.mStyle;
        if (style != null) {
            style.apply(this);
        }
        RemoteViews makeContentView = style != null ? style.makeContentView(this) : null;
        Notification buildInternal = buildInternal();
        if (makeContentView != null) {
            buildInternal.contentView = makeContentView;
        } else {
            makeContentView = this.mBuilderCompat.mContentView;
            if (makeContentView != null) {
                buildInternal.contentView = makeContentView;
            }
        }
        if (VERSION.SDK_INT >= 16 && style != null) {
            makeContentView = style.makeBigContentView(this);
            if (makeContentView != null) {
                buildInternal.bigContentView = makeContentView;
            }
        }
        if (VERSION.SDK_INT >= 21 && style != null) {
            makeContentView = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
            if (makeContentView != null) {
                buildInternal.headsUpContentView = makeContentView;
            }
        }
        if (VERSION.SDK_INT >= 16 && style != null) {
            Bundle extras = NotificationCompat.getExtras(buildInternal);
            if (extras != null) {
                style.addCompatExtras(extras);
            }
        }
        return buildInternal;
    }

    private void addAction(Action action) {
        int i = VERSION.SDK_INT;
        if (i >= 20) {
            Bundle bundle;
            Notification.Action.Builder builder = new Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            if (action.getRemoteInputs() != null) {
                for (RemoteInput addRemoteInput : RemoteInput.fromCompat(action.getRemoteInputs())) {
                    builder.addRemoteInput(addRemoteInput);
                }
            }
            if (action.getExtras() != null) {
                bundle = new Bundle(action.getExtras());
            } else {
                bundle = new Bundle();
            }
            bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
            if (VERSION.SDK_INT >= 24) {
                builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            }
            bundle.putInt("android.support.action.semanticAction", action.getSemanticAction());
            if (VERSION.SDK_INT >= 28) {
                builder.setSemanticAction(action.getSemanticAction());
            }
            bundle.putBoolean("android.support.action.showsUserInterface", action.getShowsUserInterface());
            builder.addExtras(bundle);
            this.mBuilder.addAction(builder.build());
        } else if (i >= 16) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
        }
    }

    /* Access modifiers changed, original: protected */
    public Notification buildInternal() {
        int i = VERSION.SDK_INT;
        if (i >= 26) {
            return this.mBuilder.build();
        }
        Notification build;
        RemoteViews remoteViews;
        if (i >= 24) {
            build = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
                if (!(build.getGroup() == null || (build.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(build);
                }
                if (build.getGroup() != null && (build.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build);
                }
            }
            return build;
        } else if (i >= 21) {
            this.mBuilder.setExtras(this.mExtras);
            build = this.mBuilder.build();
            remoteViews = this.mContentView;
            if (remoteViews != null) {
                build.contentView = remoteViews;
            }
            remoteViews = this.mBigContentView;
            if (remoteViews != null) {
                build.bigContentView = remoteViews;
            }
            remoteViews = this.mHeadsUpContentView;
            if (remoteViews != null) {
                build.headsUpContentView = remoteViews;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (!(build.getGroup() == null || (build.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(build);
                }
                if (build.getGroup() != null && (build.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build);
                }
            }
            return build;
        } else if (i >= 20) {
            this.mBuilder.setExtras(this.mExtras);
            build = this.mBuilder.build();
            remoteViews = this.mContentView;
            if (remoteViews != null) {
                build.contentView = remoteViews;
            }
            remoteViews = this.mBigContentView;
            if (remoteViews != null) {
                build.bigContentView = remoteViews;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (!(build.getGroup() == null || (build.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(build);
                }
                if (build.getGroup() != null && (build.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build);
                }
            }
            return build;
        } else {
            String str = "android.support.actionExtras";
            if (i >= 19) {
                SparseArray buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
                if (buildActionExtrasMap != null) {
                    this.mExtras.putSparseParcelableArray(str, buildActionExtrasMap);
                }
                this.mBuilder.setExtras(this.mExtras);
                build = this.mBuilder.build();
                remoteViews = this.mContentView;
                if (remoteViews != null) {
                    build.contentView = remoteViews;
                }
                remoteViews = this.mBigContentView;
                if (remoteViews != null) {
                    build.bigContentView = remoteViews;
                }
                return build;
            } else if (i < 16) {
                return this.mBuilder.getNotification();
            } else {
                build = this.mBuilder.build();
                Bundle extras = NotificationCompat.getExtras(build);
                Bundle bundle = new Bundle(this.mExtras);
                for (String str2 : this.mExtras.keySet()) {
                    if (extras.containsKey(str2)) {
                        bundle.remove(str2);
                    }
                }
                extras.putAll(bundle);
                SparseArray buildActionExtrasMap2 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
                if (buildActionExtrasMap2 != null) {
                    NotificationCompat.getExtras(build).putSparseParcelableArray(str, buildActionExtrasMap2);
                }
                remoteViews = this.mContentView;
                if (remoteViews != null) {
                    build.contentView = remoteViews;
                }
                remoteViews = this.mBigContentView;
                if (remoteViews != null) {
                    build.bigContentView = remoteViews;
                }
                return build;
            }
        }
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= -2;
        notification.defaults &= -3;
    }
}
