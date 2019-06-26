// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.content.Context;
import android.widget.RemoteViews;
import android.app.Notification$Builder;
import android.os.Parcelable;
import android.app.PendingIntent;
import android.util.Log;
import android.app.Notification;
import android.util.SparseArray;
import android.os.Bundle;
import java.util.List;
import java.util.Iterator;
import android.app.Notification$InboxStyle;
import java.util.ArrayList;
import android.app.Notification$BigTextStyle;
import android.app.Notification$BigPictureStyle;
import android.graphics.Bitmap;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
class NotificationCompatJellybean
{
    static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
    static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
    private static Class<?> sActionClass;
    private static Field sActionIconField;
    private static Field sActionIntentField;
    private static Field sActionTitleField;
    private static boolean sActionsAccessFailed;
    private static Field sActionsField;
    private static final Object sActionsLock;
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock;
    
    static {
        sExtrasLock = new Object();
        sActionsLock = new Object();
    }
    
    public static void addBigPictureStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final CharSequence bigContentTitle, final boolean b, final CharSequence summaryText, final Bitmap bitmap, final Bitmap bitmap2, final boolean b2) {
        final Notification$BigPictureStyle bigPicture = new Notification$BigPictureStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(bigContentTitle).bigPicture(bitmap);
        if (b2) {
            bigPicture.bigLargeIcon(bitmap2);
        }
        if (b) {
            bigPicture.setSummaryText(summaryText);
        }
    }
    
    public static void addBigTextStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final CharSequence bigContentTitle, final boolean b, final CharSequence summaryText, final CharSequence charSequence) {
        final Notification$BigTextStyle bigText = new Notification$BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(bigContentTitle).bigText(charSequence);
        if (b) {
            bigText.setSummaryText(summaryText);
        }
    }
    
    public static void addInboxStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final CharSequence bigContentTitle, final boolean b, final CharSequence summaryText, final ArrayList<CharSequence> list) {
        final Notification$InboxStyle setBigContentTitle = new Notification$InboxStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(bigContentTitle);
        if (b) {
            setBigContentTitle.setSummaryText(summaryText);
        }
        final Iterator<CharSequence> iterator = list.iterator();
        while (iterator.hasNext()) {
            setBigContentTitle.addLine((CharSequence)iterator.next());
        }
    }
    
    public static SparseArray<Bundle> buildActionExtrasMap(final List<Bundle> list) {
        final int size = list.size();
        SparseArray sparseArray = null;
        SparseArray sparseArray2;
        for (int i = 0; i < size; ++i, sparseArray = sparseArray2) {
            final Bundle bundle = list.get(i);
            sparseArray2 = sparseArray;
            if (bundle != null) {
                if ((sparseArray2 = sparseArray) == null) {
                    sparseArray2 = new SparseArray();
                }
                sparseArray2.put(i, (Object)bundle);
            }
        }
        return (SparseArray<Bundle>)sparseArray;
    }
    
    private static boolean ensureActionReflectionReadyLocked() {
        if (NotificationCompatJellybean.sActionsAccessFailed) {
            return false;
        }
        try {
            if (NotificationCompatJellybean.sActionsField == null) {
                NotificationCompatJellybean.sActionClass = Class.forName("android.app.Notification$Action");
                NotificationCompatJellybean.sActionIconField = NotificationCompatJellybean.sActionClass.getDeclaredField("icon");
                NotificationCompatJellybean.sActionTitleField = NotificationCompatJellybean.sActionClass.getDeclaredField("title");
                NotificationCompatJellybean.sActionIntentField = NotificationCompatJellybean.sActionClass.getDeclaredField("actionIntent");
                (NotificationCompatJellybean.sActionsField = Notification.class.getDeclaredField("actions")).setAccessible(true);
            }
        }
        catch (NoSuchFieldException ex) {
            Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex);
            NotificationCompatJellybean.sActionsAccessFailed = true;
        }
        catch (ClassNotFoundException ex2) {
            Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex2);
            NotificationCompatJellybean.sActionsAccessFailed = true;
        }
        return true ^ NotificationCompatJellybean.sActionsAccessFailed;
    }
    
    public static NotificationCompatBase.Action getAction(final Notification notification, final int n, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        final Object sActionsLock = NotificationCompatJellybean.sActionsLock;
        // monitorenter(sActionsLock)
        try {
            Label_0119: {
                try {
                    final Object[] actionObjectsLocked = getActionObjectsLocked(notification);
                    if (actionObjectsLocked != null) {
                        final Object obj = actionObjectsLocked[n];
                        final Bundle extras = getExtras(notification);
                        Bundle bundle = null;
                        Label_0059: {
                            if (extras != null) {
                                final SparseArray sparseParcelableArray = extras.getSparseParcelableArray("android.support.actionExtras");
                                if (sparseParcelableArray != null) {
                                    bundle = (Bundle)sparseParcelableArray.get(n);
                                    break Label_0059;
                                }
                            }
                            bundle = null;
                        }
                        // monitorexit(sActionsLock)
                        return readAction(factory, factory2, NotificationCompatJellybean.sActionIconField.getInt(obj), (CharSequence)NotificationCompatJellybean.sActionTitleField.get(obj), (PendingIntent)NotificationCompatJellybean.sActionIntentField.get(obj), bundle);
                    }
                    break Label_0119;
                }
                finally {
                    // monitorexit(sActionsLock)
                    // monitorexit(sActionsLock)
                    return null;
                }
            }
        }
        catch (IllegalAccessException ex) {}
    }
    
    public static int getActionCount(final Notification notification) {
        synchronized (NotificationCompatJellybean.sActionsLock) {
            final Object[] actionObjectsLocked = getActionObjectsLocked(notification);
            int length;
            if (actionObjectsLocked != null) {
                length = actionObjectsLocked.length;
            }
            else {
                length = 0;
            }
            return length;
        }
    }
    
    private static NotificationCompatBase.Action getActionFromBundle(final Bundle bundle, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        final Bundle bundle2 = bundle.getBundle("extras");
        boolean boolean1 = false;
        if (bundle2 != null) {
            boolean1 = bundle2.getBoolean("android.support.allowGeneratedReplies", false);
        }
        return factory.build(bundle.getInt("icon"), bundle.getCharSequence("title"), (PendingIntent)bundle.getParcelable("actionIntent"), bundle.getBundle("extras"), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "remoteInputs"), factory2), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "dataOnlyRemoteInputs"), factory2), boolean1);
    }
    
    private static Object[] getActionObjectsLocked(final Notification obj) {
        synchronized (NotificationCompatJellybean.sActionsLock) {
            if (!ensureActionReflectionReadyLocked()) {
                return null;
            }
            try {
                return (Object[])NotificationCompatJellybean.sActionsField.get(obj);
            }
            catch (IllegalAccessException ex) {
                Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex);
                NotificationCompatJellybean.sActionsAccessFailed = true;
                return null;
            }
        }
    }
    
    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        if (list == null) {
            return null;
        }
        final NotificationCompatBase.Action[] array = factory.newArray(list.size());
        for (int i = 0; i < array.length; ++i) {
            array[i] = getActionFromBundle((Bundle)list.get(i), factory, factory2);
        }
        return array;
    }
    
    private static Bundle getBundleForAction(final NotificationCompatBase.Action action) {
        final Bundle bundle = new Bundle();
        bundle.putInt("icon", action.getIcon());
        bundle.putCharSequence("title", action.getTitle());
        bundle.putParcelable("actionIntent", (Parcelable)action.getActionIntent());
        Bundle bundle2;
        if (action.getExtras() != null) {
            bundle2 = new Bundle(action.getExtras());
        }
        else {
            bundle2 = new Bundle();
        }
        bundle2.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        bundle.putBundle("extras", bundle2);
        bundle.putParcelableArray("remoteInputs", (Parcelable[])RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        return bundle;
    }
    
    public static Bundle getExtras(final Notification notification) {
        synchronized (NotificationCompatJellybean.sExtrasLock) {
            if (NotificationCompatJellybean.sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                if (NotificationCompatJellybean.sExtrasField == null) {
                    final Field declaredField = Notification.class.getDeclaredField("extras");
                    if (!Bundle.class.isAssignableFrom(declaredField.getType())) {
                        Log.e("NotificationCompat", "Notification.extras field is not of type Bundle");
                        NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    declaredField.setAccessible(true);
                    NotificationCompatJellybean.sExtrasField = declaredField;
                }
                Bundle value;
                if ((value = (Bundle)NotificationCompatJellybean.sExtrasField.get(notification)) == null) {
                    value = new Bundle();
                    NotificationCompatJellybean.sExtrasField.set(notification, value);
                }
                return value;
            }
            catch (NoSuchFieldException ex) {
                Log.e("NotificationCompat", "Unable to access notification extras", (Throwable)ex);
            }
            catch (IllegalAccessException ex2) {
                Log.e("NotificationCompat", "Unable to access notification extras", (Throwable)ex2);
            }
            NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
            return null;
        }
    }
    
    public static ArrayList<Parcelable> getParcelableArrayListForActions(final NotificationCompatBase.Action[] array) {
        if (array == null) {
            return null;
        }
        final ArrayList<Parcelable> list = new ArrayList<Parcelable>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add((Parcelable)getBundleForAction(array[i]));
        }
        return list;
    }
    
    public static NotificationCompatBase.Action readAction(final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2, final int n, final CharSequence charSequence, final PendingIntent pendingIntent, final Bundle bundle) {
        RemoteInputCompatBase.RemoteInput[] fromBundleArray;
        RemoteInputCompatBase.RemoteInput[] fromBundleArray2;
        boolean boolean1;
        if (bundle != null) {
            fromBundleArray = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "android.support.remoteInputs"), factory2);
            fromBundleArray2 = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "android.support.dataRemoteInputs"), factory2);
            boolean1 = bundle.getBoolean("android.support.allowGeneratedReplies");
        }
        else {
            boolean1 = false;
            fromBundleArray = (fromBundleArray2 = null);
        }
        return factory.build(n, charSequence, pendingIntent, bundle, fromBundleArray, fromBundleArray2, boolean1);
    }
    
    public static Bundle writeActionAndGetExtras(final Notification$Builder notification$Builder, final NotificationCompatBase.Action action) {
        notification$Builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        final Bundle bundle = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            bundle.putParcelableArray("android.support.remoteInputs", (Parcelable[])RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            bundle.putParcelableArray("android.support.dataRemoteInputs", (Parcelable[])RemoteInputCompatJellybean.toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        return bundle;
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private List<Bundle> mActionExtrasList;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private final Bundle mExtras;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean b2, final Bundle bundle, final String s, final boolean b3, final String s2, final RemoteViews mContentView, final RemoteViews mBigContentView) {
            this.mActionExtrasList = new ArrayList<Bundle>();
            final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            final int flags = notification.flags;
            final boolean b4 = false;
            this.b = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0 || b4).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (b2) {
                this.mExtras.putBoolean("android.support.localOnly", true);
            }
            if (s != null) {
                this.mExtras.putString("android.support.groupKey", s);
                if (b3) {
                    this.mExtras.putBoolean("android.support.isGroupSummary", true);
                }
                else {
                    this.mExtras.putBoolean("android.support.useSideChannel", true);
                }
            }
            if (s2 != null) {
                this.mExtras.putString("android.support.sortKey", s2);
            }
            this.mContentView = mContentView;
            this.mBigContentView = mBigContentView;
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.b, action));
        }
        
        @Override
        public Notification build() {
            final Notification build = this.b.build();
            final Bundle extras = NotificationCompatJellybean.getExtras(build);
            final Bundle bundle = new Bundle(this.mExtras);
            for (final String s : this.mExtras.keySet()) {
                if (extras.containsKey(s)) {
                    bundle.remove(s);
                }
            }
            extras.putAll(bundle);
            final SparseArray<Bundle> buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap != null) {
                NotificationCompatJellybean.getExtras(build).putSparseParcelableArray("android.support.actionExtras", (SparseArray)buildActionExtrasMap);
            }
            if (this.mContentView != null) {
                build.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                build.bigContentView = this.mBigContentView;
            }
            return build;
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
