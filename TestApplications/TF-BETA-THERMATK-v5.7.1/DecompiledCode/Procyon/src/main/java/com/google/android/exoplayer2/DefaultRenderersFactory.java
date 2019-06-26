// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import android.os.Looper;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.video.spherical.CameraMotionRenderer;
import java.util.ArrayList;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import android.os.Handler;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import android.content.Context;

public class DefaultRenderersFactory implements RenderersFactory
{
    private final long allowedVideoJoiningTimeMs;
    private final Context context;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final int extensionRendererMode;
    
    public DefaultRenderersFactory(final Context context, final int n) {
        this(context, n, 5000L);
    }
    
    public DefaultRenderersFactory(final Context context, final int extensionRendererMode, final long allowedVideoJoiningTimeMs) {
        this.context = context;
        this.extensionRendererMode = extensionRendererMode;
        this.allowedVideoJoiningTimeMs = allowedVideoJoiningTimeMs;
        this.drmSessionManager = null;
    }
    
    protected AudioProcessor[] buildAudioProcessors() {
        return new AudioProcessor[0];
    }
    
    protected void buildAudioRenderers(final Context p0, final DrmSessionManager<FrameworkMediaCrypto> p1, final AudioProcessor[] p2, final Handler p3, final AudioRendererEventListener p4, final int p5, final ArrayList<Renderer> p6) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: new             Lcom/google/android/exoplayer2/audio/MediaCodecAudioRenderer;
        //     5: dup            
        //     6: aload_1        
        //     7: getstatic       com/google/android/exoplayer2/mediacodec/MediaCodecSelector.DEFAULT:Lcom/google/android/exoplayer2/mediacodec/MediaCodecSelector;
        //    10: aload_2        
        //    11: iconst_0       
        //    12: aload           4
        //    14: aload           5
        //    16: aload_1        
        //    17: invokestatic    com/google/android/exoplayer2/audio/AudioCapabilities.getCapabilities:(Landroid/content/Context;)Lcom/google/android/exoplayer2/audio/AudioCapabilities;
        //    20: aload_3        
        //    21: invokespecial   com/google/android/exoplayer2/audio/MediaCodecAudioRenderer.<init>:(Landroid/content/Context;Lcom/google/android/exoplayer2/mediacodec/MediaCodecSelector;Lcom/google/android/exoplayer2/drm/DrmSessionManager;ZLandroid/os/Handler;Lcom/google/android/exoplayer2/audio/AudioRendererEventListener;Lcom/google/android/exoplayer2/audio/AudioCapabilities;[Lcom/google/android/exoplayer2/audio/AudioProcessor;)V
        //    24: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //    27: pop            
        //    28: iload           6
        //    30: ifne            34
        //    33: return         
        //    34: aload           7
        //    36: invokevirtual   java/util/ArrayList.size:()I
        //    39: istore          8
        //    41: iload           8
        //    43: istore          9
        //    45: iload           6
        //    47: iconst_2       
        //    48: if_icmpne       57
        //    51: iload           8
        //    53: iconst_1       
        //    54: isub           
        //    55: istore          9
        //    57: ldc             "com.google.android.exoplayer2.ext.opus.LibopusAudioRenderer"
        //    59: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //    62: iconst_3       
        //    63: anewarray       Ljava/lang/Class;
        //    66: dup            
        //    67: iconst_0       
        //    68: ldc             Landroid/os/Handler;.class
        //    70: aastore        
        //    71: dup            
        //    72: iconst_1       
        //    73: ldc             Lcom/google/android/exoplayer2/audio/AudioRendererEventListener;.class
        //    75: aastore        
        //    76: dup            
        //    77: iconst_2       
        //    78: ldc             [Lcom/google/android/exoplayer2/audio/AudioProcessor;.class
        //    80: aastore        
        //    81: invokevirtual   java/lang/Class.getConstructor:([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //    84: iconst_3       
        //    85: anewarray       Ljava/lang/Object;
        //    88: dup            
        //    89: iconst_0       
        //    90: aload           4
        //    92: aastore        
        //    93: dup            
        //    94: iconst_1       
        //    95: aload           5
        //    97: aastore        
        //    98: dup            
        //    99: iconst_2       
        //   100: aload_3        
        //   101: aastore        
        //   102: invokevirtual   java/lang/reflect/Constructor.newInstance:([Ljava/lang/Object;)Ljava/lang/Object;
        //   105: checkcast       Lcom/google/android/exoplayer2/Renderer;
        //   108: astore_1       
        //   109: iload           9
        //   111: iconst_1       
        //   112: iadd           
        //   113: istore          6
        //   115: aload           7
        //   117: iload           9
        //   119: aload_1        
        //   120: invokevirtual   java/util/ArrayList.add:(ILjava/lang/Object;)V
        //   123: ldc             "DefaultRenderersFactory"
        //   125: ldc             "Loaded LibopusAudioRenderer."
        //   127: invokestatic    com/google/android/exoplayer2/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)V
        //   130: goto            150
        //   133: astore_1       
        //   134: new             Ljava/lang/RuntimeException;
        //   137: dup            
        //   138: ldc             "Error instantiating Opus extension"
        //   140: aload_1        
        //   141: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   144: athrow         
        //   145: astore_1       
        //   146: iload           9
        //   148: istore          6
        //   150: ldc             "com.google.android.exoplayer2.ext.flac.LibflacAudioRenderer"
        //   152: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   155: iconst_3       
        //   156: anewarray       Ljava/lang/Class;
        //   159: dup            
        //   160: iconst_0       
        //   161: ldc             Landroid/os/Handler;.class
        //   163: aastore        
        //   164: dup            
        //   165: iconst_1       
        //   166: ldc             Lcom/google/android/exoplayer2/audio/AudioRendererEventListener;.class
        //   168: aastore        
        //   169: dup            
        //   170: iconst_2       
        //   171: ldc             [Lcom/google/android/exoplayer2/audio/AudioProcessor;.class
        //   173: aastore        
        //   174: invokevirtual   java/lang/Class.getConstructor:([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //   177: iconst_3       
        //   178: anewarray       Ljava/lang/Object;
        //   181: dup            
        //   182: iconst_0       
        //   183: aload           4
        //   185: aastore        
        //   186: dup            
        //   187: iconst_1       
        //   188: aload           5
        //   190: aastore        
        //   191: dup            
        //   192: iconst_2       
        //   193: aload_3        
        //   194: aastore        
        //   195: invokevirtual   java/lang/reflect/Constructor.newInstance:([Ljava/lang/Object;)Ljava/lang/Object;
        //   198: checkcast       Lcom/google/android/exoplayer2/Renderer;
        //   201: astore_1       
        //   202: iload           6
        //   204: iconst_1       
        //   205: iadd           
        //   206: istore          9
        //   208: aload           7
        //   210: iload           6
        //   212: aload_1        
        //   213: invokevirtual   java/util/ArrayList.add:(ILjava/lang/Object;)V
        //   216: ldc             "DefaultRenderersFactory"
        //   218: ldc             "Loaded LibflacAudioRenderer."
        //   220: invokestatic    com/google/android/exoplayer2/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)V
        //   223: iload           9
        //   225: istore          6
        //   227: goto            243
        //   230: astore_1       
        //   231: new             Ljava/lang/RuntimeException;
        //   234: dup            
        //   235: ldc             "Error instantiating FLAC extension"
        //   237: aload_1        
        //   238: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   241: athrow         
        //   242: astore_1       
        //   243: aload           7
        //   245: iload           6
        //   247: ldc             "com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer"
        //   249: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   252: iconst_3       
        //   253: anewarray       Ljava/lang/Class;
        //   256: dup            
        //   257: iconst_0       
        //   258: ldc             Landroid/os/Handler;.class
        //   260: aastore        
        //   261: dup            
        //   262: iconst_1       
        //   263: ldc             Lcom/google/android/exoplayer2/audio/AudioRendererEventListener;.class
        //   265: aastore        
        //   266: dup            
        //   267: iconst_2       
        //   268: ldc             [Lcom/google/android/exoplayer2/audio/AudioProcessor;.class
        //   270: aastore        
        //   271: invokevirtual   java/lang/Class.getConstructor:([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //   274: iconst_3       
        //   275: anewarray       Ljava/lang/Object;
        //   278: dup            
        //   279: iconst_0       
        //   280: aload           4
        //   282: aastore        
        //   283: dup            
        //   284: iconst_1       
        //   285: aload           5
        //   287: aastore        
        //   288: dup            
        //   289: iconst_2       
        //   290: aload_3        
        //   291: aastore        
        //   292: invokevirtual   java/lang/reflect/Constructor.newInstance:([Ljava/lang/Object;)Ljava/lang/Object;
        //   295: checkcast       Lcom/google/android/exoplayer2/Renderer;
        //   298: invokevirtual   java/util/ArrayList.add:(ILjava/lang/Object;)V
        //   301: ldc             "DefaultRenderersFactory"
        //   303: ldc             "Loaded FfmpegAudioRenderer."
        //   305: invokestatic    com/google/android/exoplayer2/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)V
        //   308: goto            323
        //   311: astore_1       
        //   312: new             Ljava/lang/RuntimeException;
        //   315: dup            
        //   316: ldc             "Error instantiating FFmpeg extension"
        //   318: aload_1        
        //   319: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   322: athrow         
        //   323: return         
        //   324: astore_1       
        //   325: goto            150
        //   328: astore_1       
        //   329: iload           9
        //   331: istore          6
        //   333: goto            243
        //   336: astore_1       
        //   337: goto            323
        //    Signature:
        //  (Landroid/content/Context;Lcom/google/android/exoplayer2/drm/DrmSessionManager<Lcom/google/android/exoplayer2/drm/FrameworkMediaCrypto;>;[Lcom/google/android/exoplayer2/audio/AudioProcessor;Landroid/os/Handler;Lcom/google/android/exoplayer2/audio/AudioRendererEventListener;ILjava/util/ArrayList<Lcom/google/android/exoplayer2/Renderer;>;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  57     109    145    150    Ljava/lang/ClassNotFoundException;
        //  57     109    133    145    Ljava/lang/Exception;
        //  115    130    324    328    Ljava/lang/ClassNotFoundException;
        //  115    130    133    145    Ljava/lang/Exception;
        //  150    202    242    243    Ljava/lang/ClassNotFoundException;
        //  150    202    230    242    Ljava/lang/Exception;
        //  208    223    328    336    Ljava/lang/ClassNotFoundException;
        //  208    223    230    242    Ljava/lang/Exception;
        //  243    308    336    340    Ljava/lang/ClassNotFoundException;
        //  243    308    311    323    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.util.ConcurrentModificationException
        //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:937)
        //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:891)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2863)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    protected void buildCameraMotionRenderers(final Context context, final int n, final ArrayList<Renderer> list) {
        list.add(new CameraMotionRenderer());
    }
    
    protected void buildMetadataRenderers(final Context context, final MetadataOutput metadataOutput, final Looper looper, final int n, final ArrayList<Renderer> list) {
        list.add(new MetadataRenderer(metadataOutput, looper));
    }
    
    protected void buildMiscellaneousRenderers(final Context context, final Handler handler, final int n, final ArrayList<Renderer> list) {
    }
    
    protected void buildTextRenderers(final Context context, final TextOutput textOutput, final Looper looper, final int n, final ArrayList<Renderer> list) {
        list.add(new TextRenderer(textOutput, looper));
    }
    
    protected void buildVideoRenderers(final Context context, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final long l, final Handler handler, final VideoRendererEventListener videoRendererEventListener, final int n, final ArrayList<Renderer> list) {
        list.add(new MediaCodecVideoRenderer(context, MediaCodecSelector.DEFAULT, l, drmSessionManager, false, handler, videoRendererEventListener, 50));
        if (n == 0) {
            return;
        }
        int size = list.size();
        if (n == 2) {
            --size;
        }
        try {
            list.add(size, (MediaCodecVideoRenderer)Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(Boolean.TYPE, Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(true, l, handler, videoRendererEventListener, 50));
            Log.i("DefaultRenderersFactory", "Loaded LibvpxVideoRenderer.");
            goto Label_0166;
        }
        catch (Exception cause) {
            throw new RuntimeException("Error instantiating VP9 extension", cause);
        }
        catch (ClassNotFoundException ex) {
            goto Label_0166;
        }
    }
    
    @Override
    public Renderer[] createRenderers(final Handler handler, final VideoRendererEventListener videoRendererEventListener, final AudioRendererEventListener audioRendererEventListener, final TextOutput textOutput, final MetadataOutput metadataOutput, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        if (drmSessionManager == null) {
            drmSessionManager = this.drmSessionManager;
        }
        final ArrayList<Renderer> list = new ArrayList<Renderer>();
        this.buildVideoRenderers(this.context, drmSessionManager, this.allowedVideoJoiningTimeMs, handler, videoRendererEventListener, this.extensionRendererMode, list);
        this.buildAudioRenderers(this.context, drmSessionManager, this.buildAudioProcessors(), handler, audioRendererEventListener, this.extensionRendererMode, list);
        this.buildTextRenderers(this.context, textOutput, handler.getLooper(), this.extensionRendererMode, list);
        this.buildMetadataRenderers(this.context, metadataOutput, handler.getLooper(), this.extensionRendererMode, list);
        this.buildCameraMotionRenderers(this.context, this.extensionRendererMode, list);
        this.buildMiscellaneousRenderers(this.context, handler, this.extensionRendererMode, list);
        return list.toArray(new Renderer[0]);
    }
}
