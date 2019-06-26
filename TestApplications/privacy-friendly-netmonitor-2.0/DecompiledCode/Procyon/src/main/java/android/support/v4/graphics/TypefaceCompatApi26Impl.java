// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.net.Uri;
import java.util.Map;
import android.content.ContentResolver;
import java.io.IOException;
import android.graphics.Typeface$Builder;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontsContractCompat;
import android.support.annotation.Nullable;
import android.os.CancellationSignal;
import android.content.res.Resources;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.GenericDeclaration;
import android.util.Log;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.nio.ByteBuffer;
import android.graphics.fonts.FontVariationAxis;
import android.content.res.AssetManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl
{
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    private static final Method sAbortCreation;
    private static final Method sAddFontFromAssetManager;
    private static final Method sAddFontFromBuffer;
    private static final Method sCreateFromFamiliesWithDefault;
    private static final Class sFontFamily;
    private static final Constructor sFontFamilyCtor;
    private static final Method sFreeze;
    
    static {
        final Constructor constructor = null;
        GenericDeclaration forName;
        Constructor<?> constructor2;
        Method method;
        Method method2;
        Method method3;
        Method method4;
        Method declaredMethod;
        try {
            forName = Class.forName("android.graphics.FontFamily");
            constructor2 = ((Class<?>)forName).getConstructor((Class<?>[])new Class[0]);
            method = ((Class)forName).getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
            method2 = ((Class)forName).getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
            method3 = ((Class)forName).getMethod("freeze", (Class<?>[])new Class[0]);
            method4 = ((Class)forName).getMethod("abortCreation", (Class<?>[])new Class[0]);
            declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance((Class<?>)forName, 1).getClass(), Integer.TYPE, Integer.TYPE);
            declaredMethod.setAccessible(true);
        }
        catch (ClassNotFoundException | NoSuchMethodException ex3) {
            final NoSuchMethodException ex2;
            final NoSuchMethodException ex = ex2;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to collect necessary methods for class ");
            sb.append(ex.getClass().getName());
            Log.e("TypefaceCompatApi26Impl", sb.toString(), (Throwable)ex);
            forName = (declaredMethod = null);
            final Method method5 = method2 = declaredMethod;
            method3 = (method4 = method2);
            method = method5;
            constructor2 = (Constructor<?>)constructor;
        }
        sFontFamilyCtor = constructor2;
        sFontFamily = (Class)forName;
        sAddFontFromAssetManager = method;
        sAddFontFromBuffer = method2;
        sFreeze = method3;
        sAbortCreation = method4;
        sCreateFromFamiliesWithDefault = declaredMethod;
    }
    
    private static boolean abortCreation(final Object obj) {
        try {
            return (boolean)TypefaceCompatApi26Impl.sAbortCreation.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private static boolean addFontFromAssetManager(final Context context, final Object obj, final String s, final int i, final int j, final int k) {
        try {
            return (boolean)TypefaceCompatApi26Impl.sAddFontFromAssetManager.invoke(obj, context.getAssets(), s, 0, false, i, j, k, null);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private static boolean addFontFromBuffer(final Object obj, final ByteBuffer byteBuffer, final int i, final int j, final int k) {
        try {
            return (boolean)TypefaceCompatApi26Impl.sAddFontFromBuffer.invoke(obj, byteBuffer, i, null, j, k);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private static Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(TypefaceCompatApi26Impl.sFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)TypefaceCompatApi26Impl.sCreateFromFamiliesWithDefault.invoke(null, instance, -1, -1);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private static boolean freeze(final Object obj) {
        try {
            return (boolean)TypefaceCompatApi26Impl.sFreeze.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private static boolean isFontFamilyPrivateAPIAvailable() {
        if (TypefaceCompatApi26Impl.sAddFontFromAssetManager == null) {
            Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
        }
        return TypefaceCompatApi26Impl.sAddFontFromAssetManager != null;
    }
    
    private static Object newFamily() {
        try {
            return TypefaceCompatApi26Impl.sFontFamilyCtor.newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        final Object family = newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry;
        for (length = entries.length, i = 0; i < length; ++i) {
            fontFileResourceEntry = entries[i];
            if (!addFontFromAssetManager(context, family, fontFileResourceEntry.getFileName(), 0, fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0)) {
                abortCreation(family);
                return null;
            }
        }
        if (!freeze(family)) {
            return null;
        }
        return createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, @Nullable CancellationSignal t, @NonNull FontsContractCompat.FontInfo[] openFileDescriptor, int i) {
        if (openFileDescriptor.length < 1) {
            return null;
        }
        if (!isFontFamilyPrivateAPIAvailable()) {
            final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(openFileDescriptor, i);
            final ContentResolver contentResolver = context.getContentResolver();
            try {
                openFileDescriptor = (FontsContractCompat.FontInfo[])(Object)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", (CancellationSignal)t);
                try {
                    final Typeface build = new Typeface$Builder(((ParcelFileDescriptor)(Object)openFileDescriptor).getFileDescriptor()).setWeight(bestInfo.getWeight()).setItalic(bestInfo.isItalic()).build();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return build;
                }
                catch (Throwable t) {
                    try {
                        throw t;
                    }
                    finally {}
                }
                finally {
                    t = null;
                }
                if (openFileDescriptor != null) {
                    if (t != null) {
                        try {
                            ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                        }
                        catch (Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    else {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                }
                throw context;
            }
            catch (IOException ex) {
                return null;
            }
        }
        final Map<Uri, ByteBuffer> prepareFontData = FontsContractCompat.prepareFontData(context, openFileDescriptor, (CancellationSignal)t);
        final Object family = newFamily();
        i = 0;
        final int length = openFileDescriptor.length;
        boolean b = false;
        while (i < length) {
            final FontsContractCompat.FontInfo fontInfo = openFileDescriptor[i];
            final ByteBuffer byteBuffer = prepareFontData.get(fontInfo.getUri());
            if (byteBuffer != null) {
                if (!addFontFromBuffer(family, byteBuffer, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic() ? 1 : 0)) {
                    abortCreation(family);
                    return null;
                }
                b = true;
            }
            ++i;
        }
        if (!b) {
            abortCreation(family);
            return null;
        }
        if (!freeze(family)) {
            return null;
        }
        return createFromFamiliesWithDefault(family);
    }
    
    @Nullable
    @Override
    public Typeface createFromResourcesFontFile(final Context context, final Resources resources, final int n, final String s, final int n2) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, n, s, n2);
        }
        final Object family = newFamily();
        if (!addFontFromAssetManager(context, family, s, 0, -1, -1)) {
            abortCreation(family);
            return null;
        }
        if (!freeze(family)) {
            return null;
        }
        return createFromFamiliesWithDefault(family);
    }
}
