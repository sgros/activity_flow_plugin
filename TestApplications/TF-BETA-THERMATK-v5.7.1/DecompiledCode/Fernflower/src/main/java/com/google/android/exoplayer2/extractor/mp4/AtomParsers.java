package com.google.android.exoplayer2.extractor.mp4;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.video.HevcConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class AtomParsers {
   private static final int TYPE_clcp = Util.getIntegerCodeForString("clcp");
   private static final int TYPE_mdta = Util.getIntegerCodeForString("mdta");
   private static final int TYPE_meta = Util.getIntegerCodeForString("meta");
   private static final int TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
   private static final int TYPE_soun = Util.getIntegerCodeForString("soun");
   private static final int TYPE_subt = Util.getIntegerCodeForString("subt");
   private static final int TYPE_text = Util.getIntegerCodeForString("text");
   private static final int TYPE_vide = Util.getIntegerCodeForString("vide");
   private static final byte[] opusMagic = Util.getUtf8Bytes("OpusHead");

   private static boolean canApplyEditWithGaplessInfo(long[] var0, long var1, long var3, long var5) {
      int var7 = var0.length;
      boolean var8 = true;
      int var9 = var7 - 1;
      var7 = Util.constrainValue(3, 0, var9);
      var9 = Util.constrainValue(var0.length - 3, 0, var9);
      if (var0[0] > var3 || var3 >= var0[var7] || var0[var9] >= var5 || var5 > var1) {
         var8 = false;
      }

      return var8;
   }

   private static int findEsdsPosition(ParsableByteArray var0, int var1, int var2) {
      int var4;
      for(int var3 = var0.getPosition(); var3 - var1 < var2; var3 += var4) {
         var0.setPosition(var3);
         var4 = var0.readInt();
         boolean var5;
         if (var4 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assertions.checkArgument(var5, "childAtomSize should be positive");
         if (var0.readInt() == Atom.TYPE_esds) {
            return var3;
         }
      }

      return -1;
   }

   private static int getTrackTypeForHdlr(int var0) {
      if (var0 == TYPE_soun) {
         return 1;
      } else if (var0 == TYPE_vide) {
         return 2;
      } else if (var0 != TYPE_text && var0 != TYPE_sbtl && var0 != TYPE_subt && var0 != TYPE_clcp) {
         return var0 == TYPE_meta ? 4 : -1;
      } else {
         return 3;
      }
   }

   private static void parseAudioSampleEntry(ParsableByteArray var0, int var1, int var2, int var3, int var4, String var5, boolean var6, DrmInitData var7, AtomParsers.StsdData var8, int var9) throws ParserException {
      var0.setPosition(var2 + 8 + 8);
      int var11;
      if (var6) {
         var11 = var0.readUnsignedShort();
         var0.skipBytes(6);
      } else {
         var0.skipBytes(8);
         var11 = 0;
      }

      int var12;
      int var13;
      int var14;
      int var15;
      if (var11 != 0 && var11 != 1) {
         if (var11 != 2) {
            return;
         }

         var0.skipBytes(16);
         var12 = (int)Math.round(var0.readDouble());
         var13 = var0.readUnsignedIntToInt();
         var0.skipBytes(20);
      } else {
         var14 = var0.readUnsignedShort();
         var0.skipBytes(6);
         var15 = var0.readUnsignedFixedPoint1616();
         var12 = var15;
         var13 = var14;
         if (var11 == 1) {
            var0.skipBytes(16);
            var13 = var14;
            var12 = var15;
         }
      }

      var15 = var0.getPosition();
      var14 = Atom.TYPE_enca;
      DrmInitData var16 = var7;
      var11 = var1;
      Pair var17;
      if (var1 == var14) {
         var17 = parseSampleEntryEncryptionData(var0, var2, var3);
         var16 = var7;
         if (var17 != null) {
            var1 = (Integer)var17.first;
            if (var7 == null) {
               var16 = null;
            } else {
               var16 = var7.copyWithSchemeType(((TrackEncryptionBox)var17.second).schemeType);
            }

            var8.trackEncryptionBoxes[var9] = (TrackEncryptionBox)var17.second;
         }

         var0.setPosition(var15);
         var11 = var1;
      }

      var1 = Atom.TYPE_ac_3;
      String var18 = "audio/raw";
      String var24;
      if (var11 == var1) {
         var24 = "audio/ac3";
      } else if (var11 == Atom.TYPE_ec_3) {
         var24 = "audio/eac3";
      } else if (var11 == Atom.TYPE_dtsc) {
         var24 = "audio/vnd.dts";
      } else if (var11 != Atom.TYPE_dtsh && var11 != Atom.TYPE_dtsl) {
         if (var11 == Atom.TYPE_dtse) {
            var24 = "audio/vnd.dts.hd;profile=lbr";
         } else if (var11 == Atom.TYPE_samr) {
            var24 = "audio/3gpp";
         } else if (var11 == Atom.TYPE_sawb) {
            var24 = "audio/amr-wb";
         } else if (var11 != Atom.TYPE_lpcm && var11 != Atom.TYPE_sowt) {
            if (var11 == Atom.TYPE__mp3) {
               var24 = "audio/mpeg";
            } else if (var11 == Atom.TYPE_alac) {
               var24 = "audio/alac";
            } else if (var11 == Atom.TYPE_alaw) {
               var24 = "audio/g711-alaw";
            } else if (var11 == Atom.TYPE_ulaw) {
               var24 = "audio/g711-mlaw";
            } else if (var11 == Atom.TYPE_Opus) {
               var24 = "audio/opus";
            } else if (var11 == Atom.TYPE_fLaC) {
               var24 = "audio/flac";
            } else {
               var24 = null;
            }
         } else {
            var24 = "audio/raw";
         }
      } else {
         var24 = "audio/vnd.dts.hd";
      }

      var9 = var12;
      var1 = var15;
      var17 = null;
      String var19 = var24;
      byte[] var25 = (byte[])var17;
      DrmInitData var27 = var16;

      String var28;
      for(var28 = var19; var1 - var2 < var3; var1 += var12) {
         var0.setPosition(var1);
         var12 = var0.readInt();
         boolean var20;
         if (var12 > 0) {
            var20 = true;
         } else {
            var20 = false;
         }

         Assertions.checkArgument(var20, "childAtomSize should be positive");
         var15 = var0.readInt();
         if (var15 == Atom.TYPE_esds || var6 && var15 == Atom.TYPE_wave) {
            var11 = var1;
            if (var15 != Atom.TYPE_esds) {
               var11 = findEsdsPosition(var0, var1, var12);
            }

            if (var11 != -1) {
               Pair var26 = parseEsdsFromParent(var0, var11);
               var19 = (String)var26.first;
               byte[] var21 = (byte[])var26.second;
               var28 = var19;
               var25 = var21;
               if ("audio/mp4a-latm".equals(var19)) {
                  var26 = CodecSpecificDataUtil.parseAacAudioSpecificConfig(var21);
                  var9 = (Integer)var26.first;
                  var13 = (Integer)var26.second;
                  var25 = var21;
                  var28 = var19;
               }
            }
         } else if (var15 == Atom.TYPE_dac3) {
            var0.setPosition(var1 + 8);
            var8.format = Ac3Util.parseAc3AnnexFFormat(var0, Integer.toString(var4), var5, var27);
         } else if (var15 == Atom.TYPE_dec3) {
            var0.setPosition(var1 + 8);
            var8.format = Ac3Util.parseEAc3AnnexFFormat(var0, Integer.toString(var4), var5, var27);
         } else if (var15 == Atom.TYPE_ddts) {
            var8.format = Format.createAudioSampleFormat(Integer.toString(var4), var28, (String)null, -1, -1, var13, var9, (List)null, var27, 0, var5);
         } else if (var15 == Atom.TYPE_alac) {
            var25 = new byte[var12];
            var0.setPosition(var1);
            var0.readBytes(var25, 0, var12);
         } else {
            int var10;
            if (var15 == Atom.TYPE_dOps) {
               var10 = var12 - 8;
               byte[] var29 = opusMagic;
               var25 = new byte[var29.length + var10];
               System.arraycopy(var29, 0, var25, 0, var29.length);
               var0.setPosition(var1 + 8);
               var0.readBytes(var25, opusMagic.length, var10);
            } else if (var12 == Atom.TYPE_dfLa) {
               var10 = var12 - 12;
               var25 = new byte[var10];
               var0.setPosition(var1 + 12);
               var0.readBytes(var25, 0, var10);
            }
         }
      }

      byte var23 = 2;
      if (var8.format == null && var28 != null) {
         if (!var18.equals(var28)) {
            var23 = -1;
         }

         var18 = Integer.toString(var4);
         List var22;
         if (var25 == null) {
            var22 = null;
         } else {
            var22 = Collections.singletonList(var25);
         }

         var8.format = Format.createAudioSampleFormat(var18, var28, (String)null, -1, -1, var13, var9, var23, var22, var27, 0, var5);
      }

   }

   static Pair parseCommonEncryptionSinfFromParent(ParsableByteArray var0, int var1, int var2) {
      int var3 = var1 + 8;
      String var4 = null;
      Object var5 = var4;
      int var6 = -1;

      int var7;
      Object var10;
      for(var7 = 0; var3 - var1 < var2; var5 = var10) {
         var0.setPosition(var3);
         int var8 = var0.readInt();
         int var9 = var0.readInt();
         String var11;
         if (var9 == Atom.TYPE_frma) {
            var10 = var0.readInt();
            var11 = var4;
         } else if (var9 == Atom.TYPE_schm) {
            var0.skipBytes(4);
            var11 = var0.readString(4);
            var10 = var5;
         } else {
            var11 = var4;
            var10 = var5;
            if (var9 == Atom.TYPE_schi) {
               var6 = var3;
               var7 = var8;
               var10 = var5;
               var11 = var4;
            }
         }

         var3 += var8;
         var4 = var11;
      }

      if (!"cenc".equals(var4) && !"cbc1".equals(var4) && !"cens".equals(var4) && !"cbcs".equals(var4)) {
         return null;
      } else {
         boolean var12 = true;
         boolean var13;
         if (var5 != null) {
            var13 = true;
         } else {
            var13 = false;
         }

         Assertions.checkArgument(var13, "frma atom is mandatory");
         if (var6 != -1) {
            var13 = true;
         } else {
            var13 = false;
         }

         Assertions.checkArgument(var13, "schi atom is mandatory");
         TrackEncryptionBox var14 = parseSchiFromParent(var0, var6, var7, var4);
         if (var14 != null) {
            var13 = var12;
         } else {
            var13 = false;
         }

         Assertions.checkArgument(var13, "tenc atom is mandatory");
         return Pair.create(var5, var14);
      }
   }

   private static Pair parseEdts(Atom.ContainerAtom var0) {
      if (var0 != null) {
         Atom.LeafAtom var8 = var0.getLeafAtomOfType(Atom.TYPE_elst);
         if (var8 != null) {
            ParsableByteArray var1 = var8.data;
            var1.setPosition(8);
            int var2 = Atom.parseFullAtomVersion(var1.readInt());
            int var3 = var1.readUnsignedIntToInt();
            long[] var4 = new long[var3];
            long[] var9 = new long[var3];

            for(int var5 = 0; var5 < var3; ++var5) {
               long var6;
               if (var2 == 1) {
                  var6 = var1.readUnsignedLongToLong();
               } else {
                  var6 = var1.readUnsignedInt();
               }

               var4[var5] = var6;
               if (var2 == 1) {
                  var6 = var1.readLong();
               } else {
                  var6 = (long)var1.readInt();
               }

               var9[var5] = var6;
               if (var1.readShort() != 1) {
                  throw new IllegalArgumentException("Unsupported media rate.");
               }

               var1.skipBytes(2);
            }

            return Pair.create(var4, var9);
         }
      }

      return Pair.create((Object)null, (Object)null);
   }

   private static Pair parseEsdsFromParent(ParsableByteArray var0, int var1) {
      var0.setPosition(var1 + 8 + 4);
      var0.skipBytes(1);
      parseExpandableClassSize(var0);
      var0.skipBytes(2);
      var1 = var0.readUnsignedByte();
      if ((var1 & 128) != 0) {
         var0.skipBytes(2);
      }

      if ((var1 & 64) != 0) {
         var0.skipBytes(var0.readUnsignedShort());
      }

      if ((var1 & 32) != 0) {
         var0.skipBytes(2);
      }

      var0.skipBytes(1);
      parseExpandableClassSize(var0);
      String var2 = MimeTypes.getMimeTypeFromMp4ObjectType(var0.readUnsignedByte());
      if (!"audio/mpeg".equals(var2) && !"audio/vnd.dts".equals(var2) && !"audio/vnd.dts.hd".equals(var2)) {
         var0.skipBytes(12);
         var0.skipBytes(1);
         var1 = parseExpandableClassSize(var0);
         byte[] var3 = new byte[var1];
         var0.readBytes(var3, 0, var1);
         return Pair.create(var2, var3);
      } else {
         return Pair.create(var2, (Object)null);
      }
   }

   private static int parseExpandableClassSize(ParsableByteArray var0) {
      int var1 = var0.readUnsignedByte();

      int var2;
      for(var2 = var1 & 127; (var1 & 128) == 128; var2 = var2 << 7 | var1 & 127) {
         var1 = var0.readUnsignedByte();
      }

      return var2;
   }

   private static int parseHdlr(ParsableByteArray var0) {
      var0.setPosition(16);
      return var0.readInt();
   }

   private static Metadata parseIlst(ParsableByteArray var0, int var1) {
      var0.skipBytes(8);
      ArrayList var2 = new ArrayList();

      while(var0.getPosition() < var1) {
         Metadata.Entry var3 = MetadataUtil.parseIlstElement(var0);
         if (var3 != null) {
            var2.add(var3);
         }
      }

      Metadata var4;
      if (var2.isEmpty()) {
         var4 = null;
      } else {
         var4 = new Metadata(var2);
      }

      return var4;
   }

   private static Pair parseMdhd(ParsableByteArray var0) {
      byte var1 = 8;
      var0.setPosition(8);
      int var2 = Atom.parseFullAtomVersion(var0.readInt());
      byte var3;
      if (var2 == 0) {
         var3 = 8;
      } else {
         var3 = 16;
      }

      var0.skipBytes(var3);
      long var4 = var0.readUnsignedInt();
      var3 = var1;
      if (var2 == 0) {
         var3 = 4;
      }

      var0.skipBytes(var3);
      int var7 = var0.readUnsignedShort();
      StringBuilder var6 = new StringBuilder();
      var6.append("");
      var6.append((char)((var7 >> 10 & 31) + 96));
      var6.append((char)((var7 >> 5 & 31) + 96));
      var6.append((char)((var7 & 31) + 96));
      return Pair.create(var4, var6.toString());
   }

   public static Metadata parseMdtaFromMeta(Atom.ContainerAtom var0) {
      Atom.LeafAtom var1 = var0.getLeafAtomOfType(Atom.TYPE_hdlr);
      Atom.LeafAtom var2 = var0.getLeafAtomOfType(Atom.TYPE_keys);
      Atom.LeafAtom var3 = var0.getLeafAtomOfType(Atom.TYPE_ilst);
      Object var4 = null;
      Metadata var8 = (Metadata)var4;
      if (var1 != null) {
         var8 = (Metadata)var4;
         if (var2 != null) {
            var8 = (Metadata)var4;
            if (var3 != null) {
               if (parseHdlr(var1.data) != TYPE_mdta) {
                  var8 = (Metadata)var4;
               } else {
                  ParsableByteArray var12 = var2.data;
                  var12.setPosition(12);
                  int var5 = var12.readInt();
                  String[] var10 = new String[var5];

                  int var6;
                  int var7;
                  for(var6 = 0; var6 < var5; ++var6) {
                     var7 = var12.readInt();
                     var12.skipBytes(4);
                     var10[var6] = var12.readString(var7 - 8);
                  }

                  var12 = var3.data;
                  var12.setPosition(8);

                  ArrayList var13;
                  for(var13 = new ArrayList(); var12.bytesLeft() > 8; var12.setPosition(var7 + var5)) {
                     var7 = var12.getPosition();
                     var5 = var12.readInt();
                     var6 = var12.readInt() - 1;
                     if (var6 >= 0 && var6 < var10.length) {
                        MdtaMetadataEntry var11 = MetadataUtil.parseMdtaMetadataEntryFromIlst(var12, var7 + var5, var10[var6]);
                        if (var11 != null) {
                           var13.add(var11);
                        }
                     } else {
                        StringBuilder var9 = new StringBuilder();
                        var9.append("Skipped metadata with unknown key index: ");
                        var9.append(var6);
                        Log.w("AtomParsers", var9.toString());
                     }
                  }

                  if (var13.isEmpty()) {
                     var8 = (Metadata)var4;
                  } else {
                     var8 = new Metadata(var13);
                  }
               }
            }
         }
      }

      return var8;
   }

   private static long parseMvhd(ParsableByteArray var0) {
      byte var1 = 8;
      var0.setPosition(8);
      if (Atom.parseFullAtomVersion(var0.readInt()) != 0) {
         var1 = 16;
      }

      var0.skipBytes(var1);
      return var0.readUnsignedInt();
   }

   private static float parsePaspFromParent(ParsableByteArray var0, int var1) {
      var0.setPosition(var1 + 8);
      var1 = var0.readUnsignedIntToInt();
      int var2 = var0.readUnsignedIntToInt();
      return (float)var1 / (float)var2;
   }

   private static byte[] parseProjFromParent(ParsableByteArray var0, int var1, int var2) {
      int var4;
      for(int var3 = var1 + 8; var3 - var1 < var2; var3 += var4) {
         var0.setPosition(var3);
         var4 = var0.readInt();
         if (var0.readInt() == Atom.TYPE_proj) {
            return Arrays.copyOfRange(var0.data, var3, var4 + var3);
         }
      }

      return null;
   }

   private static Pair parseSampleEntryEncryptionData(ParsableByteArray var0, int var1, int var2) {
      int var4;
      for(int var3 = var0.getPosition(); var3 - var1 < var2; var3 += var4) {
         var0.setPosition(var3);
         var4 = var0.readInt();
         boolean var5;
         if (var4 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assertions.checkArgument(var5, "childAtomSize should be positive");
         if (var0.readInt() == Atom.TYPE_sinf) {
            Pair var6 = parseCommonEncryptionSinfFromParent(var0, var3, var4);
            if (var6 != null) {
               return var6;
            }
         }
      }

      return null;
   }

   private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray var0, int var1, int var2, String var3) {
      int var4 = var1 + 8;

      while(true) {
         Object var5 = null;
         if (var4 - var1 >= var2) {
            return null;
         }

         var0.setPosition(var4);
         int var6 = var0.readInt();
         if (var0.readInt() == Atom.TYPE_tenc) {
            var1 = Atom.parseFullAtomVersion(var0.readInt());
            var0.skipBytes(1);
            if (var1 == 0) {
               var0.skipBytes(1);
               var2 = 0;
               var1 = 0;
            } else {
               var2 = var0.readUnsignedByte();
               var1 = var2 & 15;
               var2 = (var2 & 240) >> 4;
            }

            boolean var7;
            if (var0.readUnsignedByte() == 1) {
               var7 = true;
            } else {
               var7 = false;
            }

            var6 = var0.readUnsignedByte();
            byte[] var8 = new byte[16];
            var0.readBytes(var8, 0, var8.length);
            byte[] var9 = (byte[])var5;
            if (var7) {
               var9 = (byte[])var5;
               if (var6 == 0) {
                  var4 = var0.readUnsignedByte();
                  var9 = new byte[var4];
                  var0.readBytes(var9, 0, var4);
               }
            }

            return new TrackEncryptionBox(var7, var3, var6, var8, var2, var1, var9);
         }

         var4 += var6;
      }
   }

   public static TrackSampleTable parseStbl(Track var0, Atom.ContainerAtom var1, GaplessInfoHolder var2) throws ParserException {
      Atom.LeafAtom var4 = var1.getLeafAtomOfType(Atom.TYPE_stsz);
      Object var5;
      if (var4 != null) {
         var5 = new AtomParsers.StszSampleSizeBox(var4);
      } else {
         var4 = var1.getLeafAtomOfType(Atom.TYPE_stz2);
         if (var4 == null) {
            throw new ParserException("Track has no sample table size information");
         }

         var5 = new AtomParsers.Stz2SampleSizeBox(var4);
      }

      int var6 = ((AtomParsers.SampleSizeBox)var5).getSampleCount();
      if (var6 == 0) {
         return new TrackSampleTable(var0, new long[0], new int[0], 0, new long[0], new int[0], -9223372036854775807L);
      } else {
         var4 = var1.getLeafAtomOfType(Atom.TYPE_stco);
         boolean var7;
         if (var4 == null) {
            var4 = var1.getLeafAtomOfType(Atom.TYPE_co64);
            var7 = true;
         } else {
            var7 = false;
         }

         ParsableByteArray var8 = var4.data;
         ParsableByteArray var9 = var1.getLeafAtomOfType(Atom.TYPE_stsc).data;
         ParsableByteArray var10 = var1.getLeafAtomOfType(Atom.TYPE_stts).data;
         var4 = var1.getLeafAtomOfType(Atom.TYPE_stss);
         ParsableByteArray var42;
         if (var4 != null) {
            var42 = var4.data;
         } else {
            var42 = null;
         }

         Atom.LeafAtom var36 = var1.getLeafAtomOfType(Atom.TYPE_ctts);
         ParsableByteArray var11;
         if (var36 != null) {
            var11 = var36.data;
         } else {
            var11 = null;
         }

         AtomParsers.ChunkIterator var12 = new AtomParsers.ChunkIterator(var9, var8, var7);
         var10.setPosition(12);
         int var13 = var10.readUnsignedIntToInt() - 1;
         int var14 = var10.readUnsignedIntToInt();
         int var15 = var10.readUnsignedIntToInt();
         int var16;
         if (var11 != null) {
            var11.setPosition(12);
            var16 = var11.readUnsignedIntToInt();
         } else {
            var16 = 0;
         }

         int var17 = -1;
         int var18;
         if (var42 != null) {
            var42.setPosition(12);
            var18 = var42.readUnsignedIntToInt();
            if (var18 > 0) {
               var17 = var42.readUnsignedIntToInt() - 1;
            } else {
               var42 = null;
            }
         } else {
            var18 = 0;
         }

         boolean var19;
         if (((AtomParsers.SampleSizeBox)var5).isFixedSampleSize() && "audio/raw".equals(var0.format.sampleMimeType) && var13 == 0 && var16 == 0 && var18 == 0) {
            var19 = true;
         } else {
            var19 = false;
         }

         int[] var3;
         long var20;
         long var22;
         long var32;
         long[] var39;
         long[] var40;
         long[] var43;
         int[] var44;
         int[] var48;
         long[] var51;
         long[] var57;
         boolean var58;
         int var61;
         if (!var19) {
            long[] var49 = new long[var6];
            int[] var37 = new int[var6];
            var51 = new long[var6];
            var3 = new int[var6];
            var20 = 0L;
            var22 = var20;
            byte var24 = 0;
            int var25 = 0;
            int var26 = 0;
            byte var60 = 0;
            int var27 = var18;
            byte var28 = 0;
            var18 = var60;
            var61 = var28;
            int var65 = var17;
            var17 = var24;

            int var63;
            while(true) {
               if (var25 >= var6) {
                  var63 = var14;
                  var14 = var6;
                  var43 = var51;
                  var48 = var3;
                  var40 = var49;
                  break;
               }

               var7 = true;

               boolean var29;
               while(true) {
                  var29 = var7;
                  if (var61 != 0) {
                     break;
                  }

                  var7 = var12.moveNext();
                  var29 = var7;
                  if (!var7) {
                     break;
                  }

                  var22 = var12.offset;
                  var61 = var12.numSamples;
               }

               var63 = var14;
               if (!var29) {
                  Log.w("AtomParsers", "Unexpected end of chunk data");
                  long[] var47 = Arrays.copyOf(var49, var25);
                  var37 = Arrays.copyOf(var37, var25);
                  var43 = Arrays.copyOf(var51, var25);
                  int[] var52 = Arrays.copyOf(var3, var25);
                  var14 = var25;
                  var40 = var47;
                  var48 = var52;
                  break;
               }

               if (var11 != null) {
                  while(var26 == 0 && var16 > 0) {
                     var26 = var11.readUnsignedIntToInt();
                     var18 = var11.readInt();
                     --var16;
                  }

                  --var26;
               }

               var49[var25] = var22;
               var37[var25] = ((AtomParsers.SampleSizeBox)var5).readNextSampleSize();
               var14 = var17;
               if (var37[var25] > var17) {
                  var14 = var37[var25];
               }

               var51[var25] = var20 + (long)var18;
               byte var59;
               if (var42 == null) {
                  var59 = 1;
               } else {
                  var59 = 0;
               }

               var3[var25] = var59;
               if (var25 == var65) {
                  var3[var25] = 1;
                  --var27;
                  if (var27 > 0) {
                     var65 = var42.readUnsignedIntToInt() - 1;
                  }
               }

               var20 += (long)var15;
               int var30 = var63 - 1;
               var17 = var30;
               int var31 = var15;
               var63 = var13;
               if (var30 == 0) {
                  var17 = var30;
                  var31 = var15;
                  var63 = var13;
                  if (var13 > 0) {
                     var17 = var10.readUnsignedIntToInt();
                     var31 = var10.readInt();
                     var63 = var13 - 1;
                  }
               }

               var32 = (long)var37[var25];
               var13 = var61 - 1;
               ++var25;
               var61 = var17;
               var22 += var32;
               var17 = var14;
               var15 = var31;
               var14 = var61;
               var61 = var13;
               var13 = var63;
            }

            var22 = var20 + (long)var18;

            while(true) {
               if (var16 <= 0) {
                  var58 = true;
                  break;
               }

               if (var11.readUnsignedIntToInt() != 0) {
                  var58 = false;
                  break;
               }

               var11.readInt();
               --var16;
            }

            if (var27 != 0 || var63 != 0 || var61 != 0 || var13 != 0 || var26 != 0 || !var58) {
               StringBuilder var53 = new StringBuilder();
               var53.append("Inconsistent stbl box for track ");
               var53.append(var0.id);
               var53.append(": remainingSynchronizationSamples ");
               var53.append(var27);
               var53.append(", remainingSamplesAtTimestampDelta ");
               var53.append(var63);
               var53.append(", remainingSamplesInChunk ");
               var53.append(var61);
               var53.append(", remainingTimestampDeltaChanges ");
               var53.append(var13);
               var53.append(", remainingSamplesAtTimestampOffset ");
               var53.append(var26);
               String var55;
               if (!var58) {
                  var55 = ", ctts invalid";
               } else {
                  var55 = "";
               }

               var53.append(var55);
               Log.w("AtomParsers", var53.toString());
            }

            var57 = var43;
            var16 = var17;
            var44 = var37;
            var39 = var40;
         } else {
            var14 = var6;
            var16 = var12.length;
            var39 = new long[var16];

            for(var48 = new int[var16]; var12.moveNext(); var48[var16] = var12.numSamples) {
               var16 = var12.index;
               var39[var16] = var12.offset;
            }

            Format var46 = var0.format;
            FixedSampleSizeRechunker.Results var41 = FixedSampleSizeRechunker.rechunk(Util.getPcmFrameSize(var46.pcmEncoding, var46.channelCount), var39, var48, (long)var15);
            var39 = var41.offsets;
            var44 = var41.sizes;
            var16 = var41.maximumSize;
            var57 = var41.timestamps;
            var48 = var41.flags;
            var22 = var41.duration;
         }

         Track var56 = var0;
         var20 = Util.scaleLargeTimestamp(var22, 1000000L, var0.timescale);
         if (var0.editListDurations != null && !var2.hasGaplessInfo()) {
            var40 = var0.editListDurations;
            if (var40.length == 1 && var0.type == 1 && var57.length >= 2) {
               var20 = var0.editListMediaTimes[0];
               var32 = var20 + Util.scaleLargeTimestamp(var40[0], var0.timescale, var0.movieTimescale);
               if (canApplyEditWithGaplessInfo(var57, var22, var20, var32)) {
                  var20 = Util.scaleLargeTimestamp(var20 - var57[0], (long)var0.format.sampleRate, var0.timescale);
                  var32 = Util.scaleLargeTimestamp(var22 - var32, (long)var0.format.sampleRate, var0.timescale);
                  if ((var20 != 0L || var32 != 0L) && var20 <= 2147483647L && var32 <= 2147483647L) {
                     var2.encoderDelay = (int)var20;
                     var2.encoderPadding = (int)var32;
                     Util.scaleLargeTimestampsInPlace(var57, 1000000L, var0.timescale);
                     return new TrackSampleTable(var0, var39, var44, var16, var57, var48, Util.scaleLargeTimestamp(var0.editListDurations[0], 1000000L, var0.movieTimescale));
                  }
               }
            }

            var18 = var16;
            var43 = var0.editListDurations;
            if (var43.length == 1 && var43[0] == 0L) {
               var20 = var0.editListMediaTimes[0];

               for(var16 = 0; var16 < var57.length; ++var16) {
                  var57[var16] = Util.scaleLargeTimestamp(var57[var16] - var20, 1000000L, var56.timescale);
               }

               return new TrackSampleTable(var0, var39, var44, var18, var57, var48, Util.scaleLargeTimestamp(var22 - var20, 1000000L, var56.timescale));
            } else {
               if (var0.type == 1) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               var43 = var0.editListDurations;
               int[] var34 = new int[var43.length];
               int[] var54 = new int[var43.length];
               var6 = 0;
               var58 = false;
               var61 = 0;
               var17 = 0;

               while(true) {
                  var43 = var56.editListDurations;
                  boolean var62;
                  if (var6 >= var43.length) {
                     byte var45 = 0;
                     var62 = true;
                     if (var61 == var14) {
                        var62 = false;
                     }

                     boolean var64 = var58 | var62;
                     if (var64) {
                        var51 = new long[var61];
                     } else {
                        var51 = var39;
                     }

                     int[] var50;
                     if (var64) {
                        var50 = new int[var61];
                     } else {
                        var50 = var44;
                     }

                     if (var64) {
                        var16 = 0;
                     } else {
                        var16 = var16;
                     }

                     if (var64) {
                        var3 = new int[var61];
                     } else {
                        var3 = var48;
                     }

                     long[] var35 = new long[var61];
                     var22 = 0L;
                     var18 = 0;
                     var44 = var44;
                     int[] var38 = var3;
                     var17 = var45;

                     for(var3 = var34; var17 < var56.editListDurations.length; ++var17) {
                        var20 = var56.editListMediaTimes[var17];
                        var61 = var3[var17];
                        var14 = var54[var17];
                        if (var64) {
                           var6 = var14 - var61;
                           System.arraycopy(var39, var61, var51, var18, var6);
                           System.arraycopy(var44, var61, var50, var18, var6);
                           System.arraycopy(var48, var61, var38, var18, var6);
                        }

                        while(var61 < var14) {
                           var35[var18] = Util.scaleLargeTimestamp(var22, 1000000L, var56.movieTimescale) + Util.scaleLargeTimestamp(var57[var61] - var20, 1000000L, var56.timescale);
                           var6 = var16;
                           if (var64) {
                              var6 = var16;
                              if (var50[var18] > var16) {
                                 var6 = var44[var61];
                              }
                           }

                           ++var18;
                           ++var61;
                           var16 = var6;
                        }

                        var22 += var56.editListDurations[var17];
                     }

                     return new TrackSampleTable(var0, var51, var50, var16, var35, var38, Util.scaleLargeTimestamp(var22, 1000000L, var56.movieTimescale));
                  }

                  var20 = var56.editListMediaTimes[var6];
                  if (var20 == -1L) {
                     var16 = var17;
                     var62 = var58;
                  } else {
                     var22 = Util.scaleLargeTimestamp(var43[var6], var56.timescale, var56.movieTimescale);
                     var34[var6] = Util.binarySearchCeil(var57, var20, true, true);

                     int var10002;
                     for(var54[var6] = Util.binarySearchCeil(var57, var20 + var22, var7, false); var34[var6] < var54[var6] && (var48[var34[var6]] & 1) == 0; var10002 = var34[var6]++) {
                     }

                     var61 += var54[var6] - var34[var6];
                     if (var17 != var34[var6]) {
                        var62 = true;
                     } else {
                        var62 = false;
                     }

                     var62 |= var58;
                     var16 = var54[var6];
                  }

                  ++var6;
                  var17 = var16;
                  var58 = var62;
               }
            }
         } else {
            Util.scaleLargeTimestampsInPlace(var57, 1000000L, var0.timescale);
            return new TrackSampleTable(var0, var39, var44, var16, var57, var48, var20);
         }
      }
   }

   private static AtomParsers.StsdData parseStsd(ParsableByteArray var0, int var1, int var2, String var3, DrmInitData var4, boolean var5) throws ParserException {
      var0.setPosition(12);
      int var6 = var0.readInt();
      AtomParsers.StsdData var7 = new AtomParsers.StsdData(var6);

      for(int var8 = 0; var8 < var6; ++var8) {
         int var9 = var0.getPosition();
         int var10 = var0.readInt();
         boolean var11;
         if (var10 > 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         Assertions.checkArgument(var11, "childAtomSize should be positive");
         int var12 = var0.readInt();
         if (var12 != Atom.TYPE_avc1 && var12 != Atom.TYPE_avc3 && var12 != Atom.TYPE_encv && var12 != Atom.TYPE_mp4v && var12 != Atom.TYPE_hvc1 && var12 != Atom.TYPE_hev1 && var12 != Atom.TYPE_s263 && var12 != Atom.TYPE_vp08 && var12 != Atom.TYPE_vp09) {
            if (var12 != Atom.TYPE_mp4a && var12 != Atom.TYPE_enca && var12 != Atom.TYPE_ac_3 && var12 != Atom.TYPE_ec_3 && var12 != Atom.TYPE_dtsc && var12 != Atom.TYPE_dtse && var12 != Atom.TYPE_dtsh && var12 != Atom.TYPE_dtsl && var12 != Atom.TYPE_samr && var12 != Atom.TYPE_sawb && var12 != Atom.TYPE_lpcm && var12 != Atom.TYPE_sowt && var12 != Atom.TYPE__mp3 && var12 != Atom.TYPE_alac && var12 != Atom.TYPE_alaw && var12 != Atom.TYPE_ulaw && var12 != Atom.TYPE_Opus && var12 != Atom.TYPE_fLaC) {
               if (var12 != Atom.TYPE_TTML && var12 != Atom.TYPE_tx3g && var12 != Atom.TYPE_wvtt && var12 != Atom.TYPE_stpp && var12 != Atom.TYPE_c608) {
                  if (var12 == Atom.TYPE_camm) {
                     var7.format = Format.createSampleFormat(Integer.toString(var1), "application/x-camera-motion", (String)null, -1, (DrmInitData)null);
                  }
               } else {
                  parseTextSampleEntry(var0, var12, var9, var10, var1, var3, var7);
               }
            } else {
               parseAudioSampleEntry(var0, var12, var9, var10, var1, var3, var5, var4, var7, var8);
            }
         } else {
            parseVideoSampleEntry(var0, var12, var9, var10, var1, var2, var4, var7, var8);
         }

         var0.setPosition(var9 + var10);
      }

      return var7;
   }

   private static void parseTextSampleEntry(ParsableByteArray var0, int var1, int var2, int var3, int var4, String var5, AtomParsers.StsdData var6) throws ParserException {
      var0.setPosition(var2 + 8 + 8);
      var2 = Atom.TYPE_TTML;
      String var7 = "application/ttml+xml";
      List var8 = null;
      long var9 = Long.MAX_VALUE;
      String var11;
      if (var1 == var2) {
         var11 = var7;
      } else if (var1 == Atom.TYPE_tx3g) {
         var1 = var3 - 8 - 8;
         byte[] var12 = new byte[var1];
         var0.readBytes(var12, 0, var1);
         var8 = Collections.singletonList(var12);
         var11 = "application/x-quicktime-tx3g";
      } else if (var1 == Atom.TYPE_wvtt) {
         var11 = "application/x-mp4-vtt";
      } else if (var1 == Atom.TYPE_stpp) {
         var9 = 0L;
         var11 = var7;
      } else {
         if (var1 != Atom.TYPE_c608) {
            throw new IllegalStateException();
         }

         var6.requiredSampleTransformation = 1;
         var11 = "application/x-mp4-cea-608";
      }

      var6.format = Format.createTextSampleFormat(Integer.toString(var4), var11, (String)null, -1, 0, var5, -1, (DrmInitData)null, var9, var8);
   }

   private static AtomParsers.TkhdData parseTkhd(ParsableByteArray var0) {
      byte var1 = 8;
      var0.setPosition(8);
      int var2 = Atom.parseFullAtomVersion(var0.readInt());
      byte var3;
      if (var2 == 0) {
         var3 = 8;
      } else {
         var3 = 16;
      }

      var0.skipBytes(var3);
      int var4 = var0.readInt();
      var0.skipBytes(4);
      int var5 = var0.getPosition();
      var3 = var1;
      if (var2 == 0) {
         var3 = 4;
      }

      byte var6 = 0;
      int var12 = 0;

      boolean var13;
      while(true) {
         if (var12 >= var3) {
            var13 = true;
            break;
         }

         if (var0.data[var5 + var12] != -1) {
            var13 = false;
            break;
         }

         ++var12;
      }

      long var7 = -9223372036854775807L;
      long var9;
      if (var13) {
         var0.skipBytes(var3);
         var9 = var7;
      } else {
         if (var2 == 0) {
            var9 = var0.readUnsignedInt();
         } else {
            var9 = var0.readUnsignedLongToLong();
         }

         if (var9 == 0L) {
            var9 = var7;
         }
      }

      var0.skipBytes(16);
      var2 = var0.readInt();
      int var11 = var0.readInt();
      var0.skipBytes(4);
      var5 = var0.readInt();
      var12 = var0.readInt();
      short var14;
      if (var2 == 0 && var11 == 65536 && var5 == -65536 && var12 == 0) {
         var14 = 90;
      } else if (var2 == 0 && var11 == -65536 && var5 == 65536 && var12 == 0) {
         var14 = 270;
      } else {
         var14 = var6;
         if (var2 == -65536) {
            var14 = var6;
            if (var11 == 0) {
               var14 = var6;
               if (var5 == 0) {
                  var14 = var6;
                  if (var12 == -65536) {
                     var14 = 180;
                  }
               }
            }
         }
      }

      return new AtomParsers.TkhdData(var4, var9, var14);
   }

   public static Track parseTrak(Atom.ContainerAtom var0, Atom.LeafAtom var1, long var2, DrmInitData var4, boolean var5, boolean var6) throws ParserException {
      Atom.ContainerAtom var7 = var0.getContainerAtomOfType(Atom.TYPE_mdia);
      int var8 = getTrackTypeForHdlr(parseHdlr(var7.getLeafAtomOfType(Atom.TYPE_hdlr).data));
      if (var8 == -1) {
         return null;
      } else {
         AtomParsers.TkhdData var9 = parseTkhd(var0.getLeafAtomOfType(Atom.TYPE_tkhd).data);
         long var10 = -9223372036854775807L;
         if (var2 == -9223372036854775807L) {
            var2 = var9.duration;
         }

         long var12 = parseMvhd(var1.data);
         if (var2 == -9223372036854775807L) {
            var2 = var10;
         } else {
            var2 = Util.scaleLargeTimestamp(var2, 1000000L, var12);
         }

         Atom.ContainerAtom var16 = var7.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
         Pair var20 = parseMdhd(var7.getLeafAtomOfType(Atom.TYPE_mdhd).data);
         AtomParsers.StsdData var19 = parseStsd(var16.getLeafAtomOfType(Atom.TYPE_stsd).data, var9.id, var9.rotationDegrees, (String)var20.second, var4, var6);
         long[] var14;
         long[] var18;
         if (!var5) {
            Pair var17 = parseEdts(var0.getContainerAtomOfType(Atom.TYPE_edts));
            var14 = (long[])var17.first;
            var18 = (long[])var17.second;
         } else {
            var14 = null;
            var18 = var14;
         }

         Track var15;
         if (var19.format == null) {
            var15 = null;
         } else {
            var15 = new Track(var9.id, var8, (Long)var20.first, var12, var2, var19.format, var19.requiredSampleTransformation, var19.trackEncryptionBoxes, var19.nalUnitLengthFieldLength, var14, var18);
         }

         return var15;
      }
   }

   public static Metadata parseUdta(Atom.LeafAtom var0, boolean var1) {
      if (var1) {
         return null;
      } else {
         ParsableByteArray var4 = var0.data;
         var4.setPosition(8);

         while(var4.bytesLeft() >= 8) {
            int var2 = var4.getPosition();
            int var3 = var4.readInt();
            if (var4.readInt() == Atom.TYPE_meta) {
               var4.setPosition(var2);
               return parseUdtaMeta(var4, var2 + var3);
            }

            var4.setPosition(var2 + var3);
         }

         return null;
      }
   }

   private static Metadata parseUdtaMeta(ParsableByteArray var0, int var1) {
      var0.skipBytes(12);

      while(var0.getPosition() < var1) {
         int var2 = var0.getPosition();
         int var3 = var0.readInt();
         if (var0.readInt() == Atom.TYPE_ilst) {
            var0.setPosition(var2);
            return parseIlst(var0, var2 + var3);
         }

         var0.setPosition(var2 + var3);
      }

      return null;
   }

   private static void parseVideoSampleEntry(ParsableByteArray var0, int var1, int var2, int var3, int var4, int var5, DrmInitData var6, AtomParsers.StsdData var7, int var8) throws ParserException {
      var0.setPosition(var2 + 8 + 8);
      var0.skipBytes(16);
      int var9 = var0.readUnsignedShort();
      int var10 = var0.readUnsignedShort();
      var0.skipBytes(50);
      int var11 = var0.getPosition();
      int var12 = Atom.TYPE_encv;
      String var13 = null;
      DrmInitData var14 = var6;
      int var15 = var1;
      if (var1 == var12) {
         Pair var33 = parseSampleEntryEncryptionData(var0, var2, var3);
         DrmInitData var16 = var6;
         if (var33 != null) {
            var1 = (Integer)var33.first;
            if (var6 == null) {
               var16 = null;
            } else {
               var16 = var6.copyWithSchemeType(((TrackEncryptionBox)var33.second).schemeType);
            }

            var7.trackEncryptionBoxes[var8] = (TrackEncryptionBox)var33.second;
         }

         var0.setPosition(var11);
         var15 = var1;
         var14 = var16;
      }

      List var17 = null;
      Object var18 = var17;
      boolean var26 = false;
      float var19 = 1.0F;
      byte var32 = -1;
      var8 = var11;

      byte var27;
      for(boolean var31 = var26; var8 - var2 < var3; var32 = var27) {
         var0.setPosition(var8);
         var1 = var0.getPosition();
         int var20 = var0.readInt();
         if (var20 == 0 && var0.getPosition() - var2 == var3) {
            break;
         }

         boolean var21;
         if (var20 > 0) {
            var21 = true;
         } else {
            var21 = false;
         }

         Assertions.checkArgument(var21, "childAtomSize should be positive");
         int var22 = var0.readInt();
         float var23;
         boolean var24;
         Object var25;
         String var29;
         List var34;
         if (var22 == Atom.TYPE_avcC) {
            if (var13 == null) {
               var21 = true;
            } else {
               var21 = false;
            }

            Assertions.checkState(var21);
            var0.setPosition(var1 + 8);
            AvcConfig var28 = AvcConfig.parse(var0);
            var34 = var28.initializationData;
            var7.nalUnitLengthFieldLength = var28.nalUnitLengthFieldLength;
            var23 = var19;
            if (!var31) {
               var23 = var28.pixelWidthAspectRatio;
            }

            var29 = "video/avc";
            var24 = var31;
            var25 = var18;
            var27 = var32;
         } else if (var22 == Atom.TYPE_hvcC) {
            if (var13 == null) {
               var21 = true;
            } else {
               var21 = false;
            }

            Assertions.checkState(var21);
            var0.setPosition(var1 + 8);
            HevcConfig var30 = HevcConfig.parse(var0);
            var34 = var30.initializationData;
            var7.nalUnitLengthFieldLength = var30.nalUnitLengthFieldLength;
            var29 = "video/hevc";
            var24 = var31;
            var23 = var19;
            var25 = var18;
            var27 = var32;
         } else if (var22 == Atom.TYPE_vpcC) {
            if (var13 == null) {
               var21 = true;
            } else {
               var21 = false;
            }

            Assertions.checkState(var21);
            if (var15 == Atom.TYPE_vp08) {
               var29 = "video/x-vnd.on2.vp8";
            } else {
               var29 = "video/x-vnd.on2.vp9";
            }

            var24 = var31;
            var34 = var17;
            var23 = var19;
            var25 = var18;
            var27 = var32;
         } else if (var22 == Atom.TYPE_d263) {
            if (var13 == null) {
               var21 = true;
            } else {
               var21 = false;
            }

            Assertions.checkState(var21);
            var29 = "video/3gpp";
            var24 = var31;
            var34 = var17;
            var23 = var19;
            var25 = var18;
            var27 = var32;
         } else if (var22 == Atom.TYPE_esds) {
            if (var13 == null) {
               var21 = true;
            } else {
               var21 = false;
            }

            Assertions.checkState(var21);
            Pair var35 = parseEsdsFromParent(var0, var1);
            var29 = (String)var35.first;
            var34 = Collections.singletonList(var35.second);
            var24 = var31;
            var23 = var19;
            var25 = var18;
            var27 = var32;
         } else if (var22 == Atom.TYPE_pasp) {
            var23 = parsePaspFromParent(var0, var1);
            var24 = true;
            var29 = var13;
            var34 = var17;
            var25 = var18;
            var27 = var32;
         } else if (var22 == Atom.TYPE_sv3d) {
            var25 = parseProjFromParent(var0, var1, var20);
            var24 = var31;
            var29 = var13;
            var34 = var17;
            var23 = var19;
            var27 = var32;
         } else {
            var24 = var31;
            var29 = var13;
            var34 = var17;
            var23 = var19;
            var25 = var18;
            var27 = var32;
            if (var22 == Atom.TYPE_st3d) {
               var22 = var0.readUnsignedByte();
               var0.skipBytes(3);
               var24 = var31;
               var29 = var13;
               var34 = var17;
               var23 = var19;
               var25 = var18;
               var27 = var32;
               if (var22 == 0) {
                  var1 = var0.readUnsignedByte();
                  if (var1 != 0) {
                     if (var1 != 1) {
                        if (var1 != 2) {
                           if (var1 != 3) {
                              var24 = var31;
                              var29 = var13;
                              var34 = var17;
                              var23 = var19;
                              var25 = var18;
                              var27 = var32;
                           } else {
                              var27 = 3;
                              var24 = var31;
                              var29 = var13;
                              var34 = var17;
                              var23 = var19;
                              var25 = var18;
                           }
                        } else {
                           var27 = 2;
                           var24 = var31;
                           var29 = var13;
                           var34 = var17;
                           var23 = var19;
                           var25 = var18;
                        }
                     } else {
                        var27 = 1;
                        var24 = var31;
                        var29 = var13;
                        var34 = var17;
                        var23 = var19;
                        var25 = var18;
                     }
                  } else {
                     var27 = 0;
                     var25 = var18;
                     var23 = var19;
                     var34 = var17;
                     var29 = var13;
                     var24 = var31;
                  }
               }
            }
         }

         var8 += var20;
         var31 = var24;
         var13 = var29;
         var17 = var34;
         var19 = var23;
         var18 = var25;
      }

      if (var13 != null) {
         var7.format = Format.createVideoSampleFormat(Integer.toString(var4), var13, (String)null, -1, -1, var9, var10, -1.0F, var17, var5, var19, (byte[])var18, var32, (ColorInfo)null, var14);
      }
   }

   private static final class ChunkIterator {
      private final ParsableByteArray chunkOffsets;
      private final boolean chunkOffsetsAreLongs;
      public int index;
      public final int length;
      private int nextSamplesPerChunkChangeIndex;
      public int numSamples;
      public long offset;
      private int remainingSamplesPerChunkChanges;
      private final ParsableByteArray stsc;

      public ChunkIterator(ParsableByteArray var1, ParsableByteArray var2, boolean var3) {
         this.stsc = var1;
         this.chunkOffsets = var2;
         this.chunkOffsetsAreLongs = var3;
         var2.setPosition(12);
         this.length = var2.readUnsignedIntToInt();
         var1.setPosition(12);
         this.remainingSamplesPerChunkChanges = var1.readUnsignedIntToInt();
         int var4 = var1.readInt();
         var3 = true;
         if (var4 != 1) {
            var3 = false;
         }

         Assertions.checkState(var3, "first_chunk must be 1");
         this.index = -1;
      }

      public boolean moveNext() {
         int var1 = this.index + 1;
         this.index = var1;
         if (var1 == this.length) {
            return false;
         } else {
            long var2;
            if (this.chunkOffsetsAreLongs) {
               var2 = this.chunkOffsets.readUnsignedLongToLong();
            } else {
               var2 = this.chunkOffsets.readUnsignedInt();
            }

            this.offset = var2;
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
               this.numSamples = this.stsc.readUnsignedIntToInt();
               this.stsc.skipBytes(4);
               var1 = this.remainingSamplesPerChunkChanges - 1;
               this.remainingSamplesPerChunkChanges = var1;
               if (var1 > 0) {
                  var1 = this.stsc.readUnsignedIntToInt() - 1;
               } else {
                  var1 = -1;
               }

               this.nextSamplesPerChunkChangeIndex = var1;
            }

            return true;
         }
      }
   }

   private interface SampleSizeBox {
      int getSampleCount();

      boolean isFixedSampleSize();

      int readNextSampleSize();
   }

   private static final class StsdData {
      public Format format;
      public int nalUnitLengthFieldLength;
      public int requiredSampleTransformation;
      public final TrackEncryptionBox[] trackEncryptionBoxes;

      public StsdData(int var1) {
         this.trackEncryptionBoxes = new TrackEncryptionBox[var1];
         this.requiredSampleTransformation = 0;
      }
   }

   static final class StszSampleSizeBox implements AtomParsers.SampleSizeBox {
      private final ParsableByteArray data;
      private final int fixedSampleSize;
      private final int sampleCount;

      public StszSampleSizeBox(Atom.LeafAtom var1) {
         this.data = var1.data;
         this.data.setPosition(12);
         this.fixedSampleSize = this.data.readUnsignedIntToInt();
         this.sampleCount = this.data.readUnsignedIntToInt();
      }

      public int getSampleCount() {
         return this.sampleCount;
      }

      public boolean isFixedSampleSize() {
         boolean var1;
         if (this.fixedSampleSize != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public int readNextSampleSize() {
         int var1 = this.fixedSampleSize;
         int var2 = var1;
         if (var1 == 0) {
            var2 = this.data.readUnsignedIntToInt();
         }

         return var2;
      }
   }

   static final class Stz2SampleSizeBox implements AtomParsers.SampleSizeBox {
      private int currentByte;
      private final ParsableByteArray data;
      private final int fieldSize;
      private final int sampleCount;
      private int sampleIndex;

      public Stz2SampleSizeBox(Atom.LeafAtom var1) {
         this.data = var1.data;
         this.data.setPosition(12);
         this.fieldSize = this.data.readUnsignedIntToInt() & 255;
         this.sampleCount = this.data.readUnsignedIntToInt();
      }

      public int getSampleCount() {
         return this.sampleCount;
      }

      public boolean isFixedSampleSize() {
         return false;
      }

      public int readNextSampleSize() {
         int var1 = this.fieldSize;
         if (var1 == 8) {
            return this.data.readUnsignedByte();
         } else if (var1 == 16) {
            return this.data.readUnsignedShort();
         } else {
            var1 = this.sampleIndex++;
            if (var1 % 2 == 0) {
               this.currentByte = this.data.readUnsignedByte();
               return (this.currentByte & 240) >> 4;
            } else {
               return this.currentByte & 15;
            }
         }
      }
   }

   private static final class TkhdData {
      private final long duration;
      private final int id;
      private final int rotationDegrees;

      public TkhdData(int var1, long var2, int var4) {
         this.id = var1;
         this.duration = var2;
         this.rotationDegrees = var4;
      }
   }
}
