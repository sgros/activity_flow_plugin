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
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Parcel;
import android.security.NetworkSecurityPolicy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Display.Mode;
import android.view.Surface;
import android.view.WindowManager;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioRendererEventListener.C0148-CC;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener.C0246-CC;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;

public final class Util {
    private static final int[] CRC32_BYTES_MSBF = new int[]{0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
    public static final String DEVICE = Build.DEVICE;
    public static final String DEVICE_DEBUG_INFO;
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final Pattern ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
    public static final String MANUFACTURER = Build.MANUFACTURER;
    public static final String MODEL = Build.MODEL;
    public static final int SDK_INT = VERSION.SDK_INT;
    private static final String TAG = "Util";
    private static final Pattern XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d?\\d):?(\\d\\d)))?");
    private static final Pattern XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");

    /* renamed from: com.google.android.exoplayer2.util.Util$1 */
    static class C02341 implements VideoRendererEventListener {
        public /* synthetic */ void onDroppedFrames(int i, long j) {
            C0246-CC.$default$onDroppedFrames(this, i, j);
        }

        public /* synthetic */ void onRenderedFirstFrame(Surface surface) {
            C0246-CC.$default$onRenderedFirstFrame(this, surface);
        }

        public /* synthetic */ void onVideoDecoderInitialized(String str, long j, long j2) {
            C0246-CC.$default$onVideoDecoderInitialized(this, str, j, j2);
        }

        public /* synthetic */ void onVideoDisabled(DecoderCounters decoderCounters) {
            C0246-CC.$default$onVideoDisabled(this, decoderCounters);
        }

        public /* synthetic */ void onVideoEnabled(DecoderCounters decoderCounters) {
            C0246-CC.$default$onVideoEnabled(this, decoderCounters);
        }

        public /* synthetic */ void onVideoInputFormatChanged(Format format) {
            C0246-CC.$default$onVideoInputFormatChanged(this, format);
        }

        public /* synthetic */ void onVideoSizeChanged(int i, int i2, int i3, float f) {
            C0246-CC.$default$onVideoSizeChanged(this, i, i2, i3, f);
        }

        C02341() {
        }
    }

    /* renamed from: com.google.android.exoplayer2.util.Util$2 */
    static class C02352 implements AudioRendererEventListener {
        public /* synthetic */ void onAudioDecoderInitialized(String str, long j, long j2) {
            C0148-CC.$default$onAudioDecoderInitialized(this, str, j, j2);
        }

        public /* synthetic */ void onAudioDisabled(DecoderCounters decoderCounters) {
            C0148-CC.$default$onAudioDisabled(this, decoderCounters);
        }

        public /* synthetic */ void onAudioEnabled(DecoderCounters decoderCounters) {
            C0148-CC.$default$onAudioEnabled(this, decoderCounters);
        }

        public /* synthetic */ void onAudioInputFormatChanged(Format format) {
            C0148-CC.$default$onAudioInputFormatChanged(this, format);
        }

        public /* synthetic */ void onAudioSessionId(int i) {
            C0148-CC.$default$onAudioSessionId(this, i);
        }

        public /* synthetic */ void onAudioSinkUnderrun(int i, long j, long j2) {
            C0148-CC.$default$onAudioSinkUnderrun(this, i, j, j2);
        }

        C02352() {
        }
    }

    public static long addWithOverflowDefault(long j, long j2, long j3) {
        long j4 = j + j2;
        return ((j ^ j4) & (j2 ^ j4)) < 0 ? j3 : j4;
    }

    @EnsuresNonNull({"#1"})
    public static <T> T castNonNull(T t) {
        return t;
    }

    @EnsuresNonNull({"#1"})
    public static <T> T[] castNonNullTypeArray(T[] tArr) {
        return tArr;
    }

    public static int compareLong(long j, long j2) {
        return j < j2 ? -1 : j == j2 ? 0 : 1;
    }

    public static int getAudioContentTypeForStreamType(int i) {
        return i != 0 ? (i == 1 || i == 2 || i == 4 || i == 5 || i == 8) ? 4 : 2 : 1;
    }

    public static int getAudioUsageForStreamType(int i) {
        if (i == 0) {
            return 2;
        }
        if (i == 1) {
            return 13;
        }
        if (i == 2) {
            return 6;
        }
        int i2 = 4;
        if (i != 4) {
            i2 = 5;
            if (i != 5) {
                return i != 8 ? 1 : 3;
            }
        }
        return i2;
    }

    public static int getPcmEncoding(int i) {
        return i != 8 ? i != 16 ? i != 24 ? i != 32 ? 0 : 1073741824 : Integer.MIN_VALUE : 2 : 3;
    }

    public static int getStreamTypeForAudioUsage(int i) {
        switch (i) {
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
            case 13:
                return 1;
            default:
                return 3;
        }
    }

    public static boolean isEncodingHighResolutionIntegerPcm(int i) {
        return i == Integer.MIN_VALUE || i == 1073741824;
    }

    public static boolean isEncodingLinearPcm(int i) {
        return i == 3 || i == 2 || i == Integer.MIN_VALUE || i == 1073741824 || i == 4;
    }

    public static boolean isLinebreak(int i) {
        return i == 10 || i == 13;
    }

    static /* synthetic */ void lambda$getRendererCapabilities$1(List list) {
    }

    static /* synthetic */ void lambda$getRendererCapabilities$2(Metadata metadata) {
    }

    private static boolean shouldEscapeCharacter(char c) {
        return c == '\"' || c == '%' || c == '*' || c == '/' || c == ':' || c == '<' || c == '\\' || c == '|' || c == '>' || c == '?';
    }

    public static long subtractWithOverflowDefault(long j, long j2, long j3) {
        long j4 = j - j2;
        return ((j ^ j4) & (j2 ^ j)) < 0 ? j3 : j4;
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DEVICE);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(MODEL);
        stringBuilder.append(str);
        stringBuilder.append(MANUFACTURER);
        stringBuilder.append(str);
        stringBuilder.append(SDK_INT);
        DEVICE_DEBUG_INFO = stringBuilder.toString();
    }

    private Util() {
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static ComponentName startForegroundService(Context context, Intent intent) {
        if (SDK_INT >= 26) {
            return context.startForegroundService(intent);
        }
        return context.startService(intent);
    }

    @TargetApi(23)
    public static boolean maybeRequestReadExternalStoragePermission(Activity activity, Uri... uriArr) {
        if (SDK_INT < 23) {
            return false;
        }
        for (Uri isLocalFileUri : uriArr) {
            if (isLocalFileUri(isLocalFileUri)) {
                if (activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                    activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @TargetApi(24)
    public static boolean checkCleartextTrafficPermitted(Uri... uriArr) {
        if (SDK_INT < 24) {
            return true;
        }
        for (Uri uri : uriArr) {
            if ("http".equals(uri.getScheme())) {
                NetworkSecurityPolicy instance = NetworkSecurityPolicy.getInstance();
                String host = uri.getHost();
                Assertions.checkNotNull(host);
                if (!instance.isCleartextTrafficPermitted(host)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isLocalFileUri(Uri uri) {
        String scheme = uri.getScheme();
        return TextUtils.isEmpty(scheme) || "file".equals(scheme);
    }

    public static boolean areEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equals(obj2);
        }
    }

    public static boolean contains(Object[] objArr, Object obj) {
        for (Object areEqual : objArr) {
            if (areEqual(areEqual, obj)) {
                return true;
            }
        }
        return false;
    }

    public static <T> void removeRange(List<T> list, int i, int i2) {
        if (i < 0 || i2 > list.size() || i > i2) {
            throw new IllegalArgumentException();
        } else if (i != i2) {
            list.subList(i, i2).clear();
        }
    }

    public static <T> T[] nullSafeArrayCopy(T[] tArr, int i) {
        Assertions.checkArgument(i <= tArr.length);
        return Arrays.copyOf(tArr, i);
    }

    public static Handler createHandler(Callback callback) {
        return createHandler(getLooper(), callback);
    }

    public static Handler createHandler(Looper looper, Callback callback) {
        return new Handler(looper, callback);
    }

    public static Looper getLooper() {
        Looper myLooper = Looper.myLooper();
        return myLooper != null ? myLooper : Looper.getMainLooper();
    }

    public static ExecutorService newSingleThreadExecutor(String str) {
        return Executors.newSingleThreadExecutor(new C0231-$$Lambda$Util$MRC4FgxCpRGDforKj-F0m_7VaCA(str));
    }

    public static void closeQuietly(DataSource dataSource) {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static boolean readBoolean(Parcel parcel) {
        return parcel.readInt() != 0;
    }

    public static void writeBoolean(Parcel parcel, boolean z) {
        parcel.writeInt(z);
    }

    public static String normalizeLanguageCode(String str) {
        if (str == null) {
            str = null;
        } else {
            try {
                str = new Locale(str).getISO3Language();
            } catch (MissingResourceException unused) {
                return toLowerInvariant(str);
            }
        }
        return str;
    }

    public static String fromUtf8Bytes(byte[] bArr) {
        return new String(bArr, Charset.forName("UTF-8"));
    }

    public static String fromUtf8Bytes(byte[] bArr, int i, int i2) {
        return new String(bArr, i, i2, Charset.forName("UTF-8"));
    }

    public static byte[] getUtf8Bytes(String str) {
        return str.getBytes(Charset.forName("UTF-8"));
    }

    public static String[] split(String str, String str2) {
        return str.split(str2, -1);
    }

    public static String[] splitAtFirst(String str, String str2) {
        return str.split(str2, 2);
    }

    public static String toLowerInvariant(String str) {
        return str == null ? str : str.toLowerCase(Locale.US);
    }

    public static String toUpperInvariant(String str) {
        return str == null ? str : str.toUpperCase(Locale.US);
    }

    public static String formatInvariant(String str, Object... objArr) {
        return String.format(Locale.US, str, objArr);
    }

    public static int ceilDivide(int i, int i2) {
        return ((i + i2) - 1) / i2;
    }

    public static long ceilDivide(long j, long j2) {
        return ((j + j2) - 1) / j2;
    }

    public static int constrainValue(int i, int i2, int i3) {
        return Math.max(i2, Math.min(i, i3));
    }

    public static long constrainValue(long j, long j2, long j3) {
        return Math.max(j2, Math.min(j, j3));
    }

    public static float constrainValue(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f, f3));
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0018  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0015  */
    public static int binarySearchFloor(int[] r2, int r3, boolean r4, boolean r5) {
        /*
        r0 = java.util.Arrays.binarySearch(r2, r3);
        if (r0 >= 0) goto L_0x000a;
    L_0x0006:
        r0 = r0 + 2;
        r2 = -r0;
        goto L_0x0019;
    L_0x000a:
        r0 = r0 + -1;
        if (r0 < 0) goto L_0x0013;
    L_0x000e:
        r1 = r2[r0];
        if (r1 != r3) goto L_0x0013;
    L_0x0012:
        goto L_0x000a;
    L_0x0013:
        if (r4 == 0) goto L_0x0018;
    L_0x0015:
        r2 = r0 + 1;
        goto L_0x0019;
    L_0x0018:
        r2 = r0;
    L_0x0019:
        if (r5 == 0) goto L_0x0020;
    L_0x001b:
        r3 = 0;
        r2 = java.lang.Math.max(r3, r2);
    L_0x0020:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.binarySearchFloor(int[], int, boolean, boolean):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    public static int binarySearchFloor(long[] r4, long r5, boolean r7, boolean r8) {
        /*
        r0 = java.util.Arrays.binarySearch(r4, r5);
        if (r0 >= 0) goto L_0x000a;
    L_0x0006:
        r0 = r0 + 2;
        r4 = -r0;
        goto L_0x001b;
    L_0x000a:
        r0 = r0 + -1;
        if (r0 < 0) goto L_0x0015;
    L_0x000e:
        r1 = r4[r0];
        r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x000a;
    L_0x0015:
        if (r7 == 0) goto L_0x001a;
    L_0x0017:
        r4 = r0 + 1;
        goto L_0x001b;
    L_0x001a:
        r4 = r0;
    L_0x001b:
        if (r8 == 0) goto L_0x0022;
    L_0x001d:
        r5 = 0;
        r4 = java.lang.Math.max(r5, r4);
    L_0x0022:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.binarySearchFloor(long[], long, boolean, boolean):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0020  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001d  */
    public static <T extends java.lang.Comparable<? super T>> int binarySearchFloor(java.util.List<? extends java.lang.Comparable<? super T>> r2, T r3, boolean r4, boolean r5) {
        /*
        r0 = java.util.Collections.binarySearch(r2, r3);
        if (r0 >= 0) goto L_0x000a;
    L_0x0006:
        r0 = r0 + 2;
        r2 = -r0;
        goto L_0x0021;
    L_0x000a:
        r0 = r0 + -1;
        if (r0 < 0) goto L_0x001b;
    L_0x000e:
        r1 = r2.get(r0);
        r1 = (java.lang.Comparable) r1;
        r1 = r1.compareTo(r3);
        if (r1 != 0) goto L_0x001b;
    L_0x001a:
        goto L_0x000a;
    L_0x001b:
        if (r4 == 0) goto L_0x0020;
    L_0x001d:
        r2 = r0 + 1;
        goto L_0x0021;
    L_0x0020:
        r2 = r0;
    L_0x0021:
        if (r5 == 0) goto L_0x0028;
    L_0x0023:
        r3 = 0;
        r2 = java.lang.Math.max(r3, r2);
    L_0x0028:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.binarySearchFloor(java.util.List, java.lang.Comparable, boolean, boolean):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0018  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0015  */
    public static int binarySearchCeil(int[] r2, int r3, boolean r4, boolean r5) {
        /*
        r0 = java.util.Arrays.binarySearch(r2, r3);
        if (r0 >= 0) goto L_0x0009;
    L_0x0006:
        r3 = r0 ^ -1;
        goto L_0x0019;
    L_0x0009:
        r0 = r0 + 1;
        r1 = r2.length;
        if (r0 >= r1) goto L_0x0013;
    L_0x000e:
        r1 = r2[r0];
        if (r1 != r3) goto L_0x0013;
    L_0x0012:
        goto L_0x0009;
    L_0x0013:
        if (r4 == 0) goto L_0x0018;
    L_0x0015:
        r3 = r0 + -1;
        goto L_0x0019;
    L_0x0018:
        r3 = r0;
    L_0x0019:
        if (r5 == 0) goto L_0x0022;
    L_0x001b:
        r2 = r2.length;
        r2 = r2 + -1;
        r3 = java.lang.Math.min(r2, r3);
    L_0x0022:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.binarySearchCeil(int[], int, boolean, boolean):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    public static int binarySearchCeil(long[] r4, long r5, boolean r7, boolean r8) {
        /*
        r0 = java.util.Arrays.binarySearch(r4, r5);
        if (r0 >= 0) goto L_0x0009;
    L_0x0006:
        r5 = r0 ^ -1;
        goto L_0x001b;
    L_0x0009:
        r0 = r0 + 1;
        r1 = r4.length;
        if (r0 >= r1) goto L_0x0015;
    L_0x000e:
        r1 = r4[r0];
        r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x0009;
    L_0x0015:
        if (r7 == 0) goto L_0x001a;
    L_0x0017:
        r5 = r0 + -1;
        goto L_0x001b;
    L_0x001a:
        r5 = r0;
    L_0x001b:
        if (r8 == 0) goto L_0x0024;
    L_0x001d:
        r4 = r4.length;
        r4 = r4 + -1;
        r5 = java.lang.Math.min(r4, r5);
    L_0x0024:
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.binarySearchCeil(long[], long, boolean, boolean):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0020  */
    public static <T extends java.lang.Comparable<? super T>> int binarySearchCeil(java.util.List<? extends java.lang.Comparable<? super T>> r3, T r4, boolean r5, boolean r6) {
        /*
        r0 = java.util.Collections.binarySearch(r3, r4);
        if (r0 >= 0) goto L_0x0009;
    L_0x0006:
        r4 = r0 ^ -1;
        goto L_0x0024;
    L_0x0009:
        r1 = r3.size();
    L_0x000d:
        r0 = r0 + 1;
        if (r0 >= r1) goto L_0x001e;
    L_0x0011:
        r2 = r3.get(r0);
        r2 = (java.lang.Comparable) r2;
        r2 = r2.compareTo(r4);
        if (r2 != 0) goto L_0x001e;
    L_0x001d:
        goto L_0x000d;
    L_0x001e:
        if (r5 == 0) goto L_0x0023;
    L_0x0020:
        r4 = r0 + -1;
        goto L_0x0024;
    L_0x0023:
        r4 = r0;
    L_0x0024:
        if (r6 == 0) goto L_0x0030;
    L_0x0026:
        r3 = r3.size();
        r3 = r3 + -1;
        r4 = java.lang.Math.min(r3, r4);
    L_0x0030:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.binarySearchCeil(java.util.List, java.lang.Comparable, boolean, boolean):int");
    }

    public static long parseXsDuration(String str) {
        Matcher matcher = XS_DURATION_PATTERN.matcher(str);
        if (!matcher.matches()) {
            return (long) ((Double.parseDouble(str) * 3600.0d) * 1000.0d);
        }
        int isEmpty = 1 ^ TextUtils.isEmpty(matcher.group(1));
        String group = matcher.group(3);
        double d = 0.0d;
        double parseDouble = group != null ? Double.parseDouble(group) * 3.1556908E7d : 0.0d;
        group = matcher.group(5);
        parseDouble += group != null ? Double.parseDouble(group) * 2629739.0d : 0.0d;
        group = matcher.group(7);
        parseDouble += group != null ? Double.parseDouble(group) * 86400.0d : 0.0d;
        group = matcher.group(10);
        parseDouble += group != null ? 3600.0d * Double.parseDouble(group) : 0.0d;
        group = matcher.group(12);
        parseDouble += group != null ? Double.parseDouble(group) * 60.0d : 0.0d;
        String group2 = matcher.group(14);
        if (group2 != null) {
            d = Double.parseDouble(group2);
        }
        long j = (long) ((parseDouble + d) * 1000.0d);
        if (isEmpty != 0) {
            j = -j;
        }
        return j;
    }

    public static long parseXsDateTime(String str) throws ParserException {
        Matcher matcher = XS_DATE_TIME_PATTERN.matcher(str);
        if (matcher.matches()) {
            int i = 0;
            if (!(matcher.group(9) == null || matcher.group(9).equalsIgnoreCase("Z"))) {
                i = (Integer.parseInt(matcher.group(12)) * 60) + Integer.parseInt(matcher.group(13));
                if ("-".equals(matcher.group(11))) {
                    i *= -1;
                }
            }
            Calendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            gregorianCalendar.clear();
            gregorianCalendar.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
            if (!TextUtils.isEmpty(matcher.group(8))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("0.");
                stringBuilder.append(matcher.group(8));
                gregorianCalendar.set(14, new BigDecimal(stringBuilder.toString()).movePointRight(3).intValue());
            }
            long timeInMillis = gregorianCalendar.getTimeInMillis();
            return i != 0 ? timeInMillis - ((long) (i * 60000)) : timeInMillis;
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Invalid date/time format: ");
            stringBuilder2.append(str);
            throw new ParserException(stringBuilder2.toString());
        }
    }

    public static long scaleLargeTimestamp(long j, long j2, long j3) {
        if (j3 >= j2 && j3 % j2 == 0) {
            return j / (j3 / j2);
        }
        if (j3 < j2 && j2 % j3 == 0) {
            return j * (j2 / j3);
        }
        double d = (double) j2;
        double d2 = (double) j3;
        Double.isNaN(d);
        Double.isNaN(d2);
        d /= d2;
        double d3 = (double) j;
        Double.isNaN(d3);
        return (long) (d3 * d);
    }

    public static long[] scaleLargeTimestamps(List<Long> list, long j, long j2) {
        long[] jArr = new long[list.size()];
        int i = 0;
        if (j2 >= j && j2 % j == 0) {
            j2 /= j;
            while (i < jArr.length) {
                jArr[i] = ((Long) list.get(i)).longValue() / j2;
                i++;
            }
        } else if (j2 >= j || j % j2 != 0) {
            double d = (double) j;
            double d2 = (double) j2;
            Double.isNaN(d);
            Double.isNaN(d2);
            d /= d2;
            while (i < jArr.length) {
                d2 = (double) ((Long) list.get(i)).longValue();
                Double.isNaN(d2);
                jArr[i] = (long) (d2 * d);
                i++;
            }
        } else {
            j /= j2;
            while (i < jArr.length) {
                jArr[i] = ((Long) list.get(i)).longValue() * j;
                i++;
            }
        }
        return jArr;
    }

    public static void scaleLargeTimestampsInPlace(long[] jArr, long j, long j2) {
        int i = 0;
        if (j2 >= j && j2 % j == 0) {
            j2 /= j;
            while (i < jArr.length) {
                jArr[i] = jArr[i] / j2;
                i++;
            }
        } else if (j2 >= j || j % j2 != 0) {
            double d = (double) j;
            double d2 = (double) j2;
            Double.isNaN(d);
            Double.isNaN(d2);
            d /= d2;
            while (i < jArr.length) {
                d2 = (double) jArr[i];
                Double.isNaN(d2);
                jArr[i] = (long) (d2 * d);
                i++;
            }
        } else {
            j /= j2;
            while (i < jArr.length) {
                jArr[i] = jArr[i] * j;
                i++;
            }
        }
    }

    public static long getMediaDurationForPlayoutDuration(long j, float f) {
        if (f == 1.0f) {
            return j;
        }
        double d = (double) j;
        double d2 = (double) f;
        Double.isNaN(d);
        Double.isNaN(d2);
        return Math.round(d * d2);
    }

    public static long getPlayoutDurationForMediaDuration(long j, float f) {
        if (f == 1.0f) {
            return j;
        }
        double d = (double) j;
        double d2 = (double) f;
        Double.isNaN(d);
        Double.isNaN(d2);
        return Math.round(d / d2);
    }

    public static long resolveSeekPositionUs(long j, SeekParameters seekParameters, long j2, long j3) {
        if (SeekParameters.EXACT.equals(seekParameters)) {
            return j;
        }
        long subtractWithOverflowDefault = subtractWithOverflowDefault(j, seekParameters.toleranceBeforeUs, Long.MIN_VALUE);
        long addWithOverflowDefault = addWithOverflowDefault(j, seekParameters.toleranceAfterUs, TimestampAdjuster.DO_NOT_OFFSET);
        Object obj = 1;
        Object obj2 = (subtractWithOverflowDefault > j2 || j2 > addWithOverflowDefault) ? null : 1;
        if (subtractWithOverflowDefault > j3 || j3 > addWithOverflowDefault) {
            obj = null;
        }
        if (obj2 != null && obj != null) {
            return Math.abs(j2 - j) <= Math.abs(j3 - j) ? j2 : j3;
        } else {
            if (obj2 != null) {
                return j2;
            }
            return obj != null ? j3 : subtractWithOverflowDefault;
        }
    }

    public static int[] toArray(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ((Integer) list.get(i)).intValue();
        }
        return iArr;
    }

    public static int getIntegerCodeForString(String str) {
        int length = str.length();
        Assertions.checkArgument(length <= 4);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            i = (i << 8) | str.charAt(i2);
        }
        return i;
    }

    public static byte[] getBytesFromHexString(String str) {
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) ((Character.digit(str.charAt(i2), 16) << 4) + Character.digit(str.charAt(i2 + 1), 16));
        }
        return bArr;
    }

    public static String getCommaDelimitedSimpleClassNames(Object[] objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < objArr.length; i++) {
            stringBuilder.append(objArr[i].getClass().getSimpleName());
            if (i < objArr.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String getUserAgent(Context context, String str) {
        String str2;
        try {
            str2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException unused) {
            str2 = "?";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("/");
        stringBuilder.append(str2);
        stringBuilder.append(" (Linux;Android ");
        stringBuilder.append(VERSION.RELEASE);
        stringBuilder.append(") ");
        stringBuilder.append("ExoPlayerLib/2.9.4");
        return stringBuilder.toString();
    }

    public static String getCodecsOfType(String str, int i) {
        String[] splitCodecs = splitCodecs(str);
        String str2 = null;
        if (splitCodecs.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str3 : splitCodecs) {
            if (i == MimeTypes.getTrackTypeOfCodec(str3)) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(str3);
            }
        }
        if (stringBuilder.length() > 0) {
            str2 = stringBuilder.toString();
        }
        return str2;
    }

    public static String[] splitCodecs(String str) {
        if (TextUtils.isEmpty(str)) {
            return new String[0];
        }
        return split(str.trim(), "(\\s*,\\s*)");
    }

    public static int getAudioTrackChannelConfig(int i) {
        switch (i) {
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
                i = SDK_INT;
                return (i < 23 && i < 21) ? 0 : 6396;
            default:
                return 0;
        }
    }

    public static int getPcmFrameSize(int i, int i2) {
        if (i == Integer.MIN_VALUE) {
            return i2 * 3;
        }
        if (i != 1073741824) {
            if (i == 2) {
                return i2 * 2;
            }
            if (i == 3) {
                return i2;
            }
            if (i != 4) {
                throw new IllegalArgumentException();
            }
        }
        return i2 * 4;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003b  */
    public static java.util.UUID getDrmUuid(java.lang.String r5) {
        /*
        r0 = toLowerInvariant(r5);
        r1 = r0.hashCode();
        r2 = -1860423953; // 0xffffffff911c2eef float:-1.2320693E-28 double:NaN;
        r3 = 2;
        r4 = 1;
        if (r1 == r2) goto L_0x002e;
    L_0x000f:
        r2 = -1400551171; // 0xffffffffac8548fd float:-3.7881907E-12 double:NaN;
        if (r1 == r2) goto L_0x0024;
    L_0x0014:
        r2 = 790309106; // 0x2f1b28f2 float:1.4111715E-10 double:3.90464579E-315;
        if (r1 == r2) goto L_0x001a;
    L_0x0019:
        goto L_0x0038;
    L_0x001a:
        r1 = "clearkey";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0038;
    L_0x0022:
        r0 = 2;
        goto L_0x0039;
    L_0x0024:
        r1 = "widevine";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0038;
    L_0x002c:
        r0 = 0;
        goto L_0x0039;
    L_0x002e:
        r1 = "playready";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0038;
    L_0x0036:
        r0 = 1;
        goto L_0x0039;
    L_0x0038:
        r0 = -1;
    L_0x0039:
        if (r0 == 0) goto L_0x004c;
    L_0x003b:
        if (r0 == r4) goto L_0x0049;
    L_0x003d:
        if (r0 == r3) goto L_0x0046;
    L_0x003f:
        r5 = java.util.UUID.fromString(r5);	 Catch:{ RuntimeException -> 0x0044 }
        return r5;
    L_0x0044:
        r5 = 0;
        return r5;
    L_0x0046:
        r5 = com.google.android.exoplayer2.C0131C.CLEARKEY_UUID;
        return r5;
    L_0x0049:
        r5 = com.google.android.exoplayer2.C0131C.PLAYREADY_UUID;
        return r5;
    L_0x004c:
        r5 = com.google.android.exoplayer2.C0131C.WIDEVINE_UUID;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.Util.getDrmUuid(java.lang.String):java.util.UUID");
    }

    public static int inferContentType(Uri uri, String str) {
        if (TextUtils.isEmpty(str)) {
            return inferContentType(uri);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".");
        stringBuilder.append(str);
        return inferContentType(stringBuilder.toString());
    }

    public static int inferContentType(Uri uri) {
        String path = uri.getPath();
        if (path == null) {
            return 3;
        }
        return inferContentType(path);
    }

    public static int inferContentType(String str) {
        str = toLowerInvariant(str);
        if (str.endsWith(".mpd")) {
            return 0;
        }
        if (str.endsWith(".m3u8")) {
            return 2;
        }
        return str.matches(".*\\.ism(l)?(/manifest(\\(.+\\))?)?") ? 1 : 3;
    }

    public static String getStringForTime(StringBuilder stringBuilder, Formatter formatter, long j) {
        if (j == -9223372036854775807L) {
            j = 0;
        }
        j = (j + 500) / 1000;
        long j2 = j % 60;
        long j3 = (j / 60) % 60;
        j /= 3600;
        stringBuilder.setLength(0);
        if (j > 0) {
            return formatter.format("%d:%02d:%02d", new Object[]{Long.valueOf(j), Long.valueOf(j3), Long.valueOf(j2)}).toString();
        }
        return formatter.format("%02d:%02d", new Object[]{Long.valueOf(j3), Long.valueOf(j2)}).toString();
    }

    public static int getDefaultBufferSize(int i) {
        switch (i) {
            case 0:
                return ConnectionsManager.FileTypePhoto;
            case 1:
                return 3538944;
            case 2:
                return 13107200;
            case 3:
            case 4:
            case 5:
                return MessagesController.UPDATE_MASK_REORDER;
            case 6:
                return 0;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static String escapeFileName(String str) {
        int length = str.length();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            if (shouldEscapeCharacter(str.charAt(i3))) {
                i2++;
            }
        }
        if (i2 == 0) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder((i2 * 2) + length);
        while (i2 > 0) {
            int i4 = i + 1;
            char charAt = str.charAt(i);
            if (shouldEscapeCharacter(charAt)) {
                stringBuilder.append('%');
                stringBuilder.append(Integer.toHexString(charAt));
                i2--;
            } else {
                stringBuilder.append(charAt);
            }
            i = i4;
        }
        if (i < length) {
            stringBuilder.append(str, i, length);
        }
        return stringBuilder.toString();
    }

    public static String unescapeFileName(String str) {
        int i;
        int length = str.length();
        int i2 = 0;
        int i3 = 0;
        for (i = 0; i < length; i++) {
            if (str.charAt(i) == '%') {
                i3++;
            }
        }
        if (i3 == 0) {
            return str;
        }
        i = length - (i3 * 2);
        StringBuilder stringBuilder = new StringBuilder(i);
        Matcher matcher = ESCAPED_CHARACTER_PATTERN.matcher(str);
        while (i3 > 0 && matcher.find()) {
            char parseInt = (char) Integer.parseInt(matcher.group(1), 16);
            stringBuilder.append(str, i2, matcher.start());
            stringBuilder.append(parseInt);
            i2 = matcher.end();
            i3--;
        }
        if (i2 < length) {
            stringBuilder.append(str, i2, length);
        }
        if (stringBuilder.length() != i) {
            return null;
        }
        return stringBuilder.toString();
    }

    public static void sneakyThrow(Throwable th) {
        sneakyThrowInternal(th);
        throw null;
    }

    private static <T extends Throwable> void sneakyThrowInternal(Throwable th) throws Throwable {
        throw th;
    }

    public static void recursiveDelete(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File recursiveDelete : listFiles) {
                recursiveDelete(recursiveDelete);
            }
        }
        file.delete();
    }

    public static File createTempDirectory(Context context, String str) throws IOException {
        File createTempFile = createTempFile(context, str);
        createTempFile.delete();
        createTempFile.mkdir();
        return createTempFile;
    }

    public static File createTempFile(Context context, String str) throws IOException {
        return File.createTempFile(str, null, context.getCacheDir());
    }

    public static int crc(byte[] bArr, int i, int i2, int i3) {
        while (i < i2) {
            i3 = CRC32_BYTES_MSBF[((i3 >>> 24) ^ (bArr[i] & NalUnitUtil.EXTENDED_SAR)) & NalUnitUtil.EXTENDED_SAR] ^ (i3 << 8);
            i++;
        }
        return i3;
    }

    public static int getNetworkType(Context context) {
        if (context == null) {
            return 0;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return 0;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return 1;
        }
        int type = activeNetworkInfo.getType();
        if (type != 0) {
            if (type == 1) {
                return 2;
            }
            if (!(type == 4 || type == 5)) {
                if (type != 6) {
                    return type != 9 ? 8 : 7;
                } else {
                    return 5;
                }
            }
        }
        return getMobileNetworkType(activeNetworkInfo);
    }

    public static String getCountryCode(Context context) {
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                String networkCountryIso = telephonyManager.getNetworkCountryIso();
                if (!TextUtils.isEmpty(networkCountryIso)) {
                    return toUpperInvariant(networkCountryIso);
                }
            }
        }
        return toUpperInvariant(Locale.getDefault().getCountry());
    }

    public static boolean inflate(ParsableByteArray parsableByteArray, ParsableByteArray parsableByteArray2, Inflater inflater) {
        if (parsableByteArray.bytesLeft() <= 0) {
            return false;
        }
        byte[] bArr = parsableByteArray2.data;
        if (bArr.length < parsableByteArray.bytesLeft()) {
            bArr = new byte[(parsableByteArray.bytesLeft() * 2)];
        }
        if (inflater == null) {
            inflater = new Inflater();
        }
        inflater.setInput(parsableByteArray.data, parsableByteArray.getPosition(), parsableByteArray.bytesLeft());
        int i = 0;
        while (true) {
            try {
                i += inflater.inflate(bArr, i, bArr.length - i);
                if (inflater.finished()) {
                    parsableByteArray2.reset(bArr, i);
                    inflater.reset();
                    return true;
                } else if (inflater.needsDictionary()) {
                    break;
                } else if (inflater.needsInput()) {
                    break;
                } else if (i == bArr.length) {
                    bArr = Arrays.copyOf(bArr, bArr.length * 2);
                }
            } catch (DataFormatException unused) {
                inflater.reset();
                return false;
            } catch (Throwable th) {
                inflater.reset();
                throw th;
            }
        }
        inflater.reset();
        return false;
    }

    public static boolean isTv(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getApplicationContext().getSystemService("uimode");
        return uiModeManager != null && uiModeManager.getCurrentModeType() == 4;
    }

    public static Point getPhysicalDisplaySize(Context context) {
        return getPhysicalDisplaySize(context, ((WindowManager) context.getSystemService("window")).getDefaultDisplay());
    }

    public static Point getPhysicalDisplaySize(Context context, Display display) {
        int parseInt;
        if (SDK_INT <= 28 && display.getDisplayId() == 0 && isTv(context)) {
            if ("Sony".equals(MANUFACTURER) && MODEL.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
                return new Point(3840, 2160);
            }
            CharSequence systemProperty;
            if (SDK_INT < 28) {
                systemProperty = getSystemProperty("sys.display-size");
            } else {
                systemProperty = getSystemProperty("vendor.display-size");
            }
            if (!TextUtils.isEmpty(systemProperty)) {
                try {
                    String[] split = split(systemProperty.trim(), "x");
                    if (split.length == 2) {
                        int parseInt2 = Integer.parseInt(split[0]);
                        parseInt = Integer.parseInt(split[1]);
                        if (parseInt2 > 0 && parseInt > 0) {
                            return new Point(parseInt2, parseInt);
                        }
                    }
                } catch (NumberFormatException unused) {
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid display size: ");
                stringBuilder.append(systemProperty);
                Log.m14e(TAG, stringBuilder.toString());
            }
        }
        Point point = new Point();
        parseInt = SDK_INT;
        if (parseInt >= 23) {
            getDisplaySizeV23(display, point);
        } else if (parseInt >= 17) {
            getDisplaySizeV17(display, point);
        } else if (parseInt >= 16) {
            getDisplaySizeV16(display, point);
        } else {
            getDisplaySizeV9(display, point);
        }
        return point;
    }

    public static RendererCapabilities[] getRendererCapabilities(RenderersFactory renderersFactory, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        Renderer[] createRenderers = renderersFactory.createRenderers(new Handler(), new C02341(), new C02352(), C3352-$$Lambda$Util$Re8Vg9XyGFeMHzExHNw9i73q-Y8.INSTANCE, C3351-$$Lambda$Util$M3KUmpUnPzRNYEBE_--Y1k2KDhE.INSTANCE, drmSessionManager);
        RendererCapabilities[] rendererCapabilitiesArr = new RendererCapabilities[createRenderers.length];
        for (int i = 0; i < createRenderers.length; i++) {
            rendererCapabilitiesArr[i] = createRenderers[i].getCapabilities();
        }
        return rendererCapabilitiesArr;
    }

    private static String getSystemProperty(String str) {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{str});
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to read system property ");
            stringBuilder.append(str);
            Log.m15e(TAG, stringBuilder.toString(), e);
            return null;
        }
    }

    @TargetApi(23)
    private static void getDisplaySizeV23(Display display, Point point) {
        Mode mode = display.getMode();
        point.x = mode.getPhysicalWidth();
        point.y = mode.getPhysicalHeight();
    }

    @TargetApi(17)
    private static void getDisplaySizeV17(Display display, Point point) {
        display.getRealSize(point);
    }

    @TargetApi(16)
    private static void getDisplaySizeV16(Display display, Point point) {
        display.getSize(point);
    }

    private static void getDisplaySizeV9(Display display, Point point) {
        point.x = display.getWidth();
        point.y = display.getHeight();
    }

    private static int getMobileNetworkType(NetworkInfo networkInfo) {
        switch (networkInfo.getSubtype()) {
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
            case 18:
                return 2;
            default:
                return 6;
        }
    }
}
