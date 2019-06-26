// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

public class TableStatements
{
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
    
    public TableStatements(final Database db, final String tablename, final String[] allColumns, final String[] pkColumns) {
        this.db = db;
        this.tablename = tablename;
        this.allColumns = allColumns;
        this.pkColumns = pkColumns;
    }
    
    public DatabaseStatement getCountStatement() {
        if (this.countStatement == null) {
            this.countStatement = this.db.compileStatement(SqlUtils.createSqlCount(this.tablename));
        }
        return this.countStatement;
    }
    
    public DatabaseStatement getDeleteStatement() {
        if (this.deleteStatement == null) {
            final DatabaseStatement compileStatement = this.db.compileStatement(SqlUtils.createSqlDelete(this.tablename, this.pkColumns));
            synchronized (this) {
                if (this.deleteStatement == null) {
                    this.deleteStatement = compileStatement;
                }
                // monitorexit(this)
                if (this.deleteStatement != compileStatement) {
                    compileStatement.close();
                }
            }
        }
        return this.deleteStatement;
    }
    
    public DatabaseStatement getInsertOrReplaceStatement() {
        if (this.insertOrReplaceStatement == null) {
            final DatabaseStatement compileStatement = this.db.compileStatement(SqlUtils.createSqlInsert("INSERT OR REPLACE INTO ", this.tablename, this.allColumns));
            synchronized (this) {
                if (this.insertOrReplaceStatement == null) {
                    this.insertOrReplaceStatement = compileStatement;
                }
                // monitorexit(this)
                if (this.insertOrReplaceStatement != compileStatement) {
                    compileStatement.close();
                }
            }
        }
        return this.insertOrReplaceStatement;
    }
    
    public DatabaseStatement getInsertStatement() {
        if (this.insertStatement == null) {
            final DatabaseStatement compileStatement = this.db.compileStatement(SqlUtils.createSqlInsert("INSERT INTO ", this.tablename, this.allColumns));
            synchronized (this) {
                if (this.insertStatement == null) {
                    this.insertStatement = compileStatement;
                }
                // monitorexit(this)
                if (this.insertStatement != compileStatement) {
                    compileStatement.close();
                }
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
            final StringBuilder sb = new StringBuilder(this.getSelectAll());
            sb.append("WHERE ");
            SqlUtils.appendColumnsEqValue(sb, "T", this.pkColumns);
            this.selectByKey = sb.toString();
        }
        return this.selectByKey;
    }
    
    public String getSelectByRowId() {
        if (this.selectByRowId == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getSelectAll());
            sb.append("WHERE ROWID=?");
            this.selectByRowId = sb.toString();
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
            final DatabaseStatement compileStatement = this.db.compileStatement(SqlUtils.createSqlUpdate(this.tablename, this.allColumns, this.pkColumns));
            synchronized (this) {
                if (this.updateStatement == null) {
                    this.updateStatement = compileStatement;
                }
                // monitorexit(this)
                if (this.updateStatement != compileStatement) {
                    compileStatement.close();
                }
            }
        }
        return this.updateStatement;
    }
}
