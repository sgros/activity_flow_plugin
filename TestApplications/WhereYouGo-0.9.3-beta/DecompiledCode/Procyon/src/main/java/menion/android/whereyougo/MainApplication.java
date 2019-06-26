// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo;

import android.content.Intent;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.gui.SaveGame;
import menion.android.whereyougo.utils.ManagerNotify;
import android.app.Activity;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.gui.activity.MainActivity;
import java.util.ArrayList;
import menion.android.whereyougo.utils.StringToken;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.ExceptionHandler;
import android.content.res.Configuration;
import android.content.BroadcastReceiver;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.PreferenceValues;
import java.util.TimerTask;
import android.content.SharedPreferences;
import android.util.Log;
import android.preference.PreferenceManager;
import java.util.Locale;
import java.util.Timer;
import android.content.Context;
import android.app.Application;

public class MainApplication extends Application
{
    public static final String APP_NAME = "WhereYouGo";
    private static final String TAG = "MainApplication";
    private static Context applicationContext;
    private static Timer mTimer;
    private static OnAppVisibilityChange onAppVisibilityChange;
    private Locale locale;
    private boolean mScreenOff;
    private ScreenReceiver mScreenReceiver;
    
    public MainApplication() {
        this.locale = null;
        this.mScreenOff = false;
    }
    
    public static void appRestored() {
        onAppRestored();
        if (MainApplication.onAppVisibilityChange != null) {
            MainApplication.onAppVisibilityChange.onAppRestored();
        }
    }
    
    public static Context getContext() {
        return MainApplication.applicationContext;
    }
    
    private void initCore() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: ldc             "android.intent.action.SCREEN_ON"
        //     6: invokespecial   android/content/IntentFilter.<init>:(Ljava/lang/String;)V
        //     9: astore_1       
        //    10: aload_1        
        //    11: ldc             "android.intent.action.SCREEN_OFF"
        //    13: invokevirtual   android/content/IntentFilter.addAction:(Ljava/lang/String;)V
        //    16: aload_0        
        //    17: new             Lmenion/android/whereyougo/MainApplication$ScreenReceiver;
        //    20: dup            
        //    21: aload_0        
        //    22: aconst_null    
        //    23: invokespecial   menion/android/whereyougo/MainApplication$ScreenReceiver.<init>:(Lmenion/android/whereyougo/MainApplication;Lmenion/android/whereyougo/MainApplication$1;)V
        //    26: putfield        menion/android/whereyougo/MainApplication.mScreenReceiver:Lmenion/android/whereyougo/MainApplication$ScreenReceiver;
        //    29: aload_0        
        //    30: aload_0        
        //    31: getfield        menion/android/whereyougo/MainApplication.mScreenReceiver:Lmenion/android/whereyougo/MainApplication$ScreenReceiver;
        //    34: aload_1        
        //    35: invokevirtual   menion/android/whereyougo/MainApplication.registerReceiver:(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
        //    38: pop            
        //    39: aload_0        
        //    40: getstatic       menion/android/whereyougo/preferences/Preferences.GLOBAL_ROOT:Ljava/lang/String;
        //    43: invokevirtual   menion/android/whereyougo/MainApplication.setRoot:(Ljava/lang/String;)Z
        //    46: pop            
        //    47: aload_0        
        //    48: invokevirtual   menion/android/whereyougo/MainApplication.getExternalCacheDir:()Ljava/io/File;
        //    51: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    54: putstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //    57: getstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //    60: ldc             "/"
        //    62: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    65: ifne            92
        //    68: new             Ljava/lang/StringBuilder;
        //    71: dup            
        //    72: invokespecial   java/lang/StringBuilder.<init>:()V
        //    75: getstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: ldc             "/"
        //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    86: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    89: putstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //    92: new             Ljava/lang/StringBuilder;
        //    95: dup            
        //    96: invokespecial   java/lang/StringBuilder.<init>:()V
        //    99: getstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //   102: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   105: ldc             "audio/"
        //   107: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   110: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   113: putstatic       menion/android/whereyougo/utils/FileSystem.CACHE_AUDIO:Ljava/lang/String;
        //   116: aload_0        
        //   117: invokestatic    menion/android/whereyougo/geo/location/LocationState.init:(Landroid/content/Context;)V
        //   120: aload_0        
        //   121: fconst_1       
        //   122: invokestatic    menion/android/whereyougo/utils/Utils.getDpPixels:(Landroid/content/Context;F)F
        //   125: pop            
        //   126: ldc             "%s, app:%s"
        //   128: iconst_2       
        //   129: anewarray       Ljava/lang/Object;
        //   132: dup            
        //   133: iconst_0       
        //   134: invokestatic    menion/android/whereyougo/utils/A.getAppName:()Ljava/lang/String;
        //   137: aastore        
        //   138: dup            
        //   139: iconst_1       
        //   140: invokestatic    menion/android/whereyougo/utils/A.getAppVersion:()Ljava/lang/String;
        //   143: aastore        
        //   144: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   147: astore_2       
        //   148: ldc             "Android %s"
        //   150: iconst_1       
        //   151: anewarray       Ljava/lang/Object;
        //   154: dup            
        //   155: iconst_0       
        //   156: getstatic       android/os/Build$VERSION.RELEASE:Ljava/lang/String;
        //   159: aastore        
        //   160: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   163: astore_1       
        //   164: getstatic       cz/matejcik/openwig/WherigoLib.env:Ljava/util/Hashtable;
        //   167: ldc             "DeviceID"
        //   169: aload_2        
        //   170: invokevirtual   java/util/Hashtable.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   173: pop            
        //   174: getstatic       cz/matejcik/openwig/WherigoLib.env:Ljava/util/Hashtable;
        //   177: ldc             "Platform"
        //   179: aload_1        
        //   180: invokevirtual   java/util/Hashtable.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   183: pop            
        //   184: return         
        //   185: astore_1       
        //   186: aload_0        
        //   187: invokevirtual   menion/android/whereyougo/MainApplication.getCacheDir:()Ljava/io/File;
        //   190: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   193: putstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //   196: goto            57
        //   199: astore_1       
        //   200: new             Ljava/lang/StringBuilder;
        //   203: dup            
        //   204: invokespecial   java/lang/StringBuilder.<init>:()V
        //   207: getstatic       menion/android/whereyougo/utils/FileSystem.ROOT:Ljava/lang/String;
        //   210: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   213: ldc             "cache/"
        //   215: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   218: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   221: putstatic       menion/android/whereyougo/utils/FileSystem.CACHE:Ljava/lang/String;
        //   224: goto            57
        //   227: astore_1       
        //   228: goto            184
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  47     57     185    227    Ljava/lang/Exception;
        //  126    184    227    231    Ljava/lang/Exception;
        //  186    196    199    227    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0184:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void legacySupport4PreferencesFloat(final int n) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
        final String string = this.getString(n);
        try {
            defaultSharedPreferences.getString(string, "");
        }
        catch (Exception ex2) {
            try {
                Log.d("MainApplication", "legecySupport4PreferencesFloat() - LEGECY SUPPORT: convert float to string");
                final float float1 = defaultSharedPreferences.getFloat(string, 0.0f);
                defaultSharedPreferences.edit().remove(string).commit();
                defaultSharedPreferences.edit().putString(string, String.valueOf((Object)float1)).commit();
            }
            catch (Exception ex) {
                Log.e("MainApplication", "legecySupport4PreferencesFloat() - panic remove", (Throwable)ex);
                defaultSharedPreferences.edit().remove(string).commit();
            }
        }
    }
    
    private void legacySupport4PreferencesInt(int int1) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
        final String string = this.getString(int1);
        try {
            defaultSharedPreferences.getString(string, "");
        }
        catch (Exception ex2) {
            try {
                Log.d("MainApplication", "legecySupport4PreferencesInt() - LEGECY SUPPORT: convert int to string");
                int1 = defaultSharedPreferences.getInt(string, 0);
                defaultSharedPreferences.edit().remove(string).commit();
                defaultSharedPreferences.edit().putString(string, String.valueOf(int1)).commit();
            }
            catch (Exception ex) {
                Log.e("MainApplication", "legecySupportFloat2Int() - panic remove", (Throwable)ex);
                defaultSharedPreferences.edit().remove(string).commit();
            }
        }
    }
    
    public static void onActivityPause() {
        if (MainApplication.mTimer != null) {
            MainApplication.mTimer.cancel();
        }
        (MainApplication.mTimer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                if (!PreferenceValues.existCurrentActivity()) {
                    onAppMinimized();
                }
                LocationState.onActivityPauseInstant((Context)PreferenceValues.getCurrentActivity());
                MainApplication.mTimer = null;
            }
        }, 2000L);
    }
    
    private static void onAppMinimized() {
        Logger.w("MainApplication", "onAppMinimized()");
        if (MainApplication.onAppVisibilityChange != null) {
            MainApplication.onAppVisibilityChange.onAppMinimized();
        }
    }
    
    private static void onAppRestored() {
        Logger.w("MainApplication", "onAppRestored()");
    }
    
    public static void registerVisibilityHandler(final OnAppVisibilityChange onAppVisibilityChange) {
        MainApplication.onAppVisibilityChange = onAppVisibilityChange;
    }
    
    public void destroy() {
        while (true) {
            try {
                this.unregisterReceiver((BroadcastReceiver)this.mScreenReceiver);
                if (MainApplication.mTimer != null) {
                    MainApplication.mTimer.cancel();
                    MainApplication.mTimer = null;
                }
                MainApplication.onAppVisibilityChange = null;
            }
            catch (Exception obj) {
                Logger.w("MainApplication", "destroy(), e:" + obj);
                continue;
            }
            break;
        }
    }
    
    public boolean isScreenOff() {
        return this.mScreenOff;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.locale != null) {
            configuration.locale = this.locale;
            Locale.setDefault(this.locale);
            this.getBaseContext().getResources().updateConfiguration(configuration, this.getBaseContext().getResources().getDisplayMetrics());
        }
    }
    
    public void onCreate() {
    Label_0145_Outer:
        while (true) {
            super.onCreate();
            MainApplication.applicationContext = (Context)this;
            Log.d("MainApplication", "onCreate()");
            Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new ExceptionHandler());
            while (true) {
                ArrayList<String> parse = null;
            Label_0213:
                while (true) {
                    try {
                        this.legacySupport4PreferencesFloat(2131165565);
                        this.legacySupport4PreferencesFloat(2131165566);
                        this.legacySupport4PreferencesFloat(2131165564);
                        this.legacySupport4PreferencesInt(2131165568);
                        PreferenceManager.setDefaultValues((Context)this, 2131034113, false);
                        Preferences.setContext((Context)this);
                        Preferences.init((Context)this);
                        final Configuration configuration = this.getBaseContext().getResources().getConfiguration();
                        final String stringPreference = Preferences.getStringPreference(2131165580);
                        if (!stringPreference.equals(this.getString(2131165641)) && !configuration.locale.getLanguage().equals(stringPreference)) {
                            parse = StringToken.parse(stringPreference, "_");
                            if (parse.size() != 1) {
                                break Label_0213;
                            }
                            this.locale = new Locale(stringPreference);
                            configuration.locale = this.locale;
                            this.getBaseContext().getResources().updateConfiguration(configuration, this.getBaseContext().getResources().getDisplayMetrics());
                        }
                        this.initCore();
                        return;
                    }
                    catch (Exception ex) {
                        Log.e("MainApplication", "onCreate() - PANIC! Wipe out preferences", (Throwable)ex);
                        PreferenceManager.getDefaultSharedPreferences((Context)this).edit().clear().commit();
                        continue Label_0145_Outer;
                    }
                    break;
                }
                this.locale = new Locale(parse.get(0), parse.get(1));
                continue;
            }
        }
    }
    
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("MainApplication", "onLowMemory()");
    }
    
    public void onTerminate() {
        super.onTerminate();
        Log.d("MainApplication", "onTerminate()");
    }
    
    public void onTrimMemory(final int n) {
        super.onTrimMemory(n);
        Logger.i("MainApplication", String.format("onTrimMemory(%d)", n));
        try {
            if (Preferences.GLOBAL_SAVEGAME_AUTO && n == 20 && MainActivity.selectedFile != null && Engine.instance != null) {
                final Activity currentActivity = PreferenceValues.getCurrentActivity();
                if (currentActivity != null) {
                    if (MainActivity.wui != null) {
                        MainActivity.wui.setOnSavingFinished(new Runnable() {
                            @Override
                            public void run() {
                                ManagerNotify.toastShortMessage((Context)currentActivity, MainApplication.this.getString(2131165400));
                                MainActivity.wui.setOnSavingFinished(null);
                            }
                        });
                    }
                    new SaveGame((Context)currentActivity).execute((Object[])new Void[0]);
                }
            }
        }
        catch (Exception ex) {
            Logger.e("MainApplication", String.format("onTrimMemory(%d): savegame failed", n));
        }
    }
    
    public boolean setRoot(final String rootDirectory) {
        String absolutePath = null;
        while (true) {
            try {
                absolutePath = this.getExternalFilesDir((String)null).getAbsolutePath();
                String absolutePath2 = null;
                try {
                    absolutePath2 = this.getFilesDir().getAbsolutePath();
                    final boolean b = true;
                    boolean b2 = false;
                    Label_0078: {
                        if (rootDirectory != null) {
                            b2 = b;
                            if (FileSystem.setRootDirectory(rootDirectory)) {
                                break Label_0078;
                            }
                        }
                        if (absolutePath != null) {
                            b2 = b;
                            if (FileSystem.setRootDirectory(absolutePath)) {
                                break Label_0078;
                            }
                        }
                        if (absolutePath2 != null) {
                            b2 = b;
                            if (FileSystem.setRootDirectory(absolutePath2)) {
                                break Label_0078;
                            }
                        }
                        b2 = false;
                    }
                    Preferences.setStringPreference(2131165582, Preferences.GLOBAL_ROOT = FileSystem.ROOT);
                    if (!b2) {
                        ManagerNotify.toastShortMessage((Context)this, this.getString(2131165203));
                    }
                    return b2;
                }
                catch (Exception ex) {}
            }
            catch (Exception ex2) {
                continue;
            }
            break;
        }
    }
    
    public interface OnAppVisibilityChange
    {
        void onAppMinimized();
        
        void onAppRestored();
    }
    
    private class ScreenReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MainApplication.this.mScreenOff = true;
            }
            else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                LocationState.onScreenOn(false);
                MainApplication.this.mScreenOff = false;
            }
        }
    }
}
