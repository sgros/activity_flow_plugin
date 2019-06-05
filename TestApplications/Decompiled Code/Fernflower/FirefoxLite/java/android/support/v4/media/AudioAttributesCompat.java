package android.support.v4.media;

import android.util.SparseIntArray;
import androidx.versionedparcelable.VersionedParcelable;

public class AudioAttributesCompat implements VersionedParcelable {
   private static final int[] SDK_USAGES;
   private static final SparseIntArray SUPPRESSIBLE_USAGES = new SparseIntArray();
   AudioAttributesImpl mImpl;

   static {
      SUPPRESSIBLE_USAGES.put(5, 1);
      SUPPRESSIBLE_USAGES.put(6, 2);
      SUPPRESSIBLE_USAGES.put(7, 2);
      SUPPRESSIBLE_USAGES.put(8, 1);
      SUPPRESSIBLE_USAGES.put(9, 1);
      SUPPRESSIBLE_USAGES.put(10, 1);
      SDK_USAGES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};
   }

   AudioAttributesCompat() {
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
               if (!var0) {
                  return 3;
               } else {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Unknown usage value ");
                  var5.append(var2);
                  var5.append(" in audio attributes");
                  throw new IllegalArgumentException(var5.toString());
               }
            }
         }
      }
   }

   static String usageToString(int var0) {
      switch(var0) {
      case 0:
         return "USAGE_UNKNOWN";
      case 1:
         return "USAGE_MEDIA";
      case 2:
         return "USAGE_VOICE_COMMUNICATION";
      case 3:
         return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
      case 4:
         return "USAGE_ALARM";
      case 5:
         return "USAGE_NOTIFICATION";
      case 6:
         return "USAGE_NOTIFICATION_RINGTONE";
      case 7:
         return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
      case 8:
         return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
      case 9:
         return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
      case 10:
         return "USAGE_NOTIFICATION_EVENT";
      case 11:
         return "USAGE_ASSISTANCE_ACCESSIBILITY";
      case 12:
         return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
      case 13:
         return "USAGE_ASSISTANCE_SONIFICATION";
      case 14:
         return "USAGE_GAME";
      case 15:
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("unknown usage ");
         var1.append(var0);
         return var1.toString();
      case 16:
         return "USAGE_ASSISTANT";
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof AudioAttributesCompat;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         AudioAttributesCompat var4 = (AudioAttributesCompat)var1;
         if (this.mImpl == null) {
            if (var4.mImpl == null) {
               var3 = true;
            }

            return var3;
         } else {
            return this.mImpl.equals(var4.mImpl);
         }
      }
   }

   public int hashCode() {
      return this.mImpl.hashCode();
   }

   public String toString() {
      return this.mImpl.toString();
   }
}
