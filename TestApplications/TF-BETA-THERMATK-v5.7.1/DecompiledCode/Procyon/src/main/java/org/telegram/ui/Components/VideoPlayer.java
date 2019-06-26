// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import android.net.Uri;
import android.graphics.SurfaceTexture;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import android.content.Context;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import org.telegram.messenger.secretmedia.ExtendedDefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import org.telegram.messenger.ApplicationLoader;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import android.view.TextureView;
import com.google.android.exoplayer2.upstream.DataSource;
import android.os.Handler;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import android.annotation.SuppressLint;
import org.telegram.messenger.NotificationCenter;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Player;

@SuppressLint({ "NewApi" })
public class VideoPlayer implements EventListener, VideoListener, NotificationCenterDelegate
{
    private static final DefaultBandwidthMeter BANDWIDTH_METER;
    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT = 3;
    private static final int RENDERER_BUILDING_STATE_IDLE = 1;
    private SimpleExoPlayer audioPlayer;
    private boolean audioPlayerReady;
    private boolean autoplay;
    private VideoPlayerDelegate delegate;
    private boolean isStreaming;
    private boolean lastReportedPlayWhenReady;
    private int lastReportedPlaybackState;
    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private boolean mixedAudio;
    private boolean mixedPlayWhenReady;
    private SimpleExoPlayer player;
    private TextureView textureView;
    private MappingTrackSelector trackSelector;
    private boolean videoPlayerReady;
    
    static {
        BANDWIDTH_METER = new DefaultBandwidthMeter();
    }
    
    public VideoPlayer() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        final DefaultBandwidthMeter bandwidth_METER = VideoPlayer.BANDWIDTH_METER;
        this.mediaDataSourceFactory = new ExtendedDefaultDataSourceFactory(applicationContext, bandwidth_METER, new DefaultHttpDataSourceFactory("Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)", bandwidth_METER));
        this.mainHandler = new Handler();
        this.trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(VideoPlayer.BANDWIDTH_METER));
        this.lastReportedPlaybackState = 1;
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.playerDidStartPlaying);
    }
    
    private void checkPlayersReady() {
        if (this.audioPlayerReady && this.videoPlayerReady && this.mixedPlayWhenReady) {
            this.play();
        }
    }
    
    private void ensurePleyaerCreated() {
        final DefaultLoadControl defaultLoadControl = new DefaultLoadControl(new DefaultAllocator(true, 65536), 15000, 50000, 100, 5000, -1, true);
        if (this.player == null) {
            (this.player = ExoPlayerFactory.newSimpleInstance(ApplicationLoader.applicationContext, this.trackSelector, defaultLoadControl, null, 2)).addListener(this);
            this.player.setVideoListener((SimpleExoPlayer.VideoListener)this);
            this.player.setVideoTextureView(this.textureView);
            this.player.setPlayWhenReady(this.autoplay);
        }
        if (this.mixedAudio && this.audioPlayer == null) {
            (this.audioPlayer = ExoPlayerFactory.newSimpleInstance(ApplicationLoader.applicationContext, this.trackSelector, defaultLoadControl, null, 2)).addListener(new EventListener() {
                @Override
                public void onLoadingChanged(final boolean b) {
                }
                
                @Override
                public void onPlaybackParametersChanged(final PlaybackParameters playbackParameters) {
                }
                
                @Override
                public void onPlayerError(final ExoPlaybackException ex) {
                }
                
                @Override
                public void onPlayerStateChanged(final boolean b, final int n) {
                    if (!VideoPlayer.this.audioPlayerReady && n == 3) {
                        VideoPlayer.this.audioPlayerReady = true;
                        VideoPlayer.this.checkPlayersReady();
                    }
                }
                
                @Override
                public void onPositionDiscontinuity(final int n) {
                }
                
                public void onRepeatModeChanged(final int n) {
                }
                
                @Override
                public void onSeekProcessed() {
                }
                
                public void onShuffleModeEnabledChanged(final boolean b) {
                }
                
                @Override
                public void onTimelineChanged(final Timeline timeline, final Object o, final int n) {
                }
                
                @Override
                public void onTracksChanged(final TrackGroupArray trackGroupArray, final TrackSelectionArray trackSelectionArray) {
                }
            });
            this.audioPlayer.setPlayWhenReady(this.autoplay);
        }
    }
    
    private void maybeReportPlayerState() {
        final SimpleExoPlayer player = this.player;
        if (player == null) {
            return;
        }
        final boolean playWhenReady = player.getPlayWhenReady();
        final int playbackState = this.player.getPlaybackState();
        if (this.lastReportedPlayWhenReady != playWhenReady || this.lastReportedPlaybackState != playbackState) {
            this.delegate.onStateChanged(playWhenReady, playbackState);
            this.lastReportedPlayWhenReady = playWhenReady;
            this.lastReportedPlaybackState = playbackState;
        }
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.playerDidStartPlaying && array[0] != this && this.isPlaying()) {
            this.pause();
        }
    }
    
    public int getBufferedPercentage() {
        int bufferedPercentage;
        if (this.isStreaming) {
            final SimpleExoPlayer player = this.player;
            if (player != null) {
                bufferedPercentage = player.getBufferedPercentage();
            }
            else {
                bufferedPercentage = 0;
            }
        }
        else {
            bufferedPercentage = 100;
        }
        return bufferedPercentage;
    }
    
    public long getBufferedPosition() {
        final SimpleExoPlayer player = this.player;
        long n;
        if (player != null) {
            if (this.isStreaming) {
                n = player.getBufferedPosition();
            }
            else {
                n = player.getDuration();
            }
        }
        else {
            n = 0L;
        }
        return n;
    }
    
    public long getCurrentPosition() {
        final SimpleExoPlayer player = this.player;
        long currentPosition;
        if (player != null) {
            currentPosition = player.getCurrentPosition();
        }
        else {
            currentPosition = 0L;
        }
        return currentPosition;
    }
    
    public long getDuration() {
        final SimpleExoPlayer player = this.player;
        long duration;
        if (player != null) {
            duration = player.getDuration();
        }
        else {
            duration = 0L;
        }
        return duration;
    }
    
    public boolean getPlayWhenReady() {
        return this.player.getPlayWhenReady();
    }
    
    public int getPlaybackState() {
        return this.player.getPlaybackState();
    }
    
    public boolean isBuffering() {
        return this.player != null && this.lastReportedPlaybackState == 2;
    }
    
    public boolean isMuted() {
        return this.player.getVolume() == 0.0f;
    }
    
    public boolean isPlayerPrepared() {
        return this.player != null;
    }
    
    public boolean isPlaying() {
        if (!this.mixedAudio || !this.mixedPlayWhenReady) {
            final SimpleExoPlayer player = this.player;
            if (player == null || !player.getPlayWhenReady()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isStreaming() {
        return this.isStreaming;
    }
    
    @Override
    public void onLoadingChanged(final boolean b) {
    }
    
    @Override
    public void onPlaybackParametersChanged(final PlaybackParameters playbackParameters) {
    }
    
    @Override
    public void onPlayerError(final ExoPlaybackException ex) {
        this.delegate.onError(ex);
    }
    
    @Override
    public void onPlayerStateChanged(final boolean b, final int n) {
        this.maybeReportPlayerState();
        if (b && n == 3) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.playerDidStartPlaying, this);
        }
        if (!this.videoPlayerReady && n == 3) {
            this.videoPlayerReady = true;
            this.checkPlayersReady();
        }
    }
    
    @Override
    public void onPositionDiscontinuity(final int n) {
    }
    
    @Override
    public void onRenderedFirstFrame() {
        this.delegate.onRenderedFirstFrame();
    }
    
    public void onRepeatModeChanged(final int n) {
    }
    
    @Override
    public void onSeekProcessed() {
    }
    
    public void onShuffleModeEnabledChanged(final boolean b) {
    }
    
    @Override
    public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
        return this.delegate.onSurfaceDestroyed(surfaceTexture);
    }
    
    @Override
    public void onSurfaceSizeChanged(final int n, final int n2) {
    }
    
    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
        this.delegate.onSurfaceTextureUpdated(surfaceTexture);
    }
    
    @Override
    public void onTimelineChanged(final Timeline timeline, final Object o, final int n) {
    }
    
    @Override
    public void onTracksChanged(final TrackGroupArray trackGroupArray, final TrackSelectionArray trackSelectionArray) {
    }
    
    @Override
    public void onVideoSizeChanged(final int n, final int n2, final int n3, final float n4) {
        this.delegate.onVideoSizeChanged(n, n2, n3, n4);
    }
    
    public void pause() {
        this.mixedPlayWhenReady = false;
        final SimpleExoPlayer player = this.player;
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        final SimpleExoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            audioPlayer.setPlayWhenReady(false);
        }
    }
    
    public void play() {
        this.mixedPlayWhenReady = true;
        if (this.mixedAudio && (!this.audioPlayerReady || !this.videoPlayerReady)) {
            final SimpleExoPlayer player = this.player;
            if (player != null) {
                player.setPlayWhenReady(false);
            }
            final SimpleExoPlayer audioPlayer = this.audioPlayer;
            if (audioPlayer != null) {
                audioPlayer.setPlayWhenReady(false);
            }
            return;
        }
        final SimpleExoPlayer player2 = this.player;
        if (player2 != null) {
            player2.setPlayWhenReady(true);
        }
        final SimpleExoPlayer audioPlayer2 = this.audioPlayer;
        if (audioPlayer2 != null) {
            audioPlayer2.setPlayWhenReady(true);
        }
    }
    
    public void preparePlayer(final Uri uri, final String s) {
        int n = 0;
        this.videoPlayerReady = false;
        this.mixedAudio = false;
        final String scheme = uri.getScheme();
        this.isStreaming = (scheme != null && !scheme.startsWith("file"));
        this.ensurePleyaerCreated();
        final int hashCode = s.hashCode();
        Label_0131: {
            if (hashCode != 3680) {
                if (hashCode != 103407) {
                    if (hashCode == 3075986) {
                        if (s.equals("dash")) {
                            break Label_0131;
                        }
                    }
                }
                else if (s.equals("hls")) {
                    n = 1;
                    break Label_0131;
                }
            }
            else if (s.equals("ss")) {
                n = 2;
                break Label_0131;
            }
            n = -1;
        }
        BaseMediaSource baseMediaSource;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    baseMediaSource = new ExtractorMediaSource(uri, this.mediaDataSourceFactory, new DefaultExtractorsFactory(), this.mainHandler, null);
                }
                else {
                    final DataSource.Factory mediaDataSourceFactory = this.mediaDataSourceFactory;
                    baseMediaSource = new SsMediaSource(uri, mediaDataSourceFactory, new DefaultSsChunkSource.Factory(mediaDataSourceFactory), this.mainHandler, null);
                }
            }
            else {
                baseMediaSource = new HlsMediaSource(uri, this.mediaDataSourceFactory, this.mainHandler, null);
            }
        }
        else {
            final DataSource.Factory mediaDataSourceFactory2 = this.mediaDataSourceFactory;
            baseMediaSource = new DashMediaSource(uri, mediaDataSourceFactory2, new DefaultDashChunkSource.Factory(mediaDataSourceFactory2), this.mainHandler, null);
        }
        this.player.prepare(baseMediaSource, true, true);
    }
    
    public void preparePlayerLoop(final Uri uri, final String s, final Uri uri2, final String s2) {
        this.mixedAudio = true;
        this.audioPlayerReady = false;
        this.videoPlayerReady = false;
        this.ensurePleyaerCreated();
        MediaSource mediaSource2;
        MediaSource mediaSource = mediaSource2 = null;
        for (int i = 0; i < 2; ++i) {
            Uri uri3;
            String s3;
            if (i == 0) {
                uri3 = uri;
                s3 = s;
            }
            else {
                uri3 = uri2;
                s3 = s2;
            }
            int n = -1;
            final int hashCode = s3.hashCode();
            if (hashCode != 3680) {
                if (hashCode != 103407) {
                    if (hashCode == 3075986) {
                        if (s3.equals("dash")) {
                            n = 0;
                        }
                    }
                }
                else if (s3.equals("hls")) {
                    n = 1;
                }
            }
            else if (s3.equals("ss")) {
                n = 2;
            }
            BaseMediaSource baseMediaSource;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        baseMediaSource = new ExtractorMediaSource(uri3, this.mediaDataSourceFactory, new DefaultExtractorsFactory(), this.mainHandler, null);
                    }
                    else {
                        final DataSource.Factory mediaDataSourceFactory = this.mediaDataSourceFactory;
                        baseMediaSource = new SsMediaSource(uri3, mediaDataSourceFactory, new DefaultSsChunkSource.Factory(mediaDataSourceFactory), this.mainHandler, null);
                    }
                }
                else {
                    baseMediaSource = new HlsMediaSource(uri3, this.mediaDataSourceFactory, this.mainHandler, null);
                }
            }
            else {
                final DataSource.Factory mediaDataSourceFactory2 = this.mediaDataSourceFactory;
                baseMediaSource = new DashMediaSource(uri3, mediaDataSourceFactory2, new DefaultDashChunkSource.Factory(mediaDataSourceFactory2), this.mainHandler, null);
            }
            final LoopingMediaSource loopingMediaSource = new LoopingMediaSource(baseMediaSource);
            if (i == 0) {
                mediaSource = loopingMediaSource;
            }
            else {
                mediaSource2 = loopingMediaSource;
            }
        }
        this.player.prepare(mediaSource, true, true);
        this.audioPlayer.prepare(mediaSource2, true, true);
    }
    
    public void releasePlayer(final boolean b) {
        final SimpleExoPlayer player = this.player;
        if (player != null) {
            player.release(b);
            this.player = null;
        }
        final SimpleExoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            audioPlayer.release(b);
            this.audioPlayer = null;
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.playerDidStartPlaying);
    }
    
    public void seekTo(final long n) {
        final SimpleExoPlayer player = this.player;
        if (player != null) {
            player.seekTo(n);
        }
    }
    
    public void setDelegate(final VideoPlayerDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMute(final boolean b) {
        final SimpleExoPlayer player = this.player;
        final float n = 0.0f;
        if (player != null) {
            float volume;
            if (b) {
                volume = 0.0f;
            }
            else {
                volume = 1.0f;
            }
            player.setVolume(volume);
        }
        final SimpleExoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            float volume2;
            if (b) {
                volume2 = n;
            }
            else {
                volume2 = 1.0f;
            }
            audioPlayer.setVolume(volume2);
        }
    }
    
    public void setPlayWhenReady(final boolean b) {
        this.mixedPlayWhenReady = b;
        if (b && this.mixedAudio && (!this.audioPlayerReady || !this.videoPlayerReady)) {
            final SimpleExoPlayer player = this.player;
            if (player != null) {
                player.setPlayWhenReady(false);
            }
            final SimpleExoPlayer audioPlayer = this.audioPlayer;
            if (audioPlayer != null) {
                audioPlayer.setPlayWhenReady(false);
            }
            return;
        }
        this.autoplay = b;
        final SimpleExoPlayer player2 = this.player;
        if (player2 != null) {
            player2.setPlayWhenReady(b);
        }
        final SimpleExoPlayer audioPlayer2 = this.audioPlayer;
        if (audioPlayer2 != null) {
            audioPlayer2.setPlayWhenReady(b);
        }
    }
    
    public void setPlaybackSpeed(final float n) {
        final SimpleExoPlayer player = this.player;
        if (player != null) {
            float n2 = 1.0f;
            if (n > 1.0f) {
                n2 = 0.98f;
            }
            player.setPlaybackParameters(new PlaybackParameters(n, n2));
        }
    }
    
    public void setStreamType(final int n) {
        final SimpleExoPlayer player = this.player;
        if (player != null) {
            player.setAudioStreamType(n);
        }
        final SimpleExoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            audioPlayer.setAudioStreamType(n);
        }
    }
    
    public void setTextureView(final TextureView textureView) {
        if (this.textureView == textureView) {
            return;
        }
        this.textureView = textureView;
        final SimpleExoPlayer player = this.player;
        if (player == null) {
            return;
        }
        player.setVideoTextureView(this.textureView);
    }
    
    public void setVolume(final float n) {
        final SimpleExoPlayer player = this.player;
        if (player != null) {
            player.setVolume(n);
        }
        final SimpleExoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            audioPlayer.setVolume(n);
        }
    }
    
    public interface RendererBuilder
    {
        void buildRenderers(final VideoPlayer p0);
        
        void cancel();
    }
    
    public interface VideoPlayerDelegate
    {
        void onError(final Exception p0);
        
        void onRenderedFirstFrame();
        
        void onStateChanged(final boolean p0, final int p1);
        
        boolean onSurfaceDestroyed(final SurfaceTexture p0);
        
        void onSurfaceTextureUpdated(final SurfaceTexture p0);
        
        void onVideoSizeChanged(final int p0, final int p1, final int p2, final float p3);
    }
}
