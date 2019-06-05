// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import java.util.Map;
import android.content.ContentResolver;
import java.io.IOException;
import android.graphics.Typeface$Builder;
import android.support.v4.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import android.support.v4.content.res.FontResourcesParserCompat;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.nio.ByteBuffer;
import android.graphics.fonts.FontVariationAxis;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Executable;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl
{
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class mFontFamily;
    protected final Constructor mFontFamilyCtor;
    protected final Method mFreeze;
    
    public TypefaceCompatApi26Impl() {
        final Class clazz = null;
        Class obtainFontFamily;
        Executable obtainFontFamilyCtor;
        Method obtainAddFontFromAssetManagerMethod;
        Method obtainAddFontFromBufferMethod;
        Method obtainFreezeMethod;
        Method obtainAbortCreationMethod;
        Method obtainCreateFromFamiliesWithDefaultMethod;
        try {
            obtainFontFamily = this.obtainFontFamily();
            obtainFontFamilyCtor = this.obtainFontFamilyCtor(obtainFontFamily);
            obtainAddFontFromAssetManagerMethod = this.obtainAddFontFromAssetManagerMethod(obtainFontFamily);
            obtainAddFontFromBufferMethod = this.obtainAddFontFromBufferMethod(obtainFontFamily);
            obtainFreezeMethod = this.obtainFreezeMethod(obtainFontFamily);
            obtainAbortCreationMethod = this.obtainAbortCreationMethod(obtainFontFamily);
            obtainCreateFromFamiliesWithDefaultMethod = this.obtainCreateFromFamiliesWithDefaultMethod(obtainFontFamily);
        }
        catch (ClassNotFoundException | NoSuchMethodException ex3) {
            final NoSuchMethodException ex2;
            final NoSuchMethodException ex = ex2;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to collect necessary methods for class ");
            sb.append(ex.getClass().getName());
            Log.e("TypefaceCompatApi26Impl", sb.toString(), (Throwable)ex);
            final Method method;
            obtainFontFamilyCtor = (method = null);
            final Method method3;
            final Method method2 = method3 = method;
            obtainAbortCreationMethod = (obtainCreateFromFamiliesWithDefaultMethod = method3);
            obtainFreezeMethod = method3;
            obtainAddFontFromBufferMethod = method2;
            obtainAddFontFromAssetManagerMethod = method;
            obtainFontFamily = clazz;
        }
        this.mFontFamily = obtainFontFamily;
        this.mFontFamilyCtor = (Constructor)obtainFontFamilyCtor;
        this.mAddFontFromAssetManager = obtainAddFontFromAssetManagerMethod;
        this.mAddFontFromBuffer = obtainAddFontFromBufferMethod;
        this.mFreeze = obtainFreezeMethod;
        this.mAbortCreation = obtainAbortCreationMethod;
        this.mCreateFromFamiliesWithDefault = obtainCreateFromFamiliesWithDefaultMethod;
    }
    
    private void abortCreation(final Object obj) {
        try {
            this.mAbortCreation.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean addFontFromAssetManager(final Context context, final Object obj, final String s, final int i, final int j, final int k, final FontVariationAxis[] array) {
        try {
            return (boolean)this.mAddFontFromAssetManager.invoke(obj, context.getAssets(), s, 0, false, i, j, k, array);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean addFontFromBuffer(final Object obj, final ByteBuffer byteBuffer, final int i, final int j, final int k) {
        try {
            return (boolean)this.mAddFontFromBuffer.invoke(obj, byteBuffer, i, null, j, k);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean freeze(final Object obj) {
        try {
            return (boolean)this.mFreeze.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager == null) {
            Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return this.mAddFontFromAssetManager != null;
    }
    
    private Object newFamily() {
        try {
            return this.mFontFamilyCtor.newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    protected Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, instance, -1, -1);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object cause;
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        final Object family = this.newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry;
        for (length = entries.length, i = 0; i < length; ++i) {
            fontFileResourceEntry = entries[i];
            if (!this.addFontFromAssetManager(context, family, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0, FontVariationAxis.fromFontVariationSettings(fontFileResourceEntry.getVariationSettings()))) {
                this.abortCreation(family);
                return null;
            }
        }
        if (!this.freeze(family)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, CancellationSignal t, FontsContractCompat.FontInfo[] openFileDescriptor, final int n) {
        if (openFileDescriptor.length < 1) {
            return null;
        }
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(openFileDescriptor, n);
            final ContentResolver contentResolver = context.getContentResolver();
            try {
                openFileDescriptor = (FontsContractCompat.FontInfo[])(Object)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", (CancellationSignal)t);
                if (openFileDescriptor == null) {
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return null;
                }
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
        final Object family = this.newFamily();
        final int length = openFileDescriptor.length;
        boolean b = false;
        for (final FontsContractCompat.FontInfo fontInfo : openFileDescriptor) {
            final ByteBuffer byteBuffer = prepareFontData.get(fontInfo.getUri());
            if (byteBuffer != null) {
                if (!this.addFontFromBuffer(family, byteBuffer, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic() ? 1 : 0)) {
                    this.abortCreation(family);
                    return null;
                }
                b = true;
            }
        }
        if (!b) {
            this.abortCreation(family);
            return null;
        }
        if (!this.freeze(family)) {
            return null;
        }
        return Typeface.create(this.createFromFamiliesWithDefault(family), n);
    }
    
    @Override
    public Typeface createFromResourcesFontFile(final Context context, final Resources resources, final int n, final String s, final int n2) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, n, s, n2);
        }
        final Object family = this.newFamily();
        if (!this.addFontFromAssetManager(context, family, s, 0, -1, -1, null)) {
            this.abortCreation(family);
            return null;
        }
        if (!this.freeze(family)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(family);
    }
    
    protected Method obtainAbortCreationMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("abortCreation", (Class[])new Class[0]);
    }
    
    protected Method obtainAddFontFromAssetManagerMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
    }
    
    protected Method obtainAddFontFromBufferMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
    }
    
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class componentType) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(componentType, 1).getClass(), Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
    
    protected Class obtainFontFamily() throws ClassNotFoundException {
        return Class.forName("android.graphics.FontFamily");
    }
    
    protected Constructor obtainFontFamilyCtor(final Class clazz) throws NoSuchMethodException {
        return clazz.getConstructor((Class[])new Class[0]);
    }
    
    protected Method obtainFreezeMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("freeze", (Class[])new Class[0]);
    }
}
