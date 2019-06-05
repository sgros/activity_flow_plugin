package android.support.v4.media;

import android.media.MediaMetadata;
import android.os.Parcel;

class MediaMetadataCompatApi21 {
   public static void writeToParcel(Object var0, Parcel var1, int var2) {
      ((MediaMetadata)var0).writeToParcel(var1, var2);
   }
}
