// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

class CollectionsKt___CollectionsKt extends CollectionsKt___CollectionsJvmKt
{
    public static final <T> Sequence<T> asSequence(final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        return (Sequence<T>)new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1((Iterable)iterable);
    }
    
    public static final <T, A extends Appendable> A joinTo(final Iterable<? extends T> iterable, final A a, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, final int n, final CharSequence charSequence4, final Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        Intrinsics.checkParameterIsNotNull(a, "buffer");
        Intrinsics.checkParameterIsNotNull(charSequence, "separator");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        Intrinsics.checkParameterIsNotNull(charSequence3, "postfix");
        Intrinsics.checkParameterIsNotNull(charSequence4, "truncated");
        a.append(charSequence2);
        final Iterator<? extends T> iterator = iterable.iterator();
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n2;
            if (!iterator.hasNext()) {
                break;
            }
            final T next = (T)iterator.next();
            if (++n2 > 1) {
                a.append(charSequence);
            }
            if (n >= 0 && (n3 = n2) > n) {
                break;
            }
            StringsKt__StringBuilderKt.appendElement(a, next, function1);
        }
        if (n >= 0 && n3 > n) {
            a.append(charSequence4);
        }
        a.append(charSequence3);
        return a;
    }
    
    public static final <T> String joinToString(final Iterable<? extends T> iterable, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, final int n, final CharSequence charSequence4, final Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charSequence, "separator");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        Intrinsics.checkParameterIsNotNull(charSequence3, "postfix");
        Intrinsics.checkParameterIsNotNull(charSequence4, "truncated");
        final String string = joinTo((Iterable<?>)iterable, (StringBuilder)new StringBuilder(), charSequence, charSequence2, charSequence3, n, charSequence4, (Function1<? super Object, ? extends CharSequence>)function1).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "joinTo(StringBuilder(), \u2026ed, transform).toString()");
        return string;
    }
    
    public static final <T> T last(final List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        if (!list.isEmpty()) {
            return (T)list.get(CollectionsKt__CollectionsKt.getLastIndex((List<?>)list));
        }
        throw new NoSuchElementException("List is empty.");
    }
    
    public static final <T> List<T> plus(final Collection<? extends T> c, final T e) {
        Intrinsics.checkParameterIsNotNull(c, "receiver$0");
        final ArrayList list = new ArrayList(c.size() + 1);
        list.addAll(c);
        list.add(e);
        return (ArrayList<T>)list;
    }
    
    public static final <T> T single(final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (iterable instanceof List) {
            return (T)single((List<?>)iterable);
        }
        final Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        final T next = iterator.next();
        if (!iterator.hasNext()) {
            return next;
        }
        throw new IllegalArgumentException("Collection has more than one element.");
    }
    
    public static final <T> T single(final List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        switch (list.size()) {
            default: {
                throw new IllegalArgumentException("List has more than one element.");
            }
            case 1: {
                return (T)list.get(0);
            }
            case 0: {
                throw new NoSuchElementException("List is empty.");
            }
        }
    }
    
    public static final <T, C extends Collection<? super T>> C toCollection(final Iterable<? extends T> iterable, final C c) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        final Iterator<? extends T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            c.add((Object)iterator.next());
        }
        return c;
    }
    
    public static final <T> List<T> toList(final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (iterable instanceof Collection) {
            final Collection<? extends T> collection = (Collection<? extends T>)iterable;
            List<T> list = null;
            switch (collection.size()) {
                default: {
                    list = toMutableList((Collection<? extends T>)collection);
                    break;
                }
                case 1: {
                    Object o;
                    if (iterable instanceof List) {
                        o = ((List<Object>)iterable).get(0);
                    }
                    else {
                        o = iterable.iterator().next();
                    }
                    list = CollectionsKt__CollectionsJVMKt.listOf(o);
                    break;
                }
                case 0: {
                    list = CollectionsKt__CollectionsKt.emptyList();
                    break;
                }
            }
            return list;
        }
        return CollectionsKt__CollectionsKt.optimizeReadOnlyList(toMutableList((Iterable<? extends T>)iterable));
    }
    
    public static final <T> List<T> toMutableList(final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (iterable instanceof Collection) {
            return toMutableList((Collection<? extends T>)iterable);
        }
        return toCollection((Iterable<?>)iterable, (ArrayList<T>)new ArrayList<T>());
    }
    
    public static final <T> List<T> toMutableList(final Collection<? extends T> c) {
        Intrinsics.checkParameterIsNotNull(c, "receiver$0");
        return new ArrayList<T>(c);
    }
}
