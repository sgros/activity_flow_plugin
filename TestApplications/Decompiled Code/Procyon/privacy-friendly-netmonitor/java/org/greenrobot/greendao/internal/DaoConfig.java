// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.identityscope.IdentityScopeObject;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import java.util.Iterator;
import java.lang.reflect.Field;
import org.greenrobot.greendao.DaoException;
import java.util.ArrayList;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.database.Database;

public final class DaoConfig implements Cloneable
{
    public final String[] allColumns;
    public final Database db;
    private IdentityScope<?, ?> identityScope;
    public final boolean keyIsNumeric;
    public final String[] nonPkColumns;
    public final String[] pkColumns;
    public final Property pkProperty;
    public final Property[] properties;
    public final TableStatements statements;
    public final String tablename;
    
    public DaoConfig(final Database db, final Class<? extends AbstractDao<?, ?>> clazz) {
        this.db = db;
        try {
            final Field field = clazz.getField("TABLENAME");
            Property pkProperty = null;
            this.tablename = (String)field.get(null);
            final Property[] reflectProperties = reflectProperties(clazz);
            this.properties = reflectProperties;
            this.allColumns = new String[reflectProperties.length];
            final ArrayList<String> list = new ArrayList<String>();
            final ArrayList<String> list2 = new ArrayList<String>();
            Property property = null;
            for (int i = 0; i < reflectProperties.length; ++i) {
                final Property property2 = reflectProperties[i];
                final String columnName = property2.columnName;
                this.allColumns[i] = columnName;
                if (property2.primaryKey) {
                    list.add(columnName);
                    property = property2;
                }
                else {
                    list2.add(columnName);
                }
            }
            this.nonPkColumns = list2.toArray(new String[list2.size()]);
            this.pkColumns = list.toArray(new String[list.size()]);
            final int length = this.pkColumns.length;
            final boolean b = true;
            if (length == 1) {
                pkProperty = property;
            }
            this.pkProperty = pkProperty;
            this.statements = new TableStatements(db, this.tablename, this.allColumns, this.pkColumns);
            if (this.pkProperty != null) {
                final Class<?> type = this.pkProperty.type;
                boolean keyIsNumeric = b;
                if (!type.equals(Long.TYPE)) {
                    keyIsNumeric = b;
                    if (!type.equals(Long.class)) {
                        keyIsNumeric = b;
                        if (!type.equals(Integer.TYPE)) {
                            keyIsNumeric = b;
                            if (!type.equals(Integer.class)) {
                                keyIsNumeric = b;
                                if (!type.equals(Short.TYPE)) {
                                    keyIsNumeric = b;
                                    if (!type.equals(Short.class)) {
                                        keyIsNumeric = b;
                                        if (!type.equals(Byte.TYPE)) {
                                            keyIsNumeric = (type.equals(Byte.class) && b);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.keyIsNumeric = keyIsNumeric;
            }
            else {
                this.keyIsNumeric = false;
            }
        }
        catch (Exception ex) {
            throw new DaoException("Could not init DAOConfig", ex);
        }
    }
    
    public DaoConfig(final DaoConfig daoConfig) {
        this.db = daoConfig.db;
        this.tablename = daoConfig.tablename;
        this.properties = daoConfig.properties;
        this.allColumns = daoConfig.allColumns;
        this.pkColumns = daoConfig.pkColumns;
        this.nonPkColumns = daoConfig.nonPkColumns;
        this.pkProperty = daoConfig.pkProperty;
        this.statements = daoConfig.statements;
        this.keyIsNumeric = daoConfig.keyIsNumeric;
    }
    
    private static Property[] reflectProperties(final Class<? extends AbstractDao<?, ?>> clazz) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        final StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());
        sb.append("$Properties");
        final Field[] declaredFields = Class.forName(sb.toString()).getDeclaredFields();
        final ArrayList<Property> list = new ArrayList<Property>();
        for (int i = 0; i < declaredFields.length; ++i) {
            final Field field = declaredFields[i];
            if ((field.getModifiers() & 0x9) == 0x9) {
                final Object value = field.get(null);
                if (value instanceof Property) {
                    list.add((Property)value);
                }
            }
        }
        final Property[] array = new Property[list.size()];
        for (final Property property : list) {
            if (array[property.ordinal] != null) {
                throw new DaoException("Duplicate property ordinals");
            }
            array[property.ordinal] = property;
        }
        return array;
    }
    
    public void clearIdentityScope() {
        final IdentityScope<?, ?> identityScope = this.identityScope;
        if (identityScope != null) {
            identityScope.clear();
        }
    }
    
    public DaoConfig clone() {
        return new DaoConfig(this);
    }
    
    public IdentityScope<?, ?> getIdentityScope() {
        return this.identityScope;
    }
    
    public void initIdentityScope(final IdentityScopeType obj) {
        if (obj == IdentityScopeType.None) {
            this.identityScope = null;
        }
        else {
            if (obj != IdentityScopeType.Session) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unsupported type: ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            if (this.keyIsNumeric) {
                this.identityScope = new IdentityScopeLong<Object>();
            }
            else {
                this.identityScope = new IdentityScopeObject<Object, Object>();
            }
        }
    }
    
    public void setIdentityScope(final IdentityScope<?, ?> identityScope) {
        this.identityScope = identityScope;
    }
}
