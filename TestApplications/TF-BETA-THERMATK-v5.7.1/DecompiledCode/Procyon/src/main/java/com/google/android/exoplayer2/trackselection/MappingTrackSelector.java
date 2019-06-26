// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.RendererConfiguration;
import android.util.Pair;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.RendererCapabilities;

public abstract class MappingTrackSelector extends TrackSelector
{
    private MappedTrackInfo currentMappedTrackInfo;
    
    private static int findRenderer(final RendererCapabilities[] array, final TrackGroup trackGroup) throws ExoPlaybackException {
        int length = array.length;
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final RendererCapabilities rendererCapabilities = array[i];
            final int n2 = length;
            final int n3 = 0;
            int n4 = n;
            int n5 = n2;
            int n7;
            for (int j = n3; j < trackGroup.length; ++j, n4 = n7) {
                final int n6 = rendererCapabilities.supportsFormat(trackGroup.getFormat(j)) & 0x7;
                if (n6 > (n7 = n4)) {
                    if (n6 == 4) {
                        return i;
                    }
                    n5 = i;
                    n7 = n6;
                }
            }
            ++i;
            final int n8 = n5;
            n = n4;
            length = n8;
        }
        return length;
    }
    
    private static int[] getFormatSupport(final RendererCapabilities rendererCapabilities, final TrackGroup trackGroup) throws ExoPlaybackException {
        final int[] array = new int[trackGroup.length];
        for (int i = 0; i < trackGroup.length; ++i) {
            array[i] = rendererCapabilities.supportsFormat(trackGroup.getFormat(i));
        }
        return array;
    }
    
    private static int[] getMixedMimeTypeAdaptationSupports(final RendererCapabilities[] array) throws ExoPlaybackException {
        final int[] array2 = new int[array.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = array[i].supportsMixedMimeTypeAdaptation();
        }
        return array2;
    }
    
    public final MappedTrackInfo getCurrentMappedTrackInfo() {
        return this.currentMappedTrackInfo;
    }
    
    @Override
    public final void onSelectionActivated(final Object o) {
        this.currentMappedTrackInfo = (MappedTrackInfo)o;
    }
    
    protected abstract Pair<RendererConfiguration[], TrackSelection[]> selectTracks(final MappedTrackInfo p0, final int[][][] p1, final int[] p2) throws ExoPlaybackException;
    
    @Override
    public final TrackSelectorResult selectTracks(final RendererCapabilities[] array, final TrackGroupArray trackGroupArray, final MediaSource.MediaPeriodId mediaPeriodId, final Timeline timeline) throws ExoPlaybackException {
        final int[] array2 = new int[array.length + 1];
        final TrackGroup[][] array3 = new TrackGroup[array.length + 1][];
        final int[][][] array4 = new int[array.length + 1][][];
        final int n = 0;
        for (int i = 0; i < array3.length; ++i) {
            final int length = trackGroupArray.length;
            array3[i] = new TrackGroup[length];
            array4[i] = new int[length][];
        }
        final int[] mixedMimeTypeAdaptationSupports = getMixedMimeTypeAdaptationSupports(array);
        for (int j = 0; j < trackGroupArray.length; ++j) {
            final TrackGroup value = trackGroupArray.get(j);
            final int renderer = findRenderer(array, value);
            int[] formatSupport;
            if (renderer == array.length) {
                formatSupport = new int[value.length];
            }
            else {
                formatSupport = getFormatSupport(array[renderer], value);
            }
            final int n2 = array2[renderer];
            array3[renderer][n2] = value;
            array4[renderer][n2] = formatSupport;
            ++array2[renderer];
        }
        final TrackGroupArray[] array5 = new TrackGroupArray[array.length];
        final int[] array6 = new int[array.length];
        for (int k = n; k < array.length; ++k) {
            final int n3 = array2[k];
            array5[k] = new TrackGroupArray((TrackGroup[])Util.nullSafeArrayCopy(array3[k], n3));
            array4[k] = Util.nullSafeArrayCopy(array4[k], n3);
            array6[k] = array[k].getTrackType();
        }
        final MappedTrackInfo mappedTrackInfo = new MappedTrackInfo(array6, array5, mixedMimeTypeAdaptationSupports, array4, new TrackGroupArray((TrackGroup[])Util.nullSafeArrayCopy(array3[array.length], array2[array.length])));
        final Pair<RendererConfiguration[], TrackSelection[]> selectTracks = this.selectTracks(mappedTrackInfo, array4, mixedMimeTypeAdaptationSupports);
        return new TrackSelectorResult((RendererConfiguration[])selectTracks.first, (TrackSelection[])selectTracks.second, mappedTrackInfo);
    }
    
    public static final class MappedTrackInfo
    {
        @Deprecated
        public final int length;
        private final int rendererCount;
        private final int[][][] rendererFormatSupports;
        private final int[] rendererMixedMimeTypeAdaptiveSupports;
        private final TrackGroupArray[] rendererTrackGroups;
        private final int[] rendererTrackTypes;
        private final TrackGroupArray unmappedTrackGroups;
        
        MappedTrackInfo(final int[] rendererTrackTypes, final TrackGroupArray[] rendererTrackGroups, final int[] rendererMixedMimeTypeAdaptiveSupports, final int[][][] rendererFormatSupports, final TrackGroupArray unmappedTrackGroups) {
            this.rendererTrackTypes = rendererTrackTypes;
            this.rendererTrackGroups = rendererTrackGroups;
            this.rendererFormatSupports = rendererFormatSupports;
            this.rendererMixedMimeTypeAdaptiveSupports = rendererMixedMimeTypeAdaptiveSupports;
            this.unmappedTrackGroups = unmappedTrackGroups;
            this.rendererCount = rendererTrackTypes.length;
            this.length = this.rendererCount;
        }
        
        public int getAdaptiveSupport(final int n, final int n2, final boolean b) {
            final int length = this.rendererTrackGroups[n].get(n2).length;
            final int[] original = new int[length];
            int i = 0;
            int newLength = 0;
            while (i < length) {
                final int trackSupport = this.getTrackSupport(n, n2, i);
                int n3 = 0;
                Label_0081: {
                    if (trackSupport != 4) {
                        n3 = newLength;
                        if (!b) {
                            break Label_0081;
                        }
                        n3 = newLength;
                        if (trackSupport != 3) {
                            break Label_0081;
                        }
                    }
                    original[newLength] = i;
                    n3 = newLength + 1;
                }
                ++i;
                newLength = n3;
            }
            return this.getAdaptiveSupport(n, n2, Arrays.copyOf(original, newLength));
        }
        
        public int getAdaptiveSupport(final int n, int min, final int[] array) {
            int i = 0;
            Object o = null;
            boolean b = false;
            int n2 = 0;
            int min2 = 16;
            while (i < array.length) {
                final String sampleMimeType = this.rendererTrackGroups[n].get(min).getFormat(array[i]).sampleMimeType;
                if (n2 == 0) {
                    o = sampleMimeType;
                }
                else {
                    b |= (Util.areEqual(o, sampleMimeType) ^ true);
                }
                min2 = Math.min(min2, this.rendererFormatSupports[n][min][i] & 0x18);
                ++i;
                ++n2;
            }
            min = min2;
            if (b) {
                min = Math.min(min2, this.rendererMixedMimeTypeAdaptiveSupports[n]);
            }
            return min;
        }
        
        public int getRendererCount() {
            return this.rendererCount;
        }
        
        public int getRendererType(final int n) {
            return this.rendererTrackTypes[n];
        }
        
        public TrackGroupArray getTrackGroups(final int n) {
            return this.rendererTrackGroups[n];
        }
        
        public int getTrackSupport(final int n, final int n2, final int n3) {
            return this.rendererFormatSupports[n][n2][n3] & 0x7;
        }
        
        public TrackGroupArray getUnmappedTrackGroups() {
            return this.unmappedTrackGroups;
        }
    }
}
