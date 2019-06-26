package menion.android.whereyougo.audio;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import locus.api.android.utils.LocusConst;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class ManagerAudio {
    private static final int SOUND_POOL_BEEP = 1001;
    private static final String TAG = "ManagerAudio";
    private long lastVolumeCheck;
    private final SoundPool soundPool = new SoundPool(10, 3, 0);
    private final Hashtable<Integer, Integer> soundPoolMap = new Hashtable();
    private float volume;

    public ManagerAudio() {
        this.soundPoolMap.put(Integer.valueOf(1001), Integer.valueOf(this.soundPool.load(C0322A.getApp(), C0254R.raw.sound_beep_01, 1)));
    }

    public void playMp3File(String fileName, String fileEnd, InputStream is) {
        try {
            byte[] data = new byte[is.available()];
            is.read(data);
            Utils.closeStream(is);
            File fileMp3 = new File(FileSystem.CACHE_AUDIO + fileName + fileEnd);
            if (fileMp3.exists()) {
                fileMp3.delete();
            }
            fileMp3.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(fileMp3);
            fos.write(data);
            fos.flush();
            Utils.closeStream(fos);
            Intent intent = new Intent(C0322A.getMain(), AudioPlayService.class);
            intent.putExtra(AudioPlayService.EXTRA_FILEPATHS, fileMp3.getAbsolutePath());
            intent.putExtra(AudioPlayService.EXTRA_DELETE_FILE, false);
            intent.putExtra(AudioPlayService.EXTRA_PLAY_AS_NOTIFICATION, false);
            C0322A.getMain().startService(intent);
        } catch (Exception e) {
            Logger.m22e(TAG, "playMp3File(" + fileName + ", " + fileEnd + ", " + is + ")", e);
        }
    }

    public void playSound(int sound) {
        if (this.volume == 0.0f || System.currentTimeMillis() - this.lastVolumeCheck > 1000) {
            AudioManager mgr = (AudioManager) C0322A.getMain().getSystemService(LocusConst.VALUE_TRK_REC_ADD_WAYPOINT_AUDIO);
            this.volume = ((float) mgr.getStreamVolume(3)) / ((float) mgr.getStreamMaxVolume(3));
            this.lastVolumeCheck = System.currentTimeMillis();
        }
        this.soundPool.play(((Integer) this.soundPoolMap.get(Integer.valueOf(sound))).intValue(), this.volume, this.volume, 1, 0, 1.0f);
    }

    public void putAudio(int audioId, String filePath) {
        try {
            this.soundPoolMap.put(Integer.valueOf(audioId), Integer.valueOf(this.soundPool.load(filePath, 1)));
        } catch (Exception e) {
            Logger.m22e(TAG, "putAudio(" + audioId + ")", e);
        }
    }

    public void putAudio(int audioId, String fileName, String fileEnd, InputStream is) {
        try {
            byte[] data = new byte[is.available()];
            is.read(data);
            Utils.closeStream(is);
            String filePath = FileSystem.CACHE_AUDIO + Utils.hashString(fileName) + "." + fileEnd;
            FileSystem.saveBytes(filePath, data);
            this.soundPoolMap.put(Integer.valueOf(audioId), Integer.valueOf(this.soundPool.load(filePath, 1)));
        } catch (Exception e) {
            Logger.m22e(TAG, "putAudio(" + audioId + ", " + fileName + ", " + is + ")", e);
        }
    }

    public void removeAudio(int audioId) {
        try {
            this.soundPool.unload(audioId);
            this.soundPoolMap.remove(Integer.valueOf(audioId));
        } catch (Exception e) {
            Logger.m22e(TAG, "removeAudio(" + audioId + ")", e);
        }
    }

    public void stopSound() {
        if (C0322A.getMain() != null) {
            C0322A.getMain().stopService(new Intent(C0322A.getMain(), AudioPlayService.class));
        }
    }
}
