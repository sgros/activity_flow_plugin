// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.io.Serializable;
import java.lang.reflect.Field;
import android.os.Message;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import java.util.Collections;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import java.util.Arrays;
import java.util.Iterator;
import android.content.Context;
import android.os.Build;
import android.content.pm.ResolveInfo;
import java.io.Closeable;
import android.database.Cursor;
import android.content.pm.PackageManager;
import android.content.Intent;
import java.util.LinkedList;
import android.content.ComponentName;
import java.util.List;

public class NotificationBadge
{
    private static final List<Class<? extends Badger>> BADGERS;
    private static Badger badger;
    private static ComponentName componentName;
    private static boolean initied;
    
    static {
        (BADGERS = new LinkedList<Class<? extends Badger>>()).add(AdwHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)ApexHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)NewHtcHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)NovaHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)SonyHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)XiaomiHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)AsusHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)HuaweiHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)OPPOHomeBader.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)SamsungHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)ZukHomeBadger.class);
        NotificationBadge.BADGERS.add((Class<? extends Badger>)VivoHomeBadger.class);
    }
    
    public static boolean applyCount(final int n) {
        try {
            if (NotificationBadge.badger == null && !NotificationBadge.initied) {
                initBadger();
                NotificationBadge.initied = true;
            }
            if (NotificationBadge.badger == null) {
                return false;
            }
            NotificationBadge.badger.executeBadge(n);
            return true;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    private static boolean canResolveBroadcast(final Intent intent) {
        final PackageManager packageManager = ApplicationLoader.applicationContext.getPackageManager();
        final boolean b = false;
        final List queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 0);
        boolean b2 = b;
        if (queryBroadcastReceivers != null) {
            b2 = b;
            if (queryBroadcastReceivers.size() > 0) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public static void close(final Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (Throwable t) {}
    }
    
    private static boolean initBadger() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        final Intent launchIntentForPackage = applicationContext.getPackageManager().getLaunchIntentForPackage(applicationContext.getPackageName());
        int i = 0;
        if (launchIntentForPackage == null) {
            return false;
        }
        NotificationBadge.componentName = launchIntentForPackage.getComponent();
        final Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        final ResolveInfo resolveActivity = applicationContext.getPackageManager().resolveActivity(intent, 65536);
        if (resolveActivity != null) {
            final String packageName = resolveActivity.activityInfo.packageName;
            for (final Class<Badger> clazz : NotificationBadge.BADGERS) {
                Badger badger;
                try {
                    badger = clazz.newInstance();
                }
                catch (Exception ex) {
                    badger = null;
                }
                if (badger != null && badger.getSupportLaunchers().contains(packageName)) {
                    NotificationBadge.badger = badger;
                    break;
                }
            }
            if (NotificationBadge.badger != null) {
                return true;
            }
        }
        final List queryIntentActivities = applicationContext.getPackageManager().queryIntentActivities(intent, 65536);
        if (queryIntentActivities != null) {
            while (i < queryIntentActivities.size()) {
                final String packageName2 = queryIntentActivities.get(i).activityInfo.packageName;
                for (final Class<Badger> clazz2 : NotificationBadge.BADGERS) {
                    Badger badger2;
                    try {
                        badger2 = clazz2.newInstance();
                    }
                    catch (Exception ex2) {
                        badger2 = null;
                    }
                    if (badger2 != null && badger2.getSupportLaunchers().contains(packageName2)) {
                        NotificationBadge.badger = badger2;
                        break;
                    }
                }
                if (NotificationBadge.badger != null) {
                    break;
                }
                ++i;
            }
        }
        if (NotificationBadge.badger == null) {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                NotificationBadge.badger = (Badger)new XiaomiHomeBadger();
            }
            else if (Build.MANUFACTURER.equalsIgnoreCase("ZUK")) {
                NotificationBadge.badger = (Badger)new ZukHomeBadger();
            }
            else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
                NotificationBadge.badger = (Badger)new OPPOHomeBader();
            }
            else if (Build.MANUFACTURER.equalsIgnoreCase("VIVO")) {
                NotificationBadge.badger = (Badger)new VivoHomeBadger();
            }
            else {
                NotificationBadge.badger = (Badger)new DefaultBadger();
            }
        }
        return true;
    }
    
    public static class AdwHomeBadger implements Badger
    {
        public static final String CLASSNAME = "CNAME";
        public static final String COUNT = "COUNT";
        public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
        public static final String PACKAGENAME = "PNAME";
        
        @Override
        public void executeBadge(final int n) {
            final Intent intent = new Intent("org.adw.launcher.counter.SEND");
            intent.putExtra("PNAME", NotificationBadge.componentName.getPackageName());
            intent.putExtra("CNAME", NotificationBadge.componentName.getClassName());
            intent.putExtra("COUNT", n);
            if (canResolveBroadcast(intent)) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationLoader.applicationContext.sendBroadcast(intent);
                    }
                });
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("org.adw.launcher", "org.adwfreak.launcher");
        }
    }
    
    public static class ApexHomeBadger implements Badger
    {
        private static final String CLASS = "class";
        private static final String COUNT = "count";
        private static final String INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED";
        private static final String PACKAGENAME = "package";
        
        @Override
        public void executeBadge(final int n) {
            final Intent intent = new Intent("com.anddoes.launcher.COUNTER_CHANGED");
            intent.putExtra("package", NotificationBadge.componentName.getPackageName());
            intent.putExtra("count", n);
            intent.putExtra("class", NotificationBadge.componentName.getClassName());
            if (canResolveBroadcast(intent)) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationLoader.applicationContext.sendBroadcast(intent);
                    }
                });
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.anddoes.launcher");
        }
    }
    
    public static class AsusHomeBadger implements Badger
    {
        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
        
        @Override
        public void executeBadge(final int n) {
            final Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", n);
            intent.putExtra("badge_count_package_name", NotificationBadge.componentName.getPackageName());
            intent.putExtra("badge_count_class_name", NotificationBadge.componentName.getClassName());
            intent.putExtra("badge_vip_count", 0);
            if (canResolveBroadcast(intent)) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationLoader.applicationContext.sendBroadcast(intent);
                    }
                });
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.asus.launcher");
        }
    }
    
    public interface Badger
    {
        void executeBadge(final int p0);
        
        List<String> getSupportLaunchers();
    }
    
    public static class DefaultBadger implements Badger
    {
        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
        
        @Override
        public void executeBadge(final int n) {
            final Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", n);
            intent.putExtra("badge_count_package_name", NotificationBadge.componentName.getPackageName());
            intent.putExtra("badge_count_class_name", NotificationBadge.componentName.getClassName());
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ApplicationLoader.applicationContext.sendBroadcast(intent);
                    }
                    catch (Exception ex) {}
                }
            });
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("fr.neamar.kiss", "com.quaap.launchtime", "com.quaap.launchtime_official");
        }
    }
    
    public static class HuaweiHomeBadger implements Badger
    {
        @Override
        public void executeBadge(final int n) {
            final Bundle bundle = new Bundle();
            bundle.putString("package", ApplicationLoader.applicationContext.getPackageName());
            bundle.putString("class", NotificationBadge.componentName.getClassName());
            bundle.putInt("badgenumber", n);
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ApplicationLoader.applicationContext.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", (String)null, bundle);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            });
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.huawei.android.launcher");
        }
    }
    
    public static class NewHtcHomeBadger implements Badger
    {
        public static final String COUNT = "count";
        public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
        public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";
        public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
        public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
        public static final String PACKAGENAME = "packagename";
        
        @Override
        public void executeBadge(final int n) {
            final Intent intent = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
            intent.putExtra("com.htc.launcher.extra.COMPONENT", NotificationBadge.componentName.flattenToShortString());
            intent.putExtra("com.htc.launcher.extra.COUNT", n);
            final Intent intent2 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            intent2.putExtra("packagename", NotificationBadge.componentName.getPackageName());
            intent2.putExtra("count", n);
            if (canResolveBroadcast(intent) || canResolveBroadcast(intent2)) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationLoader.applicationContext.sendBroadcast(intent);
                        ApplicationLoader.applicationContext.sendBroadcast(intent2);
                    }
                });
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.htc.launcher");
        }
    }
    
    public static class NovaHomeBadger implements Badger
    {
        private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
        private static final String COUNT = "count";
        private static final String TAG = "tag";
        
        @Override
        public void executeBadge(final int i) {
            final ContentValues contentValues = new ContentValues();
            final StringBuilder sb = new StringBuilder();
            sb.append(NotificationBadge.componentName.getPackageName());
            sb.append("/");
            sb.append(NotificationBadge.componentName.getClassName());
            contentValues.put("tag", sb.toString());
            contentValues.put("count", Integer.valueOf(i));
            ApplicationLoader.applicationContext.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"), contentValues);
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.teslacoilsw.launcher");
        }
    }
    
    public static class OPPOHomeBader implements Badger
    {
        private static final String INTENT_ACTION = "com.oppo.unsettledevent";
        private static final String INTENT_EXTRA_BADGEUPGRADE_COUNT = "app_badge_count";
        private static final String INTENT_EXTRA_BADGE_COUNT = "number";
        private static final String INTENT_EXTRA_BADGE_UPGRADENUMBER = "upgradeNumber";
        private static final String INTENT_EXTRA_PACKAGENAME = "pakeageName";
        private static final String PROVIDER_CONTENT_URI = "content://com.android.badge/badge";
        private int mCurrentTotalCount;
        
        public OPPOHomeBader() {
            this.mCurrentTotalCount = -1;
        }
        
        @TargetApi(11)
        private void executeBadgeByContentProvider(final int n) {
            try {
                final Bundle bundle = new Bundle();
                bundle.putInt("app_badge_count", n);
                ApplicationLoader.applicationContext.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", (String)null, bundle);
            }
            catch (Throwable t) {}
        }
        
        @Override
        public void executeBadge(final int mCurrentTotalCount) {
            if (this.mCurrentTotalCount == mCurrentTotalCount) {
                return;
            }
            this.executeBadgeByContentProvider(this.mCurrentTotalCount = mCurrentTotalCount);
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Collections.singletonList("com.oppo.launcher");
        }
    }
    
    public static class SamsungHomeBadger implements Badger
    {
        private static final String[] CONTENT_PROJECTION;
        private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";
        private static DefaultBadger defaultBadger;
        
        static {
            CONTENT_PROJECTION = new String[] { "_id", "class" };
        }
        
        private ContentValues getContentValues(final ComponentName componentName, final int i, final boolean b) {
            final ContentValues contentValues = new ContentValues();
            if (b) {
                contentValues.put("package", componentName.getPackageName());
                contentValues.put("class", componentName.getClassName());
            }
            contentValues.put("badgecount", Integer.valueOf(i));
            return contentValues;
        }
        
        @Override
        public void executeBadge(final int n) {
            while (true) {
                try {
                    if (SamsungHomeBadger.defaultBadger == null) {
                        SamsungHomeBadger.defaultBadger = new DefaultBadger();
                    }
                    SamsungHomeBadger.defaultBadger.executeBadge(n);
                    final Uri parse = Uri.parse("content://com.sec.badge/apps?notify=true");
                    final ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                    Cursor cursor = null;
                    try {
                        final Cursor query = contentResolver.query(parse, SamsungHomeBadger.CONTENT_PROJECTION, "package=?", new String[] { NotificationBadge.componentName.getPackageName() }, (String)null);
                        if (query != null) {
                            cursor = query;
                            final String className = NotificationBadge.componentName.getClassName();
                            boolean b = false;
                            while (true) {
                                cursor = query;
                                if (!query.moveToNext()) {
                                    break;
                                }
                                cursor = query;
                                final int int1 = query.getInt(0);
                                cursor = query;
                                contentResolver.update(parse, this.getContentValues(NotificationBadge.componentName, n, false), "_id=?", new String[] { String.valueOf(int1) });
                                cursor = query;
                                if (!className.equals(query.getString(query.getColumnIndex("class")))) {
                                    continue;
                                }
                                b = true;
                            }
                            if (!b) {
                                cursor = query;
                                contentResolver.insert(parse, this.getContentValues(NotificationBadge.componentName, n, true));
                            }
                        }
                    }
                    finally {
                        NotificationBadge.close(cursor);
                    }
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.sec.android.app.launcher", "com.sec.android.app.twlauncher");
        }
    }
    
    public static class SonyHomeBadger implements Badger
    {
        private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
        private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
        private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
        private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";
        private static final String PROVIDER_COLUMNS_ACTIVITY_NAME = "activity_name";
        private static final String PROVIDER_COLUMNS_BADGE_COUNT = "badge_count";
        private static final String PROVIDER_COLUMNS_PACKAGE_NAME = "package_name";
        private static final String PROVIDER_CONTENT_URI = "content://com.sonymobile.home.resourceprovider/badge";
        private static final String SONY_HOME_PROVIDER_NAME = "com.sonymobile.home.resourceprovider";
        private static AsyncQueryHandler mQueryHandler;
        private final Uri BADGE_CONTENT_URI;
        
        public SonyHomeBadger() {
            this.BADGE_CONTENT_URI = Uri.parse("content://com.sonymobile.home.resourceprovider/badge");
        }
        
        private static void executeBadgeByBroadcast(final int i) {
            final Intent intent = new Intent("com.sonyericsson.home.action.UPDATE_BADGE");
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", NotificationBadge.componentName.getPackageName());
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", NotificationBadge.componentName.getClassName());
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(i));
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", i > 0);
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ApplicationLoader.applicationContext.sendBroadcast(intent);
                }
            });
        }
        
        private void executeBadgeByContentProvider(final int n) {
            if (n < 0) {
                return;
            }
            if (SonyHomeBadger.mQueryHandler == null) {
                SonyHomeBadger.mQueryHandler = new AsyncQueryHandler(ApplicationLoader.applicationContext.getApplicationContext().getContentResolver()) {
                    public void handleMessage(final Message message) {
                        try {
                            super.handleMessage(message);
                        }
                        catch (Throwable t) {}
                    }
                };
            }
            this.insertBadgeAsync(n, NotificationBadge.componentName.getPackageName(), NotificationBadge.componentName.getClassName());
        }
        
        private void insertBadgeAsync(final int i, final String s, final String s2) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put("badge_count", Integer.valueOf(i));
            contentValues.put("package_name", s);
            contentValues.put("activity_name", s2);
            SonyHomeBadger.mQueryHandler.startInsert(0, (Object)null, this.BADGE_CONTENT_URI, contentValues);
        }
        
        private static boolean sonyBadgeContentProviderExists() {
            final PackageManager packageManager = ApplicationLoader.applicationContext.getPackageManager();
            boolean b = false;
            if (packageManager.resolveContentProvider("com.sonymobile.home.resourceprovider", 0) != null) {
                b = true;
            }
            return b;
        }
        
        @Override
        public void executeBadge(final int n) {
            if (sonyBadgeContentProviderExists()) {
                this.executeBadgeByContentProvider(n);
            }
            else {
                executeBadgeByBroadcast(n);
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.sonyericsson.home", "com.sonymobile.home");
        }
    }
    
    public static class VivoHomeBadger implements Badger
    {
        @Override
        public void executeBadge(final int n) {
            final Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", ApplicationLoader.applicationContext.getPackageName());
            intent.putExtra("className", NotificationBadge.componentName.getClassName());
            intent.putExtra("notificationNum", n);
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.vivo.launcher");
        }
    }
    
    public static class XiaomiHomeBadger implements Badger
    {
        public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
        public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";
        public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";
        
        @Override
        public void executeBadge(final int n) {
            final String s = "";
            try {
                final Object instance = Class.forName("android.app.MiuiNotification").newInstance();
                final Field declaredField = instance.getClass().getDeclaredField("messageCount");
                declaredField.setAccessible(true);
                Serializable value;
                if (n == 0) {
                    value = "";
                }
                else {
                    value = n;
                }
                declaredField.set(instance, String.valueOf(value));
            }
            catch (Throwable t) {
                final Intent intent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
                final StringBuilder sb = new StringBuilder();
                sb.append(NotificationBadge.componentName.getPackageName());
                sb.append("/");
                sb.append(NotificationBadge.componentName.getClassName());
                intent.putExtra("android.intent.extra.update_application_component_name", sb.toString());
                Serializable value2;
                if (n == 0) {
                    value2 = s;
                }
                else {
                    value2 = n;
                }
                intent.putExtra("android.intent.extra.update_application_message_text", String.valueOf(value2));
                if (canResolveBroadcast(intent)) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            ApplicationLoader.applicationContext.sendBroadcast(intent);
                        }
                    });
                }
            }
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.miui.miuilite", "com.miui.home", "com.miui.miuihome", "com.miui.miuihome2", "com.miui.mihome", "com.miui.mihome2");
        }
    }
    
    public static class ZukHomeBadger implements Badger
    {
        private final Uri CONTENT_URI;
        
        public ZukHomeBadger() {
            this.CONTENT_URI = Uri.parse("content://com.android.badge/badge");
        }
        
        @TargetApi(11)
        @Override
        public void executeBadge(final int n) {
            final Bundle bundle = new Bundle();
            bundle.putInt("app_badge_count", n);
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ApplicationLoader.applicationContext.getContentResolver().call(ZukHomeBadger.this.CONTENT_URI, "setAppBadgeCount", (String)null, bundle);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            });
        }
        
        @Override
        public List<String> getSupportLaunchers() {
            return Collections.singletonList("com.zui.launcher");
        }
    }
}
