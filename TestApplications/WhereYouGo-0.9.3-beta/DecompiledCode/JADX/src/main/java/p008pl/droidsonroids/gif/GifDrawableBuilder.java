package p008pl.droidsonroids.gif;

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
import p008pl.droidsonroids.gif.InputSource.AssetFileDescriptorSource;
import p008pl.droidsonroids.gif.InputSource.AssetSource;
import p008pl.droidsonroids.gif.InputSource.ByteArraySource;
import p008pl.droidsonroids.gif.InputSource.DirectByteBufferSource;
import p008pl.droidsonroids.gif.InputSource.FileDescriptorSource;
import p008pl.droidsonroids.gif.InputSource.FileSource;
import p008pl.droidsonroids.gif.InputSource.InputStreamSource;
import p008pl.droidsonroids.gif.InputSource.ResourcesSource;
import p008pl.droidsonroids.gif.InputSource.UriSource;
import p008pl.droidsonroids.gif.annotations.Beta;

/* renamed from: pl.droidsonroids.gif.GifDrawableBuilder */
public class GifDrawableBuilder {
    private ScheduledThreadPoolExecutor mExecutor;
    private InputSource mInputSource;
    private boolean mIsRenderingTriggeredOnDraw = true;
    private GifDrawable mOldDrawable;
    private GifOptions mOptions = new GifOptions();

    public GifDrawableBuilder sampleSize(@IntRange(from = 1, to = 65535) int sampleSize) {
        this.mOptions.setInSampleSize(sampleSize);
        return this;
    }

    public GifDrawable build() throws IOException {
        if (this.mInputSource != null) {
            return this.mInputSource.build(this.mOldDrawable, this.mExecutor, this.mIsRenderingTriggeredOnDraw, this.mOptions);
        }
        throw new NullPointerException("Source is not set");
    }

    public GifDrawableBuilder with(GifDrawable drawable) {
        this.mOldDrawable = drawable;
        return this;
    }

    public GifDrawableBuilder threadPoolSize(int threadPoolSize) {
        this.mExecutor = new ScheduledThreadPoolExecutor(threadPoolSize);
        return this;
    }

    public GifDrawableBuilder taskExecutor(ScheduledThreadPoolExecutor executor) {
        this.mExecutor = executor;
        return this;
    }

    public GifDrawableBuilder renderingTriggeredOnDraw(boolean isRenderingTriggeredOnDraw) {
        this.mIsRenderingTriggeredOnDraw = isRenderingTriggeredOnDraw;
        return this;
    }

    public GifDrawableBuilder setRenderingTriggeredOnDraw(boolean isRenderingTriggeredOnDraw) {
        return renderingTriggeredOnDraw(isRenderingTriggeredOnDraw);
    }

    @Beta
    public GifDrawableBuilder options(@Nullable GifOptions options) {
        this.mOptions.setFrom(options);
        return this;
    }

    public GifDrawableBuilder from(InputStream inputStream) {
        this.mInputSource = new InputStreamSource(inputStream);
        return this;
    }

    public GifDrawableBuilder from(AssetFileDescriptor assetFileDescriptor) {
        this.mInputSource = new AssetFileDescriptorSource(assetFileDescriptor);
        return this;
    }

    public GifDrawableBuilder from(FileDescriptor fileDescriptor) {
        this.mInputSource = new FileDescriptorSource(fileDescriptor);
        return this;
    }

    public GifDrawableBuilder from(AssetManager assetManager, String assetName) {
        this.mInputSource = new AssetSource(assetManager, assetName);
        return this;
    }

    public GifDrawableBuilder from(ContentResolver contentResolver, Uri uri) {
        this.mInputSource = new UriSource(contentResolver, uri);
        return this;
    }

    public GifDrawableBuilder from(File file) {
        this.mInputSource = new FileSource(file);
        return this;
    }

    public GifDrawableBuilder from(String filePath) {
        this.mInputSource = new FileSource(filePath);
        return this;
    }

    public GifDrawableBuilder from(byte[] bytes) {
        this.mInputSource = new ByteArraySource(bytes);
        return this;
    }

    public GifDrawableBuilder from(ByteBuffer byteBuffer) {
        this.mInputSource = new DirectByteBufferSource(byteBuffer);
        return this;
    }

    public GifDrawableBuilder from(Resources resources, int resourceId) {
        this.mInputSource = new ResourcesSource(resources, resourceId);
        return this;
    }
}
