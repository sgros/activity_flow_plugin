// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class UrlLinkFrame extends Id3Frame
{
    public static final Parcelable$Creator<UrlLinkFrame> CREATOR;
    public final String description;
    public final String url;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UrlLinkFrame>() {
            public UrlLinkFrame createFromParcel(final Parcel parcel) {
                return new UrlLinkFrame(parcel);
            }
            
            public UrlLinkFrame[] newArray(final int n) {
                return new UrlLinkFrame[n];
            }
        };
    }
    
    UrlLinkFrame(final Parcel parcel) {
        final String string = parcel.readString();
        Util.castNonNull(string);
        super(string);
        this.description = parcel.readString();
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.url = string2;
    }
    
    public UrlLinkFrame(final String s, final String description, final String url) {
        super(s);
        this.description = description;
        this.url = url;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && UrlLinkFrame.class == o.getClass()) {
            final UrlLinkFrame urlLinkFrame = (UrlLinkFrame)o;
            if (!super.id.equals(urlLinkFrame.id) || !Util.areEqual(this.description, urlLinkFrame.description) || !Util.areEqual(this.url, urlLinkFrame.url)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = super.id.hashCode();
        final String description = this.description;
        int hashCode2 = 0;
        int hashCode3;
        if (description != null) {
            hashCode3 = description.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String url = this.url;
        if (url != null) {
            hashCode2 = url.hashCode();
        }
        return ((527 + hashCode) * 31 + hashCode3) * 31 + hashCode2;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": url=");
        sb.append(this.url);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(super.id);
        parcel.writeString(this.description);
        parcel.writeString(this.url);
    }
}
