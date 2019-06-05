package com.google.zxing.client.android;

import android.content.Intent;
import com.google.zxing.BarcodeFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class DecodeFormatManager {
   static final Set AZTEC_FORMATS;
   private static final Pattern COMMA_PATTERN = Pattern.compile(",");
   static final Set DATA_MATRIX_FORMATS;
   private static final Map FORMATS_FOR_MODE;
   static final Set INDUSTRIAL_FORMATS;
   private static final Set ONE_D_FORMATS;
   static final Set PDF417_FORMATS;
   static final Set PRODUCT_FORMATS;
   static final Set QR_CODE_FORMATS;

   static {
      QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
      DATA_MATRIX_FORMATS = EnumSet.of(BarcodeFormat.DATA_MATRIX);
      AZTEC_FORMATS = EnumSet.of(BarcodeFormat.AZTEC);
      PDF417_FORMATS = EnumSet.of(BarcodeFormat.PDF_417);
      PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED);
      INDUSTRIAL_FORMATS = EnumSet.of(BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODE_128, BarcodeFormat.ITF, BarcodeFormat.CODABAR);
      ONE_D_FORMATS = EnumSet.copyOf(PRODUCT_FORMATS);
      ONE_D_FORMATS.addAll(INDUSTRIAL_FORMATS);
      FORMATS_FOR_MODE = new HashMap();
      FORMATS_FOR_MODE.put("ONE_D_MODE", ONE_D_FORMATS);
      FORMATS_FOR_MODE.put("PRODUCT_MODE", PRODUCT_FORMATS);
      FORMATS_FOR_MODE.put("QR_CODE_MODE", QR_CODE_FORMATS);
      FORMATS_FOR_MODE.put("DATA_MATRIX_MODE", DATA_MATRIX_FORMATS);
      FORMATS_FOR_MODE.put("AZTEC_MODE", AZTEC_FORMATS);
      FORMATS_FOR_MODE.put("PDF417_MODE", PDF417_FORMATS);
   }

   private DecodeFormatManager() {
   }

   public static Set parseDecodeFormats(Intent var0) {
      List var1 = null;
      String var2 = var0.getStringExtra("SCAN_FORMATS");
      if (var2 != null) {
         var1 = Arrays.asList(COMMA_PATTERN.split(var2));
      }

      return parseDecodeFormats(var1, var0.getStringExtra("SCAN_MODE"));
   }

   private static Set parseDecodeFormats(Iterable var0, String var1) {
      Object var6;
      if (var0 != null) {
         label39: {
            EnumSet var2 = EnumSet.noneOf(BarcodeFormat.class);

            boolean var10001;
            Iterator var3;
            try {
               var3 = var0.iterator();
            } catch (IllegalArgumentException var5) {
               var10001 = false;
               break label39;
            }

            while(true) {
               var6 = var2;

               try {
                  if (!var3.hasNext()) {
                     return (Set)var6;
                  }

                  var2.add(BarcodeFormat.valueOf((String)var3.next()));
               } catch (IllegalArgumentException var4) {
                  var10001 = false;
                  break;
               }
            }
         }
      }

      if (var1 != null) {
         var6 = (Set)FORMATS_FOR_MODE.get(var1);
      } else {
         var6 = null;
      }

      return (Set)var6;
   }
}
