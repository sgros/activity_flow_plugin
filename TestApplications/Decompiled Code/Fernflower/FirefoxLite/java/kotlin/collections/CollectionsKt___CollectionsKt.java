package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.text.StringsKt;

class CollectionsKt___CollectionsKt extends CollectionsKt___CollectionsJvmKt {
   public static final Sequence asSequence(final Iterable var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return (Sequence)(new Sequence() {
         public Iterator iterator() {
            return var0.iterator();
         }
      });
   }

   public static final Appendable joinTo(Iterable var0, Appendable var1, CharSequence var2, CharSequence var3, CharSequence var4, int var5, CharSequence var6, Function1 var7) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "buffer");
      Intrinsics.checkParameterIsNotNull(var2, "separator");
      Intrinsics.checkParameterIsNotNull(var3, "prefix");
      Intrinsics.checkParameterIsNotNull(var4, "postfix");
      Intrinsics.checkParameterIsNotNull(var6, "truncated");
      var1.append(var3);
      Iterator var11 = var0.iterator();
      int var8 = 0;

      int var9;
      while(true) {
         var9 = var8;
         if (!var11.hasNext()) {
            break;
         }

         Object var10 = var11.next();
         ++var8;
         if (var8 > 1) {
            var1.append(var2);
         }

         if (var5 >= 0) {
            var9 = var8;
            if (var8 > var5) {
               break;
            }
         }

         StringsKt.appendElement(var1, var10, var7);
      }

      if (var5 >= 0 && var9 > var5) {
         var1.append(var6);
      }

      var1.append(var4);
      return var1;
   }

   public static final String joinToString(Iterable var0, CharSequence var1, CharSequence var2, CharSequence var3, int var4, CharSequence var5, Function1 var6) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "separator");
      Intrinsics.checkParameterIsNotNull(var2, "prefix");
      Intrinsics.checkParameterIsNotNull(var3, "postfix");
      Intrinsics.checkParameterIsNotNull(var5, "truncated");
      String var7 = ((StringBuilder)CollectionsKt.joinTo(var0, (Appendable)(new StringBuilder()), var1, var2, var3, var4, var5, var6)).toString();
      Intrinsics.checkExpressionValueIsNotNull(var7, "joinTo(StringBuilder(), …ed, transform).toString()");
      return var7;
   }

   // $FF: synthetic method
   public static String joinToString$default(Iterable var0, CharSequence var1, CharSequence var2, CharSequence var3, int var4, CharSequence var5, Function1 var6, int var7, Object var8) {
      if ((var7 & 1) != 0) {
         var1 = (CharSequence)", ";
      }

      if ((var7 & 2) != 0) {
         var2 = (CharSequence)"";
      }

      if ((var7 & 4) != 0) {
         var3 = (CharSequence)"";
      }

      if ((var7 & 8) != 0) {
         var4 = -1;
      }

      if ((var7 & 16) != 0) {
         var5 = (CharSequence)"...";
      }

      if ((var7 & 32) != 0) {
         var6 = (Function1)null;
      }

      return CollectionsKt.joinToString(var0, var1, var2, var3, var4, var5, var6);
   }

   public static final Object last(List var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (!var0.isEmpty()) {
         return var0.get(CollectionsKt.getLastIndex(var0));
      } else {
         throw (Throwable)(new NoSuchElementException("List is empty."));
      }
   }

   public static final List plus(Collection var0, Object var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      ArrayList var2 = new ArrayList(var0.size() + 1);
      var2.addAll(var0);
      var2.add(var1);
      return (List)var2;
   }

   public static final Object single(Iterable var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (var0 instanceof List) {
         return CollectionsKt.single((List)var0);
      } else {
         Iterator var2 = var0.iterator();
         if (var2.hasNext()) {
            Object var1 = var2.next();
            if (!var2.hasNext()) {
               return var1;
            } else {
               throw (Throwable)(new IllegalArgumentException("Collection has more than one element."));
            }
         } else {
            throw (Throwable)(new NoSuchElementException("Collection is empty."));
         }
      }
   }

   public static final Object single(List var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      switch(var0.size()) {
      case 0:
         throw (Throwable)(new NoSuchElementException("List is empty."));
      case 1:
         return var0.get(0);
      default:
         throw (Throwable)(new IllegalArgumentException("List has more than one element."));
      }
   }

   public static final Collection toCollection(Iterable var0, Collection var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "destination");
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      return var1;
   }

   public static final List toList(Iterable var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (var0 instanceof Collection) {
         Collection var1 = (Collection)var0;
         List var3;
         switch(var1.size()) {
         case 0:
            var3 = CollectionsKt.emptyList();
            break;
         case 1:
            Object var2;
            if (var0 instanceof List) {
               var2 = ((List)var0).get(0);
            } else {
               var2 = var0.iterator().next();
            }

            var3 = CollectionsKt.listOf(var2);
            break;
         default:
            var3 = CollectionsKt.toMutableList(var1);
         }

         return var3;
      } else {
         return CollectionsKt.optimizeReadOnlyList(CollectionsKt.toMutableList(var0));
      }
   }

   public static final List toMutableList(Iterable var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return var0 instanceof Collection ? CollectionsKt.toMutableList((Collection)var0) : (List)CollectionsKt.toCollection(var0, (Collection)(new ArrayList()));
   }

   public static final List toMutableList(Collection var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return (List)(new ArrayList(var0));
   }
}
