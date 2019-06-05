package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KCallable;
import kotlin.reflect.KDeclarationContainer;

public abstract class CallableReference implements Serializable, KCallable {
    public static final Object NO_RECEIVER = NoReceiver.INSTANCE;
    protected final Object receiver;
    private transient KCallable reflected;

    private static class NoReceiver implements Serializable {
        private static final NoReceiver INSTANCE = new NoReceiver();

        private NoReceiver() {
        }
    }

    public abstract KCallable computeReflected();

    public CallableReference() {
        this(NO_RECEIVER);
    }

    protected CallableReference(Object obj) {
        this.receiver = obj;
    }

    public Object getBoundReceiver() {
        return this.receiver;
    }

    public KCallable compute() {
        KCallable kCallable = this.reflected;
        if (kCallable != null) {
            return kCallable;
        }
        kCallable = computeReflected();
        this.reflected = kCallable;
        return kCallable;
    }

    /* Access modifiers changed, original: protected */
    public KCallable getReflected() {
        CallableReference compute = compute();
        if (compute != this) {
            return compute;
        }
        throw new KotlinReflectionNotSupportedError();
    }

    public KDeclarationContainer getOwner() {
        throw new AbstractMethodError();
    }

    public String getName() {
        throw new AbstractMethodError();
    }

    public String getSignature() {
        throw new AbstractMethodError();
    }

    public Object call(Object... objArr) {
        return getReflected().call(objArr);
    }
}
