package menion.android.whereyougo.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class StringToken implements Enumeration {
   private String delimiters;
   private int position;
   private String string;

   public StringToken(String var1, String var2) {
      if (var1 != null) {
         this.string = var1;
         this.delimiters = var2;
         this.position = 0;
      } else {
         throw new NullPointerException();
      }
   }

   public static ArrayList parse(String var0, String var1) {
      return parse(var0, var1, new ArrayList());
   }

   public static ArrayList parse(String var0, String var1, ArrayList var2) {
      StringToken var3 = new StringToken(var0.replace(var1, " " + var1), var1);

      while(var3.hasMoreTokens()) {
         var2.add(var3.nextToken().trim());
      }

      return var2;
   }

   public int countTokens() {
      int var1 = 0;
      boolean var2 = false;
      int var3 = this.position;

      boolean var6;
      for(int var4 = this.string.length(); var3 < var4; var2 = var6) {
         int var5;
         if (this.delimiters.indexOf(this.string.charAt(var3), 0) >= 0) {
            var5 = var1;
            var6 = var2;
            if (var2) {
               var5 = var1 + 1;
               var6 = false;
            }
         } else {
            var6 = true;
            var5 = var1;
         }

         ++var3;
         var1 = var5;
      }

      int var7 = var1;
      if (var2) {
         var7 = var1 + 1;
      }

      return var7;
   }

   public boolean hasMoreElements() {
      return this.hasMoreTokens();
   }

   public boolean hasMoreTokens() {
      boolean var1 = false;
      if (this.delimiters == null) {
         throw new NullPointerException();
      } else {
         int var2 = this.string.length();
         boolean var3 = var1;
         if (this.position < var2) {
            int var4 = this.position;

            while(true) {
               var3 = var1;
               if (var4 >= var2) {
                  break;
               }

               if (this.delimiters.indexOf(this.string.charAt(var4), 0) == -1) {
                  var3 = true;
                  break;
               }

               ++var4;
            }
         }

         return var3;
      }
   }

   public Object nextElement() {
      return this.nextToken();
   }

   public String nextToken() {
      if (this.delimiters == null) {
         throw new NullPointerException();
      } else {
         int var1 = this.position;
         int var2 = this.string.length();
         if (var1 < var2) {
            while(var1 < var2 && this.delimiters.indexOf(this.string.charAt(var1), 0) >= 0) {
               ++var1;
            }

            this.position = var1;
            if (var1 < var2) {
               ++this.position;

               String var3;
               while(true) {
                  if (this.position >= var2) {
                     var3 = this.string.substring(var1);
                     break;
                  }

                  if (this.delimiters.indexOf(this.string.charAt(this.position), 0) >= 0) {
                     var3 = this.string.substring(var1, this.position);
                     break;
                  }

                  ++this.position;
               }

               return var3;
            }
         }

         throw new NoSuchElementException();
      }
   }
}
