// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class BinaryFrame extends Id3Frame
{
    public static final Parcelable$Creator<BinaryFrame> CREATOR;
    public final byte[] data;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<BinaryFrame>() {
            public BinaryFrame createFromParcel(final Parcel parcel) {
                return new BinaryFrame(parcel);
            }
            
            public BinaryFrame[] newArray(final int n) {
                return new BinaryFrame[n];
            }
        };
    }
    
    BinaryFrame(final Parcel parcel) {
        final String string = parcel.readString();
        Util.castNonNull(string);
        super(string);
        final byte[] byteArray = parcel.createByteArray();
        Util.castNonNull(byteArray);
        this.data = byteArray;
    }
    
    public BinaryFrame(final String s, final byte[] data) {
        super(s);
        this.data = data;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && BinaryFrame.class == o.getClass()) {
            final BinaryFrame binaryFrame = (BinaryFrame)o;
            if (!super.id.equals(binaryFrame.id) || !Arrays.equals(this.data, binaryFrame.data)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (527 + super.id.hashCode()) * 31 + Arrays.hashCode(this.data);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(super.id);
        parcel.writeByteArray(this.data);
    }
}
