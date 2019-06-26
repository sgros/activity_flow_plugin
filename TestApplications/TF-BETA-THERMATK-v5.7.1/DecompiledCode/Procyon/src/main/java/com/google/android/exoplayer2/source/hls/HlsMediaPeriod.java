// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import java.util.Arrays;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import java.io.IOException;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.Collections;
import java.util.List;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Collection;
import java.util.ArrayList;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.SampleStream;
import java.util.IdentityHashMap;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.MediaPeriod;

public final class HlsMediaPeriod implements MediaPeriod, HlsSampleStreamWrapper.Callback, PlaylistEventListener
{
    private final Allocator allocator;
    private final boolean allowChunklessPreparation;
    private MediaPeriod.Callback callback;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private HlsSampleStreamWrapper[] enabledSampleStreamWrappers;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final TransferListener mediaTransferListener;
    private boolean notifiedReadingStarted;
    private int pendingPrepareCount;
    private final HlsPlaylistTracker playlistTracker;
    private HlsSampleStreamWrapper[] sampleStreamWrappers;
    private final IdentityHashMap<SampleStream, Integer> streamWrapperIndices;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private TrackGroupArray trackGroups;
    
    public HlsMediaPeriod(final HlsExtractorFactory extractorFactory, final HlsPlaylistTracker playlistTracker, final HlsDataSourceFactory dataSourceFactory, final TransferListener mediaTransferListener, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final MediaSourceEventListener.EventDispatcher eventDispatcher, final Allocator allocator, final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, final boolean allowChunklessPreparation) {
        this.extractorFactory = extractorFactory;
        this.playlistTracker = playlistTracker;
        this.dataSourceFactory = dataSourceFactory;
        this.mediaTransferListener = mediaTransferListener;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.allowChunklessPreparation = allowChunklessPreparation;
        this.compositeSequenceableLoader = compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(new SequenceableLoader[0]);
        this.streamWrapperIndices = new IdentityHashMap<SampleStream, Integer>();
        this.timestampAdjusterProvider = new TimestampAdjusterProvider();
        this.sampleStreamWrappers = new HlsSampleStreamWrapper[0];
        this.enabledSampleStreamWrappers = new HlsSampleStreamWrapper[0];
        eventDispatcher.mediaPeriodCreated();
    }
    
    private void buildAndPrepareMainSampleStreamWrapper(final HlsMasterPlaylist hlsMasterPlaylist, final long n) {
        final ArrayList<HlsMasterPlaylist.HlsUrl> list = (ArrayList<HlsMasterPlaylist.HlsUrl>)new ArrayList<Object>(hlsMasterPlaylist.variants);
        ArrayList<HlsMasterPlaylist.HlsUrl> list2 = new ArrayList<HlsMasterPlaylist.HlsUrl>();
        final ArrayList<Object> list3 = new ArrayList<Object>();
        for (int i = 0; i < list.size(); ++i) {
            final HlsMasterPlaylist.HlsUrl hlsUrl = list.get(i);
            final Format format = hlsUrl.format;
            if (format.height <= 0 && Util.getCodecsOfType(format.codecs, 2) == null) {
                if (Util.getCodecsOfType(format.codecs, 1) != null) {
                    list3.add(hlsUrl);
                }
            }
            else {
                list2.add(hlsUrl);
            }
        }
        if (list2.isEmpty()) {
            if (list3.size() < list.size()) {
                list.removeAll(list3);
            }
            list2 = list;
        }
        Assertions.checkArgument(list2.isEmpty() ^ true);
        final HlsMasterPlaylist.HlsUrl[] array = list2.toArray(new HlsMasterPlaylist.HlsUrl[0]);
        final String codecs = array[0].format.codecs;
        final HlsSampleStreamWrapper buildSampleStreamWrapper = this.buildSampleStreamWrapper(0, array, hlsMasterPlaylist.muxedAudioFormat, hlsMasterPlaylist.muxedCaptionFormats, n);
        this.sampleStreamWrappers[0] = buildSampleStreamWrapper;
        if (this.allowChunklessPreparation && codecs != null) {
            final boolean b = Util.getCodecsOfType(codecs, 2) != null;
            final boolean b2 = Util.getCodecsOfType(codecs, 1) != null;
            final ArrayList<TrackGroup> list4 = new ArrayList<TrackGroup>();
            if (b) {
                final Format[] array2 = new Format[list2.size()];
                for (int j = 0; j < array2.length; ++j) {
                    array2[j] = deriveVideoFormat(array[j].format);
                }
                list4.add(new TrackGroup(array2));
                if (b2 && (hlsMasterPlaylist.muxedAudioFormat != null || hlsMasterPlaylist.audios.isEmpty())) {
                    list4.add(new TrackGroup(new Format[] { deriveAudioFormat(array[0].format, hlsMasterPlaylist.muxedAudioFormat, false) }));
                }
                final List<Format> muxedCaptionFormats = hlsMasterPlaylist.muxedCaptionFormats;
                if (muxedCaptionFormats != null) {
                    for (int k = 0; k < muxedCaptionFormats.size(); ++k) {
                        list4.add(new TrackGroup(new Format[] { muxedCaptionFormats.get(k) }));
                    }
                }
            }
            else {
                if (!b2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected codecs attribute: ");
                    sb.append(codecs);
                    throw new IllegalArgumentException(sb.toString());
                }
                final Format[] array3 = new Format[list2.size()];
                for (int l = 0; l < array3.length; ++l) {
                    array3[l] = deriveAudioFormat(array[l].format, hlsMasterPlaylist.muxedAudioFormat, true);
                }
                list4.add(new TrackGroup(array3));
            }
            final TrackGroup trackGroup = new TrackGroup(new Format[] { Format.createSampleFormat("ID3", "application/id3", null, -1, null) });
            list4.add(trackGroup);
            buildSampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray((TrackGroup[])list4.toArray(new TrackGroup[0])), 0, new TrackGroupArray(new TrackGroup[] { trackGroup }));
        }
        else {
            buildSampleStreamWrapper.setIsTimestampMaster(true);
            buildSampleStreamWrapper.continuePreparing();
        }
    }
    
    private void buildAndPrepareSampleStreamWrappers(final long n) {
        final HlsMasterPlaylist masterPlaylist = this.playlistTracker.getMasterPlaylist();
        final List<HlsMasterPlaylist.HlsUrl> audios = masterPlaylist.audios;
        final List<HlsMasterPlaylist.HlsUrl> subtitles = masterPlaylist.subtitles;
        final int pendingPrepareCount = audios.size() + 1 + subtitles.size();
        this.sampleStreamWrappers = new HlsSampleStreamWrapper[pendingPrepareCount];
        this.pendingPrepareCount = pendingPrepareCount;
        this.buildAndPrepareMainSampleStreamWrapper(masterPlaylist, n);
        int i;
        int n2;
        for (i = 0, n2 = 1; i < audios.size(); ++i, ++n2) {
            final HlsMasterPlaylist.HlsUrl hlsUrl = audios.get(i);
            final HlsSampleStreamWrapper buildSampleStreamWrapper = this.buildSampleStreamWrapper(1, new HlsMasterPlaylist.HlsUrl[] { hlsUrl }, null, Collections.emptyList(), n);
            this.sampleStreamWrappers[n2] = buildSampleStreamWrapper;
            final Format format = hlsUrl.format;
            if (this.allowChunklessPreparation && format.codecs != null) {
                buildSampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray(new TrackGroup[] { new TrackGroup(new Format[] { format }) }), 0, TrackGroupArray.EMPTY);
            }
            else {
                buildSampleStreamWrapper.continuePreparing();
            }
        }
        for (int j = 0; j < subtitles.size(); ++j, ++n2) {
            final HlsMasterPlaylist.HlsUrl hlsUrl2 = subtitles.get(j);
            (this.sampleStreamWrappers[n2] = this.buildSampleStreamWrapper(3, new HlsMasterPlaylist.HlsUrl[] { hlsUrl2 }, null, Collections.emptyList(), n)).prepareWithMasterPlaylistInfo(new TrackGroupArray(new TrackGroup[] { new TrackGroup(new Format[] { hlsUrl2.format }) }), 0, TrackGroupArray.EMPTY);
        }
        this.enabledSampleStreamWrappers = this.sampleStreamWrappers;
    }
    
    private HlsSampleStreamWrapper buildSampleStreamWrapper(final int n, final HlsMasterPlaylist.HlsUrl[] array, final Format format, final List<Format> list, final long n2) {
        return new HlsSampleStreamWrapper(n, (HlsSampleStreamWrapper.Callback)this, new HlsChunkSource(this.extractorFactory, this.playlistTracker, array, this.dataSourceFactory, this.mediaTransferListener, this.timestampAdjusterProvider, list), this.allocator, n2, format, this.loadErrorHandlingPolicy, this.eventDispatcher);
    }
    
    private static Format deriveAudioFormat(final Format format, final Format format2, final boolean b) {
        String s;
        int n;
        int n2;
        String s2;
        String label;
        if (format2 != null) {
            s = format2.codecs;
            n = format2.channelCount;
            n2 = format2.selectionFlags;
            s2 = format2.language;
            label = format2.label;
        }
        else {
            s = Util.getCodecsOfType(format.codecs, 1);
            if (b) {
                n = format.channelCount;
                n2 = format.selectionFlags;
                label = (s2 = format.label);
            }
            else {
                label = (s2 = null);
                n = -1;
                n2 = 0;
            }
        }
        final String mediaMimeType = MimeTypes.getMediaMimeType(s);
        int bitrate;
        if (b) {
            bitrate = format.bitrate;
        }
        else {
            bitrate = -1;
        }
        return Format.createAudioContainerFormat(format.id, label, format.containerMimeType, mediaMimeType, s, bitrate, n, -1, null, n2, s2);
    }
    
    private static Format deriveVideoFormat(final Format format) {
        final String codecsOfType = Util.getCodecsOfType(format.codecs, 2);
        return Format.createVideoContainerFormat(format.id, format.label, format.containerMimeType, MimeTypes.getMediaMimeType(codecsOfType), codecsOfType, format.bitrate, format.width, format.height, format.frameRate, null, format.selectionFlags);
    }
    
    @Override
    public boolean continueLoading(final long n) {
        if (this.trackGroups == null) {
            final HlsSampleStreamWrapper[] sampleStreamWrappers = this.sampleStreamWrappers;
            for (int length = sampleStreamWrappers.length, i = 0; i < length; ++i) {
                sampleStreamWrappers[i].continuePreparing();
            }
            return false;
        }
        return this.compositeSequenceableLoader.continueLoading(n);
    }
    
    @Override
    public void discardBuffer(final long n, final boolean b) {
        final HlsSampleStreamWrapper[] enabledSampleStreamWrappers = this.enabledSampleStreamWrappers;
        for (int length = enabledSampleStreamWrappers.length, i = 0; i < length; ++i) {
            enabledSampleStreamWrappers[i].discardBuffer(n, b);
        }
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        return n;
    }
    
    @Override
    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }
    
    @Override
    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }
    
    @Override
    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }
    
    @Override
    public void maybeThrowPrepareError() throws IOException {
        final HlsSampleStreamWrapper[] sampleStreamWrappers = this.sampleStreamWrappers;
        for (int length = sampleStreamWrappers.length, i = 0; i < length; ++i) {
            sampleStreamWrappers[i].maybeThrowPrepareError();
        }
    }
    
    public void onContinueLoadingRequested(final HlsSampleStreamWrapper hlsSampleStreamWrapper) {
        ((SequenceableLoader.Callback<HlsMediaPeriod>)this.callback).onContinueLoadingRequested(this);
    }
    
    @Override
    public void onPlaylistChanged() {
        ((SequenceableLoader.Callback<HlsMediaPeriod>)this.callback).onContinueLoadingRequested(this);
    }
    
    @Override
    public boolean onPlaylistError(final HlsMasterPlaylist.HlsUrl hlsUrl, final long n) {
        final HlsSampleStreamWrapper[] sampleStreamWrappers = this.sampleStreamWrappers;
        final int length = sampleStreamWrappers.length;
        boolean b = true;
        for (int i = 0; i < length; ++i) {
            b &= sampleStreamWrappers[i].onPlaylistError(hlsUrl, n);
        }
        ((SequenceableLoader.Callback<HlsMediaPeriod>)this.callback).onContinueLoadingRequested(this);
        return b;
    }
    
    @Override
    public void onPlaylistRefreshRequired(final HlsMasterPlaylist.HlsUrl hlsUrl) {
        this.playlistTracker.refreshPlaylist(hlsUrl);
    }
    
    @Override
    public void onPrepared() {
        final int pendingPrepareCount = this.pendingPrepareCount - 1;
        this.pendingPrepareCount = pendingPrepareCount;
        if (pendingPrepareCount > 0) {
            return;
        }
        final HlsSampleStreamWrapper[] sampleStreamWrappers = this.sampleStreamWrappers;
        final int length = sampleStreamWrappers.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            n += sampleStreamWrappers[i].getTrackGroups().length;
            ++i;
        }
        final TrackGroup[] array = new TrackGroup[n];
        final HlsSampleStreamWrapper[] sampleStreamWrappers2 = this.sampleStreamWrappers;
        final int length2 = sampleStreamWrappers2.length;
        int j = 0;
        int n2 = 0;
        while (j < length2) {
            final HlsSampleStreamWrapper hlsSampleStreamWrapper = sampleStreamWrappers2[j];
            for (int length3 = hlsSampleStreamWrapper.getTrackGroups().length, k = 0; k < length3; ++k, ++n2) {
                array[n2] = hlsSampleStreamWrapper.getTrackGroups().get(k);
            }
            ++j;
        }
        this.trackGroups = new TrackGroupArray(array);
        this.callback.onPrepared(this);
    }
    
    @Override
    public void prepare(final MediaPeriod.Callback callback, final long n) {
        this.callback = callback;
        this.playlistTracker.addListener((HlsPlaylistTracker.PlaylistEventListener)this);
        this.buildAndPrepareSampleStreamWrappers(n);
    }
    
    @Override
    public long readDiscontinuity() {
        if (!this.notifiedReadingStarted) {
            this.eventDispatcher.readingStarted();
            this.notifiedReadingStarted = true;
        }
        return -9223372036854775807L;
    }
    
    @Override
    public void reevaluateBuffer(final long n) {
        this.compositeSequenceableLoader.reevaluateBuffer(n);
    }
    
    public void release() {
        this.playlistTracker.removeListener((HlsPlaylistTracker.PlaylistEventListener)this);
        final HlsSampleStreamWrapper[] sampleStreamWrappers = this.sampleStreamWrappers;
        for (int length = sampleStreamWrappers.length, i = 0; i < length; ++i) {
            sampleStreamWrappers[i].release();
        }
        this.callback = null;
        this.eventDispatcher.mediaPeriodReleased();
    }
    
    @Override
    public long seekToUs(final long n) {
        final HlsSampleStreamWrapper[] enabledSampleStreamWrappers = this.enabledSampleStreamWrappers;
        if (enabledSampleStreamWrappers.length > 0) {
            final boolean seekToUs = enabledSampleStreamWrappers[0].seekToUs(n, false);
            int n2 = 1;
            while (true) {
                final HlsSampleStreamWrapper[] enabledSampleStreamWrappers2 = this.enabledSampleStreamWrappers;
                if (n2 >= enabledSampleStreamWrappers2.length) {
                    break;
                }
                enabledSampleStreamWrappers2[n2].seekToUs(n, seekToUs);
                ++n2;
            }
            if (seekToUs) {
                this.timestampAdjusterProvider.reset();
            }
        }
        return n;
    }
    
    @Override
    public long selectTracks(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3, final boolean[] array4, final long n) {
        final int[] array5 = new int[array.length];
        final int[] array6 = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            int intValue;
            if (array3[i] == null) {
                intValue = -1;
            }
            else {
                intValue = this.streamWrapperIndices.get(array3[i]);
            }
            array5[i] = intValue;
            array6[i] = -1;
            if (array[i] != null) {
                final TrackGroup trackGroup = array[i].getTrackGroup();
                int n2 = 0;
                while (true) {
                    final HlsSampleStreamWrapper[] sampleStreamWrappers = this.sampleStreamWrappers;
                    if (n2 >= sampleStreamWrappers.length) {
                        break;
                    }
                    if (sampleStreamWrappers[n2].getTrackGroups().indexOf(trackGroup) != -1) {
                        array6[i] = n2;
                        break;
                    }
                    ++n2;
                }
            }
        }
        this.streamWrapperIndices.clear();
        final SampleStream[] array7 = new SampleStream[array.length];
        final SampleStream[] array8 = new SampleStream[array.length];
        final TrackSelection[] array9 = new TrackSelection[array.length];
        final HlsSampleStreamWrapper[] original = new HlsSampleStreamWrapper[this.sampleStreamWrappers.length];
        int newLength = 0;
        int j = 0;
        boolean b = false;
        while (j < this.sampleStreamWrappers.length) {
            for (int k = 0; k < array.length; ++k) {
                final int n3 = array5[k];
                final TrackSelection trackSelection = null;
                SampleStream sampleStream;
                if (n3 == j) {
                    sampleStream = array3[k];
                }
                else {
                    sampleStream = null;
                }
                array8[k] = sampleStream;
                TrackSelection trackSelection2 = trackSelection;
                if (array6[k] == j) {
                    trackSelection2 = array[k];
                }
                array9[k] = trackSelection2;
            }
            final HlsSampleStreamWrapper hlsSampleStreamWrapper = this.sampleStreamWrappers[j];
            final boolean selectTracks = hlsSampleStreamWrapper.selectTracks(array9, array2, array8, array4, n, b);
            int n4 = 0;
            int n5 = 0;
            while (true) {
                final int length = array.length;
                boolean b2 = true;
                if (n4 >= length) {
                    break;
                }
                int n6;
                if (array6[n4] == j) {
                    Assertions.checkState(array8[n4] != null);
                    array7[n4] = array8[n4];
                    this.streamWrapperIndices.put(array8[n4], j);
                    n6 = 1;
                }
                else {
                    n6 = n5;
                    if (array5[n4] == j) {
                        if (array8[n4] != null) {
                            b2 = false;
                        }
                        Assertions.checkState(b2);
                        n6 = n5;
                    }
                }
                ++n4;
                n5 = n6;
            }
            Label_0530: {
                if (n5 != 0) {
                    original[newLength] = hlsSampleStreamWrapper;
                    final int n7 = newLength + 1;
                    Label_0523: {
                        if (newLength == 0) {
                            hlsSampleStreamWrapper.setIsTimestampMaster(true);
                            if (!selectTracks) {
                                final HlsSampleStreamWrapper[] enabledSampleStreamWrappers = this.enabledSampleStreamWrappers;
                                if (enabledSampleStreamWrappers.length != 0 && hlsSampleStreamWrapper == enabledSampleStreamWrappers[0]) {
                                    break Label_0523;
                                }
                            }
                            this.timestampAdjusterProvider.reset();
                            newLength = n7;
                            b = true;
                            break Label_0530;
                        }
                        hlsSampleStreamWrapper.setIsTimestampMaster(false);
                    }
                    newLength = n7;
                }
            }
            ++j;
        }
        System.arraycopy(array7, 0, array3, 0, array7.length);
        this.enabledSampleStreamWrappers = Arrays.copyOf(original, newLength);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader((SequenceableLoader[])this.enabledSampleStreamWrappers);
        return n;
    }
}
