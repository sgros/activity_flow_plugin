// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import java.util.Iterator;
import java.io.IOException;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.EmptySampleStream;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.Util;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.source.dash.manifest.Descriptor;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.chunk.ChunkSource;
import java.util.Arrays;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import java.util.Collection;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import java.util.ArrayList;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import android.util.Pair;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import java.util.IdentityHashMap;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.dash.manifest.EventStream;
import java.util.List;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.source.chunk.ChunkSampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.MediaPeriod;

final class DashMediaPeriod implements MediaPeriod, SequenceableLoader.Callback<ChunkSampleStream<DashChunkSource>>, ReleaseCallback<DashChunkSource>
{
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final DashChunkSource.Factory chunkSourceFactory;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final long elapsedRealtimeOffsetMs;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private EventSampleStream[] eventSampleStreams;
    private List<EventStream> eventStreams;
    final int id;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private boolean notifiedReadingStarted;
    private int periodIndex;
    private final PlayerEmsgHandler playerEmsgHandler;
    private ChunkSampleStream<DashChunkSource>[] sampleStreams;
    private final IdentityHashMap<ChunkSampleStream<DashChunkSource>, PlayerEmsgHandler.PlayerTrackEmsgHandler> trackEmsgHandlerBySampleStream;
    private final TrackGroupInfo[] trackGroupInfos;
    private final TrackGroupArray trackGroups;
    private final TransferListener transferListener;
    
    public DashMediaPeriod(final int id, final DashManifest manifest, final int periodIndex, final DashChunkSource.Factory chunkSourceFactory, final TransferListener transferListener, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final MediaSourceEventListener.EventDispatcher eventDispatcher, final long elapsedRealtimeOffsetMs, final LoaderErrorThrower manifestLoaderErrorThrower, final Allocator allocator, final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, final PlayerEmsgHandler.PlayerEmsgCallback playerEmsgCallback) {
        this.id = id;
        this.manifest = manifest;
        this.periodIndex = periodIndex;
        this.chunkSourceFactory = chunkSourceFactory;
        this.transferListener = transferListener;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.elapsedRealtimeOffsetMs = elapsedRealtimeOffsetMs;
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.playerEmsgHandler = new PlayerEmsgHandler(manifest, playerEmsgCallback, allocator);
        this.sampleStreams = newSampleStreamArray(0);
        this.eventSampleStreams = new EventSampleStream[0];
        this.trackEmsgHandlerBySampleStream = new IdentityHashMap<ChunkSampleStream<DashChunkSource>, PlayerEmsgHandler.PlayerTrackEmsgHandler>();
        this.compositeSequenceableLoader = compositeSequenceableLoaderFactory.createCompositeSequenceableLoader((SequenceableLoader[])this.sampleStreams);
        final Period period = manifest.getPeriod(periodIndex);
        this.eventStreams = period.eventStreams;
        final Pair<TrackGroupArray, TrackGroupInfo[]> buildTrackGroups = buildTrackGroups(period.adaptationSets, this.eventStreams);
        this.trackGroups = (TrackGroupArray)buildTrackGroups.first;
        this.trackGroupInfos = (TrackGroupInfo[])buildTrackGroups.second;
        eventDispatcher.mediaPeriodCreated();
    }
    
    private static void buildManifestEventTrackGroupInfos(final List<EventStream> list, final TrackGroup[] array, final TrackGroupInfo[] array2, int n) {
        for (int i = 0; i < list.size(); ++i, ++n) {
            array[n] = new TrackGroup(new Format[] { Format.createSampleFormat(list.get(i).id(), "application/x-emsg", null, -1, null) });
            array2[n] = TrackGroupInfo.mpdEventTrack(i);
        }
    }
    
    private static int buildPrimaryAndEmbeddedTrackGroupInfos(final List<AdaptationSet> list, final int[][] array, final int n, final boolean[] array2, final boolean[] array3, final TrackGroup[] array4, final TrackGroupInfo[] array5) {
        int i = 0;
        int n2 = 0;
        while (i < n) {
            final int[] array6 = array[i];
            final ArrayList<Representation> list2 = new ArrayList<Representation>();
            for (int length = array6.length, j = 0; j < length; ++j) {
                list2.addAll((Collection<?>)list.get(array6[j]).representations);
            }
            final Format[] array7 = new Format[list2.size()];
            for (int k = 0; k < array7.length; ++k) {
                array7[k] = ((Representation)list2.get(k)).format;
            }
            final AdaptationSet set = list.get(array6[0]);
            int n3 = n2 + 1;
            int n4;
            if (array2[i]) {
                n4 = n3 + 1;
            }
            else {
                n4 = n3;
                n3 = -1;
            }
            int n6;
            if (array3[i]) {
                final int n5 = n4 + 1;
                n6 = n4;
                n4 = n5;
            }
            else {
                n6 = -1;
            }
            array4[n2] = new TrackGroup(array7);
            array5[n2] = TrackGroupInfo.primaryTrack(set.type, array6, n2, n3, n6);
            if (n3 != -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(set.id);
                sb.append(":emsg");
                array4[n3] = new TrackGroup(new Format[] { Format.createSampleFormat(sb.toString(), "application/x-emsg", null, -1, null) });
                array5[n3] = TrackGroupInfo.embeddedEmsgTrack(array6, n2);
            }
            if (n6 != -1) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(set.id);
                sb2.append(":cea608");
                array4[n6] = new TrackGroup(new Format[] { Format.createTextSampleFormat(sb2.toString(), "application/cea-608", 0, null) });
                array5[n6] = TrackGroupInfo.embeddedCea608Track(array6, n2);
            }
            ++i;
            n2 = n4;
        }
        return n2;
    }
    
    private ChunkSampleStream<DashChunkSource> buildSampleStream(final TrackGroupInfo trackGroupInfo, final TrackSelection trackSelection, final long n) {
        final int[] original = new int[2];
        final Format[] original2 = new Format[2];
        final boolean b = trackGroupInfo.embeddedEventMessageTrackGroupIndex != -1;
        int n2;
        if (b) {
            original2[0] = this.trackGroups.get(trackGroupInfo.embeddedEventMessageTrackGroupIndex).getFormat(0);
            original[0] = 4;
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final boolean b2 = trackGroupInfo.embeddedCea608TrackGroupIndex != -1;
        int n3 = n2;
        if (b2) {
            original2[n2] = this.trackGroups.get(trackGroupInfo.embeddedCea608TrackGroupIndex).getFormat(0);
            original[n2] = 3;
            n3 = n2 + 1;
        }
        Format[] array = original2;
        int[] copy = original;
        if (n3 < original.length) {
            array = Arrays.copyOf(original2, n3);
            copy = Arrays.copyOf(original, n3);
        }
        TrackOutput playerTrackEmsgHandler;
        if (this.manifest.dynamic && b) {
            playerTrackEmsgHandler = this.playerEmsgHandler.newPlayerTrackEmsgHandler();
        }
        else {
            playerTrackEmsgHandler = null;
        }
        final ChunkSampleStream key = new ChunkSampleStream<DashChunkSource>(trackGroupInfo.trackType, copy, array, this.chunkSourceFactory.createDashChunkSource(this.manifestLoaderErrorThrower, this.manifest, this.periodIndex, trackGroupInfo.adaptationSetIndices, trackSelection, trackGroupInfo.trackType, this.elapsedRealtimeOffsetMs, b, b2, (PlayerEmsgHandler.PlayerTrackEmsgHandler)playerTrackEmsgHandler, this.transferListener), (SequenceableLoader.Callback<ChunkSampleStream<ChunkSource>>)this, this.allocator, n, this.loadErrorHandlingPolicy, this.eventDispatcher);
        synchronized (this) {
            this.trackEmsgHandlerBySampleStream.put((ChunkSampleStream<DashChunkSource>)key, (PlayerEmsgHandler.PlayerTrackEmsgHandler)playerTrackEmsgHandler);
            return (ChunkSampleStream<DashChunkSource>)key;
        }
    }
    
    private static Pair<TrackGroupArray, TrackGroupInfo[]> buildTrackGroups(final List<AdaptationSet> list, final List<EventStream> list2) {
        final int[][] groupedAdaptationSetIndices = getGroupedAdaptationSetIndices(list);
        final int length = groupedAdaptationSetIndices.length;
        final boolean[] array = new boolean[length];
        final boolean[] array2 = new boolean[length];
        final int n = identifyEmbeddedTracks(length, list, groupedAdaptationSetIndices, array, array2) + length + list2.size();
        final TrackGroup[] array3 = new TrackGroup[n];
        final TrackGroupInfo[] array4 = new TrackGroupInfo[n];
        buildManifestEventTrackGroupInfos(list2, array3, array4, buildPrimaryAndEmbeddedTrackGroupInfos(list, groupedAdaptationSetIndices, length, array, array2, array3, array4));
        return (Pair<TrackGroupArray, TrackGroupInfo[]>)Pair.create((Object)new TrackGroupArray(array3), (Object)array4);
    }
    
    private static Descriptor findAdaptationSetSwitchingProperty(final List<Descriptor> list) {
        for (int i = 0; i < list.size(); ++i) {
            final Descriptor descriptor = list.get(i);
            if ("urn:mpeg:dash:adaptation-set-switching:2016".equals(descriptor.schemeIdUri)) {
                return descriptor;
            }
        }
        return null;
    }
    
    private static int[][] getGroupedAdaptationSetIndices(final List<AdaptationSet> list) {
        final int size = list.size();
        final SparseIntArray sparseIntArray = new SparseIntArray(size);
        for (int i = 0; i < size; ++i) {
            sparseIntArray.put(list.get(i).id, i);
        }
        final int[][] original = new int[size][];
        final boolean[] array = new boolean[size];
        int j = 0;
        int newLength = 0;
        while (j < size) {
            if (!array[j]) {
                array[j] = true;
                final Descriptor adaptationSetSwitchingProperty = findAdaptationSetSwitchingProperty(list.get(j).supplementalProperties);
                if (adaptationSetSwitchingProperty == null) {
                    original[newLength] = new int[] { j };
                    ++newLength;
                }
                else {
                    final String[] split = Util.split(adaptationSetSwitchingProperty.value, ",");
                    final int[] original2 = new int[split.length + 1];
                    original2[0] = j;
                    int k = 0;
                    int newLength2 = 1;
                    while (k < split.length) {
                        final int value = sparseIntArray.get(Integer.parseInt(split[k]), -1);
                        int n = newLength2;
                        if (value != -1) {
                            array[value] = true;
                            original2[newLength2] = value;
                            n = newLength2 + 1;
                        }
                        ++k;
                        newLength2 = n;
                    }
                    int[] copy = original2;
                    if (newLength2 < original2.length) {
                        copy = Arrays.copyOf(original2, newLength2);
                    }
                    original[newLength] = copy;
                    ++newLength;
                }
            }
            ++j;
        }
        int[][] array2 = original;
        if (newLength < size) {
            array2 = Arrays.copyOf(original, newLength);
        }
        return array2;
    }
    
    private int getPrimaryStreamIndex(int i, final int[] array) {
        i = array[i];
        if (i == -1) {
            return -1;
        }
        final int primaryTrackGroupIndex = this.trackGroupInfos[i].primaryTrackGroupIndex;
        int n;
        for (i = 0; i < array.length; ++i) {
            n = array[i];
            if (n == primaryTrackGroupIndex && this.trackGroupInfos[n].trackGroupCategory == 0) {
                return i;
            }
        }
        return -1;
    }
    
    private int[] getStreamIndexToTrackGroupIndex(final TrackSelection[] array) {
        final int[] array2 = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != null) {
                array2[i] = this.trackGroups.indexOf(array[i].getTrackGroup());
            }
            else {
                array2[i] = -1;
            }
        }
        return array2;
    }
    
    private static boolean hasCea608Track(final List<AdaptationSet> list, final int[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final List<Descriptor> accessibilityDescriptors = list.get(array[i]).accessibilityDescriptors;
            for (int j = 0; j < accessibilityDescriptors.size(); ++j) {
                if ("urn:scte:dash:cc:cea-608:2015".equals(accessibilityDescriptors.get(j).schemeIdUri)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean hasEventMessageTrack(final List<AdaptationSet> list, final int[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final List<Representation> representations = list.get(array[i]).representations;
            for (int j = 0; j < representations.size(); ++j) {
                if (!representations.get(j).inbandEventStreams.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static int identifyEmbeddedTracks(final int n, final List<AdaptationSet> list, final int[][] array, final boolean[] array2, final boolean[] array3) {
        int i = 0;
        int n2 = 0;
        while (i < n) {
            int n3 = n2;
            if (hasEventMessageTrack(list, array[i])) {
                array2[i] = true;
                n3 = n2 + 1;
            }
            n2 = n3;
            if (hasCea608Track(list, array[i])) {
                array3[i] = true;
                n2 = n3 + 1;
            }
            ++i;
        }
        return n2;
    }
    
    private static ChunkSampleStream<DashChunkSource>[] newSampleStreamArray(final int n) {
        return (ChunkSampleStream<DashChunkSource>[])new ChunkSampleStream[n];
    }
    
    private void releaseDisabledStreams(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null || !array2[i]) {
                if (array3[i] instanceof ChunkSampleStream) {
                    ((ChunkSampleStream)array3[i]).release((ChunkSampleStream.ReleaseCallback)this);
                }
                else if (array3[i] instanceof EmbeddedSampleStream) {
                    ((EmbeddedSampleStream)array3[i]).release();
                }
                array3[i] = null;
            }
        }
    }
    
    private void releaseOrphanEmbeddedStreams(final TrackSelection[] array, final SampleStream[] array2, final int[] array3) {
        for (int i = 0; i < array.length; ++i) {
            if (array2[i] instanceof EmptySampleStream || array2[i] instanceof EmbeddedSampleStream) {
                final int primaryStreamIndex = this.getPrimaryStreamIndex(i, array3);
                boolean b;
                if (primaryStreamIndex == -1) {
                    b = (array2[i] instanceof EmptySampleStream);
                }
                else {
                    b = (array2[i] instanceof EmbeddedSampleStream && ((EmbeddedSampleStream)array2[i]).parent == array2[primaryStreamIndex]);
                }
                if (!b) {
                    if (array2[i] instanceof EmbeddedSampleStream) {
                        ((EmbeddedSampleStream)array2[i]).release();
                    }
                    array2[i] = null;
                }
            }
        }
    }
    
    private void selectNewStreams(final TrackSelection[] array, final SampleStream[] array2, final boolean[] array3, final long n, final int[] array4) {
        final int n2 = 0;
        int n3 = 0;
        int i;
        while (true) {
            i = n2;
            if (n3 >= array.length) {
                break;
            }
            if (array2[n3] == null && array[n3] != null) {
                array3[n3] = true;
                final TrackGroupInfo trackGroupInfo = this.trackGroupInfos[array4[n3]];
                final int trackGroupCategory = trackGroupInfo.trackGroupCategory;
                if (trackGroupCategory == 0) {
                    array2[n3] = this.buildSampleStream(trackGroupInfo, array[n3], n);
                }
                else if (trackGroupCategory == 2) {
                    array2[n3] = new EventSampleStream(this.eventStreams.get(trackGroupInfo.eventStreamGroupIndex), array[n3].getTrackGroup().getFormat(0), this.manifest.dynamic);
                }
            }
            ++n3;
        }
        while (i < array.length) {
            if (array2[i] == null && array[i] != null) {
                final TrackGroupInfo trackGroupInfo2 = this.trackGroupInfos[array4[i]];
                if (trackGroupInfo2.trackGroupCategory == 1) {
                    final int primaryStreamIndex = this.getPrimaryStreamIndex(i, array4);
                    if (primaryStreamIndex == -1) {
                        array2[i] = new EmptySampleStream();
                    }
                    else {
                        array2[i] = ((ChunkSampleStream)array2[primaryStreamIndex]).selectEmbeddedTrack(n, trackGroupInfo2.trackType);
                    }
                }
            }
            ++i;
        }
    }
    
    @Override
    public boolean continueLoading(final long n) {
        return this.compositeSequenceableLoader.continueLoading(n);
    }
    
    @Override
    public void discardBuffer(final long n, final boolean b) {
        final ChunkSampleStream<DashChunkSource>[] sampleStreams = this.sampleStreams;
        for (int length = sampleStreams.length, i = 0; i < length; ++i) {
            sampleStreams[i].discardBuffer(n, b);
        }
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        for (final ChunkSampleStream<DashChunkSource> chunkSampleStream : this.sampleStreams) {
            if (chunkSampleStream.primaryTrackType == 2) {
                return chunkSampleStream.getAdjustedSeekPositionUs(n, seekParameters);
            }
        }
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
        this.manifestLoaderErrorThrower.maybeThrowError();
    }
    
    public void onContinueLoadingRequested(final ChunkSampleStream<DashChunkSource> chunkSampleStream) {
        ((SequenceableLoader.Callback<DashMediaPeriod>)this.callback).onContinueLoadingRequested(this);
    }
    
    @Override
    public void onSampleStreamReleased(final ChunkSampleStream<DashChunkSource> key) {
        synchronized (this) {
            final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler = this.trackEmsgHandlerBySampleStream.remove(key);
            if (playerTrackEmsgHandler != null) {
                playerTrackEmsgHandler.release();
            }
        }
    }
    
    @Override
    public void prepare(final MediaPeriod.Callback callback, final long n) {
        (this.callback = callback).onPrepared(this);
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
        this.playerEmsgHandler.release();
        final ChunkSampleStream<DashChunkSource>[] sampleStreams = this.sampleStreams;
        for (int length = sampleStreams.length, i = 0; i < length; ++i) {
            sampleStreams[i].release((ChunkSampleStream.ReleaseCallback<DashChunkSource>)this);
        }
        this.callback = null;
        this.eventDispatcher.mediaPeriodReleased();
    }
    
    @Override
    public long seekToUs(final long n) {
        final ChunkSampleStream<DashChunkSource>[] sampleStreams = this.sampleStreams;
        final int length = sampleStreams.length;
        final int n2 = 0;
        for (int i = 0; i < length; ++i) {
            sampleStreams[i].seekToUs(n);
        }
        final EventSampleStream[] eventSampleStreams = this.eventSampleStreams;
        for (int length2 = eventSampleStreams.length, j = n2; j < length2; ++j) {
            eventSampleStreams[j].seekToUs(n);
        }
        return n;
    }
    
    @Override
    public long selectTracks(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3, final boolean[] array4, final long n) {
        final int[] streamIndexToTrackGroupIndex = this.getStreamIndexToTrackGroupIndex(array);
        this.releaseDisabledStreams(array, array2, array3);
        this.releaseOrphanEmbeddedStreams(array, array3, streamIndexToTrackGroupIndex);
        this.selectNewStreams(array, array3, array4, n, streamIndexToTrackGroupIndex);
        final ArrayList<ChunkSampleStream<?>> list = new ArrayList<ChunkSampleStream<?>>();
        final ArrayList<EventSampleStream> list2 = new ArrayList<EventSampleStream>();
        for (final SampleStream sampleStream : array3) {
            if (sampleStream instanceof ChunkSampleStream) {
                list.add((ChunkSampleStream<?>)sampleStream);
            }
            else if (sampleStream instanceof EventSampleStream) {
                list2.add((EventSampleStream)sampleStream);
            }
        }
        list.toArray(this.sampleStreams = newSampleStreamArray(list.size()));
        list2.toArray(this.eventSampleStreams = new EventSampleStream[list2.size()]);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader((SequenceableLoader[])this.sampleStreams);
        return n;
    }
    
    public void updateManifest(final DashManifest manifest, final int periodIndex) {
        this.manifest = manifest;
        this.periodIndex = periodIndex;
        this.playerEmsgHandler.updateManifest(manifest);
        final ChunkSampleStream<DashChunkSource>[] sampleStreams = this.sampleStreams;
        if (sampleStreams != null) {
            for (int length = sampleStreams.length, i = 0; i < length; ++i) {
                sampleStreams[i].getChunkSource().updateManifest(manifest, periodIndex);
            }
            ((SequenceableLoader.Callback<DashMediaPeriod>)this.callback).onContinueLoadingRequested(this);
        }
        this.eventStreams = manifest.getPeriod(periodIndex).eventStreams;
        for (final EventSampleStream eventSampleStream : this.eventSampleStreams) {
            for (final EventStream eventStream : this.eventStreams) {
                if (eventStream.id().equals(eventSampleStream.eventStreamId())) {
                    final int periodCount = manifest.getPeriodCount();
                    boolean b = true;
                    if (!manifest.dynamic || periodIndex != periodCount - 1) {
                        b = false;
                    }
                    eventSampleStream.updateEventStream(eventStream, b);
                    break;
                }
            }
        }
    }
    
    private static final class TrackGroupInfo
    {
        public final int[] adaptationSetIndices;
        public final int embeddedCea608TrackGroupIndex;
        public final int embeddedEventMessageTrackGroupIndex;
        public final int eventStreamGroupIndex;
        public final int primaryTrackGroupIndex;
        public final int trackGroupCategory;
        public final int trackType;
        
        private TrackGroupInfo(final int trackType, final int trackGroupCategory, final int[] adaptationSetIndices, final int primaryTrackGroupIndex, final int embeddedEventMessageTrackGroupIndex, final int embeddedCea608TrackGroupIndex, final int eventStreamGroupIndex) {
            this.trackType = trackType;
            this.adaptationSetIndices = adaptationSetIndices;
            this.trackGroupCategory = trackGroupCategory;
            this.primaryTrackGroupIndex = primaryTrackGroupIndex;
            this.embeddedEventMessageTrackGroupIndex = embeddedEventMessageTrackGroupIndex;
            this.embeddedCea608TrackGroupIndex = embeddedCea608TrackGroupIndex;
            this.eventStreamGroupIndex = eventStreamGroupIndex;
        }
        
        public static TrackGroupInfo embeddedCea608Track(final int[] array, final int n) {
            return new TrackGroupInfo(3, 1, array, n, -1, -1, -1);
        }
        
        public static TrackGroupInfo embeddedEmsgTrack(final int[] array, final int n) {
            return new TrackGroupInfo(4, 1, array, n, -1, -1, -1);
        }
        
        public static TrackGroupInfo mpdEventTrack(final int n) {
            return new TrackGroupInfo(4, 2, null, -1, -1, -1, n);
        }
        
        public static TrackGroupInfo primaryTrack(final int n, final int[] array, final int n2, final int n3, final int n4) {
            return new TrackGroupInfo(n, 0, array, n2, n3, n4, -1);
        }
    }
}
