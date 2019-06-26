package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public abstract class AbstractExpandedDecoder {
   private final GeneralAppIdDecoder generalDecoder;
   private final BitArray information;

   AbstractExpandedDecoder(BitArray var1) {
      this.information = var1;
      this.generalDecoder = new GeneralAppIdDecoder(var1);
   }

   public static AbstractExpandedDecoder createDecoder(BitArray var0) {
      Object var1;
      if (var0.get(1)) {
         var1 = new AI01AndOtherAIs(var0);
      } else if (!var0.get(2)) {
         var1 = new AnyAIDecoder(var0);
      } else {
         switch(GeneralAppIdDecoder.extractNumericValueFromBitArray(var0, 1, 4)) {
         case 4:
            var1 = new AI013103decoder(var0);
            break;
         case 5:
            var1 = new AI01320xDecoder(var0);
            break;
         default:
            switch(GeneralAppIdDecoder.extractNumericValueFromBitArray(var0, 1, 5)) {
            case 12:
               var1 = new AI01392xDecoder(var0);
               break;
            case 13:
               var1 = new AI01393xDecoder(var0);
               break;
            default:
               switch(GeneralAppIdDecoder.extractNumericValueFromBitArray(var0, 1, 7)) {
               case 56:
                  var1 = new AI013x0x1xDecoder(var0, "310", "11");
                  break;
               case 57:
                  var1 = new AI013x0x1xDecoder(var0, "320", "11");
                  break;
               case 58:
                  var1 = new AI013x0x1xDecoder(var0, "310", "13");
                  break;
               case 59:
                  var1 = new AI013x0x1xDecoder(var0, "320", "13");
                  break;
               case 60:
                  var1 = new AI013x0x1xDecoder(var0, "310", "15");
                  break;
               case 61:
                  var1 = new AI013x0x1xDecoder(var0, "320", "15");
                  break;
               case 62:
                  var1 = new AI013x0x1xDecoder(var0, "310", "17");
                  break;
               case 63:
                  var1 = new AI013x0x1xDecoder(var0, "320", "17");
                  break;
               default:
                  throw new IllegalStateException("unknown decoder: " + var0);
               }
            }
         }
      }

      return (AbstractExpandedDecoder)var1;
   }

   protected final GeneralAppIdDecoder getGeneralDecoder() {
      return this.generalDecoder;
   }

   protected final BitArray getInformation() {
      return this.information;
   }

   public abstract String parseInformation() throws NotFoundException, FormatException;
}
