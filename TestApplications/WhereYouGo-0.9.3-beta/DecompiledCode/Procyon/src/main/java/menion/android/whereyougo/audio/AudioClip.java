// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.audio;

import android.media.MediaPlayer$OnCompletionListener;
import menion.android.whereyougo.utils.Logger;
import android.net.Uri;
import android.content.Context;
import android.media.MediaPlayer;

public class AudioClip
{
    private static final String TAG = "AudioClip";
    private AudioListener listener;
    private boolean mLoop;
    private MediaPlayer mPlayer;
    private boolean mPlaying;
    private String name;
    private int playCount;
    
    public AudioClip(final Context context, final int n) {
        this.mPlaying = false;
        this.mLoop = false;
        this.playCount = 0;
        while (true) {
            try {
                this.name = context.getResources().getResourceName(n);
                this.mPlayer = MediaPlayer.create(context, n);
                this.initMediaPlayer();
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public AudioClip(final Context context, final Uri uri) {
        this.mPlaying = false;
        this.mLoop = false;
        this.playCount = 0;
        this.mPlayer = MediaPlayer.create(context, uri);
        this.initMediaPlayer();
    }
    
    public static void destroyAudio(final AudioClip audioClip) {
        if (audioClip == null) {
            return;
        }
        try {
            audioClip.stop();
            audioClip.release();
        }
        catch (Exception ex) {
            Logger.e("AudioClip", "destroyAudio()", ex);
        }
    }
    
    private void initMediaPlayer() {
        this.mPlayer.setVolume(1000.0f, 1000.0f);
        this.mPlayer.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                AudioClip.this.mPlaying = false;
                if (AudioClip.this.mLoop) {
                    System.out.println("AudioClip loop " + AudioClip.this.name);
                    mediaPlayer.start();
                }
                else if (AudioClip.this.playCount > 0) {
                    AudioClip.this.playCount--;
                    mediaPlayer.start();
                }
                else if (AudioClip.this.listener != null) {
                    AudioClip.this.listener.playCompleted();
                }
            }
        });
    }
    
    public void loop() {
        synchronized (this) {
            this.mLoop = true;
            this.mPlaying = true;
            this.mPlayer.start();
        }
    }
    
    public void play() {
        synchronized (this) {
            if (!this.mPlaying && this.mPlayer != null) {
                this.mPlaying = true;
                this.mPlayer.start();
            }
        }
    }
    
    public void play(final int n) {
        synchronized (this) {
            if (!this.mPlaying) {
                this.playCount = n - 1;
                if (this.mPlayer != null) {
                    this.mPlaying = true;
                    this.mPlayer.start();
                }
            }
        }
    }
    
    public void registerListener(final AudioListener listener) {
        this.listener = listener;
    }
    
    public void release() {
        if (this.mPlayer != null) {
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }
    
    public void stop() {
        synchronized (this) {
            try {
                this.mLoop = false;
                if (this.mPlaying) {
                    this.mPlaying = false;
                    this.mPlayer.pause();
                }
            }
            catch (Exception ex) {
                System.err.println("AduioClip::stop " + this.name + " " + ex.toString());
            }
        }
    }
    
    public interface AudioListener
    {
        void playCompleted();
    }
}
