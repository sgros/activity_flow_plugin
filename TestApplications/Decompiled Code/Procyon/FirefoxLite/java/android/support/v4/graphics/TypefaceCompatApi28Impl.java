// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import android.graphics.Typeface;

public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl
{
    @Override
    protected Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, instance, "sans-serif", -1, -1);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    @Override
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class componentType) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(componentType, 1).getClass(), String.class, Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
