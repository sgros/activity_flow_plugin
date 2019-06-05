package android.support.v4.media;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.util.ArrayMap;

public final class MediaMetadataCompat implements Parcelable {
   public static final Creator CREATOR;
   static final ArrayMap METADATA_KEYS_TYPE = new ArrayMap();
   private static final String[] PREFERRED_BITMAP_ORDER;
   private static final String[] PREFERRED_DESCRIPTION_ORDER;
   private static final String[] PREFERRED_URI_ORDER;
   final Bundle mBundle;
   private Object mMetadataObj;

   static {
      METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.DATE", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.ART", 2);
      METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", 2);
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", 3);
      METADATA_KEYS_TYPE.put("android.media.metadata.RATING", 3);
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", 2);
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", 1);
      METADATA_KEYS_TYPE.put("android.media.metadata.ADVERTISEMENT", 0);
      METADATA_KEYS_TYPE.put("android.media.metadata.DOWNLOAD_STATUS", 0);
      PREFERRED_DESCRIPTION_ORDER = new String[]{"android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER"};
      PREFERRED_BITMAP_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART"};
      PREFERRED_URI_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI"};
      CREATOR = new Creator() {
         public MediaMetadataCompat createFromParcel(Parcel var1) {
            return new MediaMetadataCompat(var1);
         }

         public MediaMetadataCompat[] newArray(int var1) {
            return new MediaMetadataCompat[var1];
         }
      };
   }

   MediaMetadataCompat(Parcel var1) {
      this.mBundle = var1.readBundle(MediaSessionCompat.class.getClassLoader());
   }

   public static MediaMetadataCompat fromMediaMetadata(Object var0) {
      if (var0 != null && VERSION.SDK_INT >= 21) {
         Parcel var1 = Parcel.obtain();
         MediaMetadataCompatApi21.writeToParcel(var0, var1, 0);
         var1.setDataPosition(0);
         MediaMetadataCompat var2 = (MediaMetadataCompat)CREATOR.createFromParcel(var1);
         var1.recycle();
         var2.mMetadataObj = var0;
         return var2;
      } else {
         return null;
      }
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeBundle(this.mBundle);
   }
}
