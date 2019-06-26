package android.support.v4.media;

import android.media.AudioAttributes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(21)
class AudioAttributesCompatApi21 {
   private static final String TAG = "AudioAttributesCompat";
   private static Method sAudioAttributesToLegacyStreamType;

   public static int toLegacyStreamType(AudioAttributesCompatApi21.Wrapper var0) {
      AudioAttributes var3 = var0.unwrap();

      try {
         if (sAudioAttributesToLegacyStreamType == null) {
            sAudioAttributesToLegacyStreamType = AudioAttributes.class.getMethod("toLegacyStreamType", AudioAttributes.class);
         }

         int var1 = (Integer)sAudioAttributesToLegacyStreamType.invoke((Object)null, var3);
         return var1;
      } catch (InvocationTargetException | IllegalAccessException | ClassCastException | NoSuchMethodException var2) {
         Log.w("AudioAttributesCompat", "getLegacyStreamType() failed on API21+", var2);
         return -1;
      }
   }

   static final class Wrapper {
      private AudioAttributes mWrapped;

      private Wrapper(AudioAttributes var1) {
         this.mWrapped = var1;
      }

      public static AudioAttributesCompatApi21.Wrapper wrap(@NonNull AudioAttributes var0) {
         if (var0 == null) {
            throw new IllegalArgumentException("AudioAttributesApi21.Wrapper cannot wrap null");
         } else {
            return new AudioAttributesCompatApi21.Wrapper(var0);
         }
      }

      public AudioAttributes unwrap() {
         return this.mWrapped;
      }
   }
}
