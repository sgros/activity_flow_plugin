package org.telegram.PhoneFormat;

public class PhoneRule {
   public int byte8;
   public int flag12;
   public int flag13;
   public String format;
   public boolean hasIntlPrefix;
   public boolean hasTrunkPrefix;
   public int maxLen;
   public int maxVal;
   public int minVal;
   public int otherFlag;
   public int prefixLen;

   String format(String var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder(20);
      int var5 = 0;
      boolean var6 = false;
      int var7 = 0;
      boolean var8 = false;

      boolean var14;
      for(boolean var9 = false; var5 < this.format.length(); var9 = var14) {
         char var10 = this.format.charAt(var5);
         boolean var11;
         boolean var12;
         int var13;
         if (var10 != '#') {
            label84: {
               if (var10 != '(') {
                  if (var10 == 'c') {
                     if (var2 != null) {
                        var4.append(var2);
                     }

                     var12 = true;
                     var13 = var7;
                     var11 = var8;
                     var14 = var9;
                     break label84;
                  }

                  if (var10 == 'n') {
                     if (var3 != null) {
                        var4.append(var3);
                     }

                     var11 = true;
                     var12 = var6;
                     var13 = var7;
                     var14 = var9;
                     break label84;
                  }
               } else if (var7 < var1.length()) {
                  var9 = true;
               }

               if (var10 == ' ' && var5 > 0) {
                  String var15 = this.format;
                  int var16 = var5 - 1;
                  if (var15.charAt(var16) == 'n') {
                     var12 = var6;
                     var13 = var7;
                     var11 = var8;
                     var14 = var9;
                     if (var3 == null) {
                        break label84;
                     }
                  }

                  if (this.format.charAt(var16) == 'c') {
                     var12 = var6;
                     var13 = var7;
                     var11 = var8;
                     var14 = var9;
                     if (var2 == null) {
                        break label84;
                     }
                  }
               }

               if (var7 >= var1.length()) {
                  var12 = var6;
                  var13 = var7;
                  var11 = var8;
                  var14 = var9;
                  if (!var9) {
                     break label84;
                  }

                  var12 = var6;
                  var13 = var7;
                  var11 = var8;
                  var14 = var9;
                  if (var10 != ')') {
                     break label84;
                  }
               }

               var4.append(this.format.substring(var5, var5 + 1));
               var12 = var6;
               var13 = var7;
               var11 = var8;
               var14 = var9;
               if (var10 == ')') {
                  var14 = false;
                  var12 = var6;
                  var13 = var7;
                  var11 = var8;
               }
            }
         } else if (var7 < var1.length()) {
            var13 = var7 + 1;
            var4.append(var1.substring(var7, var13));
            var12 = var6;
            var11 = var8;
            var14 = var9;
         } else {
            var12 = var6;
            var13 = var7;
            var11 = var8;
            var14 = var9;
            if (var9) {
               var4.append(" ");
               var14 = var9;
               var11 = var8;
               var13 = var7;
               var12 = var6;
            }
         }

         ++var5;
         var6 = var12;
         var7 = var13;
         var8 = var11;
      }

      if (var2 != null && !var6) {
         var4.insert(0, String.format("%s ", var2));
      } else if (var3 != null && !var8) {
         var4.insert(0, var3);
      }

      return var4.toString();
   }

   boolean hasIntlPrefix() {
      boolean var1;
      if ((this.flag12 & 2) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean hasTrunkPrefix() {
      int var1 = this.flag12;
      boolean var2 = true;
      if ((var1 & 1) == 0) {
         var2 = false;
      }

      return var2;
   }
}
