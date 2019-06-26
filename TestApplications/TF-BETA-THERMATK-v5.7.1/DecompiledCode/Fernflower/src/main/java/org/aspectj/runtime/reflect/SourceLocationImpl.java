package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.SourceLocation;

class SourceLocationImpl implements SourceLocation {
   String fileName;
   int line;
   Class withinType;

   SourceLocationImpl(Class var1, String var2, int var3) {
      this.withinType = var1;
      this.fileName = var2;
      this.line = var3;
   }

   public String getFileName() {
      return this.fileName;
   }

   public int getLine() {
      return this.line;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getFileName());
      var1.append(":");
      var1.append(this.getLine());
      return var1.toString();
   }
}
