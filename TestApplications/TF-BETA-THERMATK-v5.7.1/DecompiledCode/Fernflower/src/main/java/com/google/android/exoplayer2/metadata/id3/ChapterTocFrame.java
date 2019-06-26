package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class ChapterTocFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public ChapterTocFrame createFromParcel(Parcel var1) {
         return new ChapterTocFrame(var1);
      }

      public ChapterTocFrame[] newArray(int var1) {
         return new ChapterTocFrame[var1];
      }
   };
   public final String[] children;
   public final String elementId;
   public final boolean isOrdered;
   public final boolean isRoot;
   private final Id3Frame[] subFrames;

   ChapterTocFrame(Parcel var1) {
      super("CTOC");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.elementId = (String)var2;
      byte var3 = var1.readByte();
      boolean var4 = true;
      int var5 = 0;
      boolean var6;
      if (var3 != 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      this.isRoot = var6;
      if (var1.readByte() != 0) {
         var6 = var4;
      } else {
         var6 = false;
      }

      this.isOrdered = var6;
      this.children = var1.createStringArray();
      int var7 = var1.readInt();

      for(this.subFrames = new Id3Frame[var7]; var5 < var7; ++var5) {
         this.subFrames[var5] = (Id3Frame)var1.readParcelable(Id3Frame.class.getClassLoader());
      }

   }

   public ChapterTocFrame(String var1, boolean var2, boolean var3, String[] var4, Id3Frame[] var5) {
      super("CTOC");
      this.elementId = var1;
      this.isRoot = var2;
      this.isOrdered = var3;
      this.children = var4;
      this.subFrames = var5;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && ChapterTocFrame.class == var1.getClass()) {
         ChapterTocFrame var3 = (ChapterTocFrame)var1;
         if (this.isRoot != var3.isRoot || this.isOrdered != var3.isOrdered || !Util.areEqual(this.elementId, var3.elementId) || !Arrays.equals(this.children, var3.children) || !Arrays.equals(this.subFrames, var3.subFrames)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      byte var1 = this.isRoot;
      byte var2 = this.isOrdered;
      String var3 = this.elementId;
      int var4;
      if (var3 != null) {
         var4 = var3.hashCode();
      } else {
         var4 = 0;
      }

      return ((527 + var1) * 31 + var2) * 31 + var4;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.elementId);
      var1.writeByte((byte)this.isRoot);
      var1.writeByte((byte)this.isOrdered);
      var1.writeStringArray(this.children);
      var1.writeInt(this.subFrames.length);
      Id3Frame[] var3 = this.subFrames;
      int var4 = var3.length;

      for(var2 = 0; var2 < var4; ++var2) {
         var1.writeParcelable(var3[var2], 0);
      }

   }
}
