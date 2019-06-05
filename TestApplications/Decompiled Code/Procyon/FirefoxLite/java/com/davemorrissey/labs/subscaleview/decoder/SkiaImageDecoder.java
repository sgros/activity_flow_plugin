// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Context;

public class SkiaImageDecoder implements ImageDecoder
{
    @Override
    public Bitmap decode(final Context p0, final Uri p1) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //     4: astore_3       
        //     5: new             Landroid/graphics/BitmapFactory$Options;
        //     8: dup            
        //     9: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
        //    12: astore          4
        //    14: aload           4
        //    16: getstatic       android/graphics/Bitmap$Config.RGB_565:Landroid/graphics/Bitmap$Config;
        //    19: putfield        android/graphics/BitmapFactory$Options.inPreferredConfig:Landroid/graphics/Bitmap$Config;
        //    22: aload_3        
        //    23: ldc             "android.resource://"
        //    25: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    28: ifeq            189
        //    31: aload_2        
        //    32: invokevirtual   android/net/Uri.getAuthority:()Ljava/lang/String;
        //    35: astore_3       
        //    36: aload_1        
        //    37: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //    40: aload_3        
        //    41: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    44: ifeq            56
        //    47: aload_1        
        //    48: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //    51: astore          5
        //    53: goto            66
        //    56: aload_1        
        //    57: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    60: aload_3        
        //    61: invokevirtual   android/content/pm/PackageManager.getResourcesForApplication:(Ljava/lang/String;)Landroid/content/res/Resources;
        //    64: astore          5
        //    66: aload_2        
        //    67: invokevirtual   android/net/Uri.getPathSegments:()Ljava/util/List;
        //    70: astore_2       
        //    71: aload_2        
        //    72: invokeinterface java/util/List.size:()I
        //    77: istore          6
        //    79: iconst_0       
        //    80: istore          7
        //    82: iload           6
        //    84: iconst_2       
        //    85: if_icmpne       129
        //    88: aload_2        
        //    89: iconst_0       
        //    90: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    95: checkcast       Ljava/lang/String;
        //    98: ldc             "drawable"
        //   100: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   103: ifeq            129
        //   106: aload           5
        //   108: aload_2        
        //   109: iconst_1       
        //   110: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   115: checkcast       Ljava/lang/String;
        //   118: ldc             "drawable"
        //   120: aload_3        
        //   121: invokevirtual   android/content/res/Resources.getIdentifier:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
        //   124: istore          8
        //   126: goto            174
        //   129: iload           7
        //   131: istore          8
        //   133: iload           6
        //   135: iconst_1       
        //   136: if_icmpne       174
        //   139: iload           7
        //   141: istore          8
        //   143: aload_2        
        //   144: iconst_0       
        //   145: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   150: checkcast       Ljava/lang/CharSequence;
        //   153: invokestatic    android/text/TextUtils.isDigitsOnly:(Ljava/lang/CharSequence;)Z
        //   156: ifeq            174
        //   159: aload_2        
        //   160: iconst_0       
        //   161: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   166: checkcast       Ljava/lang/String;
        //   169: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   172: istore          8
        //   174: aload_1        
        //   175: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //   178: iload           8
        //   180: aload           4
        //   182: invokestatic    android/graphics/BitmapFactory.decodeResource:(Landroid/content/res/Resources;ILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   185: astore_1       
        //   186: goto            285
        //   189: aload_3        
        //   190: ldc             "file:///android_asset/"
        //   192: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   195: istore          9
        //   197: aconst_null    
        //   198: astore          5
        //   200: iload           9
        //   202: ifeq            233
        //   205: aload_3        
        //   206: ldc             "file:///android_asset/"
        //   208: invokevirtual   java/lang/String.length:()I
        //   211: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   214: astore_2       
        //   215: aload_1        
        //   216: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //   219: aload_2        
        //   220: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //   223: aconst_null    
        //   224: aload           4
        //   226: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   229: astore_1       
        //   230: goto            285
        //   233: aload_3        
        //   234: ldc             "file://"
        //   236: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   239: ifeq            260
        //   242: aload_3        
        //   243: ldc             "file://"
        //   245: invokevirtual   java/lang/String.length:()I
        //   248: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   251: aload           4
        //   253: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   256: astore_1       
        //   257: goto            285
        //   260: aload_1        
        //   261: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   264: aload_2        
        //   265: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //   268: astore_2       
        //   269: aload_2        
        //   270: aconst_null    
        //   271: aload           4
        //   273: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   276: astore_1       
        //   277: aload_2        
        //   278: ifnull          285
        //   281: aload_2        
        //   282: invokevirtual   java/io/InputStream.close:()V
        //   285: aload_1        
        //   286: ifnull          291
        //   289: aload_1        
        //   290: areturn        
        //   291: new             Ljava/lang/RuntimeException;
        //   294: dup            
        //   295: ldc             "Skia image region decoder returned null bitmap - image format may not be supported"
        //   297: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   300: athrow         
        //   301: astore_1       
        //   302: goto            309
        //   305: astore_1       
        //   306: aload           5
        //   308: astore_2       
        //   309: aload_2        
        //   310: ifnull          317
        //   313: aload_2        
        //   314: invokevirtual   java/io/InputStream.close:()V
        //   317: aload_1        
        //   318: athrow         
        //   319: astore_2       
        //   320: iload           7
        //   322: istore          8
        //   324: goto            174
        //   327: astore_2       
        //   328: goto            285
        //   331: astore_2       
        //   332: goto            317
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  159    174    319    327    Ljava/lang/NumberFormatException;
        //  260    269    305    309    Any
        //  269    277    301    305    Any
        //  281    285    327    331    Ljava/lang/Exception;
        //  313    317    331    335    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 161 out-of-bounds for length 161
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
