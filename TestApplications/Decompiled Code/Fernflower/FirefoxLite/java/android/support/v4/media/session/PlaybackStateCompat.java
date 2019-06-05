package android.support.v4.media.session;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class PlaybackStateCompat implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public PlaybackStateCompat createFromParcel(Parcel var1) {
         return new PlaybackStateCompat(var1);
      }

      public PlaybackStateCompat[] newArray(int var1) {
         return new PlaybackStateCompat[var1];
      }
   };
   final long mActions;
   final long mActiveItemId;
   final long mBufferedPosition;
   List mCustomActions;
   final int mErrorCode;
   final CharSequence mErrorMessage;
   final Bundle mExtras;
   final long mPosition;
   final float mSpeed;
   final int mState;
   private Object mStateObj;
   final long mUpdateTime;

   PlaybackStateCompat(int var1, long var2, long var4, float var6, long var7, int var9, CharSequence var10, long var11, List var13, long var14, Bundle var16) {
      this.mState = var1;
      this.mPosition = var2;
      this.mBufferedPosition = var4;
      this.mSpeed = var6;
      this.mActions = var7;
      this.mErrorCode = var9;
      this.mErrorMessage = var10;
      this.mUpdateTime = var11;
      this.mCustomActions = new ArrayList(var13);
      this.mActiveItemId = var14;
      this.mExtras = var16;
   }

   PlaybackStateCompat(Parcel var1) {
      this.mState = var1.readInt();
      this.mPosition = var1.readLong();
      this.mSpeed = var1.readFloat();
      this.mUpdateTime = var1.readLong();
      this.mBufferedPosition = var1.readLong();
      this.mActions = var1.readLong();
      this.mErrorMessage = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mCustomActions = var1.createTypedArrayList(PlaybackStateCompat.CustomAction.CREATOR);
      this.mActiveItemId = var1.readLong();
      this.mExtras = var1.readBundle(MediaSessionCompat.class.getClassLoader());
      this.mErrorCode = var1.readInt();
   }

   public static PlaybackStateCompat fromPlaybackState(Object var0) {
      Bundle var1 = null;
      if (var0 != null && VERSION.SDK_INT >= 21) {
         List var2 = PlaybackStateCompatApi21.getCustomActions(var0);
         ArrayList var3;
         if (var2 != null) {
            var3 = new ArrayList(var2.size());
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               var3.add(PlaybackStateCompat.CustomAction.fromCustomAction(var4.next()));
            }
         } else {
            var3 = null;
         }

         if (VERSION.SDK_INT >= 22) {
            var1 = PlaybackStateCompatApi22.getExtras(var0);
         }

         PlaybackStateCompat var5 = new PlaybackStateCompat(PlaybackStateCompatApi21.getState(var0), PlaybackStateCompatApi21.getPosition(var0), PlaybackStateCompatApi21.getBufferedPosition(var0), PlaybackStateCompatApi21.getPlaybackSpeed(var0), PlaybackStateCompatApi21.getActions(var0), 0, PlaybackStateCompatApi21.getErrorMessage(var0), PlaybackStateCompatApi21.getLastPositionUpdateTime(var0), var3, PlaybackStateCompatApi21.getActiveQueueItemId(var0), var1);
         var5.mStateObj = var0;
         return var5;
      } else {
         return null;
      }
   }

   public int describeContents() {
      return 0;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("PlaybackState {");
      var1.append("state=");
      var1.append(this.mState);
      var1.append(", position=");
      var1.append(this.mPosition);
      var1.append(", buffered position=");
      var1.append(this.mBufferedPosition);
      var1.append(", speed=");
      var1.append(this.mSpeed);
      var1.append(", updated=");
      var1.append(this.mUpdateTime);
      var1.append(", actions=");
      var1.append(this.mActions);
      var1.append(", error code=");
      var1.append(this.mErrorCode);
      var1.append(", error message=");
      var1.append(this.mErrorMessage);
      var1.append(", custom actions=");
      var1.append(this.mCustomActions);
      var1.append(", active item id=");
      var1.append(this.mActiveItemId);
      var1.append("}");
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mState);
      var1.writeLong(this.mPosition);
      var1.writeFloat(this.mSpeed);
      var1.writeLong(this.mUpdateTime);
      var1.writeLong(this.mBufferedPosition);
      var1.writeLong(this.mActions);
      TextUtils.writeToParcel(this.mErrorMessage, var1, var2);
      var1.writeTypedList(this.mCustomActions);
      var1.writeLong(this.mActiveItemId);
      var1.writeBundle(this.mExtras);
      var1.writeInt(this.mErrorCode);
   }

   public static final class CustomAction implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public PlaybackStateCompat.CustomAction createFromParcel(Parcel var1) {
            return new PlaybackStateCompat.CustomAction(var1);
         }

         public PlaybackStateCompat.CustomAction[] newArray(int var1) {
            return new PlaybackStateCompat.CustomAction[var1];
         }
      };
      private final String mAction;
      private Object mCustomActionObj;
      private final Bundle mExtras;
      private final int mIcon;
      private final CharSequence mName;

      CustomAction(Parcel var1) {
         this.mAction = var1.readString();
         this.mName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
         this.mIcon = var1.readInt();
         this.mExtras = var1.readBundle(MediaSessionCompat.class.getClassLoader());
      }

      CustomAction(String var1, CharSequence var2, int var3, Bundle var4) {
         this.mAction = var1;
         this.mName = var2;
         this.mIcon = var3;
         this.mExtras = var4;
      }

      public static PlaybackStateCompat.CustomAction fromCustomAction(Object var0) {
         if (var0 != null && VERSION.SDK_INT >= 21) {
            PlaybackStateCompat.CustomAction var1 = new PlaybackStateCompat.CustomAction(PlaybackStateCompatApi21.CustomAction.getAction(var0), PlaybackStateCompatApi21.CustomAction.getName(var0), PlaybackStateCompatApi21.CustomAction.getIcon(var0), PlaybackStateCompatApi21.CustomAction.getExtras(var0));
            var1.mCustomActionObj = var0;
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
         var1.append("Action:mName='");
         var1.append(this.mName);
         var1.append(", mIcon=");
         var1.append(this.mIcon);
         var1.append(", mExtras=");
         var1.append(this.mExtras);
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeString(this.mAction);
         TextUtils.writeToParcel(this.mName, var1, var2);
         var1.writeInt(this.mIcon);
         var1.writeBundle(this.mExtras);
      }
   }
}
