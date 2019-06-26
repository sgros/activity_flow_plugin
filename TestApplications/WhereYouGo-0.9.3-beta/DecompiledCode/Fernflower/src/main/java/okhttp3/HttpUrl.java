package okhttp3;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import okhttp3.internal.Util;
import okio.Buffer;

public final class HttpUrl {
   static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
   static final String FRAGMENT_ENCODE_SET = "";
   static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
   private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
   static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
   static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
   static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
   static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
   static final String QUERY_ENCODE_SET = " \"'<>#";
   static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
   private final String fragment;
   final String host;
   private final String password;
   private final List pathSegments;
   final int port;
   private final List queryNamesAndValues;
   final String scheme;
   private final String url;
   private final String username;

   HttpUrl(HttpUrl.Builder var1) {
      Object var2 = null;
      super();
      this.scheme = var1.scheme;
      this.username = percentDecode(var1.encodedUsername, false);
      this.password = percentDecode(var1.encodedPassword, false);
      this.host = var1.host;
      this.port = var1.effectivePort();
      this.pathSegments = this.percentDecode(var1.encodedPathSegments, false);
      List var3;
      if (var1.encodedQueryNamesAndValues != null) {
         var3 = this.percentDecode(var1.encodedQueryNamesAndValues, true);
      } else {
         var3 = null;
      }

      this.queryNamesAndValues = var3;
      String var4 = (String)var2;
      if (var1.encodedFragment != null) {
         var4 = percentDecode(var1.encodedFragment, false);
      }

      this.fragment = var4;
      this.url = var1.toString();
   }

   static String canonicalize(String var0, int var1, int var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      int var8 = var1;

      while(true) {
         if (var8 >= var2) {
            var0 = var0.substring(var1, var2);
            break;
         }

         int var9 = var0.codePointAt(var8);
         if (var9 < 32 || var9 == 127 || var9 >= 128 && var7 || var3.indexOf(var9) != -1 || var9 == 37 && (!var4 || var5 && !percentEncoded(var0, var8, var2)) || var9 == 43 && var6) {
            Buffer var10 = new Buffer();
            var10.writeUtf8(var0, var1, var8);
            canonicalize(var10, var0, var8, var2, var3, var4, var5, var6, var7);
            var0 = var10.readUtf8();
            break;
         }

         var8 += Character.charCount(var9);
      }

      return var0;
   }

   static String canonicalize(String var0, String var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      return canonicalize(var0, 0, var0.length(), var1, var2, var3, var4, var5);
   }

   static void canonicalize(Buffer var0, String var1, int var2, int var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      Buffer var11;
      for(Buffer var9 = null; var2 < var3; var9 = var11) {
         int var10;
         label86: {
            var10 = var1.codePointAt(var2);
            if (var5) {
               var11 = var9;
               if (var10 == 9) {
                  break label86;
               }

               var11 = var9;
               if (var10 == 10) {
                  break label86;
               }

               var11 = var9;
               if (var10 == 12) {
                  break label86;
               }

               if (var10 == 13) {
                  var11 = var9;
                  break label86;
               }
            }

            if (var10 == 43 && var7) {
               String var14;
               if (var5) {
                  var14 = "+";
               } else {
                  var14 = "%2B";
               }

               var0.writeUtf8(var14);
               var11 = var9;
            } else if (var10 >= 32 && var10 != 127 && (var10 < 128 || !var8) && var4.indexOf(var10) == -1 && (var10 != 37 || var5 && (!var6 || percentEncoded(var1, var2, var3)))) {
               var0.writeUtf8CodePoint(var10);
               var11 = var9;
            } else {
               Buffer var12 = var9;
               if (var9 == null) {
                  var12 = new Buffer();
               }

               var12.writeUtf8CodePoint(var10);

               while(true) {
                  var11 = var12;
                  if (var12.exhausted()) {
                     break;
                  }

                  int var13 = var12.readByte() & 255;
                  var0.writeByte(37);
                  var0.writeByte(HEX_DIGITS[var13 >> 4 & 15]);
                  var0.writeByte(HEX_DIGITS[var13 & 15]);
               }
            }
         }

         var2 += Character.charCount(var10);
      }

   }

   static int decodeHexDigit(char var0) {
      int var1;
      if (var0 >= '0' && var0 <= '9') {
         var1 = var0 - 48;
      } else if (var0 >= 'a' && var0 <= 'f') {
         var1 = var0 - 97 + 10;
      } else if (var0 >= 'A' && var0 <= 'F') {
         var1 = var0 - 65 + 10;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public static int defaultPort(String var0) {
      short var1;
      if (var0.equals("http")) {
         var1 = 80;
      } else if (var0.equals("https")) {
         var1 = 443;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public static HttpUrl get(URI var0) {
      return parse(var0.toString());
   }

   public static HttpUrl get(URL var0) {
      return parse(var0.toString());
   }

   static HttpUrl getChecked(String var0) throws MalformedURLException, UnknownHostException {
      HttpUrl.Builder var1 = new HttpUrl.Builder();
      HttpUrl.Builder.ParseResult var2 = var1.parse((HttpUrl)null, var0);
      switch(var2) {
      case SUCCESS:
         return var1.build();
      case INVALID_HOST:
         throw new UnknownHostException("Invalid host: " + var0);
      default:
         throw new MalformedURLException("Invalid URL: " + var2 + " for " + var0);
      }
   }

   static void namesAndValuesToQueryString(StringBuilder var0, List var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; var2 += 2) {
         String var4 = (String)var1.get(var2);
         String var5 = (String)var1.get(var2 + 1);
         if (var2 > 0) {
            var0.append('&');
         }

         var0.append(var4);
         if (var5 != null) {
            var0.append('=');
            var0.append(var5);
         }
      }

   }

   public static HttpUrl parse(String var0) {
      HttpUrl var1 = null;
      HttpUrl.Builder var2 = new HttpUrl.Builder();
      if (var2.parse((HttpUrl)null, var0) == HttpUrl.Builder.ParseResult.SUCCESS) {
         var1 = var2.build();
      }

      return var1;
   }

   static void pathSegmentsToString(StringBuilder var0, List var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         var0.append('/');
         var0.append((String)var1.get(var2));
      }

   }

   static String percentDecode(String var0, int var1, int var2, boolean var3) {
      int var4 = var1;

      while(true) {
         if (var4 >= var2) {
            var0 = var0.substring(var1, var2);
            break;
         }

         char var5 = var0.charAt(var4);
         if (var5 == '%' || var5 == '+' && var3) {
            Buffer var6 = new Buffer();
            var6.writeUtf8(var0, var1, var4);
            percentDecode(var6, var0, var4, var2, var3);
            var0 = var6.readUtf8();
            break;
         }

         ++var4;
      }

      return var0;
   }

   static String percentDecode(String var0, boolean var1) {
      return percentDecode(var0, 0, var0.length(), var1);
   }

   private List percentDecode(List var1, boolean var2) {
      int var3 = var1.size();
      ArrayList var4 = new ArrayList(var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         String var6 = (String)var1.get(var5);
         if (var6 != null) {
            var6 = percentDecode(var6, var2);
         } else {
            var6 = null;
         }

         var4.add(var6);
      }

      return Collections.unmodifiableList(var4);
   }

   static void percentDecode(Buffer var0, String var1, int var2, int var3, boolean var4) {
      int var5;
      for(; var2 < var3; var2 += Character.charCount(var5)) {
         var5 = var1.codePointAt(var2);
         if (var5 == 37 && var2 + 2 < var3) {
            int var6 = decodeHexDigit(var1.charAt(var2 + 1));
            int var7 = decodeHexDigit(var1.charAt(var2 + 2));
            if (var6 != -1 && var7 != -1) {
               var0.writeByte((var6 << 4) + var7);
               var2 += 2;
               continue;
            }
         } else if (var5 == 43 && var4) {
            var0.writeByte(32);
            continue;
         }

         var0.writeUtf8CodePoint(var5);
      }

   }

   static boolean percentEncoded(String var0, int var1, int var2) {
      boolean var3;
      if (var1 + 2 < var2 && var0.charAt(var1) == '%' && decodeHexDigit(var0.charAt(var1 + 1)) != -1 && decodeHexDigit(var0.charAt(var1 + 2)) != -1) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   static List queryStringToNamesAndValues(String var0) {
      ArrayList var1 = new ArrayList();

      int var4;
      for(int var2 = 0; var2 <= var0.length(); var2 = var4 + 1) {
         int var3 = var0.indexOf(38, var2);
         var4 = var3;
         if (var3 == -1) {
            var4 = var0.length();
         }

         var3 = var0.indexOf(61, var2);
         if (var3 != -1 && var3 <= var4) {
            var1.add(var0.substring(var2, var3));
            var1.add(var0.substring(var3 + 1, var4));
         } else {
            var1.add(var0.substring(var2, var4));
            var1.add((Object)null);
         }
      }

      return var1;
   }

   public String encodedFragment() {
      String var1;
      if (this.fragment == null) {
         var1 = null;
      } else {
         int var2 = this.url.indexOf(35);
         var1 = this.url.substring(var2 + 1);
      }

      return var1;
   }

   public String encodedPassword() {
      String var1;
      if (this.password.isEmpty()) {
         var1 = "";
      } else {
         int var2 = this.url.indexOf(58, this.scheme.length() + 3);
         int var3 = this.url.indexOf(64);
         var1 = this.url.substring(var2 + 1, var3);
      }

      return var1;
   }

   public String encodedPath() {
      int var1 = this.url.indexOf(47, this.scheme.length() + 3);
      int var2 = Util.delimiterOffset(this.url, var1, this.url.length(), "?#");
      return this.url.substring(var1, var2);
   }

   public List encodedPathSegments() {
      int var1 = this.url.indexOf(47, this.scheme.length() + 3);
      int var2 = Util.delimiterOffset(this.url, var1, this.url.length(), "?#");
      ArrayList var3 = new ArrayList();

      while(var1 < var2) {
         int var4 = var1 + 1;
         var1 = Util.delimiterOffset(this.url, var4, var2, '/');
         var3.add(this.url.substring(var4, var1));
      }

      return var3;
   }

   public String encodedQuery() {
      String var1;
      if (this.queryNamesAndValues == null) {
         var1 = null;
      } else {
         int var2 = this.url.indexOf(63) + 1;
         int var3 = Util.delimiterOffset(this.url, var2 + 1, this.url.length(), '#');
         var1 = this.url.substring(var2, var3);
      }

      return var1;
   }

   public String encodedUsername() {
      String var1;
      if (this.username.isEmpty()) {
         var1 = "";
      } else {
         int var2 = this.scheme.length() + 3;
         int var3 = Util.delimiterOffset(this.url, var2, this.url.length(), ":@");
         var1 = this.url.substring(var2, var3);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof HttpUrl && ((HttpUrl)var1).url.equals(this.url)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public String fragment() {
      return this.fragment;
   }

   public int hashCode() {
      return this.url.hashCode();
   }

   public String host() {
      return this.host;
   }

   public boolean isHttps() {
      return this.scheme.equals("https");
   }

   public HttpUrl.Builder newBuilder() {
      HttpUrl.Builder var1 = new HttpUrl.Builder();
      var1.scheme = this.scheme;
      var1.encodedUsername = this.encodedUsername();
      var1.encodedPassword = this.encodedPassword();
      var1.host = this.host;
      int var2;
      if (this.port != defaultPort(this.scheme)) {
         var2 = this.port;
      } else {
         var2 = -1;
      }

      var1.port = var2;
      var1.encodedPathSegments.clear();
      var1.encodedPathSegments.addAll(this.encodedPathSegments());
      var1.encodedQuery(this.encodedQuery());
      var1.encodedFragment = this.encodedFragment();
      return var1;
   }

   public HttpUrl.Builder newBuilder(String var1) {
      HttpUrl.Builder var2 = new HttpUrl.Builder();
      HttpUrl.Builder var3;
      if (var2.parse(this, var1) == HttpUrl.Builder.ParseResult.SUCCESS) {
         var3 = var2;
      } else {
         var3 = null;
      }

      return var3;
   }

   public String password() {
      return this.password;
   }

   public List pathSegments() {
      return this.pathSegments;
   }

   public int pathSize() {
      return this.pathSegments.size();
   }

   public int port() {
      return this.port;
   }

   public String query() {
      String var1;
      if (this.queryNamesAndValues == null) {
         var1 = null;
      } else {
         StringBuilder var2 = new StringBuilder();
         namesAndValuesToQueryString(var2, this.queryNamesAndValues);
         var1 = var2.toString();
      }

      return var1;
   }

   public String queryParameter(String var1) {
      Object var2 = null;
      String var3;
      if (this.queryNamesAndValues == null) {
         var3 = (String)var2;
      } else {
         int var4 = 0;
         int var5 = this.queryNamesAndValues.size();

         while(true) {
            var3 = (String)var2;
            if (var4 >= var5) {
               break;
            }

            if (var1.equals(this.queryNamesAndValues.get(var4))) {
               var3 = (String)this.queryNamesAndValues.get(var4 + 1);
               break;
            }

            var4 += 2;
         }
      }

      return var3;
   }

   public String queryParameterName(int var1) {
      if (this.queryNamesAndValues == null) {
         throw new IndexOutOfBoundsException();
      } else {
         return (String)this.queryNamesAndValues.get(var1 * 2);
      }
   }

   public Set queryParameterNames() {
      Set var1;
      if (this.queryNamesAndValues == null) {
         var1 = Collections.emptySet();
      } else {
         LinkedHashSet var4 = new LinkedHashSet();
         int var2 = 0;

         for(int var3 = this.queryNamesAndValues.size(); var2 < var3; var2 += 2) {
            var4.add(this.queryNamesAndValues.get(var2));
         }

         var1 = Collections.unmodifiableSet(var4);
      }

      return var1;
   }

   public String queryParameterValue(int var1) {
      if (this.queryNamesAndValues == null) {
         throw new IndexOutOfBoundsException();
      } else {
         return (String)this.queryNamesAndValues.get(var1 * 2 + 1);
      }
   }

   public List queryParameterValues(String var1) {
      List var5;
      if (this.queryNamesAndValues == null) {
         var5 = Collections.emptyList();
      } else {
         ArrayList var2 = new ArrayList();
         int var3 = 0;

         for(int var4 = this.queryNamesAndValues.size(); var3 < var4; var3 += 2) {
            if (var1.equals(this.queryNamesAndValues.get(var3))) {
               var2.add(this.queryNamesAndValues.get(var3 + 1));
            }
         }

         var5 = Collections.unmodifiableList(var2);
      }

      return var5;
   }

   public int querySize() {
      int var1;
      if (this.queryNamesAndValues != null) {
         var1 = this.queryNamesAndValues.size() / 2;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public String redact() {
      return this.newBuilder("/...").username("").password("").build().toString();
   }

   public HttpUrl resolve(String var1) {
      HttpUrl.Builder var2 = this.newBuilder(var1);
      HttpUrl var3;
      if (var2 != null) {
         var3 = var2.build();
      } else {
         var3 = null;
      }

      return var3;
   }

   public String scheme() {
      return this.scheme;
   }

   public String toString() {
      return this.url;
   }

   public URI uri() {
      String var1 = this.newBuilder().reencodeForUri().toString();

      URI var2;
      try {
         var2 = new URI(var1);
      } catch (URISyntaxException var5) {
         try {
            var2 = URI.create(var1.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
         } catch (Exception var4) {
            throw new RuntimeException(var5);
         }
      }

      return var2;
   }

   public URL url() {
      try {
         URL var1 = new URL(this.url);
         return var1;
      } catch (MalformedURLException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String username() {
      return this.username;
   }

   public static final class Builder {
      String encodedFragment;
      String encodedPassword = "";
      final List encodedPathSegments = new ArrayList();
      List encodedQueryNamesAndValues;
      String encodedUsername = "";
      String host;
      int port = -1;
      String scheme;

      public Builder() {
         this.encodedPathSegments.add("");
      }

      private HttpUrl.Builder addPathSegments(String var1, boolean var2) {
         int var3 = 0;

         int var4;
         do {
            var4 = Util.delimiterOffset(var1, var3, var1.length(), "/\\");
            boolean var5;
            if (var4 < var1.length()) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.push(var1, var3, var4, var5, var2);
            ++var4;
            var3 = var4;
         } while(var4 <= var1.length());

         return this;
      }

      private static String canonicalizeHost(String var0, int var1, int var2) {
         var0 = HttpUrl.percentDecode(var0, var1, var2, false);
         if (var0.contains(":")) {
            InetAddress var3;
            if (var0.startsWith("[") && var0.endsWith("]")) {
               var3 = decodeIpv6(var0, 1, var0.length() - 1);
            } else {
               var3 = decodeIpv6(var0, 0, var0.length());
            }

            if (var3 == null) {
               var0 = null;
            } else {
               byte[] var4 = var3.getAddress();
               if (var4.length != 16) {
                  throw new AssertionError();
               }

               var0 = inet6AddressToAscii(var4);
            }
         } else {
            var0 = Util.domainToAscii(var0);
         }

         return var0;
      }

      private static boolean decodeIpv4Suffix(String var0, int var1, int var2, byte[] var3, int var4) {
         boolean var5 = false;
         int var6 = var4;
         int var7 = var1;

         boolean var8;
         while(true) {
            if (var7 >= var2) {
               var8 = var5;
               if (var6 == var4 + 4) {
                  var8 = true;
               }
               break;
            }

            if (var6 == var3.length) {
               var8 = var5;
               break;
            }

            var1 = var7;
            if (var6 != var4) {
               var8 = var5;
               if (var0.charAt(var7) != '.') {
                  break;
               }

               var1 = var7 + 1;
            }

            int var9 = 0;

            for(var7 = var1; var7 < var2; ++var7) {
               char var10 = var0.charAt(var7);
               if (var10 < '0' || var10 > '9') {
                  break;
               }

               if (var9 == 0) {
                  var8 = var5;
                  if (var1 != var7) {
                     return var8;
                  }
               }

               var9 = var9 * 10 + var10 - 48;
               var8 = var5;
               if (var9 > 255) {
                  return var8;
               }
            }

            var8 = var5;
            if (var7 - var1 == 0) {
               break;
            }

            var3[var6] = (byte)((byte)var9);
            ++var6;
         }

         return var8;
      }

      private static InetAddress decodeIpv6(String var0, int var1, int var2) {
         byte[] var3 = new byte[16];
         int var4 = 0;
         int var5 = -1;
         int var6 = -1;
         int var7 = var1;

         int var8;
         InetAddress var11;
         while(true) {
            var1 = var4;
            var8 = var5;
            if (var7 >= var2) {
               break;
            }

            if (var4 == var3.length) {
               var11 = null;
               return var11;
            }

            int var9;
            if (var7 + 2 <= var2 && var0.regionMatches(var7, "::", 0, 2)) {
               if (var5 != -1) {
                  var11 = null;
                  return var11;
               }

               var7 += 2;
               var4 += 2;
               var9 = var4;
               var8 = var4;
               var1 = var7;
               if (var7 == var2) {
                  var8 = var4;
                  var1 = var4;
                  break;
               }
            } else {
               var9 = var4;
               var8 = var5;
               var1 = var7;
               if (var4 != 0) {
                  if (!var0.regionMatches(var7, ":", 0, 1)) {
                     if (!var0.regionMatches(var7, ".", 0, 1)) {
                        var11 = null;
                        return var11;
                     }

                     if (!decodeIpv4Suffix(var0, var6, var2, var3, var4 - 2)) {
                        var11 = null;
                        return var11;
                     }

                     var1 = var4 + 2;
                     var8 = var5;
                     break;
                  }

                  var1 = var7 + 1;
                  var8 = var5;
                  var9 = var4;
               }
            }

            var4 = 0;

            for(var7 = var1; var1 < var2; ++var1) {
               var5 = HttpUrl.decodeHexDigit(var0.charAt(var1));
               if (var5 == -1) {
                  break;
               }

               var4 = (var4 << 4) + var5;
            }

            var5 = var1 - var7;
            if (var5 == 0 || var5 > 4) {
               var11 = null;
               return var11;
            }

            var6 = var9 + 1;
            var3[var9] = (byte)((byte)(var4 >>> 8 & 255));
            var5 = var6 + 1;
            var3[var6] = (byte)((byte)(var4 & 255));
            var4 = var5;
            var5 = var8;
            var6 = var7;
            var7 = var1;
         }

         if (var1 != var3.length) {
            if (var8 == -1) {
               var11 = null;
               return var11;
            }

            System.arraycopy(var3, var8, var3, var3.length - (var1 - var8), var1 - var8);
            Arrays.fill(var3, var8, var3.length - var1 + var8, (byte)0);
         }

         try {
            var11 = InetAddress.getByAddress(var3);
         } catch (UnknownHostException var10) {
            throw new AssertionError();
         }

         return var11;
      }

      private static String inet6AddressToAscii(byte[] var0) {
         int var1 = -1;
         int var2 = 0;

         int var3;
         int var4;
         for(var3 = 0; var3 < var0.length; var2 = var4) {
            var4 = var3;

            while(true) {
               int var5 = var4;
               if (var4 >= 16 || var0[var4] != 0 || var0[var4 + 1] != 0) {
                  int var6 = var4 - var3;
                  var4 = var2;
                  if (var6 > var2) {
                     var4 = var6;
                     var1 = var3;
                  }

                  var3 = var5 + 2;
                  break;
               }

               var4 += 2;
            }
         }

         Buffer var7 = new Buffer();
         var3 = 0;

         while(var3 < var0.length) {
            if (var3 == var1) {
               var7.writeByte(58);
               var4 = var3 + var2;
               var3 = var4;
               if (var4 == 16) {
                  var7.writeByte(58);
                  var3 = var4;
               }
            } else {
               if (var3 > 0) {
                  var7.writeByte(58);
               }

               var7.writeHexadecimalUnsignedLong((long)((var0[var3] & 255) << 8 | var0[var3 + 1] & 255));
               var3 += 2;
            }
         }

         return var7.readUtf8();
      }

      private boolean isDot(String var1) {
         boolean var2;
         if (!var1.equals(".") && !var1.equalsIgnoreCase("%2e")) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      private boolean isDotDot(String var1) {
         boolean var2;
         if (!var1.equals("..") && !var1.equalsIgnoreCase("%2e.") && !var1.equalsIgnoreCase(".%2e") && !var1.equalsIgnoreCase("%2e%2e")) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      private static int parsePort(String var0, int var1, int var2) {
         try {
            var1 = Integer.parseInt(HttpUrl.canonicalize(var0, var1, var2, "", false, false, false, true));
         } catch (NumberFormatException var3) {
            var1 = -1;
            return var1;
         }

         if (var1 <= 0 || var1 > 65535) {
            var1 = -1;
         }

         return var1;
      }

      private void pop() {
         if (((String)this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1)).isEmpty() && !this.encodedPathSegments.isEmpty()) {
            this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
         } else {
            this.encodedPathSegments.add("");
         }

      }

      private static int portColonOffset(String var0, int var1, int var2) {
         while(true) {
            int var3;
            label22: {
               int var4;
               if (var1 < var2) {
                  var3 = var1;
                  var4 = var1;
                  switch(var0.charAt(var1)) {
                  case ':':
                     break;
                  case '[':
                     do {
                        var1 = var3 + 1;
                        var3 = var1;
                        if (var1 >= var2) {
                           break label22;
                        }

                        var3 = var1;
                     } while(var0.charAt(var1) != ']');

                     var3 = var1;
                     break label22;
                  default:
                     var3 = var1;
                     break label22;
                  }
               } else {
                  var4 = var2;
               }

               return var4;
            }

            var1 = var3 + 1;
         }
      }

      private void push(String var1, int var2, int var3, boolean var4, boolean var5) {
         var1 = HttpUrl.canonicalize(var1, var2, var3, " \"<>^`{}|/\\?#", var5, false, false, true);
         if (!this.isDot(var1)) {
            if (this.isDotDot(var1)) {
               this.pop();
            } else {
               if (((String)this.encodedPathSegments.get(this.encodedPathSegments.size() - 1)).isEmpty()) {
                  this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, var1);
               } else {
                  this.encodedPathSegments.add(var1);
               }

               if (var4) {
                  this.encodedPathSegments.add("");
               }
            }
         }

      }

      private void removeAllCanonicalQueryParameters(String var1) {
         for(int var2 = this.encodedQueryNamesAndValues.size() - 2; var2 >= 0; var2 -= 2) {
            if (var1.equals(this.encodedQueryNamesAndValues.get(var2))) {
               this.encodedQueryNamesAndValues.remove(var2 + 1);
               this.encodedQueryNamesAndValues.remove(var2);
               if (this.encodedQueryNamesAndValues.isEmpty()) {
                  this.encodedQueryNamesAndValues = null;
                  break;
               }
            }
         }

      }

      private void resolvePath(String var1, int var2, int var3) {
         if (var2 != var3) {
            char var4 = var1.charAt(var2);
            if (var4 != '/' && var4 != '\\') {
               this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
            } else {
               this.encodedPathSegments.clear();
               this.encodedPathSegments.add("");
               ++var2;
            }

            while(var2 < var3) {
               int var6 = Util.delimiterOffset(var1, var2, var3, "/\\");
               boolean var5;
               if (var6 < var3) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               this.push(var1, var2, var6, var5, true);
               var2 = var6;
               if (var5) {
                  var2 = var6 + 1;
               }
            }
         }

      }

      private static int schemeDelimiterOffset(String var0, int var1, int var2) {
         if (var2 - var1 < 2) {
            var1 = -1;
         } else {
            char var3 = var0.charAt(var1);
            if (var3 >= 'a' && var3 <= 'z' || var3 >= 'A' && var3 <= 'Z') {
               ++var1;

               while(true) {
                  if (var1 >= var2) {
                     var1 = -1;
                     break;
                  }

                  var3 = var0.charAt(var1);
                  if ((var3 < 'a' || var3 > 'z') && (var3 < 'A' || var3 > 'Z') && (var3 < '0' || var3 > '9') && var3 != '+' && var3 != '-' && var3 != '.') {
                     if (var3 != ':') {
                        var1 = -1;
                     }
                     break;
                  }

                  ++var1;
               }
            } else {
               var1 = -1;
            }
         }

         return var1;
      }

      private static int slashCount(String var0, int var1, int var2) {
         int var3;
         for(var3 = 0; var1 < var2; ++var1) {
            char var4 = var0.charAt(var1);
            if (var4 != '\\' && var4 != '/') {
               break;
            }

            ++var3;
         }

         return var3;
      }

      public HttpUrl.Builder addEncodedPathSegment(String var1) {
         if (var1 == null) {
            throw new NullPointerException("encodedPathSegment == null");
         } else {
            this.push(var1, 0, var1.length(), false, true);
            return this;
         }
      }

      public HttpUrl.Builder addEncodedPathSegments(String var1) {
         if (var1 == null) {
            throw new NullPointerException("encodedPathSegments == null");
         } else {
            return this.addPathSegments(var1, true);
         }
      }

      public HttpUrl.Builder addEncodedQueryParameter(String var1, String var2) {
         if (var1 == null) {
            throw new NullPointerException("encodedName == null");
         } else {
            if (this.encodedQueryNamesAndValues == null) {
               this.encodedQueryNamesAndValues = new ArrayList();
            }

            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(var1, " \"'<>#&=", true, false, true, true));
            List var3 = this.encodedQueryNamesAndValues;
            if (var2 != null) {
               var1 = HttpUrl.canonicalize(var2, " \"'<>#&=", true, false, true, true);
            } else {
               var1 = null;
            }

            var3.add(var1);
            return this;
         }
      }

      public HttpUrl.Builder addPathSegment(String var1) {
         if (var1 == null) {
            throw new NullPointerException("pathSegment == null");
         } else {
            this.push(var1, 0, var1.length(), false, false);
            return this;
         }
      }

      public HttpUrl.Builder addPathSegments(String var1) {
         if (var1 == null) {
            throw new NullPointerException("pathSegments == null");
         } else {
            return this.addPathSegments(var1, false);
         }
      }

      public HttpUrl.Builder addQueryParameter(String var1, String var2) {
         if (var1 == null) {
            throw new NullPointerException("name == null");
         } else {
            if (this.encodedQueryNamesAndValues == null) {
               this.encodedQueryNamesAndValues = new ArrayList();
            }

            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(var1, " \"'<>#&=", false, false, true, true));
            List var3 = this.encodedQueryNamesAndValues;
            if (var2 != null) {
               var1 = HttpUrl.canonicalize(var2, " \"'<>#&=", false, false, true, true);
            } else {
               var1 = null;
            }

            var3.add(var1);
            return this;
         }
      }

      public HttpUrl build() {
         if (this.scheme == null) {
            throw new IllegalStateException("scheme == null");
         } else if (this.host == null) {
            throw new IllegalStateException("host == null");
         } else {
            return new HttpUrl(this);
         }
      }

      int effectivePort() {
         int var1;
         if (this.port != -1) {
            var1 = this.port;
         } else {
            var1 = HttpUrl.defaultPort(this.scheme);
         }

         return var1;
      }

      public HttpUrl.Builder encodedFragment(String var1) {
         if (var1 != null) {
            var1 = HttpUrl.canonicalize(var1, "", true, false, false, false);
         } else {
            var1 = null;
         }

         this.encodedFragment = var1;
         return this;
      }

      public HttpUrl.Builder encodedPassword(String var1) {
         if (var1 == null) {
            throw new NullPointerException("encodedPassword == null");
         } else {
            this.encodedPassword = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
         }
      }

      public HttpUrl.Builder encodedPath(String var1) {
         if (var1 == null) {
            throw new NullPointerException("encodedPath == null");
         } else if (!var1.startsWith("/")) {
            throw new IllegalArgumentException("unexpected encodedPath: " + var1);
         } else {
            this.resolvePath(var1, 0, var1.length());
            return this;
         }
      }

      public HttpUrl.Builder encodedQuery(String var1) {
         List var2;
         if (var1 != null) {
            var2 = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var1, " \"'<>#", true, false, true, true));
         } else {
            var2 = null;
         }

         this.encodedQueryNamesAndValues = var2;
         return this;
      }

      public HttpUrl.Builder encodedUsername(String var1) {
         if (var1 == null) {
            throw new NullPointerException("encodedUsername == null");
         } else {
            this.encodedUsername = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
         }
      }

      public HttpUrl.Builder fragment(String var1) {
         if (var1 != null) {
            var1 = HttpUrl.canonicalize(var1, "", false, false, false, false);
         } else {
            var1 = null;
         }

         this.encodedFragment = var1;
         return this;
      }

      public HttpUrl.Builder host(String var1) {
         if (var1 == null) {
            throw new NullPointerException("host == null");
         } else {
            String var2 = canonicalizeHost(var1, 0, var1.length());
            if (var2 == null) {
               throw new IllegalArgumentException("unexpected host: " + var1);
            } else {
               this.host = var2;
               return this;
            }
         }
      }

      HttpUrl.Builder.ParseResult parse(HttpUrl var1, String var2) {
         int var3 = Util.skipLeadingAsciiWhitespace(var2, 0, var2.length());
         int var4 = Util.skipTrailingAsciiWhitespace(var2, var3, var2.length());
         HttpUrl.Builder.ParseResult var10;
         if (schemeDelimiterOffset(var2, var3, var4) != -1) {
            if (var2.regionMatches(true, var3, "https:", 0, 6)) {
               this.scheme = "https";
               var3 += "https:".length();
            } else {
               if (!var2.regionMatches(true, var3, "http:", 0, 5)) {
                  var10 = HttpUrl.Builder.ParseResult.UNSUPPORTED_SCHEME;
                  return var10;
               }

               this.scheme = "http";
               var3 += "http:".length();
            }
         } else {
            if (var1 == null) {
               var10 = HttpUrl.Builder.ParseResult.MISSING_SCHEME;
               return var10;
            }

            this.scheme = var1.scheme;
         }

         boolean var5 = false;
         boolean var6 = false;
         int var7 = slashCount(var2, var3, var4);
         int var13;
         if (var7 < 2 && var1 != null && var1.scheme.equals(this.scheme)) {
            label80: {
               this.encodedUsername = var1.encodedUsername();
               this.encodedPassword = var1.encodedPassword();
               this.host = var1.host;
               this.port = var1.port;
               this.encodedPathSegments.clear();
               this.encodedPathSegments.addAll(var1.encodedPathSegments());
               if (var3 != var4) {
                  var13 = var3;
                  if (var2.charAt(var3) != '#') {
                     break label80;
                  }
               }

               this.encodedQuery(var1.encodedQuery());
               var13 = var3;
            }
         } else {
            var7 += var3;
            boolean var12 = var6;
            var13 = var7;

            label74:
            while(true) {
               int var8 = Util.delimiterOffset(var2, var13, var4, "@/\\?#");
               if (var8 != var4) {
                  var7 = var2.charAt(var8);
               } else {
                  var7 = -1;
               }

               switch(var7) {
               case -1:
               case 35:
               case 47:
               case 63:
               case 92:
                  var3 = portColonOffset(var2, var13, var8);
                  if (var3 + 1 < var8) {
                     this.host = canonicalizeHost(var2, var13, var3);
                     this.port = parsePort(var2, var3 + 1, var8);
                     if (this.port == -1) {
                        var10 = HttpUrl.Builder.ParseResult.INVALID_PORT;
                        return var10;
                     }
                  } else {
                     this.host = canonicalizeHost(var2, var13, var3);
                     this.port = HttpUrl.defaultPort(this.scheme);
                  }

                  if (this.host == null) {
                     var10 = HttpUrl.Builder.ParseResult.INVALID_HOST;
                     return var10;
                  }

                  var13 = var8;
                  break label74;
               case 64:
                  if (!var12) {
                     var7 = Util.delimiterOffset(var2, var13, var8, ':');
                     String var9 = HttpUrl.canonicalize(var2, var13, var7, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                     String var11 = var9;
                     if (var5) {
                        var11 = this.encodedUsername + "%40" + var9;
                     }

                     this.encodedUsername = var11;
                     if (var7 != var8) {
                        var12 = true;
                        this.encodedPassword = HttpUrl.canonicalize(var2, var7 + 1, var8, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                     }

                     var5 = true;
                  } else {
                     this.encodedPassword = this.encodedPassword + "%40" + HttpUrl.canonicalize(var2, var13, var8, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                  }

                  var13 = var8 + 1;
               }
            }
         }

         var3 = Util.delimiterOffset(var2, var13, var4, "?#");
         this.resolvePath(var2, var13, var3);
         var13 = var3;
         if (var3 < var4) {
            var13 = var3;
            if (var2.charAt(var3) == '?') {
               var13 = Util.delimiterOffset(var2, var3, var4, '#');
               this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var2, var3 + 1, var13, " \"'<>#", true, false, true, true));
            }
         }

         if (var13 < var4 && var2.charAt(var13) == '#') {
            this.encodedFragment = HttpUrl.canonicalize(var2, var13 + 1, var4, "", true, false, false, false);
         }

         var10 = HttpUrl.Builder.ParseResult.SUCCESS;
         return var10;
      }

      public HttpUrl.Builder password(String var1) {
         if (var1 == null) {
            throw new NullPointerException("password == null");
         } else {
            this.encodedPassword = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
         }
      }

      public HttpUrl.Builder port(int var1) {
         if (var1 > 0 && var1 <= 65535) {
            this.port = var1;
            return this;
         } else {
            throw new IllegalArgumentException("unexpected port: " + var1);
         }
      }

      public HttpUrl.Builder query(String var1) {
         List var2;
         if (var1 != null) {
            var2 = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var1, " \"'<>#", false, false, true, true));
         } else {
            var2 = null;
         }

         this.encodedQueryNamesAndValues = var2;
         return this;
      }

      HttpUrl.Builder reencodeForUri() {
         int var1 = 0;

         int var2;
         String var3;
         for(var2 = this.encodedPathSegments.size(); var1 < var2; ++var1) {
            var3 = (String)this.encodedPathSegments.get(var1);
            this.encodedPathSegments.set(var1, HttpUrl.canonicalize(var3, "[]", true, true, false, true));
         }

         if (this.encodedQueryNamesAndValues != null) {
            var1 = 0;

            for(var2 = this.encodedQueryNamesAndValues.size(); var1 < var2; ++var1) {
               var3 = (String)this.encodedQueryNamesAndValues.get(var1);
               if (var3 != null) {
                  this.encodedQueryNamesAndValues.set(var1, HttpUrl.canonicalize(var3, "\\^`{|}", true, true, true, true));
               }
            }
         }

         if (this.encodedFragment != null) {
            this.encodedFragment = HttpUrl.canonicalize(this.encodedFragment, " \"#<>\\^`{|}", true, true, false, false);
         }

         return this;
      }

      public HttpUrl.Builder removeAllEncodedQueryParameters(String var1) {
         if (var1 == null) {
            throw new NullPointerException("encodedName == null");
         } else {
            if (this.encodedQueryNamesAndValues != null) {
               this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(var1, " \"'<>#&=", true, false, true, true));
            }

            return this;
         }
      }

      public HttpUrl.Builder removeAllQueryParameters(String var1) {
         if (var1 == null) {
            throw new NullPointerException("name == null");
         } else {
            if (this.encodedQueryNamesAndValues != null) {
               this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(var1, " \"'<>#&=", false, false, true, true));
            }

            return this;
         }
      }

      public HttpUrl.Builder removePathSegment(int var1) {
         this.encodedPathSegments.remove(var1);
         if (this.encodedPathSegments.isEmpty()) {
            this.encodedPathSegments.add("");
         }

         return this;
      }

      public HttpUrl.Builder scheme(String var1) {
         if (var1 == null) {
            throw new NullPointerException("scheme == null");
         } else {
            if (var1.equalsIgnoreCase("http")) {
               this.scheme = "http";
            } else {
               if (!var1.equalsIgnoreCase("https")) {
                  throw new IllegalArgumentException("unexpected scheme: " + var1);
               }

               this.scheme = "https";
            }

            return this;
         }
      }

      public HttpUrl.Builder setEncodedPathSegment(int var1, String var2) {
         if (var2 == null) {
            throw new NullPointerException("encodedPathSegment == null");
         } else {
            String var3 = HttpUrl.canonicalize(var2, 0, var2.length(), " \"<>^`{}|/\\?#", true, false, false, true);
            this.encodedPathSegments.set(var1, var3);
            if (!this.isDot(var3) && !this.isDotDot(var3)) {
               return this;
            } else {
               throw new IllegalArgumentException("unexpected path segment: " + var2);
            }
         }
      }

      public HttpUrl.Builder setEncodedQueryParameter(String var1, String var2) {
         this.removeAllEncodedQueryParameters(var1);
         this.addEncodedQueryParameter(var1, var2);
         return this;
      }

      public HttpUrl.Builder setPathSegment(int var1, String var2) {
         if (var2 == null) {
            throw new NullPointerException("pathSegment == null");
         } else {
            String var3 = HttpUrl.canonicalize(var2, 0, var2.length(), " \"<>^`{}|/\\?#", false, false, false, true);
            if (!this.isDot(var3) && !this.isDotDot(var3)) {
               this.encodedPathSegments.set(var1, var3);
               return this;
            } else {
               throw new IllegalArgumentException("unexpected path segment: " + var2);
            }
         }
      }

      public HttpUrl.Builder setQueryParameter(String var1, String var2) {
         this.removeAllQueryParameters(var1);
         this.addQueryParameter(var1, var2);
         return this;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.scheme);
         var1.append("://");
         if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
            var1.append(this.encodedUsername);
            if (!this.encodedPassword.isEmpty()) {
               var1.append(':');
               var1.append(this.encodedPassword);
            }

            var1.append('@');
         }

         if (this.host.indexOf(58) != -1) {
            var1.append('[');
            var1.append(this.host);
            var1.append(']');
         } else {
            var1.append(this.host);
         }

         int var2 = this.effectivePort();
         if (var2 != HttpUrl.defaultPort(this.scheme)) {
            var1.append(':');
            var1.append(var2);
         }

         HttpUrl.pathSegmentsToString(var1, this.encodedPathSegments);
         if (this.encodedQueryNamesAndValues != null) {
            var1.append('?');
            HttpUrl.namesAndValuesToQueryString(var1, this.encodedQueryNamesAndValues);
         }

         if (this.encodedFragment != null) {
            var1.append('#');
            var1.append(this.encodedFragment);
         }

         return var1.toString();
      }

      public HttpUrl.Builder username(String var1) {
         if (var1 == null) {
            throw new NullPointerException("username == null");
         } else {
            this.encodedUsername = HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
         }
      }

      static enum ParseResult {
         INVALID_HOST,
         INVALID_PORT,
         MISSING_SCHEME,
         SUCCESS,
         UNSUPPORTED_SCHEME;
      }
   }
}
