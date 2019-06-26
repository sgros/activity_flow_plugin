// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcelable;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class ChapterTocFrame extends Id3Frame
{
    public static final Parcelable$Creator<ChapterTocFrame> CREATOR;
    public final String[] children;
    public final String elementId;
    public final boolean isOrdered;
    public final boolean isRoot;
    private final Id3Frame[] subFrames;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ChapterTocFrame>() {
            public ChapterTocFrame createFromParcel(final Parcel parcel) {
                return new ChapterTocFrame(parcel);
            }
            
            public ChapterTocFrame[] newArray(final int n) {
                return new ChapterTocFrame[n];
            }
        };
    }
    
    ChapterTocFrame(final Parcel parcel) {
        super("CTOC");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.elementId = string;
        final byte byte1 = parcel.readByte();
        final boolean b = true;
        int i = 0;
        this.isRoot = (byte1 != 0);
        this.isOrdered = (parcel.readByte() != 0 && b);
        this.children = parcel.createStringArray();
        final int int1 = parcel.readInt();
        this.subFrames = new Id3Frame[int1];
        while (i < int1) {
            this.subFrames[i] = (Id3Frame)parcel.readParcelable(Id3Frame.class.getClassLoader());
            ++i;
        }
    }
    
    public ChapterTocFrame(final String elementId, final boolean isRoot, final boolean isOrdered, final String[] children, final Id3Frame[] subFrames) {
        super("CTOC");
        this.elementId = elementId;
        this.isRoot = isRoot;
        this.isOrdered = isOrdered;
        this.children = children;
        this.subFrames = subFrames;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && ChapterTocFrame.class == o.getClass()) {
            final ChapterTocFrame chapterTocFrame = (ChapterTocFrame)o;
            if (this.isRoot != chapterTocFrame.isRoot || this.isOrdered != chapterTocFrame.isOrdered || !Util.areEqual(this.elementId, chapterTocFrame.elementId) || !Arrays.equals(this.children, chapterTocFrame.children) || !Arrays.equals(this.subFrames, chapterTocFrame.subFrames)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int isRoot = this.isRoot ? 1 : 0;
        final int isOrdered = this.isOrdered ? 1 : 0;
        final String elementId = this.elementId;
        int hashCode;
        if (elementId != null) {
            hashCode = elementId.hashCode();
        }
        else {
            hashCode = 0;
        }
        return ((527 + isRoot) * 31 + isOrdered) * 31 + hashCode;
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeString(this.elementId);
        parcel.writeByte((byte)(byte)(this.isRoot ? 1 : 0));
        parcel.writeByte((byte)(byte)(this.isOrdered ? 1 : 0));
        parcel.writeStringArray(this.children);
        parcel.writeInt(this.subFrames.length);
        final Id3Frame[] subFrames = this.subFrames;
        int length;
        for (length = subFrames.length, i = 0; i < length; ++i) {
            parcel.writeParcelable((Parcelable)subFrames[i], 0);
        }
    }
}
