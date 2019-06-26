package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata;

public abstract class Id3Frame implements Metadata.Entry {
   public final String id;

   public Id3Frame(String var1) {
      this.id = var1;
   }

   public int describeContents() {
      return 0;
   }

   public String toString() {
      return this.id;
   }
}
