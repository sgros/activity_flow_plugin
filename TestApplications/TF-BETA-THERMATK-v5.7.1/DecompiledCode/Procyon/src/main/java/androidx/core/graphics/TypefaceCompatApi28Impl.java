// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import android.graphics.Typeface;

public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl
{
    @Override
    protected Typeface createFromFamiliesWithDefault(Object cause) {
        try {
            final Object instance = Array.newInstance(super.mFontFamily, 1);
            Array.set(instance, 0, cause);
            cause = (InvocationTargetException)super.mCreateFromFamiliesWithDefault.invoke(null, instance, "sans-serif", -1, -1);
            return (Typeface)cause;
        }
        catch (InvocationTargetException cause) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(cause);
    }
    
    @Override
    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class type) throws NoSuchMethodException {
        final Class<?> class1 = Array.newInstance(type, 1).getClass();
        type = Integer.TYPE;
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", class1, String.class, type, type);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
