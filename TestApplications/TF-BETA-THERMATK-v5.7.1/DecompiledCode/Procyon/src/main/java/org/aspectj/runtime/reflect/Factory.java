// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.lang.Signature;
import java.util.StringTokenizer;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.JoinPoint;
import java.util.Hashtable;

public final class Factory
{
    private static Object[] NO_ARGS;
    static /* synthetic */ Class class$java$lang$ClassNotFoundException;
    static Hashtable prims;
    int count;
    String filename;
    Class lexicalClass;
    ClassLoader lookupClassLoader;
    
    static {
        (Factory.prims = new Hashtable()).put("void", Void.TYPE);
        Factory.prims.put("boolean", Boolean.TYPE);
        Factory.prims.put("byte", Byte.TYPE);
        Factory.prims.put("char", Character.TYPE);
        Factory.prims.put("short", Short.TYPE);
        Factory.prims.put("int", Integer.TYPE);
        Factory.prims.put("long", Long.TYPE);
        Factory.prims.put("float", Float.TYPE);
        Factory.prims.put("double", Double.TYPE);
        Factory.NO_ARGS = new Object[0];
    }
    
    public Factory(final String filename, final Class lexicalClass) {
        this.filename = filename;
        this.lexicalClass = lexicalClass;
        this.count = 0;
        this.lookupClassLoader = lexicalClass.getClassLoader();
    }
    
    static /* synthetic */ Class class$(final String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    static Class makeClass(final String name, final ClassLoader loader) {
        if (name.equals("*")) {
            return null;
        }
        final Class clazz = Factory.prims.get(name);
        if (clazz != null) {
            return clazz;
        }
        Label_0037: {
            if (loader != null) {
                break Label_0037;
            }
            try {
                return Class.forName(name);
                return Class.forName(name, false, loader);
            }
            catch (ClassNotFoundException ex) {
                Class class$java$lang$ClassNotFoundException;
                if ((class$java$lang$ClassNotFoundException = Factory.class$java$lang$ClassNotFoundException) == null) {
                    class$java$lang$ClassNotFoundException = (Factory.class$java$lang$ClassNotFoundException = class$("java.lang.ClassNotFoundException"));
                }
                return class$java$lang$ClassNotFoundException;
            }
        }
    }
    
    public static JoinPoint makeJP(final JoinPoint.StaticPart staticPart, final Object o, final Object o2) {
        return new JoinPointImpl(staticPart, o, o2, Factory.NO_ARGS);
    }
    
    public static JoinPoint makeJP(final JoinPoint.StaticPart staticPart, final Object o, final Object o2, final Object o3) {
        return new JoinPointImpl(staticPart, o, o2, new Object[] { o3 });
    }
    
    public MethodSignature makeMethodSig(final String s, final String s2, final String s3, final String str, final String str2, final String str3, final String s4) {
        final int int1 = Integer.parseInt(s, 16);
        final Class class1 = makeClass(s3, this.lookupClassLoader);
        final StringTokenizer stringTokenizer = new StringTokenizer(str, ":");
        final int countTokens = stringTokenizer.countTokens();
        final Class[] array = new Class[countTokens];
        final int n = 0;
        for (int i = 0; i < countTokens; ++i) {
            array[i] = makeClass(stringTokenizer.nextToken(), this.lookupClassLoader);
        }
        final StringTokenizer stringTokenizer2 = new StringTokenizer(str2, ":");
        final int countTokens2 = stringTokenizer2.countTokens();
        final String[] array2 = new String[countTokens2];
        for (int j = 0; j < countTokens2; ++j) {
            array2[j] = stringTokenizer2.nextToken();
        }
        final StringTokenizer stringTokenizer3 = new StringTokenizer(str3, ":");
        final int countTokens3 = stringTokenizer3.countTokens();
        final Class[] array3 = new Class[countTokens3];
        for (int k = n; k < countTokens3; ++k) {
            array3[k] = makeClass(stringTokenizer3.nextToken(), this.lookupClassLoader);
        }
        return new MethodSignatureImpl(int1, s2, class1, array, array2, array3, makeClass(s4, this.lookupClassLoader));
    }
    
    public JoinPoint.StaticPart makeSJP(final String s, final Signature signature, final int n) {
        return new JoinPointImpl.StaticPartImpl(this.count++, s, signature, this.makeSourceLoc(n, -1));
    }
    
    public SourceLocation makeSourceLoc(final int n, final int n2) {
        return new SourceLocationImpl(this.lexicalClass, this.filename, n);
    }
}
