package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;

final class OggPageHeader {
   private static final int TYPE_OGGS = Util.getIntegerCodeForString("OggS");
   public int bodySize;
   public long granulePosition;
   public int headerSize;
   public final int[] laces = new int[255];
   public long pageChecksum;
   public int pageSegmentCount;
   public long pageSequenceNumber;
   public int revision;
   private final ParsableByteArray scratch = new ParsableByteArray(255);
   public long streamSerialNumber;
   public int type;

   public boolean populate(ExtractorInput var1, boolean var2) throws IOException, InterruptedException {
      this.scratch.reset();
      this.reset();
      long var3 = var1.getLength();
      byte var5 = 0;
      boolean var6;
      if (var3 != -1L && var1.getLength() - var1.getPeekPosition() < 27L) {
         var6 = false;
      } else {
         var6 = true;
      }

      if (var6 && var1.peekFully(this.scratch.data, 0, 27, true)) {
         if (this.scratch.readUnsignedInt() != (long)TYPE_OGGS) {
            if (var2) {
               return false;
            } else {
               throw new ParserException("expected OggS capture pattern at begin of page");
            }
         } else {
            this.revision = this.scratch.readUnsignedByte();
            if (this.revision != 0) {
               if (var2) {
                  return false;
               } else {
                  throw new ParserException("unsupported bit stream revision");
               }
            } else {
               this.type = this.scratch.readUnsignedByte();
               this.granulePosition = this.scratch.readLittleEndianLong();
               this.streamSerialNumber = this.scratch.readLittleEndianUnsignedInt();
               this.pageSequenceNumber = this.scratch.readLittleEndianUnsignedInt();
               this.pageChecksum = this.scratch.readLittleEndianUnsignedInt();
               this.pageSegmentCount = this.scratch.readUnsignedByte();
               this.headerSize = this.pageSegmentCount + 27;
               this.scratch.reset();
               var1.peekFully(this.scratch.data, 0, this.pageSegmentCount);

               for(int var7 = var5; var7 < this.pageSegmentCount; ++var7) {
                  this.laces[var7] = this.scratch.readUnsignedByte();
                  this.bodySize += this.laces[var7];
               }

               return true;
            }
         }
      } else if (var2) {
         return false;
      } else {
         throw new EOFException();
      }
   }

   public void reset() {
      this.revision = 0;
      this.type = 0;
      this.granulePosition = 0L;
      this.streamSerialNumber = 0L;
      this.pageSequenceNumber = 0L;
      this.pageChecksum = 0L;
      this.pageSegmentCount = 0;
      this.headerSize = 0;
      this.bodySize = 0;
   }
}
