package android.support.p001v4.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.Typeface.Builder;
import android.graphics.fonts.FontVariationAxis;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.support.p001v4.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import android.support.p001v4.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import android.support.p001v4.provider.FontsContractCompat;
import android.support.p001v4.provider.FontsContractCompat.FontInfo;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

/* renamed from: android.support.v4.graphics.TypefaceCompatApi26Impl */
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class mFontFamily;
    protected final Constructor mFontFamilyCtor;
    protected final Method mFreeze;

    public TypefaceCompatApi26Impl() {
        Constructor obtainFontFamilyCtor;
        Method obtainAddFontFromAssetManagerMethod;
        Method obtainAddFontFromBufferMethod;
        Method obtainFreezeMethod;
        Method obtainAbortCreationMethod;
        Method obtainCreateFromFamiliesWithDefaultMethod;
        Class cls = null;
        try {
            Class obtainFontFamily = obtainFontFamily();
            obtainFontFamilyCtor = obtainFontFamilyCtor(obtainFontFamily);
            obtainAddFontFromAssetManagerMethod = obtainAddFontFromAssetManagerMethod(obtainFontFamily);
            obtainAddFontFromBufferMethod = obtainAddFontFromBufferMethod(obtainFontFamily);
            obtainFreezeMethod = obtainFreezeMethod(obtainFontFamily);
            obtainAbortCreationMethod = obtainAbortCreationMethod(obtainFontFamily);
            obtainCreateFromFamiliesWithDefaultMethod = obtainCreateFromFamiliesWithDefaultMethod(obtainFontFamily);
            cls = obtainFontFamily;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to collect necessary methods for class ");
            stringBuilder.append(e.getClass().getName());
            Log.e("TypefaceCompatApi26Impl", stringBuilder.toString(), e);
            obtainFontFamilyCtor = null;
            obtainAddFontFromAssetManagerMethod = obtainFontFamilyCtor;
            obtainAddFontFromBufferMethod = obtainAddFontFromAssetManagerMethod;
            obtainFreezeMethod = obtainAddFontFromBufferMethod;
            obtainAbortCreationMethod = obtainFreezeMethod;
            obtainCreateFromFamiliesWithDefaultMethod = obtainAbortCreationMethod;
        }
        this.mFontFamily = cls;
        this.mFontFamilyCtor = obtainFontFamilyCtor;
        this.mAddFontFromAssetManager = obtainAddFontFromAssetManagerMethod;
        this.mAddFontFromBuffer = obtainAddFontFromBufferMethod;
        this.mFreeze = obtainFreezeMethod;
        this.mAbortCreation = obtainAbortCreationMethod;
        this.mCreateFromFamiliesWithDefault = obtainCreateFromFamiliesWithDefaultMethod;
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
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addFontFromAssetManager(Context context, Object obj, String str, int i, int i2, int i3, FontVariationAxis[] fontVariationAxisArr) {
        try {
            return ((Boolean) this.mAddFontFromAssetManager.invoke(obj, new Object[]{context.getAssets(), str, Integer.valueOf(0), Boolean.valueOf(false), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), fontVariationAxisArr})).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addFontFromBuffer(Object obj, ByteBuffer byteBuffer, int i, int i2, int i3) {
        try {
            return ((Boolean) this.mAddFontFromBuffer.invoke(obj, new Object[]{byteBuffer, Integer.valueOf(i), null, Integer.valueOf(i2), Integer.valueOf(i3)})).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /* Access modifiers changed, original: protected */
    public Typeface createFromFamiliesWithDefault(Object obj) {
        try {
            Array.set(Array.newInstance(this.mFontFamily, 1), 0, obj);
            return (Typeface) this.mCreateFromFamiliesWithDefault.invoke(null, new Object[]{r0, Integer.valueOf(-1), Integer.valueOf(-1)});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean freeze(Object obj) {
        try {
            return ((Boolean) this.mFreeze.invoke(obj, new Object[0])).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void abortCreation(Object obj) {
        try {
            this.mAbortCreation.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        Object newFamily = newFamily();
        FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        i = entries.length;
        int i2 = 0;
        while (i2 < i) {
            FontFileResourceEntry fontFileResourceEntry = entries[i2];
            if (addFontFromAssetManager(context, newFamily, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic(), FontVariationAxis.fromFontVariationSettings(fontFileResourceEntry.getVariationSettings()))) {
                i2++;
            } else {
                abortCreation(newFamily);
                return null;
            }
        }
        if (freeze(newFamily)) {
            return createFromFamiliesWithDefault(newFamily);
        }
        return null;
    }

    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontInfo[] fontInfoArr, int i) {
        ParcelFileDescriptor openFileDescriptor;
        Throwable th;
        Throwable th2;
        if (fontInfoArr.length < 1) {
            return null;
        }
        if (isFontFamilyPrivateAPIAvailable()) {
            Map prepareFontData = FontsContractCompat.prepareFontData(context, fontInfoArr, cancellationSignal);
            Object newFamily = newFamily();
            Object obj = null;
            for (FontInfo fontInfo : fontInfoArr) {
                ByteBuffer byteBuffer = (ByteBuffer) prepareFontData.get(fontInfo.getUri());
                if (byteBuffer != null) {
                    if (addFontFromBuffer(newFamily, byteBuffer, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic())) {
                        obj = 1;
                    } else {
                        abortCreation(newFamily);
                        return null;
                    }
                }
            }
            if (obj == null) {
                abortCreation(newFamily);
                return null;
            } else if (freeze(newFamily)) {
                return Typeface.create(createFromFamiliesWithDefault(newFamily), i);
            } else {
                return null;
            }
        }
        FontInfo findBestInfo = findBestInfo(fontInfoArr, i);
        try {
            openFileDescriptor = context.getContentResolver().openFileDescriptor(findBestInfo.getUri(), "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return null;
            }
            try {
                Typeface build = new Builder(openFileDescriptor.getFileDescriptor()).setWeight(findBestInfo.getWeight()).setItalic(findBestInfo.isItalic()).build();
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return build;
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
        } catch (IOException unused) {
            return null;
        }
        if (openFileDescriptor != null) {
            if (th22 != null) {
                try {
                    openFileDescriptor.close();
                } catch (Throwable th4) {
                    th22.addSuppressed(th4);
                }
            } else {
                openFileDescriptor.close();
            }
        }
        throw th;
        throw th;
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, i, str, i2);
        }
        Object newFamily = newFamily();
        if (!addFontFromAssetManager(context, newFamily, str, 0, -1, -1, null)) {
            abortCreation(newFamily);
            return null;
        } else if (freeze(newFamily)) {
            return createFromFamiliesWithDefault(newFamily);
        } else {
            return null;
        }
    }

    /* Access modifiers changed, original: protected */
    public Class obtainFontFamily() throws ClassNotFoundException {
        return Class.forName("android.graphics.FontFamily");
    }

    /* Access modifiers changed, original: protected */
    public Constructor obtainFontFamilyCtor(Class cls) throws NoSuchMethodException {
        return cls.getConstructor(new Class[0]);
    }

    /* Access modifiers changed, original: protected */
    public Method obtainAddFontFromAssetManagerMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod("addFontFromAssetManager", new Class[]{AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class});
    }

    /* Access modifiers changed, original: protected */
    public Method obtainAddFontFromBufferMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod("addFontFromBuffer", new Class[]{ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE});
    }

    /* Access modifiers changed, original: protected */
    public Method obtainFreezeMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod("freeze", new Class[0]);
    }

    /* Access modifiers changed, original: protected */
    public Method obtainAbortCreationMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod("abortCreation", new Class[0]);
    }

    /* Access modifiers changed, original: protected */
    public Method obtainCreateFromFamiliesWithDefaultMethod(Class cls) throws NoSuchMethodException {
        Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", new Class[]{Array.newInstance(cls, 1).getClass(), Integer.TYPE, Integer.TYPE});
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
