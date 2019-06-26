package org.aspectj.runtime.reflect;

import java.util.Hashtable;
import java.util.StringTokenizer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;

public final class Factory {
   private static Object[] NO_ARGS;
   // $FF: synthetic field
   static Class class$java$lang$ClassNotFoundException;
   static Hashtable prims = new Hashtable();
   int count;
   String filename;
   Class lexicalClass;
   ClassLoader lookupClassLoader;

   static {
      prims.put("void", Void.TYPE);
      prims.put("boolean", Boolean.TYPE);
      prims.put("byte", Byte.TYPE);
      prims.put("char", Character.TYPE);
      prims.put("short", Short.TYPE);
      prims.put("int", Integer.TYPE);
      prims.put("long", Long.TYPE);
      prims.put("float", Float.TYPE);
      prims.put("double", Double.TYPE);
      NO_ARGS = new Object[0];
   }

   public Factory(String var1, Class var2) {
      this.filename = var1;
      this.lexicalClass = var2;
      this.count = 0;
      this.lookupClassLoader = var2.getClassLoader();
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         Class var2 = Class.forName(var0);
         return var2;
      } catch (ClassNotFoundException var1) {
         throw new NoClassDefFoundError(var1.getMessage());
      }
   }

   static Class makeClass(String var0, ClassLoader var1) {
      if (var0.equals("*")) {
         return null;
      } else {
         Class var2 = (Class)prims.get(var0);
         if (var2 != null) {
            return var2;
         } else {
            boolean var10001;
            Class var5;
            if (var1 == null) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var3) {
                  var10001 = false;
               }
            } else {
               try {
                  var5 = Class.forName(var0, false, var1);
                  return var5;
               } catch (ClassNotFoundException var4) {
                  var10001 = false;
               }
            }

            Class var6 = class$java$lang$ClassNotFoundException;
            var5 = var6;
            if (var6 == null) {
               var5 = class$("java.lang.ClassNotFoundException");
               class$java$lang$ClassNotFoundException = var5;
            }

            return var5;
         }
      }
   }

   public static JoinPoint makeJP(JoinPoint.StaticPart var0, Object var1, Object var2) {
      return new JoinPointImpl(var0, var1, var2, NO_ARGS);
   }

   public static JoinPoint makeJP(JoinPoint.StaticPart var0, Object var1, Object var2, Object var3) {
      return new JoinPointImpl(var0, var1, var2, new Object[]{var3});
   }

   public MethodSignature makeMethodSig(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      int var8 = Integer.parseInt(var1, 16);
      Class var12 = makeClass(var3, this.lookupClassLoader);
      StringTokenizer var14 = new StringTokenizer(var4, ":");
      int var9 = var14.countTokens();
      Class[] var13 = new Class[var9];
      byte var10 = 0;

      int var11;
      for(var11 = 0; var11 < var9; ++var11) {
         var13[var11] = makeClass(var14.nextToken(), this.lookupClassLoader);
      }

      StringTokenizer var16 = new StringTokenizer(var5, ":");
      var9 = var16.countTokens();
      String[] var15 = new String[var9];

      for(var11 = 0; var11 < var9; ++var11) {
         var15[var11] = var16.nextToken();
      }

      StringTokenizer var18 = new StringTokenizer(var6, ":");
      var9 = var18.countTokens();
      Class[] var17 = new Class[var9];

      for(var11 = var10; var11 < var9; ++var11) {
         var17[var11] = makeClass(var18.nextToken(), this.lookupClassLoader);
      }

      return new MethodSignatureImpl(var8, var2, var12, var13, var15, var17, makeClass(var7, this.lookupClassLoader));
   }

   public JoinPoint.StaticPart makeSJP(String var1, Signature var2, int var3) {
      int var4 = this.count++;
      return new JoinPointImpl.StaticPartImpl(var4, var1, var2, this.makeSourceLoc(var3, -1));
   }

   public SourceLocation makeSourceLoc(int var1, int var2) {
      return new SourceLocationImpl(this.lexicalClass, this.filename, var1);
   }
}
