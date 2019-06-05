package com.adjust.sdk;

import android.content.Context;
import android.content.res.Configuration;
import com.adjust.sdk.plugin.Plugin;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class Reflection {
   public static Object createDefaultInstance(Class var0) {
      try {
         Object var2 = var0.newInstance();
         return var2;
      } catch (Throwable var1) {
         return null;
      }
   }

   public static Object createDefaultInstance(String var0) {
      return createDefaultInstance(forName(var0));
   }

   public static Object createInstance(String var0, Class[] var1, Object... var2) {
      try {
         Object var4 = Class.forName(var0).getConstructor(var1).newInstance(var2);
         return var4;
      } catch (Throwable var3) {
         return null;
      }
   }

   public static Class forName(String var0) {
      try {
         Class var2 = Class.forName(var0);
         return var2;
      } catch (Throwable var1) {
         return null;
      }
   }

   private static Object getAdvertisingInfoObject(Context var0) throws Exception {
      return invokeStaticMethod("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[]{Context.class}, var0);
   }

   public static String getAndroidId(Context var0) {
      try {
         String var2 = (String)invokeStaticMethod("com.adjust.sdk.plugin.AndroidIdUtil", "getAndroidId", new Class[]{Context.class}, var0);
         return var2;
      } catch (Throwable var1) {
         return null;
      }
   }

   public static String getCpuAbi() {
      String var0;
      try {
         var0 = (String)readField("android.os.Build", "CPU_ABI");
      } catch (Throwable var1) {
         var0 = null;
      }

      return var0;
   }

   public static Locale getLocaleFromField(Configuration var0) {
      Locale var2;
      try {
         var2 = (Locale)readField("android.content.res.Configuration", "locale", var0);
      } catch (Throwable var1) {
         var2 = null;
      }

      return var2;
   }

   public static Locale getLocaleFromLocaleList(Configuration param0) {
      // $FF: Couldn't be decompiled
   }

   public static String getMacAddress(Context var0) {
      try {
         String var2 = (String)invokeStaticMethod("com.adjust.sdk.plugin.MacAddressUtil", "getMacAddress", new Class[]{Context.class}, var0);
         return var2;
      } catch (Throwable var1) {
         return null;
      }
   }

   public static String getPlayAdId(Context var0) {
      try {
         String var2 = (String)invokeInstanceMethod(getAdvertisingInfoObject(var0), "getId", (Class[])null);
         return var2;
      } catch (Throwable var1) {
         return null;
      }
   }

   public static Map getPluginKeys(Context var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = getPlugins().iterator();

      while(var2.hasNext()) {
         Entry var3 = ((Plugin)var2.next()).getParameter(var0);
         if (var3 != null) {
            var1.put(var3.getKey(), var3.getValue());
         }
      }

      if (var1.size() == 0) {
         return null;
      } else {
         return var1;
      }
   }

   private static List getPlugins() {
      ArrayList var0 = new ArrayList(Constants.PLUGINS.size());
      Iterator var1 = Constants.PLUGINS.iterator();

      while(var1.hasNext()) {
         Object var2 = createDefaultInstance((String)var1.next());
         if (var2 != null && var2 instanceof Plugin) {
            var0.add((Plugin)var2);
         }
      }

      return var0;
   }

   public static String[] getSupportedAbis() {
      String[] var0;
      try {
         var0 = (String[])readField("android.os.Build", "SUPPORTED_ABIS");
      } catch (Throwable var1) {
         var0 = null;
      }

      return var0;
   }

   public static Object getVMRuntimeObject() {
      try {
         Object var0 = invokeStaticMethod("dalvik.system.VMRuntime", "getRuntime", (Class[])null);
         return var0;
      } catch (Throwable var1) {
         return null;
      }
   }

   public static String getVmInstructionSet() {
      try {
         String var0 = (String)invokeInstanceMethod(getVMRuntimeObject(), "vmInstructionSet", (Class[])null);
         return var0;
      } catch (Throwable var1) {
         return null;
      }
   }

   public static Object invokeInstanceMethod(Object var0, String var1, Class[] var2, Object... var3) throws Exception {
      return invokeMethod(var0.getClass(), var1, var0, var2, var3);
   }

   public static Object invokeMethod(Class var0, String var1, Object var2, Class[] var3, Object... var4) throws Exception {
      Method var5 = var0.getMethod(var1, var3);
      return var5 == null ? null : var5.invoke(var2, var4);
   }

   public static Object invokeStaticMethod(String var0, String var1, Class[] var2, Object... var3) throws Exception {
      return invokeMethod(Class.forName(var0), var1, (Object)null, var2, var3);
   }

   public static Boolean isPlayTrackingEnabled(Context param0) {
      // $FF: Couldn't be decompiled
   }

   public static Object readField(String var0, String var1) throws Exception {
      return readField(var0, var1, (Object)null);
   }

   public static Object readField(String var0, String var1, Object var2) throws Exception {
      Class var3 = forName(var0);
      if (var3 == null) {
         return null;
      } else {
         Field var4 = var3.getField(var1);
         return var4 == null ? null : var4.get(var2);
      }
   }
}
