package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Assertions;
import java.nio.ShortBuffer;
import java.util.Arrays;

final class Sonic {
   private final int channelCount;
   private final short[] downSampleBuffer;
   private short[] inputBuffer;
   private int inputFrameCount;
   private final int inputSampleRateHz;
   private int maxDiff;
   private final int maxPeriod;
   private final int maxRequiredFrameCount;
   private int minDiff;
   private final int minPeriod;
   private int newRatePosition;
   private int oldRatePosition;
   private short[] outputBuffer;
   private int outputFrameCount;
   private final float pitch;
   private short[] pitchBuffer;
   private int pitchFrameCount;
   private int prevMinDiff;
   private int prevPeriod;
   private final float rate;
   private int remainingInputToCopyFrameCount;
   private final float speed;

   public Sonic(int var1, int var2, float var3, float var4, int var5) {
      this.inputSampleRateHz = var1;
      this.channelCount = var2;
      this.speed = var3;
      this.pitch = var4;
      this.rate = (float)var1 / (float)var5;
      this.minPeriod = var1 / 400;
      this.maxPeriod = var1 / 65;
      this.maxRequiredFrameCount = this.maxPeriod * 2;
      var1 = this.maxRequiredFrameCount;
      this.downSampleBuffer = new short[var1];
      this.inputBuffer = new short[var1 * var2];
      this.outputBuffer = new short[var1 * var2];
      this.pitchBuffer = new short[var1 * var2];
   }

   private void adjustRate(float var1, int var2) {
      if (this.outputFrameCount != var2) {
         int var3 = this.inputSampleRateHz;

         int var4;
         for(var4 = (int)((float)var3 / var1); var4 > 16384 || var3 > 16384; var3 /= 2) {
            var4 /= 2;
         }

         this.moveNewSamplesToPitchBuffer(var2);
         var2 = 0;

         while(true) {
            int var5 = this.pitchFrameCount;
            boolean var6 = true;
            if (var2 >= var5 - 1) {
               this.removePitchFrames(var5 - 1);
               return;
            }

            while(true) {
               var5 = this.oldRatePosition;
               int var7 = this.newRatePosition;
               if ((var5 + 1) * var4 <= var7 * var3) {
                  this.oldRatePosition = var5 + 1;
                  if (this.oldRatePosition == var3) {
                     this.oldRatePosition = 0;
                     if (var7 != var4) {
                        var6 = false;
                     }

                     Assertions.checkState(var6);
                     this.newRatePosition = 0;
                  }

                  ++var2;
                  break;
               }

               this.outputBuffer = this.ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, 1);
               var5 = 0;

               while(true) {
                  var7 = this.channelCount;
                  if (var5 >= var7) {
                     ++this.newRatePosition;
                     ++this.outputFrameCount;
                     break;
                  }

                  this.outputBuffer[this.outputFrameCount * var7 + var5] = this.interpolate(this.pitchBuffer, var7 * var2 + var5, var3, var4);
                  ++var5;
               }
            }
         }
      }
   }

   private void changeSpeed(float var1) {
      int var2 = this.inputFrameCount;
      if (var2 >= this.maxRequiredFrameCount) {
         int var3 = 0;

         int var4;
         do {
            if (this.remainingInputToCopyFrameCount > 0) {
               var4 = this.copyInputToOutput(var3);
            } else {
               var4 = this.findPitchPeriod(this.inputBuffer, var3);
               if ((double)var1 > 1.0D) {
                  var4 += this.skipPitchPeriod(this.inputBuffer, var3, var1, var4);
               } else {
                  var4 = this.insertPitchPeriod(this.inputBuffer, var3, var1, var4);
               }
            }

            var4 += var3;
            var3 = var4;
         } while(this.maxRequiredFrameCount + var4 <= var2);

         this.removeProcessedInputFrames(var4);
      }
   }

   private int copyInputToOutput(int var1) {
      int var2 = Math.min(this.maxRequiredFrameCount, this.remainingInputToCopyFrameCount);
      this.copyToOutput(this.inputBuffer, var1, var2);
      this.remainingInputToCopyFrameCount -= var2;
      return var2;
   }

   private void copyToOutput(short[] var1, int var2, int var3) {
      this.outputBuffer = this.ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, var3);
      int var4 = this.channelCount;
      System.arraycopy(var1, var2 * var4, this.outputBuffer, this.outputFrameCount * var4, var4 * var3);
      this.outputFrameCount += var3;
   }

   private void downSampleInput(short[] var1, int var2, int var3) {
      int var4 = this.maxRequiredFrameCount / var3;
      int var5 = this.channelCount;
      int var6 = var3 * var5;

      for(var3 = 0; var3 < var4; ++var3) {
         int var7 = 0;

         int var8;
         for(var8 = 0; var7 < var6; ++var7) {
            var8 += var1[var3 * var6 + var2 * var5 + var7];
         }

         var7 = var8 / var6;
         this.downSampleBuffer[var3] = (short)((short)var7);
      }

   }

   private short[] ensureSpaceForAdditionalFrames(short[] var1, int var2, int var3) {
      int var4 = var1.length;
      int var5 = this.channelCount;
      var4 /= var5;
      return var2 + var3 <= var4 ? var1 : Arrays.copyOf(var1, (var4 * 3 / 2 + var3) * var5);
   }

   private int findPitchPeriod(short[] var1, int var2) {
      int var3 = this.inputSampleRateHz;
      if (var3 > 4000) {
         var3 /= 4000;
      } else {
         var3 = 1;
      }

      if (this.channelCount == 1 && var3 == 1) {
         var2 = this.findPitchPeriodInRange(var1, var2, this.minPeriod, this.maxPeriod);
      } else {
         this.downSampleInput(var1, var2, var3);
         int var4 = this.findPitchPeriodInRange(this.downSampleBuffer, 0, this.minPeriod / var3, this.maxPeriod / var3);
         if (var3 != 1) {
            var4 *= var3;
            int var5 = var3 * 4;
            var3 = var4 - var5;
            var5 += var4;
            var4 = this.minPeriod;
            if (var3 < var4) {
               var3 = var4;
            }

            int var6 = this.maxPeriod;
            var4 = var5;
            if (var5 > var6) {
               var4 = var6;
            }

            if (this.channelCount == 1) {
               var2 = this.findPitchPeriodInRange(var1, var2, var3, var4);
            } else {
               this.downSampleInput(var1, var2, 1);
               var2 = this.findPitchPeriodInRange(this.downSampleBuffer, 0, var3, var4);
            }
         } else {
            var2 = var4;
         }
      }

      if (this.previousPeriodBetter(this.minDiff, this.maxDiff)) {
         var3 = this.prevPeriod;
      } else {
         var3 = var2;
      }

      this.prevMinDiff = this.minDiff;
      this.prevPeriod = var2;
      return var3;
   }

   private int findPitchPeriodInRange(short[] var1, int var2, int var3, int var4) {
      int var5 = var2 * this.channelCount;
      int var6 = 1;
      int var7 = 0;
      int var8 = 0;

      int var9;
      int var13;
      for(var9 = 255; var3 <= var4; var9 = var13) {
         int var10 = 0;

         for(var2 = 0; var10 < var3; ++var10) {
            var2 += Math.abs(var1[var5 + var10] - var1[var5 + var3 + var10]);
         }

         int var11 = var6;
         var10 = var7;
         if (var2 * var7 < var6 * var3) {
            var10 = var3;
            var11 = var2;
         }

         int var12 = var8;
         var13 = var9;
         if (var2 * var9 > var8 * var3) {
            var13 = var3;
            var12 = var2;
         }

         ++var3;
         var6 = var11;
         var7 = var10;
         var8 = var12;
      }

      this.minDiff = var6 / var7;
      this.maxDiff = var8 / var9;
      return var7;
   }

   private int insertPitchPeriod(short[] var1, int var2, float var3, int var4) {
      int var5;
      if (var3 < 0.5F) {
         var5 = (int)((float)var4 * var3 / (1.0F - var3));
      } else {
         this.remainingInputToCopyFrameCount = (int)((float)var4 * (2.0F * var3 - 1.0F) / (1.0F - var3));
         var5 = var4;
      }

      short[] var6 = this.outputBuffer;
      int var7 = this.outputFrameCount;
      int var8 = var4 + var5;
      this.outputBuffer = this.ensureSpaceForAdditionalFrames(var6, var7, var8);
      var7 = this.channelCount;
      System.arraycopy(var1, var2 * var7, this.outputBuffer, this.outputFrameCount * var7, var7 * var4);
      overlapAdd(var5, this.channelCount, this.outputBuffer, this.outputFrameCount + var4, var1, var2 + var4, var1, var2);
      this.outputFrameCount += var8;
      return var5;
   }

   private short interpolate(short[] var1, int var2, int var3, int var4) {
      short var5 = var1[var2];
      short var9 = var1[var2 + this.channelCount];
      int var6 = this.newRatePosition;
      int var7 = this.oldRatePosition;
      int var8 = (var7 + 1) * var4;
      var3 = var8 - var6 * var3;
      var4 = var8 - var7 * var4;
      return (short)((var5 * var3 + (var4 - var3) * var9) / var4);
   }

   private void moveNewSamplesToPitchBuffer(int var1) {
      int var2 = this.outputFrameCount - var1;
      this.pitchBuffer = this.ensureSpaceForAdditionalFrames(this.pitchBuffer, this.pitchFrameCount, var2);
      short[] var3 = this.outputBuffer;
      int var4 = this.channelCount;
      System.arraycopy(var3, var1 * var4, this.pitchBuffer, this.pitchFrameCount * var4, var4 * var2);
      this.outputFrameCount = var1;
      this.pitchFrameCount += var2;
   }

   private static void overlapAdd(int var0, int var1, short[] var2, int var3, short[] var4, int var5, short[] var6, int var7) {
      for(int var8 = 0; var8 < var1; ++var8) {
         int var9 = var5 * var1 + var8;
         int var10 = var7 * var1 + var8;
         int var11 = var3 * var1 + var8;

         for(int var12 = 0; var12 < var0; ++var12) {
            var2[var11] = (short)((short)((var4[var9] * (var0 - var12) + var6[var10] * var12) / var0));
            var11 += var1;
            var9 += var1;
            var10 += var1;
         }
      }

   }

   private boolean previousPeriodBetter(int var1, int var2) {
      if (var1 != 0 && this.prevPeriod != 0) {
         if (var2 > var1 * 3) {
            return false;
         } else {
            return var1 * 2 > this.prevMinDiff * 3;
         }
      } else {
         return false;
      }
   }

   private void processStreamInput() {
      int var1 = this.outputFrameCount;
      float var2 = this.speed;
      float var3 = this.pitch;
      var2 /= var3;
      var3 = this.rate * var3;
      double var4 = (double)var2;
      if (var4 <= 1.00001D && var4 >= 0.99999D) {
         this.copyToOutput(this.inputBuffer, 0, this.inputFrameCount);
         this.inputFrameCount = 0;
      } else {
         this.changeSpeed(var2);
      }

      if (var3 != 1.0F) {
         this.adjustRate(var3, var1);
      }

   }

   private void removePitchFrames(int var1) {
      if (var1 != 0) {
         short[] var2 = this.pitchBuffer;
         int var3 = this.channelCount;
         System.arraycopy(var2, var1 * var3, var2, 0, (this.pitchFrameCount - var1) * var3);
         this.pitchFrameCount -= var1;
      }
   }

   private void removeProcessedInputFrames(int var1) {
      int var2 = this.inputFrameCount - var1;
      short[] var3 = this.inputBuffer;
      int var4 = this.channelCount;
      System.arraycopy(var3, var1 * var4, var3, 0, var4 * var2);
      this.inputFrameCount = var2;
   }

   private int skipPitchPeriod(short[] var1, int var2, float var3, int var4) {
      int var5;
      if (var3 >= 2.0F) {
         var5 = (int)((float)var4 / (var3 - 1.0F));
      } else {
         this.remainingInputToCopyFrameCount = (int)((float)var4 * (2.0F - var3) / (var3 - 1.0F));
         var5 = var4;
      }

      this.outputBuffer = this.ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, var5);
      overlapAdd(var5, this.channelCount, this.outputBuffer, this.outputFrameCount, var1, var2, var1, var2 + var4);
      this.outputFrameCount += var5;
      return var5;
   }

   public void flush() {
      this.inputFrameCount = 0;
      this.outputFrameCount = 0;
      this.pitchFrameCount = 0;
      this.oldRatePosition = 0;
      this.newRatePosition = 0;
      this.remainingInputToCopyFrameCount = 0;
      this.prevPeriod = 0;
      this.prevMinDiff = 0;
      this.minDiff = 0;
      this.maxDiff = 0;
   }

   public int getFramesAvailable() {
      return this.outputFrameCount;
   }

   public void getOutput(ShortBuffer var1) {
      int var2 = Math.min(var1.remaining() / this.channelCount, this.outputFrameCount);
      var1.put(this.outputBuffer, 0, this.channelCount * var2);
      this.outputFrameCount -= var2;
      short[] var4 = this.outputBuffer;
      int var3 = this.channelCount;
      System.arraycopy(var4, var2 * var3, var4, 0, this.outputFrameCount * var3);
   }

   public void queueEndOfStream() {
      int var1 = this.inputFrameCount;
      float var2 = this.speed;
      float var3 = this.pitch;
      float var4 = var2 / var3;
      var2 = this.rate;
      int var5 = this.outputFrameCount + (int)(((float)var1 / var4 + (float)this.pitchFrameCount) / (var2 * var3) + 0.5F);
      this.inputBuffer = this.ensureSpaceForAdditionalFrames(this.inputBuffer, var1, this.maxRequiredFrameCount * 2 + var1);
      int var6 = 0;

      while(true) {
         int var7 = this.maxRequiredFrameCount;
         int var8 = this.channelCount;
         if (var6 >= var7 * 2 * var8) {
            this.inputFrameCount += var7 * 2;
            this.processStreamInput();
            if (this.outputFrameCount > var5) {
               this.outputFrameCount = var5;
            }

            this.inputFrameCount = 0;
            this.remainingInputToCopyFrameCount = 0;
            this.pitchFrameCount = 0;
            return;
         }

         this.inputBuffer[var8 * var1 + var6] = (short)0;
         ++var6;
      }
   }

   public void queueInput(ShortBuffer var1) {
      int var2 = var1.remaining();
      int var3 = this.channelCount;
      var2 /= var3;
      this.inputBuffer = this.ensureSpaceForAdditionalFrames(this.inputBuffer, this.inputFrameCount, var2);
      var1.get(this.inputBuffer, this.inputFrameCount * this.channelCount, var3 * var2 * 2 / 2);
      this.inputFrameCount += var2;
      this.processStreamInput();
   }
}
