// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.camera;

import android.hardware.Camera$PictureCallback;
import android.hardware.Camera$ShutterCallback;
import org.telegram.messenger.NotificationCenter;
import android.content.SharedPreferences;
import android.os.Build;
import android.hardware.Camera$Size;
import org.telegram.messenger.ApplicationLoader;
import android.hardware.Camera$CameraInfo;
import org.telegram.tgnet.SerializedData;
import android.util.Base64;
import org.telegram.messenger.MessagesController;
import android.graphics.drawable.BitmapDrawable;
import org.telegram.messenger.ImageLoader;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.FileOutputStream;
import android.graphics.Bitmap;
import org.telegram.messenger.Bitmaps;
import android.graphics.Matrix;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import org.telegram.messenger.Utilities;
import java.util.Locale;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import android.graphics.SurfaceTexture;
import android.hardware.Camera$Parameters;
import android.hardware.Camera;
import org.telegram.messenger.FileLog;
import android.hardware.Camera$PreviewCallback;
import java.util.concurrent.CountDownLatch;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;
import android.media.MediaRecorder;
import java.util.ArrayList;
import android.media.MediaRecorder$OnInfoListener;

public class CameraController implements MediaRecorder$OnInfoListener
{
    private static final int CORE_POOL_SIZE = 1;
    private static volatile CameraController Instance;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final int MAX_POOL_SIZE = 1;
    protected ArrayList<String> availableFlashModes;
    protected volatile ArrayList<CameraInfo> cameraInfos;
    private boolean cameraInitied;
    private boolean loadingCameras;
    private ArrayList<Runnable> onFinishCameraInitRunnables;
    private VideoTakeCallback onVideoTakeCallback;
    private String recordedFile;
    private MediaRecorder recorder;
    private ThreadPoolExecutor threadPool;
    
    public CameraController() {
        this.availableFlashModes = new ArrayList<String>();
        this.onFinishCameraInitRunnables = new ArrayList<Runnable>();
        this.threadPool = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    public static Size chooseOptimalSize(final List<Size> coll, final int n, final int n2, Size size) {
        final ArrayList<Size> coll2 = new ArrayList<Size>();
        final int width = size.getWidth();
        final int height = size.getHeight();
        for (int i = 0; i < coll.size(); ++i) {
            size = coll.get(i);
            if (size.getHeight() == size.getWidth() * height / width && size.getWidth() >= n && size.getHeight() >= n2) {
                coll2.add(size);
            }
        }
        if (coll2.size() > 0) {
            return (Size)Collections.min((Collection<?>)coll2, (Comparator<? super Object>)new CompareSizesByArea());
        }
        return Collections.max((Collection<? extends Size>)coll, (Comparator<? super Size>)new CompareSizesByArea());
    }
    
    private void finishRecordingVideo() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: lstore_1       
        //     2: new             Landroid/media/MediaMetadataRetriever;
        //     5: astore_3       
        //     6: aload_3        
        //     7: invokespecial   android/media/MediaMetadataRetriever.<init>:()V
        //    10: aload_3        
        //    11: astore          4
        //    13: aload_3        
        //    14: aload_0        
        //    15: getfield        org/telegram/messenger/camera/CameraController.recordedFile:Ljava/lang/String;
        //    18: invokevirtual   android/media/MediaMetadataRetriever.setDataSource:(Ljava/lang/String;)V
        //    21: aload_3        
        //    22: astore          4
        //    24: aload_3        
        //    25: bipush          9
        //    27: invokevirtual   android/media/MediaMetadataRetriever.extractMetadata:(I)Ljava/lang/String;
        //    30: astore          5
        //    32: lload_1        
        //    33: lstore          6
        //    35: aload           5
        //    37: ifnull          64
        //    40: aload_3        
        //    41: astore          4
        //    43: aload           5
        //    45: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //    48: l2f            
        //    49: ldc             1000.0
        //    51: fdiv           
        //    52: f2d            
        //    53: invokestatic    java/lang/Math.ceil:(D)D
        //    56: dstore          8
        //    58: dload           8
        //    60: d2i            
        //    61: i2l            
        //    62: lstore          6
        //    64: aload_3        
        //    65: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    68: goto            127
        //    71: astore          4
        //    73: goto            122
        //    76: astore          5
        //    78: goto            92
        //    81: astore          4
        //    83: aconst_null    
        //    84: astore_3       
        //    85: goto            248
        //    88: astore          5
        //    90: aconst_null    
        //    91: astore_3       
        //    92: aload_3        
        //    93: astore          4
        //    95: aload           5
        //    97: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   100: lload_1        
        //   101: lstore          6
        //   103: aload_3        
        //   104: ifnull          127
        //   107: aload_3        
        //   108: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   111: lload_1        
        //   112: lstore          6
        //   114: goto            127
        //   117: astore          4
        //   119: lload_1        
        //   120: lstore          6
        //   122: aload           4
        //   124: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   127: aload_0        
        //   128: getfield        org/telegram/messenger/camera/CameraController.recordedFile:Ljava/lang/String;
        //   131: iconst_1       
        //   132: invokestatic    android/media/ThumbnailUtils.createVideoThumbnail:(Ljava/lang/String;I)Landroid/graphics/Bitmap;
        //   135: astore          4
        //   137: new             Ljava/lang/StringBuilder;
        //   140: dup            
        //   141: invokespecial   java/lang/StringBuilder.<init>:()V
        //   144: astore_3       
        //   145: aload_3        
        //   146: ldc             "-2147483648_"
        //   148: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   151: pop            
        //   152: aload_3        
        //   153: invokestatic    org/telegram/messenger/SharedConfig.getLastLocalId:()I
        //   156: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   159: pop            
        //   160: aload_3        
        //   161: ldc             ".jpg"
        //   163: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   166: pop            
        //   167: aload_3        
        //   168: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   171: astore_3       
        //   172: new             Ljava/io/File;
        //   175: dup            
        //   176: iconst_4       
        //   177: invokestatic    org/telegram/messenger/FileLoader.getDirectory:(I)Ljava/io/File;
        //   180: aload_3        
        //   181: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   184: astore_3       
        //   185: new             Ljava/io/FileOutputStream;
        //   188: astore          5
        //   190: aload           5
        //   192: aload_3        
        //   193: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   196: aload           4
        //   198: getstatic       android/graphics/Bitmap$CompressFormat.JPEG:Landroid/graphics/Bitmap$CompressFormat;
        //   201: bipush          80
        //   203: aload           5
        //   205: invokevirtual   android/graphics/Bitmap.compress:(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //   208: pop            
        //   209: goto            219
        //   212: astore          5
        //   214: aload           5
        //   216: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   219: invokestatic    org/telegram/messenger/SharedConfig.saveConfig:()V
        //   222: new             Lorg/telegram/messenger/camera/_$$Lambda$CameraController$5HicrJ12U2c73GyHfU8XHEFg__A;
        //   225: dup            
        //   226: aload_0        
        //   227: aload_3        
        //   228: aload           4
        //   230: lload           6
        //   232: invokespecial   org/telegram/messenger/camera/_$$Lambda$CameraController$5HicrJ12U2c73GyHfU8XHEFg__A.<init>:(Lorg/telegram/messenger/camera/CameraController;Ljava/io/File;Landroid/graphics/Bitmap;J)V
        //   235: invokestatic    org/telegram/messenger/AndroidUtilities.runOnUIThread:(Ljava/lang/Runnable;)V
        //   238: return         
        //   239: astore          5
        //   241: aload           4
        //   243: astore_3       
        //   244: aload           5
        //   246: astore          4
        //   248: aload_3        
        //   249: ifnull          264
        //   252: aload_3        
        //   253: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   256: goto            264
        //   259: astore_3       
        //   260: aload_3        
        //   261: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   264: aload           4
        //   266: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      10     88     92     Ljava/lang/Exception;
        //  2      10     81     88     Any
        //  13     21     76     81     Ljava/lang/Exception;
        //  13     21     239    248    Any
        //  24     32     76     81     Ljava/lang/Exception;
        //  24     32     239    248    Any
        //  43     58     76     81     Ljava/lang/Exception;
        //  43     58     239    248    Any
        //  64     68     71     76     Ljava/lang/Exception;
        //  95     100    239    248    Any
        //  107    111    117    122    Ljava/lang/Exception;
        //  185    209    212    219    Ljava/lang/Throwable;
        //  252    256    259    264    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0064:
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
    
    public static CameraController getInstance() {
        final CameraController instance;
        if ((instance = CameraController.Instance) == null) {
            synchronized (CameraController.class) {
                if (CameraController.Instance == null) {
                    CameraController.Instance = new CameraController();
                }
            }
        }
        return instance;
    }
    
    private static int getOrientation(final byte[] array) {
        if (array == null) {
            return 0;
        }
        int n = 0;
        int n2 = 0;
        int n5 = 0;
        Label_0201: {
            Label_0199: {
                while (true) {
                    n2 = n;
                    if (n + 3 >= array.length) {
                        break Label_0199;
                    }
                    int n3 = n2 = n + 1;
                    if ((array[n] & 0xFF) != 0xFF) {
                        break Label_0199;
                    }
                    final int n4 = array[n3] & 0xFF;
                    if (n4 == 255) {
                        n = n3;
                    }
                    else {
                        n = ++n3;
                        if (n4 == 216) {
                            continue;
                        }
                        if (n4 == 1) {
                            n = n3;
                        }
                        else {
                            n2 = n3;
                            if (n4 == 217) {
                                break Label_0199;
                            }
                            if (n4 == 218) {
                                n2 = n3;
                                break Label_0199;
                            }
                            final int pack = pack(array, n3, 2, false);
                            if (pack < 2) {
                                break;
                            }
                            n = n3 + pack;
                            if (n > array.length) {
                                break;
                            }
                            if (n4 == 225 && pack >= 8 && pack(array, n3 + 2, 4, false) == 1165519206 && pack(array, n3 + 6, 2, false) == 0) {
                                n2 = n3 + 8;
                                n5 = pack - 8;
                                break Label_0201;
                            }
                            continue;
                        }
                    }
                }
                return 0;
            }
            n5 = 0;
        }
        if (n5 > 8) {
            final int pack2 = pack(array, n2, 4, false);
            if (pack2 != 1229531648 && pack2 != 1296891946) {
                return 0;
            }
            final boolean b = pack2 == 1229531648;
            final int n6 = pack(array, n2 + 4, 4, b) + 2;
            if (n6 >= 10) {
                if (n6 <= n5) {
                    int n7 = n2 + n6;
                    int n8 = n5 - n6;
                    int pack3 = pack(array, n7 - 2, 2, b);
                    while (pack3 > 0 && n8 >= 12) {
                        if (pack(array, n7, 2, b) == 274) {
                            final int pack4 = pack(array, n7 + 8, 2, b);
                            if (pack4 == 1) {
                                return 0;
                            }
                            if (pack4 == 3) {
                                return 180;
                            }
                            if (pack4 == 6) {
                                return 90;
                            }
                            if (pack4 != 8) {
                                return 0;
                            }
                            return 270;
                        }
                        else {
                            n7 += 12;
                            n8 -= 12;
                            --pack3;
                        }
                    }
                }
            }
        }
        return 0;
    }
    
    private static int pack(final byte[] array, int n, int i, final boolean b) {
        int n2;
        if (b) {
            n += i - 1;
            n2 = -1;
        }
        else {
            n2 = 1;
        }
        int n3 = 0;
        while (i > 0) {
            n3 = ((array[n] & 0xFF) | n3 << 8);
            n += n2;
            --i;
        }
        return n3;
    }
    
    public void cancelOnInitRunnable(final Runnable o) {
        this.onFinishCameraInitRunnables.remove(o);
    }
    
    public void close(final CameraSession cameraSession, final CountDownLatch countDownLatch, final Runnable runnable) {
        cameraSession.destroy();
        this.threadPool.execute(new _$$Lambda$CameraController$Mku2q5OGwNan_h4puDdyST57U90(runnable, cameraSession, countDownLatch));
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    public ArrayList<CameraInfo> getCameras() {
        return this.cameraInfos;
    }
    
    public void initCamera(final Runnable runnable) {
        if (runnable != null && !this.onFinishCameraInitRunnables.contains(runnable)) {
            this.onFinishCameraInitRunnables.add(runnable);
        }
        if (!this.loadingCameras) {
            if (!this.cameraInitied) {
                this.loadingCameras = true;
                this.threadPool.execute(new _$$Lambda$CameraController$MTo6g4R2dOVhEmQ7AM4dYTP1p5w(this));
            }
        }
    }
    
    public boolean isCameraInitied() {
        return this.cameraInitied && this.cameraInfos != null && !this.cameraInfos.isEmpty();
    }
    
    public void onInfo(MediaRecorder recorder, final int n, final int n2) {
        if (n == 800 || n == 801 || n == 1) {
            recorder = this.recorder;
            this.recorder = null;
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            if (this.onVideoTakeCallback != null) {
                this.finishRecordingVideo();
            }
        }
    }
    
    public void open(final CameraSession cameraSession, final SurfaceTexture surfaceTexture, final Runnable runnable, final Runnable runnable2) {
        if (cameraSession != null) {
            if (surfaceTexture != null) {
                this.threadPool.execute(new _$$Lambda$CameraController$nOnW1F7MY4Qrw4m6mXmkWXIiXBI(this, cameraSession, runnable2, surfaceTexture, runnable));
            }
        }
    }
    
    public void openRound(final CameraSession obj, final SurfaceTexture obj2, final Runnable runnable, final Runnable runnable2) {
        if (obj != null && obj2 != null) {
            this.threadPool.execute(new _$$Lambda$CameraController$ANuBkffO3J8bla21iFygwDfs5Ss(obj, runnable2, obj2, runnable));
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to open round ");
            sb.append(obj);
            sb.append(" tex = ");
            sb.append(obj2);
            FileLog.d(sb.toString());
        }
    }
    
    public void recordVideo(final CameraSession cameraSession, final File file, final VideoTakeCallback videoTakeCallback, final Runnable runnable) {
        if (cameraSession == null) {
            return;
        }
        final CameraInfo cameraInfo = cameraSession.cameraInfo;
        this.threadPool.execute(new _$$Lambda$CameraController$QhtYQbsLLWOPmfR7G2eDSElrRiU(this, cameraInfo.camera, cameraSession, file, cameraInfo, videoTakeCallback, runnable));
    }
    
    public void startPreview(final CameraSession cameraSession) {
        if (cameraSession == null) {
            return;
        }
        this.threadPool.execute(new _$$Lambda$CameraController$95vC3mtJ5YICFX1HGPyBSecWQH0(cameraSession));
    }
    
    public void stopPreview(final CameraSession cameraSession) {
        if (cameraSession == null) {
            return;
        }
        this.threadPool.execute(new _$$Lambda$CameraController$TvlSt_eAGck2RVWXRxqaBNBbvno(cameraSession));
    }
    
    public void stopVideoRecording(final CameraSession cameraSession, final boolean b) {
        this.threadPool.execute(new _$$Lambda$CameraController$L4S5_dxkHAFS7LzqMIp7eixEAXY(this, cameraSession, b));
    }
    
    public boolean takePicture(final File file, final CameraSession cameraSession, final Runnable runnable) {
        if (cameraSession == null) {
            return false;
        }
        final CameraInfo cameraInfo = cameraSession.cameraInfo;
        final boolean flipFront = cameraSession.isFlipFront();
        final Camera camera = cameraInfo.camera;
        try {
            camera.takePicture((Camera$ShutterCallback)null, (Camera$PictureCallback)null, (Camera$PictureCallback)new _$$Lambda$CameraController$Qbnmyb8uDsRl802IZhTfJAoPBlE(file, cameraInfo, flipFront, runnable));
            return true;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return false;
        }
    }
    
    static class CompareSizesByArea implements Comparator<Size>
    {
        @Override
        public int compare(final Size size, final Size size2) {
            return Long.signum(size.getWidth() * (long)size.getHeight() - size2.getWidth() * (long)size2.getHeight());
        }
    }
    
    public interface VideoTakeCallback
    {
        void onFinishVideoRecording(final String p0, final long p1);
    }
}
