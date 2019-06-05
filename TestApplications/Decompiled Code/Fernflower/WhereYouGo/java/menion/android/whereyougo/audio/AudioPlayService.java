package menion.android.whereyougo.audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AudioPlayService extends Service {
   public static final String EXTRA_DELETE_FILE = "EXTRA_DELETE_FILE";
   public static final String EXTRA_FILEPATHS = "EXTRA_FILEPATHS";
   public static final String EXTRA_PLAY_AS_NOTIFICATION = "EXTRA_PLAY_AS_NOTIFICATION";
   private String actualFile;
   private boolean deleteFile;
   private ArrayList filePaths;
   private MediaPlayer mediaPlayer;
   private int originalVolumeMedia = Integer.MIN_VALUE;
   private boolean playAsNotification;

   private void initNewMediaPlayer() {
      if (this.mediaPlayer != null) {
         try {
            this.mediaPlayer.stop();
         } catch (Exception var2) {
         }

         this.mediaPlayer = null;
      }

      this.mediaPlayer = new MediaPlayer();
      this.mediaPlayer.setAudioStreamType(3);
      this.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
         public void onCompletion(MediaPlayer var1) {
            AudioPlayService.this.mediaPlayer.release();
            if (AudioPlayService.this.deleteFile) {
               try {
                  File var3 = new File(AudioPlayService.this.actualFile);
                  var3.delete();
               } catch (Exception var2) {
                  var2.printStackTrace();
               }
            }

            AudioPlayService.this.playNextMedia();
         }
      });
   }

   private void playNextMedia() {
      Exception var10000;
      label49: {
         boolean var10001;
         try {
            if (this.filePaths.size() == 0) {
               this.stopSelf();
               return;
            }
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label49;
         }

         try {
            if (this.mediaPlayer == null) {
               this.initNewMediaPlayer();
            }
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label49;
         }

         try {
            this.mediaPlayer.reset();
         } catch (Exception var5) {
            try {
               this.initNewMediaPlayer();
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label49;
            }
         }

         try {
            this.actualFile = (String)this.filePaths.remove(0);
            this.mediaPlayer.setDataSource(this.actualFile);
            this.mediaPlayer.prepareAsync();
            MediaPlayer var8 = this.mediaPlayer;
            OnPreparedListener var2 = new OnPreparedListener() {
               public void onPrepared(MediaPlayer var1) {
                  try {
                     AudioPlayService.this.mediaPlayer.start();
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }

               }
            };
            var8.setOnPreparedListener(var2);
            return;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      Exception var1 = var10000;
      var1.printStackTrace();
   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onCreate() {
      super.onCreate();
   }

   public void onDestroy() {
      super.onDestroy();
      this.mediaPlayer.release();
      this.mediaPlayer = null;
      if (this.originalVolumeMedia != Integer.MIN_VALUE && this.playAsNotification) {
         ((AudioManager)this.getSystemService("audio")).setStreamVolume(3, this.originalVolumeMedia, 16);
      }

   }

   public void onStart(Intent var1, int var2) {
      if (var1 != null) {
         String var3 = var1.getStringExtra("EXTRA_FILEPATHS");
         this.playAsNotification = var1.getBooleanExtra("EXTRA_PLAY_AS_NOTIFICATION", true);
         this.deleteFile = var1.getBooleanExtra("EXTRA_DELETE_FILE", false);
         StringTokenizer var5 = new StringTokenizer(var3, ";");
         this.filePaths = new ArrayList();

         while(var5.hasMoreTokens()) {
            String var4 = var5.nextToken().trim();
            if (var4.length() > 0 && (new File(var4)).exists()) {
               this.filePaths.add(var4);
            }
         }

         if (var3 != null && this.filePaths.size() != 0) {
            if (this.mediaPlayer == null && this.playAsNotification) {
               AudioManager var6 = (AudioManager)this.getSystemService("audio");
               this.originalVolumeMedia = var6.getStreamVolume(3);
               var6.setStreamVolume(3, this.originalVolumeMedia / 4, 16);
            }

            this.playNextMedia();
         }
      }

   }
}
