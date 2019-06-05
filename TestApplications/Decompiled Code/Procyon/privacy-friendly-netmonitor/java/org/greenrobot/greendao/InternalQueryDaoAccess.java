// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import java.util.List;
import android.database.Cursor;
import org.greenrobot.greendao.internal.TableStatements;

public final class InternalQueryDaoAccess<T>
{
    private final AbstractDao<T, ?> dao;
    
    public InternalQueryDaoAccess(final AbstractDao<T, ?> dao) {
        this.dao = dao;
    }
    
    public static <T2> TableStatements getStatements(final AbstractDao<T2, ?> abstractDao) {
        return abstractDao.getStatements();
    }
    
    public TableStatements getStatements() {
        return this.dao.getStatements();
    }
    
    public List<T> loadAllAndCloseCursor(final Cursor cursor) {
        return this.dao.loadAllAndCloseCursor(cursor);
    }
    
    public T loadCurrent(final Cursor cursor, final int n, final boolean b) {
        return this.dao.loadCurrent(cursor, n, b);
    }
    
    public T loadUniqueAndCloseCursor(final Cursor cursor) {
        return this.dao.loadUniqueAndCloseCursor(cursor);
    }
}
