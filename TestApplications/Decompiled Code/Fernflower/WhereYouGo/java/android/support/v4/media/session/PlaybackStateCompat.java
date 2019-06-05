package android.support.v4.media.session;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class PlaybackStateCompat implements Parcelable {
   public static final long ACTION_FAST_FORWARD = 64L;
   public static final long ACTION_PAUSE = 2L;
   public static final long ACTION_PLAY = 4L;
   public static final long ACTION_PLAY_FROM_MEDIA_ID = 1024L;
   public static final long ACTION_PLAY_FROM_SEARCH = 2048L;
   public static final long ACTION_PLAY_FROM_URI = 8192L;
   public static final long ACTION_PLAY_PAUSE = 512L;
   public static final long ACTION_PREPARE = 16384L;
   public static final long ACTION_PREPARE_FROM_MEDIA_ID = 32768L;
   public static final long ACTION_PREPARE_FROM_SEARCH = 65536L;
   public static final long ACTION_PREPARE_FROM_URI = 131072L;
   public static final long ACTION_REWIND = 8L;
   public static final long ACTION_SEEK_TO = 256L;
   public static final long ACTION_SET_RATING = 128L;
   public static final long ACTION_SET_REPEAT_MODE = 262144L;
   public static final long ACTION_SET_SHUFFLE_MODE_ENABLED = 524288L;
   public static final long ACTION_SKIP_TO_NEXT = 32L;
   public static final long ACTION_SKIP_TO_PREVIOUS = 16L;
   public static final long ACTION_SKIP_TO_QUEUE_ITEM = 4096L;
   public static final long ACTION_STOP = 1L;
   public static final Creator CREATOR = new Creator() {
      public PlaybackStateCompat createFromParcel(Parcel var1) {
         return new PlaybackStateCompat(var1);
      }

      public PlaybackStateCompat[] newArray(int var1) {
         return new PlaybackStateCompat[var1];
      }
   };
   public static final int ERROR_CODE_ACTION_ABORTED = 10;
   public static final int ERROR_CODE_APP_ERROR = 1;
   public static final int ERROR_CODE_AUTHENTICATION_EXPIRED = 3;
   public static final int ERROR_CODE_CONCURRENT_STREAM_LIMIT = 5;
   public static final int ERROR_CODE_CONTENT_ALREADY_PLAYING = 8;
   public static final int ERROR_CODE_END_OF_QUEUE = 11;
   public static final int ERROR_CODE_NOT_AVAILABLE_IN_REGION = 7;
   public static final int ERROR_CODE_NOT_SUPPORTED = 2;
   public static final int ERROR_CODE_PARENTAL_CONTROL_RESTRICTED = 6;
   public static final int ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED = 4;
   public static final int ERROR_CODE_SKIP_LIMIT_REACHED = 9;
   public static final int ERROR_CODE_UNKNOWN_ERROR = 0;
   private static final int KEYCODE_MEDIA_PAUSE = 127;
   private static final int KEYCODE_MEDIA_PLAY = 126;
   public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
   public static final int REPEAT_MODE_ALL = 2;
   public static final int REPEAT_MODE_NONE = 0;
   public static final int REPEAT_MODE_ONE = 1;
   public static final int STATE_BUFFERING = 6;
   public static final int STATE_CONNECTING = 8;
   public static final int STATE_ERROR = 7;
   public static final int STATE_FAST_FORWARDING = 4;
   public static final int STATE_NONE = 0;
   public static final int STATE_PAUSED = 2;
   public static final int STATE_PLAYING = 3;
   public static final int STATE_REWINDING = 5;
   public static final int STATE_SKIPPING_TO_NEXT = 10;
   public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
   public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
   public static final int STATE_STOPPED = 1;
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
      this.mExtras = var1.readBundle();
      this.mErrorCode = var1.readInt();
   }

   public static PlaybackStateCompat fromPlaybackState(Object var0) {
      PlaybackStateCompat var4;
      if (var0 != null && VERSION.SDK_INT >= 21) {
         List var1 = PlaybackStateCompatApi21.getCustomActions(var0);
         ArrayList var2 = null;
         if (var1 != null) {
            ArrayList var3 = new ArrayList(var1.size());
            Iterator var5 = var1.iterator();

            while(true) {
               var2 = var3;
               if (!var5.hasNext()) {
                  break;
               }

               var3.add(PlaybackStateCompat.CustomAction.fromCustomAction(var5.next()));
            }
         }

         Bundle var7;
         if (VERSION.SDK_INT >= 22) {
            var7 = PlaybackStateCompatApi22.getExtras(var0);
         } else {
            var7 = null;
         }

         PlaybackStateCompat var6 = new PlaybackStateCompat(PlaybackStateCompatApi21.getState(var0), PlaybackStateCompatApi21.getPosition(var0), PlaybackStateCompatApi21.getBufferedPosition(var0), PlaybackStateCompatApi21.getPlaybackSpeed(var0), PlaybackStateCompatApi21.getActions(var0), 0, PlaybackStateCompatApi21.getErrorMessage(var0), PlaybackStateCompatApi21.getLastPositionUpdateTime(var0), var2, PlaybackStateCompatApi21.getActiveQueueItemId(var0), var7);
         var6.mStateObj = var0;
         var4 = var6;
      } else {
         var4 = null;
      }

      return var4;
   }

   public static int toKeyCode(long var0) {
      byte var2;
      if (var0 == 4L) {
         var2 = 126;
      } else if (var0 == 2L) {
         var2 = 127;
      } else if (var0 == 32L) {
         var2 = 87;
      } else if (var0 == 16L) {
         var2 = 88;
      } else if (var0 == 1L) {
         var2 = 86;
      } else if (var0 == 64L) {
         var2 = 90;
      } else if (var0 == 8L) {
         var2 = 89;
      } else if (var0 == 512L) {
         var2 = 85;
      } else {
         var2 = 0;
      }

      return var2;
   }

   public int describeContents() {
      return 0;
   }

   public long getActions() {
      return this.mActions;
   }

   public long getActiveQueueItemId() {
      return this.mActiveItemId;
   }

   public long getBufferedPosition() {
      return this.mBufferedPosition;
   }

   public List getCustomActions() {
      return this.mCustomActions;
   }

   public int getErrorCode() {
      return this.mErrorCode;
   }

   public CharSequence getErrorMessage() {
      return this.mErrorMessage;
   }

   @Nullable
   public Bundle getExtras() {
      return this.mExtras;
   }

   public long getLastPositionUpdateTime() {
      return this.mUpdateTime;
   }

   public float getPlaybackSpeed() {
      return this.mSpeed;
   }

   public Object getPlaybackState() {
      Object var1;
      if (this.mStateObj == null && VERSION.SDK_INT >= 21) {
         ArrayList var4 = null;
         if (this.mCustomActions != null) {
            ArrayList var2 = new ArrayList(this.mCustomActions.size());
            Iterator var3 = this.mCustomActions.iterator();

            while(true) {
               var4 = var2;
               if (!var3.hasNext()) {
                  break;
               }

               var2.add(((PlaybackStateCompat.CustomAction)var3.next()).getCustomAction());
            }
         }

         if (VERSION.SDK_INT >= 22) {
            this.mStateObj = PlaybackStateCompatApi22.newInstance(this.mState, this.mPosition, this.mBufferedPosition, this.mSpeed, this.mActions, this.mErrorMessage, this.mUpdateTime, var4, this.mActiveItemId, this.mExtras);
         } else {
            this.mStateObj = PlaybackStateCompatApi21.newInstance(this.mState, this.mPosition, this.mBufferedPosition, this.mSpeed, this.mActions, this.mErrorMessage, this.mUpdateTime, var4, this.mActiveItemId);
         }

         var1 = this.mStateObj;
      } else {
         var1 = this.mStateObj;
      }

      return var1;
   }

   public long getPosition() {
      return this.mPosition;
   }

   public int getState() {
      return this.mState;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("PlaybackState {");
      var1.append("state=").append(this.mState);
      var1.append(", position=").append(this.mPosition);
      var1.append(", buffered position=").append(this.mBufferedPosition);
      var1.append(", speed=").append(this.mSpeed);
      var1.append(", updated=").append(this.mUpdateTime);
      var1.append(", actions=").append(this.mActions);
      var1.append(", error code=").append(this.mErrorCode);
      var1.append(", error message=").append(this.mErrorMessage);
      var1.append(", custom actions=").append(this.mCustomActions);
      var1.append(", active item id=").append(this.mActiveItemId);
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

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface Actions {
   }

   public static final class Builder {
      private long mActions;
      private long mActiveItemId = -1L;
      private long mBufferedPosition;
      private final List mCustomActions = new ArrayList();
      private int mErrorCode;
      private CharSequence mErrorMessage;
      private Bundle mExtras;
      private long mPosition;
      private float mRate;
      private int mState;
      private long mUpdateTime;

      public Builder() {
      }

      public Builder(PlaybackStateCompat var1) {
         this.mState = var1.mState;
         this.mPosition = var1.mPosition;
         this.mRate = var1.mSpeed;
         this.mUpdateTime = var1.mUpdateTime;
         this.mBufferedPosition = var1.mBufferedPosition;
         this.mActions = var1.mActions;
         this.mErrorCode = var1.mErrorCode;
         this.mErrorMessage = var1.mErrorMessage;
         if (var1.mCustomActions != null) {
            this.mCustomActions.addAll(var1.mCustomActions);
         }

         this.mActiveItemId = var1.mActiveItemId;
         this.mExtras = var1.mExtras;
      }

      public PlaybackStateCompat.Builder addCustomAction(PlaybackStateCompat.CustomAction var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("You may not add a null CustomAction to PlaybackStateCompat.");
         } else {
            this.mCustomActions.add(var1);
            return this;
         }
      }

      public PlaybackStateCompat.Builder addCustomAction(String var1, String var2, int var3) {
         return this.addCustomAction(new PlaybackStateCompat.CustomAction(var1, var2, var3, (Bundle)null));
      }

      public PlaybackStateCompat build() {
         return new PlaybackStateCompat(this.mState, this.mPosition, this.mBufferedPosition, this.mRate, this.mActions, this.mErrorCode, this.mErrorMessage, this.mUpdateTime, this.mCustomActions, this.mActiveItemId, this.mExtras);
      }

      public PlaybackStateCompat.Builder setActions(long var1) {
         this.mActions = var1;
         return this;
      }

      public PlaybackStateCompat.Builder setActiveQueueItemId(long var1) {
         this.mActiveItemId = var1;
         return this;
      }

      public PlaybackStateCompat.Builder setBufferedPosition(long var1) {
         this.mBufferedPosition = var1;
         return this;
      }

      public PlaybackStateCompat.Builder setErrorMessage(int var1, CharSequence var2) {
         this.mErrorCode = var1;
         this.mErrorMessage = var2;
         return this;
      }

      public PlaybackStateCompat.Builder setErrorMessage(CharSequence var1) {
         this.mErrorMessage = var1;
         return this;
      }

      public PlaybackStateCompat.Builder setExtras(Bundle var1) {
         this.mExtras = var1;
         return this;
      }

      public PlaybackStateCompat.Builder setState(int var1, long var2, float var4) {
         return this.setState(var1, var2, var4, SystemClock.elapsedRealtime());
      }

      public PlaybackStateCompat.Builder setState(int var1, long var2, float var4, long var5) {
         this.mState = var1;
         this.mPosition = var2;
         this.mUpdateTime = var5;
         this.mRate = var4;
         return this;
      }
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
         this.mExtras = var1.readBundle();
      }

      CustomAction(String var1, CharSequence var2, int var3, Bundle var4) {
         this.mAction = var1;
         this.mName = var2;
         this.mIcon = var3;
         this.mExtras = var4;
      }

      public static PlaybackStateCompat.CustomAction fromCustomAction(Object var0) {
         PlaybackStateCompat.CustomAction var2;
         if (var0 != null && VERSION.SDK_INT >= 21) {
            PlaybackStateCompat.CustomAction var1 = new PlaybackStateCompat.CustomAction(PlaybackStateCompatApi21.CustomAction.getAction(var0), PlaybackStateCompatApi21.CustomAction.getName(var0), PlaybackStateCompatApi21.CustomAction.getIcon(var0), PlaybackStateCompatApi21.CustomAction.getExtras(var0));
            var1.mCustomActionObj = var0;
            var2 = var1;
         } else {
            var2 = null;
         }

         return var2;
      }

      public int describeContents() {
         return 0;
      }

      public String getAction() {
         return this.mAction;
      }

      public Object getCustomAction() {
         Object var1;
         if (this.mCustomActionObj == null && VERSION.SDK_INT >= 21) {
            this.mCustomActionObj = PlaybackStateCompatApi21.CustomAction.newInstance(this.mAction, this.mName, this.mIcon, this.mExtras);
            var1 = this.mCustomActionObj;
         } else {
            var1 = this.mCustomActionObj;
         }

         return var1;
      }

      public Bundle getExtras() {
         return this.mExtras;
      }

      public int getIcon() {
         return this.mIcon;
      }

      public CharSequence getName() {
         return this.mName;
      }

      public String toString() {
         return "Action:mName='" + this.mName + ", mIcon=" + this.mIcon + ", mExtras=" + this.mExtras;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeString(this.mAction);
         TextUtils.writeToParcel(this.mName, var1, var2);
         var1.writeInt(this.mIcon);
         var1.writeBundle(this.mExtras);
      }

      public static final class Builder {
         private final String mAction;
         private Bundle mExtras;
         private final int mIcon;
         private final CharSequence mName;

         public Builder(String var1, CharSequence var2, int var3) {
            if (TextUtils.isEmpty(var1)) {
               throw new IllegalArgumentException("You must specify an action to build a CustomAction.");
            } else if (TextUtils.isEmpty(var2)) {
               throw new IllegalArgumentException("You must specify a name to build a CustomAction.");
            } else if (var3 == 0) {
               throw new IllegalArgumentException("You must specify an icon resource id to build a CustomAction.");
            } else {
               this.mAction = var1;
               this.mName = var2;
               this.mIcon = var3;
            }
         }

         public PlaybackStateCompat.CustomAction build() {
            return new PlaybackStateCompat.CustomAction(this.mAction, this.mName, this.mIcon, this.mExtras);
         }

         public PlaybackStateCompat.CustomAction.Builder setExtras(Bundle var1) {
            this.mExtras = var1;
            return this;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface ErrorCode {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface MediaKeyAction {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface RepeatMode {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface State {
   }
}
