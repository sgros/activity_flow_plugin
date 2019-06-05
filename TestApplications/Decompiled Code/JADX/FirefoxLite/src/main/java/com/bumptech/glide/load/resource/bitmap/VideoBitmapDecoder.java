package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Option.CacheKeyUpdater;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoBitmapDecoder implements ResourceDecoder<ParcelFileDescriptor, Bitmap> {
    private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
    public static final Option<Integer> FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", null, new C06792());
    public static final Option<Long> TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", Long.valueOf(-1), new C06781());
    private final BitmapPool bitmapPool;
    private final MediaMetadataRetrieverFactory factory;

    static class MediaMetadataRetrieverFactory {
        MediaMetadataRetrieverFactory() {
        }

        public MediaMetadataRetriever build() {
            return new MediaMetadataRetriever();
        }
    }

    /* renamed from: com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder$1 */
    static class C06781 implements CacheKeyUpdater<Long> {
        private final ByteBuffer buffer = ByteBuffer.allocate(8);

        C06781() {
        }

        public void update(byte[] bArr, Long l, MessageDigest messageDigest) {
            messageDigest.update(bArr);
            synchronized (this.buffer) {
                this.buffer.position(0);
                messageDigest.update(this.buffer.putLong(l.longValue()).array());
            }
        }
    }

    /* renamed from: com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder$2 */
    static class C06792 implements CacheKeyUpdater<Integer> {
        private final ByteBuffer buffer = ByteBuffer.allocate(4);

        C06792() {
        }

        public void update(byte[] bArr, Integer num, MessageDigest messageDigest) {
            if (num != null) {
                messageDigest.update(bArr);
                synchronized (this.buffer) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putInt(num.intValue()).array());
                }
            }
        }
    }

    public boolean handles(ParcelFileDescriptor parcelFileDescriptor, Options options) {
        return true;
    }

    public VideoBitmapDecoder(BitmapPool bitmapPool) {
        this(bitmapPool, DEFAULT_FACTORY);
    }

    VideoBitmapDecoder(BitmapPool bitmapPool, MediaMetadataRetrieverFactory mediaMetadataRetrieverFactory) {
        this.bitmapPool = bitmapPool;
        this.factory = mediaMetadataRetrieverFactory;
    }

    public Resource<Bitmap> decode(ParcelFileDescriptor parcelFileDescriptor, int i, int i2, Options options) throws IOException {
        long longValue = ((Long) options.get(TARGET_FRAME)).longValue();
        if (longValue >= 0 || longValue == -1) {
            Integer num = (Integer) options.get(FRAME_OPTION);
            MediaMetadataRetriever build = this.factory.build();
            try {
                Bitmap frameAtTime;
                build.setDataSource(parcelFileDescriptor.getFileDescriptor());
                if (longValue == -1) {
                    frameAtTime = build.getFrameAtTime();
                } else if (num == null) {
                    frameAtTime = build.getFrameAtTime(longValue);
                } else {
                    frameAtTime = build.getFrameAtTime(longValue, num.intValue());
                }
                build.release();
                parcelFileDescriptor.close();
                return BitmapResource.obtain(frameAtTime, this.bitmapPool);
            } catch (RuntimeException e) {
                throw new IOException(e);
            } catch (Throwable th) {
                build.release();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested frame must be non-negative, or DEFAULT_FRAME, given: ");
            stringBuilder.append(longValue);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
