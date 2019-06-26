// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.exoplayer2.metadata.Metadata;

public final class MdtaMetadataEntry implements Entry
{
    public static final Parcelable$Creator<MdtaMetadataEntry> CREATOR;
    public final String key;
    public final int localeIndicator;
    public final int typeIndicator;
    public final byte[] value;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MdtaMetadataEntry>() {
            public MdtaMetadataEntry createFromParcel(final Parcel parcel) {
                return new MdtaMetadataEntry(parcel, null);
            }
            
            public MdtaMetadataEntry[] newArray(final int n) {
                return new MdtaMetadataEntry[n];
            }
        };
    }
    
    private MdtaMetadataEntry(final Parcel parcel) {
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.key = string;
        parcel.readByteArray(this.value = new byte[parcel.readInt()]);
        this.localeIndicator = parcel.readInt();
        this.typeIndicator = parcel.readInt();
    }
    
    public MdtaMetadataEntry(final String key, final byte[] value, final int localeIndicator, final int typeIndicator) {
        this.key = key;
        this.value = value;
        this.localeIndicator = localeIndicator;
        this.typeIndicator = typeIndicator;
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
        if (o != null && MdtaMetadataEntry.class == o.getClass()) {
            final MdtaMetadataEntry mdtaMetadataEntry = (MdtaMetadataEntry)o;
            if (!this.key.equals(mdtaMetadataEntry.key) || !Arrays.equals(this.value, mdtaMetadataEntry.value) || this.localeIndicator != mdtaMetadataEntry.localeIndicator || this.typeIndicator != mdtaMetadataEntry.typeIndicator) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (((527 + this.key.hashCode()) * 31 + Arrays.hashCode(this.value)) * 31 + this.localeIndicator) * 31 + this.typeIndicator;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("mdta: key=");
        sb.append(this.key);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.key);
        parcel.writeInt(this.value.length);
        parcel.writeByteArray(this.value);
        parcel.writeInt(this.localeIndicator);
        parcel.writeInt(this.typeIndicator);
    }
}
