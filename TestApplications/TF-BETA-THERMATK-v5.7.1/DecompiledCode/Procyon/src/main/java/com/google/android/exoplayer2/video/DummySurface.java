// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import android.os.Message;
import android.os.Handler;
import com.google.android.exoplayer2.util.EGLSurfaceTexture;
import android.os.Handler$Callback;
import android.os.HandlerThread;
import com.google.android.exoplayer2.util.Assertions;
import android.opengl.EGL14;
import android.content.Context;
import com.google.android.exoplayer2.util.Util;
import android.graphics.SurfaceTexture;
import android.annotation.TargetApi;
import android.view.Surface;

@TargetApi(17)
public final class DummySurface extends Surface
{
    private static int secureMode;
    private static boolean secureModeInitialized;
    public final boolean secure;
    private final DummySurfaceThread thread;
    private boolean threadReleased;
    
    private DummySurface(final DummySurfaceThread thread, final SurfaceTexture surfaceTexture, final boolean secure) {
        super(surfaceTexture);
        this.thread = thread;
        this.secure = secure;
    }
    
    private static void assertApiLevel17OrHigher() {
        if (Util.SDK_INT >= 17) {
            return;
        }
        throw new UnsupportedOperationException("Unsupported prior to API level 17");
    }
    
    @TargetApi(24)
    private static int getSecureModeV24(final Context context) {
        if (Util.SDK_INT < 26 && ("samsung".equals(Util.MANUFACTURER) || "XT1650".equals(Util.MODEL))) {
            return 0;
        }
        if (Util.SDK_INT < 26 && !context.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance")) {
            return 0;
        }
        final String eglQueryString = EGL14.eglQueryString(EGL14.eglGetDisplay(0), 12373);
        if (eglQueryString == null) {
            return 0;
        }
        if (!eglQueryString.contains("EGL_EXT_protected_content")) {
            return 0;
        }
        int n;
        if (eglQueryString.contains("EGL_KHR_surfaceless_context")) {
            n = 1;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    public static boolean isSecureSupported(final Context context) {
        synchronized (DummySurface.class) {
            final boolean secureModeInitialized = DummySurface.secureModeInitialized;
            boolean b = true;
            if (!secureModeInitialized) {
                int secureModeV24;
                if (Util.SDK_INT < 24) {
                    secureModeV24 = 0;
                }
                else {
                    secureModeV24 = getSecureModeV24(context);
                }
                DummySurface.secureMode = secureModeV24;
                DummySurface.secureModeInitialized = true;
            }
            if (DummySurface.secureMode == 0) {
                b = false;
            }
            return b;
        }
    }
    
    public static DummySurface newInstanceV17(final Context context, final boolean b) {
        assertApiLevel17OrHigher();
        int secureMode = 0;
        Assertions.checkState(!b || isSecureSupported(context));
        final DummySurfaceThread dummySurfaceThread = new DummySurfaceThread();
        if (b) {
            secureMode = DummySurface.secureMode;
        }
        return dummySurfaceThread.init(secureMode);
    }
    
    public void release() {
        super.release();
        synchronized (this.thread) {
            if (!this.threadReleased) {
                this.thread.release();
                this.threadReleased = true;
            }
        }
    }
    
    private static class DummySurfaceThread extends HandlerThread implements Handler$Callback
    {
        private EGLSurfaceTexture eglSurfaceTexture;
        private Handler handler;
        private Error initError;
        private RuntimeException initException;
        private DummySurface surface;
        
        public DummySurfaceThread() {
            super("dummySurface");
        }
        
        private void initInternal(final int n) {
            Assertions.checkNotNull(this.eglSurfaceTexture);
            this.eglSurfaceTexture.init(n);
            this.surface = new DummySurface(this, this.eglSurfaceTexture.getSurfaceTexture(), n != 0, null);
        }
        
        private void releaseInternal() {
            Assertions.checkNotNull(this.eglSurfaceTexture);
            this.eglSurfaceTexture.release();
        }
        
        public boolean handleMessage(final Message p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        android/os/Message.what:I
            //     4: istore_2       
            //     5: iload_2        
            //     6: iconst_1       
            //     7: if_icmpeq       54
            //    10: iload_2        
            //    11: iconst_2       
            //    12: if_icmpeq       17
            //    15: iconst_1       
            //    16: ireturn        
            //    17: aload_0        
            //    18: invokespecial   com/google/android/exoplayer2/video/DummySurface$DummySurfaceThread.releaseInternal:()V
            //    21: aload_0        
            //    22: invokevirtual   android/os/HandlerThread.quit:()Z
            //    25: pop            
            //    26: goto            45
            //    29: astore_1       
            //    30: goto            47
            //    33: astore_1       
            //    34: ldc             "DummySurface"
            //    36: ldc             "Failed to release dummy surface"
            //    38: aload_1        
            //    39: invokestatic    com/google/android/exoplayer2/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
            //    42: goto            21
            //    45: iconst_1       
            //    46: ireturn        
            //    47: aload_0        
            //    48: invokevirtual   android/os/HandlerThread.quit:()Z
            //    51: pop            
            //    52: aload_1        
            //    53: athrow         
            //    54: aload_0        
            //    55: aload_1        
            //    56: getfield        android/os/Message.arg1:I
            //    59: invokespecial   com/google/android/exoplayer2/video/DummySurface$DummySurfaceThread.initInternal:(I)V
            //    62: aload_0        
            //    63: monitorenter   
            //    64: aload_0        
            //    65: invokevirtual   java/lang/Object.notify:()V
            //    68: aload_0        
            //    69: monitorexit    
            //    70: goto            134
            //    73: astore_1       
            //    74: aload_0        
            //    75: monitorexit    
            //    76: aload_1        
            //    77: athrow         
            //    78: astore_1       
            //    79: goto            141
            //    82: astore_1       
            //    83: ldc             "DummySurface"
            //    85: ldc             "Failed to initialize dummy surface"
            //    87: aload_1        
            //    88: invokestatic    com/google/android/exoplayer2/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
            //    91: aload_0        
            //    92: aload_1        
            //    93: putfield        com/google/android/exoplayer2/video/DummySurface$DummySurfaceThread.initError:Ljava/lang/Error;
            //    96: aload_0        
            //    97: monitorenter   
            //    98: aload_0        
            //    99: invokevirtual   java/lang/Object.notify:()V
            //   102: aload_0        
            //   103: monitorexit    
            //   104: goto            134
            //   107: astore_1       
            //   108: aload_0        
            //   109: monitorexit    
            //   110: aload_1        
            //   111: athrow         
            //   112: astore_1       
            //   113: ldc             "DummySurface"
            //   115: ldc             "Failed to initialize dummy surface"
            //   117: aload_1        
            //   118: invokestatic    com/google/android/exoplayer2/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
            //   121: aload_0        
            //   122: aload_1        
            //   123: putfield        com/google/android/exoplayer2/video/DummySurface$DummySurfaceThread.initException:Ljava/lang/RuntimeException;
            //   126: aload_0        
            //   127: monitorenter   
            //   128: aload_0        
            //   129: invokevirtual   java/lang/Object.notify:()V
            //   132: aload_0        
            //   133: monitorexit    
            //   134: iconst_1       
            //   135: ireturn        
            //   136: astore_1       
            //   137: aload_0        
            //   138: monitorexit    
            //   139: aload_1        
            //   140: athrow         
            //   141: aload_0        
            //   142: monitorenter   
            //   143: aload_0        
            //   144: invokevirtual   java/lang/Object.notify:()V
            //   147: aload_0        
            //   148: monitorexit    
            //   149: aload_1        
            //   150: athrow         
            //   151: astore_1       
            //   152: aload_0        
            //   153: monitorexit    
            //   154: aload_1        
            //   155: athrow         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                        
            //  -----  -----  -----  -----  ----------------------------
            //  17     21     33     45     Ljava/lang/Throwable;
            //  17     21     29     33     Any
            //  34     42     29     33     Any
            //  54     62     112    141    Ljava/lang/RuntimeException;
            //  54     62     82     112    Ljava/lang/Error;
            //  54     62     78     156    Any
            //  64     70     73     78     Any
            //  74     76     73     78     Any
            //  83     96     78     156    Any
            //  98     104    107    112    Any
            //  108    110    107    112    Any
            //  113    126    78     156    Any
            //  128    134    136    141    Any
            //  137    139    136    141    Any
            //  143    149    151    156    Any
            //  152    154    151    156    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 104 out-of-bounds for length 104
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public DummySurface init(int n) {
            this.start();
            this.handler = new Handler(this.getLooper(), (Handler$Callback)this);
            this.eglSurfaceTexture = new EGLSurfaceTexture(this.handler);
            synchronized (this) {
                final Handler handler = this.handler;
                final int n2 = 0;
                handler.obtainMessage(1, n, 0).sendToTarget();
                n = n2;
                while (this.surface == null && this.initException == null && this.initError == null) {
                    try {
                        this.wait();
                    }
                    catch (InterruptedException ex) {
                        n = 1;
                    }
                }
                // monitorexit(this)
                if (n != 0) {
                    Thread.currentThread().interrupt();
                }
                final RuntimeException initException = this.initException;
                if (initException != null) {
                    throw initException;
                }
                final Error initError = this.initError;
                if (initError == null) {
                    final DummySurface surface = this.surface;
                    Assertions.checkNotNull(surface);
                    return surface;
                }
                throw initError;
            }
        }
        
        public void release() {
            Assertions.checkNotNull(this.handler);
            this.handler.sendEmptyMessage(2);
        }
    }
}
