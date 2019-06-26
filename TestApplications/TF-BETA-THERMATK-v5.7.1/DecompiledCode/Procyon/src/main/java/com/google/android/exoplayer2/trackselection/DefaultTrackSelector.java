// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import android.os.Parcel;
import java.util.Map;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import com.google.android.exoplayer2.ExoPlaybackException;
import android.util.Pair;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.RendererConfiguration;
import java.util.ArrayList;
import android.graphics.Point;
import com.google.android.exoplayer2.util.Assertions;
import java.util.HashSet;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.Format;
import java.util.List;
import com.google.android.exoplayer2.source.TrackGroup;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultTrackSelector extends MappingTrackSelector
{
    private static final int[] NO_TRACKS;
    private final AtomicReference<Parameters> parametersReference;
    private final TrackSelection.Factory trackSelectionFactory;
    
    static {
        NO_TRACKS = new int[0];
    }
    
    public DefaultTrackSelector(final TrackSelection.Factory trackSelectionFactory) {
        this.trackSelectionFactory = trackSelectionFactory;
        this.parametersReference = new AtomicReference<Parameters>(Parameters.DEFAULT);
    }
    
    private static int compareFormatValues(int n, final int n2) {
        final int n3 = -1;
        if (n == -1) {
            n = n3;
            if (n2 == -1) {
                n = 0;
            }
        }
        else if (n2 == -1) {
            n = 1;
        }
        else {
            n -= n2;
        }
        return n;
    }
    
    private static int compareInts(int n, final int n2) {
        if (n > n2) {
            n = 1;
        }
        else if (n2 > n) {
            n = -1;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    private static void filterAdaptiveVideoTrackCountForMimeType(final TrackGroup trackGroup, final int[] array, final int n, final String s, final int n2, final int n3, final int n4, final int n5, final List<Integer> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final int intValue = list.get(i);
            if (!isSupportedAdaptiveVideoTrack(trackGroup.getFormat(intValue), s, array[intValue], n, n2, n3, n4, n5)) {
                list.remove(i);
            }
        }
    }
    
    protected static boolean formatHasLanguage(final Format format, final String s) {
        return s != null && TextUtils.equals((CharSequence)s, (CharSequence)Util.normalizeLanguageCode(format.language));
    }
    
    protected static boolean formatHasNoLanguage(final Format format) {
        return TextUtils.isEmpty((CharSequence)format.language) || formatHasLanguage(format, "und");
    }
    
    private static int getAdaptiveAudioTrackCount(final TrackGroup trackGroup, final int[] array, final AudioConfigurationTuple audioConfigurationTuple, final boolean b, final boolean b2) {
        int i = 0;
        int n = 0;
        while (i < trackGroup.length) {
            int n2 = n;
            if (isSupportedAdaptiveAudioTrack(trackGroup.getFormat(i), array[i], audioConfigurationTuple, b, b2)) {
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    private static int[] getAdaptiveAudioTracks(final TrackGroup trackGroup, final int[] array, final boolean b, final boolean b2) {
        final HashSet<AudioConfigurationTuple> set = new HashSet<AudioConfigurationTuple>();
        final int n = 0;
        AudioConfigurationTuple audioConfigurationTuple = null;
        int i = 0;
        int n2 = 0;
        while (i < trackGroup.length) {
            final Format format = trackGroup.getFormat(i);
            final AudioConfigurationTuple e = new AudioConfigurationTuple(format.channelCount, format.sampleRate, format.sampleMimeType);
            int n3 = n2;
            AudioConfigurationTuple audioConfigurationTuple2 = audioConfigurationTuple;
            if (set.add(e)) {
                final int adaptiveAudioTrackCount = getAdaptiveAudioTrackCount(trackGroup, array, e, b, b2);
                n3 = n2;
                audioConfigurationTuple2 = audioConfigurationTuple;
                if (adaptiveAudioTrackCount > n2) {
                    n3 = adaptiveAudioTrackCount;
                    audioConfigurationTuple2 = e;
                }
            }
            ++i;
            n2 = n3;
            audioConfigurationTuple = audioConfigurationTuple2;
        }
        if (n2 > 1) {
            final int[] array2 = new int[n2];
            int n4 = 0;
            int n6;
            for (int j = n; j < trackGroup.length; ++j, n4 = n6) {
                final Format format2 = trackGroup.getFormat(j);
                final int n5 = array[j];
                Assertions.checkNotNull(audioConfigurationTuple);
                n6 = n4;
                if (isSupportedAdaptiveAudioTrack(format2, n5, audioConfigurationTuple, b, b2)) {
                    array2[n4] = j;
                    n6 = n4 + 1;
                }
            }
            return array2;
        }
        return DefaultTrackSelector.NO_TRACKS;
    }
    
    private static int getAdaptiveVideoTrackCountForMimeType(final TrackGroup trackGroup, final int[] array, final int n, final String s, final int n2, final int n3, final int n4, final int n5, final List<Integer> list) {
        int i = 0;
        int n6 = 0;
        while (i < list.size()) {
            final int intValue = list.get(i);
            int n7 = n6;
            if (isSupportedAdaptiveVideoTrack(trackGroup.getFormat(intValue), s, array[intValue], n, n2, n3, n4, n5)) {
                n7 = n6 + 1;
            }
            ++i;
            n6 = n7;
        }
        return n6;
    }
    
    private static int[] getAdaptiveVideoTracksForGroup(final TrackGroup trackGroup, final int[] array, final boolean b, final int n, final int n2, final int n3, final int n4, final int n5, int i, int n6, final boolean b2) {
        if (trackGroup.length < 2) {
            return DefaultTrackSelector.NO_TRACKS;
        }
        final List<Integer> viewportFilteredTrackIndices = getViewportFilteredTrackIndices(trackGroup, i, n6, b2);
        if (viewportFilteredTrackIndices.size() < 2) {
            return DefaultTrackSelector.NO_TRACKS;
        }
        String s;
        if (!b) {
            final HashSet<String> set = new HashSet<String>();
            s = null;
            i = 0;
            n6 = 0;
            while (i < viewportFilteredTrackIndices.size()) {
                final String sampleMimeType = trackGroup.getFormat(viewportFilteredTrackIndices.get(i)).sampleMimeType;
                String s2 = s;
                int n7 = n6;
                if (set.add(sampleMimeType)) {
                    final int adaptiveVideoTrackCountForMimeType = getAdaptiveVideoTrackCountForMimeType(trackGroup, array, n, sampleMimeType, n2, n3, n4, n5, viewportFilteredTrackIndices);
                    s2 = s;
                    if (adaptiveVideoTrackCountForMimeType > (n7 = n6)) {
                        n7 = adaptiveVideoTrackCountForMimeType;
                        s2 = sampleMimeType;
                    }
                }
                ++i;
                s = s2;
                n6 = n7;
            }
        }
        else {
            s = null;
        }
        filterAdaptiveVideoTrackCountForMimeType(trackGroup, array, n, s, n2, n3, n4, n5, viewportFilteredTrackIndices);
        int[] array2;
        if (viewportFilteredTrackIndices.size() < 2) {
            array2 = DefaultTrackSelector.NO_TRACKS;
        }
        else {
            array2 = Util.toArray(viewportFilteredTrackIndices);
        }
        return array2;
    }
    
    private static Point getMaxVideoSizeInViewport(final boolean b, int n, int n2, final int n3, final int n4) {
        Label_0051: {
            if (b) {
                int n5 = true ? 1 : 0;
                final boolean b2 = n3 > n4;
                if (n <= n2) {
                    n5 = (false ? 1 : 0);
                }
                if ((b2 ? 1 : 0) != n5) {
                    break Label_0051;
                }
            }
            final int n6 = n2;
            n2 = n;
            n = n6;
        }
        final int n7 = n3 * n;
        final int n8 = n4 * n2;
        if (n7 >= n8) {
            return new Point(n2, Util.ceilDivide(n8, n3));
        }
        return new Point(Util.ceilDivide(n7, n4), n);
    }
    
    private static List<Integer> getViewportFilteredTrackIndices(final TrackGroup trackGroup, int i, int pixelCount, final boolean b) {
        final ArrayList<Integer> list = new ArrayList<Integer>(trackGroup.length);
        int j = 0;
        for (int k = 0; k < trackGroup.length; ++k) {
            list.add(k);
        }
        if (i != Integer.MAX_VALUE) {
            if (pixelCount != Integer.MAX_VALUE) {
                int n = Integer.MAX_VALUE;
                while (j < trackGroup.length) {
                    final Format format = trackGroup.getFormat(j);
                    final int width = format.width;
                    int n2 = n;
                    if (width > 0) {
                        final int height = format.height;
                        n2 = n;
                        if (height > 0) {
                            final Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(b, i, pixelCount, width, height);
                            final int width2 = format.width;
                            final int height2 = format.height;
                            final int n3 = width2 * height2;
                            n2 = n;
                            if (width2 >= (int)(maxVideoSizeInViewport.x * 0.98f)) {
                                n2 = n;
                                if (height2 >= (int)(maxVideoSizeInViewport.y * 0.98f) && n3 < (n2 = n)) {
                                    n2 = n3;
                                }
                            }
                        }
                    }
                    ++j;
                    n = n2;
                }
                if (n != Integer.MAX_VALUE) {
                    for (i = list.size() - 1; i >= 0; --i) {
                        pixelCount = trackGroup.getFormat(list.get(i)).getPixelCount();
                        if (pixelCount == -1 || pixelCount > n) {
                            list.remove(i);
                        }
                    }
                }
            }
        }
        return list;
    }
    
    protected static boolean isSupported(int n, final boolean b) {
        n &= 0x7;
        return n == 4 || (b && n == 3);
    }
    
    private static boolean isSupportedAdaptiveAudioTrack(final Format format, int n, final AudioConfigurationTuple audioConfigurationTuple, final boolean b, final boolean b2) {
        boolean b4;
        final boolean b3 = b4 = false;
        if (isSupported(n, false)) {
            n = format.channelCount;
            b4 = b3;
            if (n != -1) {
                b4 = b3;
                if (n == audioConfigurationTuple.channelCount) {
                    if (!b) {
                        final String sampleMimeType = format.sampleMimeType;
                        b4 = b3;
                        if (sampleMimeType == null) {
                            return b4;
                        }
                        b4 = b3;
                        if (!TextUtils.equals((CharSequence)sampleMimeType, (CharSequence)audioConfigurationTuple.mimeType)) {
                            return b4;
                        }
                    }
                    if (!b2) {
                        n = format.sampleRate;
                        b4 = b3;
                        if (n == -1) {
                            return b4;
                        }
                        b4 = b3;
                        if (n != audioConfigurationTuple.sampleRate) {
                            return b4;
                        }
                    }
                    b4 = true;
                }
            }
        }
        return b4;
    }
    
    private static boolean isSupportedAdaptiveVideoTrack(final Format format, final String s, int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        boolean b2;
        final boolean b = b2 = false;
        if (isSupported(n, false)) {
            b2 = b;
            if ((n & n2) != 0x0) {
                if (s != null) {
                    b2 = b;
                    if (!Util.areEqual(format.sampleMimeType, s)) {
                        return b2;
                    }
                }
                n = format.width;
                if (n != -1) {
                    b2 = b;
                    if (n > n3) {
                        return b2;
                    }
                }
                n = format.height;
                if (n != -1) {
                    b2 = b;
                    if (n > n4) {
                        return b2;
                    }
                }
                final float frameRate = format.frameRate;
                if (frameRate != -1.0f) {
                    b2 = b;
                    if (frameRate > n5) {
                        return b2;
                    }
                }
                n = format.bitrate;
                if (n != -1) {
                    b2 = b;
                    if (n > n6) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    private static void maybeConfigureRenderersForTunneling(final MappedTrackInfo mappedTrackInfo, final int[][][] array, final RendererConfiguration[] array2, final TrackSelection[] array3, final int n) {
        if (n == 0) {
            return;
        }
        final boolean b = false;
        int i = 0;
        int n2 = -1;
        int n3 = -1;
        while (true) {
            while (i < mappedTrackInfo.getRendererCount()) {
                final int rendererType = mappedTrackInfo.getRendererType(i);
                final TrackSelection trackSelection = array3[i];
                int n4 = 0;
                int n5 = 0;
                Label_0146: {
                    if (rendererType != 1) {
                        n4 = n2;
                        n5 = n3;
                        if (rendererType != 2) {
                            break Label_0146;
                        }
                    }
                    n4 = n2;
                    n5 = n3;
                    if (trackSelection != null) {
                        n4 = n2;
                        n5 = n3;
                        if (rendererSupportsTunneling(array[i], mappedTrackInfo.getTrackGroups(i), trackSelection)) {
                            if (rendererType == 1) {
                                if (n3 == -1) {
                                    n5 = i;
                                    n4 = n2;
                                    break Label_0146;
                                }
                            }
                            else if (n2 == -1) {
                                n4 = i;
                                n5 = n3;
                                break Label_0146;
                            }
                            final int n6 = 0;
                            int n7 = b ? 1 : 0;
                            if (n3 != -1) {
                                n7 = (b ? 1 : 0);
                                if (n2 != -1) {
                                    n7 = 1;
                                }
                            }
                            if ((n6 & n7) != 0x0) {
                                array2[n2] = (array2[n3] = new RendererConfiguration(n));
                            }
                            return;
                        }
                    }
                }
                ++i;
                n2 = n4;
                n3 = n5;
            }
            final int n6 = 1;
            continue;
        }
    }
    
    private static boolean rendererSupportsTunneling(final int[][] array, final TrackGroupArray trackGroupArray, final TrackSelection trackSelection) {
        if (trackSelection == null) {
            return false;
        }
        final int index = trackGroupArray.indexOf(trackSelection.getTrackGroup());
        for (int i = 0; i < trackSelection.length(); ++i) {
            if ((array[index][trackSelection.getIndexInTrackGroup(i)] & 0x20) != 0x20) {
                return false;
            }
        }
        return true;
    }
    
    private static TrackSelection.Definition selectAdaptiveVideoTrack(final TrackGroupArray trackGroupArray, final int[][] array, int i, final Parameters parameters) {
        int n;
        if (parameters.allowVideoNonSeamlessAdaptiveness) {
            n = 24;
        }
        else {
            n = 16;
        }
        final boolean b = parameters.allowVideoMixedMimeTypeAdaptiveness && (i & n) != 0x0;
        TrackGroup value;
        int[] adaptiveVideoTracksForGroup;
        for (i = 0; i < trackGroupArray.length; ++i) {
            value = trackGroupArray.get(i);
            adaptiveVideoTracksForGroup = getAdaptiveVideoTracksForGroup(value, array[i], b, n, parameters.maxVideoWidth, parameters.maxVideoHeight, parameters.maxVideoFrameRate, parameters.maxVideoBitrate, parameters.viewportWidth, parameters.viewportHeight, parameters.viewportOrientationMayChange);
            if (adaptiveVideoTracksForGroup.length > 0) {
                return new TrackSelection.Definition(value, adaptiveVideoTracksForGroup);
            }
        }
        return null;
    }
    
    private static TrackSelection.Definition selectFixedVideoTrack(final TrackGroupArray trackGroupArray, final int[][] array, final Parameters parameters) {
        int i = 0;
        TrackGroup trackGroup = null;
        int n = 0;
        int n2 = 0;
        int bitrate = -1;
        int n3 = -1;
        while (i < trackGroupArray.length) {
            final TrackGroup value = trackGroupArray.get(i);
            final List<Integer> viewportFilteredTrackIndices = getViewportFilteredTrackIndices(value, parameters.viewportWidth, parameters.viewportHeight, parameters.viewportOrientationMayChange);
            final int[] array2 = array[i];
            final int n4 = n2;
            final int n5 = 0;
            int pixelCount = n3;
            int n6 = n4;
            int n7 = n;
            for (int j = n5; j < value.length; ++j) {
                if (isSupported(array2[j], parameters.exceedRendererCapabilitiesIfNecessary)) {
                    final Format format = value.getFormat(j);
                    boolean b = false;
                    Label_0234: {
                        if (viewportFilteredTrackIndices.contains(j)) {
                            final int width = format.width;
                            if (width == -1 || width <= parameters.maxVideoWidth) {
                                final int height = format.height;
                                if (height == -1 || height <= parameters.maxVideoHeight) {
                                    final float frameRate = format.frameRate;
                                    if (frameRate == -1.0f || frameRate <= parameters.maxVideoFrameRate) {
                                        final int bitrate2 = format.bitrate;
                                        if (bitrate2 == -1 || bitrate2 <= parameters.maxVideoBitrate) {
                                            b = true;
                                            break Label_0234;
                                        }
                                    }
                                }
                            }
                        }
                        b = false;
                    }
                    if (b || parameters.exceedVideoConstraintsIfNecessary) {
                        int n8;
                        if (b) {
                            n8 = 2;
                        }
                        else {
                            n8 = 1;
                        }
                        final boolean supported = isSupported(array2[j], false);
                        int n9 = n8;
                        if (supported) {
                            n9 = n8 + 1000;
                        }
                        boolean b2 = n9 > n6;
                        Label_0419: {
                            if (n9 == n6) {
                                final int compareFormatValues = compareFormatValues(format.bitrate, bitrate);
                                Label_0349: {
                                    if (parameters.forceLowestBitrate && compareFormatValues != 0) {
                                        if (compareFormatValues >= 0) {
                                            break Label_0349;
                                        }
                                    }
                                    else {
                                        final int pixelCount2 = format.getPixelCount();
                                        int n10;
                                        if (pixelCount2 != pixelCount) {
                                            n10 = compareFormatValues(pixelCount2, pixelCount);
                                        }
                                        else {
                                            n10 = compareFormatValues(format.bitrate, bitrate);
                                        }
                                        if ((!supported || !b) ? (n10 >= 0) : (n10 <= 0)) {
                                            break Label_0349;
                                        }
                                    }
                                    b2 = true;
                                    break Label_0419;
                                }
                                b2 = false;
                            }
                        }
                        if (b2) {
                            bitrate = format.bitrate;
                            pixelCount = format.getPixelCount();
                            n7 = j;
                            trackGroup = value;
                            n6 = n9;
                        }
                    }
                }
            }
            ++i;
            n = n7;
            final int n11 = pixelCount;
            n2 = n6;
            n3 = n11;
        }
        Object o;
        if (trackGroup == null) {
            o = null;
        }
        else {
            o = new TrackSelection.Definition(trackGroup, new int[] { n });
        }
        return (TrackSelection.Definition)o;
    }
    
    protected TrackSelection.Definition[] selectAllTracks(final MappedTrackInfo mappedTrackInfo, final int[][][] array, final int[] array2, final Parameters parameters) throws ExoPlaybackException {
        final int rendererCount = mappedTrackInfo.getRendererCount();
        final TrackSelection.Definition[] array3 = new TrackSelection.Definition[rendererCount];
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (true) {
            final int n4 = 1;
            if (n2 >= rendererCount) {
                break;
            }
            int n5 = n;
            int n6 = n3;
            if (2 == mappedTrackInfo.getRendererType(n2)) {
                if ((n5 = n) == 0) {
                    array3[n2] = this.selectVideoTrack(mappedTrackInfo.getTrackGroups(n2), array[n2], array2[n2], parameters, true);
                    if (array3[n2] != null) {
                        n5 = 1;
                    }
                    else {
                        n5 = 0;
                    }
                }
                int n7;
                if (mappedTrackInfo.getTrackGroups(n2).length > 0) {
                    n7 = n4;
                }
                else {
                    n7 = 0;
                }
                n6 = (n3 | n7);
            }
            ++n2;
            n = n5;
            n3 = n6;
        }
        TrackSelection.Definition definition = null;
        AudioTrackScore audioTrackScore = null;
        int n8 = -1;
        int n9 = -1;
        int intValue = Integer.MIN_VALUE;
        AudioTrackScore audioTrackScore2;
        TrackSelection.Definition definition3;
        AudioTrackScore audioTrackScore5;
        for (int i = 0; i < rendererCount; ++i, audioTrackScore5 = audioTrackScore2, definition = definition3, audioTrackScore = audioTrackScore5) {
            final int rendererType = mappedTrackInfo.getRendererType(i);
            if (rendererType != 1) {
                if (rendererType != 2) {
                    if (rendererType != 3) {
                        array3[i] = this.selectOtherTrack(rendererType, mappedTrackInfo.getTrackGroups(i), array[i], parameters);
                    }
                    else {
                        final Pair<TrackSelection.Definition, Integer> selectTextTrack = this.selectTextTrack(mappedTrackInfo.getTrackGroups(i), array[i], parameters);
                        if (selectTextTrack != null && (int)selectTextTrack.second > intValue) {
                            if (n9 != -1) {
                                array3[n9] = definition;
                            }
                            array3[i] = (TrackSelection.Definition)selectTextTrack.first;
                            intValue = (int)selectTextTrack.second;
                            n9 = i;
                            final TrackSelection.Definition definition2 = definition;
                            audioTrackScore2 = audioTrackScore;
                            definition3 = definition2;
                            continue;
                        }
                    }
                }
            }
            else {
                final TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                final int[][] array4 = array[i];
                final int n10 = array2[i];
                final boolean b = n3 == 0;
                final int n11 = n8;
                final AudioTrackScore audioTrackScore3 = audioTrackScore;
                final int n12 = i;
                final Pair<TrackSelection.Definition, AudioTrackScore> selectAudioTrack = this.selectAudioTrack(trackGroups, array4, n10, parameters, b);
                if (selectAudioTrack != null && (audioTrackScore3 == null || ((AudioTrackScore)selectAudioTrack.second).compareTo(audioTrackScore3) > 0)) {
                    if (n11 != -1) {
                        array3[n11] = null;
                    }
                    definition3 = null;
                    array3[n12] = (TrackSelection.Definition)selectAudioTrack.first;
                    audioTrackScore2 = (AudioTrackScore)selectAudioTrack.second;
                    n8 = n12;
                    continue;
                }
                definition = null;
            }
            final AudioTrackScore audioTrackScore4 = audioTrackScore;
            definition3 = definition;
            audioTrackScore2 = audioTrackScore4;
        }
        return array3;
    }
    
    protected Pair<TrackSelection.Definition, AudioTrackScore> selectAudioTrack(final TrackGroupArray trackGroupArray, final int[][] array, int i, final Parameters parameters, final boolean b) throws ExoPlaybackException {
        final Object o = null;
        AudioTrackScore audioTrackScore = null;
        i = 0;
        int n = -1;
        int n2 = -1;
        while (i < trackGroupArray.length) {
            final TrackGroup value = trackGroupArray.get(i);
            final int[] array2 = array[i];
            final int n3 = n;
            final int n4 = 0;
            int n5 = n2;
            int n6 = n3;
            int n7;
            int n8;
            AudioTrackScore audioTrackScore2;
            for (int j = n4; j < value.length; ++j, n6 = n7, n5 = n8, audioTrackScore = audioTrackScore2) {
                n7 = n6;
                n8 = n5;
                audioTrackScore2 = audioTrackScore;
                if (isSupported(array2[j], parameters.exceedRendererCapabilitiesIfNecessary)) {
                    final AudioTrackScore audioTrackScore3 = new AudioTrackScore(value.getFormat(j), parameters, array2[j]);
                    if (!audioTrackScore3.isWithinConstraints && !parameters.exceedAudioConstraintsIfNecessary) {
                        n7 = n6;
                        n8 = n5;
                        audioTrackScore2 = audioTrackScore;
                    }
                    else {
                        if (audioTrackScore != null) {
                            n7 = n6;
                            n8 = n5;
                            audioTrackScore2 = audioTrackScore;
                            if (audioTrackScore3.compareTo(audioTrackScore) <= 0) {
                                continue;
                            }
                        }
                        n7 = i;
                        n8 = j;
                        audioTrackScore2 = audioTrackScore3;
                    }
                }
            }
            ++i;
            final int n9 = n6;
            n2 = n5;
            n = n9;
        }
        if (n == -1) {
            return null;
        }
        final TrackGroup value2 = trackGroupArray.get(n);
        Object o2 = o;
        if (!parameters.forceHighestSupportedBitrate) {
            o2 = o;
            if (!parameters.forceLowestBitrate) {
                o2 = o;
                if (b) {
                    final int[] adaptiveAudioTracks = getAdaptiveAudioTracks(value2, array[n], parameters.allowAudioMixedMimeTypeAdaptiveness, parameters.allowAudioMixedSampleRateAdaptiveness);
                    o2 = o;
                    if (adaptiveAudioTracks.length > 0) {
                        o2 = new TrackSelection.Definition(value2, adaptiveAudioTracks);
                    }
                }
            }
        }
        Object o3;
        if ((o3 = o2) == null) {
            o3 = new TrackSelection.Definition(value2, new int[] { n2 });
        }
        Assertions.checkNotNull(audioTrackScore);
        return (Pair<TrackSelection.Definition, AudioTrackScore>)Pair.create(o3, (Object)audioTrackScore);
    }
    
    protected TrackSelection.Definition selectOtherTrack(int n, final TrackGroupArray trackGroupArray, final int[][] array, final Parameters parameters) throws ExoPlaybackException {
        final TrackSelection.Definition definition = null;
        TrackGroup trackGroup = null;
        int i = 0;
        n = 0;
        int n2 = 0;
        while (i < trackGroupArray.length) {
            final TrackGroup value = trackGroupArray.get(i);
            final int[] array2 = array[i];
            final int n3 = n;
            final int n4 = 0;
            n = n2;
            int n5 = n3;
            TrackGroup trackGroup2;
            int n6;
            int n7;
            for (int j = n4; j < value.length; ++j, trackGroup = trackGroup2, n5 = n6, n = n7) {
                trackGroup2 = trackGroup;
                n6 = n5;
                n7 = n;
                if (isSupported(array2[j], parameters.exceedRendererCapabilitiesIfNecessary)) {
                    int n8;
                    if ((value.getFormat(j).selectionFlags & 0x1) != 0x0) {
                        n8 = 2;
                    }
                    else {
                        n8 = 1;
                    }
                    int n9 = n8;
                    if (isSupported(array2[j], false)) {
                        n9 = n8 + 1000;
                    }
                    trackGroup2 = trackGroup;
                    n6 = n5;
                    if (n9 > (n7 = n)) {
                        n6 = j;
                        trackGroup2 = value;
                        n7 = n9;
                    }
                }
            }
            ++i;
            final int n10 = n5;
            n2 = n;
            n = n10;
        }
        Object o;
        if (trackGroup == null) {
            o = definition;
        }
        else {
            o = new TrackSelection.Definition(trackGroup, new int[] { n });
        }
        return (TrackSelection.Definition)o;
    }
    
    protected Pair<TrackSelection.Definition, Integer> selectTextTrack(final TrackGroupArray trackGroupArray, final int[][] array, final Parameters parameters) throws ExoPlaybackException {
        int i = 0;
        TrackGroup trackGroup = null;
        int n = 0;
        int j = 0;
        while (i < trackGroupArray.length) {
            final TrackGroup value = trackGroupArray.get(i);
            final int[] array2 = array[i];
            int k = 0;
            int n2 = n;
            while (k < value.length) {
                TrackGroup trackGroup2 = trackGroup;
                int n3 = n2;
                int n4 = j;
                Label_0309: {
                    if (isSupported(array2[k], parameters.exceedRendererCapabilitiesIfNecessary)) {
                        final Format format = value.getFormat(k);
                        final int n5 = format.selectionFlags & ~parameters.disabledTextTrackSelectionFlags;
                        final boolean b = (n5 & 0x1) != 0x0;
                        final boolean b2 = (n5 & 0x2) != 0x0;
                        final int formatHasLanguage = formatHasLanguage(format, parameters.preferredTextLanguage) ? 1 : 0;
                        int n6;
                        if (formatHasLanguage == 0 && (!parameters.selectUndeterminedTextLanguage || !formatHasNoLanguage(format))) {
                            if (b) {
                                n6 = 3;
                            }
                            else {
                                trackGroup2 = trackGroup;
                                n3 = n2;
                                n4 = j;
                                if (!b2) {
                                    break Label_0309;
                                }
                                if (formatHasLanguage(format, parameters.preferredAudioLanguage)) {
                                    n6 = 2;
                                }
                                else {
                                    n6 = 1;
                                }
                            }
                        }
                        else {
                            int n7;
                            if (b) {
                                n7 = 8;
                            }
                            else if (!b2) {
                                n7 = 6;
                            }
                            else {
                                n7 = 4;
                            }
                            n6 = n7 + formatHasLanguage;
                        }
                        int n8 = n6;
                        if (isSupported(array2[k], false)) {
                            n8 = n6 + 1000;
                        }
                        trackGroup2 = trackGroup;
                        n3 = n2;
                        if (n8 > (n4 = j)) {
                            n4 = n8;
                            n3 = k;
                            trackGroup2 = value;
                        }
                    }
                }
                ++k;
                trackGroup = trackGroup2;
                n2 = n3;
                j = n4;
            }
            ++i;
            n = n2;
        }
        Pair create;
        if (trackGroup == null) {
            create = null;
        }
        else {
            create = Pair.create((Object)new TrackSelection.Definition(trackGroup, new int[] { n }), (Object)j);
        }
        return (Pair<TrackSelection.Definition, Integer>)create;
    }
    
    @Override
    protected final Pair<RendererConfiguration[], TrackSelection[]> selectTracks(final MappedTrackInfo mappedTrackInfo, final int[][][] array, final int[] array2) throws ExoPlaybackException {
        final Parameters parameters = this.parametersReference.get();
        final int rendererCount = mappedTrackInfo.getRendererCount();
        final TrackSelection.Definition[] selectAllTracks = this.selectAllTracks(mappedTrackInfo, array, array2, parameters);
        int n = 0;
        while (true) {
            TrackSelection.Definition definition = null;
            if (n >= rendererCount) {
                break;
            }
            if (parameters.getRendererDisabled(n)) {
                selectAllTracks[n] = null;
            }
            else {
                final TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(n);
                if (parameters.hasSelectionOverride(n, trackGroups)) {
                    final SelectionOverride selectionOverride = parameters.getSelectionOverride(n, trackGroups);
                    if (selectionOverride != null) {
                        definition = new TrackSelection.Definition(trackGroups.get(selectionOverride.groupIndex), selectionOverride.tracks);
                    }
                    selectAllTracks[n] = definition;
                }
            }
            ++n;
        }
        final TrackSelection[] trackSelections = this.trackSelectionFactory.createTrackSelections(selectAllTracks, this.getBandwidthMeter());
        final RendererConfiguration[] array3 = new RendererConfiguration[rendererCount];
        for (int i = 0; i < rendererCount; ++i) {
            RendererConfiguration default1;
            if (!parameters.getRendererDisabled(i) && (mappedTrackInfo.getRendererType(i) == 6 || trackSelections[i] != null)) {
                default1 = RendererConfiguration.DEFAULT;
            }
            else {
                default1 = null;
            }
            array3[i] = default1;
        }
        maybeConfigureRenderersForTunneling(mappedTrackInfo, array, array3, trackSelections, parameters.tunnelingAudioSessionId);
        return (Pair<RendererConfiguration[], TrackSelection[]>)Pair.create((Object)array3, (Object)trackSelections);
    }
    
    protected TrackSelection.Definition selectVideoTrack(final TrackGroupArray trackGroupArray, final int[][] array, final int n, final Parameters parameters, final boolean b) throws ExoPlaybackException {
        TrackSelection.Definition selectAdaptiveVideoTrack;
        if (!parameters.forceHighestSupportedBitrate && !parameters.forceLowestBitrate && b) {
            selectAdaptiveVideoTrack = selectAdaptiveVideoTrack(trackGroupArray, array, n, parameters);
        }
        else {
            selectAdaptiveVideoTrack = null;
        }
        TrackSelection.Definition selectFixedVideoTrack = selectAdaptiveVideoTrack;
        if (selectAdaptiveVideoTrack == null) {
            selectFixedVideoTrack = selectFixedVideoTrack(trackGroupArray, array, parameters);
        }
        return selectFixedVideoTrack;
    }
    
    private static final class AudioConfigurationTuple
    {
        public final int channelCount;
        public final String mimeType;
        public final int sampleRate;
        
        public AudioConfigurationTuple(final int channelCount, final int sampleRate, final String mimeType) {
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.mimeType = mimeType;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && AudioConfigurationTuple.class == o.getClass()) {
                final AudioConfigurationTuple audioConfigurationTuple = (AudioConfigurationTuple)o;
                if (this.channelCount != audioConfigurationTuple.channelCount || this.sampleRate != audioConfigurationTuple.sampleRate || !TextUtils.equals((CharSequence)this.mimeType, (CharSequence)audioConfigurationTuple.mimeType)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            final int channelCount = this.channelCount;
            final int sampleRate = this.sampleRate;
            final String mimeType = this.mimeType;
            int hashCode;
            if (mimeType != null) {
                hashCode = mimeType.hashCode();
            }
            else {
                hashCode = 0;
            }
            return (channelCount * 31 + sampleRate) * 31 + hashCode;
        }
    }
    
    protected static final class AudioTrackScore implements Comparable<AudioTrackScore>
    {
        private final int bitrate;
        private final int channelCount;
        private final int defaultSelectionFlagScore;
        public final boolean isWithinConstraints;
        private final int matchLanguageScore;
        private final Parameters parameters;
        private final int sampleRate;
        private final int withinRendererCapabilitiesScore;
        
        public AudioTrackScore(final Format format, final Parameters parameters, int n) {
            this.parameters = parameters;
            final boolean b = false;
            this.withinRendererCapabilitiesScore = (DefaultTrackSelector.isSupported(n, false) ? 1 : 0);
            this.matchLanguageScore = (DefaultTrackSelector.formatHasLanguage(format, parameters.preferredAudioLanguage) ? 1 : 0);
            if ((format.selectionFlags & 0x1) != 0x0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.defaultSelectionFlagScore = n;
            this.channelCount = format.channelCount;
            this.sampleRate = format.sampleRate;
            n = format.bitrate;
            this.bitrate = n;
            boolean isWithinConstraints = false;
            Label_0122: {
                if (n != -1) {
                    isWithinConstraints = b;
                    if (n > parameters.maxAudioBitrate) {
                        break Label_0122;
                    }
                }
                n = format.channelCount;
                if (n != -1) {
                    isWithinConstraints = b;
                    if (n > parameters.maxAudioChannelCount) {
                        break Label_0122;
                    }
                }
                isWithinConstraints = true;
            }
            this.isWithinConstraints = isWithinConstraints;
        }
        
        @Override
        public int compareTo(final AudioTrackScore audioTrackScore) {
            final int withinRendererCapabilitiesScore = this.withinRendererCapabilitiesScore;
            final int withinRendererCapabilitiesScore2 = audioTrackScore.withinRendererCapabilitiesScore;
            if (withinRendererCapabilitiesScore != withinRendererCapabilitiesScore2) {
                return compareInts(withinRendererCapabilitiesScore, withinRendererCapabilitiesScore2);
            }
            final int matchLanguageScore = this.matchLanguageScore;
            final int matchLanguageScore2 = audioTrackScore.matchLanguageScore;
            if (matchLanguageScore != matchLanguageScore2) {
                return compareInts(matchLanguageScore, matchLanguageScore2);
            }
            final boolean isWithinConstraints = this.isWithinConstraints;
            final boolean isWithinConstraints2 = audioTrackScore.isWithinConstraints;
            int n = -1;
            if (isWithinConstraints != isWithinConstraints2) {
                if (isWithinConstraints) {
                    n = 1;
                }
                return n;
            }
            if (this.parameters.forceLowestBitrate) {
                final int access$400 = compareFormatValues(this.bitrate, audioTrackScore.bitrate);
                if (access$400 != 0) {
                    if (access$400 <= 0) {
                        n = 1;
                    }
                    return n;
                }
            }
            final int defaultSelectionFlagScore = this.defaultSelectionFlagScore;
            final int defaultSelectionFlagScore2 = audioTrackScore.defaultSelectionFlagScore;
            if (defaultSelectionFlagScore != defaultSelectionFlagScore2) {
                return compareInts(defaultSelectionFlagScore, defaultSelectionFlagScore2);
            }
            int n2 = n;
            if (this.isWithinConstraints) {
                n2 = n;
                if (this.withinRendererCapabilitiesScore == 1) {
                    n2 = 1;
                }
            }
            final int channelCount = this.channelCount;
            final int channelCount2 = audioTrackScore.channelCount;
            int n3;
            if (channelCount != channelCount2) {
                n3 = compareInts(channelCount, channelCount2);
            }
            else {
                final int sampleRate = this.sampleRate;
                final int sampleRate2 = audioTrackScore.sampleRate;
                if (sampleRate != sampleRate2) {
                    n3 = compareInts(sampleRate, sampleRate2);
                }
                else {
                    n3 = compareInts(this.bitrate, audioTrackScore.bitrate);
                }
            }
            return n2 * n3;
        }
    }
    
    public static final class Parameters implements Parcelable
    {
        public static final Parcelable$Creator<Parameters> CREATOR;
        public static final Parameters DEFAULT;
        public final boolean allowAudioMixedMimeTypeAdaptiveness;
        public final boolean allowAudioMixedSampleRateAdaptiveness;
        @Deprecated
        public final boolean allowMixedMimeAdaptiveness;
        @Deprecated
        public final boolean allowNonSeamlessAdaptiveness;
        public final boolean allowVideoMixedMimeTypeAdaptiveness;
        public final boolean allowVideoNonSeamlessAdaptiveness;
        public final int disabledTextTrackSelectionFlags;
        public final boolean exceedAudioConstraintsIfNecessary;
        public final boolean exceedRendererCapabilitiesIfNecessary;
        public final boolean exceedVideoConstraintsIfNecessary;
        public final boolean forceHighestSupportedBitrate;
        public final boolean forceLowestBitrate;
        public final int maxAudioBitrate;
        public final int maxAudioChannelCount;
        public final int maxVideoBitrate;
        public final int maxVideoFrameRate;
        public final int maxVideoHeight;
        public final int maxVideoWidth;
        public final String preferredAudioLanguage;
        public final String preferredTextLanguage;
        private final SparseBooleanArray rendererDisabledFlags;
        public final boolean selectUndeterminedTextLanguage;
        private final SparseArray<Map<TrackGroupArray, SelectionOverride>> selectionOverrides;
        public final int tunnelingAudioSessionId;
        public final int viewportHeight;
        public final boolean viewportOrientationMayChange;
        public final int viewportWidth;
        
        static {
            DEFAULT = new Parameters();
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<Parameters>() {
                public Parameters createFromParcel(final Parcel parcel) {
                    return new Parameters(parcel);
                }
                
                public Parameters[] newArray(final int n) {
                    return new Parameters[n];
                }
            };
        }
        
        private Parameters() {
            this(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true, Integer.MAX_VALUE, Integer.MAX_VALUE, true, null, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, false, null, false, 0, false, false, true, 0, (SparseArray<Map<TrackGroupArray, SelectionOverride>>)new SparseArray(), new SparseBooleanArray());
        }
        
        Parameters(final int maxVideoWidth, final int maxVideoHeight, final int maxVideoFrameRate, final int maxVideoBitrate, final boolean exceedVideoConstraintsIfNecessary, final boolean b, final boolean b2, final int viewportWidth, final int viewportHeight, final boolean viewportOrientationMayChange, final String s, final int maxAudioChannelCount, final int maxAudioBitrate, final boolean exceedAudioConstraintsIfNecessary, final boolean allowAudioMixedMimeTypeAdaptiveness, final boolean allowAudioMixedSampleRateAdaptiveness, final String s2, final boolean selectUndeterminedTextLanguage, final int disabledTextTrackSelectionFlags, final boolean forceLowestBitrate, final boolean forceHighestSupportedBitrate, final boolean exceedRendererCapabilitiesIfNecessary, final int tunnelingAudioSessionId, final SparseArray<Map<TrackGroupArray, SelectionOverride>> selectionOverrides, final SparseBooleanArray rendererDisabledFlags) {
            this.maxVideoWidth = maxVideoWidth;
            this.maxVideoHeight = maxVideoHeight;
            this.maxVideoFrameRate = maxVideoFrameRate;
            this.maxVideoBitrate = maxVideoBitrate;
            this.exceedVideoConstraintsIfNecessary = exceedVideoConstraintsIfNecessary;
            this.allowVideoMixedMimeTypeAdaptiveness = b;
            this.allowVideoNonSeamlessAdaptiveness = b2;
            this.viewportWidth = viewportWidth;
            this.viewportHeight = viewportHeight;
            this.viewportOrientationMayChange = viewportOrientationMayChange;
            this.preferredAudioLanguage = Util.normalizeLanguageCode(s);
            this.maxAudioChannelCount = maxAudioChannelCount;
            this.maxAudioBitrate = maxAudioBitrate;
            this.exceedAudioConstraintsIfNecessary = exceedAudioConstraintsIfNecessary;
            this.allowAudioMixedMimeTypeAdaptiveness = allowAudioMixedMimeTypeAdaptiveness;
            this.allowAudioMixedSampleRateAdaptiveness = allowAudioMixedSampleRateAdaptiveness;
            this.preferredTextLanguage = Util.normalizeLanguageCode(s2);
            this.selectUndeterminedTextLanguage = selectUndeterminedTextLanguage;
            this.disabledTextTrackSelectionFlags = disabledTextTrackSelectionFlags;
            this.forceLowestBitrate = forceLowestBitrate;
            this.forceHighestSupportedBitrate = forceHighestSupportedBitrate;
            this.exceedRendererCapabilitiesIfNecessary = exceedRendererCapabilitiesIfNecessary;
            this.tunnelingAudioSessionId = tunnelingAudioSessionId;
            this.selectionOverrides = selectionOverrides;
            this.rendererDisabledFlags = rendererDisabledFlags;
            this.allowMixedMimeAdaptiveness = b;
            this.allowNonSeamlessAdaptiveness = b2;
        }
        
        Parameters(final Parcel parcel) {
            this.maxVideoWidth = parcel.readInt();
            this.maxVideoHeight = parcel.readInt();
            this.maxVideoFrameRate = parcel.readInt();
            this.maxVideoBitrate = parcel.readInt();
            this.exceedVideoConstraintsIfNecessary = Util.readBoolean(parcel);
            this.allowVideoMixedMimeTypeAdaptiveness = Util.readBoolean(parcel);
            this.allowVideoNonSeamlessAdaptiveness = Util.readBoolean(parcel);
            this.viewportWidth = parcel.readInt();
            this.viewportHeight = parcel.readInt();
            this.viewportOrientationMayChange = Util.readBoolean(parcel);
            this.preferredAudioLanguage = parcel.readString();
            this.maxAudioChannelCount = parcel.readInt();
            this.maxAudioBitrate = parcel.readInt();
            this.exceedAudioConstraintsIfNecessary = Util.readBoolean(parcel);
            this.allowAudioMixedMimeTypeAdaptiveness = Util.readBoolean(parcel);
            this.allowAudioMixedSampleRateAdaptiveness = Util.readBoolean(parcel);
            this.preferredTextLanguage = parcel.readString();
            this.selectUndeterminedTextLanguage = Util.readBoolean(parcel);
            this.disabledTextTrackSelectionFlags = parcel.readInt();
            this.forceLowestBitrate = Util.readBoolean(parcel);
            this.forceHighestSupportedBitrate = Util.readBoolean(parcel);
            this.exceedRendererCapabilitiesIfNecessary = Util.readBoolean(parcel);
            this.tunnelingAudioSessionId = parcel.readInt();
            this.selectionOverrides = readSelectionOverrides(parcel);
            this.rendererDisabledFlags = parcel.readSparseBooleanArray();
            this.allowMixedMimeAdaptiveness = this.allowVideoMixedMimeTypeAdaptiveness;
            this.allowNonSeamlessAdaptiveness = this.allowVideoNonSeamlessAdaptiveness;
        }
        
        private static boolean areRendererDisabledFlagsEqual(final SparseBooleanArray sparseBooleanArray, final SparseBooleanArray sparseBooleanArray2) {
            final int size = sparseBooleanArray.size();
            if (sparseBooleanArray2.size() != size) {
                return false;
            }
            for (int i = 0; i < size; ++i) {
                if (sparseBooleanArray2.indexOfKey(sparseBooleanArray.keyAt(i)) < 0) {
                    return false;
                }
            }
            return true;
        }
        
        private static boolean areSelectionOverridesEqual(final SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray, final SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray2) {
            final int size = sparseArray.size();
            if (sparseArray2.size() != size) {
                return false;
            }
            for (int i = 0; i < size; ++i) {
                final int indexOfKey = sparseArray2.indexOfKey(sparseArray.keyAt(i));
                if (indexOfKey < 0 || !areSelectionOverridesEqual((Map<TrackGroupArray, SelectionOverride>)sparseArray.valueAt(i), (Map<TrackGroupArray, SelectionOverride>)sparseArray2.valueAt(indexOfKey))) {
                    return false;
                }
            }
            return true;
        }
        
        private static boolean areSelectionOverridesEqual(final Map<TrackGroupArray, SelectionOverride> map, final Map<TrackGroupArray, SelectionOverride> map2) {
            if (map2.size() != map.size()) {
                return false;
            }
            for (final Map.Entry<TrackGroupArray, SelectionOverride> entry : map.entrySet()) {
                final TrackGroupArray trackGroupArray = entry.getKey();
                if (!map2.containsKey(trackGroupArray) || !Util.areEqual(entry.getValue(), map2.get(trackGroupArray))) {
                    return false;
                }
            }
            return true;
        }
        
        private static SparseArray<Map<TrackGroupArray, SelectionOverride>> readSelectionOverrides(final Parcel parcel) {
            final int int1 = parcel.readInt();
            final SparseArray sparseArray = new SparseArray(int1);
            for (int i = 0; i < int1; ++i) {
                final int int2 = parcel.readInt();
                final int int3 = parcel.readInt();
                final HashMap hashMap = new HashMap<TrackGroupArray, SelectionOverride>(int3);
                for (int j = 0; j < int3; ++j) {
                    hashMap.put((TrackGroupArray)parcel.readParcelable(TrackGroupArray.class.getClassLoader()), (SelectionOverride)parcel.readParcelable(SelectionOverride.class.getClassLoader()));
                }
                sparseArray.put(int2, (Object)hashMap);
            }
            return (SparseArray<Map<TrackGroupArray, SelectionOverride>>)sparseArray;
        }
        
        private static void writeSelectionOverridesToParcel(final Parcel parcel, final SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray) {
            final int size = sparseArray.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; ++i) {
                final int key = sparseArray.keyAt(i);
                final Map map = (Map)sparseArray.valueAt(i);
                final int size2 = map.size();
                parcel.writeInt(key);
                parcel.writeInt(size2);
                for (final Map.Entry<Parcelable, V> entry : map.entrySet()) {
                    parcel.writeParcelable((Parcelable)entry.getKey(), 0);
                    parcel.writeParcelable((Parcelable)entry.getValue(), 0);
                }
            }
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && Parameters.class == o.getClass()) {
                final Parameters parameters = (Parameters)o;
                if (this.maxVideoWidth != parameters.maxVideoWidth || this.maxVideoHeight != parameters.maxVideoHeight || this.maxVideoFrameRate != parameters.maxVideoFrameRate || this.maxVideoBitrate != parameters.maxVideoBitrate || this.exceedVideoConstraintsIfNecessary != parameters.exceedVideoConstraintsIfNecessary || this.allowVideoMixedMimeTypeAdaptiveness != parameters.allowVideoMixedMimeTypeAdaptiveness || this.allowVideoNonSeamlessAdaptiveness != parameters.allowVideoNonSeamlessAdaptiveness || this.viewportOrientationMayChange != parameters.viewportOrientationMayChange || this.viewportWidth != parameters.viewportWidth || this.viewportHeight != parameters.viewportHeight || !TextUtils.equals((CharSequence)this.preferredAudioLanguage, (CharSequence)parameters.preferredAudioLanguage) || this.maxAudioChannelCount != parameters.maxAudioChannelCount || this.maxAudioBitrate != parameters.maxAudioBitrate || this.exceedAudioConstraintsIfNecessary != parameters.exceedAudioConstraintsIfNecessary || this.allowAudioMixedMimeTypeAdaptiveness != parameters.allowAudioMixedMimeTypeAdaptiveness || this.allowAudioMixedSampleRateAdaptiveness != parameters.allowAudioMixedSampleRateAdaptiveness || !TextUtils.equals((CharSequence)this.preferredTextLanguage, (CharSequence)parameters.preferredTextLanguage) || this.selectUndeterminedTextLanguage != parameters.selectUndeterminedTextLanguage || this.disabledTextTrackSelectionFlags != parameters.disabledTextTrackSelectionFlags || this.forceLowestBitrate != parameters.forceLowestBitrate || this.forceHighestSupportedBitrate != parameters.forceHighestSupportedBitrate || this.exceedRendererCapabilitiesIfNecessary != parameters.exceedRendererCapabilitiesIfNecessary || this.tunnelingAudioSessionId != parameters.tunnelingAudioSessionId || !areRendererDisabledFlagsEqual(this.rendererDisabledFlags, parameters.rendererDisabledFlags) || !areSelectionOverridesEqual(this.selectionOverrides, parameters.selectionOverrides)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        public final boolean getRendererDisabled(final int n) {
            return this.rendererDisabledFlags.get(n);
        }
        
        public final SelectionOverride getSelectionOverride(final int n, final TrackGroupArray trackGroupArray) {
            final Map map = (Map)this.selectionOverrides.get(n);
            SelectionOverride selectionOverride;
            if (map != null) {
                selectionOverride = map.get(trackGroupArray);
            }
            else {
                selectionOverride = null;
            }
            return selectionOverride;
        }
        
        public final boolean hasSelectionOverride(final int n, final TrackGroupArray trackGroupArray) {
            final Map map = (Map)this.selectionOverrides.get(n);
            return map != null && map.containsKey(trackGroupArray);
        }
        
        @Override
        public int hashCode() {
            final int maxVideoWidth = this.maxVideoWidth;
            final int maxVideoHeight = this.maxVideoHeight;
            final int maxVideoFrameRate = this.maxVideoFrameRate;
            final int maxVideoBitrate = this.maxVideoBitrate;
            final int exceedVideoConstraintsIfNecessary = this.exceedVideoConstraintsIfNecessary ? 1 : 0;
            final int allowVideoMixedMimeTypeAdaptiveness = this.allowVideoMixedMimeTypeAdaptiveness ? 1 : 0;
            final int allowVideoNonSeamlessAdaptiveness = this.allowVideoNonSeamlessAdaptiveness ? 1 : 0;
            final int viewportOrientationMayChange = this.viewportOrientationMayChange ? 1 : 0;
            final int viewportWidth = this.viewportWidth;
            final int viewportHeight = this.viewportHeight;
            final String preferredAudioLanguage = this.preferredAudioLanguage;
            int hashCode = 0;
            int hashCode2;
            if (preferredAudioLanguage == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = preferredAudioLanguage.hashCode();
            }
            final int maxAudioChannelCount = this.maxAudioChannelCount;
            final int maxAudioBitrate = this.maxAudioBitrate;
            final int exceedAudioConstraintsIfNecessary = this.exceedAudioConstraintsIfNecessary ? 1 : 0;
            final int allowAudioMixedMimeTypeAdaptiveness = this.allowAudioMixedMimeTypeAdaptiveness ? 1 : 0;
            final int allowAudioMixedSampleRateAdaptiveness = this.allowAudioMixedSampleRateAdaptiveness ? 1 : 0;
            final String preferredTextLanguage = this.preferredTextLanguage;
            if (preferredTextLanguage != null) {
                hashCode = preferredTextLanguage.hashCode();
            }
            return ((((((((((((((((((((((maxVideoWidth + 31) * 31 + maxVideoHeight) * 31 + maxVideoFrameRate) * 31 + maxVideoBitrate) * 31 + exceedVideoConstraintsIfNecessary) * 31 + allowVideoMixedMimeTypeAdaptiveness) * 31 + allowVideoNonSeamlessAdaptiveness) * 31 + viewportOrientationMayChange) * 31 + viewportWidth) * 31 + viewportHeight) * 31 + hashCode2) * 31 + maxAudioChannelCount) * 31 + maxAudioBitrate) * 31 + exceedAudioConstraintsIfNecessary) * 31 + allowAudioMixedMimeTypeAdaptiveness) * 31 + allowAudioMixedSampleRateAdaptiveness) * 31 + hashCode) * 31 + (this.selectUndeterminedTextLanguage ? 1 : 0)) * 31 + this.disabledTextTrackSelectionFlags) * 31 + (this.forceLowestBitrate ? 1 : 0)) * 31 + (this.forceHighestSupportedBitrate ? 1 : 0)) * 31 + (this.exceedRendererCapabilitiesIfNecessary ? 1 : 0)) * 31 + this.tunnelingAudioSessionId;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.maxVideoWidth);
            parcel.writeInt(this.maxVideoHeight);
            parcel.writeInt(this.maxVideoFrameRate);
            parcel.writeInt(this.maxVideoBitrate);
            Util.writeBoolean(parcel, this.exceedVideoConstraintsIfNecessary);
            Util.writeBoolean(parcel, this.allowVideoMixedMimeTypeAdaptiveness);
            Util.writeBoolean(parcel, this.allowVideoNonSeamlessAdaptiveness);
            parcel.writeInt(this.viewportWidth);
            parcel.writeInt(this.viewportHeight);
            Util.writeBoolean(parcel, this.viewportOrientationMayChange);
            parcel.writeString(this.preferredAudioLanguage);
            parcel.writeInt(this.maxAudioChannelCount);
            parcel.writeInt(this.maxAudioBitrate);
            Util.writeBoolean(parcel, this.exceedAudioConstraintsIfNecessary);
            Util.writeBoolean(parcel, this.allowAudioMixedMimeTypeAdaptiveness);
            Util.writeBoolean(parcel, this.allowAudioMixedSampleRateAdaptiveness);
            parcel.writeString(this.preferredTextLanguage);
            Util.writeBoolean(parcel, this.selectUndeterminedTextLanguage);
            parcel.writeInt(this.disabledTextTrackSelectionFlags);
            Util.writeBoolean(parcel, this.forceLowestBitrate);
            Util.writeBoolean(parcel, this.forceHighestSupportedBitrate);
            Util.writeBoolean(parcel, this.exceedRendererCapabilitiesIfNecessary);
            parcel.writeInt(this.tunnelingAudioSessionId);
            writeSelectionOverridesToParcel(parcel, this.selectionOverrides);
            parcel.writeSparseBooleanArray(this.rendererDisabledFlags);
        }
    }
    
    public static final class SelectionOverride implements Parcelable
    {
        public static final Parcelable$Creator<SelectionOverride> CREATOR;
        public final int groupIndex;
        public final int length;
        public final int[] tracks;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SelectionOverride>() {
                public SelectionOverride createFromParcel(final Parcel parcel) {
                    return new SelectionOverride(parcel);
                }
                
                public SelectionOverride[] newArray(final int n) {
                    return new SelectionOverride[n];
                }
            };
        }
        
        SelectionOverride(final Parcel parcel) {
            this.groupIndex = parcel.readInt();
            this.length = parcel.readByte();
            parcel.readIntArray(this.tracks = new int[this.length]);
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && SelectionOverride.class == o.getClass()) {
                final SelectionOverride selectionOverride = (SelectionOverride)o;
                if (this.groupIndex != selectionOverride.groupIndex || !Arrays.equals(this.tracks, selectionOverride.tracks)) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.groupIndex * 31 + Arrays.hashCode(this.tracks);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.groupIndex);
            parcel.writeInt(this.tracks.length);
            parcel.writeIntArray(this.tracks);
        }
    }
}
