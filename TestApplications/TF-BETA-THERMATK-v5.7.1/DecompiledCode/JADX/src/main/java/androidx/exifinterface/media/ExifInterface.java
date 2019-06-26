package androidx.exifinterface.media;

import android.content.res.AssetManager.AssetInputStream;
import android.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ExifInterface {
    static final Charset ASCII = Charset.forName("US-ASCII");
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
    public static final int[] BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
    static final byte[] EXIF_ASCII_PREFIX = new byte[]{(byte) 65, (byte) 83, (byte) 67, (byte) 73, (byte) 73, (byte) 0, (byte) 0, (byte) 0};
    private static final ExifTag[] EXIF_POINTER_TAGS = new ExifTag[]{new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1)};
    static final ExifTag[][] EXIF_TAGS;
    private static final List<Integer> FLIPPED_ROTATION_ORDER;
    static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    private static final ExifTag[] IFD_EXIF_TAGS = new ExifTag[]{new ExifTag("ExposureTime", 33434, 5), new ExifTag("FNumber", 33437, 5), new ExifTag("ExposureProgram", 34850, 3), new ExifTag("SpectralSensitivity", 34852, 2), new ExifTag("PhotographicSensitivity", 34855, 3), new ExifTag("OECF", 34856, 7), new ExifTag("ExifVersion", 36864, 2), new ExifTag("DateTimeOriginal", 36867, 2), new ExifTag("DateTimeDigitized", 36868, 2), new ExifTag("ComponentsConfiguration", 37121, 7), new ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifTag("ShutterSpeedValue", 37377, 10), new ExifTag("ApertureValue", 37378, 5), new ExifTag("BrightnessValue", 37379, 10), new ExifTag("ExposureBiasValue", 37380, 10), new ExifTag("MaxApertureValue", 37381, 5), new ExifTag("SubjectDistance", 37382, 5), new ExifTag("MeteringMode", 37383, 3), new ExifTag("LightSource", 37384, 3), new ExifTag("Flash", 37385, 3), new ExifTag("FocalLength", 37386, 5), new ExifTag("SubjectArea", 37396, 3), new ExifTag("MakerNote", 37500, 7), new ExifTag("UserComment", 37510, 7), new ExifTag("SubSecTime", 37520, 2), new ExifTag("SubSecTimeOriginal", 37521, 2), new ExifTag("SubSecTimeDigitized", 37522, 2), new ExifTag("FlashpixVersion", 40960, 7), new ExifTag("ColorSpace", 40961, 3), new ExifTag("PixelXDimension", 40962, 3, 4), new ExifTag("PixelYDimension", 40963, 3, 4), new ExifTag("RelatedSoundFile", 40964, 2), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("FlashEnergy", 41483, 5), new ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifTag("FocalPlaneXResolution", 41486, 5), new ExifTag("FocalPlaneYResolution", 41487, 5), new ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifTag("SubjectLocation", 41492, 3), new ExifTag("ExposureIndex", 41493, 5), new ExifTag("SensingMethod", 41495, 3), new ExifTag("FileSource", 41728, 7), new ExifTag("SceneType", 41729, 7), new ExifTag("CFAPattern", 41730, 7), new ExifTag("CustomRendered", 41985, 3), new ExifTag("ExposureMode", 41986, 3), new ExifTag("WhiteBalance", 41987, 3), new ExifTag("DigitalZoomRatio", 41988, 5), new ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifTag("SceneCaptureType", 41990, 3), new ExifTag("GainControl", 41991, 3), new ExifTag("Contrast", 41992, 3), new ExifTag("Saturation", 41993, 3), new ExifTag("Sharpness", 41994, 3), new ExifTag("DeviceSettingDescription", 41995, 7), new ExifTag("SubjectDistanceRange", 41996, 3), new ExifTag("ImageUniqueID", 42016, 2), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    static final String[] IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    private static final ExifTag[] IFD_GPS_TAGS = new ExifTag[]{new ExifTag("GPSVersionID", 0, 1), new ExifTag("GPSLatitudeRef", 1, 2), new ExifTag("GPSLatitude", 2, 5), new ExifTag("GPSLongitudeRef", 3, 2), new ExifTag("GPSLongitude", 4, 5), new ExifTag("GPSAltitudeRef", 5, 1), new ExifTag("GPSAltitude", 6, 5), new ExifTag("GPSTimeStamp", 7, 5), new ExifTag("GPSSatellites", 8, 2), new ExifTag("GPSStatus", 9, 2), new ExifTag("GPSMeasureMode", 10, 2), new ExifTag("GPSDOP", 11, 5), new ExifTag("GPSSpeedRef", 12, 2), new ExifTag("GPSSpeed", 13, 5), new ExifTag("GPSTrackRef", 14, 2), new ExifTag("GPSTrack", 15, 5), new ExifTag("GPSImgDirectionRef", 16, 2), new ExifTag("GPSImgDirection", 17, 5), new ExifTag("GPSMapDatum", 18, 2), new ExifTag("GPSDestLatitudeRef", 19, 2), new ExifTag("GPSDestLatitude", 20, 5), new ExifTag("GPSDestLongitudeRef", 21, 2), new ExifTag("GPSDestLongitude", 22, 5), new ExifTag("GPSDestBearingRef", 23, 2), new ExifTag("GPSDestBearing", 24, 5), new ExifTag("GPSDestDistanceRef", 25, 2), new ExifTag("GPSDestDistance", 26, 5), new ExifTag("GPSProcessingMethod", 27, 7), new ExifTag("GPSAreaInformation", 28, 7), new ExifTag("GPSDateStamp", 29, 2), new ExifTag("GPSDifferential", 30, 3)};
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = new ExifTag[]{new ExifTag("InteroperabilityIndex", 1, 2)};
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", NalUnitUtil.EXTENDED_SAR, 4), new ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
    private static final ExifTag[] IFD_TIFF_TAGS = new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", NalUnitUtil.EXTENDED_SAR, 4), new ExifTag("ImageWidth", 256, 3, 4), new ExifTag("ImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("SensorTopBorder", 4, 4), new ExifTag("SensorLeftBorder", 5, 4), new ExifTag("SensorBottomBorder", 6, 4), new ExifTag("SensorRightBorder", 7, 4), new ExifTag("ISO", 23, 3), new ExifTag("JpgFromRaw", 46, 7)};
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag("JPEGInterchangeFormat", 513, 4);
    static final byte[] JPEG_SIGNATURE = new byte[]{(byte) -1, (byte) -40, (byte) -1};
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS = new ExifTag[]{new ExifTag("PreviewImageStart", 257, 4), new ExifTag("PreviewImageLength", 258, 4)};
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS = new ExifTag[]{new ExifTag("AspectFrame", 4371, 3)};
    private static final byte[] ORF_MAKER_NOTE_HEADER_1 = new byte[]{(byte) 79, (byte) 76, (byte) 89, (byte) 77, (byte) 80, (byte) 0};
    private static final byte[] ORF_MAKER_NOTE_HEADER_2 = new byte[]{(byte) 79, (byte) 76, (byte) 89, (byte) 77, (byte) 80, (byte) 85, (byte) 83, (byte) 0, (byte) 73, (byte) 73};
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS = new ExifTag[]{new ExifTag("ThumbnailImage", 256, 7), new ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifTag("ImageProcessingIFDPointer", 8256, 4)};
    private static final ExifTag[] PEF_TAGS = new ExifTag[]{new ExifTag("ColorSpace", 55, 3)};
    private static final List<Integer> ROTATION_ORDER;
    private static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
    private static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap();
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    private static final HashSet<String> sTagSetForCompatibility = new HashSet(Arrays.asList(new String[]{"FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"}));
    private final AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes;
    private Set<Integer> mAttributesOffsets;
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private int mExifOffset;
    private final String mFilename;
    private boolean mHasThumbnail;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        private ByteOrder mByteOrder;
        private DataInputStream mDataInputStream;
        final int mLength;
        int mPosition;

        public ByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mDataInputStream = new DataInputStream(inputStream);
            this.mLength = this.mDataInputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(this.mLength);
        }

        public ByteOrderedDataInputStream(byte[] bArr) throws IOException {
            this(new ByteArrayInputStream(bArr));
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void seek(long j) throws IOException {
            int i = this.mPosition;
            if (((long) i) > j) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                j -= (long) i;
            }
            int i2 = (int) j;
            if (skipBytes(i2) != i2) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        public int peek() {
            return this.mPosition;
        }

        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = this.mDataInputStream.read(bArr, i, i2);
            this.mPosition += read;
            return read;
        }

        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public String readLine() throws IOException {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }

        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            this.mPosition += i2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, i, i2) != i2) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public void readFully(byte[] bArr) throws IOException {
            this.mPosition += bArr.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, 0, bArr.length) != bArr.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public byte readByte() throws IOException {
            this.mPosition++;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                if (read >= 0) {
                    return (byte) read;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public short readShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (short) ((read2 << 8) + read);
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (short) ((read << 8) + read2);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public int readInt() throws IOException {
            this.mPosition += 4;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                if ((((read | read2) | read3) | read4) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((read4 << 24) + (read3 << 16)) + (read2 << 8)) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (((read << 24) + (read2 << 16)) + (read3 << 8)) + read4;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public int skipBytes(int i) throws IOException {
            i = Math.min(i, this.mLength - this.mPosition);
            int i2 = 0;
            while (i2 < i) {
                i2 += this.mDataInputStream.skipBytes(i - i2);
            }
            this.mPosition += i2;
            return i2;
        }

        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 8) + read2;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        public long readLong() throws IOException {
            this.mPosition += 8;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                int read5 = this.mDataInputStream.read();
                int read6 = this.mDataInputStream.read();
                int read7 = this.mDataInputStream.read();
                int read8 = this.mDataInputStream.read();
                if ((((((((read | read2) | read3) | read4) | read5) | read6) | read7) | read8) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((((((((long) read8) << 56) + (((long) read7) << 48)) + (((long) read6) << 40)) + (((long) read5) << 32)) + (((long) read4) << 24)) + (((long) read3) << 16)) + (((long) read2) << 8)) + ((long) read);
                    }
                    int i = read2;
                    if (byteOrder == BIG_ENDIAN) {
                        return (((((((((long) read) << 56) + (((long) i) << 48)) + (((long) read3) << 40)) + (((long) read4) << 32)) + (((long) read5) << 24)) + (((long) read6) << 16)) + (((long) read7) << 8)) + ((long) read8);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }
    }

    private static class ExifAttribute {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:183:0x01b4 in {11, 14, 16, 17, 23, 26, 28, 29, 35, 38, 40, 41, 47, 50, 52, 53, 59, 62, 64, 65, 71, 74, 76, 77, 83, 86, 88, 89, 95, 98, 100, 101, 110, 111, 113, 119, 122, 123, 124, 128, 130, 131, 142, 144, 145, 150, 152, 153, 156, 158, 159, 161, 163, 165, 171, 173, 174, 175, 179, 181, 182} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        java.lang.Object getValue(java.nio.ByteOrder r11) {
            /*
            r10 = this;
            r0 = "IOException occurred while closing InputStream";
            r1 = "ExifInterface";
            r2 = 0;
            r3 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream;	 Catch:{ IOException -> 0x0196, all -> 0x0193 }
            r4 = r10.bytes;	 Catch:{ IOException -> 0x0196, all -> 0x0193 }
            r3.<init>(r4);	 Catch:{ IOException -> 0x0196, all -> 0x0193 }
            r3.setByteOrder(r11);	 Catch:{ IOException -> 0x0191 }
            r11 = r10.format;	 Catch:{ IOException -> 0x0191 }
            r4 = 1;	 Catch:{ IOException -> 0x0191 }
            r5 = 0;	 Catch:{ IOException -> 0x0191 }
            switch(r11) {
                case 1: goto L_0x014c;
                case 2: goto L_0x00fd;
                case 3: goto L_0x00e3;
                case 4: goto L_0x00c9;
                case 5: goto L_0x00a6;
                case 6: goto L_0x014c;
                case 7: goto L_0x00fd;
                case 8: goto L_0x008c;
                case 9: goto L_0x0072;
                case 10: goto L_0x004d;
                case 11: goto L_0x0032;
                case 12: goto L_0x0018;
                default: goto L_0x0016;
            };	 Catch:{ IOException -> 0x0191 }
            goto L_0x0188;	 Catch:{ IOException -> 0x0191 }
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new double[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x0029;	 Catch:{ IOException -> 0x0191 }
            r6 = r3.readDouble();	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r6;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x001c;
            r3.close();	 Catch:{ IOException -> 0x002d }
            goto L_0x0031;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new double[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x0044;	 Catch:{ IOException -> 0x0191 }
            r4 = r3.readFloat();	 Catch:{ IOException -> 0x0191 }
            r6 = (double) r4;	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r6;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x0036;
            r3.close();	 Catch:{ IOException -> 0x0048 }
            goto L_0x004c;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new androidx.exifinterface.media.ExifInterface.Rational[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x0069;	 Catch:{ IOException -> 0x0191 }
            r4 = r3.readInt();	 Catch:{ IOException -> 0x0191 }
            r6 = (long) r4;	 Catch:{ IOException -> 0x0191 }
            r4 = r3.readInt();	 Catch:{ IOException -> 0x0191 }
            r8 = (long) r4;	 Catch:{ IOException -> 0x0191 }
            r4 = new androidx.exifinterface.media.ExifInterface$Rational;	 Catch:{ IOException -> 0x0191 }
            r4.<init>(r6, r8);	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r4;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x0051;
            r3.close();	 Catch:{ IOException -> 0x006d }
            goto L_0x0071;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new int[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x0083;	 Catch:{ IOException -> 0x0191 }
            r4 = r3.readInt();	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r4;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x0076;
            r3.close();	 Catch:{ IOException -> 0x0087 }
            goto L_0x008b;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new int[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x009d;	 Catch:{ IOException -> 0x0191 }
            r4 = r3.readShort();	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r4;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x0090;
            r3.close();	 Catch:{ IOException -> 0x00a1 }
            goto L_0x00a5;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new androidx.exifinterface.media.ExifInterface.Rational[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x00c0;	 Catch:{ IOException -> 0x0191 }
            r6 = r3.readUnsignedInt();	 Catch:{ IOException -> 0x0191 }
            r8 = r3.readUnsignedInt();	 Catch:{ IOException -> 0x0191 }
            r4 = new androidx.exifinterface.media.ExifInterface$Rational;	 Catch:{ IOException -> 0x0191 }
            r4.<init>(r6, r8);	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r4;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x00aa;
            r3.close();	 Catch:{ IOException -> 0x00c4 }
            goto L_0x00c8;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new long[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x00da;	 Catch:{ IOException -> 0x0191 }
            r6 = r3.readUnsignedInt();	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r6;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x00cd;
            r3.close();	 Catch:{ IOException -> 0x00de }
            goto L_0x00e2;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r11 = new int[r11];	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x00f4;	 Catch:{ IOException -> 0x0191 }
            r4 = r3.readUnsignedShort();	 Catch:{ IOException -> 0x0191 }
            r11[r5] = r4;	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;
            goto L_0x00e7;
            r3.close();	 Catch:{ IOException -> 0x00f8 }
            goto L_0x00fc;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            r6 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x0191 }
            r6 = r6.length;	 Catch:{ IOException -> 0x0191 }
            if (r11 < r6) goto L_0x011e;	 Catch:{ IOException -> 0x0191 }
            r11 = 0;	 Catch:{ IOException -> 0x0191 }
            r6 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x0191 }
            r6 = r6.length;	 Catch:{ IOException -> 0x0191 }
            if (r11 >= r6) goto L_0x0119;	 Catch:{ IOException -> 0x0191 }
            r6 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r6 = r6[r11];	 Catch:{ IOException -> 0x0191 }
            r7 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x0191 }
            r7 = r7[r11];	 Catch:{ IOException -> 0x0191 }
            if (r6 == r7) goto L_0x0116;	 Catch:{ IOException -> 0x0191 }
            r4 = 0;	 Catch:{ IOException -> 0x0191 }
            goto L_0x0119;	 Catch:{ IOException -> 0x0191 }
            r11 = r11 + 1;	 Catch:{ IOException -> 0x0191 }
            goto L_0x0105;	 Catch:{ IOException -> 0x0191 }
            if (r4 == 0) goto L_0x011e;	 Catch:{ IOException -> 0x0191 }
            r11 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x0191 }
            r5 = r11.length;	 Catch:{ IOException -> 0x0191 }
            r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0191 }
            r11.<init>();	 Catch:{ IOException -> 0x0191 }
            r4 = r10.numberOfComponents;	 Catch:{ IOException -> 0x0191 }
            if (r5 >= r4) goto L_0x013f;	 Catch:{ IOException -> 0x0191 }
            r4 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r4 = r4[r5];	 Catch:{ IOException -> 0x0191 }
            if (r4 != 0) goto L_0x012e;	 Catch:{ IOException -> 0x0191 }
            goto L_0x013f;	 Catch:{ IOException -> 0x0191 }
            r6 = 32;	 Catch:{ IOException -> 0x0191 }
            if (r4 < r6) goto L_0x0137;	 Catch:{ IOException -> 0x0191 }
            r4 = (char) r4;	 Catch:{ IOException -> 0x0191 }
            r11.append(r4);	 Catch:{ IOException -> 0x0191 }
            goto L_0x013c;	 Catch:{ IOException -> 0x0191 }
            r4 = 63;	 Catch:{ IOException -> 0x0191 }
            r11.append(r4);	 Catch:{ IOException -> 0x0191 }
            r5 = r5 + 1;	 Catch:{ IOException -> 0x0191 }
            goto L_0x0123;	 Catch:{ IOException -> 0x0191 }
            r11 = r11.toString();	 Catch:{ IOException -> 0x0191 }
            r3.close();	 Catch:{ IOException -> 0x0147 }
            goto L_0x014b;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r11 = r11.length;	 Catch:{ IOException -> 0x0191 }
            if (r11 != r4) goto L_0x0176;	 Catch:{ IOException -> 0x0191 }
            r11 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r11 = r11[r5];	 Catch:{ IOException -> 0x0191 }
            if (r11 < 0) goto L_0x0176;	 Catch:{ IOException -> 0x0191 }
            r11 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r11 = r11[r5];	 Catch:{ IOException -> 0x0191 }
            if (r11 > r4) goto L_0x0176;	 Catch:{ IOException -> 0x0191 }
            r11 = new java.lang.String;	 Catch:{ IOException -> 0x0191 }
            r4 = new char[r4];	 Catch:{ IOException -> 0x0191 }
            r6 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r6 = r6[r5];	 Catch:{ IOException -> 0x0191 }
            r6 = r6 + 48;	 Catch:{ IOException -> 0x0191 }
            r6 = (char) r6;	 Catch:{ IOException -> 0x0191 }
            r4[r5] = r6;	 Catch:{ IOException -> 0x0191 }
            r11.<init>(r4);	 Catch:{ IOException -> 0x0191 }
            r3.close();	 Catch:{ IOException -> 0x0171 }
            goto L_0x0175;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r11 = new java.lang.String;	 Catch:{ IOException -> 0x0191 }
            r4 = r10.bytes;	 Catch:{ IOException -> 0x0191 }
            r5 = androidx.exifinterface.media.ExifInterface.ASCII;	 Catch:{ IOException -> 0x0191 }
            r11.<init>(r4, r5);	 Catch:{ IOException -> 0x0191 }
            r3.close();	 Catch:{ IOException -> 0x0183 }
            goto L_0x0187;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            return r11;
            r3.close();	 Catch:{ IOException -> 0x018c }
            goto L_0x0190;
            r11 = move-exception;
            android.util.Log.e(r1, r0, r11);
            return r2;
            r11 = move-exception;
            goto L_0x0198;
            r11 = move-exception;
            r3 = r2;
            goto L_0x01a9;
            r11 = move-exception;
            r3 = r2;
            r4 = "IOException occurred during reading a value";	 Catch:{ all -> 0x01a8 }
            android.util.Log.w(r1, r4, r11);	 Catch:{ all -> 0x01a8 }
            if (r3 == 0) goto L_0x01a7;
            r3.close();	 Catch:{ IOException -> 0x01a3 }
            goto L_0x01a7;
            r11 = move-exception;
            android.util.Log.e(r1, r0, r11);
            return r2;
            r11 = move-exception;
            if (r3 == 0) goto L_0x01b3;
            r3.close();	 Catch:{ IOException -> 0x01af }
            goto L_0x01b3;
            r2 = move-exception;
            android.util.Log.e(r1, r0, r2);
            throw r11;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface$ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object");
        }

        ExifAttribute(int i, int i2, byte[] bArr) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytes = bArr;
        }

        public static ExifAttribute createUShort(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * iArr.length)]);
            wrap.order(byteOrder);
            for (int i : iArr) {
                wrap.putShort((short) i);
            }
            return new ExifAttribute(3, iArr.length, wrap.array());
        }

        public static ExifAttribute createUShort(int i, ByteOrder byteOrder) {
            return createUShort(new int[]{i}, byteOrder);
        }

        public static ExifAttribute createULong(long[] jArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * jArr.length)]);
            wrap.order(byteOrder);
            for (long j : jArr) {
                wrap.putInt((int) j);
            }
            return new ExifAttribute(4, jArr.length, wrap.array());
        }

        public static ExifAttribute createULong(long j, ByteOrder byteOrder) {
            return createULong(new long[]{j}, byteOrder);
        }

        public static ExifAttribute createString(String str) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(0);
            byte[] bytes = stringBuilder.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes.length, bytes);
        }

        public static ExifAttribute createURational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(5, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            return createURational(new Rational[]{rational}, byteOrder);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            stringBuilder.append(", data length:");
            stringBuilder.append(this.bytes.length);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                String str = "There are more than one component";
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (double) jArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return (double) iArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof double[]) {
                    double[] dArr = (double[]) value;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof Rational[]) {
                    Rational[] rationalArr = (Rational[]) value;
                    if (rationalArr.length == 1) {
                        return rationalArr[0].calculate();
                    }
                    throw new NumberFormatException(str);
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                String str = "There are more than one component";
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException(str);
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String str = ",";
            int i = 0;
            if (value instanceof long[]) {
                long[] jArr = (long[]) value;
                while (i < jArr.length) {
                    stringBuilder.append(jArr[i]);
                    i++;
                    if (i != jArr.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            } else if (value instanceof int[]) {
                int[] iArr = (int[]) value;
                while (i < iArr.length) {
                    stringBuilder.append(iArr[i]);
                    i++;
                    if (i != iArr.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            } else if (value instanceof double[]) {
                double[] dArr = (double[]) value;
                while (i < dArr.length) {
                    stringBuilder.append(dArr[i]);
                    i++;
                    if (i != dArr.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] rationalArr = (Rational[]) value;
                while (i < rationalArr.length) {
                    stringBuilder.append(rationalArr[i].numerator);
                    stringBuilder.append('/');
                    stringBuilder.append(rationalArr[i].denominator);
                    i++;
                    if (i != rationalArr.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            }
        }
    }

    static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        ExifTag(String str, int i, int i2) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = -1;
        }

        ExifTag(String str, int i, int i2, int i3) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = i3;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isFormatCompatible(int i) {
            int i2 = this.primaryFormat;
            if (!(i2 == 7 || i == 7 || i2 == i)) {
                int i3 = this.secondaryFormat;
                if (i3 != i) {
                    if ((i2 == 4 || i3 == 4) && i == 3) {
                        return true;
                    }
                    if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && i == 8) {
                        return true;
                    }
                    if ((this.primaryFormat == 12 || this.secondaryFormat == 12) && i == 11) {
                        return true;
                    }
                    return false;
                }
            }
            return true;
        }
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        Rational(long j, long j2) {
            if (j2 == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = j;
            this.denominator = j2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.numerator);
            stringBuilder.append("/");
            stringBuilder.append(this.denominator);
            return stringBuilder.toString();
        }

        public double calculate() {
            double d = (double) this.numerator;
            double d2 = (double) this.denominator;
            Double.isNaN(d);
            Double.isNaN(d2);
            return d / d2;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:72:0x016e in {11, 22, 25, 27, 32, 33, 35, 38, 43, 47, 51, 53, 55, 57, 59, 61, 63, 65, 67, 69, 71} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void getJpegAttributes(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r10, int r11, int r12) throws java.io.IOException {
        /*
        r9 = this;
        r0 = java.nio.ByteOrder.BIG_ENDIAN;
        r10.setByteOrder(r0);
        r0 = (long) r11;
        r10.seek(r0);
        r0 = r10.readByte();
        r1 = "Invalid marker: ";
        r2 = -1;
        if (r0 != r2) goto L_0x0153;
        r3 = 1;
        r11 = r11 + r3;
        r4 = r10.readByte();
        r5 = -40;
        if (r4 != r5) goto L_0x0138;
        r11 = r11 + r3;
        r0 = r10.readByte();
        if (r0 != r2) goto L_0x011b;
        r11 = r11 + r3;
        r0 = r10.readByte();
        r11 = r11 + r3;
        r1 = -39;
        if (r0 == r1) goto L_0x0115;
        r1 = -38;
        if (r0 != r1) goto L_0x0033;
        goto L_0x0115;
        r1 = r10.readUnsignedShort();
        r1 = r1 + -2;
        r11 = r11 + 2;
        r4 = "Invalid length";
        if (r1 < 0) goto L_0x010f;
        r5 = -31;
        r6 = 0;
        r7 = "Invalid exif";
        if (r0 == r5) goto L_0x00ba;
        r5 = -2;
        if (r0 == r5) goto L_0x0090;
        switch(r0) {
            case -64: goto L_0x0057;
            case -63: goto L_0x0057;
            case -62: goto L_0x0057;
            case -61: goto L_0x0057;
            default: goto L_0x004c;
        };
        switch(r0) {
            case -59: goto L_0x0057;
            case -58: goto L_0x0057;
            case -57: goto L_0x0057;
            default: goto L_0x004f;
        };
        switch(r0) {
            case -55: goto L_0x0057;
            case -54: goto L_0x0057;
            case -53: goto L_0x0057;
            default: goto L_0x0052;
        };
        switch(r0) {
            case -51: goto L_0x0057;
            case -50: goto L_0x0057;
            case -49: goto L_0x0057;
            default: goto L_0x0055;
        };
        goto L_0x00e4;
        r0 = r10.skipBytes(r3);
        if (r0 != r3) goto L_0x0088;
        r0 = r9.mAttributes;
        r0 = r0[r12];
        r5 = r10.readUnsignedShort();
        r5 = (long) r5;
        r7 = r9.mExifByteOrder;
        r5 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r5, r7);
        r6 = "ImageLength";
        r0.put(r6, r5);
        r0 = r9.mAttributes;
        r0 = r0[r12];
        r5 = r10.readUnsignedShort();
        r5 = (long) r5;
        r7 = r9.mExifByteOrder;
        r5 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r5, r7);
        r6 = "ImageWidth";
        r0.put(r6, r5);
        r1 = r1 + -5;
        goto L_0x00e4;
        r10 = new java.io.IOException;
        r11 = "Invalid SOFx";
        r10.<init>(r11);
        throw r10;
        r0 = new byte[r1];
        r5 = r10.read(r0);
        if (r5 != r1) goto L_0x00b4;
        r1 = "UserComment";
        r5 = r9.getAttribute(r1);
        if (r5 != 0) goto L_0x00b2;
        r5 = r9.mAttributes;
        r5 = r5[r3];
        r7 = new java.lang.String;
        r8 = ASCII;
        r7.<init>(r0, r8);
        r0 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createString(r7);
        r5.put(r1, r0);
        r1 = 0;
        goto L_0x00e4;
        r10 = new java.io.IOException;
        r10.<init>(r7);
        throw r10;
        r0 = 6;
        if (r1 >= r0) goto L_0x00be;
        goto L_0x00e4;
        r5 = new byte[r0];
        r8 = r10.read(r5);
        if (r8 != r0) goto L_0x0109;
        r11 = r11 + 6;
        r1 = r1 + -6;
        r0 = IDENTIFIER_EXIF_APP1;
        r0 = java.util.Arrays.equals(r5, r0);
        if (r0 != 0) goto L_0x00d3;
        goto L_0x00e4;
        if (r1 <= 0) goto L_0x0103;
        r9.mExifOffset = r11;
        r0 = new byte[r1];
        r5 = r10.read(r0);
        if (r5 != r1) goto L_0x00fd;
        r11 = r11 + r1;
        r9.readExifSegment(r0, r12);
        goto L_0x00b2;
        if (r1 < 0) goto L_0x00f7;
        r0 = r10.skipBytes(r1);
        if (r0 != r1) goto L_0x00ef;
        r11 = r11 + r1;
        goto L_0x001d;
        r10 = new java.io.IOException;
        r11 = "Invalid JPEG segment";
        r10.<init>(r11);
        throw r10;
        r10 = new java.io.IOException;
        r10.<init>(r4);
        throw r10;
        r10 = new java.io.IOException;
        r10.<init>(r7);
        throw r10;
        r10 = new java.io.IOException;
        r10.<init>(r7);
        throw r10;
        r10 = new java.io.IOException;
        r10.<init>(r7);
        throw r10;
        r10 = new java.io.IOException;
        r10.<init>(r4);
        throw r10;
        r11 = r9.mExifByteOrder;
        r10.setByteOrder(r11);
        return;
        r10 = new java.io.IOException;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Invalid marker:";
        r11.append(r12);
        r12 = r0 & 255;
        r12 = java.lang.Integer.toHexString(r12);
        r11.append(r12);
        r11 = r11.toString();
        r10.<init>(r11);
        throw r10;
        r10 = new java.io.IOException;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r11.append(r1);
        r12 = r0 & 255;
        r12 = java.lang.Integer.toHexString(r12);
        r11.append(r12);
        r11 = r11.toString();
        r10.<init>(r11);
        throw r10;
        r10 = new java.io.IOException;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r11.append(r1);
        r12 = r0 & 255;
        r12 = java.lang.Integer.toHexString(r12);
        r11.append(r12);
        r11 = r11.toString();
        r10.<init>(r11);
        throw r10;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getJpegAttributes(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream, int, int):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x0051 in {4, 7, 8, 9, 10, 11, 12, 14, 18, 20, 22} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void loadAttributes(java.io.InputStream r5) throws java.io.IOException {
        /*
        r4 = this;
        r0 = 0;
        r1 = 0;
        r2 = EXIF_TAGS;	 Catch:{ IOException -> 0x0047 }
        r2 = r2.length;	 Catch:{ IOException -> 0x0047 }
        if (r1 >= r2) goto L_0x0013;	 Catch:{ IOException -> 0x0047 }
        r2 = r4.mAttributes;	 Catch:{ IOException -> 0x0047 }
        r3 = new java.util.HashMap;	 Catch:{ IOException -> 0x0047 }
        r3.<init>();	 Catch:{ IOException -> 0x0047 }
        r2[r1] = r3;	 Catch:{ IOException -> 0x0047 }
        r1 = r1 + 1;	 Catch:{ IOException -> 0x0047 }
        goto L_0x0002;	 Catch:{ IOException -> 0x0047 }
        r1 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x0047 }
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ IOException -> 0x0047 }
        r1.<init>(r5, r2);	 Catch:{ IOException -> 0x0047 }
        r5 = r4.getMimeType(r1);	 Catch:{ IOException -> 0x0047 }
        r4.mMimeType = r5;	 Catch:{ IOException -> 0x0047 }
        r5 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream;	 Catch:{ IOException -> 0x0047 }
        r5.<init>(r1);	 Catch:{ IOException -> 0x0047 }
        r1 = r4.mMimeType;	 Catch:{ IOException -> 0x0047 }
        switch(r1) {
            case 0: goto L_0x003b;
            case 1: goto L_0x003b;
            case 2: goto L_0x003b;
            case 3: goto L_0x003b;
            case 4: goto L_0x0037;
            case 5: goto L_0x003b;
            case 6: goto L_0x003b;
            case 7: goto L_0x0033;
            case 8: goto L_0x003b;
            case 9: goto L_0x002f;
            case 10: goto L_0x002b;
            case 11: goto L_0x003b;
            default: goto L_0x002a;
        };	 Catch:{ IOException -> 0x0047 }
        goto L_0x003e;	 Catch:{ IOException -> 0x0047 }
        r4.getRw2Attributes(r5);	 Catch:{ IOException -> 0x0047 }
        goto L_0x003e;	 Catch:{ IOException -> 0x0047 }
        r4.getRafAttributes(r5);	 Catch:{ IOException -> 0x0047 }
        goto L_0x003e;	 Catch:{ IOException -> 0x0047 }
        r4.getOrfAttributes(r5);	 Catch:{ IOException -> 0x0047 }
        goto L_0x003e;	 Catch:{ IOException -> 0x0047 }
        r4.getJpegAttributes(r5, r0, r0);	 Catch:{ IOException -> 0x0047 }
        goto L_0x003e;	 Catch:{ IOException -> 0x0047 }
        r4.getRawAttributes(r5);	 Catch:{ IOException -> 0x0047 }
        r4.setThumbnailData(r5);	 Catch:{ IOException -> 0x0047 }
        r5 = 1;	 Catch:{ IOException -> 0x0047 }
        r4.mIsSupportedFile = r5;	 Catch:{ IOException -> 0x0047 }
        goto L_0x0049;
        r5 = move-exception;
        goto L_0x004d;
    L_0x0047:
        r4.mIsSupportedFile = r0;	 Catch:{ all -> 0x0045 }
        r4.addDefaultValuesForCompatibility();
        return;
        r4.addDefaultValuesForCompatibility();
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.loadAttributes(java.io.InputStream):void");
    }

    static {
        Integer[] numArr = new Integer[4];
        Integer valueOf = Integer.valueOf(1);
        numArr[0] = valueOf;
        numArr[1] = Integer.valueOf(6);
        Integer valueOf2 = Integer.valueOf(3);
        Integer valueOf3 = Integer.valueOf(2);
        numArr[2] = valueOf2;
        Integer valueOf4 = Integer.valueOf(8);
        numArr[3] = valueOf4;
        ROTATION_ORDER = Arrays.asList(numArr);
        numArr = new Integer[4];
        Integer valueOf5 = Integer.valueOf(7);
        numArr[1] = valueOf5;
        numArr[2] = Integer.valueOf(4);
        Integer valueOf6 = Integer.valueOf(5);
        numArr[3] = valueOf6;
        FLIPPED_ROTATION_ORDER = Arrays.asList(numArr);
        r0 = new ExifTag[10][];
        ExifTag[] exifTagArr = IFD_TIFF_TAGS;
        r0[0] = exifTagArr;
        r0[1] = IFD_EXIF_TAGS;
        r0[2] = IFD_GPS_TAGS;
        r0[3] = IFD_INTEROPERABILITY_TAGS;
        r0[4] = IFD_THUMBNAIL_TAGS;
        r0[5] = exifTagArr;
        r0[6] = ORF_MAKER_NOTE_TAGS;
        r0[7] = ORF_CAMERA_SETTINGS_TAGS;
        r0[8] = ORF_IMAGE_PROCESSING_TAGS;
        r0[9] = PEF_TAGS;
        EXIF_TAGS = r0;
        r0 = EXIF_TAGS;
        sExifTagMapsForReading = new HashMap[r0.length];
        sExifTagMapsForWriting = new HashMap[r0.length];
        sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            sExifTagMapsForReading[i] = new HashMap();
            sExifTagMapsForWriting[i] = new HashMap();
            for (ExifTag exifTag : EXIF_TAGS[i]) {
                sExifTagMapsForReading[i].put(Integer.valueOf(exifTag.number), exifTag);
                sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
            }
        }
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[0].number), valueOf6);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[1].number), valueOf);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[2].number), valueOf3);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[3].number), valueOf2);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[4].number), valueOf5);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[5].number), valueOf4);
    }

    public ExifInterface(String str) throws IOException {
        Throwable th;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (str != null) {
            Closeable closeable = null;
            this.mAssetInputStream = null;
            this.mFilename = str;
            try {
                FileInputStream fileInputStream = new FileInputStream(str);
                try {
                    loadAttributes(fileInputStream);
                    closeQuietly(fileInputStream);
                    return;
                } catch (Throwable th2) {
                    th = th2;
                    closeable = fileInputStream;
                    closeQuietly(closeable);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                closeQuietly(closeable);
                throw th;
            }
        }
        throw new IllegalArgumentException("filename cannot be null");
    }

    public ExifInterface(InputStream inputStream) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (inputStream != null) {
            this.mFilename = null;
            if (inputStream instanceof AssetInputStream) {
                this.mAssetInputStream = (AssetInputStream) inputStream;
            } else {
                this.mAssetInputStream = null;
            }
            loadAttributes(inputStream);
            return;
        }
        throw new IllegalArgumentException("inputStream cannot be null");
    }

    private ExifAttribute getExifAttribute(String str) {
        Object str2;
        if ("ISOSpeedRatings".equals(str2)) {
            str2 = "PhotographicSensitivity";
        }
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(str2);
            if (exifAttribute != null) {
                return exifAttribute;
            }
        }
        return null;
    }

    public String getAttribute(String str) {
        ExifAttribute exifAttribute = getExifAttribute(str);
        if (exifAttribute != null) {
            if (!sTagSetForCompatibility.contains(str)) {
                return exifAttribute.getStringValue(this.mExifByteOrder);
            }
            if (str.equals("GPSTimeStamp")) {
                int i = exifAttribute.format;
                String str2 = "ExifInterface";
                if (i == 5 || i == 10) {
                    Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
                    if (rationalArr == null || rationalArr.length != 3) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid GPS Timestamp array. array=");
                        stringBuilder.append(Arrays.toString(rationalArr));
                        Log.w(str2, stringBuilder.toString());
                        return null;
                    }
                    return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) rationalArr[0].numerator) / ((float) rationalArr[0].denominator))), Integer.valueOf((int) (((float) rationalArr[1].numerator) / ((float) rationalArr[1].denominator))), Integer.valueOf((int) (((float) rationalArr[2].numerator) / ((float) rationalArr[2].denominator)))});
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("GPS Timestamp format is not rational. format=");
                stringBuilder2.append(exifAttribute.format);
                Log.w(str2, stringBuilder2.toString());
                return null;
            }
            try {
                return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    public int getAttributeInt(String str, int i) {
        ExifAttribute exifAttribute = getExifAttribute(str);
        if (exifAttribute == null) {
            return i;
        }
        try {
            return exifAttribute.getIntValue(this.mExifByteOrder);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    private int getMimeType(BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (isJpegFormat(bArr)) {
            return 4;
        }
        if (isRafFormat(bArr)) {
            return 9;
        }
        if (isOrfFormat(bArr)) {
            return 7;
        }
        return isRw2Format(bArr) ? 10 : 0;
    }

    private static boolean isJpegFormat(byte[] bArr) throws IOException {
        int i = 0;
        while (true) {
            byte[] bArr2 = JPEG_SIGNATURE;
            if (i >= bArr2.length) {
                return true;
            }
            if (bArr[i] != bArr2[i]) {
                return false;
            }
            i++;
        }
    }

    private boolean isRafFormat(byte[] bArr) throws IOException {
        byte[] bytes = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        for (int i = 0; i < bytes.length; i++) {
            if (bArr[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isOrfFormat(byte[] bArr) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return readShort == (short) 20306 || readShort == (short) 21330;
    }

    private boolean isRw2Format(byte[] bArr) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return readShort == (short) 85;
    }

    private void getRawAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        parseTiffHeaders(byteOrderedDataInputStream, byteOrderedDataInputStream.available());
        readImageFileDirectory(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 5);
        updateImageSizeValues(byteOrderedDataInputStream, 4);
        validateImages(byteOrderedDataInputStream);
        if (this.mMimeType == 8) {
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get("MakerNote");
            if (exifAttribute != null) {
                ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
                byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
                byteOrderedDataInputStream2.seek(6);
                readImageFileDirectory(byteOrderedDataInputStream2, 9);
                String str = "ColorSpace";
                exifAttribute = (ExifAttribute) this.mAttributes[9].get(str);
                if (exifAttribute != null) {
                    this.mAttributes[1].put(str, exifAttribute);
                }
            }
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        byteOrderedDataInputStream.skipBytes(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byteOrderedDataInputStream.read(bArr);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(bArr2);
        int i = ByteBuffer.wrap(bArr).getInt();
        int i2 = ByteBuffer.wrap(bArr2).getInt();
        getJpegAttributes(byteOrderedDataInputStream, i, 5);
        byteOrderedDataInputStream.seek((long) i2);
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        i = byteOrderedDataInputStream.readInt();
        for (int i3 = 0; i3 < i; i3++) {
            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (readUnsignedShort == TAG_RAF_IMAGE_SIZE.number) {
                i = byteOrderedDataInputStream.readShort();
                int readShort = byteOrderedDataInputStream.readShort();
                ExifAttribute createUShort = ExifAttribute.createUShort(i, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(readShort, this.mExifByteOrder);
                this.mAttributes[0].put("ImageLength", createUShort);
                this.mAttributes[0].put("ImageWidth", createUShort2);
                return;
            }
            byteOrderedDataInputStream.skipBytes(readUnsignedShort2);
        }
    }

    private void getOrfAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        getRawAttributes(byteOrderedDataInputStream);
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get("MakerNote");
        if (exifAttribute != null) {
            ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
            byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
            byte[] bArr = new byte[ORF_MAKER_NOTE_HEADER_1.length];
            byteOrderedDataInputStream2.readFully(bArr);
            byteOrderedDataInputStream2.seek(0);
            byte[] bArr2 = new byte[ORF_MAKER_NOTE_HEADER_2.length];
            byteOrderedDataInputStream2.readFully(bArr2);
            if (Arrays.equals(bArr, ORF_MAKER_NOTE_HEADER_1)) {
                byteOrderedDataInputStream2.seek(8);
            } else if (Arrays.equals(bArr2, ORF_MAKER_NOTE_HEADER_2)) {
                byteOrderedDataInputStream2.seek(12);
            }
            readImageFileDirectory(byteOrderedDataInputStream2, 6);
            exifAttribute = (ExifAttribute) this.mAttributes[7].get("PreviewImageStart");
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[7].get("PreviewImageLength");
            if (!(exifAttribute == null || exifAttribute2 == null)) {
                this.mAttributes[5].put("JPEGInterchangeFormat", exifAttribute);
                this.mAttributes[5].put("JPEGInterchangeFormatLength", exifAttribute2);
            }
            exifAttribute = (ExifAttribute) this.mAttributes[8].get("AspectFrame");
            if (exifAttribute != null) {
                int[] iArr = (int[]) exifAttribute.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 4) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid aspect frame values. frame=");
                    stringBuilder.append(Arrays.toString(iArr));
                    Log.w("ExifInterface", stringBuilder.toString());
                } else if (iArr[2] > iArr[0] && iArr[3] > iArr[1]) {
                    int i = (iArr[2] - iArr[0]) + 1;
                    int i2 = (iArr[3] - iArr[1]) + 1;
                    if (i < i2) {
                        i += i2;
                        i2 = i - i2;
                        i -= i2;
                    }
                    exifAttribute = ExifAttribute.createUShort(i, this.mExifByteOrder);
                    ExifAttribute createUShort = ExifAttribute.createUShort(i2, this.mExifByteOrder);
                    this.mAttributes[0].put("ImageWidth", exifAttribute);
                    this.mAttributes[0].put("ImageLength", createUShort);
                }
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        getRawAttributes(byteOrderedDataInputStream);
        if (((ExifAttribute) this.mAttributes[0].get("JpgFromRaw")) != null) {
            getJpegAttributes(byteOrderedDataInputStream, this.mRw2JpgFromRawOffset, 5);
        }
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[0].get("ISO");
        String str = "PhotographicSensitivity";
        ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[1].get(str);
        if (exifAttribute != null && exifAttribute2 == null) {
            this.mAttributes[1].put(str, exifAttribute);
        }
    }

    private void readExifSegment(byte[] bArr, int i) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        parseTiffHeaders(byteOrderedDataInputStream, bArr.length);
        readImageFileDirectory(byteOrderedDataInputStream, i);
    }

    private void addDefaultValuesForCompatibility() {
        String attribute = getAttribute("DateTimeOriginal");
        if (attribute != null) {
            String str = "DateTime";
            if (getAttribute(str) == null) {
                this.mAttributes[0].put(str, ExifAttribute.createString(attribute));
            }
        }
        attribute = "ImageWidth";
        if (getAttribute(attribute) == null) {
            this.mAttributes[0].put(attribute, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        attribute = "ImageLength";
        if (getAttribute(attribute) == null) {
            this.mAttributes[0].put(attribute, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        attribute = "Orientation";
        if (getAttribute(attribute) == null) {
            this.mAttributes[0].put(attribute, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        attribute = "LightSource";
        if (getAttribute(attribute) == null) {
            this.mAttributes[1].put(attribute, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        short readShort = byteOrderedDataInputStream.readShort();
        if (readShort == (short) 18761) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (readShort == (short) 19789) {
            return ByteOrder.BIG_ENDIAN;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid byte order: ");
        stringBuilder.append(Integer.toHexString(readShort));
        throw new IOException(stringBuilder.toString());
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        int i2 = this.mMimeType;
        StringBuilder stringBuilder;
        if (i2 == 7 || i2 == 10 || readUnsignedShort == 42) {
            readUnsignedShort = byteOrderedDataInputStream.readInt();
            if (readUnsignedShort < 8 || readUnsignedShort >= i) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid first Ifd offset: ");
                stringBuilder.append(readUnsignedShort);
                throw new IOException(stringBuilder.toString());
            }
            readUnsignedShort -= 8;
            if (readUnsignedShort > 0 && byteOrderedDataInputStream.skipBytes(readUnsignedShort) != readUnsignedShort) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't jump to first Ifd: ");
                stringBuilder.append(readUnsignedShort);
                throw new IOException(stringBuilder.toString());
            }
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid start code: ");
        stringBuilder.append(Integer.toHexString(readUnsignedShort));
        throw new IOException(stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00f5  */
    /* JADX WARNING: Missing block: B:87:0x027d, code skipped:
            if ("Model".equals(r6.name) != false) goto L_0x027f;
     */
    /* JADX WARNING: Missing block: B:89:0x028b, code skipped:
            if (r5.getStringValue(r0.mExifByteOrder).contains("PENTAX") == false) goto L_0x028d;
     */
    /* JADX WARNING: Missing block: B:91:0x0293, code skipped:
            if (r8.equals(r6.name) == false) goto L_0x02a2;
     */
    /* JADX WARNING: Missing block: B:93:0x029e, code skipped:
            if (r5.getIntValue(r0.mExifByteOrder) != 65535) goto L_0x02a2;
     */
    /* JADX WARNING: Missing block: B:94:0x02a0, code skipped:
            r0.mMimeType = 8;
     */
    /* JADX WARNING: Missing block: B:96:0x02a9, code skipped:
            if (((long) r24.peek()) == r13) goto L_0x02ae;
     */
    /* JADX WARNING: Missing block: B:97:0x02ab, code skipped:
            r1.seek(r13);
     */
    private void readImageFileDirectory(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r24, int r25) throws java.io.IOException {
        /*
        r23 = this;
        r0 = r23;
        r1 = r24;
        r2 = r25;
        r3 = r0.mAttributesOffsets;
        r4 = r1.mPosition;
        r4 = java.lang.Integer.valueOf(r4);
        r3.add(r4);
        r3 = r1.mPosition;
        r3 = r3 + 2;
        r4 = r1.mLength;
        if (r3 <= r4) goto L_0x001a;
    L_0x0019:
        return;
    L_0x001a:
        r3 = r24.readShort();
        r4 = r1.mPosition;
        r5 = r3 * 12;
        r4 = r4 + r5;
        r5 = r1.mLength;
        if (r4 > r5) goto L_0x0326;
    L_0x0027:
        if (r3 > 0) goto L_0x002b;
    L_0x0029:
        goto L_0x0326;
    L_0x002b:
        r5 = 0;
    L_0x002c:
        r9 = "ExifInterface";
        if (r5 >= r3) goto L_0x02b7;
    L_0x0030:
        r10 = r24.readUnsignedShort();
        r11 = r24.readUnsignedShort();
        r12 = r24.readInt();
        r13 = r24.peek();
        r13 = (long) r13;
        r15 = 4;
        r13 = r13 + r15;
        r17 = sExifTagMapsForReading;
        r4 = r17[r2];
        r8 = java.lang.Integer.valueOf(r10);
        r4 = r4.get(r8);
        r4 = (androidx.exifinterface.media.ExifInterface.ExifTag) r4;
        r8 = 7;
        if (r4 != 0) goto L_0x006b;
    L_0x0055:
        r15 = new java.lang.StringBuilder;
        r15.<init>();
        r6 = "Skip the tag entry since tag number is not defined: ";
        r15.append(r6);
        r15.append(r10);
        r6 = r15.toString();
        android.util.Log.w(r9, r6);
        goto L_0x00e7;
    L_0x006b:
        if (r11 <= 0) goto L_0x00d3;
    L_0x006d:
        r6 = IFD_FORMAT_BYTES_PER_FORMAT;
        r6 = r6.length;
        if (r11 < r6) goto L_0x0073;
    L_0x0072:
        goto L_0x00d3;
    L_0x0073:
        r6 = r4.isFormatCompatible(r11);
        if (r6 != 0) goto L_0x009c;
    L_0x0079:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Skip the tag entry since data format (";
        r6.append(r7);
        r7 = IFD_FORMAT_NAMES;
        r7 = r7[r11];
        r6.append(r7);
        r7 = ") is unexpected for tag: ";
        r6.append(r7);
        r7 = r4.name;
        r6.append(r7);
        r6 = r6.toString();
        android.util.Log.w(r9, r6);
        goto L_0x00e7;
    L_0x009c:
        if (r11 != r8) goto L_0x00a0;
    L_0x009e:
        r11 = r4.primaryFormat;
    L_0x00a0:
        r6 = (long) r12;
        r15 = IFD_FORMAT_BYTES_PER_FORMAT;
        r15 = r15[r11];
        r16 = r9;
        r8 = (long) r15;
        r6 = r6 * r8;
        r8 = 0;
        r15 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r15 < 0) goto L_0x00bc;
    L_0x00b0:
        r8 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r15 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r15 <= 0) goto L_0x00b8;
    L_0x00b7:
        goto L_0x00bc;
    L_0x00b8:
        r8 = 1;
        r9 = r16;
        goto L_0x00ea;
    L_0x00bc:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Skip the tag entry since the number of components is invalid: ";
        r8.append(r9);
        r8.append(r12);
        r8 = r8.toString();
        r9 = r16;
        android.util.Log.w(r9, r8);
        goto L_0x00e9;
    L_0x00d3:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Skip the tag entry since data format is invalid: ";
        r6.append(r7);
        r6.append(r11);
        r6 = r6.toString();
        android.util.Log.w(r9, r6);
    L_0x00e7:
        r6 = 0;
    L_0x00e9:
        r8 = 0;
    L_0x00ea:
        if (r8 != 0) goto L_0x00f5;
    L_0x00ec:
        r1.seek(r13);
        r16 = r3;
        r18 = r5;
        goto L_0x02ae;
    L_0x00f5:
        r8 = "Compression";
        r15 = 4;
        r18 = (r6 > r15 ? 1 : (r6 == r15 ? 0 : -1));
        if (r18 <= 0) goto L_0x01a2;
    L_0x00fd:
        r15 = r24.readInt();
        r16 = r3;
        r3 = r0.mMimeType;
        r18 = r5;
        r5 = 7;
        if (r3 != r5) goto L_0x0165;
    L_0x010a:
        r3 = r4.name;
        r5 = "MakerNote";
        r3 = r5.equals(r3);
        if (r3 == 0) goto L_0x0117;
    L_0x0114:
        r0.mOrfMakerNoteOffset = r15;
        goto L_0x0160;
    L_0x0117:
        r3 = 6;
        if (r2 != r3) goto L_0x0160;
    L_0x011a:
        r5 = r4.name;
        r3 = "ThumbnailImage";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0160;
    L_0x0124:
        r0.mOrfThumbnailOffset = r15;
        r0.mOrfThumbnailLength = r12;
        r3 = r0.mExifByteOrder;
        r5 = 6;
        r3 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort(r5, r3);
        r5 = r0.mOrfThumbnailOffset;
        r20 = r11;
        r19 = r12;
        r11 = (long) r5;
        r5 = r0.mExifByteOrder;
        r5 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r11, r5);
        r11 = r0.mOrfThumbnailLength;
        r11 = (long) r11;
        r2 = r0.mExifByteOrder;
        r2 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r11, r2);
        r11 = r0.mAttributes;
        r12 = 4;
        r11 = r11[r12];
        r11.put(r8, r3);
        r3 = r0.mAttributes;
        r3 = r3[r12];
        r11 = "JPEGInterchangeFormat";
        r3.put(r11, r5);
        r3 = r0.mAttributes;
        r3 = r3[r12];
        r5 = "JPEGInterchangeFormatLength";
        r3.put(r5, r2);
        goto L_0x0179;
    L_0x0160:
        r20 = r11;
        r19 = r12;
        goto L_0x0179;
    L_0x0165:
        r20 = r11;
        r19 = r12;
        r2 = 10;
        if (r3 != r2) goto L_0x0179;
    L_0x016d:
        r2 = r4.name;
        r3 = "JpgFromRaw";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0179;
    L_0x0177:
        r0.mRw2JpgFromRawOffset = r15;
    L_0x0179:
        r2 = (long) r15;
        r11 = r2 + r6;
        r5 = r1.mLength;
        r21 = r4;
        r4 = (long) r5;
        r22 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1));
        if (r22 > 0) goto L_0x0189;
    L_0x0185:
        r1.seek(r2);
        goto L_0x01ac;
    L_0x0189:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Skip the tag entry since data offset is invalid: ";
        r2.append(r3);
        r2.append(r15);
        r2 = r2.toString();
        android.util.Log.w(r9, r2);
        r1.seek(r13);
        goto L_0x02ae;
    L_0x01a2:
        r16 = r3;
        r21 = r4;
        r18 = r5;
        r20 = r11;
        r19 = r12;
    L_0x01ac:
        r2 = sExifPointerTagMap;
        r3 = java.lang.Integer.valueOf(r10);
        r2 = r2.get(r3);
        r2 = (java.lang.Integer) r2;
        r3 = 8;
        r4 = 3;
        if (r2 == 0) goto L_0x0245;
    L_0x01bd:
        r5 = -1;
        r11 = r20;
        if (r11 == r4) goto L_0x01e2;
    L_0x01c3:
        r4 = 4;
        if (r11 == r4) goto L_0x01dd;
    L_0x01c6:
        if (r11 == r3) goto L_0x01d8;
    L_0x01c8:
        r3 = 9;
        if (r11 == r3) goto L_0x01d3;
    L_0x01cc:
        r3 = 13;
        if (r11 == r3) goto L_0x01d3;
    L_0x01d0:
        r3 = 0;
        goto L_0x01e8;
    L_0x01d3:
        r3 = r24.readInt();
        goto L_0x01e6;
    L_0x01d8:
        r3 = r24.readShort();
        goto L_0x01e6;
    L_0x01dd:
        r5 = r24.readUnsignedInt();
        goto L_0x01d0;
    L_0x01e2:
        r3 = r24.readUnsignedShort();
    L_0x01e6:
        r5 = (long) r3;
        goto L_0x01d0;
    L_0x01e8:
        r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
        if (r7 <= 0) goto L_0x022d;
    L_0x01ec:
        r3 = r1.mLength;
        r3 = (long) r3;
        r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
        if (r7 >= 0) goto L_0x022d;
    L_0x01f3:
        r3 = r0.mAttributesOffsets;
        r4 = (int) r5;
        r4 = java.lang.Integer.valueOf(r4);
        r3 = r3.contains(r4);
        if (r3 != 0) goto L_0x020b;
    L_0x0200:
        r1.seek(r5);
        r2 = r2.intValue();
        r0.readImageFileDirectory(r1, r2);
        goto L_0x0241;
    L_0x020b:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Skip jump into the IFD since it has already been read: IfdType ";
        r3.append(r4);
        r3.append(r2);
        r2 = " (at ";
        r3.append(r2);
        r3.append(r5);
        r2 = ")";
        r3.append(r2);
        r2 = r3.toString();
        android.util.Log.w(r9, r2);
        goto L_0x0241;
    L_0x022d:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Skip jump into the IFD since its offset is invalid: ";
        r2.append(r3);
        r2.append(r5);
        r2 = r2.toString();
        android.util.Log.w(r9, r2);
    L_0x0241:
        r1.seek(r13);
        goto L_0x02ae;
    L_0x0245:
        r11 = r20;
        r2 = (int) r6;
        r2 = new byte[r2];
        r1.readFully(r2);
        r5 = new androidx.exifinterface.media.ExifInterface$ExifAttribute;
        r6 = r19;
        r5.<init>(r11, r6, r2);
        r2 = r0.mAttributes;
        r2 = r2[r25];
        r6 = r21;
        r7 = r6.name;
        r2.put(r7, r5);
        r2 = r6.name;
        r7 = "DNGVersion";
        r2 = r7.equals(r2);
        if (r2 == 0) goto L_0x026b;
    L_0x0269:
        r0.mMimeType = r4;
    L_0x026b:
        r2 = r6.name;
        r4 = "Make";
        r2 = r4.equals(r2);
        if (r2 != 0) goto L_0x027f;
    L_0x0275:
        r2 = r6.name;
        r4 = "Model";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x028d;
    L_0x027f:
        r2 = r0.mExifByteOrder;
        r2 = r5.getStringValue(r2);
        r4 = "PENTAX";
        r2 = r2.contains(r4);
        if (r2 != 0) goto L_0x02a0;
    L_0x028d:
        r2 = r6.name;
        r2 = r8.equals(r2);
        if (r2 == 0) goto L_0x02a2;
    L_0x0295:
        r2 = r0.mExifByteOrder;
        r2 = r5.getIntValue(r2);
        r4 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r2 != r4) goto L_0x02a2;
    L_0x02a0:
        r0.mMimeType = r3;
    L_0x02a2:
        r2 = r24.peek();
        r2 = (long) r2;
        r4 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1));
        if (r4 == 0) goto L_0x02ae;
    L_0x02ab:
        r1.seek(r13);
    L_0x02ae:
        r5 = r18 + 1;
        r5 = (short) r5;
        r2 = r25;
        r3 = r16;
        goto L_0x002c;
    L_0x02b7:
        r2 = r24.peek();
        r3 = 4;
        r2 = r2 + r3;
        r3 = r1.mLength;
        if (r2 > r3) goto L_0x0326;
    L_0x02c1:
        r2 = r24.readInt();
        r3 = (long) r2;
        r5 = 0;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 <= 0) goto L_0x0312;
    L_0x02cc:
        r5 = r1.mLength;
        if (r2 >= r5) goto L_0x0312;
    L_0x02d0:
        r5 = r0.mAttributesOffsets;
        r6 = java.lang.Integer.valueOf(r2);
        r5 = r5.contains(r6);
        if (r5 != 0) goto L_0x02fd;
    L_0x02dc:
        r1.seek(r3);
        r2 = r0.mAttributes;
        r3 = 4;
        r2 = r2[r3];
        r2 = r2.isEmpty();
        if (r2 == 0) goto L_0x02ee;
    L_0x02ea:
        r0.readImageFileDirectory(r1, r3);
        goto L_0x0326;
    L_0x02ee:
        r2 = r0.mAttributes;
        r3 = 5;
        r2 = r2[r3];
        r2 = r2.isEmpty();
        if (r2 == 0) goto L_0x0326;
    L_0x02f9:
        r0.readImageFileDirectory(r1, r3);
        goto L_0x0326;
    L_0x02fd:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "Stop reading file since re-reading an IFD may cause an infinite loop: ";
        r1.append(r3);
        r1.append(r2);
        r1 = r1.toString();
        android.util.Log.w(r9, r1);
        goto L_0x0326;
    L_0x0312:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "Stop reading file since a wrong offset may cause an infinite loop: ";
        r1.append(r3);
        r1.append(r2);
        r1 = r1.toString();
        android.util.Log.w(r9, r1);
    L_0x0326:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.readImageFileDirectory(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream, int):void");
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get("ImageWidth");
        if (((ExifAttribute) this.mAttributes[i].get("ImageLength")) == null || exifAttribute == null) {
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[i].get("JPEGInterchangeFormat");
            if (exifAttribute2 != null) {
                getJpegAttributes(byteOrderedDataInputStream, exifAttribute2.getIntValue(this.mExifByteOrder), i);
            }
        }
    }

    private void setThumbnailData(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        HashMap hashMap = this.mAttributes[4];
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("Compression");
        if (exifAttribute != null) {
            this.mThumbnailCompression = exifAttribute.getIntValue(this.mExifByteOrder);
            int i = this.mThumbnailCompression;
            if (i != 1) {
                if (i == 6) {
                    handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
                    return;
                } else if (i != 7) {
                    return;
                }
            }
            if (isSupportedDataType(hashMap)) {
                handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
                return;
            }
            return;
        }
        this.mThumbnailCompression = 6;
        handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("JPEGInterchangeFormat");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("JPEGInterchangeFormatLength");
        if (exifAttribute != null && exifAttribute2 != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int min = Math.min(exifAttribute2.getIntValue(this.mExifByteOrder), byteOrderedDataInputStream.available() - intValue);
            int i = this.mMimeType;
            if (i == 4 || i == 9 || i == 10) {
                i = this.mExifOffset;
            } else {
                if (i == 7) {
                    i = this.mOrfMakerNoteOffset;
                }
                if (intValue > 0 && min > 0) {
                    this.mHasThumbnail = true;
                    this.mThumbnailOffset = intValue;
                    this.mThumbnailLength = min;
                    if (this.mFilename == null && this.mAssetInputStream == null) {
                        byte[] bArr = new byte[min];
                        byteOrderedDataInputStream.seek((long) intValue);
                        byteOrderedDataInputStream.readFully(bArr);
                        this.mThumbnailBytes = bArr;
                        return;
                    }
                    return;
                }
            }
            intValue += i;
            if (intValue > 0) {
            }
        }
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("StripOffsets");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("StripByteCounts");
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            long[] convertToLongArray = convertToLongArray(exifAttribute.getValue(this.mExifByteOrder));
            long[] convertToLongArray2 = convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
            String str = "ExifInterface";
            if (convertToLongArray == null) {
                Log.w(str, "stripOffsets should not be null.");
            } else if (convertToLongArray2 == null) {
                Log.w(str, "stripByteCounts should not be null.");
            } else {
                long j = 0;
                for (long j2 : convertToLongArray2) {
                    j += j2;
                }
                byte[] bArr = new byte[((int) j)];
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < convertToLongArray.length; i3++) {
                    int i4 = (int) convertToLongArray2[i3];
                    int i5 = ((int) convertToLongArray[i3]) - i;
                    if (i5 < 0) {
                        Log.d(str, "Invalid strip offset value");
                    }
                    byteOrderedDataInputStream.seek((long) i5);
                    i += i5;
                    byte[] bArr2 = new byte[i4];
                    byteOrderedDataInputStream.read(bArr2);
                    i += i4;
                    System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
                    i2 += bArr2.length;
                }
                this.mHasThumbnail = true;
                this.mThumbnailBytes = bArr;
                this.mThumbnailLength = bArr.length;
            }
        }
    }

    private boolean isSupportedDataType(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("BitsPerSample");
        if (exifAttribute != null) {
            int[] iArr = (int[]) exifAttribute.getValue(this.mExifByteOrder);
            if (Arrays.equals(BITS_PER_SAMPLE_RGB, iArr)) {
                return true;
            }
            if (this.mMimeType == 3) {
                ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("PhotometricInterpretation");
                if (exifAttribute2 != null) {
                    int intValue = exifAttribute2.getIntValue(this.mExifByteOrder);
                    if ((intValue == 1 && Arrays.equals(iArr, BITS_PER_SAMPLE_GREYSCALE_2)) || (intValue == 6 && Arrays.equals(iArr, BITS_PER_SAMPLE_RGB))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isThumbnail(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("ImageLength");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("ImageWidth");
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
            if (intValue <= 512 && intValue2 <= 512) {
                return true;
            }
        }
        return false;
    }

    private void validateImages(InputStream inputStream) throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get("PixelXDimension");
        ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[1].get("PixelYDimension");
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            this.mAttributes[0].put("ImageWidth", exifAttribute);
            this.mAttributes[0].put("ImageLength", exifAttribute2);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
    }

    private void updateImageSizeValues(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get("DefaultCropSize");
        ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[i].get("SensorTopBorder");
        ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[i].get("SensorLeftBorder");
        ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[i].get("SensorBottomBorder");
        ExifAttribute exifAttribute5 = (ExifAttribute) this.mAttributes[i].get("SensorRightBorder");
        String str = "ImageLength";
        String str2 = "ImageWidth";
        if (exifAttribute != null) {
            Object createURational;
            Object createURational2;
            String str3 = "Invalid crop size values. cropSize=";
            String str4 = "ExifInterface";
            StringBuilder stringBuilder;
            if (exifAttribute.format == 5) {
                Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
                if (rationalArr == null || rationalArr.length != 2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(Arrays.toString(rationalArr));
                    Log.w(str4, stringBuilder.toString());
                    return;
                }
                createURational = ExifAttribute.createURational(rationalArr[0], this.mExifByteOrder);
                createURational2 = ExifAttribute.createURational(rationalArr[1], this.mExifByteOrder);
            } else {
                int[] iArr = (int[]) exifAttribute.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(Arrays.toString(iArr));
                    Log.w(str4, stringBuilder.toString());
                    return;
                }
                createURational = ExifAttribute.createUShort(iArr[0], this.mExifByteOrder);
                createURational2 = ExifAttribute.createUShort(iArr[1], this.mExifByteOrder);
            }
            this.mAttributes[i].put(str2, createURational);
            this.mAttributes[i].put(str, createURational2);
        } else if (exifAttribute2 == null || exifAttribute3 == null || exifAttribute4 == null || exifAttribute5 == null) {
            retrieveJpegImageSize(byteOrderedDataInputStream, i);
        } else {
            int intValue = exifAttribute2.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute4.getIntValue(this.mExifByteOrder);
            int intValue3 = exifAttribute5.getIntValue(this.mExifByteOrder);
            int intValue4 = exifAttribute3.getIntValue(this.mExifByteOrder);
            if (intValue2 > intValue && intValue3 > intValue4) {
                intValue3 -= intValue4;
                ExifAttribute createUShort = ExifAttribute.createUShort(intValue2 - intValue, this.mExifByteOrder);
                exifAttribute = ExifAttribute.createUShort(intValue3, this.mExifByteOrder);
                this.mAttributes[i].put(str, createUShort);
                this.mAttributes[i].put(str2, exifAttribute);
            }
        }
    }

    private void swapBasedOnImageSize(int i, int i2) throws IOException {
        if (!this.mAttributes[i].isEmpty() && !this.mAttributes[i2].isEmpty()) {
            String str = "ImageLength";
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(str);
            String str2 = "ImageWidth";
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[i].get(str2);
            ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[i2].get(str);
            ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[i2].get(str2);
            if (exifAttribute != null && exifAttribute2 != null && exifAttribute3 != null && exifAttribute4 != null) {
                int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                if (intValue < intValue3 && intValue2 < intValue4) {
                    HashMap[] hashMapArr = this.mAttributes;
                    HashMap hashMap = hashMapArr[i];
                    hashMapArr[i] = hashMapArr[i2];
                    hashMapArr[i2] = hashMap;
                }
            }
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    private static long[] convertToLongArray(Object obj) {
        if (!(obj instanceof int[])) {
            return obj instanceof long[] ? (long[]) obj : null;
        } else {
            int[] iArr = (int[]) obj;
            long[] jArr = new long[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                jArr[i] = (long) iArr[i];
            }
            return jArr;
        }
    }
}
