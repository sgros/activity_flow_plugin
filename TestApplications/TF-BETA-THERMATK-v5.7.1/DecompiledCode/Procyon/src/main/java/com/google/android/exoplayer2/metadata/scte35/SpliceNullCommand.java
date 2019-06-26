// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class SpliceNullCommand extends SpliceCommand
{
    public static final Parcelable$Creator<SpliceNullCommand> CREATOR;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<SpliceNullCommand>() {
            public SpliceNullCommand createFromParcel(final Parcel parcel) {
                return new SpliceNullCommand();
            }
            
            public SpliceNullCommand[] newArray(final int n) {
                return new SpliceNullCommand[n];
            }
        };
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
    }
}
