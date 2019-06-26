// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.metadata.Metadata;
import android.graphics.SurfaceTexture;
import com.google.android.exoplayer2.util.Util;
import org.telegram.messenger.Utilities;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import java.util.ArrayList;
import android.view.SurfaceHolder$Callback;
import android.view.TextureView$SurfaceTextureListener;
import com.google.android.exoplayer2.util.Log;
import java.util.Iterator;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import java.util.Collections;
import com.google.android.exoplayer2.util.Clock;
import android.os.Looper;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import android.content.Context;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import android.view.TextureView;
import com.google.android.exoplayer2.text.TextOutput;
import android.view.SurfaceHolder;
import android.view.Surface;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.MediaSource;
import android.os.Handler;
import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.audio.AudioFocusManager;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import java.util.concurrent.CopyOnWriteArraySet;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import android.annotation.TargetApi;

@TargetApi(16)
public class SimpleExoPlayer extends BasePlayer implements ExoPlayer, AudioComponent, VideoComponent, TextComponent, MetadataComponent
{
    private final AnalyticsCollector analyticsCollector;
    private AudioAttributes audioAttributes;
    private final CopyOnWriteArraySet<AudioRendererEventListener> audioDebugListeners;
    private DecoderCounters audioDecoderCounters;
    private final AudioFocusManager audioFocusManager;
    private Format audioFormat;
    private final CopyOnWriteArraySet<AudioListener> audioListeners;
    private int audioSessionId;
    private float audioVolume;
    private final BandwidthMeter bandwidthMeter;
    private final ComponentListener componentListener;
    private List<Cue> currentCues;
    private final Handler eventHandler;
    private boolean hasNotifiedFullWrongThreadWarning;
    private MediaSource mediaSource;
    private final CopyOnWriteArraySet<MetadataOutput> metadataOutputs;
    private boolean needSetSurface;
    private boolean ownsSurface;
    private final ExoPlayerImpl player;
    protected final Renderer[] renderers;
    private Surface surface;
    private int surfaceHeight;
    private SurfaceHolder surfaceHolder;
    private int surfaceWidth;
    private final CopyOnWriteArraySet<TextOutput> textOutputs;
    private TextureView textureView;
    private final CopyOnWriteArraySet<VideoRendererEventListener> videoDebugListeners;
    private DecoderCounters videoDecoderCounters;
    private Format videoFormat;
    private final CopyOnWriteArraySet<com.google.android.exoplayer2.video.VideoListener> videoListeners;
    private int videoScalingMode;
    
    protected SimpleExoPlayer(final Context context, final RenderersFactory renderersFactory, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final BandwidthMeter bandwidthMeter, final AnalyticsCollector.Factory factory, final Looper looper) {
        this(context, renderersFactory, trackSelector, loadControl, drmSessionManager, bandwidthMeter, factory, Clock.DEFAULT, looper);
    }
    
    protected SimpleExoPlayer(final Context context, final RenderersFactory renderersFactory, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final BandwidthMeter bandwidthMeter, final AnalyticsCollector.Factory factory, final Clock clock, final Looper looper) {
        this.needSetSurface = true;
        this.bandwidthMeter = bandwidthMeter;
        this.componentListener = new ComponentListener();
        this.videoListeners = new CopyOnWriteArraySet<com.google.android.exoplayer2.video.VideoListener>();
        this.audioListeners = new CopyOnWriteArraySet<AudioListener>();
        this.textOutputs = new CopyOnWriteArraySet<TextOutput>();
        this.metadataOutputs = new CopyOnWriteArraySet<MetadataOutput>();
        this.videoDebugListeners = new CopyOnWriteArraySet<VideoRendererEventListener>();
        this.audioDebugListeners = new CopyOnWriteArraySet<AudioRendererEventListener>();
        this.eventHandler = new Handler(looper);
        final Handler eventHandler = this.eventHandler;
        final ComponentListener componentListener = this.componentListener;
        this.renderers = renderersFactory.createRenderers(eventHandler, componentListener, componentListener, componentListener, componentListener, drmSessionManager);
        this.audioVolume = 1.0f;
        this.audioSessionId = 0;
        this.audioAttributes = AudioAttributes.DEFAULT;
        this.videoScalingMode = 1;
        this.currentCues = Collections.emptyList();
        this.player = new ExoPlayerImpl(this.renderers, trackSelector, loadControl, bandwidthMeter, clock, looper);
        this.addListener(this.analyticsCollector = factory.createAnalyticsCollector(this.player, clock));
        this.videoDebugListeners.add(this.analyticsCollector);
        this.videoListeners.add(this.analyticsCollector);
        this.audioDebugListeners.add(this.analyticsCollector);
        this.audioListeners.add(this.analyticsCollector);
        this.addMetadataOutput(this.analyticsCollector);
        bandwidthMeter.addEventListener(this.eventHandler, (BandwidthMeter.EventListener)this.analyticsCollector);
        if (drmSessionManager instanceof DefaultDrmSessionManager) {
            ((DefaultDrmSessionManager)drmSessionManager).addListener(this.eventHandler, this.analyticsCollector);
        }
        this.audioFocusManager = new AudioFocusManager(context, (AudioFocusManager.PlayerControl)this.componentListener);
    }
    
    private void maybeNotifySurfaceSizeChanged(final int surfaceWidth, final int surfaceHeight) {
        if (surfaceWidth != this.surfaceWidth || surfaceHeight != this.surfaceHeight) {
            this.surfaceWidth = surfaceWidth;
            this.surfaceHeight = surfaceHeight;
            final Iterator<com.google.android.exoplayer2.video.VideoListener> iterator = this.videoListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onSurfaceSizeChanged(surfaceWidth, surfaceHeight);
            }
        }
    }
    
    private void removeSurfaceCallbacks() {
        final TextureView textureView = this.textureView;
        if (textureView != null) {
            if (textureView.getSurfaceTextureListener() != this.componentListener) {
                Log.w("SimpleExoPlayer", "SurfaceTextureListener already unset or replaced.");
            }
            else {
                this.textureView.setSurfaceTextureListener((TextureView$SurfaceTextureListener)null);
            }
            this.textureView = null;
        }
        final SurfaceHolder surfaceHolder = this.surfaceHolder;
        if (surfaceHolder != null) {
            surfaceHolder.removeCallback((SurfaceHolder$Callback)this.componentListener);
            this.surfaceHolder = null;
        }
    }
    
    private void sendVolumeToRenderers() {
        final float audioVolume = this.audioVolume;
        final float volumeMultiplier = this.audioFocusManager.getVolumeMultiplier();
        for (final Renderer renderer : this.renderers) {
            if (renderer.getTrackType() == 1) {
                final PlayerMessage message = this.player.createMessage(renderer);
                message.setType(2);
                message.setPayload(audioVolume * volumeMultiplier);
                message.send();
            }
        }
    }
    
    private void setVideoSurfaceInternal(final Surface surface, final boolean ownsSurface) {
        final ArrayList<PlayerMessage> list = new ArrayList<PlayerMessage>();
        for (final Renderer renderer : this.renderers) {
            if (renderer.getTrackType() == 2) {
                final PlayerMessage message = this.player.createMessage(renderer);
                message.setType(1);
                message.setPayload(surface);
                message.send();
                list.add(message);
            }
        }
        final Surface surface2 = this.surface;
        if (surface2 != null && surface2 != surface) {
            try {
                final Iterator<Object> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().blockUntilDelivered();
                }
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (this.ownsSurface) {
                this.surface.release();
            }
        }
        this.surface = surface;
        this.ownsSurface = ownsSurface;
    }
    
    private void updatePlayWhenReady(final boolean b, final int n) {
        final ExoPlayerImpl player = this.player;
        boolean b2 = false;
        final boolean b3 = b && n != -1;
        if (n != 1) {
            b2 = true;
        }
        player.setPlayWhenReady(b3, b2);
    }
    
    private void verifyApplicationThread() {
        if (Looper.myLooper() != this.getApplicationLooper()) {
            Throwable t;
            if (this.hasNotifiedFullWrongThreadWarning) {
                t = null;
            }
            else {
                t = new IllegalStateException();
            }
            Log.w("SimpleExoPlayer", "Player is accessed on the wrong thread. See https://google.github.io/ExoPlayer/faqs.html#what-do-player-is-accessed-on-the-wrong-thread-warnings-mean", t);
            this.hasNotifiedFullWrongThreadWarning = true;
        }
    }
    
    public void addListener(final EventListener eventListener) {
        this.verifyApplicationThread();
        this.player.addListener(eventListener);
    }
    
    public void addMetadataOutput(final MetadataOutput e) {
        this.metadataOutputs.add(e);
    }
    
    public void addVideoListener(final com.google.android.exoplayer2.video.VideoListener e) {
        this.videoListeners.add(e);
    }
    
    public Looper getApplicationLooper() {
        return this.player.getApplicationLooper();
    }
    
    @Override
    public long getBufferedPosition() {
        this.verifyApplicationThread();
        return this.player.getBufferedPosition();
    }
    
    @Override
    public long getContentPosition() {
        this.verifyApplicationThread();
        return this.player.getContentPosition();
    }
    
    @Override
    public int getCurrentAdGroupIndex() {
        this.verifyApplicationThread();
        return this.player.getCurrentAdGroupIndex();
    }
    
    @Override
    public int getCurrentAdIndexInAdGroup() {
        this.verifyApplicationThread();
        return this.player.getCurrentAdIndexInAdGroup();
    }
    
    @Override
    public long getCurrentPosition() {
        this.verifyApplicationThread();
        return this.player.getCurrentPosition();
    }
    
    @Override
    public Timeline getCurrentTimeline() {
        this.verifyApplicationThread();
        return this.player.getCurrentTimeline();
    }
    
    @Override
    public int getCurrentWindowIndex() {
        this.verifyApplicationThread();
        return this.player.getCurrentWindowIndex();
    }
    
    @Override
    public long getDuration() {
        this.verifyApplicationThread();
        return this.player.getDuration();
    }
    
    public boolean getPlayWhenReady() {
        this.verifyApplicationThread();
        return this.player.getPlayWhenReady();
    }
    
    public int getPlaybackState() {
        this.verifyApplicationThread();
        return this.player.getPlaybackState();
    }
    
    @Override
    public long getTotalBufferedDuration() {
        this.verifyApplicationThread();
        return this.player.getTotalBufferedDuration();
    }
    
    public float getVolume() {
        return this.audioVolume;
    }
    
    public void prepare(final MediaSource mediaSource, final boolean b, final boolean b2) {
        this.verifyApplicationThread();
        final MediaSource mediaSource2 = this.mediaSource;
        if (mediaSource2 != null) {
            mediaSource2.removeEventListener(this.analyticsCollector);
            this.analyticsCollector.resetForNewMediaSource();
        }
        (this.mediaSource = mediaSource).addEventListener(this.eventHandler, this.analyticsCollector);
        this.updatePlayWhenReady(this.getPlayWhenReady(), this.audioFocusManager.handlePrepare(this.getPlayWhenReady()));
        this.player.prepare(mediaSource, b, b2);
    }
    
    public void release(final boolean b) {
        this.audioFocusManager.handleStop();
        if (b) {
            Utilities.globalQueue.postRunnable(new _$$Lambda$SimpleExoPlayer$lgd4w0uJZdq_ub9v7S9pJHpswBY(this, b));
        }
        else {
            this.player.release(b);
        }
        this.removeSurfaceCallbacks();
        final Surface surface = this.surface;
        if (surface != null) {
            if (this.ownsSurface) {
                surface.release();
            }
            this.surface = null;
        }
        final MediaSource mediaSource = this.mediaSource;
        if (mediaSource != null) {
            mediaSource.removeEventListener(this.analyticsCollector);
            this.mediaSource = null;
        }
        this.bandwidthMeter.removeEventListener((BandwidthMeter.EventListener)this.analyticsCollector);
        this.currentCues = Collections.emptyList();
    }
    
    @Override
    public void seekTo(final int n, final long n2) {
        this.verifyApplicationThread();
        this.analyticsCollector.notifySeekStarted();
        this.player.seekTo(n, n2);
    }
    
    public void setAudioAttributes(final AudioAttributes audioAttributes) {
        this.setAudioAttributes(audioAttributes, false);
    }
    
    public void setAudioAttributes(AudioAttributes audioAttributes, final boolean b) {
        this.verifyApplicationThread();
        if (!Util.areEqual(this.audioAttributes, audioAttributes)) {
            this.audioAttributes = audioAttributes;
            for (final Renderer renderer : this.renderers) {
                if (renderer.getTrackType() == 1) {
                    final PlayerMessage message = this.player.createMessage(renderer);
                    message.setType(3);
                    message.setPayload(audioAttributes);
                    message.send();
                }
            }
            final Iterator<AudioListener> iterator = this.audioListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onAudioAttributesChanged(audioAttributes);
            }
        }
        final AudioFocusManager audioFocusManager = this.audioFocusManager;
        if (!b) {
            audioAttributes = null;
        }
        this.updatePlayWhenReady(this.getPlayWhenReady(), audioFocusManager.setAudioAttributes(audioAttributes, this.getPlayWhenReady(), this.getPlaybackState()));
    }
    
    @Deprecated
    public void setAudioStreamType(int audioContentTypeForStreamType) {
        final int audioUsageForStreamType = Util.getAudioUsageForStreamType(audioContentTypeForStreamType);
        audioContentTypeForStreamType = Util.getAudioContentTypeForStreamType(audioContentTypeForStreamType);
        final AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(audioUsageForStreamType);
        builder.setContentType(audioContentTypeForStreamType);
        this.setAudioAttributes(builder.build());
    }
    
    public void setPlayWhenReady(final boolean b) {
        this.verifyApplicationThread();
        this.updatePlayWhenReady(b, this.audioFocusManager.handleSetPlayWhenReady(b, this.getPlaybackState()));
    }
    
    public void setPlaybackParameters(final PlaybackParameters playbackParameters) {
        this.verifyApplicationThread();
        this.player.setPlaybackParameters(playbackParameters);
    }
    
    @Deprecated
    public void setVideoListener(final VideoListener videoListener) {
        this.videoListeners.clear();
        if (videoListener != null) {
            this.addVideoListener(videoListener);
        }
    }
    
    public void setVideoTextureView(final TextureView textureView) {
        if (this.textureView == textureView) {
            return;
        }
        this.verifyApplicationThread();
        this.removeSurfaceCallbacks();
        this.textureView = textureView;
        this.needSetSurface = true;
        if (textureView == null) {
            this.setVideoSurfaceInternal(null, true);
            this.maybeNotifySurfaceSizeChanged(0, 0);
        }
        else {
            if (textureView.getSurfaceTextureListener() != null) {
                Log.w("SimpleExoPlayer", "Replacing existing SurfaceTextureListener.");
            }
            textureView.setSurfaceTextureListener((TextureView$SurfaceTextureListener)this.componentListener);
            SurfaceTexture surfaceTexture;
            if (textureView.isAvailable()) {
                surfaceTexture = textureView.getSurfaceTexture();
            }
            else {
                surfaceTexture = null;
            }
            if (surfaceTexture == null) {
                this.setVideoSurfaceInternal(null, true);
                this.maybeNotifySurfaceSizeChanged(0, 0);
            }
            else {
                this.setVideoSurfaceInternal(new Surface(surfaceTexture), true);
                this.maybeNotifySurfaceSizeChanged(textureView.getWidth(), textureView.getHeight());
            }
        }
    }
    
    public void setVolume(float constrainValue) {
        this.verifyApplicationThread();
        constrainValue = Util.constrainValue(constrainValue, 0.0f, 1.0f);
        if (this.audioVolume == constrainValue) {
            return;
        }
        this.audioVolume = constrainValue;
        this.sendVolumeToRenderers();
        final Iterator<AudioListener> iterator = this.audioListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onVolumeChanged(constrainValue);
        }
    }
    
    private final class ComponentListener implements VideoRendererEventListener, AudioRendererEventListener, TextOutput, MetadataOutput, SurfaceHolder$Callback, TextureView$SurfaceTextureListener, PlayerControl
    {
        public void executePlayerCommand(final int n) {
            final SimpleExoPlayer this$0 = SimpleExoPlayer.this;
            this$0.updatePlayWhenReady(this$0.getPlayWhenReady(), n);
        }
        
        @Override
        public void onAudioDecoderInitialized(final String s, final long n, final long n2) {
            final Iterator<AudioRendererEventListener> iterator = SimpleExoPlayer.this.audioDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onAudioDecoderInitialized(s, n, n2);
            }
        }
        
        @Override
        public void onAudioDisabled(final DecoderCounters decoderCounters) {
            final Iterator<AudioRendererEventListener> iterator = SimpleExoPlayer.this.audioDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onAudioDisabled(decoderCounters);
            }
            SimpleExoPlayer.this.audioFormat = null;
            SimpleExoPlayer.this.audioDecoderCounters = null;
            SimpleExoPlayer.this.audioSessionId = 0;
        }
        
        @Override
        public void onAudioEnabled(final DecoderCounters decoderCounters) {
            SimpleExoPlayer.this.audioDecoderCounters = decoderCounters;
            final Iterator<AudioRendererEventListener> iterator = SimpleExoPlayer.this.audioDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onAudioEnabled(decoderCounters);
            }
        }
        
        @Override
        public void onAudioInputFormatChanged(final Format format) {
            SimpleExoPlayer.this.audioFormat = format;
            final Iterator<AudioRendererEventListener> iterator = SimpleExoPlayer.this.audioDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onAudioInputFormatChanged(format);
            }
        }
        
        @Override
        public void onAudioSessionId(final int n) {
            if (SimpleExoPlayer.this.audioSessionId == n) {
                return;
            }
            SimpleExoPlayer.this.audioSessionId = n;
            for (final AudioListener o : SimpleExoPlayer.this.audioListeners) {
                if (!SimpleExoPlayer.this.audioDebugListeners.contains(o)) {
                    o.onAudioSessionId(n);
                }
            }
            final Iterator<AudioRendererEventListener> iterator2 = SimpleExoPlayer.this.audioDebugListeners.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onAudioSessionId(n);
            }
        }
        
        @Override
        public void onAudioSinkUnderrun(final int n, final long n2, final long n3) {
            final Iterator<AudioRendererEventListener> iterator = SimpleExoPlayer.this.audioDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onAudioSinkUnderrun(n, n2, n3);
            }
        }
        
        @Override
        public void onCues(final List<Cue> list) {
            SimpleExoPlayer.this.currentCues = list;
            final Iterator<TextOutput> iterator = SimpleExoPlayer.this.textOutputs.iterator();
            while (iterator.hasNext()) {
                iterator.next().onCues(list);
            }
        }
        
        @Override
        public void onDroppedFrames(final int n, final long n2) {
            final Iterator<VideoRendererEventListener> iterator = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onDroppedFrames(n, n2);
            }
        }
        
        @Override
        public void onMetadata(final Metadata metadata) {
            final Iterator<MetadataOutput> iterator = SimpleExoPlayer.this.metadataOutputs.iterator();
            while (iterator.hasNext()) {
                iterator.next().onMetadata(metadata);
            }
        }
        
        @Override
        public void onRenderedFirstFrame(final Surface surface) {
            if (SimpleExoPlayer.this.surface == surface) {
                final Iterator<com.google.android.exoplayer2.video.VideoListener> iterator = SimpleExoPlayer.this.videoListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onRenderedFirstFrame();
                }
            }
            final Iterator<VideoRendererEventListener> iterator2 = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onRenderedFirstFrame(surface);
            }
        }
        
        public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            if (SimpleExoPlayer.this.needSetSurface) {
                SimpleExoPlayer.this.setVideoSurfaceInternal(new Surface(surfaceTexture), true);
                SimpleExoPlayer.this.needSetSurface = false;
            }
            SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(n, n2);
        }
        
        public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
            final Iterator<com.google.android.exoplayer2.video.VideoListener> iterator = SimpleExoPlayer.this.videoListeners.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().onSurfaceDestroyed(surfaceTexture)) {
                    return false;
                }
            }
            SimpleExoPlayer.this.setVideoSurfaceInternal(null, true);
            SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(0, 0);
            SimpleExoPlayer.this.needSetSurface = true;
            return true;
        }
        
        public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(n, n2);
        }
        
        public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            final Iterator<com.google.android.exoplayer2.video.VideoListener> iterator = SimpleExoPlayer.this.videoListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onSurfaceTextureUpdated(surfaceTexture);
            }
        }
        
        @Override
        public void onVideoDecoderInitialized(final String s, final long n, final long n2) {
            final Iterator<VideoRendererEventListener> iterator = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onVideoDecoderInitialized(s, n, n2);
            }
        }
        
        @Override
        public void onVideoDisabled(final DecoderCounters decoderCounters) {
            final Iterator<VideoRendererEventListener> iterator = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onVideoDisabled(decoderCounters);
            }
            SimpleExoPlayer.this.videoFormat = null;
            SimpleExoPlayer.this.videoDecoderCounters = null;
        }
        
        @Override
        public void onVideoEnabled(final DecoderCounters decoderCounters) {
            SimpleExoPlayer.this.videoDecoderCounters = decoderCounters;
            final Iterator<VideoRendererEventListener> iterator = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onVideoEnabled(decoderCounters);
            }
        }
        
        @Override
        public void onVideoInputFormatChanged(final Format format) {
            SimpleExoPlayer.this.videoFormat = format;
            final Iterator<VideoRendererEventListener> iterator = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onVideoInputFormatChanged(format);
            }
        }
        
        @Override
        public void onVideoSizeChanged(final int n, final int n2, final int n3, final float n4) {
            for (final com.google.android.exoplayer2.video.VideoListener o : SimpleExoPlayer.this.videoListeners) {
                if (!SimpleExoPlayer.this.videoDebugListeners.contains(o)) {
                    o.onVideoSizeChanged(n, n2, n3, n4);
                }
            }
            final Iterator<VideoRendererEventListener> iterator2 = SimpleExoPlayer.this.videoDebugListeners.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onVideoSizeChanged(n, n2, n3, n4);
            }
        }
        
        public void setVolumeMultiplier(final float n) {
            SimpleExoPlayer.this.sendVolumeToRenderers();
        }
        
        public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
            SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(n2, n3);
        }
        
        public void surfaceCreated(final SurfaceHolder surfaceHolder) {
            SimpleExoPlayer.this.setVideoSurfaceInternal(surfaceHolder.getSurface(), false);
        }
        
        public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
            SimpleExoPlayer.this.setVideoSurfaceInternal(null, false);
            SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(0, 0);
        }
    }
    
    @Deprecated
    public interface VideoListener extends com.google.android.exoplayer2.video.VideoListener
    {
    }
}
