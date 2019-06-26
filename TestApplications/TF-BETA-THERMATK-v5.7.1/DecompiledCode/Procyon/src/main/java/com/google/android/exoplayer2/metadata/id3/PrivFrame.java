// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class PrivFrame extends Id3Frame
{
    public static final Parcelable$Creator<PrivFrame> CREATOR;
    public final String owner;
    public final byte[] privateData;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<PrivFrame>() {
            public PrivFrame createFromParcel(final Parcel parcel) {
                return new PrivFrame(parcel);
            }
            
            public PrivFrame[] newArray(final int n) {
                return new PrivFrame[n];
            }
        };
    }
    
    PrivFrame(final Parcel parcel) {
        super("PRIV");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.owner = string;
        final byte[] byteArray = parcel.createByteArray();
        Util.castNonNull(byteArray);
        this.privateData = byteArray;
    }
    
    public PrivFrame(final String owner, final byte[] privateData) {
        super("PRIV");
        this.owner = owner;
        this.privateData = privateData;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && PrivFrame.class == o.getClass()) {
            final PrivFrame privFrame = (PrivFrame)o;
            if (!Util.areEqual(this.owner, privFrame.owner) || !Arrays.equals(this.privateData, privFrame.privateData)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String owner = this.owner;
        int hashCode;
        if (owner != null) {
            hashCode = owner.hashCode();
        }
        else {
            hashCode = 0;
        }
        return (527 + hashCode) * 31 + Arrays.hashCode(this.privateData);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.id);
        sb.append(": owner=");
        sb.append(this.owner);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.owner);
        parcel.writeByteArray(this.privateData);
    }
}
