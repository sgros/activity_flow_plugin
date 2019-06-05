// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room.util;

import android.os.Build$VERSION;
import java.util.Locale;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import java.util.HashMap;
import android.arch.persistence.db.SupportSQLiteDatabase;
import java.util.Collections;
import java.util.Set;
import java.util.Map;

public class TableInfo
{
    public final Map<String, Column> columns;
    public final Set<ForeignKey> foreignKeys;
    public final Set<Index> indices;
    public final String name;
    
    public TableInfo(final String name, final Map<String, Column> m, final Set<ForeignKey> s, final Set<Index> s2) {
        this.name = name;
        this.columns = Collections.unmodifiableMap((Map<? extends String, ? extends Column>)m);
        this.foreignKeys = Collections.unmodifiableSet((Set<? extends ForeignKey>)s);
        Set<Index> unmodifiableSet;
        if (s2 == null) {
            unmodifiableSet = null;
        }
        else {
            unmodifiableSet = Collections.unmodifiableSet((Set<? extends Index>)s2);
        }
        this.indices = unmodifiableSet;
    }
    
    public static TableInfo read(final SupportSQLiteDatabase supportSQLiteDatabase, final String s) {
        return new TableInfo(s, readColumns(supportSQLiteDatabase, s), readForeignKeys(supportSQLiteDatabase, s), readIndices(supportSQLiteDatabase, s));
    }
    
    private static Map<String, Column> readColumns(SupportSQLiteDatabase query, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA table_info(`");
        sb.append(str);
        sb.append("`)");
        query = (SupportSQLiteDatabase)query.query(sb.toString());
        final HashMap<String, Column> hashMap = new HashMap<String, Column>();
        try {
            if (((Cursor)query).getColumnCount() > 0) {
                final int columnIndex = ((Cursor)query).getColumnIndex("name");
                final int columnIndex2 = ((Cursor)query).getColumnIndex("type");
                final int columnIndex3 = ((Cursor)query).getColumnIndex("notnull");
                final int columnIndex4 = ((Cursor)query).getColumnIndex("pk");
                while (((Cursor)query).moveToNext()) {
                    final String string = ((Cursor)query).getString(columnIndex);
                    hashMap.put(string, new Column(string, ((Cursor)query).getString(columnIndex2), ((Cursor)query).getInt(columnIndex3) != 0, ((Cursor)query).getInt(columnIndex4)));
                }
            }
            return hashMap;
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    private static List<ForeignKeyWithSequence> readForeignKeyFieldMappings(final Cursor cursor) {
        final int columnIndex = cursor.getColumnIndex("id");
        final int columnIndex2 = cursor.getColumnIndex("seq");
        final int columnIndex3 = cursor.getColumnIndex("from");
        final int columnIndex4 = cursor.getColumnIndex("to");
        final int count = cursor.getCount();
        final ArrayList<Comparable> list = new ArrayList<Comparable>();
        for (int i = 0; i < count; ++i) {
            cursor.moveToPosition(i);
            list.add(new ForeignKeyWithSequence(cursor.getInt(columnIndex), cursor.getInt(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4)));
        }
        Collections.sort(list);
        return (List<ForeignKeyWithSequence>)list;
    }
    
    private static Set<ForeignKey> readForeignKeys(SupportSQLiteDatabase query, final String str) {
        final HashSet<ForeignKey> set = new HashSet<ForeignKey>();
        final StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA foreign_key_list(`");
        sb.append(str);
        sb.append("`)");
        query = (SupportSQLiteDatabase)query.query(sb.toString());
        try {
            final int columnIndex = ((Cursor)query).getColumnIndex("id");
            final int columnIndex2 = ((Cursor)query).getColumnIndex("seq");
            final int columnIndex3 = ((Cursor)query).getColumnIndex("table");
            final int columnIndex4 = ((Cursor)query).getColumnIndex("on_delete");
            final int columnIndex5 = ((Cursor)query).getColumnIndex("on_update");
            final List<ForeignKeyWithSequence> foreignKeyFieldMappings = readForeignKeyFieldMappings((Cursor)query);
            for (int count = ((Cursor)query).getCount(), i = 0; i < count; ++i) {
                ((Cursor)query).moveToPosition(i);
                if (((Cursor)query).getInt(columnIndex2) == 0) {
                    final int int1 = ((Cursor)query).getInt(columnIndex);
                    final ArrayList<String> list = new ArrayList<String>();
                    final ArrayList<String> list2 = new ArrayList<String>();
                    for (final ForeignKeyWithSequence foreignKeyWithSequence : foreignKeyFieldMappings) {
                        if (foreignKeyWithSequence.mId == int1) {
                            list.add(foreignKeyWithSequence.mFrom);
                            list2.add(foreignKeyWithSequence.mTo);
                        }
                    }
                    set.add(new ForeignKey(((Cursor)query).getString(columnIndex3), ((Cursor)query).getString(columnIndex4), ((Cursor)query).getString(columnIndex5), list, list2));
                }
            }
            return set;
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    private static Index readIndex(SupportSQLiteDatabase query, final String str, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA index_xinfo(`");
        sb.append(str);
        sb.append("`)");
        query = (SupportSQLiteDatabase)query.query(sb.toString());
        try {
            final int columnIndex = ((Cursor)query).getColumnIndex("seqno");
            final int columnIndex2 = ((Cursor)query).getColumnIndex("cid");
            final int columnIndex3 = ((Cursor)query).getColumnIndex("name");
            if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1) {
                final TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
                while (((Cursor)query).moveToNext()) {
                    if (((Cursor)query).getInt(columnIndex2) < 0) {
                        continue;
                    }
                    treeMap.put(((Cursor)query).getInt(columnIndex), ((Cursor)query).getString(columnIndex3));
                }
                final ArrayList list = new ArrayList<String>(treeMap.size());
                list.addAll((Collection<?>)treeMap.values());
                return new Index(str, b, (List<String>)list);
            }
            return null;
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    private static Set<Index> readIndices(final SupportSQLiteDatabase supportSQLiteDatabase, String query) {
        final StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA index_list(`");
        sb.append(query);
        sb.append("`)");
        query = (String)supportSQLiteDatabase.query(sb.toString());
        try {
            final int columnIndex = ((Cursor)query).getColumnIndex("name");
            final int columnIndex2 = ((Cursor)query).getColumnIndex("origin");
            final int columnIndex3 = ((Cursor)query).getColumnIndex("unique");
            if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1) {
                final HashSet<Index> set = new HashSet<Index>();
                while (((Cursor)query).moveToNext()) {
                    if (!"c".equals(((Cursor)query).getString(columnIndex2))) {
                        continue;
                    }
                    final String string = ((Cursor)query).getString(columnIndex);
                    final int int1 = ((Cursor)query).getInt(columnIndex3);
                    boolean b = true;
                    if (int1 != 1) {
                        b = false;
                    }
                    final Index index = readIndex(supportSQLiteDatabase, string, b);
                    if (index == null) {
                        return null;
                    }
                    set.add(index);
                }
                return set;
            }
            return null;
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final TableInfo tableInfo = (TableInfo)o;
            Label_0063: {
                if (this.name != null) {
                    if (this.name.equals(tableInfo.name)) {
                        break Label_0063;
                    }
                }
                else if (tableInfo.name == null) {
                    break Label_0063;
                }
                return false;
            }
            Label_0098: {
                if (this.columns != null) {
                    if (this.columns.equals(tableInfo.columns)) {
                        break Label_0098;
                    }
                }
                else if (tableInfo.columns == null) {
                    break Label_0098;
                }
                return false;
            }
            if (this.foreignKeys != null) {
                if (this.foreignKeys.equals(tableInfo.foreignKeys)) {
                    return this.indices == null || tableInfo.indices == null || this.indices.equals(tableInfo.indices);
                }
            }
            else if (tableInfo.foreignKeys == null) {
                return this.indices == null || tableInfo.indices == null || this.indices.equals(tableInfo.indices);
            }
            return false;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String name = this.name;
        int hashCode = 0;
        int hashCode2;
        if (name != null) {
            hashCode2 = this.name.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        int hashCode3;
        if (this.columns != null) {
            hashCode3 = this.columns.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        if (this.foreignKeys != null) {
            hashCode = this.foreignKeys.hashCode();
        }
        return (hashCode2 * 31 + hashCode3) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TableInfo{name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", columns=");
        sb.append(this.columns);
        sb.append(", foreignKeys=");
        sb.append(this.foreignKeys);
        sb.append(", indices=");
        sb.append(this.indices);
        sb.append('}');
        return sb.toString();
    }
    
    public static class Column
    {
        public final int affinity;
        public final String name;
        public final boolean notNull;
        public final int primaryKeyPosition;
        public final String type;
        
        public Column(final String name, final String type, final boolean notNull, final int primaryKeyPosition) {
            this.name = name;
            this.type = type;
            this.notNull = notNull;
            this.primaryKeyPosition = primaryKeyPosition;
            this.affinity = findAffinity(type);
        }
        
        private static int findAffinity(String upperCase) {
            if (upperCase == null) {
                return 5;
            }
            upperCase = upperCase.toUpperCase(Locale.US);
            if (upperCase.contains("INT")) {
                return 3;
            }
            if (upperCase.contains("CHAR") || upperCase.contains("CLOB") || upperCase.contains("TEXT")) {
                return 2;
            }
            if (upperCase.contains("BLOB")) {
                return 5;
            }
            if (!upperCase.contains("REAL") && !upperCase.contains("FLOA") && !upperCase.contains("DOUB")) {
                return 1;
            }
            return 4;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Column column = (Column)o;
            if (Build$VERSION.SDK_INT >= 20) {
                if (this.primaryKeyPosition != column.primaryKeyPosition) {
                    return false;
                }
            }
            else if (this.isPrimaryKey() != column.isPrimaryKey()) {
                return false;
            }
            if (!this.name.equals(column.name)) {
                return false;
            }
            if (this.notNull != column.notNull) {
                return false;
            }
            if (this.affinity != column.affinity) {
                b = false;
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = this.name.hashCode();
            final int affinity = this.affinity;
            int n;
            if (this.notNull) {
                n = 1231;
            }
            else {
                n = 1237;
            }
            return ((hashCode * 31 + affinity) * 31 + n) * 31 + this.primaryKeyPosition;
        }
        
        public boolean isPrimaryKey() {
            return this.primaryKeyPosition > 0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Column{name='");
            sb.append(this.name);
            sb.append('\'');
            sb.append(", type='");
            sb.append(this.type);
            sb.append('\'');
            sb.append(", affinity='");
            sb.append(this.affinity);
            sb.append('\'');
            sb.append(", notNull=");
            sb.append(this.notNull);
            sb.append(", primaryKeyPosition=");
            sb.append(this.primaryKeyPosition);
            sb.append('}');
            return sb.toString();
        }
    }
    
    public static class ForeignKey
    {
        public final List<String> columnNames;
        public final String onDelete;
        public final String onUpdate;
        public final List<String> referenceColumnNames;
        public final String referenceTable;
        
        public ForeignKey(final String referenceTable, final String onDelete, final String onUpdate, final List<String> list, final List<String> list2) {
            this.referenceTable = referenceTable;
            this.onDelete = onDelete;
            this.onUpdate = onUpdate;
            this.columnNames = Collections.unmodifiableList((List<? extends String>)list);
            this.referenceColumnNames = Collections.unmodifiableList((List<? extends String>)list2);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && this.getClass() == o.getClass()) {
                final ForeignKey foreignKey = (ForeignKey)o;
                return this.referenceTable.equals(foreignKey.referenceTable) && this.onDelete.equals(foreignKey.onDelete) && this.onUpdate.equals(foreignKey.onUpdate) && this.columnNames.equals(foreignKey.columnNames) && this.referenceColumnNames.equals(foreignKey.referenceColumnNames);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (((this.referenceTable.hashCode() * 31 + this.onDelete.hashCode()) * 31 + this.onUpdate.hashCode()) * 31 + this.columnNames.hashCode()) * 31 + this.referenceColumnNames.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ForeignKey{referenceTable='");
            sb.append(this.referenceTable);
            sb.append('\'');
            sb.append(", onDelete='");
            sb.append(this.onDelete);
            sb.append('\'');
            sb.append(", onUpdate='");
            sb.append(this.onUpdate);
            sb.append('\'');
            sb.append(", columnNames=");
            sb.append(this.columnNames);
            sb.append(", referenceColumnNames=");
            sb.append(this.referenceColumnNames);
            sb.append('}');
            return sb.toString();
        }
    }
    
    static class ForeignKeyWithSequence implements Comparable<ForeignKeyWithSequence>
    {
        final String mFrom;
        final int mId;
        final int mSequence;
        final String mTo;
        
        ForeignKeyWithSequence(final int mId, final int mSequence, final String mFrom, final String mTo) {
            this.mId = mId;
            this.mSequence = mSequence;
            this.mFrom = mFrom;
            this.mTo = mTo;
        }
        
        @Override
        public int compareTo(final ForeignKeyWithSequence foreignKeyWithSequence) {
            final int n = this.mId - foreignKeyWithSequence.mId;
            if (n == 0) {
                return this.mSequence - foreignKeyWithSequence.mSequence;
            }
            return n;
        }
    }
    
    public static class Index
    {
        public final List<String> columns;
        public final String name;
        public final boolean unique;
        
        public Index(final String name, final boolean unique, final List<String> columns) {
            this.name = name;
            this.unique = unique;
            this.columns = columns;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Index index = (Index)o;
            if (this.unique != index.unique) {
                return false;
            }
            if (!this.columns.equals(index.columns)) {
                return false;
            }
            if (this.name.startsWith("index_")) {
                return index.name.startsWith("index_");
            }
            return this.name.equals(index.name);
        }
        
        @Override
        public int hashCode() {
            int n;
            if (this.name.startsWith("index_")) {
                n = "index_".hashCode();
            }
            else {
                n = this.name.hashCode();
            }
            return (n * 31 + (this.unique ? 1 : 0)) * 31 + this.columns.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Index{name='");
            sb.append(this.name);
            sb.append('\'');
            sb.append(", unique=");
            sb.append(this.unique);
            sb.append(", columns=");
            sb.append(this.columns);
            sb.append('}');
            return sb.toString();
        }
    }
}
