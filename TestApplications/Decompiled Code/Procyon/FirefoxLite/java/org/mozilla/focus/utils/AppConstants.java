// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

public final class AppConstants
{
    public static boolean isBetaBuild() {
        return "beta".equals("release");
    }
    
    public static boolean isBuiltWithFirebase() {
        return false;
    }
    
    public static boolean isDevBuild() {
        return "debug".equals("release");
    }
    
    public static boolean isFirebaseBuild() {
        return "firebase".equals("release");
    }
    
    public static boolean isNightlyBuild() {
        return false;
    }
    
    public static boolean isReleaseBuild() {
        return "release".equals("release");
    }
    
    public static boolean supportsDownloadingFiles() {
        return true;
    }
}
