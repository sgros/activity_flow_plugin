package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class MusicPlayerReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (var2.getAction().equals("android.intent.action.MEDIA_BUTTON")) {
         if (var2.getExtras() == null) {
            return;
         }

         KeyEvent var4 = (KeyEvent)var2.getExtras().get("android.intent.extra.KEY_EVENT");
         if (var4 == null) {
            return;
         }

         if (var4.getAction() != 0) {
            return;
         }

         int var3 = var4.getKeyCode();
         if (var3 != 79) {
            if (var3 == 126) {
               MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
               return;
            }

            if (var3 == 127) {
               MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
               return;
            }

            switch(var3) {
            case 85:
               break;
            case 86:
            default:
               return;
            case 87:
               MediaController.getInstance().playNextMessage();
               return;
            case 88:
               MediaController.getInstance().playPreviousMessage();
               return;
            }
         }

         if (MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
         } else {
            MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
         }
      } else if (var2.getAction().equals("org.telegram.android.musicplayer.play")) {
         MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
      } else if (!var2.getAction().equals("org.telegram.android.musicplayer.pause") && !var2.getAction().equals("android.media.AUDIO_BECOMING_NOISY")) {
         if (var2.getAction().equals("org.telegram.android.musicplayer.next")) {
            MediaController.getInstance().playNextMessage();
         } else if (var2.getAction().equals("org.telegram.android.musicplayer.close")) {
            MediaController.getInstance().cleanupPlayer(true, true);
         } else if (var2.getAction().equals("org.telegram.android.musicplayer.previous")) {
            MediaController.getInstance().playPreviousMessage();
         }
      } else {
         MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
      }

   }
}
