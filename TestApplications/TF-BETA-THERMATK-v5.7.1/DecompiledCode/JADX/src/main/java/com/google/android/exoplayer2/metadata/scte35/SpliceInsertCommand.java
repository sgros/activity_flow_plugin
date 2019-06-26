package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SpliceInsertCommand extends SpliceCommand {
    public static final Creator<SpliceInsertCommand> CREATOR = new C01881();
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

    /* renamed from: com.google.android.exoplayer2.metadata.scte35.SpliceInsertCommand$1 */
    static class C01881 implements Creator<SpliceInsertCommand> {
        C01881() {
        }

        public SpliceInsertCommand createFromParcel(Parcel parcel) {
            return new SpliceInsertCommand(parcel, null);
        }

        public SpliceInsertCommand[] newArray(int i) {
            return new SpliceInsertCommand[i];
        }
    }

    public static final class ComponentSplice {
        public final long componentSplicePlaybackPositionUs;
        public final long componentSplicePts;
        public final int componentTag;

        /* synthetic */ ComponentSplice(int i, long j, long j2, C01881 c01881) {
            this(i, j, j2);
        }

        private ComponentSplice(int i, long j, long j2) {
            this.componentTag = i;
            this.componentSplicePts = j;
            this.componentSplicePlaybackPositionUs = j2;
        }

        public void writeToParcel(Parcel parcel) {
            parcel.writeInt(this.componentTag);
            parcel.writeLong(this.componentSplicePts);
            parcel.writeLong(this.componentSplicePlaybackPositionUs);
        }

        public static ComponentSplice createFromParcel(Parcel parcel) {
            return new ComponentSplice(parcel.readInt(), parcel.readLong(), parcel.readLong());
        }
    }

    /* synthetic */ SpliceInsertCommand(Parcel parcel, C01881 c01881) {
        this(parcel);
    }

    private SpliceInsertCommand(long j, boolean z, boolean z2, boolean z3, boolean z4, long j2, long j3, List<ComponentSplice> list, boolean z5, long j4, int i, int i2, int i3) {
        this.spliceEventId = j;
        this.spliceEventCancelIndicator = z;
        this.outOfNetworkIndicator = z2;
        this.programSpliceFlag = z3;
        this.spliceImmediateFlag = z4;
        this.programSplicePts = j2;
        this.programSplicePlaybackPositionUs = j3;
        this.componentSpliceList = Collections.unmodifiableList(list);
        this.autoReturn = z5;
        this.breakDurationUs = j4;
        this.uniqueProgramId = i;
        this.availNum = i2;
        this.availsExpected = i3;
    }

    private SpliceInsertCommand(Parcel parcel) {
        this.spliceEventId = parcel.readLong();
        boolean z = false;
        this.spliceEventCancelIndicator = parcel.readByte() == (byte) 1;
        this.outOfNetworkIndicator = parcel.readByte() == (byte) 1;
        this.programSpliceFlag = parcel.readByte() == (byte) 1;
        this.spliceImmediateFlag = parcel.readByte() == (byte) 1;
        this.programSplicePts = parcel.readLong();
        this.programSplicePlaybackPositionUs = parcel.readLong();
        int readInt = parcel.readInt();
        ArrayList arrayList = new ArrayList(readInt);
        for (int i = 0; i < readInt; i++) {
            arrayList.add(ComponentSplice.createFromParcel(parcel));
        }
        this.componentSpliceList = Collections.unmodifiableList(arrayList);
        if (parcel.readByte() == (byte) 1) {
            z = true;
        }
        this.autoReturn = z;
        this.breakDurationUs = parcel.readLong();
        this.uniqueProgramId = parcel.readInt();
        this.availNum = parcel.readInt();
        this.availsExpected = parcel.readInt();
    }

    static SpliceInsertCommand parseFromSection(ParsableByteArray parsableByteArray, long j, TimestampAdjuster timestampAdjuster) {
        List list;
        boolean z;
        boolean z2;
        long j2;
        boolean z3;
        long j3;
        int i;
        int i2;
        int i3;
        boolean z4;
        TimestampAdjuster timestampAdjuster2 = timestampAdjuster;
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        boolean z5 = (parsableByteArray.readUnsignedByte() & 128) != 0;
        List emptyList = Collections.emptyList();
        if (z5) {
            list = emptyList;
            z = false;
            z2 = false;
            j2 = -9223372036854775807L;
            z3 = false;
            j3 = -9223372036854775807L;
            i = 0;
            i2 = 0;
            i3 = 0;
            z4 = false;
        } else {
            ArrayList arrayList;
            int readUnsignedByte;
            long parseSpliceTime;
            boolean z6;
            int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
            boolean z7 = (readUnsignedByte2 & 128) != 0;
            boolean z8 = (readUnsignedByte2 & 64) != 0;
            Object obj = (readUnsignedByte2 & 32) != 0 ? 1 : null;
            boolean z9 = (readUnsignedByte2 & 16) != 0;
            long parseSpliceTime2 = (!z8 || z9) ? -9223372036854775807L : TimeSignalCommand.parseSpliceTime(parsableByteArray, j);
            if (z8) {
                arrayList = emptyList;
            } else {
                readUnsignedByte = parsableByteArray.readUnsignedByte();
                arrayList = new ArrayList(readUnsignedByte);
                for (int i4 = 0; i4 < readUnsignedByte; i4++) {
                    i3 = parsableByteArray.readUnsignedByte();
                    parseSpliceTime = !z9 ? TimeSignalCommand.parseSpliceTime(parsableByteArray, j) : -9223372036854775807L;
                    arrayList.add(new ComponentSplice(i3, parseSpliceTime, timestampAdjuster2.adjustTsTimestamp(parseSpliceTime), null));
                }
            }
            if (obj != null) {
                long readUnsignedByte3 = (long) parsableByteArray.readUnsignedByte();
                z6 = (128 & readUnsignedByte3) != 0;
                parseSpliceTime = ((((readUnsignedByte3 & 1) << 32) | parsableByteArray.readUnsignedInt()) * 1000) / 90;
            } else {
                parseSpliceTime = -9223372036854775807L;
                z6 = false;
            }
            readUnsignedByte = parsableByteArray.readUnsignedShort();
            i2 = parsableByteArray.readUnsignedByte();
            i3 = parsableByteArray.readUnsignedByte();
            z = z7;
            z4 = z8;
            list = arrayList;
            boolean z10 = z6;
            i = readUnsignedByte;
            long j4 = parseSpliceTime;
            z2 = z9;
            j2 = parseSpliceTime2;
            z3 = z10;
            j3 = j4;
        }
        return new SpliceInsertCommand(readUnsignedInt, z5, z, z4, z2, j2, timestampAdjuster2.adjustTsTimestamp(j2), list, z3, j3, i, i2, i3);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.spliceEventId);
        parcel.writeByte((byte) this.spliceEventCancelIndicator);
        parcel.writeByte((byte) this.outOfNetworkIndicator);
        parcel.writeByte((byte) this.programSpliceFlag);
        parcel.writeByte((byte) this.spliceImmediateFlag);
        parcel.writeLong(this.programSplicePts);
        parcel.writeLong(this.programSplicePlaybackPositionUs);
        i = this.componentSpliceList.size();
        parcel.writeInt(i);
        for (int i2 = 0; i2 < i; i2++) {
            ((ComponentSplice) this.componentSpliceList.get(i2)).writeToParcel(parcel);
        }
        parcel.writeByte((byte) this.autoReturn);
        parcel.writeLong(this.breakDurationUs);
        parcel.writeInt(this.uniqueProgramId);
        parcel.writeInt(this.availNum);
        parcel.writeInt(this.availsExpected);
    }
}
