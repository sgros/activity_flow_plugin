package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KCallable;
import kotlin.reflect.KDeclarationContainer;

public abstract class CallableReference implements Serializable, KCallable {
   public static final Object NO_RECEIVER;
   protected final Object receiver;
   private transient KCallable reflected;

   static {
      NO_RECEIVER = CallableReference.NoReceiver.INSTANCE;
   }

   public CallableReference() {
      this(NO_RECEIVER);
   }

   protected CallableReference(Object var1) {
      this.receiver = var1;
   }

   public Object call(Object... var1) {
      return this.getReflected().call(var1);
   }

   public KCallable compute() {
      KCallable var1 = this.reflected;
      KCallable var2 = var1;
      if (var1 == null) {
         var2 = this.computeReflected();
         this.reflected = var2;
      }

      return var2;
   }

   protected abstract KCallable computeReflected();

   public Object getBoundReceiver() {
      return this.receiver;
   }

   public String getName() {
      throw new AbstractMethodError();
   }

   public KDeclarationContainer getOwner() {
      throw new AbstractMethodError();
   }

   protected KCallable getReflected() {
      KCallable var1 = this.compute();
      if (var1 != this) {
         return var1;
      } else {
         throw new KotlinReflectionNotSupportedError();
      }
   }

   public String getSignature() {
      throw new AbstractMethodError();
   }

   private static class NoReceiver implements Serializable {
      private static final CallableReference.NoReceiver INSTANCE = new CallableReference.NoReceiver();
   }
}
