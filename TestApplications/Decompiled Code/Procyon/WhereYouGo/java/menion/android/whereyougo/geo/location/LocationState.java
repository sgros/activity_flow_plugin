// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.location;

import menion.android.whereyougo.gui.activity.SatelliteActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.provider.Settings$Secure;
import menion.android.whereyougo.utils.Const;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import android.location.GpsSatellite;
import android.os.Bundle;
import android.location.GpsStatus;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.preferences.PreferenceValues;
import android.app.Activity;
import android.content.Context;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

public class LocationState
{
    private static final int GPS_OFF = 1;
    private static final int GPS_ON = 0;
    private static final String KEY_B_GPS_ENABLE_ASK_ON_ENABLE = "KEY_B_GPS_ENABLE_ASK_ON_ENABLE";
    public static final String SIMULATE_LOCATION = "SIMULATE_LOCATION";
    private static final String TAG = "LocationState";
    private static GpsConnection gpsConn;
    private static int lastSource;
    private static Location location;
    private static long mLastGpsFixTime;
    private static ArrayList<ILocationEventListener> mListeners;
    private static final Point2D.Int mSatsCount;
    private static int mSource;
    private static boolean speedCorrection;
    
    static {
        LocationState.mSource = 1;
        LocationState.mLastGpsFixTime = 0L;
        mSatsCount = new Point2D.Int();
        LocationState.speedCorrection = false;
    }
    
    public static void addLocationChangeListener(final ILocationEventListener locationEventListener) {
        // monitorenter(LocationState.class)
        if (locationEventListener == null) {
            return;
        }
        try {
            if (LocationState.mListeners != null && !LocationState.mListeners.contains(locationEventListener)) {
                LocationState.mListeners.add(locationEventListener);
                if (LocationState.mListeners.size() > 0) {
                    Collections.sort(LocationState.mListeners, new Comparator<ILocationEventListener>() {
                        @Override
                        public int compare(final ILocationEventListener locationEventListener, final ILocationEventListener locationEventListener2) {
                            return locationEventListener.getPriority() - locationEventListener2.getPriority();
                        }
                    });
                }
                onScreenOn(true);
            }
        }
        finally {
        }
        // monitorexit(LocationState.class)
    }
    
    public static void destroy(final Context context) {
        setState(context, 1, false);
        LocationState.mListeners.clear();
        LocationState.gpsConn = null;
        LocationState.location = null;
    }
    
    public static long getLastFixTime() {
        return LocationState.mLastGpsFixTime;
    }
    
    public static Location getLastKnownLocation(final Activity p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "location"
        //     3: invokevirtual   android/app/Activity.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //     6: checkcast       Landroid/location/LocationManager;
        //     9: astore_1       
        //    10: aconst_null    
        //    11: astore_2       
        //    12: new             Lmenion/android/whereyougo/geo/location/Location;
        //    15: astore_0       
        //    16: aload_0        
        //    17: aload_1        
        //    18: ldc             "gps"
        //    20: invokevirtual   android/location/LocationManager.getLastKnownLocation:(Ljava/lang/String;)Landroid/location/Location;
        //    23: invokespecial   menion/android/whereyougo/geo/location/Location.<init>:(Landroid/location/Location;)V
        //    26: aload_0        
        //    27: astore_2       
        //    28: aconst_null    
        //    29: astore_3       
        //    30: new             Lmenion/android/whereyougo/geo/location/Location;
        //    33: astore_0       
        //    34: aload_0        
        //    35: aload_1        
        //    36: ldc             "network"
        //    38: invokevirtual   android/location/LocationManager.getLastKnownLocation:(Ljava/lang/String;)Landroid/location/Location;
        //    41: invokespecial   menion/android/whereyougo/geo/location/Location.<init>:(Landroid/location/Location;)V
        //    44: aload_0        
        //    45: astore_3       
        //    46: aload_2        
        //    47: ifnull          119
        //    50: aload_3        
        //    51: ifnull          119
        //    54: aload_2        
        //    55: invokevirtual   menion/android/whereyougo/geo/location/Location.getTime:()J
        //    58: aload_3        
        //    59: invokevirtual   menion/android/whereyougo/geo/location/Location.getTime:()J
        //    62: lcmp           
        //    63: ifle            114
        //    66: aload_2        
        //    67: astore_0       
        //    68: aload_0        
        //    69: areturn        
        //    70: astore_0       
        //    71: ldc             "LocationState"
        //    73: ldc             "Failed to retrieve location: access appears to be disabled."
        //    75: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //    78: goto            28
        //    81: astore_0       
        //    82: ldc             "LocationState"
        //    84: ldc             "Failed to retrieve location: device has no GPS provider."
        //    86: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //    89: goto            28
        //    92: astore_0       
        //    93: ldc             "LocationState"
        //    95: ldc             "Failed to retrieve location: access appears to be disabled."
        //    97: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   100: goto            46
        //   103: astore_0       
        //   104: ldc             "LocationState"
        //   106: ldc             "Failed to retrieve location: device has no network provider."
        //   108: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   111: goto            46
        //   114: aload_3        
        //   115: astore_0       
        //   116: goto            68
        //   119: aload_2        
        //   120: astore_0       
        //   121: aload_2        
        //   122: ifnonnull       68
        //   125: aload_3        
        //   126: ifnull          134
        //   129: aload_3        
        //   130: astore_0       
        //   131: goto            68
        //   134: aconst_null    
        //   135: astore_0       
        //   136: goto            68
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  12     26     70     81     Ljava/lang/SecurityException;
        //  12     26     81     92     Ljava/lang/IllegalArgumentException;
        //  30     44     92     103    Ljava/lang/SecurityException;
        //  30     44     103    114    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0046:
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
    
    public static Location getLocation() {
        Location location;
        if (LocationState.location == null) {
            location = new Location("LocationState");
        }
        else {
            location = new Location(LocationState.location);
        }
        return location;
    }
    
    public static Point2D.Int getSatCount() {
        return LocationState.mSatsCount;
    }
    
    public static void init(final Context context) {
        if (LocationState.location == null) {
            LocationState.location = PreferenceValues.getLastKnownLocation();
            LocationState.mListeners = new ArrayList<ILocationEventListener>();
            LocationState.lastSource = -1;
        }
    }
    
    public static boolean isActualLocationHardwareGps() {
        return LocationState.mSource == 0 && LocationState.location.getProvider().equals("gps");
    }
    
    public static boolean isActualLocationHardwareNetwork() {
        return LocationState.mSource == 0 && LocationState.location.getProvider().equals("network");
    }
    
    public static boolean isActuallyHardwareGpsOn() {
        return LocationState.mSource == 0;
    }
    
    public static boolean isGpsRequired() {
        boolean b;
        if (LocationState.mListeners == null) {
            b = false;
        }
        else {
            final boolean b2 = false;
            int index = 0;
            while (true) {
                b = b2;
                if (index >= LocationState.mListeners.size()) {
                    return b;
                }
                if (LocationState.mListeners.get(index).isRequired()) {
                    break;
                }
                ++index;
            }
            b = true;
        }
        return b;
    }
    
    public static void onActivityPauseInstant(final Context context) {
        while (true) {
            int n = 1;
            boolean b = false;
            Label_0102: {
                try {
                    if (A.getApp() != null && ((MainApplication)A.getApp()).isScreenOff()) {
                        b = true;
                    }
                    else {
                        b = false;
                    }
                    if (context == null || !Preferences.GPS_DISABLE_WHEN_HIDE) {
                        n = 0;
                    }
                    if (!PreferenceValues.existCurrentActivity() || b) {
                        PreferenceValues.disableWakeLock();
                    }
                    if (n != 0) {
                        if (LocationState.mListeners.size() != 0 || PreferenceValues.existCurrentActivity()) {
                            break Label_0102;
                        }
                        LocationState.lastSource = LocationState.mSource;
                        setState(context, 1, true);
                    }
                    return;
                }
                catch (Exception ex) {
                    Logger.e("LocationState", "onActivityPauseInstant()", ex);
                    return;
                }
                return;
            }
            if (!b && PreferenceValues.existCurrentActivity()) {
                LocationState.lastSource = -1;
                return;
            }
            if (!isGpsRequired()) {
                LocationState.lastSource = LocationState.mSource;
                setState(context, 1, true);
                return;
            }
            LocationState.lastSource = -1;
        }
    }
    
    static void onGpsStatusChanged(final int n, final GpsStatus gpsStatus) {
        if (LocationState.mListeners != null && LocationState.mListeners.size() != 0) {
            if (n == 1 || n == 2) {
                for (int i = 0; i < LocationState.mListeners.size(); ++i) {
                    final ILocationEventListener locationEventListener = LocationState.mListeners.get(i);
                    int n2;
                    if (n == 1) {
                        n2 = 2;
                    }
                    else {
                        n2 = 1;
                    }
                    locationEventListener.onStatusChanged("gps", n2, null);
                }
            }
            else if (n == 4) {
                ArrayList<SatellitePosition> list = null;
                if (gpsStatus != null) {
                    final ArrayList<SatellitePosition> list2 = new ArrayList<SatellitePosition>();
                    final Iterator iterator = gpsStatus.getSatellites().iterator();
                    LocationState.mSatsCount.x = 0;
                    LocationState.mSatsCount.y = 0;
                    while (true) {
                        list = list2;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        final GpsSatellite gpsSatellite = iterator.next();
                        final SatellitePosition e = new SatellitePosition();
                        e.azimuth = gpsSatellite.getAzimuth();
                        e.elevation = gpsSatellite.getElevation();
                        e.prn = gpsSatellite.getPrn();
                        e.snr = (int)gpsSatellite.getSnr();
                        e.fixed = gpsSatellite.usedInFix();
                        if (e.fixed) {
                            final Point2D.Int mSatsCount = LocationState.mSatsCount;
                            ++mSatsCount.x;
                        }
                        final Point2D.Int mSatsCount2 = LocationState.mSatsCount;
                        ++mSatsCount2.y;
                        list2.add(e);
                    }
                }
                postGpsSatelliteChange(list);
            }
        }
    }
    
    protected static void onGpsStatusChanged(final Hashtable<Integer, SatellitePosition> hashtable) {
        ArrayList<SatellitePosition> list = null;
        if (hashtable != null) {
            final ArrayList<SatellitePosition> list2 = new ArrayList<SatellitePosition>();
            final Enumeration<SatellitePosition> elements = hashtable.elements();
            LocationState.mSatsCount.x = 0;
            LocationState.mSatsCount.y = 0;
            while (true) {
                list = list2;
                if (!elements.hasMoreElements()) {
                    break;
                }
                final SatellitePosition e = elements.nextElement();
                list2.add(e);
                if (e.fixed) {
                    final Point2D.Int mSatsCount = LocationState.mSatsCount;
                    ++mSatsCount.x;
                }
                final Point2D.Int mSatsCount2 = LocationState.mSatsCount;
                ++mSatsCount2.y;
            }
        }
        postGpsSatelliteChange(list);
    }
    
    static void onLocationChanged(final Location location) {
        if (location != null) {
            try {
                if (LocationState.location != null) {
                    if (LocationState.location.getProvider().equals("network") && location.getProvider().equals("gps") && LocationState.location.getAccuracy() * 3.0f < location.getAccuracy()) {
                        return;
                    }
                    if (!LocationState.speedCorrection && location.getTime() - LocationState.location.getTime() < 5000L && location.getSpeed() > 100.0f && location.getSpeed() / LocationState.location.getSpeed() > 2.0f) {
                        location.setSpeed(LocationState.location.getSpeed());
                        LocationState.speedCorrection = true;
                    }
                    else {
                        LocationState.speedCorrection = false;
                    }
                    if (LocationState.location.getProvider().equals("gps")) {
                        LocationState.mLastGpsFixTime = System.currentTimeMillis();
                    }
                    if (location.getSpeed() < 0.5f && Math.abs(location.getBearing() - LocationState.location.getBearing()) > 25.0) {
                        location.setBearing(LocationState.location.getBearing());
                    }
                }
                if (location.getProvider().equals("gps")) {
                    location.setAltitude(location.getAltitude() + Preferences.GPS_ALTITUDE_CORRECTION);
                }
                LocationState.location = location;
                for (int i = 0; i < LocationState.mListeners.size(); ++i) {
                    LocationState.mListeners.get(i).onLocationChanged(location);
                }
            }
            catch (Exception ex) {
                Logger.e("LocationState", "onLocationChanged(" + location + ")", ex);
            }
        }
    }
    
    static void onProviderDisabled(final String s) {
    }
    
    static void onProviderEnabled(final String str) {
        Logger.w("LocationState", "onProviderEnabled(" + str + ")");
    }
    
    public static void onScreenOn(final boolean b) {
        if (LocationState.lastSource != -1 && LocationState.mListeners != null && LocationState.mListeners.size() > 0 && (PreferenceValues.existCurrentActivity() || b)) {
            setState((Context)PreferenceValues.getCurrentActivity(), LocationState.lastSource, true);
            LocationState.lastSource = -1;
        }
    }
    
    static void onStatusChanged(final String str, final int i, final Bundle obj) {
        Logger.w("LocationState", "onStatusChanged(" + str + ", " + i + ", " + obj + ")");
        for (int j = 0; j < LocationState.mListeners.size(); ++j) {
            LocationState.mListeners.get(j).onStatusChanged(str, i, obj);
        }
        if (str.equals("gps") && i == 1 && LocationState.location != null) {
            LocationState.location.setProvider("network");
            onLocationChanged(LocationState.location);
        }
    }
    
    private static void postGpsSatelliteChange(final ArrayList<SatellitePosition> list) {
        if (PreferenceValues.getCurrentActivity() != null) {
            PreferenceValues.getCurrentActivity().runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < LocationState.mListeners.size(); ++i) {
                        ((ILocationEventListener)LocationState.mListeners.get(i)).onGpsStatusChanged(4, list);
                    }
                }
            });
        }
    }
    
    public static void removeLocationChangeListener(final ILocationEventListener obj) {
        synchronized (LocationState.class) {
            if (LocationState.mListeners.size() != 0 && obj != null && LocationState.mListeners.contains(obj)) {
                LocationState.mListeners.remove(obj);
                Logger.i("LocationState", "removeLocationChangeListener(" + obj + "), actualSize:" + LocationState.mListeners.size());
            }
        }
    }
    
    public static void setGpsOff(final Context context) {
        setState(context, 1, true);
    }
    
    public static void setGpsOn(final Context gpsOff) {
        if (LocationState.mSource == 0) {
            setGpsOff(gpsOff);
        }
        setState(gpsOff, 0, true);
    }
    
    public static void setLocationDirectly(final Location location) {
        if (!Const.STATE_RELEASE && !isActuallyHardwareGpsOn()) {
            location.setSpeed(20.0f);
            if (LocationState.location != null) {
                location.setBearing(LocationState.location.bearingTo(location));
            }
        }
        onLocationChanged(location);
    }
    
    private static void setState(final Context context, int n, final boolean b) {
        if (LocationState.mSource != n) {
            if (b && context != null) {
                Preferences.setPreference(2131165552, Preferences.GPS = (n == 0));
            }
            if (n == 0 && context != null) {
                LocationState.mSource = 0;
                if (LocationState.gpsConn != null) {
                    LocationState.gpsConn.destroy();
                    LocationState.gpsConn = null;
                }
                n = 0;
                final String string = Settings$Secure.getString(context.getContentResolver(), "location_providers_allowed");
                if (string != null && (string.contains("network") || string.contains("gps"))) {
                    LocationState.gpsConn = new GpsConnection(context);
                }
                else {
                    UtilsGUI.showDialogQuestion(PreferenceValues.getCurrentActivity(), 2131165210, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                        }
                    }, null);
                    n = 1;
                }
                if (n != 0) {
                    if (context instanceof SatelliteActivity) {
                        ((SatelliteActivity)context).notifyGpsDisable();
                    }
                    setState(context, 1, true);
                }
            }
            else {
                LocationState.mSource = 1;
                onGpsStatusChanged(2, null);
                if (LocationState.gpsConn != null) {
                    LocationState.gpsConn.destroy();
                    LocationState.gpsConn = null;
                }
            }
            onLocationChanged(LocationState.location);
        }
    }
}
