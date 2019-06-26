package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class FlvExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final int FLV_TAG;
   private AudioTagPayloadReader audioReader;
   private int bytesToNextTagHeader;
   private ExtractorOutput extractorOutput;
   private final ParsableByteArray headerBuffer = new ParsableByteArray(9);
   private long mediaTagTimestampOffsetUs = -9223372036854775807L;
   private final ScriptTagPayloadReader metadataReader = new ScriptTagPayloadReader();
   private boolean outputSeekMap;
   private final ParsableByteArray scratch = new ParsableByteArray(4);
   private int state = 1;
   private final ParsableByteArray tagData = new ParsableByteArray();
   private int tagDataSize;
   private final ParsableByteArray tagHeaderBuffer = new ParsableByteArray(11);
   private long tagTimestampUs;
   private int tagType;
   private VideoTagPayloadReader videoReader;

   static {
      FACTORY = _$$Lambda$FlvExtractor$bd1zICO7f_FQot_hbozdu7LjVyE.INSTANCE;
      FLV_TAG = Util.getIntegerCodeForString("FLV");
   }

   private void ensureReadyForMediaOutput() {
      if (!this.outputSeekMap) {
         this.extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
         this.outputSeekMap = true;
      }

      if (this.mediaTagTimestampOffsetUs == -9223372036854775807L) {
         long var1;
         if (this.metadataReader.getDurationUs() == -9223372036854775807L) {
            var1 = -this.tagTimestampUs;
         } else {
            var1 = 0L;
         }

         this.mediaTagTimestampOffsetUs = var1;
      }

   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new FlvExtractor()};
   }

   private ParsableByteArray prepareTagData(ExtractorInput var1) throws IOException, InterruptedException {
      if (this.tagDataSize > this.tagData.capacity()) {
         ParsableByteArray var2 = this.tagData;
         var2.reset(new byte[Math.max(var2.capacity() * 2, this.tagDataSize)], 0);
      } else {
         this.tagData.setPosition(0);
      }

      this.tagData.setLimit(this.tagDataSize);
      var1.readFully(this.tagData.data, 0, this.tagDataSize);
      return this.tagData;
   }

   private boolean readFlvHeader(ExtractorInput var1) throws IOException, InterruptedException {
      byte[] var2 = this.headerBuffer.data;
      boolean var3 = false;
      if (!var1.readFully(var2, 0, 9, true)) {
         return false;
      } else {
         this.headerBuffer.setPosition(0);
         this.headerBuffer.skipBytes(4);
         int var4 = this.headerBuffer.readUnsignedByte();
         boolean var5;
         if ((var4 & 4) != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         if ((var4 & 1) != 0) {
            var3 = true;
         }

         if (var5 && this.audioReader == null) {
            this.audioReader = new AudioTagPayloadReader(this.extractorOutput.track(8, 1));
         }

         if (var3 && this.videoReader == null) {
            this.videoReader = new VideoTagPayloadReader(this.extractorOutput.track(9, 2));
         }

         this.extractorOutput.endTracks();
         this.bytesToNextTagHeader = this.headerBuffer.readInt() - 9 + 4;
         this.state = 2;
         return true;
      }
   }

   private boolean readTagData(ExtractorInput var1) throws IOException, InterruptedException {
      int var2 = this.tagType;
      boolean var3 = true;
      boolean var4;
      if (var2 == 8 && this.audioReader != null) {
         this.ensureReadyForMediaOutput();
         this.audioReader.consume(this.prepareTagData(var1), this.mediaTagTimestampOffsetUs + this.tagTimestampUs);
         var4 = var3;
      } else if (this.tagType == 9 && this.videoReader != null) {
         this.ensureReadyForMediaOutput();
         this.videoReader.consume(this.prepareTagData(var1), this.mediaTagTimestampOffsetUs + this.tagTimestampUs);
         var4 = var3;
      } else if (this.tagType == 18 && !this.outputSeekMap) {
         this.metadataReader.consume(this.prepareTagData(var1), this.tagTimestampUs);
         long var5 = this.metadataReader.getDurationUs();
         var4 = var3;
         if (var5 != -9223372036854775807L) {
            this.extractorOutput.seekMap(new SeekMap.Unseekable(var5));
            this.outputSeekMap = true;
            var4 = var3;
         }
      } else {
         var1.skipFully(this.tagDataSize);
         var4 = false;
      }

      this.bytesToNextTagHeader = 4;
      this.state = 2;
      return var4;
   }

   private boolean readTagHeader(ExtractorInput var1) throws IOException, InterruptedException {
      if (!var1.readFully(this.tagHeaderBuffer.data, 0, 11, true)) {
         return false;
      } else {
         this.tagHeaderBuffer.setPosition(0);
         this.tagType = this.tagHeaderBuffer.readUnsignedByte();
         this.tagDataSize = this.tagHeaderBuffer.readUnsignedInt24();
         this.tagTimestampUs = (long)this.tagHeaderBuffer.readUnsignedInt24();
         this.tagTimestampUs = ((long)(this.tagHeaderBuffer.readUnsignedByte() << 24) | this.tagTimestampUs) * 1000L;
         this.tagHeaderBuffer.skipBytes(3);
         this.state = 4;
         return true;
      }
   }

   private void skipToTagHeader(ExtractorInput var1) throws IOException, InterruptedException {
      var1.skipFully(this.bytesToNextTagHeader);
      this.bytesToNextTagHeader = 0;
      this.state = 3;
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      while(true) {
         int var3 = this.state;
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 == 4) {
                     if (!this.readTagData(var1)) {
                        continue;
                     }

                     return 0;
                  }

                  throw new IllegalStateException();
               } else if (!this.readTagHeader(var1)) {
                  return -1;
               }
            } else {
               this.skipToTagHeader(var1);
            }
         } else if (!this.readFlvHeader(var1)) {
            return -1;
         }
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.state = 1;
      this.mediaTagTimestampOffsetUs = -9223372036854775807L;
      this.bytesToNextTagHeader = 0;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      byte[] var2 = this.scratch.data;
      boolean var3 = false;
      var1.peekFully(var2, 0, 3);
      this.scratch.setPosition(0);
      if (this.scratch.readUnsignedInt24() != FLV_TAG) {
         return false;
      } else {
         var1.peekFully(this.scratch.data, 0, 2);
         this.scratch.setPosition(0);
         if ((this.scratch.readUnsignedShort() & 250) != 0) {
            return false;
         } else {
            var1.peekFully(this.scratch.data, 0, 4);
            this.scratch.setPosition(0);
            int var4 = this.scratch.readInt();
            var1.resetPeekPosition();
            var1.advancePeekPosition(var4);
            var1.peekFully(this.scratch.data, 0, 4);
            this.scratch.setPosition(0);
            if (this.scratch.readInt() == 0) {
               var3 = true;
            }

            return var3;
         }
      }
   }
}
