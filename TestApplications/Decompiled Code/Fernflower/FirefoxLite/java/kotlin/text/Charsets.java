package kotlin.text;

import java.nio.charset.Charset;
import kotlin.jvm.internal.Intrinsics;

public final class Charsets {
   public static final Charsets INSTANCE = new Charsets();
   public static final Charset ISO_8859_1;
   public static final Charset US_ASCII;
   public static final Charset UTF_16;
   public static final Charset UTF_16BE;
   public static final Charset UTF_16LE;
   public static final Charset UTF_8;

   static {
      Charset var0 = Charset.forName("UTF-8");
      Intrinsics.checkExpressionValueIsNotNull(var0, "Charset.forName(\"UTF-8\")");
      UTF_8 = var0;
      var0 = Charset.forName("UTF-16");
      Intrinsics.checkExpressionValueIsNotNull(var0, "Charset.forName(\"UTF-16\")");
      UTF_16 = var0;
      var0 = Charset.forName("UTF-16BE");
      Intrinsics.checkExpressionValueIsNotNull(var0, "Charset.forName(\"UTF-16BE\")");
      UTF_16BE = var0;
      var0 = Charset.forName("UTF-16LE");
      Intrinsics.checkExpressionValueIsNotNull(var0, "Charset.forName(\"UTF-16LE\")");
      UTF_16LE = var0;
      var0 = Charset.forName("US-ASCII");
      Intrinsics.checkExpressionValueIsNotNull(var0, "Charset.forName(\"US-ASCII\")");
      US_ASCII = var0;
      var0 = Charset.forName("ISO-8859-1");
      Intrinsics.checkExpressionValueIsNotNull(var0, "Charset.forName(\"ISO-8859-1\")");
      ISO_8859_1 = var0;
   }

   private Charsets() {
   }
}
