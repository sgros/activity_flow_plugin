package pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import java.io.IOException;
import pl.droidsonroids.gif.annotations.Beta;

@Beta
public class GifTexImage2D {
   private final GifInfoHandle mGifInfoHandle;

   public GifTexImage2D(InputSource var1, @Nullable GifOptions var2) throws IOException {
      GifOptions var3 = var2;
      if (var2 == null) {
         var3 = new GifOptions();
      }

      this.mGifInfoHandle = var1.open();
      this.mGifInfoHandle.setOptions(var3.inSampleSize, var3.inIsOpaque);
      this.mGifInfoHandle.initTexImageDescriptor();
   }

   protected final void finalize() throws Throwable {
      try {
         this.recycle();
      } finally {
         super.finalize();
      }

   }

   public int getDuration() {
      return this.mGifInfoHandle.getDuration();
   }

   public int getFrameDuration(@IntRange(from = 0L) int var1) {
      return this.mGifInfoHandle.getFrameDuration(var1);
   }

   public int getHeight() {
      return this.mGifInfoHandle.getHeight();
   }

   public int getNumberOfFrames() {
      return this.mGifInfoHandle.getNumberOfFrames();
   }

   public int getWidth() {
      return this.mGifInfoHandle.getWidth();
   }

   public void glTexImage2D(int var1, int var2) {
      this.mGifInfoHandle.glTexImage2D(var1, var2);
   }

   public void glTexSubImage2D(int var1, int var2) {
      this.mGifInfoHandle.glTexSubImage2D(var1, var2);
   }

   public void recycle() {
      if (this.mGifInfoHandle != null) {
         this.mGifInfoHandle.recycle();
      }

   }

   public void seekToFrame(@IntRange(from = 0L) int var1) {
      this.mGifInfoHandle.seekToFrameGL(var1);
   }

   public void startDecoderThread() {
      this.mGifInfoHandle.startDecoderThread();
   }

   public void stopDecoderThread() {
      this.mGifInfoHandle.stopDecoderThread();
   }
}
