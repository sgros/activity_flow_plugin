package pl.droidsonroids.gif;

import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.IOException;

public class GifDecoder {
   private final GifInfoHandle mGifInfoHandle;

   public GifDecoder(@NonNull InputSource var1) throws IOException {
      this(var1, (GifOptions)null);
   }

   public GifDecoder(@NonNull InputSource var1, @Nullable GifOptions var2) throws IOException {
      this.mGifInfoHandle = var1.open();
      if (var2 != null) {
         this.mGifInfoHandle.setOptions(var2.inSampleSize, var2.inIsOpaque);
      }

   }

   private void checkBuffer(Bitmap var1) {
      if (var1.isRecycled()) {
         throw new IllegalArgumentException("Bitmap is recycled");
      } else if (var1.getWidth() < this.mGifInfoHandle.getWidth() || var1.getHeight() < this.mGifInfoHandle.getHeight()) {
         throw new IllegalArgumentException("Bitmap ia too small, size must be greater than or equal to GIF size");
      }
   }

   public long getAllocationByteCount() {
      return this.mGifInfoHandle.getAllocationByteCount();
   }

   public String getComment() {
      return this.mGifInfoHandle.getComment();
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

   public int getLoopCount() {
      return this.mGifInfoHandle.getLoopCount();
   }

   public int getNumberOfFrames() {
      return this.mGifInfoHandle.getNumberOfFrames();
   }

   public long getSourceLength() {
      return this.mGifInfoHandle.getSourceLength();
   }

   public int getWidth() {
      return this.mGifInfoHandle.getWidth();
   }

   public boolean isAnimated() {
      boolean var1 = true;
      if (this.mGifInfoHandle.getNumberOfFrames() <= 1 || this.getDuration() <= 0) {
         var1 = false;
      }

      return var1;
   }

   public void recycle() {
      this.mGifInfoHandle.recycle();
   }

   public void seekToFrame(@IntRange(from = 0L,to = 2147483647L) int var1, @NonNull Bitmap var2) {
      this.checkBuffer(var2);
      this.mGifInfoHandle.seekToFrame(var1, var2);
   }

   public void seekToTime(@IntRange(from = 0L,to = 2147483647L) int var1, @NonNull Bitmap var2) {
      this.checkBuffer(var2);
      this.mGifInfoHandle.seekToTime(var1, var2);
   }
}
