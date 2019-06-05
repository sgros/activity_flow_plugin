// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Point;
import android.net.Uri;
import android.content.Context;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory$Options;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.BitmapRegionDecoder;

public class SkiaImageRegionDecoder implements ImageRegionDecoder
{
    private BitmapRegionDecoder decoder;
    private final Object decoderLock;
    
    public SkiaImageRegionDecoder() {
        this.decoderLock = new Object();
    }
    
    @Override
    public Bitmap decodeRegion(final Rect rect, final int inSampleSize) {
        synchronized (this.decoderLock) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inSampleSize = inSampleSize;
            bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
            final Bitmap decodeRegion = this.decoder.decodeRegion(rect, bitmapFactory$Options);
            if (decodeRegion != null) {
                return decodeRegion;
            }
            throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
        }
    }
    
    @Override
    public Point init(final Context p0, final Uri p1) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
        //     4: astore_3       
        //     5: aload_3        
        //     6: ldc             "android.resource://"
        //     8: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    11: ifeq            173
        //    14: aload_2        
        //    15: invokevirtual   android/net/Uri.getAuthority:()Ljava/lang/String;
        //    18: astore          4
        //    20: aload_1        
        //    21: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //    24: aload           4
        //    26: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    29: ifeq            40
        //    32: aload_1        
        //    33: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //    36: astore_3       
        //    37: goto            50
        //    40: aload_1        
        //    41: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    44: aload           4
        //    46: invokevirtual   android/content/pm/PackageManager.getResourcesForApplication:(Ljava/lang/String;)Landroid/content/res/Resources;
        //    49: astore_3       
        //    50: aload_2        
        //    51: invokevirtual   android/net/Uri.getPathSegments:()Ljava/util/List;
        //    54: astore_2       
        //    55: aload_2        
        //    56: invokeinterface java/util/List.size:()I
        //    61: istore          5
        //    63: iload           5
        //    65: iconst_2       
        //    66: if_icmpne       110
        //    69: aload_2        
        //    70: iconst_0       
        //    71: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    76: checkcast       Ljava/lang/String;
        //    79: ldc             "drawable"
        //    81: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    84: ifeq            110
        //    87: aload_3        
        //    88: aload_2        
        //    89: iconst_1       
        //    90: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    95: checkcast       Ljava/lang/String;
        //    98: ldc             "drawable"
        //   100: aload           4
        //   102: invokevirtual   android/content/res/Resources.getIdentifier:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
        //   105: istore          5
        //   107: goto            153
        //   110: iload           5
        //   112: iconst_1       
        //   113: if_icmpne       150
        //   116: aload_2        
        //   117: iconst_0       
        //   118: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   123: checkcast       Ljava/lang/CharSequence;
        //   126: invokestatic    android/text/TextUtils.isDigitsOnly:(Ljava/lang/CharSequence;)Z
        //   129: ifeq            150
        //   132: aload_2        
        //   133: iconst_0       
        //   134: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   139: checkcast       Ljava/lang/String;
        //   142: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   145: istore          5
        //   147: goto            153
        //   150: iconst_0       
        //   151: istore          5
        //   153: aload_0        
        //   154: aload_1        
        //   155: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //   158: iload           5
        //   160: invokevirtual   android/content/res/Resources.openRawResource:(I)Ljava/io/InputStream;
        //   163: iconst_0       
        //   164: invokestatic    android/graphics/BitmapRegionDecoder.newInstance:(Ljava/io/InputStream;Z)Landroid/graphics/BitmapRegionDecoder;
        //   167: putfield        com/davemorrissey/labs/subscaleview/decoder/SkiaImageRegionDecoder.decoder:Landroid/graphics/BitmapRegionDecoder;
        //   170: goto            267
        //   173: aload_3        
        //   174: ldc             "file:///android_asset/"
        //   176: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   179: ifeq            212
        //   182: aload_3        
        //   183: ldc             "file:///android_asset/"
        //   185: invokevirtual   java/lang/String.length:()I
        //   188: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   191: astore_2       
        //   192: aload_0        
        //   193: aload_1        
        //   194: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //   197: aload_2        
        //   198: iconst_1       
        //   199: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;I)Ljava/io/InputStream;
        //   202: iconst_0       
        //   203: invokestatic    android/graphics/BitmapRegionDecoder.newInstance:(Ljava/io/InputStream;Z)Landroid/graphics/BitmapRegionDecoder;
        //   206: putfield        com/davemorrissey/labs/subscaleview/decoder/SkiaImageRegionDecoder.decoder:Landroid/graphics/BitmapRegionDecoder;
        //   209: goto            267
        //   212: aload_3        
        //   213: ldc             "file://"
        //   215: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   218: ifeq            241
        //   221: aload_0        
        //   222: aload_3        
        //   223: ldc             "file://"
        //   225: invokevirtual   java/lang/String.length:()I
        //   228: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   231: iconst_0       
        //   232: invokestatic    android/graphics/BitmapRegionDecoder.newInstance:(Ljava/lang/String;Z)Landroid/graphics/BitmapRegionDecoder;
        //   235: putfield        com/davemorrissey/labs/subscaleview/decoder/SkiaImageRegionDecoder.decoder:Landroid/graphics/BitmapRegionDecoder;
        //   238: goto            267
        //   241: aload_1        
        //   242: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   245: aload_2        
        //   246: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //   249: astore_2       
        //   250: aload_0        
        //   251: aload_2        
        //   252: iconst_0       
        //   253: invokestatic    android/graphics/BitmapRegionDecoder.newInstance:(Ljava/io/InputStream;Z)Landroid/graphics/BitmapRegionDecoder;
        //   256: putfield        com/davemorrissey/labs/subscaleview/decoder/SkiaImageRegionDecoder.decoder:Landroid/graphics/BitmapRegionDecoder;
        //   259: aload_2        
        //   260: ifnull          267
        //   263: aload_2        
        //   264: invokevirtual   java/io/InputStream.close:()V
        //   267: new             Landroid/graphics/Point;
        //   270: dup            
        //   271: aload_0        
        //   272: getfield        com/davemorrissey/labs/subscaleview/decoder/SkiaImageRegionDecoder.decoder:Landroid/graphics/BitmapRegionDecoder;
        //   275: invokevirtual   android/graphics/BitmapRegionDecoder.getWidth:()I
        //   278: aload_0        
        //   279: getfield        com/davemorrissey/labs/subscaleview/decoder/SkiaImageRegionDecoder.decoder:Landroid/graphics/BitmapRegionDecoder;
        //   282: invokevirtual   android/graphics/BitmapRegionDecoder.getHeight:()I
        //   285: invokespecial   android/graphics/Point.<init>:(II)V
        //   288: areturn        
        //   289: astore_1       
        //   290: goto            296
        //   293: astore_1       
        //   294: aconst_null    
        //   295: astore_2       
        //   296: aload_2        
        //   297: ifnull          304
        //   300: aload_2        
        //   301: invokevirtual   java/io/InputStream.close:()V
        //   304: aload_1        
        //   305: athrow         
        //   306: astore_2       
        //   307: goto            150
        //   310: astore_1       
        //   311: goto            267
        //   314: astore_2       
        //   315: goto            304
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  132    147    306    310    Ljava/lang/NumberFormatException;
        //  241    250    293    296    Any
        //  250    259    289    293    Any
        //  263    267    310    314    Ljava/lang/Exception;
        //  300    304    314    318    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 150 out-of-bounds for length 150
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
    
    @Override
    public boolean isReady() {
        return this.decoder != null && !this.decoder.isRecycled();
    }
    
    @Override
    public void recycle() {
        this.decoder.recycle();
    }
}
