// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.upstream.DefaultAllocator;

public class DefaultLoadControl implements LoadControl
{
    private final DefaultAllocator allocator;
    private final long backBufferDurationUs;
    private final long bufferForPlaybackAfterRebufferUs;
    private final long bufferForPlaybackUs;
    private boolean isBuffering;
    private final long maxBufferUs;
    private final long minBufferUs;
    private final boolean prioritizeTimeOverSizeThresholds;
    private final PriorityTaskManager priorityTaskManager;
    private final boolean retainBackBufferFromKeyframe;
    private final int targetBufferBytesOverwrite;
    private int targetBufferSize;
    
    @Deprecated
    public DefaultLoadControl(final DefaultAllocator defaultAllocator, final int n, final int n2, final int n3, final int n4, final int n5, final boolean b) {
        this(defaultAllocator, n, n2, n3, n4, n5, b, null);
    }
    
    @Deprecated
    public DefaultLoadControl(final DefaultAllocator defaultAllocator, final int n, final int n2, final int n3, final int n4, final int n5, final boolean b, final PriorityTaskManager priorityTaskManager) {
        this(defaultAllocator, n, n2, n3, n4, n5, b, priorityTaskManager, 0, false);
    }
    
    protected DefaultLoadControl(final DefaultAllocator allocator, final int n, final int n2, final int n3, final int n4, final int targetBufferBytesOverwrite, final boolean prioritizeTimeOverSizeThresholds, final PriorityTaskManager priorityTaskManager, final int n5, final boolean retainBackBufferFromKeyframe) {
        assertGreaterOrEqual(n3, 0, "bufferForPlaybackMs", "0");
        assertGreaterOrEqual(n4, 0, "bufferForPlaybackAfterRebufferMs", "0");
        assertGreaterOrEqual(n, n3, "minBufferMs", "bufferForPlaybackMs");
        assertGreaterOrEqual(n, n4, "minBufferMs", "bufferForPlaybackAfterRebufferMs");
        assertGreaterOrEqual(n2, n, "maxBufferMs", "minBufferMs");
        assertGreaterOrEqual(n5, 0, "backBufferDurationMs", "0");
        this.allocator = allocator;
        this.minBufferUs = C.msToUs(n);
        this.maxBufferUs = C.msToUs(n2);
        this.bufferForPlaybackUs = C.msToUs(n3);
        this.bufferForPlaybackAfterRebufferUs = C.msToUs(n4);
        this.targetBufferBytesOverwrite = targetBufferBytesOverwrite;
        this.prioritizeTimeOverSizeThresholds = prioritizeTimeOverSizeThresholds;
        this.priorityTaskManager = priorityTaskManager;
        this.backBufferDurationUs = C.msToUs(n5);
        this.retainBackBufferFromKeyframe = retainBackBufferFromKeyframe;
    }
    
    private static void assertGreaterOrEqual(final int n, final int n2, final String str, final String str2) {
        final boolean b = n >= n2;
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" cannot be less than ");
        sb.append(str2);
        Assertions.checkArgument(b, sb.toString());
    }
    
    private void reset(final boolean b) {
        this.targetBufferSize = 0;
        final PriorityTaskManager priorityTaskManager = this.priorityTaskManager;
        if (priorityTaskManager != null && this.isBuffering) {
            priorityTaskManager.remove(0);
        }
        this.isBuffering = false;
        if (b) {
            this.allocator.reset();
        }
    }
    
    protected int calculateTargetBufferSize(final Renderer[] array, final TrackSelectionArray trackSelectionArray) {
        int i = 0;
        int n = 0;
        while (i < array.length) {
            int n2 = n;
            if (trackSelectionArray.get(i) != null) {
                n2 = n + Util.getDefaultBufferSize(array[i].getTrackType());
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    @Override
    public Allocator getAllocator() {
        return this.allocator;
    }
    
    @Override
    public long getBackBufferDurationUs() {
        return this.backBufferDurationUs;
    }
    
    @Override
    public void onPrepared() {
        this.reset(false);
    }
    
    @Override
    public void onReleased() {
        this.reset(true);
    }
    
    @Override
    public void onStopped() {
        this.reset(true);
    }
    
    @Override
    public void onTracksSelected(final Renderer[] array, final TrackGroupArray trackGroupArray, final TrackSelectionArray trackSelectionArray) {
        int targetBufferSize;
        if ((targetBufferSize = this.targetBufferBytesOverwrite) == -1) {
            targetBufferSize = this.calculateTargetBufferSize(array, trackSelectionArray);
        }
        this.targetBufferSize = targetBufferSize;
        this.allocator.setTargetBufferSize(this.targetBufferSize);
    }
    
    @Override
    public boolean retainBackBufferFromKeyframe() {
        return this.retainBackBufferFromKeyframe;
    }
    
    @Override
    public boolean shouldContinueLoading(final long n, final float n2) {
        final int totalBytesAllocated = this.allocator.getTotalBytesAllocated();
        final int targetBufferSize = this.targetBufferSize;
        final boolean b = true;
        final boolean b2 = totalBytesAllocated >= targetBufferSize;
        final boolean isBuffering = this.isBuffering;
        long n3 = this.minBufferUs;
        if (n2 > 1.0f) {
            n3 = Math.min(Util.getMediaDurationForPlayoutDuration(n3, n2), this.maxBufferUs);
        }
        if (n < n3) {
            boolean isBuffering2 = b;
            if (!this.prioritizeTimeOverSizeThresholds) {
                isBuffering2 = (!b2 && b);
            }
            this.isBuffering = isBuffering2;
        }
        else if (n >= this.maxBufferUs || b2) {
            this.isBuffering = false;
        }
        final PriorityTaskManager priorityTaskManager = this.priorityTaskManager;
        if (priorityTaskManager != null) {
            final boolean isBuffering3 = this.isBuffering;
            if (isBuffering3 != isBuffering) {
                if (isBuffering3) {
                    priorityTaskManager.add(0);
                }
                else {
                    priorityTaskManager.remove(0);
                }
            }
        }
        return this.isBuffering;
    }
    
    @Override
    public boolean shouldStartPlayback(long n, final float n2, final boolean b) {
        final long playoutDurationForMediaDuration = Util.getPlayoutDurationForMediaDuration(n, n2);
        if (b) {
            n = this.bufferForPlaybackAfterRebufferUs;
        }
        else {
            n = this.bufferForPlaybackUs;
        }
        return n <= 0L || playoutDurationForMediaDuration >= n || (!this.prioritizeTimeOverSizeThresholds && this.allocator.getTotalBytesAllocated() >= this.targetBufferSize);
    }
}
