package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie {
   private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
   private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
   private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
   private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
   private final String domain;
   private final long expiresAt;
   private final boolean hostOnly;
   private final boolean httpOnly;
   private final String name;
   private final String path;
   private final boolean persistent;
   private final boolean secure;
   private final String value;

   private Cookie(String var1, String var2, long var3, String var5, String var6, boolean var7, boolean var8, boolean var9, boolean var10) {
      this.name = var1;
      this.value = var2;
      this.expiresAt = var3;
      this.domain = var5;
      this.path = var6;
      this.secure = var7;
      this.httpOnly = var8;
      this.hostOnly = var9;
      this.persistent = var10;
   }

   Cookie(Cookie.Builder var1) {
      if (var1.name == null) {
         throw new NullPointerException("builder.name == null");
      } else if (var1.value == null) {
         throw new NullPointerException("builder.value == null");
      } else if (var1.domain == null) {
         throw new NullPointerException("builder.domain == null");
      } else {
         this.name = var1.name;
         this.value = var1.value;
         this.expiresAt = var1.expiresAt;
         this.domain = var1.domain;
         this.path = var1.path;
         this.secure = var1.secure;
         this.httpOnly = var1.httpOnly;
         this.persistent = var1.persistent;
         this.hostOnly = var1.hostOnly;
      }
   }

   private static int dateCharacterOffset(String var0, int var1, int var2, boolean var3) {
      while(true) {
         if (var1 < var2) {
            char var4 = var0.charAt(var1);
            boolean var6;
            if ((var4 >= ' ' || var4 == '\t') && var4 < 127 && (var4 < '0' || var4 > '9') && (var4 < 'a' || var4 > 'z') && (var4 < 'A' || var4 > 'Z') && var4 != ':') {
               var6 = false;
            } else {
               var6 = true;
            }

            boolean var5;
            if (!var3) {
               var5 = true;
            } else {
               var5 = false;
            }

            if (var6 != var5) {
               ++var1;
               continue;
            }
         } else {
            var1 = var2;
         }

         return var1;
      }
   }

   private static boolean domainMatch(HttpUrl var0, String var1) {
      boolean var2 = true;
      String var3 = var0.host();
      if (!var3.equals(var1) && (!var3.endsWith(var1) || var3.charAt(var3.length() - var1.length() - 1) != '.' || Util.verifyAsIpAddress(var3))) {
         var2 = false;
      }

      return var2;
   }

   static Cookie parse(long var0, HttpUrl var2, String var3) {
      int var4 = var3.length();
      int var5 = Util.delimiterOffset(var3, 0, var4, ';');
      int var6 = Util.delimiterOffset(var3, 0, var5, '=');
      Cookie var33;
      if (var6 == var5) {
         var33 = null;
      } else {
         String var7 = Util.trimSubstring(var3, 0, var6);
         if (!var7.isEmpty() && Util.indexOfControlOrNonAscii(var7) == -1) {
            String var8 = Util.trimSubstring(var3, var6 + 1, var5);
            if (Util.indexOfControlOrNonAscii(var8) != -1) {
               var33 = null;
            } else {
               long var9 = 253402300799999L;
               long var11 = -1L;
               String var13 = null;
               String var14 = null;
               boolean var15 = false;
               boolean var16 = false;
               boolean var17 = true;
               boolean var18 = false;
               ++var5;

               String var21;
               while(var5 < var4) {
                  var6 = Util.delimiterOffset(var3, var5, var4, ';');
                  int var19 = Util.delimiterOffset(var3, var5, var6, '=');
                  String var20 = Util.trimSubstring(var3, var5, var19);
                  if (var19 < var6) {
                     var21 = Util.trimSubstring(var3, var19 + 1, var6);
                  } else {
                     var21 = "";
                  }

                  long var22;
                  boolean var24;
                  long var25;
                  boolean var27;
                  boolean var28;
                  String var29;
                  if (var20.equalsIgnoreCase("expires")) {
                     label89: {
                        try {
                           var22 = parseExpires(var21, 0, var21.length());
                        } catch (IllegalArgumentException var30) {
                           var22 = var9;
                           var21 = var13;
                           var29 = var14;
                           var28 = var15;
                           var27 = var17;
                           var24 = var18;
                           var25 = var11;
                           break label89;
                        }

                        var24 = true;
                        var25 = var11;
                        var27 = var17;
                        var28 = var15;
                        var29 = var14;
                        var21 = var13;
                     }
                  } else if (var20.equalsIgnoreCase("max-age")) {
                     label91: {
                        try {
                           var25 = parseMaxAge(var21);
                        } catch (NumberFormatException var31) {
                           var22 = var9;
                           var21 = var13;
                           var29 = var14;
                           var28 = var15;
                           var27 = var17;
                           var24 = var18;
                           var25 = var11;
                           break label91;
                        }

                        var24 = true;
                        var22 = var9;
                        var21 = var13;
                        var29 = var14;
                        var28 = var15;
                        var27 = var17;
                     }
                  } else if (var20.equalsIgnoreCase("domain")) {
                     label93: {
                        try {
                           var21 = parseDomain(var21);
                        } catch (IllegalArgumentException var32) {
                           var22 = var9;
                           var21 = var13;
                           var29 = var14;
                           var28 = var15;
                           var27 = var17;
                           var24 = var18;
                           var25 = var11;
                           break label93;
                        }

                        var27 = false;
                        var22 = var9;
                        var29 = var14;
                        var28 = var15;
                        var24 = var18;
                        var25 = var11;
                     }
                  } else if (var20.equalsIgnoreCase("path")) {
                     var29 = var21;
                     var22 = var9;
                     var21 = var13;
                     var28 = var15;
                     var27 = var17;
                     var24 = var18;
                     var25 = var11;
                  } else if (var20.equalsIgnoreCase("secure")) {
                     var28 = true;
                     var22 = var9;
                     var21 = var13;
                     var29 = var14;
                     var27 = var17;
                     var24 = var18;
                     var25 = var11;
                  } else {
                     var22 = var9;
                     var21 = var13;
                     var29 = var14;
                     var28 = var15;
                     var27 = var17;
                     var24 = var18;
                     var25 = var11;
                     if (var20.equalsIgnoreCase("httponly")) {
                        var16 = true;
                        var22 = var9;
                        var21 = var13;
                        var29 = var14;
                        var28 = var15;
                        var27 = var17;
                        var24 = var18;
                        var25 = var11;
                     }
                  }

                  var5 = var6 + 1;
                  var9 = var22;
                  var13 = var21;
                  var14 = var29;
                  var15 = var28;
                  var17 = var27;
                  var18 = var24;
                  var11 = var25;
               }

               if (var11 == Long.MIN_VALUE) {
                  var9 = Long.MIN_VALUE;
               } else if (var11 != -1L) {
                  label119: {
                     if (var11 <= 9223372036854775L) {
                        var9 = var11 * 1000L;
                     } else {
                        var9 = Long.MAX_VALUE;
                     }

                     var11 = var0 + var9;
                     if (var11 >= var0) {
                        var9 = var11;
                        if (var11 <= 253402300799999L) {
                           break label119;
                        }
                     }

                     var9 = 253402300799999L;
                  }
               }

               if (var13 == null) {
                  var21 = var2.host();
               } else {
                  var21 = var13;
                  if (!domainMatch(var2, var13)) {
                     var33 = null;
                     return var33;
                  }
               }

               label68: {
                  if (var14 != null) {
                     var3 = var14;
                     if (var14.startsWith("/")) {
                        break label68;
                     }
                  }

                  String var34 = var2.encodedPath();
                  var5 = var34.lastIndexOf(47);
                  if (var5 != 0) {
                     var3 = var34.substring(0, var5);
                  } else {
                     var3 = "/";
                  }
               }

               var33 = new Cookie(var7, var8, var9, var21, var3, var15, var16, var17, var18);
            }
         } else {
            var33 = null;
         }
      }

      return var33;
   }

   public static Cookie parse(HttpUrl var0, String var1) {
      return parse(System.currentTimeMillis(), var0, var1);
   }

   public static List parseAll(HttpUrl var0, Headers var1) {
      List var2 = var1.values("Set-Cookie");
      ArrayList var3 = null;
      int var4 = 0;

      ArrayList var8;
      for(int var5 = var2.size(); var4 < var5; var3 = var8) {
         Cookie var6 = parse(var0, (String)var2.get(var4));
         if (var6 == null) {
            var8 = var3;
         } else {
            var8 = var3;
            if (var3 == null) {
               var8 = new ArrayList();
            }

            var8.add(var6);
         }

         ++var4;
      }

      List var7;
      if (var3 != null) {
         var7 = Collections.unmodifiableList(var3);
      } else {
         var7 = Collections.emptyList();
      }

      return var7;
   }

   private static String parseDomain(String var0) {
      if (var0.endsWith(".")) {
         throw new IllegalArgumentException();
      } else {
         String var1 = var0;
         if (var0.startsWith(".")) {
            var1 = var0.substring(1);
         }

         var0 = Util.domainToAscii(var1);
         if (var0 == null) {
            throw new IllegalArgumentException();
         } else {
            return var0;
         }
      }
   }

   private static long parseExpires(String var0, int var1, int var2) {
      int var3 = dateCharacterOffset(var0, var1, var2, false);
      int var4 = -1;
      int var5 = -1;
      int var6 = -1;
      int var7 = -1;
      int var8 = -1;
      var1 = -1;

      int var10;
      for(Matcher var9 = TIME_PATTERN.matcher(var0); var3 < var2; var3 = var10) {
         var10 = dateCharacterOffset(var0, var3 + 1, var2, true);
         var9.region(var3, var10);
         int var11;
         int var12;
         int var13;
         int var14;
         int var15;
         if (var4 == -1 && var9.usePattern(TIME_PATTERN).matches()) {
            var11 = Integer.parseInt(var9.group(1));
            var3 = Integer.parseInt(var9.group(2));
            var12 = Integer.parseInt(var9.group(3));
            var13 = var1;
            var14 = var8;
            var15 = var7;
         } else if (var7 == -1 && var9.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
            var15 = Integer.parseInt(var9.group(1));
            var11 = var4;
            var3 = var5;
            var14 = var8;
            var12 = var6;
            var13 = var1;
         } else if (var8 == -1 && var9.usePattern(MONTH_PATTERN).matches()) {
            String var16 = var9.group(1).toLowerCase(Locale.US);
            var14 = MONTH_PATTERN.pattern().indexOf(var16) / 4;
            var15 = var7;
            var11 = var4;
            var3 = var5;
            var12 = var6;
            var13 = var1;
         } else {
            var15 = var7;
            var11 = var4;
            var3 = var5;
            var14 = var8;
            var12 = var6;
            var13 = var1;
            if (var1 == -1) {
               var15 = var7;
               var11 = var4;
               var3 = var5;
               var14 = var8;
               var12 = var6;
               var13 = var1;
               if (var9.usePattern(YEAR_PATTERN).matches()) {
                  var13 = Integer.parseInt(var9.group(1));
                  var15 = var7;
                  var11 = var4;
                  var3 = var5;
                  var14 = var8;
                  var12 = var6;
               }
            }
         }

         var10 = dateCharacterOffset(var0, var10 + 1, var2, false);
         var7 = var15;
         var4 = var11;
         var5 = var3;
         var8 = var14;
         var6 = var12;
         var1 = var13;
      }

      var2 = var1;
      if (var1 >= 70) {
         var2 = var1;
         if (var1 <= 99) {
            var2 = var1 + 1900;
         }
      }

      var1 = var2;
      if (var2 >= 0) {
         var1 = var2;
         if (var2 <= 69) {
            var1 = var2 + 2000;
         }
      }

      if (var1 < 1601) {
         throw new IllegalArgumentException();
      } else if (var8 == -1) {
         throw new IllegalArgumentException();
      } else if (var7 >= 1 && var7 <= 31) {
         if (var4 >= 0 && var4 <= 23) {
            if (var5 >= 0 && var5 <= 59) {
               if (var6 >= 0 && var6 <= 59) {
                  GregorianCalendar var17 = new GregorianCalendar(Util.UTC);
                  var17.setLenient(false);
                  var17.set(1, var1);
                  var17.set(2, var8 - 1);
                  var17.set(5, var7);
                  var17.set(11, var4);
                  var17.set(12, var5);
                  var17.set(13, var6);
                  var17.set(14, 0);
                  return var17.getTimeInMillis();
               } else {
                  throw new IllegalArgumentException();
               }
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            throw new IllegalArgumentException();
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static long parseMaxAge(String var0) {
      long var1 = Long.MIN_VALUE;

      long var3;
      try {
         var3 = Long.parseLong(var0);
      } catch (NumberFormatException var6) {
         if (var0.matches("-?\\d+")) {
            if (!var0.startsWith("-")) {
               var1 = Long.MAX_VALUE;
            }

            return var1;
         }

         throw var6;
      }

      var1 = var3;
      if (var3 <= 0L) {
         var1 = Long.MIN_VALUE;
      }

      return var1;
   }

   private static boolean pathMatch(HttpUrl var0, String var1) {
      boolean var2 = true;
      String var4 = var0.encodedPath();
      boolean var3;
      if (var4.equals(var1)) {
         var3 = var2;
      } else {
         if (var4.startsWith(var1)) {
            var3 = var2;
            if (var1.endsWith("/")) {
               return var3;
            }

            var3 = var2;
            if (var4.charAt(var1.length()) == '/') {
               return var3;
            }
         }

         var3 = false;
      }

      return var3;
   }

   public String domain() {
      return this.domain;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof Cookie)) {
         var3 = var2;
      } else {
         Cookie var4 = (Cookie)var1;
         var3 = var2;
         if (var4.name.equals(this.name)) {
            var3 = var2;
            if (var4.value.equals(this.value)) {
               var3 = var2;
               if (var4.domain.equals(this.domain)) {
                  var3 = var2;
                  if (var4.path.equals(this.path)) {
                     var3 = var2;
                     if (var4.expiresAt == this.expiresAt) {
                        var3 = var2;
                        if (var4.secure == this.secure) {
                           var3 = var2;
                           if (var4.httpOnly == this.httpOnly) {
                              var3 = var2;
                              if (var4.persistent == this.persistent) {
                                 var3 = var2;
                                 if (var4.hostOnly == this.hostOnly) {
                                    var3 = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public long expiresAt() {
      return this.expiresAt;
   }

   public int hashCode() {
      byte var1 = 0;
      int var2 = this.name.hashCode();
      int var3 = this.value.hashCode();
      int var4 = this.domain.hashCode();
      int var5 = this.path.hashCode();
      int var6 = (int)(this.expiresAt ^ this.expiresAt >>> 32);
      byte var7;
      if (this.secure) {
         var7 = 0;
      } else {
         var7 = 1;
      }

      byte var8;
      if (this.httpOnly) {
         var8 = 0;
      } else {
         var8 = 1;
      }

      byte var9;
      if (this.persistent) {
         var9 = 0;
      } else {
         var9 = 1;
      }

      if (!this.hostOnly) {
         var1 = 1;
      }

      return ((((((((var2 + 527) * 31 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9) * 31 + var1;
   }

   public boolean hostOnly() {
      return this.hostOnly;
   }

   public boolean httpOnly() {
      return this.httpOnly;
   }

   public boolean matches(HttpUrl var1) {
      boolean var2 = false;
      boolean var3;
      if (this.hostOnly) {
         var3 = var1.host().equals(this.domain);
      } else {
         var3 = domainMatch(var1, this.domain);
      }

      if (!var3) {
         var3 = var2;
      } else {
         var3 = var2;
         if (pathMatch(var1, this.path)) {
            if (this.secure) {
               var3 = var2;
               if (!var1.isHttps()) {
                  return var3;
               }
            }

            var3 = true;
         }
      }

      return var3;
   }

   public String name() {
      return this.name;
   }

   public String path() {
      return this.path;
   }

   public boolean persistent() {
      return this.persistent;
   }

   public boolean secure() {
      return this.secure;
   }

   public String toString() {
      return this.toString(false);
   }

   String toString(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.name);
      var2.append('=');
      var2.append(this.value);
      if (this.persistent) {
         if (this.expiresAt == Long.MIN_VALUE) {
            var2.append("; max-age=0");
         } else {
            var2.append("; expires=").append(HttpDate.format(new Date(this.expiresAt)));
         }
      }

      if (!this.hostOnly) {
         var2.append("; domain=");
         if (var1) {
            var2.append(".");
         }

         var2.append(this.domain);
      }

      var2.append("; path=").append(this.path);
      if (this.secure) {
         var2.append("; secure");
      }

      if (this.httpOnly) {
         var2.append("; httponly");
      }

      return var2.toString();
   }

   public String value() {
      return this.value;
   }

   public static final class Builder {
      String domain;
      long expiresAt = 253402300799999L;
      boolean hostOnly;
      boolean httpOnly;
      String name;
      String path = "/";
      boolean persistent;
      boolean secure;
      String value;

      private Cookie.Builder domain(String var1, boolean var2) {
         if (var1 == null) {
            throw new NullPointerException("domain == null");
         } else {
            String var3 = Util.domainToAscii(var1);
            if (var3 == null) {
               throw new IllegalArgumentException("unexpected domain: " + var1);
            } else {
               this.domain = var3;
               this.hostOnly = var2;
               return this;
            }
         }
      }

      public Cookie build() {
         return new Cookie(this);
      }

      public Cookie.Builder domain(String var1) {
         return this.domain(var1, false);
      }

      public Cookie.Builder expiresAt(long var1) {
         long var3 = var1;
         if (var1 <= 0L) {
            var3 = Long.MIN_VALUE;
         }

         var1 = var3;
         if (var3 > 253402300799999L) {
            var1 = 253402300799999L;
         }

         this.expiresAt = var1;
         this.persistent = true;
         return this;
      }

      public Cookie.Builder hostOnlyDomain(String var1) {
         return this.domain(var1, true);
      }

      public Cookie.Builder httpOnly() {
         this.httpOnly = true;
         return this;
      }

      public Cookie.Builder name(String var1) {
         if (var1 == null) {
            throw new NullPointerException("name == null");
         } else if (!var1.trim().equals(var1)) {
            throw new IllegalArgumentException("name is not trimmed");
         } else {
            this.name = var1;
            return this;
         }
      }

      public Cookie.Builder path(String var1) {
         if (!var1.startsWith("/")) {
            throw new IllegalArgumentException("path must start with '/'");
         } else {
            this.path = var1;
            return this;
         }
      }

      public Cookie.Builder secure() {
         this.secure = true;
         return this;
      }

      public Cookie.Builder value(String var1) {
         if (var1 == null) {
            throw new NullPointerException("value == null");
         } else if (!var1.trim().equals(var1)) {
            throw new IllegalArgumentException("value is not trimmed");
         } else {
            this.value = var1;
            return this;
         }
      }
   }
}
