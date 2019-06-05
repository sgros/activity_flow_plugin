package android.support.v4.media.session;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.v4.media.MediaDescriptionCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat {
   public static void ensureClassLoader(Bundle var0) {
      if (var0 != null) {
         var0.setClassLoader(MediaSessionCompat.class.getClassLoader());
      }

   }

   public static final class QueueItem implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaSessionCompat.QueueItem createFromParcel(Parcel var1) {
            return new MediaSessionCompat.QueueItem(var1);
         }

         public MediaSessionCompat.QueueItem[] newArray(int var1) {
            return new MediaSessionCompat.QueueItem[var1];
         }
      };
      private final MediaDescriptionCompat mDescription;
      private final long mId;
      private Object mItem;

      QueueItem(Parcel var1) {
         this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var1);
         this.mId = var1.readLong();
      }

      private QueueItem(Object var1, MediaDescriptionCompat var2, long var3) {
         if (var2 != null) {
            if (var3 != -1L) {
               this.mDescription = var2;
               this.mId = var3;
               this.mItem = var1;
            } else {
               throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
         } else {
            throw new IllegalArgumentException("Description cannot be null.");
         }
      }

      public static MediaSessionCompat.QueueItem fromQueueItem(Object var0) {
         return var0 != null && VERSION.SDK_INT >= 21 ? new MediaSessionCompat.QueueItem(var0, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(var0)), MediaSessionCompatApi21.QueueItem.getQueueId(var0)) : null;
      }

      public static List fromQueueItemList(List var0) {
         if (var0 != null && VERSION.SDK_INT >= 21) {
            ArrayList var1 = new ArrayList();
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               var1.add(fromQueueItem(var2.next()));
            }

            return var1;
         } else {
            return null;
         }
      }

      public int describeContents() {
         return 0;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("MediaSession.QueueItem {Description=");
         var1.append(this.mDescription);
         var1.append(", Id=");
         var1.append(this.mId);
         var1.append(" }");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         this.mDescription.writeToParcel(var1, var2);
         var1.writeLong(this.mId);
      }
   }

   public static final class ResultReceiverWrapper implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel var1) {
            return new MediaSessionCompat.ResultReceiverWrapper(var1);
         }

         public MediaSessionCompat.ResultReceiverWrapper[] newArray(int var1) {
            return new MediaSessionCompat.ResultReceiverWrapper[var1];
         }
      };
      ResultReceiver mResultReceiver;

      ResultReceiverWrapper(Parcel var1) {
         this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(var1);
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         this.mResultReceiver.writeToParcel(var1, var2);
      }
   }

   public static final class Token implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaSessionCompat.Token createFromParcel(Parcel var1) {
            Object var2;
            if (VERSION.SDK_INT >= 21) {
               var2 = var1.readParcelable((ClassLoader)null);
            } else {
               var2 = var1.readStrongBinder();
            }

            return new MediaSessionCompat.Token(var2);
         }

         public MediaSessionCompat.Token[] newArray(int var1) {
            return new MediaSessionCompat.Token[var1];
         }
      };
      private IMediaSession mExtraBinder;
      private final Object mInner;
      private Bundle mSessionToken2Bundle;

      Token(Object var1) {
         this(var1, (IMediaSession)null, (Bundle)null);
      }

      Token(Object var1, IMediaSession var2, Bundle var3) {
         this.mInner = var1;
         this.mExtraBinder = var2;
         this.mSessionToken2Bundle = var3;
      }

      public int describeContents() {
         return 0;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof MediaSessionCompat.Token)) {
            return false;
         } else {
            MediaSessionCompat.Token var3 = (MediaSessionCompat.Token)var1;
            if (this.mInner == null) {
               if (var3.mInner != null) {
                  var2 = false;
               }

               return var2;
            } else {
               return var3.mInner == null ? false : this.mInner.equals(var3.mInner);
            }
         }
      }

      public IMediaSession getExtraBinder() {
         return this.mExtraBinder;
      }

      public int hashCode() {
         return this.mInner == null ? 0 : this.mInner.hashCode();
      }

      public void setExtraBinder(IMediaSession var1) {
         this.mExtraBinder = var1;
      }

      public void setSessionToken2Bundle(Bundle var1) {
         this.mSessionToken2Bundle = var1;
      }

      public void writeToParcel(Parcel var1, int var2) {
         if (VERSION.SDK_INT >= 21) {
            var1.writeParcelable((Parcelable)this.mInner, var2);
         } else {
            var1.writeStrongBinder((IBinder)this.mInner);
         }

      }
   }
}
