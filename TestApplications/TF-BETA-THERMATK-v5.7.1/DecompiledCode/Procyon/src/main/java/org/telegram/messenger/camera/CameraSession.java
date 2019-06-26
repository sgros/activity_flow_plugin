// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.camera;

import android.hardware.Camera$PreviewCallback;
import android.hardware.Camera$Parameters;
import java.util.List;
import android.hardware.Camera$Area;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import android.graphics.Rect;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.content.SharedPreferences$Editor;
import android.os.Build;
import android.hardware.Camera$CameraInfo;
import android.content.SharedPreferences;
import android.view.WindowManager;
import android.content.Context;
import org.telegram.messenger.ApplicationLoader;
import android.hardware.Camera;
import android.view.OrientationEventListener;
import android.hardware.Camera$AutoFocusCallback;

public class CameraSession
{
    public static final int ORIENTATION_HYSTERESIS = 5;
    private Camera$AutoFocusCallback autoFocusCallback;
    protected CameraInfo cameraInfo;
    private String currentFlashMode;
    private int currentOrientation;
    private int diffOrientation;
    private boolean flipFront;
    private boolean initied;
    private boolean isVideo;
    private int jpegOrientation;
    private int lastDisplayOrientation;
    private int lastOrientation;
    private boolean meteringAreaSupported;
    private OrientationEventListener orientationEventListener;
    private final int pictureFormat;
    private final Size pictureSize;
    private final Size previewSize;
    private boolean sameTakePictureOrientation;
    
    public CameraSession(final CameraInfo cameraInfo, final Size previewSize, final Size pictureSize, final int pictureFormat) {
        this.lastOrientation = -1;
        this.lastDisplayOrientation = -1;
        this.flipFront = true;
        this.autoFocusCallback = (Camera$AutoFocusCallback)new Camera$AutoFocusCallback() {
            public void onAutoFocus(final boolean b, final Camera camera) {
            }
        };
        this.previewSize = previewSize;
        this.pictureSize = pictureSize;
        this.pictureFormat = pictureFormat;
        this.cameraInfo = cameraInfo;
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0);
        String s;
        if (this.cameraInfo.frontCamera != 0) {
            s = "flashMode_front";
        }
        else {
            s = "flashMode";
        }
        this.currentFlashMode = sharedPreferences.getString(s, "off");
        this.orientationEventListener = new OrientationEventListener(ApplicationLoader.applicationContext) {
            public void onOrientationChanged(int rotation) {
                if (CameraSession.this.orientationEventListener != null && CameraSession.this.initied) {
                    if (rotation != -1) {
                        final CameraSession this$0 = CameraSession.this;
                        this$0.jpegOrientation = this$0.roundOrientation(rotation, this$0.jpegOrientation);
                        rotation = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
                        if (CameraSession.this.lastOrientation != CameraSession.this.jpegOrientation || rotation != CameraSession.this.lastDisplayOrientation) {
                            if (!CameraSession.this.isVideo) {
                                CameraSession.this.configurePhotoCamera();
                            }
                            CameraSession.this.lastDisplayOrientation = rotation;
                            final CameraSession this$2 = CameraSession.this;
                            this$2.lastOrientation = this$2.jpegOrientation;
                        }
                    }
                }
            }
        };
        if (this.orientationEventListener.canDetectOrientation()) {
            this.orientationEventListener.enable();
        }
        else {
            this.orientationEventListener.disable();
            this.orientationEventListener = null;
        }
    }
    
    private int getDisplayOrientation(final Camera$CameraInfo camera$CameraInfo, final boolean b) {
        final int rotation = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        final int n = 0;
        final int n2 = 90;
        int n3 = n;
        if (rotation != 0) {
            if (rotation != 1) {
                if (rotation != 2) {
                    if (rotation != 3) {
                        n3 = n;
                    }
                    else {
                        n3 = 270;
                    }
                }
                else {
                    n3 = 180;
                }
            }
            else {
                n3 = 90;
            }
        }
        int n5;
        if (camera$CameraInfo.facing == 1) {
            final int n4 = n5 = (360 - (camera$CameraInfo.orientation + n3) % 360) % 360;
            if (!b && (n5 = n4) == 90) {
                n5 = 270;
            }
            if (!b && "Huawei".equals(Build.MANUFACTURER) && "angler".equals(Build.PRODUCT) && n5 == 270) {
                n5 = n2;
            }
        }
        else {
            n5 = (camera$CameraInfo.orientation - n3 + 360) % 360;
        }
        return n5;
    }
    
    private int getHigh() {
        if ("LGE".equals(Build.MANUFACTURER) && "g3_tmo_us".equals(Build.PRODUCT)) {
            return 4;
        }
        return 1;
    }
    
    private int roundOrientation(final int n, final int n2) {
        boolean b = true;
        if (n2 != -1) {
            final int abs = Math.abs(n - n2);
            if (Math.min(abs, 360 - abs) < 50) {
                b = false;
            }
        }
        if (b) {
            return (n + 45) / 90 * 90 % 360;
        }
        return n2;
    }
    
    public void checkFlashMode(final String currentFlashMode) {
        if (CameraController.getInstance().availableFlashModes.contains(this.currentFlashMode)) {
            return;
        }
        this.currentFlashMode = currentFlashMode;
        this.configurePhotoCamera();
        final SharedPreferences$Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).edit();
        String s;
        if (this.cameraInfo.frontCamera != 0) {
            s = "flashMode_front";
        }
        else {
            s = "flashMode";
        }
        edit.putString(s, currentFlashMode).commit();
    }
    
    protected void configurePhotoCamera() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/telegram/messenger/camera/CameraSession.cameraInfo:Lorg/telegram/messenger/camera/CameraInfo;
        //     4: getfield        org/telegram/messenger/camera/CameraInfo.camera:Landroid/hardware/Camera;
        //     7: astore_1       
        //     8: aload_1        
        //     9: ifnull          448
        //    12: new             Landroid/hardware/Camera$CameraInfo;
        //    15: astore_2       
        //    16: aload_2        
        //    17: invokespecial   android/hardware/Camera$CameraInfo.<init>:()V
        //    20: aconst_null    
        //    21: astore_3       
        //    22: aload_1        
        //    23: invokevirtual   android/hardware/Camera.getParameters:()Landroid/hardware/Camera$Parameters;
        //    26: astore          4
        //    28: aload           4
        //    30: astore_3       
        //    31: goto            41
        //    34: astore          4
        //    36: aload           4
        //    38: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //    41: aload_0        
        //    42: getfield        org/telegram/messenger/camera/CameraSession.cameraInfo:Lorg/telegram/messenger/camera/CameraInfo;
        //    45: invokevirtual   org/telegram/messenger/camera/CameraInfo.getCameraId:()I
        //    48: aload_2        
        //    49: invokestatic    android/hardware/Camera.getCameraInfo:(ILandroid/hardware/Camera$CameraInfo;)V
        //    52: aload_0        
        //    53: aload_2        
        //    54: iconst_1       
        //    55: invokespecial   org/telegram/messenger/camera/CameraSession.getDisplayOrientation:(Landroid/hardware/Camera$CameraInfo;Z)I
        //    58: istore          5
        //    60: ldc_w           "samsung"
        //    63: getstatic       android/os/Build.MANUFACTURER:Ljava/lang/String;
        //    66: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    69: istore          6
        //    71: iconst_0       
        //    72: istore          7
        //    74: iconst_0       
        //    75: istore          8
        //    77: iload           6
        //    79: ifeq            100
        //    82: ldc_w           "sf2wifixx"
        //    85: getstatic       android/os/Build.PRODUCT:Ljava/lang/String;
        //    88: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    91: ifeq            100
        //    94: iconst_0       
        //    95: istore          9
        //    97: goto            213
        //   100: iload           5
        //   102: ifeq            123
        //   105: iload           5
        //   107: iconst_1       
        //   108: if_icmpeq       145
        //   111: iload           5
        //   113: iconst_2       
        //   114: if_icmpeq       137
        //   117: iload           5
        //   119: iconst_3       
        //   120: if_icmpeq       129
        //   123: iconst_0       
        //   124: istore          9
        //   126: goto            149
        //   129: sipush          270
        //   132: istore          9
        //   134: goto            149
        //   137: sipush          180
        //   140: istore          9
        //   142: goto            149
        //   145: bipush          90
        //   147: istore          9
        //   149: aload_2        
        //   150: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   153: bipush          90
        //   155: irem           
        //   156: ifeq            164
        //   159: aload_2        
        //   160: iconst_0       
        //   161: putfield        android/hardware/Camera$CameraInfo.orientation:I
        //   164: aload_2        
        //   165: getfield        android/hardware/Camera$CameraInfo.facing:I
        //   168: iconst_1       
        //   169: if_icmpne       196
        //   172: sipush          360
        //   175: aload_2        
        //   176: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   179: iload           9
        //   181: iadd           
        //   182: sipush          360
        //   185: irem           
        //   186: isub           
        //   187: sipush          360
        //   190: irem           
        //   191: istore          9
        //   193: goto            213
        //   196: aload_2        
        //   197: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   200: iload           9
        //   202: isub           
        //   203: sipush          360
        //   206: iadd           
        //   207: sipush          360
        //   210: irem           
        //   211: istore          9
        //   213: aload_0        
        //   214: iload           9
        //   216: putfield        org/telegram/messenger/camera/CameraSession.currentOrientation:I
        //   219: aload_1        
        //   220: iload           9
        //   222: invokevirtual   android/hardware/Camera.setDisplayOrientation:(I)V
        //   225: aload_3        
        //   226: ifnull          448
        //   229: aload_3        
        //   230: aload_0        
        //   231: getfield        org/telegram/messenger/camera/CameraSession.previewSize:Lorg/telegram/messenger/camera/Size;
        //   234: invokevirtual   org/telegram/messenger/camera/Size.getWidth:()I
        //   237: aload_0        
        //   238: getfield        org/telegram/messenger/camera/CameraSession.previewSize:Lorg/telegram/messenger/camera/Size;
        //   241: invokevirtual   org/telegram/messenger/camera/Size.getHeight:()I
        //   244: invokevirtual   android/hardware/Camera$Parameters.setPreviewSize:(II)V
        //   247: aload_3        
        //   248: aload_0        
        //   249: getfield        org/telegram/messenger/camera/CameraSession.pictureSize:Lorg/telegram/messenger/camera/Size;
        //   252: invokevirtual   org/telegram/messenger/camera/Size.getWidth:()I
        //   255: aload_0        
        //   256: getfield        org/telegram/messenger/camera/CameraSession.pictureSize:Lorg/telegram/messenger/camera/Size;
        //   259: invokevirtual   org/telegram/messenger/camera/Size.getHeight:()I
        //   262: invokevirtual   android/hardware/Camera$Parameters.setPictureSize:(II)V
        //   265: aload_3        
        //   266: aload_0        
        //   267: getfield        org/telegram/messenger/camera/CameraSession.pictureFormat:I
        //   270: invokevirtual   android/hardware/Camera$Parameters.setPictureFormat:(I)V
        //   273: aload_3        
        //   274: invokevirtual   android/hardware/Camera$Parameters.getSupportedFocusModes:()Ljava/util/List;
        //   277: ldc_w           "continuous-picture"
        //   280: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   285: ifeq            295
        //   288: aload_3        
        //   289: ldc_w           "continuous-picture"
        //   292: invokevirtual   android/hardware/Camera$Parameters.setFocusMode:(Ljava/lang/String;)V
        //   295: aload_0        
        //   296: getfield        org/telegram/messenger/camera/CameraSession.jpegOrientation:I
        //   299: iconst_m1      
        //   300: if_icmpeq       351
        //   303: aload_2        
        //   304: getfield        android/hardware/Camera$CameraInfo.facing:I
        //   307: iconst_1       
        //   308: if_icmpne       333
        //   311: aload_2        
        //   312: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   315: aload_0        
        //   316: getfield        org/telegram/messenger/camera/CameraSession.jpegOrientation:I
        //   319: isub           
        //   320: sipush          360
        //   323: iadd           
        //   324: sipush          360
        //   327: irem           
        //   328: istore          9
        //   330: goto            354
        //   333: aload_2        
        //   334: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   337: aload_0        
        //   338: getfield        org/telegram/messenger/camera/CameraSession.jpegOrientation:I
        //   341: iadd           
        //   342: sipush          360
        //   345: irem           
        //   346: istore          9
        //   348: goto            354
        //   351: iconst_0       
        //   352: istore          9
        //   354: aload_3        
        //   355: iload           9
        //   357: invokevirtual   android/hardware/Camera$Parameters.setRotation:(I)V
        //   360: aload_2        
        //   361: getfield        android/hardware/Camera$CameraInfo.facing:I
        //   364: iconst_1       
        //   365: if_icmpne       395
        //   368: sipush          360
        //   371: iload           5
        //   373: isub           
        //   374: sipush          360
        //   377: irem           
        //   378: iload           9
        //   380: if_icmpne       386
        //   383: iconst_1       
        //   384: istore          8
        //   386: aload_0        
        //   387: iload           8
        //   389: putfield        org/telegram/messenger/camera/CameraSession.sameTakePictureOrientation:Z
        //   392: goto            415
        //   395: iload           7
        //   397: istore          8
        //   399: iload           5
        //   401: iload           9
        //   403: if_icmpne       409
        //   406: iconst_1       
        //   407: istore          8
        //   409: aload_0        
        //   410: iload           8
        //   412: putfield        org/telegram/messenger/camera/CameraSession.sameTakePictureOrientation:Z
        //   415: aload_3        
        //   416: aload_0        
        //   417: getfield        org/telegram/messenger/camera/CameraSession.currentFlashMode:Ljava/lang/String;
        //   420: invokevirtual   android/hardware/Camera$Parameters.setFlashMode:(Ljava/lang/String;)V
        //   423: aload_1        
        //   424: aload_3        
        //   425: invokevirtual   android/hardware/Camera.setParameters:(Landroid/hardware/Camera$Parameters;)V
        //   428: aload_3        
        //   429: invokevirtual   android/hardware/Camera$Parameters.getMaxNumMeteringAreas:()I
        //   432: ifle            448
        //   435: aload_0        
        //   436: iconst_1       
        //   437: putfield        org/telegram/messenger/camera/CameraSession.meteringAreaSupported:Z
        //   440: goto            448
        //   443: astore_3       
        //   444: aload_3        
        //   445: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   448: return         
        //   449: astore          4
        //   451: goto            415
        //   454: astore          4
        //   456: goto            428
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      8      443    448    Ljava/lang/Throwable;
        //  12     20     443    448    Ljava/lang/Throwable;
        //  22     28     34     41     Ljava/lang/Exception;
        //  22     28     443    448    Ljava/lang/Throwable;
        //  36     41     443    448    Ljava/lang/Throwable;
        //  41     71     443    448    Ljava/lang/Throwable;
        //  82     94     443    448    Ljava/lang/Throwable;
        //  149    164    443    448    Ljava/lang/Throwable;
        //  164    193    443    448    Ljava/lang/Throwable;
        //  196    213    443    448    Ljava/lang/Throwable;
        //  213    225    443    448    Ljava/lang/Throwable;
        //  229    295    443    448    Ljava/lang/Throwable;
        //  295    330    443    448    Ljava/lang/Throwable;
        //  333    348    443    448    Ljava/lang/Throwable;
        //  354    368    449    454    Ljava/lang/Exception;
        //  354    368    443    448    Ljava/lang/Throwable;
        //  386    392    449    454    Ljava/lang/Exception;
        //  386    392    443    448    Ljava/lang/Throwable;
        //  409    415    449    454    Ljava/lang/Exception;
        //  409    415    443    448    Ljava/lang/Throwable;
        //  415    423    443    448    Ljava/lang/Throwable;
        //  423    428    454    459    Ljava/lang/Exception;
        //  423    428    443    448    Ljava/lang/Throwable;
        //  428    440    443    448    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 221 out-of-bounds for length 221
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    protected void configureRecorder(final int n, final MediaRecorder mediaRecorder) {
        final Camera$CameraInfo camera$CameraInfo = new Camera$CameraInfo();
        Camera.getCameraInfo(this.cameraInfo.cameraId, camera$CameraInfo);
        this.getDisplayOrientation(camera$CameraInfo, false);
        final int jpegOrientation = this.jpegOrientation;
        int orientationHint;
        if (jpegOrientation != -1) {
            if (camera$CameraInfo.facing == 1) {
                orientationHint = (camera$CameraInfo.orientation - jpegOrientation + 360) % 360;
            }
            else {
                orientationHint = (camera$CameraInfo.orientation + jpegOrientation) % 360;
            }
        }
        else {
            orientationHint = 0;
        }
        mediaRecorder.setOrientationHint(orientationHint);
        final int high = this.getHigh();
        final boolean hasProfile = CamcorderProfile.hasProfile(this.cameraInfo.cameraId, high);
        final boolean hasProfile2 = CamcorderProfile.hasProfile(this.cameraInfo.cameraId, 0);
        if (hasProfile && (n == 1 || !hasProfile2)) {
            mediaRecorder.setProfile(CamcorderProfile.get(this.cameraInfo.cameraId, high));
        }
        else {
            if (!hasProfile2) {
                throw new IllegalStateException("cannot find valid CamcorderProfile");
            }
            mediaRecorder.setProfile(CamcorderProfile.get(this.cameraInfo.cameraId, 0));
        }
        this.isVideo = true;
    }
    
    protected void configureRoundCamera() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_1       
        //     2: putfield        org/telegram/messenger/camera/CameraSession.isVideo:Z
        //     5: aload_0        
        //     6: getfield        org/telegram/messenger/camera/CameraSession.cameraInfo:Lorg/telegram/messenger/camera/CameraInfo;
        //     9: getfield        org/telegram/messenger/camera/CameraInfo.camera:Landroid/hardware/Camera;
        //    12: astore_1       
        //    13: aload_1        
        //    14: ifnull          632
        //    17: new             Landroid/hardware/Camera$CameraInfo;
        //    20: astore_2       
        //    21: aload_2        
        //    22: invokespecial   android/hardware/Camera$CameraInfo.<init>:()V
        //    25: aconst_null    
        //    26: astore_3       
        //    27: aload_1        
        //    28: invokevirtual   android/hardware/Camera.getParameters:()Landroid/hardware/Camera$Parameters;
        //    31: astore          4
        //    33: aload           4
        //    35: astore_3       
        //    36: goto            46
        //    39: astore          4
        //    41: aload           4
        //    43: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //    46: aload_0        
        //    47: getfield        org/telegram/messenger/camera/CameraSession.cameraInfo:Lorg/telegram/messenger/camera/CameraInfo;
        //    50: invokevirtual   org/telegram/messenger/camera/CameraInfo.getCameraId:()I
        //    53: aload_2        
        //    54: invokestatic    android/hardware/Camera.getCameraInfo:(ILandroid/hardware/Camera$CameraInfo;)V
        //    57: aload_0        
        //    58: aload_2        
        //    59: iconst_1       
        //    60: invokespecial   org/telegram/messenger/camera/CameraSession.getDisplayOrientation:(Landroid/hardware/Camera$CameraInfo;Z)I
        //    63: istore          5
        //    65: ldc_w           "samsung"
        //    68: getstatic       android/os/Build.MANUFACTURER:Ljava/lang/String;
        //    71: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    74: istore          6
        //    76: iconst_0       
        //    77: istore          7
        //    79: iconst_0       
        //    80: istore          8
        //    82: iload           6
        //    84: ifeq            105
        //    87: ldc_w           "sf2wifixx"
        //    90: getstatic       android/os/Build.PRODUCT:Ljava/lang/String;
        //    93: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    96: ifeq            105
        //    99: iconst_0       
        //   100: istore          9
        //   102: goto            218
        //   105: iload           5
        //   107: ifeq            128
        //   110: iload           5
        //   112: iconst_1       
        //   113: if_icmpeq       150
        //   116: iload           5
        //   118: iconst_2       
        //   119: if_icmpeq       142
        //   122: iload           5
        //   124: iconst_3       
        //   125: if_icmpeq       134
        //   128: iconst_0       
        //   129: istore          9
        //   131: goto            154
        //   134: sipush          270
        //   137: istore          9
        //   139: goto            154
        //   142: sipush          180
        //   145: istore          9
        //   147: goto            154
        //   150: bipush          90
        //   152: istore          9
        //   154: aload_2        
        //   155: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   158: bipush          90
        //   160: irem           
        //   161: ifeq            169
        //   164: aload_2        
        //   165: iconst_0       
        //   166: putfield        android/hardware/Camera$CameraInfo.orientation:I
        //   169: aload_2        
        //   170: getfield        android/hardware/Camera$CameraInfo.facing:I
        //   173: iconst_1       
        //   174: if_icmpne       201
        //   177: sipush          360
        //   180: aload_2        
        //   181: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   184: iload           9
        //   186: iadd           
        //   187: sipush          360
        //   190: irem           
        //   191: isub           
        //   192: sipush          360
        //   195: irem           
        //   196: istore          9
        //   198: goto            218
        //   201: aload_2        
        //   202: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   205: iload           9
        //   207: isub           
        //   208: sipush          360
        //   211: iadd           
        //   212: sipush          360
        //   215: irem           
        //   216: istore          9
        //   218: aload_0        
        //   219: iload           9
        //   221: putfield        org/telegram/messenger/camera/CameraSession.currentOrientation:I
        //   224: aload_1        
        //   225: iload           9
        //   227: invokevirtual   android/hardware/Camera.setDisplayOrientation:(I)V
        //   230: aload_0        
        //   231: aload_0        
        //   232: getfield        org/telegram/messenger/camera/CameraSession.currentOrientation:I
        //   235: iload           5
        //   237: isub           
        //   238: putfield        org/telegram/messenger/camera/CameraSession.diffOrientation:I
        //   241: aload_3        
        //   242: ifnull          632
        //   245: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   248: istore          6
        //   250: iload           6
        //   252: ifeq            317
        //   255: new             Ljava/lang/StringBuilder;
        //   258: astore          4
        //   260: aload           4
        //   262: invokespecial   java/lang/StringBuilder.<init>:()V
        //   265: aload           4
        //   267: ldc_w           "set preview size = "
        //   270: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   273: pop            
        //   274: aload           4
        //   276: aload_0        
        //   277: getfield        org/telegram/messenger/camera/CameraSession.previewSize:Lorg/telegram/messenger/camera/Size;
        //   280: invokevirtual   org/telegram/messenger/camera/Size.getWidth:()I
        //   283: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   286: pop            
        //   287: aload           4
        //   289: ldc_w           " "
        //   292: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   295: pop            
        //   296: aload           4
        //   298: aload_0        
        //   299: getfield        org/telegram/messenger/camera/CameraSession.previewSize:Lorg/telegram/messenger/camera/Size;
        //   302: invokevirtual   org/telegram/messenger/camera/Size.getHeight:()I
        //   305: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   308: pop            
        //   309: aload           4
        //   311: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   314: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   317: aload_3        
        //   318: aload_0        
        //   319: getfield        org/telegram/messenger/camera/CameraSession.previewSize:Lorg/telegram/messenger/camera/Size;
        //   322: invokevirtual   org/telegram/messenger/camera/Size.getWidth:()I
        //   325: aload_0        
        //   326: getfield        org/telegram/messenger/camera/CameraSession.previewSize:Lorg/telegram/messenger/camera/Size;
        //   329: invokevirtual   org/telegram/messenger/camera/Size.getHeight:()I
        //   332: invokevirtual   android/hardware/Camera$Parameters.setPreviewSize:(II)V
        //   335: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   338: ifeq            403
        //   341: new             Ljava/lang/StringBuilder;
        //   344: astore          4
        //   346: aload           4
        //   348: invokespecial   java/lang/StringBuilder.<init>:()V
        //   351: aload           4
        //   353: ldc_w           "set picture size = "
        //   356: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   359: pop            
        //   360: aload           4
        //   362: aload_0        
        //   363: getfield        org/telegram/messenger/camera/CameraSession.pictureSize:Lorg/telegram/messenger/camera/Size;
        //   366: invokevirtual   org/telegram/messenger/camera/Size.getWidth:()I
        //   369: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   372: pop            
        //   373: aload           4
        //   375: ldc_w           " "
        //   378: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   381: pop            
        //   382: aload           4
        //   384: aload_0        
        //   385: getfield        org/telegram/messenger/camera/CameraSession.pictureSize:Lorg/telegram/messenger/camera/Size;
        //   388: invokevirtual   org/telegram/messenger/camera/Size.getHeight:()I
        //   391: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   394: pop            
        //   395: aload           4
        //   397: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   400: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   403: aload_3        
        //   404: aload_0        
        //   405: getfield        org/telegram/messenger/camera/CameraSession.pictureSize:Lorg/telegram/messenger/camera/Size;
        //   408: invokevirtual   org/telegram/messenger/camera/Size.getWidth:()I
        //   411: aload_0        
        //   412: getfield        org/telegram/messenger/camera/CameraSession.pictureSize:Lorg/telegram/messenger/camera/Size;
        //   415: invokevirtual   org/telegram/messenger/camera/Size.getHeight:()I
        //   418: invokevirtual   android/hardware/Camera$Parameters.setPictureSize:(II)V
        //   421: aload_3        
        //   422: aload_0        
        //   423: getfield        org/telegram/messenger/camera/CameraSession.pictureFormat:I
        //   426: invokevirtual   android/hardware/Camera$Parameters.setPictureFormat:(I)V
        //   429: aload_3        
        //   430: iconst_1       
        //   431: invokevirtual   android/hardware/Camera$Parameters.setRecordingHint:(Z)V
        //   434: aload_3        
        //   435: invokevirtual   android/hardware/Camera$Parameters.getSupportedFocusModes:()Ljava/util/List;
        //   438: ldc_w           "continuous-video"
        //   441: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   446: ifeq            459
        //   449: aload_3        
        //   450: ldc_w           "continuous-video"
        //   453: invokevirtual   android/hardware/Camera$Parameters.setFocusMode:(Ljava/lang/String;)V
        //   456: goto            481
        //   459: aload_3        
        //   460: invokevirtual   android/hardware/Camera$Parameters.getSupportedFocusModes:()Ljava/util/List;
        //   463: ldc_w           "auto"
        //   466: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   471: ifeq            481
        //   474: aload_3        
        //   475: ldc_w           "auto"
        //   478: invokevirtual   android/hardware/Camera$Parameters.setFocusMode:(Ljava/lang/String;)V
        //   481: aload_0        
        //   482: getfield        org/telegram/messenger/camera/CameraSession.jpegOrientation:I
        //   485: iconst_m1      
        //   486: if_icmpeq       537
        //   489: aload_2        
        //   490: getfield        android/hardware/Camera$CameraInfo.facing:I
        //   493: iconst_1       
        //   494: if_icmpne       519
        //   497: aload_2        
        //   498: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   501: aload_0        
        //   502: getfield        org/telegram/messenger/camera/CameraSession.jpegOrientation:I
        //   505: isub           
        //   506: sipush          360
        //   509: iadd           
        //   510: sipush          360
        //   513: irem           
        //   514: istore          9
        //   516: goto            540
        //   519: aload_2        
        //   520: getfield        android/hardware/Camera$CameraInfo.orientation:I
        //   523: aload_0        
        //   524: getfield        org/telegram/messenger/camera/CameraSession.jpegOrientation:I
        //   527: iadd           
        //   528: sipush          360
        //   531: irem           
        //   532: istore          9
        //   534: goto            540
        //   537: iconst_0       
        //   538: istore          9
        //   540: aload_3        
        //   541: iload           9
        //   543: invokevirtual   android/hardware/Camera$Parameters.setRotation:(I)V
        //   546: aload_2        
        //   547: getfield        android/hardware/Camera$CameraInfo.facing:I
        //   550: iconst_1       
        //   551: if_icmpne       581
        //   554: sipush          360
        //   557: iload           5
        //   559: isub           
        //   560: sipush          360
        //   563: irem           
        //   564: iload           9
        //   566: if_icmpne       572
        //   569: iconst_1       
        //   570: istore          8
        //   572: aload_0        
        //   573: iload           8
        //   575: putfield        org/telegram/messenger/camera/CameraSession.sameTakePictureOrientation:Z
        //   578: goto            601
        //   581: iload           7
        //   583: istore          8
        //   585: iload           5
        //   587: iload           9
        //   589: if_icmpne       595
        //   592: iconst_1       
        //   593: istore          8
        //   595: aload_0        
        //   596: iload           8
        //   598: putfield        org/telegram/messenger/camera/CameraSession.sameTakePictureOrientation:Z
        //   601: aload_3        
        //   602: ldc             "off"
        //   604: invokevirtual   android/hardware/Camera$Parameters.setFlashMode:(Ljava/lang/String;)V
        //   607: aload_1        
        //   608: aload_3        
        //   609: invokevirtual   android/hardware/Camera.setParameters:(Landroid/hardware/Camera$Parameters;)V
        //   612: aload_3        
        //   613: invokevirtual   android/hardware/Camera$Parameters.getMaxNumMeteringAreas:()I
        //   616: ifle            632
        //   619: aload_0        
        //   620: iconst_1       
        //   621: putfield        org/telegram/messenger/camera/CameraSession.meteringAreaSupported:Z
        //   624: goto            632
        //   627: astore_3       
        //   628: aload_3        
        //   629: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   632: return         
        //   633: astore          4
        //   635: goto            601
        //   638: astore          4
        //   640: goto            612
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      13     627    632    Ljava/lang/Throwable;
        //  17     25     627    632    Ljava/lang/Throwable;
        //  27     33     39     46     Ljava/lang/Exception;
        //  27     33     627    632    Ljava/lang/Throwable;
        //  41     46     627    632    Ljava/lang/Throwable;
        //  46     76     627    632    Ljava/lang/Throwable;
        //  87     99     627    632    Ljava/lang/Throwable;
        //  154    169    627    632    Ljava/lang/Throwable;
        //  169    198    627    632    Ljava/lang/Throwable;
        //  201    218    627    632    Ljava/lang/Throwable;
        //  218    241    627    632    Ljava/lang/Throwable;
        //  245    250    627    632    Ljava/lang/Throwable;
        //  255    317    627    632    Ljava/lang/Throwable;
        //  317    403    627    632    Ljava/lang/Throwable;
        //  403    456    627    632    Ljava/lang/Throwable;
        //  459    481    627    632    Ljava/lang/Throwable;
        //  481    516    627    632    Ljava/lang/Throwable;
        //  519    534    627    632    Ljava/lang/Throwable;
        //  540    554    633    638    Ljava/lang/Exception;
        //  540    554    627    632    Ljava/lang/Throwable;
        //  572    578    633    638    Ljava/lang/Exception;
        //  572    578    627    632    Ljava/lang/Throwable;
        //  595    601    633    638    Ljava/lang/Exception;
        //  595    601    627    632    Ljava/lang/Throwable;
        //  601    607    627    632    Ljava/lang/Throwable;
        //  607    612    638    643    Ljava/lang/Exception;
        //  607    612    627    632    Ljava/lang/Throwable;
        //  612    624    627    632    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 301 out-of-bounds for length 301
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public void destroy() {
        this.initied = false;
        final OrientationEventListener orientationEventListener = this.orientationEventListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
            this.orientationEventListener = null;
        }
    }
    
    protected void focusToRect(final Rect rect, final Rect rect2) {
        try {
            final Camera camera = this.cameraInfo.camera;
            if (camera != null) {
                camera.cancelAutoFocus();
                Camera$Parameters parameters = null;
                try {
                    parameters = camera.getParameters();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                if (parameters != null) {
                    parameters.setFocusMode("auto");
                    final ArrayList<Camera$Area> focusAreas = new ArrayList<Camera$Area>();
                    focusAreas.add(new Camera$Area(rect, 1000));
                    parameters.setFocusAreas((List)focusAreas);
                    if (this.meteringAreaSupported) {
                        final ArrayList<Camera$Area> meteringAreas = new ArrayList<Camera$Area>();
                        meteringAreas.add(new Camera$Area(rect2, 1000));
                        parameters.setMeteringAreas((List)meteringAreas);
                    }
                    try {
                        camera.setParameters(parameters);
                        camera.autoFocus(this.autoFocusCallback);
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
            }
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
    }
    
    public String getCurrentFlashMode() {
        return this.currentFlashMode;
    }
    
    public int getCurrentOrientation() {
        return this.currentOrientation;
    }
    
    public int getDisplayOrientation() {
        try {
            final Camera$CameraInfo camera$CameraInfo = new Camera$CameraInfo();
            Camera.getCameraInfo(this.cameraInfo.getCameraId(), camera$CameraInfo);
            return this.getDisplayOrientation(camera$CameraInfo, true);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return 0;
        }
    }
    
    public String getNextFlashMode() {
        final ArrayList<String> availableFlashModes = CameraController.getInstance().availableFlashModes;
        int i = 0;
        while (i < availableFlashModes.size()) {
            if (availableFlashModes.get(i).equals(this.currentFlashMode)) {
                if (i < availableFlashModes.size() - 1) {
                    return availableFlashModes.get(i + 1);
                }
                return availableFlashModes.get(0);
            }
            else {
                ++i;
            }
        }
        return this.currentFlashMode;
    }
    
    public int getWorldAngle() {
        return this.diffOrientation;
    }
    
    public boolean isFlipFront() {
        return this.flipFront;
    }
    
    public boolean isInitied() {
        return this.initied;
    }
    
    public boolean isSameTakePictureOrientation() {
        return this.sameTakePictureOrientation;
    }
    
    public void setCurrentFlashMode(final String currentFlashMode) {
        this.currentFlashMode = currentFlashMode;
        this.configurePhotoCamera();
        final SharedPreferences$Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).edit();
        String s;
        if (this.cameraInfo.frontCamera != 0) {
            s = "flashMode_front";
        }
        else {
            s = "flashMode";
        }
        edit.putString(s, currentFlashMode).commit();
    }
    
    public void setFlipFront(final boolean flipFront) {
        this.flipFront = flipFront;
    }
    
    public void setInitied() {
        this.initied = true;
    }
    
    public void setOneShotPreviewCallback(final Camera$PreviewCallback oneShotPreviewCallback) {
        final CameraInfo cameraInfo = this.cameraInfo;
        if (cameraInfo != null) {
            final Camera camera = cameraInfo.camera;
            if (camera != null) {
                camera.setOneShotPreviewCallback(oneShotPreviewCallback);
            }
        }
    }
    
    public void setPreviewCallback(final Camera$PreviewCallback previewCallback) {
        this.cameraInfo.camera.setPreviewCallback(previewCallback);
    }
    
    protected void stopVideoRecording() {
        this.isVideo = false;
        this.configurePhotoCamera();
    }
}
