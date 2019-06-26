package org.telegram.PhoneFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleSet {
   public static Pattern pattern = Pattern.compile("[0-9]+");
   public boolean hasRuleWithIntlPrefix;
   public boolean hasRuleWithTrunkPrefix;
   public int matchLen;
   public ArrayList rules = new ArrayList();

   String format(String var1, String var2, String var3, boolean var4) {
      int var5 = var1.length();
      int var6 = this.matchLen;
      if (var5 >= var6) {
         var5 = 0;
         String var7 = var1.substring(0, var6);
         Matcher var9 = pattern.matcher(var7);
         if (var9.find()) {
            var5 = Integer.parseInt(var9.group(0));
         }

         Iterator var10 = this.rules.iterator();

         while(true) {
            PhoneRule var8;
            if (!var10.hasNext()) {
               if (var4) {
                  break;
               }

               if (var2 != null) {
                  var10 = this.rules.iterator();

                  do {
                     if (!var10.hasNext()) {
                        return null;
                     }

                     var8 = (PhoneRule)var10.next();
                  } while(var5 < var8.minVal || var5 > var8.maxVal || var1.length() > var8.maxLen || var3 != null && (var8.flag12 & 1) == 0);

                  return var8.format(var1, var2, var3);
               }

               if (var3 == null) {
                  break;
               }

               Iterator var12 = this.rules.iterator();

               PhoneRule var11;
               do {
                  if (!var12.hasNext()) {
                     return null;
                  }

                  var11 = (PhoneRule)var12.next();
               } while(var5 < var11.minVal || var5 > var11.maxVal || var1.length() > var11.maxLen || var2 != null && (var11.flag12 & 2) == 0);

               return var11.format(var1, var2, var3);
            }

            var8 = (PhoneRule)var10.next();
            if (var5 >= var8.minVal && var5 <= var8.maxVal && var1.length() <= var8.maxLen) {
               if (var4) {
                  if ((var8.flag12 & 3) == 0 && var3 == null && var2 == null || var3 != null && (var8.flag12 & 1) != 0 || var2 != null && (var8.flag12 & 2) != 0) {
                     return var8.format(var1, var2, var3);
                  }
               } else if (var3 == null && var2 == null || var3 != null && (var8.flag12 & 1) != 0 || var2 != null && (var8.flag12 & 2) != 0) {
                  return var8.format(var1, var2, var3);
               }
            }
         }
      }

      return null;
   }

   boolean isValid(String var1, String var2, String var3, boolean var4) {
      int var5 = var1.length();
      int var6 = this.matchLen;
      if (var5 >= var6) {
         String var7 = var1.substring(0, var6);
         Matcher var11 = pattern.matcher(var7);
         if (var11.find()) {
            var6 = Integer.parseInt(var11.group(0));
         } else {
            var6 = 0;
         }

         Iterator var8 = this.rules.iterator();

         while(true) {
            if (!var8.hasNext()) {
               if (var4) {
                  break;
               }

               Iterator var13;
               if (var2 != null && !this.hasRuleWithIntlPrefix) {
                  var13 = this.rules.iterator();

                  PhoneRule var9;
                  do {
                     if (!var13.hasNext()) {
                        return false;
                     }

                     var9 = (PhoneRule)var13.next();
                  } while(var6 < var9.minVal || var6 > var9.maxVal || var1.length() != var9.maxLen || var3 != null && (var9.flag12 & 1) == 0);

                  return true;
               }

               if (var3 == null || this.hasRuleWithTrunkPrefix) {
                  break;
               }

               var13 = this.rules.iterator();

               PhoneRule var10;
               do {
                  if (!var13.hasNext()) {
                     return false;
                  }

                  var10 = (PhoneRule)var13.next();
               } while(var6 < var10.minVal || var6 > var10.maxVal || var1.length() != var10.maxLen || var2 != null && (var10.flag12 & 2) == 0);

               return true;
            }

            PhoneRule var12 = (PhoneRule)var8.next();
            if (var6 >= var12.minVal && var6 <= var12.maxVal && var1.length() == var12.maxLen) {
               if (var4) {
                  if ((var12.flag12 & 3) == 0 && var3 == null && var2 == null || var3 != null && (var12.flag12 & 1) != 0 || var2 != null && (var12.flag12 & 2) != 0) {
                     return true;
                  }
               } else if (var3 == null && var2 == null || var3 != null && (var12.flag12 & 1) != 0 || var2 != null && (var12.flag12 & 2) != 0) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
