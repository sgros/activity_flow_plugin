package menion.android.whereyougo.audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import locus.api.android.utils.LocusConst;

public class AudioPlayService extends Service {
    public static final String EXTRA_DELETE_FILE = "EXTRA_DELETE_FILE";
    public static final String EXTRA_FILEPATHS = "EXTRA_FILEPATHS";
    public static final String EXTRA_PLAY_AS_NOTIFICATION = "EXTRA_PLAY_AS_NOTIFICATION";
    private String actualFile;
    private boolean deleteFile;
    private ArrayList<String> filePaths;
    private MediaPlayer mediaPlayer;
    private int originalVolumeMedia = Integer.MIN_VALUE;
    private boolean playAsNotification;

    /* renamed from: menion.android.whereyougo.audio.AudioPlayService$1 */
    class C02571 implements OnCompletionListener {
        C02571() {
        }

        public void onCompletion(MediaPlayer mp) {
            AudioPlayService.this.mediaPlayer.release();
            if (AudioPlayService.this.deleteFile) {
                try {
                    new File(AudioPlayService.this.actualFile).delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            AudioPlayService.this.playNextMedia();
        }
    }

    /* renamed from: menion.android.whereyougo.audio.AudioPlayService$2 */
    class C02582 implements OnPreparedListener {
        C02582() {
        }

        public void onPrepared(MediaPlayer mp) {
            try {
                AudioPlayService.this.mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initNewMediaPlayer() {
        if (this.mediaPlayer != null) {
            try {
                this.mediaPlayer.stop();
            } catch (Exception e) {
            }
            this.mediaPlayer = null;
        }
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(3);
        this.mediaPlayer.setOnCompletionListener(new C02571());
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mediaPlayer.release();
        this.mediaPlayer = null;
        if (this.originalVolumeMedia != Integer.MIN_VALUE && this.playAsNotification) {
            ((AudioManager) getSystemService(LocusConst.VALUE_TRK_REC_ADD_WAYPOINT_AUDIO)).setStreamVolume(3, this.originalVolumeMedia, 16);
        }
    }

    public void onStart(Intent intent, int value) {
        if (intent != null) {
            String filePath = intent.getStringExtra(EXTRA_FILEPATHS);
            this.playAsNotification = intent.getBooleanExtra(EXTRA_PLAY_AS_NOTIFICATION, true);
            this.deleteFile = intent.getBooleanExtra(EXTRA_DELETE_FILE, false);
            StringTokenizer token = new StringTokenizer(filePath, ";");
            this.filePaths = new ArrayList();
            while (token.hasMoreTokens()) {
                String file = token.nextToken().trim();
                if (file.length() > 0 && new File(file).exists()) {
                    this.filePaths.add(file);
                }
            }
            if (filePath != null && this.filePaths.size() != 0) {
                if (this.mediaPlayer == null && this.playAsNotification) {
                    AudioManager am = (AudioManager) getSystemService(LocusConst.VALUE_TRK_REC_ADD_WAYPOINT_AUDIO);
                    this.originalVolumeMedia = am.getStreamVolume(3);
                    am.setStreamVolume(3, this.originalVolumeMedia / 4, 16);
                }
                playNextMedia();
            }
        }
    }

    private void playNextMedia() {
        try {
            if (this.filePaths.size() == 0) {
                stopSelf();
                return;
            }
            if (this.mediaPlayer == null) {
                initNewMediaPlayer();
            }
            try {
                this.mediaPlayer.reset();
            } catch (Exception e) {
                initNewMediaPlayer();
            }
            this.actualFile = (String) this.filePaths.remove(0);
            this.mediaPlayer.setDataSource(this.actualFile);
            this.mediaPlayer.prepareAsync();
            this.mediaPlayer.setOnPreparedListener(new C02582());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
