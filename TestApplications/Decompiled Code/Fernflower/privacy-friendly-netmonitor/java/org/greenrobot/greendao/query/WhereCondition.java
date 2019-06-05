package org.greenrobot.greendao.query;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;

public interface WhereCondition {
   void appendTo(StringBuilder var1, String var2);

   void appendValuesTo(List var1);

   public abstract static class AbstractCondition implements WhereCondition {
      protected final boolean hasSingleValue;
      protected final Object value;
      protected final Object[] values;

      public AbstractCondition() {
         this.hasSingleValue = false;
         this.value = null;
         this.values = null;
      }

      public AbstractCondition(Object var1) {
         this.value = var1;
         this.hasSingleValue = true;
         this.values = null;
      }

      public AbstractCondition(Object[] var1) {
         this.value = null;
         this.hasSingleValue = false;
         this.values = var1;
      }

      public void appendValuesTo(List var1) {
         if (this.hasSingleValue) {
            var1.add(this.value);
         } else if (this.values != null) {
            Object[] var2 = this.values;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               var1.add(var2[var4]);
            }
         }

      }
   }

   public static class PropertyCondition extends WhereCondition.AbstractCondition {
      public final String op;
      public final Property property;

      public PropertyCondition(Property var1, String var2) {
         this.property = var1;
         this.op = var2;
      }

      public PropertyCondition(Property var1, String var2, Object var3) {
         super(checkValueForType(var1, var3));
         this.property = var1;
         this.op = var2;
      }

      public PropertyCondition(Property var1, String var2, Object[] var3) {
         super(checkValuesForType(var1, var3));
         this.property = var1;
         this.op = var2;
      }

      private static Object checkValueForType(Property var0, Object var1) {
         if (var1 != null && var1.getClass().isArray()) {
            throw new DaoException("Illegal value: found array, but simple object required");
         } else {
            StringBuilder var3;
            if (var0.type == Date.class) {
               if (var1 instanceof Date) {
                  return ((Date)var1).getTime();
               } else if (var1 instanceof Long) {
                  return var1;
               } else {
                  var3 = new StringBuilder();
                  var3.append("Illegal date value: expected java.util.Date or Long for value ");
                  var3.append(var1);
                  throw new DaoException(var3.toString());
               }
            } else {
               if (var0.type == Boolean.TYPE || var0.type == Boolean.class) {
                  if (var1 instanceof Boolean) {
                     return Integer.valueOf((Boolean)var1);
                  }

                  if (var1 instanceof Number) {
                     int var2 = ((Number)var1).intValue();
                     if (var2 != 0 && var2 != 1) {
                        var3 = new StringBuilder();
                        var3.append("Illegal boolean value: numbers must be 0 or 1, but was ");
                        var3.append(var1);
                        throw new DaoException(var3.toString());
                     }
                  } else if (var1 instanceof String) {
                     String var4 = (String)var1;
                     if ("TRUE".equalsIgnoreCase(var4)) {
                        return 1;
                     }

                     if ("FALSE".equalsIgnoreCase(var4)) {
                        return 0;
                     }

                     var3 = new StringBuilder();
                     var3.append("Illegal boolean value: Strings must be \"TRUE\" or \"FALSE\" (case insensitive), but was ");
                     var3.append(var1);
                     throw new DaoException(var3.toString());
                  }
               }

               return var1;
            }
         }
      }

      private static Object[] checkValuesForType(Property var0, Object[] var1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = checkValueForType(var0, var1[var2]);
         }

         return var1;
      }

      public void appendTo(StringBuilder var1, String var2) {
         SqlUtils.appendProperty(var1, var2, this.property).append(this.op);
      }
   }

   public static class StringCondition extends WhereCondition.AbstractCondition {
      protected final String string;

      public StringCondition(String var1) {
         this.string = var1;
      }

      public StringCondition(String var1, Object var2) {
         super(var2);
         this.string = var1;
      }

      public StringCondition(String var1, Object... var2) {
         super(var2);
         this.string = var1;
      }

      public void appendTo(StringBuilder var1, String var2) {
         var1.append(this.string);
      }
   }
}
