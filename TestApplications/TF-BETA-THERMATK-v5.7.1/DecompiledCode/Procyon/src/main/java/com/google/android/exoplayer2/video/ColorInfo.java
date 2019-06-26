// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class ColorInfo implements Parcelable
{
    public static final Parcelable$Creator<ColorInfo> CREATOR;
    public final int colorRange;
    public final int colorSpace;
    public final int colorTransfer;
    private int hashCode;
    public final byte[] hdrStaticInfo;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ColorInfo>() {
            public ColorInfo createFromParcel(final Parcel parcel) {
                return new ColorInfo(parcel);
            }
            
            public ColorInfo[] newArray(final int n) {
                return new ColorInfo[0];
            }
        };
    }
    
    public ColorInfo(final int colorSpace, final int colorRange, final int colorTransfer, final byte[] hdrStaticInfo) {
        this.colorSpace = colorSpace;
        this.colorRange = colorRange;
        this.colorTransfer = colorTransfer;
        this.hdrStaticInfo = hdrStaticInfo;
    }
    
    ColorInfo(final Parcel parcel) {
        this.colorSpace = parcel.readInt();
        this.colorRange = parcel.readInt();
        this.colorTransfer = parcel.readInt();
        byte[] byteArray;
        if (Util.readBoolean(parcel)) {
            byteArray = parcel.createByteArray();
        }
        else {
            byteArray = null;
        }
        this.hdrStaticInfo = byteArray;
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
        if (o != null && ColorInfo.class == o.getClass()) {
            final ColorInfo colorInfo = (ColorInfo)o;
            if (this.colorSpace != colorInfo.colorSpace || this.colorRange != colorInfo.colorRange || this.colorTransfer != colorInfo.colorTransfer || !Arrays.equals(this.hdrStaticInfo, colorInfo.hdrStaticInfo)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (((527 + this.colorSpace) * 31 + this.colorRange) * 31 + this.colorTransfer) * 31 + Arrays.hashCode(this.hdrStaticInfo);
        }
        return this.hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ColorInfo(");
        sb.append(this.colorSpace);
        sb.append(", ");
        sb.append(this.colorRange);
        sb.append(", ");
        sb.append(this.colorTransfer);
        sb.append(", ");
        sb.append(this.hdrStaticInfo != null);
        sb.append(")");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.colorSpace);
        parcel.writeInt(this.colorRange);
        parcel.writeInt(this.colorTransfer);
        Util.writeBoolean(parcel, this.hdrStaticInfo != null);
        final byte[] hdrStaticInfo = this.hdrStaticInfo;
        if (hdrStaticInfo != null) {
            parcel.writeByteArray(hdrStaticInfo);
        }
    }
}
