package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;

public final class MediaDescriptionCompat implements Parcelable {
   public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
   public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
   public static final long BT_FOLDER_TYPE_GENRES = 4L;
   public static final long BT_FOLDER_TYPE_MIXED = 0L;
   public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
   public static final long BT_FOLDER_TYPE_TITLES = 1L;
   public static final long BT_FOLDER_TYPE_YEARS = 6L;
   public static final Creator CREATOR = new Creator() {
      public MediaDescriptionCompat createFromParcel(Parcel var1) {
         MediaDescriptionCompat var2;
         if (VERSION.SDK_INT < 21) {
            var2 = new MediaDescriptionCompat(var1);
         } else {
            var2 = MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(var1));
         }

         return var2;
      }

      public MediaDescriptionCompat[] newArray(int var1) {
         return new MediaDescriptionCompat[var1];
      }
   };
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
   public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";
   private final CharSequence mDescription;
   private Object mDescriptionObj;
   private final Bundle mExtras;
   private final Bitmap mIcon;
   private final Uri mIconUri;
   private final String mMediaId;
   private final Uri mMediaUri;
   private final CharSequence mSubtitle;
   private final CharSequence mTitle;

   MediaDescriptionCompat(Parcel var1) {
      this.mMediaId = var1.readString();
      this.mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mIcon = (Bitmap)var1.readParcelable((ClassLoader)null);
      this.mIconUri = (Uri)var1.readParcelable((ClassLoader)null);
      this.mExtras = var1.readBundle();
      this.mMediaUri = (Uri)var1.readParcelable((ClassLoader)null);
   }

   MediaDescriptionCompat(String var1, CharSequence var2, CharSequence var3, CharSequence var4, Bitmap var5, Uri var6, Bundle var7, Uri var8) {
      this.mMediaId = var1;
      this.mTitle = var2;
      this.mSubtitle = var3;
      this.mDescription = var4;
      this.mIcon = var5;
      this.mIconUri = var6;
      this.mExtras = var7;
      this.mMediaUri = var8;
   }

   public static MediaDescriptionCompat fromMediaDescription(Object var0) {
      Bundle var1 = null;
      MediaDescriptionCompat var2 = var1;
      if (var0 != null) {
         if (VERSION.SDK_INT < 21) {
            var2 = var1;
         } else {
            MediaDescriptionCompat.Builder var3 = new MediaDescriptionCompat.Builder();
            var3.setMediaId(MediaDescriptionCompatApi21.getMediaId(var0));
            var3.setTitle(MediaDescriptionCompatApi21.getTitle(var0));
            var3.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(var0));
            var3.setDescription(MediaDescriptionCompatApi21.getDescription(var0));
            var3.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(var0));
            var3.setIconUri(MediaDescriptionCompatApi21.getIconUri(var0));
            Bundle var4 = MediaDescriptionCompatApi21.getExtras(var0);
            Uri var5;
            if (var4 == null) {
               var5 = null;
            } else {
               var5 = (Uri)var4.getParcelable("android.support.v4.media.description.MEDIA_URI");
            }

            var1 = var4;
            if (var5 != null) {
               if (var4.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") && var4.size() == 2) {
                  var1 = null;
               } else {
                  var4.remove("android.support.v4.media.description.MEDIA_URI");
                  var4.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
                  var1 = var4;
               }
            }

            var3.setExtras(var1);
            if (var5 != null) {
               var3.setMediaUri(var5);
            } else if (VERSION.SDK_INT >= 23) {
               var3.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(var0));
            }

            var2 = var3.build();
            var2.mDescriptionObj = var0;
         }
      }

      return var2;
   }

   public int describeContents() {
      return 0;
   }

   @Nullable
   public CharSequence getDescription() {
      return this.mDescription;
   }

   @Nullable
   public Bundle getExtras() {
      return this.mExtras;
   }

   @Nullable
   public Bitmap getIconBitmap() {
      return this.mIcon;
   }

   @Nullable
   public Uri getIconUri() {
      return this.mIconUri;
   }

   public Object getMediaDescription() {
      Object var1;
      if (this.mDescriptionObj == null && VERSION.SDK_INT >= 21) {
         Object var2 = MediaDescriptionCompatApi21.Builder.newInstance();
         MediaDescriptionCompatApi21.Builder.setMediaId(var2, this.mMediaId);
         MediaDescriptionCompatApi21.Builder.setTitle(var2, this.mTitle);
         MediaDescriptionCompatApi21.Builder.setSubtitle(var2, this.mSubtitle);
         MediaDescriptionCompatApi21.Builder.setDescription(var2, this.mDescription);
         MediaDescriptionCompatApi21.Builder.setIconBitmap(var2, this.mIcon);
         MediaDescriptionCompatApi21.Builder.setIconUri(var2, this.mIconUri);
         Bundle var3 = this.mExtras;
         Bundle var4 = var3;
         if (VERSION.SDK_INT < 23) {
            var4 = var3;
            if (this.mMediaUri != null) {
               var4 = var3;
               if (var3 == null) {
                  var4 = new Bundle();
                  var4.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
               }

               var4.putParcelable("android.support.v4.media.description.MEDIA_URI", this.mMediaUri);
            }
         }

         MediaDescriptionCompatApi21.Builder.setExtras(var2, var4);
         if (VERSION.SDK_INT >= 23) {
            MediaDescriptionCompatApi23.Builder.setMediaUri(var2, this.mMediaUri);
         }

         this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(var2);
         var1 = this.mDescriptionObj;
      } else {
         var1 = this.mDescriptionObj;
      }

      return var1;
   }

   @Nullable
   public String getMediaId() {
      return this.mMediaId;
   }

   @Nullable
   public Uri getMediaUri() {
      return this.mMediaUri;
   }

   @Nullable
   public CharSequence getSubtitle() {
      return this.mSubtitle;
   }

   @Nullable
   public CharSequence getTitle() {
      return this.mTitle;
   }

   public String toString() {
      return this.mTitle + ", " + this.mSubtitle + ", " + this.mDescription;
   }

   public void writeToParcel(Parcel var1, int var2) {
      if (VERSION.SDK_INT < 21) {
         var1.writeString(this.mMediaId);
         TextUtils.writeToParcel(this.mTitle, var1, var2);
         TextUtils.writeToParcel(this.mSubtitle, var1, var2);
         TextUtils.writeToParcel(this.mDescription, var1, var2);
         var1.writeParcelable(this.mIcon, var2);
         var1.writeParcelable(this.mIconUri, var2);
         var1.writeBundle(this.mExtras);
         var1.writeParcelable(this.mMediaUri, var2);
      } else {
         MediaDescriptionCompatApi21.writeToParcel(this.getMediaDescription(), var1, var2);
      }

   }

   public static final class Builder {
      private CharSequence mDescription;
      private Bundle mExtras;
      private Bitmap mIcon;
      private Uri mIconUri;
      private String mMediaId;
      private Uri mMediaUri;
      private CharSequence mSubtitle;
      private CharSequence mTitle;

      public MediaDescriptionCompat build() {
         return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
      }

      public MediaDescriptionCompat.Builder setDescription(@Nullable CharSequence var1) {
         this.mDescription = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setExtras(@Nullable Bundle var1) {
         this.mExtras = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setIconBitmap(@Nullable Bitmap var1) {
         this.mIcon = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setIconUri(@Nullable Uri var1) {
         this.mIconUri = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setMediaId(@Nullable String var1) {
         this.mMediaId = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setMediaUri(@Nullable Uri var1) {
         this.mMediaUri = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setSubtitle(@Nullable CharSequence var1) {
         this.mSubtitle = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setTitle(@Nullable CharSequence var1) {
         this.mTitle = var1;
         return this;
      }
   }
}
