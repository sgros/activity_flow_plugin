package org.aspectj.runtime.reflect;

import java.lang.reflect.Modifier;

class StringMaker {
   static StringMaker longStringMaker;
   static StringMaker middleStringMaker;
   static StringMaker shortStringMaker = new StringMaker();
   int cacheOffset;
   boolean includeArgs = true;
   boolean includeEnclosingPoint = true;
   boolean includeJoinPointTypeName = true;
   boolean includeModifiers = false;
   boolean includeThrows = false;
   boolean shortKindName = true;
   boolean shortPrimaryTypeNames = false;
   boolean shortTypeNames = true;

   static {
      StringMaker var0 = shortStringMaker;
      var0.shortTypeNames = true;
      var0.includeArgs = false;
      var0.includeThrows = false;
      var0.includeModifiers = false;
      var0.shortPrimaryTypeNames = true;
      var0.includeJoinPointTypeName = false;
      var0.includeEnclosingPoint = false;
      var0.cacheOffset = 0;
      middleStringMaker = new StringMaker();
      var0 = middleStringMaker;
      var0.shortTypeNames = true;
      var0.includeArgs = true;
      var0.includeThrows = false;
      var0.includeModifiers = false;
      var0.shortPrimaryTypeNames = false;
      shortStringMaker.cacheOffset = 1;
      longStringMaker = new StringMaker();
      var0 = longStringMaker;
      var0.shortTypeNames = false;
      var0.includeArgs = true;
      var0.includeThrows = false;
      var0.includeModifiers = true;
      var0.shortPrimaryTypeNames = false;
      var0.shortKindName = false;
      var0.cacheOffset = 2;
   }

   public void addSignature(StringBuffer var1, Class[] var2) {
      if (var2 != null) {
         if (!this.includeArgs) {
            if (var2.length == 0) {
               var1.append("()");
            } else {
               var1.append("(..)");
            }
         } else {
            var1.append("(");
            this.addTypeNames(var1, var2);
            var1.append(")");
         }
      }
   }

   public void addThrows(StringBuffer var1, Class[] var2) {
      if (this.includeThrows && var2 != null && var2.length != 0) {
         var1.append(" throws ");
         this.addTypeNames(var1, var2);
      }

   }

   public void addTypeNames(StringBuffer var1, Class[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var3 > 0) {
            var1.append(", ");
         }

         var1.append(this.makeTypeName(var2[var3]));
      }

   }

   String makeKindName(String var1) {
      int var2 = var1.lastIndexOf(45);
      return var2 == -1 ? var1 : var1.substring(var2 + 1);
   }

   String makeModifiersString(int var1) {
      if (!this.includeModifiers) {
         return "";
      } else {
         String var2 = Modifier.toString(var1);
         if (var2.length() == 0) {
            return "";
         } else {
            StringBuffer var3 = new StringBuffer();
            var3.append(var2);
            var3.append(" ");
            return var3.toString();
         }
      }
   }

   public String makePrimaryTypeName(Class var1, String var2) {
      return this.makeTypeName(var1, var2, this.shortPrimaryTypeNames);
   }

   public String makeTypeName(Class var1) {
      return this.makeTypeName(var1, var1.getName(), this.shortTypeNames);
   }

   String makeTypeName(Class var1, String var2, boolean var3) {
      if (var1 == null) {
         return "ANONYMOUS";
      } else if (var1.isArray()) {
         Class var5 = var1.getComponentType();
         StringBuffer var4 = new StringBuffer();
         var4.append(this.makeTypeName(var5, var5.getName(), var3));
         var4.append("[]");
         return var4.toString();
      } else {
         return var3 ? this.stripPackageName(var2).replace('$', '.') : var2.replace('$', '.');
      }
   }

   String stripPackageName(String var1) {
      int var2 = var1.lastIndexOf(46);
      return var2 == -1 ? var1 : var1.substring(var2 + 1);
   }
}
