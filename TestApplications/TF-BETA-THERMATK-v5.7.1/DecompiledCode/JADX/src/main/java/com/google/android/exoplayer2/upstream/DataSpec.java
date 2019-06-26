package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

public final class DataSpec {
    public final long absoluteStreamPosition;
    public final int flags;
    public final byte[] httpBody;
    public final int httpMethod;
    public final String key;
    public final long length;
    public final long position;
    @Deprecated
    public final byte[] postBody;
    public final Uri uri;

    public DataSpec(Uri uri, int i) {
        this(uri, 0, -1, null, i);
    }

    public DataSpec(Uri uri, long j, long j2, String str) {
        this(uri, j, j, j2, str, 0);
    }

    public DataSpec(Uri uri, long j, long j2, String str, int i) {
        this(uri, j, j, j2, str, i);
    }

    public DataSpec(Uri uri, long j, long j2, long j3, String str, int i) {
        this(uri, null, j, j2, j3, str, i);
    }

    public DataSpec(Uri uri, byte[] bArr, long j, long j2, long j3, String str, int i) {
        this(uri, bArr != null ? 2 : 1, bArr, j, j2, j3, str, i);
    }

    public DataSpec(Uri uri, int i, byte[] bArr, long j, long j2, long j3, String str, int i2) {
        byte[] bArr2 = bArr;
        long j4 = j;
        long j5 = j2;
        long j6 = j3;
        boolean z = true;
        Assertions.checkArgument(j4 >= 0);
        Assertions.checkArgument(j5 >= 0);
        if (j6 <= 0 && j6 != -1) {
            z = false;
        }
        Assertions.checkArgument(z);
        this.uri = uri;
        this.httpMethod = i;
        if (bArr2 == null || bArr2.length == 0) {
            bArr2 = null;
        }
        this.httpBody = bArr2;
        this.postBody = this.httpBody;
        this.absoluteStreamPosition = j4;
        this.position = j5;
        this.length = j6;
        this.key = str;
        this.flags = i2;
    }

    public boolean isFlagSet(int i) {
        return (this.flags & i) == i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataSpec[");
        stringBuilder.append(getHttpMethodString());
        stringBuilder.append(" ");
        stringBuilder.append(this.uri);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(Arrays.toString(this.httpBody));
        stringBuilder.append(str);
        stringBuilder.append(this.absoluteStreamPosition);
        stringBuilder.append(str);
        stringBuilder.append(this.position);
        stringBuilder.append(str);
        stringBuilder.append(this.length);
        stringBuilder.append(str);
        stringBuilder.append(this.key);
        stringBuilder.append(str);
        stringBuilder.append(this.flags);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public final String getHttpMethodString() {
        return getStringForHttpMethod(this.httpMethod);
    }

    public static String getStringForHttpMethod(int i) {
        if (i == 1) {
            return "GET";
        }
        if (i == 2) {
            return "POST";
        }
        if (i == 3) {
            return "HEAD";
        }
        throw new AssertionError(i);
    }

    public DataSpec subrange(long j) {
        long j2 = this.length;
        long j3 = -1;
        if (j2 != -1) {
            j3 = j2 - j;
        }
        return subrange(j, j3);
    }

    public DataSpec subrange(long j, long j2) {
        if (j == 0 && this.length == j2) {
            return this;
        }
        return new DataSpec(this.uri, this.httpMethod, this.httpBody, this.absoluteStreamPosition + j, this.position + j, j2, this.key, this.flags);
    }
}
