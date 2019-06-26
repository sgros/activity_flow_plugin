package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.text.cea.Cea708InitializationData;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DefaultTsPayloadReaderFactory implements TsPayloadReader.Factory {
   private final List closedCaptionFormats;
   private final int flags;

   public DefaultTsPayloadReaderFactory(int var1) {
      this(var1, Collections.singletonList(Format.createTextSampleFormat((String)null, "application/cea-608", 0, (String)null)));
   }

   public DefaultTsPayloadReaderFactory(int var1, List var2) {
      this.flags = var1;
      this.closedCaptionFormats = var2;
   }

   private SeiReader buildSeiReader(TsPayloadReader.EsInfo var1) {
      return new SeiReader(this.getClosedCaptionFormats(var1));
   }

   private UserDataReader buildUserDataReader(TsPayloadReader.EsInfo var1) {
      return new UserDataReader(this.getClosedCaptionFormats(var1));
   }

   private List getClosedCaptionFormats(TsPayloadReader.EsInfo var1) {
      if (this.isSet(32)) {
         return this.closedCaptionFormats;
      } else {
         ParsableByteArray var2 = new ParsableByteArray(var1.descriptorBytes);

         int var4;
         int var5;
         Object var14;
         for(var14 = this.closedCaptionFormats; var2.bytesLeft() > 0; var2.setPosition(var5 + var4)) {
            int var3 = var2.readUnsignedByte();
            var4 = var2.readUnsignedByte();
            var5 = var2.getPosition();
            if (var3 == 134) {
               ArrayList var6 = new ArrayList();
               int var7 = var2.readUnsignedByte();
               var3 = 0;

               while(true) {
                  var14 = var6;
                  if (var3 >= (var7 & 31)) {
                     break;
                  }

                  String var8 = var2.readString(3);
                  int var9 = var2.readUnsignedByte();
                  boolean var10 = true;
                  boolean var11;
                  if ((var9 & 128) != 0) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  String var15;
                  if (var11) {
                     var9 &= 63;
                     var15 = "application/cea-708";
                  } else {
                     var15 = "application/cea-608";
                     var9 = 1;
                  }

                  byte var12 = (byte)var2.readUnsignedByte();
                  var2.skipBytes(1);
                  List var13;
                  if (var11) {
                     if ((var12 & 64) == 0) {
                        var10 = false;
                     }

                     var13 = Cea708InitializationData.buildData(var10);
                  } else {
                     var13 = null;
                  }

                  var6.add(Format.createTextSampleFormat((String)null, var15, (String)null, -1, 0, var8, var9, (DrmInitData)null, Long.MAX_VALUE, var13));
                  ++var3;
               }
            }
         }

         return (List)var14;
      }
   }

   private boolean isSet(int var1) {
      boolean var2;
      if ((var1 & this.flags) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public SparseArray createInitialPayloadReaders() {
      return new SparseArray();
   }

   public TsPayloadReader createPayloadReader(int var1, TsPayloadReader.EsInfo var2) {
      if (var1 == 2) {
         return new PesReader(new H262Reader(this.buildUserDataReader(var2)));
      } else if (var1 != 3 && var1 != 4) {
         Object var3 = null;
         Object var4 = null;
         Object var5 = null;
         Object var6 = null;
         PesReader var7;
         if (var1 != 15) {
            if (var1 == 17) {
               if (this.isSet(2)) {
                  var7 = (PesReader)var4;
               } else {
                  var7 = new PesReader(new LatmReader(var2.language));
               }

               return var7;
            } else if (var1 != 21) {
               if (var1 == 27) {
                  if (this.isSet(4)) {
                     var7 = (PesReader)var3;
                  } else {
                     var7 = new PesReader(new H264Reader(this.buildSeiReader(var2), this.isSet(1), this.isSet(8)));
                  }

                  return var7;
               } else if (var1 != 36) {
                  if (var1 == 89) {
                     return new PesReader(new DvbSubtitleReader(var2.dvbSubtitleInfos));
                  } else {
                     if (var1 != 138) {
                        if (var1 == 129) {
                           return new PesReader(new Ac3Reader(var2.language));
                        }

                        if (var1 != 130) {
                           if (var1 == 134) {
                              SectionReader var8;
                              if (this.isSet(16)) {
                                 var8 = (SectionReader)var6;
                              } else {
                                 var8 = new SectionReader(new SpliceInfoSectionReader());
                              }

                              return var8;
                           }

                           if (var1 != 135) {
                              return null;
                           }

                           return new PesReader(new Ac3Reader(var2.language));
                        }
                     }

                     return new PesReader(new DtsReader(var2.language));
                  }
               } else {
                  return new PesReader(new H265Reader(this.buildSeiReader(var2)));
               }
            } else {
               return new PesReader(new Id3Reader());
            }
         } else {
            if (this.isSet(2)) {
               var7 = (PesReader)var5;
            } else {
               var7 = new PesReader(new AdtsReader(false, var2.language));
            }

            return var7;
         }
      } else {
         return new PesReader(new MpegAudioReader(var2.language));
      }
   }
}
