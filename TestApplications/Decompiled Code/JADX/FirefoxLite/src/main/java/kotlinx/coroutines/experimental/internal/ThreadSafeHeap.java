package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: ThreadSafeHeap.kt */
public final class ThreadSafeHeap<T extends ThreadSafeHeapNode & Comparable<? super T>> {
    /* renamed from: a */
    private T[] f39a;
    public volatile int size;

    public final boolean isEmpty() {
        return this.size == 0;
    }

    public final T peek() {
        ThreadSafeHeapNode firstImpl;
        synchronized (this) {
            firstImpl = firstImpl();
        }
        return firstImpl;
    }

    public final boolean remove(T t) {
        boolean z;
        Intrinsics.checkParameterIsNotNull(t, "node");
        synchronized (this) {
            if (t.getIndex() < 0) {
                z = false;
            } else {
                removeAtImpl(t.getIndex());
                z = true;
            }
        }
        return z;
    }

    public final T firstImpl() {
        ThreadSafeHeapNode[] threadSafeHeapNodeArr = this.f39a;
        return threadSafeHeapNodeArr != null ? threadSafeHeapNodeArr[0] : null;
    }

    public final T removeAtImpl(int i) {
        if ((this.size > 0 ? 1 : null) != null) {
            ThreadSafeHeapNode[] threadSafeHeapNodeArr = this.f39a;
            if (threadSafeHeapNodeArr == null) {
                Intrinsics.throwNpe();
            }
            this.size--;
            if (i < this.size) {
                swap(i, this.size);
                int i2 = (i - 1) / 2;
                if (i > 0) {
                    ThreadSafeHeapNode threadSafeHeapNode = threadSafeHeapNodeArr[i];
                    if (threadSafeHeapNode == null) {
                        Intrinsics.throwNpe();
                    }
                    Comparable comparable = (Comparable) threadSafeHeapNode;
                    Object obj = threadSafeHeapNodeArr[i2];
                    if (obj == null) {
                        Intrinsics.throwNpe();
                    }
                    if (comparable.compareTo(obj) < 0) {
                        swap(i, i2);
                        siftUpFrom(i2);
                    }
                }
                siftDownFrom(i);
            }
            ThreadSafeHeapNode threadSafeHeapNode2 = threadSafeHeapNodeArr[this.size];
            if (threadSafeHeapNode2 == null) {
                Intrinsics.throwNpe();
            }
            threadSafeHeapNode2.setIndex(-1);
            threadSafeHeapNodeArr[this.size] = (ThreadSafeHeapNode) null;
            return threadSafeHeapNode2;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    private final void siftUpFrom(int i) {
        while (i > 0) {
            ThreadSafeHeapNode[] threadSafeHeapNodeArr = this.f39a;
            if (threadSafeHeapNodeArr == null) {
                Intrinsics.throwNpe();
            }
            int i2 = (i - 1) / 2;
            ThreadSafeHeapNode threadSafeHeapNode = threadSafeHeapNodeArr[i2];
            if (threadSafeHeapNode == null) {
                Intrinsics.throwNpe();
            }
            Comparable comparable = (Comparable) threadSafeHeapNode;
            Object obj = threadSafeHeapNodeArr[i];
            if (obj == null) {
                Intrinsics.throwNpe();
            }
            if (comparable.compareTo(obj) > 0) {
                swap(i, i2);
                i = i2;
            } else {
                return;
            }
        }
    }

    private final void siftDownFrom(int i) {
        while (true) {
            int i2 = (i * 2) + 1;
            if (i2 < this.size) {
                ThreadSafeHeapNode[] threadSafeHeapNodeArr = this.f39a;
                if (threadSafeHeapNodeArr == null) {
                    Intrinsics.throwNpe();
                }
                int i3 = i2 + 1;
                if (i3 < this.size) {
                    ThreadSafeHeapNode threadSafeHeapNode = threadSafeHeapNodeArr[i3];
                    if (threadSafeHeapNode == null) {
                        Intrinsics.throwNpe();
                    }
                    Comparable comparable = (Comparable) threadSafeHeapNode;
                    Object obj = threadSafeHeapNodeArr[i2];
                    if (obj == null) {
                        Intrinsics.throwNpe();
                    }
                    if (comparable.compareTo(obj) < 0) {
                        i2 = i3;
                    }
                }
                ThreadSafeHeapNode threadSafeHeapNode2 = threadSafeHeapNodeArr[i];
                if (threadSafeHeapNode2 == null) {
                    Intrinsics.throwNpe();
                }
                Comparable comparable2 = (Comparable) threadSafeHeapNode2;
                Object obj2 = threadSafeHeapNodeArr[i2];
                if (obj2 == null) {
                    Intrinsics.throwNpe();
                }
                if (comparable2.compareTo(obj2) > 0) {
                    swap(i, i2);
                    i = i2;
                } else {
                    return;
                }
            }
            return;
        }
    }

    private final void swap(int i, int i2) {
        ThreadSafeHeapNode[] threadSafeHeapNodeArr = this.f39a;
        if (threadSafeHeapNodeArr == null) {
            Intrinsics.throwNpe();
        }
        ThreadSafeHeapNode threadSafeHeapNode = threadSafeHeapNodeArr[i2];
        if (threadSafeHeapNode == null) {
            Intrinsics.throwNpe();
        }
        ThreadSafeHeapNode threadSafeHeapNode2 = threadSafeHeapNodeArr[i];
        if (threadSafeHeapNode2 == null) {
            Intrinsics.throwNpe();
        }
        threadSafeHeapNodeArr[i] = threadSafeHeapNode;
        threadSafeHeapNodeArr[i2] = threadSafeHeapNode2;
        threadSafeHeapNode.setIndex(i);
        threadSafeHeapNode2.setIndex(i2);
    }
}
