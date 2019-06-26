// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import java.util.HashSet;

public final class ExoPlayerLibraryInfo
{
    private static final HashSet<String> registeredModules;
    private static String registeredModulesString;
    
    static {
        registeredModules = new HashSet<String>();
        ExoPlayerLibraryInfo.registeredModulesString = "goog.exo.core";
    }
    
    public static void registerModule(final String s) {
        synchronized (ExoPlayerLibraryInfo.class) {
            if (ExoPlayerLibraryInfo.registeredModules.add(s)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(ExoPlayerLibraryInfo.registeredModulesString);
                sb.append(", ");
                sb.append(s);
                ExoPlayerLibraryInfo.registeredModulesString = sb.toString();
            }
        }
    }
    
    public static String registeredModules() {
        synchronized (ExoPlayerLibraryInfo.class) {
            return ExoPlayerLibraryInfo.registeredModulesString;
        }
    }
}
