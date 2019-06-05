// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.web;

import java.util.HashMap;
import java.util.Map;

public class GeoPermissionCache
{
    private static final Map<String, Boolean> permissions;
    
    static {
        permissions = new HashMap<String, Boolean>();
    }
    
    public static void clear() {
        GeoPermissionCache.permissions.clear();
    }
    
    public static Boolean getAllowed(final String s) {
        return GeoPermissionCache.permissions.get(s);
    }
    
    public static void putAllowed(final String s, final Boolean b) {
        GeoPermissionCache.permissions.put(s, b);
    }
}
