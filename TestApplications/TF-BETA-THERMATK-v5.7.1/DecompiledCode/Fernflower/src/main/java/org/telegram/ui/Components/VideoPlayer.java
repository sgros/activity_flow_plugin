package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.view.TextureView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.secretmedia.ExtendedDefaultDataSourceFactory;

@SuppressLint({"NewApi"})
public class VideoPlayer implements Player.EventListener, SimpleExoPlayer.VideoListener, NotificationCenter.NotificationCenterDelegate {
   private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
   private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
   private static final int RENDERER_BUILDING_STATE_BUILT = 3;
   private static final int RENDERER_BUILDING_STATE_IDLE = 1;
   private SimpleExoPlayer audioPlayer;
   private boolean audioPlayerReady;
   private boolean autoplay;
   private VideoPlayer.VideoPlayerDelegate delegate;
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

   public VideoPlayer() {
      Context var1 = ApplicationLoader.applicationContext;
      DefaultBandwidthMeter var2 = BANDWIDTH_METER;
      this.mediaDataSourceFactory = new ExtendedDefaultDataSourceFactory(var1, var2, new DefaultHttpDataSourceFactory("Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)", var2));
      this.mainHandler = new Handler();
      this.trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(BANDWIDTH_METER));
      this.lastReportedPlaybackState = 1;
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.playerDidStartPlaying);
   }

   private void checkPlayersReady() {
      if (this.audioPlayerReady && this.videoPlayerReady && this.mixedPlayWhenReady) {
         this.play();
      }

   }

   private void ensurePleyaerCreated() {
      DefaultLoadControl var1 = new DefaultLoadControl(new DefaultAllocator(true, 65536), 15000, 50000, 100, 5000, -1, true);
      if (this.player == null) {
         this.player = ExoPlayerFactory.newSimpleInstance(ApplicationLoader.applicationContext, this.trackSelector, var1, (DrmSessionManager)null, 2);
         this.player.addListener(this);
         this.player.setVideoListener(this);
         this.player.setVideoTextureView(this.textureView);
         this.player.setPlayWhenReady(this.autoplay);
      }

      if (this.mixedAudio && this.audioPlayer == null) {
         this.audioPlayer = ExoPlayerFactory.newSimpleInstance(ApplicationLoader.applicationContext, this.trackSelector, var1, (DrmSessionManager)null, 2);
         this.audioPlayer.addListener(new Player.EventListener() {
            public void onLoadingChanged(boolean var1) {
            }

            public void onPlaybackParametersChanged(PlaybackParameters var1) {
            }

            public void onPlayerError(ExoPlaybackException var1) {
            }

            public void onPlayerStateChanged(boolean var1, int var2) {
               if (!VideoPlayer.this.audioPlayerReady && var2 == 3) {
                  VideoPlayer.this.audioPlayerReady = true;
                  VideoPlayer.this.checkPlayersReady();
               }

            }

            public void onPositionDiscontinuity(int var1) {
            }

            public void onRepeatModeChanged(int var1) {
            }

            public void onSeekProcessed() {
            }

            public void onShuffleModeEnabledChanged(boolean var1) {
            }

            public void onTimelineChanged(Timeline var1, Object var2, int var3) {
            }

            public void onTracksChanged(TrackGroupArray var1, TrackSelectionArray var2) {
            }
         });
         this.audioPlayer.setPlayWhenReady(this.autoplay);
      }

   }

   private void maybeReportPlayerState() {
      SimpleExoPlayer var1 = this.player;
      if (var1 != null) {
         boolean var2 = var1.getPlayWhenReady();
         int var3 = this.player.getPlaybackState();
         if (this.lastReportedPlayWhenReady != var2 || this.lastReportedPlaybackState != var3) {
            this.delegate.onStateChanged(var2, var3);
            this.lastReportedPlayWhenReady = var2;
            this.lastReportedPlaybackState = var3;
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.playerDidStartPlaying && (VideoPlayer)var3[0] != this && this.isPlaying()) {
         this.pause();
      }

   }

   public int getBufferedPercentage() {
      int var2;
      if (this.isStreaming) {
         SimpleExoPlayer var1 = this.player;
         if (var1 != null) {
            var2 = var1.getBufferedPercentage();
         } else {
            var2 = 0;
         }
      } else {
         var2 = 100;
      }

      return var2;
   }

   public long getBufferedPosition() {
      SimpleExoPlayer var1 = this.player;
      long var2;
      if (var1 != null) {
         if (this.isStreaming) {
            var2 = var1.getBufferedPosition();
         } else {
            var2 = var1.getDuration();
         }
      } else {
         var2 = 0L;
      }

      return var2;
   }

   public long getCurrentPosition() {
      SimpleExoPlayer var1 = this.player;
      long var2;
      if (var1 != null) {
         var2 = var1.getCurrentPosition();
      } else {
         var2 = 0L;
      }

      return var2;
   }

   public long getDuration() {
      SimpleExoPlayer var1 = this.player;
      long var2;
      if (var1 != null) {
         var2 = var1.getDuration();
      } else {
         var2 = 0L;
      }

      return var2;
   }

   public boolean getPlayWhenReady() {
      return this.player.getPlayWhenReady();
   }

   public int getPlaybackState() {
      return this.player.getPlaybackState();
   }

   public boolean isBuffering() {
      boolean var1;
      if (this.player != null && this.lastReportedPlaybackState == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isMuted() {
      boolean var1;
      if (this.player.getVolume() == 0.0F) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isPlayerPrepared() {
      boolean var1;
      if (this.player != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isPlaying() {
      boolean var2;
      if (!this.mixedAudio || !this.mixedPlayWhenReady) {
         SimpleExoPlayer var1 = this.player;
         if (var1 == null || !var1.getPlayWhenReady()) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public boolean isStreaming() {
      return this.isStreaming;
   }

   public void onLoadingChanged(boolean var1) {
   }

   public void onPlaybackParametersChanged(PlaybackParameters var1) {
   }

   public void onPlayerError(ExoPlaybackException var1) {
      this.delegate.onError(var1);
   }

   public void onPlayerStateChanged(boolean var1, int var2) {
      this.maybeReportPlayerState();
      if (var1 && var2 == 3) {
         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.playerDidStartPlaying, this);
      }

      if (!this.videoPlayerReady && var2 == 3) {
         this.videoPlayerReady = true;
         this.checkPlayersReady();
      }

   }

   public void onPositionDiscontinuity(int var1) {
   }

   public void onRenderedFirstFrame() {
      this.delegate.onRenderedFirstFrame();
   }

   public void onRepeatModeChanged(int var1) {
   }

   public void onSeekProcessed() {
   }

   public void onShuffleModeEnabledChanged(boolean var1) {
   }

   public boolean onSurfaceDestroyed(SurfaceTexture var1) {
      return this.delegate.onSurfaceDestroyed(var1);
   }

   public void onSurfaceSizeChanged(int var1, int var2) {
   }

   public void onSurfaceTextureUpdated(SurfaceTexture var1) {
      this.delegate.onSurfaceTextureUpdated(var1);
   }

   public void onTimelineChanged(Timeline var1, Object var2, int var3) {
   }

   public void onTracksChanged(TrackGroupArray var1, TrackSelectionArray var2) {
   }

   public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
      this.delegate.onVideoSizeChanged(var1, var2, var3, var4);
   }

   public void pause() {
      this.mixedPlayWhenReady = false;
      SimpleExoPlayer var1 = this.player;
      if (var1 != null) {
         var1.setPlayWhenReady(false);
      }

      var1 = this.audioPlayer;
      if (var1 != null) {
         var1.setPlayWhenReady(false);
      }

   }

   public void play() {
      this.mixedPlayWhenReady = true;
      SimpleExoPlayer var1;
      if (!this.mixedAudio || this.audioPlayerReady && this.videoPlayerReady) {
         var1 = this.player;
         if (var1 != null) {
            var1.setPlayWhenReady(true);
         }

         var1 = this.audioPlayer;
         if (var1 != null) {
            var1.setPlayWhenReady(true);
         }

      } else {
         var1 = this.player;
         if (var1 != null) {
            var1.setPlayWhenReady(false);
         }

         var1 = this.audioPlayer;
         if (var1 != null) {
            var1.setPlayWhenReady(false);
         }

      }
   }

   public void preparePlayer(Uri var1, String var2) {
      byte var3 = 0;
      this.videoPlayerReady = false;
      this.mixedAudio = false;
      String var4 = var1.getScheme();
      boolean var5;
      if (var4 != null && !var4.startsWith("file")) {
         var5 = true;
      } else {
         var5 = false;
      }

      label38: {
         this.isStreaming = var5;
         this.ensurePleyaerCreated();
         int var6 = var2.hashCode();
         if (var6 != 3680) {
            if (var6 != 103407) {
               if (var6 == 3075986 && var2.equals("dash")) {
                  break label38;
               }
            } else if (var2.equals("hls")) {
               var3 = 1;
               break label38;
            }
         } else if (var2.equals("ss")) {
            var3 = 2;
            break label38;
         }

         var3 = -1;
      }

      Object var7;
      DataSource.Factory var8;
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               var7 = new ExtractorMediaSource(var1, this.mediaDataSourceFactory, new DefaultExtractorsFactory(), this.mainHandler, (ExtractorMediaSource.EventListener)null);
            } else {
               var8 = this.mediaDataSourceFactory;
               var7 = new SsMediaSource(var1, var8, new DefaultSsChunkSource.Factory(var8), this.mainHandler, (MediaSourceEventListener)null);
            }
         } else {
            var7 = new HlsMediaSource(var1, this.mediaDataSourceFactory, this.mainHandler, (MediaSourceEventListener)null);
         }
      } else {
         var8 = this.mediaDataSourceFactory;
         var7 = new DashMediaSource(var1, var8, new DefaultDashChunkSource.Factory(var8), this.mainHandler, (MediaSourceEventListener)null);
      }

      this.player.prepare((MediaSource)var7, true, true);
   }

   public void preparePlayerLoop(Uri var1, String var2, Uri var3, String var4) {
      this.mixedAudio = true;
      this.audioPlayerReady = false;
      this.videoPlayerReady = false;
      this.ensurePleyaerCreated();
      LoopingMediaSource var5 = null;
      LoopingMediaSource var6 = var5;

      for(int var7 = 0; var7 < 2; ++var7) {
         Uri var8;
         String var9;
         if (var7 == 0) {
            var8 = var1;
            var9 = var2;
         } else {
            var8 = var3;
            var9 = var4;
         }

         byte var10 = -1;
         int var11 = var9.hashCode();
         if (var11 != 3680) {
            if (var11 != 103407) {
               if (var11 == 3075986 && var9.equals("dash")) {
                  var10 = 0;
               }
            } else if (var9.equals("hls")) {
               var10 = 1;
            }
         } else if (var9.equals("ss")) {
            var10 = 2;
         }

         Object var12;
         DataSource.Factory var13;
         if (var10 != 0) {
            if (var10 != 1) {
               if (var10 != 2) {
                  var12 = new ExtractorMediaSource(var8, this.mediaDataSourceFactory, new DefaultExtractorsFactory(), this.mainHandler, (ExtractorMediaSource.EventListener)null);
               } else {
                  var13 = this.mediaDataSourceFactory;
                  var12 = new SsMediaSource(var8, var13, new DefaultSsChunkSource.Factory(var13), this.mainHandler, (MediaSourceEventListener)null);
               }
            } else {
               var12 = new HlsMediaSource(var8, this.mediaDataSourceFactory, this.mainHandler, (MediaSourceEventListener)null);
            }
         } else {
            var13 = this.mediaDataSourceFactory;
            var12 = new DashMediaSource(var8, var13, new DefaultDashChunkSource.Factory(var13), this.mainHandler, (MediaSourceEventListener)null);
         }

         LoopingMediaSource var14 = new LoopingMediaSource((MediaSource)var12);
         if (var7 == 0) {
            var5 = var14;
         } else {
            var6 = var14;
         }
      }

      this.player.prepare(var5, true, true);
      this.audioPlayer.prepare(var6, true, true);
   }

   public void releasePlayer(boolean var1) {
      SimpleExoPlayer var2 = this.player;
      if (var2 != null) {
         var2.release(var1);
         this.player = null;
      }

      var2 = this.audioPlayer;
      if (var2 != null) {
         var2.release(var1);
         this.audioPlayer = null;
      }

      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.playerDidStartPlaying);
   }

   public void seekTo(long var1) {
      SimpleExoPlayer var3 = this.player;
      if (var3 != null) {
         var3.seekTo(var1);
      }

   }

   public void setDelegate(VideoPlayer.VideoPlayerDelegate var1) {
      this.delegate = var1;
   }

   public void setMute(boolean var1) {
      SimpleExoPlayer var2 = this.player;
      float var3 = 0.0F;
      float var4;
      if (var2 != null) {
         if (var1) {
            var4 = 0.0F;
         } else {
            var4 = 1.0F;
         }

         var2.setVolume(var4);
      }

      var2 = this.audioPlayer;
      if (var2 != null) {
         if (var1) {
            var4 = var3;
         } else {
            var4 = 1.0F;
         }

         var2.setVolume(var4);
      }

   }

   public void setPlayWhenReady(boolean var1) {
      this.mixedPlayWhenReady = var1;
      SimpleExoPlayer var2;
      if (!var1 || !this.mixedAudio || this.audioPlayerReady && this.videoPlayerReady) {
         this.autoplay = var1;
         var2 = this.player;
         if (var2 != null) {
            var2.setPlayWhenReady(var1);
         }

         var2 = this.audioPlayer;
         if (var2 != null) {
            var2.setPlayWhenReady(var1);
         }

      } else {
         var2 = this.player;
         if (var2 != null) {
            var2.setPlayWhenReady(false);
         }

         var2 = this.audioPlayer;
         if (var2 != null) {
            var2.setPlayWhenReady(false);
         }

      }
   }

   public void setPlaybackSpeed(float var1) {
      SimpleExoPlayer var2 = this.player;
      if (var2 != null) {
         float var3 = 1.0F;
         if (var1 > 1.0F) {
            var3 = 0.98F;
         }

         var2.setPlaybackParameters(new PlaybackParameters(var1, var3));
      }

   }

   public void setStreamType(int var1) {
      SimpleExoPlayer var2 = this.player;
      if (var2 != null) {
         var2.setAudioStreamType(var1);
      }

      var2 = this.audioPlayer;
      if (var2 != null) {
         var2.setAudioStreamType(var1);
      }

   }

   public void setTextureView(TextureView var1) {
      if (this.textureView != var1) {
         this.textureView = var1;
         SimpleExoPlayer var2 = this.player;
         if (var2 != null) {
            var2.setVideoTextureView(this.textureView);
         }
      }
   }

   public void setVolume(float var1) {
      SimpleExoPlayer var2 = this.player;
      if (var2 != null) {
         var2.setVolume(var1);
      }

      var2 = this.audioPlayer;
      if (var2 != null) {
         var2.setVolume(var1);
      }

   }

   public interface RendererBuilder {
      void buildRenderers(VideoPlayer var1);

      void cancel();
   }

   public interface VideoPlayerDelegate {
      void onError(Exception var1);

      void onRenderedFirstFrame();

      void onStateChanged(boolean var1, int var2);

      boolean onSurfaceDestroyed(SurfaceTexture var1);

      void onSurfaceTextureUpdated(SurfaceTexture var1);

      void onVideoSizeChanged(int var1, int var2, int var3, float var4);
   }
}
