package menion.android.whereyougo.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import menion.android.whereyougo.utils.Logger;

public class AudioClip {
    private static final String TAG = "AudioClip";
    private AudioListener listener;
    private boolean mLoop = false;
    private MediaPlayer mPlayer;
    private boolean mPlaying = false;
    private String name;
    private int playCount = 0;

    /* renamed from: menion.android.whereyougo.audio.AudioClip$1 */
    class C02561 implements OnCompletionListener {
        C02561() {
        }

        public void onCompletion(MediaPlayer mp) {
            AudioClip.this.mPlaying = false;
            if (AudioClip.this.mLoop) {
                System.out.println("AudioClip loop " + AudioClip.this.name);
                mp.start();
            } else if (AudioClip.this.playCount > 0) {
                AudioClip.this.playCount = AudioClip.this.playCount - 1;
                mp.start();
            } else if (AudioClip.this.listener != null) {
                AudioClip.this.listener.playCompleted();
            }
        }
    }

    public interface AudioListener {
        void playCompleted();
    }

    public AudioClip(Context ctx, int resID) {
        try {
            this.name = ctx.getResources().getResourceName(resID);
        } catch (Exception e) {
        }
        this.mPlayer = MediaPlayer.create(ctx, resID);
        initMediaPlayer();
    }

    public AudioClip(Context ctx, Uri soundUri) {
        this.mPlayer = MediaPlayer.create(ctx, soundUri);
        initMediaPlayer();
    }

    public static void destroyAudio(AudioClip mAudio) {
        if (mAudio != null) {
            try {
                mAudio.stop();
                mAudio.release();
            } catch (Exception e) {
                Logger.m22e(TAG, "destroyAudio()", e);
            }
        }
    }

    private void initMediaPlayer() {
        this.mPlayer.setVolume(1000.0f, 1000.0f);
        this.mPlayer.setOnCompletionListener(new C02561());
    }

    public synchronized void loop() {
        this.mLoop = true;
        this.mPlaying = true;
        this.mPlayer.start();
    }

    public synchronized void play() {
        if (!this.mPlaying) {
            if (this.mPlayer != null) {
                this.mPlaying = true;
                this.mPlayer.start();
            }
        }
    }

    public synchronized void play(int count) {
        if (!this.mPlaying) {
            this.playCount = count - 1;
            if (this.mPlayer != null) {
                this.mPlaying = true;
                this.mPlayer.start();
            }
        }
    }

    public void registerListener(AudioListener listener) {
        this.listener = listener;
    }

    public void release() {
        if (this.mPlayer != null) {
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }

    public synchronized void stop() {
        try {
            this.mLoop = false;
            if (this.mPlaying) {
                this.mPlaying = false;
                this.mPlayer.pause();
            }
        } catch (Exception e) {
            System.err.println("AduioClip::stop " + this.name + " " + e.toString());
        }
        return;
    }
}
