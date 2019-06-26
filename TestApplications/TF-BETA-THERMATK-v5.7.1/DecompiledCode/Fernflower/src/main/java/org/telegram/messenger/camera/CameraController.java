package org.telegram.messenger.camera;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build;
import android.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.camera.-..Lambda.CameraController.dwxNXb3SuNA_od-SPUyypaONvgM;
import org.telegram.tgnet.SerializedData;

public class CameraController implements OnInfoListener {
   private static final int CORE_POOL_SIZE = 1;
   private static volatile CameraController Instance;
   private static final int KEEP_ALIVE_SECONDS = 60;
   private static final int MAX_POOL_SIZE = 1;
   protected ArrayList availableFlashModes = new ArrayList();
   protected volatile ArrayList cameraInfos;
   private boolean cameraInitied;
   private boolean loadingCameras;
   private ArrayList onFinishCameraInitRunnables = new ArrayList();
   private CameraController.VideoTakeCallback onVideoTakeCallback;
   private String recordedFile;
   private MediaRecorder recorder;
   private ThreadPoolExecutor threadPool;

   public CameraController() {
      this.threadPool = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
   }

   public static Size chooseOptimalSize(List var0, int var1, int var2, Size var3) {
      ArrayList var4 = new ArrayList();
      int var5 = var3.getWidth();
      int var6 = var3.getHeight();

      for(int var7 = 0; var7 < var0.size(); ++var7) {
         var3 = (Size)var0.get(var7);
         if (var3.getHeight() == var3.getWidth() * var6 / var5 && var3.getWidth() >= var1 && var3.getHeight() >= var2) {
            var4.add(var3);
         }
      }

      if (var4.size() > 0) {
         return (Size)Collections.min(var4, new CameraController.CompareSizesByArea());
      } else {
         return (Size)Collections.max(var0, new CameraController.CompareSizesByArea());
      }
   }

   private void finishRecordingVideo() {
      // $FF: Couldn't be decompiled
   }

   public static CameraController getInstance() {
      CameraController var0 = Instance;
      CameraController var1 = var0;
      if (var0 == null) {
         synchronized(CameraController.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new CameraController();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   private static int getOrientation(byte[] var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;

         int var2;
         int var3;
         int var4;
         while(true) {
            var2 = var1;
            if (var1 + 3 < var0.length) {
               var3 = var1 + 1;
               var2 = var3;
               if ((var0[var1] & 255) == 255) {
                  var4 = var0[var3] & 255;
                  if (var4 == 255) {
                     var1 = var3;
                     continue;
                  }

                  ++var3;
                  var1 = var3;
                  if (var4 == 216) {
                     continue;
                  }

                  if (var4 == 1) {
                     var1 = var3;
                     continue;
                  }

                  var2 = var3;
                  if (var4 != 217) {
                     if (var4 != 218) {
                        int var5 = pack(var0, var3, 2, false);
                        if (var5 >= 2) {
                           var1 = var3 + var5;
                           if (var1 <= var0.length) {
                              if (var4 != 225 || var5 < 8 || pack(var0, var3 + 2, 4, false) != 1165519206 || pack(var0, var3 + 6, 2, false) != 0) {
                                 continue;
                              }

                              var2 = var3 + 8;
                              var1 = var5 - 8;
                              break;
                           }
                        }

                        return 0;
                     }

                     var2 = var3;
                  }
               }
            }

            var1 = 0;
            break;
         }

         if (var1 > 8) {
            var3 = pack(var0, var2, 4, false);
            if (var3 != 1229531648 && var3 != 1296891946) {
               return 0;
            }

            boolean var6;
            if (var3 == 1229531648) {
               var6 = true;
            } else {
               var6 = false;
            }

            var4 = pack(var0, var2 + 4, 4, var6) + 2;
            if (var4 >= 10 && var4 <= var1) {
               var3 = var2 + var4;
               var2 = var1 - var4;

               for(var1 = pack(var0, var3 - 2, 2, var6); var1 > 0 && var2 >= 12; --var1) {
                  if (pack(var0, var3, 2, var6) == 274) {
                     var1 = pack(var0, var3 + 8, 2, var6);
                     if (var1 != 1) {
                        if (var1 != 3) {
                           if (var1 != 6) {
                              if (var1 != 8) {
                                 return 0;
                              }

                              return 270;
                           }

                           return 90;
                        }

                        return 180;
                     }

                     return 0;
                  }

                  var3 += 12;
                  var2 -= 12;
               }
            }
         }

         return 0;
      }
   }

   // $FF: synthetic method
   static void lambda$close$4(Runnable var0, CameraSession var1, CountDownLatch var2) {
      if (var0 != null) {
         var0.run();
      }

      Camera var5 = var1.cameraInfo.camera;
      if (var5 != null) {
         try {
            var5.stopPreview();
            var1.cameraInfo.camera.setPreviewCallbackWithBuffer((PreviewCallback)null);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         try {
            var1.cameraInfo.camera.release();
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         var1.cameraInfo.camera = null;
         if (var2 != null) {
            var2.countDown();
         }

      }
   }

   // $FF: synthetic method
   static int lambda$null$0(Size var0, Size var1) {
      int var2 = var0.mWidth;
      int var3 = var1.mWidth;
      if (var2 < var3) {
         return 1;
      } else if (var2 > var3) {
         return -1;
      } else {
         var2 = var0.mHeight;
         var3 = var1.mHeight;
         if (var2 < var3) {
            return 1;
         } else {
            return var2 > var3 ? -1 : 0;
         }
      }
   }

   // $FF: synthetic method
   static void lambda$null$12(Camera var0, CameraSession var1) {
      try {
         Parameters var2 = var0.getParameters();
         var2.setFlashMode(var1.getCurrentFlashMode());
         var0.setParameters(var2);
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   // $FF: synthetic method
   static void lambda$openRound$8(CameraSession var0, Runnable var1, SurfaceTexture var2, Runnable var3) {
      Camera var4 = var0.cameraInfo.camera;
      Camera var5 = var4;

      Exception var10000;
      label126: {
         boolean var10001;
         label127: {
            try {
               if (!BuildVars.LOGS_ENABLED) {
                  break label127;
               }
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label126;
            }

            var5 = var4;

            try {
               FileLog.d("start creating round camera session");
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label126;
            }
         }

         Camera var6 = var4;
         if (var4 == null) {
            var5 = var4;

            CameraInfo var7;
            try {
               var7 = var0.cameraInfo;
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label126;
            }

            var5 = var4;

            try {
               var6 = Camera.open(var0.cameraInfo.cameraId);
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label126;
            }

            var5 = var4;

            try {
               var7.camera = var6;
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label126;
            }
         }

         var5 = var6;

         try {
            var6.getParameters();
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label126;
         }

         var5 = var6;

         try {
            var0.configureRoundCamera();
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }

         if (var1 != null) {
            var5 = var6;

            try {
               var1.run();
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label126;
            }
         }

         var5 = var6;

         try {
            var6.setPreviewTexture(var2);
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label126;
         }

         var5 = var6;

         try {
            var6.startPreview();
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label126;
         }

         if (var3 != null) {
            var5 = var6;

            try {
               AndroidUtilities.runOnUIThread(var3);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label126;
            }
         }

         var5 = var6;

         try {
            if (!BuildVars.LOGS_ENABLED) {
               return;
            }
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label126;
         }

         var5 = var6;

         try {
            FileLog.d("round camera session created");
            return;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      Exception var21 = var10000;
      var0.cameraInfo.camera = null;
      if (var5 != null) {
         var5.release();
      }

      FileLog.e((Throwable)var21);
   }

   // $FF: synthetic method
   static void lambda$startPreview$6(CameraSession var0) {
      Exception var10000;
      Camera var4;
      label40: {
         CameraInfo var1 = var0.cameraInfo;
         Camera var2 = var1.camera;
         Camera var3 = var2;
         boolean var10001;
         if (var2 == null) {
            var4 = var2;

            try {
               var3 = Camera.open(var1.cameraId);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label40;
            }

            var4 = var2;

            try {
               var1.camera = var3;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label40;
            }
         }

         var4 = var3;

         try {
            var3.startPreview();
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var8 = var10000;
      var0.cameraInfo.camera = null;
      if (var4 != null) {
         var4.release();
      }

      FileLog.e((Throwable)var8);
   }

   // $FF: synthetic method
   static void lambda$stopPreview$7(CameraSession var0) {
      Exception var10000;
      Camera var4;
      label40: {
         CameraInfo var1 = var0.cameraInfo;
         Camera var2 = var1.camera;
         Camera var3 = var2;
         boolean var10001;
         if (var2 == null) {
            var4 = var2;

            try {
               var3 = Camera.open(var1.cameraId);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label40;
            }

            var4 = var2;

            try {
               var1.camera = var3;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label40;
            }
         }

         var4 = var3;

         try {
            var3.stopPreview();
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var8 = var10000;
      var0.cameraInfo.camera = null;
      if (var4 != null) {
         var4.release();
      }

      FileLog.e((Throwable)var8);
   }

   // $FF: synthetic method
   static void lambda$takePicture$5(File var0, CameraInfo var1, boolean var2, Runnable var3, byte[] var4, Camera var5) {
      int var6 = (int)((float)AndroidUtilities.getPhotoSize() / AndroidUtilities.density);
      String var7 = String.format(Locale.US, "%s@%d_%d", Utilities.MD5(var0.getAbsolutePath()), var6, var6);

      Throwable var10000;
      boolean var10001;
      Bitmap var32;
      label104: {
         label112: {
            float var8;
            Options var30;
            try {
               var30 = new Options();
               var30.inJustDecodeBounds = true;
               BitmapFactory.decodeByteArray(var4, 0, var4.length, var30);
               var8 = Math.max((float)var30.outWidth / (float)AndroidUtilities.getPhotoSize(), (float)var30.outHeight / (float)AndroidUtilities.getPhotoSize());
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label112;
            }

            float var9 = var8;
            if (var8 < 1.0F) {
               var9 = 1.0F;
            }

            try {
               var30.inJustDecodeBounds = false;
               var30.inSampleSize = (int)var9;
               var30.inPurgeable = true;
               var32 = BitmapFactory.decodeByteArray(var4, 0, var4.length, var30);
               break label104;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
            }
         }

         Throwable var31 = var10000;
         FileLog.e(var31);
         var32 = null;
      }

      label92: {
         Exception var34;
         label109: {
            try {
               var6 = var1.frontCamera;
            } catch (Exception var20) {
               var34 = var20;
               var10001 = false;
               break label109;
            }

            if (var6 != 0 && var2) {
               label111: {
                  Bitmap var26;
                  try {
                     Matrix var25 = new Matrix();
                     var25.setRotate((float)getOrientation(var4));
                     var25.postScale(-1.0F, 1.0F);
                     var26 = Bitmaps.createBitmap(var32, 0, 0, var32.getWidth(), var32.getHeight(), var25, true);
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label111;
                  }

                  if (var26 != var32) {
                     try {
                        var32.recycle();
                     } catch (Throwable var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label111;
                     }
                  }

                  try {
                     FileOutputStream var10 = new FileOutputStream(var0);
                     var26.compress(CompressFormat.JPEG, 80, var10);
                     var10.flush();
                     var10.getFD().sync();
                     var10.close();
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label111;
                  }

                  if (var26 != null) {
                     try {
                        ImageLoader var33 = ImageLoader.getInstance();
                        BitmapDrawable var11 = new BitmapDrawable(var26);
                        var33.putImageToCache(var11, var7);
                     } catch (Throwable var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label111;
                     }
                  }

                  if (var3 != null) {
                     try {
                        var3.run();
                     } catch (Throwable var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label111;
                     }
                  }

                  return;
               }

               Throwable var27 = var10000;

               try {
                  FileLog.e(var27);
               } catch (Exception var15) {
                  var34 = var15;
                  var10001 = false;
                  break label109;
               }
            }

            try {
               FileOutputStream var28 = new FileOutputStream(var0);
               var28.write(var4);
               var28.flush();
               var28.getFD().sync();
               var28.close();
            } catch (Exception var14) {
               var34 = var14;
               var10001 = false;
               break label109;
            }

            if (var32 == null) {
               break label92;
            }

            try {
               ImageLoader var24 = ImageLoader.getInstance();
               BitmapDrawable var29 = new BitmapDrawable(var32);
               var24.putImageToCache(var29, var7);
               break label92;
            } catch (Exception var13) {
               var34 = var13;
               var10001 = false;
            }
         }

         Exception var23 = var34;
         FileLog.e((Throwable)var23);
      }

      if (var3 != null) {
         var3.run();
      }

   }

   private static int pack(byte[] var0, int var1, int var2, boolean var3) {
      byte var4;
      if (var3) {
         var1 += var2 - 1;
         var4 = -1;
      } else {
         var4 = 1;
      }

      int var5;
      for(var5 = 0; var2 > 0; --var2) {
         var5 = var0[var1] & 255 | var5 << 8;
         var1 += var4;
      }

      return var5;
   }

   public void cancelOnInitRunnable(Runnable var1) {
      this.onFinishCameraInitRunnables.remove(var1);
   }

   public void close(CameraSession var1, CountDownLatch var2, Runnable var3) {
      var1.destroy();
      this.threadPool.execute(new _$$Lambda$CameraController$Mku2q5OGwNan_h4puDdyST57U90(var3, var1, var2));
      if (var2 != null) {
         try {
            var2.await();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

   }

   public ArrayList getCameras() {
      return this.cameraInfos;
   }

   public void initCamera(Runnable var1) {
      if (var1 != null && !this.onFinishCameraInitRunnables.contains(var1)) {
         this.onFinishCameraInitRunnables.add(var1);
      }

      if (!this.loadingCameras && !this.cameraInitied) {
         this.loadingCameras = true;
         this.threadPool.execute(new _$$Lambda$CameraController$MTo6g4R2dOVhEmQ7AM4dYTP1p5w(this));
      }

   }

   public boolean isCameraInitied() {
      boolean var1;
      if (this.cameraInitied && this.cameraInfos != null && !this.cameraInfos.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$finishRecordingVideo$11$CameraController(File var1, Bitmap var2, long var3) {
      if (this.onVideoTakeCallback != null) {
         String var5 = var1.getAbsolutePath();
         if (var2 != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(var2), Utilities.MD5(var5));
         }

         this.onVideoTakeCallback.onFinishVideoRecording(var5, var3);
         this.onVideoTakeCallback = null;
      }

   }

   // $FF: synthetic method
   public void lambda$initCamera$3$CameraController() {
      String var1 = "cameraCache";

      label301: {
         boolean var10001;
         label300: {
            label322: {
               SharedPreferences var2;
               String var3;
               dwxNXb3SuNA_od-SPUyypaONvgM var4;
               ArrayList var5;
               try {
                  if (this.cameraInfos != null) {
                     break label322;
                  }

                  var2 = MessagesController.getGlobalMainSettings();
                  var3 = var2.getString("cameraCache", (String)null);
                  var4 = _$$Lambda$CameraController$dwxNXb3SuNA_od_SPUyypaONvgM.INSTANCE;
                  var5 = new ArrayList();
               } catch (Exception var32) {
                  var10001 = false;
                  break label301;
               }

               int var6;
               int var7;
               int var8;
               int var9;
               if (var3 != null) {
                  SerializedData var50;
                  try {
                     var50 = new SerializedData(Base64.decode(var3, 0));
                     var6 = var50.readInt32(false);
                  } catch (Exception var30) {
                     var10001 = false;
                     break label301;
                  }

                  var7 = 0;

                  while(true) {
                     if (var7 >= var6) {
                        try {
                           var50.cleanup();
                           break;
                        } catch (Exception var24) {
                           var10001 = false;
                           break label301;
                        }
                     }

                     CameraInfo var56;
                     try {
                        var56 = new CameraInfo(var50.readInt32(false), var50.readInt32(false));
                        var8 = var50.readInt32(false);
                     } catch (Exception var29) {
                        var10001 = false;
                        break label301;
                     }

                     for(var9 = 0; var9 < var8; ++var9) {
                        try {
                           ArrayList var52 = var56.previewSizes;
                           Size var59 = new Size(var50.readInt32(false), var50.readInt32(false));
                           var52.add(var59);
                        } catch (Exception var28) {
                           var10001 = false;
                           break label301;
                        }
                     }

                     try {
                        var8 = var50.readInt32(false);
                     } catch (Exception var27) {
                        var10001 = false;
                        break label301;
                     }

                     for(var9 = 0; var9 < var8; ++var9) {
                        try {
                           ArrayList var60 = var56.pictureSizes;
                           Size var53 = new Size(var50.readInt32(false), var50.readInt32(false));
                           var60.add(var53);
                        } catch (Exception var26) {
                           var10001 = false;
                           break label301;
                        }
                     }

                     try {
                        var5.add(var56);
                        Collections.sort(var56.previewSizes, var4);
                        Collections.sort(var56.pictureSizes, var4);
                     } catch (Exception var25) {
                        var10001 = false;
                        break label301;
                     }

                     ++var7;
                  }
               } else {
                  android.hardware.Camera.CameraInfo var54;
                  try {
                     var8 = Camera.getNumberOfCameras();
                     var54 = new android.hardware.Camera.CameraInfo();
                  } catch (Exception var23) {
                     var10001 = false;
                     break label301;
                  }

                  var7 = 0;
                  var9 = 4;

                  while(true) {
                     if (var7 < var8) {
                        CameraInfo var11;
                        boolean var12;
                        try {
                           Camera.getCameraInfo(var7, var54);
                           var11 = new CameraInfo(var7, var54.facing);
                           var12 = ApplicationLoader.mainInterfacePaused;
                        } catch (Exception var42) {
                           var10001 = false;
                           break label301;
                        }

                        if (var12) {
                           try {
                              if (ApplicationLoader.externalInterfacePaused) {
                                 break label300;
                              }
                           } catch (Exception var31) {
                              var10001 = false;
                              break label301;
                           }
                        }

                        List var10;
                        Camera var13;
                        Parameters var14;
                        try {
                           var13 = Camera.open(var11.getCameraId());
                           var14 = var13.getParameters();
                           var10 = var14.getSupportedPreviewSizes();
                        } catch (Exception var41) {
                           var10001 = false;
                           break label301;
                        }

                        var6 = 0;

                        while(true) {
                           int var15;
                           try {
                              var15 = var10.size();
                           } catch (Exception var38) {
                              var10001 = false;
                              break label301;
                           }

                           Size var18;
                           if (var6 >= var15) {
                              try {
                                 var10 = var14.getSupportedPictureSizes();
                              } catch (Exception var37) {
                                 var10001 = false;
                                 break label301;
                              }

                              var6 = 0;

                              while(true) {
                                 label315: {
                                    android.hardware.Camera.Size var61;
                                    try {
                                       if (var6 >= var10.size()) {
                                          break;
                                       }

                                       var61 = (android.hardware.Camera.Size)var10.get(var6);
                                       if (var61.width == 1280 && var61.height != 720) {
                                          break label315;
                                       }
                                    } catch (Exception var36) {
                                       var10001 = false;
                                       break label301;
                                    }

                                    try {
                                       if ("samsung".equals(Build.MANUFACTURER) && "jflteuc".equals(Build.PRODUCT) && var61.width >= 2048) {
                                          break label315;
                                       }
                                    } catch (Exception var35) {
                                       var10001 = false;
                                       break label301;
                                    }

                                    try {
                                       ArrayList var62 = var11.pictureSizes;
                                       var18 = new Size(var61.width, var61.height);
                                       var62.add(var18);
                                       if (BuildVars.LOGS_ENABLED) {
                                          StringBuilder var63 = new StringBuilder();
                                          var63.append("picture size = ");
                                          var63.append(var61.width);
                                          var63.append(" ");
                                          var63.append(var61.height);
                                          FileLog.d(var63.toString());
                                       }
                                    } catch (Exception var34) {
                                       var10001 = false;
                                       break label301;
                                    }
                                 }

                                 ++var6;
                              }

                              try {
                                 var13.release();
                                 var5.add(var11);
                                 Collections.sort(var11.previewSizes, var4);
                                 Collections.sort(var11.pictureSizes, var4);
                                 var9 += (var11.previewSizes.size() + var11.pictureSizes.size()) * 8 + 8;
                              } catch (Exception var33) {
                                 var10001 = false;
                                 break label301;
                              }

                              ++var7;
                              break;
                           }

                           android.hardware.Camera.Size var16;
                           try {
                              var16 = (android.hardware.Camera.Size)var10.get(var6);
                              var15 = var16.width;
                           } catch (Exception var40) {
                              var10001 = false;
                              break label301;
                           }

                           label251: {
                              if (var15 == 1280) {
                                 try {
                                    var15 = var16.height;
                                 } catch (Exception var22) {
                                    var10001 = false;
                                    break label301;
                                 }

                                 if (var15 != 720) {
                                    break label251;
                                 }
                              }

                              try {
                                 if (var16.height < 2160 && var16.width < 2160) {
                                    ArrayList var17 = var11.previewSizes;
                                    var18 = new Size(var16.width, var16.height);
                                    var17.add(var18);
                                    if (BuildVars.LOGS_ENABLED) {
                                       StringBuilder var64 = new StringBuilder();
                                       var64.append("preview size = ");
                                       var64.append(var16.width);
                                       var64.append(" ");
                                       var64.append(var16.height);
                                       FileLog.d(var64.toString());
                                    }
                                 }
                              } catch (Exception var39) {
                                 var10001 = false;
                                 break label301;
                              }
                           }

                           ++var6;
                        }
                     } else {
                        SerializedData var55;
                        try {
                           var55 = new SerializedData(var9);
                           var55.writeInt32(var5.size());
                        } catch (Exception var48) {
                           var10001 = false;
                           break label301;
                        }

                        for(var7 = 0; var7 < var8; ++var7) {
                           CameraInfo var58;
                           try {
                              var58 = (CameraInfo)var5.get(var7);
                              var55.writeInt32(var58.cameraId);
                              var55.writeInt32(var58.frontCamera);
                              var6 = var58.previewSizes.size();
                              var55.writeInt32(var6);
                           } catch (Exception var47) {
                              var10001 = false;
                              break label301;
                           }

                           Size var57;
                           for(var9 = 0; var9 < var6; ++var9) {
                              try {
                                 var57 = (Size)var58.previewSizes.get(var9);
                                 var55.writeInt32(var57.mWidth);
                                 var55.writeInt32(var57.mHeight);
                              } catch (Exception var46) {
                                 var10001 = false;
                                 break label301;
                              }
                           }

                           try {
                              var6 = var58.pictureSizes.size();
                              var55.writeInt32(var6);
                           } catch (Exception var45) {
                              var10001 = false;
                              break label301;
                           }

                           for(var9 = 0; var9 < var6; ++var9) {
                              try {
                                 var57 = (Size)var58.pictureSizes.get(var9);
                                 var55.writeInt32(var57.mWidth);
                                 var55.writeInt32(var57.mHeight);
                              } catch (Exception var44) {
                                 var10001 = false;
                                 break label301;
                              }
                           }
                        }

                        try {
                           var2.edit().putString(var1, Base64.encodeToString(var55.toByteArray(), 0)).commit();
                           var55.cleanup();
                           break;
                        } catch (Exception var43) {
                           var10001 = false;
                           break label301;
                        }
                     }
                  }
               }

               try {
                  this.cameraInfos = var5;
               } catch (Exception var21) {
                  var10001 = false;
                  break label301;
               }
            }

            try {
               _$$Lambda$CameraController$llC2aHeeX_BAOaFu9N7370RXqsE var51 = new _$$Lambda$CameraController$llC2aHeeX_BAOaFu9N7370RXqsE(this);
               AndroidUtilities.runOnUIThread(var51);
               return;
            } catch (Exception var20) {
               var10001 = false;
               break label301;
            }
         }

         try {
            RuntimeException var49 = new RuntimeException("app paused");
            throw var49;
         } catch (Exception var19) {
            var10001 = false;
         }
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$CameraController$swHPhGP8pVLLDtYLp0Z_CfPjXX4(this));
   }

   // $FF: synthetic method
   public void lambda$null$1$CameraController() {
      this.loadingCameras = false;
      this.cameraInitied = true;
      if (!this.onFinishCameraInitRunnables.isEmpty()) {
         for(int var1 = 0; var1 < this.onFinishCameraInitRunnables.size(); ++var1) {
            ((Runnable)this.onFinishCameraInitRunnables.get(var1)).run();
         }

         this.onFinishCameraInitRunnables.clear();
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cameraInitied);
   }

   // $FF: synthetic method
   public void lambda$null$2$CameraController() {
      this.onFinishCameraInitRunnables.clear();
      this.loadingCameras = false;
      this.cameraInitied = false;
   }

   // $FF: synthetic method
   public void lambda$open$9$CameraController(CameraSession var1, Runnable var2, SurfaceTexture var3, Runnable var4) {
      Camera var8;
      Exception var10000;
      label158: {
         CameraInfo var5 = var1.cameraInfo;
         Camera var6 = var5.camera;
         Camera var7 = var6;
         boolean var10001;
         if (var6 == null) {
            var8 = var6;

            try {
               var7 = Camera.open(var5.cameraId);
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label158;
            }

            var8 = var6;

            try {
               var5.camera = var7;
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label158;
            }
         }

         var8 = var7;

         List var27;
         try {
            var27 = var7.getParameters().getSupportedFlashModes();
         } catch (Exception var23) {
            var10000 = var23;
            var10001 = false;
            break label158;
         }

         var8 = var7;

         try {
            this.availableFlashModes.clear();
         } catch (Exception var22) {
            var10000 = var22;
            var10001 = false;
            break label158;
         }

         if (var27 != null) {
            int var9 = 0;

            while(true) {
               var8 = var7;

               try {
                  if (var9 >= var27.size()) {
                     break;
                  }
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label158;
               }

               var8 = var7;

               String var28;
               try {
                  var28 = (String)var27.get(var9);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label158;
               }

               var8 = var7;

               label162: {
                  label163: {
                     try {
                        if (var28.equals("off")) {
                           break label163;
                        }
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label158;
                     }

                     var8 = var7;

                     try {
                        if (var28.equals("on")) {
                           break label163;
                        }
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label158;
                     }

                     var8 = var7;

                     try {
                        if (!var28.equals("auto")) {
                           break label162;
                        }
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label158;
                     }
                  }

                  var8 = var7;

                  try {
                     this.availableFlashModes.add(var28);
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label158;
                  }
               }

               ++var9;
            }

            var8 = var7;

            try {
               var1.checkFlashMode((String)this.availableFlashModes.get(0));
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label158;
            }
         }

         if (var2 != null) {
            var8 = var7;

            try {
               var2.run();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label158;
            }
         }

         var8 = var7;

         try {
            var1.configurePhotoCamera();
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label158;
         }

         var8 = var7;

         try {
            var7.setPreviewTexture(var3);
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label158;
         }

         var8 = var7;

         try {
            var7.startPreview();
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label158;
         }

         if (var4 == null) {
            return;
         }

         var8 = var7;

         try {
            AndroidUtilities.runOnUIThread(var4);
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var26 = var10000;
      var1.cameraInfo.camera = null;
      if (var8 != null) {
         var8.release();
      }

      FileLog.e((Throwable)var26);
   }

   // $FF: synthetic method
   public void lambda$recordVideo$10$CameraController(Camera var1, CameraSession var2, File var3, CameraInfo var4, CameraController.VideoTakeCallback var5, Runnable var6) {
      if (var1 != null) {
         Exception var10000;
         Exception var17;
         label71: {
            boolean var10001;
            label72: {
               label65: {
                  Parameters var7;
                  String var8;
                  label64: {
                     label63: {
                        try {
                           var7 = var1.getParameters();
                           if (var2.getCurrentFlashMode().equals("on")) {
                              break label63;
                           }
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label65;
                        }

                        var8 = "off";
                        break label64;
                     }

                     var8 = "torch";
                  }

                  try {
                     var7.setFlashMode(var8);
                     var1.setParameters(var7);
                     break label72;
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                  }
               }

               Exception var18 = var10000;

               try {
                  FileLog.e((Throwable)var18);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label71;
               }
            }

            try {
               var1.unlock();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label71;
            }

            label73: {
               try {
                  MediaRecorder var19 = new MediaRecorder();
                  this.recorder = var19;
                  this.recorder.setCamera(var1);
                  this.recorder.setVideoSource(1);
                  this.recorder.setAudioSource(5);
                  var2.configureRecorder(1, this.recorder);
                  this.recorder.setOutputFile(var3.getAbsolutePath());
                  this.recorder.setMaxFileSize(1073741824L);
                  this.recorder.setVideoFrameRate(30);
                  this.recorder.setMaxDuration(0);
                  Size var16 = new Size(16, 9);
                  var16 = chooseOptimalSize(var4.getPictureSizes(), 720, 480, var16);
                  this.recorder.setVideoEncodingBitRate(1800000);
                  this.recorder.setVideoSize(var16.getWidth(), var16.getHeight());
                  this.recorder.setOnInfoListener(this);
                  this.recorder.prepare();
                  this.recorder.start();
                  this.onVideoTakeCallback = var5;
                  this.recordedFile = var3.getAbsolutePath();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label73;
               }

               if (var6 == null) {
                  return;
               }

               try {
                  AndroidUtilities.runOnUIThread(var6);
                  return;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
               }
            }

            var17 = var10000;

            try {
               this.recorder.release();
               this.recorder = null;
               FileLog.e((Throwable)var17);
               return;
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
            }
         }

         var17 = var10000;
         FileLog.e((Throwable)var17);
      }

   }

   // $FF: synthetic method
   public void lambda$stopVideoRecording$13$CameraController(CameraSession var1, boolean var2) {
      Camera var3;
      boolean var10001;
      try {
         var3 = var1.cameraInfo.camera;
      } catch (Exception var20) {
         var10001 = false;
         return;
      }

      Exception var5;
      if (var3 != null) {
         label107: {
            MediaRecorder var4;
            try {
               if (this.recorder == null) {
                  break label107;
               }

               var4 = this.recorder;
               this.recorder = null;
            } catch (Exception var19) {
               var10001 = false;
               return;
            }

            try {
               var4.stop();
            } catch (Exception var18) {
               var5 = var18;

               try {
                  FileLog.e((Throwable)var5);
               } catch (Exception var17) {
                  var10001 = false;
                  return;
               }
            }

            try {
               var4.release();
            } catch (Exception var16) {
               var5 = var16;

               try {
                  FileLog.e((Throwable)var5);
               } catch (Exception var15) {
                  var10001 = false;
                  return;
               }
            }

            try {
               var3.reconnect();
               var3.startPreview();
            } catch (Exception var14) {
               var5 = var14;

               try {
                  FileLog.e((Throwable)var5);
               } catch (Exception var13) {
                  var10001 = false;
                  return;
               }
            }

            try {
               var1.stopVideoRecording();
            } catch (Exception var12) {
               var5 = var12;

               try {
                  FileLog.e((Throwable)var5);
               } catch (Exception var11) {
                  var10001 = false;
                  return;
               }
            }
         }
      }

      try {
         Parameters var22 = var3.getParameters();
         var22.setFlashMode("off");
         var3.setParameters(var22);
      } catch (Exception var10) {
         var5 = var10;

         try {
            FileLog.e((Throwable)var5);
         } catch (Exception var9) {
            var10001 = false;
            return;
         }
      }

      try {
         ThreadPoolExecutor var23 = this.threadPool;
         _$$Lambda$CameraController$9retvXsm_Oz3JB3x45Buo_LxHfU var21 = new _$$Lambda$CameraController$9retvXsm_Oz3JB3x45Buo_LxHfU(var3, var1);
         var23.execute(var21);
      } catch (Exception var8) {
         var10001 = false;
         return;
      }

      if (!var2) {
         try {
            if (this.onVideoTakeCallback != null) {
               this.finishRecordingVideo();
               return;
            }
         } catch (Exception var7) {
            var10001 = false;
            return;
         }
      }

      try {
         this.onVideoTakeCallback = null;
      } catch (Exception var6) {
         var10001 = false;
      }

   }

   public void onInfo(MediaRecorder var1, int var2, int var3) {
      if (var2 == 800 || var2 == 801 || var2 == 1) {
         var1 = this.recorder;
         this.recorder = null;
         if (var1 != null) {
            var1.stop();
            var1.release();
         }

         if (this.onVideoTakeCallback != null) {
            this.finishRecordingVideo();
         }
      }

   }

   public void open(CameraSession var1, SurfaceTexture var2, Runnable var3, Runnable var4) {
      if (var1 != null && var2 != null) {
         this.threadPool.execute(new _$$Lambda$CameraController$nOnW1F7MY4Qrw4m6mXmkWXIiXBI(this, var1, var4, var2, var3));
      }

   }

   public void openRound(CameraSession var1, SurfaceTexture var2, Runnable var3, Runnable var4) {
      if (var1 != null && var2 != null) {
         this.threadPool.execute(new _$$Lambda$CameraController$ANuBkffO3J8bla21iFygwDfs5Ss(var1, var4, var2, var3));
      } else {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var5 = new StringBuilder();
            var5.append("failed to open round ");
            var5.append(var1);
            var5.append(" tex = ");
            var5.append(var2);
            FileLog.d(var5.toString());
         }

      }
   }

   public void recordVideo(CameraSession var1, File var2, CameraController.VideoTakeCallback var3, Runnable var4) {
      if (var1 != null) {
         CameraInfo var5 = var1.cameraInfo;
         Camera var6 = var5.camera;
         this.threadPool.execute(new _$$Lambda$CameraController$QhtYQbsLLWOPmfR7G2eDSElrRiU(this, var6, var1, var2, var5, var3, var4));
      }
   }

   public void startPreview(CameraSession var1) {
      if (var1 != null) {
         this.threadPool.execute(new _$$Lambda$CameraController$95vC3mtJ5YICFX1HGPyBSecWQH0(var1));
      }
   }

   public void stopPreview(CameraSession var1) {
      if (var1 != null) {
         this.threadPool.execute(new _$$Lambda$CameraController$TvlSt_eAGck2RVWXRxqaBNBbvno(var1));
      }
   }

   public void stopVideoRecording(CameraSession var1, boolean var2) {
      this.threadPool.execute(new _$$Lambda$CameraController$L4S5_dxkHAFS7LzqMIp7eixEAXY(this, var1, var2));
   }

   public boolean takePicture(File var1, CameraSession var2, Runnable var3) {
      if (var2 == null) {
         return false;
      } else {
         CameraInfo var4 = var2.cameraInfo;
         boolean var5 = var2.isFlipFront();
         Camera var6 = var4.camera;

         try {
            _$$Lambda$CameraController$Qbnmyb8uDsRl802IZhTfJAoPBlE var8 = new _$$Lambda$CameraController$Qbnmyb8uDsRl802IZhTfJAoPBlE(var1, var4, var5, var3);
            var6.takePicture((ShutterCallback)null, (PictureCallback)null, var8);
            return true;
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
            return false;
         }
      }
   }

   static class CompareSizesByArea implements Comparator {
      public int compare(Size var1, Size var2) {
         return Long.signum((long)var1.getWidth() * (long)var1.getHeight() - (long)var2.getWidth() * (long)var2.getHeight());
      }
   }

   public interface VideoTakeCallback {
      void onFinishVideoRecording(String var1, long var2);
   }
}
