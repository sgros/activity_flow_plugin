// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.lang.Signature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

class JoinPointImpl implements ProceedingJoinPoint
{
    Object _this;
    Object[] args;
    StaticPart staticPart;
    Object target;
    
    public JoinPointImpl(final StaticPart staticPart, final Object this2, final Object target, final Object[] args) {
        this.staticPart = staticPart;
        this._this = this2;
        this.target = target;
        this.args = args;
    }
    
    @Override
    public Object getTarget() {
        return this.target;
    }
    
    @Override
    public final String toString() {
        return this.staticPart.toString();
    }
    
    static class StaticPartImpl implements StaticPart
    {
        private int id;
        String kind;
        Signature signature;
        SourceLocation sourceLocation;
        
        public StaticPartImpl(final int id, final String kind, final Signature signature, final SourceLocation sourceLocation) {
            this.kind = kind;
            this.signature = signature;
            this.sourceLocation = sourceLocation;
            this.id = id;
        }
        
        public String getKind() {
            return this.kind;
        }
        
        public Signature getSignature() {
            return this.signature;
        }
        
        @Override
        public final String toString() {
            return this.toString(StringMaker.middleStringMaker);
        }
        
        String toString(final StringMaker stringMaker) {
            final StringBuffer sb = new StringBuffer();
            sb.append(stringMaker.makeKindName(this.getKind()));
            sb.append("(");
            sb.append(((SignatureImpl)this.getSignature()).toString(stringMaker));
            sb.append(")");
            return sb.toString();
        }
    }
}
