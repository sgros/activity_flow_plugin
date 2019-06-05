// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import android.media.MediaMetadataRetriever;
import java.io.IOException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import java.security.MessageDigest;
import java.nio.ByteBuffer;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.Option;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.ResourceDecoder;

public class VideoBitmapDecoder implements ResourceDecoder<ParcelFileDescriptor, Bitmap>
{
    private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY;
    public static final Option<Integer> FRAME_OPTION;
    public static final Option<Long> TARGET_FRAME;
    private final BitmapPool bitmapPool;
    private final MediaMetadataRetrieverFactory factory;
    
    static {
        TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", -1L, (Option.CacheKeyUpdater<Long>)new Option.CacheKeyUpdater<Long>() {
            private final ByteBuffer buffer = ByteBuffer.allocate(8);
            
            public void update(final byte[] input, final Long n, final MessageDigest messageDigest) {
                messageDigest.update(input);
                synchronized (this.buffer) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putLong(n).array());
                }
            }
        });
        FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", null, (Option.CacheKeyUpdater<Integer>)new Option.CacheKeyUpdater<Integer>() {
            private final ByteBuffer buffer = ByteBuffer.allocate(4);
            
            public void update(final byte[] input, final Integer n, final MessageDigest messageDigest) {
                if (n == null) {
                    return;
                }
                messageDigest.update(input);
                synchronized (this.buffer) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putInt(n).array());
                }
            }
        });
        DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
    }
    
    public VideoBitmapDecoder(final BitmapPool bitmapPool) {
        this(bitmapPool, VideoBitmapDecoder.DEFAULT_FACTORY);
    }
    
    VideoBitmapDecoder(final BitmapPool bitmapPool, final MediaMetadataRetrieverFactory factory) {
        this.bitmapPool = bitmapPool;
        this.factory = factory;
    }
    
    @Override
    public Resource<Bitmap> decode(final ParcelFileDescriptor parcelFileDescriptor, final int n, final int n2, final Options options) throws IOException {
        final long longValue = options.get(VideoBitmapDecoder.TARGET_FRAME);
        if (longValue < 0L && longValue != -1L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested frame must be non-negative, or DEFAULT_FRAME, given: ");
            sb.append(longValue);
            throw new IllegalArgumentException(sb.toString());
        }
        final Integer n3 = options.get(VideoBitmapDecoder.FRAME_OPTION);
        final MediaMetadataRetriever build = this.factory.build();
        try {
            try {
                build.setDataSource(parcelFileDescriptor.getFileDescriptor());
                Bitmap bitmap;
                if (longValue == -1L) {
                    bitmap = build.getFrameAtTime();
                }
                else if (n3 == null) {
                    bitmap = build.getFrameAtTime(longValue);
                }
                else {
                    bitmap = build.getFrameAtTime(longValue, (int)n3);
                }
                build.release();
                parcelFileDescriptor.close();
                return BitmapResource.obtain(bitmap, this.bitmapPool);
            }
            finally {}
        }
        catch (RuntimeException cause) {
            throw new IOException(cause);
        }
        build.release();
    }
    
    @Override
    public boolean handles(final ParcelFileDescriptor parcelFileDescriptor, final Options options) {
        return true;
    }
    
    static class MediaMetadataRetrieverFactory
    {
        public MediaMetadataRetriever build() {
            return new MediaMetadataRetriever();
        }
    }
}
