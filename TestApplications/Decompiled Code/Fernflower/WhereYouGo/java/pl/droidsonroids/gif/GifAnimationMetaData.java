package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Locale;
import pl.droidsonroids.gif.annotations.Beta;

public class GifAnimationMetaData implements Serializable, Parcelable {
   public static final Creator CREATOR = new Creator() {
      public GifAnimationMetaData createFromParcel(Parcel var1) {
         return new GifAnimationMetaData(var1);
      }

      public GifAnimationMetaData[] newArray(int var1) {
         return new GifAnimationMetaData[var1];
      }
   };
   private static final long serialVersionUID = 5692363926580237325L;
   private final int mDuration;
   private final int mHeight;
   private final int mImageCount;
   private final int mLoopCount;
   private final long mMetadataBytesCount;
   private final long mPixelsBytesCount;
   private final int mWidth;

   public GifAnimationMetaData(@Nullable ContentResolver var1, @NonNull Uri var2) throws IOException {
      this(GifInfoHandle.openUri(var1, var2));
   }

   public GifAnimationMetaData(@NonNull AssetFileDescriptor var1) throws IOException {
      this(new GifInfoHandle(var1));
   }

   public GifAnimationMetaData(@NonNull AssetManager var1, @NonNull String var2) throws IOException {
      this(var1.openFd(var2));
   }

   public GifAnimationMetaData(@NonNull Resources var1, @DrawableRes @RawRes int var2) throws NotFoundException, IOException {
      this(var1.openRawResourceFd(var2));
   }

   private GifAnimationMetaData(Parcel var1) {
      this.mLoopCount = var1.readInt();
      this.mDuration = var1.readInt();
      this.mHeight = var1.readInt();
      this.mWidth = var1.readInt();
      this.mImageCount = var1.readInt();
      this.mMetadataBytesCount = var1.readLong();
      this.mPixelsBytesCount = var1.readLong();
   }

   // $FF: synthetic method
   GifAnimationMetaData(Parcel var1, Object var2) {
      this(var1);
   }

   public GifAnimationMetaData(@NonNull File var1) throws IOException {
      this(var1.getPath());
   }

   public GifAnimationMetaData(@NonNull FileDescriptor var1) throws IOException {
      this(new GifInfoHandle(var1));
   }

   public GifAnimationMetaData(@NonNull InputStream var1) throws IOException {
      this(new GifInfoHandle(var1));
   }

   public GifAnimationMetaData(@NonNull String var1) throws IOException {
      this(new GifInfoHandle(var1));
   }

   public GifAnimationMetaData(@NonNull ByteBuffer var1) throws IOException {
      this(new GifInfoHandle(var1));
   }

   private GifAnimationMetaData(GifInfoHandle var1) {
      this.mLoopCount = var1.getLoopCount();
      this.mDuration = var1.getDuration();
      this.mWidth = var1.getWidth();
      this.mHeight = var1.getHeight();
      this.mImageCount = var1.getNumberOfFrames();
      this.mMetadataBytesCount = var1.getMetadataByteCount();
      this.mPixelsBytesCount = var1.getAllocationByteCount();
      var1.recycle();
   }

   public GifAnimationMetaData(@NonNull byte[] var1) throws IOException {
      this(new GifInfoHandle(var1));
   }

   public int describeContents() {
      return 0;
   }

   public long getAllocationByteCount() {
      return this.mPixelsBytesCount;
   }

   @Beta
   public long getDrawableAllocationByteCount(@Nullable GifDrawable var1, @IntRange(from = 1L,to = 65535L) int var2) {
      if (var2 >= 1 && var2 <= 65535) {
         var2 *= var2;
         long var3;
         if (var1 != null && !var1.mBuffer.isRecycled()) {
            if (VERSION.SDK_INT >= 19) {
               var3 = (long)var1.mBuffer.getAllocationByteCount();
            } else {
               var3 = (long)var1.getFrameByteCount();
            }
         } else {
            var3 = (long)(this.mWidth * this.mHeight * 4 / var2);
         }

         return this.mPixelsBytesCount / (long)var2 + var3;
      } else {
         throw new IllegalStateException("Sample size " + var2 + " out of range <1, " + '\uffff' + ">");
      }
   }

   public int getDuration() {
      return this.mDuration;
   }

   public int getHeight() {
      return this.mHeight;
   }

   public int getLoopCount() {
      return this.mLoopCount;
   }

   public long getMetadataAllocationByteCount() {
      return this.mMetadataBytesCount;
   }

   public int getNumberOfFrames() {
      return this.mImageCount;
   }

   public int getWidth() {
      return this.mWidth;
   }

   public boolean isAnimated() {
      boolean var1 = true;
      if (this.mImageCount <= 1 || this.mDuration <= 0) {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      String var1;
      if (this.mLoopCount == 0) {
         var1 = "Infinity";
      } else {
         var1 = Integer.toString(this.mLoopCount);
      }

      String var2 = String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, loops: %s, duration: %d", this.mWidth, this.mHeight, this.mImageCount, var1, this.mDuration);
      var1 = var2;
      if (this.isAnimated()) {
         var1 = "Animated " + var2;
      }

      return var1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mLoopCount);
      var1.writeInt(this.mDuration);
      var1.writeInt(this.mHeight);
      var1.writeInt(this.mWidth);
      var1.writeInt(this.mImageCount);
      var1.writeLong(this.mMetadataBytesCount);
      var1.writeLong(this.mPixelsBytesCount);
   }
}
