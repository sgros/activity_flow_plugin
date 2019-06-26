// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.scte35;

import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableByteArray;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class TimeSignalCommand extends SpliceCommand
{
    public static final Parcelable$Creator<TimeSignalCommand> CREATOR;
    public final long playbackPositionUs;
    public final long ptsTime;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<TimeSignalCommand>() {
            public TimeSignalCommand createFromParcel(final Parcel parcel) {
                return new TimeSignalCommand(parcel.readLong(), parcel.readLong(), null);
            }
            
            public TimeSignalCommand[] newArray(final int n) {
                return new TimeSignalCommand[n];
            }
        };
    }
    
    private TimeSignalCommand(final long ptsTime, final long playbackPositionUs) {
        this.ptsTime = ptsTime;
        this.playbackPositionUs = playbackPositionUs;
    }
    
    static TimeSignalCommand parseFromSection(final ParsableByteArray parsableByteArray, long spliceTime, final TimestampAdjuster timestampAdjuster) {
        spliceTime = parseSpliceTime(parsableByteArray, spliceTime);
        return new TimeSignalCommand(spliceTime, timestampAdjuster.adjustTsTimestamp(spliceTime));
    }
    
    static long parseSpliceTime(final ParsableByteArray parsableByteArray, long n) {
        final long n2 = parsableByteArray.readUnsignedByte();
        if ((0x80L & n2) != 0x0L) {
            n = (0x1FFFFFFFFL & ((n2 & 0x1L) << 32 | parsableByteArray.readUnsignedInt()) + n);
        }
        else {
            n = -9223372036854775807L;
        }
        return n;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeLong(this.ptsTime);
        parcel.writeLong(this.playbackPositionUs);
    }
}
