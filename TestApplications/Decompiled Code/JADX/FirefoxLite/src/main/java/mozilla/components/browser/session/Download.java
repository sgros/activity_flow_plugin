package mozilla.components.browser.session;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: Download.kt */
public final class Download {
    private final Long contentLength;
    private final String contentType;
    private final String destinationDirectory;
    private final String fileName;
    private final String url;
    private final String userAgent;

    /* JADX WARNING: Missing block: B:14:0x0042, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r2.destinationDirectory, r3.destinationDirectory) != false) goto L_0x0047;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        if (r2 == r3) goto L_0x0047;
    L_0x0002:
        r0 = r3 instanceof mozilla.components.browser.session.Download;
        if (r0 == 0) goto L_0x0045;
    L_0x0006:
        r3 = (mozilla.components.browser.session.Download) r3;
        r0 = r2.url;
        r1 = r3.url;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0045;
    L_0x0012:
        r0 = r2.fileName;
        r1 = r3.fileName;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0045;
    L_0x001c:
        r0 = r2.contentType;
        r1 = r3.contentType;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0045;
    L_0x0026:
        r0 = r2.contentLength;
        r1 = r3.contentLength;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0045;
    L_0x0030:
        r0 = r2.userAgent;
        r1 = r3.userAgent;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0045;
    L_0x003a:
        r0 = r2.destinationDirectory;
        r3 = r3.destinationDirectory;
        r3 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r3);
        if (r3 == 0) goto L_0x0045;
    L_0x0044:
        goto L_0x0047;
    L_0x0045:
        r3 = 0;
        return r3;
    L_0x0047:
        r3 = 1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: mozilla.components.browser.session.Download.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        String str = this.url;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.fileName;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.contentType;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        Long l = this.contentLength;
        hashCode = (hashCode + (l != null ? l.hashCode() : 0)) * 31;
        str2 = this.userAgent;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.destinationDirectory;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download(url=");
        stringBuilder.append(this.url);
        stringBuilder.append(", fileName=");
        stringBuilder.append(this.fileName);
        stringBuilder.append(", contentType=");
        stringBuilder.append(this.contentType);
        stringBuilder.append(", contentLength=");
        stringBuilder.append(this.contentLength);
        stringBuilder.append(", userAgent=");
        stringBuilder.append(this.userAgent);
        stringBuilder.append(", destinationDirectory=");
        stringBuilder.append(this.destinationDirectory);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public Download(String str, String str2, String str3, Long l, String str4, String str5) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(str5, "destinationDirectory");
        this.url = str;
        this.fileName = str2;
        this.contentType = str3;
        this.contentLength = l;
        this.userAgent = str4;
        this.destinationDirectory = str5;
    }

    public final String getUrl() {
        return this.url;
    }

    public final String getFileName() {
        return this.fileName;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public final Long getContentLength() {
        return this.contentLength;
    }

    public final String getUserAgent() {
        return this.userAgent;
    }
}
