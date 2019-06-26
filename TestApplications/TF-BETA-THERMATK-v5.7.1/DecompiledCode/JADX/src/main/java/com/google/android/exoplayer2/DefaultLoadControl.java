package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;

public class DefaultLoadControl implements LoadControl {
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
    public DefaultLoadControl(DefaultAllocator defaultAllocator, int i, int i2, int i3, int i4, int i5, boolean z) {
        this(defaultAllocator, i, i2, i3, i4, i5, z, null);
    }

    @Deprecated
    public DefaultLoadControl(DefaultAllocator defaultAllocator, int i, int i2, int i3, int i4, int i5, boolean z, PriorityTaskManager priorityTaskManager) {
        this(defaultAllocator, i, i2, i3, i4, i5, z, priorityTaskManager, 0, false);
    }

    protected DefaultLoadControl(DefaultAllocator defaultAllocator, int i, int i2, int i3, int i4, int i5, boolean z, PriorityTaskManager priorityTaskManager, int i6, boolean z2) {
        String str = "bufferForPlaybackMs";
        String str2 = "0";
        assertGreaterOrEqual(i3, 0, str, str2);
        String str3 = "bufferForPlaybackAfterRebufferMs";
        assertGreaterOrEqual(i4, 0, str3, str2);
        String str4 = "minBufferMs";
        assertGreaterOrEqual(i, i3, str4, str);
        assertGreaterOrEqual(i, i4, str4, str3);
        assertGreaterOrEqual(i2, i, "maxBufferMs", str4);
        assertGreaterOrEqual(i6, 0, "backBufferDurationMs", str2);
        this.allocator = defaultAllocator;
        this.minBufferUs = C0131C.msToUs((long) i);
        this.maxBufferUs = C0131C.msToUs((long) i2);
        this.bufferForPlaybackUs = C0131C.msToUs((long) i3);
        this.bufferForPlaybackAfterRebufferUs = C0131C.msToUs((long) i4);
        this.targetBufferBytesOverwrite = i5;
        this.prioritizeTimeOverSizeThresholds = z;
        this.priorityTaskManager = priorityTaskManager;
        this.backBufferDurationUs = C0131C.msToUs((long) i6);
        this.retainBackBufferFromKeyframe = z2;
    }

    public void onPrepared() {
        reset(false);
    }

    public void onTracksSelected(Renderer[] rendererArr, TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
        int i = this.targetBufferBytesOverwrite;
        if (i == -1) {
            i = calculateTargetBufferSize(rendererArr, trackSelectionArray);
        }
        this.targetBufferSize = i;
        this.allocator.setTargetBufferSize(this.targetBufferSize);
    }

    public void onStopped() {
        reset(true);
    }

    public void onReleased() {
        reset(true);
    }

    public Allocator getAllocator() {
        return this.allocator;
    }

    public long getBackBufferDurationUs() {
        return this.backBufferDurationUs;
    }

    public boolean retainBackBufferFromKeyframe() {
        return this.retainBackBufferFromKeyframe;
    }

    public boolean shouldContinueLoading(long j, float f) {
        boolean z = true;
        Object obj = this.allocator.getTotalBytesAllocated() >= this.targetBufferSize ? 1 : null;
        boolean z2 = this.isBuffering;
        long j2 = this.minBufferUs;
        if (f > 1.0f) {
            j2 = Math.min(Util.getMediaDurationForPlayoutDuration(j2, f), this.maxBufferUs);
        }
        if (j < j2) {
            if (!(this.prioritizeTimeOverSizeThresholds || obj == null)) {
                z = false;
            }
            this.isBuffering = z;
        } else if (j >= this.maxBufferUs || obj != null) {
            this.isBuffering = false;
        }
        PriorityTaskManager priorityTaskManager = this.priorityTaskManager;
        if (priorityTaskManager != null) {
            boolean z3 = this.isBuffering;
            if (z3 != z2) {
                if (z3) {
                    priorityTaskManager.add(0);
                } else {
                    priorityTaskManager.remove(0);
                }
            }
        }
        return this.isBuffering;
    }

    public boolean shouldStartPlayback(long j, float f, boolean z) {
        j = Util.getPlayoutDurationForMediaDuration(j, f);
        long j2 = z ? this.bufferForPlaybackAfterRebufferUs : this.bufferForPlaybackUs;
        return j2 <= 0 || j >= j2 || (!this.prioritizeTimeOverSizeThresholds && this.allocator.getTotalBytesAllocated() >= this.targetBufferSize);
    }

    /* Access modifiers changed, original: protected */
    public int calculateTargetBufferSize(Renderer[] rendererArr, TrackSelectionArray trackSelectionArray) {
        int i = 0;
        for (int i2 = 0; i2 < rendererArr.length; i2++) {
            if (trackSelectionArray.get(i2) != null) {
                i += Util.getDefaultBufferSize(rendererArr[i2].getTrackType());
            }
        }
        return i;
    }

    private void reset(boolean z) {
        this.targetBufferSize = 0;
        PriorityTaskManager priorityTaskManager = this.priorityTaskManager;
        if (priorityTaskManager != null && this.isBuffering) {
            priorityTaskManager.remove(0);
        }
        this.isBuffering = false;
        if (z) {
            this.allocator.reset();
        }
    }

    private static void assertGreaterOrEqual(int i, int i2, String str, String str2) {
        boolean z = i >= i2;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" cannot be less than ");
        stringBuilder.append(str2);
        Assertions.checkArgument(z, stringBuilder.toString());
    }
}
