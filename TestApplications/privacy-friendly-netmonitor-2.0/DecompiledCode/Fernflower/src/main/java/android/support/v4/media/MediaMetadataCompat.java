package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Set;

public final class MediaMetadataCompat implements Parcelable {
   public static final Creator CREATOR;
   static final ArrayMap METADATA_KEYS_TYPE = new ArrayMap();
   public static final String METADATA_KEY_ADVERTISEMENT = "android.media.metadata.ADVERTISEMENT";
   public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
   public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
   public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
   public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
   public static final String METADATA_KEY_ART = "android.media.metadata.ART";
   public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
   public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
   public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
   public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
   public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
   public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
   public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
   public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
   public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
   public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
   public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
   public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
   public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
   public static final String METADATA_KEY_DOWNLOAD_STATUS = "android.media.metadata.DOWNLOAD_STATUS";
   public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
   public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
   public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
   public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
   public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
   public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
   public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
   public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
   public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
   public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
   public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
   static final int METADATA_TYPE_BITMAP = 2;
   static final int METADATA_TYPE_LONG = 0;
   static final int METADATA_TYPE_RATING = 3;
   static final int METADATA_TYPE_TEXT = 1;
   private static final String[] PREFERRED_BITMAP_ORDER;
   private static final String[] PREFERRED_DESCRIPTION_ORDER;
   private static final String[] PREFERRED_URI_ORDER;
   private static final String TAG = "MediaMetadata";
   final Bundle mBundle;
   private MediaDescriptionCompat mDescription;
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

   MediaMetadataCompat(Bundle var1) {
      this.mBundle = new Bundle(var1);
   }

   MediaMetadataCompat(Parcel var1) {
      this.mBundle = var1.readBundle();
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

   public boolean containsKey(String var1) {
      return this.mBundle.containsKey(var1);
   }

   public int describeContents() {
      return 0;
   }

   public Bitmap getBitmap(String var1) {
      Bitmap var3;
      try {
         var3 = (Bitmap)this.mBundle.getParcelable(var1);
      } catch (Exception var2) {
         Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", var2);
         var3 = null;
      }

      return var3;
   }

   public Bundle getBundle() {
      return this.mBundle;
   }

   public MediaDescriptionCompat getDescription() {
      if (this.mDescription != null) {
         return this.mDescription;
      } else {
         String var1 = this.getString("android.media.metadata.MEDIA_ID");
         CharSequence[] var2 = new CharSequence[3];
         CharSequence var3 = this.getText("android.media.metadata.DISPLAY_TITLE");
         int var5;
         if (!TextUtils.isEmpty(var3)) {
            var2[0] = var3;
            var2[1] = this.getText("android.media.metadata.DISPLAY_SUBTITLE");
            var2[2] = this.getText("android.media.metadata.DISPLAY_DESCRIPTION");
         } else {
            int var4 = 0;

            int var6;
            for(var5 = var4; var4 < var2.length && var5 < PREFERRED_DESCRIPTION_ORDER.length; var4 = var6) {
               var3 = this.getText(PREFERRED_DESCRIPTION_ORDER[var5]);
               var6 = var4;
               if (!TextUtils.isEmpty(var3)) {
                  var2[var4] = var3;
                  var6 = var4 + 1;
               }

               ++var5;
            }
         }

         var5 = 0;

         Uri var7;
         Bitmap var11;
         while(true) {
            String[] var10 = PREFERRED_BITMAP_ORDER;
            var7 = null;
            if (var5 >= var10.length) {
               var11 = null;
               break;
            }

            var11 = this.getBitmap(PREFERRED_BITMAP_ORDER[var5]);
            if (var11 != null) {
               break;
            }

            ++var5;
         }

         var5 = 0;

         Uri var13;
         while(true) {
            if (var5 >= PREFERRED_URI_ORDER.length) {
               var13 = null;
               break;
            }

            String var8 = this.getString(PREFERRED_URI_ORDER[var5]);
            if (!TextUtils.isEmpty(var8)) {
               var13 = Uri.parse(var8);
               break;
            }

            ++var5;
         }

         String var9 = this.getString("android.media.metadata.MEDIA_URI");
         if (!TextUtils.isEmpty(var9)) {
            var7 = Uri.parse(var9);
         }

         MediaDescriptionCompat.Builder var14 = new MediaDescriptionCompat.Builder();
         var14.setMediaId(var1);
         var14.setTitle(var2[0]);
         var14.setSubtitle(var2[1]);
         var14.setDescription(var2[2]);
         var14.setIconBitmap(var11);
         var14.setIconUri(var13);
         var14.setMediaUri(var7);
         Bundle var12 = new Bundle();
         if (this.mBundle.containsKey("android.media.metadata.BT_FOLDER_TYPE")) {
            var12.putLong("android.media.extra.BT_FOLDER_TYPE", this.getLong("android.media.metadata.BT_FOLDER_TYPE"));
         }

         if (this.mBundle.containsKey("android.media.metadata.DOWNLOAD_STATUS")) {
            var12.putLong("android.media.extra.DOWNLOAD_STATUS", this.getLong("android.media.metadata.DOWNLOAD_STATUS"));
         }

         if (!var12.isEmpty()) {
            var14.setExtras(var12);
         }

         this.mDescription = var14.build();
         return this.mDescription;
      }
   }

   public long getLong(String var1) {
      return this.mBundle.getLong(var1, 0L);
   }

   public Object getMediaMetadata() {
      if (this.mMetadataObj == null && VERSION.SDK_INT >= 21) {
         Parcel var1 = Parcel.obtain();
         this.writeToParcel(var1, 0);
         var1.setDataPosition(0);
         this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(var1);
         var1.recycle();
      }

      return this.mMetadataObj;
   }

   public RatingCompat getRating(String var1) {
      RatingCompat var3;
      try {
         if (VERSION.SDK_INT >= 19) {
            var3 = RatingCompat.fromRating(this.mBundle.getParcelable(var1));
         } else {
            var3 = (RatingCompat)this.mBundle.getParcelable(var1);
         }
      } catch (Exception var2) {
         Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", var2);
         var3 = null;
      }

      return var3;
   }

   public String getString(String var1) {
      CharSequence var2 = this.mBundle.getCharSequence(var1);
      return var2 != null ? var2.toString() : null;
   }

   public CharSequence getText(String var1) {
      return this.mBundle.getCharSequence(var1);
   }

   public Set keySet() {
      return this.mBundle.keySet();
   }

   public int size() {
      return this.mBundle.size();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeBundle(this.mBundle);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface BitmapKey {
   }

   public static final class Builder {
      private final Bundle mBundle;

      public Builder() {
         this.mBundle = new Bundle();
      }

      public Builder(MediaMetadataCompat var1) {
         this.mBundle = new Bundle(var1.mBundle);
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public Builder(MediaMetadataCompat var1, int var2) {
         this(var1);
         Iterator var5 = this.mBundle.keySet().iterator();

         while(true) {
            String var3;
            Bitmap var6;
            do {
               Object var4;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  var3 = (String)var5.next();
                  var4 = this.mBundle.get(var3);
               } while(!(var4 instanceof Bitmap));

               var6 = (Bitmap)var4;
            } while(var6.getHeight() <= var2 && var6.getWidth() <= var2);

            this.putBitmap(var3, this.scaleBitmap(var6, var2));
         }
      }

      private Bitmap scaleBitmap(Bitmap var1, int var2) {
         float var3 = (float)var2;
         var3 = Math.min(var3 / (float)var1.getWidth(), var3 / (float)var1.getHeight());
         var2 = (int)((float)var1.getHeight() * var3);
         return Bitmap.createScaledBitmap(var1, (int)((float)var1.getWidth() * var3), var2, true);
      }

      public MediaMetadataCompat build() {
         return new MediaMetadataCompat(this.mBundle);
      }

      public MediaMetadataCompat.Builder putBitmap(String var1, Bitmap var2) {
         if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && (Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1) != 2) {
            StringBuilder var3 = new StringBuilder();
            var3.append("The ");
            var3.append(var1);
            var3.append(" key cannot be used to put a Bitmap");
            throw new IllegalArgumentException(var3.toString());
         } else {
            this.mBundle.putParcelable(var1, var2);
            return this;
         }
      }

      public MediaMetadataCompat.Builder putLong(String var1, long var2) {
         if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && (Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1) != 0) {
            StringBuilder var4 = new StringBuilder();
            var4.append("The ");
            var4.append(var1);
            var4.append(" key cannot be used to put a long");
            throw new IllegalArgumentException(var4.toString());
         } else {
            this.mBundle.putLong(var1, var2);
            return this;
         }
      }

      public MediaMetadataCompat.Builder putRating(String var1, RatingCompat var2) {
         if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && (Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1) != 3) {
            StringBuilder var3 = new StringBuilder();
            var3.append("The ");
            var3.append(var1);
            var3.append(" key cannot be used to put a Rating");
            throw new IllegalArgumentException(var3.toString());
         } else {
            if (VERSION.SDK_INT >= 19) {
               this.mBundle.putParcelable(var1, (Parcelable)var2.getRating());
            } else {
               this.mBundle.putParcelable(var1, var2);
            }

            return this;
         }
      }

      public MediaMetadataCompat.Builder putString(String var1, String var2) {
         if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && (Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1) != 1) {
            StringBuilder var3 = new StringBuilder();
            var3.append("The ");
            var3.append(var1);
            var3.append(" key cannot be used to put a String");
            throw new IllegalArgumentException(var3.toString());
         } else {
            this.mBundle.putCharSequence(var1, var2);
            return this;
         }
      }

      public MediaMetadataCompat.Builder putText(String var1, CharSequence var2) {
         if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && (Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1) != 1) {
            StringBuilder var3 = new StringBuilder();
            var3.append("The ");
            var3.append(var1);
            var3.append(" key cannot be used to put a CharSequence");
            throw new IllegalArgumentException(var3.toString());
         } else {
            this.mBundle.putCharSequence(var1, var2);
            return this;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface LongKey {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface RatingKey {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface TextKey {
   }
}
