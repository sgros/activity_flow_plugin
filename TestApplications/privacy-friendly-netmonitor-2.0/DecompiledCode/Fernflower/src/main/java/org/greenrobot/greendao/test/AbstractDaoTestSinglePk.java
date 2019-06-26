package org.greenrobot.greendao.test;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;

public abstract class AbstractDaoTestSinglePk extends AbstractDaoTest {
   private Property pkColumn;
   protected Set usedPks = new HashSet();

   public AbstractDaoTestSinglePk(Class var1) {
      super(var1);
   }

   protected boolean checkKeyIsNullable() {
      if (this.createEntity((Object)null) == null) {
         DaoLog.d("Test is not available for entities with non-null keys");
         return false;
      } else {
         return true;
      }
   }

   protected abstract Object createEntity(Object var1);

   protected Object createEntityWithRandomPk() {
      return this.createEntity(this.nextPk());
   }

   protected abstract Object createRandomPk();

   protected Object nextPk() {
      for(int var1 = 0; var1 < 100000; ++var1) {
         Object var2 = this.createRandomPk();
         if (this.usedPks.add(var2)) {
            return var2;
         }
      }

      throw new IllegalStateException("Could not find a new PK");
   }

   protected Cursor queryWithDummyColumnsInFront(int var1, String var2, Object var3) {
      StringBuilder var4 = new StringBuilder("SELECT ");
      byte var5 = 0;

      int var6;
      for(var6 = 0; var6 < var1; ++var6) {
         var4.append(var2);
         var4.append(",");
      }

      SqlUtils.appendColumns(var4, "T", this.dao.getAllColumns()).append(" FROM ");
      var4.append('"');
      var4.append(this.dao.getTablename());
      var4.append('"');
      var4.append(" T");
      if (var3 != null) {
         var4.append(" WHERE ");
         assertEquals(1, this.dao.getPkColumns().length);
         var4.append(this.dao.getPkColumns()[0]);
         var4.append("=");
         DatabaseUtils.appendValueToSql(var4, var3);
      }

      String var10 = var4.toString();
      Cursor var11 = this.db.rawQuery(var10, (String[])null);
      assertTrue(var11.moveToFirst());
      var6 = var5;

      while(true) {
         RuntimeException var10000;
         boolean var10001;
         if (var6 < var1) {
            label30: {
               try {
                  assertEquals(var2, var11.getString(var6));
               } catch (RuntimeException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label30;
               }

               ++var6;
               continue;
            }
         } else {
            if (var3 == null) {
               break;
            }

            try {
               assertEquals(1, var11.getCount());
               break;
            } catch (RuntimeException var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         RuntimeException var9 = var10000;
         var11.close();
         throw var9;
      }

      return var11;
   }

   protected void runLoadPkTest(int var1) {
      Object var2 = this.nextPk();
      Object var3 = this.createEntity(var2);
      this.dao.insert(var3);
      Cursor var6 = this.queryWithDummyColumnsInFront(var1, "42", var2);

      try {
         assertEquals(var2, this.daoAccess.readKey(var6, var1));
      } finally {
         var6.close();
      }

   }

   protected void setUp() throws Exception {
      super.setUp();
      Property[] var1 = this.daoAccess.getProperties();
      int var2 = 0;

      for(int var3 = var1.length; var2 < var3; ++var2) {
         Property var4 = var1[var2];
         if (var4.primaryKey) {
            if (this.pkColumn != null) {
               throw new RuntimeException("Test does not work with multiple PK columns");
            }

            this.pkColumn = var4;
         }
      }

      if (this.pkColumn == null) {
         throw new RuntimeException("Test does not work without a PK column");
      }
   }

   public void testCount() {
      this.dao.deleteAll();
      assertEquals(0L, this.dao.count());
      this.dao.insert(this.createEntityWithRandomPk());
      assertEquals(1L, this.dao.count());
      this.dao.insert(this.createEntityWithRandomPk());
      assertEquals(2L, this.dao.count());
   }

   public void testDelete() {
      Object var1 = this.nextPk();
      this.dao.deleteByKey(var1);
      Object var2 = this.createEntity(var1);
      this.dao.insert(var2);
      assertNotNull(this.dao.load(var1));
      this.dao.deleteByKey(var1);
      assertNull(this.dao.load(var1));
   }

   public void testDeleteAll() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 10; ++var2) {
         var1.add(this.createEntityWithRandomPk());
      }

      this.dao.insertInTx((Iterable)var1);
      this.dao.deleteAll();
      assertEquals(0L, this.dao.count());
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         var3 = this.daoAccess.getKey(var3);
         assertNotNull(var3);
         assertNull(this.dao.load(var3));
      }

   }

   public void testDeleteByKeyInTx() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 10; ++var2) {
         var1.add(this.createEntityWithRandomPk());
      }

      this.dao.insertInTx((Iterable)var1);
      ArrayList var3 = new ArrayList();
      var3.add(this.daoAccess.getKey(var1.get(0)));
      var3.add(this.daoAccess.getKey(var1.get(3)));
      var3.add(this.daoAccess.getKey(var1.get(4)));
      var3.add(this.daoAccess.getKey(var1.get(8)));
      this.dao.deleteByKeyInTx((Iterable)var3);
      assertEquals((long)(var1.size() - var3.size()), this.dao.count());
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         assertNotNull(var5);
         assertNull(this.dao.load(var5));
      }

   }

   public void testDeleteInTx() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 10; ++var2) {
         var1.add(this.createEntityWithRandomPk());
      }

      this.dao.insertInTx((Iterable)var1);
      ArrayList var3 = new ArrayList();
      var3.add(var1.get(0));
      var3.add(var1.get(3));
      var3.add(var1.get(4));
      var3.add(var1.get(8));
      this.dao.deleteInTx((Iterable)var3);
      assertEquals((long)(var1.size() - var3.size()), this.dao.count());
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Object var4 = var5.next();
         var4 = this.daoAccess.getKey(var4);
         assertNotNull(var4);
         assertNull(this.dao.load(var4));
      }

   }

   public void testInsertAndLoad() {
      Object var1 = this.nextPk();
      Object var2 = this.createEntity(var1);
      this.dao.insert(var2);
      assertEquals(var1, this.daoAccess.getKey(var2));
      var1 = this.dao.load(var1);
      assertNotNull(var1);
      assertEquals(this.daoAccess.getKey(var2), this.daoAccess.getKey(var1));
   }

   public void testInsertInTx() {
      this.dao.deleteAll();
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 20; ++var2) {
         var1.add(this.createEntityWithRandomPk());
      }

      this.dao.insertInTx((Iterable)var1);
      assertEquals((long)var1.size(), this.dao.count());
   }

   public void testInsertOrReplaceInTx() {
      this.dao.deleteAll();
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < 20; ++var3) {
         Object var4 = this.createEntityWithRandomPk();
         if (var3 % 2 == 0) {
            var1.add(var4);
         }

         var2.add(var4);
      }

      this.dao.insertOrReplaceInTx((Iterable)var1);
      this.dao.insertOrReplaceInTx((Iterable)var2);
      assertEquals((long)var2.size(), this.dao.count());
   }

   public void testInsertOrReplaceTwice() {
      Object var1 = this.createEntityWithRandomPk();
      long var2 = this.dao.insert(var1);
      long var4 = this.dao.insertOrReplace(var1);
      if (this.dao.getPkProperty().type == Long.class) {
         assertEquals(var2, var4);
      }

   }

   public void testInsertTwice() {
      Object var1 = this.createEntity(this.nextPk());
      this.dao.insert(var1);

      try {
         this.dao.insert(var1);
         fail("Inserting twice should not work");
      } catch (SQLException var2) {
      }

   }

   public void testLoadAll() {
      this.dao.deleteAll();
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 15; ++var2) {
         var1.add(this.createEntity(this.nextPk()));
      }

      this.dao.insertInTx((Iterable)var1);
      List var3 = this.dao.loadAll();
      assertEquals(var1.size(), var3.size());
   }

   public void testLoadPk() {
      this.runLoadPkTest(0);
   }

   public void testLoadPkWithOffset() {
      this.runLoadPkTest(10);
   }

   public void testQuery() {
      this.dao.insert(this.createEntityWithRandomPk());
      Object var1 = this.nextPk();
      this.dao.insert(this.createEntity(var1));
      this.dao.insert(this.createEntityWithRandomPk());
      StringBuilder var2 = new StringBuilder();
      var2.append("WHERE ");
      var2.append(this.dao.getPkColumns()[0]);
      var2.append("=?");
      String var3 = var2.toString();
      List var4 = this.dao.queryRaw(var3, var1.toString());
      assertEquals(1, var4.size());
      assertEquals(var1, this.daoAccess.getKey(var4.get(0)));
   }

   public void testReadWithOffset() {
      Object var1 = this.nextPk();
      Object var2 = this.createEntity(var1);
      this.dao.insert(var2);
      Cursor var6 = this.queryWithDummyColumnsInFront(5, "42", var1);

      try {
         Object var3 = this.daoAccess.readEntity(var6, 5);
         assertEquals(var1, this.daoAccess.getKey(var3));
      } finally {
         var6.close();
      }

   }

   public void testRowId() {
      Object var1 = this.createEntityWithRandomPk();
      Object var2 = this.createEntityWithRandomPk();
      boolean var3;
      if (this.dao.insert(var1) != this.dao.insert(var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      assertTrue(var3);
   }

   public void testSave() {
      if (this.checkKeyIsNullable()) {
         this.dao.deleteAll();
         Object var1 = this.createEntity((Object)null);
         if (var1 != null) {
            this.dao.save(var1);
            this.dao.save(var1);
            assertEquals(1L, this.dao.count());
         }

      }
   }

   public void testSaveInTx() {
      if (this.checkKeyIsNullable()) {
         this.dao.deleteAll();
         ArrayList var1 = new ArrayList();
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < 20; ++var3) {
            Object var4 = this.createEntity((Object)null);
            if (var3 % 2 == 0) {
               var1.add(var4);
            }

            var2.add(var4);
         }

         this.dao.saveInTx((Iterable)var1);
         this.dao.saveInTx((Iterable)var2);
         assertEquals((long)var2.size(), this.dao.count());
      }
   }

   public void testUpdate() {
      this.dao.deleteAll();
      Object var1 = this.createEntityWithRandomPk();
      this.dao.insert(var1);
      this.dao.update(var1);
      assertEquals(1L, this.dao.count());
   }
}
