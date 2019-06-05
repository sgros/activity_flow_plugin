package android.support.p001v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Log;

/* renamed from: android.support.v4.app.NavUtils */
public final class NavUtils {
    public static boolean shouldUpRecreateTask(Activity activity, Intent intent) {
        if (VERSION.SDK_INT >= 16) {
            return activity.shouldUpRecreateTask(intent);
        }
        String action = activity.getIntent().getAction();
        boolean z = (action == null || action.equals("android.intent.action.MAIN")) ? false : true;
        return z;
    }

    public static void navigateUpTo(Activity activity, Intent intent) {
        if (VERSION.SDK_INT >= 16) {
            activity.navigateUpTo(intent);
            return;
        }
        intent.addFlags(67108864);
        activity.startActivity(intent);
        activity.finish();
    }

    public static Intent getParentActivityIntent(Activity activity) {
        if (VERSION.SDK_INT >= 16) {
            Intent parentActivityIntent = activity.getParentActivityIntent();
            if (parentActivityIntent != null) {
                return parentActivityIntent;
            }
        }
        String parentActivityName = NavUtils.getParentActivityName(activity);
        if (parentActivityName == null) {
            return null;
        }
        ComponentName componentName = new ComponentName(activity, parentActivityName);
        try {
            Intent makeMainActivity;
            if (NavUtils.getParentActivityName(activity, componentName) == null) {
                makeMainActivity = Intent.makeMainActivity(componentName);
            } else {
                makeMainActivity = new Intent().setComponent(componentName);
            }
            return makeMainActivity;
        } catch (NameNotFoundException unused) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getParentActivityIntent: bad parentActivityName '");
            stringBuilder.append(parentActivityName);
            stringBuilder.append("' in manifest");
            Log.e("NavUtils", stringBuilder.toString());
            return null;
        }
    }

    public static Intent getParentActivityIntent(Context context, ComponentName componentName) throws NameNotFoundException {
        String parentActivityName = NavUtils.getParentActivityName(context, componentName);
        if (parentActivityName == null) {
            return null;
        }
        Intent makeMainActivity;
        ComponentName componentName2 = new ComponentName(componentName.getPackageName(), parentActivityName);
        if (NavUtils.getParentActivityName(context, componentName2) == null) {
            makeMainActivity = Intent.makeMainActivity(componentName2);
        } else {
            makeMainActivity = new Intent().setComponent(componentName2);
        }
        return makeMainActivity;
    }

    public static String getParentActivityName(Activity activity) {
        try {
            return NavUtils.getParentActivityName(activity, activity.getComponentName());
        } catch (NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String getParentActivityName(Context context, ComponentName componentName) throws NameNotFoundException {
        ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(componentName, 128);
        if (VERSION.SDK_INT >= 16) {
            String str = activityInfo.parentActivityName;
            if (str != null) {
                return str;
            }
        }
        if (activityInfo.metaData == null) {
            return null;
        }
        String string = activityInfo.metaData.getString("android.support.PARENT_ACTIVITY");
        if (string == null) {
            return null;
        }
        if (string.charAt(0) == '.') {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getPackageName());
            stringBuilder.append(string);
            string = stringBuilder.toString();
        }
        return string;
    }
}
