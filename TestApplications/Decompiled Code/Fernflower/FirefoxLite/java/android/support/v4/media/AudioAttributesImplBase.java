package android.support.v4.media;

import java.util.Arrays;

class AudioAttributesImplBase implements AudioAttributesImpl {
   int mContentType = 0;
   int mFlags = 0;
   int mLegacyStream = -1;
   int mUsage = 0;

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof AudioAttributesImplBase;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         AudioAttributesImplBase var4 = (AudioAttributesImplBase)var1;
         var2 = var3;
         if (this.mContentType == var4.getContentType()) {
            var2 = var3;
            if (this.mFlags == var4.getFlags()) {
               var2 = var3;
               if (this.mUsage == var4.getUsage()) {
                  var2 = var3;
                  if (this.mLegacyStream == var4.mLegacyStream) {
                     var2 = true;
                  }
               }
            }
         }

         return var2;
      }
   }

   public int getContentType() {
      return this.mContentType;
   }

   public int getFlags() {
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

   public int getLegacyStreamType() {
      return this.mLegacyStream != -1 ? this.mLegacyStream : AudioAttributesCompat.toVolumeStreamType(false, this.mFlags, this.mUsage);
   }

   public int getUsage() {
      return this.mUsage;
   }

   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream});
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("AudioAttributesCompat:");
      if (this.mLegacyStream != -1) {
         var1.append(" stream=");
         var1.append(this.mLegacyStream);
         var1.append(" derived");
      }

      var1.append(" usage=");
      var1.append(AudioAttributesCompat.usageToString(this.mUsage));
      var1.append(" content=");
      var1.append(this.mContentType);
      var1.append(" flags=0x");
      var1.append(Integer.toHexString(this.mFlags).toUpperCase());
      return var1.toString();
   }
}
