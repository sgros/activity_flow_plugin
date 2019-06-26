package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.upstream.Allocation;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SampleQueue implements TrackOutput {
   private final int allocationLength;
   private final Allocator allocator;
   private Format downstreamFormat;
   private final SampleMetadataQueue.SampleExtrasHolder extrasHolder;
   private SampleQueue.AllocationNode firstAllocationNode;
   private Format lastUnadjustedFormat;
   private final SampleMetadataQueue metadataQueue;
   private boolean pendingFormatAdjustment;
   private boolean pendingSplice;
   private SampleQueue.AllocationNode readAllocationNode;
   private long sampleOffsetUs;
   private final ParsableByteArray scratch;
   private long totalBytesWritten;
   private SampleQueue.UpstreamFormatChangedListener upstreamFormatChangeListener;
   private SampleQueue.AllocationNode writeAllocationNode;

   public SampleQueue(Allocator var1) {
      this.allocator = var1;
      this.allocationLength = var1.getIndividualAllocationLength();
      this.metadataQueue = new SampleMetadataQueue();
      this.extrasHolder = new SampleMetadataQueue.SampleExtrasHolder();
      this.scratch = new ParsableByteArray(32);
      this.firstAllocationNode = new SampleQueue.AllocationNode(0L, this.allocationLength);
      SampleQueue.AllocationNode var2 = this.firstAllocationNode;
      this.readAllocationNode = var2;
      this.writeAllocationNode = var2;
   }

   private void advanceReadTo(long var1) {
      while(true) {
         SampleQueue.AllocationNode var3 = this.readAllocationNode;
         if (var1 < var3.endPosition) {
            return;
         }

         this.readAllocationNode = var3.next;
      }
   }

   private void clearAllocationNodes(SampleQueue.AllocationNode var1) {
      if (var1.wasInitialized) {
         SampleQueue.AllocationNode var2 = this.writeAllocationNode;
         Allocation[] var4 = new Allocation[var2.wasInitialized + (int)(var2.startPosition - var1.startPosition) / this.allocationLength];

         for(int var3 = 0; var3 < var4.length; ++var3) {
            var4[var3] = var1.allocation;
            var1 = var1.clear();
         }

         this.allocator.release(var4);
      }
   }

   private void discardDownstreamTo(long var1) {
      if (var1 != -1L) {
         while(true) {
            SampleQueue.AllocationNode var3 = this.firstAllocationNode;
            if (var1 < var3.endPosition) {
               if (this.readAllocationNode.startPosition < var3.startPosition) {
                  this.readAllocationNode = var3;
               }

               return;
            }

            this.allocator.release(var3.allocation);
            this.firstAllocationNode = this.firstAllocationNode.clear();
         }
      }
   }

   private static Format getAdjustedSampleFormat(Format var0, long var1) {
      if (var0 == null) {
         return null;
      } else {
         Format var3 = var0;
         if (var1 != 0L) {
            long var4 = var0.subsampleOffsetUs;
            var3 = var0;
            if (var4 != Long.MAX_VALUE) {
               var3 = var0.copyWithSubsampleOffsetUs(var4 + var1);
            }
         }

         return var3;
      }
   }

   private void postAppend(int var1) {
      this.totalBytesWritten += (long)var1;
      long var2 = this.totalBytesWritten;
      SampleQueue.AllocationNode var4 = this.writeAllocationNode;
      if (var2 == var4.endPosition) {
         this.writeAllocationNode = var4.next;
      }

   }

   private int preAppend(int var1) {
      SampleQueue.AllocationNode var2 = this.writeAllocationNode;
      if (!var2.wasInitialized) {
         var2.initialize(this.allocator.allocate(), new SampleQueue.AllocationNode(this.writeAllocationNode.endPosition, this.allocationLength));
      }

      return Math.min(var1, (int)(this.writeAllocationNode.endPosition - this.totalBytesWritten));
   }

   private void readData(long var1, ByteBuffer var3, int var4) {
      this.advanceReadTo(var1);

      while(var4 > 0) {
         int var5 = Math.min(var4, (int)(this.readAllocationNode.endPosition - var1));
         SampleQueue.AllocationNode var6 = this.readAllocationNode;
         var3.put(var6.allocation.data, var6.translateOffset(var1), var5);
         int var7 = var4 - var5;
         long var8 = var1 + (long)var5;
         var6 = this.readAllocationNode;
         var1 = var8;
         var4 = var7;
         if (var8 == var6.endPosition) {
            this.readAllocationNode = var6.next;
            var1 = var8;
            var4 = var7;
         }
      }

   }

   private void readData(long var1, byte[] var3, int var4) {
      this.advanceReadTo(var1);
      int var5 = var4;

      while(var5 > 0) {
         int var6 = Math.min(var5, (int)(this.readAllocationNode.endPosition - var1));
         SampleQueue.AllocationNode var7 = this.readAllocationNode;
         System.arraycopy(var7.allocation.data, var7.translateOffset(var1), var3, var4 - var5, var6);
         int var8 = var5 - var6;
         long var9 = var1 + (long)var6;
         var7 = this.readAllocationNode;
         var1 = var9;
         var5 = var8;
         if (var9 == var7.endPosition) {
            this.readAllocationNode = var7.next;
            var1 = var9;
            var5 = var8;
         }
      }

   }

   private void readEncryptionData(DecoderInputBuffer var1, SampleMetadataQueue.SampleExtrasHolder var2) {
      long var3 = var2.offset;
      this.scratch.reset(1);
      this.readData(var3, (byte[])this.scratch.data, 1);
      ++var3;
      byte[] var5 = this.scratch.data;
      byte var6 = 0;
      byte var7 = var5[0];
      boolean var8;
      if ((var7 & 128) != 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var16 = var7 & 127;
      CryptoInfo var14 = var1.cryptoInfo;
      if (var14.iv == null) {
         var14.iv = new byte[16];
      }

      this.readData(var3, var1.cryptoInfo.iv, var16);
      var3 += (long)var16;
      if (var8) {
         this.scratch.reset(2);
         this.readData(var3, (byte[])this.scratch.data, 2);
         var3 += 2L;
         var16 = this.scratch.readUnsignedShort();
      } else {
         var16 = 1;
      }

      int[] var9;
      int[] var15;
      label41: {
         var9 = var1.cryptoInfo.numBytesOfClearData;
         if (var9 != null) {
            var15 = var9;
            if (var9.length >= var16) {
               break label41;
            }
         }

         var15 = new int[var16];
      }

      label36: {
         int[] var10 = var1.cryptoInfo.numBytesOfEncryptedData;
         if (var10 != null) {
            var9 = var10;
            if (var10.length >= var16) {
               break label36;
            }
         }

         var9 = new int[var16];
      }

      long var11;
      int var17;
      if (var8) {
         var17 = var16 * 6;
         this.scratch.reset(var17);
         this.readData(var3, this.scratch.data, var17);
         var11 = var3 + (long)var17;
         this.scratch.setPosition(0);
         var17 = var6;

         while(true) {
            var3 = var11;
            if (var17 >= var16) {
               break;
            }

            var15[var17] = this.scratch.readUnsignedShort();
            var9[var17] = this.scratch.readUnsignedIntToInt();
            ++var17;
         }
      } else {
         var15[0] = 0;
         var9[0] = var2.size - (int)(var3 - var2.offset);
      }

      TrackOutput.CryptoData var18 = var2.cryptoData;
      CryptoInfo var13 = var1.cryptoInfo;
      var13.set(var16, var15, var9, var18.encryptionKey, var13.iv, var18.cryptoMode, var18.encryptedBlocks, var18.clearBlocks);
      var11 = var2.offset;
      var17 = (int)(var3 - var11);
      var2.offset = var11 + (long)var17;
      var2.size -= var17;
   }

   public int advanceTo(long var1, boolean var3, boolean var4) {
      return this.metadataQueue.advanceTo(var1, var3, var4);
   }

   public int advanceToEnd() {
      return this.metadataQueue.advanceToEnd();
   }

   public void discardTo(long var1, boolean var3, boolean var4) {
      this.discardDownstreamTo(this.metadataQueue.discardTo(var1, var3, var4));
   }

   public void discardToEnd() {
      this.discardDownstreamTo(this.metadataQueue.discardToEnd());
   }

   public void discardToRead() {
      this.discardDownstreamTo(this.metadataQueue.discardToRead());
   }

   public void discardUpstreamSamples(int var1) {
      this.totalBytesWritten = this.metadataQueue.discardUpstreamSamples(var1);
      long var2 = this.totalBytesWritten;
      SampleQueue.AllocationNode var5;
      if (var2 != 0L) {
         SampleQueue.AllocationNode var4 = this.firstAllocationNode;
         var5 = var4;
         if (var2 != var4.startPosition) {
            while(this.totalBytesWritten > var5.endPosition) {
               var5 = var5.next;
            }

            SampleQueue.AllocationNode var6 = var5.next;
            this.clearAllocationNodes(var6);
            var5.next = new SampleQueue.AllocationNode(var5.endPosition, this.allocationLength);
            if (this.totalBytesWritten == var5.endPosition) {
               var4 = var5.next;
            } else {
               var4 = var5;
            }

            this.writeAllocationNode = var4;
            if (this.readAllocationNode == var6) {
               this.readAllocationNode = var5.next;
            }

            return;
         }
      }

      this.clearAllocationNodes(this.firstAllocationNode);
      this.firstAllocationNode = new SampleQueue.AllocationNode(this.totalBytesWritten, this.allocationLength);
      var5 = this.firstAllocationNode;
      this.readAllocationNode = var5;
      this.writeAllocationNode = var5;
   }

   public void format(Format var1) {
      Format var2 = getAdjustedSampleFormat(var1, this.sampleOffsetUs);
      boolean var3 = this.metadataQueue.format(var2);
      this.lastUnadjustedFormat = var1;
      this.pendingFormatAdjustment = false;
      SampleQueue.UpstreamFormatChangedListener var4 = this.upstreamFormatChangeListener;
      if (var4 != null && var3) {
         var4.onUpstreamFormatChanged(var2);
      }

   }

   public int getFirstIndex() {
      return this.metadataQueue.getFirstIndex();
   }

   public long getFirstTimestampUs() {
      return this.metadataQueue.getFirstTimestampUs();
   }

   public long getLargestQueuedTimestampUs() {
      return this.metadataQueue.getLargestQueuedTimestampUs();
   }

   public int getReadIndex() {
      return this.metadataQueue.getReadIndex();
   }

   public Format getUpstreamFormat() {
      return this.metadataQueue.getUpstreamFormat();
   }

   public int getWriteIndex() {
      return this.metadataQueue.getWriteIndex();
   }

   public boolean hasNextSample() {
      return this.metadataQueue.hasNextSample();
   }

   public boolean isLastSampleQueued() {
      return this.metadataQueue.isLastSampleQueued();
   }

   public int peekSourceId() {
      return this.metadataQueue.peekSourceId();
   }

   public int read(FormatHolder var1, DecoderInputBuffer var2, boolean var3, boolean var4, long var5) {
      int var7 = this.metadataQueue.read(var1, var2, var3, var4, this.downstreamFormat, this.extrasHolder);
      if (var7 != -5) {
         if (var7 != -4) {
            if (var7 == -3) {
               return -3;
            } else {
               throw new IllegalStateException();
            }
         } else {
            if (!var2.isEndOfStream()) {
               if (var2.timeUs < var5) {
                  var2.addFlag(Integer.MIN_VALUE);
               }

               if (var2.isEncrypted()) {
                  this.readEncryptionData(var2, this.extrasHolder);
               }

               var2.ensureSpaceForWrite(this.extrasHolder.size);
               SampleMetadataQueue.SampleExtrasHolder var8 = this.extrasHolder;
               this.readData(var8.offset, var2.data, var8.size);
            }

            return -4;
         }
      } else {
         this.downstreamFormat = var1.format;
         return -5;
      }
   }

   public void reset() {
      this.reset(false);
   }

   public void reset(boolean var1) {
      this.metadataQueue.reset(var1);
      this.clearAllocationNodes(this.firstAllocationNode);
      this.firstAllocationNode = new SampleQueue.AllocationNode(0L, this.allocationLength);
      SampleQueue.AllocationNode var2 = this.firstAllocationNode;
      this.readAllocationNode = var2;
      this.writeAllocationNode = var2;
      this.totalBytesWritten = 0L;
      this.allocator.trim();
   }

   public void rewind() {
      this.metadataQueue.rewind();
      this.readAllocationNode = this.firstAllocationNode;
   }

   public int sampleData(ExtractorInput var1, int var2, boolean var3) throws IOException, InterruptedException {
      var2 = this.preAppend(var2);
      SampleQueue.AllocationNode var4 = this.writeAllocationNode;
      var2 = var1.read(var4.allocation.data, var4.translateOffset(this.totalBytesWritten), var2);
      if (var2 == -1) {
         if (var3) {
            return -1;
         } else {
            throw new EOFException();
         }
      } else {
         this.postAppend(var2);
         return var2;
      }
   }

   public void sampleData(ParsableByteArray var1, int var2) {
      while(var2 > 0) {
         int var3 = this.preAppend(var2);
         SampleQueue.AllocationNode var4 = this.writeAllocationNode;
         var1.readBytes(var4.allocation.data, var4.translateOffset(this.totalBytesWritten), var3);
         var2 -= var3;
         this.postAppend(var3);
      }

   }

   public void sampleMetadata(long var1, int var3, int var4, int var5, TrackOutput.CryptoData var6) {
      if (this.pendingFormatAdjustment) {
         this.format(this.lastUnadjustedFormat);
      }

      long var7 = var1 + this.sampleOffsetUs;
      if (this.pendingSplice) {
         if ((var3 & 1) == 0 || !this.metadataQueue.attemptSplice(var7)) {
            return;
         }

         this.pendingSplice = false;
      }

      var1 = this.totalBytesWritten;
      long var9 = (long)var4;
      long var11 = (long)var5;
      this.metadataQueue.commitSample(var7, var3, var1 - var9 - var11, var4, var6);
   }

   public boolean setReadPosition(int var1) {
      return this.metadataQueue.setReadPosition(var1);
   }

   public void setSampleOffsetUs(long var1) {
      if (this.sampleOffsetUs != var1) {
         this.sampleOffsetUs = var1;
         this.pendingFormatAdjustment = true;
      }

   }

   public void setUpstreamFormatChangeListener(SampleQueue.UpstreamFormatChangedListener var1) {
      this.upstreamFormatChangeListener = var1;
   }

   public void sourceId(int var1) {
      this.metadataQueue.sourceId(var1);
   }

   public void splice() {
      this.pendingSplice = true;
   }

   private static final class AllocationNode {
      public Allocation allocation;
      public final long endPosition;
      public SampleQueue.AllocationNode next;
      public final long startPosition;
      public boolean wasInitialized;

      public AllocationNode(long var1, int var3) {
         this.startPosition = var1;
         this.endPosition = var1 + (long)var3;
      }

      public SampleQueue.AllocationNode clear() {
         this.allocation = null;
         SampleQueue.AllocationNode var1 = this.next;
         this.next = null;
         return var1;
      }

      public void initialize(Allocation var1, SampleQueue.AllocationNode var2) {
         this.allocation = var1;
         this.next = var2;
         this.wasInitialized = true;
      }

      public int translateOffset(long var1) {
         return (int)(var1 - this.startPosition) + this.allocation.offset;
      }
   }

   public interface UpstreamFormatChangedListener {
      void onUpstreamFormatChanged(Format var1);
   }
}
