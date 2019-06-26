package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public final class Mp4Extractor implements Extractor, SeekMap {
   private static final int BRAND_QUICKTIME;
   public static final ExtractorsFactory FACTORY;
   private long[][] accumulatedSampleSizes;
   private ParsableByteArray atomData;
   private final ParsableByteArray atomHeader;
   private int atomHeaderBytesRead;
   private long atomSize;
   private int atomType;
   private final ArrayDeque containerAtoms;
   private long durationUs;
   private ExtractorOutput extractorOutput;
   private int firstVideoTrackIndex;
   private final int flags;
   private boolean isQuickTime;
   private final ParsableByteArray nalLength;
   private final ParsableByteArray nalStartCode;
   private int parserState;
   private int sampleBytesWritten;
   private int sampleCurrentNalBytesRemaining;
   private int sampleTrackIndex;
   private Mp4Extractor.Mp4Track[] tracks;

   static {
      FACTORY = _$$Lambda$Mp4Extractor$quy71uYOGsneho91FZy1d2UGE1Q.INSTANCE;
      BRAND_QUICKTIME = Util.getIntegerCodeForString("qt  ");
   }

   public Mp4Extractor() {
      this(0);
   }

   public Mp4Extractor(int var1) {
      this.flags = var1;
      this.atomHeader = new ParsableByteArray(16);
      this.containerAtoms = new ArrayDeque();
      this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
      this.nalLength = new ParsableByteArray(4);
      this.sampleTrackIndex = -1;
   }

   private static long[][] calculateAccumulatedSampleSizes(Mp4Extractor.Mp4Track[] var0) {
      long[][] var1 = new long[var0.length][];
      int[] var2 = new int[var0.length];
      long[] var3 = new long[var0.length];
      boolean[] var4 = new boolean[var0.length];

      int var5;
      for(var5 = 0; var5 < var0.length; ++var5) {
         var1[var5] = new long[var0[var5].sampleTable.sampleCount];
         var3[var5] = var0[var5].sampleTable.timestampsUs[0];
      }

      long var6 = 0L;
      int var8 = 0;

      while(var8 < var0.length) {
         int var9 = -1;
         long var10 = Long.MAX_VALUE;

         long var13;
         for(var5 = 0; var5 < var0.length; var10 = var13) {
            int var12 = var9;
            var13 = var10;
            if (!var4[var5]) {
               var12 = var9;
               var13 = var10;
               if (var3[var5] <= var10) {
                  var13 = var3[var5];
                  var12 = var5;
               }
            }

            ++var5;
            var9 = var12;
         }

         var5 = var2[var9];
         var1[var9][var5] = var6;
         var6 += (long)var0[var9].sampleTable.sizes[var5];
         ++var5;
         var2[var9] = var5;
         if (var5 < var1[var9].length) {
            var3[var9] = var0[var9].sampleTable.timestampsUs[var5];
         } else {
            var4[var9] = true;
            ++var8;
         }
      }

      return var1;
   }

   private void enterReadingAtomHeaderState() {
      this.parserState = 0;
      this.atomHeaderBytesRead = 0;
   }

   private static int getSynchronizationSampleIndex(TrackSampleTable var0, long var1) {
      int var3 = var0.getIndexOfEarlierOrEqualSynchronizationSample(var1);
      int var4 = var3;
      if (var3 == -1) {
         var4 = var0.getIndexOfLaterOrEqualSynchronizationSample(var1);
      }

      return var4;
   }

   private int getTrackIndexOfNextReadSample(long var1) {
      int var3 = 0;
      long var4 = Long.MAX_VALUE;
      boolean var6 = true;
      long var7 = Long.MAX_VALUE;
      int var9 = -1;
      int var10 = -1;
      boolean var11 = true;
      long var12 = Long.MAX_VALUE;

      while(true) {
         Mp4Extractor.Mp4Track[] var14 = this.tracks;
         if (var3 >= var14.length) {
            if (var4 == Long.MAX_VALUE || !var6 || var7 < var4 + 1048576L) {
               var9 = var10;
            }

            return var9;
         }

         Mp4Extractor.Mp4Track var26 = var14[var3];
         int var15 = var26.sampleIndex;
         TrackSampleTable var27 = var26.sampleTable;
         long var16;
         if (var15 == var27.sampleCount) {
            var16 = var4;
         } else {
            long var18 = var27.offsets[var15];
            long var20 = this.accumulatedSampleSizes[var3][var15];
            var16 = var18 - var1;
            boolean var28;
            if (var16 >= 0L && var16 < 262144L) {
               var28 = false;
            } else {
               var28 = true;
            }

            long var22;
            int var24;
            boolean var25;
            label62: {
               if (var28 || !var11) {
                  var22 = var7;
                  var24 = var10;
                  var25 = var11;
                  var18 = var12;
                  if (var28 != var11) {
                     break label62;
                  }

                  var22 = var7;
                  var24 = var10;
                  var25 = var11;
                  var18 = var12;
                  if (var16 >= var12) {
                     break label62;
                  }
               }

               var25 = var28;
               var24 = var3;
               var18 = var16;
               var22 = var20;
            }

            var16 = var4;
            var7 = var22;
            var10 = var24;
            var11 = var25;
            var12 = var18;
            if (var20 < var4) {
               var9 = var3;
               var12 = var18;
               var11 = var25;
               var10 = var24;
               var7 = var22;
               var6 = var28;
               var16 = var20;
            }
         }

         ++var3;
         var4 = var16;
      }
   }

   private ArrayList getTrackSampleTables(Atom.ContainerAtom var1, GaplessInfoHolder var2, boolean var3) throws ParserException {
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var1.containerChildren.size(); ++var5) {
         Atom.ContainerAtom var6 = (Atom.ContainerAtom)var1.containerChildren.get(var5);
         if (var6.type == Atom.TYPE_trak) {
            Track var7 = AtomParsers.parseTrak(var6, var1.getLeafAtomOfType(Atom.TYPE_mvhd), -9223372036854775807L, (DrmInitData)null, var3, this.isQuickTime);
            if (var7 != null) {
               TrackSampleTable var8 = AtomParsers.parseStbl(var7, var6.getContainerAtomOfType(Atom.TYPE_mdia).getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl), var2);
               if (var8.sampleCount != 0) {
                  var4.add(var8);
               }
            }
         }
      }

      return var4;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new Mp4Extractor()};
   }

   private static long maybeAdjustSeekOffset(TrackSampleTable var0, long var1, long var3) {
      int var5 = getSynchronizationSampleIndex(var0, var1);
      return var5 == -1 ? var3 : Math.min(var0.offsets[var5], var3);
   }

   private void processAtomEnded(long var1) throws ParserException {
      while(!this.containerAtoms.isEmpty() && ((Atom.ContainerAtom)this.containerAtoms.peek()).endPosition == var1) {
         Atom.ContainerAtom var3 = (Atom.ContainerAtom)this.containerAtoms.pop();
         if (var3.type == Atom.TYPE_moov) {
            this.processMoovAtom(var3);
            this.containerAtoms.clear();
            this.parserState = 2;
         } else if (!this.containerAtoms.isEmpty()) {
            ((Atom.ContainerAtom)this.containerAtoms.peek()).add(var3);
         }
      }

      if (this.parserState != 2) {
         this.enterReadingAtomHeaderState();
      }

   }

   private static boolean processFtypAtom(ParsableByteArray var0) {
      var0.setPosition(8);
      if (var0.readInt() == BRAND_QUICKTIME) {
         return true;
      } else {
         var0.skipBytes(4);

         do {
            if (var0.bytesLeft() <= 0) {
               return false;
            }
         } while(var0.readInt() != BRAND_QUICKTIME);

         return true;
      }
   }

   private void processMoovAtom(Atom.ContainerAtom var1) throws ParserException {
      ArrayList var2 = new ArrayList();
      GaplessInfoHolder var3 = new GaplessInfoHolder();
      Atom.LeafAtom var4 = var1.getLeafAtomOfType(Atom.TYPE_udta);
      Metadata var5 = null;
      Metadata var20;
      if (var4 != null) {
         Metadata var6 = AtomParsers.parseUdta(var4, this.isQuickTime);
         var20 = var6;
         if (var6 != null) {
            var3.setFromMetadata(var6);
            var20 = var6;
         }
      } else {
         var20 = null;
      }

      Atom.ContainerAtom var21 = var1.getContainerAtomOfType(Atom.TYPE_meta);
      if (var21 != null) {
         var5 = AtomParsers.parseMdtaFromMeta(var21);
      }

      int var7 = this.flags;
      boolean var8 = true;
      if ((var7 & 1) == 0) {
         var8 = false;
      }

      ArrayList var9 = this.getTrackSampleTables(var1, var3, var8);
      int var10 = var9.size();
      int var11 = 0;
      var7 = -1;

      long var12;
      int var15;
      for(var12 = -9223372036854775807L; var11 < var10; var7 = var15) {
         TrackSampleTable var19 = (TrackSampleTable)var9.get(var11);
         Track var14 = var19.track;
         Mp4Extractor.Mp4Track var22 = new Mp4Extractor.Mp4Track(var14, var19, this.extractorOutput.track(var11, var14.type));
         var15 = var19.maximumSize;
         Format var16 = var14.format.copyWithMaxInputSize(var15 + 30);
         var16 = MetadataUtil.getFormatWithMetadata(var14.type, var16, var20, var5, var3);
         var22.trackOutput.format(var16);
         long var17 = var14.durationUs;
         if (var17 == -9223372036854775807L) {
            var17 = var19.durationUs;
         }

         var12 = Math.max(var12, var17);
         if (var14.type == 2) {
            var15 = var7;
            if (var7 == -1) {
               var15 = var2.size();
            }
         } else {
            var15 = var7;
         }

         var2.add(var22);
         ++var11;
      }

      this.firstVideoTrackIndex = var7;
      this.durationUs = var12;
      this.tracks = (Mp4Extractor.Mp4Track[])var2.toArray(new Mp4Extractor.Mp4Track[0]);
      this.accumulatedSampleSizes = calculateAccumulatedSampleSizes(this.tracks);
      this.extractorOutput.endTracks();
      this.extractorOutput.seekMap(this);
   }

   private boolean readAtomHeader(ExtractorInput var1) throws IOException, InterruptedException {
      if (this.atomHeaderBytesRead == 0) {
         if (!var1.readFully(this.atomHeader.data, 0, 8, true)) {
            return false;
         }

         this.atomHeaderBytesRead = 8;
         this.atomHeader.setPosition(0);
         this.atomSize = this.atomHeader.readUnsignedInt();
         this.atomType = this.atomHeader.readInt();
      }

      long var2 = this.atomSize;
      if (var2 == 1L) {
         var1.readFully(this.atomHeader.data, 8, 8);
         this.atomHeaderBytesRead += 8;
         this.atomSize = this.atomHeader.readUnsignedLongToLong();
      } else if (var2 == 0L) {
         long var4 = var1.getLength();
         var2 = var4;
         if (var4 == -1L) {
            var2 = var4;
            if (!this.containerAtoms.isEmpty()) {
               var2 = ((Atom.ContainerAtom)this.containerAtoms.peek()).endPosition;
            }
         }

         if (var2 != -1L) {
            this.atomSize = var2 - var1.getPosition() + (long)this.atomHeaderBytesRead;
         }
      }

      if (this.atomSize >= (long)this.atomHeaderBytesRead) {
         if (shouldParseContainerAtom(this.atomType)) {
            var2 = var1.getPosition() + this.atomSize - (long)this.atomHeaderBytesRead;
            this.containerAtoms.push(new Atom.ContainerAtom(this.atomType, var2));
            if (this.atomSize == (long)this.atomHeaderBytesRead) {
               this.processAtomEnded(var2);
            } else {
               this.enterReadingAtomHeaderState();
            }
         } else if (shouldParseLeafAtom(this.atomType)) {
            boolean var6;
            if (this.atomHeaderBytesRead == 8) {
               var6 = true;
            } else {
               var6 = false;
            }

            Assertions.checkState(var6);
            if (this.atomSize <= 2147483647L) {
               var6 = true;
            } else {
               var6 = false;
            }

            Assertions.checkState(var6);
            this.atomData = new ParsableByteArray((int)this.atomSize);
            System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
            this.parserState = 1;
         } else {
            this.atomData = null;
            this.parserState = 1;
         }

         return true;
      } else {
         throw new ParserException("Atom size less than header length (unsupported).");
      }
   }

   private boolean readAtomPayload(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3;
      long var5;
      boolean var8;
      boolean var9;
      label29: {
         var3 = this.atomSize - (long)this.atomHeaderBytesRead;
         var5 = var1.getPosition();
         ParsableByteArray var7 = this.atomData;
         var8 = true;
         if (var7 != null) {
            var1.readFully(var7.data, this.atomHeaderBytesRead, (int)var3);
            if (this.atomType == Atom.TYPE_ftyp) {
               this.isQuickTime = processFtypAtom(this.atomData);
            } else if (!this.containerAtoms.isEmpty()) {
               ((Atom.ContainerAtom)this.containerAtoms.peek()).add(new Atom.LeafAtom(this.atomType, this.atomData));
            }
         } else {
            if (var3 >= 262144L) {
               var2.position = var1.getPosition() + var3;
               var9 = true;
               break label29;
            }

            var1.skipFully((int)var3);
         }

         var9 = false;
      }

      this.processAtomEnded(var5 + var3);
      if (!var9 || this.parserState == 2) {
         var8 = false;
      }

      return var8;
   }

   private int readSample(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3 = var1.getPosition();
      if (this.sampleTrackIndex == -1) {
         this.sampleTrackIndex = this.getTrackIndexOfNextReadSample(var3);
         if (this.sampleTrackIndex == -1) {
            return -1;
         }
      }

      Mp4Extractor.Mp4Track var5 = this.tracks[this.sampleTrackIndex];
      TrackOutput var6 = var5.trackOutput;
      int var7 = var5.sampleIndex;
      TrackSampleTable var8 = var5.sampleTable;
      long var9 = var8.offsets[var7];
      int var11 = var8.sizes[var7];
      var3 = var9 - var3 + (long)this.sampleBytesWritten;
      if (var3 >= 0L && var3 < 262144L) {
         var9 = var3;
         int var12 = var11;
         if (var5.track.sampleTransformation == 1) {
            var9 = var3 + 8L;
            var12 = var11 - 8;
         }

         var1.skipFully((int)var9);
         int var13 = var5.track.nalUnitLengthFieldLength;
         if (var13 != 0) {
            byte[] var16 = this.nalLength.data;
            var16[0] = (byte)0;
            var16[1] = (byte)0;
            var16[2] = (byte)0;
            int var14 = 4 - var13;

            while(true) {
               var11 = var12;
               if (this.sampleBytesWritten >= var12) {
                  break;
               }

               var11 = this.sampleCurrentNalBytesRemaining;
               if (var11 == 0) {
                  var1.readFully(this.nalLength.data, var14, var13);
                  this.nalLength.setPosition(0);
                  this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                  this.nalStartCode.setPosition(0);
                  var6.sampleData(this.nalStartCode, 4);
                  this.sampleBytesWritten += 4;
                  var12 += var14;
               } else {
                  var11 = var6.sampleData(var1, var11, false);
                  this.sampleBytesWritten += var11;
                  this.sampleCurrentNalBytesRemaining -= var11;
               }
            }
         } else {
            while(true) {
               var13 = this.sampleBytesWritten;
               var11 = var12;
               if (var13 >= var12) {
                  break;
               }

               var11 = var6.sampleData(var1, var12 - var13, false);
               this.sampleBytesWritten += var11;
               this.sampleCurrentNalBytesRemaining -= var11;
            }
         }

         TrackSampleTable var15 = var5.sampleTable;
         var6.sampleMetadata(var15.timestampsUs[var7], var15.flags[var7], var11, 0, (TrackOutput.CryptoData)null);
         ++var5.sampleIndex;
         this.sampleTrackIndex = -1;
         this.sampleBytesWritten = 0;
         this.sampleCurrentNalBytesRemaining = 0;
         return 0;
      } else {
         var2.position = var9;
         return 1;
      }
   }

   private static boolean shouldParseContainerAtom(int var0) {
      boolean var1;
      if (var0 != Atom.TYPE_moov && var0 != Atom.TYPE_trak && var0 != Atom.TYPE_mdia && var0 != Atom.TYPE_minf && var0 != Atom.TYPE_stbl && var0 != Atom.TYPE_edts && var0 != Atom.TYPE_meta) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean shouldParseLeafAtom(int var0) {
      boolean var1;
      if (var0 != Atom.TYPE_mdhd && var0 != Atom.TYPE_mvhd && var0 != Atom.TYPE_hdlr && var0 != Atom.TYPE_stsd && var0 != Atom.TYPE_stts && var0 != Atom.TYPE_stss && var0 != Atom.TYPE_ctts && var0 != Atom.TYPE_elst && var0 != Atom.TYPE_stsc && var0 != Atom.TYPE_stsz && var0 != Atom.TYPE_stz2 && var0 != Atom.TYPE_stco && var0 != Atom.TYPE_co64 && var0 != Atom.TYPE_tkhd && var0 != Atom.TYPE_ftyp && var0 != Atom.TYPE_udta && var0 != Atom.TYPE_keys && var0 != Atom.TYPE_ilst) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void updateSampleIndices(long var1) {
      Mp4Extractor.Mp4Track[] var3 = this.tracks;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Mp4Extractor.Mp4Track var6 = var3[var5];
         TrackSampleTable var7 = var6.sampleTable;
         int var8 = var7.getIndexOfEarlierOrEqualSynchronizationSample(var1);
         int var9 = var8;
         if (var8 == -1) {
            var9 = var7.getIndexOfLaterOrEqualSynchronizationSample(var1);
         }

         var6.sampleIndex = var9;
      }

   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      Mp4Extractor.Mp4Track[] var3 = this.tracks;
      if (var3.length == 0) {
         return new SeekMap.SeekPoints(SeekPoint.START);
      } else {
         int var4 = this.firstVideoTrackIndex;
         TrackSampleTable var16;
         long var5;
         long var7;
         long var10;
         long var12;
         if (var4 != -1) {
            var16 = var3[var4].sampleTable;
            var4 = getSynchronizationSampleIndex(var16, var1);
            if (var4 == -1) {
               return new SeekMap.SeekPoints(SeekPoint.START);
            }

            label44: {
               var5 = var16.timestampsUs[var4];
               var7 = var16.offsets[var4];
               if (var5 < var1 && var4 < var16.sampleCount - 1) {
                  int var9 = var16.getIndexOfLaterOrEqualSynchronizationSample(var1);
                  if (var9 != -1 && var9 != var4) {
                     var1 = var16.timestampsUs[var9];
                     var10 = var16.offsets[var9];
                     break label44;
                  }
               }

               var10 = -1L;
               var1 = -9223372036854775807L;
            }

            var12 = var1;
            var1 = var10;
            var10 = var7;
         } else {
            var10 = Long.MAX_VALUE;
            var7 = -1L;
            var12 = -9223372036854775807L;
            var5 = var1;
            var1 = var7;
         }

         var4 = 0;

         while(true) {
            var3 = this.tracks;
            if (var4 >= var3.length) {
               SeekPoint var17 = new SeekPoint(var5, var10);
               if (var12 == -9223372036854775807L) {
                  return new SeekMap.SeekPoints(var17);
               }

               return new SeekMap.SeekPoints(var17, new SeekPoint(var12, var1));
            }

            long var14 = var1;
            var7 = var10;
            if (var4 != this.firstVideoTrackIndex) {
               var16 = var3[var4].sampleTable;
               var7 = maybeAdjustSeekOffset(var16, var5, var10);
               var10 = var1;
               if (var12 != -9223372036854775807L) {
                  var10 = maybeAdjustSeekOffset(var16, var12, var1);
               }

               var14 = var10;
            }

            ++var4;
            var1 = var14;
            var10 = var7;
         }
      }
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
   }

   public boolean isSeekable() {
      return true;
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      while(true) {
         int var3 = this.parserState;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  return this.readSample(var1, var2);
               }

               throw new IllegalStateException();
            }

            if (this.readAtomPayload(var1, var2)) {
               return 1;
            }
         } else if (!this.readAtomHeader(var1)) {
            return -1;
         }
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.containerAtoms.clear();
      this.atomHeaderBytesRead = 0;
      this.sampleTrackIndex = -1;
      this.sampleBytesWritten = 0;
      this.sampleCurrentNalBytesRemaining = 0;
      if (var1 == 0L) {
         this.enterReadingAtomHeaderState();
      } else if (this.tracks != null) {
         this.updateSampleIndices(var3);
      }

   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      return Sniffer.sniffUnfragmented(var1);
   }

   private static final class Mp4Track {
      public int sampleIndex;
      public final TrackSampleTable sampleTable;
      public final Track track;
      public final TrackOutput trackOutput;

      public Mp4Track(Track var1, TrackSampleTable var2, TrackOutput var3) {
         this.track = var1;
         this.sampleTable = var2;
         this.trackOutput = var3;
      }
   }
}
