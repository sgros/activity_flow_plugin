// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import java.io.IOException;
import android.support.annotation.Nullable;
import pl.droidsonroids.gif.annotations.Beta;

@Beta
public class GifTexImage2D
{
    private final GifInfoHandle mGifInfoHandle;
    
    public GifTexImage2D(final InputSource inputSource, @Nullable final GifOptions gifOptions) throws IOException {
        GifOptions gifOptions2 = gifOptions;
        if (gifOptions == null) {
            gifOptions2 = new GifOptions();
        }
        (this.mGifInfoHandle = inputSource.open()).setOptions(gifOptions2.inSampleSize, gifOptions2.inIsOpaque);
        this.mGifInfoHandle.initTexImageDescriptor();
    }
    
    @Override
    protected final void finalize() throws Throwable {
        try {
            this.recycle();
        }
        finally {
            super.finalize();
        }
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
    
    public int getNumberOfFrames() {
        return this.mGifInfoHandle.getNumberOfFrames();
    }
    
    public int getWidth() {
        return this.mGifInfoHandle.getWidth();
    }
    
    public void glTexImage2D(final int n, final int n2) {
        this.mGifInfoHandle.glTexImage2D(n, n2);
    }
    
    public void glTexSubImage2D(final int n, final int n2) {
        this.mGifInfoHandle.glTexSubImage2D(n, n2);
    }
    
    public void recycle() {
        if (this.mGifInfoHandle != null) {
            this.mGifInfoHandle.recycle();
        }
    }
    
    public void seekToFrame(@IntRange(from = 0L) final int n) {
        this.mGifInfoHandle.seekToFrameGL(n);
    }
    
    public void startDecoderThread() {
        this.mGifInfoHandle.startDecoderThread();
    }
    
    public void stopDecoderThread() {
        this.mGifInfoHandle.stopDecoderThread();
    }
}
