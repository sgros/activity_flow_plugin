package kotlinx.coroutines.experimental;

final class CompletedIdempotentResult {
   public final Object result;

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("CompletedIdempotentResult[");
      var1.append(this.result);
      var1.append(']');
      return var1.toString();
   }
}
