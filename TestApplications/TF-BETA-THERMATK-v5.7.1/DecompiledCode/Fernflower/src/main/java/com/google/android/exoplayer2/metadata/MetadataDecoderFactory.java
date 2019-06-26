package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
import com.google.android.exoplayer2.metadata.icy.IcyDecoder;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.scte35.SpliceInfoDecoder;

public interface MetadataDecoderFactory {
   MetadataDecoderFactory DEFAULT = new MetadataDecoderFactory() {
      public MetadataDecoder createDecoder(Format var1) {
         byte var2;
         label40: {
            String var3 = var1.sampleMimeType;
            switch(var3.hashCode()) {
            case -1348231605:
               if (var3.equals("application/x-icy")) {
                  var2 = 3;
                  break label40;
               }
               break;
            case -1248341703:
               if (var3.equals("application/id3")) {
                  var2 = 0;
                  break label40;
               }
               break;
            case 1154383568:
               if (var3.equals("application/x-emsg")) {
                  var2 = 1;
                  break label40;
               }
               break;
            case 1652648887:
               if (var3.equals("application/x-scte35")) {
                  var2 = 2;
                  break label40;
               }
            }

            var2 = -1;
         }

         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 == 3) {
                     return new IcyDecoder();
                  } else {
                     throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
                  }
               } else {
                  return new SpliceInfoDecoder();
               }
            } else {
               return new EventMessageDecoder();
            }
         } else {
            return new Id3Decoder();
         }
      }

      public boolean supportsFormat(Format var1) {
         String var3 = var1.sampleMimeType;
         boolean var2;
         if (!"application/id3".equals(var3) && !"application/x-emsg".equals(var3) && !"application/x-scte35".equals(var3) && !"application/x-icy".equals(var3)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }
   };

   MetadataDecoder createDecoder(Format var1);

   boolean supportsFormat(Format var1);
}
