package com.journeyapps.barcodescanner;

import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class DefaultDecoderFactory implements DecoderFactory {
   private String characterSet;
   private Collection decodeFormats;
   private Map hints;
   private boolean inverted;

   public DefaultDecoderFactory() {
   }

   public DefaultDecoderFactory(Collection var1, Map var2, String var3, boolean var4) {
      this.decodeFormats = var1;
      this.hints = var2;
      this.characterSet = var3;
      this.inverted = var4;
   }

   public Decoder createDecoder(Map var1) {
      EnumMap var2 = new EnumMap(DecodeHintType.class);
      var2.putAll(var1);
      if (this.hints != null) {
         var2.putAll(this.hints);
      }

      if (this.decodeFormats != null) {
         var2.put(DecodeHintType.POSSIBLE_FORMATS, this.decodeFormats);
      }

      if (this.characterSet != null) {
         var2.put(DecodeHintType.CHARACTER_SET, this.characterSet);
      }

      MultiFormatReader var3 = new MultiFormatReader();
      var3.setHints(var2);
      Object var4;
      if (this.inverted) {
         var4 = new InvertedDecoder(var3);
      } else {
         var4 = new Decoder(var3);
      }

      return (Decoder)var4;
   }
}
