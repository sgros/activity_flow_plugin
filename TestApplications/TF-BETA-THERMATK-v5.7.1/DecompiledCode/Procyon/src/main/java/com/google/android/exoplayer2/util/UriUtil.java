// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.Iterator;
import android.net.Uri$Builder;
import android.net.Uri;
import android.text.TextUtils;

public final class UriUtil
{
    private static final int FRAGMENT = 3;
    private static final int INDEX_COUNT = 4;
    private static final int PATH = 1;
    private static final int QUERY = 2;
    private static final int SCHEME_COLON = 0;
    
    private UriUtil() {
    }
    
    private static int[] getUriIndices(final String s) {
        final int[] array = new int[4];
        if (TextUtils.isEmpty((CharSequence)s)) {
            array[0] = -1;
            return array;
        }
        final int length = s.length();
        int index = s.indexOf(35);
        if (index == -1) {
            index = length;
        }
        final int index2 = s.indexOf(63);
        int n;
        if (index2 == -1 || (n = index2) > index) {
            n = index;
        }
        final int index3 = s.indexOf(47);
        int n2;
        if (index3 == -1 || (n2 = index3) > n) {
            n2 = n;
        }
        int index4;
        if ((index4 = s.indexOf(58)) > n2) {
            index4 = -1;
        }
        final int index5 = index4 + 2;
        int n3;
        if (index5 < n && s.charAt(index4 + 1) == '/' && s.charAt(index5) == '/') {
            final int index6 = s.indexOf(47, index4 + 3);
            if (index6 == -1 || (n3 = index6) > n) {
                n3 = n;
            }
        }
        else {
            n3 = index4 + 1;
        }
        array[0] = index4;
        array[1] = n3;
        array[2] = n;
        array[3] = index;
        return array;
    }
    
    private static String removeDotSegments(final StringBuilder sb, int n, int start) {
        if (n >= start) {
            return sb.toString();
        }
        int n2 = n;
        if (sb.charAt(n) == '/') {
            n2 = n + 1;
        }
        final int n3 = n2;
        n = start;
        start = n3;
    Label_0034:
        while (true) {
            int i = start;
            while (i <= n) {
                int n4;
                if (i == n) {
                    n4 = i;
                }
                else {
                    if (sb.charAt(i) != '/') {
                        ++i;
                        continue;
                    }
                    n4 = i + 1;
                }
                final int n5 = start + 1;
                if (i == n5 && sb.charAt(start) == '.') {
                    sb.delete(start, n4);
                    n -= n4 - start;
                    continue Label_0034;
                }
                if (i == start + 2 && sb.charAt(start) == '.' && sb.charAt(n5) == '.') {
                    start = sb.lastIndexOf("/", start - 2) + 1;
                    int start2;
                    if (start > n2) {
                        start2 = start;
                    }
                    else {
                        start2 = n2;
                    }
                    sb.delete(start2, n4);
                    n -= n4 - start2;
                }
                else {
                    start = i + 1;
                }
                continue Label_0034;
            }
            break;
        }
        return sb.toString();
    }
    
    public static Uri removeQueryParameter(final Uri uri, final String anObject) {
        final Uri$Builder buildUpon = uri.buildUpon();
        buildUpon.clearQuery();
        for (final String s : uri.getQueryParameterNames()) {
            if (!s.equals(anObject)) {
                final Iterator iterator2 = uri.getQueryParameters(s).iterator();
                while (iterator2.hasNext()) {
                    buildUpon.appendQueryParameter(s, (String)iterator2.next());
                }
            }
        }
        return buildUpon.build();
    }
    
    public static String resolve(String str, final String s) {
        final StringBuilder sb = new StringBuilder();
        String s2 = str;
        if (str == null) {
            s2 = "";
        }
        if ((str = s) == null) {
            str = "";
        }
        final int[] uriIndices = getUriIndices(str);
        if (uriIndices[0] != -1) {
            sb.append(str);
            removeDotSegments(sb, uriIndices[1], uriIndices[2]);
            return sb.toString();
        }
        final int[] uriIndices2 = getUriIndices(s2);
        if (uriIndices[3] == 0) {
            sb.append(s2, 0, uriIndices2[3]);
            sb.append(str);
            return sb.toString();
        }
        if (uriIndices[2] == 0) {
            sb.append(s2, 0, uriIndices2[2]);
            sb.append(str);
            return sb.toString();
        }
        if (uriIndices[1] != 0) {
            final int end = uriIndices2[0] + 1;
            sb.append(s2, 0, end);
            sb.append(str);
            return removeDotSegments(sb, uriIndices[1] + end, end + uriIndices[2]);
        }
        if (str.charAt(uriIndices[1]) == '/') {
            sb.append(s2, 0, uriIndices2[1]);
            sb.append(str);
            return removeDotSegments(sb, uriIndices2[1], uriIndices2[1] + uriIndices[2]);
        }
        if (uriIndices2[0] + 2 < uriIndices2[1] && uriIndices2[1] == uriIndices2[2]) {
            sb.append(s2, 0, uriIndices2[1]);
            sb.append('/');
            sb.append(str);
            return removeDotSegments(sb, uriIndices2[1], uriIndices2[1] + uriIndices[2] + 1);
        }
        int lastIndex = s2.lastIndexOf(47, uriIndices2[2] - 1);
        if (lastIndex == -1) {
            lastIndex = uriIndices2[1];
        }
        else {
            ++lastIndex;
        }
        sb.append(s2, 0, lastIndex);
        sb.append(str);
        return removeDotSegments(sb, uriIndices2[1], lastIndex + uriIndices[2]);
    }
    
    public static Uri resolveToUri(final String s, final String s2) {
        return Uri.parse(resolve(s, s2));
    }
}
