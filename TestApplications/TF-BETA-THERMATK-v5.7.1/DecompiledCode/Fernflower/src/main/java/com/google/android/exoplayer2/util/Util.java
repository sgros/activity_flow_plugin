package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.security.NetworkSecurityPolicy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.view.Display.Mode;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioRendererEventListener$_CC;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener$_CC;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

public final class Util {
   private static final int[] CRC32_BYTES_MSBF;
   public static final String DEVICE;
   public static final String DEVICE_DEBUG_INFO;
   public static final byte[] EMPTY_BYTE_ARRAY;
   private static final Pattern ESCAPED_CHARACTER_PATTERN;
   public static final String MANUFACTURER;
   public static final String MODEL;
   public static final int SDK_INT;
   private static final String TAG = "Util";
   private static final Pattern XS_DATE_TIME_PATTERN;
   private static final Pattern XS_DURATION_PATTERN;

   static {
      SDK_INT = VERSION.SDK_INT;
      DEVICE = Build.DEVICE;
      MANUFACTURER = Build.MANUFACTURER;
      MODEL = Build.MODEL;
      StringBuilder var0 = new StringBuilder();
      var0.append(DEVICE);
      var0.append(", ");
      var0.append(MODEL);
      var0.append(", ");
      var0.append(MANUFACTURER);
      var0.append(", ");
      var0.append(SDK_INT);
      DEVICE_DEBUG_INFO = var0.toString();
      EMPTY_BYTE_ARRAY = new byte[0];
      XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d?\\d):?(\\d\\d)))?");
      XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
      ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
      CRC32_BYTES_MSBF = new int[]{0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
   }

   private Util() {
   }

   public static long addWithOverflowDefault(long var0, long var2, long var4) {
      long var6 = var0 + var2;
      return ((var0 ^ var6) & (var2 ^ var6)) < 0L ? var4 : var6;
   }

   public static boolean areEqual(Object var0, Object var1) {
      boolean var2;
      if (var0 == null) {
         if (var1 == null) {
            var2 = true;
         } else {
            var2 = false;
         }
      } else {
         var2 = var0.equals(var1);
      }

      return var2;
   }

   public static int binarySearchCeil(List var0, Comparable var1, boolean var2, boolean var3) {
      int var4 = Collections.binarySearch(var0, var1);
      int var5;
      if (var4 < 0) {
         var4 = ~var4;
      } else {
         var5 = var0.size();

         do {
            ++var4;
         } while(var4 < var5 && ((Comparable)var0.get(var4)).compareTo(var1) == 0);

         if (var2) {
            --var4;
         }
      }

      var5 = var4;
      if (var3) {
         var5 = Math.min(var0.size() - 1, var4);
      }

      return var5;
   }

   public static int binarySearchCeil(int[] var0, int var1, boolean var2, boolean var3) {
      int var4 = Arrays.binarySearch(var0, var1);
      int var5 = var4;
      if (var4 < 0) {
         var1 = ~var4;
      } else {
         while(true) {
            ++var5;
            if (var5 >= var0.length || var0[var5] != var1) {
               if (var2) {
                  var1 = var5 - 1;
               } else {
                  var1 = var5;
               }
               break;
            }
         }
      }

      var5 = var1;
      if (var3) {
         var5 = Math.min(var0.length - 1, var1);
      }

      return var5;
   }

   public static int binarySearchCeil(long[] var0, long var1, boolean var3, boolean var4) {
      int var5 = Arrays.binarySearch(var0, var1);
      int var6 = var5;
      if (var5 < 0) {
         var6 = ~var5;
      } else {
         while(true) {
            ++var6;
            if (var6 >= var0.length || var0[var6] != var1) {
               if (var3) {
                  --var6;
               }
               break;
            }
         }
      }

      var5 = var6;
      if (var4) {
         var5 = Math.min(var0.length - 1, var6);
      }

      return var5;
   }

   public static int binarySearchFloor(List var0, Comparable var1, boolean var2, boolean var3) {
      int var4 = Collections.binarySearch(var0, var1);
      int var5 = var4;
      if (var4 < 0) {
         var5 = -(var4 + 2);
      } else {
         while(true) {
            --var5;
            if (var5 < 0 || ((Comparable)var0.get(var5)).compareTo(var1) != 0) {
               if (var2) {
                  ++var5;
               }
               break;
            }
         }
      }

      var4 = var5;
      if (var3) {
         var4 = Math.max(0, var5);
      }

      return var4;
   }

   public static int binarySearchFloor(int[] var0, int var1, boolean var2, boolean var3) {
      int var4 = Arrays.binarySearch(var0, var1);
      int var5 = var4;
      if (var4 < 0) {
         var1 = -(var4 + 2);
      } else {
         while(true) {
            --var5;
            if (var5 < 0 || var0[var5] != var1) {
               if (var2) {
                  var1 = var5 + 1;
               } else {
                  var1 = var5;
               }
               break;
            }
         }
      }

      var5 = var1;
      if (var3) {
         var5 = Math.max(0, var1);
      }

      return var5;
   }

   public static int binarySearchFloor(long[] var0, long var1, boolean var3, boolean var4) {
      int var5 = Arrays.binarySearch(var0, var1);
      int var6 = var5;
      if (var5 < 0) {
         var6 = -(var5 + 2);
      } else {
         while(true) {
            --var6;
            if (var6 < 0 || var0[var6] != var1) {
               if (var3) {
                  ++var6;
               }
               break;
            }
         }
      }

      var5 = var6;
      if (var4) {
         var5 = Math.max(0, var6);
      }

      return var5;
   }

   @EnsuresNonNull({"#1"})
   public static Object castNonNull(Object var0) {
      return var0;
   }

   @EnsuresNonNull({"#1"})
   public static Object[] castNonNullTypeArray(Object[] var0) {
      return var0;
   }

   public static int ceilDivide(int var0, int var1) {
      return (var0 + var1 - 1) / var1;
   }

   public static long ceilDivide(long var0, long var2) {
      return (var0 + var2 - 1L) / var2;
   }

   @TargetApi(24)
   public static boolean checkCleartextTrafficPermitted(Uri... var0) {
      if (SDK_INT < 24) {
         return true;
      } else {
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            Uri var3 = var0[var2];
            if ("http".equals(var3.getScheme())) {
               NetworkSecurityPolicy var4 = NetworkSecurityPolicy.getInstance();
               String var5 = var3.getHost();
               Assertions.checkNotNull(var5);
               if (!var4.isCleartextTrafficPermitted((String)var5)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static void closeQuietly(DataSource var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var1) {
         }
      }

   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var1) {
         }
      }

   }

   public static int compareLong(long var0, long var2) {
      byte var4;
      if (var0 < var2) {
         var4 = -1;
      } else if (var0 == var2) {
         var4 = 0;
      } else {
         var4 = 1;
      }

      return var4;
   }

   public static float constrainValue(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var0, var2));
   }

   public static int constrainValue(int var0, int var1, int var2) {
      return Math.max(var1, Math.min(var0, var2));
   }

   public static long constrainValue(long var0, long var2, long var4) {
      return Math.max(var2, Math.min(var0, var4));
   }

   public static boolean contains(Object[] var0, Object var1) {
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (areEqual(var0[var3], var1)) {
            return true;
         }
      }

      return false;
   }

   public static int crc(byte[] var0, int var1, int var2, int var3) {
      while(var1 < var2) {
         var3 = CRC32_BYTES_MSBF[(var3 >>> 24 ^ var0[var1] & 255) & 255] ^ var3 << 8;
         ++var1;
      }

      return var3;
   }

   public static Handler createHandler(Callback var0) {
      return createHandler(getLooper(), var0);
   }

   public static Handler createHandler(Looper var0, Callback var1) {
      return new Handler(var0, var1);
   }

   public static File createTempDirectory(Context var0, String var1) throws IOException {
      File var2 = createTempFile(var0, var1);
      var2.delete();
      var2.mkdir();
      return var2;
   }

   public static File createTempFile(Context var0, String var1) throws IOException {
      return File.createTempFile(var1, (String)null, var0.getCacheDir());
   }

   public static String escapeFileName(String var0) {
      int var1 = var0.length();
      byte var2 = 0;
      int var3 = 0;

      int var4;
      int var5;
      for(var4 = 0; var3 < var1; var4 = var5) {
         var5 = var4;
         if (shouldEscapeCharacter(var0.charAt(var3))) {
            var5 = var4 + 1;
         }

         ++var3;
      }

      if (var4 == 0) {
         return var0;
      } else {
         StringBuilder var6 = new StringBuilder(var4 * 2 + var1);
         var5 = var4;

         for(var4 = var2; var5 > 0; ++var4) {
            char var7 = var0.charAt(var4);
            if (shouldEscapeCharacter(var7)) {
               var6.append('%');
               var6.append(Integer.toHexString(var7));
               --var5;
            } else {
               var6.append(var7);
            }
         }

         if (var4 < var1) {
            var6.append(var0, var4, var1);
         }

         return var6.toString();
      }
   }

   public static String formatInvariant(String var0, Object... var1) {
      return String.format(Locale.US, var0, var1);
   }

   public static String fromUtf8Bytes(byte[] var0) {
      return new String(var0, Charset.forName("UTF-8"));
   }

   public static String fromUtf8Bytes(byte[] var0, int var1, int var2) {
      return new String(var0, var1, var2, Charset.forName("UTF-8"));
   }

   public static int getAudioContentTypeForStreamType(int var0) {
      if (var0 != 0) {
         return var0 != 1 && var0 != 2 && var0 != 4 && var0 != 5 && var0 != 8 ? 2 : 4;
      } else {
         return 1;
      }
   }

   public static int getAudioTrackChannelConfig(int var0) {
      switch(var0) {
      case 1:
         return 4;
      case 2:
         return 12;
      case 3:
         return 28;
      case 4:
         return 204;
      case 5:
         return 220;
      case 6:
         return 252;
      case 7:
         return 1276;
      case 8:
         var0 = SDK_INT;
         if (var0 >= 23) {
            return 6396;
         } else {
            if (var0 >= 21) {
               return 6396;
            }

            return 0;
         }
      default:
         return 0;
      }
   }

   public static int getAudioUsageForStreamType(int var0) {
      if (var0 != 0) {
         if (var0 != 1) {
            if (var0 != 2) {
               byte var1 = 4;
               if (var0 != 4) {
                  var1 = 5;
                  if (var0 != 5) {
                     if (var0 != 8) {
                        return 1;
                     }

                     return 3;
                  }
               }

               return var1;
            } else {
               return 6;
            }
         } else {
            return 13;
         }
      } else {
         return 2;
      }
   }

   public static byte[] getBytesFromHexString(String var0) {
      byte[] var1 = new byte[var0.length() / 2];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         int var3 = var2 * 2;
         var1[var2] = (byte)((byte)((Character.digit(var0.charAt(var3), 16) << 4) + Character.digit(var0.charAt(var3 + 1), 16)));
      }

      return var1;
   }

   public static String getCodecsOfType(String var0, int var1) {
      String[] var2 = splitCodecs(var0);
      int var3 = var2.length;
      var0 = null;
      if (var3 == 0) {
         return null;
      } else {
         StringBuilder var4 = new StringBuilder();
         int var5 = var2.length;

         for(var3 = 0; var3 < var5; ++var3) {
            String var6 = var2[var3];
            if (var1 == MimeTypes.getTrackTypeOfCodec(var6)) {
               if (var4.length() > 0) {
                  var4.append(",");
               }

               var4.append(var6);
            }
         }

         if (var4.length() > 0) {
            var0 = var4.toString();
         }

         return var0;
      }
   }

   public static String getCommaDelimitedSimpleClassNames(Object[] var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(var0[var2].getClass().getSimpleName());
         if (var2 < var0.length - 1) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   public static String getCountryCode(Context var0) {
      if (var0 != null) {
         TelephonyManager var1 = (TelephonyManager)var0.getSystemService("phone");
         if (var1 != null) {
            String var2 = var1.getNetworkCountryIso();
            if (!TextUtils.isEmpty(var2)) {
               return toUpperInvariant(var2);
            }
         }
      }

      return toUpperInvariant(Locale.getDefault().getCountry());
   }

   public static int getDefaultBufferSize(int var0) {
      switch(var0) {
      case 0:
         return 16777216;
      case 1:
         return 3538944;
      case 2:
         return 13107200;
      case 3:
      case 4:
      case 5:
         return 131072;
      case 6:
         return 0;
      default:
         throw new IllegalArgumentException();
      }
   }

   @TargetApi(16)
   private static void getDisplaySizeV16(Display var0, Point var1) {
      var0.getSize(var1);
   }

   @TargetApi(17)
   private static void getDisplaySizeV17(Display var0, Point var1) {
      var0.getRealSize(var1);
   }

   @TargetApi(23)
   private static void getDisplaySizeV23(Display var0, Point var1) {
      Mode var2 = var0.getMode();
      var1.x = var2.getPhysicalWidth();
      var1.y = var2.getPhysicalHeight();
   }

   private static void getDisplaySizeV9(Display var0, Point var1) {
      var1.x = var0.getWidth();
      var1.y = var0.getHeight();
   }

   public static UUID getDrmUuid(String var0) {
      byte var5;
      label40: {
         String var1 = toLowerInvariant(var0);
         int var2 = var1.hashCode();
         if (var2 != -1860423953) {
            if (var2 != -1400551171) {
               if (var2 == 790309106 && var1.equals("clearkey")) {
                  var5 = 2;
                  break label40;
               }
            } else if (var1.equals("widevine")) {
               var5 = 0;
               break label40;
            }
         } else if (var1.equals("playready")) {
            var5 = 1;
            break label40;
         }

         var5 = -1;
      }

      if (var5 != 0) {
         if (var5 != 1) {
            if (var5 != 2) {
               try {
                  UUID var4 = UUID.fromString(var0);
                  return var4;
               } catch (RuntimeException var3) {
                  return null;
               }
            } else {
               return C.CLEARKEY_UUID;
            }
         } else {
            return C.PLAYREADY_UUID;
         }
      } else {
         return C.WIDEVINE_UUID;
      }
   }

   public static int getIntegerCodeForString(String var0) {
      int var1 = var0.length();
      int var2 = 0;
      boolean var3;
      if (var1 <= 4) {
         var3 = true;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);

      int var4;
      for(var4 = 0; var2 < var1; ++var2) {
         var4 = var4 << 8 | var0.charAt(var2);
      }

      return var4;
   }

   public static Looper getLooper() {
      Looper var0 = Looper.myLooper();
      if (var0 == null) {
         var0 = Looper.getMainLooper();
      }

      return var0;
   }

   public static long getMediaDurationForPlayoutDuration(long var0, float var2) {
      if (var2 == 1.0F) {
         return var0;
      } else {
         double var3 = (double)var0;
         double var5 = (double)var2;
         Double.isNaN(var3);
         Double.isNaN(var5);
         return Math.round(var3 * var5);
      }
   }

   private static int getMobileNetworkType(NetworkInfo var0) {
      switch(var0.getSubtype()) {
      case 1:
      case 2:
         return 3;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 14:
      case 15:
      case 17:
         return 4;
      case 13:
         return 5;
      case 16:
      default:
         return 6;
      case 18:
         return 2;
      }
   }

   public static int getNetworkType(Context var0) {
      if (var0 == null) {
         return 0;
      } else {
         ConnectivityManager var2 = (ConnectivityManager)var0.getSystemService("connectivity");
         if (var2 == null) {
            return 0;
         } else {
            NetworkInfo var3 = var2.getActiveNetworkInfo();
            if (var3 != null && var3.isConnected()) {
               int var1 = var3.getType();
               if (var1 != 0) {
                  if (var1 == 1) {
                     return 2;
                  }

                  if (var1 != 4 && var1 != 5) {
                     if (var1 != 6) {
                        if (var1 != 9) {
                           return 8;
                        }

                        return 7;
                     }

                     return 5;
                  }
               }

               return getMobileNetworkType(var3);
            } else {
               return 1;
            }
         }
      }
   }

   public static int getPcmEncoding(int var0) {
      if (var0 != 8) {
         if (var0 != 16) {
            if (var0 != 24) {
               return var0 != 32 ? 0 : 1073741824;
            } else {
               return Integer.MIN_VALUE;
            }
         } else {
            return 2;
         }
      } else {
         return 3;
      }
   }

   public static int getPcmFrameSize(int var0, int var1) {
      if (var0 != Integer.MIN_VALUE) {
         if (var0 != 1073741824) {
            if (var0 == 2) {
               return var1 * 2;
            }

            if (var0 == 3) {
               return var1;
            }

            if (var0 != 4) {
               throw new IllegalArgumentException();
            }
         }

         return var1 * 4;
      } else {
         return var1 * 3;
      }
   }

   public static Point getPhysicalDisplaySize(Context var0) {
      return getPhysicalDisplaySize(var0, ((WindowManager)var0.getSystemService("window")).getDefaultDisplay());
   }

   public static Point getPhysicalDisplaySize(Context var0, Display var1) {
      int var4;
      if (SDK_INT <= 28 && var1.getDisplayId() == 0 && isTv(var0)) {
         if ("Sony".equals(MANUFACTURER) && MODEL.startsWith("BRAVIA") && var0.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
            return new Point(3840, 2160);
         }

         String var7;
         if (SDK_INT < 28) {
            var7 = getSystemProperty("sys.display-size");
         } else {
            var7 = getSystemProperty("vendor.display-size");
         }

         if (!TextUtils.isEmpty(var7)) {
            label67: {
               boolean var10001;
               int var3;
               try {
                  String[] var2 = split(var7.trim(), "x");
                  if (var2.length != 2) {
                     break label67;
                  }

                  var3 = Integer.parseInt(var2[0]);
                  var4 = Integer.parseInt(var2[1]);
               } catch (NumberFormatException var6) {
                  var10001 = false;
                  break label67;
               }

               if (var3 > 0 && var4 > 0) {
                  try {
                     Point var9 = new Point(var3, var4);
                     return var9;
                  } catch (NumberFormatException var5) {
                     var10001 = false;
                  }
               }
            }

            StringBuilder var10 = new StringBuilder();
            var10.append("Invalid display size: ");
            var10.append(var7);
            Log.e("Util", var10.toString());
         }
      }

      Point var8 = new Point();
      var4 = SDK_INT;
      if (var4 >= 23) {
         getDisplaySizeV23(var1, var8);
      } else if (var4 >= 17) {
         getDisplaySizeV17(var1, var8);
      } else if (var4 >= 16) {
         getDisplaySizeV16(var1, var8);
      } else {
         getDisplaySizeV9(var1, var8);
      }

      return var8;
   }

   public static long getPlayoutDurationForMediaDuration(long var0, float var2) {
      if (var2 == 1.0F) {
         return var0;
      } else {
         double var3 = (double)var0;
         double var5 = (double)var2;
         Double.isNaN(var3);
         Double.isNaN(var5);
         return Math.round(var3 / var5);
      }
   }

   public static RendererCapabilities[] getRendererCapabilities(RenderersFactory var0, DrmSessionManager var1) {
      Renderer[] var4 = var0.createRenderers(new Handler(), new VideoRendererEventListener() {
         // $FF: synthetic method
         public void onDroppedFrames(int var1, long var2) {
            VideoRendererEventListener$_CC.$default$onDroppedFrames(this, var1, var2);
         }

         // $FF: synthetic method
         public void onRenderedFirstFrame(Surface var1) {
            VideoRendererEventListener$_CC.$default$onRenderedFirstFrame(this, var1);
         }

         // $FF: synthetic method
         public void onVideoDecoderInitialized(String var1, long var2, long var4) {
            VideoRendererEventListener$_CC.$default$onVideoDecoderInitialized(this, var1, var2, var4);
         }

         // $FF: synthetic method
         public void onVideoDisabled(DecoderCounters var1) {
            VideoRendererEventListener$_CC.$default$onVideoDisabled(this, var1);
         }

         // $FF: synthetic method
         public void onVideoEnabled(DecoderCounters var1) {
            VideoRendererEventListener$_CC.$default$onVideoEnabled(this, var1);
         }

         // $FF: synthetic method
         public void onVideoInputFormatChanged(Format var1) {
            VideoRendererEventListener$_CC.$default$onVideoInputFormatChanged(this, var1);
         }

         // $FF: synthetic method
         public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
            VideoRendererEventListener$_CC.$default$onVideoSizeChanged(this, var1, var2, var3, var4);
         }
      }, new AudioRendererEventListener() {
         // $FF: synthetic method
         public void onAudioDecoderInitialized(String var1, long var2, long var4) {
            AudioRendererEventListener$_CC.$default$onAudioDecoderInitialized(this, var1, var2, var4);
         }

         // $FF: synthetic method
         public void onAudioDisabled(DecoderCounters var1) {
            AudioRendererEventListener$_CC.$default$onAudioDisabled(this, var1);
         }

         // $FF: synthetic method
         public void onAudioEnabled(DecoderCounters var1) {
            AudioRendererEventListener$_CC.$default$onAudioEnabled(this, var1);
         }

         // $FF: synthetic method
         public void onAudioInputFormatChanged(Format var1) {
            AudioRendererEventListener$_CC.$default$onAudioInputFormatChanged(this, var1);
         }

         // $FF: synthetic method
         public void onAudioSessionId(int var1) {
            AudioRendererEventListener$_CC.$default$onAudioSessionId(this, var1);
         }

         // $FF: synthetic method
         public void onAudioSinkUnderrun(int var1, long var2, long var4) {
            AudioRendererEventListener$_CC.$default$onAudioSinkUnderrun(this, var1, var2, var4);
         }
      }, _$$Lambda$Util$Re8Vg9XyGFeMHzExHNw9i73q_Y8.INSTANCE, _$$Lambda$Util$M3KUmpUnPzRNYEBE___Y1k2KDhE.INSTANCE, var1);
      RendererCapabilities[] var3 = new RendererCapabilities[var4.length];

      for(int var2 = 0; var2 < var4.length; ++var2) {
         var3[var2] = var4[var2].getCapabilities();
      }

      return var3;
   }

   public static int getStreamTypeForAudioUsage(int var0) {
      switch(var0) {
      case 1:
      case 12:
      case 14:
         return 3;
      case 2:
         return 0;
      case 3:
         return 8;
      case 4:
         return 4;
      case 5:
      case 7:
      case 8:
      case 9:
      case 10:
         return 5;
      case 6:
         return 2;
      case 11:
      default:
         return 3;
      case 13:
         return 1;
      }
   }

   public static String getStringForTime(StringBuilder var0, Formatter var1, long var2) {
      long var4 = var2;
      if (var2 == -9223372036854775807L) {
         var4 = 0L;
      }

      long var6 = (var4 + 500L) / 1000L;
      var2 = var6 % 60L;
      var4 = var6 / 60L % 60L;
      var6 /= 3600L;
      var0.setLength(0);
      String var8;
      if (var6 > 0L) {
         var8 = var1.format("%d:%02d:%02d", var6, var4, var2).toString();
      } else {
         var8 = var1.format("%02d:%02d", var4, var2).toString();
      }

      return var8;
   }

   private static String getSystemProperty(String var0) {
      try {
         Class var4 = Class.forName("android.os.SystemProperties");
         String var5 = (String)var4.getMethod("get", String.class).invoke(var4, var0);
         return var5;
      } catch (Exception var3) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Failed to read system property ");
         var1.append(var0);
         Log.e("Util", var1.toString(), var3);
         return null;
      }
   }

   public static String getUserAgent(Context var0, String var1) {
      String var4;
      try {
         String var2 = var0.getPackageName();
         var4 = var0.getPackageManager().getPackageInfo(var2, 0).versionName;
      } catch (NameNotFoundException var3) {
         var4 = "?";
      }

      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append("/");
      var5.append(var4);
      var5.append(" (Linux;Android ");
      var5.append(VERSION.RELEASE);
      var5.append(") ");
      var5.append("ExoPlayerLib/2.9.4");
      return var5.toString();
   }

   public static byte[] getUtf8Bytes(String var0) {
      return var0.getBytes(Charset.forName("UTF-8"));
   }

   public static int inferContentType(Uri var0) {
      String var2 = var0.getPath();
      int var1;
      if (var2 == null) {
         var1 = 3;
      } else {
         var1 = inferContentType(var2);
      }

      return var1;
   }

   public static int inferContentType(Uri var0, String var1) {
      int var2;
      if (TextUtils.isEmpty(var1)) {
         var2 = inferContentType(var0);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(".");
         var3.append(var1);
         var2 = inferContentType(var3.toString());
      }

      return var2;
   }

   public static int inferContentType(String var0) {
      var0 = toLowerInvariant(var0);
      if (var0.endsWith(".mpd")) {
         return 0;
      } else if (var0.endsWith(".m3u8")) {
         return 2;
      } else {
         return var0.matches(".*\\.ism(l)?(/manifest(\\(.+\\))?)?") ? 1 : 3;
      }
   }

   public static boolean inflate(ParsableByteArray param0, ParsableByteArray param1, Inflater param2) {
      // $FF: Couldn't be decompiled
   }

   public static boolean isEncodingHighResolutionIntegerPcm(int var0) {
      boolean var1;
      if (var0 != Integer.MIN_VALUE && var0 != 1073741824) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isEncodingLinearPcm(int var0) {
      boolean var1;
      if (var0 != 3 && var0 != 2 && var0 != Integer.MIN_VALUE && var0 != 1073741824 && var0 != 4) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isLinebreak(int var0) {
      boolean var1;
      if (var0 != 10 && var0 != 13) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isLocalFileUri(Uri var0) {
      String var2 = var0.getScheme();
      boolean var1;
      if (!TextUtils.isEmpty(var2) && !"file".equals(var2)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isTv(Context var0) {
      UiModeManager var2 = (UiModeManager)var0.getApplicationContext().getSystemService("uimode");
      boolean var1;
      if (var2 != null && var2.getCurrentModeType() == 4) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   static void lambda$getRendererCapabilities$1(List var0) {
   }

   // $FF: synthetic method
   static void lambda$getRendererCapabilities$2(Metadata var0) {
   }

   // $FF: synthetic method
   static Thread lambda$newSingleThreadExecutor$0(String var0, Runnable var1) {
      return new Thread(var1, var0);
   }

   @TargetApi(23)
   public static boolean maybeRequestReadExternalStoragePermission(Activity var0, Uri... var1) {
      if (SDK_INT < 23) {
         return false;
      } else {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (isLocalFileUri(var1[var3])) {
               if (var0.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                  var0.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
                  return true;
               }
               break;
            }
         }

         return false;
      }
   }

   public static ExecutorService newSingleThreadExecutor(String var0) {
      return Executors.newSingleThreadExecutor(new _$$Lambda$Util$MRC4FgxCpRGDforKj_F0m_7VaCA(var0));
   }

   public static String normalizeLanguageCode(String var0) {
      if (var0 == null) {
         var0 = null;
      } else {
         String var3;
         try {
            Locale var1 = new Locale(var0);
            var3 = var1.getISO3Language();
         } catch (MissingResourceException var2) {
            return toLowerInvariant(var0);
         }

         var0 = var3;
      }

      return var0;
   }

   public static Object[] nullSafeArrayCopy(Object[] var0, int var1) {
      boolean var2;
      if (var1 <= var0.length) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkArgument(var2);
      return Arrays.copyOf(var0, var1);
   }

   public static long parseXsDateTime(String var0) throws ParserException {
      Matcher var1 = XS_DATE_TIME_PATTERN.matcher(var0);
      if (var1.matches()) {
         var0 = var1.group(9);
         int var2 = 0;
         if (var0 != null && !var1.group(9).equalsIgnoreCase("Z")) {
            int var3 = Integer.parseInt(var1.group(12)) * 60 + Integer.parseInt(var1.group(13));
            var2 = var3;
            if ("-".equals(var1.group(11))) {
               var2 = var3 * -1;
            }
         }

         GregorianCalendar var4 = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
         var4.clear();
         var4.set(Integer.parseInt(var1.group(1)), Integer.parseInt(var1.group(2)) - 1, Integer.parseInt(var1.group(3)), Integer.parseInt(var1.group(4)), Integer.parseInt(var1.group(5)), Integer.parseInt(var1.group(6)));
         if (!TextUtils.isEmpty(var1.group(8))) {
            StringBuilder var9 = new StringBuilder();
            var9.append("0.");
            var9.append(var1.group(8));
            var4.set(14, (new BigDecimal(var9.toString())).movePointRight(3).intValue());
         }

         long var5 = var4.getTimeInMillis();
         long var7 = var5;
         if (var2 != 0) {
            var7 = var5 - (long)(var2 * '\uea60');
         }

         return var7;
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append("Invalid date/time format: ");
         var10.append(var0);
         throw new ParserException(var10.toString());
      }
   }

   public static long parseXsDuration(String var0) {
      Matcher var1 = XS_DURATION_PATTERN.matcher(var0);
      if (var1.matches()) {
         boolean var2 = TextUtils.isEmpty(var1.group(1));
         var0 = var1.group(3);
         double var3 = 0.0D;
         double var5;
         if (var0 != null) {
            var5 = Double.parseDouble(var0) * 3.1556908E7D;
         } else {
            var5 = 0.0D;
         }

         var0 = var1.group(5);
         double var7;
         if (var0 != null) {
            var7 = Double.parseDouble(var0) * 2629739.0D;
         } else {
            var7 = 0.0D;
         }

         var0 = var1.group(7);
         double var9;
         if (var0 != null) {
            var9 = Double.parseDouble(var0) * 86400.0D;
         } else {
            var9 = 0.0D;
         }

         var0 = var1.group(10);
         double var11;
         if (var0 != null) {
            var11 = 3600.0D * Double.parseDouble(var0);
         } else {
            var11 = 0.0D;
         }

         var0 = var1.group(12);
         double var13;
         if (var0 != null) {
            var13 = Double.parseDouble(var0) * 60.0D;
         } else {
            var13 = 0.0D;
         }

         var0 = var1.group(14);
         if (var0 != null) {
            var3 = Double.parseDouble(var0);
         }

         long var15 = (long)((var5 + var7 + var9 + var11 + var13 + var3) * 1000.0D);
         long var17 = var15;
         if (true ^ var2) {
            var17 = -var15;
         }

         return var17;
      } else {
         return (long)(Double.parseDouble(var0) * 3600.0D * 1000.0D);
      }
   }

   public static boolean readBoolean(Parcel var0) {
      boolean var1;
      if (var0.readInt() != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static void recursiveDelete(File var0) {
      File[] var1 = var0.listFiles();
      if (var1 != null) {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            recursiveDelete(var1[var3]);
         }
      }

      var0.delete();
   }

   public static void removeRange(List var0, int var1, int var2) {
      if (var1 >= 0 && var2 <= var0.size() && var1 <= var2) {
         if (var1 != var2) {
            var0.subList(var1, var2).clear();
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public static long resolveSeekPositionUs(long var0, SeekParameters var2, long var3, long var5) {
      if (SeekParameters.EXACT.equals(var2)) {
         return var0;
      } else {
         long var7 = subtractWithOverflowDefault(var0, var2.toleranceBeforeUs, Long.MIN_VALUE);
         long var9 = addWithOverflowDefault(var0, var2.toleranceAfterUs, Long.MAX_VALUE);
         boolean var11 = true;
         boolean var12;
         if (var7 <= var3 && var3 <= var9) {
            var12 = true;
         } else {
            var12 = false;
         }

         if (var7 > var5 || var5 > var9) {
            var11 = false;
         }

         if (var12 && var11) {
            return Math.abs(var3 - var0) <= Math.abs(var5 - var0) ? var3 : var5;
         } else if (var12) {
            return var3;
         } else {
            return var11 ? var5 : var7;
         }
      }
   }

   public static long scaleLargeTimestamp(long var0, long var2, long var4) {
      if (var4 >= var2 && var4 % var2 == 0L) {
         return var0 / (var4 / var2);
      } else if (var4 < var2 && var2 % var4 == 0L) {
         return var0 * (var2 / var4);
      } else {
         double var6 = (double)var2;
         double var8 = (double)var4;
         Double.isNaN(var6);
         Double.isNaN(var8);
         var6 /= var8;
         var8 = (double)var0;
         Double.isNaN(var8);
         return (long)(var8 * var6);
      }
   }

   public static long[] scaleLargeTimestamps(List var0, long var1, long var3) {
      long[] var5 = new long[var0.size()];
      byte var6 = 0;
      byte var7 = 0;
      int var8 = 0;
      if (var3 >= var1 && var3 % var1 == 0L) {
         for(var1 = var3 / var1; var8 < var5.length; ++var8) {
            var5[var8] = (Long)var0.get(var8) / var1;
         }
      } else if (var3 < var1 && var1 % var3 == 0L) {
         var1 /= var3;

         for(var8 = var6; var8 < var5.length; ++var8) {
            var5[var8] = (Long)var0.get(var8) * var1;
         }
      } else {
         double var9 = (double)var1;
         double var11 = (double)var3;
         Double.isNaN(var9);
         Double.isNaN(var11);
         var11 = var9 / var11;

         for(var8 = var7; var8 < var5.length; ++var8) {
            var9 = (double)(Long)var0.get(var8);
            Double.isNaN(var9);
            var5[var8] = (long)(var9 * var11);
         }
      }

      return var5;
   }

   public static void scaleLargeTimestampsInPlace(long[] var0, long var1, long var3) {
      byte var5 = 0;
      byte var6 = 0;
      int var7 = 0;
      if (var3 >= var1 && var3 % var1 == 0L) {
         for(var1 = var3 / var1; var7 < var0.length; ++var7) {
            var0[var7] /= var1;
         }
      } else if (var3 < var1 && var1 % var3 == 0L) {
         var1 /= var3;

         for(var7 = var5; var7 < var0.length; ++var7) {
            var0[var7] *= var1;
         }
      } else {
         double var8 = (double)var1;
         double var10 = (double)var3;
         Double.isNaN(var8);
         Double.isNaN(var10);
         var8 /= var10;

         for(var7 = var6; var7 < var0.length; ++var7) {
            var10 = (double)var0[var7];
            Double.isNaN(var10);
            var0[var7] = (long)(var10 * var8);
         }
      }

   }

   private static boolean shouldEscapeCharacter(char var0) {
      return var0 == '"' || var0 == '%' || var0 == '*' || var0 == '/' || var0 == ':' || var0 == '<' || var0 == '\\' || var0 == '|' || var0 == '>' || var0 == '?';
   }

   public static void sneakyThrow(Throwable var0) {
      sneakyThrowInternal(var0);
      throw null;
   }

   private static void sneakyThrowInternal(Throwable var0) throws Throwable {
      throw var0;
   }

   public static String[] split(String var0, String var1) {
      return var0.split(var1, -1);
   }

   public static String[] splitAtFirst(String var0, String var1) {
      return var0.split(var1, 2);
   }

   public static String[] splitCodecs(String var0) {
      return TextUtils.isEmpty(var0) ? new String[0] : split(var0.trim(), "(\\s*,\\s*)");
   }

   public static ComponentName startForegroundService(Context var0, Intent var1) {
      return SDK_INT >= 26 ? var0.startForegroundService(var1) : var0.startService(var1);
   }

   public static long subtractWithOverflowDefault(long var0, long var2, long var4) {
      long var6 = var0 - var2;
      return ((var0 ^ var6) & (var2 ^ var0)) < 0L ? var4 : var6;
   }

   public static int[] toArray(List var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.size();
         int[] var2 = new int[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = (Integer)var0.get(var3);
         }

         return var2;
      }
   }

   public static byte[] toByteArray(InputStream var0) throws IOException {
      byte[] var1 = new byte[4096];
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      while(true) {
         int var3 = var0.read(var1);
         if (var3 == -1) {
            return var2.toByteArray();
         }

         var2.write(var1, 0, var3);
      }
   }

   public static String toLowerInvariant(String var0) {
      if (var0 != null) {
         var0 = var0.toLowerCase(Locale.US);
      }

      return var0;
   }

   public static String toUpperInvariant(String var0) {
      if (var0 != null) {
         var0 = var0.toUpperCase(Locale.US);
      }

      return var0;
   }

   public static String unescapeFileName(String var0) {
      int var1 = var0.length();
      byte var2 = 0;
      int var3 = 0;

      int var4;
      int var5;
      for(var4 = 0; var3 < var1; var4 = var5) {
         var5 = var4;
         if (var0.charAt(var3) == '%') {
            var5 = var4 + 1;
         }

         ++var3;
      }

      if (var4 == 0) {
         return var0;
      } else {
         var5 = var1 - var4 * 2;
         StringBuilder var6 = new StringBuilder(var5);
         Matcher var7 = ESCAPED_CHARACTER_PATTERN.matcher(var0);

         for(var3 = var2; var4 > 0 && var7.find(); --var4) {
            char var8 = (char)Integer.parseInt(var7.group(1), 16);
            var6.append(var0, var3, var7.start());
            var6.append(var8);
            var3 = var7.end();
         }

         if (var3 < var1) {
            var6.append(var0, var3, var1);
         }

         return var6.length() != var5 ? null : var6.toString();
      }
   }

   public static void writeBoolean(Parcel var0, boolean var1) {
      var0.writeInt(var1);
   }
}
