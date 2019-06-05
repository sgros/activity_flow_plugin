// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.Nullable;
import android.net.Uri;
import android.content.ContentResolver;
import android.support.annotation.RawRes;
import android.support.annotation.DrawableRes;
import android.content.res.Resources;
import java.io.InputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class InputSource
{
    private InputSource() {
    }
    
    final GifDrawable build(final GifDrawable gifDrawable, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, final boolean b, final GifOptions gifOptions) throws IOException {
        final GifInfoHandle open = this.open();
        open.setOptions(gifOptions.inSampleSize, gifOptions.inIsOpaque);
        return new GifDrawable(open, gifDrawable, scheduledThreadPoolExecutor, b);
    }
    
    abstract GifInfoHandle open() throws IOException;
    
    public static class AssetFileDescriptorSource extends InputSource
    {
        private final AssetFileDescriptor mAssetFileDescriptor;
        
        public AssetFileDescriptorSource(@NonNull final AssetFileDescriptor mAssetFileDescriptor) {
            super(null);
            this.mAssetFileDescriptor = mAssetFileDescriptor;
        }
        
        @Override
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetFileDescriptor);
        }
    }
    
    public static final class AssetSource extends InputSource
    {
        private final AssetManager mAssetManager;
        private final String mAssetName;
        
        public AssetSource(@NonNull final AssetManager mAssetManager, @NonNull final String mAssetName) {
            super(null);
            this.mAssetManager = mAssetManager;
            this.mAssetName = mAssetName;
        }
        
        @Override
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetManager.openFd(this.mAssetName));
        }
    }
    
    public static final class ByteArraySource extends InputSource
    {
        private final byte[] bytes;
        
        public ByteArraySource(@NonNull final byte[] bytes) {
            super(null);
            this.bytes = bytes;
        }
        
        @Override
        GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.bytes);
        }
    }
    
    public static final class DirectByteBufferSource extends InputSource
    {
        private final ByteBuffer byteBuffer;
        
        public DirectByteBufferSource(@NonNull final ByteBuffer byteBuffer) {
            super(null);
            this.byteBuffer = byteBuffer;
        }
        
        @Override
        GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.byteBuffer);
        }
    }
    
    public static final class FileDescriptorSource extends InputSource
    {
        private final FileDescriptor mFd;
        
        public FileDescriptorSource(@NonNull final FileDescriptor mFd) {
            super(null);
            this.mFd = mFd;
        }
        
        @Override
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mFd);
        }
    }
    
    public static final class FileSource extends InputSource
    {
        private final String mPath;
        
        public FileSource(@NonNull final File file) {
            super(null);
            this.mPath = file.getPath();
        }
        
        public FileSource(@NonNull final String mPath) {
            super(null);
            this.mPath = mPath;
        }
        
        @Override
        GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.mPath);
        }
    }
    
    public static final class InputStreamSource extends InputSource
    {
        private final InputStream inputStream;
        
        public InputStreamSource(@NonNull final InputStream inputStream) {
            super(null);
            this.inputStream = inputStream;
        }
        
        @Override
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.inputStream);
        }
    }
    
    public static class ResourcesSource extends InputSource
    {
        private final int mResourceId;
        private final Resources mResources;
        
        public ResourcesSource(@NonNull final Resources mResources, @DrawableRes @RawRes final int mResourceId) {
            super(null);
            this.mResources = mResources;
            this.mResourceId = mResourceId;
        }
        
        @Override
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mResources.openRawResourceFd(this.mResourceId));
        }
    }
    
    public static final class UriSource extends InputSource
    {
        private final ContentResolver mContentResolver;
        private final Uri mUri;
        
        public UriSource(@Nullable final ContentResolver mContentResolver, @NonNull final Uri mUri) {
            super(null);
            this.mContentResolver = mContentResolver;
            this.mUri = mUri;
        }
        
        @Override
        GifInfoHandle open() throws IOException {
            return GifInfoHandle.openUri(this.mContentResolver, this.mUri);
        }
    }
}
