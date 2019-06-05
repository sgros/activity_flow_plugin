package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class InputSource {
   private InputSource() {
   }

   // $FF: synthetic method
   InputSource(Object var1) {
      this();
   }

   final GifDrawable build(GifDrawable var1, ScheduledThreadPoolExecutor var2, boolean var3, GifOptions var4) throws IOException {
      GifInfoHandle var5 = this.open();
      var5.setOptions(var4.inSampleSize, var4.inIsOpaque);
      return new GifDrawable(var5, var1, var2, var3);
   }

   abstract GifInfoHandle open() throws IOException;

   public static class AssetFileDescriptorSource extends InputSource {
      private final AssetFileDescriptor mAssetFileDescriptor;

      public AssetFileDescriptorSource(@NonNull AssetFileDescriptor var1) {
         super(null);
         this.mAssetFileDescriptor = var1;
      }

      GifInfoHandle open() throws IOException {
         return new GifInfoHandle(this.mAssetFileDescriptor);
      }
   }

   public static final class AssetSource extends InputSource {
      private final AssetManager mAssetManager;
      private final String mAssetName;

      public AssetSource(@NonNull AssetManager var1, @NonNull String var2) {
         super(null);
         this.mAssetManager = var1;
         this.mAssetName = var2;
      }

      GifInfoHandle open() throws IOException {
         return new GifInfoHandle(this.mAssetManager.openFd(this.mAssetName));
      }
   }

   public static final class ByteArraySource extends InputSource {
      private final byte[] bytes;

      public ByteArraySource(@NonNull byte[] var1) {
         super(null);
         this.bytes = var1;
      }

      GifInfoHandle open() throws GifIOException {
         return new GifInfoHandle(this.bytes);
      }
   }

   public static final class DirectByteBufferSource extends InputSource {
      private final ByteBuffer byteBuffer;

      public DirectByteBufferSource(@NonNull ByteBuffer var1) {
         super(null);
         this.byteBuffer = var1;
      }

      GifInfoHandle open() throws GifIOException {
         return new GifInfoHandle(this.byteBuffer);
      }
   }

   public static final class FileDescriptorSource extends InputSource {
      private final FileDescriptor mFd;

      public FileDescriptorSource(@NonNull FileDescriptor var1) {
         super(null);
         this.mFd = var1;
      }

      GifInfoHandle open() throws IOException {
         return new GifInfoHandle(this.mFd);
      }
   }

   public static final class FileSource extends InputSource {
      private final String mPath;

      public FileSource(@NonNull File var1) {
         super(null);
         this.mPath = var1.getPath();
      }

      public FileSource(@NonNull String var1) {
         super(null);
         this.mPath = var1;
      }

      GifInfoHandle open() throws GifIOException {
         return new GifInfoHandle(this.mPath);
      }
   }

   public static final class InputStreamSource extends InputSource {
      private final InputStream inputStream;

      public InputStreamSource(@NonNull InputStream var1) {
         super(null);
         this.inputStream = var1;
      }

      GifInfoHandle open() throws IOException {
         return new GifInfoHandle(this.inputStream);
      }
   }

   public static class ResourcesSource extends InputSource {
      private final int mResourceId;
      private final Resources mResources;

      public ResourcesSource(@NonNull Resources var1, @DrawableRes @RawRes int var2) {
         super(null);
         this.mResources = var1;
         this.mResourceId = var2;
      }

      GifInfoHandle open() throws IOException {
         return new GifInfoHandle(this.mResources.openRawResourceFd(this.mResourceId));
      }
   }

   public static final class UriSource extends InputSource {
      private final ContentResolver mContentResolver;
      private final Uri mUri;

      public UriSource(@Nullable ContentResolver var1, @NonNull Uri var2) {
         super(null);
         this.mContentResolver = var1;
         this.mUri = var2;
      }

      GifInfoHandle open() throws IOException {
         return GifInfoHandle.openUri(this.mContentResolver, this.mUri);
      }
   }
}
