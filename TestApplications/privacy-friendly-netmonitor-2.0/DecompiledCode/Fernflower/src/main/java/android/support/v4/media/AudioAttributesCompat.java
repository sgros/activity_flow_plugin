package android.support.v4.media;

import android.media.AudioAttributes;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public class AudioAttributesCompat {
   public static final int CONTENT_TYPE_MOVIE = 3;
   public static final int CONTENT_TYPE_MUSIC = 2;
   public static final int CONTENT_TYPE_SONIFICATION = 4;
   public static final int CONTENT_TYPE_SPEECH = 1;
   public static final int CONTENT_TYPE_UNKNOWN = 0;
   private static final int FLAG_ALL = 1023;
   private static final int FLAG_ALL_PUBLIC = 273;
   public static final int FLAG_AUDIBILITY_ENFORCED = 1;
   private static final int FLAG_BEACON = 8;
   private static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
   private static final int FLAG_BYPASS_MUTE = 128;
   private static final int FLAG_DEEP_BUFFER = 512;
   public static final int FLAG_HW_AV_SYNC = 16;
   private static final int FLAG_HW_HOTWORD = 32;
   private static final int FLAG_LOW_LATENCY = 256;
   private static final int FLAG_SCO = 4;
   private static final int FLAG_SECURE = 2;
   private static final int[] SDK_USAGES;
   private static final int SUPPRESSIBLE_CALL = 2;
   private static final int SUPPRESSIBLE_NOTIFICATION = 1;
   private static final SparseIntArray SUPPRESSIBLE_USAGES = new SparseIntArray();
   private static final String TAG = "AudioAttributesCompat";
   public static final int USAGE_ALARM = 4;
   public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
   public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
   public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
   public static final int USAGE_ASSISTANT = 16;
   public static final int USAGE_GAME = 14;
   public static final int USAGE_MEDIA = 1;
   public static final int USAGE_NOTIFICATION = 5;
   public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
   public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
   public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
   public static final int USAGE_NOTIFICATION_EVENT = 10;
   public static final int USAGE_NOTIFICATION_RINGTONE = 6;
   public static final int USAGE_UNKNOWN = 0;
   private static final int USAGE_VIRTUAL_SOURCE = 15;
   public static final int USAGE_VOICE_COMMUNICATION = 2;
   public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
   private static boolean sForceLegacyBehavior;
   private AudioAttributesCompatApi21.Wrapper mAudioAttributesWrapper;
   int mContentType;
   int mFlags;
   Integer mLegacyStream;
   int mUsage;

   static {
      SUPPRESSIBLE_USAGES.put(5, 1);
      SUPPRESSIBLE_USAGES.put(6, 2);
      SUPPRESSIBLE_USAGES.put(7, 2);
      SUPPRESSIBLE_USAGES.put(8, 1);
      SUPPRESSIBLE_USAGES.put(9, 1);
      SUPPRESSIBLE_USAGES.put(10, 1);
      SDK_USAGES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};
   }

   private AudioAttributesCompat() {
      this.mUsage = 0;
      this.mContentType = 0;
      this.mFlags = 0;
   }

   // $FF: synthetic method
   AudioAttributesCompat(Object var1) {
      this();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static void setForceLegacyBehavior(boolean var0) {
      sForceLegacyBehavior = var0;
   }

   static int toVolumeStreamType(boolean var0, int var1, int var2) {
      byte var3 = 1;
      byte var6;
      if ((var1 & 1) == 1) {
         if (var0) {
            var6 = var3;
         } else {
            var6 = 7;
         }

         return var6;
      } else {
         var3 = 0;
         byte var4 = 0;
         if ((var1 & 4) == 4) {
            if (var0) {
               var6 = var4;
            } else {
               var6 = 6;
            }

            return var6;
         } else {
            var1 = 3;
            switch(var2) {
            case 0:
               if (var0) {
                  var1 = Integer.MIN_VALUE;
               }

               return var1;
            case 1:
            case 12:
            case 14:
            case 16:
               return 3;
            case 2:
               return 0;
            case 3:
               if (var0) {
                  var6 = var3;
               } else {
                  var6 = 8;
               }

               return var6;
            case 4:
               return 4;
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
               return 5;
            case 6:
               return 2;
            case 11:
               return 10;
            case 13:
               return 1;
            case 15:
            default:
               if (var0) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Unknown usage value ");
                  var5.append(var2);
                  var5.append(" in audio attributes");
                  throw new IllegalArgumentException(var5.toString());
               } else {
                  return 3;
               }
            }
         }
      }
   }

   static int toVolumeStreamType(boolean var0, AudioAttributesCompat var1) {
      return toVolumeStreamType(var0, var1.getFlags(), var1.getUsage());
   }

   private static int usageForStreamType(int var0) {
      switch(var0) {
      case 0:
         return 2;
      case 1:
      case 7:
         return 13;
      case 2:
         return 6;
      case 3:
         return 1;
      case 4:
         return 4;
      case 5:
         return 5;
      case 6:
         return 2;
      case 8:
         return 3;
      case 9:
      default:
         return 0;
      case 10:
         return 11;
      }
   }

   static String usageToString(int var0) {
      switch(var0) {
      case 0:
         return new String("USAGE_UNKNOWN");
      case 1:
         return new String("USAGE_MEDIA");
      case 2:
         return new String("USAGE_VOICE_COMMUNICATION");
      case 3:
         return new String("USAGE_VOICE_COMMUNICATION_SIGNALLING");
      case 4:
         return new String("USAGE_ALARM");
      case 5:
         return new String("USAGE_NOTIFICATION");
      case 6:
         return new String("USAGE_NOTIFICATION_RINGTONE");
      case 7:
         return new String("USAGE_NOTIFICATION_COMMUNICATION_REQUEST");
      case 8:
         return new String("USAGE_NOTIFICATION_COMMUNICATION_INSTANT");
      case 9:
         return new String("USAGE_NOTIFICATION_COMMUNICATION_DELAYED");
      case 10:
         return new String("USAGE_NOTIFICATION_EVENT");
      case 11:
         return new String("USAGE_ASSISTANCE_ACCESSIBILITY");
      case 12:
         return new String("USAGE_ASSISTANCE_NAVIGATION_GUIDANCE");
      case 13:
         return new String("USAGE_ASSISTANCE_SONIFICATION");
      case 14:
         return new String("USAGE_GAME");
      case 15:
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("unknown usage ");
         var1.append(var0);
         return new String(var1.toString());
      case 16:
         return new String("USAGE_ASSISTANT");
      }
   }

   @Nullable
   public static AudioAttributesCompat wrap(@NonNull Object var0) {
      if (VERSION.SDK_INT >= 21 && !sForceLegacyBehavior) {
         AudioAttributesCompat var1 = new AudioAttributesCompat();
         var1.mAudioAttributesWrapper = AudioAttributesCompatApi21.Wrapper.wrap((AudioAttributes)var0);
         return var1;
      } else {
         return null;
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         AudioAttributesCompat var3 = (AudioAttributesCompat)var1;
         if (VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
            return this.mAudioAttributesWrapper.unwrap().equals(var3.unwrap());
         } else {
            if (this.mContentType == var3.getContentType() && this.mFlags == var3.getFlags() && this.mUsage == var3.getUsage()) {
               if (this.mLegacyStream != null) {
                  if (this.mLegacyStream.equals(var3.mLegacyStream)) {
                     return var2;
                  }
               } else if (var3.mLegacyStream == null) {
                  return var2;
               }
            }

            var2 = false;
            return var2;
         }
      } else {
         return false;
      }
   }

   public int getContentType() {
      return VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && this.mAudioAttributesWrapper != null ? this.mAudioAttributesWrapper.unwrap().getContentType() : this.mContentType;
   }

   public int getFlags() {
      if (VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
         return this.mAudioAttributesWrapper.unwrap().getFlags();
      } else {
         int var1 = this.mFlags;
         int var2 = this.getLegacyStreamType();
         int var3;
         if (var2 == 6) {
            var3 = var1 | 4;
         } else {
            var3 = var1;
            if (var2 == 7) {
               var3 = var1 | 1;
            }
         }

         return var3 & 273;
      }
   }

   public int getLegacyStreamType() {
      if (this.mLegacyStream != null) {
         return this.mLegacyStream;
      } else {
         return VERSION.SDK_INT >= 21 && !sForceLegacyBehavior ? AudioAttributesCompatApi21.toLegacyStreamType(this.mAudioAttributesWrapper) : toVolumeStreamType(false, this.mFlags, this.mUsage);
      }
   }

   public int getUsage() {
      return VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && this.mAudioAttributesWrapper != null ? this.mAudioAttributesWrapper.unwrap().getUsage() : this.mUsage;
   }

   public int getVolumeControlStream() {
      if (this == null) {
         throw new IllegalArgumentException("Invalid null audio attributes");
      } else {
         return VERSION.SDK_INT >= 26 && !sForceLegacyBehavior && this.unwrap() != null ? ((AudioAttributes)this.unwrap()).getVolumeControlStream() : toVolumeStreamType(true, this);
      }
   }

   public int hashCode() {
      return VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && this.mAudioAttributesWrapper != null ? this.mAudioAttributesWrapper.unwrap().hashCode() : Arrays.hashCode(new Object[]{this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream});
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("AudioAttributesCompat:");
      if (this.unwrap() != null) {
         var1.append(" audioattributes=");
         var1.append(this.unwrap());
      } else {
         if (this.mLegacyStream != null) {
            var1.append(" stream=");
            var1.append(this.mLegacyStream);
            var1.append(" derived");
         }

         var1.append(" usage=");
         var1.append(this.usageToString());
         var1.append(" content=");
         var1.append(this.mContentType);
         var1.append(" flags=0x");
         var1.append(Integer.toHexString(this.mFlags).toUpperCase());
      }

      return var1.toString();
   }

   @Nullable
   public Object unwrap() {
      return this.mAudioAttributesWrapper != null ? this.mAudioAttributesWrapper.unwrap() : null;
   }

   String usageToString() {
      return usageToString(this.mUsage);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface AttributeContentType {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface AttributeUsage {
   }

   private abstract static class AudioManagerHidden {
      public static final int STREAM_ACCESSIBILITY = 10;
      public static final int STREAM_BLUETOOTH_SCO = 6;
      public static final int STREAM_SYSTEM_ENFORCED = 7;
      public static final int STREAM_TTS = 9;
   }

   public static class Builder {
      private Object mAAObject;
      private int mContentType = 0;
      private int mFlags = 0;
      private Integer mLegacyStream;
      private int mUsage = 0;

      public Builder() {
      }

      public Builder(AudioAttributesCompat var1) {
         this.mUsage = var1.mUsage;
         this.mContentType = var1.mContentType;
         this.mFlags = var1.mFlags;
         this.mLegacyStream = var1.mLegacyStream;
         this.mAAObject = var1.unwrap();
      }

      public AudioAttributesCompat build() {
         if (!AudioAttributesCompat.sForceLegacyBehavior && VERSION.SDK_INT >= 21) {
            if (this.mAAObject != null) {
               return AudioAttributesCompat.wrap(this.mAAObject);
            } else {
               android.media.AudioAttributes.Builder var2 = (new android.media.AudioAttributes.Builder()).setContentType(this.mContentType).setFlags(this.mFlags).setUsage(this.mUsage);
               if (this.mLegacyStream != null) {
                  var2.setLegacyStreamType(this.mLegacyStream);
               }

               return AudioAttributesCompat.wrap(var2.build());
            }
         } else {
            AudioAttributesCompat var1 = new AudioAttributesCompat();
            var1.mContentType = this.mContentType;
            var1.mFlags = this.mFlags;
            var1.mUsage = this.mUsage;
            var1.mLegacyStream = this.mLegacyStream;
            var1.mAudioAttributesWrapper = null;
            return var1;
         }
      }

      public AudioAttributesCompat.Builder setContentType(int var1) {
         switch(var1) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
            this.mContentType = var1;
            break;
         default:
            this.mUsage = 0;
         }

         return this;
      }

      public AudioAttributesCompat.Builder setFlags(int var1) {
         this.mFlags |= var1 & 1023;
         return this;
      }

      public AudioAttributesCompat.Builder setLegacyStreamType(int var1) {
         if (var1 == 10) {
            throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
         } else {
            this.mLegacyStream = var1;
            this.mUsage = AudioAttributesCompat.usageForStreamType(var1);
            return this;
         }
      }

      public AudioAttributesCompat.Builder setUsage(int var1) {
         switch(var1) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
            this.mUsage = var1;
            break;
         case 16:
            if (!AudioAttributesCompat.sForceLegacyBehavior && VERSION.SDK_INT > 25) {
               this.mUsage = var1;
            } else {
               this.mUsage = 12;
            }
            break;
         default:
            this.mUsage = 0;
         }

         return this;
      }
   }
}
