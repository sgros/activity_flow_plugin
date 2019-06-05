package org.greenrobot.greendao.query;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;

class WhereCollector {
   private final AbstractDao dao;
   private final String tablePrefix;
   private final List whereConditions;

   WhereCollector(AbstractDao var1, String var2) {
      this.dao = var1;
      this.tablePrefix = var2;
      this.whereConditions = new ArrayList();
   }

   void add(WhereCondition var1, WhereCondition... var2) {
      this.checkCondition(var1);
      this.whereConditions.add(var1);
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         var1 = var2[var3];
         this.checkCondition(var1);
         this.whereConditions.add(var1);
      }

   }

   void addCondition(StringBuilder var1, List var2, WhereCondition var3) {
      this.checkCondition(var3);
      var3.appendTo(var1, this.tablePrefix);
      var3.appendValuesTo(var2);
   }

   void appendWhereClause(StringBuilder var1, String var2, List var3) {
      ListIterator var4 = this.whereConditions.listIterator();

      while(var4.hasNext()) {
         if (var4.hasPrevious()) {
            var1.append(" AND ");
         }

         WhereCondition var5 = (WhereCondition)var4.next();
         var5.appendTo(var1, var2);
         var5.appendValuesTo(var3);
      }

   }

   void checkCondition(WhereCondition var1) {
      if (var1 instanceof WhereCondition.PropertyCondition) {
         this.checkProperty(((WhereCondition.PropertyCondition)var1).property);
      }

   }

   void checkProperty(Property var1) {
      if (this.dao != null) {
         Property[] var2 = this.dao.getProperties();
         boolean var3 = false;
         int var4 = var2.length;
         int var5 = 0;

         boolean var6;
         while(true) {
            var6 = var3;
            if (var5 >= var4) {
               break;
            }

            if (var1 == var2[var5]) {
               var6 = true;
               break;
            }

            ++var5;
         }

         if (!var6) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Property '");
            var7.append(var1.name);
            var7.append("' is not part of ");
            var7.append(this.dao);
            throw new DaoException(var7.toString());
         }
      }

   }

   WhereCondition combineWhereConditions(String var1, WhereCondition var2, WhereCondition var3, WhereCondition... var4) {
      StringBuilder var5 = new StringBuilder("(");
      ArrayList var6 = new ArrayList();
      this.addCondition(var5, var6, var2);
      var5.append(var1);
      this.addCondition(var5, var6, var3);
      int var7 = 0;

      for(int var8 = var4.length; var7 < var8; ++var7) {
         var2 = var4[var7];
         var5.append(var1);
         this.addCondition(var5, var6, var2);
      }

      var5.append(')');
      return new WhereCondition.StringCondition(var5.toString(), var6.toArray());
   }

   boolean isEmpty() {
      return this.whereConditions.isEmpty();
   }
}
