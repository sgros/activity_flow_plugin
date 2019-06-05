package android.support.v4.media;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class MediaBrowserCompat {
   static final boolean DEBUG = Log.isLoggable("MediaBrowserCompat", 3);

   public abstract static class CustomActionCallback {
      public void onError(String var1, Bundle var2, Bundle var3) {
      }

      public void onProgressUpdate(String var1, Bundle var2, Bundle var3) {
      }

      public void onResult(String var1, Bundle var2, Bundle var3) {
      }
   }

   private static class CustomActionResultReceiver extends ResultReceiver {
      private final String mAction;
      private final MediaBrowserCompat.CustomActionCallback mCallback;
      private final Bundle mExtras;

      protected void onReceiveResult(int var1, Bundle var2) {
         if (this.mCallback != null) {
            MediaSessionCompat.ensureClassLoader(var2);
            switch(var1) {
            case -1:
               this.mCallback.onError(this.mAction, this.mExtras, var2);
               break;
            case 0:
               this.mCallback.onResult(this.mAction, this.mExtras, var2);
               break;
            case 1:
               this.mCallback.onProgressUpdate(this.mAction, this.mExtras, var2);
               break;
            default:
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown result code: ");
               var3.append(var1);
               var3.append(" (extras=");
               var3.append(this.mExtras);
               var3.append(", resultData=");
               var3.append(var2);
               var3.append(")");
               Log.w("MediaBrowserCompat", var3.toString());
            }

         }
      }
   }

   public abstract static class ItemCallback {
      public void onError(String var1) {
      }

      public void onItemLoaded(MediaBrowserCompat.MediaItem var1) {
      }
   }

   private static class ItemReceiver extends ResultReceiver {
      private final MediaBrowserCompat.ItemCallback mCallback;
      private final String mMediaId;

      protected void onReceiveResult(int var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         if (var1 == 0 && var2 != null && var2.containsKey("media_item")) {
            Parcelable var3 = var2.getParcelable("media_item");
            if (var3 != null && !(var3 instanceof MediaBrowserCompat.MediaItem)) {
               this.mCallback.onError(this.mMediaId);
            } else {
               this.mCallback.onItemLoaded((MediaBrowserCompat.MediaItem)var3);
            }

         } else {
            this.mCallback.onError(this.mMediaId);
         }
      }
   }

   public static class MediaItem implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaBrowserCompat.MediaItem createFromParcel(Parcel var1) {
            return new MediaBrowserCompat.MediaItem(var1);
         }

         public MediaBrowserCompat.MediaItem[] newArray(int var1) {
            return new MediaBrowserCompat.MediaItem[var1];
         }
      };
      private final MediaDescriptionCompat mDescription;
      private final int mFlags;

      MediaItem(Parcel var1) {
         this.mFlags = var1.readInt();
         this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var1);
      }

      public int describeContents() {
         return 0;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("MediaItem{");
         var1.append("mFlags=");
         var1.append(this.mFlags);
         var1.append(", mDescription=");
         var1.append(this.mDescription);
         var1.append('}');
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.mFlags);
         this.mDescription.writeToParcel(var1, var2);
      }
   }

   public abstract static class SearchCallback {
      public void onError(String var1, Bundle var2) {
      }

      public void onSearchResult(String var1, Bundle var2, List var3) {
      }
   }

   private static class SearchResultReceiver extends ResultReceiver {
      private final MediaBrowserCompat.SearchCallback mCallback;
      private final Bundle mExtras;
      private final String mQuery;

      protected void onReceiveResult(int var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         if (var1 == 0 && var2 != null && var2.containsKey("search_results")) {
            Parcelable[] var3 = var2.getParcelableArray("search_results");
            ArrayList var6 = null;
            if (var3 != null) {
               ArrayList var4 = new ArrayList();
               int var5 = var3.length;
               var1 = 0;

               while(true) {
                  var6 = var4;
                  if (var1 >= var5) {
                     break;
                  }

                  var4.add((MediaBrowserCompat.MediaItem)var3[var1]);
                  ++var1;
               }
            }

            this.mCallback.onSearchResult(this.mQuery, this.mExtras, var6);
         } else {
            this.mCallback.onError(this.mQuery, this.mExtras);
         }
      }
   }
}
