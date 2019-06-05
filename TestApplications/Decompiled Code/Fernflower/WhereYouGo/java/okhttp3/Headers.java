package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Headers {
   private final String[] namesAndValues;

   Headers(Headers.Builder var1) {
      this.namesAndValues = (String[])var1.namesAndValues.toArray(new String[var1.namesAndValues.size()]);
   }

   private Headers(String[] var1) {
      this.namesAndValues = var1;
   }

   private static String get(String[] var0, String var1) {
      int var2 = var0.length - 2;

      String var3;
      while(true) {
         if (var2 < 0) {
            var3 = null;
            break;
         }

         if (var1.equalsIgnoreCase(var0[var2])) {
            var3 = var0[var2 + 1];
            break;
         }

         var2 -= 2;
      }

      return var3;
   }

   public static Headers of(Map var0) {
      if (var0 == null) {
         throw new NullPointerException("headers == null");
      } else {
         String[] var1 = new String[var0.size() * 2];
         int var2 = 0;
         Iterator var5 = var0.entrySet().iterator();

         while(var5.hasNext()) {
            Entry var3 = (Entry)var5.next();
            if (var3.getKey() != null && var3.getValue() != null) {
               String var4 = ((String)var3.getKey()).trim();
               String var6 = ((String)var3.getValue()).trim();
               if (var4.length() != 0 && var4.indexOf(0) == -1 && var6.indexOf(0) == -1) {
                  var1[var2] = var4;
                  var1[var2 + 1] = var6;
                  var2 += 2;
                  continue;
               }

               throw new IllegalArgumentException("Unexpected header: " + var4 + ": " + var6);
            }

            throw new IllegalArgumentException("Headers cannot be null");
         }

         return new Headers(var1);
      }
   }

   public static Headers of(String... var0) {
      if (var0 == null) {
         throw new NullPointerException("namesAndValues == null");
      } else if (var0.length % 2 != 0) {
         throw new IllegalArgumentException("Expected alternating header names and values");
      } else {
         var0 = (String[])var0.clone();

         int var1;
         for(var1 = 0; var1 < var0.length; ++var1) {
            if (var0[var1] == null) {
               throw new IllegalArgumentException("Headers cannot be null");
            }

            var0[var1] = var0[var1].trim();
         }

         for(var1 = 0; var1 < var0.length; var1 += 2) {
            String var2 = var0[var1];
            String var3 = var0[var1 + 1];
            if (var2.length() == 0 || var2.indexOf(0) != -1 || var3.indexOf(0) != -1) {
               throw new IllegalArgumentException("Unexpected header: " + var2 + ": " + var3);
            }
         }

         return new Headers(var0);
      }
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof Headers && Arrays.equals(((Headers)var1).namesAndValues, this.namesAndValues)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public String get(String var1) {
      return get(this.namesAndValues, var1);
   }

   public Date getDate(String var1) {
      var1 = this.get(var1);
      Date var2;
      if (var1 != null) {
         var2 = HttpDate.parse(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public int hashCode() {
      return Arrays.hashCode(this.namesAndValues);
   }

   public String name(int var1) {
      return this.namesAndValues[var1 * 2];
   }

   public Set names() {
      TreeSet var1 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         var1.add(this.name(var2));
      }

      return Collections.unmodifiableSet(var1);
   }

   public Headers.Builder newBuilder() {
      Headers.Builder var1 = new Headers.Builder();
      Collections.addAll(var1.namesAndValues, this.namesAndValues);
      return var1;
   }

   public int size() {
      return this.namesAndValues.length / 2;
   }

   public Map toMultimap() {
      TreeMap var1 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         String var4 = this.name(var2).toLowerCase(Locale.US);
         List var5 = (List)var1.get(var4);
         Object var6 = var5;
         if (var5 == null) {
            var6 = new ArrayList(2);
            var1.put(var4, var6);
         }

         ((List)var6).add(this.value(var2));
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         var1.append(this.name(var2)).append(": ").append(this.value(var2)).append("\n");
      }

      return var1.toString();
   }

   public String value(int var1) {
      return this.namesAndValues[var1 * 2 + 1];
   }

   public List values(String var1) {
      ArrayList var2 = null;
      int var3 = 0;

      ArrayList var5;
      for(int var4 = this.size(); var3 < var4; var2 = var5) {
         var5 = var2;
         if (var1.equalsIgnoreCase(this.name(var3))) {
            var5 = var2;
            if (var2 == null) {
               var5 = new ArrayList(2);
            }

            var5.add(this.value(var3));
         }

         ++var3;
      }

      List var6;
      if (var2 != null) {
         var6 = Collections.unmodifiableList(var2);
      } else {
         var6 = Collections.emptyList();
      }

      return var6;
   }

   public static final class Builder {
      final List namesAndValues = new ArrayList(20);

      private void checkNameAndValue(String var1, String var2) {
         if (var1 == null) {
            throw new NullPointerException("name == null");
         } else if (var1.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
         } else {
            int var3 = 0;

            int var4;
            char var5;
            for(var4 = var1.length(); var3 < var4; ++var3) {
               var5 = var1.charAt(var3);
               if (var5 <= ' ' || var5 >= 127) {
                  throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(var5), var3, var1));
               }
            }

            if (var2 == null) {
               throw new NullPointerException("value == null");
            } else {
               var3 = 0;

               for(var4 = var2.length(); var3 < var4; ++var3) {
                  var5 = var2.charAt(var3);
                  if (var5 <= 31 && var5 != '\t' || var5 >= 127) {
                     throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", Integer.valueOf(var5), var3, var1, var2));
                  }
               }

            }
         }
      }

      public Headers.Builder add(String var1) {
         int var2 = var1.indexOf(":");
         if (var2 == -1) {
            throw new IllegalArgumentException("Unexpected header: " + var1);
         } else {
            return this.add(var1.substring(0, var2).trim(), var1.substring(var2 + 1));
         }
      }

      public Headers.Builder add(String var1, String var2) {
         this.checkNameAndValue(var1, var2);
         return this.addLenient(var1, var2);
      }

      Headers.Builder addLenient(String var1) {
         int var2 = var1.indexOf(":", 1);
         Headers.Builder var3;
         if (var2 != -1) {
            var3 = this.addLenient(var1.substring(0, var2), var1.substring(var2 + 1));
         } else if (var1.startsWith(":")) {
            var3 = this.addLenient("", var1.substring(1));
         } else {
            var3 = this.addLenient("", var1);
         }

         return var3;
      }

      Headers.Builder addLenient(String var1, String var2) {
         this.namesAndValues.add(var1);
         this.namesAndValues.add(var2.trim());
         return this;
      }

      public Headers build() {
         return new Headers(this);
      }

      public String get(String var1) {
         int var2 = this.namesAndValues.size() - 2;

         while(true) {
            if (var2 < 0) {
               var1 = null;
               break;
            }

            if (var1.equalsIgnoreCase((String)this.namesAndValues.get(var2))) {
               var1 = (String)this.namesAndValues.get(var2 + 1);
               break;
            }

            var2 -= 2;
         }

         return var1;
      }

      public Headers.Builder removeAll(String var1) {
         int var3;
         for(int var2 = 0; var2 < this.namesAndValues.size(); var2 = var3 + 2) {
            var3 = var2;
            if (var1.equalsIgnoreCase((String)this.namesAndValues.get(var2))) {
               this.namesAndValues.remove(var2);
               this.namesAndValues.remove(var2);
               var3 = var2 - 2;
            }
         }

         return this;
      }

      public Headers.Builder set(String var1, String var2) {
         this.checkNameAndValue(var1, var2);
         this.removeAll(var1);
         this.addLenient(var1, var2);
         return this;
      }
   }
}
