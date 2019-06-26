// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.scte35;

import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;
import java.util.ArrayList;
import android.os.Parcel;
import java.util.List;
import android.os.Parcelable$Creator;

public final class SpliceScheduleCommand extends SpliceCommand
{
    public static final Parcelable$Creator<SpliceScheduleCommand> CREATOR;
    public final List<Event> events;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<SpliceScheduleCommand>() {
            public SpliceScheduleCommand createFromParcel(final Parcel parcel) {
                return new SpliceScheduleCommand(parcel, null);
            }
            
            public SpliceScheduleCommand[] newArray(final int n) {
                return new SpliceScheduleCommand[n];
            }
        };
    }
    
    private SpliceScheduleCommand(final Parcel parcel) {
        final int int1 = parcel.readInt();
        final ArrayList list = new ArrayList<Event>(int1);
        for (int i = 0; i < int1; ++i) {
            list.add(createFromParcel(parcel));
        }
        this.events = Collections.unmodifiableList((List<? extends Event>)list);
    }
    
    private SpliceScheduleCommand(final List<Event> list) {
        this.events = Collections.unmodifiableList((List<? extends Event>)list);
    }
    
    static SpliceScheduleCommand parseFromSection(final ParsableByteArray parsableByteArray) {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final ArrayList list = new ArrayList<Event>(unsignedByte);
        for (int i = 0; i < unsignedByte; ++i) {
            list.add(parseFromSection(parsableByteArray));
        }
        return new SpliceScheduleCommand((List<Event>)list);
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        final int size = this.events.size();
        parcel.writeInt(size);
        for (i = 0; i < size; ++i) {
            this.events.get(i).writeToParcel(parcel);
        }
    }
    
    public static final class ComponentSplice
    {
        public final int componentTag;
        public final long utcSpliceTime;
        
        private ComponentSplice(final int componentTag, final long utcSpliceTime) {
            this.componentTag = componentTag;
            this.utcSpliceTime = utcSpliceTime;
        }
        
        private static ComponentSplice createFromParcel(final Parcel parcel) {
            return new ComponentSplice(parcel.readInt(), parcel.readLong());
        }
        
        private void writeToParcel(final Parcel parcel) {
            parcel.writeInt(this.componentTag);
            parcel.writeLong(this.utcSpliceTime);
        }
    }
    
    public static final class Event
    {
        public final boolean autoReturn;
        public final int availNum;
        public final int availsExpected;
        public final long breakDurationUs;
        public final List<ComponentSplice> componentSpliceList;
        public final boolean outOfNetworkIndicator;
        public final boolean programSpliceFlag;
        public final boolean spliceEventCancelIndicator;
        public final long spliceEventId;
        public final int uniqueProgramId;
        public final long utcSpliceTime;
        
        private Event(final long spliceEventId, final boolean spliceEventCancelIndicator, final boolean outOfNetworkIndicator, final boolean programSpliceFlag, final List<ComponentSplice> list, final long utcSpliceTime, final boolean autoReturn, final long breakDurationUs, final int uniqueProgramId, final int availNum, final int availsExpected) {
            this.spliceEventId = spliceEventId;
            this.spliceEventCancelIndicator = spliceEventCancelIndicator;
            this.outOfNetworkIndicator = outOfNetworkIndicator;
            this.programSpliceFlag = programSpliceFlag;
            this.componentSpliceList = Collections.unmodifiableList((List<? extends ComponentSplice>)list);
            this.utcSpliceTime = utcSpliceTime;
            this.autoReturn = autoReturn;
            this.breakDurationUs = breakDurationUs;
            this.uniqueProgramId = uniqueProgramId;
            this.availNum = availNum;
            this.availsExpected = availsExpected;
        }
        
        private Event(final Parcel parcel) {
            this.spliceEventId = parcel.readLong();
            final byte byte1 = parcel.readByte();
            final boolean b = false;
            this.spliceEventCancelIndicator = (byte1 == 1);
            this.outOfNetworkIndicator = (parcel.readByte() == 1);
            this.programSpliceFlag = (parcel.readByte() == 1);
            final int int1 = parcel.readInt();
            final ArrayList list = new ArrayList<ComponentSplice>(int1);
            for (int i = 0; i < int1; ++i) {
                list.add(createFromParcel(parcel));
            }
            this.componentSpliceList = Collections.unmodifiableList((List<? extends ComponentSplice>)list);
            this.utcSpliceTime = parcel.readLong();
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
        
        private static Event createFromParcel(final Parcel parcel) {
            return new Event(parcel);
        }
        
        private static Event parseFromSection(final ParsableByteArray parsableByteArray) {
            final long unsignedInt = parsableByteArray.readUnsignedInt();
            final boolean b = (parsableByteArray.readUnsignedByte() & 0x80) != 0x0;
            ArrayList<ComponentSplice> list = new ArrayList<ComponentSplice>();
            boolean b2;
            boolean b3;
            boolean b5;
            int unsignedShort;
            int unsignedByte3;
            int unsignedByte4;
            long n4;
            long n5;
            if (!b) {
                final int unsignedByte = parsableByteArray.readUnsignedByte();
                b2 = ((unsignedByte & 0x80) != 0x0);
                b3 = ((unsignedByte & 0x40) != 0x0);
                final boolean b4 = (unsignedByte & 0x20) != 0x0;
                long unsignedInt2;
                if (b3) {
                    unsignedInt2 = parsableByteArray.readUnsignedInt();
                }
                else {
                    unsignedInt2 = -9223372036854775807L;
                }
                if (!b3) {
                    final int unsignedByte2 = parsableByteArray.readUnsignedByte();
                    list = new ArrayList<ComponentSplice>(unsignedByte2);
                    for (int i = 0; i < unsignedByte2; ++i) {
                        list.add(new ComponentSplice(parsableByteArray.readUnsignedByte(), parsableByteArray.readUnsignedInt()));
                    }
                }
                long n2;
                if (b4) {
                    final long n = parsableByteArray.readUnsignedByte();
                    b5 = ((0x80L & n) != 0x0L);
                    n2 = ((n & 0x1L) << 32 | parsableByteArray.readUnsignedInt()) * 1000L / 90L;
                }
                else {
                    b5 = false;
                    n2 = -9223372036854775807L;
                }
                unsignedShort = parsableByteArray.readUnsignedShort();
                unsignedByte3 = parsableByteArray.readUnsignedByte();
                unsignedByte4 = parsableByteArray.readUnsignedByte();
                final long n3 = n2;
                n4 = unsignedInt2;
                n5 = n3;
            }
            else {
                b2 = false;
                n4 = -9223372036854775807L;
                b5 = false;
                n5 = -9223372036854775807L;
                unsignedShort = 0;
                unsignedByte3 = 0;
                unsignedByte4 = 0;
                b3 = false;
            }
            return new Event(unsignedInt, b, b2, b3, list, n4, b5, n5, unsignedShort, unsignedByte3, unsignedByte4);
        }
        
        private void writeToParcel(final Parcel parcel) {
            parcel.writeLong(this.spliceEventId);
            parcel.writeByte((byte)(byte)(this.spliceEventCancelIndicator ? 1 : 0));
            parcel.writeByte((byte)(byte)(this.outOfNetworkIndicator ? 1 : 0));
            parcel.writeByte((byte)(byte)(this.programSpliceFlag ? 1 : 0));
            final int size = this.componentSpliceList.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; ++i) {
                this.componentSpliceList.get(i).writeToParcel(parcel);
            }
            parcel.writeLong(this.utcSpliceTime);
            parcel.writeByte((byte)(byte)(this.autoReturn ? 1 : 0));
            parcel.writeLong(this.breakDurationUs);
            parcel.writeInt(this.uniqueProgramId);
            parcel.writeInt(this.availNum);
            parcel.writeInt(this.availsExpected);
        }
    }
}
