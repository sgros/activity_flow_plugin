package menion.android.whereyougo.gui.extension.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Logger;

public class CustomActivity extends FragmentActivity {
    protected static void customOnCreate(Activity activity) {
        if (!(activity instanceof CustomMainActivity)) {
            setScreenBasic(activity);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Const.SCREEN_WIDTH = metrics.widthPixels;
        Const.SCREEN_HEIGHT = metrics.heightPixels;
        switch (Preferences.APPEARANCE_FONT_SIZE) {
            case 1:
                activity.setTheme(C0254R.style.FontSizeSmall);
                return;
            case 2:
                activity.setTheme(C0254R.style.FontSizeMedium);
                return;
            case 3:
                activity.setTheme(C0254R.style.FontSizeLarge);
                return;
            default:
                return;
        }
    }

    protected static void setScreenBasic(Activity activity) {
        try {
            activity.requestWindowFeature(1);
        } catch (Exception e) {
        }
    }

    protected static void customOnPause(Activity activity) {
        if (PreferenceValues.getCurrentActivity() == activity) {
            PreferenceValues.setCurrentActivity(null);
        }
        MainApplication.onActivityPause();
    }

    protected static void customOnResume(Activity activity) {
        PreferenceValues.setCurrentActivity(activity);
        PreferenceValues.enableWakeLock();
    }

    protected static void customOnStart(Activity activity) {
        setStatusbar(activity);
        setScreenFullscreen(activity);
    }

    public static void setStatusbar(Activity activity) {
        try {
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService("notification");
            if (Preferences.APPEARANCE_STATUSBAR) {
                Context context = activity.getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setAction("android.intent.action.MAIN");
                notificationManager.notify(0, new Builder(activity).setContentTitle(C0322A.getAppName()).setSmallIcon(C0254R.C0252drawable.ic_title_logo).setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)).setOngoing(true).build());
                return;
            }
            notificationManager.cancel(0);
        } catch (Exception e) {
        }
    }

    public static void setScreenFullscreen(Activity activity) {
        try {
            if (Preferences.APPEARANCE_FULLSCREEN) {
                activity.getWindow().setFlags(1024, 1024);
            } else {
                activity.getWindow().clearFlags(1024);
            }
        } catch (Exception e) {
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public int getParentViewId() {
        return -1;
    }

    public void onCreate(Bundle savedInstanceState) {
        Logger.m25v(getLocalClassName(), "onCreate(), id:" + hashCode());
        try {
            super.onCreate(savedInstanceState);
            customOnCreate(this);
        } catch (Exception e) {
            Logger.m22e(getLocalClassName(), "onCreate()", e);
        }
    }

    public void onDestroy() {
        Logger.m25v(getLocalClassName(), "onDestroy(), id:" + hashCode());
        try {
            super.onDestroy();
            if (getParentViewId() != -1) {
                unbindDrawables(findViewById(getParentViewId()));
                System.gc();
            }
        } catch (Exception e) {
            Logger.m22e(getLocalClassName(), "onDestroy()", e);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        Logger.m25v(getLocalClassName(), "onPause(), id:" + hashCode());
        try {
            super.onPause();
            customOnPause(this);
        } catch (Exception e) {
            Logger.m22e(getLocalClassName(), "onPause()", e);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        Logger.m25v(getLocalClassName(), "onResume(), id:" + hashCode());
        try {
            super.onResume();
            customOnResume(this);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Const.SCREEN_WIDTH = metrics.widthPixels;
            Const.SCREEN_HEIGHT = metrics.heightPixels;
        } catch (Exception e) {
            Logger.m22e(getLocalClassName(), "onResume()", e);
        }
    }

    public void onStart() {
        Logger.m25v(getLocalClassName(), "onStart(), id:" + hashCode());
        try {
            super.onStart();
            customOnStart(this);
        } catch (Exception e) {
            Logger.m22e(getLocalClassName(), "onStart()", e);
        }
    }

    private void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        }
    }
}
