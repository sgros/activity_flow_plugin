// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.browser.session;

import kotlin.jvm.internal.Intrinsics;

public final class Download
{
    private final Long contentLength;
    private final String contentType;
    private final String destinationDirectory;
    private final String fileName;
    private final String url;
    private final String userAgent;
    
    public Download(final String url, final String fileName, final String contentType, final Long contentLength, final String userAgent, final String destinationDirectory) {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(destinationDirectory, "destinationDirectory");
        this.url = url;
        this.fileName = fileName;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.userAgent = userAgent;
        this.destinationDirectory = destinationDirectory;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof Download) {
                final Download download = (Download)o;
                if (Intrinsics.areEqual(this.url, download.url) && Intrinsics.areEqual(this.fileName, download.fileName) && Intrinsics.areEqual(this.contentType, download.contentType) && Intrinsics.areEqual(this.contentLength, download.contentLength) && Intrinsics.areEqual(this.userAgent, download.userAgent) && Intrinsics.areEqual(this.destinationDirectory, download.destinationDirectory)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public final Long getContentLength() {
        return this.contentLength;
    }
    
    public final String getContentType() {
        return this.contentType;
    }
    
    public final String getFileName() {
        return this.fileName;
    }
    
    public final String getUrl() {
        return this.url;
    }
    
    public final String getUserAgent() {
        return this.userAgent;
    }
    
    @Override
    public int hashCode() {
        final String url = this.url;
        int hashCode = 0;
        int hashCode2;
        if (url != null) {
            hashCode2 = url.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String fileName = this.fileName;
        int hashCode3;
        if (fileName != null) {
            hashCode3 = fileName.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String contentType = this.contentType;
        int hashCode4;
        if (contentType != null) {
            hashCode4 = contentType.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        final Long contentLength = this.contentLength;
        int hashCode5;
        if (contentLength != null) {
            hashCode5 = contentLength.hashCode();
        }
        else {
            hashCode5 = 0;
        }
        final String userAgent = this.userAgent;
        int hashCode6;
        if (userAgent != null) {
            hashCode6 = userAgent.hashCode();
        }
        else {
            hashCode6 = 0;
        }
        final String destinationDirectory = this.destinationDirectory;
        if (destinationDirectory != null) {
            hashCode = destinationDirectory.hashCode();
        }
        return ((((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Download(url=");
        sb.append(this.url);
        sb.append(", fileName=");
        sb.append(this.fileName);
        sb.append(", contentType=");
        sb.append(this.contentType);
        sb.append(", contentLength=");
        sb.append(this.contentLength);
        sb.append(", userAgent=");
        sb.append(this.userAgent);
        sb.append(", destinationDirectory=");
        sb.append(this.destinationDirectory);
        sb.append(")");
        return sb.toString();
    }
}
