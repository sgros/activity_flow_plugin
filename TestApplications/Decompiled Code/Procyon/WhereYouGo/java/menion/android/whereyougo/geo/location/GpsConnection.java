// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.location;

import android.os.Bundle;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import android.location.GpsStatus$Listener;
import java.util.TimerTask;
import menion.android.whereyougo.audio.UtilsAudio;
import menion.android.whereyougo.preferences.Preferences;
import android.location.LocationListener;
import android.content.Context;
import java.util.Timer;
import android.location.LocationManager;
import android.location.GpsStatus;

public class GpsConnection
{
    private static final String TAG = "GpsConnection";
    private final MyGpsListener gpsListener;
    private boolean gpsProviderEnabled;
    private GpsStatus gpsStatus;
    private boolean isFixed;
    private final MyLocationListener llGPS;
    private final MyLocationListener llNetwork;
    private LocationManager locationManager;
    private Timer mGpsTimer;
    private boolean networkProviderEnabled;
    
    public GpsConnection(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: ldc             "GpsConnection"
        //     6: ldc             "onCreate()"
        //     8: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //    11: aload_0        
        //    12: new             Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //    15: dup            
        //    16: aload_0        
        //    17: invokespecial   menion/android/whereyougo/geo/location/GpsConnection$MyLocationListener.<init>:(Lmenion/android/whereyougo/geo/location/GpsConnection;)V
        //    20: putfield        menion/android/whereyougo/geo/location/GpsConnection.llGPS:Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //    23: aload_0        
        //    24: new             Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //    27: dup            
        //    28: aload_0        
        //    29: invokespecial   menion/android/whereyougo/geo/location/GpsConnection$MyLocationListener.<init>:(Lmenion/android/whereyougo/geo/location/GpsConnection;)V
        //    32: putfield        menion/android/whereyougo/geo/location/GpsConnection.llNetwork:Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //    35: aload_0        
        //    36: new             Lmenion/android/whereyougo/geo/location/GpsConnection$MyGpsListener;
        //    39: dup            
        //    40: aload_0        
        //    41: aconst_null    
        //    42: invokespecial   menion/android/whereyougo/geo/location/GpsConnection$MyGpsListener.<init>:(Lmenion/android/whereyougo/geo/location/GpsConnection;Lmenion/android/whereyougo/geo/location/GpsConnection$1;)V
        //    45: putfield        menion/android/whereyougo/geo/location/GpsConnection.gpsListener:Lmenion/android/whereyougo/geo/location/GpsConnection$MyGpsListener;
        //    48: aload_0        
        //    49: iconst_0       
        //    50: putfield        menion/android/whereyougo/geo/location/GpsConnection.isFixed:Z
        //    53: aload_0        
        //    54: aload_1        
        //    55: ldc             "location"
        //    57: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //    60: checkcast       Landroid/location/LocationManager;
        //    63: putfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //    66: aload_0        
        //    67: getfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //    70: invokevirtual   android/location/LocationManager.getAllProviders:()Ljava/util/List;
        //    73: astore_2       
        //    74: aload_0        
        //    75: getfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //    78: aload_0        
        //    79: getfield        menion/android/whereyougo/geo/location/GpsConnection.llGPS:Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //    82: invokevirtual   android/location/LocationManager.removeUpdates:(Landroid/location/LocationListener;)V
        //    85: aload_0        
        //    86: getfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //    89: aload_0        
        //    90: getfield        menion/android/whereyougo/geo/location/GpsConnection.llNetwork:Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //    93: invokevirtual   android/location/LocationManager.removeUpdates:(Landroid/location/LocationListener;)V
        //    96: aload_2        
        //    97: ldc             "network"
        //    99: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   104: ifeq            134
        //   107: aload_0        
        //   108: getfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //   111: ldc             "network"
        //   113: getstatic       menion/android/whereyougo/preferences/Preferences.GPS_MIN_TIME:I
        //   116: sipush          1000
        //   119: imul           
        //   120: i2l            
        //   121: fconst_0       
        //   122: aload_0        
        //   123: getfield        menion/android/whereyougo/geo/location/GpsConnection.llNetwork:Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //   126: invokevirtual   android/location/LocationManager.requestLocationUpdates:(Ljava/lang/String;JFLandroid/location/LocationListener;)V
        //   129: aload_0        
        //   130: iconst_1       
        //   131: putfield        menion/android/whereyougo/geo/location/GpsConnection.networkProviderEnabled:Z
        //   134: aload_2        
        //   135: ldc             "gps"
        //   137: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   142: ifeq            172
        //   145: aload_0        
        //   146: getfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //   149: ldc             "gps"
        //   151: getstatic       menion/android/whereyougo/preferences/Preferences.GPS_MIN_TIME:I
        //   154: sipush          1000
        //   157: imul           
        //   158: i2l            
        //   159: fconst_0       
        //   160: aload_0        
        //   161: getfield        menion/android/whereyougo/geo/location/GpsConnection.llGPS:Lmenion/android/whereyougo/geo/location/GpsConnection$MyLocationListener;
        //   164: invokevirtual   android/location/LocationManager.requestLocationUpdates:(Ljava/lang/String;JFLandroid/location/LocationListener;)V
        //   167: aload_0        
        //   168: iconst_1       
        //   169: putfield        menion/android/whereyougo/geo/location/GpsConnection.gpsProviderEnabled:Z
        //   172: aload_0        
        //   173: getfield        menion/android/whereyougo/geo/location/GpsConnection.locationManager:Landroid/location/LocationManager;
        //   176: aload_0        
        //   177: getfield        menion/android/whereyougo/geo/location/GpsConnection.gpsListener:Lmenion/android/whereyougo/geo/location/GpsConnection$MyGpsListener;
        //   180: invokevirtual   android/location/LocationManager.addGpsStatusListener:(Landroid/location/GpsStatus$Listener;)Z
        //   183: pop            
        //   184: aload_0        
        //   185: getfield        menion/android/whereyougo/geo/location/GpsConnection.networkProviderEnabled:Z
        //   188: ifne            198
        //   191: aload_0        
        //   192: getfield        menion/android/whereyougo/geo/location/GpsConnection.gpsProviderEnabled:Z
        //   195: ifeq            359
        //   198: aload_1        
        //   199: aload_1        
        //   200: ldc             2131165208
        //   202: invokevirtual   android/content/Context.getString:(I)Ljava/lang/String;
        //   205: invokestatic    menion/android/whereyougo/utils/ManagerNotify.toastShortMessage:(Landroid/content/Context;Ljava/lang/String;)V
        //   208: return         
        //   209: astore_3       
        //   210: ldc             "GpsConnection"
        //   212: new             Ljava/lang/StringBuilder;
        //   215: dup            
        //   216: invokespecial   java/lang/StringBuilder.<init>:()V
        //   219: ldc             "problem removing listeners llGPS, e:"
        //   221: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   224: aload_3        
        //   225: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   228: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   231: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   234: goto            85
        //   237: astore_3       
        //   238: ldc             "GpsConnection"
        //   240: new             Ljava/lang/StringBuilder;
        //   243: dup            
        //   244: invokespecial   java/lang/StringBuilder.<init>:()V
        //   247: ldc             "problem removing listeners llNetwork, e:"
        //   249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   252: aload_3        
        //   253: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   256: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   259: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   262: goto            96
        //   265: astore_3       
        //   266: ldc             "GpsConnection"
        //   268: new             Ljava/lang/StringBuilder;
        //   271: dup            
        //   272: invokespecial   java/lang/StringBuilder.<init>:()V
        //   275: ldc             "problem adding 'network' provider, e:"
        //   277: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   280: aload_3        
        //   281: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   284: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   287: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   290: aload_0        
        //   291: iconst_0       
        //   292: putfield        menion/android/whereyougo/geo/location/GpsConnection.networkProviderEnabled:Z
        //   295: goto            134
        //   298: astore_2       
        //   299: ldc             "GpsConnection"
        //   301: new             Ljava/lang/StringBuilder;
        //   304: dup            
        //   305: invokespecial   java/lang/StringBuilder.<init>:()V
        //   308: ldc             "problem adding 'GPS' provider, e:"
        //   310: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   313: aload_2        
        //   314: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   317: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   320: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   323: aload_0        
        //   324: iconst_0       
        //   325: putfield        menion/android/whereyougo/geo/location/GpsConnection.gpsProviderEnabled:Z
        //   328: goto            172
        //   331: astore_2       
        //   332: ldc             "GpsConnection"
        //   334: new             Ljava/lang/StringBuilder;
        //   337: dup            
        //   338: invokespecial   java/lang/StringBuilder.<init>:()V
        //   341: ldc             "problem adding 'GPS status' listener, e:"
        //   343: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   346: aload_2        
        //   347: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   350: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   353: invokestatic    menion/android/whereyougo/utils/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   356: goto            184
        //   359: invokestatic    menion/android/whereyougo/preferences/PreferenceValues.getCurrentActivity:()Landroid/app/Activity;
        //   362: ifnull          373
        //   365: invokestatic    menion/android/whereyougo/preferences/PreferenceValues.getCurrentActivity:()Landroid/app/Activity;
        //   368: ldc             2131165226
        //   370: invokestatic    menion/android/whereyougo/gui/utils/UtilsGUI.showDialogInfo:(Landroid/app/Activity;I)V
        //   373: aload_1        
        //   374: invokestatic    menion/android/whereyougo/geo/location/LocationState.setGpsOff:(Landroid/content/Context;)V
        //   377: aload_0        
        //   378: invokevirtual   menion/android/whereyougo/geo/location/GpsConnection.destroy:()V
        //   381: goto            208
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  74     85     209    237    Ljava/lang/Exception;
        //  85     96     237    265    Ljava/lang/Exception;
        //  107    134    265    298    Ljava/lang/Exception;
        //  145    172    298    331    Ljava/lang/Exception;
        //  172    184    331    359    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 177 out-of-bounds for length 177
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    
    private void disableNetwork() {
        if (this.networkProviderEnabled) {
            this.locationManager.removeUpdates((LocationListener)this.llNetwork);
            this.networkProviderEnabled = false;
        }
    }
    
    private void enableNetwork() {
        if (this.networkProviderEnabled) {
            return;
        }
        try {
            this.locationManager.requestLocationUpdates("network", (long)(Preferences.GPS_MIN_TIME * 1000), 0.0f, (LocationListener)this.llNetwork);
            this.networkProviderEnabled = true;
        }
        catch (Exception ex) {}
    }
    
    private void handleOnLocationChanged(final Location location) {
        synchronized (this) {
            if (!this.isFixed) {
                if (location.getProvider().equals("gps")) {
                    if (Preferences.GPS_BEEP_ON_GPS_FIX) {
                        UtilsAudio.playBeep(1);
                    }
                    this.disableNetwork();
                    this.isFixed = true;
                }
                LocationState.onLocationChanged(location);
            }
            else if (location.getProvider().equals("gps")) {
                LocationState.onLocationChanged(location);
                this.setNewTimer();
            }
        }
    }
    
    private void setNewTimer() {
        if (this.mGpsTimer != null) {
            this.mGpsTimer.cancel();
        }
        (this.mGpsTimer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                if (Preferences.GPS_BEEP_ON_GPS_FIX) {
                    UtilsAudio.playBeep(2);
                }
                GpsConnection.this.mGpsTimer = null;
                GpsConnection.this.isFixed = false;
            }
        }, 60000L);
    }
    
    public void destroy() {
        if (this.locationManager != null) {
            this.disableNetwork();
            this.locationManager.removeUpdates((LocationListener)this.llGPS);
            this.locationManager.removeGpsStatusListener((GpsStatus$Listener)this.gpsListener);
            this.locationManager = null;
            ManagerNotify.toastShortMessage(2131165207);
        }
    }
    
    public boolean isProviderEnabled(final String s) {
        return this.locationManager != null && this.locationManager.isProviderEnabled(s);
    }
    
    private class MyGpsListener implements GpsStatus$Listener
    {
        public void onGpsStatusChanged(final int i) {
            while (true) {
                Label_0126: {
                    while (true) {
                        Label_0098: {
                            try {
                                if (GpsConnection.this.locationManager != null && i != 3) {
                                    if (i != 4) {
                                        break Label_0126;
                                    }
                                    if (GpsConnection.this.gpsStatus != null) {
                                        break Label_0098;
                                    }
                                    GpsConnection.this.gpsStatus = GpsConnection.this.locationManager.getGpsStatus((GpsStatus)null);
                                    LocationState.onGpsStatusChanged(i, GpsConnection.this.gpsStatus);
                                }
                                return;
                            }
                            catch (Exception ex) {
                                Logger.e("GpsConnection", "onGpsStatusChanged(" + i + ")", ex);
                                return;
                            }
                        }
                        GpsConnection.this.gpsStatus = GpsConnection.this.locationManager.getGpsStatus(GpsConnection.this.gpsStatus);
                        continue;
                    }
                }
                if (i == 1) {
                    LocationState.onGpsStatusChanged(i, null);
                    return;
                }
                if (i == 2) {
                    LocationState.onGpsStatusChanged(i, null);
                }
            }
        }
    }
    
    private class MyLocationListener implements LocationListener
    {
        public MyLocationListener() {
        }
        
        public void onLocationChanged(final android.location.Location location) {
            GpsConnection.this.handleOnLocationChanged(new Location(location));
        }
        
        public void onProviderDisabled(final String s) {
            LocationState.onProviderDisabled(s);
            if (GpsConnection.this.locationManager != null && !GpsConnection.this.locationManager.isProviderEnabled("gps") && !GpsConnection.this.locationManager.isProviderEnabled("network")) {
                LocationState.setGpsOff((Context)PreferenceValues.getCurrentActivity());
                GpsConnection.this.destroy();
            }
            else if (s.equals("gps")) {
                GpsConnection.this.enableNetwork();
            }
        }
        
        public void onProviderEnabled(final String s) {
            LocationState.onProviderEnabled(s);
        }
        
        public void onStatusChanged(final String s, final int n, final Bundle bundle) {
            LocationState.onStatusChanged(s, n, bundle);
        }
    }
}
