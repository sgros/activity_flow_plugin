// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs.web;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Download implements Parcelable
{
    public static final Parcelable$Creator<Download> CREATOR;
    private final String contentDisposition;
    private final long contentLength;
    private final String mimeType;
    private final String name;
    private final boolean startFromContextMenu;
    private final String url;
    private final String userAgent;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Download>() {
            public Download createFromParcel(final Parcel parcel) {
                return new Download(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readByte() != 0);
            }
            
            public Download[] newArray(final int n) {
                return new Download[n];
            }
        };
    }
    
    public Download(final String url, final String name, final String userAgent, final String contentDisposition, final String mimeType, final long contentLength, final boolean startFromContextMenu) {
        this.url = url;
        this.name = name;
        this.userAgent = userAgent;
        this.contentDisposition = contentDisposition;
        this.mimeType = mimeType;
        this.contentLength = contentLength;
        this.startFromContextMenu = startFromContextMenu;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getContentDisposition() {
        return this.contentDisposition;
    }
    
    public long getContentLength() {
        return this.contentLength;
    }
    
    public String getMimeType() {
        return this.mimeType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public String getUserAgent() {
        return this.userAgent;
    }
    
    public boolean isStartFromContextMenu() {
        return this.startFromContextMenu;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.url);
        parcel.writeString(this.name);
        parcel.writeString(this.userAgent);
        parcel.writeString(this.contentDisposition);
        parcel.writeString(this.mimeType);
        parcel.writeLong(this.contentLength);
        parcel.writeByte((byte)(byte)((this.startFromContextMenu ^ true) ? 1 : 0));
    }
}
