// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcelable;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public final class ChapterFrame extends Id3Frame
{
    public static final Parcelable$Creator<ChapterFrame> CREATOR;
    public final String chapterId;
    public final long endOffset;
    public final int endTimeMs;
    public final long startOffset;
    public final int startTimeMs;
    private final Id3Frame[] subFrames;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ChapterFrame>() {
            public ChapterFrame createFromParcel(final Parcel parcel) {
                return new ChapterFrame(parcel);
            }
            
            public ChapterFrame[] newArray(final int n) {
                return new ChapterFrame[n];
            }
        };
    }
    
    ChapterFrame(final Parcel parcel) {
        super("CHAP");
        final String string = parcel.readString();
        Util.castNonNull(string);
        this.chapterId = string;
        this.startTimeMs = parcel.readInt();
        this.endTimeMs = parcel.readInt();
        this.startOffset = parcel.readLong();
        this.endOffset = parcel.readLong();
        final int int1 = parcel.readInt();
        this.subFrames = new Id3Frame[int1];
        for (int i = 0; i < int1; ++i) {
            this.subFrames[i] = (Id3Frame)parcel.readParcelable(Id3Frame.class.getClassLoader());
        }
    }
    
    public ChapterFrame(final String chapterId, final int startTimeMs, final int endTimeMs, final long startOffset, final long endOffset, final Id3Frame[] subFrames) {
        super("CHAP");
        this.chapterId = chapterId;
        this.startTimeMs = startTimeMs;
        this.endTimeMs = endTimeMs;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.subFrames = subFrames;
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
        if (o != null && ChapterFrame.class == o.getClass()) {
            final ChapterFrame chapterFrame = (ChapterFrame)o;
            if (this.startTimeMs != chapterFrame.startTimeMs || this.endTimeMs != chapterFrame.endTimeMs || this.startOffset != chapterFrame.startOffset || this.endOffset != chapterFrame.endOffset || !Util.areEqual(this.chapterId, chapterFrame.chapterId) || !Arrays.equals(this.subFrames, chapterFrame.subFrames)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int startTimeMs = this.startTimeMs;
        final int endTimeMs = this.endTimeMs;
        final int n = (int)this.startOffset;
        final int n2 = (int)this.endOffset;
        final String chapterId = this.chapterId;
        int hashCode;
        if (chapterId != null) {
            hashCode = chapterId.hashCode();
        }
        else {
            hashCode = 0;
        }
        return ((((527 + startTimeMs) * 31 + endTimeMs) * 31 + n) * 31 + n2) * 31 + hashCode;
    }
    
    public void writeToParcel(final Parcel parcel, int i) {
        parcel.writeString(this.chapterId);
        parcel.writeInt(this.startTimeMs);
        parcel.writeInt(this.endTimeMs);
        parcel.writeLong(this.startOffset);
        parcel.writeLong(this.endOffset);
        parcel.writeInt(this.subFrames.length);
        final Id3Frame[] subFrames = this.subFrames;
        int length;
        for (length = subFrames.length, i = 0; i < length; ++i) {
            parcel.writeParcelable((Parcelable)subFrames[i], 0);
        }
    }
}
