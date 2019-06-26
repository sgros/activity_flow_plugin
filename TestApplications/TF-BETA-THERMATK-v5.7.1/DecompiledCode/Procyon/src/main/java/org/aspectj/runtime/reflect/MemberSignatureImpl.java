// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.MemberSignature;

abstract class MemberSignatureImpl extends SignatureImpl implements MemberSignature
{
    MemberSignatureImpl(final int n, final String s, final Class clazz) {
        super(n, s, clazz);
    }
}
