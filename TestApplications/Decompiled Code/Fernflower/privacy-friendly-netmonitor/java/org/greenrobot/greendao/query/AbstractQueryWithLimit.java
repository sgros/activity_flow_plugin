package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryWithLimit extends AbstractQuery {
   protected final int limitPosition;
   protected final int offsetPosition;

   protected AbstractQueryWithLimit(AbstractDao var1, String var2, String[] var3, int var4, int var5) {
      super(var1, var2, var3);
      this.limitPosition = var4;
      this.offsetPosition = var5;
   }

   public void setLimit(int var1) {
      this.checkThread();
      if (this.limitPosition == -1) {
         throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here");
      } else {
         this.parameters[this.limitPosition] = Integer.toString(var1);
      }
   }

   public void setOffset(int var1) {
      this.checkThread();
      if (this.offsetPosition == -1) {
         throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here");
      } else {
         this.parameters[this.offsetPosition] = Integer.toString(var1);
      }
   }

   public AbstractQueryWithLimit setParameter(int var1, Object var2) {
      if (var1 < 0 || var1 != this.limitPosition && var1 != this.offsetPosition) {
         return (AbstractQueryWithLimit)super.setParameter(var1, var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Illegal parameter index: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }
}
