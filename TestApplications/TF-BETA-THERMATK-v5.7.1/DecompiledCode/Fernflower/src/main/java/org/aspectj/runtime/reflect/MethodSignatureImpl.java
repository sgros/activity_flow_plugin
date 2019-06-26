package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.MethodSignature;

class MethodSignatureImpl extends CodeSignatureImpl implements MethodSignature {
   Class returnType;

   MethodSignatureImpl(int var1, String var2, Class var3, Class[] var4, String[] var5, Class[] var6, Class var7) {
      super(var1, var2, var3, var4, var5, var6);
      this.returnType = var7;
   }

   protected String createToString(StringMaker var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1.makeModifiersString(this.getModifiers()));
      if (var1.includeArgs) {
         var2.append(var1.makeTypeName(this.getReturnType()));
      }

      if (var1.includeArgs) {
         var2.append(" ");
      }

      var2.append(var1.makePrimaryTypeName(this.getDeclaringType(), this.getDeclaringTypeName()));
      var2.append(".");
      var2.append(this.getName());
      var1.addSignature(var2, this.getParameterTypes());
      var1.addThrows(var2, this.getExceptionTypes());
      return var2.toString();
   }

   public Class getReturnType() {
      if (this.returnType == null) {
         this.returnType = this.extractType(6);
      }

      return this.returnType;
   }
}
