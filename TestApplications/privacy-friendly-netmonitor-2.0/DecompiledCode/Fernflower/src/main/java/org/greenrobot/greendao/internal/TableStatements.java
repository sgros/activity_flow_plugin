package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

public class TableStatements {
   private final String[] allColumns;
   private DatabaseStatement countStatement;
   private final Database db;
   private DatabaseStatement deleteStatement;
   private DatabaseStatement insertOrReplaceStatement;
   private DatabaseStatement insertStatement;
   private final String[] pkColumns;
   private volatile String selectAll;
   private volatile String selectByKey;
   private volatile String selectByRowId;
   private volatile String selectKeys;
   private final String tablename;
   private DatabaseStatement updateStatement;

   public TableStatements(Database var1, String var2, String[] var3, String[] var4) {
      this.db = var1;
      this.tablename = var2;
      this.allColumns = var3;
      this.pkColumns = var4;
   }

   public DatabaseStatement getCountStatement() {
      if (this.countStatement == null) {
         String var1 = SqlUtils.createSqlCount(this.tablename);
         this.countStatement = this.db.compileStatement(var1);
      }

      return this.countStatement;
   }

   public DatabaseStatement getDeleteStatement() {
      if (this.deleteStatement == null) {
         String var1 = SqlUtils.createSqlDelete(this.tablename, this.pkColumns);
         DatabaseStatement var14 = this.db.compileStatement(var1);
         synchronized(this){}

         label172: {
            Throwable var10000;
            boolean var10001;
            label165: {
               try {
                  if (this.deleteStatement == null) {
                     this.deleteStatement = var14;
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label165;
               }

               label162:
               try {
                  break label172;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label162;
               }
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.deleteStatement != var14) {
            var14.close();
         }
      }

      return this.deleteStatement;
   }

   public DatabaseStatement getInsertOrReplaceStatement() {
      if (this.insertOrReplaceStatement == null) {
         String var1 = SqlUtils.createSqlInsert("INSERT OR REPLACE INTO ", this.tablename, this.allColumns);
         DatabaseStatement var14 = this.db.compileStatement(var1);
         synchronized(this){}

         label172: {
            Throwable var10000;
            boolean var10001;
            label165: {
               try {
                  if (this.insertOrReplaceStatement == null) {
                     this.insertOrReplaceStatement = var14;
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label165;
               }

               label162:
               try {
                  break label172;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label162;
               }
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.insertOrReplaceStatement != var14) {
            var14.close();
         }
      }

      return this.insertOrReplaceStatement;
   }

   public DatabaseStatement getInsertStatement() {
      if (this.insertStatement == null) {
         String var1 = SqlUtils.createSqlInsert("INSERT INTO ", this.tablename, this.allColumns);
         DatabaseStatement var14 = this.db.compileStatement(var1);
         synchronized(this){}

         label172: {
            Throwable var10000;
            boolean var10001;
            label165: {
               try {
                  if (this.insertStatement == null) {
                     this.insertStatement = var14;
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label165;
               }

               label162:
               try {
                  break label172;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label162;
               }
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.insertStatement != var14) {
            var14.close();
         }
      }

      return this.insertStatement;
   }

   public String getSelectAll() {
      if (this.selectAll == null) {
         this.selectAll = SqlUtils.createSqlSelect(this.tablename, "T", this.allColumns, false);
      }

      return this.selectAll;
   }

   public String getSelectByKey() {
      if (this.selectByKey == null) {
         StringBuilder var1 = new StringBuilder(this.getSelectAll());
         var1.append("WHERE ");
         SqlUtils.appendColumnsEqValue(var1, "T", this.pkColumns);
         this.selectByKey = var1.toString();
      }

      return this.selectByKey;
   }

   public String getSelectByRowId() {
      if (this.selectByRowId == null) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.getSelectAll());
         var1.append("WHERE ROWID=?");
         this.selectByRowId = var1.toString();
      }

      return this.selectByRowId;
   }

   public String getSelectKeys() {
      if (this.selectKeys == null) {
         this.selectKeys = SqlUtils.createSqlSelect(this.tablename, "T", this.pkColumns, false);
      }

      return this.selectKeys;
   }

   public DatabaseStatement getUpdateStatement() {
      if (this.updateStatement == null) {
         String var1 = SqlUtils.createSqlUpdate(this.tablename, this.allColumns, this.pkColumns);
         DatabaseStatement var14 = this.db.compileStatement(var1);
         synchronized(this){}

         label172: {
            Throwable var10000;
            boolean var10001;
            label165: {
               try {
                  if (this.updateStatement == null) {
                     this.updateStatement = var14;
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label165;
               }

               label162:
               try {
                  break label172;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label162;
               }
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.updateStatement != var14) {
            var14.close();
         }
      }

      return this.updateStatement;
   }
}
