package se.krka.kahlua.vm;

public final class UpValue {
   public int index;
   public LuaThread thread;
   public Object value;

   public final Object getValue() {
      Object var1;
      if (this.thread == null) {
         var1 = this.value;
      } else {
         var1 = this.thread.objectStack[this.index];
      }

      return var1;
   }

   public final void setValue(Object var1) {
      if (this.thread == null) {
         this.value = var1;
      } else {
         this.thread.objectStack[this.index] = var1;
      }

   }
}
