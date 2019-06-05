package org.mozilla.rocket.tabs.web;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Download implements Parcelable {
    public static final Creator<Download> CREATOR = new C05981();
    private final String contentDisposition;
    private final long contentLength;
    private final String mimeType;
    private final String name;
    private final boolean startFromContextMenu;
    private final String url;
    private final String userAgent;

    /* renamed from: org.mozilla.rocket.tabs.web.Download$1 */
    static class C05981 implements Creator<Download> {
        C05981() {
        }

        public Download createFromParcel(Parcel parcel) {
            return new Download(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readByte() != (byte) 0);
        }

        public Download[] newArray(int i) {
            return new Download[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public Download(String str, String str2, String str3, String str4, String str5, long j, boolean z) {
        this.url = str;
        this.name = str2;
        this.userAgent = str3;
        this.contentDisposition = str4;
        this.mimeType = str5;
        this.contentLength = j;
        this.startFromContextMenu = z;
    }

    public String getUrl() {
        return this.url;
    }

    public String getName() {
        return this.name;
    }

    public String getContentDisposition() {
        return this.contentDisposition;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public boolean isStartFromContextMenu() {
        return this.startFromContextMenu;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.name);
        parcel.writeString(this.userAgent);
        parcel.writeString(this.contentDisposition);
        parcel.writeString(this.mimeType);
        parcel.writeLong(this.contentLength);
        parcel.writeByte((byte) (this.startFromContextMenu ^ 1));
    }
}
