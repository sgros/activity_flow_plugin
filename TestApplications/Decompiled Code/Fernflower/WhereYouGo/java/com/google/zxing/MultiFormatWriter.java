package com.google.zxing;

import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.oned.UPCEWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Map;

public final class MultiFormatWriter implements Writer {
   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      Object var6;
      switch(var2) {
      case EAN_8:
         var6 = new EAN8Writer();
         break;
      case UPC_E:
         var6 = new UPCEWriter();
         break;
      case EAN_13:
         var6 = new EAN13Writer();
         break;
      case UPC_A:
         var6 = new UPCAWriter();
         break;
      case QR_CODE:
         var6 = new QRCodeWriter();
         break;
      case CODE_39:
         var6 = new Code39Writer();
         break;
      case CODE_93:
         var6 = new Code93Writer();
         break;
      case CODE_128:
         var6 = new Code128Writer();
         break;
      case ITF:
         var6 = new ITFWriter();
         break;
      case PDF_417:
         var6 = new PDF417Writer();
         break;
      case CODABAR:
         var6 = new CodaBarWriter();
         break;
      case DATA_MATRIX:
         var6 = new DataMatrixWriter();
         break;
      case AZTEC:
         var6 = new AztecWriter();
         break;
      default:
         throw new IllegalArgumentException("No encoder available for format " + var2);
      }

      return ((Writer)var6).encode(var1, var2, var3, var4, var5);
   }
}
