// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.MethodSignature;

class MethodSignatureImpl extends CodeSignatureImpl implements MethodSignature
{
    Class returnType;
    
    MethodSignatureImpl(final int n, final String s, final Class clazz, final Class[] array, final String[] array2, final Class[] array3, final Class returnType) {
        super(n, s, clazz, array, array2, array3);
        this.returnType = returnType;
    }
    
    @Override
    protected String createToString(final StringMaker stringMaker) {
        final StringBuffer sb = new StringBuffer();
        sb.append(stringMaker.makeModifiersString(this.getModifiers()));
        if (stringMaker.includeArgs) {
            sb.append(stringMaker.makeTypeName(this.getReturnType()));
        }
        if (stringMaker.includeArgs) {
            sb.append(" ");
        }
        sb.append(stringMaker.makePrimaryTypeName(this.getDeclaringType(), this.getDeclaringTypeName()));
        sb.append(".");
        sb.append(this.getName());
        stringMaker.addSignature(sb, this.getParameterTypes());
        stringMaker.addThrows(sb, this.getExceptionTypes());
        return sb.toString();
    }
    
    public Class getReturnType() {
        if (this.returnType == null) {
            this.returnType = this.extractType(6);
        }
        return this.returnType;
    }
}
