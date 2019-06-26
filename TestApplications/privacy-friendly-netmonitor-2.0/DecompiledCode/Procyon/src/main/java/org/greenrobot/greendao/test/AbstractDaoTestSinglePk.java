// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.test;

import java.util.List;
import android.database.SQLException;
import java.util.Iterator;
import java.util.ArrayList;
import android.database.DatabaseUtils;
import org.greenrobot.greendao.internal.SqlUtils;
import android.database.Cursor;
import org.greenrobot.greendao.DaoLog;
import java.util.HashSet;
import java.util.Set;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.AbstractDao;

public abstract class AbstractDaoTestSinglePk<D extends AbstractDao<T, K>, T, K> extends AbstractDaoTest<D, T, K>
{
    private Property pkColumn;
    protected Set<K> usedPks;
    
    public AbstractDaoTestSinglePk(final Class<D> clazz) {
        super(clazz);
        this.usedPks = new HashSet<K>();
    }
    
    protected boolean checkKeyIsNullable() {
        if (this.createEntity(null) == null) {
            DaoLog.d("Test is not available for entities with non-null keys");
            return false;
        }
        return true;
    }
    
    protected abstract T createEntity(final K p0);
    
    protected T createEntityWithRandomPk() {
        return this.createEntity(this.nextPk());
    }
    
    protected abstract K createRandomPk();
    
    protected K nextPk() {
        for (int i = 0; i < 100000; ++i) {
            final K randomPk = this.createRandomPk();
            if (this.usedPks.add(randomPk)) {
                return randomPk;
            }
        }
        throw new IllegalStateException("Could not find a new PK");
    }
    
    protected Cursor queryWithDummyColumnsInFront(final int n, final String str, final K k) {
        final StringBuilder sb = new StringBuilder("SELECT ");
        final int n2 = 0;
        for (int i = 0; i < n; ++i) {
            sb.append(str);
            sb.append(",");
        }
        SqlUtils.appendColumns(sb, "T", this.dao.getAllColumns()).append(" FROM ");
        sb.append('\"');
        sb.append(this.dao.getTablename());
        sb.append('\"');
        sb.append(" T");
        if (k != null) {
            sb.append(" WHERE ");
            assertEquals(1, this.dao.getPkColumns().length);
            sb.append(this.dao.getPkColumns()[0]);
            sb.append("=");
            DatabaseUtils.appendValueToSql(sb, (Object)k);
        }
        final Cursor rawQuery = this.db.rawQuery(sb.toString(), null);
        assertTrue(rawQuery.moveToFirst());
        int j = n2;
        while (j < n) {
            Label_0236: {
                try {
                    assertEquals(str, rawQuery.getString(j));
                    ++j;
                    continue;
                }
                catch (RuntimeException ex) {
                    break Label_0236;
                }
                break;
            }
            rawQuery.close();
            throw;
        }
        if (k != null) {
            assertEquals(1, rawQuery.getCount());
        }
        return rawQuery;
    }
    
    protected void runLoadPkTest(final int n) {
        final K nextPk = this.nextPk();
        ((AbstractDao<T, K>)this.dao).insert(this.createEntity(nextPk));
        final Cursor queryWithDummyColumnsInFront = this.queryWithDummyColumnsInFront(n, "42", nextPk);
        try {
            assertEquals((Object)nextPk, (Object)this.daoAccess.readKey(queryWithDummyColumnsInFront, n));
        }
        finally {
            queryWithDummyColumnsInFront.close();
        }
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Property[] properties = this.daoAccess.getProperties();
        for (int i = 0; i < properties.length; ++i) {
            final Property pkColumn = properties[i];
            if (pkColumn.primaryKey) {
                if (this.pkColumn != null) {
                    throw new RuntimeException("Test does not work with multiple PK columns");
                }
                this.pkColumn = pkColumn;
            }
        }
        if (this.pkColumn == null) {
            throw new RuntimeException("Test does not work without a PK column");
        }
    }
    
    public void testCount() {
        this.dao.deleteAll();
        assertEquals(0L, this.dao.count());
        ((AbstractDao<T, K>)this.dao).insert(this.createEntityWithRandomPk());
        assertEquals(1L, this.dao.count());
        ((AbstractDao<T, K>)this.dao).insert(this.createEntityWithRandomPk());
        assertEquals(2L, this.dao.count());
    }
    
    public void testDelete() {
        final K nextPk = this.nextPk();
        ((AbstractDao<T, K>)this.dao).deleteByKey(nextPk);
        ((AbstractDao<T, K>)this.dao).insert(this.createEntity(nextPk));
        assertNotNull(((AbstractDao<Object, K>)this.dao).load(nextPk));
        ((AbstractDao<T, K>)this.dao).deleteByKey(nextPk);
        assertNull(((AbstractDao<Object, K>)this.dao).load(nextPk));
    }
    
    public void testDeleteAll() {
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < 10; ++i) {
            list.add(this.createEntityWithRandomPk());
        }
        ((AbstractDao<T, K>)this.dao).insertInTx(list);
        this.dao.deleteAll();
        assertEquals(0L, this.dao.count());
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            final K key = this.daoAccess.getKey(iterator.next());
            assertNotNull((Object)key);
            assertNull(((AbstractDao<Object, K>)this.dao).load(key));
        }
    }
    
    public void testDeleteByKeyInTx() {
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < 10; ++i) {
            list.add(this.createEntityWithRandomPk());
        }
        ((AbstractDao<T, K>)this.dao).insertInTx(list);
        final ArrayList<K> list2 = new ArrayList<K>();
        list2.add(this.daoAccess.getKey(list.get(0)));
        list2.add(this.daoAccess.getKey(list.get(3)));
        list2.add(this.daoAccess.getKey(list.get(4)));
        list2.add(this.daoAccess.getKey(list.get(8)));
        ((AbstractDao<T, K>)this.dao).deleteByKeyInTx(list2);
        assertEquals((long)(list.size() - list2.size()), this.dao.count());
        for (final K next : list2) {
            assertNotNull((Object)next);
            assertNull(((AbstractDao<Object, K>)this.dao).load(next));
        }
    }
    
    public void testDeleteInTx() {
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < 10; ++i) {
            list.add(this.createEntityWithRandomPk());
        }
        ((AbstractDao<T, K>)this.dao).insertInTx(list);
        final ArrayList<T> list2 = new ArrayList<T>();
        list2.add(list.get(0));
        list2.add(list.get(3));
        list2.add(list.get(4));
        list2.add(list.get(8));
        ((AbstractDao<T, K>)this.dao).deleteInTx(list2);
        assertEquals((long)(list.size() - list2.size()), this.dao.count());
        final Iterator<Object> iterator = list2.iterator();
        while (iterator.hasNext()) {
            final K key = this.daoAccess.getKey(iterator.next());
            assertNotNull((Object)key);
            assertNull(((AbstractDao<Object, K>)this.dao).load(key));
        }
    }
    
    public void testInsertAndLoad() {
        final K nextPk = this.nextPk();
        final T entity = this.createEntity(nextPk);
        ((AbstractDao<T, K>)this.dao).insert(entity);
        assertEquals((Object)nextPk, (Object)this.daoAccess.getKey(entity));
        final Object load = ((AbstractDao<Object, K>)this.dao).load(nextPk);
        assertNotNull(load);
        assertEquals((Object)this.daoAccess.getKey(entity), (Object)this.daoAccess.getKey((T)load));
    }
    
    public void testInsertInTx() {
        this.dao.deleteAll();
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < 20; ++i) {
            list.add(this.createEntityWithRandomPk());
        }
        ((AbstractDao<T, K>)this.dao).insertInTx(list);
        assertEquals((long)list.size(), this.dao.count());
    }
    
    public void testInsertOrReplaceInTx() {
        this.dao.deleteAll();
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<T>();
        final ArrayList<T> list2 = new ArrayList<T>();
        for (int i = 0; i < 20; ++i) {
            final T entityWithRandomPk = this.createEntityWithRandomPk();
            if (i % 2 == 0) {
                list.add(entityWithRandomPk);
            }
            list2.add(entityWithRandomPk);
        }
        ((AbstractDao<T, K>)this.dao).insertOrReplaceInTx((Iterable<T>)list);
        ((AbstractDao<T, K>)this.dao).insertOrReplaceInTx(list2);
        assertEquals((long)list2.size(), this.dao.count());
    }
    
    public void testInsertOrReplaceTwice() {
        final T entityWithRandomPk = this.createEntityWithRandomPk();
        final long insert = ((AbstractDao<T, K>)this.dao).insert(entityWithRandomPk);
        final long insertOrReplace = ((AbstractDao<T, K>)this.dao).insertOrReplace(entityWithRandomPk);
        if (this.dao.getPkProperty().type == Long.class) {
            assertEquals(insert, insertOrReplace);
        }
    }
    
    public void testInsertTwice() {
        final T entity = this.createEntity(this.nextPk());
        ((AbstractDao<T, K>)this.dao).insert(entity);
        try {
            ((AbstractDao<T, K>)this.dao).insert(entity);
            fail("Inserting twice should not work");
        }
        catch (SQLException ex) {}
    }
    
    public void testLoadAll() {
        this.dao.deleteAll();
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < 15; ++i) {
            list.add(this.createEntity(this.nextPk()));
        }
        ((AbstractDao<T, K>)this.dao).insertInTx(list);
        assertEquals(list.size(), ((AbstractDao<T, K>)this.dao).loadAll().size());
    }
    
    public void testLoadPk() {
        this.runLoadPkTest(0);
    }
    
    public void testLoadPkWithOffset() {
        this.runLoadPkTest(10);
    }
    
    public void testQuery() {
        ((AbstractDao<T, K>)this.dao).insert(this.createEntityWithRandomPk());
        final K nextPk = this.nextPk();
        ((AbstractDao<T, K>)this.dao).insert(this.createEntity(nextPk));
        ((AbstractDao<T, K>)this.dao).insert(this.createEntityWithRandomPk());
        final StringBuilder sb = new StringBuilder();
        sb.append("WHERE ");
        sb.append(this.dao.getPkColumns()[0]);
        sb.append("=?");
        final List<T> queryRaw = this.dao.queryRaw(sb.toString(), nextPk.toString());
        assertEquals(1, queryRaw.size());
        assertEquals((Object)nextPk, (Object)this.daoAccess.getKey(queryRaw.get(0)));
    }
    
    public void testReadWithOffset() {
        final K nextPk = this.nextPk();
        ((AbstractDao<T, K>)this.dao).insert(this.createEntity(nextPk));
        final Cursor queryWithDummyColumnsInFront = this.queryWithDummyColumnsInFront(5, "42", nextPk);
        try {
            assertEquals((Object)nextPk, (Object)this.daoAccess.getKey(this.daoAccess.readEntity(queryWithDummyColumnsInFront, 5)));
        }
        finally {
            queryWithDummyColumnsInFront.close();
        }
    }
    
    public void testRowId() {
        assertTrue(((AbstractDao<T, K>)this.dao).insert(this.createEntityWithRandomPk()) != ((AbstractDao<T, K>)this.dao).insert(this.createEntityWithRandomPk()));
    }
    
    public void testSave() {
        if (!this.checkKeyIsNullable()) {
            return;
        }
        this.dao.deleteAll();
        final T entity = this.createEntity(null);
        if (entity != null) {
            ((AbstractDao<T, K>)this.dao).save(entity);
            ((AbstractDao<T, K>)this.dao).save(entity);
            assertEquals(1L, this.dao.count());
        }
    }
    
    public void testSaveInTx() {
        if (!this.checkKeyIsNullable()) {
            return;
        }
        this.dao.deleteAll();
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<T>();
        final ArrayList<T> list2 = new ArrayList<T>();
        for (int i = 0; i < 20; ++i) {
            final T entity = this.createEntity(null);
            if (i % 2 == 0) {
                list.add(entity);
            }
            list2.add(entity);
        }
        ((AbstractDao<T, K>)this.dao).saveInTx((Iterable<T>)list);
        ((AbstractDao<T, K>)this.dao).saveInTx(list2);
        assertEquals((long)list2.size(), this.dao.count());
    }
    
    public void testUpdate() {
        this.dao.deleteAll();
        final T entityWithRandomPk = this.createEntityWithRandomPk();
        ((AbstractDao<T, K>)this.dao).insert(entityWithRandomPk);
        ((AbstractDao<T, K>)this.dao).update(entityWithRandomPk);
        assertEquals(1L, this.dao.count());
    }
}
