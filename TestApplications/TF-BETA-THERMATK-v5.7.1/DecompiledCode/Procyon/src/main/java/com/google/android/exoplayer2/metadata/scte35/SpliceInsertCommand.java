// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.scte35;

import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import android.os.Parcel;
import java.util.List;
import android.os.Parcelable$Creator;

public final class SpliceInsertCommand extends SpliceCommand
{
    public static final Parcelable$Creator<SpliceInsertCommand> CREATOR;
    public final boolean autoReturn;
    public final int availNum;
    public final int availsExpected;
    public final long breakDurationUs;
    public final List<ComponentSplice> componentSpliceList;
    public final boolean outOfNetworkIndicator;
    public final boolean programSpliceFlag;
    public final long programSplicePlaybackPositionUs;
    public final long programSplicePts;
    public final boolean spliceEventCancelIndicator;
    public final long spliceEventId;
    public final boolean spliceImmediateFlag;
    public final int uniqueProgramId;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<SpliceInsertCommand>() {
            public SpliceInsertCommand createFromParcel(final Parcel parcel) {
                return new SpliceInsertCommand(parcel, null);
            }
            
            public SpliceInsertCommand[] newArray(final int n) {
                return new SpliceInsertCommand[n];
            }
        };
    }
    
    private SpliceInsertCommand(final long spliceEventId, final boolean spliceEventCancelIndicator, final boolean outOfNetworkIndicator, final boolean programSpliceFlag, final boolean spliceImmediateFlag, final long programSplicePts, final long programSplicePlaybackPositionUs, final List<ComponentSplice> list, final boolean autoReturn, final long breakDurationUs, final int uniqueProgramId, final int availNum, final int availsExpected) {
        this.spliceEventId = spliceEventId;
        this.spliceEventCancelIndicator = spliceEventCancelIndicator;
        this.outOfNetworkIndicator = outOfNetworkIndicator;
        this.programSpliceFlag = programSpliceFlag;
        this.spliceImmediateFlag = spliceImmediateFlag;
        this.programSplicePts = programSplicePts;
        this.programSplicePlaybackPositionUs = programSplicePlaybackPositionUs;
        this.componentSpliceList = Collections.unmodifiableList((List<? extends ComponentSplice>)list);
        this.autoReturn = autoReturn;
        this.breakDurationUs = breakDurationUs;
        this.uniqueProgramId = uniqueProgramId;
        this.availNum = availNum;
        this.availsExpected = availsExpected;
    }
    
    private SpliceInsertCommand(final Parcel parcel) {
        this.spliceEventId = parcel.readLong();
        final byte byte1 = parcel.readByte();
        final boolean b = false;
        this.spliceEventCancelIndicator = (byte1 == 1);
        this.outOfNetworkIndicator = (parcel.readByte() == 1);
        this.programSpliceFlag = (parcel.readByte() == 1);
        this.spliceImmediateFlag = (parcel.readByte() == 1);
        this.programSplicePts = parcel.readLong();
        this.programSplicePlaybackPositionUs = parcel.readLong();
        final int int1 = parcel.readInt();
        final ArrayList list = new ArrayList<ComponentSplice>(int1);
        for (int i = 0; i < int1; ++i) {
            list.add(ComponentSplice.createFromParcel(parcel));
        }
        this.componentSpliceList = Collections.unmodifiableList((List<? extends ComponentSplice>)list);
        boolean autoReturn = b;
        if (parcel.readByte() == 1) {
            autoReturn = true;
        }
        this.autoReturn = autoReturn;
        this.breakDurationUs = parcel.readLong();
        this.uniqueProgramId = parcel.readInt();
        this.availNum = parcel.readInt();
        this.availsExpected = parcel.readInt();
    }
    
    static SpliceInsertCommand parseFromSection(final ParsableByteArray parsableByteArray, long n, final TimestampAdjuster timestampAdjuster) {
        final long unsignedInt = parsableByteArray.readUnsignedInt();
        final boolean b = (parsableByteArray.readUnsignedByte() & 0x80) != 0x0;
        List<ComponentSplice> emptyList = Collections.emptyList();
        boolean b3;
        boolean b5;
        long spliceTime;
        int unsignedShort;
        int unsignedByte4;
        int unsignedByte5;
        List<ComponentSplice> list2;
        boolean b8;
        boolean b9;
        if (!b) {
            final int unsignedByte = parsableByteArray.readUnsignedByte();
            final boolean b2 = (unsignedByte & 0x80) != 0x0;
            b3 = ((unsignedByte & 0x40) != 0x0);
            final boolean b4 = (unsignedByte & 0x20) != 0x0;
            b5 = ((unsignedByte & 0x10) != 0x0);
            if (b3 && !b5) {
                spliceTime = TimeSignalCommand.parseSpliceTime(parsableByteArray, n);
            }
            else {
                spliceTime = -9223372036854775807L;
            }
            if (!b3) {
                final int unsignedByte2 = parsableByteArray.readUnsignedByte();
                final ArrayList list = new ArrayList<ComponentSplice>(unsignedByte2);
                int n2 = 0;
                while (true) {
                    emptyList = (List<ComponentSplice>)list;
                    if (n2 >= unsignedByte2) {
                        break;
                    }
                    final int unsignedByte3 = parsableByteArray.readUnsignedByte();
                    long spliceTime2;
                    if (!b5) {
                        spliceTime2 = TimeSignalCommand.parseSpliceTime(parsableByteArray, n);
                    }
                    else {
                        spliceTime2 = -9223372036854775807L;
                    }
                    list.add(new ComponentSplice(unsignedByte3, spliceTime2, timestampAdjuster.adjustTsTimestamp(spliceTime2)));
                    ++n2;
                }
            }
            boolean b6;
            if (b4) {
                n = parsableByteArray.readUnsignedByte();
                b6 = ((0x80L & n) != 0x0L);
                n = ((n & 0x1L) << 32 | parsableByteArray.readUnsignedInt()) * 1000L / 90L;
            }
            else {
                n = -9223372036854775807L;
                b6 = false;
            }
            unsignedShort = parsableByteArray.readUnsignedShort();
            unsignedByte4 = parsableByteArray.readUnsignedByte();
            unsignedByte5 = parsableByteArray.readUnsignedByte();
            list2 = emptyList;
            final boolean b7 = b6;
            b8 = b2;
            b9 = b7;
        }
        else {
            list2 = emptyList;
            b8 = false;
            b5 = false;
            spliceTime = -9223372036854775807L;
            b9 = false;
            n = -9223372036854775807L;
            unsignedShort = 0;
            unsignedByte4 = 0;
            unsignedByte5 = 0;
            b3 = false;
        }
        return new SpliceInsertCommand(unsignedInt, b, b8, b3, b5, spliceTime, timestampAdjuster.adjustTsTimestamp(spliceTime), list2, b9, n, unsignedShort, unsignedByte4, unsignedByte5);
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeLong(this.spliceEventId);
        parcel.writeByte((byte)(byte)(this.spliceEventCancelIndicator ? 1 : 0));
        parcel.writeByte((byte)(byte)(this.outOfNetworkIndicator ? 1 : 0));
        parcel.writeByte((byte)(byte)(this.programSpliceFlag ? 1 : 0));
        parcel.writeByte((byte)(byte)(this.spliceImmediateFlag ? 1 : 0));
        parcel.writeLong(this.programSplicePts);
        parcel.writeLong(this.programSplicePlaybackPositionUs);
        final int size = this.componentSpliceList.size();
        parcel.writeInt(size);
        for (i = 0; i < size; ++i) {
            this.componentSpliceList.get(i).writeToParcel(parcel);
        }
        parcel.writeByte((byte)(byte)(this.autoReturn ? 1 : 0));
        parcel.writeLong(this.breakDurationUs);
        parcel.writeInt(this.uniqueProgramId);
        parcel.writeInt(this.availNum);
        parcel.writeInt(this.availsExpected);
    }
    
    public static final class ComponentSplice
    {
        public final long componentSplicePlaybackPositionUs;
        public final long componentSplicePts;
        public final int componentTag;
        
        private ComponentSplice(final int componentTag, final long componentSplicePts, final long componentSplicePlaybackPositionUs) {
            this.componentTag = componentTag;
            this.componentSplicePts = componentSplicePts;
            this.componentSplicePlaybackPositionUs = componentSplicePlaybackPositionUs;
        }
        
        public static ComponentSplice createFromParcel(final Parcel parcel) {
            return new ComponentSplice(parcel.readInt(), parcel.readLong(), parcel.readLong());
        }
        
        public void writeToParcel(final Parcel parcel) {
            parcel.writeInt(this.componentTag);
            parcel.writeLong(this.componentSplicePts);
            parcel.writeLong(this.componentSplicePlaybackPositionUs);
        }
    }
}
