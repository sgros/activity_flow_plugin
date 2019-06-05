package mozilla.components.support.base.observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class Consumable {
   public static final Consumable.Companion Companion = new Consumable.Companion((DefaultConstructorMarker)null);
   private Object value;

   private Consumable(Object var1) {
      this.value = var1;
   }

   // $FF: synthetic method
   public Consumable(Object var1, DefaultConstructorMarker var2) {
      this(var1);
   }

   public final boolean consumeBy(List var1) {
      synchronized(this){}

      Throwable var10000;
      label341: {
         Object var2;
         boolean var10001;
         try {
            Intrinsics.checkParameterIsNotNull(var1, "consumers");
            var2 = this.value;
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label341;
         }

         if (var2 == null) {
            return false;
         }

         Collection var36;
         Iterator var38;
         try {
            Iterable var3 = (Iterable)var1;
            ArrayList var35 = new ArrayList(CollectionsKt.collectionSizeOrDefault(var3, 10));
            var36 = (Collection)var35;
            var38 = var3.iterator();
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label341;
         }

         while(true) {
            try {
               if (!var38.hasNext()) {
                  break;
               }

               var36.add((Boolean)((Function1)var38.next()).invoke(var2));
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label341;
            }
         }

         try {
            var1 = (List)var36;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label341;
         }

         boolean var4 = true;

         try {
            if (var1.contains(true)) {
               this.value = null;
               return var4;
            }
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label341;
         }

         var4 = false;
         return var4;
      }

      Throwable var37 = var10000;
      throw var37;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final Consumable empty() {
         return new Consumable((Object)null, (DefaultConstructorMarker)null);
      }

      public final Consumable from(Object var1) {
         return new Consumable(var1, (DefaultConstructorMarker)null);
      }
   }
}
