package kotlinx.coroutines.experimental;

final class Empty implements Incomplete {
   private final boolean isActive;

   public Empty(boolean var1) {
      this.isActive = var1;
   }

   public NodeList getList() {
      return null;
   }

   public boolean isActive() {
      return this.isActive;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Empty{");
      String var2;
      if (this.isActive()) {
         var2 = "Active";
      } else {
         var2 = "New";
      }

      var1.append(var2);
      var1.append('}');
      return var1.toString();
   }
}
