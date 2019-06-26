package menion.android.whereyougo.audio;

import java.io.ByteArrayInputStream;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;

public class UtilsAudio {
   private static final String TAG = "UtilsAudio";

   public static void playBeep(int var0) {
      try {
         AudioClip var1;
         if (A.getApp() != null) {
            var1 = new AudioClip(A.getApp(), 2131099648);
            var1.play(var0);
         } else if (A.getMain() != null) {
            var1 = new AudioClip(A.getMain(), 2131099648);
            var1.play(var0);
         }
      } catch (Exception var2) {
         Logger.e("UtilsAudio", "playBeep(" + var0 + ")", var2);
      }

   }

   public static void playSound(byte[] var0, String var1) {
      StringBuilder var2 = (new StringBuilder()).append("playSound(");
      int var3;
      if (var0 != null) {
         var3 = var0.length;
      } else {
         var3 = 0;
      }

      Logger.i("UtilsAudio", var2.append(var3).append(", ").append(var1).append(")").toString());
      if (var0 != null && var0.length != 0 && var1 != null) {
         try {
            ByteArrayInputStream var5 = new ByteArrayInputStream(var0);
            if ("audio/x-wav".equals(var1)) {
               A.getManagerAudio().playMp3File("audio", ".wav", var5);
            } else if ("audio/mpeg".equals(var1)) {
               A.getManagerAudio().playMp3File("audio", ".mp3", var5);
            } else {
               Logger.e("UtilsAudio", "playSound(): unsupported mime-type");
            }
         } catch (Exception var4) {
            Logger.e("UtilsAudio", "playSound() failed", var4);
         }
      } else {
         Logger.e("UtilsAudio", "playSound(): invalid parameters");
      }

   }

   public static void stopSound() {
      A.getManagerAudio().stopSound();
   }
}
