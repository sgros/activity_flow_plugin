package okio;

import java.util.AbstractList;
import java.util.RandomAccess;

public final class Options extends AbstractList implements RandomAccess {
   final ByteString[] byteStrings;

   private Options(ByteString[] var1) {
      this.byteStrings = var1;
   }

   public static Options of(ByteString... var0) {
      return new Options((ByteString[])var0.clone());
   }

   public ByteString get(int var1) {
      return this.byteStrings[var1];
   }

   public int size() {
      return this.byteStrings.length;
   }
}
