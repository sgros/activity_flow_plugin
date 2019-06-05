package kotlin.text;

import java.util.regex.Matcher;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

public final class RegexKt {
   // $FF: synthetic method
   public static final MatchResult access$findNext(Matcher var0, int var1, CharSequence var2) {
      return findNext(var0, var1, var2);
   }

   // $FF: synthetic method
   public static final IntRange access$range(java.util.regex.MatchResult var0, int var1) {
      return range(var0, var1);
   }

   private static final MatchResult findNext(Matcher var0, int var1, CharSequence var2) {
      MatchResult var3;
      if (!var0.find(var1)) {
         var3 = null;
      } else {
         var3 = (MatchResult)(new MatcherMatchResult(var0, var2));
      }

      return var3;
   }

   private static final IntRange range(java.util.regex.MatchResult var0, int var1) {
      return RangesKt.until(var0.start(var1), var0.end(var1));
   }
}
