package org.telegram.messenger.voip;

import android.media.AudioRecord;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.AudioEffect.Descriptor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public class AudioRecordJNI {
   private AcousticEchoCanceler aec;
   private AutomaticGainControl agc;
   private AudioRecord audioRecord;
   private ByteBuffer buffer;
   private int bufferSize;
   private long nativeInst;
   private boolean needResampling = false;
   private NoiseSuppressor ns;
   private boolean running;
   private Thread thread;

   public AudioRecordJNI(long var1) {
      this.nativeInst = var1;
   }

   private int getBufferSize(int var1, int var2) {
      return Math.max(AudioRecord.getMinBufferSize(var2, 16, 2), var1);
   }

   private static boolean isGoodAudioEffect(AudioEffect var0) {
      Pattern var1 = makeNonEmptyRegex("adsp_good_impls");
      Pattern var2 = makeNonEmptyRegex("adsp_good_names");
      Descriptor var3 = var0.getDescriptor();
      StringBuilder var4 = new StringBuilder();
      var4.append(var0.getClass().getSimpleName());
      var4.append(": implementor=");
      var4.append(var3.implementor);
      var4.append(", name=");
      var4.append(var3.name);
      VLog.d(var4.toString());
      if (var1 != null && var1.matcher(var3.implementor).find()) {
         return true;
      } else if (var2 != null && var2.matcher(var3.name).find()) {
         return true;
      } else {
         if (var0 instanceof AcousticEchoCanceler) {
            Pattern var6 = makeNonEmptyRegex("aaec_good_impls");
            var1 = makeNonEmptyRegex("aaec_good_names");
            if (var6 != null && var6.matcher(var3.implementor).find()) {
               return true;
            }

            if (var1 != null && var1.matcher(var3.name).find()) {
               return true;
            }
         }

         if (var0 instanceof NoiseSuppressor) {
            Pattern var5 = makeNonEmptyRegex("ans_good_impls");
            var1 = makeNonEmptyRegex("ans_good_names");
            if (var5 != null && var5.matcher(var3.implementor).find()) {
               return true;
            }

            if (var1 != null && var1.matcher(var3.name).find()) {
               return true;
            }
         }

         return false;
      }
   }

   private static Pattern makeNonEmptyRegex(String var0) {
      var0 = VoIPServerConfig.getString(var0, "");
      if (TextUtils.isEmpty(var0)) {
         return null;
      } else {
         try {
            Pattern var2 = Pattern.compile(var0);
            return var2;
         } catch (Exception var1) {
            VLog.e((Throwable)var1);
            return null;
         }
      }
   }

   private native void nativeCallback(ByteBuffer var1);

   private void startThread() {
      if (this.thread == null) {
         this.running = true;
         final ByteBuffer var1;
         if (this.needResampling) {
            var1 = ByteBuffer.allocateDirect(1764);
         } else {
            var1 = null;
         }

         this.thread = new Thread(new Runnable() {
            public void run() {
               while(true) {
                  Exception var10000;
                  while(true) {
                     if (AudioRecordJNI.this.running) {
                        label50: {
                           boolean var10001;
                           label41: {
                              try {
                                 if (!AudioRecordJNI.this.needResampling) {
                                    AudioRecordJNI.this.audioRecord.read(AudioRecordJNI.this.buffer, 1920);
                                    break label41;
                                 }
                              } catch (Exception var5) {
                                 var10000 = var5;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 AudioRecordJNI.this.audioRecord.read(var1, 1764);
                                 Resampler.convert44to48(var1, AudioRecordJNI.this.buffer);
                              } catch (Exception var3) {
                                 var10000 = var3;
                                 var10001 = false;
                                 break;
                              }
                           }

                           try {
                              if (!AudioRecordJNI.this.running) {
                                 AudioRecordJNI.this.audioRecord.stop();
                                 break label50;
                              }
                           } catch (Exception var4) {
                              var10000 = var4;
                              var10001 = false;
                              break;
                           }

                           try {
                              AudioRecordJNI.this.nativeCallback(AudioRecordJNI.this.buffer);
                              continue;
                           } catch (Exception var2) {
                              var10000 = var2;
                              var10001 = false;
                              break;
                           }
                        }
                     }

                     VLog.i("audiorecord thread exits");
                     return;
                  }

                  Exception var1x = var10000;
                  VLog.e((Throwable)var1x);
               }
            }
         });
         this.thread.start();
      } else {
         throw new IllegalStateException("thread already started");
      }
   }

   private boolean tryInit(int var1, int var2) {
      AudioRecord var3 = this.audioRecord;
      if (var3 != null) {
         try {
            var3.release();
         } catch (Exception var8) {
         }
      }

      StringBuilder var9 = new StringBuilder();
      var9.append("Trying to initialize AudioRecord with source=");
      var9.append(var1);
      var9.append(" and sample rate=");
      var9.append(var2);
      VLog.i(var9.toString());
      int var4 = this.getBufferSize(this.bufferSize, 48000);

      try {
         var3 = new AudioRecord(var1, var2, 16, 2, var4);
         this.audioRecord = var3;
      } catch (Exception var7) {
         VLog.e("AudioRecord init failed!", var7);
      }

      boolean var5 = false;
      boolean var6;
      if (var2 != 48000) {
         var6 = true;
      } else {
         var6 = false;
      }

      this.needResampling = var6;
      var3 = this.audioRecord;
      var6 = var5;
      if (var3 != null) {
         var6 = var5;
         if (var3.getState() == 1) {
            var6 = true;
         }
      }

      return var6;
   }

   public int getEnabledEffectsMask() {
      AcousticEchoCanceler var1 = this.aec;
      byte var2;
      if (var1 != null && var1.getEnabled()) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      NoiseSuppressor var4 = this.ns;
      int var3 = var2;
      if (var4 != null) {
         var3 = var2;
         if (var4.getEnabled()) {
            var3 = var2 | 2;
         }
      }

      return var3;
   }

   public void init(int var1, int var2, int var3, int var4) {
      if (this.audioRecord != null) {
         throw new IllegalStateException("already inited");
      } else {
         this.bufferSize = var4;
         boolean var5 = this.tryInit(7, 48000);
         boolean var6 = var5;
         if (!var5) {
            var6 = this.tryInit(1, 48000);
         }

         var5 = var6;
         if (!var6) {
            var5 = this.tryInit(7, 44100);
         }

         var6 = var5;
         if (!var5) {
            var6 = this.tryInit(1, 44100);
         }

         if (var6) {
            if (VERSION.SDK_INT >= 16) {
               label147: {
                  var5 = false;

                  Throwable var7;
                  Throwable var10000;
                  boolean var10001;
                  label134: {
                     label133: {
                        try {
                           if (AutomaticGainControl.isAvailable()) {
                              this.agc = AutomaticGainControl.create(this.audioRecord.getAudioSessionId());
                              if (this.agc != null) {
                                 this.agc.setEnabled(false);
                              }
                              break label134;
                           }
                        } catch (Throwable var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label133;
                        }

                        try {
                           VLog.w("AutomaticGainControl is not available on this device :(");
                           break label134;
                        } catch (Throwable var16) {
                           var10000 = var16;
                           var10001 = false;
                        }
                     }

                     var7 = var10000;
                     VLog.e("error creating AutomaticGainControl", var7);
                  }

                  label123: {
                     label122: {
                        NoiseSuppressor var18;
                        label121: {
                           label120: {
                              label119: {
                                 try {
                                    if (!NoiseSuppressor.isAvailable()) {
                                       break label120;
                                    }

                                    this.ns = NoiseSuppressor.create(this.audioRecord.getAudioSessionId());
                                    if (this.ns == null) {
                                       break label123;
                                    }

                                    var18 = this.ns;
                                    if (VoIPServerConfig.getBoolean("use_system_ns", true) && isGoodAudioEffect(this.ns)) {
                                       break label119;
                                    }
                                 } catch (Throwable var15) {
                                    var10000 = var15;
                                    var10001 = false;
                                    break label122;
                                 }

                                 var6 = false;
                                 break label121;
                              }

                              var6 = true;
                              break label121;
                           }

                           try {
                              VLog.w("NoiseSuppressor is not available on this device :(");
                              break label123;
                           } catch (Throwable var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label122;
                           }
                        }

                        try {
                           var18.setEnabled(var6);
                           break label123;
                        } catch (Throwable var13) {
                           var10000 = var13;
                           var10001 = false;
                        }
                     }

                     var7 = var10000;
                     VLog.e("error creating NoiseSuppressor", var7);
                  }

                  label149: {
                     AcousticEchoCanceler var19;
                     label98: {
                        try {
                           if (AcousticEchoCanceler.isAvailable()) {
                              this.aec = AcousticEchoCanceler.create(this.audioRecord.getAudioSessionId());
                              if (this.aec == null) {
                                 break label147;
                              }

                              var19 = this.aec;
                              break label98;
                           }
                        } catch (Throwable var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label149;
                        }

                        try {
                           VLog.w("AcousticEchoCanceler is not available on this device");
                           break label147;
                        } catch (Throwable var11) {
                           var10000 = var11;
                           var10001 = false;
                           break label149;
                        }
                     }

                     var6 = var5;

                     label151: {
                        try {
                           if (!VoIPServerConfig.getBoolean("use_system_aec", true)) {
                              break label151;
                           }
                        } catch (Throwable var10) {
                           var10000 = var10;
                           var10001 = false;
                           break label149;
                        }

                        var6 = var5;

                        try {
                           if (!isGoodAudioEffect(this.aec)) {
                              break label151;
                           }
                        } catch (Throwable var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label149;
                        }

                        var6 = true;
                     }

                     try {
                        var19.setEnabled(var6);
                        break label147;
                     } catch (Throwable var8) {
                        var10000 = var8;
                        var10001 = false;
                     }
                  }

                  var7 = var10000;
                  VLog.e("error creating AcousticEchoCanceler", var7);
               }
            }

            this.buffer = ByteBuffer.allocateDirect(var4);
         }
      }
   }

   public void release() {
      this.running = false;
      Thread var1 = this.thread;
      if (var1 != null) {
         try {
            var1.join();
         } catch (InterruptedException var2) {
            VLog.e((Throwable)var2);
         }

         this.thread = null;
      }

      AudioRecord var3 = this.audioRecord;
      if (var3 != null) {
         var3.release();
         this.audioRecord = null;
      }

      AutomaticGainControl var4 = this.agc;
      if (var4 != null) {
         var4.release();
         this.agc = null;
      }

      NoiseSuppressor var5 = this.ns;
      if (var5 != null) {
         var5.release();
         this.ns = null;
      }

      AcousticEchoCanceler var6 = this.aec;
      if (var6 != null) {
         var6.release();
         this.aec = null;
      }

   }

   public boolean start() {
      AudioRecord var1 = this.audioRecord;
      if (var1 != null && var1.getState() == 1) {
         try {
            if (this.thread == null) {
               if (this.audioRecord == null) {
                  return false;
               }

               this.audioRecord.startRecording();
               this.startThread();
            } else {
               this.audioRecord.startRecording();
            }

            return true;
         } catch (Exception var2) {
            VLog.e("Error initializing AudioRecord", var2);
         }
      }

      return false;
   }

   public void stop() {
      try {
         if (this.audioRecord != null) {
            this.audioRecord.stop();
         }
      } catch (Exception var2) {
      }

   }
}
