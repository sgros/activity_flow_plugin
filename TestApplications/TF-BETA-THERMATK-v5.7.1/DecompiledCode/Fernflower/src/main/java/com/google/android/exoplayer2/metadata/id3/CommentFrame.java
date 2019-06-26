package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;

public final class CommentFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public CommentFrame createFromParcel(Parcel var1) {
         return new CommentFrame(var1);
      }

      public CommentFrame[] newArray(int var1) {
         return new CommentFrame[var1];
      }
   };
   public final String description;
   public final String language;
   public final String text;

   CommentFrame(Parcel var1) {
      super("COMM");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.language = (String)var2;
      var2 = var1.readString();
      Util.castNonNull(var2);
      this.description = (String)var2;
      String var3 = var1.readString();
      Util.castNonNull(var3);
      this.text = (String)var3;
   }

   public CommentFrame(String var1, String var2, String var3) {
      super("COMM");
      this.language = var1;
      this.description = var2;
      this.text = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && CommentFrame.class == var1.getClass()) {
         CommentFrame var3 = (CommentFrame)var1;
         if (!Util.areEqual(this.description, var3.description) || !Util.areEqual(this.language, var3.language) || !Util.areEqual(this.text, var3.text)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.language;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.description;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.text;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return ((527 + var3) * 31 + var4) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.id);
      var1.append(": language=");
      var1.append(this.language);
      var1.append(", description=");
      var1.append(this.description);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(super.id);
      var1.writeString(this.language);
      var1.writeString(this.text);
   }
}
