// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.nio.ByteBuffer;
import com.bumptech.glide.util.Preconditions;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.io.IOException;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.nio.charset.Charset;
import com.bumptech.glide.load.ImageHeaderParser;

public final class DefaultImageHeaderParser implements ImageHeaderParser
{
    private static final int[] BYTES_PER_FORMAT;
    static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
    
    static {
        JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\u0000\u0000".getBytes(Charset.forName("UTF-8"));
        BYTES_PER_FORMAT = new int[] { 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8 };
    }
    
    private static int calcTagOffset(final int n, final int n2) {
        return n + 2 + n2 * 12;
    }
    
    private int getOrientation(final Reader reader, final ArrayPool arrayPool) throws IOException {
        final int uInt16 = reader.getUInt16();
        if (!handles(uInt16)) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Parser doesn't handle magic number: ");
                sb.append(uInt16);
                Log.d("DfltImageHeaderParser", sb.toString());
            }
            return -1;
        }
        final int moveToExifSegmentAndGetLength = this.moveToExifSegmentAndGetLength(reader);
        if (moveToExifSegmentAndGetLength == -1) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                Log.d("DfltImageHeaderParser", "Failed to parse exif segment length, or exif segment not found");
            }
            return -1;
        }
        final byte[] array = arrayPool.get(moveToExifSegmentAndGetLength, byte[].class);
        try {
            return this.parseExifSegment(reader, array, moveToExifSegmentAndGetLength);
        }
        finally {
            arrayPool.put(array, byte[].class);
        }
    }
    
    private ImageType getType(final Reader reader) throws IOException {
        final int uInt16 = reader.getUInt16();
        if (uInt16 == 65496) {
            return ImageType.JPEG;
        }
        final int n = (uInt16 << 16 & 0xFFFF0000) | (reader.getUInt16() & 0xFFFF);
        if (n == -1991225785) {
            reader.skip(21L);
            ImageType imageType;
            if (reader.getByte() >= 3) {
                imageType = ImageType.PNG_A;
            }
            else {
                imageType = ImageType.PNG;
            }
            return imageType;
        }
        if (n >> 8 == 4671814) {
            return ImageType.GIF;
        }
        if (n != 1380533830) {
            return ImageType.UNKNOWN;
        }
        reader.skip(4L);
        if (((reader.getUInt16() << 16 & 0xFFFF0000) | (reader.getUInt16() & 0xFFFF)) != 0x57454250) {
            return ImageType.UNKNOWN;
        }
        final int n2 = (reader.getUInt16() << 16 & 0xFFFF0000) | (reader.getUInt16() & 0xFFFF);
        if ((n2 & 0xFFFFFF00) != 0x56503800) {
            return ImageType.UNKNOWN;
        }
        final int n3 = n2 & 0xFF;
        if (n3 == 88) {
            reader.skip(4L);
            ImageType imageType2;
            if ((reader.getByte() & 0x10) != 0x0) {
                imageType2 = ImageType.WEBP_A;
            }
            else {
                imageType2 = ImageType.WEBP;
            }
            return imageType2;
        }
        if (n3 == 76) {
            reader.skip(4L);
            ImageType imageType3;
            if ((reader.getByte() & 0x8) != 0x0) {
                imageType3 = ImageType.WEBP_A;
            }
            else {
                imageType3 = ImageType.WEBP;
            }
            return imageType3;
        }
        return ImageType.WEBP;
    }
    
    private static boolean handles(final int n) {
        return (n & 0xFFD8) == 0xFFD8 || n == 19789 || n == 18761;
    }
    
    private boolean hasJpegExifPreamble(final byte[] array, int n) {
        boolean b2;
        final boolean b = b2 = (array != null && n > DefaultImageHeaderParser.JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length);
        if (b) {
            n = 0;
            while (true) {
                b2 = b;
                if (n >= DefaultImageHeaderParser.JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
                    break;
                }
                if (array[n] != DefaultImageHeaderParser.JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[n]) {
                    b2 = false;
                    break;
                }
                ++n;
            }
        }
        return b2;
    }
    
    private int moveToExifSegmentAndGetLength(final Reader reader) throws IOException {
        long skip;
        long n;
        short uInt9;
        int i;
        do {
            final short uInt8 = reader.getUInt8();
            if (uInt8 != 255) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown segmentId=");
                    sb.append(uInt8);
                    Log.d("DfltImageHeaderParser", sb.toString());
                }
                return -1;
            }
            uInt9 = reader.getUInt8();
            if (uInt9 == 218) {
                return -1;
            }
            if (uInt9 == 217) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    Log.d("DfltImageHeaderParser", "Found MARKER_EOI in exif segment");
                }
                return -1;
            }
            i = reader.getUInt16() - 2;
            if (uInt9 == 225) {
                return i;
            }
            n = i;
            skip = reader.skip(n);
        } while (skip == n);
        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to skip enough data, type: ");
            sb2.append(uInt9);
            sb2.append(", wanted to skip: ");
            sb2.append(i);
            sb2.append(", but actually skipped: ");
            sb2.append(skip);
            Log.d("DfltImageHeaderParser", sb2.toString());
        }
        return -1;
    }
    
    private static int parseExifSegment(final RandomAccessReader randomAccessReader) {
        final int length = "Exif\u0000\u0000".length();
        final short int16 = randomAccessReader.getInt16(length);
        ByteOrder byteOrder;
        if (int16 == 19789) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        else if (int16 == 18761) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        }
        else {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown endianness = ");
                sb.append(int16);
                Log.d("DfltImageHeaderParser", sb.toString());
            }
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        randomAccessReader.order(byteOrder);
        final int n = randomAccessReader.getInt32(length + 4) + length;
        for (short int17 = randomAccessReader.getInt16(n), i = 0; i < int17; ++i) {
            final int calcTagOffset = calcTagOffset(n, i);
            final short int18 = randomAccessReader.getInt16(calcTagOffset);
            if (int18 == 274) {
                final short int19 = randomAccessReader.getInt16(calcTagOffset + 2);
                if (int19 >= 1 && int19 <= 12) {
                    final int int20 = randomAccessReader.getInt32(calcTagOffset + 4);
                    if (int20 < 0) {
                        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                            Log.d("DfltImageHeaderParser", "Negative tiff component count");
                        }
                    }
                    else {
                        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Got tagIndex=");
                            sb2.append(i);
                            sb2.append(" tagType=");
                            sb2.append(int18);
                            sb2.append(" formatCode=");
                            sb2.append(int19);
                            sb2.append(" componentCount=");
                            sb2.append(int20);
                            Log.d("DfltImageHeaderParser", sb2.toString());
                        }
                        final int n2 = int20 + DefaultImageHeaderParser.BYTES_PER_FORMAT[int19];
                        if (n2 > 4) {
                            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("Got byte count > 4, not orientation, continuing, formatCode=");
                                sb3.append(int19);
                                Log.d("DfltImageHeaderParser", sb3.toString());
                            }
                        }
                        else {
                            final int j = calcTagOffset + 8;
                            if (j >= 0 && j <= randomAccessReader.length()) {
                                if (n2 >= 0 && n2 + j <= randomAccessReader.length()) {
                                    return randomAccessReader.getInt16(j);
                                }
                                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Illegal number of bytes for TI tag data tagType=");
                                    sb4.append(int18);
                                    Log.d("DfltImageHeaderParser", sb4.toString());
                                }
                            }
                            else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("Illegal tagValueOffset=");
                                sb5.append(j);
                                sb5.append(" tagType=");
                                sb5.append(int18);
                                Log.d("DfltImageHeaderParser", sb5.toString());
                            }
                        }
                    }
                }
                else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Got invalid format code = ");
                    sb6.append(int19);
                    Log.d("DfltImageHeaderParser", sb6.toString());
                }
            }
        }
        return -1;
    }
    
    private int parseExifSegment(final Reader reader, final byte[] array, final int i) throws IOException {
        final int read = reader.read(array, i);
        if (read != i) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to read exif segment data, length: ");
                sb.append(i);
                sb.append(", actually read: ");
                sb.append(read);
                Log.d("DfltImageHeaderParser", sb.toString());
            }
            return -1;
        }
        if (this.hasJpegExifPreamble(array, i)) {
            return parseExifSegment(new RandomAccessReader(array, i));
        }
        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            Log.d("DfltImageHeaderParser", "Missing jpeg exif preamble");
        }
        return -1;
    }
    
    @Override
    public int getOrientation(final InputStream inputStream, final ArrayPool arrayPool) throws IOException {
        return this.getOrientation((Reader)new StreamReader(Preconditions.checkNotNull(inputStream)), Preconditions.checkNotNull(arrayPool));
    }
    
    @Override
    public ImageType getType(final InputStream inputStream) throws IOException {
        return this.getType((Reader)new StreamReader(Preconditions.checkNotNull(inputStream)));
    }
    
    private static final class RandomAccessReader
    {
        private final ByteBuffer data;
        
        RandomAccessReader(final byte[] array, final int n) {
            this.data = (ByteBuffer)ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).limit(n);
        }
        
        private boolean isAvailable(final int n, final int n2) {
            return this.data.remaining() - n >= n2;
        }
        
        short getInt16(int n) {
            short short1;
            if (this.isAvailable(n, 2)) {
                n = (short1 = this.data.getShort(n));
            }
            else {
                n = (short1 = -1);
            }
            return short1;
        }
        
        int getInt32(int int1) {
            if (this.isAvailable(int1, 4)) {
                int1 = this.data.getInt(int1);
            }
            else {
                int1 = -1;
            }
            return int1;
        }
        
        int length() {
            return this.data.remaining();
        }
        
        void order(final ByteOrder bo) {
            this.data.order(bo);
        }
    }
    
    private interface Reader
    {
        int getByte() throws IOException;
        
        int getUInt16() throws IOException;
        
        short getUInt8() throws IOException;
        
        int read(final byte[] p0, final int p1) throws IOException;
        
        long skip(final long p0) throws IOException;
    }
    
    private static final class StreamReader implements Reader
    {
        private final InputStream is;
        
        StreamReader(final InputStream is) {
            this.is = is;
        }
        
        @Override
        public int getByte() throws IOException {
            return this.is.read();
        }
        
        @Override
        public int getUInt16() throws IOException {
            return (this.is.read() << 8 & 0xFF00) | (this.is.read() & 0xFF);
        }
        
        @Override
        public short getUInt8() throws IOException {
            return (short)(this.is.read() & 0xFF);
        }
        
        @Override
        public int read(final byte[] b, final int n) throws IOException {
            int i;
            int read;
            for (i = n; i > 0; i -= read) {
                read = this.is.read(b, n - i, i);
                if (read == -1) {
                    break;
                }
            }
            return n - i;
        }
        
        @Override
        public long skip(final long n) throws IOException {
            if (n < 0L) {
                return 0L;
            }
            long n2 = n;
            while (n2 > 0L) {
                final long skip = this.is.skip(n2);
                if (skip > 0L) {
                    n2 -= skip;
                }
                else {
                    if (this.is.read() == -1) {
                        break;
                    }
                    --n2;
                }
            }
            return n - n2;
        }
    }
}
