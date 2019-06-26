// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.source.chunk.Chunk;
import android.os.Message;
import java.util.Iterator;
import com.google.android.exoplayer2.ParserException;
import java.util.Map;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.util.Util;
import java.util.TreeMap;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import android.os.Handler;
import com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
import com.google.android.exoplayer2.upstream.Allocator;
import android.os.Handler$Callback;

public final class PlayerEmsgHandler implements Handler$Callback
{
    private final Allocator allocator;
    private final EventMessageDecoder decoder;
    private long expiredManifestPublishTimeUs;
    private final Handler handler;
    private boolean isWaitingForManifestRefresh;
    private long lastLoadedChunkEndTimeBeforeRefreshUs;
    private long lastLoadedChunkEndTimeUs;
    private DashManifest manifest;
    private final TreeMap<Long, Long> manifestPublishTimeToExpiryTimeUs;
    private final PlayerEmsgCallback playerEmsgCallback;
    private boolean released;
    
    public PlayerEmsgHandler(final DashManifest manifest, final PlayerEmsgCallback playerEmsgCallback, final Allocator allocator) {
        this.manifest = manifest;
        this.playerEmsgCallback = playerEmsgCallback;
        this.allocator = allocator;
        this.manifestPublishTimeToExpiryTimeUs = new TreeMap<Long, Long>();
        this.handler = Util.createHandler((Handler$Callback)this);
        this.decoder = new EventMessageDecoder();
        this.lastLoadedChunkEndTimeUs = -9223372036854775807L;
        this.lastLoadedChunkEndTimeBeforeRefreshUs = -9223372036854775807L;
    }
    
    private Map.Entry<Long, Long> ceilingExpiryEntryForPublishTime(final long l) {
        return this.manifestPublishTimeToExpiryTimeUs.ceilingEntry(l);
    }
    
    private static long getManifestPublishTimeMsInEmsg(final EventMessage eventMessage) {
        try {
            return Util.parseXsDateTime(Util.fromUtf8Bytes(eventMessage.messageData));
        }
        catch (ParserException ex) {
            return -9223372036854775807L;
        }
    }
    
    private void handleManifestExpiredMessage(final long n, final long l) {
        final Long n2 = this.manifestPublishTimeToExpiryTimeUs.get(l);
        if (n2 == null) {
            this.manifestPublishTimeToExpiryTimeUs.put(l, n);
        }
        else if (n2 > n) {
            this.manifestPublishTimeToExpiryTimeUs.put(l, n);
        }
    }
    
    public static boolean isPlayerEmsgEvent(final String anObject, final String anObject2) {
        return "urn:mpeg:dash:event:2012".equals(anObject) && ("1".equals(anObject2) || "2".equals(anObject2) || "3".equals(anObject2));
    }
    
    private void maybeNotifyDashManifestRefreshNeeded() {
        final long lastLoadedChunkEndTimeBeforeRefreshUs = this.lastLoadedChunkEndTimeBeforeRefreshUs;
        if (lastLoadedChunkEndTimeBeforeRefreshUs != -9223372036854775807L && lastLoadedChunkEndTimeBeforeRefreshUs == this.lastLoadedChunkEndTimeUs) {
            return;
        }
        this.isWaitingForManifestRefresh = true;
        this.lastLoadedChunkEndTimeBeforeRefreshUs = this.lastLoadedChunkEndTimeUs;
        this.playerEmsgCallback.onDashManifestRefreshRequested();
    }
    
    private void notifyManifestPublishTimeExpired() {
        this.playerEmsgCallback.onDashManifestPublishTimeExpired(this.expiredManifestPublishTimeUs);
    }
    
    private void removePreviouslyExpiredManifestPublishTimeValues() {
        final Iterator<Map.Entry<Long, Long>> iterator = this.manifestPublishTimeToExpiryTimeUs.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey() < this.manifest.publishTimeMs) {
                iterator.remove();
            }
        }
    }
    
    public boolean handleMessage(final Message message) {
        if (this.released) {
            return true;
        }
        if (message.what != 1) {
            return false;
        }
        final ManifestExpiryEventInfo manifestExpiryEventInfo = (ManifestExpiryEventInfo)message.obj;
        this.handleManifestExpiredMessage(manifestExpiryEventInfo.eventTimeUs, manifestExpiryEventInfo.manifestPublishTimeMsInEmsg);
        return true;
    }
    
    boolean maybeRefreshManifestBeforeLoadingNextChunk(final long n) {
        final DashManifest manifest = this.manifest;
        final boolean dynamic = manifest.dynamic;
        final boolean b = false;
        if (!dynamic) {
            return false;
        }
        if (this.isWaitingForManifestRefresh) {
            return true;
        }
        final Map.Entry<Long, Long> ceilingExpiryEntryForPublishTime = this.ceilingExpiryEntryForPublishTime(manifest.publishTimeMs);
        boolean b2 = b;
        if (ceilingExpiryEntryForPublishTime != null) {
            b2 = b;
            if (ceilingExpiryEntryForPublishTime.getValue() < n) {
                this.expiredManifestPublishTimeUs = ceilingExpiryEntryForPublishTime.getKey();
                this.notifyManifestPublishTimeExpired();
                b2 = true;
            }
        }
        if (b2) {
            this.maybeNotifyDashManifestRefreshNeeded();
        }
        return b2;
    }
    
    boolean maybeRefreshManifestOnLoadingError(final Chunk chunk) {
        if (!this.manifest.dynamic) {
            return false;
        }
        if (this.isWaitingForManifestRefresh) {
            return true;
        }
        final long lastLoadedChunkEndTimeUs = this.lastLoadedChunkEndTimeUs;
        if (lastLoadedChunkEndTimeUs != -9223372036854775807L && lastLoadedChunkEndTimeUs < chunk.startTimeUs) {
            this.maybeNotifyDashManifestRefreshNeeded();
            return true;
        }
        return false;
    }
    
    public PlayerTrackEmsgHandler newPlayerTrackEmsgHandler() {
        return new PlayerTrackEmsgHandler(new SampleQueue(this.allocator));
    }
    
    void onChunkLoadCompleted(final Chunk chunk) {
        final long lastLoadedChunkEndTimeUs = this.lastLoadedChunkEndTimeUs;
        if (lastLoadedChunkEndTimeUs != -9223372036854775807L || chunk.endTimeUs > lastLoadedChunkEndTimeUs) {
            this.lastLoadedChunkEndTimeUs = chunk.endTimeUs;
        }
    }
    
    public void release() {
        this.released = true;
        this.handler.removeCallbacksAndMessages((Object)null);
    }
    
    public void updateManifest(final DashManifest manifest) {
        this.isWaitingForManifestRefresh = false;
        this.expiredManifestPublishTimeUs = -9223372036854775807L;
        this.manifest = manifest;
        this.removePreviouslyExpiredManifestPublishTimeValues();
    }
    
    private static final class ManifestExpiryEventInfo
    {
        public final long eventTimeUs;
        public final long manifestPublishTimeMsInEmsg;
        
        public ManifestExpiryEventInfo(final long eventTimeUs, final long manifestPublishTimeMsInEmsg) {
            this.eventTimeUs = eventTimeUs;
            this.manifestPublishTimeMsInEmsg = manifestPublishTimeMsInEmsg;
        }
    }
    
    public interface PlayerEmsgCallback
    {
        void onDashManifestPublishTimeExpired(final long p0);
        
        void onDashManifestRefreshRequested();
    }
    
    public final class PlayerTrackEmsgHandler implements TrackOutput
    {
        private final MetadataInputBuffer buffer;
        private final FormatHolder formatHolder;
        private final SampleQueue sampleQueue;
        
        PlayerTrackEmsgHandler(final SampleQueue sampleQueue) {
            this.sampleQueue = sampleQueue;
            this.formatHolder = new FormatHolder();
            this.buffer = new MetadataInputBuffer();
        }
        
        private MetadataInputBuffer dequeueSample() {
            this.buffer.clear();
            if (this.sampleQueue.read(this.formatHolder, this.buffer, false, false, 0L) == -4) {
                this.buffer.flip();
                return this.buffer;
            }
            return null;
        }
        
        private void onManifestExpiredMessageEncountered(final long n, final long n2) {
            PlayerEmsgHandler.this.handler.sendMessage(PlayerEmsgHandler.this.handler.obtainMessage(1, (Object)new ManifestExpiryEventInfo(n, n2)));
        }
        
        private void parseAndDiscardSamples() {
            while (this.sampleQueue.hasNextSample()) {
                final MetadataInputBuffer dequeueSample = this.dequeueSample();
                if (dequeueSample == null) {
                    continue;
                }
                final long timeUs = dequeueSample.timeUs;
                final EventMessage eventMessage = (EventMessage)PlayerEmsgHandler.this.decoder.decode(dequeueSample).get(0);
                if (!PlayerEmsgHandler.isPlayerEmsgEvent(eventMessage.schemeIdUri, eventMessage.value)) {
                    continue;
                }
                this.parsePlayerEmsgEvent(timeUs, eventMessage);
            }
            this.sampleQueue.discardToRead();
        }
        
        private void parsePlayerEmsgEvent(final long n, final EventMessage eventMessage) {
            final long access$100 = getManifestPublishTimeMsInEmsg(eventMessage);
            if (access$100 == -9223372036854775807L) {
                return;
            }
            this.onManifestExpiredMessageEncountered(n, access$100);
        }
        
        @Override
        public void format(final Format format) {
            this.sampleQueue.format(format);
        }
        
        public boolean maybeRefreshManifestBeforeLoadingNextChunk(final long n) {
            return PlayerEmsgHandler.this.maybeRefreshManifestBeforeLoadingNextChunk(n);
        }
        
        public boolean maybeRefreshManifestOnLoadingError(final Chunk chunk) {
            return PlayerEmsgHandler.this.maybeRefreshManifestOnLoadingError(chunk);
        }
        
        public void onChunkLoadCompleted(final Chunk chunk) {
            PlayerEmsgHandler.this.onChunkLoadCompleted(chunk);
        }
        
        public void release() {
            this.sampleQueue.reset();
        }
        
        @Override
        public int sampleData(final ExtractorInput extractorInput, final int n, final boolean b) throws IOException, InterruptedException {
            return this.sampleQueue.sampleData(extractorInput, n, b);
        }
        
        @Override
        public void sampleData(final ParsableByteArray parsableByteArray, final int n) {
            this.sampleQueue.sampleData(parsableByteArray, n);
        }
        
        @Override
        public void sampleMetadata(final long n, final int n2, final int n3, final int n4, final CryptoData cryptoData) {
            this.sampleQueue.sampleMetadata(n, n2, n3, n4, cryptoData);
            this.parseAndDiscardSamples();
        }
    }
}
