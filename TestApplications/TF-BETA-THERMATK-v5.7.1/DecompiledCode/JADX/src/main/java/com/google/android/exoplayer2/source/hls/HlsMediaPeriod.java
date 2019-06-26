package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.Callback;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

public final class HlsMediaPeriod implements MediaPeriod, Callback, PlaylistEventListener {
    private final Allocator allocator;
    private final boolean allowChunklessPreparation;
    private MediaPeriod.Callback callback;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private HlsSampleStreamWrapper[] enabledSampleStreamWrappers = new HlsSampleStreamWrapper[0];
    private final EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final TransferListener mediaTransferListener;
    private boolean notifiedReadingStarted;
    private int pendingPrepareCount;
    private final HlsPlaylistTracker playlistTracker;
    private HlsSampleStreamWrapper[] sampleStreamWrappers = new HlsSampleStreamWrapper[0];
    private final IdentityHashMap<SampleStream, Integer> streamWrapperIndices = new IdentityHashMap();
    private final TimestampAdjusterProvider timestampAdjusterProvider = new TimestampAdjusterProvider();
    private TrackGroupArray trackGroups;

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        return j;
    }

    public HlsMediaPeriod(HlsExtractorFactory hlsExtractorFactory, HlsPlaylistTracker hlsPlaylistTracker, HlsDataSourceFactory hlsDataSourceFactory, TransferListener transferListener, LoadErrorHandlingPolicy loadErrorHandlingPolicy, EventDispatcher eventDispatcher, Allocator allocator, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, boolean z) {
        this.extractorFactory = hlsExtractorFactory;
        this.playlistTracker = hlsPlaylistTracker;
        this.dataSourceFactory = hlsDataSourceFactory;
        this.mediaTransferListener = transferListener;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.allowChunklessPreparation = z;
        this.compositeSequenceableLoader = compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(new SequenceableLoader[0]);
        eventDispatcher.mediaPeriodCreated();
    }

    public void release() {
        this.playlistTracker.removeListener(this);
        for (HlsSampleStreamWrapper release : this.sampleStreamWrappers) {
            release.release();
        }
        this.callback = null;
        this.eventDispatcher.mediaPeriodReleased();
    }

    public void prepare(MediaPeriod.Callback callback, long j) {
        this.callback = callback;
        this.playlistTracker.addListener(this);
        buildAndPrepareSampleStreamWrappers(j);
    }

    public void maybeThrowPrepareError() throws IOException {
        for (HlsSampleStreamWrapper maybeThrowPrepareError : this.sampleStreamWrappers) {
            maybeThrowPrepareError.maybeThrowPrepareError();
        }
    }

    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    /* JADX WARNING: Missing block: B:56:0x00ea, code skipped:
            if (r5 != r8[0]) goto L_0x00ee;
     */
    public long selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[] r21, boolean[] r22, com.google.android.exoplayer2.source.SampleStream[] r23, boolean[] r24, long r25) {
        /*
        r20 = this;
        r0 = r20;
        r1 = r21;
        r2 = r23;
        r3 = r1.length;
        r3 = new int[r3];
        r4 = r1.length;
        r4 = new int[r4];
        r6 = 0;
    L_0x000d:
        r7 = r1.length;
        if (r6 >= r7) goto L_0x004e;
    L_0x0010:
        r7 = r2[r6];
        r8 = -1;
        if (r7 != 0) goto L_0x0017;
    L_0x0015:
        r7 = -1;
        goto L_0x0025;
    L_0x0017:
        r7 = r0.streamWrapperIndices;
        r9 = r2[r6];
        r7 = r7.get(r9);
        r7 = (java.lang.Integer) r7;
        r7 = r7.intValue();
    L_0x0025:
        r3[r6] = r7;
        r4[r6] = r8;
        r7 = r1[r6];
        if (r7 == 0) goto L_0x004b;
    L_0x002d:
        r7 = r1[r6];
        r7 = r7.getTrackGroup();
        r9 = 0;
    L_0x0034:
        r10 = r0.sampleStreamWrappers;
        r11 = r10.length;
        if (r9 >= r11) goto L_0x004b;
    L_0x0039:
        r10 = r10[r9];
        r10 = r10.getTrackGroups();
        r10 = r10.indexOf(r7);
        if (r10 == r8) goto L_0x0048;
    L_0x0045:
        r4[r6] = r9;
        goto L_0x004b;
    L_0x0048:
        r9 = r9 + 1;
        goto L_0x0034;
    L_0x004b:
        r6 = r6 + 1;
        goto L_0x000d;
    L_0x004e:
        r6 = r0.streamWrapperIndices;
        r6.clear();
        r6 = r1.length;
        r6 = new com.google.android.exoplayer2.source.SampleStream[r6];
        r7 = r1.length;
        r7 = new com.google.android.exoplayer2.source.SampleStream[r7];
        r8 = r1.length;
        r15 = new com.google.android.exoplayer2.trackselection.TrackSelection[r8];
        r8 = r0.sampleStreamWrappers;
        r8 = r8.length;
        r13 = new com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper[r8];
        r12 = 0;
        r14 = 0;
        r16 = 0;
    L_0x0065:
        r8 = r0.sampleStreamWrappers;
        r8 = r8.length;
        if (r14 >= r8) goto L_0x010a;
    L_0x006a:
        r8 = 0;
    L_0x006b:
        r9 = r1.length;
        if (r8 >= r9) goto L_0x0084;
    L_0x006e:
        r9 = r3[r8];
        r10 = 0;
        if (r9 != r14) goto L_0x0076;
    L_0x0073:
        r9 = r2[r8];
        goto L_0x0077;
    L_0x0076:
        r9 = r10;
    L_0x0077:
        r7[r8] = r9;
        r9 = r4[r8];
        if (r9 != r14) goto L_0x007f;
    L_0x007d:
        r10 = r1[r8];
    L_0x007f:
        r15[r8] = r10;
        r8 = r8 + 1;
        goto L_0x006b;
    L_0x0084:
        r8 = r0.sampleStreamWrappers;
        r11 = r8[r14];
        r8 = r11;
        r9 = r15;
        r10 = r22;
        r5 = r11;
        r11 = r7;
        r2 = r12;
        r12 = r24;
        r17 = r2;
        r18 = r13;
        r2 = r14;
        r13 = r25;
        r19 = r15;
        r15 = r16;
        r8 = r8.selectTracks(r9, r10, r11, r12, r13, r15);
        r9 = 0;
        r10 = 0;
    L_0x00a2:
        r11 = r1.length;
        r12 = 1;
        if (r9 >= r11) goto L_0x00d5;
    L_0x00a6:
        r11 = r4[r9];
        if (r11 != r2) goto L_0x00c5;
    L_0x00aa:
        r10 = r7[r9];
        if (r10 == 0) goto L_0x00b0;
    L_0x00ae:
        r10 = 1;
        goto L_0x00b1;
    L_0x00b0:
        r10 = 0;
    L_0x00b1:
        com.google.android.exoplayer2.util.Assertions.checkState(r10);
        r10 = r7[r9];
        r6[r9] = r10;
        r10 = r0.streamWrapperIndices;
        r11 = r7[r9];
        r13 = java.lang.Integer.valueOf(r2);
        r10.put(r11, r13);
        r10 = 1;
        goto L_0x00d2;
    L_0x00c5:
        r11 = r3[r9];
        if (r11 != r2) goto L_0x00d2;
    L_0x00c9:
        r11 = r7[r9];
        if (r11 != 0) goto L_0x00ce;
    L_0x00cd:
        goto L_0x00cf;
    L_0x00ce:
        r12 = 0;
    L_0x00cf:
        com.google.android.exoplayer2.util.Assertions.checkState(r12);
    L_0x00d2:
        r9 = r9 + 1;
        goto L_0x00a2;
    L_0x00d5:
        if (r10 == 0) goto L_0x00fd;
    L_0x00d7:
        r18[r17] = r5;
        r9 = r17 + 1;
        if (r17 != 0) goto L_0x00f7;
    L_0x00dd:
        r5.setIsTimestampMaster(r12);
        if (r8 != 0) goto L_0x00ed;
    L_0x00e2:
        r8 = r0.enabledSampleStreamWrappers;
        r10 = r8.length;
        if (r10 == 0) goto L_0x00ed;
    L_0x00e7:
        r10 = 0;
        r8 = r8[r10];
        if (r5 == r8) goto L_0x00fb;
    L_0x00ec:
        goto L_0x00ee;
    L_0x00ed:
        r10 = 0;
    L_0x00ee:
        r5 = r0.timestampAdjusterProvider;
        r5.reset();
        r12 = r9;
        r16 = 1;
        goto L_0x0100;
    L_0x00f7:
        r10 = 0;
        r5.setIsTimestampMaster(r10);
    L_0x00fb:
        r12 = r9;
        goto L_0x0100;
    L_0x00fd:
        r10 = 0;
        r12 = r17;
    L_0x0100:
        r14 = r2 + 1;
        r2 = r23;
        r13 = r18;
        r15 = r19;
        goto L_0x0065;
    L_0x010a:
        r17 = r12;
        r18 = r13;
        r10 = 0;
        r1 = r6.length;
        r2 = r23;
        java.lang.System.arraycopy(r6, r10, r2, r10, r1);
        r1 = r18;
        r1 = java.util.Arrays.copyOf(r1, r12);
        r1 = (com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper[]) r1;
        r0.enabledSampleStreamWrappers = r1;
        r1 = r0.compositeSequenceableLoaderFactory;
        r2 = r0.enabledSampleStreamWrappers;
        r1 = r1.createCompositeSequenceableLoader(r2);
        r0.compositeSequenceableLoader = r1;
        return r25;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.HlsMediaPeriod.selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[], boolean[], com.google.android.exoplayer2.source.SampleStream[], boolean[], long):long");
    }

    public void discardBuffer(long j, boolean z) {
        for (HlsSampleStreamWrapper discardBuffer : this.enabledSampleStreamWrappers) {
            discardBuffer.discardBuffer(j, z);
        }
    }

    public void reevaluateBuffer(long j) {
        this.compositeSequenceableLoader.reevaluateBuffer(j);
    }

    public boolean continueLoading(long j) {
        if (this.trackGroups != null) {
            return this.compositeSequenceableLoader.continueLoading(j);
        }
        for (HlsSampleStreamWrapper continuePreparing : this.sampleStreamWrappers) {
            continuePreparing.continuePreparing();
        }
        return false;
    }

    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }

    public long readDiscontinuity() {
        if (!this.notifiedReadingStarted) {
            this.eventDispatcher.readingStarted();
            this.notifiedReadingStarted = true;
        }
        return -9223372036854775807L;
    }

    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }

    public long seekToUs(long j) {
        HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr = this.enabledSampleStreamWrappers;
        if (hlsSampleStreamWrapperArr.length > 0) {
            boolean seekToUs = hlsSampleStreamWrapperArr[0].seekToUs(j, false);
            int i = 1;
            while (true) {
                HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr2 = this.enabledSampleStreamWrappers;
                if (i >= hlsSampleStreamWrapperArr2.length) {
                    break;
                }
                hlsSampleStreamWrapperArr2[i].seekToUs(j, seekToUs);
                i++;
            }
            if (seekToUs) {
                this.timestampAdjusterProvider.reset();
            }
        }
        return j;
    }

    public void onPrepared() {
        int i = this.pendingPrepareCount - 1;
        this.pendingPrepareCount = i;
        if (i <= 0) {
            int i2 = 0;
            for (HlsSampleStreamWrapper trackGroups : this.sampleStreamWrappers) {
                i2 += trackGroups.getTrackGroups().length;
            }
            TrackGroup[] trackGroupArr = new TrackGroup[i2];
            HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr = this.sampleStreamWrappers;
            int length = hlsSampleStreamWrapperArr.length;
            i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                HlsSampleStreamWrapper hlsSampleStreamWrapper = hlsSampleStreamWrapperArr[i2];
                int i4 = hlsSampleStreamWrapper.getTrackGroups().length;
                int i5 = i3;
                i3 = 0;
                while (i3 < i4) {
                    int i6 = i5 + 1;
                    trackGroupArr[i5] = hlsSampleStreamWrapper.getTrackGroups().get(i3);
                    i3++;
                    i5 = i6;
                }
                i2++;
                i3 = i5;
            }
            this.trackGroups = new TrackGroupArray(trackGroupArr);
            this.callback.onPrepared(this);
        }
    }

    public void onPlaylistRefreshRequired(HlsUrl hlsUrl) {
        this.playlistTracker.refreshPlaylist(hlsUrl);
    }

    public void onContinueLoadingRequested(HlsSampleStreamWrapper hlsSampleStreamWrapper) {
        this.callback.onContinueLoadingRequested(this);
    }

    public void onPlaylistChanged() {
        this.callback.onContinueLoadingRequested(this);
    }

    public boolean onPlaylistError(HlsUrl hlsUrl, long j) {
        int i = 1;
        for (HlsSampleStreamWrapper onPlaylistError : this.sampleStreamWrappers) {
            i &= onPlaylistError.onPlaylistError(hlsUrl, j);
        }
        this.callback.onContinueLoadingRequested(this);
        return i;
    }

    private void buildAndPrepareSampleStreamWrappers(long j) {
        HlsUrl hlsUrl;
        HlsSampleStreamWrapper buildSampleStreamWrapper;
        int i;
        HlsMasterPlaylist masterPlaylist = this.playlistTracker.getMasterPlaylist();
        List list = masterPlaylist.audios;
        List list2 = masterPlaylist.subtitles;
        int size = (list.size() + 1) + list2.size();
        this.sampleStreamWrappers = new HlsSampleStreamWrapper[size];
        this.pendingPrepareCount = size;
        buildAndPrepareMainSampleStreamWrapper(masterPlaylist, j);
        int i2 = 0;
        int i3 = 0;
        int i4 = 1;
        while (i3 < list.size()) {
            hlsUrl = (HlsUrl) list.get(i3);
            buildSampleStreamWrapper = buildSampleStreamWrapper(1, new HlsUrl[]{(HlsUrl) list.get(i3)}, null, Collections.emptyList(), j);
            i = i4 + 1;
            this.sampleStreamWrappers[i4] = buildSampleStreamWrapper;
            Format format = hlsUrl.format;
            if (!this.allowChunklessPreparation || format.codecs == null) {
                buildSampleStreamWrapper.continuePreparing();
            } else {
                TrackGroup[] trackGroupArr = new TrackGroup[1];
                trackGroupArr[0] = new TrackGroup(format);
                buildSampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray(trackGroupArr), 0, TrackGroupArray.EMPTY);
            }
            i3++;
            i4 = i;
            i2 = 0;
        }
        int i5 = 0;
        while (i5 < list2.size()) {
            hlsUrl = (HlsUrl) list2.get(i5);
            buildSampleStreamWrapper = buildSampleStreamWrapper(3, new HlsUrl[]{hlsUrl}, null, Collections.emptyList(), j);
            i = i4 + 1;
            this.sampleStreamWrappers[i4] = buildSampleStreamWrapper;
            TrackGroup[] trackGroupArr2 = new TrackGroup[1];
            trackGroupArr2[0] = new TrackGroup(hlsUrl.format);
            buildSampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray(trackGroupArr2), 0, TrackGroupArray.EMPTY);
            i5++;
            i4 = i;
        }
        this.enabledSampleStreamWrappers = this.sampleStreamWrappers;
    }

    private void buildAndPrepareMainSampleStreamWrapper(HlsMasterPlaylist hlsMasterPlaylist, long j) {
        List list;
        HlsMasterPlaylist hlsMasterPlaylist2 = hlsMasterPlaylist;
        ArrayList arrayList = new ArrayList(hlsMasterPlaylist2.variants);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            HlsUrl hlsUrl = (HlsUrl) arrayList.get(i);
            Format format = hlsUrl.format;
            if (format.height > 0 || Util.getCodecsOfType(format.codecs, 2) != null) {
                arrayList2.add(hlsUrl);
            } else if (Util.getCodecsOfType(format.codecs, 1) != null) {
                arrayList3.add(hlsUrl);
            }
        }
        if (arrayList2.isEmpty()) {
            if (arrayList3.size() < arrayList.size()) {
                arrayList.removeAll(arrayList3);
            }
            list = arrayList;
        } else {
            list = arrayList2;
        }
        Assertions.checkArgument(list.isEmpty() ^ 1);
        HlsUrl[] hlsUrlArr = (HlsUrl[]) list.toArray(new HlsUrl[0]);
        String str = hlsUrlArr[0].format.codecs;
        HlsSampleStreamWrapper buildSampleStreamWrapper = buildSampleStreamWrapper(0, hlsUrlArr, hlsMasterPlaylist2.muxedAudioFormat, hlsMasterPlaylist2.muxedCaptionFormats, j);
        this.sampleStreamWrappers[0] = buildSampleStreamWrapper;
        if (!this.allowChunklessPreparation || str == null) {
            buildSampleStreamWrapper.setIsTimestampMaster(true);
            buildSampleStreamWrapper.continuePreparing();
            return;
        }
        Object obj = Util.getCodecsOfType(str, 2) != null ? 1 : null;
        Object obj2 = Util.getCodecsOfType(str, 1) != null ? 1 : null;
        ArrayList arrayList4 = new ArrayList();
        Format[] formatArr;
        int i2;
        if (obj != null) {
            formatArr = new Format[list.size()];
            for (int i3 = 0; i3 < formatArr.length; i3++) {
                formatArr[i3] = deriveVideoFormat(hlsUrlArr[i3].format);
            }
            arrayList4.add(new TrackGroup(formatArr));
            if (obj2 != null && (hlsMasterPlaylist2.muxedAudioFormat != null || hlsMasterPlaylist2.audios.isEmpty())) {
                arrayList4.add(new TrackGroup(deriveAudioFormat(hlsUrlArr[0].format, hlsMasterPlaylist2.muxedAudioFormat, false)));
            }
            List list2 = hlsMasterPlaylist2.muxedCaptionFormats;
            if (list2 != null) {
                for (i2 = 0; i2 < list2.size(); i2++) {
                    arrayList4.add(new TrackGroup((Format) list2.get(i2)));
                }
            }
        } else if (obj2 != null) {
            formatArr = new Format[list.size()];
            for (i2 = 0; i2 < formatArr.length; i2++) {
                formatArr[i2] = deriveAudioFormat(hlsUrlArr[i2].format, hlsMasterPlaylist2.muxedAudioFormat, true);
            }
            arrayList4.add(new TrackGroup(formatArr));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected codecs attribute: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        arrayList4.add(new TrackGroup(Format.createSampleFormat("ID3", MimeTypes.APPLICATION_ID3, null, -1, null)));
        buildSampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray((TrackGroup[]) arrayList4.toArray(new TrackGroup[0])), 0, new TrackGroupArray(r1));
    }

    private HlsSampleStreamWrapper buildSampleStreamWrapper(int i, HlsUrl[] hlsUrlArr, Format format, List<Format> list, long j) {
        return new HlsSampleStreamWrapper(i, this, new HlsChunkSource(this.extractorFactory, this.playlistTracker, hlsUrlArr, this.dataSourceFactory, this.mediaTransferListener, this.timestampAdjusterProvider, list), this.allocator, j, format, this.loadErrorHandlingPolicy, this.eventDispatcher);
    }

    private static Format deriveVideoFormat(Format format) {
        String codecsOfType = Util.getCodecsOfType(format.codecs, 2);
        return Format.createVideoContainerFormat(format.f14id, format.label, format.containerMimeType, MimeTypes.getMediaMimeType(codecsOfType), codecsOfType, format.bitrate, format.width, format.height, format.frameRate, null, format.selectionFlags);
    }

    private static Format deriveAudioFormat(Format format, Format format2, boolean z) {
        String str;
        String str2;
        int i;
        int i2;
        String str3;
        Format format3 = format;
        Format format4 = format2;
        int i3;
        if (format4 != null) {
            String str4 = format4.codecs;
            int i4 = format4.channelCount;
            i3 = format4.selectionFlags;
            String str5 = format4.language;
            str = format4.label;
            str2 = str4;
            i = i4;
            i2 = i3;
            str3 = str5;
        } else {
            String codecsOfType = Util.getCodecsOfType(format3.codecs, 1);
            if (z) {
                int i5 = format3.channelCount;
                i3 = format3.selectionFlags;
                str2 = codecsOfType;
                i = i5;
                str = format3.label;
                str3 = str;
                i2 = i3;
            } else {
                str2 = codecsOfType;
                str = null;
                str3 = str;
                i = -1;
                i2 = 0;
            }
        }
        return Format.createAudioContainerFormat(format3.f14id, str, format3.containerMimeType, MimeTypes.getMediaMimeType(str2), str2, z ? format3.bitrate : -1, i, -1, null, i2, str3);
    }
}
