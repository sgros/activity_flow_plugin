package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

/* compiled from: _Collections.kt */
class CollectionsKt___CollectionsKt extends CollectionsKt___CollectionsJvmKt {
    public static final <T> T last(List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        if (!list.isEmpty()) {
            return list.get(CollectionsKt__CollectionsKt.getLastIndex(list));
        }
        throw new NoSuchElementException("List is empty.");
    }

    public static final <T> T single(Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (iterable instanceof List) {
            return single((List) iterable);
        }
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            Object next = it.next();
            if (!it.hasNext()) {
                return next;
            }
            throw new IllegalArgumentException("Collection has more than one element.");
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public static final <T> T single(List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        switch (list.size()) {
            case 0:
                throw new NoSuchElementException("List is empty.");
            case 1:
                return list.get(0);
            default:
                throw new IllegalArgumentException("List has more than one element.");
        }
    }

    public static final <T, C extends Collection<? super T>> C toCollection(Iterable<? extends T> iterable, C c) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        for (Object add : iterable) {
            c.add(add);
        }
        return c;
    }

    public static final <T> List<T> toList(Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (!(iterable instanceof Collection)) {
            return CollectionsKt__CollectionsKt.optimizeReadOnlyList(toMutableList((Iterable) iterable));
        }
        List<T> emptyList;
        Collection collection = (Collection) iterable;
        switch (collection.size()) {
            case 0:
                emptyList = CollectionsKt__CollectionsKt.emptyList();
                break;
            case 1:
                emptyList = CollectionsKt__CollectionsJVMKt.listOf(iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next());
                break;
            default:
                emptyList = toMutableList(collection);
                break;
        }
        return emptyList;
    }

    public static final <T> List<T> toMutableList(Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (iterable instanceof Collection) {
            return toMutableList((Collection) iterable);
        }
        return (List) toCollection(iterable, new ArrayList());
    }

    public static final <T> List<T> toMutableList(Collection<? extends T> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "receiver$0");
        return new ArrayList(collection);
    }

    public static final <T> List<T> plus(Collection<? extends T> collection, T t) {
        Intrinsics.checkParameterIsNotNull(collection, "receiver$0");
        ArrayList arrayList = new ArrayList(collection.size() + 1);
        arrayList.addAll(collection);
        arrayList.add(t);
        return arrayList;
    }

    public static final <T, A extends Appendable> A joinTo(Iterable<? extends T> iterable, A a, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        Intrinsics.checkParameterIsNotNull(a, "buffer");
        Intrinsics.checkParameterIsNotNull(charSequence, "separator");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        Intrinsics.checkParameterIsNotNull(charSequence3, "postfix");
        Intrinsics.checkParameterIsNotNull(charSequence4, "truncated");
        a.append(charSequence2);
        int i2 = 0;
        for (Object next : iterable) {
            i2++;
            if (i2 > 1) {
                a.append(charSequence);
            }
            if (i >= 0 && i2 > i) {
                break;
            }
            StringsKt__StringBuilderKt.appendElement(a, next, function1);
        }
        if (i >= 0 && r5 > i) {
            a.append(charSequence4);
        }
        a.append(charSequence3);
        return a;
    }

    public static /* synthetic */ String joinToString$default(Iterable iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charSequence = ", ";
        }
        if ((i2 & 2) != 0) {
            charSequence2 = "";
        }
        CharSequence charSequence5 = charSequence2;
        if ((i2 & 4) != 0) {
            charSequence3 = "";
        }
        CharSequence charSequence6 = charSequence3;
        int i3 = (i2 & 8) != 0 ? -1 : i;
        if ((i2 & 16) != 0) {
            charSequence4 = "...";
        }
        CharSequence charSequence7 = charSequence4;
        if ((i2 & 32) != 0) {
            function1 = (Function1) null;
        }
        return joinToString(iterable, charSequence, charSequence5, charSequence6, i3, charSequence7, function1);
    }

    public static final <T> String joinToString(Iterable<? extends T> iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charSequence, "separator");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        Intrinsics.checkParameterIsNotNull(charSequence3, "postfix");
        Intrinsics.checkParameterIsNotNull(charSequence4, "truncated");
        String stringBuilder = ((StringBuilder) joinTo(iterable, new StringBuilder(), charSequence, charSequence2, charSequence3, i, charSequence4, function1)).toString();
        Intrinsics.checkExpressionValueIsNotNull(stringBuilder, "joinTo(StringBuilder(), …ed, transform).toString()");
        return stringBuilder;
    }

    public static final <T> Sequence<T> asSequence(Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        return new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(iterable);
    }
}
