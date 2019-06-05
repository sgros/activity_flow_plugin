package org.mozilla.urlutils;

import android.text.TextUtils;
import com.adjust.sdk.Constants;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
    private static final String[] COMMON_SUBDOMAINS_PREFIX_ARRAY = new String[]{"www.", "mobile.", "m."};
    private static final String[] HTTP_SCHEME_PREFIX_ARRAY = new String[]{"http://", "https://"};

    public static boolean isHttpOrHttps(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.startsWith("http:") || str.startsWith("https:")) {
            z = true;
        }
        return z;
    }

    public static String stripUserInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            URI uri = new URI(str);
            if (uri.getUserInfo() == null) {
                return str;
            }
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()).toString();
        } catch (URISyntaxException unused) {
            return str;
        }
    }

    public static boolean isPermittedResourceProtocol(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.startsWith("http") || str.startsWith(Constants.SCHEME) || str.startsWith("file") || str.startsWith("data")) {
            z = true;
        }
        return z;
    }

    public static boolean isSupportedProtocol(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (isPermittedResourceProtocol(str) || str.startsWith("error")) {
            z = true;
        }
        return z;
    }

    public static boolean isInternalErrorURL(String str) {
        return "data:text/html;charset=utf-8;base64,".equals(str);
    }

    public static boolean urlsMatchExceptForTrailingSlash(String str, String str2) {
        int length = str.length() - str2.length();
        if (length == 0) {
            return str.equalsIgnoreCase(str2);
        }
        boolean z = false;
        if (length == 1) {
            if (str.charAt(str.length() - 1) == '/') {
                if (str.regionMatches(true, 0, str2, 0, str2.length())) {
                    z = true;
                }
            }
            return z;
        } else if (length != -1) {
            return false;
        } else {
            if (str2.charAt(str2.length() - 1) == '/') {
                if (str2.regionMatches(true, 0, str, 0, str.length())) {
                    z = true;
                }
            }
            return z;
        }
    }

    public static String stripCommonSubdomains(String str) {
        return stripPrefix(str, COMMON_SUBDOMAINS_PREFIX_ARRAY);
    }

    public static String stripHttp(String str) {
        return stripPrefix(str, HTTP_SCHEME_PREFIX_ARRAY);
    }

    public static String stripPrefix(String str, String[] strArr) {
        if (str == null) {
            return null;
        }
        int i = 0;
        for (String str2 : strArr) {
            if (str.startsWith(str2)) {
                i = str2.length();
                break;
            }
        }
        return str.substring(i);
    }
}
