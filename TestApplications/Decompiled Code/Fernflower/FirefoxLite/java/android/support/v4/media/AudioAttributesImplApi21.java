package android.support.v4.media;

import android.annotation.TargetApi;
import android.media.AudioAttributes;

@TargetApi(21)
class AudioAttributesImplApi21 implements AudioAttributesImpl {
   AudioAttributes mAudioAttributes;
   int mLegacyStreamType = -1;

   public boolean equals(Object var1) {
      if (!(var1 instanceof AudioAttributesImplApi21)) {
         return false;
      } else {
         AudioAttributesImplApi21 var2 = (AudioAttributesImplApi21)var1;
         return this.mAudioAttributes.equals(var2.mAudioAttributes);
      }
   }

   public int hashCode() {
      return this.mAudioAttributes.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("AudioAttributesCompat: audioattributes=");
      var1.append(this.mAudioAttributes);
      return var1.toString();
   }
}
