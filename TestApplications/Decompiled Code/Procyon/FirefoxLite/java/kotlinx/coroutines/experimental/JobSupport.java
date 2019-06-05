// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlinx.coroutines.experimental.internal.OpDescriptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.coroutines.experimental.Continuation;
import java.util.concurrent.CancellationException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.TypeCastException;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class JobSupport implements Job
{
    private static final AtomicReferenceFieldUpdater _state$FU;
    private volatile Object _state;
    private volatile DisposableHandle parentHandle;
    
    static {
        _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    }
    
    public JobSupport(final boolean b) {
        Empty state;
        if (b) {
            state = JobSupportKt.access$getEmptyActive$p();
        }
        else {
            state = JobSupportKt.access$getEmptyNew$p();
        }
        this._state = state;
    }
    
    private final boolean addLastAtomic(Object prev, final NodeList list, final JobNode<?> jobNode) {
        final JobNode<?> jobNode2 = jobNode;
        final LockFreeLinkedListNode.CondAddOp condAddOp = (LockFreeLinkedListNode.CondAddOp)new JobSupport$addLastAtomic$$inlined$addLastIf.JobSupport$addLastAtomic$$inlined$addLastIf$1((LockFreeLinkedListNode)jobNode2, (LockFreeLinkedListNode)jobNode2, this, prev);
        boolean b = false;
    Label_0076:
        while (true) {
            prev = list.getPrev();
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
            switch (((LockFreeLinkedListNode)prev).tryCondAddNext(jobNode2, list, condAddOp)) {
                default: {
                    continue;
                }
                case 2: {
                    b = false;
                    break Label_0076;
                }
                case 1: {
                    b = true;
                    break Label_0076;
                }
            }
        }
        return b;
    }
    
    private final void cancelChildrenInternal(ChildJob nextChild, final Throwable t) {
        do {
            nextChild.childJob.cancel(new JobCancellationException("Child job was cancelled because of parent failure", t, nextChild.childJob));
            nextChild = this.nextChild(nextChild);
        } while (nextChild != null);
    }
    
    private final Object coerceProposedUpdate(final Incomplete incomplete, final Object o) {
        Object cancelled = o;
        if (incomplete instanceof Finishing) {
            final Finishing finishing = (Finishing)incomplete;
            cancelled = o;
            if (finishing.cancelled != null) {
                cancelled = o;
                if (!this.isCorrespondinglyCancelled(finishing.cancelled, o)) {
                    cancelled = this.createCancelled(finishing.cancelled, o);
                }
            }
        }
        return cancelled;
    }
    
    private final void completeUpdateState(final Incomplete obj, final Object o, final int n) {
        final boolean b = o instanceof CompletedExceptionally;
        final Throwable t = null;
        Object o2;
        if (!b) {
            o2 = null;
        }
        else {
            o2 = o;
        }
        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o2;
        if (!this.isCancelling(obj)) {
            this.onCancellationInternal$kotlinx_coroutines_core(completedExceptionally);
        }
        this.onCompletionInternal$kotlinx_coroutines_core(o, n);
        Throwable cause = t;
        if (completedExceptionally != null) {
            cause = completedExceptionally.cause;
        }
        if (obj instanceof JobNode) {
            try {
                ((JobNode)obj).invoke(cause);
            }
            catch (Throwable t2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception in completion handler ");
                sb.append(obj);
                sb.append(" for ");
                sb.append(this);
                this.handleException$kotlinx_coroutines_core(new CompletionHandlerException(sb.toString(), t2));
            }
        }
        else {
            final NodeList list = obj.getList();
            if (list != null) {
                this.notifyCompletion(list, cause);
            }
        }
    }
    
    private final Cancelled createCancelled(final Cancelled cancelled, final Object o) {
        if (!(o instanceof CompletedExceptionally)) {
            return cancelled;
        }
        final Throwable cause = ((CompletedExceptionally)o).cause;
        if (Intrinsics.areEqual(cancelled.cause, cause)) {
            return cancelled;
        }
        if (!(cancelled.cause instanceof JobCancellationException)) {
            ExceptionsKt__ExceptionsKt.addSuppressed(cause, cancelled.cause);
        }
        return new Cancelled(this, cause);
    }
    
    private final ChildJob firstChild(final Incomplete incomplete) {
        final boolean b = incomplete instanceof ChildJob;
        final ChildJob childJob = null;
        Incomplete incomplete2;
        if (!b) {
            incomplete2 = null;
        }
        else {
            incomplete2 = incomplete;
        }
        final ChildJob childJob2 = (ChildJob)incomplete2;
        ChildJob nextChild;
        if (childJob2 != null) {
            nextChild = childJob2;
        }
        else {
            final NodeList list = incomplete.getList();
            nextChild = childJob;
            if (list != null) {
                nextChild = this.nextChild(list);
            }
        }
        return nextChild;
    }
    
    private final Throwable getExceptionOrNull(Object o) {
        final boolean b = o instanceof CompletedExceptionally;
        final Throwable t = null;
        if (!b) {
            o = null;
        }
        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
        Throwable cause = t;
        if (completedExceptionally != null) {
            cause = completedExceptionally.cause;
        }
        return cause;
    }
    
    private final boolean isCancelling(final Incomplete incomplete) {
        return incomplete instanceof Finishing && ((Finishing)incomplete).cancelled != null;
    }
    
    private final boolean isCorrespondinglyCancelled(final Cancelled cancelled, final Object o) {
        final boolean b = o instanceof Cancelled;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final Cancelled cancelled2 = (Cancelled)o;
        if (Intrinsics.areEqual(cancelled2.cause, cancelled.cause) || cancelled2.cause instanceof JobCancellationException) {
            b2 = true;
        }
        return b2;
    }
    
    private final boolean makeCancelling(final Throwable t) {
        while (true) {
            final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                final Empty empty = (Empty)state$kotlinx_coroutines_core;
                if (empty.isActive()) {
                    this.promoteEmptyToNodeList(empty);
                }
                else {
                    if (this.updateStateCancelled((Incomplete)state$kotlinx_coroutines_core, t)) {
                        return true;
                    }
                    continue;
                }
            }
            else if (state$kotlinx_coroutines_core instanceof JobNode) {
                this.promoteSingleToNodeList((JobNode<?>)state$kotlinx_coroutines_core);
            }
            else if (state$kotlinx_coroutines_core instanceof NodeList) {
                final NodeList list = (NodeList)state$kotlinx_coroutines_core;
                if (list.isActive()) {
                    if (this.tryMakeCancelling((Incomplete)state$kotlinx_coroutines_core, list.getList(), t)) {
                        return true;
                    }
                    continue;
                }
                else {
                    if (this.updateStateCancelled((Incomplete)state$kotlinx_coroutines_core, t)) {
                        return true;
                    }
                    continue;
                }
            }
            else {
                if (!(state$kotlinx_coroutines_core instanceof Finishing)) {
                    return false;
                }
                final Finishing finishing = (Finishing)state$kotlinx_coroutines_core;
                if (finishing.cancelled != null) {
                    return false;
                }
                if (this.tryMakeCancelling((Incomplete)state$kotlinx_coroutines_core, finishing.getList(), t)) {
                    return true;
                }
                continue;
            }
        }
    }
    
    private final int makeCompletingInternal(Object o, final int n) {
        while (true) {
            final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return 0;
            }
            final boolean b = state$kotlinx_coroutines_core instanceof Finishing;
            if (b && ((Finishing)state$kotlinx_coroutines_core).completing) {
                return 0;
            }
            final Finishing finishing = (Finishing)state$kotlinx_coroutines_core;
            ChildJob firstChild = this.firstChild(finishing);
            final Cancelled cancelled = null;
            if (firstChild == null) {
                if (!b && this.hasOnFinishingHandler$kotlinx_coroutines_core(o)) {
                    firstChild = null;
                }
                else {
                    if (this.updateState(finishing, o, n)) {
                        return 1;
                    }
                    continue;
                }
            }
            final NodeList list = finishing.getList();
            if (list != null) {
                if (o instanceof CompletedExceptionally && firstChild != null) {
                    this.cancelChildrenInternal(firstChild, ((CompletedExceptionally)o).cause);
                }
                Finishing finishing2;
                if (!b) {
                    finishing2 = null;
                }
                else {
                    finishing2 = (Finishing)state$kotlinx_coroutines_core;
                }
                final Finishing finishing3 = finishing2;
                Cancelled cancelled2 = null;
                Label_0185: {
                    if (finishing3 != null) {
                        cancelled2 = finishing3.cancelled;
                        if (cancelled2 != null) {
                            break Label_0185;
                        }
                    }
                    Object o2;
                    if (!(o instanceof Cancelled)) {
                        o2 = cancelled;
                    }
                    else {
                        o2 = o;
                    }
                    cancelled2 = (Cancelled)o2;
                }
                final Finishing finishing4 = new Finishing(list, cancelled2, true);
                if (!JobSupport._state$FU.compareAndSet(this, state$kotlinx_coroutines_core, finishing4)) {
                    continue;
                }
                if (!b) {
                    this.onFinishingInternal$kotlinx_coroutines_core(o);
                }
                if (firstChild != null && this.tryWaitForChild(firstChild, o)) {
                    return 2;
                }
                if (this.updateState(finishing4, o, 0)) {
                    return 1;
                }
                continue;
            }
            else if (state$kotlinx_coroutines_core instanceof Empty) {
                this.promoteEmptyToNodeList((Empty)state$kotlinx_coroutines_core);
            }
            else {
                if (!(state$kotlinx_coroutines_core instanceof JobNode)) {
                    o = new StringBuilder();
                    ((StringBuilder)o).append("Unexpected state with an empty list: ");
                    ((StringBuilder)o).append(state$kotlinx_coroutines_core);
                    throw new IllegalStateException(((StringBuilder)o).toString().toString());
                }
                this.promoteSingleToNodeList((JobNode<?>)state$kotlinx_coroutines_core);
            }
        }
    }
    
    private final boolean makeCompletingOnCancel(final Throwable t) {
        return this.makeCompleting$kotlinx_coroutines_core(new Cancelled(this, t));
    }
    
    private final JobNode<?> makeNode(final Function1<? super Throwable, Unit> function1, final boolean b) {
        final boolean b2 = false;
        boolean b3 = false;
        final JobNode<?> jobNode = null;
        JobCancellationNode<?> jobCancellationNode = null;
        JobNode<?> jobNode2;
        if (b) {
            if (function1 instanceof JobCancellationNode) {
                jobCancellationNode = (JobCancellationNode<?>)function1;
            }
            final JobCancellationNode<?> jobCancellationNode2 = jobCancellationNode;
            if (jobCancellationNode2 != null) {
                if (jobCancellationNode2.job == this) {
                    b3 = true;
                }
                if (!b3) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                if (jobCancellationNode2 != null) {
                    jobNode2 = jobCancellationNode2;
                    return jobNode2;
                }
            }
            jobNode2 = new InvokeOnCancellation(this, function1);
        }
        else {
            JobNode<?> jobNode3;
            if (!(function1 instanceof JobNode)) {
                jobNode3 = jobNode;
            }
            else {
                jobNode3 = (JobNode<?>)function1;
            }
            final JobNode<?> jobNode4 = jobNode3;
            if (jobNode4 != null) {
                int n = b2 ? 1 : 0;
                if (jobNode4.job == this) {
                    n = (b2 ? 1 : 0);
                    if (!(jobNode4 instanceof JobCancellationNode)) {
                        n = 1;
                    }
                }
                if (n == 0) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                if (jobNode4 != null) {
                    jobNode2 = jobNode4;
                    return jobNode2;
                }
            }
            jobNode2 = new InvokeOnCompletion(this, function1);
        }
        return jobNode2;
    }
    
    private final ChildJob nextChild(LockFreeLinkedListNode lockFreeLinkedListNode) {
        LockFreeLinkedListNode lockFreeLinkedListNode2;
        while (true) {
            lockFreeLinkedListNode2 = lockFreeLinkedListNode;
            if (!lockFreeLinkedListNode.isRemoved()) {
                break;
            }
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode2.getNextNode();
            if (lockFreeLinkedListNode.isRemoved()) {
                lockFreeLinkedListNode2 = lockFreeLinkedListNode;
            }
            else {
                if (lockFreeLinkedListNode instanceof ChildJob) {
                    return (ChildJob)lockFreeLinkedListNode;
                }
                lockFreeLinkedListNode2 = lockFreeLinkedListNode;
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
                continue;
            }
        }
    }
    
    private final void notifyCancellation(final NodeList list, final Throwable t) {
        Throwable t2 = null;
        final Object next = list.getNext();
        if (next != null) {
            Throwable t3;
            for (LockFreeLinkedListNode nextNode = (LockFreeLinkedListNode)next; Intrinsics.areEqual(nextNode, list) ^ true; nextNode = nextNode.getNextNode(), t2 = t3) {
                t3 = t2;
                if (nextNode instanceof JobCancellationNode) {
                    final JobNode obj = (JobNode)nextNode;
                    try {
                        obj.invoke(t);
                        t3 = t2;
                    }
                    catch (Throwable t4) {
                        if (t2 != null) {
                            ExceptionsKt__ExceptionsKt.addSuppressed(t2, t4);
                            if (t2 != null) {
                                t3 = t2;
                                continue;
                            }
                        }
                        final JobSupport obj2 = this;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Exception in completion handler ");
                        sb.append(obj);
                        sb.append(" for ");
                        sb.append(obj2);
                        t3 = new CompletionHandlerException(sb.toString(), t4);
                        final Unit instance = Unit.INSTANCE;
                    }
                }
            }
            if (t2 != null) {
                this.handleException$kotlinx_coroutines_core(t2);
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }
    
    private final void notifyCompletion(final NodeList list, final Throwable t) {
        Object o = null;
        final Object next = list.getNext();
        if (next != null) {
            Object obj;
            for (LockFreeLinkedListNode nextNode = (LockFreeLinkedListNode)next; Intrinsics.areEqual(nextNode, list) ^ true; nextNode = nextNode.getNextNode(), o = obj) {
                obj = o;
                if (nextNode instanceof JobNode) {
                    obj = nextNode;
                    try {
                        ((CompletionHandlerBase)obj).invoke(t);
                        obj = o;
                    }
                    catch (Throwable t2) {
                        if (o != null) {
                            ExceptionsKt__ExceptionsKt.addSuppressed((Throwable)o, t2);
                            if (o != null) {
                                obj = o;
                                continue;
                            }
                        }
                        final JobSupport obj2 = this;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Exception in completion handler ");
                        sb.append(obj);
                        sb.append(" for ");
                        sb.append(obj2);
                        obj = new CompletionHandlerException(sb.toString(), t2);
                        final Unit instance = Unit.INSTANCE;
                    }
                }
            }
            if (o != null) {
                this.handleException$kotlinx_coroutines_core((Throwable)o);
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }
    
    private final void promoteEmptyToNodeList(final Empty empty) {
        JobSupport._state$FU.compareAndSet(this, empty, new NodeList(empty.isActive()));
    }
    
    private final void promoteSingleToNodeList(final JobNode<?> jobNode) {
        jobNode.addOneIfEmpty(new NodeList(true));
        JobSupport._state$FU.compareAndSet(this, jobNode, jobNode.getNextNode());
    }
    
    private final int startInternal(final Object o) {
        if (o instanceof Empty) {
            if (((Empty)o).isActive()) {
                return 0;
            }
            if (!JobSupport._state$FU.compareAndSet(this, o, JobSupportKt.access$getEmptyActive$p())) {
                return -1;
            }
            this.onStartInternal$kotlinx_coroutines_core();
            return 1;
        }
        else {
            if (o instanceof NodeList) {
                final int tryMakeActive = ((NodeList)o).tryMakeActive();
                if (tryMakeActive == 1) {
                    this.onStartInternal$kotlinx_coroutines_core();
                }
                return tryMakeActive;
            }
            return 0;
        }
    }
    
    private final String stateString() {
        final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
        String string;
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            final StringBuilder sb = new StringBuilder();
            final Finishing finishing = (Finishing)state$kotlinx_coroutines_core;
            if (finishing.cancelled != null) {
                sb.append("Cancelling");
            }
            if (finishing.completing) {
                sb.append("Completing");
            }
            string = sb.toString();
            Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder().apply(builderAction).toString()");
        }
        else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            if (((Finishing)state$kotlinx_coroutines_core).isActive()) {
                string = "Active";
            }
            else {
                string = "New";
            }
        }
        else if (state$kotlinx_coroutines_core instanceof Cancelled) {
            string = "Cancelled";
        }
        else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            string = "CompletedExceptionally";
        }
        else {
            string = "Completed";
        }
        return string;
    }
    
    private final CancellationException toCancellationException(final Throwable t, final String s) {
        Throwable t2;
        if (!(t instanceof CancellationException)) {
            t2 = null;
        }
        else {
            t2 = t;
        }
        final CancellationException ex = (CancellationException)t2;
        CancellationException ex2;
        if (ex != null) {
            ex2 = ex;
        }
        else {
            ex2 = new JobCancellationException(s, t, this);
        }
        return ex2;
    }
    
    private final boolean tryMakeCancelling(final Incomplete incomplete, final NodeList list, final Throwable t) {
        final Cancelled cancelled = new Cancelled(this, t);
        if (!JobSupport._state$FU.compareAndSet(this, incomplete, new Finishing(list, cancelled, false))) {
            return false;
        }
        this.onFinishingInternal$kotlinx_coroutines_core(cancelled);
        this.onCancellationInternal$kotlinx_coroutines_core(cancelled);
        this.notifyCancellation(list, t);
        return true;
    }
    
    private final boolean tryUpdateState(final Incomplete incomplete, final Object o) {
        if (o instanceof Incomplete) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!JobSupport._state$FU.compareAndSet(this, incomplete, o)) {
            return false;
        }
        final DisposableHandle parentHandle = this.parentHandle;
        if (parentHandle != null) {
            parentHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        return true;
    }
    
    private final boolean tryWaitForChild(ChildJob nextChild, final Object o) {
        while (DefaultImpls.invokeOnCompletion$default(nextChild.childJob, false, false, (Function1)new ChildCompletion(this, nextChild, o), 1, (Object)null) == NonDisposableHandle.INSTANCE) {
            nextChild = this.nextChild(nextChild);
            if (nextChild != null) {
                continue;
            }
            return false;
        }
        return true;
    }
    
    private final boolean updateState(final Incomplete incomplete, Object coerceProposedUpdate, final int n) {
        coerceProposedUpdate = this.coerceProposedUpdate(incomplete, coerceProposedUpdate);
        if (!this.tryUpdateState(incomplete, coerceProposedUpdate)) {
            return false;
        }
        this.completeUpdateState(incomplete, coerceProposedUpdate, n);
        return true;
    }
    
    private final boolean updateStateCancelled(final Incomplete incomplete, final Throwable t) {
        return this.updateState(incomplete, new Cancelled(this, t), 0);
    }
    
    @Override
    public final DisposableHandle attachChild(final Job job) {
        Intrinsics.checkParameterIsNotNull(job, "child");
        return DefaultImpls.invokeOnCompletion$default((Job)this, true, false, (Function1)new ChildJob(this, job), 2, (Object)null);
    }
    
    public final Object awaitInternal$kotlinx_coroutines_core(final Continuation<Object> continuation) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                    return state$kotlinx_coroutines_core;
                }
                throw ((CompletedExceptionally)state$kotlinx_coroutines_core).cause;
            }
        } while (this.startInternal(state$kotlinx_coroutines_core) < 0);
        return this.awaitSuspend(continuation);
    }
    
    final /* synthetic */ Object awaitSuspend(final Continuation<Object> continuation) {
        final CancellableContinuationImpl<Object> cancellableContinuationImpl = new CancellableContinuationImpl<Object>(CoroutineIntrinsics.normalizeContinuation(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        final CancellableContinuationImpl<Object> cancellableContinuationImpl2 = cancellableContinuationImpl;
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuationImpl2, this.invokeOnCompletion((Function1<? super Throwable, Unit>)new JobSupport$awaitSuspend$$inlined$suspendCancellableCoroutine$lambda.JobSupport$awaitSuspend$$inlined$suspendCancellableCoroutine$lambda$1((CancellableContinuation)cancellableContinuationImpl2, this)));
        return cancellableContinuationImpl.getResult();
    }
    
    @Override
    public boolean cancel(final Throwable t) {
        boolean b = false;
        switch (this.getOnCancelMode$kotlinx_coroutines_core()) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid onCancelMode ");
                sb.append(this.getOnCancelMode$kotlinx_coroutines_core());
                throw new IllegalStateException(sb.toString().toString());
            }
            case 1: {
                b = this.makeCompletingOnCancel(t);
                break;
            }
            case 0: {
                b = this.makeCancelling(t);
                break;
            }
        }
        return b;
    }
    
    public final void continueCompleting$kotlinx_coroutines_core(final ChildJob childJob, final Object obj) {
        Intrinsics.checkParameterIsNotNull(childJob, "lastChild");
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Finishing)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Job ");
                sb.append(this);
                sb.append(" is found in expected state while completing with ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString(), this.getExceptionOrNull(obj));
            }
            final ChildJob nextChild = this.nextChild(childJob);
            if (nextChild != null && this.tryWaitForChild(nextChild, obj)) {
                return;
            }
        } while (!this.updateState((Incomplete)state$kotlinx_coroutines_core, obj, 0));
    }
    
    @Override
    public <R> R fold(final R r, final Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return DefaultImpls.fold(this, r, function2);
    }
    
    @Override
    public <E extends Element> E get(final CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get(this, key);
    }
    
    @Override
    public final CancellationException getCancellationException() {
        final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            final Finishing finishing = (Finishing)state$kotlinx_coroutines_core;
            if (finishing.cancelled != null) {
                return this.toCancellationException(finishing.cancelled.cause, "Job is being cancelled");
            }
        }
        if (state$kotlinx_coroutines_core instanceof Incomplete) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Job was not completed or cancelled yet: ");
            sb.append(this);
            throw new IllegalStateException(sb.toString().toString());
        }
        CancellationException ex;
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            ex = this.toCancellationException(((CompletedExceptionally)state$kotlinx_coroutines_core).cause, "Job has failed");
        }
        else {
            ex = new JobCancellationException("Job has completed normally", null, this);
        }
        return ex;
    }
    
    @Override
    public final CoroutineContext.Key<?> getKey() {
        return Job.Key;
    }
    
    public int getOnCancelMode$kotlinx_coroutines_core() {
        return 0;
    }
    
    public final Object getState$kotlinx_coroutines_core() {
        Object state;
        while (true) {
            state = this._state;
            if (!(state instanceof OpDescriptor)) {
                break;
            }
            ((OpDescriptor)state).perform(this);
        }
        return state;
    }
    
    public void handleException$kotlinx_coroutines_core(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
        throw t;
    }
    
    public boolean hasOnFinishingHandler$kotlinx_coroutines_core(final Object o) {
        return false;
    }
    
    public final void initParentJobInternal$kotlinx_coroutines_core(final Job job) {
        if (this.parentHandle != null) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
            return;
        }
        job.start();
        final DisposableHandle attachChild = job.attachChild(this);
        this.parentHandle = attachChild;
        if (this.isCompleted()) {
            attachChild.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }
    
    public final DisposableHandle invokeOnCompletion(final Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return this.invokeOnCompletion(false, true, function1);
    }
    
    @Override
    public DisposableHandle invokeOnCompletion(final boolean b, final boolean b2, final Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        final Throwable t = null;
        JobNode<?> jobNode = null;
        while (true) {
            final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                final Empty empty = (Empty)state$kotlinx_coroutines_core;
                if (empty.isActive()) {
                    JobNode<?> node;
                    if (jobNode != null) {
                        node = jobNode;
                    }
                    else {
                        node = this.makeNode(function1, b);
                    }
                    jobNode = node;
                    if (JobSupport._state$FU.compareAndSet(this, state$kotlinx_coroutines_core, node)) {
                        return node;
                    }
                    continue;
                }
                else {
                    this.promoteEmptyToNodeList(empty);
                }
            }
            else {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                    if (b2) {
                        Object o = state$kotlinx_coroutines_core;
                        if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                            o = null;
                        }
                        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
                        Throwable cause = t;
                        if (completedExceptionally != null) {
                            cause = completedExceptionally.cause;
                        }
                        function1.invoke(cause);
                    }
                    return NonDisposableHandle.INSTANCE;
                }
                final NodeList list = ((Empty)state$kotlinx_coroutines_core).getList();
                if (list == null) {
                    if (state$kotlinx_coroutines_core == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobNode<*>");
                    }
                    this.promoteSingleToNodeList((JobNode<?>)state$kotlinx_coroutines_core);
                }
                else {
                    if (state$kotlinx_coroutines_core instanceof Finishing) {
                        final Finishing finishing = (Finishing)state$kotlinx_coroutines_core;
                        if (finishing.cancelled != null && b) {
                            if (b2) {
                                function1.invoke(finishing.cancelled.cause);
                            }
                            return NonDisposableHandle.INSTANCE;
                        }
                    }
                    JobNode<?> node2;
                    if (jobNode != null) {
                        node2 = jobNode;
                    }
                    else {
                        node2 = this.makeNode(function1, b);
                    }
                    jobNode = node2;
                    if (this.addLastAtomic(state$kotlinx_coroutines_core, list, node2)) {
                        return node2;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public final boolean isActive() {
        final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
        return state$kotlinx_coroutines_core instanceof Incomplete && ((Incomplete)state$kotlinx_coroutines_core).isActive();
    }
    
    public final boolean isCompleted() {
        return !(this.getState$kotlinx_coroutines_core() instanceof Incomplete);
    }
    
    public final boolean makeCompleting$kotlinx_coroutines_core(final Object o) {
        boolean b = false;
        if (this.makeCompletingInternal(o, 0) != 0) {
            b = true;
        }
        return b;
    }
    
    public final boolean makeCompletingOnce$kotlinx_coroutines_core(final Object obj, final int n) {
        boolean b = false;
        switch (this.makeCompletingInternal(obj, n)) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Job ");
                sb.append(this);
                sb.append(" is already complete or completing, ");
                sb.append("but is being completed with ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString(), this.getExceptionOrNull(obj));
            }
            case 2: {
                b = false;
                break;
            }
            case 1: {
                b = true;
                break;
            }
        }
        return b;
    }
    
    @Override
    public CoroutineContext minusKey(final CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey(this, key);
    }
    
    public String nameString$kotlinx_coroutines_core() {
        return DebugKt.getClassSimpleName(this);
    }
    
    public void onCancellationInternal$kotlinx_coroutines_core(final CompletedExceptionally completedExceptionally) {
    }
    
    public void onCompletionInternal$kotlinx_coroutines_core(final Object o, final int n) {
    }
    
    public void onFinishingInternal$kotlinx_coroutines_core(final Object o) {
    }
    
    public void onStartInternal$kotlinx_coroutines_core() {
    }
    
    @Override
    public CoroutineContext plus(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus(this, coroutineContext);
    }
    
    public final void removeNode$kotlinx_coroutines_core(final JobNode<?> jobNode) {
        Intrinsics.checkParameterIsNotNull(jobNode, "node");
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof JobNode) {
                if (state$kotlinx_coroutines_core != jobNode) {
                    return;
                }
                continue;
            }
            else if (state$kotlinx_coroutines_core instanceof Incomplete) {
                if (((JobNode<?>)state$kotlinx_coroutines_core).getList() != null) {
                    jobNode.remove();
                }
            }
        } while (!JobSupport._state$FU.compareAndSet(this, state$kotlinx_coroutines_core, JobSupportKt.access$getEmptyActive$p()));
    }
    
    @Override
    public final boolean start() {
        while (true) {
            switch (this.startInternal(this.getState$kotlinx_coroutines_core())) {
                default: {
                    continue;
                }
                case 1: {
                    return true;
                }
                case 0: {
                    return false;
                }
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.nameString$kotlinx_coroutines_core());
        sb.append('{');
        sb.append(this.stateString());
        sb.append("}@");
        sb.append(DebugKt.getHexAddress(this));
        return sb.toString();
    }
    
    private static final class Finishing implements Incomplete
    {
        public final Cancelled cancelled;
        public final boolean completing;
        private final NodeList list;
        
        public Finishing(final NodeList list, final Cancelled cancelled, final boolean completing) {
            Intrinsics.checkParameterIsNotNull(list, "list");
            this.list = list;
            this.cancelled = cancelled;
            this.completing = completing;
        }
        
        @Override
        public NodeList getList() {
            return this.list;
        }
        
        @Override
        public boolean isActive() {
            return this.cancelled == null;
        }
    }
}
