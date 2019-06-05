package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import com.adjust.sdk.Constants;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParser.ImageType;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
    private static final int[] BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\u0000\u0000".getBytes(Charset.forName(Constants.ENCODING));

    private static final class RandomAccessReader {
        private final ByteBuffer data;

        RandomAccessReader(byte[] bArr, int i) {
            this.data = (ByteBuffer) ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).limit(i);
        }

        /* Access modifiers changed, original: 0000 */
        public void order(ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }

        /* Access modifiers changed, original: 0000 */
        public int length() {
            return this.data.remaining();
        }

        /* Access modifiers changed, original: 0000 */
        public int getInt32(int i) {
            return isAvailable(i, 4) ? this.data.getInt(i) : -1;
        }

        /* Access modifiers changed, original: 0000 */
        public short getInt16(int i) {
            return isAvailable(i, 2) ? this.data.getShort(i) : (short) -1;
        }

        private boolean isAvailable(int i, int i2) {
            return this.data.remaining() - i >= i2;
        }
    }

    private interface Reader {
        int getByte() throws IOException;

        int getUInt16() throws IOException;

        short getUInt8() throws IOException;

        int read(byte[] bArr, int i) throws IOException;

        long skip(long j) throws IOException;
    }

    private static final class StreamReader implements Reader {
        /* renamed from: is */
        private final InputStream f61is;

        StreamReader(InputStream inputStream) {
            this.f61is = inputStream;
        }

        public int getUInt16() throws IOException {
            return ((this.f61is.read() << 8) & 65280) | (this.f61is.read() & 255);
        }

        public short getUInt8() throws IOException {
            return (short) (this.f61is.read() & 255);
        }

        public long skip(long j) throws IOException {
            if (j < 0) {
                return 0;
            }
            long j2 = j;
            while (j2 > 0) {
                long skip = this.f61is.skip(j2);
                if (skip > 0) {
                    j2 -= skip;
                } else if (this.f61is.read() == -1) {
                    break;
                } else {
                    j2--;
                }
            }
            return j - j2;
        }

        public int read(byte[] bArr, int i) throws IOException {
            int i2 = i;
            while (i2 > 0) {
                int read = this.f61is.read(bArr, i - i2, i2);
                if (read == -1) {
                    break;
                }
                i2 -= read;
            }
            return i - i2;
        }

        public int getByte() throws IOException {
            return this.f61is.read();
        }
    }

    private static int calcTagOffset(int i, int i2) {
        return (i + 2) + (i2 * 12);
    }

    private static boolean handles(int i) {
        return (i & 65496) == 65496 || i == 19789 || i == 18761;
    }

    public ImageType getType(InputStream inputStream) throws IOException {
        return getType(new StreamReader((InputStream) Preconditions.checkNotNull(inputStream)));
    }

    public int getOrientation(InputStream inputStream, ArrayPool arrayPool) throws IOException {
        return getOrientation(new StreamReader((InputStream) Preconditions.checkNotNull(inputStream)), (ArrayPool) Preconditions.checkNotNull(arrayPool));
    }

    private ImageType getType(Reader reader) throws IOException {
        int uInt16 = reader.getUInt16();
        if (uInt16 == 65496) {
            return ImageType.JPEG;
        }
        uInt16 = ((uInt16 << 16) & -65536) | (reader.getUInt16() & 65535);
        if (uInt16 == -1991225785) {
            reader.skip(21);
            return reader.getByte() >= 3 ? ImageType.PNG_A : ImageType.PNG;
        } else if ((uInt16 >> 8) == 4671814) {
            return ImageType.GIF;
        } else {
            if (uInt16 != 1380533830) {
                return ImageType.UNKNOWN;
            }
            reader.skip(4);
            if ((((reader.getUInt16() << 16) & -65536) | (reader.getUInt16() & 65535)) != 1464156752) {
                return ImageType.UNKNOWN;
            }
            uInt16 = ((reader.getUInt16() << 16) & -65536) | (reader.getUInt16() & 65535);
            if ((uInt16 & -256) != 1448097792) {
                return ImageType.UNKNOWN;
            }
            uInt16 &= 255;
            if (uInt16 == 88) {
                reader.skip(4);
                return (reader.getByte() & 16) != 0 ? ImageType.WEBP_A : ImageType.WEBP;
            } else if (uInt16 != 76) {
                return ImageType.WEBP;
            } else {
                reader.skip(4);
                return (reader.getByte() & 8) != 0 ? ImageType.WEBP_A : ImageType.WEBP;
            }
        }
    }

    private int getOrientation(Reader reader, ArrayPool arrayPool) throws IOException {
        int uInt16 = reader.getUInt16();
        if (handles(uInt16)) {
            uInt16 = moveToExifSegmentAndGetLength(reader);
            if (uInt16 == -1) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    Log.d("DfltImageHeaderParser", "Failed to parse exif segment length, or exif segment not found");
                }
                return -1;
            }
            byte[] bArr = (byte[]) arrayPool.get(uInt16, byte[].class);
            try {
                int parseExifSegment = parseExifSegment(reader, bArr, uInt16);
                return parseExifSegment;
            } finally {
                arrayPool.put(bArr, byte[].class);
            }
        } else {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Parser doesn't handle magic number: ");
                stringBuilder.append(uInt16);
                Log.d("DfltImageHeaderParser", stringBuilder.toString());
            }
            return -1;
        }
    }

    private int parseExifSegment(Reader reader, byte[] bArr, int i) throws IOException {
        int read = reader.read(bArr, i);
        if (read != i) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to read exif segment data, length: ");
                stringBuilder.append(i);
                stringBuilder.append(", actually read: ");
                stringBuilder.append(read);
                Log.d("DfltImageHeaderParser", stringBuilder.toString());
            }
            return -1;
        } else if (hasJpegExifPreamble(bArr, i)) {
            return parseExifSegment(new RandomAccessReader(bArr, i));
        } else {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                Log.d("DfltImageHeaderParser", "Missing jpeg exif preamble");
            }
            return -1;
        }
    }

    private boolean hasJpegExifPreamble(byte[] bArr, int i) {
        boolean z = bArr != null && i > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length;
        if (!z) {
            return z;
        }
        for (int i2 = 0; i2 < JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length; i2++) {
            if (bArr[i2] != JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[i2]) {
                return false;
            }
        }
        return z;
    }

    private int moveToExifSegmentAndGetLength(Reader reader) throws IOException {
        long skip;
        short uInt8;
        int uInt16;
        long j;
        do {
            uInt8 = reader.getUInt8();
            if (uInt8 != (short) 255) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown segmentId=");
                    stringBuilder.append(uInt8);
                    Log.d("DfltImageHeaderParser", stringBuilder.toString());
                }
                return -1;
            }
            uInt8 = reader.getUInt8();
            if (uInt8 == (short) 218) {
                return -1;
            }
            if (uInt8 == (short) 217) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    Log.d("DfltImageHeaderParser", "Found MARKER_EOI in exif segment");
                }
                return -1;
            }
            uInt16 = reader.getUInt16() - 2;
            if (uInt8 == (short) 225) {
                return uInt16;
            }
            j = (long) uInt16;
            skip = reader.skip(j);
        } while (skip == j);
        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to skip enough data, type: ");
            stringBuilder2.append(uInt8);
            stringBuilder2.append(", wanted to skip: ");
            stringBuilder2.append(uInt16);
            stringBuilder2.append(", but actually skipped: ");
            stringBuilder2.append(skip);
            Log.d("DfltImageHeaderParser", stringBuilder2.toString());
        }
        return -1;
    }

    private static int parseExifSegment(RandomAccessReader randomAccessReader) {
        ByteOrder byteOrder;
        int length = "Exif\u0000\u0000".length();
        short int16 = randomAccessReader.getInt16(length);
        if (int16 == (short) 19789) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else if (int16 == (short) 18761) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        } else {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown endianness = ");
                stringBuilder.append(int16);
                Log.d("DfltImageHeaderParser", stringBuilder.toString());
            }
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        randomAccessReader.order(byteOrder);
        int int32 = randomAccessReader.getInt32(length + 4) + length;
        short int162 = randomAccessReader.getInt16(int32);
        for (short s = (short) 0; s < int162; s++) {
            int calcTagOffset = calcTagOffset(int32, s);
            short int163 = randomAccessReader.getInt16(calcTagOffset);
            if (int163 == (short) 274) {
                short int164 = randomAccessReader.getInt16(calcTagOffset + 2);
                StringBuilder stringBuilder2;
                if (int164 >= (short) 1 && int164 <= (short) 12) {
                    int int322 = randomAccessReader.getInt32(calcTagOffset + 4);
                    if (int322 >= 0) {
                        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("Got tagIndex=");
                            stringBuilder3.append(s);
                            stringBuilder3.append(" tagType=");
                            stringBuilder3.append(int163);
                            stringBuilder3.append(" formatCode=");
                            stringBuilder3.append(int164);
                            stringBuilder3.append(" componentCount=");
                            stringBuilder3.append(int322);
                            Log.d("DfltImageHeaderParser", stringBuilder3.toString());
                        }
                        int322 += BYTES_PER_FORMAT[int164];
                        if (int322 <= 4) {
                            calcTagOffset += 8;
                            if (calcTagOffset < 0 || calcTagOffset > randomAccessReader.length()) {
                                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                                    StringBuilder stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append("Illegal tagValueOffset=");
                                    stringBuilder4.append(calcTagOffset);
                                    stringBuilder4.append(" tagType=");
                                    stringBuilder4.append(int163);
                                    Log.d("DfltImageHeaderParser", stringBuilder4.toString());
                                }
                            } else if (int322 >= 0 && int322 + calcTagOffset <= randomAccessReader.length()) {
                                return randomAccessReader.getInt16(calcTagOffset);
                            } else {
                                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                                    StringBuilder stringBuilder5 = new StringBuilder();
                                    stringBuilder5.append("Illegal number of bytes for TI tag data tagType=");
                                    stringBuilder5.append(int163);
                                    Log.d("DfltImageHeaderParser", stringBuilder5.toString());
                                }
                            }
                        } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Got byte count > 4, not orientation, continuing, formatCode=");
                            stringBuilder2.append(int164);
                            Log.d("DfltImageHeaderParser", stringBuilder2.toString());
                        }
                    } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                        Log.d("DfltImageHeaderParser", "Negative tiff component count");
                    }
                } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Got invalid format code = ");
                    stringBuilder2.append(int164);
                    Log.d("DfltImageHeaderParser", stringBuilder2.toString());
                }
            }
        }
        return -1;
    }
}
