// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics;

import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import androidx.core.content.res.FontResourcesParserCompat;
import android.content.Context;
import java.lang.reflect.GenericDeclaration;
import android.util.Log;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.Os;
import java.io.File;
import android.os.ParcelFileDescriptor;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl
{
    private static Method sAddFontWeightStyle;
    private static Method sCreateFromFamiliesWithDefault;
    private static Class sFontFamily;
    private static Constructor sFontFamilyCtor;
    private static boolean sHasInitBeenCalled = false;
    
    private static boolean addFontWeightStyle(final Object ex, final String s, final int i, final boolean b) {
        init();
        try {
            return (boolean)TypefaceCompatApi21Impl.sAddFontWeightStyle.invoke(ex, s, i, b);
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        throw new RuntimeException(ex);
    }
    
    private static Typeface createFromFamiliesWithDefault(Object cause) {
        init();
        try {
            final Object instance = Array.newInstance(TypefaceCompatApi21Impl.sFontFamily, 1);
            Array.set(instance, 0, cause);
            cause = (InvocationTargetException)TypefaceCompatApi21Impl.sCreateFromFamiliesWithDefault.invoke(null, instance);
            return (Typeface)cause;
        }
        catch (InvocationTargetException cause) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(cause);
    }
    
    private File getFile(final ParcelFileDescriptor parcelFileDescriptor) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            final String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        }
        catch (ErrnoException ex) {
            return null;
        }
    }
    
    private static void init() {
        if (TypefaceCompatApi21Impl.sHasInitBeenCalled) {
            return;
        }
        TypefaceCompatApi21Impl.sHasInitBeenCalled = true;
        final Constructor<?> constructor = null;
        GenericDeclaration forName = null;
        Constructor<?> constructor2 = null;
        GenericDeclaration method = null;
        GenericDeclaration method2 = null;
        Label_0114: {
            try {
                forName = Class.forName("android.graphics.FontFamily");
                constructor2 = ((Class<?>)forName).getConstructor((Class<?>[])new Class[0]);
                method = ((Class)forName).getMethod("addFontWeightStyle", String.class, Integer.TYPE, Boolean.TYPE);
                method2 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance((Class<?>)forName, 1).getClass());
                break Label_0114;
            }
            catch (NoSuchMethodException constructor2) {}
            catch (ClassNotFoundException ex) {}
            Log.e("TypefaceCompatApi21Impl", constructor2.getClass().getName(), (Throwable)constructor2);
            method2 = null;
            method = (forName = method2);
            constructor2 = constructor;
        }
        TypefaceCompatApi21Impl.sFontFamilyCtor = constructor2;
        TypefaceCompatApi21Impl.sFontFamily = (Class)forName;
        TypefaceCompatApi21Impl.sAddFontWeightStyle = (Method)method;
        TypefaceCompatApi21Impl.sCreateFromFamiliesWithDefault = (Method)method2;
    }
    
    private static Object newFamily() {
        init();
        InvocationTargetException instance = null;
        try {
            instance = TypefaceCompatApi21Impl.sFontFamilyCtor.newInstance(new Object[0]);
            return instance;
        }
        catch (InvocationTargetException instance) {}
        catch (InstantiationException instance) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(instance);
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry tempFile, final Resources resources, int i) {
        final Object family = newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = tempFile.getEntries();
        final int length = entries.length;
        i = 0;
        while (i < length) {
            final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = entries[i];
            tempFile = (FontResourcesParserCompat.FontFamilyFilesResourceEntry)TypefaceCompatUtil.getTempFile(context);
            if (tempFile == null) {
                return null;
            }
            try {
                if (!TypefaceCompatUtil.copyToFile((File)tempFile, resources, fontFileResourceEntry.getResourceId())) {
                    return null;
                }
                if (!addFontWeightStyle(family, ((File)tempFile).getPath(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) {
                    return null;
                }
                ((File)tempFile).delete();
                ++i;
                continue;
            }
            catch (RuntimeException ex) {
                return null;
            }
            finally {
                ((File)tempFile).delete();
            }
            break;
        }
        return createFromFamiliesWithDefault(family);
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
        //     9: aload_3        
        //    10: iload           4
        //    12: invokevirtual   androidx/core/graphics/TypefaceCompatBaseImpl.findBestInfo:([Landroidx/core/provider/FontsContractCompat$FontInfo;I)Landroidx/core/provider/FontsContractCompat$FontInfo;
        //    15: astore_3       
        //    16: aload_1        
        //    17: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    20: astore          5
        //    22: aload           5
        //    24: aload_3        
        //    25: invokevirtual   androidx/core/provider/FontsContractCompat$FontInfo.getUri:()Landroid/net/Uri;
        //    28: ldc             "r"
        //    30: aload_2        
        //    31: invokevirtual   android/content/ContentResolver.openFileDescriptor:(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
        //    34: astore          5
        //    36: aload           5
        //    38: ifnonnull       53
        //    41: aload           5
        //    43: ifnull          51
        //    46: aload           5
        //    48: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //    51: aconst_null    
        //    52: areturn        
        //    53: aload_0        
        //    54: aload           5
        //    56: invokespecial   androidx/core/graphics/TypefaceCompatApi21Impl.getFile:(Landroid/os/ParcelFileDescriptor;)Ljava/io/File;
        //    59: astore_2       
        //    60: aload_2        
        //    61: ifnull          91
        //    64: aload_2        
        //    65: invokevirtual   java/io/File.canRead:()Z
        //    68: ifne            74
        //    71: goto            91
        //    74: aload_2        
        //    75: invokestatic    android/graphics/Typeface.createFromFile:(Ljava/io/File;)Landroid/graphics/Typeface;
        //    78: astore_1       
        //    79: aload           5
        //    81: ifnull          89
        //    84: aload           5
        //    86: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //    89: aload_1        
        //    90: areturn        
        //    91: new             Ljava/io/FileInputStream;
        //    94: astore          6
        //    96: aload           6
        //    98: aload           5
        //   100: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
        //   103: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/FileDescriptor;)V
        //   106: aload_0        
        //   107: aload_1        
        //   108: aload           6
        //   110: invokespecial   androidx/core/graphics/TypefaceCompatBaseImpl.createFromInputStream:(Landroid/content/Context;Ljava/io/InputStream;)Landroid/graphics/Typeface;
        //   113: astore_1       
        //   114: aload           6
        //   116: invokevirtual   java/io/FileInputStream.close:()V
        //   119: aload           5
        //   121: ifnull          129
        //   124: aload           5
        //   126: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   129: aload_1        
        //   130: areturn        
        //   131: astore_1       
        //   132: aconst_null    
        //   133: astore_2       
        //   134: goto            145
        //   137: astore_1       
        //   138: aload_1        
        //   139: athrow         
        //   140: astore_3       
        //   141: aload_1        
        //   142: astore_2       
        //   143: aload_3        
        //   144: astore_1       
        //   145: aload_2        
        //   146: ifnull          157
        //   149: aload           6
        //   151: invokevirtual   java/io/FileInputStream.close:()V
        //   154: goto            162
        //   157: aload           6
        //   159: invokevirtual   java/io/FileInputStream.close:()V
        //   162: aload_1        
        //   163: athrow         
        //   164: astore_1       
        //   165: aconst_null    
        //   166: astore_2       
        //   167: goto            174
        //   170: astore_2       
        //   171: aload_2        
        //   172: athrow         
        //   173: astore_1       
        //   174: aload           5
        //   176: ifnull          196
        //   179: aload_2        
        //   180: ifnull          191
        //   183: aload           5
        //   185: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   188: goto            196
        //   191: aload           5
        //   193: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   196: aload_1        
        //   197: athrow         
        //   198: astore_1       
        //   199: aconst_null    
        //   200: areturn        
        //   201: astore_2       
        //   202: goto            162
        //   205: astore_2       
        //   206: goto            196
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  22     36     198    201    Ljava/io/IOException;
        //  46     51     198    201    Ljava/io/IOException;
        //  53     60     170    174    Ljava/lang/Throwable;
        //  53     60     164    170    Any
        //  64     71     170    174    Ljava/lang/Throwable;
        //  64     71     164    170    Any
        //  74     79     170    174    Ljava/lang/Throwable;
        //  74     79     164    170    Any
        //  84     89     198    201    Ljava/io/IOException;
        //  91     106    170    174    Ljava/lang/Throwable;
        //  91     106    164    170    Any
        //  106    114    137    145    Ljava/lang/Throwable;
        //  106    114    131    137    Any
        //  114    119    170    174    Ljava/lang/Throwable;
        //  114    119    164    170    Any
        //  124    129    198    201    Ljava/io/IOException;
        //  138    140    140    145    Any
        //  149    154    201    205    Ljava/lang/Throwable;
        //  149    154    164    170    Any
        //  157    162    170    174    Ljava/lang/Throwable;
        //  157    162    164    170    Any
        //  162    164    170    174    Ljava/lang/Throwable;
        //  162    164    164    170    Any
        //  171    173    173    174    Any
        //  183    188    205    209    Ljava/lang/Throwable;
        //  191    196    198    201    Ljava/io/IOException;
        //  196    198    198    201    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 114 out-of-bounds for length 114
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
}
