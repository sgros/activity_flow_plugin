// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal;

import java.util.concurrent.ThreadFactory;
import java.io.InterruptedIOException;
import okio.Buffer;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import java.util.Locale;
import java.net.IDN;
import java.util.concurrent.TimeUnit;
import okio.Source;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.Closeable;
import java.io.IOException;
import okio.BufferedSource;
import okhttp3.MediaType;
import java.util.regex.Pattern;
import okio.ByteString;
import java.nio.charset.Charset;
import java.util.TimeZone;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;

public final class Util
{
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final RequestBody EMPTY_REQUEST;
    public static final ResponseBody EMPTY_RESPONSE;
    public static final String[] EMPTY_STRING_ARRAY;
    public static final TimeZone UTC;
    private static final Charset UTF_16_BE;
    private static final ByteString UTF_16_BE_BOM;
    private static final Charset UTF_16_LE;
    private static final ByteString UTF_16_LE_BOM;
    private static final Charset UTF_32_BE;
    private static final ByteString UTF_32_BE_BOM;
    private static final Charset UTF_32_LE;
    private static final ByteString UTF_32_LE_BOM;
    public static final Charset UTF_8;
    private static final ByteString UTF_8_BOM;
    private static final Pattern VERIFY_AS_IP_ADDRESS;
    
    static {
        EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_STRING_ARRAY = new String[0];
        EMPTY_RESPONSE = ResponseBody.create(null, Util.EMPTY_BYTE_ARRAY);
        EMPTY_REQUEST = RequestBody.create(null, Util.EMPTY_BYTE_ARRAY);
        UTF_8_BOM = ByteString.decodeHex("efbbbf");
        UTF_16_BE_BOM = ByteString.decodeHex("feff");
        UTF_16_LE_BOM = ByteString.decodeHex("fffe");
        UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
        UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
        UTF_8 = Charset.forName("UTF-8");
        UTF_16_BE = Charset.forName("UTF-16BE");
        UTF_16_LE = Charset.forName("UTF-16LE");
        UTF_32_BE = Charset.forName("UTF-32BE");
        UTF_32_LE = Charset.forName("UTF-32LE");
        UTC = TimeZone.getTimeZone("GMT");
        VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    }
    
    private Util() {
    }
    
    public static Charset bomAwareCharset(final BufferedSource bufferedSource, Charset charset) throws IOException {
        if (bufferedSource.rangeEquals(0L, Util.UTF_8_BOM)) {
            bufferedSource.skip(Util.UTF_8_BOM.size());
            charset = Util.UTF_8;
        }
        else if (bufferedSource.rangeEquals(0L, Util.UTF_16_BE_BOM)) {
            bufferedSource.skip(Util.UTF_16_BE_BOM.size());
            charset = Util.UTF_16_BE;
        }
        else if (bufferedSource.rangeEquals(0L, Util.UTF_16_LE_BOM)) {
            bufferedSource.skip(Util.UTF_16_LE_BOM.size());
            charset = Util.UTF_16_LE;
        }
        else if (bufferedSource.rangeEquals(0L, Util.UTF_32_BE_BOM)) {
            bufferedSource.skip(Util.UTF_32_BE_BOM.size());
            charset = Util.UTF_32_BE;
        }
        else if (bufferedSource.rangeEquals(0L, Util.UTF_32_LE_BOM)) {
            bufferedSource.skip(Util.UTF_32_LE_BOM.size());
            charset = Util.UTF_32_LE;
        }
        return charset;
    }
    
    public static void checkOffsetAndCount(final long n, final long n2, final long n3) {
        if ((n2 | n3) < 0L || n2 > n || n - n2 < n3) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static void closeQuietly(final ServerSocket serverSocket) {
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static void closeQuietly(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        }
        catch (AssertionError assertionError) {
            if (!isAndroidGetsocknameError(assertionError)) {
                throw assertionError;
            }
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static String[] concat(final String[] array, final String s) {
        final String[] array2 = new String[array.length + 1];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array2.length - 1] = s;
        return array2;
    }
    
    private static boolean containsInvalidHostnameAsciiCodes(final String s) {
        final boolean b = true;
        int i = 0;
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            boolean b2 = b;
            if (char1 > '\u001f') {
                if (char1 >= '\u007f') {
                    b2 = b;
                }
                else {
                    b2 = b;
                    if (" #%/:?@[\\]".indexOf(char1) == -1) {
                        ++i;
                        continue;
                    }
                }
            }
            return b2;
        }
        return false;
    }
    
    public static int delimiterOffset(final String s, int i, final int n, final char c) {
        while (i < n) {
            if (s.charAt(i) == c) {
                return i;
            }
            ++i;
        }
        i = n;
        return i;
    }
    
    public static int delimiterOffset(final String s, int i, final int n, final String s2) {
        while (i < n) {
            if (s2.indexOf(s.charAt(i)) != -1) {
                return i;
            }
            ++i;
        }
        i = n;
        return i;
    }
    
    public static boolean discard(final Source source, final int n, final TimeUnit timeUnit) {
        try {
            return skipAll(source, n, timeUnit);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public static String domainToAscii(String lowerCase) {
        try {
            lowerCase = IDN.toASCII(lowerCase).toLowerCase(Locale.US);
            if (lowerCase.isEmpty()) {
                lowerCase = null;
            }
            else if (containsInvalidHostnameAsciiCodes(lowerCase)) {
                lowerCase = null;
            }
            return lowerCase;
        }
        catch (IllegalArgumentException ex) {
            lowerCase = null;
            return lowerCase;
        }
    }
    
    public static boolean equal(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
    
    public static String format(final String format, final Object... args) {
        return String.format(Locale.US, format, args);
    }
    
    public static String hostHeader(final HttpUrl httpUrl, final boolean b) {
        String str;
        if (httpUrl.host().contains(":")) {
            str = "[" + httpUrl.host() + "]";
        }
        else {
            str = httpUrl.host();
        }
        if (!b) {
            final String string = str;
            if (httpUrl.port() == HttpUrl.defaultPort(httpUrl.scheme())) {
                return string;
            }
        }
        return str + ":" + httpUrl.port();
    }
    
    public static <T> List<T> immutableList(final List<T> c) {
        return Collections.unmodifiableList((List<? extends T>)new ArrayList<T>((Collection<? extends T>)c));
    }
    
    public static <T> List<T> immutableList(final T... array) {
        return Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])array.clone()));
    }
    
    public static <T> int indexOf(final T[] array, final T t) {
        for (int i = 0; i < array.length; ++i) {
            if (equal(array[i], t)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOfControlOrNonAscii(final String s) {
        int i = 0;
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            int n = i;
            if (char1 > '\u001f') {
                if (char1 < '\u007f') {
                    ++i;
                    continue;
                }
                n = i;
            }
            return n;
        }
        return -1;
    }
    
    private static <T> List<T> intersect(final T[] array, final T[] array2) {
        final ArrayList<T> list = new ArrayList<T>();
        for (final T t : array) {
            for (final T obj : array2) {
                if (t.equals(obj)) {
                    list.add(obj);
                    break;
                }
            }
        }
        return list;
    }
    
    public static <T> T[] intersect(final Class<T> componentType, final T[] array, final T[] array2) {
        final List<T> intersect = intersect(array, array2);
        return intersect.toArray((T[])Array.newInstance(componentType, intersect.size()));
    }
    
    public static boolean isAndroidGetsocknameError(final AssertionError assertionError) {
        return assertionError.getCause() != null && assertionError.getMessage() != null && assertionError.getMessage().contains("getsockname failed");
    }
    
    public static boolean skipAll(final Source source, final int n, final TimeUnit timeUnit) throws IOException {
        final long nanoTime = System.nanoTime();
        Label_0110: {
            if (!source.timeout().hasDeadline()) {
                break Label_0110;
            }
            long a = source.timeout().deadlineNanoTime() - nanoTime;
            while (true) {
                source.timeout().deadlineNanoTime(Math.min(a, timeUnit.toNanos(n)) + nanoTime);
                Label_0118: {
                    try {
                        final Buffer buffer = new Buffer();
                        while (source.read(buffer, 8192L) != -1L) {
                            buffer.clear();
                        }
                        break Label_0118;
                    }
                    catch (InterruptedIOException ex) {
                        if (a == Long.MAX_VALUE) {
                            source.timeout().clearDeadline();
                        }
                        else {
                            source.timeout().deadlineNanoTime(nanoTime + a);
                        }
                        return false;
                        // iftrue(Label_0143:, a != 9223372036854775807L)
                        while (true) {
                            source.timeout().clearDeadline();
                            b = true;
                            return b;
                            continue;
                        }
                        Label_0143: {
                            source.timeout().deadlineNanoTime(nanoTime + a);
                        }
                        return true;
                        a = Long.MAX_VALUE;
                    }
                    finally {
                        while (true) {
                            if (a == Long.MAX_VALUE) {
                                source.timeout().clearDeadline();
                                break Label_0197;
                            }
                            source.timeout().deadlineNanoTime(nanoTime + a);
                            break Label_0197;
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public static int skipLeadingAsciiWhitespace(final String s, int i, final int n) {
        while (i < n) {
            switch (s.charAt(i)) {
                default: {
                    return i;
                }
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ': {
                    ++i;
                    continue;
                }
            }
        }
        i = n;
        return i;
    }
    
    public static int skipTrailingAsciiWhitespace(final String s, final int n, int n2) {
        int index = n2 - 1;
    Label_0072:
        while (true) {
            n2 = n;
            if (index < n) {
                break;
            }
            switch (s.charAt(index)) {
                default: {
                    n2 = index + 1;
                    break Label_0072;
                }
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ': {
                    --index;
                    continue;
                }
            }
        }
        return n2;
    }
    
    public static ThreadFactory threadFactory(final String s, final boolean b) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable target) {
                final Thread thread = new Thread(target, s);
                thread.setDaemon(b);
                return thread;
            }
        };
    }
    
    public static String toHumanReadableAscii(final String s) {
        int i = 0;
        final int length = s.length();
        String utf8;
        while (true) {
            utf8 = s;
            if (i >= length) {
                break;
            }
            final int codePoint = s.codePointAt(i);
            if (codePoint <= 31 || codePoint >= 127) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, 0, i);
                while (i < length) {
                    final int codePoint2 = s.codePointAt(i);
                    int n;
                    if (codePoint2 > 31 && codePoint2 < 127) {
                        n = codePoint2;
                    }
                    else {
                        n = 63;
                    }
                    buffer.writeUtf8CodePoint(n);
                    i += Character.charCount(codePoint2);
                }
                utf8 = buffer.readUtf8();
                break;
            }
            i += Character.charCount(codePoint);
        }
        return utf8;
    }
    
    public static String trimSubstring(final String s, int skipLeadingAsciiWhitespace, final int n) {
        skipLeadingAsciiWhitespace = skipLeadingAsciiWhitespace(s, skipLeadingAsciiWhitespace, n);
        return s.substring(skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace(s, skipLeadingAsciiWhitespace, n));
    }
    
    public static boolean verifyAsIpAddress(final String input) {
        return Util.VERIFY_AS_IP_ADDRESS.matcher(input).matches();
    }
}
