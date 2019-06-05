package org.mozilla.focus.web;

import java.util.HashMap;
import java.util.Map;

public class GeoPermissionCache {
   private static final Map permissions = new HashMap();

   public static void clear() {
      permissions.clear();
   }

   public static Boolean getAllowed(String var0) {
      return (Boolean)permissions.get(var0);
   }

   public static void putAllowed(String var0, Boolean var1) {
      permissions.put(var0, var1);
   }
}
