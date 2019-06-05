// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import java.util.Arrays;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collection;
import okhttp3.internal.Util;
import java.util.Collections;
import java.util.ArrayList;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import okio.Buffer;
import java.util.List;

public final class HttpUrl
{
    static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    static final String FRAGMENT_ENCODE_SET = "";
    static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
    private static final char[] HEX_DIGITS;
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
    static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    private final String fragment;
    final String host;
    private final String password;
    private final List<String> pathSegments;
    final int port;
    private final List<String> queryNamesAndValues;
    final String scheme;
    private final String url;
    private final String username;
    
    static {
        HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    HttpUrl(final Builder builder) {
        final String s = null;
        this.scheme = builder.scheme;
        this.username = percentDecode(builder.encodedUsername, false);
        this.password = percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = this.percentDecode(builder.encodedPathSegments, false);
        List<String> percentDecode;
        if (builder.encodedQueryNamesAndValues != null) {
            percentDecode = this.percentDecode(builder.encodedQueryNamesAndValues, true);
        }
        else {
            percentDecode = null;
        }
        this.queryNamesAndValues = percentDecode;
        String percentDecode2 = s;
        if (builder.encodedFragment != null) {
            percentDecode2 = percentDecode(builder.encodedFragment, false);
        }
        this.fragment = percentDecode2;
        this.url = builder.toString();
    }
    
    static String canonicalize(String s, final int beginIndex, final int endIndex, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        int codePoint;
        for (int i = beginIndex; i < endIndex; i += Character.charCount(codePoint)) {
            codePoint = s.codePointAt(i);
            if (codePoint < 32 || codePoint == 127 || (codePoint >= 128 && b4) || s2.indexOf(codePoint) != -1 || (codePoint == 37 && (!b || (b2 && !percentEncoded(s, i, endIndex)))) || (codePoint == 43 && b3)) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, beginIndex, i);
                canonicalize(buffer, s, i, endIndex, s2, b, b2, b3, b4);
                s = buffer.readUtf8();
                return s;
            }
        }
        s = s.substring(beginIndex, endIndex);
        return s;
    }
    
    static String canonicalize(final String s, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        return canonicalize(s, 0, s.length(), s2, b, b2, b3, b4);
    }
    
    static void canonicalize(final Buffer buffer, final String s, int i, final int n, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        Buffer buffer2 = null;
    Label_0064_Outer:
        while (i < n) {
            final int codePoint = s.codePointAt(i);
        Label_0064:
            while (true) {
                Label_0079: {
                    if (!b) {
                        break Label_0079;
                    }
                    Buffer buffer3 = buffer2;
                    if (codePoint != 9) {
                        buffer3 = buffer2;
                        if (codePoint != 10) {
                            buffer3 = buffer2;
                            if (codePoint != 12) {
                                if (codePoint != 13) {
                                    break Label_0079;
                                }
                                buffer3 = buffer2;
                            }
                        }
                    }
                    i += Character.charCount(codePoint);
                    buffer2 = buffer3;
                    continue Label_0064_Outer;
                }
                if (codePoint == 43 && b3) {
                    String s3;
                    if (b) {
                        s3 = "+";
                    }
                    else {
                        s3 = "%2B";
                    }
                    buffer.writeUtf8(s3);
                    final Buffer buffer3 = buffer2;
                    continue Label_0064;
                }
                if (codePoint >= 32 && codePoint != 127 && (codePoint < 128 || !b4) && s2.indexOf(codePoint) == -1 && (codePoint != 37 || (b && (!b2 || percentEncoded(s, i, n))))) {
                    buffer.writeUtf8CodePoint(codePoint);
                    final Buffer buffer3 = buffer2;
                    continue Label_0064;
                }
                Buffer buffer4;
                if ((buffer4 = buffer2) == null) {
                    buffer4 = new Buffer();
                }
                buffer4.writeUtf8CodePoint(codePoint);
                while (true) {
                    final Buffer buffer3 = buffer4;
                    if (buffer4.exhausted()) {
                        continue Label_0064;
                    }
                    final int n2 = buffer4.readByte() & 0xFF;
                    buffer.writeByte(37);
                    buffer.writeByte((int)HttpUrl.HEX_DIGITS[n2 >> 4 & 0xF]);
                    buffer.writeByte((int)HttpUrl.HEX_DIGITS[n2 & 0xF]);
                }
                break;
            }
        }
    }
    
    static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            c -= 48;
        }
        else if (c >= 'a' && c <= 'f') {
            c = (char)(c - 'a' + 10);
        }
        else if (c >= 'A' && c <= 'F') {
            c = (char)(c - 'A' + 10);
        }
        else {
            c = (char)(-1);
        }
        return c;
    }
    
    public static int defaultPort(final String s) {
        int n;
        if (s.equals("http")) {
            n = 80;
        }
        else if (s.equals("https")) {
            n = 443;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    public static HttpUrl get(final URI uri) {
        return parse(uri.toString());
    }
    
    public static HttpUrl get(final URL url) {
        return parse(url.toString());
    }
    
    static HttpUrl getChecked(final String s) throws MalformedURLException, UnknownHostException {
        final Builder builder = new Builder();
        final ParseResult parse = builder.parse(null, s);
        switch (parse) {
            default: {
                throw new MalformedURLException("Invalid URL: " + parse + " for " + s);
            }
            case SUCCESS: {
                return builder.build();
            }
            case INVALID_HOST: {
                throw new UnknownHostException("Invalid host: " + s);
            }
        }
    }
    
    static void namesAndValuesToQueryString(final StringBuilder sb, final List<String> list) {
        for (int i = 0; i < list.size(); i += 2) {
            final String str = list.get(i);
            final String str2 = list.get(i + 1);
            if (i > 0) {
                sb.append('&');
            }
            sb.append(str);
            if (str2 != null) {
                sb.append('=');
                sb.append(str2);
            }
        }
    }
    
    public static HttpUrl parse(final String s) {
        HttpUrl build = null;
        final Builder builder = new Builder();
        if (builder.parse(null, s) == ParseResult.SUCCESS) {
            build = builder.build();
        }
        return build;
    }
    
    static void pathSegmentsToString(final StringBuilder sb, final List<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            sb.append('/');
            sb.append(list.get(i));
        }
    }
    
    static String percentDecode(String s, final int beginIndex, final int endIndex, final boolean b) {
        for (int i = beginIndex; i < endIndex; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '%' || (char1 == '+' && b)) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, beginIndex, i);
                percentDecode(buffer, s, i, endIndex, b);
                s = buffer.readUtf8();
                return s;
            }
        }
        s = s.substring(beginIndex, endIndex);
        return s;
    }
    
    static String percentDecode(final String s, final boolean b) {
        return percentDecode(s, 0, s.length(), b);
    }
    
    private List<String> percentDecode(final List<String> list, final boolean b) {
        final int size = list.size();
        final ArrayList list2 = new ArrayList<String>(size);
        for (int i = 0; i < size; ++i) {
            final String s = list.get(i);
            String percentDecode;
            if (s != null) {
                percentDecode = percentDecode(s, b);
            }
            else {
                percentDecode = null;
            }
            list2.add(percentDecode);
        }
        return Collections.unmodifiableList((List<? extends String>)list2);
    }
    
    static void percentDecode(final Buffer buffer, final String s, int i, final int n, final boolean b) {
    Label_0077_Outer:
        while (i < n) {
            final int codePoint = s.codePointAt(i);
            while (true) {
                Label_0110: {
                    if (codePoint == 37 && i + 2 < n) {
                        final int decodeHexDigit = decodeHexDigit(s.charAt(i + 1));
                        final int decodeHexDigit2 = decodeHexDigit(s.charAt(i + 2));
                        if (decodeHexDigit == -1 || decodeHexDigit2 == -1) {
                            break Label_0110;
                        }
                        buffer.writeByte((decodeHexDigit << 4) + decodeHexDigit2);
                        i += 2;
                    }
                    else {
                        if (codePoint != 43 || !b) {
                            break Label_0110;
                        }
                        buffer.writeByte(32);
                    }
                    i += Character.charCount(codePoint);
                    continue Label_0077_Outer;
                }
                buffer.writeUtf8CodePoint(codePoint);
                continue;
            }
        }
    }
    
    static boolean percentEncoded(final String s, final int index, final int n) {
        return index + 2 < n && s.charAt(index) == '%' && decodeHexDigit(s.charAt(index + 1)) != -1 && decodeHexDigit(s.charAt(index + 2)) != -1;
    }
    
    static List<String> queryStringToNamesAndValues(final String s) {
        final ArrayList<String> list = new ArrayList<String>();
        int n;
        for (int i = 0; i <= s.length(); i = n + 1) {
            if ((n = s.indexOf(38, i)) == -1) {
                n = s.length();
            }
            final int index = s.indexOf(61, i);
            if (index == -1 || index > n) {
                list.add(s.substring(i, n));
                list.add(null);
            }
            else {
                list.add(s.substring(i, index));
                list.add(s.substring(index + 1, n));
            }
        }
        return list;
    }
    
    public String encodedFragment() {
        String substring;
        if (this.fragment == null) {
            substring = null;
        }
        else {
            substring = this.url.substring(this.url.indexOf(35) + 1);
        }
        return substring;
    }
    
    public String encodedPassword() {
        String substring;
        if (this.password.isEmpty()) {
            substring = "";
        }
        else {
            substring = this.url.substring(this.url.indexOf(58, this.scheme.length() + 3) + 1, this.url.indexOf(64));
        }
        return substring;
    }
    
    public String encodedPath() {
        final int index = this.url.indexOf(47, this.scheme.length() + 3);
        return this.url.substring(index, Util.delimiterOffset(this.url, index, this.url.length(), "?#"));
    }
    
    public List<String> encodedPathSegments() {
        int i = this.url.indexOf(47, this.scheme.length() + 3);
        final int delimiterOffset = Util.delimiterOffset(this.url, i, this.url.length(), "?#");
        final ArrayList<String> list = new ArrayList<String>();
        while (i < delimiterOffset) {
            final int beginIndex = i + 1;
            i = Util.delimiterOffset(this.url, beginIndex, delimiterOffset, '/');
            list.add(this.url.substring(beginIndex, i));
        }
        return list;
    }
    
    public String encodedQuery() {
        String substring;
        if (this.queryNamesAndValues == null) {
            substring = null;
        }
        else {
            final int beginIndex = this.url.indexOf(63) + 1;
            substring = this.url.substring(beginIndex, Util.delimiterOffset(this.url, beginIndex + 1, this.url.length(), '#'));
        }
        return substring;
    }
    
    public String encodedUsername() {
        String substring;
        if (this.username.isEmpty()) {
            substring = "";
        }
        else {
            final int beginIndex = this.scheme.length() + 3;
            substring = this.url.substring(beginIndex, Util.delimiterOffset(this.url, beginIndex, this.url.length(), ":@"));
        }
        return substring;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof HttpUrl && ((HttpUrl)o).url.equals(this.url);
    }
    
    public String fragment() {
        return this.fragment;
    }
    
    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
    
    public String host() {
        return this.host;
    }
    
    public boolean isHttps() {
        return this.scheme.equals("https");
    }
    
    public Builder newBuilder() {
        final Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = this.encodedUsername();
        builder.encodedPassword = this.encodedPassword();
        builder.host = this.host;
        int port;
        if (this.port != defaultPort(this.scheme)) {
            port = this.port;
        }
        else {
            port = -1;
        }
        builder.port = port;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(this.encodedPathSegments());
        builder.encodedQuery(this.encodedQuery());
        builder.encodedFragment = this.encodedFragment();
        return builder;
    }
    
    public Builder newBuilder(final String s) {
        final Builder builder = new Builder();
        Builder builder2;
        if (builder.parse(this, s) == ParseResult.SUCCESS) {
            builder2 = builder;
        }
        else {
            builder2 = null;
        }
        return builder2;
    }
    
    public String password() {
        return this.password;
    }
    
    public List<String> pathSegments() {
        return this.pathSegments;
    }
    
    public int pathSize() {
        return this.pathSegments.size();
    }
    
    public int port() {
        return this.port;
    }
    
    public String query() {
        String string;
        if (this.queryNamesAndValues == null) {
            string = null;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            namesAndValuesToQueryString(sb, this.queryNamesAndValues);
            string = sb.toString();
        }
        return string;
    }
    
    public String queryParameter(final String s) {
        final String s2 = null;
        String s3;
        if (this.queryNamesAndValues == null) {
            s3 = s2;
        }
        else {
            int n = 0;
            final int size = this.queryNamesAndValues.size();
            while (true) {
                s3 = s2;
                if (n >= size) {
                    return s3;
                }
                if (s.equals(this.queryNamesAndValues.get(n))) {
                    break;
                }
                n += 2;
            }
            s3 = this.queryNamesAndValues.get(n + 1);
        }
        return s3;
    }
    
    public String queryParameterName(final int n) {
        if (this.queryNamesAndValues == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.queryNamesAndValues.get(n * 2);
    }
    
    public Set<String> queryParameterNames() {
        Set<String> set;
        if (this.queryNamesAndValues == null) {
            set = Collections.emptySet();
        }
        else {
            final LinkedHashSet<String> s = new LinkedHashSet<String>();
            for (int i = 0; i < this.queryNamesAndValues.size(); i += 2) {
                s.add(this.queryNamesAndValues.get(i));
            }
            set = (Set<String>)Collections.unmodifiableSet((Set<?>)s);
        }
        return set;
    }
    
    public String queryParameterValue(final int n) {
        if (this.queryNamesAndValues == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.queryNamesAndValues.get(n * 2 + 1);
    }
    
    public List<String> queryParameterValues(final String s) {
        List<String> list;
        if (this.queryNamesAndValues == null) {
            list = Collections.emptyList();
        }
        else {
            final ArrayList<String> list2 = new ArrayList<String>();
            for (int i = 0; i < this.queryNamesAndValues.size(); i += 2) {
                if (s.equals(this.queryNamesAndValues.get(i))) {
                    list2.add(this.queryNamesAndValues.get(i + 1));
                }
            }
            list = (List<String>)Collections.unmodifiableList((List<?>)list2);
        }
        return list;
    }
    
    public int querySize() {
        int n;
        if (this.queryNamesAndValues != null) {
            n = this.queryNamesAndValues.size() / 2;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    public String redact() {
        return this.newBuilder("/...").username("").password("").build().toString();
    }
    
    public HttpUrl resolve(final String s) {
        final Builder builder = this.newBuilder(s);
        HttpUrl build;
        if (builder != null) {
            build = builder.build();
        }
        else {
            build = null;
        }
        return build;
    }
    
    public String scheme() {
        return this.scheme;
    }
    
    @Override
    public String toString() {
        return this.url;
    }
    
    public URI uri() {
        final String string = this.newBuilder().reencodeForUri().toString();
        try {
            return new URI(string);
        }
        catch (URISyntaxException cause) {
            try {
                final URI create = URI.create(string.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
            }
            catch (Exception ex) {
                throw new RuntimeException(cause);
            }
        }
    }
    
    public URL url() {
        try {
            return new URL(this.url);
        }
        catch (MalformedURLException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    public String username() {
        return this.username;
    }
    
    public static final class Builder
    {
        String encodedFragment;
        String encodedPassword;
        final List<String> encodedPathSegments;
        List<String> encodedQueryNamesAndValues;
        String encodedUsername;
        String host;
        int port;
        String scheme;
        
        public Builder() {
            this.encodedUsername = "";
            this.encodedPassword = "";
            this.port = -1;
            (this.encodedPathSegments = new ArrayList<String>()).add("");
        }
        
        private Builder addPathSegments(final String s, final boolean b) {
            int n = 0;
            int i;
            do {
                i = Util.delimiterOffset(s, n, s.length(), "/\\");
                this.push(s, n, i, i < s.length(), b);
                n = ++i;
            } while (i <= s.length());
            return this;
        }
        
        private static String canonicalizeHost(String s, final int n, final int n2) {
            s = HttpUrl.percentDecode(s, n, n2, false);
            if (s.contains(":")) {
                InetAddress inetAddress;
                if (s.startsWith("[") && s.endsWith("]")) {
                    inetAddress = decodeIpv6(s, 1, s.length() - 1);
                }
                else {
                    inetAddress = decodeIpv6(s, 0, s.length());
                }
                if (inetAddress == null) {
                    s = null;
                }
                else {
                    final byte[] address = inetAddress.getAddress();
                    if (address.length != 16) {
                        throw new AssertionError();
                    }
                    s = inet6AddressToAscii(address);
                }
            }
            else {
                s = Util.domainToAscii(s);
            }
            return s;
        }
        
        private static boolean decodeIpv4Suffix(final String s, int n, final int n2, final byte[] array, final int n3) {
            final boolean b = false;
            int n4 = n3;
            int i = n;
            while (i < n2) {
                boolean b2;
                if (n4 == array.length) {
                    b2 = b;
                }
                else {
                    n = i;
                    if (n4 != n3) {
                        b2 = b;
                        if (s.charAt(i) != '.') {
                            return b2;
                        }
                        n = i + 1;
                    }
                    int n5 = 0;
                    for (i = n; i < n2; ++i) {
                        final char char1 = s.charAt(i);
                        if (char1 < '0' || char1 > '9') {
                            break;
                        }
                        if (n5 == 0) {
                            b2 = b;
                            if (n != i) {
                                return b2;
                            }
                        }
                        n5 = n5 * 10 + char1 - 48;
                        b2 = b;
                        if (n5 > 255) {
                            return b2;
                        }
                    }
                    b2 = b;
                    if (i - n != 0) {
                        array[n4] = (byte)n5;
                        ++n4;
                        continue;
                    }
                }
                return b2;
            }
            boolean b2 = b;
            if (n4 == n3 + 4) {
                b2 = true;
                return b2;
            }
            return b2;
        }
        
        private static InetAddress decodeIpv6(final String s, int i, final int n) {
            final byte[] array = new byte[16];
            int n2 = 0;
            int n3 = -1;
            int n4 = -1;
            int toffset = i;
            while (true) {
                Label_0215: {
                    int fromIndex;
                    while (true) {
                        i = n2;
                        fromIndex = n3;
                        if (toffset < n) {
                            InetAddress byAddress;
                            if (n2 == array.length) {
                                byAddress = null;
                            }
                            else {
                                int n6;
                                int n7;
                                if (toffset + 2 <= n && s.regionMatches(toffset, "::", 0, 2)) {
                                    if (n3 != -1) {
                                        byAddress = null;
                                        return byAddress;
                                    }
                                    toffset += 2;
                                    n2 += 2;
                                    final int n5 = n2;
                                    n6 = n2;
                                    n7 = n5;
                                    if ((i = toffset) == n) {
                                        fromIndex = n5;
                                        i = n2;
                                        break;
                                    }
                                }
                                else {
                                    n6 = n2;
                                    n7 = n3;
                                    i = toffset;
                                    if (n2 != 0) {
                                        if (!s.regionMatches(toffset, ":", 0, 1)) {
                                            break Label_0215;
                                        }
                                        i = toffset + 1;
                                        n7 = n3;
                                        n6 = n2;
                                    }
                                }
                                int n8 = 0;
                                final int n9 = i;
                                while (i < n) {
                                    final int decodeHexDigit = HttpUrl.decodeHexDigit(s.charAt(i));
                                    if (decodeHexDigit == -1) {
                                        break;
                                    }
                                    n8 = (n8 << 4) + decodeHexDigit;
                                    ++i;
                                }
                                final int n10 = i - n9;
                                if (n10 == 0 || n10 > 4) {
                                    return null;
                                }
                                final int n11 = n6 + 1;
                                array[n6] = (byte)(n8 >>> 8 & 0xFF);
                                final int n12 = n11 + 1;
                                array[n11] = (byte)(n8 & 0xFF);
                                n2 = n12;
                                n3 = n7;
                                n4 = n9;
                                toffset = i;
                                continue;
                            }
                            return byAddress;
                        }
                        break;
                    }
                    if (i != array.length) {
                        if (fromIndex == -1) {
                            return null;
                        }
                        System.arraycopy(array, fromIndex, array, array.length - (i - fromIndex), i - fromIndex);
                        Arrays.fill(array, fromIndex, array.length - i + fromIndex, (byte)0);
                    }
                    try {
                        return InetAddress.getByAddress(array);
                    }
                    catch (UnknownHostException ex) {
                        throw new AssertionError();
                    }
                    return null;
                }
                if (!s.regionMatches(toffset, ".", 0, 1)) {
                    return null;
                }
                if (!decodeIpv4Suffix(s, n4, n, array, n2 - 2)) {
                    return null;
                }
                i = n2 + 2;
                int fromIndex = n3;
                continue;
            }
        }
        
        private static String inet6AddressToAscii(final byte[] array) {
            int n = -1;
            int n2 = 0;
            int n4;
            int n6;
            for (int i = 0; i < array.length; i = n4 + 2, n2 = n6) {
                int n3 = i;
                while (true) {
                    n4 = n3;
                    if (n4 >= 16 || array[n4] != 0 || array[n4 + 1] != 0) {
                        break;
                    }
                    n3 = n4 + 2;
                }
                final int n5 = n4 - i;
                if (n5 > (n6 = n2)) {
                    n6 = n5;
                    n = i;
                }
            }
            final Buffer buffer = new Buffer();
            int j = 0;
            while (j < array.length) {
                if (j == n) {
                    buffer.writeByte(58);
                    final int n7 = j + n2;
                    if ((j = n7) != 16) {
                        continue;
                    }
                    buffer.writeByte(58);
                    j = n7;
                }
                else {
                    if (j > 0) {
                        buffer.writeByte(58);
                    }
                    buffer.writeHexadecimalUnsignedLong((long)((array[j] & 0xFF) << 8 | (array[j + 1] & 0xFF)));
                    j += 2;
                }
            }
            return buffer.readUtf8();
        }
        
        private boolean isDot(final String s) {
            return s.equals(".") || s.equalsIgnoreCase("%2e");
        }
        
        private boolean isDotDot(final String s) {
            return s.equals("..") || s.equalsIgnoreCase("%2e.") || s.equalsIgnoreCase(".%2e") || s.equalsIgnoreCase("%2e%2e");
        }
        
        private static int parsePort(final String s, int int1, final int n) {
            try {
                int1 = Integer.parseInt(HttpUrl.canonicalize(s, int1, n, "", false, false, false, true));
                if (int1 <= 0 || int1 > 65535) {
                    int1 = -1;
                }
                return int1;
            }
            catch (NumberFormatException ex) {
                int1 = -1;
                return int1;
            }
        }
        
        private void pop() {
            if (this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1).isEmpty() && !this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
            }
            else {
                this.encodedPathSegments.add("");
            }
        }
        
        private static int portColonOffset(final String s, int i, final int n) {
            while (i < n) {
                int n2 = i;
                final int n3 = i;
                int n4 = 0;
                Label_0042: {
                    switch (s.charAt(i)) {
                        default: {
                            n4 = i;
                            break;
                        }
                        case '[': {
                            do {
                                i = n2 + 1;
                                if ((n4 = i) >= n) {
                                    break Label_0042;
                                }
                                n2 = i;
                            } while (s.charAt(i) != ']');
                            n4 = i;
                            break;
                        }
                        case ':': {
                            return n3;
                        }
                    }
                }
                i = n4 + 1;
            }
            return n;
        }
        
        private void push(String canonicalize, final int n, final int n2, final boolean b, final boolean b2) {
            canonicalize = HttpUrl.canonicalize(canonicalize, n, n2, " \"<>^`{}|/\\?#", b2, false, false, true);
            if (!this.isDot(canonicalize)) {
                if (this.isDotDot(canonicalize)) {
                    this.pop();
                }
                else {
                    if (this.encodedPathSegments.get(this.encodedPathSegments.size() - 1).isEmpty()) {
                        this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, canonicalize);
                    }
                    else {
                        this.encodedPathSegments.add(canonicalize);
                    }
                    if (b) {
                        this.encodedPathSegments.add("");
                    }
                }
            }
        }
        
        private void removeAllCanonicalQueryParameters(final String s) {
            for (int i = this.encodedQueryNamesAndValues.size() - 2; i >= 0; i -= 2) {
                if (s.equals(this.encodedQueryNamesAndValues.get(i))) {
                    this.encodedQueryNamesAndValues.remove(i + 1);
                    this.encodedQueryNamesAndValues.remove(i);
                    if (this.encodedQueryNamesAndValues.isEmpty()) {
                        this.encodedQueryNamesAndValues = null;
                        break;
                    }
                }
            }
        }
        
        private void resolvePath(final String s, int i, final int n) {
            if (i != n) {
                final char char1 = s.charAt(i);
                if (char1 == '/' || char1 == '\\') {
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.add("");
                    ++i;
                }
                else {
                    this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
                }
                while (i < n) {
                    final int delimiterOffset = Util.delimiterOffset(s, i, n, "/\\");
                    final boolean b = delimiterOffset < n;
                    this.push(s, i, delimiterOffset, b, true);
                    i = delimiterOffset;
                    if (b) {
                        i = delimiterOffset + 1;
                    }
                }
            }
        }
        
        private static int schemeDelimiterOffset(final String s, int i, final int n) {
            if (n - i < 2) {
                i = -1;
            }
            else {
                final char char1 = s.charAt(i);
                if ((char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z')) {
                    i = -1;
                }
                else {
                    ++i;
                    while (i < n) {
                        final char char2 = s.charAt(i);
                        if ((char2 >= 'a' && char2 <= 'z') || (char2 >= 'A' && char2 <= 'Z') || (char2 >= '0' && char2 <= '9') || char2 == '+' || char2 == '-' || char2 == '.') {
                            ++i;
                        }
                        else {
                            if (char2 != ':') {
                                i = -1;
                                return i;
                            }
                            return i;
                        }
                    }
                    i = -1;
                }
            }
            return i;
        }
        
        private static int slashCount(final String s, int i, final int n) {
            int n2 = 0;
            while (i < n) {
                final char char1 = s.charAt(i);
                if (char1 != '\\' && char1 != '/') {
                    break;
                }
                ++n2;
                ++i;
            }
            return n2;
        }
        
        public Builder addEncodedPathSegment(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            this.push(s, 0, s.length(), false, true);
            return this;
        }
        
        public Builder addEncodedPathSegments(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPathSegments == null");
            }
            return this.addPathSegments(s, true);
        }
        
        public Builder addEncodedQueryParameter(String canonicalize, final String s) {
            if (canonicalize == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList<String>();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(canonicalize, " \"'<>#&=", true, false, true, true));
            final List<String> encodedQueryNamesAndValues = this.encodedQueryNamesAndValues;
            if (s != null) {
                canonicalize = HttpUrl.canonicalize(s, " \"'<>#&=", true, false, true, true);
            }
            else {
                canonicalize = null;
            }
            encodedQueryNamesAndValues.add(canonicalize);
            return this;
        }
        
        public Builder addPathSegment(final String s) {
            if (s == null) {
                throw new NullPointerException("pathSegment == null");
            }
            this.push(s, 0, s.length(), false, false);
            return this;
        }
        
        public Builder addPathSegments(final String s) {
            if (s == null) {
                throw new NullPointerException("pathSegments == null");
            }
            return this.addPathSegments(s, false);
        }
        
        public Builder addQueryParameter(String canonicalize, final String s) {
            if (canonicalize == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList<String>();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(canonicalize, " \"'<>#&=", false, false, true, true));
            final List<String> encodedQueryNamesAndValues = this.encodedQueryNamesAndValues;
            if (s != null) {
                canonicalize = HttpUrl.canonicalize(s, " \"'<>#&=", false, false, true, true);
            }
            else {
                canonicalize = null;
            }
            encodedQueryNamesAndValues.add(canonicalize);
            return this;
        }
        
        public HttpUrl build() {
            if (this.scheme == null) {
                throw new IllegalStateException("scheme == null");
            }
            if (this.host == null) {
                throw new IllegalStateException("host == null");
            }
            return new HttpUrl(this);
        }
        
        int effectivePort() {
            int n;
            if (this.port != -1) {
                n = this.port;
            }
            else {
                n = HttpUrl.defaultPort(this.scheme);
            }
            return n;
        }
        
        public Builder encodedFragment(String canonicalize) {
            if (canonicalize != null) {
                canonicalize = HttpUrl.canonicalize(canonicalize, "", true, false, false, false);
            }
            else {
                canonicalize = null;
            }
            this.encodedFragment = canonicalize;
            return this;
        }
        
        public Builder encodedPassword(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPassword == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }
        
        public Builder encodedPath(final String str) {
            if (str == null) {
                throw new NullPointerException("encodedPath == null");
            }
            if (!str.startsWith("/")) {
                throw new IllegalArgumentException("unexpected encodedPath: " + str);
            }
            this.resolvePath(str, 0, str.length());
            return this;
        }
        
        public Builder encodedQuery(final String s) {
            List<String> queryStringToNamesAndValues;
            if (s != null) {
                queryStringToNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, " \"'<>#", true, false, true, true));
            }
            else {
                queryStringToNamesAndValues = null;
            }
            this.encodedQueryNamesAndValues = queryStringToNamesAndValues;
            return this;
        }
        
        public Builder encodedUsername(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedUsername == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }
        
        public Builder fragment(String canonicalize) {
            if (canonicalize != null) {
                canonicalize = HttpUrl.canonicalize(canonicalize, "", false, false, false, false);
            }
            else {
                canonicalize = null;
            }
            this.encodedFragment = canonicalize;
            return this;
        }
        
        public Builder host(final String str) {
            if (str == null) {
                throw new NullPointerException("host == null");
            }
            final String canonicalizeHost = canonicalizeHost(str, 0, str.length());
            if (canonicalizeHost == null) {
                throw new IllegalArgumentException("unexpected host: " + str);
            }
            this.host = canonicalizeHost;
            return this;
        }
        
        ParseResult parse(final HttpUrl httpUrl, final String s) {
            int skipLeadingAsciiWhitespace = Util.skipLeadingAsciiWhitespace(s, 0, s.length());
            final int skipTrailingAsciiWhitespace = Util.skipTrailingAsciiWhitespace(s, skipLeadingAsciiWhitespace, s.length());
            if (schemeDelimiterOffset(s, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace) != -1) {
                if (s.regionMatches(true, skipLeadingAsciiWhitespace, "https:", 0, 6)) {
                    this.scheme = "https";
                    skipLeadingAsciiWhitespace += "https:".length();
                }
                else {
                    if (!s.regionMatches(true, skipLeadingAsciiWhitespace, "http:", 0, 5)) {
                        return ParseResult.UNSUPPORTED_SCHEME;
                    }
                    this.scheme = "http";
                    skipLeadingAsciiWhitespace += "http:".length();
                }
            }
            else {
                if (httpUrl == null) {
                    return ParseResult.MISSING_SCHEME;
                }
                this.scheme = httpUrl.scheme;
            }
            int n = 0;
            final int n2 = 0;
            final int slashCount = slashCount(s, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace);
            int n6 = 0;
            Label_0535: {
                if (slashCount >= 2 || httpUrl == null || !httpUrl.scheme.equals(this.scheme)) {
                    final int n3 = skipLeadingAsciiWhitespace + slashCount;
                    int n4 = n2;
                    int n5 = n3;
                    int delimiterOffset = 0;
                Label_0207:
                    while (true) {
                        delimiterOffset = Util.delimiterOffset(s, n5, skipTrailingAsciiWhitespace, "@/\\?#");
                        int char1;
                        if (delimiterOffset != skipTrailingAsciiWhitespace) {
                            char1 = s.charAt(delimiterOffset);
                        }
                        else {
                            char1 = -1;
                        }
                        switch (char1) {
                            default: {
                                continue;
                            }
                            case -1:
                            case 35:
                            case 47:
                            case 63:
                            case 92: {
                                break Label_0207;
                            }
                            case 64: {
                                if (n4 == 0) {
                                    final int delimiterOffset2 = Util.delimiterOffset(s, n5, delimiterOffset, ':');
                                    String s2 = HttpUrl.canonicalize(s, n5, delimiterOffset2, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                    if (n != 0) {
                                        s2 = this.encodedUsername + "%40" + s2;
                                    }
                                    this.encodedUsername = s2;
                                    if (delimiterOffset2 != delimiterOffset) {
                                        n4 = 1;
                                        this.encodedPassword = HttpUrl.canonicalize(s, delimiterOffset2 + 1, delimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                    }
                                    n = 1;
                                }
                                else {
                                    this.encodedPassword = this.encodedPassword + "%40" + HttpUrl.canonicalize(s, n5, delimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                }
                                n5 = delimiterOffset + 1;
                                continue;
                            }
                        }
                    }
                    final int portColonOffset = portColonOffset(s, n5, delimiterOffset);
                    if (portColonOffset + 1 < delimiterOffset) {
                        this.host = canonicalizeHost(s, n5, portColonOffset);
                        this.port = parsePort(s, portColonOffset + 1, delimiterOffset);
                        if (this.port == -1) {
                            return ParseResult.INVALID_PORT;
                        }
                    }
                    else {
                        this.host = canonicalizeHost(s, n5, portColonOffset);
                        this.port = HttpUrl.defaultPort(this.scheme);
                    }
                    if (this.host == null) {
                        return ParseResult.INVALID_HOST;
                    }
                    n6 = delimiterOffset;
                }
                else {
                    this.encodedUsername = httpUrl.encodedUsername();
                    this.encodedPassword = httpUrl.encodedPassword();
                    this.host = httpUrl.host;
                    this.port = httpUrl.port;
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.addAll(httpUrl.encodedPathSegments());
                    if (skipLeadingAsciiWhitespace != skipTrailingAsciiWhitespace) {
                        n6 = skipLeadingAsciiWhitespace;
                        if (s.charAt(skipLeadingAsciiWhitespace) != '#') {
                            break Label_0535;
                        }
                    }
                    this.encodedQuery(httpUrl.encodedQuery());
                    n6 = skipLeadingAsciiWhitespace;
                }
            }
            final int delimiterOffset3 = Util.delimiterOffset(s, n6, skipTrailingAsciiWhitespace, "?#");
            this.resolvePath(s, n6, delimiterOffset3);
            int delimiterOffset4;
            if ((delimiterOffset4 = delimiterOffset3) < skipTrailingAsciiWhitespace) {
                delimiterOffset4 = delimiterOffset3;
                if (s.charAt(delimiterOffset3) == '?') {
                    delimiterOffset4 = Util.delimiterOffset(s, delimiterOffset3, skipTrailingAsciiWhitespace, '#');
                    this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, delimiterOffset3 + 1, delimiterOffset4, " \"'<>#", true, false, true, true));
                }
            }
            if (delimiterOffset4 < skipTrailingAsciiWhitespace && s.charAt(delimiterOffset4) == '#') {
                this.encodedFragment = HttpUrl.canonicalize(s, delimiterOffset4 + 1, skipTrailingAsciiWhitespace, "", true, false, false, false);
            }
            return ParseResult.SUCCESS;
        }
        
        public Builder password(final String s) {
            if (s == null) {
                throw new NullPointerException("password == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }
        
        public Builder port(final int n) {
            if (n <= 0 || n > 65535) {
                throw new IllegalArgumentException("unexpected port: " + n);
            }
            this.port = n;
            return this;
        }
        
        public Builder query(final String s) {
            List<String> queryStringToNamesAndValues;
            if (s != null) {
                queryStringToNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, " \"'<>#", false, false, true, true));
            }
            else {
                queryStringToNamesAndValues = null;
            }
            this.encodedQueryNamesAndValues = queryStringToNamesAndValues;
            return this;
        }
        
        Builder reencodeForUri() {
            for (int i = 0; i < this.encodedPathSegments.size(); ++i) {
                this.encodedPathSegments.set(i, HttpUrl.canonicalize(this.encodedPathSegments.get(i), "[]", true, true, false, true));
            }
            if (this.encodedQueryNamesAndValues != null) {
                for (int j = 0; j < this.encodedQueryNamesAndValues.size(); ++j) {
                    final String s = this.encodedQueryNamesAndValues.get(j);
                    if (s != null) {
                        this.encodedQueryNamesAndValues.set(j, HttpUrl.canonicalize(s, "\\^`{|}", true, true, true, true));
                    }
                }
            }
            if (this.encodedFragment != null) {
                this.encodedFragment = HttpUrl.canonicalize(this.encodedFragment, " \"#<>\\^`{|}", true, true, false, false);
            }
            return this;
        }
        
        public Builder removeAllEncodedQueryParameters(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues != null) {
                this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(s, " \"'<>#&=", true, false, true, true));
            }
            return this;
        }
        
        public Builder removeAllQueryParameters(final String s) {
            if (s == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues != null) {
                this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(s, " \"'<>#&=", false, false, true, true));
            }
            return this;
        }
        
        public Builder removePathSegment(final int n) {
            this.encodedPathSegments.remove(n);
            if (this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add("");
            }
            return this;
        }
        
        public Builder scheme(final String str) {
            if (str == null) {
                throw new NullPointerException("scheme == null");
            }
            if (str.equalsIgnoreCase("http")) {
                this.scheme = "http";
            }
            else {
                if (!str.equalsIgnoreCase("https")) {
                    throw new IllegalArgumentException("unexpected scheme: " + str);
                }
                this.scheme = "https";
            }
            return this;
        }
        
        public Builder setEncodedPathSegment(final int n, final String str) {
            if (str == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            final String canonicalize = HttpUrl.canonicalize(str, 0, str.length(), " \"<>^`{}|/\\?#", true, false, false, true);
            this.encodedPathSegments.set(n, canonicalize);
            if (this.isDot(canonicalize) || this.isDotDot(canonicalize)) {
                throw new IllegalArgumentException("unexpected path segment: " + str);
            }
            return this;
        }
        
        public Builder setEncodedQueryParameter(final String s, final String s2) {
            this.removeAllEncodedQueryParameters(s);
            this.addEncodedQueryParameter(s, s2);
            return this;
        }
        
        public Builder setPathSegment(final int n, final String str) {
            if (str == null) {
                throw new NullPointerException("pathSegment == null");
            }
            final String canonicalize = HttpUrl.canonicalize(str, 0, str.length(), " \"<>^`{}|/\\?#", false, false, false, true);
            if (this.isDot(canonicalize) || this.isDotDot(canonicalize)) {
                throw new IllegalArgumentException("unexpected path segment: " + str);
            }
            this.encodedPathSegments.set(n, canonicalize);
            return this;
        }
        
        public Builder setQueryParameter(final String s, final String s2) {
            this.removeAllQueryParameters(s);
            this.addQueryParameter(s, s2);
            return this;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.scheme);
            sb.append("://");
            if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
                sb.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    sb.append(':');
                    sb.append(this.encodedPassword);
                }
                sb.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                sb.append('[');
                sb.append(this.host);
                sb.append(']');
            }
            else {
                sb.append(this.host);
            }
            final int effectivePort = this.effectivePort();
            if (effectivePort != HttpUrl.defaultPort(this.scheme)) {
                sb.append(':');
                sb.append(effectivePort);
            }
            HttpUrl.pathSegmentsToString(sb, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                sb.append('?');
                HttpUrl.namesAndValuesToQueryString(sb, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                sb.append('#');
                sb.append(this.encodedFragment);
            }
            return sb.toString();
        }
        
        public Builder username(final String s) {
            if (s == null) {
                throw new NullPointerException("username == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }
        
        enum ParseResult
        {
            INVALID_HOST, 
            INVALID_PORT, 
            MISSING_SCHEME, 
            SUCCESS, 
            UNSUPPORTED_SCHEME;
        }
    }
}
