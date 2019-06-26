// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class InternalFrame extends Id3Frame
{
    public static final Parcelable$Creator<InternalFrame> CREATOR;
    public final String description;
    public final String domain;
    public final String text;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<InternalFrame>() {
            public InternalFrame createFromParcel(final Parcel parcel) {
                return new InternalFrame(parcel);
            }
            
            public InternalFrame[] newArray(final int n) {
                return new InternalFrame[n];
            }
        };
    }
    
    InternalFrame(final Parcel parcel) {
        super("----");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.domain = string;
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.description = string2;
        final String string3 = parcel.readString();
        Util.castNonNull(string3);
        this.text = string3;
    }
    
    public InternalFrame(final String domain, final String description, final String text) {
        super("----");
        this.domain = domain;
        this.description = description;
        this.text = text;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && InternalFrame.class == o.getClass()) {
            final InternalFrame internalFrame = (InternalFrame)o;
            if (!Util.areEqual(this.description, internalFrame.description) || !Util.areEqual(this.domain, internalFrame.domain) || !Util.areEqual(this.text, internalFrame.text)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String domain = this.domain;
        int hashCode = 0;
        int hashCode2;
        if (domain != null) {
            hashCode2 = domain.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String description = this.description;
        int hashCode3;
        if (description != null) {
            hashCode3 = description.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String text = this.text;
        if (text != null) {
            hashCode = text.hashCode();
        }
        return ((527 + hashCode2) * 31 + hashCode3) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": domain=");
        sb.append(this.domain);
        sb.append(", description=");
        sb.append(this.description);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(super.id);
        parcel.writeString(this.domain);
        parcel.writeString(this.text);
    }
}
