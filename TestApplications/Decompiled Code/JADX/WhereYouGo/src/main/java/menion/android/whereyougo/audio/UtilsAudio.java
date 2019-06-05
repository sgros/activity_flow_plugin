package menion.android.whereyougo.audio;

import java.io.ByteArrayInputStream;
import locus.api.android.utils.LocusConst;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;

public class UtilsAudio {
    private static final String TAG = "UtilsAudio";

    public static void playBeep(int count) {
        try {
            if (C0322A.getApp() != null) {
                new AudioClip(C0322A.getApp(), (int) C0254R.raw.sound_beep_01).play(count);
            } else if (C0322A.getMain() != null) {
                new AudioClip(C0322A.getMain(), (int) C0254R.raw.sound_beep_01).play(count);
            }
        } catch (Exception e) {
            Logger.m22e(TAG, "playBeep(" + count + ")", e);
        }
    }

    public static void playSound(byte[] data, String mime) {
        Logger.m24i(TAG, "playSound(" + (data != null ? data.length : 0) + ", " + mime + ")");
        if (data == null || data.length == 0 || mime == null) {
            Logger.m21e(TAG, "playSound(): invalid parameters");
            return;
        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if ("audio/x-wav".equals(mime)) {
                C0322A.getManagerAudio().playMp3File(LocusConst.VALUE_TRK_REC_ADD_WAYPOINT_AUDIO, ".wav", bis);
            } else if ("audio/mpeg".equals(mime)) {
                C0322A.getManagerAudio().playMp3File(LocusConst.VALUE_TRK_REC_ADD_WAYPOINT_AUDIO, ".mp3", bis);
            } else {
                Logger.m21e(TAG, "playSound(): unsupported mime-type");
            }
        } catch (Exception e) {
            Logger.m22e(TAG, "playSound() failed", e);
        }
    }

    public static void stopSound() {
        C0322A.getManagerAudio().stopSound();
    }
}
