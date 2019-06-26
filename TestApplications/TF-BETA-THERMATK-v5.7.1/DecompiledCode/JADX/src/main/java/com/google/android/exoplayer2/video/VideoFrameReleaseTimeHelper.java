package com.google.android.exoplayer2.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.exoplayer2.util.Util;

@TargetApi(16)
public final class VideoFrameReleaseTimeHelper {
    private long adjustedLastFrameTimeNs;
    private final DefaultDisplayListener displayListener;
    private long frameCount;
    private boolean haveSync;
    private long lastFramePresentationTimeUs;
    private long pendingAdjustedFrameTimeNs;
    private long syncFramePresentationTimeNs;
    private long syncUnadjustedReleaseTimeNs;
    private long vsyncDurationNs;
    private long vsyncOffsetNs;
    private final VSyncSampler vsyncSampler;
    private final WindowManager windowManager;

    @TargetApi(17)
    private final class DefaultDisplayListener implements DisplayListener {
        private final DisplayManager displayManager;

        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public DefaultDisplayListener(DisplayManager displayManager) {
            this.displayManager = displayManager;
        }

        public void register() {
            this.displayManager.registerDisplayListener(this, null);
        }

        public void unregister() {
            this.displayManager.unregisterDisplayListener(this);
        }

        public void onDisplayChanged(int i) {
            if (i == 0) {
                VideoFrameReleaseTimeHelper.this.updateDefaultDisplayRefreshRateParams();
            }
        }
    }

    private static final class VSyncSampler implements FrameCallback, Callback {
        private static final VSyncSampler INSTANCE = new VSyncSampler();
        private Choreographer choreographer;
        private final HandlerThread choreographerOwnerThread = new HandlerThread("ChoreographerOwner:Handler");
        private final Handler handler;
        private int observerCount;
        public volatile long sampledVsyncTimeNs = -9223372036854775807L;

        public static VSyncSampler getInstance() {
            return INSTANCE;
        }

        private VSyncSampler() {
            this.choreographerOwnerThread.start();
            this.handler = Util.createHandler(this.choreographerOwnerThread.getLooper(), this);
            this.handler.sendEmptyMessage(0);
        }

        public void addObserver() {
            this.handler.sendEmptyMessage(1);
        }

        public void removeObserver() {
            this.handler.sendEmptyMessage(2);
        }

        public void doFrame(long j) {
            this.sampledVsyncTimeNs = j;
            this.choreographer.postFrameCallbackDelayed(this, 500);
        }

        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                createChoreographerInstanceInternal();
                return true;
            } else if (i == 1) {
                addObserverInternal();
                return true;
            } else if (i != 2) {
                return false;
            } else {
                removeObserverInternal();
                return true;
            }
        }

        private void createChoreographerInstanceInternal() {
            this.choreographer = Choreographer.getInstance();
        }

        private void addObserverInternal() {
            this.observerCount++;
            if (this.observerCount == 1) {
                this.choreographer.postFrameCallback(this);
            }
        }

        private void removeObserverInternal() {
            this.observerCount--;
            if (this.observerCount == 0) {
                this.choreographer.removeFrameCallback(this);
                this.sampledVsyncTimeNs = -9223372036854775807L;
            }
        }
    }

    public VideoFrameReleaseTimeHelper(Context context) {
        DefaultDisplayListener defaultDisplayListener = null;
        if (context != null) {
            context = context.getApplicationContext();
            this.windowManager = (WindowManager) context.getSystemService("window");
        } else {
            this.windowManager = null;
        }
        if (this.windowManager != null) {
            if (Util.SDK_INT >= 17) {
                defaultDisplayListener = maybeBuildDefaultDisplayListenerV17(context);
            }
            this.displayListener = defaultDisplayListener;
            this.vsyncSampler = VSyncSampler.getInstance();
        } else {
            this.displayListener = null;
            this.vsyncSampler = null;
        }
        this.vsyncDurationNs = -9223372036854775807L;
        this.vsyncOffsetNs = -9223372036854775807L;
    }

    public void enable() {
        this.haveSync = false;
        if (this.windowManager != null) {
            this.vsyncSampler.addObserver();
            DefaultDisplayListener defaultDisplayListener = this.displayListener;
            if (defaultDisplayListener != null) {
                defaultDisplayListener.register();
            }
            updateDefaultDisplayRefreshRateParams();
        }
    }

    public void disable() {
        if (this.windowManager != null) {
            DefaultDisplayListener defaultDisplayListener = this.displayListener;
            if (defaultDisplayListener != null) {
                defaultDisplayListener.unregister();
            }
            this.vsyncSampler.removeObserver();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0048  */
    public long adjustReleaseTime(long r11, long r13) {
        /*
        r10 = this;
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r0 * r11;
        r2 = r10.haveSync;
        if (r2 == 0) goto L_0x0042;
    L_0x0008:
        r2 = r10.lastFramePresentationTimeUs;
        r4 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1));
        if (r4 == 0) goto L_0x0019;
    L_0x000e:
        r2 = r10.frameCount;
        r4 = 1;
        r2 = r2 + r4;
        r10.frameCount = r2;
        r2 = r10.pendingAdjustedFrameTimeNs;
        r10.adjustedLastFrameTimeNs = r2;
    L_0x0019:
        r2 = r10.frameCount;
        r4 = 6;
        r6 = 0;
        r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r7 < 0) goto L_0x003a;
    L_0x0022:
        r4 = r10.syncFramePresentationTimeNs;
        r4 = r0 - r4;
        r4 = r4 / r2;
        r2 = r10.adjustedLastFrameTimeNs;
        r2 = r2 + r4;
        r4 = r10.isDriftTooLarge(r2, r13);
        if (r4 == 0) goto L_0x0033;
    L_0x0030:
        r10.haveSync = r6;
        goto L_0x0042;
    L_0x0033:
        r4 = r10.syncUnadjustedReleaseTimeNs;
        r4 = r4 + r2;
        r6 = r10.syncFramePresentationTimeNs;
        r4 = r4 - r6;
        goto L_0x0044;
    L_0x003a:
        r2 = r10.isDriftTooLarge(r0, r13);
        if (r2 == 0) goto L_0x0042;
    L_0x0040:
        r10.haveSync = r6;
    L_0x0042:
        r4 = r13;
        r2 = r0;
    L_0x0044:
        r6 = r10.haveSync;
        if (r6 != 0) goto L_0x0053;
    L_0x0048:
        r10.syncFramePresentationTimeNs = r0;
        r10.syncUnadjustedReleaseTimeNs = r13;
        r13 = 0;
        r10.frameCount = r13;
        r13 = 1;
        r10.haveSync = r13;
    L_0x0053:
        r10.lastFramePresentationTimeUs = r11;
        r10.pendingAdjustedFrameTimeNs = r2;
        r11 = r10.vsyncSampler;
        if (r11 == 0) goto L_0x0078;
    L_0x005b:
        r12 = r10.vsyncDurationNs;
        r0 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r14 != 0) goto L_0x0067;
    L_0x0066:
        goto L_0x0078;
    L_0x0067:
        r6 = r11.sampledVsyncTimeNs;
        r11 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1));
        if (r11 != 0) goto L_0x006e;
    L_0x006d:
        return r4;
    L_0x006e:
        r8 = r10.vsyncDurationNs;
        r11 = closestVsync(r4, r6, r8);
        r13 = r10.vsyncOffsetNs;
        r11 = r11 - r13;
        return r11;
    L_0x0078:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.VideoFrameReleaseTimeHelper.adjustReleaseTime(long, long):long");
    }

    @TargetApi(17)
    private DefaultDisplayListener maybeBuildDefaultDisplayListenerV17(Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
        if (displayManager == null) {
            return null;
        }
        return new DefaultDisplayListener(displayManager);
    }

    private void updateDefaultDisplayRefreshRateParams() {
        Display defaultDisplay = this.windowManager.getDefaultDisplay();
        if (defaultDisplay != null) {
            double refreshRate = (double) defaultDisplay.getRefreshRate();
            Double.isNaN(refreshRate);
            this.vsyncDurationNs = (long) (1.0E9d / refreshRate);
            this.vsyncOffsetNs = (this.vsyncDurationNs * 80) / 100;
        }
    }

    private boolean isDriftTooLarge(long j, long j2) {
        return Math.abs((j2 - this.syncUnadjustedReleaseTimeNs) - (j - this.syncFramePresentationTimeNs)) > 20000000;
    }

    private static long closestVsync(long j, long j2, long j3) {
        j2 += ((j - j2) / j3) * j3;
        if (j <= j2) {
            j3 = j2 - j3;
        } else {
            long j4 = j2;
            j2 = j3 + j2;
            j3 = j4;
        }
        return j2 - j < j - j3 ? j2 : j3;
    }
}
