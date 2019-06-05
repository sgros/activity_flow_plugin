package menion.android.whereyougo.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import java.io.PrintStream;
import menion.android.whereyougo.utils.Logger;

public class AudioClip {
   private static final String TAG = "AudioClip";
   private AudioClip.AudioListener listener;
   private boolean mLoop = false;
   private MediaPlayer mPlayer;
   private boolean mPlaying = false;
   private String name;
   private int playCount = 0;

   public AudioClip(Context var1, int var2) {
      try {
         this.name = var1.getResources().getResourceName(var2);
      } catch (Exception var4) {
      }

      this.mPlayer = MediaPlayer.create(var1, var2);
      this.initMediaPlayer();
   }

   public AudioClip(Context var1, Uri var2) {
      this.mPlayer = MediaPlayer.create(var1, var2);
      this.initMediaPlayer();
   }

   // $FF: synthetic method
   static int access$310(AudioClip var0) {
      int var1 = var0.playCount--;
      return var1;
   }

   public static void destroyAudio(AudioClip var0) {
      if (var0 != null) {
         try {
            var0.stop();
            var0.release();
         } catch (Exception var1) {
            Logger.e("AudioClip", "destroyAudio()", var1);
         }
      }

   }

   private void initMediaPlayer() {
      this.mPlayer.setVolume(1000.0F, 1000.0F);
      this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
         public void onCompletion(MediaPlayer var1) {
            AudioClip.this.mPlaying = false;
            if (AudioClip.this.mLoop) {
               System.out.println("AudioClip loop " + AudioClip.this.name);
               var1.start();
            } else if (AudioClip.this.playCount > 0) {
               AudioClip.access$310(AudioClip.this);
               var1.start();
            } else if (AudioClip.this.listener != null) {
               AudioClip.this.listener.playCompleted();
            }

         }
      });
   }

   public void loop() {
      synchronized(this){}

      try {
         this.mLoop = true;
         this.mPlaying = true;
         this.mPlayer.start();
      } finally {
         ;
      }

   }

   public void play() {
      synchronized(this){}

      Throwable var10000;
      label82: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.mPlaying;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label82;
         }

         if (var1) {
            return;
         }

         label73:
         try {
            if (this.mPlayer != null) {
               this.mPlaying = true;
               this.mPlayer.start();
            }

            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label73;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void play(int var1) {
      synchronized(this){}

      Throwable var10000;
      label82: {
         boolean var10001;
         boolean var2;
         try {
            var2 = this.mPlaying;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label82;
         }

         if (var2) {
            return;
         }

         label73:
         try {
            this.playCount = var1 - 1;
            if (this.mPlayer != null) {
               this.mPlaying = true;
               this.mPlayer.start();
            }

            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label73;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void registerListener(AudioClip.AudioListener var1) {
      this.listener = var1;
   }

   public void release() {
      if (this.mPlayer != null) {
         this.mPlayer.release();
         this.mPlayer = null;
      }

   }

   public void stop() {
      synchronized(this){}

      try {
         this.mLoop = false;
         if (this.mPlaying) {
            this.mPlaying = false;
            this.mPlayer.pause();
         }
      } catch (Exception var6) {
         PrintStream var2 = System.err;
         StringBuilder var3 = new StringBuilder();
         var2.println(var3.append("AduioClip::stop ").append(this.name).append(" ").append(var6.toString()).toString());
      } finally {
         ;
      }

   }

   public interface AudioListener {
      void playCompleted();
   }
}
