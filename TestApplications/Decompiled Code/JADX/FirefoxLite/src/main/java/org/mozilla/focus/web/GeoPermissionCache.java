package org.mozilla.focus.web;

import java.util.HashMap;
import java.util.Map;

public class GeoPermissionCache {
    private static final Map<String, Boolean> permissions = new HashMap();

    public static Boolean getAllowed(String str) {
        return (Boolean) permissions.get(str);
    }

    public static void putAllowed(String str, Boolean bool) {
        permissions.put(str, bool);
    }

    public static void clear() {
        permissions.clear();
    }
}
