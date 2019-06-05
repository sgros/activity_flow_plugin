package android.arch.persistence.room.util;

public class StringUtil {
   public static final String[] EMPTY_STRING_ARRAY = new String[0];

   public static void appendPlaceholders(StringBuilder var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         var0.append("?");
         if (var2 < var1 - 1) {
            var0.append(",");
         }
      }

   }

   public static StringBuilder newStringBuilder() {
      return new StringBuilder();
   }
}
