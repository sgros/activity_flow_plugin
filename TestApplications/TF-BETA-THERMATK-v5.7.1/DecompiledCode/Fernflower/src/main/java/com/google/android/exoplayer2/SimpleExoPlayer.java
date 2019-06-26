package com.google.android.exoplayer2;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.SurfaceHolder.Callback;
import android.view.TextureView.SurfaceTextureListener;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioFocusManager;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import org.telegram.messenger.Utilities;

@TargetApi(16)
public class SimpleExoPlayer extends BasePlayer implements ExoPlayer, Player.AudioComponent, Player.VideoComponent, Player.TextComponent, Player.MetadataComponent {
   private final AnalyticsCollector analyticsCollector;
   private AudioAttributes audioAttributes;
   private final CopyOnWriteArraySet audioDebugListeners;
   private DecoderCounters audioDecoderCounters;
   private final AudioFocusManager audioFocusManager;
   private Format audioFormat;
   private final CopyOnWriteArraySet audioListeners;
   private int audioSessionId;
   private float audioVolume;
   private final BandwidthMeter bandwidthMeter;
   private final SimpleExoPlayer.ComponentListener componentListener;
   private List currentCues;
   private final Handler eventHandler;
   private boolean hasNotifiedFullWrongThreadWarning;
   private MediaSource mediaSource;
   private final CopyOnWriteArraySet metadataOutputs;
   private boolean needSetSurface;
   private boolean ownsSurface;
   private final ExoPlayerImpl player;
   protected final Renderer[] renderers;
   private Surface surface;
   private int surfaceHeight;
   private SurfaceHolder surfaceHolder;
   private int surfaceWidth;
   private final CopyOnWriteArraySet textOutputs;
   private TextureView textureView;
   private final CopyOnWriteArraySet videoDebugListeners;
   private DecoderCounters videoDecoderCounters;
   private Format videoFormat;
   private final CopyOnWriteArraySet videoListeners;
   private int videoScalingMode;

   protected SimpleExoPlayer(Context var1, RenderersFactory var2, TrackSelector var3, LoadControl var4, DrmSessionManager var5, BandwidthMeter var6, AnalyticsCollector.Factory var7, Looper var8) {
      this(var1, var2, var3, var4, var5, var6, var7, Clock.DEFAULT, var8);
   }

   protected SimpleExoPlayer(Context var1, RenderersFactory var2, TrackSelector var3, LoadControl var4, DrmSessionManager var5, BandwidthMeter var6, AnalyticsCollector.Factory var7, Clock var8, Looper var9) {
      this.needSetSurface = true;
      this.bandwidthMeter = var6;
      this.componentListener = new SimpleExoPlayer.ComponentListener();
      this.videoListeners = new CopyOnWriteArraySet();
      this.audioListeners = new CopyOnWriteArraySet();
      this.textOutputs = new CopyOnWriteArraySet();
      this.metadataOutputs = new CopyOnWriteArraySet();
      this.videoDebugListeners = new CopyOnWriteArraySet();
      this.audioDebugListeners = new CopyOnWriteArraySet();
      this.eventHandler = new Handler(var9);
      Handler var10 = this.eventHandler;
      SimpleExoPlayer.ComponentListener var11 = this.componentListener;
      this.renderers = var2.createRenderers(var10, var11, var11, var11, var11, var5);
      this.audioVolume = 1.0F;
      this.audioSessionId = 0;
      this.audioAttributes = AudioAttributes.DEFAULT;
      this.videoScalingMode = 1;
      this.currentCues = Collections.emptyList();
      this.player = new ExoPlayerImpl(this.renderers, var3, var4, var6, var8, var9);
      this.analyticsCollector = var7.createAnalyticsCollector(this.player, var8);
      this.addListener(this.analyticsCollector);
      this.videoDebugListeners.add(this.analyticsCollector);
      this.videoListeners.add(this.analyticsCollector);
      this.audioDebugListeners.add(this.analyticsCollector);
      this.audioListeners.add(this.analyticsCollector);
      this.addMetadataOutput(this.analyticsCollector);
      var6.addEventListener(this.eventHandler, this.analyticsCollector);
      if (var5 instanceof DefaultDrmSessionManager) {
         ((DefaultDrmSessionManager)var5).addListener(this.eventHandler, this.analyticsCollector);
      }

      this.audioFocusManager = new AudioFocusManager(var1, this.componentListener);
   }

   private void maybeNotifySurfaceSizeChanged(int var1, int var2) {
      if (var1 != this.surfaceWidth || var2 != this.surfaceHeight) {
         this.surfaceWidth = var1;
         this.surfaceHeight = var2;
         Iterator var3 = this.videoListeners.iterator();

         while(var3.hasNext()) {
            ((com.google.android.exoplayer2.video.VideoListener)var3.next()).onSurfaceSizeChanged(var1, var2);
         }
      }

   }

   private void removeSurfaceCallbacks() {
      TextureView var1 = this.textureView;
      if (var1 != null) {
         if (var1.getSurfaceTextureListener() != this.componentListener) {
            Log.w("SimpleExoPlayer", "SurfaceTextureListener already unset or replaced.");
         } else {
            this.textureView.setSurfaceTextureListener((SurfaceTextureListener)null);
         }

         this.textureView = null;
      }

      SurfaceHolder var2 = this.surfaceHolder;
      if (var2 != null) {
         var2.removeCallback(this.componentListener);
         this.surfaceHolder = null;
      }

   }

   private void sendVolumeToRenderers() {
      float var1 = this.audioVolume;
      float var2 = this.audioFocusManager.getVolumeMultiplier();
      Renderer[] var3 = this.renderers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Renderer var6 = var3[var5];
         if (var6.getTrackType() == 1) {
            PlayerMessage var7 = this.player.createMessage(var6);
            var7.setType(2);
            var7.setPayload(var1 * var2);
            var7.send();
         }
      }

   }

   private void setVideoSurfaceInternal(Surface var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      Renderer[] var4 = this.renderers;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Renderer var7 = var4[var6];
         if (var7.getTrackType() == 2) {
            PlayerMessage var12 = this.player.createMessage(var7);
            var12.setType(1);
            var12.setPayload(var1);
            var12.send();
            var3.add(var12);
         }
      }

      Surface var11 = this.surface;
      if (var11 != null && var11 != var1) {
         label37: {
            label36: {
               boolean var10001;
               Iterator var10;
               try {
                  var10 = var3.iterator();
               } catch (InterruptedException var9) {
                  var10001 = false;
                  break label36;
               }

               while(true) {
                  try {
                     if (!var10.hasNext()) {
                        break label37;
                     }

                     ((PlayerMessage)var10.next()).blockUntilDelivered();
                  } catch (InterruptedException var8) {
                     var10001 = false;
                     break;
                  }
               }
            }

            Thread.currentThread().interrupt();
         }

         if (this.ownsSurface) {
            this.surface.release();
         }
      }

      this.surface = var1;
      this.ownsSurface = var2;
   }

   private void updatePlayWhenReady(boolean var1, int var2) {
      ExoPlayerImpl var3 = this.player;
      boolean var4 = false;
      if (var1 && var2 != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var2 != 1) {
         var4 = true;
      }

      var3.setPlayWhenReady(var1, var4);
   }

   private void verifyApplicationThread() {
      if (Looper.myLooper() != this.getApplicationLooper()) {
         IllegalStateException var1;
         if (this.hasNotifiedFullWrongThreadWarning) {
            var1 = null;
         } else {
            var1 = new IllegalStateException();
         }

         Log.w("SimpleExoPlayer", "Player is accessed on the wrong thread. See https://google.github.io/ExoPlayer/faqs.html#what-do-player-is-accessed-on-the-wrong-thread-warnings-mean", var1);
         this.hasNotifiedFullWrongThreadWarning = true;
      }

   }

   public void addListener(Player.EventListener var1) {
      this.verifyApplicationThread();
      this.player.addListener(var1);
   }

   public void addMetadataOutput(MetadataOutput var1) {
      this.metadataOutputs.add(var1);
   }

   public void addVideoListener(com.google.android.exoplayer2.video.VideoListener var1) {
      this.videoListeners.add(var1);
   }

   public Looper getApplicationLooper() {
      return this.player.getApplicationLooper();
   }

   public long getBufferedPosition() {
      this.verifyApplicationThread();
      return this.player.getBufferedPosition();
   }

   public long getContentPosition() {
      this.verifyApplicationThread();
      return this.player.getContentPosition();
   }

   public int getCurrentAdGroupIndex() {
      this.verifyApplicationThread();
      return this.player.getCurrentAdGroupIndex();
   }

   public int getCurrentAdIndexInAdGroup() {
      this.verifyApplicationThread();
      return this.player.getCurrentAdIndexInAdGroup();
   }

   public long getCurrentPosition() {
      this.verifyApplicationThread();
      return this.player.getCurrentPosition();
   }

   public Timeline getCurrentTimeline() {
      this.verifyApplicationThread();
      return this.player.getCurrentTimeline();
   }

   public int getCurrentWindowIndex() {
      this.verifyApplicationThread();
      return this.player.getCurrentWindowIndex();
   }

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

   public long getTotalBufferedDuration() {
      this.verifyApplicationThread();
      return this.player.getTotalBufferedDuration();
   }

   public float getVolume() {
      return this.audioVolume;
   }

   // $FF: synthetic method
   public void lambda$release$0$SimpleExoPlayer(boolean var1) {
      this.player.release(var1);
   }

   public void prepare(MediaSource var1, boolean var2, boolean var3) {
      this.verifyApplicationThread();
      MediaSource var4 = this.mediaSource;
      if (var4 != null) {
         var4.removeEventListener(this.analyticsCollector);
         this.analyticsCollector.resetForNewMediaSource();
      }

      this.mediaSource = var1;
      var1.addEventListener(this.eventHandler, this.analyticsCollector);
      int var5 = this.audioFocusManager.handlePrepare(this.getPlayWhenReady());
      this.updatePlayWhenReady(this.getPlayWhenReady(), var5);
      this.player.prepare(var1, var2, var3);
   }

   public void release(boolean var1) {
      this.audioFocusManager.handleStop();
      if (var1) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$SimpleExoPlayer$lgd4w0uJZdq_ub9v7S9pJHpswBY(this, var1));
      } else {
         this.player.release(var1);
      }

      this.removeSurfaceCallbacks();
      Surface var2 = this.surface;
      if (var2 != null) {
         if (this.ownsSurface) {
            var2.release();
         }

         this.surface = null;
      }

      MediaSource var3 = this.mediaSource;
      if (var3 != null) {
         var3.removeEventListener(this.analyticsCollector);
         this.mediaSource = null;
      }

      this.bandwidthMeter.removeEventListener(this.analyticsCollector);
      this.currentCues = Collections.emptyList();
   }

   public void seekTo(int var1, long var2) {
      this.verifyApplicationThread();
      this.analyticsCollector.notifySeekStarted();
      this.player.seekTo(var1, var2);
   }

   public void setAudioAttributes(AudioAttributes var1) {
      this.setAudioAttributes(var1, false);
   }

   public void setAudioAttributes(AudioAttributes var1, boolean var2) {
      this.verifyApplicationThread();
      int var5;
      if (!Util.areEqual(this.audioAttributes, var1)) {
         this.audioAttributes = var1;
         Renderer[] var3 = this.renderers;
         int var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            Renderer var6 = var3[var5];
            if (var6.getTrackType() == 1) {
               PlayerMessage var9 = this.player.createMessage(var6);
               var9.setType(3);
               var9.setPayload(var1);
               var9.send();
            }
         }

         Iterator var7 = this.audioListeners.iterator();

         while(var7.hasNext()) {
            ((AudioListener)var7.next()).onAudioAttributesChanged(var1);
         }
      }

      AudioFocusManager var8 = this.audioFocusManager;
      if (!var2) {
         var1 = null;
      }

      var5 = var8.setAudioAttributes(var1, this.getPlayWhenReady(), this.getPlaybackState());
      this.updatePlayWhenReady(this.getPlayWhenReady(), var5);
   }

   @Deprecated
   public void setAudioStreamType(int var1) {
      int var2 = Util.getAudioUsageForStreamType(var1);
      var1 = Util.getAudioContentTypeForStreamType(var1);
      AudioAttributes.Builder var3 = new AudioAttributes.Builder();
      var3.setUsage(var2);
      var3.setContentType(var1);
      this.setAudioAttributes(var3.build());
   }

   public void setPlayWhenReady(boolean var1) {
      this.verifyApplicationThread();
      this.updatePlayWhenReady(var1, this.audioFocusManager.handleSetPlayWhenReady(var1, this.getPlaybackState()));
   }

   public void setPlaybackParameters(PlaybackParameters var1) {
      this.verifyApplicationThread();
      this.player.setPlaybackParameters(var1);
   }

   @Deprecated
   public void setVideoListener(SimpleExoPlayer.VideoListener var1) {
      this.videoListeners.clear();
      if (var1 != null) {
         this.addVideoListener(var1);
      }

   }

   public void setVideoTextureView(TextureView var1) {
      if (this.textureView != var1) {
         this.verifyApplicationThread();
         this.removeSurfaceCallbacks();
         this.textureView = var1;
         this.needSetSurface = true;
         if (var1 == null) {
            this.setVideoSurfaceInternal((Surface)null, true);
            this.maybeNotifySurfaceSizeChanged(0, 0);
         } else {
            if (var1.getSurfaceTextureListener() != null) {
               Log.w("SimpleExoPlayer", "Replacing existing SurfaceTextureListener.");
            }

            var1.setSurfaceTextureListener(this.componentListener);
            SurfaceTexture var2;
            if (var1.isAvailable()) {
               var2 = var1.getSurfaceTexture();
            } else {
               var2 = null;
            }

            if (var2 == null) {
               this.setVideoSurfaceInternal((Surface)null, true);
               this.maybeNotifySurfaceSizeChanged(0, 0);
            } else {
               this.setVideoSurfaceInternal(new Surface(var2), true);
               this.maybeNotifySurfaceSizeChanged(var1.getWidth(), var1.getHeight());
            }
         }

      }
   }

   public void setVolume(float var1) {
      this.verifyApplicationThread();
      var1 = Util.constrainValue(var1, 0.0F, 1.0F);
      if (this.audioVolume != var1) {
         this.audioVolume = var1;
         this.sendVolumeToRenderers();
         Iterator var2 = this.audioListeners.iterator();

         while(var2.hasNext()) {
            ((AudioListener)var2.next()).onVolumeChanged(var1);
         }

      }
   }

   private final class ComponentListener implements VideoRendererEventListener, AudioRendererEventListener, TextOutput, MetadataOutput, Callback, SurfaceTextureListener, AudioFocusManager.PlayerControl {
      private ComponentListener() {
      }

      // $FF: synthetic method
      ComponentListener(Object var2) {
         this();
      }

      public void executePlayerCommand(int var1) {
         SimpleExoPlayer var2 = SimpleExoPlayer.this;
         var2.updatePlayWhenReady(var2.getPlayWhenReady(), var1);
      }

      public void onAudioDecoderInitialized(String var1, long var2, long var4) {
         Iterator var6 = SimpleExoPlayer.this.audioDebugListeners.iterator();

         while(var6.hasNext()) {
            ((AudioRendererEventListener)var6.next()).onAudioDecoderInitialized(var1, var2, var4);
         }

      }

      public void onAudioDisabled(DecoderCounters var1) {
         Iterator var2 = SimpleExoPlayer.this.audioDebugListeners.iterator();

         while(var2.hasNext()) {
            ((AudioRendererEventListener)var2.next()).onAudioDisabled(var1);
         }

         SimpleExoPlayer.this.audioFormat = null;
         SimpleExoPlayer.this.audioDecoderCounters = null;
         SimpleExoPlayer.this.audioSessionId = 0;
      }

      public void onAudioEnabled(DecoderCounters var1) {
         SimpleExoPlayer.this.audioDecoderCounters = var1;
         Iterator var2 = SimpleExoPlayer.this.audioDebugListeners.iterator();

         while(var2.hasNext()) {
            ((AudioRendererEventListener)var2.next()).onAudioEnabled(var1);
         }

      }

      public void onAudioInputFormatChanged(Format var1) {
         SimpleExoPlayer.this.audioFormat = var1;
         Iterator var2 = SimpleExoPlayer.this.audioDebugListeners.iterator();

         while(var2.hasNext()) {
            ((AudioRendererEventListener)var2.next()).onAudioInputFormatChanged(var1);
         }

      }

      public void onAudioSessionId(int var1) {
         if (SimpleExoPlayer.this.audioSessionId != var1) {
            SimpleExoPlayer.this.audioSessionId = var1;
            Iterator var2 = SimpleExoPlayer.this.audioListeners.iterator();

            while(var2.hasNext()) {
               AudioListener var3 = (AudioListener)var2.next();
               if (!SimpleExoPlayer.this.audioDebugListeners.contains(var3)) {
                  var3.onAudioSessionId(var1);
               }
            }

            var2 = SimpleExoPlayer.this.audioDebugListeners.iterator();

            while(var2.hasNext()) {
               ((AudioRendererEventListener)var2.next()).onAudioSessionId(var1);
            }

         }
      }

      public void onAudioSinkUnderrun(int var1, long var2, long var4) {
         Iterator var6 = SimpleExoPlayer.this.audioDebugListeners.iterator();

         while(var6.hasNext()) {
            ((AudioRendererEventListener)var6.next()).onAudioSinkUnderrun(var1, var2, var4);
         }

      }

      public void onCues(List var1) {
         SimpleExoPlayer.this.currentCues = var1;
         Iterator var2 = SimpleExoPlayer.this.textOutputs.iterator();

         while(var2.hasNext()) {
            ((TextOutput)var2.next()).onCues(var1);
         }

      }

      public void onDroppedFrames(int var1, long var2) {
         Iterator var4 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var4.hasNext()) {
            ((VideoRendererEventListener)var4.next()).onDroppedFrames(var1, var2);
         }

      }

      public void onMetadata(Metadata var1) {
         Iterator var2 = SimpleExoPlayer.this.metadataOutputs.iterator();

         while(var2.hasNext()) {
            ((MetadataOutput)var2.next()).onMetadata(var1);
         }

      }

      public void onRenderedFirstFrame(Surface var1) {
         Iterator var2;
         if (SimpleExoPlayer.this.surface == var1) {
            var2 = SimpleExoPlayer.this.videoListeners.iterator();

            while(var2.hasNext()) {
               ((com.google.android.exoplayer2.video.VideoListener)var2.next()).onRenderedFirstFrame();
            }
         }

         var2 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var2.hasNext()) {
            ((VideoRendererEventListener)var2.next()).onRenderedFirstFrame(var1);
         }

      }

      public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
         if (SimpleExoPlayer.this.needSetSurface) {
            SimpleExoPlayer.this.setVideoSurfaceInternal(new Surface(var1), true);
            SimpleExoPlayer.this.needSetSurface = false;
         }

         SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(var2, var3);
      }

      public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
         Iterator var2 = SimpleExoPlayer.this.videoListeners.iterator();

         do {
            if (!var2.hasNext()) {
               SimpleExoPlayer.this.setVideoSurfaceInternal((Surface)null, true);
               SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(0, 0);
               SimpleExoPlayer.this.needSetSurface = true;
               return true;
            }
         } while(!((com.google.android.exoplayer2.video.VideoListener)var2.next()).onSurfaceDestroyed(var1));

         return false;
      }

      public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
         SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(var2, var3);
      }

      public void onSurfaceTextureUpdated(SurfaceTexture var1) {
         Iterator var2 = SimpleExoPlayer.this.videoListeners.iterator();

         while(var2.hasNext()) {
            ((com.google.android.exoplayer2.video.VideoListener)var2.next()).onSurfaceTextureUpdated(var1);
         }

      }

      public void onVideoDecoderInitialized(String var1, long var2, long var4) {
         Iterator var6 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var6.hasNext()) {
            ((VideoRendererEventListener)var6.next()).onVideoDecoderInitialized(var1, var2, var4);
         }

      }

      public void onVideoDisabled(DecoderCounters var1) {
         Iterator var2 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var2.hasNext()) {
            ((VideoRendererEventListener)var2.next()).onVideoDisabled(var1);
         }

         SimpleExoPlayer.this.videoFormat = null;
         SimpleExoPlayer.this.videoDecoderCounters = null;
      }

      public void onVideoEnabled(DecoderCounters var1) {
         SimpleExoPlayer.this.videoDecoderCounters = var1;
         Iterator var2 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var2.hasNext()) {
            ((VideoRendererEventListener)var2.next()).onVideoEnabled(var1);
         }

      }

      public void onVideoInputFormatChanged(Format var1) {
         SimpleExoPlayer.this.videoFormat = var1;
         Iterator var2 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var2.hasNext()) {
            ((VideoRendererEventListener)var2.next()).onVideoInputFormatChanged(var1);
         }

      }

      public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
         Iterator var5 = SimpleExoPlayer.this.videoListeners.iterator();

         while(var5.hasNext()) {
            com.google.android.exoplayer2.video.VideoListener var6 = (com.google.android.exoplayer2.video.VideoListener)var5.next();
            if (!SimpleExoPlayer.this.videoDebugListeners.contains(var6)) {
               var6.onVideoSizeChanged(var1, var2, var3, var4);
            }
         }

         var5 = SimpleExoPlayer.this.videoDebugListeners.iterator();

         while(var5.hasNext()) {
            ((VideoRendererEventListener)var5.next()).onVideoSizeChanged(var1, var2, var3, var4);
         }

      }

      public void setVolumeMultiplier(float var1) {
         SimpleExoPlayer.this.sendVolumeToRenderers();
      }

      public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
         SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(var3, var4);
      }

      public void surfaceCreated(SurfaceHolder var1) {
         SimpleExoPlayer.this.setVideoSurfaceInternal(var1.getSurface(), false);
      }

      public void surfaceDestroyed(SurfaceHolder var1) {
         SimpleExoPlayer.this.setVideoSurfaceInternal((Surface)null, false);
         SimpleExoPlayer.this.maybeNotifySurfaceSizeChanged(0, 0);
      }
   }

   @Deprecated
   public interface VideoListener extends com.google.android.exoplayer2.video.VideoListener {
   }
}
