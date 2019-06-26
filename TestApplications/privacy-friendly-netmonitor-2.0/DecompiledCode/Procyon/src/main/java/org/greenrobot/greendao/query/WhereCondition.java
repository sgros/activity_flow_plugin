// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import org.greenrobot.greendao.internal.SqlUtils;
import java.util.Date;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import java.util.List;

public interface WhereCondition
{
    void appendTo(final StringBuilder p0, final String p1);
    
    void appendValuesTo(final List<Object> p0);
    
    public abstract static class AbstractCondition implements WhereCondition
    {
        protected final boolean hasSingleValue;
        protected final Object value;
        protected final Object[] values;
        
        public AbstractCondition() {
            this.hasSingleValue = false;
            this.value = null;
            this.values = null;
        }
        
        public AbstractCondition(final Object value) {
            this.value = value;
            this.hasSingleValue = true;
            this.values = null;
        }
        
        public AbstractCondition(final Object[] values) {
            this.value = null;
            this.hasSingleValue = false;
            this.values = values;
        }
        
        @Override
        public void appendValuesTo(final List<Object> list) {
            if (this.hasSingleValue) {
                list.add(this.value);
            }
            else if (this.values != null) {
                final Object[] values = this.values;
                for (int length = values.length, i = 0; i < length; ++i) {
                    list.add(values[i]);
                }
            }
        }
    }
    
    public static class PropertyCondition extends AbstractCondition
    {
        public final String op;
        public final Property property;
        
        public PropertyCondition(final Property property, final String op) {
            this.property = property;
            this.op = op;
        }
        
        public PropertyCondition(final Property property, final String op, final Object o) {
            super(checkValueForType(property, o));
            this.property = property;
            this.op = op;
        }
        
        public PropertyCondition(final Property property, final String op, final Object[] array) {
            super(checkValuesForType(property, array));
            this.property = property;
            this.op = op;
        }
        
        private static Object checkValueForType(final Property property, final Object obj) {
            if (obj != null && obj.getClass().isArray()) {
                throw new DaoException("Illegal value: found array, but simple object required");
            }
            if (property.type != Date.class) {
                if (property.type == Boolean.TYPE || property.type == Boolean.class) {
                    if (obj instanceof Boolean) {
                        return ((boolean)obj) ? 1 : 0;
                    }
                    if (obj instanceof Number) {
                        final int intValue = ((Number)obj).intValue();
                        if (intValue != 0 && intValue != 1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Illegal boolean value: numbers must be 0 or 1, but was ");
                            sb.append(obj);
                            throw new DaoException(sb.toString());
                        }
                    }
                    else if (obj instanceof String) {
                        final String s = (String)obj;
                        if ("TRUE".equalsIgnoreCase(s)) {
                            return 1;
                        }
                        if ("FALSE".equalsIgnoreCase(s)) {
                            return 0;
                        }
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Illegal boolean value: Strings must be \"TRUE\" or \"FALSE\" (case insensitive), but was ");
                        sb2.append(obj);
                        throw new DaoException(sb2.toString());
                    }
                }
                return obj;
            }
            if (obj instanceof Date) {
                return ((Date)obj).getTime();
            }
            if (obj instanceof Long) {
                return obj;
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Illegal date value: expected java.util.Date or Long for value ");
            sb3.append(obj);
            throw new DaoException(sb3.toString());
        }
        
        private static Object[] checkValuesForType(final Property property, final Object[] array) {
            for (int i = 0; i < array.length; ++i) {
                array[i] = checkValueForType(property, array[i]);
            }
            return array;
        }
        
        @Override
        public void appendTo(final StringBuilder sb, final String s) {
            SqlUtils.appendProperty(sb, s, this.property).append(this.op);
        }
    }
    
    public static class StringCondition extends AbstractCondition
    {
        protected final String string;
        
        public StringCondition(final String string) {
            this.string = string;
        }
        
        public StringCondition(final String string, final Object o) {
            super(o);
            this.string = string;
        }
        
        public StringCondition(final String string, final Object... array) {
            super(array);
            this.string = string;
        }
        
        @Override
        public void appendTo(final StringBuilder sb, final String s) {
            sb.append(this.string);
        }
    }
}
