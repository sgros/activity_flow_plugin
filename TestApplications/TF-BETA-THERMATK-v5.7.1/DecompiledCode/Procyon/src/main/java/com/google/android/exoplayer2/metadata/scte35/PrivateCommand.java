// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.scte35;

import com.google.android.exoplayer2.util.ParsableByteArray;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class PrivateCommand extends SpliceCommand
{
    public static final Parcelable$Creator<PrivateCommand> CREATOR;
    public final byte[] commandBytes;
    public final long identifier;
    public final long ptsAdjustment;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<PrivateCommand>() {
            public PrivateCommand createFromParcel(final Parcel parcel) {
                return new PrivateCommand(parcel, null);
            }
            
            public PrivateCommand[] newArray(final int n) {
                return new PrivateCommand[n];
            }
        };
    }
    
    private PrivateCommand(final long identifier, final byte[] commandBytes, final long ptsAdjustment) {
        this.ptsAdjustment = ptsAdjustment;
        this.identifier = identifier;
        this.commandBytes = commandBytes;
    }
    
    private PrivateCommand(final Parcel parcel) {
        this.ptsAdjustment = parcel.readLong();
        this.identifier = parcel.readLong();
        parcel.readByteArray(this.commandBytes = new byte[parcel.readInt()]);
    }
    
    static PrivateCommand parseFromSection(final ParsableByteArray parsableByteArray, final int n, final long n2) {
        final long unsignedInt = parsableByteArray.readUnsignedInt();
        final byte[] array = new byte[n - 4];
        parsableByteArray.readBytes(array, 0, array.length);
        return new PrivateCommand(unsignedInt, array, n2);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeLong(this.ptsAdjustment);
        parcel.writeLong(this.identifier);
        parcel.writeInt(this.commandBytes.length);
        parcel.writeByteArray(this.commandBytes);
    }
}
