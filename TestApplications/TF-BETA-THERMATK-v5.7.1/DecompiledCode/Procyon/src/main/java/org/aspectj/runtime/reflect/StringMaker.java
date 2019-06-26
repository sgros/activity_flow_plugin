// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import java.lang.reflect.Modifier;

class StringMaker
{
    static StringMaker longStringMaker;
    static StringMaker middleStringMaker;
    static StringMaker shortStringMaker;
    int cacheOffset;
    boolean includeArgs;
    boolean includeEnclosingPoint;
    boolean includeJoinPointTypeName;
    boolean includeModifiers;
    boolean includeThrows;
    boolean shortKindName;
    boolean shortPrimaryTypeNames;
    boolean shortTypeNames;
    
    static {
        StringMaker.shortStringMaker = new StringMaker();
        final StringMaker shortStringMaker = StringMaker.shortStringMaker;
        shortStringMaker.shortTypeNames = true;
        shortStringMaker.includeArgs = false;
        shortStringMaker.includeThrows = false;
        shortStringMaker.includeModifiers = false;
        shortStringMaker.shortPrimaryTypeNames = true;
        shortStringMaker.includeJoinPointTypeName = false;
        shortStringMaker.includeEnclosingPoint = false;
        shortStringMaker.cacheOffset = 0;
        StringMaker.middleStringMaker = new StringMaker();
        final StringMaker middleStringMaker = StringMaker.middleStringMaker;
        middleStringMaker.shortTypeNames = true;
        middleStringMaker.includeArgs = true;
        middleStringMaker.includeThrows = false;
        middleStringMaker.includeModifiers = false;
        middleStringMaker.shortPrimaryTypeNames = false;
        StringMaker.shortStringMaker.cacheOffset = 1;
        StringMaker.longStringMaker = new StringMaker();
        final StringMaker longStringMaker = StringMaker.longStringMaker;
        longStringMaker.shortTypeNames = false;
        longStringMaker.includeArgs = true;
        longStringMaker.includeThrows = false;
        longStringMaker.includeModifiers = true;
        longStringMaker.shortPrimaryTypeNames = false;
        longStringMaker.shortKindName = false;
        longStringMaker.cacheOffset = 2;
    }
    
    StringMaker() {
        this.shortTypeNames = true;
        this.includeArgs = true;
        this.includeThrows = false;
        this.includeModifiers = false;
        this.shortPrimaryTypeNames = false;
        this.includeJoinPointTypeName = true;
        this.includeEnclosingPoint = true;
        this.shortKindName = true;
    }
    
    public void addSignature(final StringBuffer sb, final Class[] array) {
        if (array == null) {
            return;
        }
        if (this.includeArgs) {
            sb.append("(");
            this.addTypeNames(sb, array);
            sb.append(")");
            return;
        }
        if (array.length == 0) {
            sb.append("()");
            return;
        }
        sb.append("(..)");
    }
    
    public void addThrows(final StringBuffer sb, final Class[] array) {
        if (this.includeThrows && array != null) {
            if (array.length != 0) {
                sb.append(" throws ");
                this.addTypeNames(sb, array);
            }
        }
    }
    
    public void addTypeNames(final StringBuffer sb, final Class[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.makeTypeName(array[i]));
        }
    }
    
    String makeKindName(final String s) {
        final int lastIndex = s.lastIndexOf(45);
        if (lastIndex == -1) {
            return s;
        }
        return s.substring(lastIndex + 1);
    }
    
    String makeModifiersString(final int mod) {
        if (!this.includeModifiers) {
            return "";
        }
        final String string = Modifier.toString(mod);
        if (string.length() == 0) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        sb.append(string);
        sb.append(" ");
        return sb.toString();
    }
    
    public String makePrimaryTypeName(final Class clazz, final String s) {
        return this.makeTypeName(clazz, s, this.shortPrimaryTypeNames);
    }
    
    public String makeTypeName(final Class clazz) {
        return this.makeTypeName(clazz, clazz.getName(), this.shortTypeNames);
    }
    
    String makeTypeName(final Class clazz, final String s, final boolean b) {
        if (clazz == null) {
            return "ANONYMOUS";
        }
        if (clazz.isArray()) {
            final Class componentType = clazz.getComponentType();
            final StringBuffer sb = new StringBuffer();
            sb.append(this.makeTypeName(componentType, componentType.getName(), b));
            sb.append("[]");
            return sb.toString();
        }
        if (b) {
            return this.stripPackageName(s).replace('$', '.');
        }
        return s.replace('$', '.');
    }
    
    String stripPackageName(final String s) {
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex == -1) {
            return s;
        }
        return s.substring(lastIndex + 1);
    }
}
