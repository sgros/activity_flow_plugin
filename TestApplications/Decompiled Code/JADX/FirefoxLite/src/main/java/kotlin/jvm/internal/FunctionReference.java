package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

public class FunctionReference extends CallableReference implements FunctionBase, KFunction {
    private final int arity;

    public FunctionReference(int i, Object obj) {
        super(obj);
        this.arity = i;
    }

    public int getArity() {
        return this.arity;
    }

    /* Access modifiers changed, original: protected */
    public KFunction getReflected() {
        return (KFunction) super.getReflected();
    }

    /* Access modifiers changed, original: protected */
    public KCallable computeReflected() {
        return Reflection.function(this);
    }

    /* JADX WARNING: Missing block: B:16:0x004e, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(getBoundReceiver(), r5.getBoundReceiver()) != false) goto L_0x0052;
     */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        if (r5 != r4) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r5 instanceof kotlin.jvm.internal.FunctionReference;
        r2 = 0;
        if (r1 == 0) goto L_0x0053;
    L_0x0009:
        r5 = (kotlin.jvm.internal.FunctionReference) r5;
        r1 = r4.getOwner();
        if (r1 != 0) goto L_0x0018;
    L_0x0011:
        r1 = r5.getOwner();
        if (r1 != 0) goto L_0x0051;
    L_0x0017:
        goto L_0x0026;
    L_0x0018:
        r1 = r4.getOwner();
        r3 = r5.getOwner();
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0051;
    L_0x0026:
        r1 = r4.getName();
        r3 = r5.getName();
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0051;
    L_0x0034:
        r1 = r4.getSignature();
        r3 = r5.getSignature();
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0051;
    L_0x0042:
        r1 = r4.getBoundReceiver();
        r5 = r5.getBoundReceiver();
        r5 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r5);
        if (r5 == 0) goto L_0x0051;
    L_0x0050:
        goto L_0x0052;
    L_0x0051:
        r0 = 0;
    L_0x0052:
        return r0;
    L_0x0053:
        r0 = r5 instanceof kotlin.reflect.KFunction;
        if (r0 == 0) goto L_0x0060;
    L_0x0057:
        r0 = r4.compute();
        r5 = r5.equals(r0);
        return r5;
    L_0x0060:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.jvm.internal.FunctionReference.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return (((getOwner() == null ? 0 : getOwner().hashCode() * 31) + getName().hashCode()) * 31) + getSignature().hashCode();
    }

    public String toString() {
        FunctionReference compute = compute();
        if (compute != this) {
            return compute.toString();
        }
        String str;
        if ("<init>".equals(getName())) {
            str = "constructor (Kotlin reflection is not available)";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("function ");
            stringBuilder.append(getName());
            stringBuilder.append(" (Kotlin reflection is not available)");
            str = stringBuilder.toString();
        }
        return str;
    }
}
