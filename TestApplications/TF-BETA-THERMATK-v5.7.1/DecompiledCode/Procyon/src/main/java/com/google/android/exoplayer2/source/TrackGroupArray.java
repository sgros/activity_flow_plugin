// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import java.util.Arrays;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class TrackGroupArray implements Parcelable
{
    public static final Parcelable$Creator<TrackGroupArray> CREATOR;
    public static final TrackGroupArray EMPTY;
    private int hashCode;
    public final int length;
    private final TrackGroup[] trackGroups;
    
    static {
        EMPTY = new TrackGroupArray(new TrackGroup[0]);
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<TrackGroupArray>() {
            public TrackGroupArray createFromParcel(final Parcel parcel) {
                return new TrackGroupArray(parcel);
            }
            
            public TrackGroupArray[] newArray(final int n) {
                return new TrackGroupArray[n];
            }
        };
    }
    
    TrackGroupArray(final Parcel parcel) {
        this.length = parcel.readInt();
        this.trackGroups = new TrackGroup[this.length];
        for (int i = 0; i < this.length; ++i) {
            this.trackGroups[i] = (TrackGroup)parcel.readParcelable(TrackGroup.class.getClassLoader());
        }
    }
    
    public TrackGroupArray(final TrackGroup... trackGroups) {
        this.trackGroups = trackGroups;
        this.length = trackGroups.length;
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
        if (o != null && TrackGroupArray.class == o.getClass()) {
            final TrackGroupArray trackGroupArray = (TrackGroupArray)o;
            if (this.length != trackGroupArray.length || !Arrays.equals(this.trackGroups, trackGroupArray.trackGroups)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public TrackGroup get(final int n) {
        return this.trackGroups[n];
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Arrays.hashCode(this.trackGroups);
        }
        return this.hashCode;
    }
    
    public int indexOf(final TrackGroup trackGroup) {
        for (int i = 0; i < this.length; ++i) {
            if (this.trackGroups[i] == trackGroup) {
                return i;
            }
        }
        return -1;
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeInt(this.length);
        for (i = 0; i < this.length; ++i) {
            parcel.writeParcelable((Parcelable)this.trackGroups[i], 0);
        }
    }
}
