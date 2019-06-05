// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.adjust.sdk.plugin.Plugin;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import android.content.res.Configuration;
import android.content.Context;

public class Reflection
{
    public static Object createDefaultInstance(final Class clazz) {
        try {
            return clazz.newInstance();
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static Object createDefaultInstance(final String s) {
        return createDefaultInstance(forName(s));
    }
    
    public static Object createInstance(final String className, final Class[] parameterTypes, final Object... initargs) {
        try {
            return Class.forName(className).getConstructor((Class<?>[])parameterTypes).newInstance(initargs);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static Class forName(final String className) {
        try {
            return Class.forName(className);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    private static Object getAdvertisingInfoObject(final Context context) throws Exception {
        return invokeStaticMethod("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[] { Context.class }, context);
    }
    
    public static String getAndroidId(final Context context) {
        try {
            return (String)invokeStaticMethod("com.adjust.sdk.plugin.AndroidIdUtil", "getAndroidId", new Class[] { Context.class }, context);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static String getCpuAbi() {
        String s;
        try {
            s = (String)readField("android.os.Build", "CPU_ABI");
        }
        catch (Throwable t) {
            s = null;
        }
        return s;
    }
    
    public static Locale getLocaleFromField(final Configuration configuration) {
        Locale locale;
        try {
            locale = (Locale)readField("android.content.res.Configuration", "locale", configuration);
        }
        catch (Throwable t) {
            locale = null;
        }
        return locale;
    }
    
    public static Locale getLocaleFromLocaleList(final Configuration configuration) {
        Locale locale;
        try {
            final Object invokeInstanceMethod = invokeInstanceMethod(configuration, "getLocales", null, new Object[0]);
            if (invokeInstanceMethod == null) {
                return null;
            }
            locale = (Locale)invokeInstanceMethod(invokeInstanceMethod, "get", new Class[] { Integer.TYPE }, 0);
        }
        catch (Throwable t) {
            locale = null;
        }
        return locale;
    }
    
    public static String getMacAddress(final Context context) {
        try {
            return (String)invokeStaticMethod("com.adjust.sdk.plugin.MacAddressUtil", "getMacAddress", new Class[] { Context.class }, context);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static String getPlayAdId(final Context context) {
        try {
            return (String)invokeInstanceMethod(getAdvertisingInfoObject(context), "getId", null, new Object[0]);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static Map<String, String> getPluginKeys(final Context context) {
        final HashMap<String, Object> hashMap = (HashMap<String, Object>)new HashMap<String, String>();
        final Iterator<Plugin> iterator = getPlugins().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, String> parameter = iterator.next().getParameter(context);
            if (parameter != null) {
                hashMap.put(parameter.getKey(), parameter.getValue());
            }
        }
        if (hashMap.size() == 0) {
            return null;
        }
        return (Map<String, String>)hashMap;
    }
    
    private static List<Plugin> getPlugins() {
        final ArrayList<Plugin> list = new ArrayList<Plugin>(Constants.PLUGINS.size());
        final Iterator<String> iterator = Constants.PLUGINS.iterator();
        while (iterator.hasNext()) {
            final Object defaultInstance = createDefaultInstance(iterator.next());
            if (defaultInstance != null && defaultInstance instanceof Plugin) {
                list.add((Plugin)defaultInstance);
            }
        }
        return list;
    }
    
    public static String[] getSupportedAbis() {
        String[] array;
        try {
            array = (String[])readField("android.os.Build", "SUPPORTED_ABIS");
        }
        catch (Throwable t) {
            array = null;
        }
        return array;
    }
    
    public static Object getVMRuntimeObject() {
        try {
            return invokeStaticMethod("dalvik.system.VMRuntime", "getRuntime", null, new Object[0]);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static String getVmInstructionSet() {
        try {
            return (String)invokeInstanceMethod(getVMRuntimeObject(), "vmInstructionSet", null, new Object[0]);
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static Object invokeInstanceMethod(final Object o, final String s, final Class[] array, final Object... array2) throws Exception {
        return invokeMethod(o.getClass(), s, o, array, array2);
    }
    
    public static Object invokeMethod(final Class clazz, final String name, final Object obj, final Class[] parameterTypes, final Object... args) throws Exception {
        final Method method = clazz.getMethod(name, (Class[])parameterTypes);
        if (method == null) {
            return null;
        }
        return method.invoke(obj, args);
    }
    
    public static Object invokeStaticMethod(final String className, final String s, final Class[] array, final Object... array2) throws Exception {
        return invokeMethod(Class.forName(className), s, null, array, array2);
    }
    
    public static Boolean isPlayTrackingEnabled(final Context context) {
        try {
            final Boolean b = (Boolean)invokeInstanceMethod(getAdvertisingInfoObject(context), "isLimitAdTrackingEnabled", null, new Object[0]);
            Boolean value;
            if (b == null) {
                value = null;
            }
            else {
                value = (b ^ true);
            }
            return value;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static Object readField(final String s, final String s2) throws Exception {
        return readField(s, s2, null);
    }
    
    public static Object readField(final String s, final String name, final Object obj) throws Exception {
        final Class forName = forName(s);
        if (forName == null) {
            return null;
        }
        final Field field = forName.getField(name);
        if (field == null) {
            return null;
        }
        return field.get(obj);
    }
}
