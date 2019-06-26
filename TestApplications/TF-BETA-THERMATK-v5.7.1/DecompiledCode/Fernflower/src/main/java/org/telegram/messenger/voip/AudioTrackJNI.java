package org.telegram.messenger.voip;

import android.media.AudioTrack;
import java.nio.ByteBuffer;

public class AudioTrackJNI {
   private AudioTrack audioTrack;
   private byte[] buffer = new byte[1920];
   private int bufferSize;
   private long nativeInst;
   private boolean needResampling;
   private boolean running;
   private Thread thread;

   public AudioTrackJNI(long var1) {
      this.nativeInst = var1;
   }

   private int getBufferSize(int var1, int var2) {
      return Math.max(AudioTrack.getMinBufferSize(var2, 4, 2), var1);
   }

   private native void nativeCallback(byte[] var1);

   private void startThread() {
      if (this.thread == null) {
         this.running = true;
         this.thread = new Thread(new Runnable() {
            public void run() {
               try {
                  AudioTrackJNI.this.audioTrack.play();
               } catch (Exception var5) {
                  VLog.e("error starting AudioTrack", var5);
                  return;
               }

               boolean var1 = AudioTrackJNI.this.needResampling;
               ByteBuffer var2 = null;
               ByteBuffer var3;
               if (var1) {
                  var3 = ByteBuffer.allocateDirect(1920);
               } else {
                  var3 = null;
               }

               if (AudioTrackJNI.this.needResampling) {
                  var2 = ByteBuffer.allocateDirect(1764);
               }

               while(AudioTrackJNI.this.running) {
                  Exception var10000;
                  label59: {
                     boolean var10001;
                     label49: {
                        try {
                           if (AudioTrackJNI.this.needResampling) {
                              AudioTrackJNI.this.nativeCallback(AudioTrackJNI.this.buffer);
                              var3.rewind();
                              var3.put(AudioTrackJNI.this.buffer);
                              Resampler.convert48to44(var3, var2);
                              var2.rewind();
                              var2.get(AudioTrackJNI.this.buffer, 0, 1764);
                              AudioTrackJNI.this.audioTrack.write(AudioTrackJNI.this.buffer, 0, 1764);
                              break label49;
                           }
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break label59;
                        }

                        try {
                           AudioTrackJNI.this.nativeCallback(AudioTrackJNI.this.buffer);
                           AudioTrackJNI.this.audioTrack.write(AudioTrackJNI.this.buffer, 0, 1920);
                        } catch (Exception var7) {
                           var10000 = var7;
                           var10001 = false;
                           break label59;
                        }
                     }

                     try {
                        if (AudioTrackJNI.this.running) {
                           continue;
                        }

                        AudioTrackJNI.this.audioTrack.stop();
                        break;
                     } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                     }
                  }

                  Exception var4 = var10000;
                  VLog.e((Throwable)var4);
               }

               VLog.i("audiotrack thread exits");
            }
         });
         this.thread.start();
      } else {
         throw new IllegalStateException("thread already started");
      }
   }

   public void init(int var1, int var2, int var3, int var4) {
      if (this.audioTrack == null) {
         var2 = this.getBufferSize(var4, 48000);
         this.bufferSize = var4;
         byte var7;
         if (var3 == 1) {
            var7 = 4;
         } else {
            var7 = 12;
         }

         this.audioTrack = new AudioTrack(0, 48000, var7, 2, var2, 1);
         if (this.audioTrack.getState() != 1) {
            VLog.w("Error initializing AudioTrack with 48k, trying 44.1k with resampling");

            try {
               this.audioTrack.release();
            } catch (Throwable var6) {
            }

            var2 = this.getBufferSize(var4 * 6, 44100);
            StringBuilder var5 = new StringBuilder();
            var5.append("buffer size: ");
            var5.append(var2);
            VLog.d(var5.toString());
            if (var3 == 1) {
               var7 = 4;
            } else {
               var7 = 12;
            }

            this.audioTrack = new AudioTrack(0, 44100, var7, 2, var2, 1);
            this.needResampling = true;
         }

      } else {
         throw new IllegalStateException("already inited");
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

      AudioTrack var3 = this.audioTrack;
      if (var3 != null) {
         var3.release();
         this.audioTrack = null;
      }

   }

   public void start() {
      if (this.thread == null) {
         this.startThread();
      } else {
         this.audioTrack.play();
      }

   }

   public void stop() {
      AudioTrack var1 = this.audioTrack;
      if (var1 != null) {
         try {
            var1.stop();
         } catch (Exception var2) {
         }
      }

   }
}
