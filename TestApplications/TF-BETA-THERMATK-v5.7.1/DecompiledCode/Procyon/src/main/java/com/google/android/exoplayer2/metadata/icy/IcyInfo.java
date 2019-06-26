// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.icy;

import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.exoplayer2.metadata.Metadata;

public final class IcyInfo implements Entry
{
    public static final Parcelable$Creator<IcyInfo> CREATOR;
    public final String title;
    public final String url;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<IcyInfo>() {
            public IcyInfo createFromParcel(final Parcel parcel) {
                return new IcyInfo(parcel);
            }
            
            public IcyInfo[] newArray(final int n) {
                return new IcyInfo[n];
            }
        };
    }
    
    IcyInfo(final Parcel parcel) {
        this.title = parcel.readString();
        this.url = parcel.readString();
    }
    
    public IcyInfo(final String title, final String url) {
        this.title = title;
        this.url = url;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && IcyInfo.class == o.getClass()) {
            final IcyInfo icyInfo = (IcyInfo)o;
            if (!Util.areEqual(this.title, icyInfo.title) || !Util.areEqual(this.url, icyInfo.url)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String title = this.title;
        int hashCode = 0;
        int hashCode2;
        if (title != null) {
            hashCode2 = title.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String url = this.url;
        if (url != null) {
            hashCode = url.hashCode();
        }
        return (527 + hashCode2) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ICY: title=\"");
        sb.append(this.title);
        sb.append("\", url=\"");
        sb.append(this.url);
        sb.append("\"");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.title);
        parcel.writeString(this.url);
    }
}
