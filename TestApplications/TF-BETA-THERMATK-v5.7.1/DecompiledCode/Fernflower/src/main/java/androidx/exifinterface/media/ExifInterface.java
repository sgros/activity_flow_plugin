package androidx.exifinterface.media;

import android.content.res.AssetManager.AssetInputStream;
import android.util.Log;
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
   static final Charset ASCII;
   public static final int[] BITS_PER_SAMPLE_GREYSCALE_1;
   public static final int[] BITS_PER_SAMPLE_GREYSCALE_2;
   public static final int[] BITS_PER_SAMPLE_RGB;
   static final byte[] EXIF_ASCII_PREFIX;
   private static final ExifInterface.ExifTag[] EXIF_POINTER_TAGS;
   static final ExifInterface.ExifTag[][] EXIF_TAGS;
   private static final List FLIPPED_ROTATION_ORDER;
   static final byte[] IDENTIFIER_EXIF_APP1;
   private static final ExifInterface.ExifTag[] IFD_EXIF_TAGS;
   static final int[] IFD_FORMAT_BYTES_PER_FORMAT;
   static final String[] IFD_FORMAT_NAMES;
   private static final ExifInterface.ExifTag[] IFD_GPS_TAGS;
   private static final ExifInterface.ExifTag[] IFD_INTEROPERABILITY_TAGS;
   private static final ExifInterface.ExifTag[] IFD_THUMBNAIL_TAGS;
   private static final ExifInterface.ExifTag[] IFD_TIFF_TAGS;
   private static final ExifInterface.ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
   private static final ExifInterface.ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
   static final byte[] JPEG_SIGNATURE;
   private static final ExifInterface.ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
   private static final ExifInterface.ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
   private static final byte[] ORF_MAKER_NOTE_HEADER_1;
   private static final byte[] ORF_MAKER_NOTE_HEADER_2;
   private static final ExifInterface.ExifTag[] ORF_MAKER_NOTE_TAGS;
   private static final ExifInterface.ExifTag[] PEF_TAGS;
   private static final List ROTATION_ORDER;
   private static final ExifInterface.ExifTag TAG_RAF_IMAGE_SIZE;
   private static final HashMap sExifPointerTagMap;
   private static final HashMap[] sExifTagMapsForReading;
   private static final HashMap[] sExifTagMapsForWriting;
   private static SimpleDateFormat sFormatter;
   private static final Pattern sGpsTimestampPattern;
   private static final Pattern sNonZeroTimePattern;
   private static final HashSet sTagSetForCompatibility;
   private final AssetInputStream mAssetInputStream;
   private final HashMap[] mAttributes;
   private Set mAttributesOffsets;
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
      Integer var0 = 1;
      Integer var1 = 3;
      Integer var2 = 2;
      Integer var3 = 8;
      ROTATION_ORDER = Arrays.asList(var0, 6, var1, var3);
      Integer var4 = 7;
      Integer var5 = 5;
      FLIPPED_ROTATION_ORDER = Arrays.asList(var2, var4, 4, var5);
      BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
      BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
      BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
      JPEG_SIGNATURE = new byte[]{-1, -40, -1};
      ORF_MAKER_NOTE_HEADER_1 = new byte[]{79, 76, 89, 77, 80, 0};
      ORF_MAKER_NOTE_HEADER_2 = new byte[]{79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
      IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
      IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
      EXIF_ASCII_PREFIX = new byte[]{65, 83, 67, 73, 73, 0, 0, 0};
      IFD_TIFF_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("NewSubfileType", 254, 4), new ExifInterface.ExifTag("SubfileType", 255, 4), new ExifInterface.ExifTag("ImageWidth", 256, 3, 4), new ExifInterface.ExifTag("ImageLength", 257, 3, 4), new ExifInterface.ExifTag("BitsPerSample", 258, 3), new ExifInterface.ExifTag("Compression", 259, 3), new ExifInterface.ExifTag("PhotometricInterpretation", 262, 3), new ExifInterface.ExifTag("ImageDescription", 270, 2), new ExifInterface.ExifTag("Make", 271, 2), new ExifInterface.ExifTag("Model", 272, 2), new ExifInterface.ExifTag("StripOffsets", 273, 3, 4), new ExifInterface.ExifTag("Orientation", 274, 3), new ExifInterface.ExifTag("SamplesPerPixel", 277, 3), new ExifInterface.ExifTag("RowsPerStrip", 278, 3, 4), new ExifInterface.ExifTag("StripByteCounts", 279, 3, 4), new ExifInterface.ExifTag("XResolution", 282, 5), new ExifInterface.ExifTag("YResolution", 283, 5), new ExifInterface.ExifTag("PlanarConfiguration", 284, 3), new ExifInterface.ExifTag("ResolutionUnit", 296, 3), new ExifInterface.ExifTag("TransferFunction", 301, 3), new ExifInterface.ExifTag("Software", 305, 2), new ExifInterface.ExifTag("DateTime", 306, 2), new ExifInterface.ExifTag("Artist", 315, 2), new ExifInterface.ExifTag("WhitePoint", 318, 5), new ExifInterface.ExifTag("PrimaryChromaticities", 319, 5), new ExifInterface.ExifTag("SubIFDPointer", 330, 4), new ExifInterface.ExifTag("JPEGInterchangeFormat", 513, 4), new ExifInterface.ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifInterface.ExifTag("YCbCrCoefficients", 529, 5), new ExifInterface.ExifTag("YCbCrSubSampling", 530, 3), new ExifInterface.ExifTag("YCbCrPositioning", 531, 3), new ExifInterface.ExifTag("ReferenceBlackWhite", 532, 5), new ExifInterface.ExifTag("Copyright", 33432, 2), new ExifInterface.ExifTag("ExifIFDPointer", 34665, 4), new ExifInterface.ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifInterface.ExifTag("SensorTopBorder", 4, 4), new ExifInterface.ExifTag("SensorLeftBorder", 5, 4), new ExifInterface.ExifTag("SensorBottomBorder", 6, 4), new ExifInterface.ExifTag("SensorRightBorder", 7, 4), new ExifInterface.ExifTag("ISO", 23, 3), new ExifInterface.ExifTag("JpgFromRaw", 46, 7)};
      IFD_EXIF_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("ExposureTime", 33434, 5), new ExifInterface.ExifTag("FNumber", 33437, 5), new ExifInterface.ExifTag("ExposureProgram", 34850, 3), new ExifInterface.ExifTag("SpectralSensitivity", 34852, 2), new ExifInterface.ExifTag("PhotographicSensitivity", 34855, 3), new ExifInterface.ExifTag("OECF", 34856, 7), new ExifInterface.ExifTag("ExifVersion", 36864, 2), new ExifInterface.ExifTag("DateTimeOriginal", 36867, 2), new ExifInterface.ExifTag("DateTimeDigitized", 36868, 2), new ExifInterface.ExifTag("ComponentsConfiguration", 37121, 7), new ExifInterface.ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifInterface.ExifTag("ShutterSpeedValue", 37377, 10), new ExifInterface.ExifTag("ApertureValue", 37378, 5), new ExifInterface.ExifTag("BrightnessValue", 37379, 10), new ExifInterface.ExifTag("ExposureBiasValue", 37380, 10), new ExifInterface.ExifTag("MaxApertureValue", 37381, 5), new ExifInterface.ExifTag("SubjectDistance", 37382, 5), new ExifInterface.ExifTag("MeteringMode", 37383, 3), new ExifInterface.ExifTag("LightSource", 37384, 3), new ExifInterface.ExifTag("Flash", 37385, 3), new ExifInterface.ExifTag("FocalLength", 37386, 5), new ExifInterface.ExifTag("SubjectArea", 37396, 3), new ExifInterface.ExifTag("MakerNote", 37500, 7), new ExifInterface.ExifTag("UserComment", 37510, 7), new ExifInterface.ExifTag("SubSecTime", 37520, 2), new ExifInterface.ExifTag("SubSecTimeOriginal", 37521, 2), new ExifInterface.ExifTag("SubSecTimeDigitized", 37522, 2), new ExifInterface.ExifTag("FlashpixVersion", 40960, 7), new ExifInterface.ExifTag("ColorSpace", 40961, 3), new ExifInterface.ExifTag("PixelXDimension", 40962, 3, 4), new ExifInterface.ExifTag("PixelYDimension", 40963, 3, 4), new ExifInterface.ExifTag("RelatedSoundFile", 40964, 2), new ExifInterface.ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifInterface.ExifTag("FlashEnergy", 41483, 5), new ExifInterface.ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifInterface.ExifTag("FocalPlaneXResolution", 41486, 5), new ExifInterface.ExifTag("FocalPlaneYResolution", 41487, 5), new ExifInterface.ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifInterface.ExifTag("SubjectLocation", 41492, 3), new ExifInterface.ExifTag("ExposureIndex", 41493, 5), new ExifInterface.ExifTag("SensingMethod", 41495, 3), new ExifInterface.ExifTag("FileSource", 41728, 7), new ExifInterface.ExifTag("SceneType", 41729, 7), new ExifInterface.ExifTag("CFAPattern", 41730, 7), new ExifInterface.ExifTag("CustomRendered", 41985, 3), new ExifInterface.ExifTag("ExposureMode", 41986, 3), new ExifInterface.ExifTag("WhiteBalance", 41987, 3), new ExifInterface.ExifTag("DigitalZoomRatio", 41988, 5), new ExifInterface.ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifInterface.ExifTag("SceneCaptureType", 41990, 3), new ExifInterface.ExifTag("GainControl", 41991, 3), new ExifInterface.ExifTag("Contrast", 41992, 3), new ExifInterface.ExifTag("Saturation", 41993, 3), new ExifInterface.ExifTag("Sharpness", 41994, 3), new ExifInterface.ExifTag("DeviceSettingDescription", 41995, 7), new ExifInterface.ExifTag("SubjectDistanceRange", 41996, 3), new ExifInterface.ExifTag("ImageUniqueID", 42016, 2), new ExifInterface.ExifTag("DNGVersion", 50706, 1), new ExifInterface.ExifTag("DefaultCropSize", 50720, 3, 4)};
      IFD_GPS_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("GPSVersionID", 0, 1), new ExifInterface.ExifTag("GPSLatitudeRef", 1, 2), new ExifInterface.ExifTag("GPSLatitude", 2, 5), new ExifInterface.ExifTag("GPSLongitudeRef", 3, 2), new ExifInterface.ExifTag("GPSLongitude", 4, 5), new ExifInterface.ExifTag("GPSAltitudeRef", 5, 1), new ExifInterface.ExifTag("GPSAltitude", 6, 5), new ExifInterface.ExifTag("GPSTimeStamp", 7, 5), new ExifInterface.ExifTag("GPSSatellites", 8, 2), new ExifInterface.ExifTag("GPSStatus", 9, 2), new ExifInterface.ExifTag("GPSMeasureMode", 10, 2), new ExifInterface.ExifTag("GPSDOP", 11, 5), new ExifInterface.ExifTag("GPSSpeedRef", 12, 2), new ExifInterface.ExifTag("GPSSpeed", 13, 5), new ExifInterface.ExifTag("GPSTrackRef", 14, 2), new ExifInterface.ExifTag("GPSTrack", 15, 5), new ExifInterface.ExifTag("GPSImgDirectionRef", 16, 2), new ExifInterface.ExifTag("GPSImgDirection", 17, 5), new ExifInterface.ExifTag("GPSMapDatum", 18, 2), new ExifInterface.ExifTag("GPSDestLatitudeRef", 19, 2), new ExifInterface.ExifTag("GPSDestLatitude", 20, 5), new ExifInterface.ExifTag("GPSDestLongitudeRef", 21, 2), new ExifInterface.ExifTag("GPSDestLongitude", 22, 5), new ExifInterface.ExifTag("GPSDestBearingRef", 23, 2), new ExifInterface.ExifTag("GPSDestBearing", 24, 5), new ExifInterface.ExifTag("GPSDestDistanceRef", 25, 2), new ExifInterface.ExifTag("GPSDestDistance", 26, 5), new ExifInterface.ExifTag("GPSProcessingMethod", 27, 7), new ExifInterface.ExifTag("GPSAreaInformation", 28, 7), new ExifInterface.ExifTag("GPSDateStamp", 29, 2), new ExifInterface.ExifTag("GPSDifferential", 30, 3)};
      IFD_INTEROPERABILITY_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("InteroperabilityIndex", 1, 2)};
      IFD_THUMBNAIL_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("NewSubfileType", 254, 4), new ExifInterface.ExifTag("SubfileType", 255, 4), new ExifInterface.ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifInterface.ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifInterface.ExifTag("BitsPerSample", 258, 3), new ExifInterface.ExifTag("Compression", 259, 3), new ExifInterface.ExifTag("PhotometricInterpretation", 262, 3), new ExifInterface.ExifTag("ImageDescription", 270, 2), new ExifInterface.ExifTag("Make", 271, 2), new ExifInterface.ExifTag("Model", 272, 2), new ExifInterface.ExifTag("StripOffsets", 273, 3, 4), new ExifInterface.ExifTag("Orientation", 274, 3), new ExifInterface.ExifTag("SamplesPerPixel", 277, 3), new ExifInterface.ExifTag("RowsPerStrip", 278, 3, 4), new ExifInterface.ExifTag("StripByteCounts", 279, 3, 4), new ExifInterface.ExifTag("XResolution", 282, 5), new ExifInterface.ExifTag("YResolution", 283, 5), new ExifInterface.ExifTag("PlanarConfiguration", 284, 3), new ExifInterface.ExifTag("ResolutionUnit", 296, 3), new ExifInterface.ExifTag("TransferFunction", 301, 3), new ExifInterface.ExifTag("Software", 305, 2), new ExifInterface.ExifTag("DateTime", 306, 2), new ExifInterface.ExifTag("Artist", 315, 2), new ExifInterface.ExifTag("WhitePoint", 318, 5), new ExifInterface.ExifTag("PrimaryChromaticities", 319, 5), new ExifInterface.ExifTag("SubIFDPointer", 330, 4), new ExifInterface.ExifTag("JPEGInterchangeFormat", 513, 4), new ExifInterface.ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifInterface.ExifTag("YCbCrCoefficients", 529, 5), new ExifInterface.ExifTag("YCbCrSubSampling", 530, 3), new ExifInterface.ExifTag("YCbCrPositioning", 531, 3), new ExifInterface.ExifTag("ReferenceBlackWhite", 532, 5), new ExifInterface.ExifTag("Copyright", 33432, 2), new ExifInterface.ExifTag("ExifIFDPointer", 34665, 4), new ExifInterface.ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifInterface.ExifTag("DNGVersion", 50706, 1), new ExifInterface.ExifTag("DefaultCropSize", 50720, 3, 4)};
      TAG_RAF_IMAGE_SIZE = new ExifInterface.ExifTag("StripOffsets", 273, 3);
      ORF_MAKER_NOTE_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("ThumbnailImage", 256, 7), new ExifInterface.ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifInterface.ExifTag("ImageProcessingIFDPointer", 8256, 4)};
      ORF_CAMERA_SETTINGS_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("PreviewImageStart", 257, 4), new ExifInterface.ExifTag("PreviewImageLength", 258, 4)};
      ORF_IMAGE_PROCESSING_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("AspectFrame", 4371, 3)};
      PEF_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("ColorSpace", 55, 3)};
      ExifInterface.ExifTag[] var6 = IFD_TIFF_TAGS;
      EXIF_TAGS = new ExifInterface.ExifTag[][]{var6, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, var6, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, PEF_TAGS};
      EXIF_POINTER_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("SubIFDPointer", 330, 4), new ExifInterface.ExifTag("ExifIFDPointer", 34665, 4), new ExifInterface.ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifInterface.ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifInterface.ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifInterface.ExifTag("ImageProcessingIFDPointer", 8256, 1)};
      JPEG_INTERCHANGE_FORMAT_TAG = new ExifInterface.ExifTag("JPEGInterchangeFormat", 513, 4);
      JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifInterface.ExifTag("JPEGInterchangeFormatLength", 514, 4);
      ExifInterface.ExifTag[][] var11 = EXIF_TAGS;
      sExifTagMapsForReading = new HashMap[var11.length];
      sExifTagMapsForWriting = new HashMap[var11.length];
      sTagSetForCompatibility = new HashSet(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
      sExifPointerTagMap = new HashMap();
      ASCII = Charset.forName("US-ASCII");
      IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
      sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
      sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

      for(int var7 = 0; var7 < EXIF_TAGS.length; ++var7) {
         sExifTagMapsForReading[var7] = new HashMap();
         sExifTagMapsForWriting[var7] = new HashMap();
         ExifInterface.ExifTag[] var8 = EXIF_TAGS[var7];
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            ExifInterface.ExifTag var12 = var8[var10];
            sExifTagMapsForReading[var7].put(var12.number, var12);
            sExifTagMapsForWriting[var7].put(var12.name, var12);
         }
      }

      sExifPointerTagMap.put(EXIF_POINTER_TAGS[0].number, var5);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[1].number, var0);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[2].number, var2);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[3].number, var1);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[4].number, var4);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[5].number, var3);
      sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
      sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
   }

   public ExifInterface(InputStream var1) throws IOException {
      ExifInterface.ExifTag[][] var2 = EXIF_TAGS;
      this.mAttributes = new HashMap[var2.length];
      this.mAttributesOffsets = new HashSet(var2.length);
      this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
      if (var1 != null) {
         this.mFilename = null;
         if (var1 instanceof AssetInputStream) {
            this.mAssetInputStream = (AssetInputStream)var1;
         } else {
            this.mAssetInputStream = null;
         }

         this.loadAttributes(var1);
      } else {
         throw new IllegalArgumentException("inputStream cannot be null");
      }
   }

   public ExifInterface(String var1) throws IOException {
      ExifInterface.ExifTag[][] var2 = EXIF_TAGS;
      this.mAttributes = new HashMap[var2.length];
      this.mAttributesOffsets = new HashSet(var2.length);
      this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
      if (var1 != null) {
         var2 = null;
         this.mAssetInputStream = null;
         this.mFilename = var1;
         boolean var8 = false;

         FileInputStream var3;
         try {
            var8 = true;
            var3 = new FileInputStream(var1);
            var8 = false;
         } finally {
            if (var8) {
               closeQuietly(var2);
            }
         }

         try {
            this.loadAttributes(var3);
         } finally {
            ;
         }

         closeQuietly(var3);
      } else {
         throw new IllegalArgumentException("filename cannot be null");
      }
   }

   private void addDefaultValuesForCompatibility() {
      String var1 = this.getAttribute("DateTimeOriginal");
      if (var1 != null && this.getAttribute("DateTime") == null) {
         this.mAttributes[0].put("DateTime", ExifInterface.ExifAttribute.createString(var1));
      }

      if (this.getAttribute("ImageWidth") == null) {
         this.mAttributes[0].put("ImageWidth", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.getAttribute("ImageLength") == null) {
         this.mAttributes[0].put("ImageLength", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.getAttribute("Orientation") == null) {
         this.mAttributes[0].put("Orientation", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.getAttribute("LightSource") == null) {
         this.mAttributes[1].put("LightSource", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

   }

   private static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   private static long[] convertToLongArray(Object var0) {
      if (!(var0 instanceof int[])) {
         return var0 instanceof long[] ? (long[])var0 : null;
      } else {
         int[] var1 = (int[])var0;
         long[] var3 = new long[var1.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var3[var2] = (long)var1[var2];
         }

         return var3;
      }
   }

   private ExifInterface.ExifAttribute getExifAttribute(String var1) {
      String var2 = var1;
      if ("ISOSpeedRatings".equals(var1)) {
         var2 = "PhotographicSensitivity";
      }

      for(int var3 = 0; var3 < EXIF_TAGS.length; ++var3) {
         ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[var3].get(var2);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   private void getJpegAttributes(ExifInterface.ByteOrderedDataInputStream var1, int var2, int var3) throws IOException {
      var1.setByteOrder(ByteOrder.BIG_ENDIAN);
      var1.seek((long)var2);
      byte var4 = var1.readByte();
      StringBuilder var8;
      if (var4 != -1) {
         var8 = new StringBuilder();
         var8.append("Invalid marker: ");
         var8.append(Integer.toHexString(var4 & 255));
         throw new IOException(var8.toString());
      } else if (var1.readByte() != -40) {
         var8 = new StringBuilder();
         var8.append("Invalid marker: ");
         var8.append(Integer.toHexString(var4 & 255));
         throw new IOException(var8.toString());
      } else {
         var2 = var2 + 1 + 1;

         while(true) {
            var4 = var1.readByte();
            if (var4 != -1) {
               var8 = new StringBuilder();
               var8.append("Invalid marker:");
               var8.append(Integer.toHexString(var4 & 255));
               throw new IOException(var8.toString());
            }

            byte var5 = var1.readByte();
            if (var5 == -39 || var5 == -38) {
               var1.setByteOrder(this.mExifByteOrder);
               return;
            }

            int var9 = var1.readUnsignedShort() - 2;
            int var6 = var2 + 1 + 1 + 2;
            if (var9 < 0) {
               throw new IOException("Invalid length");
            }

            label94: {
               byte[] var7;
               if (var5 != -31) {
                  if (var5 != -2) {
                     switch(var5) {
                     default:
                        switch(var5) {
                        case -59:
                        case -58:
                        case -57:
                           break;
                        default:
                           switch(var5) {
                           case -55:
                           case -54:
                           case -53:
                              break;
                           default:
                              switch(var5) {
                              case -51:
                              case -50:
                              case -49:
                                 break;
                              default:
                                 var2 = var9;
                                 var9 = var6;
                                 break label94;
                              }
                           }
                        }
                     case -64:
                     case -63:
                     case -62:
                     case -61:
                        if (var1.skipBytes(1) != 1) {
                           throw new IOException("Invalid SOFx");
                        }

                        this.mAttributes[var3].put("ImageLength", ExifInterface.ExifAttribute.createULong((long)var1.readUnsignedShort(), this.mExifByteOrder));
                        this.mAttributes[var3].put("ImageWidth", ExifInterface.ExifAttribute.createULong((long)var1.readUnsignedShort(), this.mExifByteOrder));
                        var2 = var9 - 5;
                        var9 = var6;
                        break label94;
                     }
                  }

                  var7 = new byte[var9];
                  if (var1.read(var7) != var9) {
                     throw new IOException("Invalid exif");
                  }

                  var9 = var6;
                  if (this.getAttribute("UserComment") == null) {
                     this.mAttributes[1].put("UserComment", ExifInterface.ExifAttribute.createString(new String(var7, ASCII)));
                     var9 = var6;
                  }
               } else {
                  if (var9 < 6) {
                     var2 = var9;
                     var9 = var6;
                     break label94;
                  }

                  var7 = new byte[6];
                  if (var1.read(var7) != 6) {
                     throw new IOException("Invalid exif");
                  }

                  var6 += 6;
                  var2 = var9 - 6;
                  if (!Arrays.equals(var7, IDENTIFIER_EXIF_APP1)) {
                     var9 = var6;
                     break label94;
                  }

                  if (var2 <= 0) {
                     throw new IOException("Invalid exif");
                  }

                  this.mExifOffset = var6;
                  var7 = new byte[var2];
                  if (var1.read(var7) != var2) {
                     throw new IOException("Invalid exif");
                  }

                  var9 = var6 + var2;
                  this.readExifSegment(var7, var3);
               }

               var2 = 0;
            }

            if (var2 < 0) {
               throw new IOException("Invalid length");
            }

            if (var1.skipBytes(var2) != var2) {
               throw new IOException("Invalid JPEG segment");
            }

            var2 += var9;
         }
      }
   }

   private int getMimeType(BufferedInputStream var1) throws IOException {
      var1.mark(5000);
      byte[] var2 = new byte[5000];
      var1.read(var2);
      var1.reset();
      if (isJpegFormat(var2)) {
         return 4;
      } else if (this.isRafFormat(var2)) {
         return 9;
      } else if (this.isOrfFormat(var2)) {
         return 7;
      } else {
         return this.isRw2Format(var2) ? 10 : 0;
      }
   }

   private void getOrfAttributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      this.getRawAttributes(var1);
      ExifInterface.ExifAttribute var8 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("MakerNote");
      if (var8 != null) {
         var1 = new ExifInterface.ByteOrderedDataInputStream(var8.bytes);
         var1.setByteOrder(this.mExifByteOrder);
         byte[] var2 = new byte[ORF_MAKER_NOTE_HEADER_1.length];
         var1.readFully(var2);
         var1.seek(0L);
         byte[] var3 = new byte[ORF_MAKER_NOTE_HEADER_2.length];
         var1.readFully(var3);
         if (Arrays.equals(var2, ORF_MAKER_NOTE_HEADER_1)) {
            var1.seek(8L);
         } else if (Arrays.equals(var3, ORF_MAKER_NOTE_HEADER_2)) {
            var1.seek(12L);
         }

         this.readImageFileDirectory(var1, 6);
         var8 = (ExifInterface.ExifAttribute)this.mAttributes[7].get("PreviewImageStart");
         ExifInterface.ExifAttribute var9 = (ExifInterface.ExifAttribute)this.mAttributes[7].get("PreviewImageLength");
         if (var8 != null && var9 != null) {
            this.mAttributes[5].put("JPEGInterchangeFormat", var8);
            this.mAttributes[5].put("JPEGInterchangeFormatLength", var9);
         }

         var8 = (ExifInterface.ExifAttribute)this.mAttributes[8].get("AspectFrame");
         if (var8 != null) {
            int[] var10 = (int[])var8.getValue(this.mExifByteOrder);
            if (var10 != null && var10.length == 4) {
               if (var10[2] > var10[0] && var10[3] > var10[1]) {
                  int var4 = var10[2] - var10[0] + 1;
                  int var5 = var10[3] - var10[1] + 1;
                  int var6 = var4;
                  int var7 = var5;
                  if (var4 < var5) {
                     var6 = var4 + var5;
                     var7 = var6 - var5;
                     var6 -= var7;
                  }

                  var9 = ExifInterface.ExifAttribute.createUShort(var6, this.mExifByteOrder);
                  var8 = ExifInterface.ExifAttribute.createUShort(var7, this.mExifByteOrder);
                  this.mAttributes[0].put("ImageWidth", var9);
                  this.mAttributes[0].put("ImageLength", var8);
               }
            } else {
               StringBuilder var11 = new StringBuilder();
               var11.append("Invalid aspect frame values. frame=");
               var11.append(Arrays.toString(var10));
               Log.w("ExifInterface", var11.toString());
            }
         }
      }

   }

   private void getRafAttributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      var1.skipBytes(84);
      byte[] var2 = new byte[4];
      byte[] var3 = new byte[4];
      var1.read(var2);
      var1.skipBytes(4);
      var1.read(var3);
      int var4 = ByteBuffer.wrap(var2).getInt();
      int var5 = ByteBuffer.wrap(var3).getInt();
      this.getJpegAttributes(var1, var4, 5);
      var1.seek((long)var5);
      var1.setByteOrder(ByteOrder.BIG_ENDIAN);
      var5 = var1.readInt();

      for(var4 = 0; var4 < var5; ++var4) {
         int var6 = var1.readUnsignedShort();
         int var7 = var1.readUnsignedShort();
         if (var6 == TAG_RAF_IMAGE_SIZE.number) {
            short var11 = var1.readShort();
            short var10 = var1.readShort();
            ExifInterface.ExifAttribute var8 = ExifInterface.ExifAttribute.createUShort(var11, this.mExifByteOrder);
            ExifInterface.ExifAttribute var9 = ExifInterface.ExifAttribute.createUShort(var10, this.mExifByteOrder);
            this.mAttributes[0].put("ImageLength", var8);
            this.mAttributes[0].put("ImageWidth", var9);
            return;
         }

         var1.skipBytes(var7);
      }

   }

   private void getRawAttributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      this.parseTiffHeaders(var1, var1.available());
      this.readImageFileDirectory(var1, 0);
      this.updateImageSizeValues(var1, 0);
      this.updateImageSizeValues(var1, 5);
      this.updateImageSizeValues(var1, 4);
      this.validateImages(var1);
      if (this.mMimeType == 8) {
         ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("MakerNote");
         if (var2 != null) {
            var1 = new ExifInterface.ByteOrderedDataInputStream(var2.bytes);
            var1.setByteOrder(this.mExifByteOrder);
            var1.seek(6L);
            this.readImageFileDirectory(var1, 9);
            var2 = (ExifInterface.ExifAttribute)this.mAttributes[9].get("ColorSpace");
            if (var2 != null) {
               this.mAttributes[1].put("ColorSpace", var2);
            }
         }
      }

   }

   private void getRw2Attributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      this.getRawAttributes(var1);
      if ((ExifInterface.ExifAttribute)this.mAttributes[0].get("JpgFromRaw") != null) {
         this.getJpegAttributes(var1, this.mRw2JpgFromRawOffset, 5);
      }

      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[0].get("ISO");
      ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("PhotographicSensitivity");
      if (var3 != null && var2 == null) {
         this.mAttributes[1].put("PhotographicSensitivity", var3);
      }

   }

   private void handleThumbnailFromJfif(ExifInterface.ByteOrderedDataInputStream var1, HashMap var2) throws IOException {
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)var2.get("JPEGInterchangeFormat");
      ExifInterface.ExifAttribute var8 = (ExifInterface.ExifAttribute)var2.get("JPEGInterchangeFormatLength");
      if (var3 != null && var8 != null) {
         int var5;
         int var7;
         label37: {
            int var4 = var3.getIntValue(this.mExifByteOrder);
            var5 = Math.min(var8.getIntValue(this.mExifByteOrder), var1.available() - var4);
            int var6 = this.mMimeType;
            if (var6 != 4 && var6 != 9 && var6 != 10) {
               var7 = var4;
               if (var6 != 7) {
                  break label37;
               }

               var7 = this.mOrfMakerNoteOffset;
            } else {
               var7 = this.mExifOffset;
            }

            var7 += var4;
         }

         if (var7 > 0 && var5 > 0) {
            this.mHasThumbnail = true;
            this.mThumbnailOffset = var7;
            this.mThumbnailLength = var5;
            if (this.mFilename == null && this.mAssetInputStream == null) {
               byte[] var9 = new byte[var5];
               var1.seek((long)var7);
               var1.readFully(var9);
               this.mThumbnailBytes = var9;
            }
         }
      }

   }

   private void handleThumbnailFromStrips(ExifInterface.ByteOrderedDataInputStream var1, HashMap var2) throws IOException {
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)var2.get("StripOffsets");
      ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)var2.get("StripByteCounts");
      if (var3 != null && var4 != null) {
         long[] var13 = convertToLongArray(var3.getValue(this.mExifByteOrder));
         long[] var15 = convertToLongArray(var4.getValue(this.mExifByteOrder));
         if (var13 == null) {
            Log.w("ExifInterface", "stripOffsets should not be null.");
            return;
         }

         if (var15 == null) {
            Log.w("ExifInterface", "stripByteCounts should not be null.");
            return;
         }

         int var5 = var15.length;
         long var6 = 0L;

         int var8;
         for(var8 = 0; var8 < var5; ++var8) {
            var6 += var15[var8];
         }

         byte[] var14 = new byte[(int)var6];
         int var9 = 0;
         var5 = 0;

         for(var8 = 0; var9 < var13.length; ++var9) {
            int var10 = (int)var13[var9];
            int var11 = (int)var15[var9];
            var10 -= var5;
            if (var10 < 0) {
               Log.d("ExifInterface", "Invalid strip offset value");
            }

            var1.seek((long)var10);
            byte[] var12 = new byte[var11];
            var1.read(var12);
            var5 = var5 + var10 + var11;
            System.arraycopy(var12, 0, var14, var8, var12.length);
            var8 += var12.length;
         }

         this.mHasThumbnail = true;
         this.mThumbnailBytes = var14;
         this.mThumbnailLength = var14.length;
      }

   }

   private static boolean isJpegFormat(byte[] var0) throws IOException {
      int var1 = 0;

      while(true) {
         byte[] var2 = JPEG_SIGNATURE;
         if (var1 >= var2.length) {
            return true;
         }

         if (var0[var1] != var2[var1]) {
            return false;
         }

         ++var1;
      }
   }

   private boolean isOrfFormat(byte[] var1) throws IOException {
      ExifInterface.ByteOrderedDataInputStream var4 = new ExifInterface.ByteOrderedDataInputStream(var1);
      this.mExifByteOrder = this.readByteOrder(var4);
      var4.setByteOrder(this.mExifByteOrder);
      short var2 = var4.readShort();
      var4.close();
      boolean var3;
      if (var2 != 20306 && var2 != 21330) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   private boolean isRafFormat(byte[] var1) throws IOException {
      byte[] var2 = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1[var3] != var2[var3]) {
            return false;
         }
      }

      return true;
   }

   private boolean isRw2Format(byte[] var1) throws IOException {
      ExifInterface.ByteOrderedDataInputStream var4 = new ExifInterface.ByteOrderedDataInputStream(var1);
      this.mExifByteOrder = this.readByteOrder(var4);
      var4.setByteOrder(this.mExifByteOrder);
      short var2 = var4.readShort();
      var4.close();
      boolean var3;
      if (var2 == 85) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private boolean isSupportedDataType(HashMap var1) throws IOException {
      ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)var1.get("BitsPerSample");
      if (var2 != null) {
         int[] var5 = (int[])var2.getValue(this.mExifByteOrder);
         if (Arrays.equals(BITS_PER_SAMPLE_RGB, var5)) {
            return true;
         }

         if (this.mMimeType == 3) {
            ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)var1.get("PhotometricInterpretation");
            if (var4 != null) {
               int var3 = var4.getIntValue(this.mExifByteOrder);
               if (var3 == 1 && Arrays.equals(var5, BITS_PER_SAMPLE_GREYSCALE_2) || var3 == 6 && Arrays.equals(var5, BITS_PER_SAMPLE_RGB)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean isThumbnail(HashMap var1) throws IOException {
      ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)var1.get("ImageLength");
      ExifInterface.ExifAttribute var5 = (ExifInterface.ExifAttribute)var1.get("ImageWidth");
      if (var2 != null && var5 != null) {
         int var3 = var2.getIntValue(this.mExifByteOrder);
         int var4 = var5.getIntValue(this.mExifByteOrder);
         if (var3 <= 512 && var4 <= 512) {
            return true;
         }
      }

      return false;
   }

   private void loadAttributes(InputStream param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void parseTiffHeaders(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      this.mExifByteOrder = this.readByteOrder(var1);
      var1.setByteOrder(this.mExifByteOrder);
      int var3 = var1.readUnsignedShort();
      int var4 = this.mMimeType;
      StringBuilder var5;
      if (var4 != 7 && var4 != 10 && var3 != 42) {
         var5 = new StringBuilder();
         var5.append("Invalid start code: ");
         var5.append(Integer.toHexString(var3));
         throw new IOException(var5.toString());
      } else {
         var3 = var1.readInt();
         if (var3 >= 8 && var3 < var2) {
            var2 = var3 - 8;
            if (var2 > 0 && var1.skipBytes(var2) != var2) {
               var5 = new StringBuilder();
               var5.append("Couldn't jump to first Ifd: ");
               var5.append(var2);
               throw new IOException(var5.toString());
            }
         } else {
            var5 = new StringBuilder();
            var5.append("Invalid first Ifd offset: ");
            var5.append(var3);
            throw new IOException(var5.toString());
         }
      }
   }

   private ByteOrder readByteOrder(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      short var2 = var1.readShort();
      if (var2 != 18761) {
         if (var2 == 19789) {
            return ByteOrder.BIG_ENDIAN;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Invalid byte order: ");
            var3.append(Integer.toHexString(var2));
            throw new IOException(var3.toString());
         }
      } else {
         return ByteOrder.LITTLE_ENDIAN;
      }
   }

   private void readExifSegment(byte[] var1, int var2) throws IOException {
      ExifInterface.ByteOrderedDataInputStream var3 = new ExifInterface.ByteOrderedDataInputStream(var1);
      this.parseTiffHeaders(var3, var1.length);
      this.readImageFileDirectory(var3, var2);
   }

   private void readImageFileDirectory(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      this.mAttributesOffsets.add(var1.mPosition);
      if (var1.mPosition + 2 <= var1.mLength) {
         short var3 = var1.readShort();
         if (var1.mPosition + var3 * 12 <= var1.mLength && var3 > 0) {
            long var14;
            for(short var4 = 0; var4 < var3; ++var4) {
               int var6;
               int var7;
               int var8;
               long var9;
               ExifInterface.ExifTag var11;
               int var13;
               boolean var22;
               label120: {
                  label119: {
                     var6 = var1.readUnsignedShort();
                     var7 = var1.readUnsignedShort();
                     var8 = var1.readInt();
                     var9 = (long)var1.peek() + 4L;
                     var11 = (ExifInterface.ExifTag)sExifTagMapsForReading[var2].get(var6);
                     StringBuilder var12;
                     if (var11 == null) {
                        var12 = new StringBuilder();
                        var12.append("Skip the tag entry since tag number is not defined: ");
                        var12.append(var6);
                        Log.w("ExifInterface", var12.toString());
                     } else if (var7 > 0 && var7 < IFD_FORMAT_BYTES_PER_FORMAT.length) {
                        if (var11.isFormatCompatible(var7)) {
                           var13 = var7;
                           if (var7 == 7) {
                              var13 = var11.primaryFormat;
                           }

                           var14 = (long)var8 * (long)IFD_FORMAT_BYTES_PER_FORMAT[var13];
                           if (var14 >= 0L && var14 <= 2147483647L) {
                              var22 = true;
                              break label120;
                           }

                           var12 = new StringBuilder();
                           var12.append("Skip the tag entry since the number of components is invalid: ");
                           var12.append(var8);
                           Log.w("ExifInterface", var12.toString());
                           break label119;
                        }

                        var12 = new StringBuilder();
                        var12.append("Skip the tag entry since data format (");
                        var12.append(IFD_FORMAT_NAMES[var7]);
                        var12.append(") is unexpected for tag: ");
                        var12.append(var11.name);
                        Log.w("ExifInterface", var12.toString());
                     } else {
                        var12 = new StringBuilder();
                        var12.append("Skip the tag entry since data format is invalid: ");
                        var12.append(var7);
                        Log.w("ExifInterface", var12.toString());
                     }

                     var14 = 0L;
                     var13 = var7;
                  }

                  var22 = false;
               }

               if (!var22) {
                  var1.seek(var9);
               } else {
                  StringBuilder var23;
                  ExifInterface.ExifAttribute var24;
                  if (var14 > 4L) {
                     var7 = var1.readInt();
                     int var16 = this.mMimeType;
                     if (var16 == 7) {
                        if ("MakerNote".equals(var11.name)) {
                           this.mOrfMakerNoteOffset = var7;
                        } else if (var2 == 6 && "ThumbnailImage".equals(var11.name)) {
                           this.mOrfThumbnailOffset = var7;
                           this.mOrfThumbnailLength = var8;
                           var24 = ExifInterface.ExifAttribute.createUShort(6, this.mExifByteOrder);
                           ExifInterface.ExifAttribute var17 = ExifInterface.ExifAttribute.createULong((long)this.mOrfThumbnailOffset, this.mExifByteOrder);
                           ExifInterface.ExifAttribute var18 = ExifInterface.ExifAttribute.createULong((long)this.mOrfThumbnailLength, this.mExifByteOrder);
                           this.mAttributes[4].put("Compression", var24);
                           this.mAttributes[4].put("JPEGInterchangeFormat", var17);
                           this.mAttributes[4].put("JPEGInterchangeFormatLength", var18);
                        }
                     } else if (var16 == 10 && "JpgFromRaw".equals(var11.name)) {
                        this.mRw2JpgFromRawOffset = var7;
                     }

                     long var19 = (long)var7;
                     if (var19 + var14 > (long)var1.mLength) {
                        var23 = new StringBuilder();
                        var23.append("Skip the tag entry since data offset is invalid: ");
                        var23.append(var7);
                        Log.w("ExifInterface", var23.toString());
                        var1.seek(var9);
                        continue;
                     }

                     var1.seek(var19);
                  }

                  Integer var25 = (Integer)sExifPointerTagMap.get(var6);
                  if (var25 != null) {
                     label140: {
                        var14 = -1L;
                        if (var13 != 3) {
                           if (var13 == 4) {
                              var14 = var1.readUnsignedInt();
                              break label140;
                           }

                           if (var13 != 8) {
                              if (var13 != 9 && var13 != 13) {
                                 break label140;
                              }

                              var13 = var1.readInt();
                           } else {
                              var13 = var1.readShort();
                           }
                        } else {
                           var13 = var1.readUnsignedShort();
                        }

                        var14 = (long)var13;
                     }

                     if (var14 > 0L && var14 < (long)var1.mLength) {
                        if (!this.mAttributesOffsets.contains((int)var14)) {
                           var1.seek(var14);
                           this.readImageFileDirectory(var1, var25);
                        } else {
                           var23 = new StringBuilder();
                           var23.append("Skip jump into the IFD since it has already been read: IfdType ");
                           var23.append(var25);
                           var23.append(" (at ");
                           var23.append(var14);
                           var23.append(")");
                           Log.w("ExifInterface", var23.toString());
                        }
                     } else {
                        var23 = new StringBuilder();
                        var23.append("Skip jump into the IFD since its offset is invalid: ");
                        var23.append(var14);
                        Log.w("ExifInterface", var23.toString());
                     }

                     var1.seek(var9);
                  } else {
                     byte[] var26 = new byte[(int)var14];
                     var1.readFully(var26);
                     var24 = new ExifInterface.ExifAttribute(var13, var8, var26);
                     this.mAttributes[var2].put(var11.name, var24);
                     if ("DNGVersion".equals(var11.name)) {
                        this.mMimeType = 3;
                     }

                     if (("Make".equals(var11.name) || "Model".equals(var11.name)) && var24.getStringValue(this.mExifByteOrder).contains("PENTAX") || "Compression".equals(var11.name) && var24.getIntValue(this.mExifByteOrder) == 65535) {
                        this.mMimeType = 8;
                     }

                     if ((long)var1.peek() != var9) {
                        var1.seek(var9);
                     }
                  }
               }
            }

            if (var1.peek() + 4 <= var1.mLength) {
               var2 = var1.readInt();
               var14 = (long)var2;
               StringBuilder var21;
               if (var14 > 0L && var2 < var1.mLength) {
                  if (!this.mAttributesOffsets.contains(var2)) {
                     var1.seek(var14);
                     if (this.mAttributes[4].isEmpty()) {
                        this.readImageFileDirectory(var1, 4);
                     } else if (this.mAttributes[5].isEmpty()) {
                        this.readImageFileDirectory(var1, 5);
                     }
                  } else {
                     var21 = new StringBuilder();
                     var21.append("Stop reading file since re-reading an IFD may cause an infinite loop: ");
                     var21.append(var2);
                     Log.w("ExifInterface", var21.toString());
                  }
               } else {
                  var21 = new StringBuilder();
                  var21.append("Stop reading file since a wrong offset may cause an infinite loop: ");
                  var21.append(var2);
                  Log.w("ExifInterface", var21.toString());
               }
            }
         }

      }
   }

   private void retrieveJpegImageSize(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageLength");
      ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageWidth");
      if (var3 == null || var4 == null) {
         var4 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("JPEGInterchangeFormat");
         if (var4 != null) {
            this.getJpegAttributes(var1, var4.getIntValue(this.mExifByteOrder), var2);
         }
      }

   }

   private void setThumbnailData(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      HashMap var2 = this.mAttributes[4];
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)var2.get("Compression");
      if (var3 != null) {
         this.mThumbnailCompression = var3.getIntValue(this.mExifByteOrder);
         int var4 = this.mThumbnailCompression;
         if (var4 != 1) {
            if (var4 == 6) {
               this.handleThumbnailFromJfif(var1, var2);
               return;
            }

            if (var4 != 7) {
               return;
            }
         }

         if (this.isSupportedDataType(var2)) {
            this.handleThumbnailFromStrips(var1, var2);
         }
      } else {
         this.mThumbnailCompression = 6;
         this.handleThumbnailFromJfif(var1, var2);
      }

   }

   private void swapBasedOnImageSize(int var1, int var2) throws IOException {
      if (!this.mAttributes[var1].isEmpty() && !this.mAttributes[var2].isEmpty()) {
         ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[var1].get("ImageLength");
         ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[var1].get("ImageWidth");
         ExifInterface.ExifAttribute var5 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageLength");
         ExifInterface.ExifAttribute var6 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageWidth");
         if (var3 != null && var4 != null && var5 != null && var6 != null) {
            int var7 = var3.getIntValue(this.mExifByteOrder);
            int var8 = var4.getIntValue(this.mExifByteOrder);
            int var9 = var5.getIntValue(this.mExifByteOrder);
            int var10 = var6.getIntValue(this.mExifByteOrder);
            if (var7 < var9 && var8 < var10) {
               HashMap[] var11 = this.mAttributes;
               HashMap var12 = var11[var1];
               var11[var1] = var11[var2];
               var11[var2] = var12;
            }
         }
      }

   }

   private void updateImageSizeValues(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("DefaultCropSize");
      ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorTopBorder");
      ExifInterface.ExifAttribute var5 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorLeftBorder");
      ExifInterface.ExifAttribute var6 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorBottomBorder");
      ExifInterface.ExifAttribute var7 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorRightBorder");
      ExifInterface.ExifAttribute var13;
      if (var3 == null) {
         if (var4 != null && var5 != null && var6 != null && var7 != null) {
            int var8 = var4.getIntValue(this.mExifByteOrder);
            int var9 = var6.getIntValue(this.mExifByteOrder);
            int var10 = var7.getIntValue(this.mExifByteOrder);
            int var11 = var5.getIntValue(this.mExifByteOrder);
            if (var9 > var8 && var10 > var11) {
               var4 = ExifInterface.ExifAttribute.createUShort(var9 - var8, this.mExifByteOrder);
               var13 = ExifInterface.ExifAttribute.createUShort(var10 - var11, this.mExifByteOrder);
               this.mAttributes[var2].put("ImageLength", var4);
               this.mAttributes[var2].put("ImageWidth", var13);
            }
         } else {
            this.retrieveJpegImageSize(var1, var2);
         }
      } else {
         StringBuilder var15;
         if (var3.format == 5) {
            ExifInterface.Rational[] var14 = (ExifInterface.Rational[])var3.getValue(this.mExifByteOrder);
            if (var14 == null || var14.length != 2) {
               var15 = new StringBuilder();
               var15.append("Invalid crop size values. cropSize=");
               var15.append(Arrays.toString(var14));
               Log.w("ExifInterface", var15.toString());
               return;
            }

            var4 = ExifInterface.ExifAttribute.createURational(var14[0], this.mExifByteOrder);
            var13 = ExifInterface.ExifAttribute.createURational(var14[1], this.mExifByteOrder);
         } else {
            int[] var12 = (int[])var3.getValue(this.mExifByteOrder);
            if (var12 == null || var12.length != 2) {
               var15 = new StringBuilder();
               var15.append("Invalid crop size values. cropSize=");
               var15.append(Arrays.toString(var12));
               Log.w("ExifInterface", var15.toString());
               return;
            }

            var4 = ExifInterface.ExifAttribute.createUShort(var12[0], this.mExifByteOrder);
            var13 = ExifInterface.ExifAttribute.createUShort(var12[1], this.mExifByteOrder);
         }

         this.mAttributes[var2].put("ImageWidth", var4);
         this.mAttributes[var2].put("ImageLength", var13);
      }

   }

   private void validateImages(InputStream var1) throws IOException {
      this.swapBasedOnImageSize(0, 5);
      this.swapBasedOnImageSize(0, 4);
      this.swapBasedOnImageSize(5, 4);
      ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("PixelXDimension");
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("PixelYDimension");
      if (var2 != null && var3 != null) {
         this.mAttributes[0].put("ImageWidth", var2);
         this.mAttributes[0].put("ImageLength", var3);
      }

      if (this.mAttributes[4].isEmpty() && this.isThumbnail(this.mAttributes[5])) {
         HashMap[] var4 = this.mAttributes;
         var4[4] = var4[5];
         var4[5] = new HashMap();
      }

      if (!this.isThumbnail(this.mAttributes[4])) {
         Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
      }

   }

   public String getAttribute(String var1) {
      ExifInterface.ExifAttribute var2 = this.getExifAttribute(var1);
      if (var2 != null) {
         if (!sTagSetForCompatibility.contains(var1)) {
            return var2.getStringValue(this.mExifByteOrder);
         }

         if (var1.equals("GPSTimeStamp")) {
            int var3 = var2.format;
            StringBuilder var5;
            if (var3 != 5 && var3 != 10) {
               var5 = new StringBuilder();
               var5.append("GPS Timestamp format is not rational. format=");
               var5.append(var2.format);
               Log.w("ExifInterface", var5.toString());
               return null;
            }

            ExifInterface.Rational[] var6 = (ExifInterface.Rational[])var2.getValue(this.mExifByteOrder);
            if (var6 != null && var6.length == 3) {
               return String.format("%02d:%02d:%02d", (int)((float)var6[0].numerator / (float)var6[0].denominator), (int)((float)var6[1].numerator / (float)var6[1].denominator), (int)((float)var6[2].numerator / (float)var6[2].denominator));
            }

            var5 = new StringBuilder();
            var5.append("Invalid GPS Timestamp array. array=");
            var5.append(Arrays.toString(var6));
            Log.w("ExifInterface", var5.toString());
            return null;
         }

         try {
            var1 = Double.toString(var2.getDoubleValue(this.mExifByteOrder));
            return var1;
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   public int getAttributeInt(String var1, int var2) {
      ExifInterface.ExifAttribute var5 = this.getExifAttribute(var1);
      if (var5 == null) {
         return var2;
      } else {
         try {
            int var3 = var5.getIntValue(this.mExifByteOrder);
            return var3;
         } catch (NumberFormatException var4) {
            return var2;
         }
      }
   }

   private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
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

      public ByteOrderedDataInputStream(InputStream var1) throws IOException {
         this.mByteOrder = ByteOrder.BIG_ENDIAN;
         this.mDataInputStream = new DataInputStream(var1);
         this.mLength = this.mDataInputStream.available();
         this.mPosition = 0;
         this.mDataInputStream.mark(this.mLength);
      }

      public ByteOrderedDataInputStream(byte[] var1) throws IOException {
         this((InputStream)(new ByteArrayInputStream(var1)));
      }

      public int available() throws IOException {
         return this.mDataInputStream.available();
      }

      public int peek() {
         return this.mPosition;
      }

      public int read() throws IOException {
         ++this.mPosition;
         return this.mDataInputStream.read();
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         var2 = this.mDataInputStream.read(var1, var2, var3);
         this.mPosition += var2;
         return var2;
      }

      public boolean readBoolean() throws IOException {
         ++this.mPosition;
         return this.mDataInputStream.readBoolean();
      }

      public byte readByte() throws IOException {
         ++this.mPosition;
         if (this.mPosition <= this.mLength) {
            int var1 = this.mDataInputStream.read();
            if (var1 >= 0) {
               return (byte)var1;
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public char readChar() throws IOException {
         this.mPosition += 2;
         return this.mDataInputStream.readChar();
      }

      public double readDouble() throws IOException {
         return Double.longBitsToDouble(this.readLong());
      }

      public float readFloat() throws IOException {
         return Float.intBitsToFloat(this.readInt());
      }

      public void readFully(byte[] var1) throws IOException {
         this.mPosition += var1.length;
         if (this.mPosition <= this.mLength) {
            if (this.mDataInputStream.read(var1, 0, var1.length) != var1.length) {
               throw new IOException("Couldn't read up to the length of buffer");
            }
         } else {
            throw new EOFException();
         }
      }

      public void readFully(byte[] var1, int var2, int var3) throws IOException {
         this.mPosition += var3;
         if (this.mPosition <= this.mLength) {
            if (this.mDataInputStream.read(var1, var2, var3) != var3) {
               throw new IOException("Couldn't read up to the length of buffer");
            }
         } else {
            throw new EOFException();
         }
      }

      public int readInt() throws IOException {
         this.mPosition += 4;
         if (this.mPosition <= this.mLength) {
            int var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            int var3 = this.mDataInputStream.read();
            int var4 = this.mDataInputStream.read();
            if ((var1 | var2 | var3 | var4) >= 0) {
               ByteOrder var5 = this.mByteOrder;
               if (var5 == LITTLE_ENDIAN) {
                  return (var4 << 24) + (var3 << 16) + (var2 << 8) + var1;
               } else if (var5 == BIG_ENDIAN) {
                  return (var1 << 24) + (var2 << 16) + (var3 << 8) + var4;
               } else {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Invalid byte order: ");
                  var6.append(this.mByteOrder);
                  throw new IOException(var6.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public String readLine() throws IOException {
         Log.d("ExifInterface", "Currently unsupported");
         return null;
      }

      public long readLong() throws IOException {
         this.mPosition += 8;
         if (this.mPosition <= this.mLength) {
            int var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            int var3 = this.mDataInputStream.read();
            int var4 = this.mDataInputStream.read();
            int var5 = this.mDataInputStream.read();
            int var6 = this.mDataInputStream.read();
            int var7 = this.mDataInputStream.read();
            int var8 = this.mDataInputStream.read();
            if ((var1 | var2 | var3 | var4 | var5 | var6 | var7 | var8) >= 0) {
               ByteOrder var9 = this.mByteOrder;
               if (var9 == LITTLE_ENDIAN) {
                  return ((long)var8 << 56) + ((long)var7 << 48) + ((long)var6 << 40) + ((long)var5 << 32) + ((long)var4 << 24) + ((long)var3 << 16) + ((long)var2 << 8) + (long)var1;
               } else if (var9 == BIG_ENDIAN) {
                  return ((long)var1 << 56) + ((long)var2 << 48) + ((long)var3 << 40) + ((long)var4 << 32) + ((long)var5 << 24) + ((long)var6 << 16) + ((long)var7 << 8) + (long)var8;
               } else {
                  StringBuilder var10 = new StringBuilder();
                  var10.append("Invalid byte order: ");
                  var10.append(this.mByteOrder);
                  throw new IOException(var10.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public short readShort() throws IOException {
         this.mPosition += 2;
         if (this.mPosition <= this.mLength) {
            int var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            if ((var1 | var2) >= 0) {
               ByteOrder var3 = this.mByteOrder;
               if (var3 == LITTLE_ENDIAN) {
                  return (short)((var2 << 8) + var1);
               } else if (var3 == BIG_ENDIAN) {
                  return (short)((var1 << 8) + var2);
               } else {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Invalid byte order: ");
                  var4.append(this.mByteOrder);
                  throw new IOException(var4.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public String readUTF() throws IOException {
         this.mPosition += 2;
         return this.mDataInputStream.readUTF();
      }

      public int readUnsignedByte() throws IOException {
         ++this.mPosition;
         return this.mDataInputStream.readUnsignedByte();
      }

      public long readUnsignedInt() throws IOException {
         return (long)this.readInt() & 4294967295L;
      }

      public int readUnsignedShort() throws IOException {
         this.mPosition += 2;
         if (this.mPosition <= this.mLength) {
            int var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            if ((var1 | var2) >= 0) {
               ByteOrder var3 = this.mByteOrder;
               if (var3 == LITTLE_ENDIAN) {
                  return (var2 << 8) + var1;
               } else if (var3 == BIG_ENDIAN) {
                  return (var1 << 8) + var2;
               } else {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Invalid byte order: ");
                  var4.append(this.mByteOrder);
                  throw new IOException(var4.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public void seek(long var1) throws IOException {
         int var3 = this.mPosition;
         if ((long)var3 > var1) {
            this.mPosition = 0;
            this.mDataInputStream.reset();
            this.mDataInputStream.mark(this.mLength);
         } else {
            var1 -= (long)var3;
         }

         var3 = (int)var1;
         if (this.skipBytes(var3) != var3) {
            throw new IOException("Couldn't seek up to the byteCount");
         }
      }

      public void setByteOrder(ByteOrder var1) {
         this.mByteOrder = var1;
      }

      public int skipBytes(int var1) throws IOException {
         int var2 = Math.min(var1, this.mLength - this.mPosition);

         for(var1 = 0; var1 < var2; var1 += this.mDataInputStream.skipBytes(var2 - var1)) {
         }

         this.mPosition += var1;
         return var1;
      }
   }

   private static class ExifAttribute {
      public final byte[] bytes;
      public final int format;
      public final int numberOfComponents;

      ExifAttribute(int var1, int var2, byte[] var3) {
         this.format = var1;
         this.numberOfComponents = var2;
         this.bytes = var3;
      }

      public static ExifInterface.ExifAttribute createString(String var0) {
         StringBuilder var1 = new StringBuilder();
         var1.append(var0);
         var1.append('\u0000');
         byte[] var2 = var1.toString().getBytes(ExifInterface.ASCII);
         return new ExifInterface.ExifAttribute(2, var2.length, var2);
      }

      public static ExifInterface.ExifAttribute createULong(long var0, ByteOrder var2) {
         return createULong(new long[]{var0}, var2);
      }

      public static ExifInterface.ExifAttribute createULong(long[] var0, ByteOrder var1) {
         ByteBuffer var2 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * var0.length]);
         var2.order(var1);
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var2.putInt((int)var0[var4]);
         }

         return new ExifInterface.ExifAttribute(4, var0.length, var2.array());
      }

      public static ExifInterface.ExifAttribute createURational(ExifInterface.Rational var0, ByteOrder var1) {
         return createURational(new ExifInterface.Rational[]{var0}, var1);
      }

      public static ExifInterface.ExifAttribute createURational(ExifInterface.Rational[] var0, ByteOrder var1) {
         ByteBuffer var2 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * var0.length]);
         var2.order(var1);
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ExifInterface.Rational var5 = var0[var4];
            var2.putInt((int)var5.numerator);
            var2.putInt((int)var5.denominator);
         }

         return new ExifInterface.ExifAttribute(5, var0.length, var2.array());
      }

      public static ExifInterface.ExifAttribute createUShort(int var0, ByteOrder var1) {
         return createUShort(new int[]{var0}, var1);
      }

      public static ExifInterface.ExifAttribute createUShort(int[] var0, ByteOrder var1) {
         ByteBuffer var2 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * var0.length]);
         var2.order(var1);
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var2.putShort((short)var0[var4]);
         }

         return new ExifInterface.ExifAttribute(3, var0.length, var2.array());
      }

      public double getDoubleValue(ByteOrder var1) {
         Object var2 = this.getValue(var1);
         if (var2 != null) {
            if (var2 instanceof String) {
               return Double.parseDouble((String)var2);
            } else if (var2 instanceof long[]) {
               long[] var6 = (long[])var2;
               if (var6.length == 1) {
                  return (double)var6[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof int[]) {
               int[] var5 = (int[])var2;
               if (var5.length == 1) {
                  return (double)var5[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof double[]) {
               double[] var4 = (double[])var2;
               if (var4.length == 1) {
                  return var4[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof ExifInterface.Rational[]) {
               ExifInterface.Rational[] var3 = (ExifInterface.Rational[])var2;
               if (var3.length == 1) {
                  return var3[0].calculate();
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else {
               throw new NumberFormatException("Couldn't find a double value");
            }
         } else {
            throw new NumberFormatException("NULL can't be converted to a double value");
         }
      }

      public int getIntValue(ByteOrder var1) {
         Object var2 = this.getValue(var1);
         if (var2 != null) {
            if (var2 instanceof String) {
               return Integer.parseInt((String)var2);
            } else if (var2 instanceof long[]) {
               long[] var4 = (long[])var2;
               if (var4.length == 1) {
                  return (int)var4[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof int[]) {
               int[] var3 = (int[])var2;
               if (var3.length == 1) {
                  return var3[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else {
               throw new NumberFormatException("Couldn't find a integer value");
            }
         } else {
            throw new NumberFormatException("NULL can't be converted to a integer value");
         }
      }

      public String getStringValue(ByteOrder var1) {
         Object var2 = this.getValue(var1);
         if (var2 == null) {
            return null;
         } else if (var2 instanceof String) {
            return (String)var2;
         } else {
            StringBuilder var8 = new StringBuilder();
            boolean var3 = var2 instanceof long[];
            byte var4 = 0;
            byte var5 = 0;
            byte var6 = 0;
            int var7 = 0;
            int var13;
            if (var3) {
               long[] var12 = (long[])var2;

               while(var7 < var12.length) {
                  var8.append(var12[var7]);
                  var13 = var7 + 1;
                  var7 = var13;
                  if (var13 != var12.length) {
                     var8.append(",");
                     var7 = var13;
                  }
               }

               return var8.toString();
            } else if (var2 instanceof int[]) {
               int[] var11 = (int[])var2;
               var7 = var4;

               while(var7 < var11.length) {
                  var8.append(var11[var7]);
                  var13 = var7 + 1;
                  var7 = var13;
                  if (var13 != var11.length) {
                     var8.append(",");
                     var7 = var13;
                  }
               }

               return var8.toString();
            } else if (var2 instanceof double[]) {
               double[] var10 = (double[])var2;
               var7 = var5;

               while(var7 < var10.length) {
                  var8.append(var10[var7]);
                  var13 = var7 + 1;
                  var7 = var13;
                  if (var13 != var10.length) {
                     var8.append(",");
                     var7 = var13;
                  }
               }

               return var8.toString();
            } else if (var2 instanceof ExifInterface.Rational[]) {
               ExifInterface.Rational[] var9 = (ExifInterface.Rational[])var2;
               var7 = var6;

               while(var7 < var9.length) {
                  var8.append(var9[var7].numerator);
                  var8.append('/');
                  var8.append(var9[var7].denominator);
                  var13 = var7 + 1;
                  var7 = var13;
                  if (var13 != var9.length) {
                     var8.append(",");
                     var7 = var13;
                  }
               }

               return var8.toString();
            } else {
               return null;
            }
         }
      }

      Object getValue(ByteOrder param1) {
         // $FF: Couldn't be decompiled
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("(");
         var1.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
         var1.append(", data length:");
         var1.append(this.bytes.length);
         var1.append(")");
         return var1.toString();
      }
   }

   static class ExifTag {
      public final String name;
      public final int number;
      public final int primaryFormat;
      public final int secondaryFormat;

      ExifTag(String var1, int var2, int var3) {
         this.name = var1;
         this.number = var2;
         this.primaryFormat = var3;
         this.secondaryFormat = -1;
      }

      ExifTag(String var1, int var2, int var3, int var4) {
         this.name = var1;
         this.number = var2;
         this.primaryFormat = var3;
         this.secondaryFormat = var4;
      }

      boolean isFormatCompatible(int var1) {
         int var2 = this.primaryFormat;
         if (var2 != 7 && var1 != 7 && var2 != var1) {
            int var3 = this.secondaryFormat;
            if (var3 != var1) {
               if ((var2 == 4 || var3 == 4) && var1 == 3) {
                  return true;
               }

               if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && var1 == 8) {
                  return true;
               }

               if ((this.primaryFormat == 12 || this.secondaryFormat == 12) && var1 == 11) {
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

      Rational(long var1, long var3) {
         if (var3 == 0L) {
            this.numerator = 0L;
            this.denominator = 1L;
         } else {
            this.numerator = var1;
            this.denominator = var3;
         }
      }

      public double calculate() {
         double var1 = (double)this.numerator;
         double var3 = (double)this.denominator;
         Double.isNaN(var1);
         Double.isNaN(var3);
         return var1 / var3;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.numerator);
         var1.append("/");
         var1.append(this.denominator);
         return var1.toString();
      }
   }
}
