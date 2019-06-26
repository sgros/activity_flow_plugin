// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class GeobFrame extends Id3Frame
{
    public static final Parcelable$Creator<GeobFrame> CREATOR;
    public final byte[] data;
    public final String description;
    public final String filename;
    public final String mimeType;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<GeobFrame>() {
            public GeobFrame createFromParcel(final Parcel parcel) {
                return new GeobFrame(parcel);
            }
            
            public GeobFrame[] newArray(final int n) {
                return new GeobFrame[n];
            }
        };
    }
    
    GeobFrame(final Parcel parcel) {
        super("GEOB");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.mimeType = string;
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.filename = string2;
        final String string3 = parcel.readString();
        Util.castNonNull(string3);
        this.description = string3;
        final byte[] byteArray = parcel.createByteArray();
        Util.castNonNull(byteArray);
        this.data = byteArray;
    }
    
    public GeobFrame(final String mimeType, final String filename, final String description, final byte[] data) {
        super("GEOB");
        this.mimeType = mimeType;
        this.filename = filename;
        this.description = description;
        this.data = data;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && GeobFrame.class == o.getClass()) {
            final GeobFrame geobFrame = (GeobFrame)o;
            if (!Util.areEqual(this.mimeType, geobFrame.mimeType) || !Util.areEqual(this.filename, geobFrame.filename) || !Util.areEqual(this.description, geobFrame.description) || !Arrays.equals(this.data, geobFrame.data)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String mimeType = this.mimeType;
        int hashCode = 0;
        int hashCode2;
        if (mimeType != null) {
            hashCode2 = mimeType.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String filename = this.filename;
        int hashCode3;
        if (filename != null) {
            hashCode3 = filename.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String description = this.description;
        if (description != null) {
            hashCode = description.hashCode();
        }
        return (((527 + hashCode2) * 31 + hashCode3) * 31 + hashCode) * 31 + Arrays.hashCode(this.data);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": mimeType=");
        sb.append(this.mimeType);
        sb.append(", filename=");
        sb.append(this.filename);
        sb.append(", description=");
        sb.append(this.description);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.mimeType);
        parcel.writeString(this.filename);
        parcel.writeString(this.description);
        parcel.writeByteArray(this.data);
    }
}
