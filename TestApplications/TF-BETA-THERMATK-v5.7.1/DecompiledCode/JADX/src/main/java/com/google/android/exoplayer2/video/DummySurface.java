package com.google.android.exoplayer2.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.view.Surface;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.EGLSurfaceTexture;
import com.google.android.exoplayer2.util.Util;

@TargetApi(17)
public final class DummySurface extends Surface {
    private static int secureMode;
    private static boolean secureModeInitialized;
    public final boolean secure;
    private final DummySurfaceThread thread;
    private boolean threadReleased;

    private static class DummySurfaceThread extends HandlerThread implements Callback {
        private EGLSurfaceTexture eglSurfaceTexture;
        private Handler handler;
        private Error initError;
        private RuntimeException initException;
        private DummySurface surface;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:60:0x005f in {4, 6, 13, 14, 16, 23, 26, 36, 39, 46, 47, 50, 55, 59} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        public boolean handleMessage(android.os.Message r4) {
            /*
            r3 = this;
            r0 = r4.what;
            r1 = 1;
            if (r0 == r1) goto L_0x0020;
            r4 = 2;
            if (r0 == r4) goto L_0x0009;
            return r1;
            r3.releaseInternal();	 Catch:{ Throwable -> 0x0012 }
            r3.quit();
            goto L_0x001b;
            r4 = move-exception;
            goto L_0x001c;
            r4 = move-exception;
            r0 = "DummySurface";	 Catch:{ all -> 0x0010 }
            r2 = "Failed to release dummy surface";	 Catch:{ all -> 0x0010 }
            com.google.android.exoplayer2.util.Log.m15e(r0, r2, r4);	 Catch:{ all -> 0x0010 }
            goto L_0x000c;
            return r1;
            r3.quit();
            throw r4;
            r4 = r4.arg1;	 Catch:{ RuntimeException -> 0x0043, Error -> 0x0030 }
            r3.initInternal(r4);	 Catch:{ RuntimeException -> 0x0043, Error -> 0x0030 }
            monitor-enter(r3);
            r3.notify();	 Catch:{ all -> 0x002b }
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
            goto L_0x0052;	 Catch:{ all -> 0x002b }
            r4 = move-exception;	 Catch:{ all -> 0x002b }
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
            throw r4;
            r4 = move-exception;
            goto L_0x0056;
            r4 = move-exception;
            r0 = "DummySurface";	 Catch:{ all -> 0x002e }
            r2 = "Failed to initialize dummy surface";	 Catch:{ all -> 0x002e }
            com.google.android.exoplayer2.util.Log.m15e(r0, r2, r4);	 Catch:{ all -> 0x002e }
            r3.initError = r4;	 Catch:{ all -> 0x002e }
            monitor-enter(r3);
            r3.notify();	 Catch:{ all -> 0x0040 }
            monitor-exit(r3);	 Catch:{ all -> 0x0040 }
            goto L_0x0052;	 Catch:{ all -> 0x0040 }
            r4 = move-exception;	 Catch:{ all -> 0x0040 }
            monitor-exit(r3);	 Catch:{ all -> 0x0040 }
            throw r4;
            r4 = move-exception;
            r0 = "DummySurface";	 Catch:{ all -> 0x002e }
            r2 = "Failed to initialize dummy surface";	 Catch:{ all -> 0x002e }
            com.google.android.exoplayer2.util.Log.m15e(r0, r2, r4);	 Catch:{ all -> 0x002e }
            r3.initException = r4;	 Catch:{ all -> 0x002e }
            monitor-enter(r3);
            r3.notify();	 Catch:{ all -> 0x0053 }
            monitor-exit(r3);	 Catch:{ all -> 0x0053 }
            return r1;	 Catch:{ all -> 0x0053 }
            r4 = move-exception;	 Catch:{ all -> 0x0053 }
            monitor-exit(r3);	 Catch:{ all -> 0x0053 }
            throw r4;
            monitor-enter(r3);
            r3.notify();	 Catch:{ all -> 0x005c }
            monitor-exit(r3);	 Catch:{ all -> 0x005c }
            throw r4;
            r4 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x005c }
            throw r4;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.DummySurface$DummySurfaceThread.handleMessage(android.os.Message):boolean");
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:31:0x0054 in {12, 13, 18, 24, 25, 26, 30} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        public com.google.android.exoplayer2.video.DummySurface init(int r4) {
            /*
            r3 = this;
            r3.start();
            r0 = new android.os.Handler;
            r1 = r3.getLooper();
            r0.<init>(r1, r3);
            r3.handler = r0;
            r0 = new com.google.android.exoplayer2.util.EGLSurfaceTexture;
            r1 = r3.handler;
            r0.<init>(r1);
            r3.eglSurfaceTexture = r0;
            monitor-enter(r3);
            r0 = r3.handler;	 Catch:{ all -> 0x0051 }
            r1 = 1;	 Catch:{ all -> 0x0051 }
            r2 = 0;	 Catch:{ all -> 0x0051 }
            r4 = r0.obtainMessage(r1, r4, r2);	 Catch:{ all -> 0x0051 }
            r4.sendToTarget();	 Catch:{ all -> 0x0051 }
            r4 = r3.surface;	 Catch:{ all -> 0x0051 }
            if (r4 != 0) goto L_0x0035;	 Catch:{ all -> 0x0051 }
            r4 = r3.initException;	 Catch:{ all -> 0x0051 }
            if (r4 != 0) goto L_0x0035;	 Catch:{ all -> 0x0051 }
            r4 = r3.initError;	 Catch:{ all -> 0x0051 }
            if (r4 != 0) goto L_0x0035;
            r3.wait();	 Catch:{ InterruptedException -> 0x0033 }
            goto L_0x0023;
            r2 = 1;
            goto L_0x0023;
            monitor-exit(r3);	 Catch:{ all -> 0x0051 }
            if (r2 == 0) goto L_0x003f;
            r4 = java.lang.Thread.currentThread();
            r4.interrupt();
            r4 = r3.initException;
            if (r4 != 0) goto L_0x0050;
            r4 = r3.initError;
            if (r4 != 0) goto L_0x004f;
            r4 = r3.surface;
            com.google.android.exoplayer2.util.Assertions.checkNotNull(r4);
            r4 = (com.google.android.exoplayer2.video.DummySurface) r4;
            return r4;
            throw r4;
            throw r4;
            r4 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0051 }
            throw r4;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.DummySurface$DummySurfaceThread.init(int):com.google.android.exoplayer2.video.DummySurface");
        }

        public DummySurfaceThread() {
            super("dummySurface");
        }

        public void release() {
            Assertions.checkNotNull(this.handler);
            this.handler.sendEmptyMessage(2);
        }

        private void initInternal(int i) {
            Assertions.checkNotNull(this.eglSurfaceTexture);
            this.eglSurfaceTexture.init(i);
            this.surface = new DummySurface(this, this.eglSurfaceTexture.getSurfaceTexture(), i != 0);
        }

        private void releaseInternal() {
            Assertions.checkNotNull(this.eglSurfaceTexture);
            this.eglSurfaceTexture.release();
        }
    }

    public static synchronized boolean isSecureSupported(Context context) {
        boolean z;
        synchronized (DummySurface.class) {
            z = true;
            if (!secureModeInitialized) {
                secureMode = Util.SDK_INT < 24 ? 0 : getSecureModeV24(context);
                secureModeInitialized = true;
            }
            if (secureMode == 0) {
                z = false;
            }
        }
        return z;
    }

    public static DummySurface newInstanceV17(Context context, boolean z) {
        assertApiLevel17OrHigher();
        int i = 0;
        boolean z2 = !z || isSecureSupported(context);
        Assertions.checkState(z2);
        DummySurfaceThread dummySurfaceThread = new DummySurfaceThread();
        if (z) {
            i = secureMode;
        }
        return dummySurfaceThread.init(i);
    }

    private DummySurface(DummySurfaceThread dummySurfaceThread, SurfaceTexture surfaceTexture, boolean z) {
        super(surfaceTexture);
        this.thread = dummySurfaceThread;
        this.secure = z;
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

    private static void assertApiLevel17OrHigher() {
        if (Util.SDK_INT < 17) {
            throw new UnsupportedOperationException("Unsupported prior to API level 17");
        }
    }

    /* JADX WARNING: Missing block: B:5:0x0019, code skipped:
            if ("XT1650".equals(com.google.android.exoplayer2.util.Util.MODEL) != false) goto L_0x001b;
     */
    @android.annotation.TargetApi(24)
    private static int getSecureModeV24(android.content.Context r4) {
        /*
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 26;
        r2 = 0;
        if (r0 >= r1) goto L_0x001c;
    L_0x0007:
        r0 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r3 = "samsung";
        r0 = r3.equals(r0);
        if (r0 != 0) goto L_0x001b;
    L_0x0011:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r3 = "XT1650";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001c;
    L_0x001b:
        return r2;
    L_0x001c:
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r0 >= r1) goto L_0x002d;
    L_0x0020:
        r4 = r4.getPackageManager();
        r0 = "android.hardware.vr.high_performance";
        r4 = r4.hasSystemFeature(r0);
        if (r4 != 0) goto L_0x002d;
    L_0x002c:
        return r2;
    L_0x002d:
        r4 = android.opengl.EGL14.eglGetDisplay(r2);
        r0 = 12373; // 0x3055 float:1.7338E-41 double:6.113E-320;
        r4 = android.opengl.EGL14.eglQueryString(r4, r0);
        if (r4 != 0) goto L_0x003a;
    L_0x0039:
        return r2;
    L_0x003a:
        r0 = "EGL_EXT_protected_content";
        r0 = r4.contains(r0);
        if (r0 != 0) goto L_0x0043;
    L_0x0042:
        return r2;
    L_0x0043:
        r0 = "EGL_KHR_surfaceless_context";
        r4 = r4.contains(r0);
        if (r4 == 0) goto L_0x004d;
    L_0x004b:
        r4 = 1;
        goto L_0x004e;
    L_0x004d:
        r4 = 2;
    L_0x004e:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.DummySurface.getSecureModeV24(android.content.Context):int");
    }
}
