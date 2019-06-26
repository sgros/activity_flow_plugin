package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class GifInfoHandle {
   private volatile long gifInfoPtr;

   static {
      LibraryLoader.loadLibrary((Context)null);
   }

   GifInfoHandle() {
   }

   GifInfoHandle(AssetFileDescriptor var1) throws IOException {
      try {
         this.gifInfoPtr = openFd(var1.getFileDescriptor(), var1.getStartOffset());
      } finally {
         try {
            var1.close();
         } catch (IOException var6) {
         }

      }

   }

   GifInfoHandle(FileDescriptor var1) throws GifIOException {
      this.gifInfoPtr = openFd(var1, 0L);
   }

   GifInfoHandle(InputStream var1) throws GifIOException {
      if (!var1.markSupported()) {
         throw new IllegalArgumentException("InputStream does not support marking");
      } else {
         this.gifInfoPtr = openStream(var1);
      }
   }

   GifInfoHandle(String var1) throws GifIOException {
      this.gifInfoPtr = openFile(var1);
   }

   GifInfoHandle(ByteBuffer var1) throws GifIOException {
      this.gifInfoPtr = openDirectByteBuffer(var1);
   }

   GifInfoHandle(byte[] var1) throws GifIOException {
      this.gifInfoPtr = openByteArray(var1);
   }

   private static native void bindSurface(long var0, Surface var2, long[] var3);

   private static native void free(long var0);

   private static native long getAllocationByteCount(long var0);

   private static native String getComment(long var0);

   private static native int getCurrentFrameIndex(long var0);

   private static native int getCurrentLoop(long var0);

   private static native int getCurrentPosition(long var0);

   private static native int getDuration(long var0);

   private static native int getFrameDuration(long var0, int var2);

   private static native int getHeight(long var0);

   private static native int getLoopCount(long var0);

   private static native long getMetadataByteCount(long var0);

   private static native int getNativeErrorCode(long var0);

   private static native int getNumberOfFrames(long var0);

   private static native long[] getSavedState(long var0);

   private static native long getSourceLength(long var0);

   private static native int getWidth(long var0);

   private static native void glTexImage2D(long var0, int var2, int var3);

   private static native void glTexSubImage2D(long var0, int var2, int var3);

   private static native void initTexImageDescriptor(long var0);

   private static native boolean isAnimationCompleted(long var0);

   private static native boolean isOpaque(long var0);

   static native long openByteArray(byte[] var0) throws GifIOException;

   static native long openDirectByteBuffer(ByteBuffer var0) throws GifIOException;

   static native long openFd(FileDescriptor var0, long var1) throws GifIOException;

   static native long openFile(String var0) throws GifIOException;

   static native long openStream(InputStream var0) throws GifIOException;

   static GifInfoHandle openUri(ContentResolver var0, Uri var1) throws IOException {
      GifInfoHandle var2;
      if ("file".equals(var1.getScheme())) {
         var2 = new GifInfoHandle(var1.getPath());
      } else {
         var2 = new GifInfoHandle(var0.openAssetFileDescriptor(var1, "r"));
      }

      return var2;
   }

   private static native void postUnbindSurface(long var0);

   private static native long renderFrame(long var0, Bitmap var2);

   private static native boolean reset(long var0);

   private static native long restoreRemainder(long var0);

   private static native int restoreSavedState(long var0, long[] var2, Bitmap var3);

   private static native void saveRemainder(long var0);

   private static native void seekToFrame(long var0, int var2, Bitmap var3);

   private static native void seekToFrameGL(long var0, int var2);

   private static native void seekToTime(long var0, int var2, Bitmap var3);

   private static native void setLoopCount(long var0, char var2);

   private static native void setOptions(long var0, char var2, boolean var3);

   private static native void setSpeedFactor(long var0, float var2);

   private static native void startDecoderThread(long var0);

   private static native void stopDecoderThread(long var0);

   void bindSurface(Surface var1, long[] var2) {
      bindSurface(this.gifInfoPtr, var1, var2);
   }

   protected void finalize() throws Throwable {
      try {
         this.recycle();
      } finally {
         super.finalize();
      }

   }

   long getAllocationByteCount() {
      synchronized(this){}

      long var1;
      try {
         var1 = getAllocationByteCount(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   String getComment() {
      synchronized(this){}

      String var1;
      try {
         var1 = getComment(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getCurrentFrameIndex() {
      synchronized(this){}

      int var1;
      try {
         var1 = getCurrentFrameIndex(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getCurrentLoop() {
      synchronized(this){}

      int var1;
      try {
         var1 = getCurrentLoop(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getCurrentPosition() {
      synchronized(this){}

      int var1;
      try {
         var1 = getCurrentPosition(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getDuration() {
      synchronized(this){}

      int var1;
      try {
         var1 = getDuration(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getFrameDuration(@IntRange(from = 0L) int var1) {
      Throwable var10000;
      boolean var10001;
      label164: {
         label163: {
            synchronized(this){}
            if (var1 >= 0) {
               try {
                  if (var1 < getNumberOfFrames(this.gifInfoPtr)) {
                     break label163;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label164;
               }
            }

            try {
               IndexOutOfBoundsException var2 = new IndexOutOfBoundsException("Frame index is out of bounds");
               throw var2;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label164;
            }
         }

         label155:
         try {
            var1 = getFrameDuration(this.gifInfoPtr, var1);
            return var1;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label155;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   int getHeight() {
      synchronized(this){}

      int var1;
      try {
         var1 = getHeight(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getLoopCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = getLoopCount(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   long getMetadataByteCount() {
      synchronized(this){}

      long var1;
      try {
         var1 = getMetadataByteCount(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getNativeErrorCode() {
      synchronized(this){}

      int var1;
      try {
         var1 = getNativeErrorCode(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getNumberOfFrames() {
      synchronized(this){}

      int var1;
      try {
         var1 = getNumberOfFrames(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   long[] getSavedState() {
      synchronized(this){}

      long[] var1;
      try {
         var1 = getSavedState(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   long getSourceLength() {
      synchronized(this){}

      long var1;
      try {
         var1 = getSourceLength(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int getWidth() {
      synchronized(this){}

      int var1;
      try {
         var1 = getWidth(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   void glTexImage2D(int var1, int var2) {
      glTexImage2D(this.gifInfoPtr, var1, var2);
   }

   void glTexSubImage2D(int var1, int var2) {
      glTexSubImage2D(this.gifInfoPtr, var1, var2);
   }

   void initTexImageDescriptor() {
      initTexImageDescriptor(this.gifInfoPtr);
   }

   boolean isAnimationCompleted() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = isAnimationCompleted(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   boolean isOpaque() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = isOpaque(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   boolean isRecycled() {
      synchronized(this){}
      boolean var6 = false;

      long var1;
      try {
         var6 = true;
         var1 = this.gifInfoPtr;
         var6 = false;
      } finally {
         if (var6) {
            ;
         }
      }

      boolean var3;
      if (var1 == 0L) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   void postUnbindSurface() {
      synchronized(this){}

      try {
         postUnbindSurface(this.gifInfoPtr);
      } finally {
         ;
      }

   }

   void recycle() {
      synchronized(this){}

      try {
         free(this.gifInfoPtr);
         this.gifInfoPtr = 0L;
      } finally {
         ;
      }

   }

   long renderFrame(Bitmap var1) {
      synchronized(this){}

      long var2;
      try {
         var2 = renderFrame(this.gifInfoPtr, var1);
      } finally {
         ;
      }

      return var2;
   }

   boolean reset() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = reset(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   long restoreRemainder() {
      synchronized(this){}

      long var1;
      try {
         var1 = restoreRemainder(this.gifInfoPtr);
      } finally {
         ;
      }

      return var1;
   }

   int restoreSavedState(long[] var1, Bitmap var2) {
      synchronized(this){}

      int var3;
      try {
         var3 = restoreSavedState(this.gifInfoPtr, var1, var2);
      } finally {
         ;
      }

      return var3;
   }

   void saveRemainder() {
      synchronized(this){}

      try {
         saveRemainder(this.gifInfoPtr);
      } finally {
         ;
      }

   }

   void seekToFrame(@IntRange(from = 0L,to = 2147483647L) int var1, Bitmap var2) {
      synchronized(this){}

      try {
         seekToFrame(this.gifInfoPtr, var1, var2);
      } finally {
         ;
      }

   }

   void seekToFrameGL(@IntRange(from = 0L) int var1) {
      if (var1 >= 0 && var1 < getNumberOfFrames(this.gifInfoPtr)) {
         seekToFrameGL(this.gifInfoPtr, var1);
      } else {
         throw new IndexOutOfBoundsException("Frame index is out of bounds");
      }
   }

   void seekToTime(@IntRange(from = 0L,to = 2147483647L) int var1, Bitmap var2) {
      synchronized(this){}

      try {
         seekToTime(this.gifInfoPtr, var1, var2);
      } finally {
         ;
      }

   }

   void setLoopCount(@IntRange(from = 0L,to = 65535L) int param1) {
      // $FF: Couldn't be decompiled
   }

   void setOptions(char var1, boolean var2) {
      setOptions(this.gifInfoPtr, var1, var2);
   }

   void setSpeedFactor(@FloatRange(from = 0.0D,fromInclusive = false) float param1) {
      // $FF: Couldn't be decompiled
   }

   void startDecoderThread() {
      startDecoderThread(this.gifInfoPtr);
   }

   void stopDecoderThread() {
      stopDecoderThread(this.gifInfoPtr);
   }
}
