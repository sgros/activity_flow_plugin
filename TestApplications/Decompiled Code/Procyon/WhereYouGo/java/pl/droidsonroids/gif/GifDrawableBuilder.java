// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import pl.droidsonroids.gif.annotations.Beta;
import android.support.annotation.Nullable;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.FileDescriptor;
import java.io.File;
import android.content.res.Resources;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.content.ContentResolver;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class GifDrawableBuilder
{
    private ScheduledThreadPoolExecutor mExecutor;
    private InputSource mInputSource;
    private boolean mIsRenderingTriggeredOnDraw;
    private GifDrawable mOldDrawable;
    private GifOptions mOptions;
    
    public GifDrawableBuilder() {
        this.mIsRenderingTriggeredOnDraw = true;
        this.mOptions = new GifOptions();
    }
    
    public GifDrawable build() throws IOException {
        if (this.mInputSource == null) {
            throw new NullPointerException("Source is not set");
        }
        return this.mInputSource.build(this.mOldDrawable, this.mExecutor, this.mIsRenderingTriggeredOnDraw, this.mOptions);
    }
    
    public GifDrawableBuilder from(final ContentResolver contentResolver, final Uri uri) {
        this.mInputSource = new InputSource.UriSource(contentResolver, uri);
        return this;
    }
    
    public GifDrawableBuilder from(final AssetFileDescriptor assetFileDescriptor) {
        this.mInputSource = new InputSource.AssetFileDescriptorSource(assetFileDescriptor);
        return this;
    }
    
    public GifDrawableBuilder from(final AssetManager assetManager, final String s) {
        this.mInputSource = new InputSource.AssetSource(assetManager, s);
        return this;
    }
    
    public GifDrawableBuilder from(final Resources resources, final int n) {
        this.mInputSource = new InputSource.ResourcesSource(resources, n);
        return this;
    }
    
    public GifDrawableBuilder from(final File file) {
        this.mInputSource = new InputSource.FileSource(file);
        return this;
    }
    
    public GifDrawableBuilder from(final FileDescriptor fileDescriptor) {
        this.mInputSource = new InputSource.FileDescriptorSource(fileDescriptor);
        return this;
    }
    
    public GifDrawableBuilder from(final InputStream inputStream) {
        this.mInputSource = new InputSource.InputStreamSource(inputStream);
        return this;
    }
    
    public GifDrawableBuilder from(final String s) {
        this.mInputSource = new InputSource.FileSource(s);
        return this;
    }
    
    public GifDrawableBuilder from(final ByteBuffer byteBuffer) {
        this.mInputSource = new InputSource.DirectByteBufferSource(byteBuffer);
        return this;
    }
    
    public GifDrawableBuilder from(final byte[] array) {
        this.mInputSource = new InputSource.ByteArraySource(array);
        return this;
    }
    
    @Beta
    public GifDrawableBuilder options(@Nullable final GifOptions from) {
        this.mOptions.setFrom(from);
        return this;
    }
    
    public GifDrawableBuilder renderingTriggeredOnDraw(final boolean mIsRenderingTriggeredOnDraw) {
        this.mIsRenderingTriggeredOnDraw = mIsRenderingTriggeredOnDraw;
        return this;
    }
    
    public GifDrawableBuilder sampleSize(@IntRange(from = 1L, to = 65535L) final int inSampleSize) {
        this.mOptions.setInSampleSize(inSampleSize);
        return this;
    }
    
    public GifDrawableBuilder setRenderingTriggeredOnDraw(final boolean b) {
        return this.renderingTriggeredOnDraw(b);
    }
    
    public GifDrawableBuilder taskExecutor(final ScheduledThreadPoolExecutor mExecutor) {
        this.mExecutor = mExecutor;
        return this;
    }
    
    public GifDrawableBuilder threadPoolSize(final int corePoolSize) {
        this.mExecutor = new ScheduledThreadPoolExecutor(corePoolSize);
        return this;
    }
    
    public GifDrawableBuilder with(final GifDrawable mOldDrawable) {
        this.mOldDrawable = mOldDrawable;
        return this;
    }
}
