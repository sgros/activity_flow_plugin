package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class ChapterFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public ChapterFrame createFromParcel(Parcel var1) {
         return new ChapterFrame(var1);
      }

      public ChapterFrame[] newArray(int var1) {
         return new ChapterFrame[var1];
      }
   };
   public final String chapterId;
   public final long endOffset;
   public final int endTimeMs;
   public final long startOffset;
   public final int startTimeMs;
   private final Id3Frame[] subFrames;

   ChapterFrame(Parcel var1) {
      super("CHAP");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.chapterId = (String)var2;
      this.startTimeMs = var1.readInt();
      this.endTimeMs = var1.readInt();
      this.startOffset = var1.readLong();
      this.endOffset = var1.readLong();
      int var3 = var1.readInt();
      this.subFrames = new Id3Frame[var3];

      for(int var4 = 0; var4 < var3; ++var4) {
         this.subFrames[var4] = (Id3Frame)var1.readParcelable(Id3Frame.class.getClassLoader());
      }

   }

   public ChapterFrame(String var1, int var2, int var3, long var4, long var6, Id3Frame[] var8) {
      super("CHAP");
      this.chapterId = var1;
      this.startTimeMs = var2;
      this.endTimeMs = var3;
      this.startOffset = var4;
      this.endOffset = var6;
      this.subFrames = var8;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && ChapterFrame.class == var1.getClass()) {
         ChapterFrame var3 = (ChapterFrame)var1;
         if (this.startTimeMs != var3.startTimeMs || this.endTimeMs != var3.endTimeMs || this.startOffset != var3.startOffset || this.endOffset != var3.endOffset || !Util.areEqual(this.chapterId, var3.chapterId) || !Arrays.equals(this.subFrames, var3.subFrames)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.startTimeMs;
      int var2 = this.endTimeMs;
      int var3 = (int)this.startOffset;
      int var4 = (int)this.endOffset;
      String var5 = this.chapterId;
      int var6;
      if (var5 != null) {
         var6 = var5.hashCode();
      } else {
         var6 = 0;
      }

      return ((((527 + var1) * 31 + var2) * 31 + var3) * 31 + var4) * 31 + var6;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.chapterId);
      var1.writeInt(this.startTimeMs);
      var1.writeInt(this.endTimeMs);
      var1.writeLong(this.startOffset);
      var1.writeLong(this.endOffset);
      var1.writeInt(this.subFrames.length);
      Id3Frame[] var3 = this.subFrames;
      int var4 = var3.length;

      for(var2 = 0; var2 < var4; ++var2) {
         var1.writeParcelable(var3[var2], 0);
      }

   }
}
