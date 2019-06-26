package com.google.android.exoplayer2;

import java.util.HashSet;

public final class ExoPlayerLibraryInfo {
    private static final HashSet<String> registeredModules = new HashSet();
    private static String registeredModulesString = "goog.exo.core";

    public static synchronized String registeredModules() {
        String str;
        synchronized (ExoPlayerLibraryInfo.class) {
            str = registeredModulesString;
        }
        return str;
    }

    public static synchronized void registerModule(String str) {
        synchronized (ExoPlayerLibraryInfo.class) {
            if (registeredModules.add(str)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(registeredModulesString);
                stringBuilder.append(", ");
                stringBuilder.append(str);
                registeredModulesString = stringBuilder.toString();
            }
        }
    }
}
