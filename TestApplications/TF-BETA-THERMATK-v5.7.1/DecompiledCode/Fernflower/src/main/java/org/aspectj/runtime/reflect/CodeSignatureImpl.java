package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.CodeSignature;

abstract class CodeSignatureImpl extends MemberSignatureImpl implements CodeSignature {
   Class[] exceptionTypes;
   String[] parameterNames;
   Class[] parameterTypes;

   CodeSignatureImpl(int var1, String var2, Class var3, Class[] var4, String[] var5, Class[] var6) {
      super(var1, var2, var3);
      this.parameterTypes = var4;
      this.parameterNames = var5;
      this.exceptionTypes = var6;
   }

   public Class[] getExceptionTypes() {
      if (this.exceptionTypes == null) {
         this.exceptionTypes = this.extractTypes(5);
      }

      return this.exceptionTypes;
   }

   public Class[] getParameterTypes() {
      if (this.parameterTypes == null) {
         this.parameterTypes = this.extractTypes(3);
      }

      return this.parameterTypes;
   }
}
