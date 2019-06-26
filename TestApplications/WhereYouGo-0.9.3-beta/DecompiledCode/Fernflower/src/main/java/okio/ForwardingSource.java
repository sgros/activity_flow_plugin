package okio;

import java.io.IOException;

public abstract class ForwardingSource implements Source {
   private final Source delegate;

   public ForwardingSource(Source var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("delegate == null");
      } else {
         this.delegate = var1;
      }
   }

   public void close() throws IOException {
      this.delegate.close();
   }

   public final Source delegate() {
      return this.delegate;
   }

   public long read(Buffer var1, long var2) throws IOException {
      return this.delegate.read(var1, var2);
   }

   public Timeout timeout() {
      return this.delegate.timeout();
   }

   public String toString() {
      return this.getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
   }
}
