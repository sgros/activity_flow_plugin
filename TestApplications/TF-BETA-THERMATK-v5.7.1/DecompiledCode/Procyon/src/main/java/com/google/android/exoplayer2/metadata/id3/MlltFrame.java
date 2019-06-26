// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import java.util.Arrays;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class MlltFrame extends Id3Frame
{
    public static final Parcelable$Creator<MlltFrame> CREATOR;
    public final int bytesBetweenReference;
    public final int[] bytesDeviations;
    public final int millisecondsBetweenReference;
    public final int[] millisecondsDeviations;
    public final int mpegFramesBetweenReference;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MlltFrame>() {
            public MlltFrame createFromParcel(final Parcel parcel) {
                return new MlltFrame(parcel);
            }
            
            public MlltFrame[] newArray(final int n) {
                return new MlltFrame[n];
            }
        };
    }
    
    public MlltFrame(final int mpegFramesBetweenReference, final int bytesBetweenReference, final int millisecondsBetweenReference, final int[] bytesDeviations, final int[] millisecondsDeviations) {
        super("MLLT");
        this.mpegFramesBetweenReference = mpegFramesBetweenReference;
        this.bytesBetweenReference = bytesBetweenReference;
        this.millisecondsBetweenReference = millisecondsBetweenReference;
        this.bytesDeviations = bytesDeviations;
        this.millisecondsDeviations = millisecondsDeviations;
    }
    
    MlltFrame(final Parcel parcel) {
        super("MLLT");
        this.mpegFramesBetweenReference = parcel.readInt();
        this.bytesBetweenReference = parcel.readInt();
        this.millisecondsBetweenReference = parcel.readInt();
        this.bytesDeviations = parcel.createIntArray();
        this.millisecondsDeviations = parcel.createIntArray();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && MlltFrame.class == o.getClass()) {
            final MlltFrame mlltFrame = (MlltFrame)o;
            if (this.mpegFramesBetweenReference != mlltFrame.mpegFramesBetweenReference || this.bytesBetweenReference != mlltFrame.bytesBetweenReference || this.millisecondsBetweenReference != mlltFrame.millisecondsBetweenReference || !Arrays.equals(this.bytesDeviations, mlltFrame.bytesDeviations) || !Arrays.equals(this.millisecondsDeviations, mlltFrame.millisecondsDeviations)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return ((((527 + this.mpegFramesBetweenReference) * 31 + this.bytesBetweenReference) * 31 + this.millisecondsBetweenReference) * 31 + Arrays.hashCode(this.bytesDeviations)) * 31 + Arrays.hashCode(this.millisecondsDeviations);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mpegFramesBetweenReference);
        parcel.writeInt(this.bytesBetweenReference);
        parcel.writeInt(this.millisecondsBetweenReference);
        parcel.writeIntArray(this.bytesDeviations);
        parcel.writeIntArray(this.millisecondsDeviations);
    }
}
