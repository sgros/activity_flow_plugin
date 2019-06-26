package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

final class ScriptTagPayloadReader extends TagPayloadReader {
   private long durationUs = -9223372036854775807L;

   public ScriptTagPayloadReader() {
      super((TrackOutput)null);
   }

   private static Boolean readAmfBoolean(ParsableByteArray var0) {
      int var1 = var0.readUnsignedByte();
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   private static Object readAmfData(ParsableByteArray var0, int var1) {
      if (var1 != 0) {
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 != 8) {
                     if (var1 != 10) {
                        return var1 != 11 ? null : readAmfDate(var0);
                     } else {
                        return readAmfStrictArray(var0);
                     }
                  } else {
                     return readAmfEcmaArray(var0);
                  }
               } else {
                  return readAmfObject(var0);
               }
            } else {
               return readAmfString(var0);
            }
         } else {
            return readAmfBoolean(var0);
         }
      } else {
         return readAmfDouble(var0);
      }
   }

   private static Date readAmfDate(ParsableByteArray var0) {
      Date var1 = new Date((long)readAmfDouble(var0));
      var0.skipBytes(2);
      return var1;
   }

   private static Double readAmfDouble(ParsableByteArray var0) {
      return Double.longBitsToDouble(var0.readLong());
   }

   private static HashMap readAmfEcmaArray(ParsableByteArray var0) {
      int var1 = var0.readUnsignedIntToInt();
      HashMap var2 = new HashMap(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.put(readAmfString(var0), readAmfData(var0, readAmfType(var0)));
      }

      return var2;
   }

   private static HashMap readAmfObject(ParsableByteArray var0) {
      HashMap var1 = new HashMap();

      while(true) {
         String var2 = readAmfString(var0);
         int var3 = readAmfType(var0);
         if (var3 == 9) {
            return var1;
         }

         var1.put(var2, readAmfData(var0, var3));
      }
   }

   private static ArrayList readAmfStrictArray(ParsableByteArray var0) {
      int var1 = var0.readUnsignedIntToInt();
      ArrayList var2 = new ArrayList(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.add(readAmfData(var0, readAmfType(var0)));
      }

      return var2;
   }

   private static String readAmfString(ParsableByteArray var0) {
      int var1 = var0.readUnsignedShort();
      int var2 = var0.getPosition();
      var0.skipBytes(var1);
      return new String(var0.data, var2, var1);
   }

   private static int readAmfType(ParsableByteArray var0) {
      return var0.readUnsignedByte();
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   protected boolean parseHeader(ParsableByteArray var1) {
      return true;
   }

   protected void parsePayload(ParsableByteArray var1, long var2) throws ParserException {
      if (readAmfType(var1) == 2) {
         if ("onMetaData".equals(readAmfString(var1))) {
            if (readAmfType(var1) == 8) {
               HashMap var6 = readAmfEcmaArray(var1);
               if (var6.containsKey("duration")) {
                  double var4 = (Double)var6.get("duration");
                  if (var4 > 0.0D) {
                     this.durationUs = (long)(var4 * 1000000.0D);
                  }
               }

            }
         }
      } else {
         throw new ParserException();
      }
   }
}
