// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import android.os.Message;
import android.os.HandlerThread;
import android.view.Choreographer;
import android.os.Handler$Callback;
import android.view.Choreographer$FrameCallback;
import android.os.Handler;
import android.hardware.display.DisplayManager$DisplayListener;
import android.view.Display;
import android.hardware.display.DisplayManager;
import com.google.android.exoplayer2.util.Util;
import android.content.Context;
import android.view.WindowManager;
import android.annotation.TargetApi;

@TargetApi(16)
public final class VideoFrameReleaseTimeHelper
{
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
    
    public VideoFrameReleaseTimeHelper(Context applicationContext) {
        DefaultDisplayListener maybeBuildDefaultDisplayListenerV17 = null;
        if (applicationContext != null) {
            applicationContext = applicationContext.getApplicationContext();
            this.windowManager = (WindowManager)applicationContext.getSystemService("window");
        }
        else {
            this.windowManager = null;
        }
        if (this.windowManager != null) {
            if (Util.SDK_INT >= 17) {
                maybeBuildDefaultDisplayListenerV17 = this.maybeBuildDefaultDisplayListenerV17(applicationContext);
            }
            this.displayListener = maybeBuildDefaultDisplayListenerV17;
            this.vsyncSampler = VSyncSampler.getInstance();
        }
        else {
            this.displayListener = null;
            this.vsyncSampler = null;
        }
        this.vsyncDurationNs = -9223372036854775807L;
        this.vsyncOffsetNs = -9223372036854775807L;
    }
    
    private static long closestVsync(final long n, long n2, long n3) {
        n2 += (n - n2) / n3 * n3;
        if (n <= n2) {
            n3 = n2 - n3;
        }
        else {
            final long n4 = n3 + n2;
            n3 = n2;
            n2 = n4;
        }
        if (n2 - n >= n - n3) {
            n2 = n3;
        }
        return n2;
    }
    
    private boolean isDriftTooLarge(final long n, final long n2) {
        return Math.abs(n2 - this.syncUnadjustedReleaseTimeNs - (n - this.syncFramePresentationTimeNs)) > 20000000L;
    }
    
    @TargetApi(17)
    private DefaultDisplayListener maybeBuildDefaultDisplayListenerV17(final Context context) {
        final DisplayManager displayManager = (DisplayManager)context.getSystemService("display");
        DefaultDisplayListener defaultDisplayListener;
        if (displayManager == null) {
            defaultDisplayListener = null;
        }
        else {
            defaultDisplayListener = new DefaultDisplayListener(displayManager);
        }
        return defaultDisplayListener;
    }
    
    private void updateDefaultDisplayRefreshRateParams() {
        final Display defaultDisplay = this.windowManager.getDefaultDisplay();
        if (defaultDisplay != null) {
            final double v = defaultDisplay.getRefreshRate();
            Double.isNaN(v);
            this.vsyncDurationNs = (long)(1.0E9 / v);
            this.vsyncOffsetNs = this.vsyncDurationNs * 80L / 100L;
        }
    }
    
    public long adjustReleaseTime(long sampledVsyncTimeNs, final long syncUnadjustedReleaseTimeNs) {
        final long syncFramePresentationTimeNs = 1000L * sampledVsyncTimeNs;
        long pendingAdjustedFrameTimeNs = 0L;
        long n = 0L;
        Label_0134: {
            if (this.haveSync) {
                if (sampledVsyncTimeNs != this.lastFramePresentationTimeUs) {
                    ++this.frameCount;
                    this.adjustedLastFrameTimeNs = this.pendingAdjustedFrameTimeNs;
                }
                final long frameCount = this.frameCount;
                if (frameCount >= 6L) {
                    pendingAdjustedFrameTimeNs = this.adjustedLastFrameTimeNs + (syncFramePresentationTimeNs - this.syncFramePresentationTimeNs) / frameCount;
                    if (!this.isDriftTooLarge(pendingAdjustedFrameTimeNs, syncUnadjustedReleaseTimeNs)) {
                        n = this.syncUnadjustedReleaseTimeNs + pendingAdjustedFrameTimeNs - this.syncFramePresentationTimeNs;
                        break Label_0134;
                    }
                    this.haveSync = false;
                }
                else if (this.isDriftTooLarge(syncFramePresentationTimeNs, syncUnadjustedReleaseTimeNs)) {
                    this.haveSync = false;
                }
            }
            n = syncUnadjustedReleaseTimeNs;
            pendingAdjustedFrameTimeNs = syncFramePresentationTimeNs;
        }
        if (!this.haveSync) {
            this.syncFramePresentationTimeNs = syncFramePresentationTimeNs;
            this.syncUnadjustedReleaseTimeNs = syncUnadjustedReleaseTimeNs;
            this.frameCount = 0L;
            this.haveSync = true;
        }
        this.lastFramePresentationTimeUs = sampledVsyncTimeNs;
        this.pendingAdjustedFrameTimeNs = pendingAdjustedFrameTimeNs;
        final VSyncSampler vsyncSampler = this.vsyncSampler;
        if (vsyncSampler == null || this.vsyncDurationNs == -9223372036854775807L) {
            return n;
        }
        sampledVsyncTimeNs = vsyncSampler.sampledVsyncTimeNs;
        if (sampledVsyncTimeNs == -9223372036854775807L) {
            return n;
        }
        return closestVsync(n, sampledVsyncTimeNs, this.vsyncDurationNs) - this.vsyncOffsetNs;
    }
    
    public void disable() {
        if (this.windowManager != null) {
            final DefaultDisplayListener displayListener = this.displayListener;
            if (displayListener != null) {
                displayListener.unregister();
            }
            this.vsyncSampler.removeObserver();
        }
    }
    
    public void enable() {
        this.haveSync = false;
        if (this.windowManager != null) {
            this.vsyncSampler.addObserver();
            final DefaultDisplayListener displayListener = this.displayListener;
            if (displayListener != null) {
                displayListener.register();
            }
            this.updateDefaultDisplayRefreshRateParams();
        }
    }
    
    @TargetApi(17)
    private final class DefaultDisplayListener implements DisplayManager$DisplayListener
    {
        private final DisplayManager displayManager;
        
        public DefaultDisplayListener(final DisplayManager displayManager) {
            this.displayManager = displayManager;
        }
        
        public void onDisplayAdded(final int n) {
        }
        
        public void onDisplayChanged(final int n) {
            if (n == 0) {
                VideoFrameReleaseTimeHelper.this.updateDefaultDisplayRefreshRateParams();
            }
        }
        
        public void onDisplayRemoved(final int n) {
        }
        
        public void register() {
            this.displayManager.registerDisplayListener((DisplayManager$DisplayListener)this, (Handler)null);
        }
        
        public void unregister() {
            this.displayManager.unregisterDisplayListener((DisplayManager$DisplayListener)this);
        }
    }
    
    private static final class VSyncSampler implements Choreographer$FrameCallback, Handler$Callback
    {
        private static final VSyncSampler INSTANCE;
        private Choreographer choreographer;
        private final HandlerThread choreographerOwnerThread;
        private final Handler handler;
        private int observerCount;
        public volatile long sampledVsyncTimeNs;
        
        static {
            INSTANCE = new VSyncSampler();
        }
        
        private VSyncSampler() {
            this.sampledVsyncTimeNs = -9223372036854775807L;
            (this.choreographerOwnerThread = new HandlerThread("ChoreographerOwner:Handler")).start();
            (this.handler = Util.createHandler(this.choreographerOwnerThread.getLooper(), (Handler$Callback)this)).sendEmptyMessage(0);
        }
        
        private void addObserverInternal() {
            ++this.observerCount;
            if (this.observerCount == 1) {
                this.choreographer.postFrameCallback((Choreographer$FrameCallback)this);
            }
        }
        
        private void createChoreographerInstanceInternal() {
            this.choreographer = Choreographer.getInstance();
        }
        
        public static VSyncSampler getInstance() {
            return VSyncSampler.INSTANCE;
        }
        
        private void removeObserverInternal() {
            --this.observerCount;
            if (this.observerCount == 0) {
                this.choreographer.removeFrameCallback((Choreographer$FrameCallback)this);
                this.sampledVsyncTimeNs = -9223372036854775807L;
            }
        }
        
        public void addObserver() {
            this.handler.sendEmptyMessage(1);
        }
        
        public void doFrame(final long sampledVsyncTimeNs) {
            this.sampledVsyncTimeNs = sampledVsyncTimeNs;
            this.choreographer.postFrameCallbackDelayed((Choreographer$FrameCallback)this, 500L);
        }
        
        public boolean handleMessage(final Message message) {
            final int what = message.what;
            if (what == 0) {
                this.createChoreographerInstanceInternal();
                return true;
            }
            if (what == 1) {
                this.addObserverInternal();
                return true;
            }
            if (what != 2) {
                return false;
            }
            this.removeObserverInternal();
            return true;
        }
        
        public void removeObserver() {
            this.handler.sendEmptyMessage(2);
        }
    }
}
