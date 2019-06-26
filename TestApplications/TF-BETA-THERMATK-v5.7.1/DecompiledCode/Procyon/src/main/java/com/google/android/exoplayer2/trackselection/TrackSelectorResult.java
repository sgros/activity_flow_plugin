// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.RendererConfiguration;

public final class TrackSelectorResult
{
    public final Object info;
    public final int length;
    public final RendererConfiguration[] rendererConfigurations;
    public final TrackSelectionArray selections;
    
    public TrackSelectorResult(final RendererConfiguration[] rendererConfigurations, final TrackSelection[] array, final Object info) {
        this.rendererConfigurations = rendererConfigurations;
        this.selections = new TrackSelectionArray(array);
        this.info = info;
        this.length = rendererConfigurations.length;
    }
    
    public boolean isEquivalent(final TrackSelectorResult trackSelectorResult) {
        if (trackSelectorResult != null && trackSelectorResult.selections.length == this.selections.length) {
            for (int i = 0; i < this.selections.length; ++i) {
                if (!this.isEquivalent(trackSelectorResult, i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean isEquivalent(final TrackSelectorResult trackSelectorResult, final int n) {
        final boolean b = false;
        if (trackSelectorResult == null) {
            return false;
        }
        boolean b2 = b;
        if (Util.areEqual(this.rendererConfigurations[n], trackSelectorResult.rendererConfigurations[n])) {
            b2 = b;
            if (Util.areEqual(this.selections.get(n), trackSelectorResult.selections.get(n))) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public boolean isRendererEnabled(final int n) {
        return this.rendererConfigurations[n] != null;
    }
}
