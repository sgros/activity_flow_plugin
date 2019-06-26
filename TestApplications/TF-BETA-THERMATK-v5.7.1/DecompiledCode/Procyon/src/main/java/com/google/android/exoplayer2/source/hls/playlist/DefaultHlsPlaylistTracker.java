// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.UriUtil;
import java.util.Iterator;
import com.google.android.exoplayer2.util.Assertions;
import android.net.Uri;
import java.util.Collection;
import java.util.Map;
import java.io.IOException;
import android.os.SystemClock;
import java.util.ArrayList;
import android.os.Handler;
import java.util.IdentityHashMap;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import java.util.List;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.Loader;

public final class DefaultHlsPlaylistTracker implements HlsPlaylistTracker, Callback<ParsingLoadable<HlsPlaylist>>
{
    public static final Factory FACTORY;
    private final HlsDataSourceFactory dataSourceFactory;
    private MediaSourceEventListener.EventDispatcher eventDispatcher;
    private Loader initialPlaylistLoader;
    private long initialStartTimeUs;
    private boolean isLive;
    private final List<PlaylistEventListener> listeners;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private HlsMasterPlaylist masterPlaylist;
    private ParsingLoadable.Parser<HlsPlaylist> mediaPlaylistParser;
    private final IdentityHashMap<HlsMasterPlaylist.HlsUrl, MediaPlaylistBundle> playlistBundles;
    private final HlsPlaylistParserFactory playlistParserFactory;
    private Handler playlistRefreshHandler;
    private HlsMasterPlaylist.HlsUrl primaryHlsUrl;
    private PrimaryPlaylistListener primaryPlaylistListener;
    private HlsMediaPlaylist primaryUrlSnapshot;
    
    static {
        FACTORY = (Factory)_$$Lambda$lKTLOVxne0MoBOOliKH0gO2KDMM.INSTANCE;
    }
    
    public DefaultHlsPlaylistTracker(final HlsDataSourceFactory dataSourceFactory, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final HlsPlaylistParserFactory playlistParserFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.playlistParserFactory = playlistParserFactory;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.listeners = new ArrayList<PlaylistEventListener>();
        this.playlistBundles = new IdentityHashMap<HlsMasterPlaylist.HlsUrl, MediaPlaylistBundle>();
        this.initialStartTimeUs = -9223372036854775807L;
    }
    
    @Deprecated
    public DefaultHlsPlaylistTracker(final HlsDataSourceFactory hlsDataSourceFactory, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final ParsingLoadable.Parser<HlsPlaylist> parser) {
        this(hlsDataSourceFactory, loadErrorHandlingPolicy, createFixedFactory(parser));
    }
    
    private void createBundles(final List<HlsMasterPlaylist.HlsUrl> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            final HlsMasterPlaylist.HlsUrl key = list.get(i);
            this.playlistBundles.put(key, new MediaPlaylistBundle(key));
        }
    }
    
    private static HlsPlaylistParserFactory createFixedFactory(final ParsingLoadable.Parser<HlsPlaylist> parser) {
        return new HlsPlaylistParserFactory() {
            @Override
            public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
                return parser;
            }
            
            @Override
            public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(final HlsMasterPlaylist hlsMasterPlaylist) {
                return parser;
            }
        };
    }
    
    private static HlsMediaPlaylist.Segment getFirstOldOverlappingSegment(final HlsMediaPlaylist hlsMediaPlaylist, final HlsMediaPlaylist hlsMediaPlaylist2) {
        final int n = (int)(hlsMediaPlaylist2.mediaSequence - hlsMediaPlaylist.mediaSequence);
        final List<HlsMediaPlaylist.Segment> segments = hlsMediaPlaylist.segments;
        HlsMediaPlaylist.Segment segment;
        if (n < segments.size()) {
            segment = (HlsMediaPlaylist.Segment)segments.get(n);
        }
        else {
            segment = null;
        }
        return segment;
    }
    
    private HlsMediaPlaylist getLatestPlaylistSnapshot(final HlsMediaPlaylist hlsMediaPlaylist, final HlsMediaPlaylist hlsMediaPlaylist2) {
        if (!hlsMediaPlaylist2.isNewerThan(hlsMediaPlaylist)) {
            HlsMediaPlaylist copyWithEndTag = hlsMediaPlaylist;
            if (hlsMediaPlaylist2.hasEndTag) {
                copyWithEndTag = hlsMediaPlaylist.copyWithEndTag();
            }
            return copyWithEndTag;
        }
        return hlsMediaPlaylist2.copyWith(this.getLoadedPlaylistStartTimeUs(hlsMediaPlaylist, hlsMediaPlaylist2), this.getLoadedPlaylistDiscontinuitySequence(hlsMediaPlaylist, hlsMediaPlaylist2));
    }
    
    private int getLoadedPlaylistDiscontinuitySequence(final HlsMediaPlaylist hlsMediaPlaylist, final HlsMediaPlaylist hlsMediaPlaylist2) {
        if (hlsMediaPlaylist2.hasDiscontinuitySequence) {
            return hlsMediaPlaylist2.discontinuitySequence;
        }
        final HlsMediaPlaylist primaryUrlSnapshot = this.primaryUrlSnapshot;
        int discontinuitySequence;
        if (primaryUrlSnapshot != null) {
            discontinuitySequence = primaryUrlSnapshot.discontinuitySequence;
        }
        else {
            discontinuitySequence = 0;
        }
        if (hlsMediaPlaylist == null) {
            return discontinuitySequence;
        }
        final HlsMediaPlaylist.Segment firstOldOverlappingSegment = getFirstOldOverlappingSegment(hlsMediaPlaylist, hlsMediaPlaylist2);
        if (firstOldOverlappingSegment != null) {
            return hlsMediaPlaylist.discontinuitySequence + firstOldOverlappingSegment.relativeDiscontinuitySequence - ((HlsMediaPlaylist.Segment)hlsMediaPlaylist2.segments.get(0)).relativeDiscontinuitySequence;
        }
        return discontinuitySequence;
    }
    
    private long getLoadedPlaylistStartTimeUs(final HlsMediaPlaylist hlsMediaPlaylist, final HlsMediaPlaylist hlsMediaPlaylist2) {
        if (hlsMediaPlaylist2.hasProgramDateTime) {
            return hlsMediaPlaylist2.startTimeUs;
        }
        final HlsMediaPlaylist primaryUrlSnapshot = this.primaryUrlSnapshot;
        long startTimeUs;
        if (primaryUrlSnapshot != null) {
            startTimeUs = primaryUrlSnapshot.startTimeUs;
        }
        else {
            startTimeUs = 0L;
        }
        if (hlsMediaPlaylist == null) {
            return startTimeUs;
        }
        final int size = hlsMediaPlaylist.segments.size();
        final HlsMediaPlaylist.Segment firstOldOverlappingSegment = getFirstOldOverlappingSegment(hlsMediaPlaylist, hlsMediaPlaylist2);
        if (firstOldOverlappingSegment != null) {
            return hlsMediaPlaylist.startTimeUs + firstOldOverlappingSegment.relativeStartTimeUs;
        }
        if (size == hlsMediaPlaylist2.mediaSequence - hlsMediaPlaylist.mediaSequence) {
            return hlsMediaPlaylist.getEndTimeUs();
        }
        return startTimeUs;
    }
    
    private boolean maybeSelectNewPrimaryUrl() {
        final List<HlsMasterPlaylist.HlsUrl> variants = this.masterPlaylist.variants;
        final int size = variants.size();
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i = 0; i < size; ++i) {
            final MediaPlaylistBundle mediaPlaylistBundle = this.playlistBundles.get(variants.get(i));
            if (elapsedRealtime > mediaPlaylistBundle.blacklistUntilMs) {
                this.primaryHlsUrl = mediaPlaylistBundle.playlistUrl;
                mediaPlaylistBundle.loadPlaylist();
                return true;
            }
        }
        return false;
    }
    
    private void maybeSetPrimaryUrl(final HlsMasterPlaylist.HlsUrl primaryHlsUrl) {
        if (primaryHlsUrl != this.primaryHlsUrl && this.masterPlaylist.variants.contains(primaryHlsUrl)) {
            final HlsMediaPlaylist primaryUrlSnapshot = this.primaryUrlSnapshot;
            if (primaryUrlSnapshot == null || !primaryUrlSnapshot.hasEndTag) {
                this.primaryHlsUrl = primaryHlsUrl;
                this.playlistBundles.get(this.primaryHlsUrl).loadPlaylist();
            }
        }
    }
    
    private boolean notifyPlaylistError(final HlsMasterPlaylist.HlsUrl hlsUrl, final long n) {
        final int size = this.listeners.size();
        int i = 0;
        boolean b = false;
        while (i < size) {
            b |= (this.listeners.get(i).onPlaylistError(hlsUrl, n) ^ true);
            ++i;
        }
        return b;
    }
    
    private void onPlaylistUpdated(final HlsMasterPlaylist.HlsUrl hlsUrl, final HlsMediaPlaylist primaryUrlSnapshot) {
        if (hlsUrl == this.primaryHlsUrl) {
            if (this.primaryUrlSnapshot == null) {
                this.isLive = (primaryUrlSnapshot.hasEndTag ^ true);
                this.initialStartTimeUs = primaryUrlSnapshot.startTimeUs;
            }
            this.primaryUrlSnapshot = primaryUrlSnapshot;
            this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(primaryUrlSnapshot);
        }
        for (int size = this.listeners.size(), i = 0; i < size; ++i) {
            this.listeners.get(i).onPlaylistChanged();
        }
    }
    
    @Override
    public void addListener(final PlaylistEventListener playlistEventListener) {
        this.listeners.add(playlistEventListener);
    }
    
    @Override
    public long getInitialStartTimeUs() {
        return this.initialStartTimeUs;
    }
    
    @Override
    public HlsMasterPlaylist getMasterPlaylist() {
        return this.masterPlaylist;
    }
    
    @Override
    public HlsMediaPlaylist getPlaylistSnapshot(final HlsMasterPlaylist.HlsUrl key, final boolean b) {
        final HlsMediaPlaylist playlistSnapshot = this.playlistBundles.get(key).getPlaylistSnapshot();
        if (playlistSnapshot != null && b) {
            this.maybeSetPrimaryUrl(key);
        }
        return playlistSnapshot;
    }
    
    @Override
    public boolean isLive() {
        return this.isLive;
    }
    
    @Override
    public boolean isSnapshotValid(final HlsMasterPlaylist.HlsUrl key) {
        return this.playlistBundles.get(key).isSnapshotValid();
    }
    
    @Override
    public void maybeThrowPlaylistRefreshError(final HlsMasterPlaylist.HlsUrl key) throws IOException {
        this.playlistBundles.get(key).maybeThrowPlaylistRefreshError();
    }
    
    @Override
    public void maybeThrowPrimaryPlaylistRefreshError() throws IOException {
        final Loader initialPlaylistLoader = this.initialPlaylistLoader;
        if (initialPlaylistLoader != null) {
            initialPlaylistLoader.maybeThrowError();
        }
        final HlsMasterPlaylist.HlsUrl primaryHlsUrl = this.primaryHlsUrl;
        if (primaryHlsUrl != null) {
            this.maybeThrowPlaylistRefreshError(primaryHlsUrl);
        }
    }
    
    public void onLoadCanceled(final ParsingLoadable<HlsPlaylist> parsingLoadable, final long n, final long n2, final boolean b) {
        this.eventDispatcher.loadCanceled(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), 4, n, n2, parsingLoadable.bytesLoaded());
    }
    
    public void onLoadCompleted(final ParsingLoadable<HlsPlaylist> parsingLoadable, final long n, final long n2) {
        final HlsPlaylist hlsPlaylist = parsingLoadable.getResult();
        final boolean b = hlsPlaylist instanceof HlsMediaPlaylist;
        HlsMasterPlaylist singleVariantMasterPlaylist;
        if (b) {
            singleVariantMasterPlaylist = HlsMasterPlaylist.createSingleVariantMasterPlaylist(hlsPlaylist.baseUri);
        }
        else {
            singleVariantMasterPlaylist = (HlsMasterPlaylist)hlsPlaylist;
        }
        this.masterPlaylist = singleVariantMasterPlaylist;
        this.mediaPlaylistParser = this.playlistParserFactory.createPlaylistParser(singleVariantMasterPlaylist);
        this.primaryHlsUrl = (HlsMasterPlaylist.HlsUrl)singleVariantMasterPlaylist.variants.get(0);
        final ArrayList<HlsMasterPlaylist.HlsUrl> list = new ArrayList<HlsMasterPlaylist.HlsUrl>();
        list.addAll((Collection<? extends HlsMasterPlaylist.HlsUrl>)singleVariantMasterPlaylist.variants);
        list.addAll((Collection<? extends HlsMasterPlaylist.HlsUrl>)singleVariantMasterPlaylist.audios);
        list.addAll((Collection<? extends HlsMasterPlaylist.HlsUrl>)singleVariantMasterPlaylist.subtitles);
        this.createBundles(list);
        final MediaPlaylistBundle mediaPlaylistBundle = this.playlistBundles.get(this.primaryHlsUrl);
        if (b) {
            mediaPlaylistBundle.processLoadedPlaylist((HlsMediaPlaylist)hlsPlaylist, n2);
        }
        else {
            mediaPlaylistBundle.loadPlaylist();
        }
        this.eventDispatcher.loadCompleted(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), 4, n, n2, parsingLoadable.bytesLoaded());
    }
    
    public LoadErrorAction onLoadError(final ParsingLoadable<HlsPlaylist> parsingLoadable, final long n, final long n2, final IOException ex, final int n3) {
        final long retryDelayMs = this.loadErrorHandlingPolicy.getRetryDelayMsFor(parsingLoadable.type, n2, ex, n3);
        final boolean b = retryDelayMs == -9223372036854775807L;
        this.eventDispatcher.loadError(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), 4, n, n2, parsingLoadable.bytesLoaded(), ex, b);
        Loader.LoadErrorAction loadErrorAction;
        if (b) {
            loadErrorAction = Loader.DONT_RETRY_FATAL;
        }
        else {
            loadErrorAction = Loader.createRetryAction(false, retryDelayMs);
        }
        return loadErrorAction;
    }
    
    @Override
    public void refreshPlaylist(final HlsMasterPlaylist.HlsUrl key) {
        this.playlistBundles.get(key).loadPlaylist();
    }
    
    @Override
    public void removeListener(final PlaylistEventListener playlistEventListener) {
        this.listeners.remove(playlistEventListener);
    }
    
    @Override
    public void start(final Uri uri, final MediaSourceEventListener.EventDispatcher eventDispatcher, final PrimaryPlaylistListener primaryPlaylistListener) {
        this.playlistRefreshHandler = new Handler();
        this.eventDispatcher = eventDispatcher;
        this.primaryPlaylistListener = primaryPlaylistListener;
        final ParsingLoadable parsingLoadable = new ParsingLoadable(this.dataSourceFactory.createDataSource(4), uri, 4, (ParsingLoadable.Parser<? extends T>)this.playlistParserFactory.createPlaylistParser());
        Assertions.checkState(this.initialPlaylistLoader == null);
        this.initialPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MasterPlaylist");
        eventDispatcher.loadStarted(parsingLoadable.dataSpec, parsingLoadable.type, this.initialPlaylistLoader.startLoading(parsingLoadable, (Loader.Callback<ParsingLoadable>)this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(parsingLoadable.type)));
    }
    
    @Override
    public void stop() {
        this.primaryHlsUrl = null;
        this.primaryUrlSnapshot = null;
        this.masterPlaylist = null;
        this.initialStartTimeUs = -9223372036854775807L;
        this.initialPlaylistLoader.release();
        this.initialPlaylistLoader = null;
        final Iterator<MediaPlaylistBundle> iterator = this.playlistBundles.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().release();
        }
        this.playlistRefreshHandler.removeCallbacksAndMessages((Object)null);
        this.playlistRefreshHandler = null;
        this.playlistBundles.clear();
    }
    
    private final class MediaPlaylistBundle implements Callback<ParsingLoadable<HlsPlaylist>>, Runnable
    {
        private long blacklistUntilMs;
        private long earliestNextLoadTimeMs;
        private long lastSnapshotChangeMs;
        private long lastSnapshotLoadMs;
        private boolean loadPending;
        private final ParsingLoadable<HlsPlaylist> mediaPlaylistLoadable;
        private final Loader mediaPlaylistLoader;
        private IOException playlistError;
        private HlsMediaPlaylist playlistSnapshot;
        private final HlsMasterPlaylist.HlsUrl playlistUrl;
        
        public MediaPlaylistBundle(final HlsMasterPlaylist.HlsUrl playlistUrl) {
            this.playlistUrl = playlistUrl;
            this.mediaPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MediaPlaylist");
            this.mediaPlaylistLoadable = new ParsingLoadable<HlsPlaylist>(DefaultHlsPlaylistTracker.this.dataSourceFactory.createDataSource(4), UriUtil.resolveToUri(DefaultHlsPlaylistTracker.this.masterPlaylist.baseUri, playlistUrl.url), 4, DefaultHlsPlaylistTracker.this.mediaPlaylistParser);
        }
        
        private boolean blacklistPlaylist(final long n) {
            this.blacklistUntilMs = SystemClock.elapsedRealtime() + n;
            return DefaultHlsPlaylistTracker.this.primaryHlsUrl == this.playlistUrl && !DefaultHlsPlaylistTracker.this.maybeSelectNewPrimaryUrl();
        }
        
        private void loadPlaylistImmediately() {
            final long startLoading = this.mediaPlaylistLoader.startLoading(this.mediaPlaylistLoadable, (Loader.Callback<ParsingLoadable<HlsPlaylist>>)this, DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.mediaPlaylistLoadable.type));
            final MediaSourceEventListener.EventDispatcher access$700 = DefaultHlsPlaylistTracker.this.eventDispatcher;
            final ParsingLoadable<HlsPlaylist> mediaPlaylistLoadable = this.mediaPlaylistLoadable;
            access$700.loadStarted(mediaPlaylistLoadable.dataSpec, mediaPlaylistLoadable.type, startLoading);
        }
        
        private void processLoadedPlaylist(HlsMediaPlaylist hlsMediaPlaylist, long n) {
            final HlsMediaPlaylist playlistSnapshot = this.playlistSnapshot;
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            this.lastSnapshotLoadMs = elapsedRealtime;
            this.playlistSnapshot = DefaultHlsPlaylistTracker.this.getLatestPlaylistSnapshot(playlistSnapshot, hlsMediaPlaylist);
            final HlsMediaPlaylist playlistSnapshot2 = this.playlistSnapshot;
            if (playlistSnapshot2 != playlistSnapshot) {
                this.playlistError = null;
                this.lastSnapshotChangeMs = elapsedRealtime;
                DefaultHlsPlaylistTracker.this.onPlaylistUpdated(this.playlistUrl, playlistSnapshot2);
            }
            else if (!playlistSnapshot2.hasEndTag) {
                final long mediaSequence = hlsMediaPlaylist.mediaSequence;
                final long n2 = hlsMediaPlaylist.segments.size();
                hlsMediaPlaylist = this.playlistSnapshot;
                if (mediaSequence + n2 < hlsMediaPlaylist.mediaSequence) {
                    this.playlistError = new PlaylistResetException(this.playlistUrl.url);
                    DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, -9223372036854775807L);
                }
                else {
                    final double n3 = (double)(elapsedRealtime - this.lastSnapshotChangeMs);
                    final double v = (double)C.usToMs(hlsMediaPlaylist.targetDurationUs);
                    Double.isNaN(v);
                    if (n3 > v * 3.5) {
                        this.playlistError = new PlaylistStuckException(this.playlistUrl.url);
                        n = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(4, n, this.playlistError, 1);
                        DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, n);
                        if (n != -9223372036854775807L) {
                            this.blacklistPlaylist(n);
                        }
                    }
                }
            }
            hlsMediaPlaylist = this.playlistSnapshot;
            if (hlsMediaPlaylist != playlistSnapshot) {
                n = hlsMediaPlaylist.targetDurationUs;
            }
            else {
                n = hlsMediaPlaylist.targetDurationUs / 2L;
            }
            this.earliestNextLoadTimeMs = elapsedRealtime + C.usToMs(n);
            if (this.playlistUrl == DefaultHlsPlaylistTracker.this.primaryHlsUrl && !this.playlistSnapshot.hasEndTag) {
                this.loadPlaylist();
            }
        }
        
        public HlsMediaPlaylist getPlaylistSnapshot() {
            return this.playlistSnapshot;
        }
        
        public boolean isSnapshotValid() {
            final HlsMediaPlaylist playlistSnapshot = this.playlistSnapshot;
            boolean b = false;
            if (playlistSnapshot == null) {
                return false;
            }
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            final long max = Math.max(30000L, C.usToMs(this.playlistSnapshot.durationUs));
            final HlsMediaPlaylist playlistSnapshot2 = this.playlistSnapshot;
            if (!playlistSnapshot2.hasEndTag) {
                final int playlistType = playlistSnapshot2.playlistType;
                if (playlistType != 2 && playlistType != 1 && this.lastSnapshotLoadMs + max <= elapsedRealtime) {
                    return b;
                }
            }
            b = true;
            return b;
        }
        
        public void loadPlaylist() {
            this.blacklistUntilMs = 0L;
            if (!this.loadPending) {
                if (!this.mediaPlaylistLoader.isLoading()) {
                    final long elapsedRealtime = SystemClock.elapsedRealtime();
                    if (elapsedRealtime < this.earliestNextLoadTimeMs) {
                        this.loadPending = true;
                        DefaultHlsPlaylistTracker.this.playlistRefreshHandler.postDelayed((Runnable)this, this.earliestNextLoadTimeMs - elapsedRealtime);
                    }
                    else {
                        this.loadPlaylistImmediately();
                    }
                }
            }
        }
        
        public void maybeThrowPlaylistRefreshError() throws IOException {
            this.mediaPlaylistLoader.maybeThrowError();
            final IOException playlistError = this.playlistError;
            if (playlistError == null) {
                return;
            }
            throw playlistError;
        }
        
        public void onLoadCanceled(final ParsingLoadable<HlsPlaylist> parsingLoadable, final long n, final long n2, final boolean b) {
            DefaultHlsPlaylistTracker.this.eventDispatcher.loadCanceled(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), 4, n, n2, parsingLoadable.bytesLoaded());
        }
        
        public void onLoadCompleted(final ParsingLoadable<HlsPlaylist> parsingLoadable, final long n, final long n2) {
            final HlsPlaylist hlsPlaylist = parsingLoadable.getResult();
            if (hlsPlaylist instanceof HlsMediaPlaylist) {
                this.processLoadedPlaylist((HlsMediaPlaylist)hlsPlaylist, n2);
                DefaultHlsPlaylistTracker.this.eventDispatcher.loadCompleted(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), 4, n, n2, parsingLoadable.bytesLoaded());
            }
            else {
                this.playlistError = new ParserException("Loaded playlist has unexpected type.");
            }
        }
        
        public LoadErrorAction onLoadError(final ParsingLoadable<HlsPlaylist> parsingLoadable, final long n, final long n2, final IOException ex, final int n3) {
            final long blacklistDurationMs = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(parsingLoadable.type, n2, ex, n3);
            final boolean b = blacklistDurationMs != -9223372036854775807L;
            boolean b3;
            final boolean b2 = b3 = (DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, blacklistDurationMs) || !b);
            if (b) {
                b3 = (b2 | this.blacklistPlaylist(blacklistDurationMs));
            }
            Loader.LoadErrorAction loadErrorAction;
            if (b3) {
                final long retryDelayMs = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getRetryDelayMsFor(parsingLoadable.type, n2, ex, n3);
                if (retryDelayMs != -9223372036854775807L) {
                    loadErrorAction = Loader.createRetryAction(false, retryDelayMs);
                }
                else {
                    loadErrorAction = Loader.DONT_RETRY_FATAL;
                }
            }
            else {
                loadErrorAction = Loader.DONT_RETRY;
            }
            DefaultHlsPlaylistTracker.this.eventDispatcher.loadError(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), 4, n, n2, parsingLoadable.bytesLoaded(), ex, loadErrorAction.isRetry() ^ true);
            return loadErrorAction;
        }
        
        public void release() {
            this.mediaPlaylistLoader.release();
        }
        
        @Override
        public void run() {
            this.loadPending = false;
            this.loadPlaylistImmediately();
        }
    }
}
