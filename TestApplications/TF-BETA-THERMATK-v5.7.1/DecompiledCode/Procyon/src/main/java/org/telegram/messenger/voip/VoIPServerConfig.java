// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import org.json.JSONException;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import org.json.JSONObject;

public class VoIPServerConfig
{
    private static JSONObject config;
    
    static {
        VoIPServerConfig.config = new JSONObject();
    }
    
    public static boolean getBoolean(final String s, final boolean b) {
        return VoIPServerConfig.config.optBoolean(s, b);
    }
    
    public static double getDouble(final String s, final double n) {
        return VoIPServerConfig.config.optDouble(s, n);
    }
    
    public static int getInt(final String s, final int n) {
        return VoIPServerConfig.config.optInt(s, n);
    }
    
    public static String getString(final String s, final String s2) {
        return VoIPServerConfig.config.optString(s, s2);
    }
    
    private static native void nativeSetConfig(final String p0);
    
    public static void setConfig(final String s) {
        try {
            VoIPServerConfig.config = new JSONObject(s);
            nativeSetConfig(s);
        }
        catch (JSONException ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error parsing VoIP config", (Throwable)ex);
            }
        }
    }
}
