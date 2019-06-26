package com.google.android.exoplayer2.extractor.mp4;

import android.util.Pair;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.text.cea.CeaUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FragmentedMp4Extractor implements Extractor {
   private static final Format EMSG_FORMAT;
   public static final ExtractorsFactory FACTORY;
   private static final byte[] PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE;
   private static final int SAMPLE_GROUP_TYPE_seig;
   private final TrackOutput additionalEmsgTrackOutput;
   private ParsableByteArray atomData;
   private final ParsableByteArray atomHeader;
   private int atomHeaderBytesRead;
   private long atomSize;
   private int atomType;
   private TrackOutput[] cea608TrackOutputs;
   private final List closedCaptionFormats;
   private final ArrayDeque containerAtoms;
   private FragmentedMp4Extractor.TrackBundle currentTrackBundle;
   private long durationUs;
   private TrackOutput[] emsgTrackOutputs;
   private long endOfMdatPosition;
   private final byte[] extendedTypeScratch;
   private ExtractorOutput extractorOutput;
   private final int flags;
   private boolean haveOutputSeekMap;
   private final ParsableByteArray nalBuffer;
   private final ParsableByteArray nalPrefix;
   private final ParsableByteArray nalStartCode;
   private int parserState;
   private int pendingMetadataSampleBytes;
   private final ArrayDeque pendingMetadataSampleInfos;
   private long pendingSeekTimeUs;
   private boolean processSeiNalUnitPayload;
   private int sampleBytesWritten;
   private int sampleCurrentNalBytesRemaining;
   private int sampleSize;
   private long segmentIndexEarliestPresentationTimeUs;
   private final DrmInitData sideloadedDrmInitData;
   private final Track sideloadedTrack;
   private final TimestampAdjuster timestampAdjuster;
   private final SparseArray trackBundles;

   static {
      FACTORY = _$$Lambda$FragmentedMp4Extractor$i0zfpH_PcF0vytkdatCL0xeWFhQ.INSTANCE;
      SAMPLE_GROUP_TYPE_seig = Util.getIntegerCodeForString("seig");
      PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE = new byte[]{-94, 57, 79, 82, 90, -101, 79, 20, -94, 68, 108, 66, 124, 100, -115, -12};
      EMSG_FORMAT = Format.createSampleFormat((String)null, "application/x-emsg", Long.MAX_VALUE);
   }

   public FragmentedMp4Extractor() {
      this(0);
   }

   public FragmentedMp4Extractor(int var1) {
      this(var1, (TimestampAdjuster)null);
   }

   public FragmentedMp4Extractor(int var1, TimestampAdjuster var2) {
      this(var1, var2, (Track)null, (DrmInitData)null);
   }

   public FragmentedMp4Extractor(int var1, TimestampAdjuster var2, Track var3, DrmInitData var4) {
      this(var1, var2, var3, var4, Collections.emptyList());
   }

   public FragmentedMp4Extractor(int var1, TimestampAdjuster var2, Track var3, DrmInitData var4, List var5) {
      this(var1, var2, var3, var4, var5, (TrackOutput)null);
   }

   public FragmentedMp4Extractor(int var1, TimestampAdjuster var2, Track var3, DrmInitData var4, List var5, TrackOutput var6) {
      byte var7;
      if (var3 != null) {
         var7 = 8;
      } else {
         var7 = 0;
      }

      this.flags = var1 | var7;
      this.timestampAdjuster = var2;
      this.sideloadedTrack = var3;
      this.sideloadedDrmInitData = var4;
      this.closedCaptionFormats = Collections.unmodifiableList(var5);
      this.additionalEmsgTrackOutput = var6;
      this.atomHeader = new ParsableByteArray(16);
      this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
      this.nalPrefix = new ParsableByteArray(5);
      this.nalBuffer = new ParsableByteArray();
      this.extendedTypeScratch = new byte[16];
      this.containerAtoms = new ArrayDeque();
      this.pendingMetadataSampleInfos = new ArrayDeque();
      this.trackBundles = new SparseArray();
      this.durationUs = -9223372036854775807L;
      this.pendingSeekTimeUs = -9223372036854775807L;
      this.segmentIndexEarliestPresentationTimeUs = -9223372036854775807L;
      this.enterReadingAtomHeaderState();
   }

   private void enterReadingAtomHeaderState() {
      this.parserState = 0;
      this.atomHeaderBytesRead = 0;
   }

   private DefaultSampleValues getDefaultSampleValues(SparseArray var1, int var2) {
      if (var1.size() == 1) {
         return (DefaultSampleValues)var1.valueAt(0);
      } else {
         Object var3 = var1.get(var2);
         Assertions.checkNotNull(var3);
         return (DefaultSampleValues)var3;
      }
   }

   private static DrmInitData getDrmInitDataFromAtoms(List var0) {
      int var1 = var0.size();
      Object var2 = null;
      int var3 = 0;

      ArrayList var4;
      ArrayList var6;
      for(var4 = null; var3 < var1; var4 = var6) {
         Atom.LeafAtom var5 = (Atom.LeafAtom)var0.get(var3);
         var6 = var4;
         if (var5.type == Atom.TYPE_pssh) {
            var6 = var4;
            if (var4 == null) {
               var6 = new ArrayList();
            }

            byte[] var7 = var5.data.data;
            UUID var9 = PsshAtomUtil.parseUuid(var7);
            if (var9 == null) {
               Log.w("FragmentedMp4Extractor", "Skipped pssh atom (failed to extract uuid)");
            } else {
               var6.add(new DrmInitData.SchemeData(var9, "video/mp4", var7));
            }
         }

         ++var3;
      }

      DrmInitData var8;
      if (var4 == null) {
         var8 = (DrmInitData)var2;
      } else {
         var8 = new DrmInitData(var4);
      }

      return var8;
   }

   private static FragmentedMp4Extractor.TrackBundle getNextFragmentRun(SparseArray var0) {
      int var1 = var0.size();
      FragmentedMp4Extractor.TrackBundle var2 = null;
      long var3 = Long.MAX_VALUE;

      long var9;
      for(int var5 = 0; var5 < var1; var3 = var9) {
         FragmentedMp4Extractor.TrackBundle var6 = (FragmentedMp4Extractor.TrackBundle)var0.valueAt(var5);
         int var7 = var6.currentTrackRunIndex;
         TrackFragment var8 = var6.fragment;
         if (var7 == var8.trunCount) {
            var9 = var3;
         } else {
            long var11 = var8.trunDataPosition[var7];
            var9 = var3;
            if (var11 < var3) {
               var2 = var6;
               var9 = var11;
            }
         }

         ++var5;
      }

      return var2;
   }

   private static FragmentedMp4Extractor.TrackBundle getTrackBundle(SparseArray var0, int var1) {
      return var0.size() == 1 ? (FragmentedMp4Extractor.TrackBundle)var0.valueAt(0) : (FragmentedMp4Extractor.TrackBundle)var0.get(var1);
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new FragmentedMp4Extractor()};
   }

   private void maybeInitExtraTracks() {
      TrackOutput[] var1 = this.emsgTrackOutputs;
      byte var2 = 0;
      TrackOutput var5;
      int var6;
      if (var1 == null) {
         this.emsgTrackOutputs = new TrackOutput[2];
         var5 = this.additionalEmsgTrackOutput;
         byte var3;
         if (var5 != null) {
            this.emsgTrackOutputs[0] = var5;
            var3 = 1;
         } else {
            var3 = 0;
         }

         int var4 = var3;
         if ((this.flags & 4) != 0) {
            this.emsgTrackOutputs[var3] = this.extractorOutput.track(this.trackBundles.size(), 4);
            var4 = var3 + 1;
         }

         this.emsgTrackOutputs = (TrackOutput[])Arrays.copyOf(this.emsgTrackOutputs, var4);
         var1 = this.emsgTrackOutputs;
         var4 = var1.length;

         for(var6 = 0; var6 < var4; ++var6) {
            var1[var6].format(EMSG_FORMAT);
         }
      }

      if (this.cea608TrackOutputs == null) {
         this.cea608TrackOutputs = new TrackOutput[this.closedCaptionFormats.size()];

         for(var6 = var2; var6 < this.cea608TrackOutputs.length; ++var6) {
            var5 = this.extractorOutput.track(this.trackBundles.size() + 1 + var6, 3);
            var5.format((Format)this.closedCaptionFormats.get(var6));
            this.cea608TrackOutputs[var6] = var5;
         }
      }

   }

   private void onContainerAtomRead(Atom.ContainerAtom var1) throws ParserException {
      int var2 = var1.type;
      if (var2 == Atom.TYPE_moov) {
         this.onMoovContainerAtomRead(var1);
      } else if (var2 == Atom.TYPE_moof) {
         this.onMoofContainerAtomRead(var1);
      } else if (!this.containerAtoms.isEmpty()) {
         ((Atom.ContainerAtom)this.containerAtoms.peek()).add(var1);
      }

   }

   private void onEmsgLeafAtomRead(ParsableByteArray var1) {
      TrackOutput[] var2 = this.emsgTrackOutputs;
      if (var2 != null && var2.length != 0) {
         var1.setPosition(12);
         int var3 = var1.bytesLeft();
         var1.readNullTerminatedString();
         var1.readNullTerminatedString();
         long var4 = var1.readUnsignedInt();
         long var6 = Util.scaleLargeTimestamp(var1.readUnsignedInt(), 1000000L, var4);
         TrackOutput[] var8 = this.emsgTrackOutputs;
         int var9 = var8.length;

         int var10;
         for(var10 = 0; var10 < var9; ++var10) {
            TrackOutput var13 = var8[var10];
            var1.setPosition(12);
            var13.sampleData(var1, var3);
         }

         var4 = this.segmentIndexEarliestPresentationTimeUs;
         if (var4 != -9223372036854775807L) {
            var4 += var6;
            TimestampAdjuster var11 = this.timestampAdjuster;
            if (var11 != null) {
               var4 = var11.adjustSampleTimestamp(var4);
            }

            TrackOutput[] var12 = this.emsgTrackOutputs;
            var9 = var12.length;

            for(var10 = 0; var10 < var9; ++var10) {
               var12[var10].sampleMetadata(var4, 1, var3, 0, (TrackOutput.CryptoData)null);
            }
         } else {
            this.pendingMetadataSampleInfos.addLast(new FragmentedMp4Extractor.MetadataSampleInfo(var6, var3));
            this.pendingMetadataSampleBytes += var3;
         }
      }

   }

   private void onLeafAtomRead(Atom.LeafAtom var1, long var2) throws ParserException {
      if (!this.containerAtoms.isEmpty()) {
         ((Atom.ContainerAtom)this.containerAtoms.peek()).add(var1);
      } else {
         int var4 = var1.type;
         if (var4 == Atom.TYPE_sidx) {
            Pair var5 = parseSidx(var1.data, var2);
            this.segmentIndexEarliestPresentationTimeUs = (Long)var5.first;
            this.extractorOutput.seekMap((SeekMap)var5.second);
            this.haveOutputSeekMap = true;
         } else if (var4 == Atom.TYPE_emsg) {
            this.onEmsgLeafAtomRead(var1.data);
         }
      }

   }

   private void onMoofContainerAtomRead(Atom.ContainerAtom var1) throws ParserException {
      parseMoof(var1, this.trackBundles, this.flags, this.extendedTypeScratch);
      DrmInitData var5;
      if (this.sideloadedDrmInitData != null) {
         var5 = null;
      } else {
         var5 = getDrmInitDataFromAtoms(var1.leafChildren);
      }

      byte var2 = 0;
      int var3;
      int var4;
      if (var5 != null) {
         var3 = this.trackBundles.size();

         for(var4 = 0; var4 < var3; ++var4) {
            ((FragmentedMp4Extractor.TrackBundle)this.trackBundles.valueAt(var4)).updateDrmInitData(var5);
         }
      }

      if (this.pendingSeekTimeUs != -9223372036854775807L) {
         var3 = this.trackBundles.size();

         for(var4 = var2; var4 < var3; ++var4) {
            ((FragmentedMp4Extractor.TrackBundle)this.trackBundles.valueAt(var4)).seek(this.pendingSeekTimeUs);
         }

         this.pendingSeekTimeUs = -9223372036854775807L;
      }

   }

   private void onMoovContainerAtomRead(Atom.ContainerAtom var1) throws ParserException {
      Track var2 = this.sideloadedTrack;
      boolean var3 = true;
      byte var4 = 0;
      byte var5 = 0;
      boolean var6;
      if (var2 == null) {
         var6 = true;
      } else {
         var6 = false;
      }

      Assertions.checkState(var6, "Unexpected moov box.");
      DrmInitData var17 = this.sideloadedDrmInitData;
      if (var17 == null) {
         var17 = getDrmInitDataFromAtoms(var1.leafChildren);
      }

      Atom.ContainerAtom var7 = var1.getContainerAtomOfType(Atom.TYPE_mvex);
      SparseArray var8 = new SparseArray();
      int var9 = var7.leafChildren.size();
      long var10 = -9223372036854775807L;

      int var12;
      for(var12 = 0; var12 < var9; ++var12) {
         Atom.LeafAtom var13 = (Atom.LeafAtom)var7.leafChildren.get(var12);
         int var14 = var13.type;
         if (var14 == Atom.TYPE_trex) {
            Pair var20 = parseTrex(var13.data);
            var8.put((Integer)var20.first, var20.second);
         } else if (var14 == Atom.TYPE_mehd) {
            var10 = parseMehd(var13.data);
         }
      }

      SparseArray var19 = new SparseArray();
      var12 = var1.containerChildren.size();

      for(var9 = 0; var9 < var12; ++var9) {
         Atom.ContainerAtom var21 = (Atom.ContainerAtom)var1.containerChildren.get(var9);
         if (var21.type == Atom.TYPE_trak) {
            Atom.LeafAtom var15 = var1.getLeafAtomOfType(Atom.TYPE_mvhd);
            if ((this.flags & 16) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            Track var22 = AtomParsers.parseTrak(var21, var15, var10, var17, var6, false);
            this.modifyTrack(var22);
            if (var22 != null) {
               var19.put(var22.id, var22);
            }
         }
      }

      var9 = var19.size();
      Track var16;
      if (this.trackBundles.size() == 0) {
         for(var12 = var5; var12 < var9; ++var12) {
            var16 = (Track)var19.valueAt(var12);
            FragmentedMp4Extractor.TrackBundle var18 = new FragmentedMp4Extractor.TrackBundle(this.extractorOutput.track(var12, var16.type));
            var18.init(var16, this.getDefaultSampleValues(var8, var16.id));
            this.trackBundles.put(var16.id, var18);
            this.durationUs = Math.max(this.durationUs, var16.durationUs);
         }

         this.maybeInitExtraTracks();
         this.extractorOutput.endTracks();
      } else {
         if (this.trackBundles.size() == var9) {
            var6 = var3;
         } else {
            var6 = false;
         }

         Assertions.checkState(var6);

         for(var12 = var4; var12 < var9; ++var12) {
            var16 = (Track)var19.valueAt(var12);
            ((FragmentedMp4Extractor.TrackBundle)this.trackBundles.get(var16.id)).init(var16, this.getDefaultSampleValues(var8, var16.id));
         }
      }

   }

   private void outputPendingMetadataSamples(long var1) {
      label19:
      while(true) {
         if (!this.pendingMetadataSampleInfos.isEmpty()) {
            FragmentedMp4Extractor.MetadataSampleInfo var3 = (FragmentedMp4Extractor.MetadataSampleInfo)this.pendingMetadataSampleInfos.removeFirst();
            this.pendingMetadataSampleBytes -= var3.size;
            long var4 = var3.presentationTimeDeltaUs + var1;
            TimestampAdjuster var6 = this.timestampAdjuster;
            long var7 = var4;
            if (var6 != null) {
               var7 = var6.adjustSampleTimestamp(var4);
            }

            TrackOutput[] var11 = this.emsgTrackOutputs;
            int var9 = var11.length;
            int var10 = 0;

            while(true) {
               if (var10 >= var9) {
                  continue label19;
               }

               var11[var10].sampleMetadata(var7, 1, var3.size, this.pendingMetadataSampleBytes, (TrackOutput.CryptoData)null);
               ++var10;
            }
         }

         return;
      }
   }

   private static long parseMehd(ParsableByteArray var0) {
      var0.setPosition(8);
      long var1;
      if (Atom.parseFullAtomVersion(var0.readInt()) == 0) {
         var1 = var0.readUnsignedInt();
      } else {
         var1 = var0.readUnsignedLongToLong();
      }

      return var1;
   }

   private static void parseMoof(Atom.ContainerAtom var0, SparseArray var1, int var2, byte[] var3) throws ParserException {
      int var4 = var0.containerChildren.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         Atom.ContainerAtom var6 = (Atom.ContainerAtom)var0.containerChildren.get(var5);
         if (var6.type == Atom.TYPE_traf) {
            parseTraf(var6, var1, var2, var3);
         }
      }

   }

   private static void parseSaio(ParsableByteArray var0, TrackFragment var1) throws ParserException {
      var0.setPosition(8);
      int var2 = var0.readInt();
      if ((Atom.parseFullAtomFlags(var2) & 1) == 1) {
         var0.skipBytes(8);
      }

      int var3 = var0.readUnsignedIntToInt();
      if (var3 == 1) {
         var3 = Atom.parseFullAtomVersion(var2);
         long var4 = var1.auxiliaryDataPosition;
         long var6;
         if (var3 == 0) {
            var6 = var0.readUnsignedInt();
         } else {
            var6 = var0.readUnsignedLongToLong();
         }

         var1.auxiliaryDataPosition = var4 + var6;
      } else {
         StringBuilder var8 = new StringBuilder();
         var8.append("Unexpected saio entry count: ");
         var8.append(var3);
         throw new ParserException(var8.toString());
      }
   }

   private static void parseSaiz(TrackEncryptionBox var0, ParsableByteArray var1, TrackFragment var2) throws ParserException {
      int var3 = var0.perSampleIvSize;
      var1.setPosition(8);
      int var4 = Atom.parseFullAtomFlags(var1.readInt());
      boolean var5 = true;
      if ((var4 & 1) == 1) {
         var1.skipBytes(8);
      }

      var4 = var1.readUnsignedByte();
      int var6 = var1.readUnsignedIntToInt();
      if (var6 != var2.sampleCount) {
         StringBuilder var10 = new StringBuilder();
         var10.append("Length mismatch: ");
         var10.append(var6);
         var10.append(", ");
         var10.append(var2.sampleCount);
         throw new ParserException(var10.toString());
      } else {
         int var8;
         if (var4 == 0) {
            boolean[] var9 = var2.sampleHasSubsampleEncryptionTable;
            int var7 = 0;
            var4 = 0;

            while(true) {
               var8 = var4;
               if (var7 >= var6) {
                  break;
               }

               var8 = var1.readUnsignedByte();
               var4 += var8;
               if (var8 > var3) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var9[var7] = var5;
               ++var7;
            }
         } else {
            if (var4 <= var3) {
               var5 = false;
            }

            var8 = var4 * var6 + 0;
            Arrays.fill(var2.sampleHasSubsampleEncryptionTable, 0, var6, var5);
         }

         var2.initEncryptionData(var8);
      }
   }

   private static void parseSenc(ParsableByteArray var0, int var1, TrackFragment var2) throws ParserException {
      var0.setPosition(var1 + 8);
      var1 = Atom.parseFullAtomFlags(var0.readInt());
      if ((var1 & 1) == 0) {
         boolean var3;
         if ((var1 & 2) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var1 = var0.readUnsignedIntToInt();
         if (var1 == var2.sampleCount) {
            Arrays.fill(var2.sampleHasSubsampleEncryptionTable, 0, var1, var3);
            var2.initEncryptionData(var0.bytesLeft());
            var2.fillEncryptionData(var0);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Length mismatch: ");
            var4.append(var1);
            var4.append(", ");
            var4.append(var2.sampleCount);
            throw new ParserException(var4.toString());
         }
      } else {
         throw new ParserException("Overriding TrackEncryptionBox parameters is unsupported.");
      }
   }

   private static void parseSenc(ParsableByteArray var0, TrackFragment var1) throws ParserException {
      parseSenc(var0, 0, var1);
   }

   private static void parseSgpd(ParsableByteArray var0, ParsableByteArray var1, String var2, TrackFragment var3) throws ParserException {
      var0.setPosition(8);
      int var4 = var0.readInt();
      if (var0.readInt() == SAMPLE_GROUP_TYPE_seig) {
         if (Atom.parseFullAtomVersion(var4) == 1) {
            var0.skipBytes(4);
         }

         if (var0.readInt() != 1) {
            throw new ParserException("Entry count in sbgp != 1 (unsupported).");
         } else {
            var1.setPosition(8);
            var4 = var1.readInt();
            if (var1.readInt() == SAMPLE_GROUP_TYPE_seig) {
               var4 = Atom.parseFullAtomVersion(var4);
               if (var4 == 1) {
                  if (var1.readUnsignedInt() == 0L) {
                     throw new ParserException("Variable length description in sgpd found (unsupported)");
                  }
               } else if (var4 >= 2) {
                  var1.skipBytes(4);
               }

               if (var1.readUnsignedInt() == 1L) {
                  var1.skipBytes(1);
                  int var5 = var1.readUnsignedByte();
                  boolean var6;
                  if (var1.readUnsignedByte() == 1) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  if (var6) {
                     int var7 = var1.readUnsignedByte();
                     byte[] var8 = new byte[16];
                     var1.readBytes(var8, 0, var8.length);
                     byte[] var9;
                     if (var6 && var7 == 0) {
                        var4 = var1.readUnsignedByte();
                        var9 = new byte[var4];
                        var1.readBytes(var9, 0, var4);
                     } else {
                        var9 = null;
                     }

                     var3.definesEncryptionData = true;
                     var3.trackEncryptionBox = new TrackEncryptionBox(var6, var2, var7, var8, (var5 & 240) >> 4, var5 & 15, var9);
                  }
               } else {
                  throw new ParserException("Entry count in sgpd != 1 (unsupported).");
               }
            }
         }
      }
   }

   private static Pair parseSidx(ParsableByteArray var0, long var1) throws ParserException {
      var0.setPosition(8);
      int var3 = Atom.parseFullAtomVersion(var0.readInt());
      var0.skipBytes(4);
      long var4 = var0.readUnsignedInt();
      long var6;
      long var8;
      if (var3 == 0) {
         var6 = var0.readUnsignedInt();
         var8 = var0.readUnsignedInt();
      } else {
         var6 = var0.readUnsignedLongToLong();
         var8 = var0.readUnsignedLongToLong();
      }

      var1 += var8;
      long var10 = Util.scaleLargeTimestamp(var6, 1000000L, var4);
      var0.skipBytes(2);
      var3 = var0.readUnsignedShort();
      int[] var12 = new int[var3];
      long[] var13 = new long[var3];
      long[] var14 = new long[var3];
      long[] var15 = new long[var3];
      var8 = var6;
      var6 = var10;

      for(int var16 = 0; var16 < var3; ++var16) {
         int var17 = var0.readInt();
         if ((var17 & Integer.MIN_VALUE) != 0) {
            throw new ParserException("Unhandled indirect reference");
         }

         long var18 = var0.readUnsignedInt();
         var12[var16] = var17 & Integer.MAX_VALUE;
         var13[var16] = var1;
         var15[var16] = var6;
         var8 += var18;
         var6 = Util.scaleLargeTimestamp(var8, 1000000L, var4);
         var14[var16] = var6 - var15[var16];
         var0.skipBytes(4);
         var1 += (long)var12[var16];
      }

      return Pair.create(var10, new ChunkIndex(var12, var13, var14, var15));
   }

   private static long parseTfdt(ParsableByteArray var0) {
      var0.setPosition(8);
      long var1;
      if (Atom.parseFullAtomVersion(var0.readInt()) == 1) {
         var1 = var0.readUnsignedLongToLong();
      } else {
         var1 = var0.readUnsignedInt();
      }

      return var1;
   }

   private static FragmentedMp4Extractor.TrackBundle parseTfhd(ParsableByteArray var0, SparseArray var1) {
      var0.setPosition(8);
      int var2 = Atom.parseFullAtomFlags(var0.readInt());
      FragmentedMp4Extractor.TrackBundle var9 = getTrackBundle(var1, var0.readInt());
      if (var9 == null) {
         return null;
      } else {
         if ((var2 & 1) != 0) {
            long var3 = var0.readUnsignedLongToLong();
            TrackFragment var5 = var9.fragment;
            var5.dataPosition = var3;
            var5.auxiliaryDataPosition = var3;
         }

         DefaultSampleValues var10 = var9.defaultSampleValues;
         int var6;
         if ((var2 & 2) != 0) {
            var6 = var0.readUnsignedIntToInt() - 1;
         } else {
            var6 = var10.sampleDescriptionIndex;
         }

         int var7;
         if ((var2 & 8) != 0) {
            var7 = var0.readUnsignedIntToInt();
         } else {
            var7 = var10.duration;
         }

         int var8;
         if ((var2 & 16) != 0) {
            var8 = var0.readUnsignedIntToInt();
         } else {
            var8 = var10.size;
         }

         if ((var2 & 32) != 0) {
            var2 = var0.readUnsignedIntToInt();
         } else {
            var2 = var10.flags;
         }

         var9.fragment.header = new DefaultSampleValues(var6, var7, var8, var2);
         return var9;
      }
   }

   private static void parseTraf(Atom.ContainerAtom var0, SparseArray var1, int var2, byte[] var3) throws ParserException {
      FragmentedMp4Extractor.TrackBundle var14 = parseTfhd(var0.getLeafAtomOfType(Atom.TYPE_tfhd).data, var1);
      if (var14 != null) {
         TrackFragment var4 = var14.fragment;
         long var5 = var4.nextFragmentDecodeTime;
         var14.reset();
         long var7 = var5;
         if (var0.getLeafAtomOfType(Atom.TYPE_tfdt) != null) {
            var7 = var5;
            if ((var2 & 2) == 0) {
               var7 = parseTfdt(var0.getLeafAtomOfType(Atom.TYPE_tfdt).data);
            }
         }

         parseTruns(var0, var14, var7, var2);
         TrackEncryptionBox var15 = var14.track.getSampleDescriptionEncryptionBox(var4.header.sampleDescriptionIndex);
         Atom.LeafAtom var9 = var0.getLeafAtomOfType(Atom.TYPE_saiz);
         if (var9 != null) {
            parseSaiz(var15, var9.data, var4);
         }

         var9 = var0.getLeafAtomOfType(Atom.TYPE_saio);
         if (var9 != null) {
            parseSaio(var9.data, var4);
         }

         var9 = var0.getLeafAtomOfType(Atom.TYPE_senc);
         if (var9 != null) {
            parseSenc(var9.data, var4);
         }

         var9 = var0.getLeafAtomOfType(Atom.TYPE_sbgp);
         Atom.LeafAtom var10 = var0.getLeafAtomOfType(Atom.TYPE_sgpd);
         if (var9 != null && var10 != null) {
            ParsableByteArray var12 = var9.data;
            ParsableByteArray var13 = var10.data;
            String var16;
            if (var15 != null) {
               var16 = var15.schemeType;
            } else {
               var16 = null;
            }

            parseSgpd(var12, var13, var16, var4);
         }

         int var11 = var0.leafChildren.size();

         for(var2 = 0; var2 < var11; ++var2) {
            Atom.LeafAtom var17 = (Atom.LeafAtom)var0.leafChildren.get(var2);
            if (var17.type == Atom.TYPE_uuid) {
               parseUuid(var17.data, var4, var3);
            }
         }

      }
   }

   private static Pair parseTrex(ParsableByteArray var0) {
      var0.setPosition(12);
      return Pair.create(var0.readInt(), new DefaultSampleValues(var0.readUnsignedIntToInt() - 1, var0.readUnsignedIntToInt(), var0.readUnsignedIntToInt(), var0.readInt()));
   }

   private static int parseTrun(FragmentedMp4Extractor.TrackBundle var0, int var1, long var2, int var4, ParsableByteArray var5, int var6) {
      var5.setPosition(8);
      int var7 = Atom.parseFullAtomFlags(var5.readInt());
      Track var8 = var0.track;
      TrackFragment var29 = var0.fragment;
      DefaultSampleValues var9 = var29.header;
      var29.trunLength[var1] = var5.readUnsignedIntToInt();
      long[] var10 = var29.trunDataPosition;
      var10[var1] = var29.dataPosition;
      if ((var7 & 1) != 0) {
         var10[var1] += (long)var5.readInt();
      }

      boolean var11;
      if ((var7 & 4) != 0) {
         var11 = true;
      } else {
         var11 = false;
      }

      int var12 = var9.flags;
      if (var11) {
         var12 = var5.readUnsignedIntToInt();
      }

      boolean var13;
      if ((var7 & 256) != 0) {
         var13 = true;
      } else {
         var13 = false;
      }

      boolean var14;
      if ((var7 & 512) != 0) {
         var14 = true;
      } else {
         var14 = false;
      }

      boolean var15;
      if ((var7 & 1024) != 0) {
         var15 = true;
      } else {
         var15 = false;
      }

      boolean var31;
      if ((var7 & 2048) != 0) {
         var31 = true;
      } else {
         var31 = false;
      }

      var10 = var8.editListDurations;
      long var16 = 0L;
      long var18 = var16;
      if (var10 != null) {
         var18 = var16;
         if (var10.length == 1) {
            var18 = var16;
            if (var10[0] == 0L) {
               var18 = Util.scaleLargeTimestamp(var8.editListMediaTimes[0], 1000L, var8.timescale);
            }
         }
      }

      int[] var32 = var29.sampleSizeTable;
      int[] var20 = var29.sampleCompositionTimeOffsetTable;
      long[] var21 = var29.sampleDecodingTimeTable;
      boolean[] var22 = var29.sampleIsSyncFrameTable;
      boolean var30;
      if (var8.type == 2 && (var4 & 1) != 0) {
         var30 = true;
      } else {
         var30 = false;
      }

      int var23 = var6 + var29.trunLength[var1];
      var16 = var8.timescale;
      if (var1 > 0) {
         var2 = var29.nextFragmentDecodeTime;
      }

      int var24 = var6;

      long var27;
      for(var6 = var23; var24 < var6; var2 += var27) {
         if (var13) {
            var23 = var5.readUnsignedIntToInt();
         } else {
            var23 = var9.duration;
         }

         int var25;
         if (var14) {
            var25 = var5.readUnsignedIntToInt();
         } else {
            var25 = var9.size;
         }

         if (var24 == 0 && var11) {
            var1 = var12;
         } else if (var15) {
            var1 = var5.readInt();
         } else {
            var1 = var9.flags;
         }

         if (var31) {
            var20[var24] = (int)((long)var5.readInt() * 1000L / var16);
         } else {
            var20[var24] = 0;
         }

         var21[var24] = Util.scaleLargeTimestamp(var2, 1000L, var16) - var18;
         var32[var24] = var25;
         boolean var26;
         if ((var1 >> 16 & 1) != 0 || var30 && var24 != 0) {
            var26 = false;
         } else {
            var26 = true;
         }

         var22[var24] = var26;
         var27 = (long)var23;
         ++var24;
      }

      var29.nextFragmentDecodeTime = var2;
      return var6;
   }

   private static void parseTruns(Atom.ContainerAtom var0, FragmentedMp4Extractor.TrackBundle var1, long var2, int var4) {
      List var14 = var0.leafChildren;
      int var5 = var14.size();
      byte var6 = 0;
      int var7 = 0;
      int var8 = 0;

      int var9;
      Atom.LeafAtom var10;
      int var11;
      int var12;
      for(var9 = 0; var7 < var5; var9 = var12) {
         var10 = (Atom.LeafAtom)var14.get(var7);
         var11 = var8;
         var12 = var9;
         if (var10.type == Atom.TYPE_trun) {
            ParsableByteArray var15 = var10.data;
            var15.setPosition(12);
            int var13 = var15.readUnsignedIntToInt();
            var11 = var8;
            var12 = var9;
            if (var13 > 0) {
               var12 = var9 + var13;
               var11 = var8 + 1;
            }
         }

         ++var7;
         var8 = var11;
      }

      var1.currentTrackRunIndex = 0;
      var1.currentSampleInTrackRun = 0;
      var1.currentSampleIndex = 0;
      var1.fragment.initTables(var8, var9);
      var8 = 0;
      var7 = 0;

      for(var9 = var6; var9 < var5; var7 = var12) {
         var10 = (Atom.LeafAtom)var14.get(var9);
         var11 = var8;
         var12 = var7;
         if (var10.type == Atom.TYPE_trun) {
            var12 = parseTrun(var1, var8, var2, var4, var10.data, var7);
            var11 = var8 + 1;
         }

         ++var9;
         var8 = var11;
      }

   }

   private static void parseUuid(ParsableByteArray var0, TrackFragment var1, byte[] var2) throws ParserException {
      var0.setPosition(8);
      var0.readBytes(var2, 0, 16);
      if (Arrays.equals(var2, PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE)) {
         parseSenc(var0, 16, var1);
      }
   }

   private void processAtomEnded(long var1) throws ParserException {
      while(!this.containerAtoms.isEmpty() && ((Atom.ContainerAtom)this.containerAtoms.peek()).endPosition == var1) {
         this.onContainerAtomRead((Atom.ContainerAtom)this.containerAtoms.pop());
      }

      this.enterReadingAtomHeaderState();
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

      if (this.atomSize < (long)this.atomHeaderBytesRead) {
         throw new ParserException("Atom size less than header length (unsupported).");
      } else {
         var2 = var1.getPosition() - (long)this.atomHeaderBytesRead;
         int var7;
         if (this.atomType == Atom.TYPE_moof) {
            int var6 = this.trackBundles.size();

            for(var7 = 0; var7 < var6; ++var7) {
               TrackFragment var8 = ((FragmentedMp4Extractor.TrackBundle)this.trackBundles.valueAt(var7)).fragment;
               var8.atomPosition = var2;
               var8.auxiliaryDataPosition = var2;
               var8.dataPosition = var2;
            }
         }

         var7 = this.atomType;
         if (var7 == Atom.TYPE_mdat) {
            this.currentTrackBundle = null;
            this.endOfMdatPosition = this.atomSize + var2;
            if (!this.haveOutputSeekMap) {
               this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs, var2));
               this.haveOutputSeekMap = true;
            }

            this.parserState = 2;
            return true;
         } else {
            if (shouldParseContainerAtom(var7)) {
               var2 = var1.getPosition() + this.atomSize - 8L;
               this.containerAtoms.push(new Atom.ContainerAtom(this.atomType, var2));
               if (this.atomSize == (long)this.atomHeaderBytesRead) {
                  this.processAtomEnded(var2);
               } else {
                  this.enterReadingAtomHeaderState();
               }
            } else if (shouldParseLeafAtom(this.atomType)) {
               if (this.atomHeaderBytesRead != 8) {
                  throw new ParserException("Leaf atom defines extended atom size (unsupported).");
               }

               var2 = this.atomSize;
               if (var2 > 2147483647L) {
                  throw new ParserException("Leaf atom with length > 2147483647 (unsupported).");
               }

               this.atomData = new ParsableByteArray((int)var2);
               System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
               this.parserState = 1;
            } else {
               if (this.atomSize > 2147483647L) {
                  throw new ParserException("Skipping atom with length > 2147483647 (unsupported).");
               }

               this.atomData = null;
               this.parserState = 1;
            }

            return true;
         }
      }
   }

   private void readAtomPayload(ExtractorInput var1) throws IOException, InterruptedException {
      int var2 = (int)this.atomSize - this.atomHeaderBytesRead;
      ParsableByteArray var3 = this.atomData;
      if (var3 != null) {
         var1.readFully(var3.data, 8, var2);
         this.onLeafAtomRead(new Atom.LeafAtom(this.atomType, this.atomData), var1.getPosition());
      } else {
         var1.skipFully(var2);
      }

      this.processAtomEnded(var1.getPosition());
   }

   private void readEncryptionData(ExtractorInput var1) throws IOException, InterruptedException {
      int var2 = this.trackBundles.size();
      FragmentedMp4Extractor.TrackBundle var3 = null;
      long var4 = Long.MAX_VALUE;

      int var6;
      long var9;
      for(var6 = 0; var6 < var2; var4 = var9) {
         TrackFragment var7 = ((FragmentedMp4Extractor.TrackBundle)this.trackBundles.valueAt(var6)).fragment;
         FragmentedMp4Extractor.TrackBundle var8 = var3;
         var9 = var4;
         if (var7.sampleEncryptionDataNeedsFill) {
            long var11 = var7.auxiliaryDataPosition;
            var8 = var3;
            var9 = var4;
            if (var11 < var4) {
               var8 = (FragmentedMp4Extractor.TrackBundle)this.trackBundles.valueAt(var6);
               var9 = var11;
            }
         }

         ++var6;
         var3 = var8;
      }

      if (var3 == null) {
         this.parserState = 3;
      } else {
         var6 = (int)(var4 - var1.getPosition());
         if (var6 >= 0) {
            var1.skipFully(var6);
            var3.fragment.fillEncryptionData(var1);
         } else {
            throw new ParserException("Offset to encryption data was negative.");
         }
      }
   }

   private boolean readSample(ExtractorInput var1) throws IOException, InterruptedException {
      int var3;
      int var4;
      if (this.parserState == 3) {
         if (this.currentTrackBundle == null) {
            FragmentedMp4Extractor.TrackBundle var2 = getNextFragmentRun(this.trackBundles);
            if (var2 == null) {
               var3 = (int)(this.endOfMdatPosition - var1.getPosition());
               if (var3 >= 0) {
                  var1.skipFully(var3);
                  this.enterReadingAtomHeaderState();
                  return false;
               }

               throw new ParserException("Offset to end of mdat was negative.");
            }

            var4 = (int)(var2.fragment.trunDataPosition[var2.currentTrackRunIndex] - var1.getPosition());
            var3 = var4;
            if (var4 < 0) {
               Log.w("FragmentedMp4Extractor", "Ignoring negative offset to sample data.");
               var3 = 0;
            }

            var1.skipFully(var3);
            this.currentTrackBundle = var2;
         }

         FragmentedMp4Extractor.TrackBundle var5 = this.currentTrackBundle;
         int[] var19 = var5.fragment.sampleSizeTable;
         var3 = var5.currentSampleIndex;
         this.sampleSize = var19[var3];
         if (var3 < var5.firstSampleToOutputIndex) {
            var1.skipFully(this.sampleSize);
            this.currentTrackBundle.skipSampleEncryptionData();
            if (!this.currentTrackBundle.next()) {
               this.currentTrackBundle = null;
            }

            this.parserState = 3;
            return true;
         }

         if (var5.track.sampleTransformation == 1) {
            this.sampleSize -= 8;
            var1.skipFully(8);
         }

         this.sampleBytesWritten = this.currentTrackBundle.outputSampleEncryptionData();
         this.sampleSize += this.sampleBytesWritten;
         this.parserState = 4;
         this.sampleCurrentNalBytesRemaining = 0;
      }

      FragmentedMp4Extractor.TrackBundle var6 = this.currentTrackBundle;
      TrackFragment var21 = var6.fragment;
      Track var7 = var6.track;
      TrackOutput var20 = var6.output;
      var4 = var6.currentSampleIndex;
      long var8 = var21.getSamplePresentationTime(var4) * 1000L;
      TimestampAdjuster var22 = this.timestampAdjuster;
      long var10 = var8;
      if (var22 != null) {
         var10 = var22.adjustSampleTimestamp(var8);
      }

      int var12 = var7.nalUnitLengthFieldLength;
      if (var12 != 0) {
         byte[] var13 = this.nalPrefix.data;
         var13[0] = (byte)0;
         var13[1] = (byte)0;
         var13[2] = (byte)0;
         int var14 = 4 - var12;

         label77:
         while(true) {
            while(true) {
               if (this.sampleBytesWritten >= this.sampleSize) {
                  break label77;
               }

               var3 = this.sampleCurrentNalBytesRemaining;
               if (var3 == 0) {
                  var1.readFully(var13, var14, var12 + 1);
                  this.nalPrefix.setPosition(0);
                  this.sampleCurrentNalBytesRemaining = this.nalPrefix.readUnsignedIntToInt() - 1;
                  this.nalStartCode.setPosition(0);
                  var20.sampleData(this.nalStartCode, 4);
                  var20.sampleData(this.nalPrefix, 1);
                  boolean var15;
                  if (this.cea608TrackOutputs.length > 0 && NalUnitUtil.isNalUnitSei(var7.format.sampleMimeType, var13[4])) {
                     var15 = true;
                  } else {
                     var15 = false;
                  }

                  this.processSeiNalUnitPayload = var15;
                  this.sampleBytesWritten += 5;
                  this.sampleSize += var14;
               } else {
                  if (this.processSeiNalUnitPayload) {
                     this.nalBuffer.reset(var3);
                     var1.readFully(this.nalBuffer.data, 0, this.sampleCurrentNalBytesRemaining);
                     var20.sampleData(this.nalBuffer, this.sampleCurrentNalBytesRemaining);
                     var3 = this.sampleCurrentNalBytesRemaining;
                     ParsableByteArray var23 = this.nalBuffer;
                     int var16 = NalUnitUtil.unescapeStream(var23.data, var23.limit());
                     this.nalBuffer.setPosition("video/hevc".equals(var7.format.sampleMimeType));
                     this.nalBuffer.setLimit(var16);
                     CeaUtil.consume(var10, this.nalBuffer, this.cea608TrackOutputs);
                  } else {
                     var3 = var20.sampleData(var1, var3, false);
                  }

                  this.sampleBytesWritten += var3;
                  this.sampleCurrentNalBytesRemaining -= var3;
               }
            }
         }
      } else {
         while(true) {
            var12 = this.sampleBytesWritten;
            var3 = this.sampleSize;
            if (var12 >= var3) {
               break;
            }

            var3 = var20.sampleData(var1, var3 - var12, false);
            this.sampleBytesWritten += var3;
         }
      }

      var3 = var21.sampleIsSyncFrameTable[var4];
      TrackEncryptionBox var17 = this.currentTrackBundle.getEncryptionBoxIfEncrypted();
      TrackOutput.CryptoData var18;
      if (var17 != null) {
         var18 = var17.cryptoData;
         var3 |= 1073741824;
      } else {
         var18 = null;
      }

      var20.sampleMetadata(var10, var3, this.sampleSize, 0, var18);
      this.outputPendingMetadataSamples(var10);
      if (!this.currentTrackBundle.next()) {
         this.currentTrackBundle = null;
      }

      this.parserState = 3;
      return true;
   }

   private static boolean shouldParseContainerAtom(int var0) {
      boolean var1;
      if (var0 != Atom.TYPE_moov && var0 != Atom.TYPE_trak && var0 != Atom.TYPE_mdia && var0 != Atom.TYPE_minf && var0 != Atom.TYPE_stbl && var0 != Atom.TYPE_moof && var0 != Atom.TYPE_traf && var0 != Atom.TYPE_mvex && var0 != Atom.TYPE_edts) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean shouldParseLeafAtom(int var0) {
      boolean var1;
      if (var0 != Atom.TYPE_hdlr && var0 != Atom.TYPE_mdhd && var0 != Atom.TYPE_mvhd && var0 != Atom.TYPE_sidx && var0 != Atom.TYPE_stsd && var0 != Atom.TYPE_tfdt && var0 != Atom.TYPE_tfhd && var0 != Atom.TYPE_tkhd && var0 != Atom.TYPE_trex && var0 != Atom.TYPE_trun && var0 != Atom.TYPE_pssh && var0 != Atom.TYPE_saiz && var0 != Atom.TYPE_saio && var0 != Atom.TYPE_senc && var0 != Atom.TYPE_uuid && var0 != Atom.TYPE_sbgp && var0 != Atom.TYPE_sgpd && var0 != Atom.TYPE_elst && var0 != Atom.TYPE_mehd && var0 != Atom.TYPE_emsg) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
      Track var2 = this.sideloadedTrack;
      if (var2 != null) {
         FragmentedMp4Extractor.TrackBundle var3 = new FragmentedMp4Extractor.TrackBundle(var1.track(0, var2.type));
         var3.init(this.sideloadedTrack, new DefaultSampleValues(0, 0, 0, 0));
         this.trackBundles.put(0, var3);
         this.maybeInitExtraTracks();
         this.extractorOutput.endTracks();
      }

   }

   protected Track modifyTrack(Track var1) {
      return var1;
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      while(true) {
         int var3 = this.parserState;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (this.readSample(var1)) {
                     return 0;
                  }
               } else {
                  this.readEncryptionData(var1);
               }
            } else {
               this.readAtomPayload(var1);
            }
         } else if (!this.readAtomHeader(var1)) {
            return -1;
         }
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      int var5 = this.trackBundles.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         ((FragmentedMp4Extractor.TrackBundle)this.trackBundles.valueAt(var6)).reset();
      }

      this.pendingMetadataSampleInfos.clear();
      this.pendingMetadataSampleBytes = 0;
      this.pendingSeekTimeUs = var3;
      this.containerAtoms.clear();
      this.enterReadingAtomHeaderState();
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      return Sniffer.sniffFragmented(var1);
   }

   private static final class MetadataSampleInfo {
      public final long presentationTimeDeltaUs;
      public final int size;

      public MetadataSampleInfo(long var1, int var3) {
         this.presentationTimeDeltaUs = var1;
         this.size = var3;
      }
   }

   private static final class TrackBundle {
      public int currentSampleInTrackRun;
      public int currentSampleIndex;
      public int currentTrackRunIndex;
      private final ParsableByteArray defaultInitializationVector;
      public DefaultSampleValues defaultSampleValues;
      private final ParsableByteArray encryptionSignalByte;
      public int firstSampleToOutputIndex;
      public final TrackFragment fragment;
      public final TrackOutput output;
      public Track track;

      public TrackBundle(TrackOutput var1) {
         this.output = var1;
         this.fragment = new TrackFragment();
         this.encryptionSignalByte = new ParsableByteArray(1);
         this.defaultInitializationVector = new ParsableByteArray();
      }

      private TrackEncryptionBox getEncryptionBoxIfEncrypted() {
         TrackFragment var1 = this.fragment;
         int var2 = var1.header.sampleDescriptionIndex;
         TrackEncryptionBox var3 = var1.trackEncryptionBox;
         if (var3 == null) {
            var3 = this.track.getSampleDescriptionEncryptionBox(var2);
         }

         if (var3 == null || !var3.isEncrypted) {
            var3 = null;
         }

         return var3;
      }

      private void skipSampleEncryptionData() {
         TrackEncryptionBox var1 = this.getEncryptionBoxIfEncrypted();
         if (var1 != null) {
            ParsableByteArray var2 = this.fragment.sampleEncryptionData;
            int var3 = var1.perSampleIvSize;
            if (var3 != 0) {
               var2.skipBytes(var3);
            }

            if (this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex)) {
               var2.skipBytes(var2.readUnsignedShort() * 6);
            }

         }
      }

      public void init(Track var1, DefaultSampleValues var2) {
         Assertions.checkNotNull(var1);
         this.track = (Track)var1;
         Assertions.checkNotNull(var2);
         this.defaultSampleValues = (DefaultSampleValues)var2;
         this.output.format(var1.format);
         this.reset();
      }

      public boolean next() {
         ++this.currentSampleIndex;
         ++this.currentSampleInTrackRun;
         int var1 = this.currentSampleInTrackRun;
         int[] var2 = this.fragment.trunLength;
         int var3 = this.currentTrackRunIndex;
         if (var1 == var2[var3]) {
            this.currentTrackRunIndex = var3 + 1;
            this.currentSampleInTrackRun = 0;
            return false;
         } else {
            return true;
         }
      }

      public int outputSampleEncryptionData() {
         TrackEncryptionBox var1 = this.getEncryptionBoxIfEncrypted();
         if (var1 == null) {
            return 0;
         } else {
            int var2 = var1.perSampleIvSize;
            byte[] var3;
            ParsableByteArray var6;
            if (var2 != 0) {
               var6 = this.fragment.sampleEncryptionData;
            } else {
               var3 = var1.defaultInitializationVector;
               this.defaultInitializationVector.reset(var3, var3.length);
               var6 = this.defaultInitializationVector;
               var2 = var3.length;
            }

            boolean var4 = this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex);
            var3 = this.encryptionSignalByte.data;
            short var5;
            if (var4) {
               var5 = 128;
            } else {
               var5 = 0;
            }

            var3[0] = (byte)((byte)(var5 | var2));
            this.encryptionSignalByte.setPosition(0);
            this.output.sampleData(this.encryptionSignalByte, 1);
            this.output.sampleData(var6, var2);
            if (!var4) {
               return var2 + 1;
            } else {
               var6 = this.fragment.sampleEncryptionData;
               int var7 = var6.readUnsignedShort();
               var6.skipBytes(-2);
               var7 = var7 * 6 + 2;
               this.output.sampleData(var6, var7);
               return var2 + 1 + var7;
            }
         }
      }

      public void reset() {
         this.fragment.reset();
         this.currentSampleIndex = 0;
         this.currentTrackRunIndex = 0;
         this.currentSampleInTrackRun = 0;
         this.firstSampleToOutputIndex = 0;
      }

      public void seek(long var1) {
         var1 = C.usToMs(var1);
         int var3 = this.currentSampleIndex;

         while(true) {
            TrackFragment var4 = this.fragment;
            if (var3 >= var4.sampleCount || var4.getSamplePresentationTime(var3) >= var1) {
               return;
            }

            if (this.fragment.sampleIsSyncFrameTable[var3]) {
               this.firstSampleToOutputIndex = var3;
            }

            ++var3;
         }
      }

      public void updateDrmInitData(DrmInitData var1) {
         TrackEncryptionBox var2 = this.track.getSampleDescriptionEncryptionBox(this.fragment.header.sampleDescriptionIndex);
         String var3;
         if (var2 != null) {
            var3 = var2.schemeType;
         } else {
            var3 = null;
         }

         this.output.format(this.track.format.copyWithDrmInitData(var1.copyWithSchemeType(var3)));
      }
   }
}
