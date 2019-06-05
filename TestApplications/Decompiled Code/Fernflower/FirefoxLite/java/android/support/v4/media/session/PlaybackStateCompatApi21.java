package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;
import java.util.List;

class PlaybackStateCompatApi21 {
   public static long getActions(Object var0) {
      return ((PlaybackState)var0).getActions();
   }

   public static long getActiveQueueItemId(Object var0) {
      return ((PlaybackState)var0).getActiveQueueItemId();
   }

   public static long getBufferedPosition(Object var0) {
      return ((PlaybackState)var0).getBufferedPosition();
   }

   public static List getCustomActions(Object var0) {
      return ((PlaybackState)var0).getCustomActions();
   }

   public static CharSequence getErrorMessage(Object var0) {
      return ((PlaybackState)var0).getErrorMessage();
   }

   public static long getLastPositionUpdateTime(Object var0) {
      return ((PlaybackState)var0).getLastPositionUpdateTime();
   }

   public static float getPlaybackSpeed(Object var0) {
      return ((PlaybackState)var0).getPlaybackSpeed();
   }

   public static long getPosition(Object var0) {
      return ((PlaybackState)var0).getPosition();
   }

   public static int getState(Object var0) {
      return ((PlaybackState)var0).getState();
   }

   static final class CustomAction {
      public static String getAction(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getAction();
      }

      public static Bundle getExtras(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getExtras();
      }

      public static int getIcon(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getIcon();
      }

      public static CharSequence getName(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getName();
      }
   }
}
