package kotlin.properties;

public final class Delegates {
   public static final Delegates INSTANCE = new Delegates();

   private Delegates() {
   }

   public final ReadWriteProperty notNull() {
      return (ReadWriteProperty)(new NotNullVar());
   }
}
