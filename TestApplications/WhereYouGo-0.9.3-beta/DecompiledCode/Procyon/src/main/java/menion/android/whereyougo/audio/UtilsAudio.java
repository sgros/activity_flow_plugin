// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.audio;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import menion.android.whereyougo.utils.Logger;
import android.content.Context;
import menion.android.whereyougo.utils.A;

public class UtilsAudio
{
    private static final String TAG = "UtilsAudio";
    
    public static void playBeep(final int i) {
        try {
            if (A.getApp() != null) {
                new AudioClip((Context)A.getApp(), 2131099648).play(i);
            }
            else if (A.getMain() != null) {
                new AudioClip((Context)A.getMain(), 2131099648).play(i);
            }
        }
        catch (Exception ex) {
            Logger.e("UtilsAudio", "playBeep(" + i + ")", ex);
        }
    }
    
    public static void playSound(final byte[] buf, final String anObject) {
        final StringBuilder append = new StringBuilder().append("playSound(");
        int length;
        if (buf != null) {
            length = buf.length;
        }
        else {
            length = 0;
        }
        Logger.i("UtilsAudio", append.append(length).append(", ").append(anObject).append(")").toString());
        if (buf == null || buf.length == 0 || anObject == null) {
            Logger.e("UtilsAudio", "playSound(): invalid parameters");
        }
        else {
            ByteArrayInputStream byteArrayInputStream = null;
            Label_0117: {
                try {
                    byteArrayInputStream = new ByteArrayInputStream(buf);
                    if (!"audio/x-wav".equals(anObject)) {
                        break Label_0117;
                    }
                    A.getManagerAudio().playMp3File("audio", ".wav", byteArrayInputStream);
                }
                catch (Exception ex) {
                    Logger.e("UtilsAudio", "playSound() failed", ex);
                }
                return;
            }
            if ("audio/mpeg".equals(anObject)) {
                A.getManagerAudio().playMp3File("audio", ".mp3", byteArrayInputStream);
            }
            else {
                Logger.e("UtilsAudio", "playSound(): unsupported mime-type");
            }
        }
    }
    
    public static void stopSound() {
        A.getManagerAudio().stopSound();
    }
}
