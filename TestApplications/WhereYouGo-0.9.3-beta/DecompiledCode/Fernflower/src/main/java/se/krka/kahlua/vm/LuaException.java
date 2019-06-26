package se.krka.kahlua.vm;

public class LuaException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   public Object errorMessage;

   public LuaException(Object var1) {
      this.errorMessage = var1;
   }

   public String getMessage() {
      String var1;
      if (this.errorMessage == null) {
         var1 = "nil";
      } else {
         var1 = this.errorMessage.toString();
      }

      return var1;
   }
}
