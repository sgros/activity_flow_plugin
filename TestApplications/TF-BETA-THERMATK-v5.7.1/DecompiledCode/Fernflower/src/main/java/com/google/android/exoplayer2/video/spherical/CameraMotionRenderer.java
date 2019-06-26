package com.google.android.exoplayer2.video.spherical;

import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

public class CameraMotionRenderer extends BaseRenderer {
   private final DecoderInputBuffer buffer = new DecoderInputBuffer(1);
   private final FormatHolder formatHolder = new FormatHolder();
   private long lastTimestampUs;
   private CameraMotionListener listener;
   private long offsetUs;
   private final ParsableByteArray scratch = new ParsableByteArray();

   public CameraMotionRenderer() {
      super(5);
   }

   private float[] parseMetadata(ByteBuffer var1) {
      if (var1.remaining() != 16) {
         return null;
      } else {
         this.scratch.reset(var1.array(), var1.limit());
         this.scratch.setPosition(var1.arrayOffset() + 4);
         float[] var3 = new float[3];

         for(int var2 = 0; var2 < 3; ++var2) {
            var3[var2] = Float.intBitsToFloat(this.scratch.readLittleEndianInt());
         }

         return var3;
      }
   }

   private void resetListener() {
      this.lastTimestampUs = 0L;
      CameraMotionListener var1 = this.listener;
      if (var1 != null) {
         var1.onCameraMotionReset();
      }

   }

   public void handleMessage(int var1, Object var2) throws ExoPlaybackException {
      if (var1 == 7) {
         this.listener = (CameraMotionListener)var2;
      } else {
         super.handleMessage(var1, var2);
      }

   }

   public boolean isEnded() {
      return this.hasReadStreamToEnd();
   }

   public boolean isReady() {
      return true;
   }

   protected void onDisabled() {
      this.resetListener();
   }

   protected void onPositionReset(long var1, boolean var3) throws ExoPlaybackException {
      this.resetListener();
   }

   protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
      this.offsetUs = var2;
   }

   public void render(long var1, long var3) throws ExoPlaybackException {
      while(true) {
         if (!this.hasReadStreamToEnd() && this.lastTimestampUs < 100000L + var1) {
            this.buffer.clear();
            if (this.readSource(this.formatHolder, this.buffer, false) == -4 && !this.buffer.isEndOfStream()) {
               this.buffer.flip();
               DecoderInputBuffer var5 = this.buffer;
               this.lastTimestampUs = var5.timeUs;
               if (this.listener == null) {
                  continue;
               }

               float[] var7 = this.parseMetadata(var5.data);
               if (var7 != null) {
                  CameraMotionListener var6 = this.listener;
                  Util.castNonNull(var6);
                  ((CameraMotionListener)var6).onCameraMotion(this.lastTimestampUs - this.offsetUs, var7);
               }
               continue;
            }
         }

         return;
      }
   }

   public int supportsFormat(Format var1) {
      byte var2;
      if ("application/x-camera-motion".equals(var1.sampleMimeType)) {
         var2 = 4;
      } else {
         var2 = 0;
      }

      return var2;
   }
}
