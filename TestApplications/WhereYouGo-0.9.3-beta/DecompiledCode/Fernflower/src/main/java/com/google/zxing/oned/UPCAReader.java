package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import java.util.Map;

public final class UPCAReader extends UPCEANReader {
   private final UPCEANReader ean13Reader = new EAN13Reader();

   private static Result maybeReturnResult(Result var0) throws FormatException {
      String var1 = var0.getText();
      if (var1.charAt(0) == '0') {
         return new Result(var1.substring(1), (byte[])null, var0.getResultPoints(), BarcodeFormat.UPC_A);
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, FormatException {
      return maybeReturnResult(this.ean13Reader.decode(var1));
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, FormatException {
      return maybeReturnResult(this.ean13Reader.decode(var1, var2));
   }

   protected int decodeMiddle(BitArray var1, int[] var2, StringBuilder var3) throws NotFoundException {
      return this.ean13Reader.decodeMiddle(var1, var2, var3);
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, FormatException, ChecksumException {
      return maybeReturnResult(this.ean13Reader.decodeRow(var1, var2, var3));
   }

   public Result decodeRow(int var1, BitArray var2, int[] var3, Map var4) throws NotFoundException, FormatException, ChecksumException {
      return maybeReturnResult(this.ean13Reader.decodeRow(var1, var2, var3, var4));
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.UPC_A;
   }
}
