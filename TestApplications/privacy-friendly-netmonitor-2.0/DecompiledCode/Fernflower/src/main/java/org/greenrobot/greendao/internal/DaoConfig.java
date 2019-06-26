package org.greenrobot.greendao.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.identityscope.IdentityScopeObject;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public final class DaoConfig implements Cloneable {
   public final String[] allColumns;
   public final Database db;
   private IdentityScope identityScope;
   public final boolean keyIsNumeric;
   public final String[] nonPkColumns;
   public final String[] pkColumns;
   public final Property pkProperty;
   public final Property[] properties;
   public final TableStatements statements;
   public final String tablename;

   public DaoConfig(Database var1, Class var2) {
      this.db = var1;

      Exception var10000;
      label159: {
         Field var3;
         boolean var10001;
         try {
            var3 = var2.getField("TABLENAME");
         } catch (Exception var28) {
            var10000 = var28;
            var10001 = false;
            break label159;
         }

         Property var4 = null;

         Property[] var5;
         ArrayList var6;
         ArrayList var7;
         try {
            this.tablename = (String)var3.get((Object)null);
            var5 = reflectProperties(var2);
            this.properties = var5;
            this.allColumns = new String[var5.length];
            var6 = new ArrayList();
            var7 = new ArrayList();
         } catch (Exception var27) {
            var10000 = var27;
            var10001 = false;
            break label159;
         }

         Property var31 = null;
         int var8 = 0;

         while(true) {
            try {
               if (var8 >= var5.length) {
                  break;
               }
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label159;
            }

            Property var33 = var5[var8];

            label143: {
               String var9;
               label142: {
                  try {
                     var9 = var33.columnName;
                     this.allColumns[var8] = var9;
                     if (!var33.primaryKey) {
                        break label142;
                     }

                     var6.add(var9);
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label159;
                  }

                  var31 = var33;
                  break label143;
               }

               try {
                  var7.add(var9);
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label159;
               }
            }

            ++var8;
         }

         try {
            this.nonPkColumns = (String[])var7.toArray(new String[var7.size()]);
            this.pkColumns = (String[])var6.toArray(new String[var6.size()]);
            var8 = this.pkColumns.length;
         } catch (Exception var23) {
            var10000 = var23;
            var10001 = false;
            break label159;
         }

         boolean var10 = true;
         if (var8 == 1) {
            var4 = var31;
         }

         label125: {
            label161: {
               Class var29;
               try {
                  this.pkProperty = var4;
                  TableStatements var32 = new TableStatements(var1, this.tablename, this.allColumns, this.pkColumns);
                  this.statements = var32;
                  if (this.pkProperty == null) {
                     break label161;
                  }

                  var29 = this.pkProperty.type;
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label159;
               }

               boolean var11 = var10;

               label162: {
                  try {
                     if (var29.equals(Long.TYPE)) {
                        break label162;
                     }
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label159;
                  }

                  var11 = var10;

                  try {
                     if (var29.equals(Long.class)) {
                        break label162;
                     }
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label159;
                  }

                  var11 = var10;

                  try {
                     if (var29.equals(Integer.TYPE)) {
                        break label162;
                     }
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label159;
                  }

                  var11 = var10;

                  try {
                     if (var29.equals(Integer.class)) {
                        break label162;
                     }
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label159;
                  }

                  var11 = var10;

                  try {
                     if (var29.equals(Short.TYPE)) {
                        break label162;
                     }
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label159;
                  }

                  var11 = var10;

                  try {
                     if (var29.equals(Short.class)) {
                        break label162;
                     }
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label159;
                  }

                  var11 = var10;

                  label114: {
                     try {
                        if (var29.equals(Byte.TYPE)) {
                           break label162;
                        }

                        if (var29.equals(Byte.class)) {
                           break label114;
                        }
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label159;
                     }

                     var11 = false;
                     break label162;
                  }

                  var11 = var10;
               }

               try {
                  this.keyIsNumeric = var11;
                  break label125;
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label159;
               }
            }

            try {
               this.keyIsNumeric = false;
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label159;
            }
         }

         try {
            return;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      Exception var30 = var10000;
      throw new DaoException("Could not init DAOConfig", var30);
   }

   public DaoConfig(DaoConfig var1) {
      this.db = var1.db;
      this.tablename = var1.tablename;
      this.properties = var1.properties;
      this.allColumns = var1.allColumns;
      this.pkColumns = var1.pkColumns;
      this.nonPkColumns = var1.nonPkColumns;
      this.pkProperty = var1.pkProperty;
      this.statements = var1.statements;
      this.keyIsNumeric = var1.keyIsNumeric;
   }

   private static Property[] reflectProperties(Class var0) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.getName());
      var1.append("$Properties");
      Field[] var7 = Class.forName(var1.toString()).getDeclaredFields();
      ArrayList var5 = new ArrayList();
      int var2 = 0;

      for(int var3 = var7.length; var2 < var3; ++var2) {
         Field var4 = var7[var2];
         if ((var4.getModifiers() & 9) == 9) {
            Object var9 = var4.get((Object)null);
            if (var9 instanceof Property) {
               var5.add((Property)var9);
            }
         }
      }

      Property[] var8 = new Property[var5.size()];

      Property var10;
      for(Iterator var6 = var5.iterator(); var6.hasNext(); var8[var10.ordinal] = var10) {
         var10 = (Property)var6.next();
         if (var8[var10.ordinal] != null) {
            throw new DaoException("Duplicate property ordinals");
         }
      }

      return var8;
   }

   public void clearIdentityScope() {
      IdentityScope var1 = this.identityScope;
      if (var1 != null) {
         var1.clear();
      }

   }

   public DaoConfig clone() {
      return new DaoConfig(this);
   }

   public IdentityScope getIdentityScope() {
      return this.identityScope;
   }

   public void initIdentityScope(IdentityScopeType var1) {
      if (var1 == IdentityScopeType.None) {
         this.identityScope = null;
      } else {
         if (var1 != IdentityScopeType.Session) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Unsupported type: ");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }

         if (this.keyIsNumeric) {
            this.identityScope = new IdentityScopeLong();
         } else {
            this.identityScope = new IdentityScopeObject();
         }
      }

   }

   public void setIdentityScope(IdentityScope var1) {
      this.identityScope = var1;
   }
}
