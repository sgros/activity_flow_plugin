package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Assertions;
import java.io.EOFException;
import java.io.IOException;

final class DefaultOggSeeker implements OggSeeker {
   private long end;
   private long endGranule;
   private final long endPosition;
   private final OggPageHeader pageHeader = new OggPageHeader();
   private long positionBeforeSeekToEnd;
   private long start;
   private long startGranule;
   private final long startPosition;
   private int state;
   private final StreamReader streamReader;
   private long targetGranule;
   private long totalGranules;

   public DefaultOggSeeker(long var1, long var3, StreamReader var5, long var6, long var8, boolean var10) {
      boolean var11;
      if (var1 >= 0L && var3 > var1) {
         var11 = true;
      } else {
         var11 = false;
      }

      Assertions.checkArgument(var11);
      this.streamReader = var5;
      this.startPosition = var1;
      this.endPosition = var3;
      if (var6 != var3 - var1 && !var10) {
         this.state = 0;
      } else {
         this.totalGranules = var8;
         this.state = 3;
      }

   }

   private long getEstimatedPosition(long var1, long var3, long var5) {
      long var7 = this.endPosition;
      long var9 = this.startPosition;
      var3 = var1 + (var3 * (var7 - var9) / this.totalGranules - var5);
      var1 = var3;
      if (var3 < var9) {
         var1 = var9;
      }

      var5 = this.endPosition;
      var3 = var1;
      if (var1 >= var5) {
         var3 = var5 - 1L;
      }

      return var3;
   }

   public DefaultOggSeeker.OggSeekMap createSeekMap() {
      DefaultOggSeeker.OggSeekMap var1;
      if (this.totalGranules != 0L) {
         var1 = new DefaultOggSeeker.OggSeekMap();
      } else {
         var1 = null;
      }

      return var1;
   }

   public long getNextSeekPosition(long var1, ExtractorInput var3) throws IOException, InterruptedException {
      long var4 = this.start;
      long var6 = this.end;
      long var8 = 2L;
      if (var4 == var6) {
         return -(this.startGranule + 2L);
      } else {
         var6 = var3.getPosition();
         if (!this.skipToNextPage(var3, this.end)) {
            var1 = this.start;
            if (var1 != var6) {
               return var1;
            } else {
               throw new IOException("No ogg page can be found.");
            }
         } else {
            this.pageHeader.populate(var3, false);
            var3.resetPeekPosition();
            OggPageHeader var10 = this.pageHeader;
            var4 = var1 - var10.granulePosition;
            int var11 = var10.headerSize + var10.bodySize;
            if (var4 >= 0L && var4 <= 72000L) {
               var3.skipFully(var11);
               return -(this.pageHeader.granulePosition + 2L);
            } else {
               if (var4 < 0L) {
                  this.end = var6;
                  this.endGranule = this.pageHeader.granulePosition;
               } else {
                  var6 = var3.getPosition();
                  var1 = (long)var11;
                  this.start = var6 + var1;
                  this.startGranule = this.pageHeader.granulePosition;
                  if (this.end - this.start + var1 < 100000L) {
                     var3.skipFully(var11);
                     return -(this.startGranule + 2L);
                  }
               }

               var6 = this.end;
               var1 = this.start;
               if (var6 - var1 < 100000L) {
                  this.end = var1;
                  return var1;
               } else {
                  var6 = (long)var11;
                  if (var4 <= 0L) {
                     var1 = var8;
                  } else {
                     var1 = 1L;
                  }

                  var8 = var3.getPosition();
                  long var12 = this.end;
                  long var14 = this.start;
                  return Math.min(Math.max(var8 - var6 * var1 + var4 * (var12 - var14) / (this.endGranule - this.startGranule), var14), this.end - 1L);
               }
            }
         }
      }
   }

   public long read(ExtractorInput var1) throws IOException, InterruptedException {
      int var2 = this.state;
      long var5;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               if (var2 == 3) {
                  return -1L;
               }

               throw new IllegalStateException();
            }

            long var3 = this.targetGranule;
            var5 = 0L;
            if (var3 != 0L) {
               var5 = this.getNextSeekPosition(var3, var1);
               if (var5 >= 0L) {
                  return var5;
               }

               var5 = this.skipToPageOfGranule(var1, this.targetGranule, -(var5 + 2L));
            }

            this.state = 3;
            return -(var5 + 2L);
         }
      } else {
         this.positionBeforeSeekToEnd = var1.getPosition();
         this.state = 1;
         var5 = this.endPosition - 65307L;
         if (var5 > this.positionBeforeSeekToEnd) {
            return var5;
         }
      }

      this.totalGranules = this.readGranuleOfLastPage(var1);
      this.state = 3;
      return this.positionBeforeSeekToEnd;
   }

   long readGranuleOfLastPage(ExtractorInput var1) throws IOException, InterruptedException {
      this.skipToNextPage(var1);
      this.pageHeader.reset();

      while((this.pageHeader.type & 4) != 4 && var1.getPosition() < this.endPosition) {
         this.pageHeader.populate(var1, false);
         OggPageHeader var2 = this.pageHeader;
         var1.skipFully(var2.headerSize + var2.bodySize);
      }

      return this.pageHeader.granulePosition;
   }

   public void resetSeeking() {
      this.start = this.startPosition;
      this.end = this.endPosition;
      this.startGranule = 0L;
      this.endGranule = this.totalGranules;
   }

   void skipToNextPage(ExtractorInput var1) throws IOException, InterruptedException {
      if (!this.skipToNextPage(var1, this.endPosition)) {
         throw new EOFException();
      }
   }

   boolean skipToNextPage(ExtractorInput var1, long var2) throws IOException, InterruptedException {
      long var4 = Math.min(var2 + 3L, this.endPosition);
      byte[] var6 = new byte[2048];
      int var7 = var6.length;

      while(true) {
         var2 = var1.getPosition();
         long var8 = (long)var7;
         int var10 = 0;
         if (var2 + var8 > var4) {
            var7 = (int)(var4 - var1.getPosition());
            if (var7 < 4) {
               return false;
            }
         }

         var1.peekFully(var6, 0, var7, false);

         while(true) {
            int var11 = var7 - 3;
            if (var10 >= var11) {
               var1.skipFully(var11);
               break;
            }

            if (var6[var10] == 79 && var6[var10 + 1] == 103 && var6[var10 + 2] == 103 && var6[var10 + 3] == 83) {
               var1.skipFully(var10);
               return true;
            }

            ++var10;
         }
      }
   }

   long skipToPageOfGranule(ExtractorInput var1, long var2, long var4) throws IOException, InterruptedException {
      this.pageHeader.populate(var1, false);

      while(true) {
         OggPageHeader var6 = this.pageHeader;
         if (var6.granulePosition >= var2) {
            var1.resetPeekPosition();
            return var4;
         }

         var1.skipFully(var6.headerSize + var6.bodySize);
         var6 = this.pageHeader;
         var4 = var6.granulePosition;
         var6.populate(var1, false);
      }
   }

   public long startSeek(long var1) {
      int var3 = this.state;
      boolean var4;
      if (var3 != 3 && var3 != 2) {
         var4 = false;
      } else {
         var4 = true;
      }

      Assertions.checkArgument(var4);
      long var5 = 0L;
      if (var1 == 0L) {
         var1 = var5;
      } else {
         var1 = this.streamReader.convertTimeToGranule(var1);
      }

      this.targetGranule = var1;
      this.state = 2;
      this.resetSeeking();
      return this.targetGranule;
   }

   private class OggSeekMap implements SeekMap {
      private OggSeekMap() {
      }

      // $FF: synthetic method
      OggSeekMap(Object var2) {
         this();
      }

      public long getDurationUs() {
         return DefaultOggSeeker.this.streamReader.convertGranuleToTime(DefaultOggSeeker.this.totalGranules);
      }

      public SeekMap.SeekPoints getSeekPoints(long var1) {
         if (var1 == 0L) {
            return new SeekMap.SeekPoints(new SeekPoint(0L, DefaultOggSeeker.this.startPosition));
         } else {
            long var3 = DefaultOggSeeker.this.streamReader.convertTimeToGranule(var1);
            DefaultOggSeeker var5 = DefaultOggSeeker.this;
            return new SeekMap.SeekPoints(new SeekPoint(var1, var5.getEstimatedPosition(var5.startPosition, var3, 30000L)));
         }
      }

      public boolean isSeekable() {
         return true;
      }
   }
}
