// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class ApicFrame extends Id3Frame
{
    public static final Parcelable$Creator<ApicFrame> CREATOR;
    public final String description;
    public final String mimeType;
    public final byte[] pictureData;
    public final int pictureType;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ApicFrame>() {
            public ApicFrame createFromParcel(final Parcel parcel) {
                return new ApicFrame(parcel);
            }
            
            public ApicFrame[] newArray(final int n) {
                return new ApicFrame[n];
            }
        };
    }
    
    ApicFrame(final Parcel parcel) {
        super("APIC");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.mimeType = string;
        final String string2 = parcel.readString();
        Util.castNonNull(string2);
        this.description = string2;
        this.pictureType = parcel.readInt();
        final byte[] byteArray = parcel.createByteArray();
        Util.castNonNull(byteArray);
        this.pictureData = byteArray;
    }
    
    public ApicFrame(final String mimeType, final String description, final int pictureType, final byte[] pictureData) {
        super("APIC");
        this.mimeType = mimeType;
        this.description = description;
        this.pictureType = pictureType;
        this.pictureData = pictureData;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && ApicFrame.class == o.getClass()) {
            final ApicFrame apicFrame = (ApicFrame)o;
            if (this.pictureType != apicFrame.pictureType || !Util.areEqual(this.mimeType, apicFrame.mimeType) || !Util.areEqual(this.description, apicFrame.description) || !Arrays.equals(this.pictureData, apicFrame.pictureData)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int pictureType = this.pictureType;
        final String mimeType = this.mimeType;
        int hashCode = 0;
        int hashCode2;
        if (mimeType != null) {
            hashCode2 = mimeType.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String description = this.description;
        if (description != null) {
            hashCode = description.hashCode();
        }
        return (((527 + pictureType) * 31 + hashCode2) * 31 + hashCode) * 31 + Arrays.hashCode(this.pictureData);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": mimeType=");
        sb.append(this.mimeType);
        sb.append(", description=");
        sb.append(this.description);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.mimeType);
        parcel.writeString(this.description);
        parcel.writeInt(this.pictureType);
        parcel.writeByteArray(this.pictureData);
    }
}
