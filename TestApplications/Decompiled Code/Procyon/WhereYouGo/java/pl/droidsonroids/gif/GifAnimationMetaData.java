// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.util.Locale;
import pl.droidsonroids.gif.annotations.Beta;
import android.os.Build$VERSION;
import android.support.annotation.IntRange;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.FileDescriptor;
import java.io.File;
import android.content.res.Resources$NotFoundException;
import android.support.annotation.RawRes;
import android.support.annotation.DrawableRes;
import android.content.res.Resources;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
import android.support.annotation.NonNull;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.content.ContentResolver;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import java.io.Serializable;

public class GifAnimationMetaData implements Serializable, Parcelable
{
    public static final Parcelable$Creator<GifAnimationMetaData> CREATOR;
    private static final long serialVersionUID = 5692363926580237325L;
    private final int mDuration;
    private final int mHeight;
    private final int mImageCount;
    private final int mLoopCount;
    private final long mMetadataBytesCount;
    private final long mPixelsBytesCount;
    private final int mWidth;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<GifAnimationMetaData>() {
            public GifAnimationMetaData createFromParcel(final Parcel parcel) {
                return new GifAnimationMetaData(parcel, null);
            }
            
            public GifAnimationMetaData[] newArray(final int n) {
                return new GifAnimationMetaData[n];
            }
        };
    }
    
    public GifAnimationMetaData(@Nullable final ContentResolver contentResolver, @NonNull final Uri uri) throws IOException {
        this(GifInfoHandle.openUri(contentResolver, uri));
    }
    
    public GifAnimationMetaData(@NonNull final AssetFileDescriptor assetFileDescriptor) throws IOException {
        this(new GifInfoHandle(assetFileDescriptor));
    }
    
    public GifAnimationMetaData(@NonNull final AssetManager assetManager, @NonNull final String s) throws IOException {
        this(assetManager.openFd(s));
    }
    
    public GifAnimationMetaData(@NonNull final Resources resources, @DrawableRes @RawRes final int n) throws Resources$NotFoundException, IOException {
        this(resources.openRawResourceFd(n));
    }
    
    private GifAnimationMetaData(final Parcel parcel) {
        this.mLoopCount = parcel.readInt();
        this.mDuration = parcel.readInt();
        this.mHeight = parcel.readInt();
        this.mWidth = parcel.readInt();
        this.mImageCount = parcel.readInt();
        this.mMetadataBytesCount = parcel.readLong();
        this.mPixelsBytesCount = parcel.readLong();
    }
    
    public GifAnimationMetaData(@NonNull final File file) throws IOException {
        this(file.getPath());
    }
    
    public GifAnimationMetaData(@NonNull final FileDescriptor fileDescriptor) throws IOException {
        this(new GifInfoHandle(fileDescriptor));
    }
    
    public GifAnimationMetaData(@NonNull final InputStream inputStream) throws IOException {
        this(new GifInfoHandle(inputStream));
    }
    
    public GifAnimationMetaData(@NonNull final String s) throws IOException {
        this(new GifInfoHandle(s));
    }
    
    public GifAnimationMetaData(@NonNull final ByteBuffer byteBuffer) throws IOException {
        this(new GifInfoHandle(byteBuffer));
    }
    
    private GifAnimationMetaData(final GifInfoHandle gifInfoHandle) {
        this.mLoopCount = gifInfoHandle.getLoopCount();
        this.mDuration = gifInfoHandle.getDuration();
        this.mWidth = gifInfoHandle.getWidth();
        this.mHeight = gifInfoHandle.getHeight();
        this.mImageCount = gifInfoHandle.getNumberOfFrames();
        this.mMetadataBytesCount = gifInfoHandle.getMetadataByteCount();
        this.mPixelsBytesCount = gifInfoHandle.getAllocationByteCount();
        gifInfoHandle.recycle();
    }
    
    public GifAnimationMetaData(@NonNull final byte[] array) throws IOException {
        this(new GifInfoHandle(array));
    }
    
    public int describeContents() {
        return 0;
    }
    
    public long getAllocationByteCount() {
        return this.mPixelsBytesCount;
    }
    
    @Beta
    public long getDrawableAllocationByteCount(@Nullable final GifDrawable gifDrawable, @IntRange(from = 1L, to = 65535L) int i) {
        if (i < 1 || i > 65535) {
            throw new IllegalStateException("Sample size " + i + " out of range <1, " + '\uffff' + ">");
        }
        i *= i;
        long n;
        if (gifDrawable != null && !gifDrawable.mBuffer.isRecycled()) {
            if (Build$VERSION.SDK_INT >= 19) {
                n = gifDrawable.mBuffer.getAllocationByteCount();
            }
            else {
                n = gifDrawable.getFrameByteCount();
            }
        }
        else {
            n = this.mWidth * this.mHeight * 4 / i;
        }
        return this.mPixelsBytesCount / i + n;
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
        boolean b = true;
        if (this.mImageCount <= 1 || this.mDuration <= 0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public String toString() {
        String string;
        if (this.mLoopCount == 0) {
            string = "Infinity";
        }
        else {
            string = Integer.toString(this.mLoopCount);
        }
        String str = String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, loops: %s, duration: %d", this.mWidth, this.mHeight, this.mImageCount, string, this.mDuration);
        if (this.isAnimated()) {
            str = "Animated " + str;
        }
        return str;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mLoopCount);
        parcel.writeInt(this.mDuration);
        parcel.writeInt(this.mHeight);
        parcel.writeInt(this.mWidth);
        parcel.writeInt(this.mImageCount);
        parcel.writeLong(this.mMetadataBytesCount);
        parcel.writeLong(this.mPixelsBytesCount);
    }
}
