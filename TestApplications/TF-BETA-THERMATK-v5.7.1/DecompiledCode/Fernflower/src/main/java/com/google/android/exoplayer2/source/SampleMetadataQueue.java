package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;

final class SampleMetadataQueue {
   private int absoluteFirstIndex;
   private int capacity = 1000;
   private TrackOutput.CryptoData[] cryptoDatas;
   private int[] flags;
   private Format[] formats;
   private boolean isLastSampleQueued;
   private long largestDiscardedTimestampUs;
   private long largestQueuedTimestampUs;
   private int length;
   private long[] offsets;
   private int readPosition;
   private int relativeFirstIndex;
   private int[] sizes;
   private int[] sourceIds;
   private long[] timesUs;
   private Format upstreamFormat;
   private boolean upstreamFormatRequired;
   private boolean upstreamKeyframeRequired;
   private int upstreamSourceId;

   public SampleMetadataQueue() {
      int var1 = this.capacity;
      this.sourceIds = new int[var1];
      this.offsets = new long[var1];
      this.timesUs = new long[var1];
      this.flags = new int[var1];
      this.sizes = new int[var1];
      this.cryptoDatas = new TrackOutput.CryptoData[var1];
      this.formats = new Format[var1];
      this.largestDiscardedTimestampUs = Long.MIN_VALUE;
      this.largestQueuedTimestampUs = Long.MIN_VALUE;
      this.upstreamFormatRequired = true;
      this.upstreamKeyframeRequired = true;
   }

   private long discardSamples(int var1) {
      this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, this.getLargestTimestamp(var1));
      this.length -= var1;
      this.absoluteFirstIndex += var1;
      this.relativeFirstIndex += var1;
      int var2 = this.relativeFirstIndex;
      int var3 = this.capacity;
      if (var2 >= var3) {
         this.relativeFirstIndex = var2 - var3;
      }

      this.readPosition -= var1;
      if (this.readPosition < 0) {
         this.readPosition = 0;
      }

      if (this.length == 0) {
         var3 = this.relativeFirstIndex;
         var1 = var3;
         if (var3 == 0) {
            var1 = this.capacity;
         }

         --var1;
         return this.offsets[var1] + (long)this.sizes[var1];
      } else {
         return this.offsets[this.relativeFirstIndex];
      }
   }

   private int findSampleBefore(int var1, int var2, long var3, boolean var5) {
      int var6 = var1;
      var1 = 0;

      int var7;
      for(var7 = -1; var1 < var2 && this.timesUs[var6] <= var3; ++var1) {
         if (!var5 || (this.flags[var6] & 1) != 0) {
            var7 = var1;
         }

         int var8 = var6 + 1;
         var6 = var8;
         if (var8 == this.capacity) {
            var6 = 0;
         }
      }

      return var7;
   }

   private long getLargestTimestamp(int var1) {
      long var2 = Long.MIN_VALUE;
      if (var1 == 0) {
         return Long.MIN_VALUE;
      } else {
         int var4 = this.getRelativeIndex(var1 - 1);
         int var5 = 0;

         long var6;
         while(true) {
            var6 = var2;
            if (var5 >= var1) {
               break;
            }

            var2 = Math.max(var2, this.timesUs[var4]);
            if ((this.flags[var4] & 1) != 0) {
               var6 = var2;
               break;
            }

            int var8 = var4 - 1;
            var4 = var8;
            if (var8 == -1) {
               var4 = this.capacity - 1;
            }

            ++var5;
         }

         return var6;
      }
   }

   private int getRelativeIndex(int var1) {
      var1 += this.relativeFirstIndex;
      int var2 = this.capacity;
      if (var1 >= var2) {
         var1 -= var2;
      }

      return var1;
   }

   public int advanceTo(long var1, boolean var3, boolean var4) {
      synchronized(this){}

      Throwable var10000;
      label184: {
         boolean var10001;
         int var5;
         label188: {
            try {
               var5 = this.getRelativeIndex(this.readPosition);
               if (!this.hasNextSample() || var1 < this.timesUs[var5]) {
                  return -1;
               }

               if (var1 <= this.largestQueuedTimestampUs) {
                  break label188;
               }
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label184;
            }

            if (!var4) {
               return -1;
            }
         }

         try {
            var5 = this.findSampleBefore(var5, this.length - this.readPosition, var1, var3);
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label184;
         }

         if (var5 == -1) {
            return -1;
         }

         try {
            this.readPosition += var5;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label184;
         }

         return var5;
      }

      Throwable var6 = var10000;
      throw var6;
   }

   public int advanceToEnd() {
      synchronized(this){}

      int var1;
      int var2;
      try {
         var1 = this.length;
         var2 = this.readPosition;
         this.readPosition = this.length;
      } finally {
         ;
      }

      return var1 - var2;
   }

   public boolean attemptSplice(long var1) {
      synchronized(this){}

      Throwable var10000;
      label531: {
         int var3;
         boolean var10001;
         try {
            var3 = this.length;
         } catch (Throwable var66) {
            var10000 = var66;
            var10001 = false;
            break label531;
         }

         boolean var4 = false;
         long var5;
         if (var3 == 0) {
            label535: {
               try {
                  var5 = this.largestDiscardedTimestampUs;
               } catch (Throwable var60) {
                  var10000 = var60;
                  var10001 = false;
                  break label535;
               }

               if (var1 > var5) {
                  var4 = true;
               }

               return var4;
            }
         } else {
            label536: {
               try {
                  var5 = Math.max(this.largestDiscardedTimestampUs, this.getLargestTimestamp(this.readPosition));
               } catch (Throwable var65) {
                  var10000 = var65;
                  var10001 = false;
                  break label536;
               }

               if (var5 >= var1) {
                  return false;
               }

               int var7;
               try {
                  var7 = this.length;
                  var3 = this.getRelativeIndex(this.length - 1);
               } catch (Throwable var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label536;
               }

               while(true) {
                  try {
                     if (var7 <= this.readPosition || this.timesUs[var3] < var1) {
                        break;
                     }
                  } catch (Throwable var64) {
                     var10000 = var64;
                     var10001 = false;
                     break label536;
                  }

                  int var8 = var7 - 1;
                  int var9 = var3 - 1;
                  var7 = var8;
                  var3 = var9;
                  if (var9 == -1) {
                     try {
                        var3 = this.capacity - 1;
                     } catch (Throwable var62) {
                        var10000 = var62;
                        var10001 = false;
                        break label536;
                     }

                     var7 = var8;
                  }
               }

               try {
                  this.discardUpstreamSamples(this.absoluteFirstIndex + var7);
               } catch (Throwable var61) {
                  var10000 = var61;
                  var10001 = false;
                  break label536;
               }

               return true;
            }
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   public void commitSample(long var1, int var3, long var4, int var6, TrackOutput.CryptoData var7) {
      synchronized(this){}

      Throwable var10000;
      label360: {
         boolean var8;
         boolean var10001;
         try {
            var8 = this.upstreamKeyframeRequired;
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label360;
         }

         if (var8) {
            if ((var3 & 1) == 0) {
               return;
            }

            try {
               this.upstreamKeyframeRequired = false;
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label360;
            }
         }

         label345: {
            label344: {
               try {
                  if (!this.upstreamFormatRequired) {
                     break label344;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label360;
               }

               var8 = false;
               break label345;
            }

            var8 = true;
         }

         try {
            Assertions.checkState(var8);
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label360;
         }

         if ((536870912 & var3) != 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         try {
            this.isLastSampleQueued = var8;
            this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, var1);
            int var9 = this.getRelativeIndex(this.length);
            this.timesUs[var9] = var1;
            this.offsets[var9] = var4;
            this.sizes[var9] = var6;
            this.flags[var9] = var3;
            this.cryptoDatas[var9] = var7;
            this.formats[var9] = this.upstreamFormat;
            this.sourceIds[var9] = this.upstreamSourceId;
            ++this.length;
            if (this.length == this.capacity) {
               var3 = this.capacity + 1000;
               int[] var10 = new int[var3];
               long[] var11 = new long[var3];
               long[] var12 = new long[var3];
               int[] var13 = new int[var3];
               int[] var47 = new int[var3];
               TrackOutput.CryptoData[] var14 = new TrackOutput.CryptoData[var3];
               Format[] var15 = new Format[var3];
               var6 = this.capacity - this.relativeFirstIndex;
               System.arraycopy(this.offsets, this.relativeFirstIndex, var11, 0, var6);
               System.arraycopy(this.timesUs, this.relativeFirstIndex, var12, 0, var6);
               System.arraycopy(this.flags, this.relativeFirstIndex, var13, 0, var6);
               System.arraycopy(this.sizes, this.relativeFirstIndex, var47, 0, var6);
               System.arraycopy(this.cryptoDatas, this.relativeFirstIndex, var14, 0, var6);
               System.arraycopy(this.formats, this.relativeFirstIndex, var15, 0, var6);
               System.arraycopy(this.sourceIds, this.relativeFirstIndex, var10, 0, var6);
               var9 = this.relativeFirstIndex;
               System.arraycopy(this.offsets, 0, var11, var6, var9);
               System.arraycopy(this.timesUs, 0, var12, var6, var9);
               System.arraycopy(this.flags, 0, var13, var6, var9);
               System.arraycopy(this.sizes, 0, var47, var6, var9);
               System.arraycopy(this.cryptoDatas, 0, var14, var6, var9);
               System.arraycopy(this.formats, 0, var15, var6, var9);
               System.arraycopy(this.sourceIds, 0, var10, var6, var9);
               this.offsets = var11;
               this.timesUs = var12;
               this.flags = var13;
               this.sizes = var47;
               this.cryptoDatas = var14;
               this.formats = var15;
               this.sourceIds = var10;
               this.relativeFirstIndex = 0;
               this.length = this.capacity;
               this.capacity = var3;
            }
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label360;
         }

         return;
      }

      Throwable var46 = var10000;
      throw var46;
   }

   public long discardTo(long var1, boolean var3, boolean var4) {
      synchronized(this){}

      Throwable var10000;
      label345: {
         boolean var10001;
         label340: {
            try {
               if (this.length != 0 && var1 >= this.timesUs[this.relativeFirstIndex]) {
                  break label340;
               }
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label345;
            }

            return -1L;
         }

         int var5;
         label347: {
            if (var4) {
               try {
                  if (this.readPosition != this.length) {
                     var5 = this.readPosition + 1;
                     break label347;
                  }
               } catch (Throwable var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label345;
               }
            }

            try {
               var5 = this.length;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label345;
            }
         }

         try {
            var5 = this.findSampleBefore(this.relativeFirstIndex, var5, var1, var3);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label345;
         }

         if (var5 == -1) {
            return -1L;
         }

         try {
            var1 = this.discardSamples(var5);
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label345;
         }

         return var1;
      }

      Throwable var6 = var10000;
      throw var6;
   }

   public long discardToEnd() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         int var1;
         boolean var10001;
         try {
            var1 = this.length;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label78;
         }

         if (var1 == 0) {
            return -1L;
         }

         long var2;
         try {
            var2 = this.discardSamples(this.length);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         return var2;
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public long discardToRead() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         int var1;
         boolean var10001;
         try {
            var1 = this.readPosition;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label78;
         }

         if (var1 == 0) {
            return -1L;
         }

         long var2;
         try {
            var2 = this.discardSamples(this.readPosition);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         return var2;
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public long discardUpstreamSamples(int var1) {
      var1 = this.getWriteIndex() - var1;
      boolean var2 = false;
      boolean var3;
      if (var1 >= 0 && var1 <= this.length - this.readPosition) {
         var3 = true;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);
      this.length -= var1;
      this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, this.getLargestTimestamp(this.length));
      var3 = var2;
      if (var1 == 0) {
         var3 = var2;
         if (this.isLastSampleQueued) {
            var3 = true;
         }
      }

      this.isLastSampleQueued = var3;
      var1 = this.length;
      if (var1 == 0) {
         return 0L;
      } else {
         var1 = this.getRelativeIndex(var1 - 1);
         return this.offsets[var1] + (long)this.sizes[var1];
      }
   }

   public boolean format(Format var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 == null) {
         label109: {
            try {
               this.upstreamFormatRequired = true;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label109;
            }

            return false;
         }
      } else {
         label120: {
            boolean var2;
            try {
               this.upstreamFormatRequired = false;
               var2 = Util.areEqual(var1, this.upstreamFormat);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label120;
            }

            if (var2) {
               return false;
            }

            try {
               this.upstreamFormat = var1;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label120;
            }

            return true;
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   public int getFirstIndex() {
      return this.absoluteFirstIndex;
   }

   public long getFirstTimestampUs() {
      synchronized(this){}
      boolean var5 = false;

      long var1;
      try {
         var5 = true;
         if (this.length != 0) {
            var1 = this.timesUs[this.relativeFirstIndex];
            var5 = false;
            return var1;
         }

         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      var1 = Long.MIN_VALUE;
      return var1;
   }

   public long getLargestQueuedTimestampUs() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.largestQueuedTimestampUs;
      } finally {
         ;
      }

      return var1;
   }

   public int getReadIndex() {
      return this.absoluteFirstIndex + this.readPosition;
   }

   public Format getUpstreamFormat() {
      synchronized(this){}
      boolean var3 = false;

      Format var1;
      try {
         var3 = true;
         if (!this.upstreamFormatRequired) {
            var1 = this.upstreamFormat;
            var3 = false;
            return var1;
         }

         var3 = false;
      } finally {
         if (var3) {
            ;
         }
      }

      var1 = null;
      return var1;
   }

   public int getWriteIndex() {
      return this.absoluteFirstIndex + this.length;
   }

   public boolean hasNextSample() {
      synchronized(this){}
      boolean var6 = false;

      int var1;
      int var2;
      try {
         var6 = true;
         var1 = this.readPosition;
         var2 = this.length;
         var6 = false;
      } finally {
         if (var6) {
            ;
         }
      }

      boolean var3;
      if (var1 != var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isLastSampleQueued() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.isLastSampleQueued;
      } finally {
         ;
      }

      return var1;
   }

   public int peekSourceId() {
      int var1 = this.getRelativeIndex(this.readPosition);
      if (this.hasNextSample()) {
         var1 = this.sourceIds[var1];
      } else {
         var1 = this.upstreamSourceId;
      }

      return var1;
   }

   public int read(FormatHolder var1, DecoderInputBuffer var2, boolean var3, boolean var4, Format var5, SampleMetadataQueue.SampleExtrasHolder var6) {
      synchronized(this){}

      Throwable var10000;
      label1291: {
         boolean var10001;
         label1292: {
            try {
               if (!this.hasNextSample()) {
                  break label1292;
               }
            } catch (Throwable var139) {
               var10000 = var139;
               var10001 = false;
               break label1291;
            }

            int var7;
            try {
               var7 = this.getRelativeIndex(this.readPosition);
            } catch (Throwable var135) {
               var10000 = var135;
               var10001 = false;
               break label1291;
            }

            if (!var3) {
               label1289: {
                  try {
                     if (this.formats[var7] != var5) {
                        break label1289;
                     }
                  } catch (Throwable var134) {
                     var10000 = var134;
                     var10001 = false;
                     break label1291;
                  }

                  try {
                     var3 = var2.isFlagsOnly();
                  } catch (Throwable var132) {
                     var10000 = var132;
                     var10001 = false;
                     break label1291;
                  }

                  if (var3) {
                     return -3;
                  }

                  try {
                     var2.timeUs = this.timesUs[var7];
                     var2.setFlags(this.flags[var7]);
                     var6.size = this.sizes[var7];
                     var6.offset = this.offsets[var7];
                     var6.cryptoData = this.cryptoDatas[var7];
                     ++this.readPosition;
                  } catch (Throwable var131) {
                     var10000 = var131;
                     var10001 = false;
                     break label1291;
                  }

                  return -4;
               }
            }

            try {
               var1.format = this.formats[var7];
            } catch (Throwable var133) {
               var10000 = var133;
               var10001 = false;
               break label1291;
            }

            return -5;
         }

         if (!var4) {
            label1297: {
               try {
                  if (this.isLastSampleQueued) {
                     break label1297;
                  }
               } catch (Throwable var138) {
                  var10000 = var138;
                  var10001 = false;
                  break label1291;
               }

               try {
                  if (this.upstreamFormat == null) {
                     return -3;
                  }
               } catch (Throwable var137) {
                  var10000 = var137;
                  var10001 = false;
                  break label1291;
               }

               label1253:
               if (!var3) {
                  try {
                     if (this.upstreamFormat != var5) {
                        break label1253;
                     }
                  } catch (Throwable var136) {
                     var10000 = var136;
                     var10001 = false;
                     break label1291;
                  }

                  return -3;
               }

               try {
                  var1.format = this.upstreamFormat;
               } catch (Throwable var129) {
                  var10000 = var129;
                  var10001 = false;
                  break label1291;
               }

               return -5;
            }
         }

         try {
            var2.setFlags(4);
         } catch (Throwable var130) {
            var10000 = var130;
            var10001 = false;
            break label1291;
         }

         return -4;
      }

      Throwable var140 = var10000;
      throw var140;
   }

   public void reset(boolean var1) {
      this.length = 0;
      this.absoluteFirstIndex = 0;
      this.relativeFirstIndex = 0;
      this.readPosition = 0;
      this.upstreamKeyframeRequired = true;
      this.largestDiscardedTimestampUs = Long.MIN_VALUE;
      this.largestQueuedTimestampUs = Long.MIN_VALUE;
      this.isLastSampleQueued = false;
      if (var1) {
         this.upstreamFormat = null;
         this.upstreamFormatRequired = true;
      }

   }

   public void rewind() {
      synchronized(this){}

      try {
         this.readPosition = 0;
      } finally {
         ;
      }

   }

   public boolean setReadPosition(int var1) {
      synchronized(this){}

      try {
         if (this.absoluteFirstIndex > var1 || var1 > this.absoluteFirstIndex + this.length) {
            return false;
         }

         this.readPosition = var1 - this.absoluteFirstIndex;
      } finally {
         ;
      }

      return true;
   }

   public void sourceId(int var1) {
      this.upstreamSourceId = var1;
   }

   public static final class SampleExtrasHolder {
      public TrackOutput.CryptoData cryptoData;
      public long offset;
      public int size;
   }
}
