package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;

public final class MediaDescriptionCompat implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public MediaDescriptionCompat createFromParcel(Parcel var1) {
         return VERSION.SDK_INT < 21 ? new MediaDescriptionCompat(var1) : MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(var1));
      }

      public MediaDescriptionCompat[] newArray(int var1) {
         return new MediaDescriptionCompat[var1];
      }
   };
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
      ClassLoader var2 = this.getClass().getClassLoader();
      this.mIcon = (Bitmap)var1.readParcelable(var2);
      this.mIconUri = (Uri)var1.readParcelable(var2);
      this.mExtras = var1.readBundle(var2);
      this.mMediaUri = (Uri)var1.readParcelable(var2);
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
      if (var0 != null && VERSION.SDK_INT >= 21) {
         MediaDescriptionCompat.Builder var2 = new MediaDescriptionCompat.Builder();
         var2.setMediaId(MediaDescriptionCompatApi21.getMediaId(var0));
         var2.setTitle(MediaDescriptionCompatApi21.getTitle(var0));
         var2.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(var0));
         var2.setDescription(MediaDescriptionCompatApi21.getDescription(var0));
         var2.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(var0));
         var2.setIconUri(MediaDescriptionCompatApi21.getIconUri(var0));
         Bundle var3 = MediaDescriptionCompatApi21.getExtras(var0);
         Uri var4;
         if (var3 != null) {
            MediaSessionCompat.ensureClassLoader(var3);
            var4 = (Uri)var3.getParcelable("android.support.v4.media.description.MEDIA_URI");
         } else {
            var4 = null;
         }

         label30: {
            if (var4 != null) {
               if (var3.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") && var3.size() == 2) {
                  break label30;
               }

               var3.remove("android.support.v4.media.description.MEDIA_URI");
               var3.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
            }

            var1 = var3;
         }

         var2.setExtras(var1);
         if (var4 != null) {
            var2.setMediaUri(var4);
         } else if (VERSION.SDK_INT >= 23) {
            var2.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(var0));
         }

         MediaDescriptionCompat var5 = var2.build();
         var5.mDescriptionObj = var0;
         return var5;
      } else {
         return null;
      }
   }

   public int describeContents() {
      return 0;
   }

   public Object getMediaDescription() {
      if (this.mDescriptionObj == null && VERSION.SDK_INT >= 21) {
         Object var1 = MediaDescriptionCompatApi21.Builder.newInstance();
         MediaDescriptionCompatApi21.Builder.setMediaId(var1, this.mMediaId);
         MediaDescriptionCompatApi21.Builder.setTitle(var1, this.mTitle);
         MediaDescriptionCompatApi21.Builder.setSubtitle(var1, this.mSubtitle);
         MediaDescriptionCompatApi21.Builder.setDescription(var1, this.mDescription);
         MediaDescriptionCompatApi21.Builder.setIconBitmap(var1, this.mIcon);
         MediaDescriptionCompatApi21.Builder.setIconUri(var1, this.mIconUri);
         Bundle var2 = this.mExtras;
         Bundle var3 = var2;
         if (VERSION.SDK_INT < 23) {
            var3 = var2;
            if (this.mMediaUri != null) {
               var3 = var2;
               if (var2 == null) {
                  var3 = new Bundle();
                  var3.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
               }

               var3.putParcelable("android.support.v4.media.description.MEDIA_URI", this.mMediaUri);
            }
         }

         MediaDescriptionCompatApi21.Builder.setExtras(var1, var3);
         if (VERSION.SDK_INT >= 23) {
            MediaDescriptionCompatApi23.Builder.setMediaUri(var1, this.mMediaUri);
         }

         this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(var1);
         return this.mDescriptionObj;
      } else {
         return this.mDescriptionObj;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mTitle);
      var1.append(", ");
      var1.append(this.mSubtitle);
      var1.append(", ");
      var1.append(this.mDescription);
      return var1.toString();
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

      public MediaDescriptionCompat.Builder setDescription(CharSequence var1) {
         this.mDescription = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setExtras(Bundle var1) {
         this.mExtras = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setIconBitmap(Bitmap var1) {
         this.mIcon = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setIconUri(Uri var1) {
         this.mIconUri = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setMediaId(String var1) {
         this.mMediaId = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setMediaUri(Uri var1) {
         this.mMediaUri = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setSubtitle(CharSequence var1) {
         this.mSubtitle = var1;
         return this;
      }

      public MediaDescriptionCompat.Builder setTitle(CharSequence var1) {
         this.mTitle = var1;
         return this;
      }
   }
}
