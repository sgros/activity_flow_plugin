// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.urlutils;

import java.net.URISyntaxException;
import java.net.URI;
import android.text.TextUtils;

public class UrlUtils
{
    private static final String[] COMMON_SUBDOMAINS_PREFIX_ARRAY;
    private static final String[] HTTP_SCHEME_PREFIX_ARRAY;
    
    static {
        HTTP_SCHEME_PREFIX_ARRAY = new String[] { "http://", "https://" };
        COMMON_SUBDOMAINS_PREFIX_ARRAY = new String[] { "www.", "mobile.", "m." };
    }
    
    public static boolean isHttpOrHttps(final String s) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        boolean b = false;
        if (empty) {
            return false;
        }
        if (s.startsWith("http:") || s.startsWith("https:")) {
            b = true;
        }
        return b;
    }
    
    public static boolean isInternalErrorURL(final String anObject) {
        return "data:text/html;charset=utf-8;base64,".equals(anObject);
    }
    
    public static boolean isPermittedResourceProtocol(final String s) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        boolean b = false;
        if (empty) {
            return false;
        }
        if (s.startsWith("http") || s.startsWith("https") || s.startsWith("file") || s.startsWith("data")) {
            b = true;
        }
        return b;
    }
    
    public static boolean isSupportedProtocol(final String s) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        boolean b = false;
        if (empty) {
            return false;
        }
        if (isPermittedResourceProtocol(s) || s.startsWith("error")) {
            b = true;
        }
        return b;
    }
    
    public static String stripCommonSubdomains(final String s) {
        return stripPrefix(s, UrlUtils.COMMON_SUBDOMAINS_PREFIX_ARRAY);
    }
    
    public static String stripHttp(final String s) {
        return stripPrefix(s, UrlUtils.HTTP_SCHEME_PREFIX_ARRAY);
    }
    
    public static String stripPrefix(final String s, final String[] array) {
        if (s == null) {
            return null;
        }
        final int length = array.length;
        final int n = 0;
        int n2 = 0;
        int length2;
        while (true) {
            length2 = n;
            if (n2 >= length) {
                break;
            }
            final String prefix = array[n2];
            if (s.startsWith(prefix)) {
                length2 = prefix.length();
                break;
            }
            ++n2;
        }
        return s.substring(length2);
    }
    
    public static String stripUserInfo(final String str) {
        if (TextUtils.isEmpty((CharSequence)str)) {
            return "";
        }
        try {
            final URI uri = new URI(str);
            if (uri.getUserInfo() == null) {
                return str;
            }
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()).toString();
        }
        catch (URISyntaxException ex) {
            return str;
        }
    }
    
    public static boolean urlsMatchExceptForTrailingSlash(final String other, final String s) {
        final int n = other.length() - s.length();
        if (n == 0) {
            return other.equalsIgnoreCase(s);
        }
        final boolean b = false;
        final boolean b2 = false;
        if (n == 1) {
            boolean b3 = b2;
            if (other.charAt(other.length() - 1) == '/') {
                b3 = b2;
                if (other.regionMatches(true, 0, s, 0, s.length())) {
                    b3 = true;
                }
            }
            return b3;
        }
        if (n == -1) {
            boolean b4 = b;
            if (s.charAt(s.length() - 1) == '/') {
                b4 = b;
                if (s.regionMatches(true, 0, other, 0, other.length())) {
                    b4 = true;
                }
            }
            return b4;
        }
        return false;
    }
}
