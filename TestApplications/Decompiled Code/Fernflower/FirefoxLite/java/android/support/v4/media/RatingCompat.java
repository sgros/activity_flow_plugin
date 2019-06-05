package android.support.v4.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class RatingCompat implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public RatingCompat createFromParcel(Parcel var1) {
         return new RatingCompat(var1.readInt(), var1.readFloat());
      }

      public RatingCompat[] newArray(int var1) {
         return new RatingCompat[var1];
      }
   };
   private final int mRatingStyle;
   private final float mRatingValue;

   RatingCompat(int var1, float var2) {
      this.mRatingStyle = var1;
      this.mRatingValue = var2;
   }

   public int describeContents() {
      return this.mRatingStyle;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Rating:style=");
      var1.append(this.mRatingStyle);
      var1.append(" rating=");
      String var2;
      if (this.mRatingValue < 0.0F) {
         var2 = "unrated";
      } else {
         var2 = String.valueOf(this.mRatingValue);
      }

      var1.append(var2);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mRatingStyle);
      var1.writeFloat(this.mRatingValue);
   }
}
