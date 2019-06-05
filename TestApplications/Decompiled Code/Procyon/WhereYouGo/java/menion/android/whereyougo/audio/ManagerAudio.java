// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.audio;

import android.media.AudioManager;
import menion.android.whereyougo.utils.Logger;
import android.content.Intent;
import java.io.FileOutputStream;
import java.io.File;
import menion.android.whereyougo.utils.FileSystem;
import java.io.Closeable;
import menion.android.whereyougo.utils.Utils;
import java.io.InputStream;
import android.content.Context;
import menion.android.whereyougo.utils.A;
import java.util.Hashtable;
import android.media.SoundPool;

public class ManagerAudio
{
    private static final int SOUND_POOL_BEEP = 1001;
    private static final String TAG = "ManagerAudio";
    private long lastVolumeCheck;
    private final SoundPool soundPool;
    private final Hashtable<Integer, Integer> soundPoolMap;
    private float volume;
    
    public ManagerAudio() {
        this.soundPool = new SoundPool(10, 3, 0);
        (this.soundPoolMap = new Hashtable<Integer, Integer>()).put(1001, this.soundPool.load((Context)A.getApp(), 2131099648, 1));
    }
    
    public void playMp3File(final String s, final String s2, final InputStream obj) {
        try {
            final byte[] array = new byte[obj.available()];
            obj.read(array);
            Utils.closeStream(obj);
            final File file = new File(FileSystem.CACHE_AUDIO + s + s2);
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(array);
            fileOutputStream.flush();
            Utils.closeStream(fileOutputStream);
            final Intent intent = new Intent((Context)A.getMain(), (Class)AudioPlayService.class);
            intent.putExtra("EXTRA_FILEPATHS", file.getAbsolutePath());
            intent.putExtra("EXTRA_DELETE_FILE", false);
            intent.putExtra("EXTRA_PLAY_AS_NOTIFICATION", false);
            A.getMain().startService(intent);
        }
        catch (Exception ex) {
            Logger.e("ManagerAudio", "playMp3File(" + s + ", " + s2 + ", " + obj + ")", ex);
        }
    }
    
    public void playSound(final int i) {
        if (this.volume == 0.0f || System.currentTimeMillis() - this.lastVolumeCheck > 1000L) {
            final AudioManager audioManager = (AudioManager)A.getMain().getSystemService("audio");
            this.volume = audioManager.getStreamVolume(3) / (float)audioManager.getStreamMaxVolume(3);
            this.lastVolumeCheck = System.currentTimeMillis();
        }
        this.soundPool.play((int)this.soundPoolMap.get(i), this.volume, this.volume, 1, 0, 1.0f);
    }
    
    public void putAudio(final int n, final String s) {
        try {
            this.soundPoolMap.put(n, this.soundPool.load(s, 1));
        }
        catch (Exception ex) {
            Logger.e("ManagerAudio", "putAudio(" + n + ")", ex);
        }
    }
    
    public void putAudio(final int n, final String str, String string, final InputStream obj) {
        try {
            final byte[] b = new byte[obj.available()];
            obj.read(b);
            Utils.closeStream(obj);
            string = FileSystem.CACHE_AUDIO + Utils.hashString(str) + "." + string;
            FileSystem.saveBytes(string, b);
            this.soundPoolMap.put(n, this.soundPool.load(string, 1));
        }
        catch (Exception ex) {
            Logger.e("ManagerAudio", "putAudio(" + n + ", " + str + ", " + obj + ")", ex);
        }
    }
    
    public void removeAudio(final int n) {
        try {
            this.soundPool.unload(n);
            this.soundPoolMap.remove(n);
        }
        catch (Exception ex) {
            Logger.e("ManagerAudio", "removeAudio(" + n + ")", ex);
        }
    }
    
    public void stopSound() {
        if (A.getMain() != null) {
            A.getMain().stopService(new Intent((Context)A.getMain(), (Class)AudioPlayService.class));
        }
    }
}
