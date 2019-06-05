package android.support.v4.media.session;

import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.os.Bundle;
import java.util.List;

class MediaControllerCompatApi21 {
   public static Object createCallback(MediaControllerCompatApi21.Callback var0) {
      return new MediaControllerCompatApi21.CallbackProxy(var0);
   }

   public interface Callback {
      void onAudioInfoChanged(int var1, int var2, int var3, int var4, int var5);

      void onExtrasChanged(Bundle var1);

      void onMetadataChanged(Object var1);

      void onPlaybackStateChanged(Object var1);

      void onQueueChanged(List var1);

      void onQueueTitleChanged(CharSequence var1);

      void onSessionDestroyed();

      void onSessionEvent(String var1, Bundle var2);
   }

   static class CallbackProxy extends android.media.session.MediaController.Callback {
      protected final MediaControllerCompatApi21.Callback mCallback;

      public CallbackProxy(MediaControllerCompatApi21.Callback var1) {
         this.mCallback = var1;
      }

      public void onAudioInfoChanged(android.media.session.MediaController.PlaybackInfo var1) {
         this.mCallback.onAudioInfoChanged(var1.getPlaybackType(), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(var1), var1.getVolumeControl(), var1.getMaxVolume(), var1.getCurrentVolume());
      }

      public void onExtrasChanged(Bundle var1) {
         MediaSessionCompat.ensureClassLoader(var1);
         this.mCallback.onExtrasChanged(var1);
      }

      public void onMetadataChanged(MediaMetadata var1) {
         this.mCallback.onMetadataChanged(var1);
      }

      public void onPlaybackStateChanged(PlaybackState var1) {
         this.mCallback.onPlaybackStateChanged(var1);
      }

      public void onQueueChanged(List var1) {
         this.mCallback.onQueueChanged(var1);
      }

      public void onQueueTitleChanged(CharSequence var1) {
         this.mCallback.onQueueTitleChanged(var1);
      }

      public void onSessionDestroyed() {
         this.mCallback.onSessionDestroyed();
      }

      public void onSessionEvent(String var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         this.mCallback.onSessionEvent(var1, var2);
      }
   }

   public static class PlaybackInfo {
      public static AudioAttributes getAudioAttributes(Object var0) {
         return ((android.media.session.MediaController.PlaybackInfo)var0).getAudioAttributes();
      }

      public static int getLegacyAudioStream(Object var0) {
         return toLegacyStreamType(getAudioAttributes(var0));
      }

      private static int toLegacyStreamType(AudioAttributes var0) {
         if ((var0.getFlags() & 1) == 1) {
            return 7;
         } else if ((var0.getFlags() & 4) == 4) {
            return 6;
         } else {
            switch(var0.getUsage()) {
            case 1:
            case 11:
            case 12:
            case 14:
               return 3;
            case 2:
               return 0;
            case 3:
               return 8;
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
            case 13:
               return 1;
            default:
               return 3;
            }
         }
      }
   }
}
