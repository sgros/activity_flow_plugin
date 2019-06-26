package org.telegram.PhoneFormat;

import java.util.ArrayList;
import java.util.Iterator;

public class CallingCodeInfo {
   public String callingCode = "";
   public ArrayList countries = new ArrayList();
   public ArrayList intlPrefixes = new ArrayList();
   public ArrayList ruleSets = new ArrayList();
   public ArrayList trunkPrefixes = new ArrayList();

   String format(String var1) {
      boolean var2 = var1.startsWith(this.callingCode);
      String var3 = null;
      String var4;
      String var5;
      if (var2) {
         var4 = this.callingCode;
         var5 = var1.substring(var4.length());
      } else {
         var4 = this.matchingTrunkCode(var1);
         if (var4 != null) {
            var5 = var1.substring(var4.length());
            var3 = var4;
            var4 = null;
         } else {
            var5 = var1;
            var4 = null;
         }
      }

      Iterator var6 = this.ruleSets.iterator();

      String var7;
      do {
         if (!var6.hasNext()) {
            Iterator var9 = this.ruleSets.iterator();

            String var8;
            do {
               if (!var9.hasNext()) {
                  var3 = var1;
                  if (var4 != null) {
                     var3 = var1;
                     if (var5.length() != 0) {
                        var3 = String.format("%s %s", var4, var5);
                     }
                  }

                  return var3;
               }

               var8 = ((RuleSet)var9.next()).format(var5, var4, var3, false);
            } while(var8 == null);

            return var8;
         }

         var7 = ((RuleSet)var6.next()).format(var5, var4, var3, true);
      } while(var7 == null);

      return var7;
   }

   boolean isValidPhoneNumber(String var1) {
      boolean var2 = var1.startsWith(this.callingCode);
      String var3 = null;
      String var4;
      String var5;
      if (var2) {
         var4 = this.callingCode;
         var5 = var1.substring(var4.length());
         var1 = var4;
      } else {
         var4 = this.matchingTrunkCode(var1);
         if (var4 != null) {
            var5 = var1.substring(var4.length());
            var3 = var4;
            var1 = null;
         } else {
            var4 = null;
            var5 = var1;
            var1 = var4;
         }
      }

      Iterator var6 = this.ruleSets.iterator();

      do {
         if (!var6.hasNext()) {
            var6 = this.ruleSets.iterator();

            do {
               if (!var6.hasNext()) {
                  return false;
               }
            } while(!((RuleSet)var6.next()).isValid(var5, var1, var3, false));

            return true;
         }
      } while(!((RuleSet)var6.next()).isValid(var5, var1, var3, true));

      return true;
   }

   String matchingAccessCode(String var1) {
      Iterator var2 = this.intlPrefixes.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (String)var2.next();
      } while(!var1.startsWith(var3));

      return var3;
   }

   String matchingTrunkCode(String var1) {
      Iterator var2 = this.trunkPrefixes.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (String)var2.next();
      } while(!var1.startsWith(var3));

      return var3;
   }
}
