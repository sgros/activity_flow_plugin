package kotlin.text;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class Regex implements Serializable {
   public static final Regex.Companion Companion = new Regex.Companion((DefaultConstructorMarker)null);
   private final Pattern nativePattern;

   public Regex(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "pattern");
      Pattern var2 = Pattern.compile(var1);
      Intrinsics.checkExpressionValueIsNotNull(var2, "Pattern.compile(pattern)");
      this(var2);
   }

   public Regex(Pattern var1) {
      Intrinsics.checkParameterIsNotNull(var1, "nativePattern");
      super();
      this.nativePattern = var1;
   }

   // $FF: synthetic method
   public static MatchResult find$default(Regex var0, CharSequence var1, int var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = 0;
      }

      return var0.find(var1, var2);
   }

   public final MatchResult find(CharSequence var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "input");
      Matcher var3 = this.nativePattern.matcher(var1);
      Intrinsics.checkExpressionValueIsNotNull(var3, "nativePattern.matcher(input)");
      return RegexKt.access$findNext(var3, var2, var1);
   }

   public final String replace(CharSequence var1, String var2) {
      Intrinsics.checkParameterIsNotNull(var1, "input");
      Intrinsics.checkParameterIsNotNull(var2, "replacement");
      String var3 = this.nativePattern.matcher(var1).replaceAll(var2);
      Intrinsics.checkExpressionValueIsNotNull(var3, "nativePattern.matcher(in…).replaceAll(replacement)");
      return var3;
   }

   public String toString() {
      String var1 = this.nativePattern.toString();
      Intrinsics.checkExpressionValueIsNotNull(var1, "nativePattern.toString()");
      return var1;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
