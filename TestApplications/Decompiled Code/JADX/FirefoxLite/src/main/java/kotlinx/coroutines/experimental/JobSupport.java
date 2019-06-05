package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.coroutines.experimental.CoroutineContext.Key;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Job.DefaultImpls;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode.CondAddOp;
import kotlinx.coroutines.experimental.internal.OpDescriptor;

/* compiled from: JobSupport.kt */
public class JobSupport implements Job {
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    private volatile Object _state;
    private volatile DisposableHandle parentHandle;

    /* compiled from: JobSupport.kt */
    private static final class Finishing implements Incomplete {
        public final Cancelled cancelled;
        public final boolean completing;
        private final NodeList list;

        public Finishing(NodeList nodeList, Cancelled cancelled, boolean z) {
            Intrinsics.checkParameterIsNotNull(nodeList, "list");
            this.list = nodeList;
            this.cancelled = cancelled;
            this.completing = z;
        }

        public NodeList getList() {
            return this.list;
        }

        public boolean isActive() {
            return this.cancelled == null;
        }
    }

    public int getOnCancelMode$kotlinx_coroutines_core() {
        return 0;
    }

    public boolean hasOnFinishingHandler$kotlinx_coroutines_core(Object obj) {
        return false;
    }

    public void onCancellationInternal$kotlinx_coroutines_core(CompletedExceptionally completedExceptionally) {
    }

    public void onCompletionInternal$kotlinx_coroutines_core(Object obj, int i) {
    }

    public void onFinishingInternal$kotlinx_coroutines_core(Object obj) {
    }

    public void onStartInternal$kotlinx_coroutines_core() {
    }

    public JobSupport(boolean z) {
        this._state = z ? JobSupportKt.EmptyActive : JobSupportKt.EmptyNew;
    }

    public <R> R fold(R r, Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return DefaultImpls.fold(this, r, function2);
    }

    public <E extends Element> E get(Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get(this, key);
    }

    public CoroutineContext minusKey(Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey(this, key);
    }

    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus(this, coroutineContext);
    }

    public final Key<?> getKey() {
        return Job.Key;
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(Job job) {
        if ((this.parentHandle == null ? 1 : null) == null) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
        } else {
            job.start();
            DisposableHandle attachChild = job.attachChild(this);
            this.parentHandle = attachChild;
            if (isCompleted()) {
                attachChild.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    public final boolean isActive() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).isActive();
    }

    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof Incomplete);
    }

    private final boolean updateState(Incomplete incomplete, Object obj, int i) {
        obj = coerceProposedUpdate(incomplete, obj);
        if (!tryUpdateState(incomplete, obj)) {
            return false;
        }
        completeUpdateState(incomplete, obj, i);
        return true;
    }

    private final Object coerceProposedUpdate(Incomplete incomplete, Object obj) {
        if (!(incomplete instanceof Finishing)) {
            return obj;
        }
        Finishing finishing = (Finishing) incomplete;
        return (finishing.cancelled == null || isCorrespondinglyCancelled(finishing.cancelled, obj)) ? obj : createCancelled(finishing.cancelled, obj);
    }

    private final boolean isCorrespondinglyCancelled(Cancelled cancelled, Object obj) {
        boolean z = false;
        if (!(obj instanceof Cancelled)) {
            return false;
        }
        Cancelled cancelled2 = (Cancelled) obj;
        if (Intrinsics.areEqual(cancelled2.cause, cancelled.cause) || (cancelled2.cause instanceof JobCancellationException)) {
            z = true;
        }
        return z;
    }

    private final Cancelled createCancelled(Cancelled cancelled, Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            return cancelled;
        }
        Throwable th = ((CompletedExceptionally) obj).cause;
        if (Intrinsics.areEqual(cancelled.cause, th)) {
            return cancelled;
        }
        if (!(cancelled.cause instanceof JobCancellationException)) {
            ExceptionsKt__ExceptionsKt.addSuppressed(th, cancelled.cause);
        }
        return new Cancelled(this, th);
    }

    private final boolean tryUpdateState(Incomplete incomplete, Object obj) {
        if ((!(obj instanceof Incomplete) ? 1 : null) == null) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!_state$FU.compareAndSet(this, incomplete, obj)) {
            return false;
        } else {
            DisposableHandle disposableHandle = this.parentHandle;
            if (disposableHandle != null) {
                disposableHandle.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
            return true;
        }
    }

    private final void completeUpdateState(Incomplete incomplete, Object obj, int i) {
        Throwable th = null;
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
        if (!isCancelling(incomplete)) {
            onCancellationInternal$kotlinx_coroutines_core(completedExceptionally);
        }
        onCompletionInternal$kotlinx_coroutines_core(obj, i);
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        if (incomplete instanceof JobNode) {
            try {
                ((JobNode) incomplete).invoke(th);
                return;
            } catch (Throwable th2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception in completion handler ");
                stringBuilder.append(incomplete);
                stringBuilder.append(" for ");
                stringBuilder.append(this);
                handleException$kotlinx_coroutines_core(new CompletionHandlerException(stringBuilder.toString(), th2));
                return;
            }
        }
        NodeList list = incomplete.getList();
        if (list != null) {
            notifyCompletion(list, th);
        }
    }

    private final int startInternal(Object obj) {
        if (obj instanceof Empty) {
            if (((Empty) obj).isActive()) {
                return 0;
            }
            if (!_state$FU.compareAndSet(this, obj, JobSupportKt.EmptyActive)) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        } else if (!(obj instanceof NodeList)) {
            return 0;
        } else {
            int tryMakeActive = ((NodeList) obj).tryMakeActive();
            if (tryMakeActive == 1) {
                onStartInternal$kotlinx_coroutines_core();
            }
            return tryMakeActive;
        }
    }

    public final CancellationException getCancellationException() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Finishing finishing = (Finishing) state$kotlinx_coroutines_core;
            if (finishing.cancelled != null) {
                return toCancellationException(finishing.cancelled.cause, "Job is being cancelled");
            }
        }
        if (state$kotlinx_coroutines_core instanceof Incomplete) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Job was not completed or cancelled yet: ");
            stringBuilder.append(this);
            throw new IllegalStateException(stringBuilder.toString().toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            return toCancellationException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause, "Job has failed");
        } else {
            return new JobCancellationException("Job has completed normally", null, this);
        }
    }

    private final CancellationException toCancellationException(Throwable th, String str) {
        CancellationException cancellationException = (CancellationException) (!(th instanceof CancellationException) ? null : th);
        return cancellationException != null ? cancellationException : new JobCancellationException(str, th, this);
    }

    public final DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return invokeOnCompletion(false, true, function1);
    }

    public DisposableHandle invokeOnCompletion(boolean z, boolean z2, Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Object obj = null;
        JobNode jobNode = (JobNode) null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                Empty empty = (Empty) state$kotlinx_coroutines_core;
                if (empty.isActive()) {
                    if (jobNode == null) {
                        jobNode = makeNode(function1, z);
                    }
                    if (_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, jobNode)) {
                        return jobNode;
                    }
                } else {
                    promoteEmptyToNodeList(empty);
                }
            } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
                NodeList list = ((Incomplete) state$kotlinx_coroutines_core).getList();
                if (list != null) {
                    if (state$kotlinx_coroutines_core instanceof Finishing) {
                        Finishing finishing = (Finishing) state$kotlinx_coroutines_core;
                        if (finishing.cancelled != null && z) {
                            if (z2) {
                                function1.invoke(finishing.cancelled.cause);
                            }
                            return NonDisposableHandle.INSTANCE;
                        }
                    }
                    if (jobNode == null) {
                        jobNode = makeNode(function1, z);
                    }
                    if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                        return jobNode;
                    }
                } else if (state$kotlinx_coroutines_core != null) {
                    promoteSingleToNodeList((JobNode) state$kotlinx_coroutines_core);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobNode<*>");
                }
            } else {
                if (z2) {
                    if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                        state$kotlinx_coroutines_core = null;
                    }
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) state$kotlinx_coroutines_core;
                    if (completedExceptionally != null) {
                        obj = completedExceptionally.cause;
                    }
                    function1.invoke(obj);
                }
                return NonDisposableHandle.INSTANCE;
            }
        }
    }

    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> function1, boolean z) {
        Object obj = null;
        Function1<? super Throwable, Unit> function12 = null;
        if (z) {
            if (function1 instanceof JobCancellationNode) {
                function12 = function1;
            }
            JobCancellationNode jobCancellationNode = (JobCancellationNode) function12;
            if (jobCancellationNode != null) {
                if (jobCancellationNode.job == this) {
                    obj = 1;
                }
                if (obj == null) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                } else if (jobCancellationNode != null) {
                    return jobCancellationNode;
                }
            }
            return new InvokeOnCancellation(this, function1);
        }
        if (function1 instanceof JobNode) {
            function12 = function1;
        }
        JobNode<?> jobNode = (JobNode) function12;
        if (jobNode != null) {
            if (jobNode.job == this && !(jobNode instanceof JobCancellationNode)) {
                obj = 1;
            }
            if (obj == null) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            } else if (jobNode != null) {
                return jobNode;
            }
        }
        return new InvokeOnCompletion(this, function1);
    }

    private final void promoteEmptyToNodeList(Empty empty) {
        _state$FU.compareAndSet(this, empty, new NodeList(empty.isActive()));
    }

    private final void promoteSingleToNodeList(JobNode<?> jobNode) {
        jobNode.addOneIfEmpty(new NodeList(true));
        _state$FU.compareAndSet(this, jobNode, jobNode.getNextNode());
    }

    public boolean cancel(Throwable th) {
        switch (getOnCancelMode$kotlinx_coroutines_core()) {
            case 0:
                return makeCancelling(th);
            case 1:
                return makeCompletingOnCancel(th);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid onCancelMode ");
                stringBuilder.append(getOnCancelMode$kotlinx_coroutines_core());
                throw new IllegalStateException(stringBuilder.toString().toString());
        }
    }

    private final boolean updateStateCancelled(Incomplete incomplete, Throwable th) {
        return updateState(incomplete, new Cancelled(this, th), 0);
    }

    private final boolean tryMakeCancelling(Incomplete incomplete, NodeList nodeList, Throwable th) {
        Cancelled cancelled = new Cancelled(this, th);
        if (!_state$FU.compareAndSet(this, incomplete, new Finishing(nodeList, cancelled, false))) {
            return false;
        }
        onFinishingInternal$kotlinx_coroutines_core(cancelled);
        onCancellationInternal$kotlinx_coroutines_core(cancelled);
        notifyCancellation(nodeList, th);
        return true;
    }

    private final boolean makeCompletingOnCancel(Throwable th) {
        return makeCompleting$kotlinx_coroutines_core(new Cancelled(this, th));
    }

    public final boolean makeCompleting$kotlinx_coroutines_core(Object obj) {
        return makeCompletingInternal(obj, 0) != 0;
    }

    public final boolean makeCompletingOnce$kotlinx_coroutines_core(Object obj, int i) {
        switch (makeCompletingInternal(obj, i)) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Job ");
                stringBuilder.append(this);
                stringBuilder.append(" is already complete or completing, ");
                stringBuilder.append("but is being completed with ");
                stringBuilder.append(obj);
                throw new IllegalStateException(stringBuilder.toString(), getExceptionOrNull(obj));
        }
    }

    private final void cancelChildrenInternal(ChildJob childJob, Throwable th) {
        while (true) {
            childJob.childJob.cancel(new JobCancellationException("Child job was cancelled because of parent failure", th, childJob.childJob));
            childJob = nextChild(childJob);
            if (childJob == null) {
                return;
            }
        }
    }

    private final Throwable getExceptionOrNull(Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            obj = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
        return completedExceptionally != null ? completedExceptionally.cause : null;
    }

    private final ChildJob firstChild(Incomplete incomplete) {
        ChildJob childJob = !(incomplete instanceof ChildJob) ? null : incomplete;
        if (childJob != null) {
            return childJob;
        }
        NodeList list = incomplete.getList();
        return list != null ? nextChild(list) : null;
    }

    private final boolean tryWaitForChild(ChildJob childJob, Object obj) {
        while (DefaultImpls.invokeOnCompletion$default(childJob.childJob, false, false, new ChildCompletion(this, childJob, obj), 1, null) == NonDisposableHandle.INSTANCE) {
            childJob = nextChild(childJob);
            if (childJob == null) {
                return false;
            }
        }
        return true;
    }

    private final ChildJob nextChild(LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildJob) {
                    return (ChildJob) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    public final DisposableHandle attachChild(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "child");
        return DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildJob(this, job), 2, null);
    }

    public void handleException$kotlinx_coroutines_core(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        throw th;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nameString$kotlinx_coroutines_core());
        stringBuilder.append('{');
        stringBuilder.append(stateString());
        stringBuilder.append("}@");
        stringBuilder.append(DebugKt.getHexAddress(this));
        return stringBuilder.toString();
    }

    public String nameString$kotlinx_coroutines_core() {
        return DebugKt.getClassSimpleName(this);
    }

    private final String stateString() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            StringBuilder stringBuilder = new StringBuilder();
            Finishing finishing = (Finishing) state$kotlinx_coroutines_core;
            if (finishing.cancelled != null) {
                stringBuilder.append("Cancelling");
            }
            if (finishing.completing) {
                stringBuilder.append("Completing");
            }
            String stringBuilder2 = stringBuilder.toString();
            Intrinsics.checkExpressionValueIsNotNull(stringBuilder2, "StringBuilder().apply(builderAction).toString()");
            return stringBuilder2;
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            return ((Incomplete) state$kotlinx_coroutines_core).isActive() ? "Active" : "New";
        } else {
            if (state$kotlinx_coroutines_core instanceof Cancelled) {
                return "Cancelled";
            }
            return state$kotlinx_coroutines_core instanceof CompletedExceptionally ? "CompletedExceptionally" : "Completed";
        }
    }

    private final boolean isCancelling(Incomplete incomplete) {
        return (incomplete instanceof Finishing) && ((Finishing) incomplete).cancelled != null;
    }

    public final Object awaitInternal$kotlinx_coroutines_core(Continuation<Object> continuation) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                    return state$kotlinx_coroutines_core;
                }
                throw ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return awaitSuspend(continuation);
    }

    public final Object getState$kotlinx_coroutines_core() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    private final void notifyCompletion(NodeList nodeList, Throwable th) {
        Throwable th2 = (Throwable) null;
        Object next = nodeList.getNext();
        if (next != null) {
            for (next = (LockFreeLinkedListNode) next; (Intrinsics.areEqual(next, nodeList) ^ 1) != 0; next = next.getNextNode()) {
                if (next instanceof JobNode) {
                    JobNode jobNode = (JobNode) next;
                    try {
                        jobNode.invoke(th);
                    } catch (Throwable th3) {
                        if (th2 != null) {
                            ExceptionsKt__ExceptionsKt.addSuppressed(th2, th3);
                            if (th2 != null) {
                            }
                        }
                        JobSupport jobSupport = this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Exception in completion handler ");
                        stringBuilder.append(jobNode);
                        stringBuilder.append(" for ");
                        stringBuilder.append(jobSupport);
                        Throwable completionHandlerException = new CompletionHandlerException(stringBuilder.toString(), th3);
                        Unit unit = Unit.INSTANCE;
                        th2 = completionHandlerException;
                    }
                }
            }
            if (th2 != null) {
                handleException$kotlinx_coroutines_core(th2);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }

    private final void notifyCancellation(NodeList nodeList, Throwable th) {
        Throwable th2 = (Throwable) null;
        Object next = nodeList.getNext();
        if (next != null) {
            for (next = (LockFreeLinkedListNode) next; (Intrinsics.areEqual(next, nodeList) ^ 1) != 0; next = next.getNextNode()) {
                if (next instanceof JobCancellationNode) {
                    JobNode jobNode = (JobNode) next;
                    try {
                        jobNode.invoke(th);
                    } catch (Throwable th3) {
                        if (th2 != null) {
                            ExceptionsKt__ExceptionsKt.addSuppressed(th2, th3);
                            if (th2 != null) {
                            }
                        }
                        JobSupport jobSupport = this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Exception in completion handler ");
                        stringBuilder.append(jobNode);
                        stringBuilder.append(" for ");
                        stringBuilder.append(jobSupport);
                        Throwable completionHandlerException = new CompletionHandlerException(stringBuilder.toString(), th3);
                        Unit unit = Unit.INSTANCE;
                        th2 = completionHandlerException;
                    }
                }
            }
            if (th2 != null) {
                handleException$kotlinx_coroutines_core(th2);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }

    public final boolean start() {
        while (true) {
            switch (startInternal(getState$kotlinx_coroutines_core())) {
                case 0:
                    return false;
                case 1:
                    return true;
                default:
            }
        }
    }

    private final boolean addLastAtomic(Object obj, NodeList nodeList, JobNode<?> jobNode) {
        LockFreeLinkedListNode lockFreeLinkedListNode = jobNode;
        CondAddOp jobSupport$addLastAtomic$$inlined$addLastIf$1 = new JobSupport$addLastAtomic$$inlined$addLastIf$1(lockFreeLinkedListNode, lockFreeLinkedListNode, this, obj);
        while (true) {
            obj = nodeList.getPrev();
            if (obj != null) {
                switch (((LockFreeLinkedListNode) obj).tryCondAddNext(lockFreeLinkedListNode, nodeList, jobSupport$addLastAtomic$$inlined$addLastIf$1)) {
                    case 1:
                        return true;
                    case 2:
                        return false;
                    default:
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    public final void removeNode$kotlinx_coroutines_core(JobNode<?> jobNode) {
        Intrinsics.checkParameterIsNotNull(jobNode, "node");
        JobNode<?> state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof JobNode)) {
                if ((state$kotlinx_coroutines_core instanceof Incomplete) && state$kotlinx_coroutines_core.getList() != null) {
                    jobNode.remove();
                }
                return;
            } else if (state$kotlinx_coroutines_core != jobNode) {
                return;
            }
        } while (!_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, JobSupportKt.EmptyActive));
    }

    private final boolean makeCancelling(Throwable th) {
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                Empty empty = (Empty) state$kotlinx_coroutines_core;
                if (empty.isActive()) {
                    promoteEmptyToNodeList(empty);
                } else if (updateStateCancelled((Incomplete) state$kotlinx_coroutines_core, th)) {
                    return true;
                }
            } else if (state$kotlinx_coroutines_core instanceof JobNode) {
                promoteSingleToNodeList((JobNode) state$kotlinx_coroutines_core);
            } else if (state$kotlinx_coroutines_core instanceof NodeList) {
                NodeList nodeList = (NodeList) state$kotlinx_coroutines_core;
                if (nodeList.isActive()) {
                    if (tryMakeCancelling((Incomplete) state$kotlinx_coroutines_core, nodeList.getList(), th)) {
                        return true;
                    }
                } else if (updateStateCancelled((Incomplete) state$kotlinx_coroutines_core, th)) {
                    return true;
                }
            } else if (!(state$kotlinx_coroutines_core instanceof Finishing)) {
                return false;
            } else {
                Finishing finishing = (Finishing) state$kotlinx_coroutines_core;
                if (finishing.cancelled != null) {
                    return false;
                }
                if (tryMakeCancelling((Incomplete) state$kotlinx_coroutines_core, finishing.getList(), th)) {
                    return true;
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:26:0x004c, code skipped:
            if (r7 != null) goto L_0x0058;
     */
    private final int makeCompletingInternal(java.lang.Object r9, int r10) {
        /*
        r8 = this;
    L_0x0000:
        r0 = r8.getState$kotlinx_coroutines_core();
        r1 = r0 instanceof kotlinx.coroutines.experimental.Incomplete;
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r1 = r0 instanceof kotlinx.coroutines.experimental.JobSupport.Finishing;
        if (r1 == 0) goto L_0x0016;
    L_0x000e:
        r3 = r0;
        r3 = (kotlinx.coroutines.experimental.JobSupport.Finishing) r3;
        r3 = r3.completing;
        if (r3 == 0) goto L_0x0016;
    L_0x0015:
        return r2;
    L_0x0016:
        r3 = r0;
        r3 = (kotlinx.coroutines.experimental.Incomplete) r3;
        r4 = r8.firstChild(r3);
        r5 = 1;
        r6 = 0;
        if (r4 == 0) goto L_0x0022;
    L_0x0021:
        goto L_0x002d;
    L_0x0022:
        if (r1 != 0) goto L_0x00b0;
    L_0x0024:
        r4 = r8.hasOnFinishingHandler$kotlinx_coroutines_core(r9);
        if (r4 == 0) goto L_0x00b0;
    L_0x002a:
        r4 = r6;
        r4 = (kotlinx.coroutines.experimental.ChildJob) r4;
    L_0x002d:
        r3 = r3.getList();
        if (r3 == 0) goto L_0x007d;
    L_0x0033:
        r7 = r9 instanceof kotlinx.coroutines.experimental.CompletedExceptionally;
        if (r7 == 0) goto L_0x0041;
    L_0x0037:
        if (r4 == 0) goto L_0x0041;
    L_0x0039:
        r7 = r9;
        r7 = (kotlinx.coroutines.experimental.CompletedExceptionally) r7;
        r7 = r7.cause;
        r8.cancelChildrenInternal(r4, r7);
    L_0x0041:
        if (r1 != 0) goto L_0x0045;
    L_0x0043:
        r7 = r6;
        goto L_0x0046;
    L_0x0045:
        r7 = r0;
    L_0x0046:
        r7 = (kotlinx.coroutines.experimental.JobSupport.Finishing) r7;
        if (r7 == 0) goto L_0x004f;
    L_0x004a:
        r7 = r7.cancelled;
        if (r7 == 0) goto L_0x004f;
    L_0x004e:
        goto L_0x0058;
    L_0x004f:
        r7 = r9 instanceof kotlinx.coroutines.experimental.Cancelled;
        if (r7 != 0) goto L_0x0054;
    L_0x0053:
        goto L_0x0055;
    L_0x0054:
        r6 = r9;
    L_0x0055:
        r7 = r6;
        r7 = (kotlinx.coroutines.experimental.Cancelled) r7;
    L_0x0058:
        r6 = new kotlinx.coroutines.experimental.JobSupport$Finishing;
        r6.<init>(r3, r7, r5);
        r3 = _state$FU;
        r0 = r3.compareAndSet(r8, r0, r6);
        if (r0 == 0) goto L_0x0000;
    L_0x0065:
        if (r1 != 0) goto L_0x006a;
    L_0x0067:
        r8.onFinishingInternal$kotlinx_coroutines_core(r9);
    L_0x006a:
        if (r4 == 0) goto L_0x0074;
    L_0x006c:
        r0 = r8.tryWaitForChild(r4, r9);
        if (r0 == 0) goto L_0x0074;
    L_0x0072:
        r9 = 2;
        return r9;
    L_0x0074:
        r6 = (kotlinx.coroutines.experimental.Incomplete) r6;
        r0 = r8.updateState(r6, r9, r2);
        if (r0 == 0) goto L_0x0000;
    L_0x007c:
        return r5;
    L_0x007d:
        r1 = r0 instanceof kotlinx.coroutines.experimental.Empty;
        if (r1 == 0) goto L_0x0088;
    L_0x0081:
        r0 = (kotlinx.coroutines.experimental.Empty) r0;
        r8.promoteEmptyToNodeList(r0);
        goto L_0x0000;
    L_0x0088:
        r1 = r0 instanceof kotlinx.coroutines.experimental.JobNode;
        if (r1 == 0) goto L_0x0093;
    L_0x008c:
        r0 = (kotlinx.coroutines.experimental.JobNode) r0;
        r8.promoteSingleToNodeList(r0);
        goto L_0x0000;
    L_0x0093:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Unexpected state with an empty list: ";
        r9.append(r10);
        r9.append(r0);
        r9 = r9.toString();
        r10 = new java.lang.IllegalStateException;
        r9 = r9.toString();
        r10.<init>(r9);
        r10 = (java.lang.Throwable) r10;
        throw r10;
    L_0x00b0:
        r0 = r8.updateState(r3, r9, r10);
        if (r0 == 0) goto L_0x0000;
    L_0x00b6:
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.JobSupport.makeCompletingInternal(java.lang.Object, int):int");
    }

    public final void continueCompleting$kotlinx_coroutines_core(ChildJob childJob, Object obj) {
        Intrinsics.checkParameterIsNotNull(childJob, "lastChild");
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Finishing) {
                ChildJob nextChild = nextChild(childJob);
                if (nextChild != null && tryWaitForChild(nextChild, obj)) {
                    return;
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Job ");
                stringBuilder.append(this);
                stringBuilder.append(" is found in expected state while completing with ");
                stringBuilder.append(obj);
                throw new IllegalStateException(stringBuilder.toString(), getExceptionOrNull(obj));
            }
        } while (!updateState((Incomplete) state$kotlinx_coroutines_core, obj, 0));
    }

    /* Access modifiers changed, original: final|synthetic */
    public final /* synthetic */ Object awaitSuspend(Continuation<Object> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuation, invokeOnCompletion(new C0766x9d7e2941(cancellableContinuation, this)));
        return cancellableContinuationImpl.getResult();
    }
}
