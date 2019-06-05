// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.util.Map;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class Lifecycling
{
    private static Map<Class, Constructor<? extends GenericLifecycleObserver>> sCallbackCache;
    private static Constructor<? extends GenericLifecycleObserver> sREFLECTIVE;
    
    static {
        while (true) {
            try {
                Lifecycling.sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(Object.class);
                Lifecycling.sCallbackCache = new HashMap<Class, Constructor<? extends GenericLifecycleObserver>>();
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            break;
        }
    }
    
    static String getAdapterName(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s.replace(".", "_"));
        sb.append("_LifecycleAdapter");
        return sb.toString();
    }
    
    @NonNull
    static GenericLifecycleObserver getCallback(final Object o) {
        if (o instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver)o;
        }
        try {
            final Class<?> class1 = o.getClass();
            final Constructor<? extends GenericLifecycleObserver> constructor = Lifecycling.sCallbackCache.get(class1);
            if (constructor != null) {
                return (GenericLifecycleObserver)constructor.newInstance(o);
            }
            final Constructor<? extends GenericLifecycleObserver> generatedAdapterConstructor = getGeneratedAdapterConstructor(class1);
            Constructor<? extends GenericLifecycleObserver> sreflective;
            if (generatedAdapterConstructor != null) {
                sreflective = generatedAdapterConstructor;
                if (!generatedAdapterConstructor.isAccessible()) {
                    generatedAdapterConstructor.setAccessible(true);
                    sreflective = generatedAdapterConstructor;
                }
            }
            else {
                sreflective = Lifecycling.sREFLECTIVE;
            }
            Lifecycling.sCallbackCache.put(class1, sreflective);
            return (GenericLifecycleObserver)sreflective.newInstance(o);
        }
        catch (InvocationTargetException cause) {
            throw new RuntimeException(cause);
        }
        catch (InstantiationException cause2) {
            throw new RuntimeException(cause2);
        }
        catch (IllegalAccessException cause3) {
            throw new RuntimeException(cause3);
        }
    }
    
    @Nullable
    private static Constructor<? extends GenericLifecycleObserver> getGeneratedAdapterConstructor(final Class<?> clazz) {
        final Package package1 = clazz.getPackage();
        String name;
        if (package1 != null) {
            name = package1.getName();
        }
        else {
            name = "";
        }
        String s = clazz.getCanonicalName();
        if (s == null) {
            return null;
        }
        if (!name.isEmpty()) {
            s = s.substring(name.length() + 1);
        }
        final String adapterName = getAdapterName(s);
        try {
            String string;
            if (name.isEmpty()) {
                string = adapterName;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(name);
                sb.append(".");
                sb.append(adapterName);
                string = sb.toString();
            }
            return (Constructor<? extends GenericLifecycleObserver>)Class.forName(string).getDeclaredConstructor(clazz);
        }
        catch (NoSuchMethodException cause) {
            throw new RuntimeException(cause);
        }
        catch (ClassNotFoundException ex) {
            final Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getGeneratedAdapterConstructor(superclass);
            }
            return null;
        }
    }
}
