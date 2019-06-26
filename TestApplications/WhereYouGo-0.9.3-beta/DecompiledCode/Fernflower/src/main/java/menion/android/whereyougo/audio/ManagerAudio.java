package menion.android.whereyougo.audio;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class ManagerAudio {
   private static final int SOUND_POOL_BEEP = 1001;
   private static final String TAG = "ManagerAudio";
   private long lastVolumeCheck;
   private final SoundPool soundPool = new SoundPool(10, 3, 0);
   private final Hashtable soundPoolMap = new Hashtable();
   private float volume;

   public ManagerAudio() {
      this.soundPoolMap.put(1001, this.soundPool.load(A.getApp(), 2131099648, 1));
   }

   public void playMp3File(String var1, String var2, InputStream var3) {
      try {
         byte[] var4 = new byte[var3.available()];
         var3.read(var4);
         Utils.closeStream(var3);
         StringBuilder var6 = new StringBuilder();
         File var5 = new File(var6.append(FileSystem.CACHE_AUDIO).append(var1).append(var2).toString());
         if (var5.exists()) {
            var5.delete();
         }

         var5.getParentFile().mkdirs();
         FileOutputStream var9 = new FileOutputStream(var5);
         var9.write(var4);
         var9.flush();
         Utils.closeStream(var9);
         Intent var8 = new Intent(A.getMain(), AudioPlayService.class);
         var8.putExtra("EXTRA_FILEPATHS", var5.getAbsolutePath());
         var8.putExtra("EXTRA_DELETE_FILE", false);
         var8.putExtra("EXTRA_PLAY_AS_NOTIFICATION", false);
         A.getMain().startService(var8);
      } catch (Exception var7) {
         Logger.e("ManagerAudio", "playMp3File(" + var1 + ", " + var2 + ", " + var3 + ")", var7);
      }

   }

   public void playSound(int var1) {
      if (this.volume == 0.0F || System.currentTimeMillis() - this.lastVolumeCheck > 1000L) {
         AudioManager var2 = (AudioManager)A.getMain().getSystemService("audio");
         this.volume = (float)var2.getStreamVolume(3) / (float)var2.getStreamMaxVolume(3);
         this.lastVolumeCheck = System.currentTimeMillis();
      }

      this.soundPool.play((Integer)this.soundPoolMap.get(var1), this.volume, this.volume, 1, 0, 1.0F);
   }

   public void putAudio(int var1, String var2) {
      try {
         this.soundPoolMap.put(var1, this.soundPool.load(var2, 1));
      } catch (Exception var3) {
         Logger.e("ManagerAudio", "putAudio(" + var1 + ")", var3);
      }

   }

   public void putAudio(int var1, String var2, String var3, InputStream var4) {
      try {
         byte[] var5 = new byte[var4.available()];
         var4.read(var5);
         Utils.closeStream(var4);
         StringBuilder var6 = new StringBuilder();
         var3 = var6.append(FileSystem.CACHE_AUDIO).append(Utils.hashString(var2)).append(".").append(var3).toString();
         FileSystem.saveBytes(var3, var5);
         this.soundPoolMap.put(var1, this.soundPool.load(var3, 1));
      } catch (Exception var7) {
         Logger.e("ManagerAudio", "putAudio(" + var1 + ", " + var2 + ", " + var4 + ")", var7);
      }

   }

   public void removeAudio(int var1) {
      try {
         this.soundPool.unload(var1);
         this.soundPoolMap.remove(var1);
      } catch (Exception var3) {
         Logger.e("ManagerAudio", "removeAudio(" + var1 + ")", var3);
      }

   }

   public void stopSound() {
      if (A.getMain() != null) {
         Intent var1 = new Intent(A.getMain(), AudioPlayService.class);
         A.getMain().stopService(var1);
      }

   }
}
