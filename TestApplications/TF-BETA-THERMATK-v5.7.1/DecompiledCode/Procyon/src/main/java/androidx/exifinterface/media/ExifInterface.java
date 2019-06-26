// 
// Decompiled by Procyon v0.5.34
// 

package androidx.exifinterface.media;

import java.io.EOFException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataInput;
import java.nio.ByteBuffer;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;
import java.util.Collection;
import java.util.Arrays;
import java.nio.ByteOrder;
import java.util.Set;
import android.content.res.AssetManager$AssetInputStream;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.nio.charset.Charset;

public class ExifInterface
{
    static final Charset ASCII;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2;
    public static final int[] BITS_PER_SAMPLE_RGB;
    static final byte[] EXIF_ASCII_PREFIX;
    private static final ExifTag[] EXIF_POINTER_TAGS;
    static final ExifTag[][] EXIF_TAGS;
    private static final List<Integer> FLIPPED_ROTATION_ORDER;
    static final byte[] IDENTIFIER_EXIF_APP1;
    private static final ExifTag[] IFD_EXIF_TAGS;
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT;
    static final String[] IFD_FORMAT_NAMES;
    private static final ExifTag[] IFD_GPS_TAGS;
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS;
    private static final ExifTag[] IFD_TIFF_TAGS;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
    static final byte[] JPEG_SIGNATURE;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
    private static final byte[] ORF_MAKER_NOTE_HEADER_1;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS;
    private static final ExifTag[] PEF_TAGS;
    private static final List<Integer> ROTATION_ORDER;
    private static final ExifTag TAG_RAF_IMAGE_SIZE;
    private static final HashMap<Integer, Integer> sExifPointerTagMap;
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
    private static SimpleDateFormat sFormatter;
    private static final Pattern sGpsTimestampPattern;
    private static final Pattern sNonZeroTimePattern;
    private static final HashSet<String> sTagSetForCompatibility;
    private final AssetManager$AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes;
    private Set<Integer> mAttributesOffsets;
    private ByteOrder mExifByteOrder;
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
    
    static {
        final Integer value = 1;
        final Integer value2 = 3;
        final Integer value3 = 2;
        final Integer value4 = 8;
        ROTATION_ORDER = Arrays.asList(value, 6, value2, value4);
        final Integer value5 = 7;
        final Integer value6 = 5;
        FLIPPED_ROTATION_ORDER = Arrays.asList(value3, value5, 4, value6);
        BITS_PER_SAMPLE_RGB = new int[] { 8, 8, 8 };
        BITS_PER_SAMPLE_GREYSCALE_1 = new int[] { 4 };
        BITS_PER_SAMPLE_GREYSCALE_2 = new int[] { 8 };
        JPEG_SIGNATURE = new byte[] { -1, -40, -1 };
        ORF_MAKER_NOTE_HEADER_1 = new byte[] { 79, 76, 89, 77, 80, 0 };
        ORF_MAKER_NOTE_HEADER_2 = new byte[] { 79, 76, 89, 77, 80, 85, 83, 0, 73, 73 };
        IFD_FORMAT_NAMES = new String[] { "", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE" };
        IFD_FORMAT_BYTES_PER_FORMAT = new int[] { 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1 };
        EXIF_ASCII_PREFIX = new byte[] { 65, 83, 67, 73, 73, 0, 0, 0 };
        IFD_TIFF_TAGS = new ExifTag[] { new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ImageWidth", 256, 3, 4), new ExifTag("ImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("SensorTopBorder", 4, 4), new ExifTag("SensorLeftBorder", 5, 4), new ExifTag("SensorBottomBorder", 6, 4), new ExifTag("SensorRightBorder", 7, 4), new ExifTag("ISO", 23, 3), new ExifTag("JpgFromRaw", 46, 7) };
        IFD_EXIF_TAGS = new ExifTag[] { new ExifTag("ExposureTime", 33434, 5), new ExifTag("FNumber", 33437, 5), new ExifTag("ExposureProgram", 34850, 3), new ExifTag("SpectralSensitivity", 34852, 2), new ExifTag("PhotographicSensitivity", 34855, 3), new ExifTag("OECF", 34856, 7), new ExifTag("ExifVersion", 36864, 2), new ExifTag("DateTimeOriginal", 36867, 2), new ExifTag("DateTimeDigitized", 36868, 2), new ExifTag("ComponentsConfiguration", 37121, 7), new ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifTag("ShutterSpeedValue", 37377, 10), new ExifTag("ApertureValue", 37378, 5), new ExifTag("BrightnessValue", 37379, 10), new ExifTag("ExposureBiasValue", 37380, 10), new ExifTag("MaxApertureValue", 37381, 5), new ExifTag("SubjectDistance", 37382, 5), new ExifTag("MeteringMode", 37383, 3), new ExifTag("LightSource", 37384, 3), new ExifTag("Flash", 37385, 3), new ExifTag("FocalLength", 37386, 5), new ExifTag("SubjectArea", 37396, 3), new ExifTag("MakerNote", 37500, 7), new ExifTag("UserComment", 37510, 7), new ExifTag("SubSecTime", 37520, 2), new ExifTag("SubSecTimeOriginal", 37521, 2), new ExifTag("SubSecTimeDigitized", 37522, 2), new ExifTag("FlashpixVersion", 40960, 7), new ExifTag("ColorSpace", 40961, 3), new ExifTag("PixelXDimension", 40962, 3, 4), new ExifTag("PixelYDimension", 40963, 3, 4), new ExifTag("RelatedSoundFile", 40964, 2), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("FlashEnergy", 41483, 5), new ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifTag("FocalPlaneXResolution", 41486, 5), new ExifTag("FocalPlaneYResolution", 41487, 5), new ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifTag("SubjectLocation", 41492, 3), new ExifTag("ExposureIndex", 41493, 5), new ExifTag("SensingMethod", 41495, 3), new ExifTag("FileSource", 41728, 7), new ExifTag("SceneType", 41729, 7), new ExifTag("CFAPattern", 41730, 7), new ExifTag("CustomRendered", 41985, 3), new ExifTag("ExposureMode", 41986, 3), new ExifTag("WhiteBalance", 41987, 3), new ExifTag("DigitalZoomRatio", 41988, 5), new ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifTag("SceneCaptureType", 41990, 3), new ExifTag("GainControl", 41991, 3), new ExifTag("Contrast", 41992, 3), new ExifTag("Saturation", 41993, 3), new ExifTag("Sharpness", 41994, 3), new ExifTag("DeviceSettingDescription", 41995, 7), new ExifTag("SubjectDistanceRange", 41996, 3), new ExifTag("ImageUniqueID", 42016, 2), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4) };
        IFD_GPS_TAGS = new ExifTag[] { new ExifTag("GPSVersionID", 0, 1), new ExifTag("GPSLatitudeRef", 1, 2), new ExifTag("GPSLatitude", 2, 5), new ExifTag("GPSLongitudeRef", 3, 2), new ExifTag("GPSLongitude", 4, 5), new ExifTag("GPSAltitudeRef", 5, 1), new ExifTag("GPSAltitude", 6, 5), new ExifTag("GPSTimeStamp", 7, 5), new ExifTag("GPSSatellites", 8, 2), new ExifTag("GPSStatus", 9, 2), new ExifTag("GPSMeasureMode", 10, 2), new ExifTag("GPSDOP", 11, 5), new ExifTag("GPSSpeedRef", 12, 2), new ExifTag("GPSSpeed", 13, 5), new ExifTag("GPSTrackRef", 14, 2), new ExifTag("GPSTrack", 15, 5), new ExifTag("GPSImgDirectionRef", 16, 2), new ExifTag("GPSImgDirection", 17, 5), new ExifTag("GPSMapDatum", 18, 2), new ExifTag("GPSDestLatitudeRef", 19, 2), new ExifTag("GPSDestLatitude", 20, 5), new ExifTag("GPSDestLongitudeRef", 21, 2), new ExifTag("GPSDestLongitude", 22, 5), new ExifTag("GPSDestBearingRef", 23, 2), new ExifTag("GPSDestBearing", 24, 5), new ExifTag("GPSDestDistanceRef", 25, 2), new ExifTag("GPSDestDistance", 26, 5), new ExifTag("GPSProcessingMethod", 27, 7), new ExifTag("GPSAreaInformation", 28, 7), new ExifTag("GPSDateStamp", 29, 2), new ExifTag("GPSDifferential", 30, 3) };
        IFD_INTEROPERABILITY_TAGS = new ExifTag[] { new ExifTag("InteroperabilityIndex", 1, 2) };
        IFD_THUMBNAIL_TAGS = new ExifTag[] { new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4) };
        TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
        ORF_MAKER_NOTE_TAGS = new ExifTag[] { new ExifTag("ThumbnailImage", 256, 7), new ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifTag("ImageProcessingIFDPointer", 8256, 4) };
        ORF_CAMERA_SETTINGS_TAGS = new ExifTag[] { new ExifTag("PreviewImageStart", 257, 4), new ExifTag("PreviewImageLength", 258, 4) };
        ORF_IMAGE_PROCESSING_TAGS = new ExifTag[] { new ExifTag("AspectFrame", 4371, 3) };
        PEF_TAGS = new ExifTag[] { new ExifTag("ColorSpace", 55, 3) };
        final ExifTag[] ifd_TIFF_TAGS = ExifInterface.IFD_TIFF_TAGS;
        EXIF_TAGS = new ExifTag[][] { ifd_TIFF_TAGS, ExifInterface.IFD_EXIF_TAGS, ExifInterface.IFD_GPS_TAGS, ExifInterface.IFD_INTEROPERABILITY_TAGS, ExifInterface.IFD_THUMBNAIL_TAGS, ifd_TIFF_TAGS, ExifInterface.ORF_MAKER_NOTE_TAGS, ExifInterface.ORF_CAMERA_SETTINGS_TAGS, ExifInterface.ORF_IMAGE_PROCESSING_TAGS, ExifInterface.PEF_TAGS };
        EXIF_POINTER_TAGS = new ExifTag[] { new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1) };
        JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag("JPEGInterchangeFormat", 513, 4);
        JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
        final ExifTag[][] exif_TAGS = ExifInterface.EXIF_TAGS;
        sExifTagMapsForReading = new HashMap[exif_TAGS.length];
        sExifTagMapsForWriting = new HashMap[exif_TAGS.length];
        sTagSetForCompatibility = new HashSet<String>(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
        sExifPointerTagMap = new HashMap<Integer, Integer>();
        ASCII = Charset.forName("US-ASCII");
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ExifInterface.ASCII);
        (ExifInterface.sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss")).setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < ExifInterface.EXIF_TAGS.length; ++i) {
            ExifInterface.sExifTagMapsForReading[i] = new HashMap<Integer, ExifTag>();
            ExifInterface.sExifTagMapsForWriting[i] = new HashMap<String, ExifTag>();
            for (final ExifTag exifTag : ExifInterface.EXIF_TAGS[i]) {
                ExifInterface.sExifTagMapsForReading[i].put(exifTag.number, exifTag);
                ExifInterface.sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
            }
        }
        ExifInterface.sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[0].number, value6);
        ExifInterface.sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[1].number, value);
        ExifInterface.sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[2].number, value3);
        ExifInterface.sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[3].number, value2);
        ExifInterface.sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[4].number, value5);
        ExifInterface.sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[5].number, value4);
        sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
        sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    }
    
    public ExifInterface(final InputStream inputStream) throws IOException {
        final ExifTag[][] exif_TAGS = ExifInterface.EXIF_TAGS;
        this.mAttributes = (HashMap<String, ExifAttribute>[])new HashMap[exif_TAGS.length];
        this.mAttributesOffsets = new HashSet<Integer>(exif_TAGS.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (inputStream != null) {
            this.mFilename = null;
            if (inputStream instanceof AssetManager$AssetInputStream) {
                this.mAssetInputStream = (AssetManager$AssetInputStream)inputStream;
            }
            else {
                this.mAssetInputStream = null;
            }
            this.loadAttributes(inputStream);
            return;
        }
        throw new IllegalArgumentException("inputStream cannot be null");
    }
    
    public ExifInterface(final String s) throws IOException {
        final ExifTag[][] exif_TAGS = ExifInterface.EXIF_TAGS;
        this.mAttributes = (HashMap<String, ExifAttribute>[])new HashMap[exif_TAGS.length];
        this.mAttributesOffsets = new HashSet<Integer>(exif_TAGS.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (s == null) {
            throw new IllegalArgumentException("filename cannot be null");
        }
        Closeable closeable = null;
        this.mAssetInputStream = null;
        this.mFilename = s;
        try {
            final FileInputStream fileInputStream = new FileInputStream(s);
            try {
                this.loadAttributes(fileInputStream);
                closeQuietly(fileInputStream);
                return;
            }
            finally {
                closeable = fileInputStream;
            }
        }
        finally {}
        closeQuietly(closeable);
    }
    
    private void addDefaultValuesForCompatibility() {
        final String attribute = this.getAttribute("DateTimeOriginal");
        if (attribute != null && this.getAttribute("DateTime") == null) {
            this.mAttributes[0].put("DateTime", ExifAttribute.createString(attribute));
        }
        if (this.getAttribute("ImageWidth") == null) {
            this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute("ImageLength") == null) {
            this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute("Orientation") == null) {
            this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute("LightSource") == null) {
            this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
    }
    
    private static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            goto Label_0016;
        }
        try {
            closeable.close();
            goto Label_0016;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            goto Label_0016;
        }
    }
    
    private static long[] convertToLongArray(final Object o) {
        if (o instanceof int[]) {
            final int[] array = (int[])o;
            final long[] array2 = new long[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = array[i];
            }
            return array2;
        }
        if (o instanceof long[]) {
            return (long[])o;
        }
        return null;
    }
    
    private ExifAttribute getExifAttribute(final String anObject) {
        String key = anObject;
        if ("ISOSpeedRatings".equals(anObject)) {
            key = "PhotographicSensitivity";
        }
        for (int i = 0; i < ExifInterface.EXIF_TAGS.length; ++i) {
            final ExifAttribute exifAttribute = this.mAttributes[i].get(key);
            if (exifAttribute != null) {
                return exifAttribute;
            }
        }
        return null;
    }
    
    private void getJpegAttributes(final ByteOrderedDataInputStream byteOrderedDataInputStream, int n, final int n2) throws IOException {
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        byteOrderedDataInputStream.seek(n);
        final byte byte1 = byteOrderedDataInputStream.readByte();
        if (byte1 != -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid marker: ");
            sb.append(Integer.toHexString(byte1 & 0xFF));
            throw new IOException(sb.toString());
        }
        if (byteOrderedDataInputStream.readByte() != -40) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid marker: ");
            sb2.append(Integer.toHexString(byte1 & 0xFF));
            throw new IOException(sb2.toString());
        }
        n = n + 1 + 1;
        while (true) {
            final byte byte2 = byteOrderedDataInputStream.readByte();
            if (byte2 != -1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Invalid marker:");
                sb3.append(Integer.toHexString(byte2 & 0xFF));
                throw new IOException(sb3.toString());
            }
            final byte byte3 = byteOrderedDataInputStream.readByte();
            if (byte3 == -39 || byte3 == -38) {
                byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
                return;
            }
            final int n3 = byteOrderedDataInputStream.readUnsignedShort() - 2;
            int mExifOffset = n + 1 + 1 + 2;
            if (n3 < 0) {
                throw new IOException("Invalid length");
            }
            int n4 = 0;
            Label_0499: {
                if (byte3 != -31) {
                    if (byte3 != -2) {
                        Label_0238: {
                            switch (byte3) {
                                default: {
                                    switch (byte3) {
                                        default: {
                                            switch (byte3) {
                                                default: {
                                                    switch (byte3) {
                                                        default: {
                                                            n = n3;
                                                            n4 = mExifOffset;
                                                            break Label_0499;
                                                        }
                                                        case -51:
                                                        case -50:
                                                        case -49: {
                                                            break Label_0238;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case -55:
                                                case -54:
                                                case -53: {
                                                    break Label_0238;
                                                }
                                            }
                                            break;
                                        }
                                        case -59:
                                        case -58:
                                        case -57: {
                                            break Label_0238;
                                        }
                                    }
                                    break;
                                }
                                case -64:
                                case -63:
                                case -62:
                                case -61: {
                                    if (byteOrderedDataInputStream.skipBytes(1) == 1) {
                                        this.mAttributes[n2].put("ImageLength", ExifAttribute.createULong(byteOrderedDataInputStream.readUnsignedShort(), this.mExifByteOrder));
                                        this.mAttributes[n2].put("ImageWidth", ExifAttribute.createULong(byteOrderedDataInputStream.readUnsignedShort(), this.mExifByteOrder));
                                        n = n3 - 5;
                                        n4 = mExifOffset;
                                        break Label_0499;
                                    }
                                    throw new IOException("Invalid SOFx");
                                }
                            }
                        }
                    }
                    else {
                        final byte[] array = new byte[n3];
                        if (byteOrderedDataInputStream.read(array) != n3) {
                            throw new IOException("Invalid exif");
                        }
                        n4 = mExifOffset;
                        if (this.getAttribute("UserComment") == null) {
                            this.mAttributes[1].put("UserComment", ExifAttribute.createString(new String(array, ExifInterface.ASCII)));
                            n4 = mExifOffset;
                        }
                    }
                }
                else {
                    if (n3 < 6) {
                        n = n3;
                        n4 = mExifOffset;
                        break Label_0499;
                    }
                    final byte[] array2 = new byte[6];
                    if (byteOrderedDataInputStream.read(array2) != 6) {
                        throw new IOException("Invalid exif");
                    }
                    mExifOffset += 6;
                    n = n3 - 6;
                    if (!Arrays.equals(array2, ExifInterface.IDENTIFIER_EXIF_APP1)) {
                        n4 = mExifOffset;
                        break Label_0499;
                    }
                    if (n <= 0) {
                        throw new IOException("Invalid exif");
                    }
                    this.mExifOffset = mExifOffset;
                    final byte[] b = new byte[n];
                    if (byteOrderedDataInputStream.read(b) != n) {
                        throw new IOException("Invalid exif");
                    }
                    n4 = mExifOffset + n;
                    this.readExifSegment(b, n2);
                }
                n = 0;
            }
            if (n < 0) {
                throw new IOException("Invalid length");
            }
            if (byteOrderedDataInputStream.skipBytes(n) != n) {
                throw new IOException("Invalid JPEG segment");
            }
            n += n4;
        }
    }
    
    private int getMimeType(final BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(5000);
        final byte[] b = new byte[5000];
        bufferedInputStream.read(b);
        bufferedInputStream.reset();
        if (isJpegFormat(b)) {
            return 4;
        }
        if (this.isRafFormat(b)) {
            return 9;
        }
        if (this.isOrfFormat(b)) {
            return 7;
        }
        if (this.isRw2Format(b)) {
            return 10;
        }
        return 0;
    }
    
    private void getOrfAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        this.getRawAttributes(byteOrderedDataInputStream);
        final ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
        if (exifAttribute != null) {
            byteOrderedDataInputStream = new ByteOrderedDataInputStream(exifAttribute.bytes);
            byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
            final byte[] a = new byte[ExifInterface.ORF_MAKER_NOTE_HEADER_1.length];
            byteOrderedDataInputStream.readFully(a);
            byteOrderedDataInputStream.seek(0L);
            final byte[] a2 = new byte[ExifInterface.ORF_MAKER_NOTE_HEADER_2.length];
            byteOrderedDataInputStream.readFully(a2);
            if (Arrays.equals(a, ExifInterface.ORF_MAKER_NOTE_HEADER_1)) {
                byteOrderedDataInputStream.seek(8L);
            }
            else if (Arrays.equals(a2, ExifInterface.ORF_MAKER_NOTE_HEADER_2)) {
                byteOrderedDataInputStream.seek(12L);
            }
            this.readImageFileDirectory(byteOrderedDataInputStream, 6);
            final ExifAttribute value = this.mAttributes[7].get("PreviewImageStart");
            final ExifAttribute value2 = this.mAttributes[7].get("PreviewImageLength");
            if (value != null && value2 != null) {
                this.mAttributes[5].put("JPEGInterchangeFormat", value);
                this.mAttributes[5].put("JPEGInterchangeFormatLength", value2);
            }
            final ExifAttribute exifAttribute2 = this.mAttributes[8].get("AspectFrame");
            if (exifAttribute2 != null) {
                final int[] a3 = (int[])exifAttribute2.getValue(this.mExifByteOrder);
                if (a3 != null && a3.length == 4) {
                    if (a3[2] > a3[0] && a3[3] > a3[1]) {
                        final int n = a3[2] - a3[0] + 1;
                        final int n2 = a3[3] - a3[1] + 1;
                        int n3;
                        int n4;
                        if ((n3 = n) < (n4 = n2)) {
                            final int n5 = n + n2;
                            n4 = n5 - n2;
                            n3 = n5 - n4;
                        }
                        final ExifAttribute uShort = ExifAttribute.createUShort(n3, this.mExifByteOrder);
                        final ExifAttribute uShort2 = ExifAttribute.createUShort(n4, this.mExifByteOrder);
                        this.mAttributes[0].put("ImageWidth", uShort);
                        this.mAttributes[0].put("ImageLength", uShort2);
                    }
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid aspect frame values. frame=");
                    sb.append(Arrays.toString(a3));
                    Log.w("ExifInterface", sb.toString());
                }
            }
        }
    }
    
    private void getRafAttributes(final ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        byteOrderedDataInputStream.skipBytes(84);
        final byte[] array = new byte[4];
        final byte[] array2 = new byte[4];
        byteOrderedDataInputStream.read(array);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(array2);
        final int int1 = ByteBuffer.wrap(array).getInt();
        final int int2 = ByteBuffer.wrap(array2).getInt();
        this.getJpegAttributes(byteOrderedDataInputStream, int1, 5);
        byteOrderedDataInputStream.seek(int2);
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        for (int int3 = byteOrderedDataInputStream.readInt(), i = 0; i < int3; ++i) {
            final int unsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            final int unsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (unsignedShort == ExifInterface.TAG_RAF_IMAGE_SIZE.number) {
                final short short1 = byteOrderedDataInputStream.readShort();
                final short short2 = byteOrderedDataInputStream.readShort();
                final ExifAttribute uShort = ExifAttribute.createUShort(short1, this.mExifByteOrder);
                final ExifAttribute uShort2 = ExifAttribute.createUShort(short2, this.mExifByteOrder);
                this.mAttributes[0].put("ImageLength", uShort);
                this.mAttributes[0].put("ImageWidth", uShort2);
                return;
            }
            byteOrderedDataInputStream.skipBytes(unsignedShort2);
        }
    }
    
    private void getRawAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        this.parseTiffHeaders(byteOrderedDataInputStream, byteOrderedDataInputStream.available());
        this.readImageFileDirectory(byteOrderedDataInputStream, 0);
        this.updateImageSizeValues(byteOrderedDataInputStream, 0);
        this.updateImageSizeValues(byteOrderedDataInputStream, 5);
        this.updateImageSizeValues(byteOrderedDataInputStream, 4);
        this.validateImages(byteOrderedDataInputStream);
        if (this.mMimeType == 8) {
            final ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
            if (exifAttribute != null) {
                byteOrderedDataInputStream = new ByteOrderedDataInputStream(exifAttribute.bytes);
                byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
                byteOrderedDataInputStream.seek(6L);
                this.readImageFileDirectory(byteOrderedDataInputStream, 9);
                final ExifAttribute value = this.mAttributes[9].get("ColorSpace");
                if (value != null) {
                    this.mAttributes[1].put("ColorSpace", value);
                }
            }
        }
    }
    
    private void getRw2Attributes(final ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        this.getRawAttributes(byteOrderedDataInputStream);
        if (this.mAttributes[0].get("JpgFromRaw") != null) {
            this.getJpegAttributes(byteOrderedDataInputStream, this.mRw2JpgFromRawOffset, 5);
        }
        final ExifAttribute value = this.mAttributes[0].get("ISO");
        final ExifAttribute exifAttribute = this.mAttributes[1].get("PhotographicSensitivity");
        if (value != null && exifAttribute == null) {
            this.mAttributes[1].put("PhotographicSensitivity", value);
        }
    }
    
    private void handleThumbnailFromJfif(final ByteOrderedDataInputStream byteOrderedDataInputStream, final HashMap hashMap) throws IOException {
        final ExifAttribute exifAttribute = hashMap.get("JPEGInterchangeFormat");
        final ExifAttribute exifAttribute2 = hashMap.get("JPEGInterchangeFormatLength");
        if (exifAttribute != null && exifAttribute2 != null) {
            final int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            final int min = Math.min(exifAttribute2.getIntValue(this.mExifByteOrder), byteOrderedDataInputStream.available() - intValue);
            final int mMimeType = this.mMimeType;
            int mThumbnailOffset = 0;
            Label_0120: {
                int n;
                if (mMimeType != 4 && mMimeType != 9 && mMimeType != 10) {
                    mThumbnailOffset = intValue;
                    if (mMimeType != 7) {
                        break Label_0120;
                    }
                    n = this.mOrfMakerNoteOffset;
                }
                else {
                    n = this.mExifOffset;
                }
                mThumbnailOffset = intValue + n;
            }
            if (mThumbnailOffset > 0 && min > 0) {
                this.mHasThumbnail = true;
                this.mThumbnailOffset = mThumbnailOffset;
                this.mThumbnailLength = min;
                if (this.mFilename == null && this.mAssetInputStream == null) {
                    final byte[] mThumbnailBytes = new byte[min];
                    byteOrderedDataInputStream.seek(mThumbnailOffset);
                    byteOrderedDataInputStream.readFully(mThumbnailBytes);
                    this.mThumbnailBytes = mThumbnailBytes;
                }
            }
        }
    }
    
    private void handleThumbnailFromStrips(final ByteOrderedDataInputStream byteOrderedDataInputStream, final HashMap hashMap) throws IOException {
        final ExifAttribute exifAttribute = hashMap.get("StripOffsets");
        final ExifAttribute exifAttribute2 = hashMap.get("StripByteCounts");
        if (exifAttribute != null && exifAttribute2 != null) {
            final long[] convertToLongArray = convertToLongArray(exifAttribute.getValue(this.mExifByteOrder));
            final long[] convertToLongArray2 = convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
            if (convertToLongArray == null) {
                Log.w("ExifInterface", "stripOffsets should not be null.");
                return;
            }
            if (convertToLongArray2 == null) {
                Log.w("ExifInterface", "stripByteCounts should not be null.");
                return;
            }
            final int length = convertToLongArray2.length;
            long n = 0L;
            for (int i = 0; i < length; ++i) {
                n += convertToLongArray2[i];
            }
            final byte[] mThumbnailBytes = new byte[(int)n];
            int j = 0;
            int n2 = 0;
            int n3 = 0;
            while (j < convertToLongArray.length) {
                final int n4 = (int)convertToLongArray[j];
                final int n5 = (int)convertToLongArray2[j];
                final int n6 = n4 - n2;
                if (n6 < 0) {
                    Log.d("ExifInterface", "Invalid strip offset value");
                }
                byteOrderedDataInputStream.seek(n6);
                final byte[] b = new byte[n5];
                byteOrderedDataInputStream.read(b);
                n2 = n2 + n6 + n5;
                System.arraycopy(b, 0, mThumbnailBytes, n3, b.length);
                n3 += b.length;
                ++j;
            }
            this.mHasThumbnail = true;
            this.mThumbnailBytes = mThumbnailBytes;
            this.mThumbnailLength = mThumbnailBytes.length;
        }
    }
    
    private static boolean isJpegFormat(final byte[] array) throws IOException {
        int n = 0;
        while (true) {
            final byte[] jpeg_SIGNATURE = ExifInterface.JPEG_SIGNATURE;
            if (n >= jpeg_SIGNATURE.length) {
                return true;
            }
            if (array[n] != jpeg_SIGNATURE[n]) {
                return false;
            }
            ++n;
        }
    }
    
    private boolean isOrfFormat(final byte[] array) throws IOException {
        final ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(array);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder = this.readByteOrder(byteOrderedDataInputStream));
        final short short1 = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return short1 == 20306 || short1 == 21330;
    }
    
    private boolean isRafFormat(final byte[] array) throws IOException {
        final byte[] bytes = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        for (int i = 0; i < bytes.length; ++i) {
            if (array[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isRw2Format(final byte[] array) throws IOException {
        final ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(array);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder = this.readByteOrder(byteOrderedDataInputStream));
        final short short1 = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return short1 == 85;
    }
    
    private boolean isSupportedDataType(final HashMap hashMap) throws IOException {
        final ExifAttribute exifAttribute = hashMap.get("BitsPerSample");
        if (exifAttribute != null) {
            final int[] a = (int[])exifAttribute.getValue(this.mExifByteOrder);
            if (Arrays.equals(ExifInterface.BITS_PER_SAMPLE_RGB, a)) {
                return true;
            }
            if (this.mMimeType == 3) {
                final ExifAttribute exifAttribute2 = hashMap.get("PhotometricInterpretation");
                if (exifAttribute2 != null) {
                    final int intValue = exifAttribute2.getIntValue(this.mExifByteOrder);
                    if ((intValue == 1 && Arrays.equals(a, ExifInterface.BITS_PER_SAMPLE_GREYSCALE_2)) || (intValue == 6 && Arrays.equals(a, ExifInterface.BITS_PER_SAMPLE_RGB))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isThumbnail(final HashMap hashMap) throws IOException {
        final ExifAttribute exifAttribute = hashMap.get("ImageLength");
        final ExifAttribute exifAttribute2 = hashMap.get("ImageWidth");
        if (exifAttribute != null && exifAttribute2 != null) {
            final int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            final int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
            if (intValue <= 512 && intValue2 <= 512) {
                return true;
            }
        }
        return false;
    }
    
    private void loadAttributes(final InputStream in) throws IOException {
        int i = 0;
        try {
            try {
                while (i < ExifInterface.EXIF_TAGS.length) {
                    this.mAttributes[i] = new HashMap<String, ExifAttribute>();
                    ++i;
                }
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(in, 5000);
                this.mMimeType = this.getMimeType(bufferedInputStream);
                final ByteOrderedDataInputStream thumbnailData = new ByteOrderedDataInputStream(bufferedInputStream);
                switch (this.mMimeType) {
                    case 10: {
                        this.getRw2Attributes(thumbnailData);
                        break;
                    }
                    case 9: {
                        this.getRafAttributes(thumbnailData);
                        break;
                    }
                    case 7: {
                        this.getOrfAttributes(thumbnailData);
                        break;
                    }
                    case 4: {
                        this.getJpegAttributes(thumbnailData, 0, 0);
                        break;
                    }
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 11: {
                        this.getRawAttributes(thumbnailData);
                        break;
                    }
                }
                this.setThumbnailData(thumbnailData);
                this.mIsSupportedFile = true;
            }
            finally {}
        }
        catch (IOException ex) {
            this.mIsSupportedFile = false;
        }
        this.addDefaultValuesForCompatibility();
        return;
        this.addDefaultValuesForCompatibility();
    }
    
    private void parseTiffHeaders(final ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder = this.readByteOrder(byteOrderedDataInputStream));
        final int unsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        final int mMimeType = this.mMimeType;
        if (mMimeType != 7 && mMimeType != 10 && unsignedShort != 42) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid start code: ");
            sb.append(Integer.toHexString(unsignedShort));
            throw new IOException(sb.toString());
        }
        final int int1 = byteOrderedDataInputStream.readInt();
        if (int1 < 8 || int1 >= i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid first Ifd offset: ");
            sb2.append(int1);
            throw new IOException(sb2.toString());
        }
        i = int1 - 8;
        if (i > 0 && byteOrderedDataInputStream.skipBytes(i) != i) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Couldn't jump to first Ifd: ");
            sb3.append(i);
            throw new IOException(sb3.toString());
        }
    }
    
    private ByteOrder readByteOrder(final ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        final short short1 = byteOrderedDataInputStream.readShort();
        if (short1 == 18761) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (short1 == 19789) {
            return ByteOrder.BIG_ENDIAN;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid byte order: ");
        sb.append(Integer.toHexString(short1));
        throw new IOException(sb.toString());
    }
    
    private void readExifSegment(final byte[] array, final int n) throws IOException {
        final ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(array);
        this.parseTiffHeaders(byteOrderedDataInputStream, array.length);
        this.readImageFileDirectory(byteOrderedDataInputStream, n);
    }
    
    private void readImageFileDirectory(final ByteOrderedDataInputStream byteOrderedDataInputStream, int int1) throws IOException {
        this.mAttributesOffsets.add(byteOrderedDataInputStream.mPosition);
        if (byteOrderedDataInputStream.mPosition + 2 > byteOrderedDataInputStream.mLength) {
            return;
        }
        final short short1 = byteOrderedDataInputStream.readShort();
        if (byteOrderedDataInputStream.mPosition + short1 * 12 <= byteOrderedDataInputStream.mLength) {
            if (short1 > 0) {
                short n = 0;
                while (true) {
                    final int n2 = int1;
                    if (n >= short1) {
                        break;
                    }
                    final int unsignedShort = byteOrderedDataInputStream.readUnsignedShort();
                    final int unsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
                    final int int2 = byteOrderedDataInputStream.readInt();
                    final long n3 = byteOrderedDataInputStream.peek() + 4L;
                    final ExifTag exifTag = ExifInterface.sExifTagMapsForReading[n2].get(unsignedShort);
                    int primaryFormat = 0;
                    long n4 = 0L;
                    boolean b = false;
                    Label_0402: {
                        Label_0399: {
                            if (exifTag == null) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Skip the tag entry since tag number is not defined: ");
                                sb.append(unsignedShort);
                                Log.w("ExifInterface", sb.toString());
                            }
                            else if (unsignedShort2 > 0 && unsignedShort2 < ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT.length) {
                                if (!exifTag.isFormatCompatible(unsignedShort2)) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Skip the tag entry since data format (");
                                    sb2.append(ExifInterface.IFD_FORMAT_NAMES[unsignedShort2]);
                                    sb2.append(") is unexpected for tag: ");
                                    sb2.append(exifTag.name);
                                    Log.w("ExifInterface", sb2.toString());
                                }
                                else {
                                    if ((primaryFormat = unsignedShort2) == 7) {
                                        primaryFormat = exifTag.primaryFormat;
                                    }
                                    n4 = int2 * (long)ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[primaryFormat];
                                    if (n4 >= 0L && n4 <= 2147483647L) {
                                        b = true;
                                        break Label_0402;
                                    }
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Skip the tag entry since the number of components is invalid: ");
                                    sb3.append(int2);
                                    Log.w("ExifInterface", sb3.toString());
                                    break Label_0399;
                                }
                            }
                            else {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("Skip the tag entry since data format is invalid: ");
                                sb4.append(unsignedShort2);
                                Log.w("ExifInterface", sb4.toString());
                            }
                            n4 = 0L;
                            primaryFormat = unsignedShort2;
                        }
                        b = false;
                    }
                    Label_1120: {
                        if (!b) {
                            byteOrderedDataInputStream.seek(n3);
                        }
                        else {
                            if (n4 > 4L) {
                                final int int3 = byteOrderedDataInputStream.readInt();
                                final int mMimeType = this.mMimeType;
                                if (mMimeType == 7) {
                                    if ("MakerNote".equals(exifTag.name)) {
                                        this.mOrfMakerNoteOffset = int3;
                                    }
                                    else if (n2 == 6 && "ThumbnailImage".equals(exifTag.name)) {
                                        this.mOrfThumbnailOffset = int3;
                                        this.mOrfThumbnailLength = int2;
                                        final ExifAttribute uShort = ExifAttribute.createUShort(6, this.mExifByteOrder);
                                        final ExifAttribute uLong = ExifAttribute.createULong(this.mOrfThumbnailOffset, this.mExifByteOrder);
                                        final ExifAttribute uLong2 = ExifAttribute.createULong(this.mOrfThumbnailLength, this.mExifByteOrder);
                                        this.mAttributes[4].put("Compression", uShort);
                                        this.mAttributes[4].put("JPEGInterchangeFormat", uLong);
                                        this.mAttributes[4].put("JPEGInterchangeFormatLength", uLong2);
                                    }
                                }
                                else if (mMimeType == 10 && "JpgFromRaw".equals(exifTag.name)) {
                                    this.mRw2JpgFromRawOffset = int3;
                                }
                                final long n5 = int3;
                                if (n5 + n4 > byteOrderedDataInputStream.mLength) {
                                    final StringBuilder sb5 = new StringBuilder();
                                    sb5.append("Skip the tag entry since data offset is invalid: ");
                                    sb5.append(int3);
                                    Log.w("ExifInterface", sb5.toString());
                                    byteOrderedDataInputStream.seek(n3);
                                    break Label_1120;
                                }
                                byteOrderedDataInputStream.seek(n5);
                            }
                            final Integer obj = ExifInterface.sExifPointerTagMap.get(unsignedShort);
                            if (obj != null) {
                                long unsignedInt = -1L;
                                while (true) {
                                    int n6 = 0;
                                    Label_0784: {
                                        if (primaryFormat == 3) {
                                            n6 = byteOrderedDataInputStream.readUnsignedShort();
                                            break Label_0784;
                                        }
                                        if (primaryFormat != 4) {
                                            if (primaryFormat == 8) {
                                                n6 = byteOrderedDataInputStream.readShort();
                                                break Label_0784;
                                            }
                                            if (primaryFormat == 9 || primaryFormat == 13) {
                                                n6 = byteOrderedDataInputStream.readInt();
                                                break Label_0784;
                                            }
                                        }
                                        else {
                                            unsignedInt = byteOrderedDataInputStream.readUnsignedInt();
                                        }
                                        if (unsignedInt > 0L && unsignedInt < byteOrderedDataInputStream.mLength) {
                                            if (!this.mAttributesOffsets.contains((int)unsignedInt)) {
                                                byteOrderedDataInputStream.seek(unsignedInt);
                                                this.readImageFileDirectory(byteOrderedDataInputStream, obj);
                                            }
                                            else {
                                                final StringBuilder sb6 = new StringBuilder();
                                                sb6.append("Skip jump into the IFD since it has already been read: IfdType ");
                                                sb6.append(obj);
                                                sb6.append(" (at ");
                                                sb6.append(unsignedInt);
                                                sb6.append(")");
                                                Log.w("ExifInterface", sb6.toString());
                                            }
                                        }
                                        else {
                                            final StringBuilder sb7 = new StringBuilder();
                                            sb7.append("Skip jump into the IFD since its offset is invalid: ");
                                            sb7.append(unsignedInt);
                                            Log.w("ExifInterface", sb7.toString());
                                        }
                                        byteOrderedDataInputStream.seek(n3);
                                        break Label_1120;
                                    }
                                    unsignedInt = n6;
                                    continue;
                                }
                            }
                            final byte[] array = new byte[(int)n4];
                            byteOrderedDataInputStream.readFully(array);
                            final ExifAttribute value = new ExifAttribute(primaryFormat, int2, array);
                            this.mAttributes[int1].put(exifTag.name, value);
                            if ("DNGVersion".equals(exifTag.name)) {
                                this.mMimeType = 3;
                            }
                            if ((("Make".equals(exifTag.name) || "Model".equals(exifTag.name)) && value.getStringValue(this.mExifByteOrder).contains("PENTAX")) || ("Compression".equals(exifTag.name) && value.getIntValue(this.mExifByteOrder) == 65535)) {
                                this.mMimeType = 8;
                            }
                            if (byteOrderedDataInputStream.peek() != n3) {
                                byteOrderedDataInputStream.seek(n3);
                            }
                        }
                    }
                    ++n;
                }
                if (byteOrderedDataInputStream.peek() + 4 <= byteOrderedDataInputStream.mLength) {
                    int1 = byteOrderedDataInputStream.readInt();
                    final long n7 = int1;
                    if (n7 > 0L && int1 < byteOrderedDataInputStream.mLength) {
                        if (!this.mAttributesOffsets.contains(int1)) {
                            byteOrderedDataInputStream.seek(n7);
                            if (this.mAttributes[4].isEmpty()) {
                                this.readImageFileDirectory(byteOrderedDataInputStream, 4);
                            }
                            else if (this.mAttributes[5].isEmpty()) {
                                this.readImageFileDirectory(byteOrderedDataInputStream, 5);
                            }
                        }
                        else {
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append("Stop reading file since re-reading an IFD may cause an infinite loop: ");
                            sb8.append(int1);
                            Log.w("ExifInterface", sb8.toString());
                        }
                    }
                    else {
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append("Stop reading file since a wrong offset may cause an infinite loop: ");
                        sb9.append(int1);
                        Log.w("ExifInterface", sb9.toString());
                    }
                }
            }
        }
    }
    
    private void retrieveJpegImageSize(final ByteOrderedDataInputStream byteOrderedDataInputStream, final int n) throws IOException {
        final ExifAttribute exifAttribute = this.mAttributes[n].get("ImageLength");
        final ExifAttribute exifAttribute2 = this.mAttributes[n].get("ImageWidth");
        if (exifAttribute == null || exifAttribute2 == null) {
            final ExifAttribute exifAttribute3 = this.mAttributes[n].get("JPEGInterchangeFormat");
            if (exifAttribute3 != null) {
                this.getJpegAttributes(byteOrderedDataInputStream, exifAttribute3.getIntValue(this.mExifByteOrder), n);
            }
        }
    }
    
    private void setThumbnailData(final ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        final HashMap<String, ExifAttribute> hashMap = this.mAttributes[4];
        final ExifAttribute exifAttribute = hashMap.get("Compression");
        if (exifAttribute != null) {
            this.mThumbnailCompression = exifAttribute.getIntValue(this.mExifByteOrder);
            final int mThumbnailCompression = this.mThumbnailCompression;
            if (mThumbnailCompression != 1) {
                if (mThumbnailCompression == 6) {
                    this.handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
                    return;
                }
                if (mThumbnailCompression != 7) {
                    return;
                }
            }
            if (this.isSupportedDataType(hashMap)) {
                this.handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
            }
        }
        else {
            this.mThumbnailCompression = 6;
            this.handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
        }
    }
    
    private void swapBasedOnImageSize(final int n, final int n2) throws IOException {
        if (!this.mAttributes[n].isEmpty()) {
            if (!this.mAttributes[n2].isEmpty()) {
                final ExifAttribute exifAttribute = this.mAttributes[n].get("ImageLength");
                final ExifAttribute exifAttribute2 = this.mAttributes[n].get("ImageWidth");
                final ExifAttribute exifAttribute3 = this.mAttributes[n2].get("ImageLength");
                final ExifAttribute exifAttribute4 = this.mAttributes[n2].get("ImageWidth");
                if (exifAttribute != null) {
                    if (exifAttribute2 != null) {
                        if (exifAttribute3 != null) {
                            if (exifAttribute4 != null) {
                                final int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                                final int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                                final int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                                final int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                                if (intValue < intValue3 && intValue2 < intValue4) {
                                    final HashMap<String, ExifAttribute>[] mAttributes = this.mAttributes;
                                    final HashMap<String, ExifAttribute> hashMap = mAttributes[n];
                                    mAttributes[n] = mAttributes[n2];
                                    mAttributes[n2] = hashMap;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void updateImageSizeValues(final ByteOrderedDataInputStream byteOrderedDataInputStream, final int n) throws IOException {
        final ExifAttribute exifAttribute = this.mAttributes[n].get("DefaultCropSize");
        final ExifAttribute exifAttribute2 = this.mAttributes[n].get("SensorTopBorder");
        final ExifAttribute exifAttribute3 = this.mAttributes[n].get("SensorLeftBorder");
        final ExifAttribute exifAttribute4 = this.mAttributes[n].get("SensorBottomBorder");
        final ExifAttribute exifAttribute5 = this.mAttributes[n].get("SensorRightBorder");
        if (exifAttribute != null) {
            ExifAttribute value;
            ExifAttribute value2;
            if (exifAttribute.format == 5) {
                final Rational[] a = (Rational[])exifAttribute.getValue(this.mExifByteOrder);
                if (a == null || a.length != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid crop size values. cropSize=");
                    sb.append(Arrays.toString(a));
                    Log.w("ExifInterface", sb.toString());
                    return;
                }
                value = ExifAttribute.createURational(a[0], this.mExifByteOrder);
                value2 = ExifAttribute.createURational(a[1], this.mExifByteOrder);
            }
            else {
                final int[] a2 = (int[])exifAttribute.getValue(this.mExifByteOrder);
                if (a2 == null || a2.length != 2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid crop size values. cropSize=");
                    sb2.append(Arrays.toString(a2));
                    Log.w("ExifInterface", sb2.toString());
                    return;
                }
                value = ExifAttribute.createUShort(a2[0], this.mExifByteOrder);
                value2 = ExifAttribute.createUShort(a2[1], this.mExifByteOrder);
            }
            this.mAttributes[n].put("ImageWidth", value);
            this.mAttributes[n].put("ImageLength", value2);
        }
        else if (exifAttribute2 != null && exifAttribute3 != null && exifAttribute4 != null && exifAttribute5 != null) {
            final int intValue = exifAttribute2.getIntValue(this.mExifByteOrder);
            final int intValue2 = exifAttribute4.getIntValue(this.mExifByteOrder);
            final int intValue3 = exifAttribute5.getIntValue(this.mExifByteOrder);
            final int intValue4 = exifAttribute3.getIntValue(this.mExifByteOrder);
            if (intValue2 > intValue && intValue3 > intValue4) {
                final ExifAttribute uShort = ExifAttribute.createUShort(intValue2 - intValue, this.mExifByteOrder);
                final ExifAttribute uShort2 = ExifAttribute.createUShort(intValue3 - intValue4, this.mExifByteOrder);
                this.mAttributes[n].put("ImageLength", uShort);
                this.mAttributes[n].put("ImageWidth", uShort2);
            }
        }
        else {
            this.retrieveJpegImageSize(byteOrderedDataInputStream, n);
        }
    }
    
    private void validateImages(final InputStream inputStream) throws IOException {
        this.swapBasedOnImageSize(0, 5);
        this.swapBasedOnImageSize(0, 4);
        this.swapBasedOnImageSize(5, 4);
        final ExifAttribute value = this.mAttributes[1].get("PixelXDimension");
        final ExifAttribute value2 = this.mAttributes[1].get("PixelYDimension");
        if (value != null && value2 != null) {
            this.mAttributes[0].put("ImageWidth", value);
            this.mAttributes[0].put("ImageLength", value2);
        }
        if (this.mAttributes[4].isEmpty() && this.isThumbnail(this.mAttributes[5])) {
            final HashMap<String, ExifAttribute>[] mAttributes = this.mAttributes;
            mAttributes[4] = mAttributes[5];
            mAttributes[5] = new HashMap<String, ExifAttribute>();
        }
        if (!this.isThumbnail(this.mAttributes[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
    }
    
    public String getAttribute(String string) {
        final ExifAttribute exifAttribute = this.getExifAttribute(string);
        Label_0247: {
            if (exifAttribute == null) {
                break Label_0247;
            }
            if (!ExifInterface.sTagSetForCompatibility.contains(string)) {
                return exifAttribute.getStringValue(this.mExifByteOrder);
            }
            if (string.equals("GPSTimeStamp")) {
                final int format = exifAttribute.format;
                if (format != 5 && format != 10) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("GPS Timestamp format is not rational. format=");
                    sb.append(exifAttribute.format);
                    Log.w("ExifInterface", sb.toString());
                    return null;
                }
                final Rational[] a = (Rational[])exifAttribute.getValue(this.mExifByteOrder);
                if (a != null && a.length == 3) {
                    return String.format("%02d:%02d:%02d", (int)(a[0].numerator / (float)a[0].denominator), (int)(a[1].numerator / (float)a[1].denominator), (int)(a[2].numerator / (float)a[2].denominator));
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid GPS Timestamp array. array=");
                sb2.append(Arrays.toString(a));
                Log.w("ExifInterface", sb2.toString());
                return null;
            }
            try {
                string = Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
                return string;
                return null;
            }
            catch (NumberFormatException ex) {
                return null;
            }
        }
    }
    
    public int getAttributeInt(final String s, final int n) {
        final ExifAttribute exifAttribute = this.getExifAttribute(s);
        if (exifAttribute == null) {
            return n;
        }
        try {
            return exifAttribute.getIntValue(this.mExifByteOrder);
        }
        catch (NumberFormatException ex) {
            return n;
        }
    }
    
    private static class ByteOrderedDataInputStream extends InputStream implements DataInput
    {
        private static final ByteOrder BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN;
        private ByteOrder mByteOrder;
        private DataInputStream mDataInputStream;
        final int mLength;
        int mPosition;
        
        static {
            LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
            BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        }
        
        public ByteOrderedDataInputStream(final InputStream in) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mDataInputStream = new DataInputStream(in);
            this.mLength = this.mDataInputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(this.mLength);
        }
        
        public ByteOrderedDataInputStream(final byte[] buf) throws IOException {
            this(new ByteArrayInputStream(buf));
        }
        
        @Override
        public int available() throws IOException {
            return this.mDataInputStream.available();
        }
        
        public int peek() {
            return this.mPosition;
        }
        
        @Override
        public int read() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.read();
        }
        
        @Override
        public int read(final byte[] b, int read, final int len) throws IOException {
            read = this.mDataInputStream.read(b, read, len);
            this.mPosition += read;
            return read;
        }
        
        @Override
        public boolean readBoolean() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.readBoolean();
        }
        
        @Override
        public byte readByte() throws IOException {
            ++this.mPosition;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            final int read = this.mDataInputStream.read();
            if (read >= 0) {
                return (byte)read;
            }
            throw new EOFException();
        }
        
        @Override
        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }
        
        @Override
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(this.readLong());
        }
        
        @Override
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(this.readInt());
        }
        
        @Override
        public void readFully(final byte[] b) throws IOException {
            this.mPosition += b.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            if (this.mDataInputStream.read(b, 0, b.length) == b.length) {
                return;
            }
            throw new IOException("Couldn't read up to the length of buffer");
        }
        
        @Override
        public void readFully(final byte[] b, final int off, final int len) throws IOException {
            this.mPosition += len;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            if (this.mDataInputStream.read(b, off, len) == len) {
                return;
            }
            throw new IOException("Couldn't read up to the length of buffer");
        }
        
        @Override
        public int readInt() throws IOException {
            this.mPosition += 4;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            final int read = this.mDataInputStream.read();
            final int read2 = this.mDataInputStream.read();
            final int read3 = this.mDataInputStream.read();
            final int read4 = this.mDataInputStream.read();
            if ((read | read2 | read3 | read4) < 0) {
                throw new EOFException();
            }
            final ByteOrder mByteOrder = this.mByteOrder;
            if (mByteOrder == ByteOrderedDataInputStream.LITTLE_ENDIAN) {
                return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
            }
            if (mByteOrder == ByteOrderedDataInputStream.BIG_ENDIAN) {
                return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid byte order: ");
            sb.append(this.mByteOrder);
            throw new IOException(sb.toString());
        }
        
        @Override
        public String readLine() throws IOException {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }
        
        @Override
        public long readLong() throws IOException {
            this.mPosition += 8;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            final int read = this.mDataInputStream.read();
            final int read2 = this.mDataInputStream.read();
            final int read3 = this.mDataInputStream.read();
            final int read4 = this.mDataInputStream.read();
            final int read5 = this.mDataInputStream.read();
            final int read6 = this.mDataInputStream.read();
            final int read7 = this.mDataInputStream.read();
            final int read8 = this.mDataInputStream.read();
            if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) < 0) {
                throw new EOFException();
            }
            final ByteOrder mByteOrder = this.mByteOrder;
            if (mByteOrder == ByteOrderedDataInputStream.LITTLE_ENDIAN) {
                return ((long)read8 << 56) + ((long)read7 << 48) + ((long)read6 << 40) + ((long)read5 << 32) + ((long)read4 << 24) + ((long)read3 << 16) + ((long)read2 << 8) + read;
            }
            if (mByteOrder == ByteOrderedDataInputStream.BIG_ENDIAN) {
                return ((long)read << 56) + ((long)read2 << 48) + ((long)read3 << 40) + ((long)read4 << 32) + ((long)read5 << 24) + ((long)read6 << 16) + ((long)read7 << 8) + read8;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid byte order: ");
            sb.append(this.mByteOrder);
            throw new IOException(sb.toString());
        }
        
        @Override
        public short readShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            final int read = this.mDataInputStream.read();
            final int read2 = this.mDataInputStream.read();
            if ((read | read2) < 0) {
                throw new EOFException();
            }
            final ByteOrder mByteOrder = this.mByteOrder;
            if (mByteOrder == ByteOrderedDataInputStream.LITTLE_ENDIAN) {
                return (short)((read2 << 8) + read);
            }
            if (mByteOrder == ByteOrderedDataInputStream.BIG_ENDIAN) {
                return (short)((read << 8) + read2);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid byte order: ");
            sb.append(this.mByteOrder);
            throw new IOException(sb.toString());
        }
        
        @Override
        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }
        
        @Override
        public int readUnsignedByte() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.readUnsignedByte();
        }
        
        public long readUnsignedInt() throws IOException {
            return (long)this.readInt() & 0xFFFFFFFFL;
        }
        
        @Override
        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            final int read = this.mDataInputStream.read();
            final int read2 = this.mDataInputStream.read();
            if ((read | read2) < 0) {
                throw new EOFException();
            }
            final ByteOrder mByteOrder = this.mByteOrder;
            if (mByteOrder == ByteOrderedDataInputStream.LITTLE_ENDIAN) {
                return (read2 << 8) + read;
            }
            if (mByteOrder == ByteOrderedDataInputStream.BIG_ENDIAN) {
                return (read << 8) + read2;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid byte order: ");
            sb.append(this.mByteOrder);
            throw new IOException(sb.toString());
        }
        
        public void seek(long n) throws IOException {
            final int mPosition = this.mPosition;
            if (mPosition > n) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            }
            else {
                n -= mPosition;
            }
            final int n2 = (int)n;
            if (this.skipBytes(n2) == n2) {
                return;
            }
            throw new IOException("Couldn't seek up to the byteCount");
        }
        
        public void setByteOrder(final ByteOrder mByteOrder) {
            this.mByteOrder = mByteOrder;
        }
        
        @Override
        public int skipBytes(int i) throws IOException {
            int min;
            for (min = Math.min(i, this.mLength - this.mPosition), i = 0; i < min; i += this.mDataInputStream.skipBytes(min - i)) {}
            this.mPosition += i;
            return i;
        }
    }
    
    private static class ExifAttribute
    {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;
        
        ExifAttribute(final int format, final int numberOfComponents, final byte[] bytes) {
            this.format = format;
            this.numberOfComponents = numberOfComponents;
            this.bytes = bytes;
        }
        
        public static ExifAttribute createString(final String str) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append('\0');
            final byte[] bytes = sb.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes.length, bytes);
        }
        
        public static ExifAttribute createULong(final long n, final ByteOrder byteOrder) {
            return createULong(new long[] { n }, byteOrder);
        }
        
        public static ExifAttribute createULong(final long[] array, final ByteOrder bo) {
            final ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * array.length]);
            wrap.order(bo);
            for (int length = array.length, i = 0; i < length; ++i) {
                wrap.putInt((int)array[i]);
            }
            return new ExifAttribute(4, array.length, wrap.array());
        }
        
        public static ExifAttribute createURational(final Rational rational, final ByteOrder byteOrder) {
            return createURational(new Rational[] { rational }, byteOrder);
        }
        
        public static ExifAttribute createURational(final Rational[] array, final ByteOrder bo) {
            final ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * array.length]);
            wrap.order(bo);
            for (final Rational rational : array) {
                wrap.putInt((int)rational.numerator);
                wrap.putInt((int)rational.denominator);
            }
            return new ExifAttribute(5, array.length, wrap.array());
        }
        
        public static ExifAttribute createUShort(final int n, final ByteOrder byteOrder) {
            return createUShort(new int[] { n }, byteOrder);
        }
        
        public static ExifAttribute createUShort(final int[] array, final ByteOrder bo) {
            final ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * array.length]);
            wrap.order(bo);
            for (int length = array.length, i = 0; i < length; ++i) {
                wrap.putShort((short)array[i]);
            }
            return new ExifAttribute(3, array.length, wrap.array());
        }
        
        public double getDoubleValue(final ByteOrder byteOrder) {
            final Object value = this.getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            }
            if (value instanceof String) {
                return Double.parseDouble((String)value);
            }
            if (value instanceof long[]) {
                final long[] array = (long[])value;
                if (array.length == 1) {
                    return (double)array[0];
                }
                throw new NumberFormatException("There are more than one component");
            }
            else if (value instanceof int[]) {
                final int[] array2 = (int[])value;
                if (array2.length == 1) {
                    return array2[0];
                }
                throw new NumberFormatException("There are more than one component");
            }
            else if (value instanceof double[]) {
                final double[] array3 = (double[])value;
                if (array3.length == 1) {
                    return array3[0];
                }
                throw new NumberFormatException("There are more than one component");
            }
            else {
                if (!(value instanceof Rational[])) {
                    throw new NumberFormatException("Couldn't find a double value");
                }
                final Rational[] array4 = (Rational[])value;
                if (array4.length == 1) {
                    return array4[0].calculate();
                }
                throw new NumberFormatException("There are more than one component");
            }
        }
        
        public int getIntValue(final ByteOrder byteOrder) {
            final Object value = this.getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            }
            if (value instanceof String) {
                return Integer.parseInt((String)value);
            }
            if (value instanceof long[]) {
                final long[] array = (long[])value;
                if (array.length == 1) {
                    return (int)array[0];
                }
                throw new NumberFormatException("There are more than one component");
            }
            else {
                if (!(value instanceof int[])) {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
                final int[] array2 = (int[])value;
                if (array2.length == 1) {
                    return array2[0];
                }
                throw new NumberFormatException("There are more than one component");
            }
        }
        
        public String getStringValue(final ByteOrder byteOrder) {
            final Object value = this.getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String)value;
            }
            final StringBuilder sb = new StringBuilder();
            final boolean b = value instanceof long[];
            final int n = 0;
            final int n2 = 0;
            final int n3 = 0;
            int i = 0;
            if (b) {
                int n4;
                for (long[] array = (long[])value; i < array.length; i = n4) {
                    sb.append(array[i]);
                    n4 = i + 1;
                    if ((i = n4) != array.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
            if (value instanceof int[]) {
                final int[] array2 = (int[])value;
                int n5;
                for (int j = n; j < array2.length; j = n5) {
                    sb.append(array2[j]);
                    n5 = j + 1;
                    if ((j = n5) != array2.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
            if (value instanceof double[]) {
                final double[] array3 = (double[])value;
                int n6;
                for (int k = n2; k < array3.length; k = n6) {
                    sb.append(array3[k]);
                    n6 = k + 1;
                    if ((k = n6) != array3.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
            if (value instanceof Rational[]) {
                final Rational[] array4 = (Rational[])value;
                int n7;
                for (int l = n3; l < array4.length; l = n7) {
                    sb.append(array4[l].numerator);
                    sb.append('/');
                    sb.append(array4[l].denominator);
                    n7 = l + 1;
                    if ((l = n7) != array4.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
            return null;
        }
        
        Object getValue(final ByteOrder p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     4: aload_2        
            //     5: aload_0        
            //     6: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //     9: invokespecial   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.<init>:([B)V
            //    12: aload_2        
            //    13: astore_3       
            //    14: aload_2        
            //    15: aload_1        
            //    16: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.setByteOrder:(Ljava/nio/ByteOrder;)V
            //    19: aload_2        
            //    20: astore_3       
            //    21: aload_0        
            //    22: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.format:I
            //    25: istore          4
            //    27: iconst_1       
            //    28: istore          5
            //    30: iconst_0       
            //    31: istore          6
            //    33: iconst_0       
            //    34: istore          7
            //    36: iconst_0       
            //    37: istore          8
            //    39: iconst_0       
            //    40: istore          9
            //    42: iconst_0       
            //    43: istore          10
            //    45: iconst_0       
            //    46: istore          11
            //    48: iconst_0       
            //    49: istore          12
            //    51: iconst_0       
            //    52: istore          13
            //    54: iconst_0       
            //    55: istore          14
            //    57: iload           4
            //    59: tableswitch {
            //                2: 803
            //                3: 618
            //                4: 559
            //                5: 500
            //                6: 429
            //                7: 803
            //                8: 618
            //                9: 370
            //               10: 311
            //               11: 238
            //               12: 178
            //               13: 123
            //          default: 120
            //        }
            //   120: goto            918
            //   123: aload_2        
            //   124: astore_3       
            //   125: aload_0        
            //   126: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   129: newarray        D
            //   131: astore_1       
            //   132: aload_2        
            //   133: astore_3       
            //   134: iload           14
            //   136: aload_0        
            //   137: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   140: if_icmpge       159
            //   143: aload_2        
            //   144: astore_3       
            //   145: aload_1        
            //   146: iload           14
            //   148: aload_2        
            //   149: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readDouble:()D
            //   152: dastore        
            //   153: iinc            14, 1
            //   156: goto            132
            //   159: aload_2        
            //   160: invokevirtual   java/io/InputStream.close:()V
            //   163: goto            176
            //   166: astore_3       
            //   167: ldc             "ExifInterface"
            //   169: ldc             "IOException occurred while closing InputStream"
            //   171: aload_3        
            //   172: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   175: pop            
            //   176: aload_1        
            //   177: areturn        
            //   178: aload_2        
            //   179: astore_3       
            //   180: aload_0        
            //   181: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   184: newarray        D
            //   186: astore_1       
            //   187: iload           6
            //   189: istore          14
            //   191: aload_2        
            //   192: astore_3       
            //   193: iload           14
            //   195: aload_0        
            //   196: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   199: if_icmpge       219
            //   202: aload_2        
            //   203: astore_3       
            //   204: aload_1        
            //   205: iload           14
            //   207: aload_2        
            //   208: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readFloat:()F
            //   211: f2d            
            //   212: dastore        
            //   213: iinc            14, 1
            //   216: goto            191
            //   219: aload_2        
            //   220: invokevirtual   java/io/InputStream.close:()V
            //   223: goto            236
            //   226: astore_3       
            //   227: ldc             "ExifInterface"
            //   229: ldc             "IOException occurred while closing InputStream"
            //   231: aload_3        
            //   232: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   235: pop            
            //   236: aload_1        
            //   237: areturn        
            //   238: aload_2        
            //   239: astore_3       
            //   240: aload_0        
            //   241: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   244: anewarray       Landroidx/exifinterface/media/ExifInterface$Rational;
            //   247: astore_1       
            //   248: iload           7
            //   250: istore          14
            //   252: aload_2        
            //   253: astore_3       
            //   254: iload           14
            //   256: aload_0        
            //   257: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   260: if_icmpge       292
            //   263: aload_2        
            //   264: astore_3       
            //   265: aload_1        
            //   266: iload           14
            //   268: new             Landroidx/exifinterface/media/ExifInterface$Rational;
            //   271: dup            
            //   272: aload_2        
            //   273: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readInt:()I
            //   276: i2l            
            //   277: aload_2        
            //   278: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readInt:()I
            //   281: i2l            
            //   282: invokespecial   androidx/exifinterface/media/ExifInterface$Rational.<init>:(JJ)V
            //   285: aastore        
            //   286: iinc            14, 1
            //   289: goto            252
            //   292: aload_2        
            //   293: invokevirtual   java/io/InputStream.close:()V
            //   296: goto            309
            //   299: astore_3       
            //   300: ldc             "ExifInterface"
            //   302: ldc             "IOException occurred while closing InputStream"
            //   304: aload_3        
            //   305: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   308: pop            
            //   309: aload_1        
            //   310: areturn        
            //   311: aload_2        
            //   312: astore_3       
            //   313: aload_0        
            //   314: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   317: newarray        I
            //   319: astore_1       
            //   320: iload           8
            //   322: istore          14
            //   324: aload_2        
            //   325: astore_3       
            //   326: iload           14
            //   328: aload_0        
            //   329: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   332: if_icmpge       351
            //   335: aload_2        
            //   336: astore_3       
            //   337: aload_1        
            //   338: iload           14
            //   340: aload_2        
            //   341: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readInt:()I
            //   344: iastore        
            //   345: iinc            14, 1
            //   348: goto            324
            //   351: aload_2        
            //   352: invokevirtual   java/io/InputStream.close:()V
            //   355: goto            368
            //   358: astore_3       
            //   359: ldc             "ExifInterface"
            //   361: ldc             "IOException occurred while closing InputStream"
            //   363: aload_3        
            //   364: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   367: pop            
            //   368: aload_1        
            //   369: areturn        
            //   370: aload_2        
            //   371: astore_3       
            //   372: aload_0        
            //   373: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   376: newarray        I
            //   378: astore_1       
            //   379: iload           9
            //   381: istore          14
            //   383: aload_2        
            //   384: astore_3       
            //   385: iload           14
            //   387: aload_0        
            //   388: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   391: if_icmpge       410
            //   394: aload_2        
            //   395: astore_3       
            //   396: aload_1        
            //   397: iload           14
            //   399: aload_2        
            //   400: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readShort:()S
            //   403: iastore        
            //   404: iinc            14, 1
            //   407: goto            383
            //   410: aload_2        
            //   411: invokevirtual   java/io/InputStream.close:()V
            //   414: goto            427
            //   417: astore_3       
            //   418: ldc             "ExifInterface"
            //   420: ldc             "IOException occurred while closing InputStream"
            //   422: aload_3        
            //   423: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   426: pop            
            //   427: aload_1        
            //   428: areturn        
            //   429: aload_2        
            //   430: astore_3       
            //   431: aload_0        
            //   432: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   435: anewarray       Landroidx/exifinterface/media/ExifInterface$Rational;
            //   438: astore_1       
            //   439: iload           10
            //   441: istore          14
            //   443: aload_2        
            //   444: astore_3       
            //   445: iload           14
            //   447: aload_0        
            //   448: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   451: if_icmpge       481
            //   454: aload_2        
            //   455: astore_3       
            //   456: aload_1        
            //   457: iload           14
            //   459: new             Landroidx/exifinterface/media/ExifInterface$Rational;
            //   462: dup            
            //   463: aload_2        
            //   464: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readUnsignedInt:()J
            //   467: aload_2        
            //   468: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readUnsignedInt:()J
            //   471: invokespecial   androidx/exifinterface/media/ExifInterface$Rational.<init>:(JJ)V
            //   474: aastore        
            //   475: iinc            14, 1
            //   478: goto            443
            //   481: aload_2        
            //   482: invokevirtual   java/io/InputStream.close:()V
            //   485: goto            498
            //   488: astore_3       
            //   489: ldc             "ExifInterface"
            //   491: ldc             "IOException occurred while closing InputStream"
            //   493: aload_3        
            //   494: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   497: pop            
            //   498: aload_1        
            //   499: areturn        
            //   500: aload_2        
            //   501: astore_3       
            //   502: aload_0        
            //   503: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   506: newarray        J
            //   508: astore_1       
            //   509: iload           11
            //   511: istore          14
            //   513: aload_2        
            //   514: astore_3       
            //   515: iload           14
            //   517: aload_0        
            //   518: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   521: if_icmpge       540
            //   524: aload_2        
            //   525: astore_3       
            //   526: aload_1        
            //   527: iload           14
            //   529: aload_2        
            //   530: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readUnsignedInt:()J
            //   533: lastore        
            //   534: iinc            14, 1
            //   537: goto            513
            //   540: aload_2        
            //   541: invokevirtual   java/io/InputStream.close:()V
            //   544: goto            557
            //   547: astore_3       
            //   548: ldc             "ExifInterface"
            //   550: ldc             "IOException occurred while closing InputStream"
            //   552: aload_3        
            //   553: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   556: pop            
            //   557: aload_1        
            //   558: areturn        
            //   559: aload_2        
            //   560: astore_3       
            //   561: aload_0        
            //   562: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   565: newarray        I
            //   567: astore_1       
            //   568: iload           12
            //   570: istore          14
            //   572: aload_2        
            //   573: astore_3       
            //   574: iload           14
            //   576: aload_0        
            //   577: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   580: if_icmpge       599
            //   583: aload_2        
            //   584: astore_3       
            //   585: aload_1        
            //   586: iload           14
            //   588: aload_2        
            //   589: invokevirtual   androidx/exifinterface/media/ExifInterface$ByteOrderedDataInputStream.readUnsignedShort:()I
            //   592: iastore        
            //   593: iinc            14, 1
            //   596: goto            572
            //   599: aload_2        
            //   600: invokevirtual   java/io/InputStream.close:()V
            //   603: goto            616
            //   606: astore_3       
            //   607: ldc             "ExifInterface"
            //   609: ldc             "IOException occurred while closing InputStream"
            //   611: aload_3        
            //   612: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   615: pop            
            //   616: aload_1        
            //   617: areturn        
            //   618: iload           13
            //   620: istore          14
            //   622: aload_2        
            //   623: astore_3       
            //   624: aload_0        
            //   625: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   628: getstatic       androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX:[B
            //   631: arraylength    
            //   632: if_icmplt       700
            //   635: iconst_0       
            //   636: istore          14
            //   638: iload           5
            //   640: istore          6
            //   642: aload_2        
            //   643: astore_3       
            //   644: iload           14
            //   646: getstatic       androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX:[B
            //   649: arraylength    
            //   650: if_icmpge       683
            //   653: aload_2        
            //   654: astore_3       
            //   655: aload_0        
            //   656: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   659: iload           14
            //   661: baload         
            //   662: getstatic       androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX:[B
            //   665: iload           14
            //   667: baload         
            //   668: if_icmpeq       677
            //   671: iconst_0       
            //   672: istore          6
            //   674: goto            683
            //   677: iinc            14, 1
            //   680: goto            638
            //   683: iload           13
            //   685: istore          14
            //   687: iload           6
            //   689: ifeq            700
            //   692: aload_2        
            //   693: astore_3       
            //   694: getstatic       androidx/exifinterface/media/ExifInterface.EXIF_ASCII_PREFIX:[B
            //   697: arraylength    
            //   698: istore          14
            //   700: aload_2        
            //   701: astore_3       
            //   702: new             Ljava/lang/StringBuilder;
            //   705: astore_1       
            //   706: aload_2        
            //   707: astore_3       
            //   708: aload_1        
            //   709: invokespecial   java/lang/StringBuilder.<init>:()V
            //   712: aload_2        
            //   713: astore_3       
            //   714: iload           14
            //   716: aload_0        
            //   717: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.numberOfComponents:I
            //   720: if_icmpge       777
            //   723: aload_2        
            //   724: astore_3       
            //   725: aload_0        
            //   726: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   729: iload           14
            //   731: baload         
            //   732: istore          6
            //   734: iload           6
            //   736: ifne            742
            //   739: goto            777
            //   742: iload           6
            //   744: bipush          32
            //   746: if_icmplt       762
            //   749: aload_2        
            //   750: astore_3       
            //   751: aload_1        
            //   752: iload           6
            //   754: i2c            
            //   755: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
            //   758: pop            
            //   759: goto            771
            //   762: aload_2        
            //   763: astore_3       
            //   764: aload_1        
            //   765: bipush          63
            //   767: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
            //   770: pop            
            //   771: iinc            14, 1
            //   774: goto            712
            //   777: aload_2        
            //   778: astore_3       
            //   779: aload_1        
            //   780: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   783: astore_1       
            //   784: aload_2        
            //   785: invokevirtual   java/io/InputStream.close:()V
            //   788: goto            801
            //   791: astore_3       
            //   792: ldc             "ExifInterface"
            //   794: ldc             "IOException occurred while closing InputStream"
            //   796: aload_3        
            //   797: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   800: pop            
            //   801: aload_1        
            //   802: areturn        
            //   803: aload_2        
            //   804: astore_3       
            //   805: aload_0        
            //   806: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   809: arraylength    
            //   810: iconst_1       
            //   811: if_icmpne       882
            //   814: aload_2        
            //   815: astore_3       
            //   816: aload_0        
            //   817: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   820: iconst_0       
            //   821: baload         
            //   822: iflt            882
            //   825: aload_2        
            //   826: astore_3       
            //   827: aload_0        
            //   828: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   831: iconst_0       
            //   832: baload         
            //   833: iconst_1       
            //   834: if_icmpgt       882
            //   837: aload_2        
            //   838: astore_3       
            //   839: new             Ljava/lang/String;
            //   842: dup            
            //   843: iconst_1       
            //   844: newarray        C
            //   846: dup            
            //   847: iconst_0       
            //   848: aload_0        
            //   849: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   852: iconst_0       
            //   853: baload         
            //   854: bipush          48
            //   856: iadd           
            //   857: i2c            
            //   858: castore        
            //   859: invokespecial   java/lang/String.<init>:([C)V
            //   862: astore_1       
            //   863: aload_2        
            //   864: invokevirtual   java/io/InputStream.close:()V
            //   867: goto            880
            //   870: astore_3       
            //   871: ldc             "ExifInterface"
            //   873: ldc             "IOException occurred while closing InputStream"
            //   875: aload_3        
            //   876: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   879: pop            
            //   880: aload_1        
            //   881: areturn        
            //   882: aload_2        
            //   883: astore_3       
            //   884: new             Ljava/lang/String;
            //   887: dup            
            //   888: aload_0        
            //   889: getfield        androidx/exifinterface/media/ExifInterface$ExifAttribute.bytes:[B
            //   892: getstatic       androidx/exifinterface/media/ExifInterface.ASCII:Ljava/nio/charset/Charset;
            //   895: invokespecial   java/lang/String.<init>:([BLjava/nio/charset/Charset;)V
            //   898: astore_1       
            //   899: aload_2        
            //   900: invokevirtual   java/io/InputStream.close:()V
            //   903: goto            916
            //   906: astore_3       
            //   907: ldc             "ExifInterface"
            //   909: ldc             "IOException occurred while closing InputStream"
            //   911: aload_3        
            //   912: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   915: pop            
            //   916: aload_1        
            //   917: areturn        
            //   918: aload_2        
            //   919: invokevirtual   java/io/InputStream.close:()V
            //   922: goto            935
            //   925: astore_1       
            //   926: ldc             "ExifInterface"
            //   928: ldc             "IOException occurred while closing InputStream"
            //   930: aload_1        
            //   931: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   934: pop            
            //   935: aconst_null    
            //   936: areturn        
            //   937: astore_3       
            //   938: aload_2        
            //   939: astore_1       
            //   940: aload_3        
            //   941: astore_2       
            //   942: goto            954
            //   945: astore_1       
            //   946: aconst_null    
            //   947: astore_3       
            //   948: goto            989
            //   951: astore_2       
            //   952: aconst_null    
            //   953: astore_1       
            //   954: aload_1        
            //   955: astore_3       
            //   956: ldc             "ExifInterface"
            //   958: ldc             "IOException occurred during reading a value"
            //   960: aload_2        
            //   961: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   964: pop            
            //   965: aload_1        
            //   966: ifnull          986
            //   969: aload_1        
            //   970: invokevirtual   java/io/InputStream.close:()V
            //   973: goto            986
            //   976: astore_1       
            //   977: ldc             "ExifInterface"
            //   979: ldc             "IOException occurred while closing InputStream"
            //   981: aload_1        
            //   982: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   985: pop            
            //   986: aconst_null    
            //   987: areturn        
            //   988: astore_1       
            //   989: aload_3        
            //   990: ifnull          1010
            //   993: aload_3        
            //   994: invokevirtual   java/io/InputStream.close:()V
            //   997: goto            1010
            //  1000: astore_3       
            //  1001: ldc             "ExifInterface"
            //  1003: ldc             "IOException occurred while closing InputStream"
            //  1005: aload_3        
            //  1006: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1009: pop            
            //  1010: aload_1        
            //  1011: athrow         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  0      12     951    954    Ljava/io/IOException;
            //  0      12     945    951    Any
            //  14     19     937    945    Ljava/io/IOException;
            //  14     19     988    989    Any
            //  21     27     937    945    Ljava/io/IOException;
            //  21     27     988    989    Any
            //  125    132    937    945    Ljava/io/IOException;
            //  125    132    988    989    Any
            //  134    143    937    945    Ljava/io/IOException;
            //  134    143    988    989    Any
            //  145    153    937    945    Ljava/io/IOException;
            //  145    153    988    989    Any
            //  159    163    166    176    Ljava/io/IOException;
            //  180    187    937    945    Ljava/io/IOException;
            //  180    187    988    989    Any
            //  193    202    937    945    Ljava/io/IOException;
            //  193    202    988    989    Any
            //  204    213    937    945    Ljava/io/IOException;
            //  204    213    988    989    Any
            //  219    223    226    236    Ljava/io/IOException;
            //  240    248    937    945    Ljava/io/IOException;
            //  240    248    988    989    Any
            //  254    263    937    945    Ljava/io/IOException;
            //  254    263    988    989    Any
            //  265    286    937    945    Ljava/io/IOException;
            //  265    286    988    989    Any
            //  292    296    299    309    Ljava/io/IOException;
            //  313    320    937    945    Ljava/io/IOException;
            //  313    320    988    989    Any
            //  326    335    937    945    Ljava/io/IOException;
            //  326    335    988    989    Any
            //  337    345    937    945    Ljava/io/IOException;
            //  337    345    988    989    Any
            //  351    355    358    368    Ljava/io/IOException;
            //  372    379    937    945    Ljava/io/IOException;
            //  372    379    988    989    Any
            //  385    394    937    945    Ljava/io/IOException;
            //  385    394    988    989    Any
            //  396    404    937    945    Ljava/io/IOException;
            //  396    404    988    989    Any
            //  410    414    417    427    Ljava/io/IOException;
            //  431    439    937    945    Ljava/io/IOException;
            //  431    439    988    989    Any
            //  445    454    937    945    Ljava/io/IOException;
            //  445    454    988    989    Any
            //  456    475    937    945    Ljava/io/IOException;
            //  456    475    988    989    Any
            //  481    485    488    498    Ljava/io/IOException;
            //  502    509    937    945    Ljava/io/IOException;
            //  502    509    988    989    Any
            //  515    524    937    945    Ljava/io/IOException;
            //  515    524    988    989    Any
            //  526    534    937    945    Ljava/io/IOException;
            //  526    534    988    989    Any
            //  540    544    547    557    Ljava/io/IOException;
            //  561    568    937    945    Ljava/io/IOException;
            //  561    568    988    989    Any
            //  574    583    937    945    Ljava/io/IOException;
            //  574    583    988    989    Any
            //  585    593    937    945    Ljava/io/IOException;
            //  585    593    988    989    Any
            //  599    603    606    616    Ljava/io/IOException;
            //  624    635    937    945    Ljava/io/IOException;
            //  624    635    988    989    Any
            //  644    653    937    945    Ljava/io/IOException;
            //  644    653    988    989    Any
            //  655    671    937    945    Ljava/io/IOException;
            //  655    671    988    989    Any
            //  694    700    937    945    Ljava/io/IOException;
            //  694    700    988    989    Any
            //  702    706    937    945    Ljava/io/IOException;
            //  702    706    988    989    Any
            //  708    712    937    945    Ljava/io/IOException;
            //  708    712    988    989    Any
            //  714    723    937    945    Ljava/io/IOException;
            //  714    723    988    989    Any
            //  725    734    937    945    Ljava/io/IOException;
            //  725    734    988    989    Any
            //  751    759    937    945    Ljava/io/IOException;
            //  751    759    988    989    Any
            //  764    771    937    945    Ljava/io/IOException;
            //  764    771    988    989    Any
            //  779    784    937    945    Ljava/io/IOException;
            //  779    784    988    989    Any
            //  784    788    791    801    Ljava/io/IOException;
            //  805    814    937    945    Ljava/io/IOException;
            //  805    814    988    989    Any
            //  816    825    937    945    Ljava/io/IOException;
            //  816    825    988    989    Any
            //  827    837    937    945    Ljava/io/IOException;
            //  827    837    988    989    Any
            //  839    863    937    945    Ljava/io/IOException;
            //  839    863    988    989    Any
            //  863    867    870    880    Ljava/io/IOException;
            //  884    899    937    945    Ljava/io/IOException;
            //  884    899    988    989    Any
            //  899    903    906    916    Ljava/io/IOException;
            //  918    922    925    935    Ljava/io/IOException;
            //  956    965    988    989    Any
            //  969    973    976    986    Ljava/io/IOException;
            //  993    997    1000   1010   Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0120:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            sb.append(", data length:");
            sb.append(this.bytes.length);
            sb.append(")");
            return sb.toString();
        }
    }
    
    static class ExifTag
    {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;
        
        ExifTag(final String name, final int number, final int primaryFormat) {
            this.name = name;
            this.number = number;
            this.primaryFormat = primaryFormat;
            this.secondaryFormat = -1;
        }
        
        ExifTag(final String name, final int number, final int primaryFormat, final int secondaryFormat) {
            this.name = name;
            this.number = number;
            this.primaryFormat = primaryFormat;
            this.secondaryFormat = secondaryFormat;
        }
        
        boolean isFormatCompatible(final int n) {
            final int primaryFormat = this.primaryFormat;
            if (primaryFormat != 7) {
                if (n != 7) {
                    if (primaryFormat != n) {
                        final int secondaryFormat = this.secondaryFormat;
                        if (secondaryFormat != n) {
                            return ((primaryFormat == 4 || secondaryFormat == 4) && n == 3) || ((this.primaryFormat == 9 || this.secondaryFormat == 9) && n == 8) || ((this.primaryFormat == 12 || this.secondaryFormat == 12) && n == 11);
                        }
                    }
                }
            }
            return true;
        }
    }
    
    private static class Rational
    {
        public final long denominator;
        public final long numerator;
        
        Rational(final long numerator, final long denominator) {
            if (denominator == 0L) {
                this.numerator = 0L;
                this.denominator = 1L;
                return;
            }
            this.numerator = numerator;
            this.denominator = denominator;
        }
        
        public double calculate() {
            final double v = (double)this.numerator;
            final double v2 = (double)this.denominator;
            Double.isNaN(v);
            Double.isNaN(v2);
            return v / v2;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.numerator);
            sb.append("/");
            sb.append(this.denominator);
            return sb.toString();
        }
    }
}
