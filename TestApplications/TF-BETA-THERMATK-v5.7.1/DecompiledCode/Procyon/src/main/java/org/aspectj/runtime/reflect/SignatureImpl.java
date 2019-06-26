// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import java.lang.ref.SoftReference;
import java.util.StringTokenizer;
import org.aspectj.lang.Signature;

abstract class SignatureImpl implements Signature
{
    static Class[] EMPTY_CLASS_ARRAY;
    static String[] EMPTY_STRING_ARRAY;
    private static boolean useCache = true;
    Class declaringType;
    String declaringTypeName;
    ClassLoader lookupClassLoader;
    int modifiers;
    String name;
    Cache stringCache;
    private String stringRep;
    
    static {
        SignatureImpl.EMPTY_STRING_ARRAY = new String[0];
        SignatureImpl.EMPTY_CLASS_ARRAY = new Class[0];
    }
    
    SignatureImpl(final int modifiers, final String name, final Class declaringType) {
        this.modifiers = -1;
        this.lookupClassLoader = null;
        this.modifiers = modifiers;
        this.name = name;
        this.declaringType = declaringType;
    }
    
    private ClassLoader getLookupClassLoader() {
        if (this.lookupClassLoader == null) {
            this.lookupClassLoader = this.getClass().getClassLoader();
        }
        return this.lookupClassLoader;
    }
    
    protected abstract String createToString(final StringMaker p0);
    
    int extractInt(final int n) {
        return Integer.parseInt(this.extractString(n), 16);
    }
    
    String extractString(int index) {
        final int index2 = this.stringRep.indexOf(45);
        int n = 0;
        int i = index;
        index = index2;
        while (i > 0) {
            n = index + 1;
            index = this.stringRep.indexOf(45, n);
            --i;
        }
        int length;
        if ((length = index) == -1) {
            length = this.stringRep.length();
        }
        return this.stringRep.substring(n, length);
    }
    
    Class extractType(final int n) {
        return Factory.makeClass(this.extractString(n), this.getLookupClassLoader());
    }
    
    Class[] extractTypes(int i) {
        final StringTokenizer stringTokenizer = new StringTokenizer(this.extractString(i), ":");
        final int countTokens = stringTokenizer.countTokens();
        final Class[] array = new Class[countTokens];
        for (i = 0; i < countTokens; ++i) {
            array[i] = Factory.makeClass(stringTokenizer.nextToken(), this.getLookupClassLoader());
        }
        return array;
    }
    
    public Class getDeclaringType() {
        if (this.declaringType == null) {
            this.declaringType = this.extractType(2);
        }
        return this.declaringType;
    }
    
    public String getDeclaringTypeName() {
        if (this.declaringTypeName == null) {
            this.declaringTypeName = this.getDeclaringType().getName();
        }
        return this.declaringTypeName;
    }
    
    public int getModifiers() {
        if (this.modifiers == -1) {
            this.modifiers = this.extractInt(0);
        }
        return this.modifiers;
    }
    
    public String getName() {
        if (this.name == null) {
            this.name = this.extractString(1);
        }
        return this.name;
    }
    
    @Override
    public final String toString() {
        return this.toString(StringMaker.middleStringMaker);
    }
    
    String toString(final StringMaker stringMaker) {
        String value = null;
        Label_0055: {
            if (SignatureImpl.useCache) {
                final Cache stringCache = this.stringCache;
                if (stringCache != null) {
                    value = stringCache.get(stringMaker.cacheOffset);
                    break Label_0055;
                }
                try {
                    this.stringCache = (Cache)new CacheImpl();
                }
                catch (Throwable t) {
                    SignatureImpl.useCache = false;
                }
            }
            value = null;
        }
        String toString = value;
        if (value == null) {
            toString = this.createToString(stringMaker);
        }
        if (SignatureImpl.useCache) {
            this.stringCache.set(stringMaker.cacheOffset, toString);
        }
        return toString;
    }
    
    private interface Cache
    {
        String get(final int p0);
        
        void set(final int p0, final String p1);
    }
    
    private static final class CacheImpl implements Cache
    {
        private SoftReference toStringCacheRef;
        
        public CacheImpl() {
            this.makeCache();
        }
        
        private String[] array() {
            return this.toStringCacheRef.get();
        }
        
        private String[] makeCache() {
            final String[] referent = new String[3];
            this.toStringCacheRef = new SoftReference(referent);
            return referent;
        }
        
        @Override
        public String get(final int n) {
            final String[] array = this.array();
            if (array == null) {
                return null;
            }
            return array[n];
        }
        
        @Override
        public void set(final int n, final String s) {
            String[] array;
            if ((array = this.array()) == null) {
                array = this.makeCache();
            }
            array[n] = s;
        }
    }
}
