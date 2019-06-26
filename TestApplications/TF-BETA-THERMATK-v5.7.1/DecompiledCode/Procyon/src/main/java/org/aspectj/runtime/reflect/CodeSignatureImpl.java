// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.CodeSignature;

abstract class CodeSignatureImpl extends MemberSignatureImpl implements CodeSignature
{
    Class[] exceptionTypes;
    String[] parameterNames;
    Class[] parameterTypes;
    
    CodeSignatureImpl(final int n, final String s, final Class clazz, final Class[] parameterTypes, final String[] parameterNames, final Class[] exceptionTypes) {
        super(n, s, clazz);
        this.parameterTypes = parameterTypes;
        this.parameterNames = parameterNames;
        this.exceptionTypes = exceptionTypes;
    }
    
    public Class[] getExceptionTypes() {
        if (this.exceptionTypes == null) {
            this.exceptionTypes = this.extractTypes(5);
        }
        return this.exceptionTypes;
    }
    
    public Class[] getParameterTypes() {
        if (this.parameterTypes == null) {
            this.parameterTypes = this.extractTypes(3);
        }
        return this.parameterTypes;
    }
}
