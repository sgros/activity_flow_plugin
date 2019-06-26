// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class MusicPlayerReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals("android.intent.action.MEDIA_BUTTON")) {
            if (intent.getExtras() == null) {
                return;
            }
            final KeyEvent keyEvent = (KeyEvent)intent.getExtras().get("android.intent.extra.KEY_EVENT");
            if (keyEvent == null) {
                return;
            }
            if (keyEvent.getAction() != 0) {
                return;
            }
            final int keyCode = keyEvent.getKeyCode();
            if (keyCode != 79) {
                if (keyCode == 126) {
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                    return;
                }
                if (keyCode == 127) {
                    MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
                    return;
                }
                switch (keyCode) {
                    default: {
                        return;
                    }
                    case 85: {
                        break;
                    }
                    case 86: {
                        return;
                    }
                    case 88: {
                        MediaController.getInstance().playPreviousMessage();
                        return;
                    }
                    case 87: {
                        MediaController.getInstance().playNextMessage();
                        return;
                    }
                }
            }
            if (MediaController.getInstance().isMessagePaused()) {
                MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
            }
            else {
                MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
            }
        }
        else if (intent.getAction().equals("org.telegram.android.musicplayer.play")) {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
        }
        else if (!intent.getAction().equals("org.telegram.android.musicplayer.pause") && !intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")) {
            if (intent.getAction().equals("org.telegram.android.musicplayer.next")) {
                MediaController.getInstance().playNextMessage();
            }
            else if (intent.getAction().equals("org.telegram.android.musicplayer.close")) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            else if (intent.getAction().equals("org.telegram.android.musicplayer.previous")) {
                MediaController.getInstance().playPreviousMessage();
            }
        }
        else {
            MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
        }
    }
}
