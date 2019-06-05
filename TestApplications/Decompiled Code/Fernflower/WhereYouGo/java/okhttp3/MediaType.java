package okhttp3;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MediaType {
   private static final Pattern PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
   private static final String QUOTED = "\"([^\"]*)\"";
   private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
   private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
   private final String charset;
   private final String mediaType;
   private final String subtype;
   private final String type;

   private MediaType(String var1, String var2, String var3, String var4) {
      this.mediaType = var1;
      this.type = var2;
      this.subtype = var3;
      this.charset = var4;
   }

   public static MediaType parse(String var0) {
      Object var1 = null;
      Matcher var2 = TYPE_SUBTYPE.matcher(var0);
      MediaType var9;
      if (!var2.lookingAt()) {
         var9 = (MediaType)var1;
      } else {
         String var3 = var2.group(1).toLowerCase(Locale.US);
         String var4 = var2.group(2).toLowerCase(Locale.US);
         String var5 = null;
         Matcher var6 = PARAMETER.matcher(var0);
         int var7 = var2.end();

         while(true) {
            if (var7 >= var0.length()) {
               var9 = new MediaType(var0, var3, var4, var5);
               break;
            }

            var6.region(var7, var0.length());
            var9 = (MediaType)var1;
            if (!var6.lookingAt()) {
               break;
            }

            String var8 = var6.group(1);
            String var10 = var5;
            if (var8 != null) {
               if (!var8.equalsIgnoreCase("charset")) {
                  var10 = var5;
               } else {
                  var10 = var6.group(2);
                  if (var10 != null) {
                     if (var10.startsWith("'") && var10.endsWith("'") && var10.length() > 2) {
                        var10 = var10.substring(1, var10.length() - 1);
                     }
                  } else {
                     var10 = var6.group(3);
                  }

                  if (var5 != null && !var10.equalsIgnoreCase(var5)) {
                     throw new IllegalArgumentException("Multiple different charsets: " + var0);
                  }
               }
            }

            var7 = var6.end();
            var5 = var10;
         }
      }

      return var9;
   }

   public Charset charset() {
      Charset var1;
      if (this.charset != null) {
         var1 = Charset.forName(this.charset);
      } else {
         var1 = null;
      }

      return var1;
   }

   public Charset charset(Charset var1) {
      if (this.charset != null) {
         var1 = Charset.forName(this.charset);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof MediaType && ((MediaType)var1).mediaType.equals(this.mediaType)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.mediaType.hashCode();
   }

   public String subtype() {
      return this.subtype;
   }

   public String toString() {
      return this.mediaType;
   }

   public String type() {
      return this.type;
   }
}
