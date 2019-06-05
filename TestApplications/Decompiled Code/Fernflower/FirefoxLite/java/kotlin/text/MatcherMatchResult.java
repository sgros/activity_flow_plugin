package kotlin.text;

import java.util.Iterator;
import java.util.regex.Matcher;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.SequencesKt;

final class MatcherMatchResult implements MatchResult {
   private final MatchGroupCollection groups;
   private final CharSequence input;
   private final java.util.regex.MatchResult matchResult;
   private final Matcher matcher;

   public MatcherMatchResult(Matcher var1, CharSequence var2) {
      Intrinsics.checkParameterIsNotNull(var1, "matcher");
      Intrinsics.checkParameterIsNotNull(var2, "input");
      super();
      this.matcher = var1;
      this.input = var2;
      this.matchResult = this.matcher.toMatchResult();
      this.groups = (MatchGroupCollection)(new MatchNamedGroupCollection() {
         public MatchGroup get(int var1) {
            java.util.regex.MatchResult var2 = MatcherMatchResult.this.matchResult;
            Intrinsics.checkExpressionValueIsNotNull(var2, "matchResult");
            IntRange var4 = RegexKt.access$range(var2, var1);
            MatchGroup var5;
            if (var4.getStart() >= 0) {
               String var3 = MatcherMatchResult.this.matchResult.group(var1);
               Intrinsics.checkExpressionValueIsNotNull(var3, "matchResult.group(index)");
               var5 = new MatchGroup(var3, var4);
            } else {
               var5 = null;
            }

            return var5;
         }

         public int getSize() {
            return MatcherMatchResult.this.matchResult.groupCount() + 1;
         }

         public boolean isEmpty() {
            return false;
         }

         public Iterator iterator() {
            return SequencesKt.map(CollectionsKt.asSequence((Iterable)CollectionsKt.getIndices(this)), (Function1)(new Function1() {
               public final MatchGroup invoke(int var1) {
                  return get(var1);
               }
            })).iterator();
         }
      });
   }

   public MatchGroupCollection getGroups() {
      return this.groups;
   }
}
