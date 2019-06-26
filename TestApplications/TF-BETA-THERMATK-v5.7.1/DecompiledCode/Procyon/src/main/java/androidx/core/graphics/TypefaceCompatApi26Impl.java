// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics;

import android.content.res.AssetManager;
import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import androidx.core.content.res.FontResourcesParserCompat;
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
        Class obtainFontFamily = null;
        Constructor obtainFontFamilyCtor = null;
        Method obtainAddFontFromAssetManagerMethod = null;
        Executable obtainAddFontFromBufferMethod = null;
        Method obtainFreezeMethod = null;
        Method obtainAbortCreationMethod = null;
        Method obtainCreateFromFamiliesWithDefaultMethod = null;
        Label_0139: {
            try {
                obtainFontFamily = this.obtainFontFamily();
                obtainFontFamilyCtor = this.obtainFontFamilyCtor(obtainFontFamily);
                obtainAddFontFromAssetManagerMethod = this.obtainAddFontFromAssetManagerMethod(obtainFontFamily);
                obtainAddFontFromBufferMethod = this.obtainAddFontFromBufferMethod(obtainFontFamily);
                obtainFreezeMethod = this.obtainFreezeMethod(obtainFontFamily);
                obtainAbortCreationMethod = this.obtainAbortCreationMethod(obtainFontFamily);
                obtainCreateFromFamiliesWithDefaultMethod = this.obtainCreateFromFamiliesWithDefaultMethod(obtainFontFamily);
                break Label_0139;
            }
            catch (NoSuchMethodException obtainFontFamily) {}
            catch (ClassNotFoundException ex) {}
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to collect necessary methods for class ");
            sb.append(obtainFontFamily.getClass().getName());
            Log.e("TypefaceCompatApi26Impl", sb.toString(), (Throwable)obtainFontFamily);
            final Constructor constructor2;
            final Constructor constructor = constructor2 = null;
            final Constructor constructor4;
            final Constructor constructor3 = constructor4 = constructor2;
            obtainFreezeMethod = (obtainAbortCreationMethod = (Method)constructor4);
            obtainAddFontFromBufferMethod = constructor4;
            obtainAddFontFromAssetManagerMethod = (Method)constructor3;
            obtainFontFamilyCtor = constructor2;
            obtainCreateFromFamiliesWithDefaultMethod = (Method)constructor;
            obtainFontFamily = clazz;
        }
        this.mFontFamily = obtainFontFamily;
        this.mFontFamilyCtor = obtainFontFamilyCtor;
        this.mAddFontFromAssetManager = obtainAddFontFromAssetManagerMethod;
        this.mAddFontFromBuffer = (Method)obtainAddFontFromBufferMethod;
        this.mFreeze = obtainFreezeMethod;
        this.mAbortCreation = obtainAbortCreationMethod;
        this.mCreateFromFamiliesWithDefault = obtainCreateFromFamiliesWithDefaultMethod;
    }
    
    private void abortCreation(final Object obj) {
        try {
            this.mAbortCreation.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {}
    }
    
    private boolean addFontFromAssetManager(final Context context, final Object obj, final String s, final int i, final int j, final int k, final FontVariationAxis[] array) {
        try {
            return (boolean)this.mAddFontFromAssetManager.invoke(obj, context.getAssets(), s, 0, false, i, j, k, array);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
    }
    
    private boolean addFontFromBuffer(final Object obj, final ByteBuffer byteBuffer, final int i, final int j, final int k) {
        try {
            return (boolean)this.mAddFontFromBuffer.invoke(obj, byteBuffer, i, null, j, k);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
    }
    
    private boolean freeze(final Object obj) {
        try {
            return (boolean)this.mFreeze.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            return false;
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
            return null;
        }
    }
    
    protected Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, instance, -1, -1);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            return null;
        }
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        final Object family = this.newFamily();
        if (family == null) {
            return null;
        }
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
    public Typeface createFromFontInfo(final Context p0, final CancellationSignal p1, final FontsContractCompat.FontInfo[] p2, final int p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: arraylength    
        //     2: iconst_1       
        //     3: if_icmpge       8
        //     6: aconst_null    
        //     7: areturn        
        //     8: aload_0        
        //     9: invokespecial   androidx/core/graphics/TypefaceCompatApi26Impl.isFontFamilyPrivateAPIAvailable:()Z
        //    12: ifne            133
        //    15: aload_0        
        //    16: aload_3        
        //    17: iload           4
        //    19: invokevirtual   androidx/core/graphics/TypefaceCompatBaseImpl.findBestInfo:([Landroidx/core/provider/FontsContractCompat$FontInfo;I)Landroidx/core/provider/FontsContractCompat$FontInfo;
        //    22: astore          5
        //    24: aload_1        
        //    25: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    28: astore_1       
        //    29: aload_1        
        //    30: aload           5
        //    32: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.getUri:()Landroid/net/Uri;
        //    35: ldc             "r"
        //    37: aload_2        
        //    38: invokevirtual   android/content/ContentResolver.openFileDescriptor:(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
        //    41: astore_3       
        //    42: aload_3        
        //    43: ifnonnull       56
        //    46: aload_3        
        //    47: ifnull          54
        //    50: aload_3        
        //    51: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //    54: aconst_null    
        //    55: areturn        
        //    56: new             Landroid/graphics/Typeface$Builder;
        //    59: astore_1       
        //    60: aload_1        
        //    61: aload_3        
        //    62: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
        //    65: invokespecial   android/graphics/Typeface$Builder.<init>:(Ljava/io/FileDescriptor;)V
        //    68: aload_1        
        //    69: aload           5
        //    71: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.getWeight:()I
        //    74: invokevirtual   android/graphics/Typeface$Builder.setWeight:(I)Landroid/graphics/Typeface$Builder;
        //    77: aload           5
        //    79: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.isItalic:()Z
        //    82: invokevirtual   android/graphics/Typeface$Builder.setItalic:(Z)Landroid/graphics/Typeface$Builder;
        //    85: invokevirtual   android/graphics/Typeface$Builder.build:()Landroid/graphics/Typeface;
        //    88: astore_1       
        //    89: aload_3        
        //    90: ifnull          97
        //    93: aload_3        
        //    94: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //    97: aload_1        
        //    98: areturn        
        //    99: astore_1       
        //   100: aconst_null    
        //   101: astore_2       
        //   102: goto            109
        //   105: astore_2       
        //   106: aload_2        
        //   107: athrow         
        //   108: astore_1       
        //   109: aload_3        
        //   110: ifnull          128
        //   113: aload_2        
        //   114: ifnull          124
        //   117: aload_3        
        //   118: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   121: goto            128
        //   124: aload_3        
        //   125: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   128: aload_1        
        //   129: athrow         
        //   130: astore_1       
        //   131: aconst_null    
        //   132: areturn        
        //   133: aload_1        
        //   134: aload_3        
        //   135: aload_2        
        //   136: invokestatic    androidx/core/provider/FontsContractCompat.prepareFontData:(Landroid/content/Context;[Landroidx/core/provider/FontsContractCompat$FontInfo;Landroid/os/CancellationSignal;)Ljava/util/Map;
        //   139: astore_1       
        //   140: aload_0        
        //   141: invokespecial   androidx/core/graphics/TypefaceCompatApi26Impl.newFamily:()Ljava/lang/Object;
        //   144: astore          5
        //   146: aload           5
        //   148: ifnonnull       153
        //   151: aconst_null    
        //   152: areturn        
        //   153: aload_3        
        //   154: arraylength    
        //   155: istore          6
        //   157: iconst_0       
        //   158: istore          7
        //   160: iconst_0       
        //   161: istore          8
        //   163: iload           8
        //   165: iload           6
        //   167: if_icmpge       238
        //   170: aload_3        
        //   171: iload           8
        //   173: aaload         
        //   174: astore_2       
        //   175: aload_1        
        //   176: aload_2        
        //   177: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.getUri:()Landroid/net/Uri;
        //   180: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   185: checkcast       Ljava/nio/ByteBuffer;
        //   188: astore          9
        //   190: aload           9
        //   192: ifnonnull       198
        //   195: goto            232
        //   198: aload_0        
        //   199: aload           5
        //   201: aload           9
        //   203: aload_2        
        //   204: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.getTtcIndex:()I
        //   207: aload_2        
        //   208: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.getWeight:()I
        //   211: aload_2        
        //   212: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.isItalic:()Z
        //   215: invokespecial   androidx/core/graphics/TypefaceCompatApi26Impl.addFontFromBuffer:(Ljava/lang/Object;Ljava/nio/ByteBuffer;III)Z
        //   218: ifne            229
        //   221: aload_0        
        //   222: aload           5
        //   224: invokespecial   androidx/core/graphics/TypefaceCompatApi26Impl.abortCreation:(Ljava/lang/Object;)V
        //   227: aconst_null    
        //   228: areturn        
        //   229: iconst_1       
        //   230: istore          7
        //   232: iinc            8, 1
        //   235: goto            163
        //   238: iload           7
        //   240: ifne            251
        //   243: aload_0        
        //   244: aload           5
        //   246: invokespecial   androidx/core/graphics/TypefaceCompatApi26Impl.abortCreation:(Ljava/lang/Object;)V
        //   249: aconst_null    
        //   250: areturn        
        //   251: aload_0        
        //   252: aload           5
        //   254: invokespecial   androidx/core/graphics/TypefaceCompatApi26Impl.freeze:(Ljava/lang/Object;)Z
        //   257: ifne            262
        //   260: aconst_null    
        //   261: areturn        
        //   262: aload_0        
        //   263: aload           5
        //   265: invokevirtual   androidx/core/graphics/TypefaceCompatApi26Impl.createFromFamiliesWithDefault:(Ljava/lang/Object;)Landroid/graphics/Typeface;
        //   268: astore_1       
        //   269: aload_1        
        //   270: ifnonnull       275
        //   273: aconst_null    
        //   274: areturn        
        //   275: aload_1        
        //   276: iload           4
        //   278: invokestatic    android/graphics/Typeface.create:(Landroid/graphics/Typeface;I)Landroid/graphics/Typeface;
        //   281: areturn        
        //   282: astore_2       
        //   283: goto            128
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  29     42     130    133    Ljava/io/IOException;
        //  50     54     130    133    Ljava/io/IOException;
        //  56     89     105    109    Ljava/lang/Throwable;
        //  56     89     99     105    Any
        //  93     97     130    133    Ljava/io/IOException;
        //  106    108    108    109    Any
        //  117    121    282    286    Ljava/lang/Throwable;
        //  124    128    130    133    Ljava/io/IOException;
        //  128    130    130    133    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0124:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Typeface createFromResourcesFontFile(final Context context, final Resources resources, final int n, final String s, final int n2) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, n, s, n2);
        }
        final Object family = this.newFamily();
        if (family == null) {
            return null;
        }
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
        final Class<Integer> type = Integer.TYPE;
        final Class<Boolean> type2 = Boolean.TYPE;
        final Class<Integer> type3 = Integer.TYPE;
        return clazz.getMethod("addFontFromAssetManager", AssetManager.class, String.class, type, type2, type3, type3, type3, FontVariationAxis[].class);
    }
    
    protected Method obtainAddFontFromBufferMethod(final Class clazz) throws NoSuchMethodException {
        final Class<Integer> type = Integer.TYPE;
        return clazz.getMethod("addFontFromBuffer", ByteBuffer.class, type, FontVariationAxis[].class, type, type);
    }
    
    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class class1) throws NoSuchMethodException {
        class1 = Array.newInstance(class1, 1).getClass();
        final Class<Integer> type = Integer.TYPE;
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", class1, type, type);
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
