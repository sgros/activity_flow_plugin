package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;

public abstract class PropertyReference extends CallableReference implements KProperty {
   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PropertyReference)) {
         return var1 instanceof KProperty ? var1.equals(this.compute()) : false;
      } else {
         PropertyReference var3 = (PropertyReference)var1;
         if (!this.getOwner().equals(var3.getOwner()) || !this.getName().equals(var3.getName()) || !this.getSignature().equals(var3.getSignature()) || !Intrinsics.areEqual(this.getBoundReceiver(), var3.getBoundReceiver())) {
            var2 = false;
         }

         return var2;
      }
   }

   protected KProperty getReflected() {
      return (KProperty)super.getReflected();
   }

   public int hashCode() {
      return (this.getOwner().hashCode() * 31 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
   }

   public String toString() {
      KCallable var1 = this.compute();
      if (var1 != this) {
         return var1.toString();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("property ");
         var2.append(this.getName());
         var2.append(" (Kotlin reflection is not available)");
         return var2.toString();
      }
   }
}
