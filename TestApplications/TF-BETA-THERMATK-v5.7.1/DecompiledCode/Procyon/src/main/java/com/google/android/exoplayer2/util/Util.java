// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import android.content.ComponentName;
import android.content.Intent;
import com.google.android.exoplayer2.SeekParameters;
import android.os.Parcel;
import java.util.regex.Matcher;
import com.google.android.exoplayer2.ParserException;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.MissingResourceException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.app.Activity;
import com.google.android.exoplayer2.metadata.Metadata;
import android.app.UiModeManager;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.Formatter;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.audio.AudioRendererEventListener$_CC;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import android.view.Surface;
import com.google.android.exoplayer2.video.VideoRendererEventListener$_CC;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.RenderersFactory;
import android.view.WindowManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.exoplayer2.C;
import java.util.UUID;
import android.view.Display$Mode;
import android.graphics.Point;
import android.view.Display;
import android.text.TextUtils;
import android.telephony.TelephonyManager;
import java.nio.charset.Charset;
import java.util.Locale;
import java.io.File;
import android.content.Context;
import android.os.Looper;
import android.os.Handler;
import android.os.Handler$Callback;
import java.io.Closeable;
import java.io.IOException;
import com.google.android.exoplayer2.upstream.DataSource;
import android.annotation.TargetApi;
import android.security.NetworkSecurityPolicy;
import android.net.Uri;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.os.Build;
import android.os.Build$VERSION;
import java.util.regex.Pattern;

public final class Util
{
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
        SDK_INT = Build$VERSION.SDK_INT;
        DEVICE = Build.DEVICE;
        MANUFACTURER = Build.MANUFACTURER;
        MODEL = Build.MODEL;
        final StringBuilder sb = new StringBuilder();
        sb.append(Util.DEVICE);
        sb.append(", ");
        sb.append(Util.MODEL);
        sb.append(", ");
        sb.append(Util.MANUFACTURER);
        sb.append(", ");
        sb.append(Util.SDK_INT);
        DEVICE_DEBUG_INFO = sb.toString();
        EMPTY_BYTE_ARRAY = new byte[0];
        XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d?\\d):?(\\d\\d)))?");
        XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
        ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
        CRC32_BYTES_MSBF = new int[] { 0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108 };
    }
    
    private Util() {
    }
    
    public static long addWithOverflowDefault(final long n, final long n2, final long n3) {
        final long n4 = n + n2;
        if (((n ^ n4) & (n2 ^ n4)) < 0L) {
            return n3;
        }
        return n4;
    }
    
    public static boolean areEqual(final Object o, final Object obj) {
        boolean equals;
        if (o == null) {
            equals = (obj == null);
        }
        else {
            equals = o.equals(obj);
        }
        return equals;
    }
    
    public static <T extends Comparable<? super T>> int binarySearchCeil(final List<? extends Comparable<? super T>> list, final T key, final boolean b, final boolean b2) {
        int binarySearch = Collections.binarySearch(list, key);
        if (binarySearch < 0) {
            binarySearch ^= -1;
        }
        else {
            while (++binarySearch < list.size() && ((Comparable)list.get(binarySearch)).compareTo(key) == 0) {}
            if (b) {
                --binarySearch;
            }
        }
        int min = binarySearch;
        if (b2) {
            min = Math.min(list.size() - 1, binarySearch);
        }
        return min;
    }
    
    public static int binarySearchCeil(final int[] a, int n, final boolean b, final boolean b2) {
        int binarySearch;
        final int n2 = binarySearch = Arrays.binarySearch(a, n);
        if (n2 < 0) {
            n = ~n2;
        }
        else {
            while (++binarySearch < a.length && a[binarySearch] == n) {}
            if (b) {
                n = binarySearch - 1;
            }
            else {
                n = binarySearch;
            }
        }
        int min = n;
        if (b2) {
            min = Math.min(a.length - 1, n);
        }
        return min;
    }
    
    public static int binarySearchCeil(final long[] a, final long key, final boolean b, final boolean b2) {
        int binarySearch;
        final int n = binarySearch = Arrays.binarySearch(a, key);
        if (n < 0) {
            binarySearch = ~n;
        }
        else {
            while (++binarySearch < a.length && a[binarySearch] == key) {}
            if (b) {
                --binarySearch;
            }
        }
        int min = binarySearch;
        if (b2) {
            min = Math.min(a.length - 1, binarySearch);
        }
        return min;
    }
    
    public static <T extends Comparable<? super T>> int binarySearchFloor(final List<? extends Comparable<? super T>> list, final T key, final boolean b, final boolean b2) {
        int binarySearch;
        final int n = binarySearch = Collections.binarySearch(list, key);
        if (n < 0) {
            binarySearch = -(n + 2);
        }
        else {
            while (--binarySearch >= 0 && ((Comparable)list.get(binarySearch)).compareTo(key) == 0) {}
            if (b) {
                ++binarySearch;
            }
        }
        int max = binarySearch;
        if (b2) {
            max = Math.max(0, binarySearch);
        }
        return max;
    }
    
    public static int binarySearchFloor(final int[] a, int n, final boolean b, final boolean b2) {
        int binarySearch;
        final int n2 = binarySearch = Arrays.binarySearch(a, n);
        if (n2 < 0) {
            n = -(n2 + 2);
        }
        else {
            while (--binarySearch >= 0 && a[binarySearch] == n) {}
            if (b) {
                n = binarySearch + 1;
            }
            else {
                n = binarySearch;
            }
        }
        int max = n;
        if (b2) {
            max = Math.max(0, n);
        }
        return max;
    }
    
    public static int binarySearchFloor(final long[] a, final long key, final boolean b, final boolean b2) {
        int binarySearch;
        final int n = binarySearch = Arrays.binarySearch(a, key);
        if (n < 0) {
            binarySearch = -(n + 2);
        }
        else {
            while (--binarySearch >= 0 && a[binarySearch] == key) {}
            if (b) {
                ++binarySearch;
            }
        }
        int max = binarySearch;
        if (b2) {
            max = Math.max(0, binarySearch);
        }
        return max;
    }
    
    @EnsuresNonNull({ "#1" })
    public static <T> T castNonNull(final T t) {
        return t;
    }
    
    @EnsuresNonNull({ "#1" })
    public static <T> T[] castNonNullTypeArray(final T[] array) {
        return array;
    }
    
    public static int ceilDivide(final int n, final int n2) {
        return (n + n2 - 1) / n2;
    }
    
    public static long ceilDivide(final long n, final long n2) {
        return (n + n2 - 1L) / n2;
    }
    
    @TargetApi(24)
    public static boolean checkCleartextTrafficPermitted(final Uri... array) {
        if (Util.SDK_INT < 24) {
            return true;
        }
        for (final Uri uri : array) {
            if ("http".equals(uri.getScheme())) {
                final NetworkSecurityPolicy instance = NetworkSecurityPolicy.getInstance();
                final String host = uri.getHost();
                Assertions.checkNotNull(host);
                if (!instance.isCleartextTrafficPermitted((String)host)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void closeQuietly(final DataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        try {
            dataSource.close();
        }
        catch (IOException ex) {}
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
    
    public static int compareLong(final long n, final long n2) {
        int n3;
        if (n < n2) {
            n3 = -1;
        }
        else if (n == n2) {
            n3 = 0;
        }
        else {
            n3 = 1;
        }
        return n3;
    }
    
    public static float constrainValue(final float a, final float a2, final float b) {
        return Math.max(a2, Math.min(a, b));
    }
    
    public static int constrainValue(final int a, final int a2, final int b) {
        return Math.max(a2, Math.min(a, b));
    }
    
    public static long constrainValue(final long a, final long a2, final long b) {
        return Math.max(a2, Math.min(a, b));
    }
    
    public static boolean contains(final Object[] array, final Object o) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (areEqual(array[i], o)) {
                return true;
            }
        }
        return false;
    }
    
    public static int crc(final byte[] array, int i, final int n, int n2) {
        while (i < n) {
            n2 = (Util.CRC32_BYTES_MSBF[(n2 >>> 24 ^ (array[i] & 0xFF)) & 0xFF] ^ n2 << 8);
            ++i;
        }
        return n2;
    }
    
    public static Handler createHandler(final Handler$Callback handler$Callback) {
        return createHandler(getLooper(), handler$Callback);
    }
    
    public static Handler createHandler(final Looper looper, final Handler$Callback handler$Callback) {
        return new Handler(looper, handler$Callback);
    }
    
    public static File createTempDirectory(final Context context, final String s) throws IOException {
        final File tempFile = createTempFile(context, s);
        tempFile.delete();
        tempFile.mkdir();
        return tempFile;
    }
    
    public static File createTempFile(final Context context, final String prefix) throws IOException {
        return File.createTempFile(prefix, null, context.getCacheDir());
    }
    
    public static String escapeFileName(final String s) {
        final int length = s.length();
        final int n = 0;
        int i = 0;
        int n2 = 0;
        while (i < length) {
            int n3 = n2;
            if (shouldEscapeCharacter(s.charAt(i))) {
                n3 = n2 + 1;
            }
            ++i;
            n2 = n3;
        }
        if (n2 == 0) {
            return s;
        }
        final StringBuilder sb = new StringBuilder(n2 * 2 + length);
        int j = n2;
        int n4 = n;
        while (j > 0) {
            final char char1 = s.charAt(n4);
            if (shouldEscapeCharacter(char1)) {
                sb.append('%');
                sb.append(Integer.toHexString(char1));
                --j;
            }
            else {
                sb.append(char1);
            }
            ++n4;
        }
        if (n4 < length) {
            sb.append(s, n4, length);
        }
        return sb.toString();
    }
    
    public static String formatInvariant(final String format, final Object... args) {
        return String.format(Locale.US, format, args);
    }
    
    public static String fromUtf8Bytes(final byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-8"));
    }
    
    public static String fromUtf8Bytes(final byte[] bytes, final int offset, final int length) {
        return new String(bytes, offset, length, Charset.forName("UTF-8"));
    }
    
    public static int getAudioContentTypeForStreamType(final int n) {
        if (n == 0) {
            return 1;
        }
        if (n != 1 && n != 2 && n != 4 && n != 5 && n != 8) {
            return 2;
        }
        return 4;
    }
    
    public static int getAudioTrackChannelConfig(int sdk_INT) {
        switch (sdk_INT) {
            default: {
                return 0;
            }
            case 8: {
                sdk_INT = Util.SDK_INT;
                if (sdk_INT >= 23) {
                    return 6396;
                }
                if (sdk_INT >= 21) {
                    return 6396;
                }
                return 0;
            }
            case 7: {
                return 1276;
            }
            case 6: {
                return 252;
            }
            case 5: {
                return 220;
            }
            case 4: {
                return 204;
            }
            case 3: {
                return 28;
            }
            case 2: {
                return 12;
            }
            case 1: {
                return 4;
            }
        }
    }
    
    public static int getAudioUsageForStreamType(final int n) {
        if (n == 0) {
            return 2;
        }
        if (n == 1) {
            return 13;
        }
        if (n != 2) {
            int n2 = 4;
            if (n != 4) {
                n2 = 5;
                if (n != 5) {
                    if (n != 8) {
                        return 1;
                    }
                    return 3;
                }
            }
            return n2;
        }
        return 6;
    }
    
    public static byte[] getBytesFromHexString(final String s) {
        final byte[] array = new byte[s.length() / 2];
        for (int i = 0; i < array.length; ++i) {
            final int index = i * 2;
            array[i] = (byte)((Character.digit(s.charAt(index), 16) << 4) + Character.digit(s.charAt(index + 1), 16));
        }
        return array;
    }
    
    public static String getCodecsOfType(String string, final int n) {
        final String[] splitCodecs = splitCodecs(string);
        final int length = splitCodecs.length;
        string = null;
        if (length == 0) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (final String str : splitCodecs) {
            if (n == MimeTypes.getTrackTypeOfCodec(str)) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(str);
            }
        }
        if (sb.length() > 0) {
            string = sb.toString();
        }
        return string;
    }
    
    public static String getCommaDelimitedSimpleClassNames(final Object[] array) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(array[i].getClass().getSimpleName());
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public static String getCountryCode(final Context context) {
        if (context != null) {
            final TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
            if (telephonyManager != null) {
                final String networkCountryIso = telephonyManager.getNetworkCountryIso();
                if (!TextUtils.isEmpty((CharSequence)networkCountryIso)) {
                    return toUpperInvariant(networkCountryIso);
                }
            }
        }
        return toUpperInvariant(Locale.getDefault().getCountry());
    }
    
    public static int getDefaultBufferSize(final int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException();
            }
            case 6: {
                return 0;
            }
            case 3:
            case 4:
            case 5: {
                return 131072;
            }
            case 2: {
                return 13107200;
            }
            case 1: {
                return 3538944;
            }
            case 0: {
                return 16777216;
            }
        }
    }
    
    @TargetApi(16)
    private static void getDisplaySizeV16(final Display display, final Point point) {
        display.getSize(point);
    }
    
    @TargetApi(17)
    private static void getDisplaySizeV17(final Display display, final Point point) {
        display.getRealSize(point);
    }
    
    @TargetApi(23)
    private static void getDisplaySizeV23(final Display display, final Point point) {
        final Display$Mode mode = display.getMode();
        point.x = mode.getPhysicalWidth();
        point.y = mode.getPhysicalHeight();
    }
    
    private static void getDisplaySizeV9(final Display display, final Point point) {
        point.x = display.getWidth();
        point.y = display.getHeight();
    }
    
    public static UUID getDrmUuid(final String name) {
        final String lowerInvariant = toLowerInvariant(name);
        final int hashCode = lowerInvariant.hashCode();
        int n = 0;
        Label_0081: {
            if (hashCode != -1860423953) {
                if (hashCode != -1400551171) {
                    if (hashCode == 790309106) {
                        if (lowerInvariant.equals("clearkey")) {
                            n = 2;
                            break Label_0081;
                        }
                    }
                }
                else if (lowerInvariant.equals("widevine")) {
                    n = 0;
                    break Label_0081;
                }
            }
            else if (lowerInvariant.equals("playready")) {
                n = 1;
                break Label_0081;
            }
            n = -1;
        }
        if (n == 0) {
            return C.WIDEVINE_UUID;
        }
        if (n != 1) {
            if (n != 2) {
                try {
                    return UUID.fromString(name);
                }
                catch (RuntimeException ex) {
                    return null;
                }
            }
            return C.CLEARKEY_UUID;
        }
        return C.PLAYREADY_UUID;
    }
    
    public static int getIntegerCodeForString(final String s) {
        final int length = s.length();
        int i = 0;
        Assertions.checkArgument(length <= 4);
        int n = 0;
        while (i < length) {
            n = (n << 8 | s.charAt(i));
            ++i;
        }
        return n;
    }
    
    public static Looper getLooper() {
        Looper looper = Looper.myLooper();
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        return looper;
    }
    
    public static long getMediaDurationForPlayoutDuration(final long n, final float n2) {
        if (n2 == 1.0f) {
            return n;
        }
        final double v = (double)n;
        final double v2 = n2;
        Double.isNaN(v);
        Double.isNaN(v2);
        return Math.round(v * v2);
    }
    
    private static int getMobileNetworkType(final NetworkInfo networkInfo) {
        switch (networkInfo.getSubtype()) {
            default: {
                return 6;
            }
            case 18: {
                return 2;
            }
            case 13: {
                return 5;
            }
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
            case 17: {
                return 4;
            }
            case 1:
            case 2: {
                return 3;
            }
        }
    }
    
    public static int getNetworkType(final Context context) {
        if (context == null) {
            return 0;
        }
        final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return 0;
        }
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            final int type = activeNetworkInfo.getType();
            if (type != 0) {
                if (type == 1) {
                    return 2;
                }
                if (type != 4 && type != 5) {
                    if (type == 6) {
                        return 5;
                    }
                    if (type != 9) {
                        return 8;
                    }
                    return 7;
                }
            }
            return getMobileNetworkType(activeNetworkInfo);
        }
        return 1;
    }
    
    public static int getPcmEncoding(final int n) {
        if (n == 8) {
            return 3;
        }
        if (n == 16) {
            return 2;
        }
        if (n == 24) {
            return Integer.MIN_VALUE;
        }
        if (n != 32) {
            return 0;
        }
        return 1073741824;
    }
    
    public static int getPcmFrameSize(final int n, final int n2) {
        if (n != Integer.MIN_VALUE) {
            if (n != 1073741824) {
                if (n == 2) {
                    return n2 * 2;
                }
                if (n == 3) {
                    return n2;
                }
                if (n != 4) {
                    throw new IllegalArgumentException();
                }
            }
            return n2 * 4;
        }
        return n2 * 3;
    }
    
    public static Point getPhysicalDisplaySize(final Context context) {
        return getPhysicalDisplaySize(context, ((WindowManager)context.getSystemService("window")).getDefaultDisplay());
    }
    
    public static Point getPhysicalDisplaySize(Context str, final Display display) {
        Label_0190: {
            if (Util.SDK_INT > 28 || display.getDisplayId() != 0 || !isTv(str)) {
                break Label_0190;
            }
            if ("Sony".equals(Util.MANUFACTURER) && Util.MODEL.startsWith("BRAVIA") && str.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
                return new Point(3840, 2160);
            }
            if (Util.SDK_INT < 28) {
                str = (Context)getSystemProperty("sys.display-size");
            }
            else {
                str = (Context)getSystemProperty("vendor.display-size");
            }
            if (TextUtils.isEmpty((CharSequence)str)) {
                break Label_0190;
            }
            while (true) {
                try {
                    final String[] split = split(((String)str).trim(), "x");
                    if (split.length == 2) {
                        final int int1 = Integer.parseInt(split[0]);
                        final int int2 = Integer.parseInt(split[1]);
                        if (int1 > 0 && int2 > 0) {
                            return new Point(int1, int2);
                        }
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid display size: ");
                    sb.append((String)str);
                    Log.e("Util", sb.toString());
                    str = (Context)new Point();
                    final int sdk_INT = Util.SDK_INT;
                    if (sdk_INT >= 23) {
                        getDisplaySizeV23(display, (Point)str);
                    }
                    else if (sdk_INT >= 17) {
                        getDisplaySizeV17(display, (Point)str);
                    }
                    else if (sdk_INT >= 16) {
                        getDisplaySizeV16(display, (Point)str);
                    }
                    else {
                        getDisplaySizeV9(display, (Point)str);
                    }
                    return (Point)str;
                }
                catch (NumberFormatException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static long getPlayoutDurationForMediaDuration(final long n, final float n2) {
        if (n2 == 1.0f) {
            return n;
        }
        final double v = (double)n;
        final double v2 = n2;
        Double.isNaN(v);
        Double.isNaN(v2);
        return Math.round(v / v2);
    }
    
    public static RendererCapabilities[] getRendererCapabilities(final RenderersFactory renderersFactory, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        final Renderer[] renderers = renderersFactory.createRenderers(new Handler(), new VideoRendererEventListener() {}, new AudioRendererEventListener() {}, (TextOutput)_$$Lambda$Util$Re8Vg9XyGFeMHzExHNw9i73q_Y8.INSTANCE, (MetadataOutput)_$$Lambda$Util$M3KUmpUnPzRNYEBE___Y1k2KDhE.INSTANCE, drmSessionManager);
        final RendererCapabilities[] array = new RendererCapabilities[renderers.length];
        for (int i = 0; i < renderers.length; ++i) {
            array[i] = renderers[i].getCapabilities();
        }
        return array;
    }
    
    public static int getStreamTypeForAudioUsage(final int n) {
        switch (n) {
            default: {
                return 3;
            }
            case 13: {
                return 1;
            }
            case 6: {
                return 2;
            }
            case 5:
            case 7:
            case 8:
            case 9:
            case 10: {
                return 5;
            }
            case 4: {
                return 4;
            }
            case 3: {
                return 8;
            }
            case 2: {
                return 0;
            }
            case 1:
            case 12:
            case 14: {
                return 3;
            }
        }
    }
    
    public static String getStringForTime(final StringBuilder sb, final Formatter formatter, long n) {
        long n2 = n;
        if (n == -9223372036854775807L) {
            n2 = 0L;
        }
        final long n3 = (n2 + 500L) / 1000L;
        n = n3 % 60L;
        final long n4 = n3 / 60L % 60L;
        final long l = n3 / 3600L;
        sb.setLength(0);
        String s;
        if (l > 0L) {
            s = formatter.format("%d:%02d:%02d", l, n4, n).toString();
        }
        else {
            s = formatter.format("%02d:%02d", n4, n).toString();
        }
        return s;
    }
    
    private static String getSystemProperty(final String str) {
        try {
            final Class<?> forName = Class.forName("android.os.SystemProperties");
            return (String)forName.getMethod("get", String.class).invoke(forName, str);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to read system property ");
            sb.append(str);
            Log.e("Util", sb.toString(), ex);
            return null;
        }
    }
    
    public static String getUserAgent(final Context context, final String str) {
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            versionName = "?";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/");
        sb.append(versionName);
        sb.append(" (Linux;Android ");
        sb.append(Build$VERSION.RELEASE);
        sb.append(") ");
        sb.append("ExoPlayerLib/2.9.4");
        return sb.toString();
    }
    
    public static byte[] getUtf8Bytes(final String s) {
        return s.getBytes(Charset.forName("UTF-8"));
    }
    
    public static int inferContentType(final Uri uri) {
        final String path = uri.getPath();
        int inferContentType;
        if (path == null) {
            inferContentType = 3;
        }
        else {
            inferContentType = inferContentType(path);
        }
        return inferContentType;
    }
    
    public static int inferContentType(final Uri uri, final String str) {
        int n;
        if (TextUtils.isEmpty((CharSequence)str)) {
            n = inferContentType(uri);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(str);
            n = inferContentType(sb.toString());
        }
        return n;
    }
    
    public static int inferContentType(String lowerInvariant) {
        lowerInvariant = toLowerInvariant(lowerInvariant);
        if (lowerInvariant.endsWith(".mpd")) {
            return 0;
        }
        if (lowerInvariant.endsWith(".m3u8")) {
            return 2;
        }
        if (lowerInvariant.matches(".*\\.ism(l)?(/manifest(\\(.+\\))?)?")) {
            return 1;
        }
        return 3;
    }
    
    public static boolean inflate(final ParsableByteArray parsableByteArray, final ParsableByteArray parsableByteArray2, final Inflater inflater) {
        if (parsableByteArray.bytesLeft() <= 0) {
            return false;
        }
        byte[] array;
        if ((array = parsableByteArray2.data).length < parsableByteArray.bytesLeft()) {
            array = new byte[parsableByteArray.bytesLeft() * 2];
        }
        Inflater inflater2;
        if ((inflater2 = inflater) == null) {
            inflater2 = new Inflater();
        }
        inflater2.setInput(parsableByteArray.data, parsableByteArray.getPosition(), parsableByteArray.bytesLeft());
        int off = 0;
        try {
            while (true) {
                final int n = off + inflater2.inflate(array, off, array.length - off);
                if (inflater2.finished()) {
                    parsableByteArray2.reset(array, n);
                    return true;
                }
                if (inflater2.needsDictionary() || inflater2.needsInput()) {
                    return false;
                }
                if ((off = n) != array.length) {
                    continue;
                }
                array = Arrays.copyOf(array, array.length * 2);
                off = n;
            }
        }
        catch (DataFormatException ex) {
            return false;
        }
        finally {
            inflater2.reset();
        }
    }
    
    public static boolean isEncodingHighResolutionIntegerPcm(final int n) {
        return n == Integer.MIN_VALUE || n == 1073741824;
    }
    
    public static boolean isEncodingLinearPcm(final int n) {
        return n == 3 || n == 2 || n == Integer.MIN_VALUE || n == 1073741824 || n == 4;
    }
    
    public static boolean isLinebreak(final int n) {
        return n == 10 || n == 13;
    }
    
    public static boolean isLocalFileUri(final Uri uri) {
        final String scheme = uri.getScheme();
        return TextUtils.isEmpty((CharSequence)scheme) || "file".equals(scheme);
    }
    
    public static boolean isTv(final Context context) {
        final UiModeManager uiModeManager = (UiModeManager)context.getApplicationContext().getSystemService("uimode");
        return uiModeManager != null && uiModeManager.getCurrentModeType() == 4;
    }
    
    @TargetApi(23)
    public static boolean maybeRequestReadExternalStoragePermission(final Activity activity, final Uri... array) {
        if (Util.SDK_INT < 23) {
            return false;
        }
        final int length = array.length;
        int i = 0;
        while (i < length) {
            if (isLocalFileUri(array[i])) {
                if (activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                    activity.requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 0);
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    public static ExecutorService newSingleThreadExecutor(final String s) {
        return Executors.newSingleThreadExecutor(new _$$Lambda$Util$MRC4FgxCpRGDforKj_F0m_7VaCA(s));
    }
    
    public static String normalizeLanguageCode(String iso3Language) {
        if (iso3Language == null) {
            iso3Language = null;
            return iso3Language;
        }
        try {
            iso3Language = new Locale(iso3Language).getISO3Language();
            return iso3Language;
        }
        catch (MissingResourceException ex) {
            return toLowerInvariant(iso3Language);
        }
    }
    
    public static <T> T[] nullSafeArrayCopy(final T[] original, final int newLength) {
        Assertions.checkArgument(newLength <= original.length);
        return Arrays.copyOf(original, newLength);
    }
    
    public static long parseXsDateTime(String group) throws ParserException {
        final Matcher matcher = Util.XS_DATE_TIME_PATTERN.matcher(group);
        if (matcher.matches()) {
            group = matcher.group(9);
            int n = 0;
            if (group != null) {
                if (!matcher.group(9).equalsIgnoreCase("Z")) {
                    final int n2 = n = Integer.parseInt(matcher.group(12)) * 60 + Integer.parseInt(matcher.group(13));
                    if ("-".equals(matcher.group(11))) {
                        n = n2 * -1;
                    }
                }
            }
            final GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            gregorianCalendar.clear();
            gregorianCalendar.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
            if (!TextUtils.isEmpty((CharSequence)matcher.group(8))) {
                final StringBuilder sb = new StringBuilder();
                sb.append("0.");
                sb.append(matcher.group(8));
                gregorianCalendar.set(14, new BigDecimal(sb.toString()).movePointRight(3).intValue());
            }
            long timeInMillis = gregorianCalendar.getTimeInMillis();
            if (n != 0) {
                timeInMillis -= n * 60000;
            }
            return timeInMillis;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid date/time format: ");
        sb2.append(group);
        throw new ParserException(sb2.toString());
    }
    
    public static long parseXsDuration(String s) {
        final Matcher matcher = Util.XS_DURATION_PATTERN.matcher(s);
        if (matcher.matches()) {
            final boolean empty = TextUtils.isEmpty((CharSequence)matcher.group(1));
            s = matcher.group(3);
            double double1 = 0.0;
            double n;
            if (s != null) {
                n = Double.parseDouble(s) * 3.1556908E7;
            }
            else {
                n = 0.0;
            }
            s = matcher.group(5);
            double n2;
            if (s != null) {
                n2 = Double.parseDouble(s) * 2629739.0;
            }
            else {
                n2 = 0.0;
            }
            s = matcher.group(7);
            double n3;
            if (s != null) {
                n3 = Double.parseDouble(s) * 86400.0;
            }
            else {
                n3 = 0.0;
            }
            s = matcher.group(10);
            double n4;
            if (s != null) {
                n4 = 3600.0 * Double.parseDouble(s);
            }
            else {
                n4 = 0.0;
            }
            s = matcher.group(12);
            double n5;
            if (s != null) {
                n5 = Double.parseDouble(s) * 60.0;
            }
            else {
                n5 = 0.0;
            }
            s = matcher.group(14);
            if (s != null) {
                double1 = Double.parseDouble(s);
            }
            long n6 = (long)((n + n2 + n3 + n4 + n5 + double1) * 1000.0);
            if (true ^ empty) {
                n6 = -n6;
            }
            return n6;
        }
        return (long)(Double.parseDouble(s) * 3600.0 * 1000.0);
    }
    
    public static boolean readBoolean(final Parcel parcel) {
        return parcel.readInt() != 0;
    }
    
    public static void recursiveDelete(final File file) {
        final File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                recursiveDelete(listFiles[i]);
            }
        }
        file.delete();
    }
    
    public static <T> void removeRange(final List<T> list, final int n, final int n2) {
        if (n >= 0 && n2 <= list.size() && n <= n2) {
            if (n != n2) {
                list.subList(n, n2).clear();
            }
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public static long resolveSeekPositionUs(final long n, final SeekParameters seekParameters, final long n2, final long n3) {
        if (SeekParameters.EXACT.equals(seekParameters)) {
            return n;
        }
        final long subtractWithOverflowDefault = subtractWithOverflowDefault(n, seekParameters.toleranceBeforeUs, Long.MIN_VALUE);
        final long addWithOverflowDefault = addWithOverflowDefault(n, seekParameters.toleranceAfterUs, Long.MAX_VALUE);
        boolean b = true;
        final boolean b2 = subtractWithOverflowDefault <= n2 && n2 <= addWithOverflowDefault;
        if (subtractWithOverflowDefault > n3 || n3 > addWithOverflowDefault) {
            b = false;
        }
        if (b2 && b) {
            if (Math.abs(n2 - n) <= Math.abs(n3 - n)) {
                return n2;
            }
            return n3;
        }
        else {
            if (b2) {
                return n2;
            }
            if (b) {
                return n3;
            }
            return subtractWithOverflowDefault;
        }
    }
    
    public static long scaleLargeTimestamp(final long n, final long n2, final long n3) {
        if (n3 >= n2 && n3 % n2 == 0L) {
            return n / (n3 / n2);
        }
        if (n3 < n2 && n2 % n3 == 0L) {
            return n * (n2 / n3);
        }
        final double v = (double)n2;
        final double v2 = (double)n3;
        Double.isNaN(v);
        Double.isNaN(v2);
        final double n4 = v / v2;
        final double v3 = (double)n;
        Double.isNaN(v3);
        return (long)(v3 * n4);
    }
    
    public static long[] scaleLargeTimestamps(final List<Long> list, long n, final long n2) {
        final long[] array = new long[list.size()];
        final int n3 = 0;
        final int n4 = 0;
        int i = 0;
        if (n2 >= n && n2 % n == 0L) {
            n = n2 / n;
            while (i < array.length) {
                array[i] = list.get(i) / n;
                ++i;
            }
        }
        else if (n2 < n && n % n2 == 0L) {
            n /= n2;
            for (int j = n3; j < array.length; ++j) {
                array[j] = list.get(j) * n;
            }
        }
        else {
            final double v = (double)n;
            final double v2 = (double)n2;
            Double.isNaN(v);
            Double.isNaN(v2);
            final double n5 = v / v2;
            for (int k = n4; k < array.length; ++k) {
                final double v3 = list.get(k);
                Double.isNaN(v3);
                array[k] = (long)(v3 * n5);
            }
        }
        return array;
    }
    
    public static void scaleLargeTimestampsInPlace(final long[] array, long n, final long n2) {
        final int n3 = 0;
        final int n4 = 0;
        int i = 0;
        if (n2 >= n && n2 % n == 0L) {
            n = n2 / n;
            while (i < array.length) {
                array[i] /= n;
                ++i;
            }
        }
        else if (n2 < n && n % n2 == 0L) {
            n /= n2;
            for (int j = n3; j < array.length; ++j) {
                array[j] *= n;
            }
        }
        else {
            final double v = (double)n;
            final double v2 = (double)n2;
            Double.isNaN(v);
            Double.isNaN(v2);
            final double n5 = v / v2;
            for (int k = n4; k < array.length; ++k) {
                final double v3 = (double)array[k];
                Double.isNaN(v3);
                array[k] = (long)(v3 * n5);
            }
        }
    }
    
    private static boolean shouldEscapeCharacter(final char c) {
        return c == '\"' || c == '%' || c == '*' || c == '/' || c == ':' || c == '<' || c == '\\' || c == '|' || c == '>' || c == '?';
    }
    
    public static void sneakyThrow(final Throwable t) {
        sneakyThrowInternal(t);
        throw null;
    }
    
    private static <T extends Throwable> void sneakyThrowInternal(final Throwable t) throws T, Throwable {
        throw t;
    }
    
    public static String[] split(final String s, final String regex) {
        return s.split(regex, -1);
    }
    
    public static String[] splitAtFirst(final String s, final String regex) {
        return s.split(regex, 2);
    }
    
    public static String[] splitCodecs(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return new String[0];
        }
        return split(s.trim(), "(\\s*,\\s*)");
    }
    
    public static ComponentName startForegroundService(final Context context, final Intent intent) {
        if (Util.SDK_INT >= 26) {
            return context.startForegroundService(intent);
        }
        return context.startService(intent);
    }
    
    public static long subtractWithOverflowDefault(final long n, final long n2, final long n3) {
        final long n4 = n - n2;
        if (((n ^ n4) & (n2 ^ n)) < 0L) {
            return n3;
        }
        return n4;
    }
    
    public static int[] toArray(final List<Integer> list) {
        if (list == null) {
            return null;
        }
        final int size = list.size();
        final int[] array = new int[size];
        for (int i = 0; i < size; ++i) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        final byte[] array = new byte[4096];
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static String toLowerInvariant(String lowerCase) {
        if (lowerCase != null) {
            lowerCase = lowerCase.toLowerCase(Locale.US);
        }
        return lowerCase;
    }
    
    public static String toUpperInvariant(String upperCase) {
        if (upperCase != null) {
            upperCase = upperCase.toUpperCase(Locale.US);
        }
        return upperCase;
    }
    
    public static String unescapeFileName(final String s) {
        final int length = s.length();
        final int n = 0;
        int i = 0;
        int n2 = 0;
        while (i < length) {
            int n3 = n2;
            if (s.charAt(i) == '%') {
                n3 = n2 + 1;
            }
            ++i;
            n2 = n3;
        }
        if (n2 == 0) {
            return s;
        }
        final int capacity = length - n2 * 2;
        final StringBuilder sb = new StringBuilder(capacity);
        final Matcher matcher = Util.ESCAPED_CHARACTER_PATTERN.matcher(s);
        int end = n;
        while (n2 > 0 && matcher.find()) {
            final char c = (char)Integer.parseInt(matcher.group(1), 16);
            sb.append(s, end, matcher.start());
            sb.append(c);
            end = matcher.end();
            --n2;
        }
        if (end < length) {
            sb.append(s, end, length);
        }
        if (sb.length() != capacity) {
            return null;
        }
        return sb.toString();
    }
    
    public static void writeBoolean(final Parcel parcel, final boolean b) {
        parcel.writeInt((int)(b ? 1 : 0));
    }
}
