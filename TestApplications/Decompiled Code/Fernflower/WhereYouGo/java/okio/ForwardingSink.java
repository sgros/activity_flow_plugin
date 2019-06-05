package okio;

import java.io.IOException;

public abstract class ForwardingSink implements Sink {
   private final Sink delegate;

   public ForwardingSink(Sink var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("delegate == null");
      } else {
         this.delegate = var1;
      }
   }

   public void close() throws IOException {
      this.delegate.close();
   }

   public final Sink delegate() {
      return this.delegate;
   }

   public void flush() throws IOException {
      this.delegate.flush();
   }

   public Timeout timeout() {
      return this.delegate.timeout();
   }

   public String toString() {
      return this.getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
   }

   public void write(Buffer var1, long var2) throws IOException {
      this.delegate.write(var1, var2);
   }
}
