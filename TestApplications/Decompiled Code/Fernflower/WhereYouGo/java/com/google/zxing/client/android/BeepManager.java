package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Vibrator;
import android.util.Log;
import java.io.IOException;

public final class BeepManager {
   private static final float BEEP_VOLUME = 0.1F;
   private static final String TAG = BeepManager.class.getSimpleName();
   private static final long VIBRATE_DURATION = 200L;
   private boolean beepEnabled = true;
   private final Context context;
   private boolean vibrateEnabled = false;

   public BeepManager(Activity var1) {
      var1.setVolumeControlStream(3);
      this.context = var1.getApplicationContext();
   }

   public boolean isBeepEnabled() {
      return this.beepEnabled;
   }

   public boolean isVibrateEnabled() {
      return this.vibrateEnabled;
   }

   public MediaPlayer playBeepSound() {
      MediaPlayer var1 = new MediaPlayer();
      var1.setAudioStreamType(3);
      var1.setOnCompletionListener(new OnCompletionListener() {
         public void onCompletion(MediaPlayer var1) {
            var1.stop();
            var1.release();
         }
      });
      var1.setOnErrorListener(new OnErrorListener() {
         public boolean onError(MediaPlayer var1, int var2, int var3) {
            Log.w(BeepManager.TAG, "Failed to beep " + var2 + ", " + var3);
            var1.stop();
            var1.release();
            return true;
         }
      });

      label69: {
         IOException var10000;
         label59: {
            boolean var10001;
            AssetFileDescriptor var2;
            try {
               var2 = this.context.getResources().openRawResourceFd(R.raw.zxing_beep);
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break label59;
            }

            try {
               var1.setDataSource(var2.getFileDescriptor(), var2.getStartOffset(), var2.getLength());
               break label69;
            } finally {
               label53:
               try {
                  var2.close();
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label53;
               }
            }
         }

         IOException var11 = var10000;
         Log.w(TAG, var11);
         var1.release();
         var1 = null;
         return var1;
      }

      var1.setVolume(0.1F, 0.1F);
      var1.prepare();
      var1.start();
      return var1;
   }

   public void playBeepSoundAndVibrate() {
      synchronized(this){}

      try {
         if (this.beepEnabled) {
            this.playBeepSound();
         }

         if (this.vibrateEnabled) {
            ((Vibrator)this.context.getSystemService("vibrator")).vibrate(200L);
         }
      } finally {
         ;
      }

   }

   public void setBeepEnabled(boolean var1) {
      this.beepEnabled = var1;
   }

   public void setVibrateEnabled(boolean var1) {
      this.vibrateEnabled = var1;
   }
}
