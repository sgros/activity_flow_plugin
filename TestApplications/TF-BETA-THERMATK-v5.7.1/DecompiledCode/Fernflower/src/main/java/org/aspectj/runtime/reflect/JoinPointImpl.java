package org.aspectj.runtime.reflect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

class JoinPointImpl implements ProceedingJoinPoint {
   Object _this;
   Object[] args;
   JoinPoint.StaticPart staticPart;
   Object target;

   public JoinPointImpl(JoinPoint.StaticPart var1, Object var2, Object var3, Object[] var4) {
      this.staticPart = var1;
      this._this = var2;
      this.target = var3;
      this.args = var4;
   }

   public Object getTarget() {
      return this.target;
   }

   public final String toString() {
      return this.staticPart.toString();
   }

   static class StaticPartImpl implements JoinPoint.StaticPart {
      private int id;
      String kind;
      Signature signature;
      SourceLocation sourceLocation;

      public StaticPartImpl(int var1, String var2, Signature var3, SourceLocation var4) {
         this.kind = var2;
         this.signature = var3;
         this.sourceLocation = var4;
         this.id = var1;
      }

      public String getKind() {
         return this.kind;
      }

      public Signature getSignature() {
         return this.signature;
      }

      public final String toString() {
         return this.toString(StringMaker.middleStringMaker);
      }

      String toString(StringMaker var1) {
         StringBuffer var2 = new StringBuffer();
         var2.append(var1.makeKindName(this.getKind()));
         var2.append("(");
         var2.append(((SignatureImpl)this.getSignature()).toString(var1));
         var2.append(")");
         return var2.toString();
      }
   }
}
