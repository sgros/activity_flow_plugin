package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Vibrator;
import android.util.Log;

public final class BeepManager {
    private static final float BEEP_VOLUME = 0.1f;
    private static final String TAG = BeepManager.class.getSimpleName();
    private static final long VIBRATE_DURATION = 200;
    private boolean beepEnabled = true;
    private final Context context;
    private boolean vibrateEnabled = false;

    /* renamed from: com.google.zxing.client.android.BeepManager$1 */
    class C01811 implements OnCompletionListener {
        C01811() {
        }

        public void onCompletion(MediaPlayer mp) {
            mp.stop();
            mp.release();
        }
    }

    /* renamed from: com.google.zxing.client.android.BeepManager$2 */
    class C01822 implements OnErrorListener {
        C01822() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.w(BeepManager.TAG, "Failed to beep " + what + ", " + extra);
            mp.stop();
            mp.release();
            return true;
        }
    }

    public BeepManager(Activity activity) {
        activity.setVolumeControlStream(3);
        this.context = activity.getApplicationContext();
    }

    public boolean isBeepEnabled() {
        return this.beepEnabled;
    }

    public void setBeepEnabled(boolean beepEnabled) {
        this.beepEnabled = beepEnabled;
    }

    public boolean isVibrateEnabled() {
        return this.vibrateEnabled;
    }

    public void setVibrateEnabled(boolean vibrateEnabled) {
        this.vibrateEnabled = vibrateEnabled;
    }

    public synchronized void playBeepSoundAndVibrate() {
        if (this.beepEnabled) {
            playBeepSound();
        }
        if (this.vibrateEnabled) {
            ((Vibrator) this.context.getSystemService("vibrator")).vibrate(VIBRATE_DURATION);
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public android.media.MediaPlayer playBeepSound() {
        /*
        r8 = this;
        r0 = new android.media.MediaPlayer;
        r0.<init>();
        r1 = 3;
        r0.setAudioStreamType(r1);
        r1 = new com.google.zxing.client.android.BeepManager$1;
        r1.<init>();
        r0.setOnCompletionListener(r1);
        r1 = new com.google.zxing.client.android.BeepManager$2;
        r1.<init>();
        r0.setOnErrorListener(r1);
        r1 = r8.context;	 Catch:{ IOException -> 0x004c }
        r1 = r1.getResources();	 Catch:{ IOException -> 0x004c }
        r2 = com.google.zxing.client.android.C0186R.raw.zxing_beep;	 Catch:{ IOException -> 0x004c }
        r6 = r1.openRawResourceFd(r2);	 Catch:{ IOException -> 0x004c }
        r1 = r6.getFileDescriptor();	 Catch:{ all -> 0x0047 }
        r2 = r6.getStartOffset();	 Catch:{ all -> 0x0047 }
        r4 = r6.getLength();	 Catch:{ all -> 0x0047 }
        r0.setDataSource(r1, r2, r4);	 Catch:{ all -> 0x0047 }
        r6.close();	 Catch:{ IOException -> 0x004c }
        r1 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
        r2 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
        r0.setVolume(r1, r2);	 Catch:{ IOException -> 0x004c }
        r0.prepare();	 Catch:{ IOException -> 0x004c }
        r0.start();	 Catch:{ IOException -> 0x004c }
    L_0x0046:
        return r0;
    L_0x0047:
        r1 = move-exception;
        r6.close();	 Catch:{ IOException -> 0x004c }
        throw r1;	 Catch:{ IOException -> 0x004c }
    L_0x004c:
        r7 = move-exception;
        r1 = TAG;
        android.util.Log.w(r1, r7);
        r0.release();
        r0 = 0;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.client.android.BeepManager.playBeepSound():android.media.MediaPlayer");
    }
}
