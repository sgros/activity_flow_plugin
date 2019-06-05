// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.audio;

import java.util.StringTokenizer;
import android.media.AudioManager;
import android.os.IBinder;
import android.content.Intent;
import java.io.File;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer;
import java.util.ArrayList;
import android.app.Service;

public class AudioPlayService extends Service
{
    public static final String EXTRA_DELETE_FILE = "EXTRA_DELETE_FILE";
    public static final String EXTRA_FILEPATHS = "EXTRA_FILEPATHS";
    public static final String EXTRA_PLAY_AS_NOTIFICATION = "EXTRA_PLAY_AS_NOTIFICATION";
    private String actualFile;
    private boolean deleteFile;
    private ArrayList<String> filePaths;
    private MediaPlayer mediaPlayer;
    private int originalVolumeMedia;
    private boolean playAsNotification;
    
    public AudioPlayService() {
        this.originalVolumeMedia = Integer.MIN_VALUE;
    }
    
    private void initNewMediaPlayer() {
        Label_0019: {
            if (this.mediaPlayer == null) {
                break Label_0019;
            }
            while (true) {
                try {
                    this.mediaPlayer.stop();
                    this.mediaPlayer = null;
                    (this.mediaPlayer = new MediaPlayer()).setAudioStreamType(3);
                    this.mediaPlayer.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
                        public void onCompletion(final MediaPlayer mediaPlayer) {
                            AudioPlayService.this.mediaPlayer.release();
                            while (true) {
                                if (!AudioPlayService.this.deleteFile) {
                                    break Label_0040;
                                }
                                try {
                                    new File(AudioPlayService.this.actualFile).delete();
                                    AudioPlayService.this.playNextMedia();
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                    continue;
                                }
                                break;
                            }
                        }
                    });
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private void playNextMedia() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        menion/android/whereyougo/audio/AudioPlayService.filePaths:Ljava/util/ArrayList;
        //     4: invokevirtual   java/util/ArrayList.size:()I
        //     7: ifne            15
        //    10: aload_0        
        //    11: invokevirtual   menion/android/whereyougo/audio/AudioPlayService.stopSelf:()V
        //    14: return         
        //    15: aload_0        
        //    16: getfield        menion/android/whereyougo/audio/AudioPlayService.mediaPlayer:Landroid/media/MediaPlayer;
        //    19: ifnonnull       26
        //    22: aload_0        
        //    23: invokespecial   menion/android/whereyougo/audio/AudioPlayService.initNewMediaPlayer:()V
        //    26: aload_0        
        //    27: getfield        menion/android/whereyougo/audio/AudioPlayService.mediaPlayer:Landroid/media/MediaPlayer;
        //    30: invokevirtual   android/media/MediaPlayer.reset:()V
        //    33: aload_0        
        //    34: aload_0        
        //    35: getfield        menion/android/whereyougo/audio/AudioPlayService.filePaths:Ljava/util/ArrayList;
        //    38: iconst_0       
        //    39: invokevirtual   java/util/ArrayList.remove:(I)Ljava/lang/Object;
        //    42: checkcast       Ljava/lang/String;
        //    45: putfield        menion/android/whereyougo/audio/AudioPlayService.actualFile:Ljava/lang/String;
        //    48: aload_0        
        //    49: getfield        menion/android/whereyougo/audio/AudioPlayService.mediaPlayer:Landroid/media/MediaPlayer;
        //    52: aload_0        
        //    53: getfield        menion/android/whereyougo/audio/AudioPlayService.actualFile:Ljava/lang/String;
        //    56: invokevirtual   android/media/MediaPlayer.setDataSource:(Ljava/lang/String;)V
        //    59: aload_0        
        //    60: getfield        menion/android/whereyougo/audio/AudioPlayService.mediaPlayer:Landroid/media/MediaPlayer;
        //    63: invokevirtual   android/media/MediaPlayer.prepareAsync:()V
        //    66: aload_0        
        //    67: getfield        menion/android/whereyougo/audio/AudioPlayService.mediaPlayer:Landroid/media/MediaPlayer;
        //    70: astore_1       
        //    71: new             Lmenion/android/whereyougo/audio/AudioPlayService$2;
        //    74: astore_2       
        //    75: aload_2        
        //    76: aload_0        
        //    77: invokespecial   menion/android/whereyougo/audio/AudioPlayService$2.<init>:(Lmenion/android/whereyougo/audio/AudioPlayService;)V
        //    80: aload_1        
        //    81: aload_2        
        //    82: invokevirtual   android/media/MediaPlayer.setOnPreparedListener:(Landroid/media/MediaPlayer$OnPreparedListener;)V
        //    85: goto            14
        //    88: astore_1       
        //    89: aload_1        
        //    90: invokevirtual   java/lang/Exception.printStackTrace:()V
        //    93: goto            14
        //    96: astore_1       
        //    97: aload_0        
        //    98: invokespecial   menion/android/whereyougo/audio/AudioPlayService.initNewMediaPlayer:()V
        //   101: goto            33
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      14     88     96     Ljava/lang/Exception;
        //  15     26     88     96     Ljava/lang/Exception;
        //  26     33     96     104    Ljava/lang/Exception;
        //  33     85     88     96     Ljava/lang/Exception;
        //  97     101    88     96     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0026:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public IBinder onBind(final Intent intent) {
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
            ((AudioManager)this.getSystemService("audio")).setStreamVolume(3, this.originalVolumeMedia, 16);
        }
    }
    
    public void onStart(final Intent intent, final int n) {
        if (intent != null) {
            final String stringExtra = intent.getStringExtra("EXTRA_FILEPATHS");
            this.playAsNotification = intent.getBooleanExtra("EXTRA_PLAY_AS_NOTIFICATION", true);
            this.deleteFile = intent.getBooleanExtra("EXTRA_DELETE_FILE", false);
            final StringTokenizer stringTokenizer = new StringTokenizer(stringExtra, ";");
            this.filePaths = new ArrayList<String>();
            while (stringTokenizer.hasMoreTokens()) {
                final String trim = stringTokenizer.nextToken().trim();
                if (trim.length() > 0 && new File(trim).exists()) {
                    this.filePaths.add(trim);
                }
            }
            if (stringExtra != null && this.filePaths.size() != 0) {
                if (this.mediaPlayer == null && this.playAsNotification) {
                    final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
                    this.originalVolumeMedia = audioManager.getStreamVolume(3);
                    audioManager.setStreamVolume(3, this.originalVolumeMedia / 4, 16);
                }
                this.playNextMedia();
            }
        }
    }
}
