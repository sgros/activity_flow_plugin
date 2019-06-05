package net.sqlcipher;

public class CursorIndexOutOfBoundsException extends IndexOutOfBoundsException {
   public CursorIndexOutOfBoundsException(int var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("Index ");
      var3.append(var1);
      var3.append(" requested, with a size of ");
      var3.append(var2);
      super(var3.toString());
   }

   public CursorIndexOutOfBoundsException(String var1) {
      super(var1);
   }
}
