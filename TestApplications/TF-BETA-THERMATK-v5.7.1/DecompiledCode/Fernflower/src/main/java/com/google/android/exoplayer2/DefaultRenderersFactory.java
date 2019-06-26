package com.google.android.exoplayer2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.video.spherical.CameraMotionRenderer;
import java.util.ArrayList;

public class DefaultRenderersFactory implements RenderersFactory {
   private final long allowedVideoJoiningTimeMs;
   private final Context context;
   private final DrmSessionManager drmSessionManager;
   private final int extensionRendererMode;

   public DefaultRenderersFactory(Context var1, int var2) {
      this(var1, var2, 5000L);
   }

   public DefaultRenderersFactory(Context var1, int var2, long var3) {
      this.context = var1;
      this.extensionRendererMode = var2;
      this.allowedVideoJoiningTimeMs = var3;
      this.drmSessionManager = null;
   }

   protected AudioProcessor[] buildAudioProcessors() {
      return new AudioProcessor[0];
   }

   protected void buildAudioRenderers(Context param1, DrmSessionManager param2, AudioProcessor[] param3, Handler param4, AudioRendererEventListener param5, int param6, ArrayList param7) {
      // $FF: Couldn't be decompiled
   }

   protected void buildCameraMotionRenderers(Context var1, int var2, ArrayList var3) {
      var3.add(new CameraMotionRenderer());
   }

   protected void buildMetadataRenderers(Context var1, MetadataOutput var2, Looper var3, int var4, ArrayList var5) {
      var5.add(new MetadataRenderer(var2, var3));
   }

   protected void buildMiscellaneousRenderers(Context var1, Handler var2, int var3, ArrayList var4) {
   }

   protected void buildTextRenderers(Context var1, TextOutput var2, Looper var3, int var4, ArrayList var5) {
      var5.add(new TextRenderer(var2, var3));
   }

   protected void buildVideoRenderers(Context var1, DrmSessionManager var2, long var3, Handler var5, VideoRendererEventListener var6, int var7, ArrayList var8) {
      var8.add(new MediaCodecVideoRenderer(var1, MediaCodecSelector.DEFAULT, var3, var2, false, var5, var6, 50));
      if (var7 != 0) {
         int var9 = var8.size();
         int var10 = var9;
         if (var7 == 2) {
            var10 = var9 - 1;
         }

         try {
            var8.add(var10, (Renderer)Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(Boolean.TYPE, Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(true, var3, var5, var6, 50));
            Log.i("DefaultRenderersFactory", "Loaded LibvpxVideoRenderer.");
         } catch (ClassNotFoundException var11) {
         } catch (Exception var12) {
            throw new RuntimeException("Error instantiating VP9 extension", var12);
         }

      }
   }

   public Renderer[] createRenderers(Handler var1, VideoRendererEventListener var2, AudioRendererEventListener var3, TextOutput var4, MetadataOutput var5, DrmSessionManager var6) {
      if (var6 == null) {
         var6 = this.drmSessionManager;
      }

      ArrayList var7 = new ArrayList();
      this.buildVideoRenderers(this.context, var6, this.allowedVideoJoiningTimeMs, var1, var2, this.extensionRendererMode, var7);
      this.buildAudioRenderers(this.context, var6, this.buildAudioProcessors(), var1, var3, this.extensionRendererMode, var7);
      this.buildTextRenderers(this.context, var4, var1.getLooper(), this.extensionRendererMode, var7);
      this.buildMetadataRenderers(this.context, var5, var1.getLooper(), this.extensionRendererMode, var7);
      this.buildCameraMotionRenderers(this.context, this.extensionRendererMode, var7);
      this.buildMiscellaneousRenderers(this.context, var1, this.extensionRendererMode, var7);
      return (Renderer[])var7.toArray(new Renderer[0]);
   }
}
