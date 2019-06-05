package android.arch.persistence.room.util;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.database.Cursor;
import android.os.Build.VERSION;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TableInfo {
   public final Map columns;
   public final Set foreignKeys;
   public final Set indices;
   public final String name;

   public TableInfo(String var1, Map var2, Set var3, Set var4) {
      this.name = var1;
      this.columns = Collections.unmodifiableMap(var2);
      this.foreignKeys = Collections.unmodifiableSet(var3);
      Set var5;
      if (var4 == null) {
         var5 = null;
      } else {
         var5 = Collections.unmodifiableSet(var4);
      }

      this.indices = var5;
   }

   public static TableInfo read(SupportSQLiteDatabase var0, String var1) {
      return new TableInfo(var1, readColumns(var0, var1), readForeignKeys(var0, var1), readIndices(var0, var1));
   }

   private static Map readColumns(SupportSQLiteDatabase var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("PRAGMA table_info(`");
      var2.append(var1);
      var2.append("`)");
      Cursor var23 = var0.query(var2.toString());
      HashMap var26 = new HashMap();

      label164: {
         Throwable var10000;
         label163: {
            int var3;
            int var4;
            int var5;
            int var6;
            boolean var10001;
            try {
               if (var23.getColumnCount() <= 0) {
                  break label164;
               }

               var3 = var23.getColumnIndex("name");
               var4 = var23.getColumnIndex("type");
               var5 = var23.getColumnIndex("notnull");
               var6 = var23.getColumnIndex("pk");
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label163;
            }

            while(true) {
               String var7;
               String var8;
               boolean var9;
               label158: {
                  label157: {
                     try {
                        if (!var23.moveToNext()) {
                           break label164;
                        }

                        var7 = var23.getString(var3);
                        var8 = var23.getString(var4);
                        if (var23.getInt(var5) != 0) {
                           break label157;
                        }
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break;
                     }

                     var9 = false;
                     break label158;
                  }

                  var9 = true;
               }

               try {
                  int var10 = var23.getInt(var6);
                  TableInfo.Column var25 = new TableInfo.Column(var7, var8, var9, var10);
                  var26.put(var7, var25);
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var24 = var10000;
         var23.close();
         throw var24;
      }

      var23.close();
      return var26;
   }

   private static List readForeignKeyFieldMappings(Cursor var0) {
      int var1 = var0.getColumnIndex("id");
      int var2 = var0.getColumnIndex("seq");
      int var3 = var0.getColumnIndex("from");
      int var4 = var0.getColumnIndex("to");
      int var5 = var0.getCount();
      ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < var5; ++var7) {
         var0.moveToPosition(var7);
         var6.add(new TableInfo.ForeignKeyWithSequence(var0.getInt(var1), var0.getInt(var2), var0.getString(var3), var0.getString(var4)));
      }

      Collections.sort(var6);
      return var6;
   }

   private static Set readForeignKeys(SupportSQLiteDatabase var0, String var1) {
      HashSet var2 = new HashSet();
      StringBuilder var3 = new StringBuilder();
      var3.append("PRAGMA foreign_key_list(`");
      var3.append(var1);
      var3.append("`)");
      Cursor var45 = var0.query(var3.toString());

      Throwable var10000;
      label341: {
         int var4;
         int var5;
         int var6;
         int var7;
         int var8;
         int var9;
         boolean var10001;
         List var46;
         try {
            var4 = var45.getColumnIndex("id");
            var5 = var45.getColumnIndex("seq");
            var6 = var45.getColumnIndex("table");
            var7 = var45.getColumnIndex("on_delete");
            var8 = var45.getColumnIndex("on_update");
            var46 = readForeignKeyFieldMappings(var45);
            var9 = var45.getCount();
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label341;
         }

         int var10 = 0;

         label333:
         while(true) {
            if (var10 >= var9) {
               var45.close();
               return var2;
            }

            label343: {
               try {
                  var45.moveToPosition(var10);
                  if (var45.getInt(var5) != 0) {
                     break label343;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break;
               }

               int var11;
               ArrayList var12;
               Iterator var13;
               ArrayList var48;
               try {
                  var11 = var45.getInt(var4);
                  var48 = new ArrayList();
                  var12 = new ArrayList();
                  var13 = var46.iterator();
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break;
               }

               label324:
               while(true) {
                  try {
                     TableInfo.ForeignKeyWithSequence var14;
                     do {
                        if (!var13.hasNext()) {
                           break label324;
                        }

                        var14 = (TableInfo.ForeignKeyWithSequence)var13.next();
                     } while(var14.mId != var11);

                     var48.add(var14.mFrom);
                     var12.add(var14.mTo);
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label333;
                  }
               }

               try {
                  TableInfo.ForeignKey var49 = new TableInfo.ForeignKey(var45.getString(var6), var45.getString(var7), var45.getString(var8), var48, var12);
                  var2.add(var49);
               } catch (Throwable var40) {
                  var10000 = var40;
                  var10001 = false;
                  break;
               }
            }

            ++var10;
         }
      }

      Throwable var47 = var10000;
      var45.close();
      throw var47;
   }

   private static TableInfo.Index readIndex(SupportSQLiteDatabase var0, String var1, boolean var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("PRAGMA index_xinfo(`");
      var3.append(var1);
      var3.append("`)");
      Cursor var38 = var0.query(var3.toString());

      Throwable var10000;
      label344: {
         int var4;
         int var5;
         int var6;
         boolean var10001;
         try {
            var4 = var38.getColumnIndex("seqno");
            var5 = var38.getColumnIndex("cid");
            var6 = var38.getColumnIndex("name");
         } catch (Throwable var37) {
            var10000 = var37;
            var10001 = false;
            break label344;
         }

         if (var4 == -1 || var5 == -1 || var6 == -1) {
            var38.close();
            return null;
         }

         TreeMap var7;
         try {
            var7 = new TreeMap();
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label344;
         }

         label328:
         while(true) {
            while(true) {
               try {
                  if (!var38.moveToNext()) {
                     break label328;
                  }

                  if (var38.getInt(var5) < 0) {
                     continue;
                  }
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label344;
               }

               try {
                  var7.put(var38.getInt(var4), var38.getString(var6));
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label344;
               }
            }
         }

         TableInfo.Index var40;
         try {
            ArrayList var41 = new ArrayList(var7.size());
            var41.addAll(var7.values());
            var40 = new TableInfo.Index(var1, var2, var41);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label344;
         }

         var38.close();
         return var40;
      }

      Throwable var39 = var10000;
      var38.close();
      throw var39;
   }

   private static Set readIndices(SupportSQLiteDatabase var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("PRAGMA index_list(`");
      var2.append(var1);
      var2.append("`)");
      Cursor var52 = var0.query(var2.toString());

      Throwable var10000;
      label505: {
         int var3;
         int var4;
         int var5;
         boolean var10001;
         try {
            var3 = var52.getColumnIndex("name");
            var4 = var52.getColumnIndex("origin");
            var5 = var52.getColumnIndex("unique");
         } catch (Throwable var50) {
            var10000 = var50;
            var10001 = false;
            break label505;
         }

         if (var3 == -1 || var4 == -1 || var5 == -1) {
            var52.close();
            return null;
         }

         HashSet var53;
         try {
            var53 = new HashSet();
         } catch (Throwable var49) {
            var10000 = var49;
            var10001 = false;
            break label505;
         }

         label486:
         while(true) {
            while(true) {
               try {
                  if (!var52.moveToNext()) {
                     break label486;
                  }

                  if (!"c".equals(var52.getString(var4))) {
                     continue;
                  }
               } catch (Throwable var48) {
                  var10000 = var48;
                  var10001 = false;
                  break label505;
               }

               String var6;
               int var7;
               try {
                  var6 = var52.getString(var3);
                  var7 = var52.getInt(var5);
               } catch (Throwable var47) {
                  var10000 = var47;
                  var10001 = false;
                  break label505;
               }

               boolean var8 = true;
               if (var7 != 1) {
                  var8 = false;
               }

               TableInfo.Index var54;
               try {
                  var54 = readIndex(var0, var6, var8);
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label505;
               }

               if (var54 == null) {
                  var52.close();
                  return null;
               }

               try {
                  var53.add(var54);
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label505;
               }
            }
         }

         var52.close();
         return var53;
      }

      Throwable var51 = var10000;
      var52.close();
      throw var51;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         TableInfo var2 = (TableInfo)var1;
         if (this.name != null) {
            if (!this.name.equals(var2.name)) {
               return false;
            }
         } else if (var2.name != null) {
            return false;
         }

         label46: {
            if (this.columns != null) {
               if (this.columns.equals(var2.columns)) {
                  break label46;
               }
            } else if (var2.columns == null) {
               break label46;
            }

            return false;
         }

         label39: {
            if (this.foreignKeys != null) {
               if (this.foreignKeys.equals(var2.foreignKeys)) {
                  break label39;
               }
            } else if (var2.foreignKeys == null) {
               break label39;
            }

            return false;
         }

         if (this.indices != null && var2.indices != null) {
            return this.indices.equals(var2.indices);
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.name;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = this.name.hashCode();
      } else {
         var3 = 0;
      }

      int var4;
      if (this.columns != null) {
         var4 = this.columns.hashCode();
      } else {
         var4 = 0;
      }

      if (this.foreignKeys != null) {
         var2 = this.foreignKeys.hashCode();
      }

      return (var3 * 31 + var4) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("TableInfo{name='");
      var1.append(this.name);
      var1.append('\'');
      var1.append(", columns=");
      var1.append(this.columns);
      var1.append(", foreignKeys=");
      var1.append(this.foreignKeys);
      var1.append(", indices=");
      var1.append(this.indices);
      var1.append('}');
      return var1.toString();
   }

   public static class Column {
      public final int affinity;
      public final String name;
      public final boolean notNull;
      public final int primaryKeyPosition;
      public final String type;

      public Column(String var1, String var2, boolean var3, int var4) {
         this.name = var1;
         this.type = var2;
         this.notNull = var3;
         this.primaryKeyPosition = var4;
         this.affinity = findAffinity(var2);
      }

      private static int findAffinity(String var0) {
         if (var0 == null) {
            return 5;
         } else {
            var0 = var0.toUpperCase(Locale.US);
            if (var0.contains("INT")) {
               return 3;
            } else if (!var0.contains("CHAR") && !var0.contains("CLOB") && !var0.contains("TEXT")) {
               if (var0.contains("BLOB")) {
                  return 5;
               } else {
                  return !var0.contains("REAL") && !var0.contains("FLOA") && !var0.contains("DOUB") ? 1 : 4;
               }
            } else {
               return 2;
            }
         }
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            TableInfo.Column var3 = (TableInfo.Column)var1;
            if (VERSION.SDK_INT >= 20) {
               if (this.primaryKeyPosition != var3.primaryKeyPosition) {
                  return false;
               }
            } else if (this.isPrimaryKey() != var3.isPrimaryKey()) {
               return false;
            }

            if (!this.name.equals(var3.name)) {
               return false;
            } else if (this.notNull != var3.notNull) {
               return false;
            } else {
               if (this.affinity != var3.affinity) {
                  var2 = false;
               }

               return var2;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.name.hashCode();
         int var2 = this.affinity;
         short var3;
         if (this.notNull) {
            var3 = 1231;
         } else {
            var3 = 1237;
         }

         return ((var1 * 31 + var2) * 31 + var3) * 31 + this.primaryKeyPosition;
      }

      public boolean isPrimaryKey() {
         boolean var1;
         if (this.primaryKeyPosition > 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Column{name='");
         var1.append(this.name);
         var1.append('\'');
         var1.append(", type='");
         var1.append(this.type);
         var1.append('\'');
         var1.append(", affinity='");
         var1.append(this.affinity);
         var1.append('\'');
         var1.append(", notNull=");
         var1.append(this.notNull);
         var1.append(", primaryKeyPosition=");
         var1.append(this.primaryKeyPosition);
         var1.append('}');
         return var1.toString();
      }
   }

   public static class ForeignKey {
      public final List columnNames;
      public final String onDelete;
      public final String onUpdate;
      public final List referenceColumnNames;
      public final String referenceTable;

      public ForeignKey(String var1, String var2, String var3, List var4, List var5) {
         this.referenceTable = var1;
         this.onDelete = var2;
         this.onUpdate = var3;
         this.columnNames = Collections.unmodifiableList(var4);
         this.referenceColumnNames = Collections.unmodifiableList(var5);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            TableInfo.ForeignKey var2 = (TableInfo.ForeignKey)var1;
            if (!this.referenceTable.equals(var2.referenceTable)) {
               return false;
            } else if (!this.onDelete.equals(var2.onDelete)) {
               return false;
            } else if (!this.onUpdate.equals(var2.onUpdate)) {
               return false;
            } else {
               return !this.columnNames.equals(var2.columnNames) ? false : this.referenceColumnNames.equals(var2.referenceColumnNames);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (((this.referenceTable.hashCode() * 31 + this.onDelete.hashCode()) * 31 + this.onUpdate.hashCode()) * 31 + this.columnNames.hashCode()) * 31 + this.referenceColumnNames.hashCode();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("ForeignKey{referenceTable='");
         var1.append(this.referenceTable);
         var1.append('\'');
         var1.append(", onDelete='");
         var1.append(this.onDelete);
         var1.append('\'');
         var1.append(", onUpdate='");
         var1.append(this.onUpdate);
         var1.append('\'');
         var1.append(", columnNames=");
         var1.append(this.columnNames);
         var1.append(", referenceColumnNames=");
         var1.append(this.referenceColumnNames);
         var1.append('}');
         return var1.toString();
      }
   }

   static class ForeignKeyWithSequence implements Comparable {
      final String mFrom;
      final int mId;
      final int mSequence;
      final String mTo;

      ForeignKeyWithSequence(int var1, int var2, String var3, String var4) {
         this.mId = var1;
         this.mSequence = var2;
         this.mFrom = var3;
         this.mTo = var4;
      }

      public int compareTo(TableInfo.ForeignKeyWithSequence var1) {
         int var2 = this.mId - var1.mId;
         return var2 == 0 ? this.mSequence - var1.mSequence : var2;
      }
   }

   public static class Index {
      public final List columns;
      public final String name;
      public final boolean unique;

      public Index(String var1, boolean var2, List var3) {
         this.name = var1;
         this.unique = var2;
         this.columns = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            TableInfo.Index var2 = (TableInfo.Index)var1;
            if (this.unique != var2.unique) {
               return false;
            } else if (!this.columns.equals(var2.columns)) {
               return false;
            } else {
               return this.name.startsWith("index_") ? var2.name.startsWith("index_") : this.name.equals(var2.name);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1;
         if (this.name.startsWith("index_")) {
            var1 = "index_".hashCode();
         } else {
            var1 = this.name.hashCode();
         }

         return (var1 * 31 + this.unique) * 31 + this.columns.hashCode();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Index{name='");
         var1.append(this.name);
         var1.append('\'');
         var1.append(", unique=");
         var1.append(this.unique);
         var1.append(", columns=");
         var1.append(this.columns);
         var1.append('}');
         return var1.toString();
      }
   }
}
