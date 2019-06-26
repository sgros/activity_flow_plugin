package com.google.android.exoplayer2.metadata.scte35;

import com.google.android.exoplayer2.metadata.Metadata;

public abstract class SpliceCommand implements Metadata.Entry {
   public int describeContents() {
      return 0;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("SCTE-35 splice command: type=");
      var1.append(this.getClass().getSimpleName());
      return var1.toString();
   }
}
