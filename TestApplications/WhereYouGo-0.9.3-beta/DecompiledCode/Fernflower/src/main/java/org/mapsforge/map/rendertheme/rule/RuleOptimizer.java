package org.mapsforge.map.rendertheme.rule;

import java.util.Stack;
import java.util.logging.Logger;

final class RuleOptimizer {
   private static final Logger LOGGER = Logger.getLogger(RuleOptimizer.class.getName());

   private RuleOptimizer() {
      throw new IllegalStateException();
   }

   static AttributeMatcher optimize(AttributeMatcher var0, Stack var1) {
      AttributeMatcher var2 = var0;
      if (!(var0 instanceof AnyMatcher)) {
         if (var0 instanceof NegativeMatcher) {
            var2 = var0;
         } else if (var0 instanceof KeyMatcher) {
            var2 = optimizeKeyMatcher(var0, var1);
         } else {
            if (!(var0 instanceof ValueMatcher)) {
               throw new IllegalArgumentException("unknown AttributeMatcher: " + var0);
            }

            var2 = optimizeValueMatcher(var0, var1);
         }
      }

      return var2;
   }

   static ClosedMatcher optimize(ClosedMatcher var0, Stack var1) {
      Object var2;
      if (var0 instanceof AnyMatcher) {
         var2 = var0;
      } else {
         int var3 = 0;
         int var4 = var1.size();

         while(true) {
            var2 = var0;
            if (var3 >= var4) {
               break;
            }

            if (((Rule)var1.get(var3)).closedMatcher.isCoveredBy(var0)) {
               var2 = AnyMatcher.INSTANCE;
               break;
            }

            if (!var0.isCoveredBy(((Rule)var1.get(var3)).closedMatcher)) {
               LOGGER.warning("unreachable rule (closed)");
            }

            ++var3;
         }
      }

      return (ClosedMatcher)var2;
   }

   static ElementMatcher optimize(ElementMatcher var0, Stack var1) {
      Object var2;
      if (var0 instanceof AnyMatcher) {
         var2 = var0;
      } else {
         int var3 = 0;
         int var4 = var1.size();

         while(true) {
            var2 = var0;
            if (var3 >= var4) {
               break;
            }

            Rule var5 = (Rule)var1.get(var3);
            if (var5.elementMatcher.isCoveredBy(var0)) {
               var2 = AnyMatcher.INSTANCE;
               break;
            }

            if (!var0.isCoveredBy(var5.elementMatcher)) {
               LOGGER.warning("unreachable rule (e)");
            }

            ++var3;
         }
      }

      return (ElementMatcher)var2;
   }

   private static AttributeMatcher optimizeKeyMatcher(AttributeMatcher var0, Stack var1) {
      int var2 = 0;
      int var3 = var1.size();

      Object var4;
      while(true) {
         var4 = var0;
         if (var2 >= var3) {
            break;
         }

         if (var1.get(var2) instanceof PositiveRule && ((PositiveRule)var1.get(var2)).keyMatcher.isCoveredBy(var0)) {
            var4 = AnyMatcher.INSTANCE;
            break;
         }

         ++var2;
      }

      return (AttributeMatcher)var4;
   }

   private static AttributeMatcher optimizeValueMatcher(AttributeMatcher var0, Stack var1) {
      int var2 = 0;
      int var3 = var1.size();

      Object var4;
      while(true) {
         var4 = var0;
         if (var2 >= var3) {
            break;
         }

         if (var1.get(var2) instanceof PositiveRule && ((PositiveRule)var1.get(var2)).valueMatcher.isCoveredBy(var0)) {
            var4 = AnyMatcher.INSTANCE;
            break;
         }

         ++var2;
      }

      return (AttributeMatcher)var4;
   }
}
