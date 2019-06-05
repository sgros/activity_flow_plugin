// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import java.io.IOException;
import android.support.annotation.NonNull;

public class GifDecoder
{
    private final GifInfoHandle mGifInfoHandle;
    
    public GifDecoder(@NonNull final InputSource inputSource) throws IOException {
        this(inputSource, null);
    }
    
    public GifDecoder(@NonNull final InputSource inputSource, @Nullable final GifOptions gifOptions) throws IOException {
        this.mGifInfoHandle = inputSource.open();
        if (gifOptions != null) {
            this.mGifInfoHandle.setOptions(gifOptions.inSampleSize, gifOptions.inIsOpaque);
        }
    }
    
    private void checkBuffer(final Bitmap bitmap) {
        if (bitmap.isRecycled()) {
            throw new IllegalArgumentException("Bitmap is recycled");
        }
        if (bitmap.getWidth() < this.mGifInfoHandle.getWidth() || bitmap.getHeight() < this.mGifInfoHandle.getHeight()) {
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
    
    public int getFrameDuration(@IntRange(from = 0L) final int n) {
        return this.mGifInfoHandle.getFrameDuration(n);
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
        boolean b = true;
        if (this.mGifInfoHandle.getNumberOfFrames() <= 1 || this.getDuration() <= 0) {
            b = false;
        }
        return b;
    }
    
    public void recycle() {
        this.mGifInfoHandle.recycle();
    }
    
    public void seekToFrame(@IntRange(from = 0L, to = 2147483647L) final int n, @NonNull final Bitmap bitmap) {
        this.checkBuffer(bitmap);
        this.mGifInfoHandle.seekToFrame(n, bitmap);
    }
    
    public void seekToTime(@IntRange(from = 0L, to = 2147483647L) final int n, @NonNull final Bitmap bitmap) {
        this.checkBuffer(bitmap);
        this.mGifInfoHandle.seekToTime(n, bitmap);
    }
}
