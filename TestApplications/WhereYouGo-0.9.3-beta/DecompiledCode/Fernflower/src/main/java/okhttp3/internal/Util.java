package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;

public final class Util {
   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   public static final RequestBody EMPTY_REQUEST;
   public static final ResponseBody EMPTY_RESPONSE;
   public static final String[] EMPTY_STRING_ARRAY = new String[0];
   public static final TimeZone UTC;
   private static final Charset UTF_16_BE;
   private static final ByteString UTF_16_BE_BOM;
   private static final Charset UTF_16_LE;
   private static final ByteString UTF_16_LE_BOM;
   private static final Charset UTF_32_BE;
   private static final ByteString UTF_32_BE_BOM;
   private static final Charset UTF_32_LE;
   private static final ByteString UTF_32_LE_BOM;
   public static final Charset UTF_8;
   private static final ByteString UTF_8_BOM;
   private static final Pattern VERIFY_AS_IP_ADDRESS;

   static {
      EMPTY_RESPONSE = ResponseBody.create((MediaType)null, (byte[])EMPTY_BYTE_ARRAY);
      EMPTY_REQUEST = RequestBody.create((MediaType)null, (byte[])EMPTY_BYTE_ARRAY);
      UTF_8_BOM = ByteString.decodeHex("efbbbf");
      UTF_16_BE_BOM = ByteString.decodeHex("feff");
      UTF_16_LE_BOM = ByteString.decodeHex("fffe");
      UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
      UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
      UTF_8 = Charset.forName("UTF-8");
      UTF_16_BE = Charset.forName("UTF-16BE");
      UTF_16_LE = Charset.forName("UTF-16LE");
      UTF_32_BE = Charset.forName("UTF-32BE");
      UTF_32_LE = Charset.forName("UTF-32LE");
      UTC = TimeZone.getTimeZone("GMT");
      VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
   }

   private Util() {
   }

   public static Charset bomAwareCharset(BufferedSource var0, Charset var1) throws IOException {
      if (var0.rangeEquals(0L, UTF_8_BOM)) {
         var0.skip((long)UTF_8_BOM.size());
         var1 = UTF_8;
      } else if (var0.rangeEquals(0L, UTF_16_BE_BOM)) {
         var0.skip((long)UTF_16_BE_BOM.size());
         var1 = UTF_16_BE;
      } else if (var0.rangeEquals(0L, UTF_16_LE_BOM)) {
         var0.skip((long)UTF_16_LE_BOM.size());
         var1 = UTF_16_LE;
      } else if (var0.rangeEquals(0L, UTF_32_BE_BOM)) {
         var0.skip((long)UTF_32_BE_BOM.size());
         var1 = UTF_32_BE;
      } else if (var0.rangeEquals(0L, UTF_32_LE_BOM)) {
         var0.skip((long)UTF_32_LE_BOM.size());
         var1 = UTF_32_LE;
      }

      return var1;
   }

   public static void checkOffsetAndCount(long var0, long var2, long var4) {
      if ((var2 | var4) < 0L || var2 > var0 || var0 - var2 < var4) {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   public static void closeQuietly(ServerSocket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   public static void closeQuietly(Socket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (AssertionError var1) {
            if (!isAndroidGetsocknameError(var1)) {
               throw var1;
            }
         } catch (RuntimeException var2) {
            throw var2;
         } catch (Exception var3) {
         }
      }

   }

   public static String[] concat(String[] var0, String var1) {
      String[] var2 = new String[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
   }

   private static boolean containsInvalidHostnameAsciiCodes(String var0) {
      boolean var1 = true;
      int var2 = 0;

      boolean var4;
      while(true) {
         if (var2 >= var0.length()) {
            var4 = false;
            break;
         }

         char var3 = var0.charAt(var2);
         var4 = var1;
         if (var3 <= 31) {
            break;
         }

         if (var3 >= 127) {
            var4 = var1;
            break;
         }

         var4 = var1;
         if (" #%/:?@[\\]".indexOf(var3) != -1) {
            break;
         }

         ++var2;
      }

      return var4;
   }

   public static int delimiterOffset(String var0, int var1, int var2, char var3) {
      while(true) {
         if (var1 < var2) {
            if (var0.charAt(var1) != var3) {
               ++var1;
               continue;
            }
         } else {
            var1 = var2;
         }

         return var1;
      }
   }

   public static int delimiterOffset(String var0, int var1, int var2, String var3) {
      while(true) {
         if (var1 < var2) {
            if (var3.indexOf(var0.charAt(var1)) == -1) {
               ++var1;
               continue;
            }
         } else {
            var1 = var2;
         }

         return var1;
      }
   }

   public static boolean discard(Source var0, int var1, TimeUnit var2) {
      boolean var3;
      try {
         var3 = skipAll(var0, var1, var2);
      } catch (IOException var4) {
         var3 = false;
      }

      return var3;
   }

   public static String domainToAscii(String var0) {
      label30: {
         boolean var1;
         try {
            var0 = IDN.toASCII(var0).toLowerCase(Locale.US);
            if (var0.isEmpty()) {
               break label30;
            }

            var1 = containsInvalidHostnameAsciiCodes(var0);
         } catch (IllegalArgumentException var2) {
            var0 = null;
            return var0;
         }

         if (var1) {
            var0 = null;
         }

         return var0;
      }

      var0 = null;
      return var0;
   }

   public static boolean equal(Object var0, Object var1) {
      boolean var2;
      if (var0 != var1 && (var0 == null || !var0.equals(var1))) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public static String format(String var0, Object... var1) {
      return String.format(Locale.US, var0, var1);
   }

   public static String hostHeader(HttpUrl var0, boolean var1) {
      String var2;
      if (var0.host().contains(":")) {
         var2 = "[" + var0.host() + "]";
      } else {
         var2 = var0.host();
      }

      String var3;
      if (!var1) {
         var3 = var2;
         if (var0.port() == HttpUrl.defaultPort(var0.scheme())) {
            return var3;
         }
      }

      var3 = var2 + ":" + var0.port();
      return var3;
   }

   public static List immutableList(List var0) {
      return Collections.unmodifiableList(new ArrayList(var0));
   }

   public static List immutableList(Object... var0) {
      return Collections.unmodifiableList(Arrays.asList((Object[])var0.clone()));
   }

   public static int indexOf(Object[] var0, Object var1) {
      int var2 = 0;
      int var3 = var0.length;

      while(true) {
         if (var2 >= var3) {
            var2 = -1;
            break;
         }

         if (equal(var0[var2], var1)) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public static int indexOfControlOrNonAscii(String var0) {
      int var1 = 0;
      int var2 = var0.length();

      int var4;
      while(true) {
         if (var1 >= var2) {
            var4 = -1;
            break;
         }

         char var3 = var0.charAt(var1);
         var4 = var1;
         if (var3 <= 31) {
            break;
         }

         if (var3 >= 127) {
            var4 = var1;
            break;
         }

         ++var1;
      }

      return var4;
   }

   private static List intersect(Object[] var0, Object[] var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var0[var4];
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Object var8 = var1[var7];
            if (var5.equals(var8)) {
               var2.add(var8);
               break;
            }
         }
      }

      return var2;
   }

   public static Object[] intersect(Class var0, Object[] var1, Object[] var2) {
      List var3 = intersect(var1, var2);
      return var3.toArray((Object[])Array.newInstance(var0, var3.size()));
   }

   public static boolean isAndroidGetsocknameError(AssertionError var0) {
      boolean var1;
      if (var0.getCause() != null && var0.getMessage() != null && var0.getMessage().contains("getsockname failed")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean skipAll(Source param0, int param1, TimeUnit param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static int skipLeadingAsciiWhitespace(String var0, int var1, int var2) {
      while(true) {
         if (var1 < var2) {
            switch(var0.charAt(var1)) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
               ++var1;
               continue;
            }
         } else {
            var1 = var2;
         }

         return var1;
      }
   }

   public static int skipTrailingAsciiWhitespace(String var0, int var1, int var2) {
      int var3 = var2 - 1;

      while(true) {
         var2 = var1;
         if (var3 < var1) {
            return var2;
         }

         switch(var0.charAt(var3)) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            --var3;
            break;
         default:
            var2 = var3 + 1;
            return var2;
         }
      }
   }

   public static ThreadFactory threadFactory(final String var0, final boolean var1) {
      return new ThreadFactory() {
         public Thread newThread(Runnable var1x) {
            Thread var2 = new Thread(var1x, var0);
            var2.setDaemon(var1);
            return var2;
         }
      };
   }

   public static String toHumanReadableAscii(String var0) {
      int var1 = 0;
      int var2 = var0.length();

      String var3;
      while(true) {
         var3 = var0;
         if (var1 >= var2) {
            break;
         }

         int var4 = var0.codePointAt(var1);
         if (var4 <= 31 || var4 >= 127) {
            Buffer var6 = new Buffer();
            var6.writeUtf8(var0, 0, var1);

            while(var1 < var2) {
               int var5 = var0.codePointAt(var1);
               if (var5 > 31 && var5 < 127) {
                  var4 = var5;
               } else {
                  var4 = 63;
               }

               var6.writeUtf8CodePoint(var4);
               var1 += Character.charCount(var5);
            }

            var3 = var6.readUtf8();
            break;
         }

         var1 += Character.charCount(var4);
      }

      return var3;
   }

   public static String trimSubstring(String var0, int var1, int var2) {
      var1 = skipLeadingAsciiWhitespace(var0, var1, var2);
      return var0.substring(var1, skipTrailingAsciiWhitespace(var0, var1, var2));
   }

   public static boolean verifyAsIpAddress(String var0) {
      return VERIFY_AS_IP_ADDRESS.matcher(var0).matches();
   }
}
