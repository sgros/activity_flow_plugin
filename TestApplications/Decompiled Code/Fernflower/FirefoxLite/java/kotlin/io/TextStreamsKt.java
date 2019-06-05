package kotlin.io;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

public final class TextStreamsKt {
   public static final void forEachLine(Reader param0, Function1 param1) {
      // $FF: Couldn't be decompiled
   }

   public static final Sequence lineSequence(BufferedReader var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return SequencesKt.constrainOnce((Sequence)(new LinesSequence(var0)));
   }

   public static final List readLines(Reader var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      final ArrayList var1 = new ArrayList();
      forEachLine(var0, (Function1)(new Function1() {
         public final void invoke(String var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "it");
            var1.add(var1x);
         }
      }));
      return (List)var1;
   }
}
