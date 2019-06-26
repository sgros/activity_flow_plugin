// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Parcel;
import com.google.android.exoplayer2.Format;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class TrackGroup implements Parcelable
{
    public static final Parcelable$Creator<TrackGroup> CREATOR;
    private final Format[] formats;
    private int hashCode;
    public final int length;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<TrackGroup>() {
            public TrackGroup createFromParcel(final Parcel parcel) {
                return new TrackGroup(parcel);
            }
            
            public TrackGroup[] newArray(final int n) {
                return new TrackGroup[n];
            }
        };
    }
    
    TrackGroup(final Parcel parcel) {
        this.length = parcel.readInt();
        this.formats = new Format[this.length];
        for (int i = 0; i < this.length; ++i) {
            this.formats[i] = (Format)parcel.readParcelable(Format.class.getClassLoader());
        }
    }
    
    public TrackGroup(final Format... formats) {
        Assertions.checkState(formats.length > 0);
        this.formats = formats;
        this.length = formats.length;
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
        if (o != null && TrackGroup.class == o.getClass()) {
            final TrackGroup trackGroup = (TrackGroup)o;
            if (this.length != trackGroup.length || !Arrays.equals(this.formats, trackGroup.formats)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public Format getFormat(final int n) {
        return this.formats[n];
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 527 + Arrays.hashCode(this.formats);
        }
        return this.hashCode;
    }
    
    public int indexOf(final Format format) {
        int n = 0;
        while (true) {
            final Format[] formats = this.formats;
            if (n >= formats.length) {
                return -1;
            }
            if (format == formats[n]) {
                return n;
            }
            ++n;
        }
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeInt(this.length);
        for (i = 0; i < this.length; ++i) {
            parcel.writeParcelable((Parcelable)this.formats[i], 0);
        }
    }
}
