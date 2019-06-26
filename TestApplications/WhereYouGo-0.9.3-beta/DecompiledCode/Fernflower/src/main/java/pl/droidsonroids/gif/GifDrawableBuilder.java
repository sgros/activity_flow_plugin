package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import pl.droidsonroids.gif.annotations.Beta;

public class GifDrawableBuilder {
   private ScheduledThreadPoolExecutor mExecutor;
   private InputSource mInputSource;
   private boolean mIsRenderingTriggeredOnDraw = true;
   private GifDrawable mOldDrawable;
   private GifOptions mOptions = new GifOptions();

   public GifDrawable build() throws IOException {
      if (this.mInputSource == null) {
         throw new NullPointerException("Source is not set");
      } else {
         return this.mInputSource.build(this.mOldDrawable, this.mExecutor, this.mIsRenderingTriggeredOnDraw, this.mOptions);
      }
   }

   public GifDrawableBuilder from(ContentResolver var1, Uri var2) {
      this.mInputSource = new InputSource.UriSource(var1, var2);
      return this;
   }

   public GifDrawableBuilder from(AssetFileDescriptor var1) {
      this.mInputSource = new InputSource.AssetFileDescriptorSource(var1);
      return this;
   }

   public GifDrawableBuilder from(AssetManager var1, String var2) {
      this.mInputSource = new InputSource.AssetSource(var1, var2);
      return this;
   }

   public GifDrawableBuilder from(Resources var1, int var2) {
      this.mInputSource = new InputSource.ResourcesSource(var1, var2);
      return this;
   }

   public GifDrawableBuilder from(File var1) {
      this.mInputSource = new InputSource.FileSource(var1);
      return this;
   }

   public GifDrawableBuilder from(FileDescriptor var1) {
      this.mInputSource = new InputSource.FileDescriptorSource(var1);
      return this;
   }

   public GifDrawableBuilder from(InputStream var1) {
      this.mInputSource = new InputSource.InputStreamSource(var1);
      return this;
   }

   public GifDrawableBuilder from(String var1) {
      this.mInputSource = new InputSource.FileSource(var1);
      return this;
   }

   public GifDrawableBuilder from(ByteBuffer var1) {
      this.mInputSource = new InputSource.DirectByteBufferSource(var1);
      return this;
   }

   public GifDrawableBuilder from(byte[] var1) {
      this.mInputSource = new InputSource.ByteArraySource(var1);
      return this;
   }

   @Beta
   public GifDrawableBuilder options(@Nullable GifOptions var1) {
      this.mOptions.setFrom(var1);
      return this;
   }

   public GifDrawableBuilder renderingTriggeredOnDraw(boolean var1) {
      this.mIsRenderingTriggeredOnDraw = var1;
      return this;
   }

   public GifDrawableBuilder sampleSize(@IntRange(from = 1L,to = 65535L) int var1) {
      this.mOptions.setInSampleSize(var1);
      return this;
   }

   public GifDrawableBuilder setRenderingTriggeredOnDraw(boolean var1) {
      return this.renderingTriggeredOnDraw(var1);
   }

   public GifDrawableBuilder taskExecutor(ScheduledThreadPoolExecutor var1) {
      this.mExecutor = var1;
      return this;
   }

   public GifDrawableBuilder threadPoolSize(int var1) {
      this.mExecutor = new ScheduledThreadPoolExecutor(var1);
      return this;
   }

   public GifDrawableBuilder with(GifDrawable var1) {
      this.mOldDrawable = var1;
      return this;
   }
}
