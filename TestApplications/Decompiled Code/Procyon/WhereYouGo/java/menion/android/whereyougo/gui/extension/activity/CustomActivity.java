// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension.activity;

import menion.android.whereyougo.utils.Logger;
import android.os.Bundle;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable$Callback;
import android.view.View;
import android.app.PendingIntent;
import menion.android.whereyougo.utils.A;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.content.Intent;
import menion.android.whereyougo.gui.activity.MainActivity;
import android.app.NotificationManager;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Const;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;

public class CustomActivity extends FragmentActivity
{
    protected static void customOnCreate(final Activity screenBasic) {
        if (!(screenBasic instanceof CustomMainActivity)) {
            setScreenBasic(screenBasic);
        }
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        screenBasic.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Const.SCREEN_WIDTH = displayMetrics.widthPixels;
        Const.SCREEN_HEIGHT = displayMetrics.heightPixels;
        switch (Preferences.APPEARANCE_FONT_SIZE) {
            case 1: {
                screenBasic.setTheme(2131361803);
                break;
            }
            case 2: {
                screenBasic.setTheme(2131361802);
                break;
            }
            case 3: {
                screenBasic.setTheme(2131361801);
                break;
            }
        }
    }
    
    protected static void customOnPause(final Activity activity) {
        if (PreferenceValues.getCurrentActivity() == activity) {
            PreferenceValues.setCurrentActivity(null);
        }
        MainApplication.onActivityPause();
    }
    
    protected static void customOnResume(final Activity currentActivity) {
        PreferenceValues.setCurrentActivity(currentActivity);
        PreferenceValues.enableWakeLock();
    }
    
    protected static void customOnStart(final Activity activity) {
        setStatusbar(activity);
        setScreenFullscreen(activity);
    }
    
    protected static void setScreenBasic(final Activity activity) {
        try {
            activity.requestWindowFeature(1);
        }
        catch (Exception ex) {}
    }
    
    public static void setScreenFullscreen(final Activity activity) {
        try {
            if (Preferences.APPEARANCE_FULLSCREEN) {
                activity.getWindow().setFlags(1024, 1024);
            }
            else {
                activity.getWindow().clearFlags(1024);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void setStatusbar(final Activity activity) {
        try {
            final NotificationManager notificationManager = (NotificationManager)activity.getSystemService("notification");
            if (Preferences.APPEARANCE_STATUSBAR) {
                final Context applicationContext = activity.getApplicationContext();
                final Intent intent = new Intent(applicationContext, (Class)MainActivity.class);
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setAction("android.intent.action.MAIN");
                notificationManager.notify(0, new NotificationCompat.Builder((Context)activity).setContentTitle(A.getAppName()).setSmallIcon(2130837551).setContentIntent(PendingIntent.getActivity(applicationContext, 0, intent, 0)).setOngoing(true).build());
            }
            else {
                notificationManager.cancel(0);
            }
        }
        catch (Exception ex) {}
    }
    
    private void unbindDrawables(final View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback((Drawable$Callback)null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                    this.unbindDrawables(((ViewGroup)view).getChildAt(i));
                }
                ((ViewGroup)view).removeAllViews();
            }
        }
    }
    
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }
    
    public int getParentViewId() {
        return -1;
    }
    
    public void onCreate(final Bundle bundle) {
        Logger.v(this.getLocalClassName(), "onCreate(), id:" + this.hashCode());
        try {
            super.onCreate(bundle);
            customOnCreate(this);
        }
        catch (Exception ex) {
            Logger.e(this.getLocalClassName(), "onCreate()", ex);
        }
    }
    
    public void onDestroy() {
        Logger.v(this.getLocalClassName(), "onDestroy(), id:" + this.hashCode());
        try {
            super.onDestroy();
            if (this.getParentViewId() != -1) {
                this.unbindDrawables(this.findViewById(this.getParentViewId()));
                System.gc();
            }
        }
        catch (Exception ex) {
            Logger.e(this.getLocalClassName(), "onDestroy()", ex);
        }
    }
    
    @Override
    protected void onPause() {
        Logger.v(this.getLocalClassName(), "onPause(), id:" + this.hashCode());
        try {
            super.onPause();
            customOnPause(this);
        }
        catch (Exception ex) {
            Logger.e(this.getLocalClassName(), "onPause()", ex);
        }
    }
    
    @Override
    protected void onResume() {
        Logger.v(this.getLocalClassName(), "onResume(), id:" + this.hashCode());
        try {
            super.onResume();
            customOnResume(this);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Const.SCREEN_WIDTH = displayMetrics.widthPixels;
            Const.SCREEN_HEIGHT = displayMetrics.heightPixels;
        }
        catch (Exception ex) {
            Logger.e(this.getLocalClassName(), "onResume()", ex);
        }
    }
    
    public void onStart() {
        Logger.v(this.getLocalClassName(), "onStart(), id:" + this.hashCode());
        try {
            super.onStart();
            customOnStart(this);
        }
        catch (Exception ex) {
            Logger.e(this.getLocalClassName(), "onStart()", ex);
        }
    }
}
