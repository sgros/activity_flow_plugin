package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode.CondAddOp;

/* compiled from: LockFreeLinkedList.kt */
public final class JobSupport$addLastAtomic$$inlined$addLastIf$1 extends CondAddOp {
    final /* synthetic */ Object $expect$inlined;
    final /* synthetic */ LockFreeLinkedListNode $node;
    final /* synthetic */ JobSupport this$0;

    public JobSupport$addLastAtomic$$inlined$addLastIf$1(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2, JobSupport jobSupport, Object obj) {
        this.$node = lockFreeLinkedListNode;
        this.this$0 = jobSupport;
        this.$expect$inlined = obj;
        super(lockFreeLinkedListNode2);
    }

    public Object prepare(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
        return (this.this$0.getState$kotlinx_coroutines_core() == this.$expect$inlined ? 1 : null) != null ? null : LockFreeLinkedListKt.getCONDITION_FALSE();
    }
}
